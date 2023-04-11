/**
 * 
 */
package com.iris.sdmx.exceltohtml.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.UserMaster;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_RETURN_PREVIEW_HISTORY")
@JsonInclude(Include.NON_NULL)
public class SdmxReturnPreviewHistoryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_PREVIEW_HISTORY_ID")
	private Long returnPreviewHistoryId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_PREVIEW_ID_FK")
	private SdmxReturnPreviewEntity sdmxReturnPreviewIdFk;

	@Column(name = "OTHER_DETAIL_JSON")
	private String otherDetailJson;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "IS_SUCCESS_RECORD")
	private Boolean isSuccessRecord;

	/**
	 * 
	 */
	public SdmxReturnPreviewHistoryEntity() {

	}

	/**
	 * @return the returnPreviewHistoryId
	 */
	public Long getReturnPreviewHistoryId() {
		return returnPreviewHistoryId;
	}

	/**
	 * @param returnPreviewHistoryId the returnPreviewHistoryId to set
	 */
	public void setReturnPreviewHistoryId(Long returnPreviewHistoryId) {
		this.returnPreviewHistoryId = returnPreviewHistoryId;
	}

	/**
	 * @return the sdmxReturnPreviewIdFk
	 */
	public SdmxReturnPreviewEntity getSdmxReturnPreviewIdFk() {
		return sdmxReturnPreviewIdFk;
	}

	/**
	 * @param sdmxReturnPreviewIdFk the sdmxReturnPreviewIdFk to set
	 */
	public void setSdmxReturnPreviewIdFk(SdmxReturnPreviewEntity sdmxReturnPreviewIdFk) {
		this.sdmxReturnPreviewIdFk = sdmxReturnPreviewIdFk;
	}

	/**
	 * @return the otherDetailJson
	 */
	public String getOtherDetailJson() {
		return otherDetailJson;
	}

	/**
	 * @param otherDetailJson the otherDetailJson to set
	 */
	public void setOtherDetailJson(String otherDetailJson) {
		this.otherDetailJson = otherDetailJson;
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
	 * @return the isSuccessRecord
	 */
	public Boolean getIsSuccessRecord() {
		return isSuccessRecord;
	}

	/**
	 * @param isSuccessRecord the isSuccessRecord to set
	 */
	public void setIsSuccessRecord(Boolean isSuccessRecord) {
		this.isSuccessRecord = isSuccessRecord;
	}

}
