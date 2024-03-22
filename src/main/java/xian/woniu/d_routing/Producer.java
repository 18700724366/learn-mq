package xian.woniu.d_routing;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2021/06/30 16:38:47
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.newConnection();
        Channel channel = connection.createChannel();

        // 参数1：交换机名称
        // 参数2：交换机类型
        channel.exchangeDeclare("direct_logs", "direct");

        // 发送消息
        for (int i = 0; i < 10; i++) {
            String serverity = getSeverity();
            String msg = ("Eason" + i);
            System.out.println("级别：" + serverity + ", 日志" + msg);
            channel.basicPublish("direct_logs", serverity, null, msg.getBytes());
        }

        // 释放资源
        RabbitMqUtils.release(channel, connection);
    }

    private static String getSeverity() {
        int r = new Random().nextInt(3);
        String serverity = null;
        switch (r) {
            case 0:
                serverity = "info";
                break;
            case 1:
                serverity = "warning";
                break;
            case 2:
                serverity = "error";
                break;
            default:
        }
        return serverity;
    }
}
