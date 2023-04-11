package com.iris.sdmx.upload.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;

/**
 * @author vjadhav
 *
 */
@Entity
@Table(name = "TBL_ELEMENT_AUDIT")
@JsonInclude(Include.NON_NULL)
public class ElementAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ELEMENT_AUDIT_ID")
	private Long elementAuditId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_DETAILS_ID_FK")
	private FileDetails fileDetails;

	@Column(name = "ELEMENT_CODE")
	private String elementCode;

	@Column(name = "ELEMENT_VERSION")
	private String elementVersion;

	@Column(name = "ROW_COUNT")
	private int rowCount;

	@Column(name = "IS_PUSHED_TO_LND")
	private String isPushedToLnd;

	@Column(name = "ELE_RETURN_REF")
	private String eleReturnRef;

	@Column(name = "IS_REVISED")
	private String isRevised;

	@Column(name = "VALIDATION_STATUS")
	private int valStatus;

	@Column(name = "VALIDATION_RESULT")
	private String valResult;

	@Column(name = "CONVERTED_FILE_NAME")
	private String convertedFileName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_PROPERTY_VAL_ID_FK")
	private ReturnPropertyValue returnPropertyVal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS")
	private FilingStatus status;

	/**
	 * 
	 */
	public ElementAudit() {
	}

	/**
	 * @param elementAuditId
	 * @param elementCode
	 * @param elementVersion
	 * @param eleReturnRef
	 * @param convertedFileName
	 * @param status
	 */
	public ElementAudit(Long elementAuditId, String elementCode, String elementVersion, String eleReturnRef, String convertedFileName) {
		this.elementAuditId = elementAuditId;
		this.elementCode = elementCode;
		this.elementVersion = elementVersion;
		this.eleReturnRef = eleReturnRef;
		this.convertedFileName = convertedFileName;
	}

	public ReturnPropertyValue getReturnPropertyVal() {
		return returnPropertyVal;
	}

	public void setReturnPropertyVal(ReturnPropertyValue returnPropertyVal) {
		this.returnPropertyVal = returnPropertyVal;
	}

	/**
	 * @return the elementAuditId
	 */
	public Long getElementAuditId() {
		return elementAuditId;
	}

	/**
	 * @param elementAuditId the elementAuditId to set
	 */
	public void setElementAuditId(Long elementAuditId) {
		this.elementAuditId = elementAuditId;
	}

	/**
	 * @return the fileDetails
	 */
	public FileDetails getFileDetails() {
		return fileDetails;
	}

	/**
	 * @param fileDetails the fileDetails to set
	 */
	public void setFileDetails(FileDetails fileDetails) {
		this.fileDetails = fileDetails;
	}

	/**
	 * @return the elementCode
	 */
	public String getElementCode() {
		return elementCode;
	}

	/**
	 * @param elementCode the elementCode to set
	 */
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}

	/**
	 * @return the elementVersion
	 */
	public String getElementVersion() {
		return elementVersion;
	}

	/**
	 * @param elementVersion the elementVersion to set
	 */
	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
	}

	/**
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @param rowCount the rowCount to set
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * @return the isPushedToLnd
	 */
	public String getIsPushedToLnd() {
		return isPushedToLnd;
	}

	/**
	 * @param isPushedToLnd the isPushedToLnd to set
	 */
	public void setIsPushedToLnd(String isPushedToLnd) {
		this.isPushedToLnd = isPushedToLnd;
	}

	/**
	 * @return the eleReturnRef
	 */
	public String getEleReturnRef() {
		return eleReturnRef;
	}

	/**
	 * @param eleReturnRef the eleReturnRef to set
	 */
	public void setEleReturnRef(String eleReturnRef) {
		this.eleReturnRef = eleReturnRef;
	}

	/**
	 * @return the isRevised
	 */
	public String getIsRevised() {
		return isRevised;
	}

	/**
	 * @param isRevised the isRevised to set
	 */
	public void setIsRevised(String isRevised) {
		this.isRevised = isRevised;
	}

	/**
	 * @return the valStatus
	 */
	public int getValStatus() {
		return valStatus;
	}

	/**
	 * @param valStatus the valStatus to set
	 */
	public void setValStatus(int valStatus) {
		this.valStatus = valStatus;
	}

	/**
	 * @return the valResult
	 */
	public String getValResult() {
		return valResult;
	}

	/**
	 * @param valResult the valResult to set
	 */
	public void setValResult(String valResult) {
		this.valResult = valResult;
	}

	/**
	 * @return the convertedFileName
	 */
	public String getConvertedFileName() {
		return convertedFileName;
	}

	/**
	 * @param convertedFileName the convertedFileName to set
	 */
	public void setConvertedFileName(String convertedFileName) {
		this.convertedFileName = convertedFileName;
	}

	/**
	 * @return the status
	 */
	public FilingStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(FilingStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ElementAudit [elementAuditId=" + elementAuditId + ", fileDetails=" + fileDetails + ", elementCode=" + elementCode + ", elementVersion=" + elementVersion + ", rowCount=" + rowCount + ", isPushedToLnd=" + isPushedToLnd + ", eleReturnRef=" + eleReturnRef + ", isRevised=" + isRevised + ", valStatus=" + valStatus + ", valResult=" + valResult + ", convertedFileName=" + convertedFileName + ", returnPropertyVal=" + returnPropertyVal + ", status=" + status + "]";
	}

}
