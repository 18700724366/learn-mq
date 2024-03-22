package xian.woniu.i_ttl.a;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 20:44:22
 */
public class Declarer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            // 通过x-message-ttl参数来设置消息的TTL
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-message-ttl", 6000);
            channel.queueDeclare("queue_demo", false, false, false, arguments);

            System.out.println("over");
        }
    }
}
