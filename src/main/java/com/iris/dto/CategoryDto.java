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
public class CategoryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1459888893457877184L;

	private Integer categoryId;

	private String categoryName;

	private String categoryCode;

	private List<SubCategoryDto> subCategory;

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			CategoryDto categoryDto = (CategoryDto) obj;
			if (categoryDto.getCategoryId().equals(this.getCategoryId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getCategoryId().hashCode();
	}

	/**
	 * @return the subCategory
	 */
	public List<SubCategoryDto> getSubCategory() {
		return subCategory;
	}

	/**
	 * @param subCategory the subCategory to set
	 */
	public void setSubCategory(List<SubCategoryDto> subCategory) {
		this.subCategory = subCategory;
	}

	/**
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * @return the categoryId
	 */
	public Integer getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
