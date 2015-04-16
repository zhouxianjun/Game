package game.server;

import game.world.event.HandlerEvent;
import game.world.handler.Handler;

import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/14 18:29
 */
public class Cache {
    public static Map<Short, HandlerEvent<Handler>> GAME_EVENT_CMD = null;
}
