package game.world.event;

import game.world.WorldManager;
import game.world.error.ErrorCode;
import game.world.handler.Handler;
import game.world.net.Packet;
import game.world.netty.codec.Worker;
import game.world.protobuf.ResultPro;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/14 15:51
 */
@Getter
@Slf4j
public abstract class ReceivedEvent<T> implements Runnable, Event<T> {

    private long startTime;
    private int length;
    private short cmd;
    private ResultPro.Result ret;
    private byte[] data;
    private Worker<T, ? extends ReceivedEvent> worker;
    private Channel channel;
    private T object;

    public ReceivedEvent(int length, short cmd, T object, Channel channel, ResultPro.Result ret, Worker<T, ? extends ReceivedEvent> worker, byte[] data) {
        this.length = length;
        this.cmd = cmd;
        this.object = object;
        this.channel = channel;
        this.ret = ret;
        this.worker = worker;
        this.data = data;
        this.startTime = System.currentTimeMillis();
    }

    public void write(Packet packet) {
        if (packet.getCmd() == null)
            packet.setCmd(cmd);
        channel.writeAndFlush(packet);
        log.debug("回复消息：IP:{}, 玩家:{}, CMD:0x{}, 耗时:{}毫秒", new Object[]{worker.ip, object, Integer.toHexString(cmd), System.currentTimeMillis() - startTime});
    }

    protected void handle(final HandlerEvent<Handler> handlerEvent){
        if(!handlerEvent.isAsync()) {
            handle(this, handlerEvent.getHandler());
        } else {
            WorldManager.getInstance().executeDBEvent(new Runnable() {
                @Override
                public void run() {
                    handle(ReceivedEvent.this, handlerEvent.getHandler());
                }
            });
        }
    }
    
    protected void handle(Event event, Handler handler) {
        try {
            if(handler == null) {
                event.write(Packet.createGlobalException(ErrorCode.NOT_FOUND));
            }
            handler.handle(event);
        } catch(Exception e) {
            event.write(Packet.createError(ErrorCode.UNKNOWN_ERROR, null));
            log.error("Handler异常:", e);
        }
    }
}
