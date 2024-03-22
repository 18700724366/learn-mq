package xian.woniu.c_publish_subscribe;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
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
        channel.exchangeDeclare("logs", "fanout");

        // 发送消息
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("logs", "", null, ("Andy" + i).getBytes());
        }

        // 释放资源
        RabbitMqUtils.release(channel, connection);
    }
}
