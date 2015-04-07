package game.world.event;

import com.sun.xml.internal.ws.api.message.Packet;
import game.world.BasicUser;
import game.world.netty.codec.MessageWorker;
import game.world.protobuf.ResultPro;
import io.netty.channel.Channel;
import lombok.Getter;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/2 11:34
 */
@Getter
public class MessageReceivedEvent implements Runnable, Event {
    private long startTime;

    private byte[] data;

    private final int length;

    private final int cmd;

    private Object user;

    private Channel channel;

    private ResultPro.Result ret;

    private MessageWorker messageWorker;

    public MessageReceivedEvent(int length, int cmd, BasicUser user, Channel channel, ResultPro.Result ret, MessageWorker messageWorker, byte[] data) {
        this.length = length;
        this.cmd = cmd;
        this.user = user;
        this.channel = channel;
        this.ret = ret;
        this.messageWorker = messageWorker;
        this.data = data;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void write(Packet packet) {

    }

    @Override
    public void run() {

    }
}
