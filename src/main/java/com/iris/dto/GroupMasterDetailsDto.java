/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author Siddique
 *
 */
public class GroupMasterDetailsDto implements Serializable {

	private static final long serialVersionUID = 1039739437376334410L;

	private Long id;
	private String fileName;
	private Long createdOn;
	private Integer status;
	private Long processStartTime;
	private Long processEndTime;
	private Long totalRecords;
	private String modifiedFileName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getProcessStartTime() {
		return processStartTime;
	}

	public void setProcessStartTime(Long processStartTime) {
		this.processStartTime = processStartTime;
	}

	public Long getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Long processEndTime) {
		this.processEndTime = processEndTime;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getModifiedFileName() {
		return modifiedFileName;
	}

	public void setModifiedFileName(String modifiedFileName) {
		this.modifiedFileName = modifiedFileName;
	}

}
