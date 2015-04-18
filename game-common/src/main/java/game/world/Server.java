package game.world;

import com.google.protobuf.MessageLite;
import game.world.utils.BasicProto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/2 10:09
 */
@Component
@Data
@NoArgsConstructor
public class Server extends BasicProto implements Serializable {
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

    public Server(MessageLite proto){
        parseProto(proto);
    }

    public String getAddress(){
        return getIp() + ":" + getPort();
    }
}
