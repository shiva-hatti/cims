/**
 * 
 */
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

import com.iris.sdmx.status.entity.AdminStatus;

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_RETURN_ENTITY_CHANNEL_MAP_APPROVAL")
public class EntityReturnChanMapApproval implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2374664582636132207L;

	/**
	 * 
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RET_ENT_CHAN_MAP_APP_ID")
	private Long returnEntityChanMapAppId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAT_ID_FK")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUB_CAT_ID_FK")
	private SubCategory subCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entity;

	@Column(name = "RETURN_ENTITY_CHANNEL_MAP_JSON")
	private String returnEntityChanMapJson;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADMIN_STATUS_ID_FK")
	private AdminStatus approvalStatus;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVED_BY_FK")
	private UserMaster approvedBy;

	@Column(name = "APPROVED_ON")
	private Date approvedOn;

	@Column(name = "COMMENT")
	private String comment;

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

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the returnEntityChanMapAppId
	 */
	public Long getReturnEntityChanMapAppId() {
		return returnEntityChanMapAppId;
	}

	/**
	 * @param returnEntityChanMapAppId the returnEntityChanMapAppId to set
	 */
	public void setReturnEntityChanMapAppId(Long returnEntityChanMapAppId) {
		this.returnEntityChanMapAppId = returnEntityChanMapAppId;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the subCategory
	 */
	public SubCategory getSubCategory() {
		return subCategory;
	}

	/**
	 * @param subCategory the subCategory to set
	 */
	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	/**
	 * @return the entity
	 */
	public EntityBean getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(EntityBean entity) {
		this.entity = entity;
	}

	/**
	 * @return the returnEntityChanMapJson
	 */
	public String getReturnEntityChanMapJson() {
		return returnEntityChanMapJson;
	}

	/**
	 * @param returnEntityChanMapJson the returnEntityChanMapJson to set
	 */
	public void setReturnEntityChanMapJson(String returnEntityChanMapJson) {
		this.returnEntityChanMapJson = returnEntityChanMapJson;
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
	 * @return the approvalStatus
	 */
	public AdminStatus getApprovalStatus() {
		return approvalStatus;
	}

	/**
	 * @param approvalStatus the approvalStatus to set
	 */
	public void setApprovalStatus(AdminStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	/**
	 * @return the approvedBy
	 */
	public UserMaster getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(UserMaster approvedBy) {
		this.approvedBy = approvedBy;
	}

	/**
	 * @return the approvedOn
	 */
	public Date getApprovedOn() {
		return approvedOn;
	}

	/**
	 * @param approvedOn the approvedOn to set
	 */
	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

}
