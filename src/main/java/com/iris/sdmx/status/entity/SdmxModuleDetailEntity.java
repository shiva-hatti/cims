/**
 * 
 */
package com.iris.sdmx.status.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_MODULE_DETAIL")
public class SdmxModuleDetailEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6695990184449061077L;

	/**
	 * @param moduleStatusId
	 */
	public SdmxModuleDetailEntity(Long moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * 
	 */
	public SdmxModuleDetailEntity() {
	}

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MODULE_ID")
	private Long moduleId;

	/**
	 * 
	 */
	@Column(name = "MODULE_CODE")
	private String moduleCode;

	/**
	 * 
	 */
	@Column(name = "MODULE_NAME")
	private String moduleName;

	/**
	 * 
	 */
	@Column(name = "MODULE_DESC")
	private String moduleDesc;

	/**
	 * @return the moduleId
	 */
	public Long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the moduleCode
	 */
	public String getModuleCode() {
		return moduleCode;
	}

	/**
	 * @param moduleCode the moduleCode to set
	 */
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the moduleDesc
	 */
	public String getModuleDesc() {
		return moduleDesc;
	}

	/**
	 * @param moduleDesc the moduleDesc to set
	 */
	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxModuleDetailEntity [moduleId=" + moduleId + ", moduleCode=" + moduleCode + ", moduleName=" + moduleName + ", moduleDesc=" + moduleDesc + "]";
	}
}