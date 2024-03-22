package xian.woniu.i_ttl.a;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 20:51:35
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            String msg = "andy";
            channel.basicPublish("", "queue_demo", null, msg.getBytes());

            System.out.println("消息已发出");
        }
    }
}
