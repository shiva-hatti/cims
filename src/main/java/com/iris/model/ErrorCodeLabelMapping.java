package com.iris.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This bean represents Error code Label and language mapping for NON-XBRL returns
 * 
 * @author apagaria
 */
@Entity
@Table(name = "TBL_ERROR_CODE_LABEL_MAPPING")
@JsonInclude(Include.NON_NULL)
public class ErrorCodeLabelMapping implements Serializable {

	private static final long serialVersionUID = -6305709526119023051L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ERROR_CODE_LABEL_MAPPING_ID")
	private Long errorCodeLabelMappingId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUAGE_ID_FK")
	private LanguageMaster languageIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ERROR_CODE_DETAIL_ID_FK")
	private ErrorCodeDetail errorCodeDetailIdFk;

	@Column(name = "ERROR_KEY_LABEL_FOR_FILE_BASED")
	private String errorKeyLabelForFileBased;

	@Column(name = "ERROR_KEY_LABEL_FOR_WEB_BASED")
	private String errorKeyLabelForWebBased;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	/**
	 * @return the errorCodeLabelMappingId
	 */
	public Long getErrorCodeLabelMappingId() {
		return errorCodeLabelMappingId;
	}

	/**
	 * @param errorCodeLabelMappingId the errorCodeLabelMappingId to set
	 */
	public void setErrorCodeLabelMappingId(Long errorCodeLabelMappingId) {
		this.errorCodeLabelMappingId = errorCodeLabelMappingId;
	}

	/**
	 * @return the languageIdFk
	 */
	public LanguageMaster getLanguageIdFk() {
		return languageIdFk;
	}

	/**
	 * @param languageIdFk the languageIdFk to set
	 */
	public void setLanguageIdFk(LanguageMaster languageIdFk) {
		this.languageIdFk = languageIdFk;
	}

	/**
	 * @return the errorCodeDetailIdFk
	 */
	public ErrorCodeDetail getErrorCodeDetailIdFk() {
		return errorCodeDetailIdFk;
	}

	/**
	 * @param errorCodeDetailIdFk the errorCodeDetailIdFk to set
	 */
	public void setErrorCodeDetailIdFk(ErrorCodeDetail errorCodeDetailIdFk) {
		this.errorCodeDetailIdFk = errorCodeDetailIdFk;
	}

	/**
	 * @return the errorKeyLabelForFileBased
	 */
	public String getErrorKeyLabelForFileBased() {
		return errorKeyLabelForFileBased;
	}

	/**
	 * @param errorKeyLabelForFileBased the errorKeyLabelForFileBased to set
	 */
	public void setErrorKeyLabelForFileBased(String errorKeyLabelForFileBased) {
		this.errorKeyLabelForFileBased = errorKeyLabelForFileBased;
	}

	/**
	 * @return the errorKeyLabelForWebBased
	 */
	public String getErrorKeyLabelForWebBased() {
		return errorKeyLabelForWebBased;
	}

	/**
	 * @param errorKeyLabelForWebBased the errorKeyLabelForWebBased to set
	 */
	public void setErrorKeyLabelForWebBased(String errorKeyLabelForWebBased) {
		this.errorKeyLabelForWebBased = errorKeyLabelForWebBased;
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

	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * @return the modifiedBy
	 */
	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the lastUpdatedOn
	 */
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * @param lastUpdatedOn the lastUpdatedOn to set
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	@Override
	public String toString() {
		return "ErrorCodeLabelMapping [errorCodeLabelMappingId=" + errorCodeLabelMappingId + ", languageIdFk=" + languageIdFk + ", errorCodeDetailIdFk=" + errorCodeDetailIdFk + ", errorKeyLabelForFileBased=" + errorKeyLabelForFileBased + ", errorKeyLabelForWebBased=" + errorKeyLabelForWebBased + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy + ", lastUpdatedOn=" + lastUpdatedOn + "]";
	}
}