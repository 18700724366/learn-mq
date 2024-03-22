package xian.woniu.o_delay_plugin;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/04 22:49:26
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            // 声明延迟交换机
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-delayed-type", "direct");
            channel.exchangeDeclare("delayed_exchange", "x-delayed-message", false, false, arguments);

            // 声明队列
            channel.queueDeclare("Q1", false, false, false, null);

            // 绑定队列到交换机
            channel.queueBind("Q1", "delayed_exchange", "rk");

            // 投递消息，设置延迟时间为20s
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            Map<String, Object> headers = new HashMap<>();
            headers.put("x-delay", 20000);
            builder.headers(headers);
            AMQP.BasicProperties properties = builder.build();

            channel.basicPublish("delayed_exchange", "rk", properties, "G.E.M".getBytes());

        }
    }
}