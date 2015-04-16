package game.center.codec;

import game.world.Server;
import game.world.netty.codec.AbstractDecoderHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/16 15:35
 */
public class CenterDecoderHandler extends AbstractDecoderHandler<CenterWorker, Server> {
    public CenterDecoderHandler(ChannelGroup channelGroup) {
        super(channelGroup);
    }

    public CenterDecoderHandler(ChannelGroup channelGroup, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(channelGroup, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
