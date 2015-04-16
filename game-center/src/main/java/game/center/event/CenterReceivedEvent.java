package game.center.event;

import game.world.Server;
import game.world.event.ReceivedEvent;
import game.world.netty.codec.Worker;
import game.world.protobuf.ResultPro;
import io.netty.channel.Channel;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/16 15:37
 */
public class CenterReceivedEvent extends ReceivedEvent<Server> {
    public CenterReceivedEvent(int length, short cmd, Server object, Channel channel, ResultPro.Result ret, Worker<Server, ? extends ReceivedEvent> worker, byte[] data) {
        super(length, cmd, object, channel, ret, worker, data);
    }

    @Override
    public void run() {

    }
}
