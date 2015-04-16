package game.center.codec;

import game.center.event.CenterReceivedEvent;
import game.world.Server;
import game.world.netty.codec.Worker;
import io.netty.channel.Channel;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/16 15:36
 */
public class CenterWorker extends Worker<Server, CenterReceivedEvent> {
    public CenterWorker(Channel channel) {
        super(channel);
    }

    @Override
    protected boolean ping(Channel channel, Server object, short cmd) {
        return false;
    }
}
