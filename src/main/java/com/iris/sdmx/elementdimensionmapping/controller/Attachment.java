/**
 * 
 */
package com.iris.sdmx.elementdimensionmapping.controller;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_SDMX_ATTACHMENT_TYPE")
public class Attachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -127785675023503841L;

	@Id
	@Column(name = "ATTACHEMENT_ID")
	private Integer attachmentId;

	@Column(name = "ATTACHEMENT_NAME")
	private String attachmentName;

	@Column(name = "ATTACHEMENT_CODE")
	private String attachmentCode;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the attachmentId
	 */
	public Integer getAttachmentId() {
		return attachmentId;
	}

	/**
	 * @param attachmentId the attachmentId to set
	 */
	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	/**
	 * @return the attachmentName
	 */
	public String getAttachmentName() {
		return attachmentName;
	}

	/**
	 * @param attachmentName the attachmentName to set
	 */
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	/**
	 * @return the attachmentCode
	 */
	public String getAttachmentCode() {
		return attachmentCode;
	}

	/**
	 * @param attachmentCode the attachmentCode to set
	 */
	public void setAttachmentCode(String attachmentCode) {
		this.attachmentCode = attachmentCode;
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
}
