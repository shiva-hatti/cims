/**
 * 
 */
package com.iris.sdmx.exceltohtml.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_ELE_DIM_TYPE_MAP")
@JsonInclude(Include.NON_NULL)
public class SdmxEleDimTypeMapEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ELE_DIM_TYPE_MAP_ID")
	private Long eleDimTypeMapId;

	@Column(name = "DSD_CODE")
	private String dsdCode;

	@Column(name = "ELEMENT_VER")
	private String elementVer;

	@Column(name = "DIM_CODE")
	private String dimCode;

	@Column(name = "DIM_TYPE")
	private String dimType;

	@Column(name = "GROUP_NUM")
	private Integer groupNum;

	@Column(name = "ELE_DIM_HASH")
	private String eleDimHash;

	public SdmxEleDimTypeMapEntity() {
		// TODO Auto-generated constructor stub
	}

	public SdmxEleDimTypeMapEntity(String dsdCode, String elementVer, String dimCode, String dimType, Integer groupNum) {
		this.dsdCode = dsdCode;
		this.elementVer = elementVer;
		this.dimCode = dimCode;
		this.dimType = dimType;
		this.groupNum = groupNum;
	}

	/**
	 * @return the eleDimTypeMapId
	 */
	public Long getEleDimTypeMapId() {
		return eleDimTypeMapId;
	}

	/**
	 * @param eleDimTypeMapId the eleDimTypeMapId to set
	 */
	public void setEleDimTypeMapId(Long eleDimTypeMapId) {
		this.eleDimTypeMapId = eleDimTypeMapId;
	}

	/**
	 * @return the dsdCode
	 */
	public String getDsdCode() {
		return dsdCode;
	}

	/**
	 * @param dsdCode the dsdCode to set
	 */
	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	/**
	 * @return the elementVer
	 */
	public String getElementVer() {
		return elementVer;
	}

	/**
	 * @param elementVer the elementVer to set
	 */
	public void setElementVer(String elementVer) {
		this.elementVer = elementVer;
	}

	/**
	 * @return the dimCode
	 */
	public String getDimCode() {
		return dimCode;
	}

	/**
	 * @param dimCode the dimCode to set
	 */
	public void setDimCode(String dimCode) {
		this.dimCode = dimCode;
	}

	/**
	 * @return the dimType
	 */
	public String getDimType() {
		return dimType;
	}

	/**
	 * @param dimType the dimType to set
	 */
	public void setDimType(String dimType) {
		this.dimType = dimType;
	}

	/**
	 * @return the groupNum
	 */
	public Integer getGroupNum() {
		return groupNum;
	}

	/**
	 * @param groupNum the groupNum to set
	 */
	public void setGroupNum(Integer groupNum) {
		this.groupNum = groupNum;
	}

	/**
	 * @return the eleDimHash
	 */
	public String getEleDimHash() {
		return eleDimHash;
	}

	/**
	 * @param eleDimHash the eleDimHash to set
	 */
	public void setEleDimHash(String eleDimHash) {
		this.eleDimHash = eleDimHash;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxEleDimTypeMapEntity [eleDimTypeMapId=" + eleDimTypeMapId + ", dsdCode=" + dsdCode + ", elementVer=" + elementVer + ", dimCode=" + dimCode + ", dimType=" + dimType + ", groupNum=" + groupNum + ", eleDimHash=" + eleDimHash + "]";
	}
}
