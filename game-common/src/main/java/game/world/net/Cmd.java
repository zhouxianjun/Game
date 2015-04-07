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
	int in();
	/**
	 * 请求回复消息包ID
	 * @return
	 */
	int out() default 0;
	/**
	 * 是否异步
	 * @return
	 */
	boolean async() default false;
}
