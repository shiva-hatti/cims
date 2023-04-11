package com.iris.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.iris.service.impl.ReturnApprovalDetailsService;

public class JsonUtility {

	private static final Logger LOGGER = LogManager.getLogger(ReturnApprovalDetailsService.class);

	private final static Gson GSON = new Gson();

	public static String convertObjectToJson(Object obj) {
		if (obj != null) {
			return GSON.toJson(obj);
		} else {
			return null;
		}
	}

	public static String extractResponseValueFromServiceResponseString(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject.get("response").toString();
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}
		return null;
	}

	public static String extractResponseValueFromServiceResponseString(String jsonStr, String key) {
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			return jsonObject.get(key).toString();
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}
		return null;
	}

	public static Gson getGsonObject() {
		return GSON;
	}

}
