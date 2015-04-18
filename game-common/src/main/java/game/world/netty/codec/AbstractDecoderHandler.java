package game.world.netty.codec;

import com.google.protobuf.InvalidProtocolBufferException;
import game.world.event.ReceivedEvent;
import game.world.net.AppCmd;
import game.world.net.Packet;
import game.world.protobuf.ResultPro;
import game.world.utils.ClassUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/14 11:26
 */
@Slf4j
public abstract class AbstractDecoderHandler<W extends Worker<T, ? extends ReceivedEvent<T>>, T> extends LengthFieldBasedFrameDecoder {
    private final ChannelGroup channelGroup;
    private Channel channel;
    protected Worker<T, ? extends ReceivedEvent<T>> worker;
    public AbstractDecoderHandler(ChannelGroup channelGroup) {
        super(4096, 0, 2, 0, 0);
        this.channelGroup = channelGroup;
    }

    public AbstractDecoderHandler(ChannelGroup channelGroup, int maxFrameLength,
                                  int lengthFieldOffset, int lengthFieldLength,
                                  int lengthAdjustment, int initialBytesToStrip) {
        super(4096, 0, 2, 0, 0);
        this.channelGroup = channelGroup;
    }

    @Override
    protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
        try {
            if(buffer.readableBytes() < 1){
                log.warn("message length error......");
                channel.close();
                return null;
            }
            //整个消息包大小
            final short totalLength = buffer.readShort();
            //当前请求CMD
            final short cmd = buffer.readShort();
            //消息RET 长度
            final short retSize = buffer.readShort();
            //消息RET
            final byte[] ret = new byte[retSize];
            buffer.readBytes(ret);
            ResultPro.Result result;
            try {
                result = ResultPro.Result.parseFrom(ret);
            } catch (InvalidProtocolBufferException e) {
                result = ResultPro.Result.getDefaultInstance();
            }
            //消息RET
            final byte[] body = new byte[buffer.readableBytes()];
            buffer.readBytes(body);
            log.info("接收到消息:ip:{}, code:{},msg:{}", new Object[]{((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress(), result.getCode(), result.getMsg()});
            messageReceived(buffer, totalLength, cmd, result, body);
        } catch (Exception e) {
            log.error("IP:"+ worker.ip +" 接收消息异常:不符合标准!", e);
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
        connection(channel);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        disconnection();
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error("IP:"+ worker.ip +" 连接异常，关闭连接", cause);
        ctx.close().sync();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            timeOut(e.state(), ctx);
        }
    }

    /**
     * 收到消息
     * @param buffer
     * @param length
     * @param cmd
     * @param result
     * @param body
     */
    protected void messageReceived(ByteBuf buffer, int length, short cmd, ResultPro.Result result, byte[] body){
        worker.messageReceived(buffer, length, cmd, result, body);
    }
    protected void connection(Channel channel){
        Class<? extends Worker<T, ? extends ReceivedEvent<T>>> eventClass = getWorkerClass();
        try {
            Constructor<? extends Worker<T, ? extends ReceivedEvent<T>>> constructor = eventClass.getDeclaredConstructor(new Class[]{Channel.class});
            worker = constructor.newInstance(channel);
        } catch (Exception e) {
            log.error("工作创建失败!", e);
        }
    }
    protected Class<? extends Worker<T, ? extends ReceivedEvent<T>>> getWorkerClass(){
       return (Class<? extends Worker<T, ? extends ReceivedEvent<T>>>) ClassUtil.getSuperClassGenricType(getClass(), 0);
    }

    /**
     * 断开连接
     */
    protected void disconnection(){
        if (worker != null)
            worker.processDisconnection();
    }

    /**
     * 超时
     * @param state
     * @param ctx
     * @throws Exception
     */
    protected void timeOut(IdleState state, ChannelHandlerContext ctx) throws Exception {
        log.warn("IP:{}, {} 超时，关闭连接", worker.ip, state.name());
        ctx.close().sync();
    }
}
