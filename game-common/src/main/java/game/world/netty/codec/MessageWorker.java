package game.world.netty.codec;

import com.google.protobuf.InvalidProtocolBufferException;
import game.world.BasicUser;
import game.world.WorldManager;
import game.world.disruptor.DisruptorEvent;
import game.world.event.MessageReceivedEvent;
import game.world.protobuf.ResultPro;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/3/27 14:07
 */
@Slf4j
public class MessageWorker {
    public final String loginIp;
    private final Channel channel;
    public String version;
    private volatile BasicUser user;
    private AtomicBoolean offlineProcessed = new AtomicBoolean(false);

    /**
     * 玩家线性线程
     */
    private volatile DisruptorEvent taskExec;

    private volatile DisruptorEvent taskInExec;

    public MessageWorker(Channel channel){
        if (channel == null){
            throw new NullPointerException("新建MessageWorker, channel为null");
        }
        this.channel = channel;
        loginIp = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
        taskInExec = new DisruptorEvent("MessageWorkerIn", WorldManager.CORE_NUM);
        log.info("connection ip = {}",loginIp);
    }

    public void messageReceived(ByteBuf buffer){
        if(buffer.readableBytes() < 1){
            log.warn("message length error......");
            channel.close();
            return;
        }
        //整个消息包大小
        final short length = buffer.readShort();
        //当前请求CMD
        final short cmd = buffer.readShort();
        //消息RET
        final byte[] ret = new byte[length - 2];
        buffer.readBytes(ret);
        ResultPro.Result result;
        try {
            result = ResultPro.Result.parseFrom(ret);
        } catch (InvalidProtocolBufferException e) {
            result = ResultPro.Result.getDefaultInstance();
        }
        log.info("RET:code:{},msg:{}", new Object[]{result.getCode(), result.getMsg()});
        //消息RET
        final byte[] body = new byte[buffer.readableBytes()];
        buffer.readBytes(body);

        MessageReceivedEvent messEvent = new MessageReceivedEvent(length, cmd, user, channel, result, this, body);
        taskInExec.publish(messEvent);
    }

    /**
     * 玩家掉线处理：设置当前MessageWorker的附加对象为null，设置玩家lastOffLineTime
     */
    public void processDisconnection() {
        log.info("-----玩家【{}】掉线------", user);
        if (offlineProcessed.compareAndSet(false, true)) {
            if (user == null) {
                return;
            }
            user.offline();
            user = null;
        }
    }

    public void executeTask(Runnable event) {
        taskExec.publish(event);
    }
}
