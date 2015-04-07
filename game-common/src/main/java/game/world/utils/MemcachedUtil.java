package game.world.utils;

import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.MemcachedClient;

@Slf4j
public class MemcachedUtil {
	private static MemcachedClient memcachedClient;
	public static <T> T get(String key){
		try {
			return memcachedClient.get(key);
		} catch (Exception e) {
			log.warn("从Memcached获取{}缓存数据错误!", key);
		}
		return null;
	}
	
	/**
	 * 删除缓存,不抛出异常
	 * @param key
	 * @return boolean
	 */
	public static boolean delete(String key){
		try {
			return memcachedClient.delete(key);
		} catch (Exception e) {
			log.warn("从Memcached删除{}缓存数据错误!", key);
		}
		return true;
	}
	public static void setMemcachedClient(MemcachedClient memcachedClient) {
		MemcachedUtil.memcachedClient = memcachedClient;
	}
}
