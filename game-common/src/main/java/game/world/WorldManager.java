package game.world;

import com.google.common.collect.HashBiMap;
import game.world.disruptor.DisruptorEvent;
import game.world.listeners.UserStateListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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

    private ConcurrentHashMap<Integer, BasicUser> onlineUserMap = new ConcurrentHashMap<Integer, BasicUser>();

    private HashBiMap<String, Integer> biReconnectSessionMap = HashBiMap.create();

    public static final int CORE_NUM = Runtime.getRuntime().availableProcessors();

    public static final int GAME_THREAD_COUNT = getClosestPowerOf2(CORE_NUM);
    public static final int GAME_THREAD_COUNT_TO_MOD = GAME_THREAD_COUNT - 1;

    public static final int LOGIN_WORKER_NUM = 1;

    public void init(){
        loginWorkers = new DisruptorEvent("LOGIN_WORKER_", LOGIN_WORKER_NUM, 1 << 16);
        // ------------- 初始化 db worker（执行DB操作） ----------------
        dbWorkers = new DisruptorEvent("DB_WORKER_", GAME_THREAD_COUNT * 2, 1 << 16);

        // ------------- 初始化 game worker(线性执行) -----------------
        gameWorkers = new DisruptorEvent[GAME_THREAD_COUNT];

        for (int i = 0; i < GAME_THREAD_COUNT; i++) {
            gameWorkers[i] = new DisruptorEvent("GAME_WORKER_" + i, 1);
        }

        onlineUserTask.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                Iterator<Map.Entry<Integer, BasicUser>> it = onlineUserMap.entrySet().iterator();
                long nowTime = System.currentTimeMillis();
                while(it.hasNext()) {
                    BasicUser user = it.next().getValue();
                    if((user.heartTime > 0 && nowTime - user.heartTime > OFF_LINE_TIMEOUT)
                            || (user.lastOfflineTime > 0 && nowTime - user.lastOfflineTime > OFF_LINE_TIMEOUT)) {
                        it.remove();
                        log.info("定时任务从在线玩家列表移除玩家【 {}】", user);
                        if(player.desk != null) {
                            player.desk.recycleAi();
                        }
                        player.channel.close();
                        biReconnectSessionMap.inverse().remove(player.id);
                    }
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private ScheduledExecutorService onlineUserTask = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "ONLINE_USER_TASK");
            return thread;
        }
    });

    /**
     * 获得最近的2的倍数
     *
     * @param x
     * @return
     */
    private static int getClosestPowerOf2(int x) {
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        x++;
        return x;
    }

    /**
     * 获取用户状态监听器
     * @return
     */
    public static List<UserStateListener> getUserStateListeners(){
        return null;
    }

    public void executeDBEvent(Runnable event) {
        dbWorkers.publish(event);
    }
}
