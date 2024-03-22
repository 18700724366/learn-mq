package xian.woniu.l_reject.c;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xian.woniu.z_utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/05 23:34:42
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            for (int i = 0; i < 10; i++) {
                String msg = "G.E.M";
                channel.basicPublish("", "queue.normal", null, (msg + i).getBytes());
            }
            System.out.println("投递消息完毕");
        }
    }
}
