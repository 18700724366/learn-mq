package xian.woniu.m_rpc;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/01 16:19:37
 */
public class RpcClient {

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明队列
            channel.queueDeclare("rpc_queue", false, false, false, null);

            // indexes表示数字的斐波那契数列中的位置
            int[] indexes = {1, 2, 3, 4, 5, 6, 7, 8, 9};

            // 用于存放correlationId和请求数据映射关系的map
            Map<String, Integer> correlationMap = new HashMap<>();

            // 回调队列
            String callbackQueueName = channel.queueDeclare().getQueue();

            for (int index : indexes) {

                // RPCClient消费回调队列
                channel.basicConsume(callbackQueueName, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                        String result = new String(body);
                        String correlationId = properties.getCorrelationId();
                        Integer requestIndex = correlationMap.remove(correlationId);
                        /*
                            如果requestIndex为null，则直接跳过结果的输出即可，不需要做其他处理。
                            什么时候requestIndex会为null呢？
                            1. RpcClient向RpcServer发送了请求，附带了correlationId，投递到rpc_queue队列中
                            2. RpcServer消费到rpc_queue中的消息，计算出结果，并把结果投递到了回调队列中，附带了correlationId，以让RpcClient接收结果
                            3  RpcServer正打算向MQ服务发送“确认消费完消息”的请求时，RpcServer挂了。
                            4. RpcClient接收到结果，根据correlationId找到了对应的请求，然后打印结果，并从map中删除了correlationId
                            5. RpcServer重启好了，再次从rpc_queue中消费到了之前自己消费过的消息，又一次投递到了回调队列中，如此RpcServer就针对RpcClient
                               的一个请求发送了两次响应
                            6. RpcClient接收到结果，根据correlationId找不到对应的请求，此时的requestIndex就为null
                         */
                        if (requestIndex != null) {
                            System.out.println(requestIndex + ": " + result);
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    }
                });


                // 生成唯一的correlationId映射到一个请求
                String correlationId = UUID.randomUUID().toString();
                correlationMap.put(correlationId, index);

                // 消息的附带信息
                BasicProperties props = new BasicProperties()
                        .builder()
                        .correlationId(correlationId)
                        .replyTo(callbackQueueName)
                        .build();

                // 向rpc_queue队列投递消息
                channel.basicPublish("", "rpc_queue", props, (index + "").getBytes());
            }
            System.out.println("Enter...");
            System.in.read();
        }
    }
}
