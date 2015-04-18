package game.world;

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

    private DisruptorEvent dbWorkers;

    private DisruptorEvent[] gameWorkers;

    private ConcurrentHashMap<Integer, BasicUser> onlineUserMap = new ConcurrentHashMap<Integer, BasicUser>();

    public static final int CORE_NUM = Runtime.getRuntime().availableProcessors();

    public static final int GAME_THREAD_COUNT = getClosestPowerOf2(CORE_NUM);
    public static final int GAME_THREAD_COUNT_TO_MOD = GAME_THREAD_COUNT - 1;

    /**
     * 离线超时时间
     */
    public static final int OFF_LINE_TIMEOUT = 120000;

    private static final WorldManager worldManager = new WorldManager();

    public static WorldManager getInstance() {
        return worldManager;
    }

    public void init(){
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
                        user.offline();
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

    public DisruptorEvent getExecutorService(int id) {
        return gameWorkers[id & GAME_THREAD_COUNT_TO_MOD];
    }

    public void stop(){
       /* updateExec.shutdownNow();
        onlineUserTask.shutdownNow();
        aiSheduled.shutdownNow();
        for (int i = 0; i < GAME_THREAD_COUNT; i++) {
            gameWorkers[i].shutdown();
        }

        for(int i = 0; i < PK_WORKER_NUM; i ++) {
            pkWorkers[i].shutdown();
        }
        for(int i = 0; i < MULTI_PK_WORKER_NUM; i ++) {
            multiPkWorkers[i].shutdown();
        }
        for(ScheduledExecutorService scheduled : scheduledList) {
            scheduled.shutdownNow();
        }

        dbWorkers.shutdown();
        loginWorkers.shutdown();
        HttpDispatcher.shutdown();
        LogService.shutdown();*/
        log.info("成功并优雅的停止了服务器......");
    }
}
