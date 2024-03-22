package xian.woniu.l_reject.c;

import com.rabbitmq.client.*;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/05 23:35:21
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.basicConsume("queue.normal", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("A recv: " + msg);
                    // 拒绝消息，同时禁止消息入队，此时消息会进入死信队列
                    channel.basicReject(envelope.getDeliveryTag(), false);
                }
            });

            System.out.println("Enter...");
            System.in.read();
        }
    }
}
