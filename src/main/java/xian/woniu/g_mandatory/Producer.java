package xian.woniu.g_mandatory;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/02 23:05:29
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare("queue_demo", false, false, false, null);

            // 注意下面故意使用了一个无效的路由键
            String msg = "匆匆那年";
            channel.basicPublish("", "rk", true, null, msg.getBytes());

            System.out.println("消息已发出");

            // 添加ReturnListener监听器
            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println("被退回的消息：" + msg);
                }
            });

            // 阻塞主线程，否则有可能消息还没来得及被退回来，线程就结束了
            System.out.println("Enter...");
            System.in.read();
        }
    }
}
