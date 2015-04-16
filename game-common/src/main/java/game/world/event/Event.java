package game.world.event;

import game.world.net.Packet;
import game.world.netty.codec.Worker;
import game.world.protobuf.ResultPro;
import io.netty.channel.Channel;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/2 11:33
 */
public interface Event<T> {
    /**
     * 请求开始时间
     * @return
     */
    long getStartTime();
    /**
     * 包大小：cmd+ret+data
     * @return
     */
    public int getLength();

    /**
     * 请求的cmd
     * @return
     */
    public short getCmd();

    /**
     * 状态
     * @return
     */
    public ResultPro.Result getRet();

    /**
     * 包体数据
     * @return
     */
    public byte[] getData();

    /**
     * 玩家
     * @return
     */
    public T getObject();

    /**
     * 维持的长连接管道
     * @return
     */
    public Channel getChannel();

    /**
     * 往该事件写回数据包
     * @return
     */
    public void write(Packet packet);

    /**
     * 当前玩家的消息工作者
     * @return
     */
    public Worker<T, ? extends ReceivedEvent> getWorker();
}
