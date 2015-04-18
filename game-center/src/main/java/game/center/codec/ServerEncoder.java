package game.center.codec;

import game.world.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:发送给服务器的编码
 * @date 2015/4/17 15:03
 */
public class ServerEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        msg.write(out);
    }
}
