package game.center.codec;

import game.center.event.ServerReceivedEvent;
import game.world.Server;
import game.world.netty.codec.Worker;
import io.netty.channel.Channel;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:处理服务器的Worker
 * @date 2015/4/16 15:36
 */
public class ServerWorker extends Worker<Server, ServerReceivedEvent> {
    public ServerWorker(Channel channel) {
        super(channel);
    }

    @Override
    protected boolean ping(Channel channel, Server object, short cmd) {
        return false;
    }
}
