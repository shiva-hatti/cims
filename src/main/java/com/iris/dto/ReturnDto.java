package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.Frequency;
import com.iris.model.Regulator;
import com.iris.model.UploadChannel;

@JsonInclude(Include.NON_NULL)
public class ReturnDto implements Serializable {

	private static final long serialVersionUID = -8541319963212477139L;

	private Long returnId;
	private String returnCode;
	private String returnName;
	private Boolean isParent;
	private Boolean isActive;
	private Long frequencyId;
	private Frequency frequency;
	private Boolean allowRevision;
	private Boolean deptMapped;
	private String emailIds;
	private String selected;
	private String sheetName;
	private String tableName;
	@JsonInclude(Include.NON_NULL)
	private List<UploadChannel> uploadChannelList;
	private String agencyMasterCode;
	private String templateVersion;
	private String ebrVersion;

	private Regulator regulator;

	private List<Long> returnTypeIds;

	private List<String> celReferences;

	private boolean isAssignedToEntity;

	private Long regulatorId;

	private String regulatorCode;

	private String regulatorName;

	public ReturnDto(Long returnId, String returnCode, String returnName, Long regulatorId, String regulatorCode, String regulatorName) {
		this.returnId = returnId;
		this.returnCode = returnCode;
		this.returnName = returnName;

		this.regulator = new Regulator();
		this.regulator.setRegulatorId(regulatorId);
		this.regulator.setRegulatorName(regulatorName);
		this.regulator.setRegulatorCode(regulatorCode);
	}

	public ReturnDto(Long returnId, String returnCode) {
		this.returnId = returnId;
		this.returnCode = returnCode;
	}

	public ReturnDto() {

	}

	/**
	 * @return the isAssignedToEntity
	 */
	public boolean isAssignedToEntity() {
		return isAssignedToEntity;
	}

	/**
	 * @param isAssignedToEntity the isAssignedToEntity to set
	 */
	public void setAssignedToEntity(boolean isAssignedToEntity) {
		this.isAssignedToEntity = isAssignedToEntity;
	}

	/**
	 * @return the celReferences
	 */
	public List<String> getCelReferences() {
		return celReferences;
	}

	/**
	 * @param celReferences the celReferences to set
	 */
	public void setCelReferences(List<String> celReferences) {
		this.celReferences = celReferences;
	}

	/**
	 * @return the returnTypeIds
	 */
	public List<Long> getReturnTypeIds() {
		return returnTypeIds;
	}

	/**
	 * @param returnTypeIds the returnTypeIds to set
	 */
	public void setReturnTypeIds(List<Long> returnTypeIds) {
		this.returnTypeIds = returnTypeIds;
	}

	/**
	 * @return the regulator
	 */
	public Regulator getRegulator() {
		return regulator;
	}

	/**
	 * @param regulator the regulator to set
	 */
	public void setRegulator(Regulator regulator) {
		this.regulator = regulator;
	}

	public Long getReturnId() {
		return returnId;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public List<UploadChannel> getUploadChannelList() {
		return uploadChannelList;
	}

	public void setUploadChannelList(List<UploadChannel> uploadChannelList) {
		this.uploadChannelList = uploadChannelList;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the isParent
	 */
	public Boolean getIsParent() {
		return isParent;
	}

	/**
	 * @param isParent the isParent to set
	 */
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the allowRevision
	 */
	public Boolean getAllowRevision() {
		return allowRevision;
	}

	/**
	 * @param allowRevision the allowRevision to set
	 */
	public void setAllowRevision(Boolean allowRevision) {
		this.allowRevision = allowRevision;
	}

	/**
	 * @return the frequencyId
	 */
	public Long getFrequencyId() {
		return frequencyId;
	}

	/**
	 * @param frequencyId the frequencyId to set
	 */
	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	/**
	 * @return the frequency
	 */
	public Frequency getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the deptMapped
	 */
	public Boolean getDeptMapped() {
		return deptMapped;
	}

	/**
	 * @param deptMapped the deptMapped to set
	 */
	public void setDeptMapped(Boolean deptMapped) {
		this.deptMapped = deptMapped;
	}

	/**
	 * @return the emailIds
	 */
	public String getEmailIds() {
		return emailIds;
	}

	/**
	 * @param emailIds the emailIds to set
	 */
	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the agencyMasterCode
	 */
	public String getAgencyMasterCode() {
		return agencyMasterCode;
	}

	/**
	 * @param agencyMasterCode the agencyMasterCode to set
	 */
	public void setAgencyMasterCode(String agencyMasterCode) {
		this.agencyMasterCode = agencyMasterCode;
	}

	/**
	 * @return the templateVersion
	 */
	public String getTemplateVersion() {
		return templateVersion;
	}

	/**
	 * @param templateVersion the templateVersion to set
	 */
	public void setTemplateVersion(String templateVersion) {
		this.templateVersion = templateVersion;
	}

	/**
	 * @return the ebrVersion
	 */
	public String getEbrVersion() {
		return ebrVersion;
	}

	/**
	 * @param ebrVersion the ebrVersion to set
	 */
	public void setEbrVersion(String ebrVersion) {
		this.ebrVersion = ebrVersion;
	}

	/**
	 * @return the regulatorId
	 */
	public Long getRegulatorId() {
		return regulatorId;
	}

	/**
	 * @param regulatorId the regulatorId to set
	 */
	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	/**
	 * @return the regulatorCode
	 */
	public String getRegulatorCode() {
		return regulatorCode;
	}

	/**
	 * @param regulatorCode the regulatorCode to set
	 */
	public void setRegulatorCode(String regulatorCode) {
		this.regulatorCode = regulatorCode;
	}

	/**
	 * @return the regulatorName
	 */
	public String getRegulatorName() {
		return regulatorName;
	}

	/**
	 * @param regulatorName the regulatorName to set
	 */
	public void setRegulatorName(String regulatorName) {
		this.regulatorName = regulatorName;
	}

}
