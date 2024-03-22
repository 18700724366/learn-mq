package xian.woniu.l_reject.b;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/04 14:10:36
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare("queue.normal", false, false, true, null);

            for (int i = 0; i < 10; i++) {
                String msg = "G.E.M";
                channel.basicPublish("", "queue.normal", null, (msg + i).getBytes());
            }
            System.out.println("投递消息完毕");
        }
    }
}