package com.iris.dto;

import java.io.Serializable;

/**
 * 
 * @author sanjayv
 */
public class RoleNameDto implements Serializable {

	private static final long serialVersionUID = 8813042586892944078L;

	private String langCode;
	private String roleName;

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}