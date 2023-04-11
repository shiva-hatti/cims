/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author apagaria
 *
 */
@JsonInclude(Include.NON_NULL)
public class SdmxReturnPreviewDetailJsonBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean isDataPointMapped;

	private Boolean isFormulaValidated;

	private Boolean isFusionDataInserted;

	private Boolean otherDetailedStored;

	private Boolean csvConversion;

	private Boolean recordStatus;

	private FusionDetailBeanHistory fusionDetailBeanHistory;

	private String errorCode;

	private String errorMessage;

	/**
	 * @return the isDataPointMapped
	 */
	public Boolean getIsDataPointMapped() {
		return isDataPointMapped;
	}

	/**
	 * @param isDataPointMapped the isDataPointMapped to set
	 */
	public void setIsDataPointMapped(Boolean isDataPointMapped) {
		this.isDataPointMapped = isDataPointMapped;
	}

	/**
	 * @return the isFormulaValidated
	 */
	public Boolean getIsFormulaValidated() {
		return isFormulaValidated;
	}

	/**
	 * @param isFormulaValidated the isFormulaValidated to set
	 */
	public void setIsFormulaValidated(Boolean isFormulaValidated) {
		this.isFormulaValidated = isFormulaValidated;
	}

	/**
	 * @return the isFusionDataInserted
	 */
	public Boolean getIsFusionDataInserted() {
		return isFusionDataInserted;
	}

	/**
	 * @param isFusionDataInserted the isFusionDataInserted to set
	 */
	public void setIsFusionDataInserted(Boolean isFusionDataInserted) {
		this.isFusionDataInserted = isFusionDataInserted;
	}

	/**
	 * @return the otherDetailedStored
	 */
	public Boolean getOtherDetailedStored() {
		return otherDetailedStored;
	}

	/**
	 * @param otherDetailedStored the otherDetailedStored to set
	 */
	public void setOtherDetailedStored(Boolean otherDetailedStored) {
		this.otherDetailedStored = otherDetailedStored;
	}

	/**
	 * @return the csvConversion
	 */
	public Boolean getCsvConversion() {
		return csvConversion;
	}

	/**
	 * @param csvConversion the csvConversion to set
	 */
	public void setCsvConversion(Boolean csvConversion) {
		this.csvConversion = csvConversion;
	}

	/**
	 * @return the recordStatus
	 */
	public Boolean getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @param recordStatus the recordStatus to set
	 */
	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @return the fusionDetailBeanHistory
	 */
	public FusionDetailBeanHistory getFusionDetailBeanHistory() {
		return fusionDetailBeanHistory;
	}

	/**
	 * @param fusionDetailBeanHistory the fusionDetailBeanHistory to set
	 */
	public void setFusionDetailBeanHistory(FusionDetailBeanHistory fusionDetailBeanHistory) {
		this.fusionDetailBeanHistory = fusionDetailBeanHistory;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
