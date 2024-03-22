package xian.woniu.k_priority_queue;

import com.rabbitmq.client.*;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 22:19:13
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.basicConsume("queue.priority", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("recv: " + msg);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });

            System.out.println("Enter...");
            System.in.read();

        }
    }
}
