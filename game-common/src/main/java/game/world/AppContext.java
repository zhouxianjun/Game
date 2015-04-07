package game.world;

import cn.huizhi.network.Dispatcher;
import cn.huizhi.network.command.Cmd;
import cn.huizhi.network.event.HandlerEvent;
import cn.huizhi.network.handler.Handler;
import game.world.event.HandlerEvent;
import game.world.handler.Handler;
import game.world.net.Cmd;
import game.world.net.Dispatcher;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AppContext {

	private static AbstractApplicationContext applicationContext;
	
	public AppContext(String[] paths) {
		applicationContext = new ClassPathXmlApplicationContext(paths);
	}
	public void init() {
		Map<String, Handler> handlerMap = applicationContext.getBeansOfType(Handler.class);
		for(Entry<String, Handler> entry : handlerMap.entrySet()) {
			Handler handler = entry.getValue();
			Cmd cmd = handler.getClass().getAnnotation(Cmd.class);
			if(cmd == null) continue;
			int inCmd = cmd.in();
			HandlerEvent event = HandlerEvent.newInstance(inCmd, cmd.async(), handler);
			Dispatcher.register(inCmd, event);
		}
	}
	@SuppressWarnings("unchecked")  
    public static <T> T getBeanByName(String name) {
        return (T) applicationContext.getBean(name);
    }
    public static <T> T getBean(Class<T> beanClass) {
        Map<String, T> beanMap = applicationContext.getBeansOfType(beanClass);
        Iterator<T> iterator = beanMap.values().iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }
    public static <T> T getBean(String name, Class<T> requiredType) {
		try {
			T t = applicationContext.getBean(name, requiredType);
			return t;
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
    
    public static void destroy() {
    	applicationContext.destroy();
    }
}
