package com.iris.sdmx.upload.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author vjadhav
 *
 */

@JsonInclude(Include.NON_NULL)
public class EbrFileDetailsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long ebrFileAuditId;

	private Long uploadChannelIdFk;

	private String uploadChannelDesc;

	private String fileName;

	private Long size;

	private String fileChecksum;

	private String fileMimeType;

	private String fileType;

	private Date uploadDate;

	private Long entityIdFk;

	private String entityCode;

	private String entityName;

	private String ifscCode;

	private int elementCount;

	private Long userIdFk;

	private String userName;

	private String userEmailId;

	private Integer status;

	private String jsonFileName;

	private String filePath;

	private Long roleIdFk;

	private String langCode;

	private Date createdRecordDate;

	private String userSelectedFileName;

	/**
	 * @return the userSelectedFileName
	 */
	public String getUserSelectedFileName() {
		return userSelectedFileName;
	}

	/**
	 * @param userSelectedFileName the userSelectedFileName to set
	 */
	public void setUserSelectedFileName(String userSelectedFileName) {
		this.userSelectedFileName = userSelectedFileName;
	}

	/**
	 * @return the createdRecordDate
	 */
	public Date getCreatedRecordDate() {
		return createdRecordDate;
	}

	/**
	 * @param createdRecordDate the createdRecordDate to set
	 */
	public void setCreatedRecordDate(Date createdRecordDate) {
		this.createdRecordDate = createdRecordDate;
	}

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return langCode;
	}

	/**
	 * @param langCode the langCode to set
	 */
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public EbrFileDetailsBean() {

	}

	public EbrFileDetailsBean(Long ebrFileAuditId, Long uploadChannelIdFk, String fileName, Long size, String fileMimeType, String fileType, Long entityIdFk, String entityCode, String entityName, String ifscCode, int elementCount, Long userIdFk, String userEmailId, Integer status, String jsonFileName, Long roleIdFk) {
		this.ebrFileAuditId = ebrFileAuditId;
		this.uploadChannelIdFk = uploadChannelIdFk;
		this.fileName = fileName;
		this.size = size;
		this.fileMimeType = fileMimeType;
		this.fileType = fileType;
		this.entityIdFk = entityIdFk;
		this.entityCode = entityCode;
		this.entityName = entityName;
		this.ifscCode = ifscCode;
		this.elementCount = elementCount;
		this.userIdFk = userIdFk;
		this.userEmailId = userEmailId;
		this.status = status;
		this.jsonFileName = jsonFileName;
		this.roleIdFk = roleIdFk;
	}

	public Long getRoleIdFk() {
		return roleIdFk;
	}

	public void setRoleIdFk(Long roleIdFk) {
		this.roleIdFk = roleIdFk;
	}

	/**
	 * @return the ebrFileAuditId
	 */
	public Long getEbrFileAuditId() {
		return ebrFileAuditId;
	}

	/**
	 * @param ebrFileAuditId the ebrFileAuditId to set
	 */
	public void setEbrFileAuditId(Long ebrFileAuditId) {
		this.ebrFileAuditId = ebrFileAuditId;
	}

	/**
	 * @return the uploadChannelIdFk
	 */
	public Long getUploadChannelIdFk() {
		return uploadChannelIdFk;
	}

	/**
	 * @param uploadChannelIdFk the uploadChannelIdFk to set
	 */
	public void setUploadChannelIdFk(Long uploadChannelIdFk) {
		this.uploadChannelIdFk = uploadChannelIdFk;
	}

	/**
	 * @return the uploadChannelDesc
	 */
	public String getUploadChannelDesc() {
		return uploadChannelDesc;
	}

	/**
	 * @param uploadChannelDesc the uploadChannelDesc to set
	 */
	public void setUploadChannelDesc(String uploadChannelDesc) {
		this.uploadChannelDesc = uploadChannelDesc;
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
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * @return the fileChecksum
	 */
	public String getFileChecksum() {
		return fileChecksum;
	}

	/**
	 * @param fileChecksum the fileChecksum to set
	 */
	public void setFileChecksum(String fileChecksum) {
		this.fileChecksum = fileChecksum;
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
	 * @return the uploadDate
	 */
	public Date getUploadDate() {
		return uploadDate;
	}

	/**
	 * @param uploadDate the uploadDate to set
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * @return the entityIdFk
	 */
	public Long getEntityIdFk() {
		return entityIdFk;
	}

	/**
	 * @param entityIdFk the entityIdFk to set
	 */
	public void setEntityIdFk(Long entityIdFk) {
		this.entityIdFk = entityIdFk;
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
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
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
	 * @return the elementCount
	 */
	public int getElementCount() {
		return elementCount;
	}

	/**
	 * @param elementCount the elementCount to set
	 */
	public void setElementCount(int elementCount) {
		this.elementCount = elementCount;
	}

	/**
	 * @return the userIdFk
	 */
	public Long getUserIdFk() {
		return userIdFk;
	}

	/**
	 * @param userIdFk the userIdFk to set
	 */
	public void setUserIdFk(Long userIdFk) {
		this.userIdFk = userIdFk;
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
	 * @return the userEmailId
	 */
	public String getUserEmailId() {
		return userEmailId;
	}

	/**
	 * @param userEmailId the userEmailId to set
	 */
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "EbrFileAuditBean [ebrFileAuditId=" + ebrFileAuditId + ", uploadChannelIdFk=" + uploadChannelIdFk + ", uploadChannelDesc=" + uploadChannelDesc + ", fileName=" + fileName + ", size=" + size + ", fileChecksum=" + fileChecksum + ", fileMimeType=" + fileMimeType + ", fileType=" + fileType + ", uploadDate=" + uploadDate + ", entityIdFk=" + entityIdFk + ", entityCode=" + entityCode + ", entityName=" + entityName + ", elementCount=" + elementCount + ", userIdFk=" + userIdFk + ", userName=" + userName + ", userEmailId=" + userEmailId + ", status=" + status + ", jsonFileName=" + jsonFileName + ", filePath=" + filePath + "]";
	}

}
