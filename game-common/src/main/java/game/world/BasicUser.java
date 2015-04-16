package game.world;

import com.sun.xml.internal.ws.api.message.Packet;
import game.world.listeners.UserStateListener;
import game.world.netty.codec.Worker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/2 10:07
 */
@Slf4j
public class BasicUser {
    public int id;

    public String name;

    public int sex;

    public Server server;

    public volatile Channel channel;

    /**
     * 登陆时间（秒）
     */
    public volatile int loginTime;
    /**
     * 心跳时间
     */
    public volatile long heartTime;

    /**
     * 最后离线时间（秒）
     */
    public volatile long lastOfflineTime;

    public volatile Worker worker;

    /**
     * 给玩家发数据包
     * @param packet
     */
    public ChannelFuture send(Packet packet) {
        if(isOnline()) {
            return channel.writeAndFlush(packet);
        }
        return null;
    }
    /**
     * 判断玩家是否在线
     * @return
     */
    public boolean isOnline() {
        if(channel == null) return false;
        if(!channel.isActive()) {
            return false;
        }
        return true;
    }

    public void offline() {
        channel.close();
        stateChanged("offline");
    }

    public void reconnect() {
        stateChanged("reconnect");
    }

    public void online() {
        stateChanged("online");
    }

    /**
     * 玩家状态变更
     * @param state
     */
    private void stateChanged(final String state){
        Iterator<UserStateListener> it = WorldManager.getUserStateListeners().iterator();
        while(it.hasNext()) {
            final UserStateListener listener = it.next();
            worker.executeTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        UserStateListener.class.getMethod(state).invoke(listener, BasicUser.this);
                    } catch (Exception e) {
                        log.warn("用户:[{}]状态变更:<{}>异常:{}", new Object[]{BasicUser.this, state, e});
                    }
                }
            });
        }
    }
}
