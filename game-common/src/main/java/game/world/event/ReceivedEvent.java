package game.world.event;

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
}
