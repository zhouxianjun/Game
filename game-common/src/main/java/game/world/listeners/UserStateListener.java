package game.world.listeners;

import game.world.BasicUser;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/2 10:12
 */
public interface UserStateListener {
    /**
     * 离线
     * @param user
     */
    void offline(BasicUser user);
    /**
     * 上线
     * @param user
     */
    void online(BasicUser user);
    /**
     * 重连
     * @param user
     */
    void reconnect(BasicUser user);
}
