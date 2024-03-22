package xian.woniu.a_hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.217.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("root");
        factory.setPassword("root");

        // 获取连接
        Connection connection = factory.newConnection();

        // 获取通道
        Channel channel = connection.createChannel();
        // 声明队列
        // 参数1：队列名称，如果队列不存在，则创建队列，并与当前通道绑定，如果队列已经存在，则直接与当前通道绑定
        // 参数2：是否持久化
        // 参数3：是否排他
        // 参数4：是否自动删除队列
        // 参数5：附加参数
        channel.queueDeclare("hello", false, false, false, null);

        for (int i = 1; i <= 10; i++) {
            // 投递消息
            // 参数1：交换机名称，这里使用的是默认交换机，默认与所有队列绑定
            // 参数2：队列名称
            // 参数3：消息的附加设置
            // 参数4：消息的具体内容
            channel.basicPublish("", "hello", null, String.valueOf(i).getBytes());
        }


        channel.close();
        connection.close();
    }
}
