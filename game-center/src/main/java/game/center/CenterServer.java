package game.center;

import game.center.codec.ServerDecoderHandler;
import game.center.codec.ServerEncoder;
import game.world.AppContext;
import game.world.WorldManager;
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
        return new ServerDecoderHandler(getAllChannels());
    }

    @Override
    protected ChannelHandler getEncoderHandler() {
        return new ServerEncoder();
    }

    public static void main(String[] args) {
        WorldManager.getInstance().init();
        new AppContext(new String[]{"spring*.xml"});
        CenterServer centerServer = new CenterServer(1, 1);
        centerServer.start(4000);
        Cache.CENTER_EVENT_CMD = Dispatcher.getHandlers(Handler.class);
    }
}
