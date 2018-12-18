package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sunding
 */
public abstract class CollectionUtil {

	public CollectionUtil() {
	}

	public static boolean isNull(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isNotNull(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	public static boolean isNull(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static boolean isNotNull(Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}

	public static <K, V> Map<K, V> newSingleMap(K key, V value) {
		HashMap map = new HashMap(1);
		map.put(key, value);
		return map;
	}
}
