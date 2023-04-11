package com.iris.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AttachmentInfoBean")
public class AttachmentInfoBean {
	private String contentType;
	private String attachmentName;
	private Object attachment;

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
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
	 * @return the attachment
	 */
	public Object getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

}
