package xian.woniu.l_reject.a;

import com.rabbitmq.client.*;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare("queue.normal", false, false, true, null);

            channel.basicConsume("queue.normal", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("A recv: " + msg);
                    // 拒绝消息，同时让被拒绝的消息入队
                    channel.basicReject(envelope.getDeliveryTag(), true);
                }
            });

            System.out.println("Enter...");
            System.in.read();
        }
    }
}
