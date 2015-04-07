package game.world.error;

public abstract interface ErrorDesc extends Errors {
	final String FAIL = "操作失败";
	final String SUCCESS = "操作成功";
	final String PARAM_FAIL = "参数验证失败";
	final String NOT_FOUND = "未找到";
	final String UNKNOWN_ERROR = "未知错误";
	final String UN_AUTHORIZED = "未授权";
	final String NO_ACCESS = "禁止访问:没有访问的权限";
}
