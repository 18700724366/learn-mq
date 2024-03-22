package xian.woniu.h_alternate_exchange;

import xian.woniu.z_utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gao
 * @time 2022/07/02 23:48:50
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMqUtils.newConnection();
             Channel channel = connection.createChannel()) {

            /*
                注意下面故意将路由键设置为不可能路由到队列的值，此时消息会交给备份交换机
             */
            String msg = "爱你";
            channel.basicPublish("normalExchange", "rk", null, msg.getBytes());
        }
    }
}
