package game.server.codec;

import game.world.net.AppCmd;
import game.world.net.Packet;
import game.world.protobuf.ServerPro;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:发送中心服的编码
 * @date 2015/4/13 14:34
 */
@Slf4j
public class CenterEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        msg.write(out);
        if (msg.getCmd() != AppCmd.PING){
            ServerPro.Server server = ServerPro.Server.parseFrom(msg.getBody().toByteArray());
            log.debug("回复消息：IP:, CENTER:, CMD:0x{}, 耗时:毫秒", new Object[]{Integer.toHexString(msg.getCmd())});
            //log.debug("回复消息CMD:0x{}, IP:{}, ");
        }
    }
}
