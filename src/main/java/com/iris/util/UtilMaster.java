package com.iris.util;

import java.io.Serializable;
import java.util.List;

public class UtilMaster implements Serializable {

	private static final long serialVersionUID = -5333930946826770933L;

	public static boolean isEmpty(String input) {
		boolean flag = false;
		if (input == null || input.trim().length() == 0) {
			flag = true;
		}
		return flag;
	}

	public static boolean isEmpty(Integer input) {
		boolean flag = false;
		if (input == null) {
			flag = true;
		}
		return flag;
	}

	public static boolean isEmpty(Object input) {
		boolean flag = false;
		if (input == null) {
			flag = true;
		}
		return flag;
	}

	public static String trimInput(String input) {
		if (input != null) {
			return input.trim();
		}
		return null;
	}

	public static boolean isEmpty(List<String> inputList) {
		boolean flag = false;
		if (inputList == null || inputList.size() == 0) {
			flag = true;
		}
		return flag;
	}

	public static String convertArrayToString(Object[] eAlertArr) {
		if (eAlertArr != null && eAlertArr.length > 0) {
			StringBuilder eAlert = new StringBuilder();
			for (int i = 0; i < eAlertArr.length; i++) {
				eAlert = eAlert.append(eAlertArr[i]);
				if (i < eAlertArr.length - 1) {
					eAlert = eAlert.append(",");
				}
			}
			return eAlert.toString();
		} else {
			return null;
		}
	}

}