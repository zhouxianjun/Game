package game.server.handler;

import game.world.event.Event;
import game.world.handler.Handler;
import game.world.net.AppCmd;
import game.world.net.Cmd;
import org.springframework.stereotype.Component;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/16 10:38
 */
@Component
@Cmd(AppCmd.LOGIN)
public class LoginHandler implements Handler {
    @Override
    public void handle(Event event) throws Exception {

    }
}
