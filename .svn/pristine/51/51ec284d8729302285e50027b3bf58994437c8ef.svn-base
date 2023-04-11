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
public class SubCategoryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6093920070898946888L;

	private Integer subCategoryId;

	private String subCategoryName;

	private String subCategoryCode;

	private List<EntityDto> entityDtoList;

	private Integer entityCount;

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			SubCategoryDto subCategoryDto = (SubCategoryDto) obj;
			if (subCategoryDto.getSubCategoryId().equals(this.getSubCategoryId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getSubCategoryId().hashCode();
	}

	/**
	 * @return the entityDtoList
	 */
	public List<EntityDto> getEntityDtoList() {
		return entityDtoList;
	}

	/**
	 * @param entityDtoList the entityDtoList to set
	 */
	public void setEntityDtoList(List<EntityDto> entityDtoList) {
		this.entityDtoList = entityDtoList;
	}

	/**
	 * @return the subCategoryCode
	 */
	public String getSubCategoryCode() {
		return subCategoryCode;
	}

	/**
	 * @param subCategoryCode the subCategoryCode to set
	 */
	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}

	/**
	 * @return the subCategoryId
	 */
	public Integer getSubCategoryId() {
		return subCategoryId;
	}

	/**
	 * @param subCategoryId the subCategoryId to set
	 */
	public void setSubCategoryId(Integer subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	/**
	 * @return the subCategoryName
	 */
	public String getSubCategoryName() {
		return subCategoryName;
	}

	/**
	 * @param subCategoryName the subCategoryName to set
	 */
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	/**
	 * @return the entityCount
	 */
	public Integer getEntityCount() {
		return entityCount;
	}

	/**
	 * @param entityCount the entityCount to set
	 */
	public void setEntityCount(Integer entityCount) {
		this.entityCount = entityCount;
	}

}
