package xian.woniu.l_reject.d;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/07 09:46:06
 */
public class Declarer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare("exchange.dlx", "direct", false, false, null);
            channel.exchangeDeclare("exchange.normal", "fanout", false, false, null);
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", "exchange.dlx");
            arguments.put("x-dead-letter-routing-key", "rk");
            channel.queueDeclare("queue.normal", false, false, false, arguments);
            channel.queueBind("queue.normal", "exchange.normal", "");
            channel.queueDeclare("queue.dlx", false, false, false, null);
            channel.queueBind("queue.dlx", "exchange.dlx", "rk");

            System.out.println("over");
        }
    }
}
