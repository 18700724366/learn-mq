package xian.woniu.h_alternate_exchange;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/02 23:33:17
 */
public class Declarer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {
            Map<String, Object> map = new HashMap<>();
            map.put("alternate-exchange", "myAe");

            channel.exchangeDeclare("normalExchange", "direct", true, false, map);
            channel.exchangeDeclare("myAe", "fanout", true, false, null);
            channel.queueDeclare("normalQueue", true, false, false, null);
            channel.queueBind("normalQueue", "normalExchange", "normalKey");
            channel.queueDeclare("unroutedQueue", true, false, false, null);
            channel.queueBind("unroutedQueue", "myAe", "");

            System.out.println("over");
        }
    }
}
