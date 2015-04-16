package game.world.net;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/7 11:16
 */
public class AppCmd {
    /** 全局异常 */
    public static final short GLOBAL_EXC = 0x0000;
    /**ping包心跳*/
    public static final short PING = 0X0001;
    /**登陆*/
    public static final short LOGIN = 0X0002;
    /**连接中心服*/
    public static final short CENTER_CONNECT = 0X0003;
}
