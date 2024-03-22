package xian.woniu.l_reject.d;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/07 09:41:59
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.basicConsume("queue.normal", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("recv: " + msg);
                    if ("G.E.M9".equals(msg)) {
                        channel.basicNack(envelope.getDeliveryTag(), true, false);
                    }
                }
            });

            System.out.println("Enter...");
            System.in.read();
        }

    }
}
