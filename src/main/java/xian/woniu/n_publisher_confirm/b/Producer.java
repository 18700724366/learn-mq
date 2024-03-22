package xian.woniu.n_publisher_confirm.b;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/04 16:48:17
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            // 声明队列
            channel.queueDeclare("queue_demo", false, false, false, null);

            try {
                // 开启发布确认模式
                channel.confirmSelect();

                int batchSize = 5;
                int msgCount = 0;

                for (int i = 1; i <= 23; i++) {
                    String msg = "G.E.M" + i;
                    channel.basicPublish("", "queue_demo", null, msg.getBytes());
                    msgCount++;
                    if (msgCount == batchSize) {
                        channel.waitForConfirmsOrDie(5_000);
                        msgCount = 0;
                    }
                }
                if (msgCount > 0) {
                    channel.waitForConfirmsOrDie(5_000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}