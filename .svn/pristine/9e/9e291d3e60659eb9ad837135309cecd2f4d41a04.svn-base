/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author sajadhav
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class RegulatorDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1020241028404950411L;

	private Integer regulatorId;

	private String regulatorCode;

	private String regulatorName;

	private List<ReturnDto> returnList;

	private int returnCount;

	@Override
	public int hashCode() {
		return regulatorCode.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (this.getClass() != obj.getClass()) {
				return false;
			} else {
				RegulatorDto returnGroupMappingDto = (RegulatorDto) obj;
				return returnGroupMappingDto.getRegulatorCode().equals((this.getRegulatorCode()));
			}
		}
	}

	/**
	 * @return the returnList
	 */
	public List<ReturnDto> getReturnList() {
		return returnList;
	}

	/**
	 * @param returnList the returnList to set
	 */
	public void setReturnList(List<ReturnDto> returnList) {
		this.returnList = returnList;
	}

	/**
	 * @return the regulatorId
	 */
	public Integer getRegulatorId() {
		return regulatorId;
	}

	/**
	 * @param regulatorId the regulatorId to set
	 */
	public void setRegulatorId(Integer regulatorId) {
		this.regulatorId = regulatorId;
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
	 * @return the returnCount
	 */
	public int getReturnCount() {
		return returnCount;
	}

	/**
	 * @param returnCount the returnCount to set
	 */
	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}

}
