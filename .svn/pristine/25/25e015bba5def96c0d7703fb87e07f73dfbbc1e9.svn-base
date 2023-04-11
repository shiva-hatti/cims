/**
 * 
 */
package com.iris.sdmx.status.entity;

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

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_PROCESS_DETAIL")
public class SdmxProcessDetailEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6695990184449061077L;

	/**
	 * @param moduleStatusId
	 */
	public SdmxProcessDetailEntity(Integer processId) {
		this.processId = processId;
	}

	/**
	 * 
	 */
	public SdmxProcessDetailEntity() {
	}

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROCESS_ID_PK")
	private Integer processId;

	/**
	 * 
	 */
	@Column(name = "PROCESS_CODE")
	private String processCode;

	/**
	 * 
	 */
	@Column(name = "PROCESS_NAME")
	private String processName;

	/**
	 * 
	 */
	@Column(name = "PROCESS_DESC")
	private String processDesc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SDMX_MODULE_ID_FK")
	private SdmxModuleDetailEntity sdmxModuleDetailIdFk;

	/**
	 * @return the processId
	 */
	public Integer getProcessId() {
		return processId;
	}

	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	/**
	 * @return the processCode
	 */
	public String getProcessCode() {
		return processCode;
	}

	/**
	 * @param processCode the processCode to set
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * @return the processDesc
	 */
	public String getProcessDesc() {
		return processDesc;
	}

	/**
	 * @param processDesc the processDesc to set
	 */
	public void setProcessDesc(String processDesc) {
		this.processDesc = processDesc;
	}

	/**
	 * @return the sdmxModuleDetailIdFk
	 */
	public SdmxModuleDetailEntity getSdmxModuleDetailIdFk() {
		return sdmxModuleDetailIdFk;
	}

	/**
	 * @param sdmxModuleDetailIdFk the sdmxModuleDetailIdFk to set
	 */
	public void setSdmxModuleDetailIdFk(SdmxModuleDetailEntity sdmxModuleDetailIdFk) {
		this.sdmxModuleDetailIdFk = sdmxModuleDetailIdFk;
	}
}