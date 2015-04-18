package game.server.codec;

import game.server.event.CenterReceivedEvent;
import game.world.Server;
import game.world.netty.codec.Worker;
import io.netty.channel.Channel;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:中心服的消息处理worker
 * @date 2015/4/18 13:36
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
