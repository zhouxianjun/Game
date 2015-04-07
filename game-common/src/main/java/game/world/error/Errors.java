package game.world.error;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract interface Errors {
	public Logger logger = LoggerFactory.getLogger(Errors.class.getName());
}
