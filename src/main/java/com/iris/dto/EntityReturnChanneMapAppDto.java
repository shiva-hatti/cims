/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.listener.ApplicationStartListener;
import com.iris.util.AESV2;

/**
 * @author sajadhav
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class EntityReturnChanneMapAppDto implements Serializable {

	static final Logger LOGGER = LogManager.getLogger(EntityReturnChanneMapAppDto.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8720949784404033198L;

	private CategoryDto categoryDto;

	private SubCategoryDto subCategoryDto;

	private EntityDto entityDto;

	private String entityReturnChannelMapJson;

	private UserDto createdBy;

	private DataListDto returnChannelMapping;

	private Long entityReturnChanneMapAppId;

	private Long adminStatusId;

	private UserDto approvedBy;

	private Long createdOn;

	private String comment;

	public EntityReturnChanneMapAppDto(Long entityReturnChanneMapAppId) {
		this.entityReturnChanneMapAppId = entityReturnChanneMapAppId;
	}

	public EntityReturnChanneMapAppDto(Long entityReturnChanneMapAppId, Long categoryId, String catCode, String categoryName, Long subCatId, String subCatCode, String subCategoryName, Long entityId, String entityCode, String ifscCode, String entityName, String entityReturChannelJson, Long userId, String userName, Date createdOn) {
		this.categoryDto = new CategoryDto();
		this.categoryDto.setCategoryCode(catCode);
		this.categoryDto.setCategoryId(categoryId.intValue());
		this.categoryDto.setCategoryName(categoryName);

		this.subCategoryDto = new SubCategoryDto();
		this.subCategoryDto.setSubCategoryCode(subCatCode);
		this.subCategoryDto.setSubCategoryId(subCatId.intValue());
		this.subCategoryDto.setSubCategoryName(subCategoryName);

		this.entityDto = new EntityDto();
		this.entityDto.setEntityId(entityId);
		this.entityDto.setEntityCode(entityCode);
		this.entityDto.setEntityName(entityName);

		this.entityReturnChannelMapJson = entityReturChannelJson;
		this.createdBy = new UserDto();
		this.createdBy.setUserId(userId);
		try {
			this.createdBy.setUserName(AESV2.getInstance().decrypt(userName));
		} catch (Exception e) {
			LOGGER.error("Exception while decrypting username : " + userName);
		}

		if (createdOn != null) {
			this.createdOn = createdOn.getTime();
		}
		this.entityReturnChanneMapAppId = entityReturnChanneMapAppId;
	}

	public EntityReturnChanneMapAppDto() {

	}

	/**
	 * @return the categoryDto
	 */
	public CategoryDto getCategoryDto() {
		return categoryDto;
	}

	/**
	 * @param categoryDto the categoryDto to set
	 */
	public void setCategoryDto(CategoryDto categoryDto) {
		this.categoryDto = categoryDto;
	}

	/**
	 * @return the subCategoryDto
	 */
	public SubCategoryDto getSubCategoryDto() {
		return subCategoryDto;
	}

	/**
	 * @param subCategoryDto the subCategoryDto to set
	 */
	public void setSubCategoryDto(SubCategoryDto subCategoryDto) {
		this.subCategoryDto = subCategoryDto;
	}

	/**
	 * @return the entityDto
	 */
	public EntityDto getEntityDto() {
		return entityDto;
	}

	/**
	 * @param entityDto the entityDto to set
	 */
	public void setEntityDto(EntityDto entityDto) {
		this.entityDto = entityDto;
	}

	/**
	 * @return the entityReturnChannelMapJson
	 */
	public String getEntityReturnChannelMapJson() {
		return entityReturnChannelMapJson;
	}

	/**
	 * @param entityReturnChannelMapJson the entityReturnChannelMapJson to set
	 */
	public void setEntityReturnChannelMapJson(String entityReturnChannelMapJson) {
		this.entityReturnChannelMapJson = entityReturnChannelMapJson;
	}

	/**
	 * @return the createdBy
	 */
	public UserDto getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserDto createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the returnChannelMapping
	 */
	public DataListDto getReturnChannelMapping() {
		return returnChannelMapping;
	}

	/**
	 * @param returnChannelMapping the returnChannelMapping to set
	 */
	public void setReturnChannelMapping(DataListDto returnChannelMapping) {
		this.returnChannelMapping = returnChannelMapping;
	}

	/**
	 * @return the createdOn
	 */
	public Long getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the entityReturnChanneMapAppId
	 */
	public Long getEntityReturnChanneMapAppId() {
		return entityReturnChanneMapAppId;
	}

	/**
	 * @param entityReturnChanneMapAppId the entityReturnChanneMapAppId to set
	 */
	public void setEntityReturnChanneMapAppId(Long entityReturnChanneMapAppId) {
		this.entityReturnChanneMapAppId = entityReturnChanneMapAppId;
	}

	/**
	 * @return the adminStatusId
	 */
	public Long getAdminStatusId() {
		return adminStatusId;
	}

	/**
	 * @param adminStatusId the adminStatusId to set
	 */
	public void setAdminStatusId(Long adminStatusId) {
		this.adminStatusId = adminStatusId;
	}

	/**
	 * @return the approvedBy
	 */
	public UserDto getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(UserDto approvedBy) {
		this.approvedBy = approvedBy;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
