package xian.woniu.f_consumer_tag;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gao
 * @time 2022/07/02 22:03:15
 */
public class MyConsumer extends DefaultConsumer {

    private static Map<String, Map<String, Object>> sessions = new ConcurrentHashMap<>();

    private Channel channel;
    private String consumerTag;

    public MyConsumer(Channel channel, String consumerTag) {
        super(channel);
        this.channel = channel;
        this.consumerTag = consumerTag;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws
            IOException {
        String msg = new String(body);
        System.out.println(consumerTag + " recv: " + msg);
        channel.basicAck(envelope.getDeliveryTag(), false);

        Map<String, Object> channelSession = sessions.get(consumerTag);
        if (channelSession == null) {
            channelSession = new ConcurrentHashMap();
            sessions.put(consumerTag, channelSession);
        }
        Integer messageCount = (Integer) channelSession.get("messageCount");
        if (messageCount == null) {
            messageCount = 1;
        } else {
            messageCount++;
        }
        channelSession.put("messageCount", messageCount);


    }

    public Map<String, Object> getChannelSession() {
        return sessions.get(consumerTag);
    }

    public int getMessageCount() {
        Object obj = getChannelSession().get("messageCount");
        return obj == null ? 0 : Integer.valueOf(obj.toString());
    }
}
