package game.center;

import game.center.codec.CenterDecoderHandler;
import game.center.codec.CenterEncoder;
import game.world.handler.Handler;
import game.world.net.Dispatcher;
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
        return new CenterDecoderHandler(getAllChannels());
    }

    @Override
    protected ChannelHandler getEncoderHandler() {
        return new CenterEncoder();
    }

    public static void main(String[] args) {
        CenterServer centerServer = new CenterServer(1, 1);
        centerServer.start(4000);
        Cache.CENTER_EVENT_CMD = Dispatcher.getHandlers(Handler.class);
    }
}
