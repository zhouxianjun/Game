package game.center;

import game.world.netty.AbstractServer;
import io.netty.channel.ChannelHandler;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/14 11:04
 */
public class CenterServer extends AbstractServer {

    public CenterServer(int boss, int worker) {
        super(boss, worker);
    }

    @Override
    protected ChannelHandler getDecoderHandler() {
        return null;
    }

    @Override
    protected ChannelHandler getEncoderHandler() {
        return null;
    }
}
