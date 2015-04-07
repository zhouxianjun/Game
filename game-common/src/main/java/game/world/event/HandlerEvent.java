package game.world.event;

import cn.huizhi.network.handler.Handler;

public class HandlerEvent {

	private int cmd;
	
	private boolean async;
	
	private Handler handler;

	private HandlerEvent(int cmd, boolean async, Handler handler) {
		this.cmd = cmd;
		this.async = async;
		this.handler = handler;
	}
	public static HandlerEvent newInstance(int cmd, boolean sync, Handler handler) {
		return new HandlerEvent(cmd, sync, handler);
	}
	public int getCmd() {
		return cmd;
	}
	public boolean isAsync() {
		return async;
	}
	public Handler getHandler() {
		return handler;
	}
	
	
}
