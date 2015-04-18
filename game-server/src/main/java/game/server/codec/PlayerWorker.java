package game.server.codec;

import game.server.event.PlayerReceivedEvent;
import game.world.BasicUser;
import game.world.event.ReceivedEvent;
import game.world.net.AppCmd;
import game.world.net.Packet;
import game.world.netty.codec.Worker;
import io.netty.channel.Channel;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description: 玩家客户端的消息woker
 * @date 2015/4/14 16:30
 */
public class PlayerWorker extends Worker<BasicUser, PlayerReceivedEvent> {

    public PlayerWorker(Channel channel) {
        super(channel);
    }

    @Override
    protected boolean ping(Channel channel, BasicUser object, short cmd) {
        if (cmd == AppCmd.PING){
            object.heartTime = System.currentTimeMillis();
            channel.writeAndFlush(Packet.PING);
            return true;
        }
        return false;
    }
}
