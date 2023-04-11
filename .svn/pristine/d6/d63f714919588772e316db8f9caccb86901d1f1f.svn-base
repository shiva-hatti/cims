/**
 * 
 */
package com.iris.sdmx.ebrvalidation.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author sajadhav
 *
 */
@JsonInclude(Include.NON_NULL)
public class EbrFileDetails {

	private Long fileDetailsId;

	private String entityCode;

	private String entityName;

	private String ifscCode;

	private Long userId;

	private Long uploadedOnInLong;

	private String fileType;

	private String fileName;

	private String systemModifiedFileName;

	private Integer fileSize;

	private String fileChecksum;

	public EbrFileDetails() {

	}

	public EbrFileDetails(Long fileDetailsId, String entityCode, Date uploadedOn, String systemModifiedFileName, String entityName, Long userId) {
		this.fileDetailsId = fileDetailsId;
		this.entityCode = entityCode;

		if (uploadedOn != null) {
			this.uploadedOnInLong = uploadedOn.getTime();
		}

		this.systemModifiedFileName = systemModifiedFileName;
		this.entityName = entityName;
		this.userId = userId;
	}

	/**
	 * @return the fileDetailsId
	 */
	public Long getFileDetailsId() {
		return fileDetailsId;
	}

	/**
	 * @param fileDetailsId the fileDetailsId to set
	 */
	public void setFileDetailsId(Long fileDetailsId) {
		this.fileDetailsId = fileDetailsId;
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
	 * @return the uploadedOnInLong
	 */
	public Long getUploadedOnInLong() {
		return uploadedOnInLong;
	}

	/**
	 * @param uploadedOnInLong the uploadedOnInLong to set
	 */
	public void setUploadedOnInLong(Long uploadedOnInLong) {
		this.uploadedOnInLong = uploadedOnInLong;
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
	 * @return the fileSize
	 */
	public Integer getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

}
