package game.world;

import com.google.common.collect.HashBiMap;
import game.world.disruptor.DisruptorEvent;
import game.world.listeners.UserStateListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/2 10:01
 */
@Slf4j
public class WorldManager {
    private DisruptorEvent loginWorkers;

    private DisruptorEvent dbWorkers;

    private DisruptorEvent[] gameWorkers;

    private ConcurrentHashMap<Integer, BasicUser> onlinePlayerMap = new ConcurrentHashMap<Integer, BasicUser>();

    private HashBiMap<String, Integer> biReconnectSessionMap = HashBiMap.create();

    public static final int CORE_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 获取用户状态监听器
     * @return
     */
    public static List<UserStateListener> getUserStateListeners(){
        return null;
    }
}
