/**
 * 
 */
package com.iris.ebr.business.technical.metadata.bean;

import java.io.Serializable;

/**
 * @author sajadhav
 *
 */

public class AttachmentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -127785675023503841L;

	private Integer attachmentId;

	private String attachmentName;

	private String attachmentCode;

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
