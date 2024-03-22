package xian.woniu.m_rpc;

import com.rabbitmq.client.*;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/01 16:10:32
 */
public class RpcServer {

    private static int fib(int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明队列
            channel.queueDeclare("rpc_queue", false, false, false, null);
            // 消费队列
            channel.basicConsume("rpc_queue", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("recv: " + msg);
                    int fib = fib(Integer.valueOf(msg));

                    // 获取回调队列的名字
                    String callbackQueueName = properties.getReplyTo();
                    // 将结果投递到回调队列中
                    channel.basicPublish("", callbackQueueName, properties, (fib + "").getBytes());

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });
            System.out.println("Enter...");
            System.in.read();
        }
    }
}
