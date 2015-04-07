package game.world.handler;

import game.world.event.Event;

public interface Handler {

	public void handle(final Event event) throws Exception;
}
