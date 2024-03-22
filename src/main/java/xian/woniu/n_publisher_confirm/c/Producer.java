package xian.woniu.n_publisher_confirm.c;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

            // 用于缓存某一批次的消息
            List<String> msgList = new ArrayList<>();

            try {
                // 开启发布确认模式
                channel.confirmSelect();

                int batchSize = 5;
                int msgCount = 0;

                for (int i = 1; i <= 23; i++) {
                    String msg = "G.E.M" + i;
                    channel.basicPublish("", "queue_demo", null, msg.getBytes());
                    msgList.add(msg);
                    msgCount++;
                    if (msgCount == batchSize) {
                        channel.waitForConfirmsOrDie(5_000);
                        msgCount = 0;
                        msgList.clear();
                    }
                }
                if (msgCount > 0) {
                    channel.waitForConfirmsOrDie(5_000);
                    msgList.clear();
                }
            } catch (InterruptedException e) {
                // 本批次的消息的msgList集合中，我们可以生成日志，或者重试发送消息
                e.printStackTrace();
            }
        }
    }
}