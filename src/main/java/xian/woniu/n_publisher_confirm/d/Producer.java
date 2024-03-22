package xian.woniu.n_publisher_confirm.d;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/04 17:37:09
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {
            // 开启发布确认模式
            channel.confirmSelect();
            // outstandingConfirms用于存放还未被确认的消息
            ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

            ConfirmCallback cleanOutstandingConfirms = (deliveryTag, multiple) -> {
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag, true);
                    confirmed.clear();
                    System.out.println("异步批量确认");
                } else {
                    outstandingConfirms.remove(deliveryTag);
                    System.out.println("异步单个确认");
                }
            };

            ConfirmCallback logOutstandingConfirms = (deliveryTag, multiple) -> {
                String msg = outstandingConfirms.get(deliveryTag);
                System.err.format(
                        "Message with body %s has been nack-ed. deliveryTag: %d, multiple: %b%n",
                        msg, deliveryTag, multiple
                );
                cleanOutstandingConfirms.handle(deliveryTag, multiple);
            };

            // 为channel添加确认监听器：
            channel.addConfirmListener(cleanOutstandingConfirms, logOutstandingConfirms);

            // 声明队列
            channel.queueDeclare("queue_demo", false, false, false, null);

            for (int i = 1; i <= 20; i++) {
                String msg = "G.E.M" + i;
                outstandingConfirms.put(channel.getNextPublishSeqNo(), msg);
                channel.basicPublish("", "queue_demo", null, msg.getBytes());
            }

        }
    }
}