package game.world.error;

public abstract interface ErrorCode extends Errors {
	final int FAIL = 0;
	final int SUCCESS = 1;
	final int PARAM_FAIL = 400;
	final int NOT_FOUND = 404;
	final int UNKNOWN_ERROR = 500;
	final int UN_AUTHORIZED = 401;
	final int NO_ACCESS = 403;
}
