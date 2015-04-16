package game.world.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {
	static ClassPathScanHandler cpsh = new ClassPathScanHandler(false,true,null);
	/**
	 * 获取同一路径下所有子类或接口实现类
	 * 
	 * @return
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getAllAssignedClass(Class<?> cls, String basePackage) throws IOException,
			ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (Class<?> c : cpsh.getPackageAllClasses(basePackage, true)) {
			if (cls.isAssignableFrom(c) && !cls.equals(c)) {
				classes.add(c);
			}
		}
		return classes;
	}

	/**
	 * 取得当前类路径下的所有类
	 * 
	 * @param cls
	 * @return
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getClasses(Class<?> cls) throws IOException,
			ClassNotFoundException {
		String pk = cls.getPackage().getName();
		String path = pk.replace('.', '/');
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(path);
		return getClasses(new File(url.getFile()), pk);
	}

	/**
	 * 迭代查找类
	 * 
	 * @param dir
	 * @param pk
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!dir.exists()) {
			return classes;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				classes.addAll(getClasses(f, pk + "." + f.getName()));
			}
			String name = f.getName();
			if (name.endsWith(".class")) {
				classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
			}
		}
		return classes;
	}

	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {

		//返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return null;
		}
		//返回表示此类型实际类型参数的 Type 对象的数组。
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return null;
		}
		if (!(params[index] instanceof Class)) {
			return null;
		}

		return (Class) params[index];
	}
}
