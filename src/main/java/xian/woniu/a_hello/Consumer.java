package xian.woniu.a_hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.217.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("root");
        factory.setPassword("root");

        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        // 让channel绑定队列，如果不存在则创建，如果已经存在则直接绑定
        channel.queueDeclare("hello", false, false, false, null);

        // 消费消息，注意，这里会消费掉队列中的所有消息（除非指定qos）
        channel.basicConsume("hello", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("recv: " + msg);
                if (msg.equals("10")) {
                    // 只有消费到的消息是10的时候，才确认消费到消息
                    // 测试时可以将第二个参数设置为 true 或 false，再观察队列中还剩几个消息
                    channel.basicAck(envelope.getDeliveryTag(), true);
                }
            }
        });

        // 因为消费消息是异步的，所以这里故意暂停，防止消息还没有被消费就关闭了连接
        System.out.println("Enter...");
        System.in.read();

        channel.close();
        connection.close();
    }
}
