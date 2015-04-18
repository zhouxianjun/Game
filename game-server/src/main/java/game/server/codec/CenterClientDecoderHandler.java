package game.server.codec;

import game.world.BasicUser;
import game.world.Server;
import game.world.netty.codec.AbstractDecoderHandler;
import game.world.protobuf.ResultPro;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:接收中心服的解码
 * @date 2015/4/14 13:53
 */
public class CenterClientDecoderHandler extends AbstractDecoderHandler<CenterWorker, Server> {

    public CenterClientDecoderHandler(ChannelGroup channelGroup) {
        super(channelGroup);
    }
}
