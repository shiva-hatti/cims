package com.iris.sdmx.sdmxDataModelCodesDownloadEntity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.databind.JsonNode;
import com.iris.model.UserMaster;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name = "TBL_SDMX_DOCUMENTS_DOWNLOAD")
@TypeDefs({ @TypeDef(name = "json", typeClass = JsonStringType.class) })
public class SdmxFilingDocumentsDownload implements Serializable {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = 2982332388678631564L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SDMX_DOCUMENT_DOWNLOAD_ID")
	private Long sdmxDocumentDownloadID;

	@Column(name = "TYPE_OF_REQUEST")
	private int typeOfRequest;

	@Column(name = "ELEMENT_COMBINATION")
	private String elementCombination;

	@Column(name = "RETURN_COMBINATION")
	private String returnCombination;

	@Column(name = "DOCUMENT_TYPE")
	private String documentType;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	/**
	 * @return the sdmxDocumentDownloadID
	 */
	public Long getSdmxDocumentDownloadID() {
		return sdmxDocumentDownloadID;
	}

	/**
	 * @param sdmxDocumentDownloadID the sdmxDocumentDownloadID to set
	 */
	public void setSdmxDocumentDownloadID(Long sdmxDocumentDownloadID) {
		this.sdmxDocumentDownloadID = sdmxDocumentDownloadID;
	}

	/**
	 * @return the typeOfRequest
	 */
	public int getTypeOfRequest() {
		return typeOfRequest;
	}

	/**
	 * @param typeOfRequest the typeOfRequest to set
	 */
	public void setTypeOfRequest(int typeOfRequest) {
		this.typeOfRequest = typeOfRequest;
	}

	/**
	 * @return the elementCombination
	 */
	public String getElementCombination() {
		return elementCombination;
	}

	/**
	 * @param elementCombination the elementCombination to set
	 */
	public void setElementCombination(String elementCombination) {
		this.elementCombination = elementCombination;
	}

	/**
	 * @return the returnCombination
	 */
	public String getReturnCombination() {
		return returnCombination;
	}

	/**
	 * @param returnCombination the returnCombination to set
	 */
	public void setReturnCombination(String returnCombination) {
		this.returnCombination = returnCombination;
	}

	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType the documentType to set
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the createdBy
	 */
	public UserMaster getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

}
