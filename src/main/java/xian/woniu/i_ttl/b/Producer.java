package xian.woniu.i_ttl.b;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 21:02:59
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            // 声明一个普通队列
            channel.queueDeclare("queue_demo", false, false, false, null);

            // 投递消息时，设置消息的TTL
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.deliveryMode(2);    // 持久化消息
            builder.expiration("6000"); // 设置TTL：6000ms
            AMQP.BasicProperties properties = builder.build();
            String msg = "cindy";
            channel.basicPublish("", "queue_demo", properties, msg.getBytes());

            // 投递消息时，没有设置消息的TTL
            msg = "andy";
            channel.basicPublish("", "queue_demo", null, msg.getBytes());

            System.out.println("投递消息完毕");
        }
    }
}
