package game.server;

import game.world.net.AppCmd;
import game.world.net.Packet;
import game.world.netty.AbstractClient;
import game.world.protobuf.ServerPro;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/15 17:39
 */
public class CenterClient extends AbstractClient {

    public CenterClient(String ip, int port, ChannelHandler handler, ChannelHandler encoder, String name) {
        super(ip, port, handler, encoder, name);
    }

    public CenterClient(String ip, int port, ChannelHandler handler, String name) {
        super(ip, port, handler, name);
    }

    @Override
    protected void connected(Channel channel) {
        ServerPro.Server.Builder server = ServerPro.Server.newBuilder();
        channel.writeAndFlush(Packet.createSuccess(AppCmd.CENTER_CONNECT, server.build()));
    }
}
