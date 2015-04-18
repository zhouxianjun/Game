package game.world.utils;

import game.world.error.ErrorCode;
import game.world.error.ErrorDesc;
import game.world.error.Errors;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 错误信息
 * 
 * @author 周先军
 * 
 */
@Component
public class ErrorsUtil implements Errors {
	public static ArrayList<String> errorsCodeClasses;
	public static ArrayList<String> errorsDescClasses;
	public static String basePackage = "/";
	protected static Map<Object, String> errors = null;
	private static Map<String, Object> errorsCodeTemp = null;
	private static Map<String, Object> errorsDescTemp = null;

	/**
	 * 获取错误信息(在国际化里面找不到则获取内存)
	 * 
	 * @param error
	 *            错误信息代码
	 * @param locale
	 *            语言
	 * @return
	 */
	public static String getErrorDesc(Object error, Locale locale) {
		String msg = I18nUtil.getMessage(String.valueOf(error), locale);
		return msg == null ? getErrorDesc(error) : msg;
	}

	/**
	 * 直接在内存里面获取
	 * 
	 * @param error
	 *            错误信息代码
	 * @return
	 */
	public static String getErrorDesc(Object error) {
		if (errors == null)
			init();
		String errorDescr = errors.get(error);
		errorDescr = errorDescr == null ? "错误码:" + error : errorDescr;
		return errorDescr;
	}

	private static void addAllClass(List<Class<?>> allAssignedClass, ArrayList<String> classes){
		if(classes != null && classes.size() > 0){
			for (String strClass : classes) {
				if(strClass != null && !"".equals(strClass) && !StringUtils.isEmpty(strClass)){
					try {
						Class<?> c = Class.forName(strClass);
						if(c != null)
							allAssignedClass.add(c);
					} catch (ClassNotFoundException e) {
						logger.debug(strClass + " 不合法!");
					}
				}
			}
		}
	}
	
	private static void init(){
		errors = new HashMap<Object, String>();
		errorsCodeTemp = new HashMap<String, Object>();
		errorsDescTemp = new HashMap<String, Object>();
		try {
			List<Class<?>> allAssignedClass = ClassUtil.getAllAssignedClass(ErrorCode.class, basePackage);
			if(allAssignedClass == null)
				allAssignedClass = new ArrayList<Class<?>>();
			allAssignedClass.add(ErrorCode.class);
			addAllClass(allAssignedClass, errorsCodeClasses);
			for (Class<?> c : allAssignedClass) {
				Class<?> implClass = Class.forName(c.getName());
				put(implClass, errorsCodeTemp);
			}
			allAssignedClass.clear();
			allAssignedClass = null;
			allAssignedClass = ClassUtil.getAllAssignedClass(ErrorDesc.class, basePackage);
			if(allAssignedClass == null)
				allAssignedClass = new ArrayList<Class<?>>();
			allAssignedClass.add(ErrorDesc.class);
			addAllClass(allAssignedClass, errorsDescClasses);
			for (Class<?> c : allAssignedClass) {
				Class<?> implClass = Class.forName(c.getName());
				put(implClass, errorsDescTemp);
			}
			for (Object code : errorsCodeTemp.keySet()) {
				Object value = errorsDescTemp.get(code);
				Object object = errorsCodeTemp.get(code);
				errors.put(object, value == null ? "错误码:" + object : value.toString());
			}
		} catch (Exception e) {
			logger.error("内存错误码初始化失败!", e);
		}
	}

	private static void put(Class<?> c, Map<String, Object> map) {
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			Object key = null;
			try {
				key = field.get(c);
			} catch (Exception e) {
				key = name;
			}
			if (key != null)
				map.put(name, key);
		}
	}

	public static void main(String[] args) {
		init();
	}

	public void setErrorsCodeClasses(ArrayList<String> errorsCodeClasses) {
		ErrorsUtil.errorsCodeClasses = errorsCodeClasses;
	}

	public void setErrorsDescClasses(ArrayList<String> errorsDescClasses) {
		ErrorsUtil.errorsDescClasses = errorsDescClasses;
	}

	public void setBasePackage(String basePackage) {
		ErrorsUtil.basePackage = basePackage;
	}

}
