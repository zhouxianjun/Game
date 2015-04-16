package game.server.event;

import com.sun.tools.javac.util.Assert;
import game.server.Cache;
import game.world.AppContext;
import game.world.BasicUser;
import game.world.WorldManager;
import game.world.error.ErrorCode;
import game.world.event.Event;
import game.world.event.HandlerEvent;
import game.world.event.ReceivedEvent;
import game.world.handler.Handler;
import game.world.net.Packet;
import game.world.netty.codec.Worker;
import game.world.protobuf.ResultPro;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/14 16:26
 */
@Slf4j
public class PlayerReceivedEvent extends ReceivedEvent<BasicUser> {

    public PlayerReceivedEvent(int length, short cmd, BasicUser object, Channel channel, ResultPro.Result ret, Worker<BasicUser, PlayerReceivedEvent> worker, byte[] data) {
        super(length, cmd, object, channel, ret, worker, data);
    }

    @Override
    public void run() {
        Assert.checkNonNull(Cache.GAME_EVENT_CMD, "游戏事件处理Dispatcher未初始化!");
        final HandlerEvent<Handler> handlerEvent = Cache.GAME_EVENT_CMD.get(this.getCmd());
        if(handlerEvent == null) {
            log.info("收到没有处理事件的消息, [玩家 = {},cmd = 0x{}]", this.getObject(), Integer.toHexString(this.getCmd()));
            this.write(Packet.createGlobalException());
            return;
        }
        if(!handlerEvent.isAsync()) {
            handle(this, handlerEvent.getHandler());
        } else {
            WorldManager.getInstance().executeDBEvent(new Runnable() {
                @Override
                public void run() {
                    handle(PlayerReceivedEvent.this, handlerEvent.getHandler());
                }
            });
        }
    }

    private void handle(Event event, Handler handler) {
        try {
            if(handler == null) {
                event.write(Packet.createGlobalException(ErrorCode.NOT_FOUND));
            }
            handler.handle(event);
        } catch(Exception e) {
            event.write(Packet.createError(ErrorCode.UNKNOWN_ERROR, null));
            log.error("游戏Handler异常:", e);
        }
    }
}
