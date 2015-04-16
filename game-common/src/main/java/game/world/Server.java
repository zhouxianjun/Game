package game.world;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/2 10:09
 */
@Component
@Getter
public class Server {
    @Value("#{config_params['game_server_area']}")
    private int area;

    @Value("#{config_params['game_server_probability']}")
    private int probability;

    @Value("#{config_params['game_server_ip']}")
    private String ip;

    @Value("#{config_params['game_server_port']}")
    private int port;

    @Value("#{config_params['game_server_name']}")
    private String name;

    @Value("#{config_params['game_server_version']}")
    private String version;

    @Value("#{config_params['game_server_max']}")
    private int max;

    private int cur;

    private boolean online;

    public String getAddress(){
        return getIp() + ":" + getPort();
    }
}
