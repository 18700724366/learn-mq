package xian.woniu.k_priority_queue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/03 22:14:01
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            // 设置消息优先级为5
            builder.priority(5);
            AMQP.BasicProperties properties = builder.build();

            String msg = "优先级为5的消息";
            channel.basicPublish("", "queue.priority", properties, msg.getBytes());

            builder.priority(6);
            properties = builder.build();
            msg = "优先级为6的消息";
            channel.basicPublish("", "queue.priority", properties, msg.getBytes());

            System.out.println("投递消息完毕");
        }
    }
}
