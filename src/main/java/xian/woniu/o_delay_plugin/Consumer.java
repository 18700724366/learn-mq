package xian.woniu.o_delay_plugin;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/04 22:52:22
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare("Q1", false, false, false, null);

            channel.basicConsume("Q1", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("recv: " + msg);
                }
            });

            System.out.println("Enter...");
            System.in.read();
        }
    }
}
