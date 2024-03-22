package xian.woniu.j_dlx;

import com.rabbitmq.client.*;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 21:58:31
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.basicConsume("queue.dlx", new DefaultConsumer(channel) {
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
