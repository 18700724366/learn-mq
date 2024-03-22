package xian.woniu.f_consumer_tag;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/02 12:24:06
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare("test_queue", false, false, false, null);
            for (int i = 1; i <= 20; i++) {
                channel.basicPublish("", "test_queue", null, ("test" + i).getBytes());
            }
        }
    }
}
