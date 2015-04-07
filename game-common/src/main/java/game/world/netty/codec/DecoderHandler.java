package game.world.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/3/27 13:53
 */
@Slf4j
public class DecoderHandler extends LengthFieldBasedFrameDecoder {

    private final ChannelGroup channelGroup;
    private Channel channel;
    private MessageWorker worker;

    public DecoderHandler(ChannelGroup channelGroup) {
        super(4096, 0, 2, 0, 0);
        this.channelGroup = channelGroup;
    }

    @Override
    protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer,
                                   int index, int length) {
        try {
            worker.messageReceived(buffer);
        } catch (Exception e) {
            log.error("extractFrame exception.{}", e.getMessage());
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(channel);
        worker = new MessageWorker(channel);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        worker.processDisconnection();
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error("-----------玩家【{}】exceptionCaught.{}", worker, cause.getMessage());
        ctx.close().sync();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                log.warn("-----------------玩家【{}】读超时，关闭连接----------", worker);
                ctx.close().sync();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                log.warn("----------------玩家【{}】写超时，关闭连接-----------", worker);
                ctx.close().sync();
            }
        }
    }
}
