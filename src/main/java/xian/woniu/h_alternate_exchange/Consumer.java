package xian.woniu.h_alternate_exchange;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/02 23:55:52
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {
            channel.basicConsume("unroutedQueue", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("recv message:" + msg);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });

            System.out.println("Enter...");
            System.in.read();
        }
    }
}
