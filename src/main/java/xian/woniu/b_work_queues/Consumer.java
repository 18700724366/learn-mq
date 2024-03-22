package xian.woniu.b_work_queues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2021/05/05 18:06:15
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.188.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("root");
        factory.setPassword("root123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("queue", false, false, false, null);

        channel.basicQos(1);
        channel.basicConsume("queue", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Consumer recv: " + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

        System.out.println("Enter...");
        System.in.read();

        channel.close();
        connection.close();
    }
}
