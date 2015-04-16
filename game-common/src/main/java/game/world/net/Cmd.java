package game.world.net;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cmd {

	/**
	 * 请求消息包ID
	 * @return
	 */
	short value();
	/**
	 * 是否异步
	 * @return
	 */
	boolean async() default false;
}
