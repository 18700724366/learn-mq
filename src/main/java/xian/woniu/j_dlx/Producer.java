package xian.woniu.j_dlx;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 21:39:51
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.deliveryMode(2);    // 持久化消息
            builder.expiration("6000"); // 设置TTL：6000ms
            AMQP.BasicProperties properties = builder.build();

            String msg = "eason";
            // 因为exchange.normal的类型是fanout，所以路由键是什么都可以
            channel.basicPublish("exchange.normal", "", properties, msg.getBytes());

            System.out.println("消息已投递");
        }
    }
}
