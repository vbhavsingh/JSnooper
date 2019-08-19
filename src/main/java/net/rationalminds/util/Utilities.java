package net.rationalminds.util;

import java.util.HashMap;
import java.util.Map;

public class Utilities {

	/**
	 * 
	 * @param oldMap
	 * @param newMap
	 * @return
	 */
	public static Map<String, String> insertIntoMap(Map<String, String> oldMap,
			Map<String, String> newMap) {
		if (oldMap == null) {
			oldMap = new HashMap<String, String>();
		}
		if (newMap != null) {
			oldMap.putAll(newMap);
		}
		return oldMap;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map<String, String> map) {
		if (map == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			builder.append("\"");
			builder.append(entry.getKey());
			builder.append("\":");
			builder.append("\"");
			builder.append(entry.getValue());
			builder.append("\",");
		}
		builder.append("}");
		return builder.toString();
	}

}
