package xian.woniu.e_topic;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/01/10 17:28:14
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare("topic_logs", "topic");
            channel.queueDeclare("Q2", false, false, false, null);
            channel.queueBind("Q2", "topic_logs", "*.*.rabbit");
            channel.queueBind("Q2", "topic_logs", "lazy.#");

            channel.basicConsume("Q2", true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("B recv: " + new String(body));
                }
            });

            System.out.println("Enter...");
            System.in.read();
        }
    }
}
