package game.center.event;

import com.sun.tools.javac.util.Assert;
import game.center.Cache;
import game.world.Server;
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
 * @Description:接收处理服务器发来的CMD
 * @date 2015/4/16 15:37
 */
@Slf4j
public class ServerReceivedEvent extends ReceivedEvent<Server> {
    public ServerReceivedEvent(int length, short cmd, Server object, Channel channel, ResultPro.Result ret, Worker<Server, ? extends ReceivedEvent> worker, byte[] data) {
        super(length, cmd, object, channel, ret, worker, data);
    }

    @Override
    public void run() {
        Assert.checkNonNull(Cache.CENTER_EVENT_CMD, "中心服事件处理Dispatcher未初始化!");
        HandlerEvent<Handler> handlerEvent = Cache.CENTER_EVENT_CMD.get(this.getCmd());
        if(handlerEvent == null) {
            log.info("收到没有处理事件的消息, [玩家 = {},cmd = 0x{}]", this.getObject(), Integer.toHexString(this.getCmd()));
            this.write(Packet.createGlobalException());
            return;
        }
        handle(handlerEvent);
    }
}
