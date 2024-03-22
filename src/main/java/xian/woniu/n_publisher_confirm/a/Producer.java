package xian.woniu.n_publisher_confirm.a;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/04 16:08:09
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明队列
            channel.queueDeclare("queue_demo", false, false, false, null);
            try {
                // 在channel上开启“发布者确认”模式
                channel.confirmSelect();
                for (int i = 1; i <= 10; i++) {
                    // 投递消息
                    String msg = "Cindy" + i;
                    channel.basicPublish("", "queue_demo", null, msg.getBytes());
                    // 阻塞，直到Broker接收/拒绝了消息，或者等待超时
                    channel.waitForConfirmsOrDie(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
