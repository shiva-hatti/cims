package com.iris.caching;

import java.util.HashMap;
import java.util.Map;

public class ObjectCache {

	private static Map<String, Object> cacheMap = new HashMap<>();
	private static Map<String, Object> sdmxServiceMap = new HashMap<>();
	private static Map<String, Map<String, String>> lableMap = new HashMap<>();
	private static Map<String, String> errorCodeKeyMap = new HashMap<>();
	private static String dateFormat;
	private static String timeFormat;

	public static String getLabelKeyValue(String languageCode, String key) {
		if (lableMap.get(languageCode) != null) {
			if (lableMap.get(languageCode).get(key) != null) {
				return lableMap.get(languageCode).get(key);
			} else {
				return key;
			}
		} else {
			return key;
		}
	}

	public static void putLabelKeyValue(String languageCode, String key, String value) {
		if (key != null && languageCode != null) {
			Map<String, String> languageMap = lableMap.get(languageCode);
			if (languageMap != null) {
				languageMap.put(key, value);
			} else {
				Map<String, String> labelKeyValueMap = new HashMap<>();
				labelKeyValueMap.put(key, value);
				lableMap.put(languageCode, labelKeyValueMap);
			}
		}
	}

	public static void putErrorCodeKey(String errorCode, String errorKey) {
		if (errorCode != null) {
			errorCodeKeyMap.put(errorCode, errorKey);
		}
	}

	public static String getErrorCodeKey(String errorCode) {
		if (errorCode != null) {
			if (errorCodeKeyMap.get(errorCode) != null) {
				return errorCodeKeyMap.get(errorCode);
			}
		}
		return errorCode;
	}

	//	public static <K, V> String getErrorCodeFromKey(V value) {
	//		return errorCodeKeyMap.entrySet()
	//					   .stream()
	//					   .filter(entry -> value.equals(entry.getValue()))
	//					   .map(Map.Entry::getKey)
	//					   .findFirst().orElse(value.toString());
	//	}

	public static Map<String, Object> getCacheMap() {
		return cacheMap;
	}

	public static void setCacheMap(Map<String, Object> cacheMap) {
		ObjectCache.cacheMap = cacheMap;
	}

	/**
	 * @return the sdmxServiceMap
	 */
	public static Map<String, Object> getSdmxServiceMap() {
		return sdmxServiceMap;
	}

	/**
	 * @param sdmxServiceMap the sdmxServiceMap to set
	 */
	public static void setSdmxServiceMap(Map<String, Object> sdmxServiceMap) {
		ObjectCache.sdmxServiceMap = sdmxServiceMap;
	}

	public static String getDateFormat() {
		return dateFormat;
	}

	public static void setDateFormat(String dateFormat) {
		ObjectCache.dateFormat = dateFormat;
	}

	public static String getTimeFormat() {
		return timeFormat;
	}

	public static void setTimeFormat(String timeFormat) {
		ObjectCache.timeFormat = timeFormat;
	}

}