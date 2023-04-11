package com.iris.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Transient;
import com.iris.model.ErrorKey;
import com.iris.model.LanguageMaster;
import com.iris.model.UserMaster;

public class ErrorLableDto implements Serializable {

	private static final long serialVersionUID = 1599942123183614042L;

	private String languageCode;
	private String errorKey;
	private String errorCode;
	private String errorDispLable;

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDispLable() {
		return errorDispLable;
	}

	public void setErrorDispLable(String errorDispLable) {
		this.errorDispLable = errorDispLable;
	}

}
