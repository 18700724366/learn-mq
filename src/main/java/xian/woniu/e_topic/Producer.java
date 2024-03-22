package xian.woniu.e_topic;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2021/06/30 16:38:47
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            // 参数1：交换机名称
            // 参数2：交换机类型
            channel.exchangeDeclare("topic_logs", "topic");

            // 发送消息
            Scanner in = new Scanner(System.in);
            System.out.println("请输入宠物昵称：");
            String msg = in.nextLine();
            System.out.println("请描述您的宠物：<speed>.<color>.<species>");
            String routingKey = in.nextLine();
            channel.basicPublish("topic_logs", routingKey, null, msg.getBytes());
        }
    }
}
