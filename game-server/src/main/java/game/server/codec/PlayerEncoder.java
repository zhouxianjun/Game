package game.server.codec;

import game.world.net.AppCmd;
import game.world.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description: 发送给玩家的消息编码
 * @date 2015/4/13 14:34
 */
@Slf4j
public class PlayerEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        msg.write(out);
        if (msg.getCmd() != AppCmd.PING){
            log.debug("回复消息：IP:, 玩家:, CMD:0x{}, 耗时:毫秒", new Object[]{Integer.toHexString(msg.getCmd())});
            //log.debug("回复消息CMD:0x{}, IP:{}, ");
        }
    }
}
