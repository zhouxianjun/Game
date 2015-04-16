package game.server;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/15 11:04
 */
@Component
@Data
public class Config {
    @Value("#{config_params['game_server_boss_thread']}")
    private Integer gameServerBossThread;
    @Value("#{config_params['game_server_worker_thread']}")
    private Integer gameServerWorkerThread;
    @Value("#{config_params['center_ip']}")
    private String centerIp;
    @Value("#{config_params['center_port']}")
    private Integer centerPort;
}
