package com.iris.model;

/**
 * @author Shivabasava Hatti
 *
 */
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

@Entity
@Table(name = "TBL_ROS_MASTER")
public class RosMaster implements Serializable {
	private static final long serialVersionUID = -9179125889769472395L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROS_MASTER_ID")
	private Long rosMasterId;

	@Column(name = "ENTITY_CODE")
	private String entityCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnObj;

	@Column(name = "ISM_INSTCD")
	private String instituteCode;

	@Column(name = "ACTIVE_FLG")
	private Boolean isActive;

	@Column(name = "INSTITUTE_NAME")
	private String instituteName;

	@Column(name = "INST_AREA_OPERATION")
	private String instituteAreaOperation;

	@Column(name = "ACTIVITY_CODE")
	private String activityCode;

	@Column(name = "ACTIVITY_NAME")
	private String activityName;

	@Column(name = "INST_CATEGORY")
	private String instituteCategory;

	@Column(name = "INST_SUBCATEGORY")
	private String instituteSubCategory;

	@Column(name = "INST_PRINCIPAL_REG")
	private String instituteRegulatorName;

	@Column(name = "INST_PLACE")
	private String institutePlace;

	@Column(name = "INST_DISTRICT")
	private String instituteDistrict;

	@Column(name = "INST_STATE")
	private String instituteState;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	/**
	 * @return the rosMasterId
	 */
	public Long getRosMasterId() {
		return rosMasterId;
	}

	/**
	 * @param rosMasterId the rosMasterId to set
	 */
	public void setRosMasterId(Long rosMasterId) {
		this.rosMasterId = rosMasterId;
	}

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @return the returnObj
	 */
	public Return getReturnObj() {
		return returnObj;
	}

	/**
	 * @param returnObj the returnObj to set
	 */
	public void setReturnObj(Return returnObj) {
		this.returnObj = returnObj;
	}

	/**
	 * @return the instituteCode
	 */
	public String getInstituteCode() {
		return instituteCode;
	}

	/**
	 * @param instituteCode the instituteCode to set
	 */
	public void setInstituteCode(String instituteCode) {
		this.instituteCode = instituteCode;
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
	 * @return the instituteName
	 */
	public String getInstituteName() {
		return instituteName;
	}

	/**
	 * @param instituteName the instituteName to set
	 */
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	/**
	 * @return the instituteAreaOperation
	 */
	public String getInstituteAreaOperation() {
		return instituteAreaOperation;
	}

	/**
	 * @param instituteAreaOperation the instituteAreaOperation to set
	 */
	public void setInstituteAreaOperation(String instituteAreaOperation) {
		this.instituteAreaOperation = instituteAreaOperation;
	}

	/**
	 * @return the activityCode
	 */
	public String getActivityCode() {
		return activityCode;
	}

	/**
	 * @param activityCode the activityCode to set
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * @return the instituteCategory
	 */
	public String getInstituteCategory() {
		return instituteCategory;
	}

	/**
	 * @param instituteCategory the instituteCategory to set
	 */
	public void setInstituteCategory(String instituteCategory) {
		this.instituteCategory = instituteCategory;
	}

	/**
	 * @return the instituteSubCategory
	 */
	public String getInstituteSubCategory() {
		return instituteSubCategory;
	}

	/**
	 * @param instituteSubCategory the instituteSubCategory to set
	 */
	public void setInstituteSubCategory(String instituteSubCategory) {
		this.instituteSubCategory = instituteSubCategory;
	}

	/**
	 * @return the instituteRegulatorName
	 */
	public String getInstituteRegulatorName() {
		return instituteRegulatorName;
	}

	/**
	 * @param instituteRegulatorName the instituteRegulatorName to set
	 */
	public void setInstituteRegulatorName(String instituteRegulatorName) {
		this.instituteRegulatorName = instituteRegulatorName;
	}

	/**
	 * @return the institutePlace
	 */
	public String getInstitutePlace() {
		return institutePlace;
	}

	/**
	 * @param institutePlace the institutePlace to set
	 */
	public void setInstitutePlace(String institutePlace) {
		this.institutePlace = institutePlace;
	}

	/**
	 * @return the instituteDistrict
	 */
	public String getInstituteDistrict() {
		return instituteDistrict;
	}

	/**
	 * @param instituteDistrict the instituteDistrict to set
	 */
	public void setInstituteDistrict(String instituteDistrict) {
		this.instituteDistrict = instituteDistrict;
	}

	/**
	 * @return the instituteState
	 */
	public String getInstituteState() {
		return instituteState;
	}

	/**
	 * @param instituteState the instituteState to set
	 */
	public void setInstituteState(String instituteState) {
		this.instituteState = instituteState;
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
}
