package xian.woniu.n_transaction;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/05 23:58:49
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            // 声明队列
            channel.queueDeclare("test_queue", false, false, false, null);

            try {
                // 开启事务
                channel.txSelect();
                String msg = "rabbit";
                channel.basicPublish("", "test_queue", null, msg.getBytes());

                String msg2 = "elephant";
                channel.basicPublish("", "test_queue", null, msg2.getBytes());

                /*
                    阻塞，此时应该去rabbitmq的控制台查看队列是否已经接收到消息了
                    在开启事务和没有开启事务两种环境下分别测试，对比结果
                 */
                System.out.println("Enter...");
                System.in.read();

                // 提交事务
                channel.txCommit();
            } catch (Exception e) {
                e.printStackTrace();
                channel.txRollback();
            }

        }
    }
}
