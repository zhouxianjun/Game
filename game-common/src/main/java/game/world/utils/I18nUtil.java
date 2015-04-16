package game.world.utils;

import game.world.AppContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Locale;

/**
 * 国际化资源信息
 * @author 周先军
 *
 */
public class I18nUtil {
	/**中文简体*/
	public static final String zh_CN = "zh_CN";
	/**中文繁体*/
	public static final String zh_TW = "zh_TW";
	/**美国*/
	public static final String en_US = "en_US";
	/**英国*/
	public static final String en_GB = "en_GB";
	/**加拿大*/
	public static final String en_CA = "en_CA";
	/**澳大利亚*/
	public static final String en_AU = "en_AU";
	/**日本*/
	public static final String ja_JP = "ja_JP";
	/**法国*/
	public static final String fr_FR = "fr_FR";
	public static String getMessage(String code, Object[] args, String defaultMessage,Locale locale) {
		return AppContext.getApplicationContext().getMessage(code, args, defaultMessage, locale);
	}
	
	public static String getMessage(String code, Object[] args, Locale locale) {
		return AppContext.getApplicationContext().getMessage(code, args, null, locale);
	}
	
	public static String getMessage(String code, Locale locale) {
		AbstractApplicationContext applicationContext = AppContext.getApplicationContext();
		return applicationContext == null ? null : applicationContext.getMessage(code, null, null, locale);
	}
	
	public static String getMessage(String code, String defaultMessage, Locale locale) {
		return AppContext.getApplicationContext().getMessage(code, null, defaultMessage, locale);
	}
}
