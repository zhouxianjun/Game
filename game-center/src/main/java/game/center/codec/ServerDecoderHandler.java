package game.center.codec;

import game.world.Server;
import game.world.netty.codec.AbstractDecoderHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:接收服务器的解码
 * @date 2015/4/16 15:35
 */
public class ServerDecoderHandler extends AbstractDecoderHandler<ServerWorker, Server> {
    public ServerDecoderHandler(ChannelGroup channelGroup) {
        super(channelGroup);
    }

    public ServerDecoderHandler(ChannelGroup channelGroup, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(channelGroup, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
