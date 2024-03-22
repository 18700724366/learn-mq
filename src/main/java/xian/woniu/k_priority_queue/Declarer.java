package xian.woniu.k_priority_queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 22:09:14
 */
public class Declarer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            Map<String, Object> arguments = new HashMap<>();
            // 设置队列中消息的最大优先级
            arguments.put("x-max-priority", 10);
            channel.queueDeclare("queue.priority", false, false, false, arguments);

            System.out.println("over");
        }
    }
}
