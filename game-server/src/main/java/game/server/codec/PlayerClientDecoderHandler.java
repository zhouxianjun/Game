package game.server.codec;

import game.world.BasicUser;
import game.world.netty.codec.AbstractDecoderHandler;
import game.world.netty.codec.Worker;
import game.world.protobuf.ResultPro;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description: 接收玩家客户端的消息解码
 * @date 2015/4/14 11:40
 */
@Slf4j
public class PlayerClientDecoderHandler extends AbstractDecoderHandler<PlayerWorker, BasicUser> {
    public PlayerClientDecoderHandler(ChannelGroup channelGroup) {
        super(channelGroup);
    }
}
