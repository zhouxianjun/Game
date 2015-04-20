package game.server.codec;

import game.server.GameServer;
import game.server.event.CenterReceivedEvent;
import game.world.Server;
import game.world.netty.codec.Worker;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:中心服的消息处理worker
 * @date 2015/4/18 13:36
 */
@Slf4j
public class CenterWorker extends Worker<Server, CenterReceivedEvent> {
    public CenterWorker(Channel channel) {
        super(channel);
    }

    @Override
    protected boolean ping(Channel channel, Server object, short cmd) {
        return false;
    }

    @Override
    protected void disconnection(Server object, Channel channel, String ip) {
        log.warn("与中心服{}断开连接!", ip);
        GameServer.connectCenter(null);
    }
}
