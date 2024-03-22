package xian.woniu.l_reject.b;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/04 14:13:27
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare("queue.normal", false, false, true, null);

            channel.basicConsume("queue.normal", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("B recv: " + msg);
                    // 确认消费消息
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });

            System.out.println("Enter...");
            System.in.read();
        }
    }
}