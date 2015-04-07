package game.world.net;

import com.google.common.collect.Maps;
import com.google.protobuf.InvalidProtocolBufferException;
import game.world.event.Event;
import game.world.event.HandlerEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 游戏业务分发处理器
 * @author zhouxianjun
 */
@Slf4j
public class Dispatcher {

	private final static Map<Integer, HandlerEvent> handlers = Maps.newConcurrentMap();
	
	public static void dispatch(final Event event){
		final HandlerEvent handlerEvent = handlers.get(event.getCmd());
		if(handlerEvent == null) {
			log.info("收到没有处理事件的消息, 断开连接-[玩家 = {},cmd = 0x{}]", event.getUser(), Integer.toHexString(event.getCmd()));
            event.write(Packet.createGlobalExceptionPacket(SysErrorCode.CMD_NOT_EXIST));
            return;
		}
		if(!handlerEvent.isAsync()) {
			handle(event, handlerEvent.getHandler());
		} else {
			WorldManager.getInstance().executeDBEvent(new Runnable() {
				@Override
				public void run() {
					handle(event, handlerEvent.getHandler());
				}
			});
		}
	}
	
	private static void handle(Event event, Handler handler) {
		try {
			if(handler == null) {
				throw AppException.createGlobalAppException(SysErrorCode.CMD_NOT_EXIST);
			}
			handler.handle(event);
		} catch(AppException e) {
			handleError(event, e.getErrorCode(), e);
		} catch (InvalidProtocolBufferException e) {
			handleError(event, SysErrorCode.PROTOBUF_RESOLVE_ERROR, e);
		} catch(Exception e) {
			handleError(event, SysErrorCode.ERROR_SYSTEM, e);
		} catch (Throwable e) {
			handleError(event, SysErrorCode.ERROR_SYSTEM, e);
		}
	}
	
	private static void handleError(Event event, ErrorCode errorCode, Throwable e) {
		event.write(Packet.create(AppMessage.CMD_GLOBAL_EXC, errorCode));
		LOG.error("处理事件异常-[玩家 = {}, cmd = 0x{}, errorCode= {}, errorMsg = {}]", new Object[]{event.attachment(), Integer.toHexString(event.cmd()), errorCode, e.getMessage()});
		LOG.error(e.getMessage(), e);
	}
	public static void register(int cmd, HandlerEvent event) {
		handlers.put(cmd, event);
	}
	
}
