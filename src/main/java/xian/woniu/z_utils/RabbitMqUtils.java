package xian.woniu.z_utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2021/06/30 16:31:51
 */
public class RabbitMqUtils {
    private static final String HOST = "192.168.3.128";
    private static final int PORT = 5672;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123";
    private static final String VIRTUAL_HOST = "/";

    public static Connection newConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        return factory.newConnection();
    }

    public static void release(Channel channel, Connection connection) throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
