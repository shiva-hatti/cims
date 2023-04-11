package com.iris.model;

import java.io.Serializable;

/**
 * @author apagaria
 *
 */
public class ReturnUploadNBusinessData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -971611428043866420L;
	/**
	 * 
	 */
	private Long uploadId;

	/**
	 * 
	 */
	private int fillingStatusId;

	/**
	 * 
	 */
	private String csvValidatorJson;

	/**
	 * 
	 */
	private String applicationProcessId;

	/**
	 * 
	 */
	private String consistancyCheckJson;

	/**
	 * 
	 */
	private Boolean isConsistancyCheckFailure;
	private String etlFolderPath;

	private int noOfErrors;
	private int noOfWarnings;

	/**
	 * @return the isConsistancyCheckFailure
	 */
	public Boolean getIsConsistancyCheckFailure() {
		return isConsistancyCheckFailure;
	}

	/**
	 * @param isConsistancyCheckFailure the isConsistancyCheckFailure to set
	 */
	public void setIsConsistancyCheckFailure(Boolean isConsistancyCheckFailure) {
		this.isConsistancyCheckFailure = isConsistancyCheckFailure;
	}

	/**
	 * @return the uploadId
	 */
	public Long getUploadId() {
		return uploadId;
	}

	/**
	 * @param uploadId the uploadId to set
	 */
	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	/**
	 * @return the fillingStatusId
	 */
	public int getFillingStatusId() {
		return fillingStatusId;
	}

	/**
	 * @param fillingStatusId the fillingStatusId to set
	 */
	public void setFillingStatusId(int fillingStatusId) {
		this.fillingStatusId = fillingStatusId;
	}

	/**
	 * @return the csvValidatorJson
	 */
	public String getCsvValidatorJson() {
		return csvValidatorJson;
	}

	/**
	 * @param csvValidatorJson the csvValidatorJson to set
	 */
	public void setCsvValidatorJson(String csvValidatorJson) {
		this.csvValidatorJson = csvValidatorJson;
	}

	/**
	 * @return the applicationProcessId
	 */
	public String getApplicationProcessId() {
		return applicationProcessId;
	}

	/**
	 * @param applicationProcessId the applicationProcessId to set
	 */
	public void setApplicationProcessId(String applicationProcessId) {
		this.applicationProcessId = applicationProcessId;
	}

	/**
	 * @return the consistancyCheckJson
	 */
	public String getConsistancyCheckJson() {
		return consistancyCheckJson;
	}

	/**
	 * @param consistancyCheckJson the consistancyCheckJson to set
	 */
	public void setConsistancyCheckJson(String consistancyCheckJson) {
		this.consistancyCheckJson = consistancyCheckJson;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "ReturnUploadNBusinessData [uploadId=" + uploadId + ", fillingStatusId=" + fillingStatusId + ", csvValidatorJson=" + csvValidatorJson + ", applicationProcessId=" + applicationProcessId + "]";
	}

	/**
	 * @return the etlFolderPath
	 */
	public String getEtlFolderPath() {
		return etlFolderPath;
	}

	/**
	 * @param etlFolderPath the etlFolderPath to set
	 */
	public void setEtlFolderPath(String etlFolderPath) {
		this.etlFolderPath = etlFolderPath;
	}

	public int getNoOfErrors() {
		return noOfErrors;
	}

	public void setNoOfErrors(int noOfErrors) {
		this.noOfErrors = noOfErrors;
	}

	public int getNoOfWarnings() {
		return noOfWarnings;
	}

	public void setNoOfWarnings(int noOfWarnings) {
		this.noOfWarnings = noOfWarnings;
	}

}
