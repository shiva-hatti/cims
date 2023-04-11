package com.iris.sdmx.menu.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author vjadhav
 *
 */
@Entity
@Table(name = "TBL_SDMX_ACTION_MENU_MAPPING")
@JsonInclude(Include.NON_NULL)
public class SdmxActionMenuMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7486902495744302371L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SDMX_ACTION_MENU_ID")
	private Long sdmxActionMenuId;

	@Column(name = "ACTION_NAME")
	private String actionName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SDMX_MENU_ID_FK")
	private SdmxMenu sdmxMenuIdFk;

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
	public SdmxMenu getSdmxMenuIdFk() {
		return sdmxMenuIdFk;
	}

	/**
	 * @param sdmxMenuIdFk the sdmxMenuIdFk to set
	 */
	public void setSdmxMenuIdFk(SdmxMenu sdmxMenuIdFk) {
		this.sdmxMenuIdFk = sdmxMenuIdFk;
	}

	SdmxActionMenuMapping() {
	}

	@Override
	public String toString() {
		return "SdmxActionMenuMapping [sdmxActionMenuId=" + sdmxActionMenuId + ", actionName=" + actionName + ", sdmxMenuIdFk=" + sdmxMenuIdFk + "]";
	}

}
