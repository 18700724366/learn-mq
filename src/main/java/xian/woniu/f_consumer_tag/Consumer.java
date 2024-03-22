package xian.woniu.f_consumer_tag;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/02 13:40:34
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel();
             Channel channel2 = connection.createChannel()) {

            // 以下channel的consumerTag为：A
            String consumerTag = "A";
            xian.woniu.f_consumer_tag.MyConsumer consumer = new xian.woniu.f_consumer_tag.MyConsumer(channel, consumerTag);
            channel.basicConsume("test_queue", false, consumerTag, consumer);

            // 以下channel的consumerTag为：B
            String consumerTag2 = "B";
            xian.woniu.f_consumer_tag.MyConsumer consumer2 = new xian.woniu.f_consumer_tag.MyConsumer(channel2, consumerTag);
            channel2.basicConsume("test_queue", false, consumerTag2, consumer2);

            System.out.println("Enter...");
            System.in.read();

            System.out.println(consumerTag + "消费的消息数:" + consumer.getMessageCount());
            System.out.println(consumerTag2 + "消费的消息数:" + consumer2.getMessageCount());
        }
    }
}
