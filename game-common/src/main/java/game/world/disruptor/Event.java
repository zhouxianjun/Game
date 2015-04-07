package game.world.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Event {

	private final static Logger LOG = LoggerFactory.getLogger(Event.class.getName());
	
	private Runnable event;

	public void setEvent(Runnable event) {
		this.event = event;
	}
	
	public void run() {
		try {
			this.event.run();
		} catch(Throwable e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
}
