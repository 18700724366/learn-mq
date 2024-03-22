package xian.woniu.c_publish_subscribe;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2021/06/30 18:05:16
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("logs", "fanout");

        // 声明一个临时队列，该临时队列的属性是：
        // 1. 非持久化
        // 2. 排他
        // 3. 自动删除
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, "logs", "");

        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("B revc: " + msg);
            }
        });

        System.out.println("Enter...");
        System.in.read();

        RabbitMqUtils.release(channel, connection);
    }
}
