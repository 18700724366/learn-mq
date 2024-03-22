package xian.woniu.j_dlx;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 21:31:54
 */
public class Declarer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare("exchange.dlx", "direct", false, true, null);
            channel.exchangeDeclare("exchange.normal", "fanout", false, true, null);
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-message-ttl", 6000);
            arguments.put("x-dead-letter-exchange", "exchange.dlx");
            arguments.put("x-dead-letter-routing-key", "rk");
            channel.queueDeclare("queue.normal", false, false, true, arguments);
            channel.queueBind("queue.normal", "exchange.normal", "");
            channel.queueDeclare("queue.dlx", false, false, true, null);
            channel.queueBind("queue.dlx", "exchange.dlx", "rk");

            System.out.println("over");
        }
    }
}
