package com.iris.sdmx.menu.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.iris.sdmx.menu.entity.SdmxMenu;

public class SdmxActionMenuMappingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9166632782562082613L;

	private Long sdmxActionMenuId;

	private String actionName;

	private Long sdmxMenuIdFk;

	/**
	 * @return the sdmxActionMenuId
	 */
	public Long getSdmxActionMenuId() {
		return sdmxActionMenuId;
	}

	/**
	 * @param sdmxActionMenuId the sdmxActionMenuId to set
	 */
	public void setSdmxActionMenuId(Long sdmxActionMenuId) {
		this.sdmxActionMenuId = sdmxActionMenuId;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return the sdmxMenuIdFk
	 */
	public Long getSdmxMenuIdFk() {
		return sdmxMenuIdFk;
	}

	/**
	 * @param sdmxMenuIdFk the sdmxMenuIdFk to set
	 */
	public void setSdmxMenuIdFk(Long sdmxMenuIdFk) {
		this.sdmxMenuIdFk = sdmxMenuIdFk;
	}

	public SdmxActionMenuMappingBean() {
	}

	@Override
	public String toString() {
		return "SdmxActionMenuMappingBean [sdmxActionMenuId=" + sdmxActionMenuId + ", actionName=" + actionName + ", sdmxMenuIdFk=" + sdmxMenuIdFk + "]";
	}

}
