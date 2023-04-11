package com.iris.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.iris.sdmx.upload.bean.ElementAuditBean;

/**
 * @author apagaria
 *
 */
public class FileDetailsBeanLimitedField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8234286936384310373L;

	private Long id;

	private String fileName;

	private String systemModifiedFileName;

	private String fileType;

	private String fileMimeType;

	private Date creationDate;

	private String entityCode;

	private Boolean isActive;

	private String userName;

	private String ifscCode;

	private Long userId;

	private Long roleId;

	private String jsonFileName;

	private List<ElementAuditBean> elementAuditBeanList;

	/**
	 * @param id
	 * @param fileName
	 * @param fileCreationTime
	 * @param fileType
	 * @param fileMimeType
	 * @param creationDate
	 * @param entityCode
	 * @param isActive
	 * @param userName
	 * @param ifscCode
	 * @param filingStatusId
	 * @param status
	 * @param userId
	 * @param jsonFileName
	 * @param roleId
	 */
	public FileDetailsBeanLimitedField(Long id, String fileName, String systemModifiedFileName, Date fileCreationTime, String fileType, String fileMimeType, Date creationDate, String entityCode, Boolean isActive, String userName, String ifscCode, Long userId, String jsonFileName, Long roleId) {
		this.id = id;
		this.fileName = fileName;
		this.systemModifiedFileName = systemModifiedFileName;
		this.fileType = fileType;
		this.fileMimeType = fileMimeType;
		this.creationDate = creationDate;
		this.entityCode = entityCode;
		this.isActive = isActive;
		this.userName = userName;
		this.ifscCode = ifscCode;
		this.userId = userId;
		this.roleId = roleId;
	}

	/**
	 * 
	 */
	public FileDetailsBeanLimitedField() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the systemModifiedFileName
	 */
	public String getSystemModifiedFileName() {
		return systemModifiedFileName;
	}

	/**
	 * @param systemModifiedFileName the systemModifiedFileName to set
	 */
	public void setSystemModifiedFileName(String systemModifiedFileName) {
		this.systemModifiedFileName = systemModifiedFileName;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the fileMimeType
	 */
	public String getFileMimeType() {
		return fileMimeType;
	}

	/**
	 * @param fileMimeType the fileMimeType to set
	 */
	public void setFileMimeType(String fileMimeType) {
		this.fileMimeType = fileMimeType;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the ifscCode
	 */
	public String getIfscCode() {
		return ifscCode;
	}

	/**
	 * @param ifscCode the ifscCode to set
	 */
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the jsonFileName
	 */
	public String getJsonFileName() {
		return jsonFileName;
	}

	/**
	 * @param jsonFileName the jsonFileName to set
	 */
	public void setJsonFileName(String jsonFileName) {
		this.jsonFileName = jsonFileName;
	}

	/**
	 * @return the elementAuditBeanList
	 */
	public List<ElementAuditBean> getElementAuditBeanList() {
		return elementAuditBeanList;
	}

	/**
	 * @param elementAuditBeanList the elementAuditBeanList to set
	 */
	public void setElementAuditBeanList(List<ElementAuditBean> elementAuditBeanList) {
		this.elementAuditBeanList = elementAuditBeanList;
	}

}