package game.world.disruptor;


public class EventFactory implements com.lmax.disruptor.EventFactory<Event> {

	@Override
	public Event newInstance() {
		return new Event();
	}

}
