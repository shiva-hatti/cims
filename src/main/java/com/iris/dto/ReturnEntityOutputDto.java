/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * @author apagaria
 *
 */
public class ReturnEntityOutputDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6070641444793661047L;

	private Long returnId;

	private String returnCode;

	private String returnName;

	private ReturnEntityGroupDto returnEntityGroupDto;

	private ReturnEntityFreqDto returnEntityFreqDto;

	private Set<ReturnEntityDto> returnEntityDtoSet;

	private Long regulatorId;

	private String regulatorCode;

	private String regulatorName;

	private Boolean isNonXbrl;

	private Boolean isApplicableForDynamicWebForm;

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnName
	 */
	public String getReturnName() {
		return returnName;
	}

	/**
	 * @param returnName the returnName to set
	 */
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	/**
	 * @return the returnEntityGroupDto
	 */
	public ReturnEntityGroupDto getReturnEntityGroupDto() {
		return returnEntityGroupDto;
	}

	/**
	 * @param returnEntityGroupDto the returnEntityGroupDto to set
	 */
	public void setReturnEntityGroupDto(ReturnEntityGroupDto returnEntityGroupDto) {
		this.returnEntityGroupDto = returnEntityGroupDto;
	}

	/**
	 * @return the returnEntityFreqDto
	 */
	public ReturnEntityFreqDto getReturnEntityFreqDto() {
		return returnEntityFreqDto;
	}

	/**
	 * @param returnEntityFreqDto the returnEntityFreqDto to set
	 */
	public void setReturnEntityFreqDto(ReturnEntityFreqDto returnEntityFreqDto) {
		this.returnEntityFreqDto = returnEntityFreqDto;
	}

	/**
	 * @return the returnEntityDtoSet
	 */
	public Set<ReturnEntityDto> getReturnEntityDtoSet() {
		return returnEntityDtoSet;
	}

	/**
	 * @param returnEntityDtoSet the returnEntityDtoSet to set
	 */
	public void setReturnEntityDtoSet(Set<ReturnEntityDto> returnEntityDtoSet) {
		this.returnEntityDtoSet = returnEntityDtoSet;
	}

	/**
	 * @return the regulatorId
	 */
	public Long getRegulatorId() {
		return regulatorId;
	}

	/**
	 * @param regulatorId the regulatorId to set
	 */
	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	/**
	 * @return the regulatorCode
	 */
	public String getRegulatorCode() {
		return regulatorCode;
	}

	/**
	 * @param regulatorCode the regulatorCode to set
	 */
	public void setRegulatorCode(String regulatorCode) {
		this.regulatorCode = regulatorCode;
	}

	/**
	 * @return the regulatorName
	 */
	public String getRegulatorName() {
		return regulatorName;
	}

	/**
	 * @param regulatorName the regulatorName to set
	 */
	public void setRegulatorName(String regulatorName) {
		this.regulatorName = regulatorName;
	}

	/**
	 * @return the isNonXbrl
	 */
	public Boolean getIsNonXbrl() {
		return isNonXbrl;
	}

	/**
	 * @param isNonXbrl the isNonXbrl to set
	 */
	public void setIsNonXbrl(Boolean isNonXbrl) {
		this.isNonXbrl = isNonXbrl;
	}

	/**
	 * @return the isApplicableForDynamicWebForm
	 */
	public Boolean getIsApplicableForDynamicWebForm() {
		return isApplicableForDynamicWebForm;
	}

	/**
	 * @param isApplicableForDynamicWebForm the isApplicableForDynamicWebForm to set
	 */
	public void setIsApplicableForDynamicWebForm(Boolean isApplicableForDynamicWebForm) {
		this.isApplicableForDynamicWebForm = isApplicableForDynamicWebForm;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "ReturnEntityOutputDto [returnId=" + returnId + ", returnCode=" + returnCode + ", returnName=" + returnName + ", returnEntityGroupDto=" + returnEntityGroupDto + ", returnEntityFreqDto=" + returnEntityFreqDto + ", returnEntityDtoSet=" + returnEntityDtoSet + "]";
	}

}
