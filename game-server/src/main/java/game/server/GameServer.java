package game.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import game.server.codec.CenterEncoder;
import game.server.codec.CenterClientDecoderHandler;
import game.server.codec.PlayerClientDecoderHandler;
import game.world.AppContext;
import game.world.Server;
import game.world.WorldManager;
import game.server.codec.ServerEncoder;
import game.world.handler.Handler;
import game.world.net.Dispatcher;
import game.world.netty.AbstractServer;
import game.world.utils.MemcachedUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/13 17:03
 */
@Slf4j
public class GameServer extends AbstractServer {
    private static GameServer gameServer;
    private static ChannelGroup allCenterChannels = new DefaultChannelGroup(new DefaultEventExecutorGroup(1).next());
    private static CenterClient centerClient;

    public GameServer(int boss, int worker) {
        super(boss, worker);
    }

    public static void connectCenter(String ip, int port){
        centerClient = new CenterClient(ip, port, new CenterClientDecoderHandler(allCenterChannels), new CenterEncoder(), "中心服");
        centerClient.connect();
    }

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            WorldManager.getInstance().init();
            //应用上下文初始化
            new AppContext(new String[]{"spring*.xml"});
            Config config = AppContext.getBean(Config.class);
            Server server = AppContext.getBean(Server.class);
            Cache.GAME_EVENT_CMD = Dispatcher.getHandlers(Handler.class);

            //启动游戏服务器
            gameServer = new GameServer(config.getGameServerBossThread(), config.getGameServerWorkerThread());
            gameServer.start(server.getPort());

            if (StringUtils.isBlank(config.getCenterIp()) && config.getCenterPort() != null){
                connectCenter(config.getCenterIp(), config.getCenterPort());
            }

            Map<Integer, Map<String, Server>> servers = MemcachedUtil.get(MemcachedCacheVar.ALL_GAME_SERVER);
            if (servers == null){
                servers = Maps.newHashMap();
            }
            Map<String, Server> serverMap = servers.get(server.getArea());
            if (serverMap == null){
                Lists.newArrayList();
            }
            serverMap.put(server.getAddress(), server);
            MemcachedUtil.set(MemcachedCacheVar.ALL_GAME_SERVER, 0, serverMap);

            log.info("启动服务器花费时间：{}ms", (System.currentTimeMillis() - startTime));
        }catch (Exception e){
            log.error("启动服务器失败!", e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // do shutdown procedure here.
                    log.info("正在优雅的停止服务器.....");
                    ChannelGroupFuture future = gameServer.getAllChannels().close();
                    future.awaitUninterruptibly();// 阻塞，直到服务器关闭
                    ChannelGroupFuture futureCenter = allCenterChannels.close();
                    futureCenter.awaitUninterruptibly();// 阻塞，直到服务器关闭
                } catch (Exception e) {
                    log.error("停止服务器异常!", e);
                } finally {
                    WorldManager.getInstance().stop();
                    AppContext.destroy();
                    log.info("server is shutdown on port ");
                }
            }
        }));
    }

    @Override
    protected ChannelHandler getDecoderHandler() {
        return new PlayerClientDecoderHandler(getAllChannels());
    }

    @Override
    protected ChannelHandler getEncoderHandler() {
        return new ServerEncoder();
    }
}
