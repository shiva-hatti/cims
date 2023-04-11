package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_PLATFORM_MASTER")
public class Platform implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8508154866856231035L;

	@Id
	@Column(name = "PLATFORM_ID")
	private Long platFormId;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "PLATFORM_KEY")
	private String platFormKey;

	@Column(name = "PLATFORM_CODE")
	private String platFormCode;

	@Column(name = "PLATFORM_URL")
	private String platFormUrl;

	@Column(name = "PLATFORM_APP_ID")
	private String platFormAppId;

	public Long getPlatFormId() {
		return platFormId;
	}

	public void setPlatFormId(Long platFormId) {
		this.platFormId = platFormId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getPlatFormKey() {
		return platFormKey;
	}

	public void setPlatFormKey(String platFormKey) {
		this.platFormKey = platFormKey;
	}

	public String getPlatFormCode() {
		return platFormCode;
	}

	public void setPlatFormCode(String platFormCode) {
		this.platFormCode = platFormCode;
	}

	public String getPlatFormUrl() {
		return platFormUrl;
	}

	public void setPlatFormUrl(String platFormUrl) {
		this.platFormUrl = platFormUrl;
	}

	public String getPlatFormAppId() {
		return platFormAppId;
	}

	public void setPlatFormAppId(String platFormAppId) {
		this.platFormAppId = platFormAppId;
	}

}
