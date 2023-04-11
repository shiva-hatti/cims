/**
 * 
 */
package com.iris.sdmx.element.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.Regulator;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_ELEMENT")
@JsonInclude(Include.NON_NULL)
public class SdmxElementEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ELEMENT_ID")
	private Long elementId;

	@Column(name = "DSD_CODE")
	@Size(min = 1, max = 50)
	private String dsdCode;

	@Column(name = "ELEMENT_LABEL")
	@Size(min = 1, max = 2000)
	private String elementLabel;

	@Column(name = "ELEMENT_VER")
	private String elementVer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSIFICATION_ID_FK")
	private SdmxElementClassificationEntity classificationIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPENDENCY_TYPE_ID_FK")
	private SdmxElementDependencyTypeEntity dependencyTypeIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FLOW_TYPE_ID_FK")
	private SdmxElementFlowTypeEntity flowTypeIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NATURE_ID_FK")
	private SdmxElementNatureEntity natureIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SECTOR_ID_FK")
	private SdmxElementSectorEntity sectorIdFk;

	@Column(name = "ELEMENT_DESC")
	@Size(min = 1, max = 10000)
	private String elementDesc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ELEMENT_ID_FK")
	private SdmxElementEntity parentElementIdFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_BY")
	private UserMaster modifyBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_ON")
	private Date modifyOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGULATOR_OWNER_ID_FK")
	private Regulator regulatorOwnerIdFk;

	@OneToMany(mappedBy = "elementIdFk", fetch = FetchType.LAZY)
	private Set<SdmxModelCodesEntity> modelCodeSet;

	@Column(name = "IS_PENDING")
	private Boolean isPending;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENCY_MASTER_ID_FK")
	private AgencyMaster agencyMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USAGE_ID_FK")
	private SdmxElementUsageEntity usageIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FREQUENCY_ID_FK")
	private SdmxElementFrequencyEntity frequencyIdFk;

	/**
	 * 
	 */
	public SdmxElementEntity() {
	}

	/**
	 * @param elementId
	 */
	public SdmxElementEntity(Long elementId) {
		this.elementId = elementId;
	}

	/**
	 * @return the elementId
	 */
	public Long getElementId() {
		return elementId;
	}

	/**
	 * @param elementId the elementId to set
	 */
	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	/**
	 * @return the dsdCode
	 */
	public String getDsdCode() {
		return dsdCode;
	}

	/**
	 * @param dsdCode the dsdCode to set
	 */
	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	/**
	 * @return the elementLabel
	 */
	public String getElementLabel() {
		return elementLabel;
	}

	/**
	 * @param elementLabel the elementLabel to set
	 */
	public void setElementLabel(String elementLabel) {
		this.elementLabel = elementLabel;
	}

	/**
	 * @return the elementVer
	 */
	public String getElementVer() {
		return elementVer;
	}

	/**
	 * @param elementVer the elementVer to set
	 */
	public void setElementVer(String elementVer) {
		this.elementVer = elementVer;
	}

	/**
	 * @return the classificationIdFk
	 */
	public SdmxElementClassificationEntity getClassificationIdFk() {
		return classificationIdFk;
	}

	/**
	 * @param classificationIdFk the classificationIdFk to set
	 */
	public void setClassificationIdFk(SdmxElementClassificationEntity classificationIdFk) {
		this.classificationIdFk = classificationIdFk;
	}

	/**
	 * @return the dependencyTypeIdFk
	 */
	public SdmxElementDependencyTypeEntity getDependencyTypeIdFk() {
		return dependencyTypeIdFk;
	}

	/**
	 * @param dependencyTypeIdFk the dependencyTypeIdFk to set
	 */
	public void setDependencyTypeIdFk(SdmxElementDependencyTypeEntity dependencyTypeIdFk) {
		this.dependencyTypeIdFk = dependencyTypeIdFk;
	}

	/**
	 * @return the flowTypeIdFk
	 */
	public SdmxElementFlowTypeEntity getFlowTypeIdFk() {
		return flowTypeIdFk;
	}

	/**
	 * @param flowTypeIdFk the flowTypeIdFk to set
	 */
	public void setFlowTypeIdFk(SdmxElementFlowTypeEntity flowTypeIdFk) {
		this.flowTypeIdFk = flowTypeIdFk;
	}

	/**
	 * @return the natureIdFk
	 */
	public SdmxElementNatureEntity getNatureIdFk() {
		return natureIdFk;
	}

	/**
	 * @param natureIdFk the natureIdFk to set
	 */
	public void setNatureIdFk(SdmxElementNatureEntity natureIdFk) {
		this.natureIdFk = natureIdFk;
	}

	/**
	 * @return the sectorIdFk
	 */
	public SdmxElementSectorEntity getSectorIdFk() {
		return sectorIdFk;
	}

	/**
	 * @param sectorIdFk the sectorIdFk to set
	 */
	public void setSectorIdFk(SdmxElementSectorEntity sectorIdFk) {
		this.sectorIdFk = sectorIdFk;
	}

	/**
	 * @return the elementDesc
	 */
	public String getElementDesc() {
		return elementDesc;
	}

	/**
	 * @param elementDesc the elementDesc to set
	 */
	public void setElementDesc(String elementDesc) {
		this.elementDesc = elementDesc;
	}

	/**
	 * @return the parentElementIdFk
	 */
	public SdmxElementEntity getParentElementIdFk() {
		return parentElementIdFk;
	}

	/**
	 * @param parentElementIdFk the parentElementIdFk to set
	 */
	public void setParentElementIdFk(SdmxElementEntity parentElementIdFk) {
		this.parentElementIdFk = parentElementIdFk;
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
	 * @return the modifyBy
	 */
	public UserMaster getModifyBy() {
		return modifyBy;
	}

	/**
	 * @param modifyBy the modifyBy to set
	 */
	public void setModifyBy(UserMaster modifyBy) {
		this.modifyBy = modifyBy;
	}

	/**
	 * @return the modifyOn
	 */
	public Date getModifyOn() {
		return modifyOn;
	}

	/**
	 * @param modifyOn the modifyOn to set
	 */
	public void setModifyOn(Date modifyOn) {
		this.modifyOn = modifyOn;
	}

	/**
	 * @return the regulatorOwnerIdFk
	 */
	public Regulator getRegulatorOwnerIdFk() {
		return regulatorOwnerIdFk;
	}

	/**
	 * @param regulatorOwnerIdFk the regulatorOwnerIdFk to set
	 */
	public void setRegulatorOwnerIdFk(Regulator regulatorOwnerIdFk) {
		this.regulatorOwnerIdFk = regulatorOwnerIdFk;
	}

	/**
	 * @return the isPending
	 */
	public Boolean getIsPending() {
		return isPending;
	}

	/**
	 * @param isPending the isPending to set
	 */
	public void setIsPending(Boolean isPending) {
		this.isPending = isPending;
	}

	/**
	 * @return the agencyMaster
	 */
	public AgencyMaster getAgencyMaster() {
		return agencyMaster;
	}

	/**
	 * @param agencyMaster the agencyMaster to set
	 */
	public void setAgencyMaster(AgencyMaster agencyMaster) {
		this.agencyMaster = agencyMaster;
	}

	/**
	 *
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classificationIdFk == null) ? 0 : classificationIdFk.hashCode());
		result = prime * result + ((dependencyTypeIdFk == null) ? 0 : dependencyTypeIdFk.hashCode());
		result = prime * result + ((dsdCode == null) ? 0 : dsdCode.hashCode());
		result = prime * result + ((elementDesc == null) ? 0 : elementDesc.hashCode());
		result = prime * result + ((elementLabel == null) ? 0 : elementLabel.hashCode());
		result = prime * result + ((flowTypeIdFk == null) ? 0 : flowTypeIdFk.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((natureIdFk == null) ? 0 : natureIdFk.hashCode());
		result = prime * result + ((parentElementIdFk == null) ? 0 : parentElementIdFk.hashCode());
		result = prime * result + ((sectorIdFk == null) ? 0 : sectorIdFk.hashCode());
		result = prime * result + ((usageIdFk == null) ? 0 : usageIdFk.hashCode());
		result = prime * result + ((frequencyIdFk == null) ? 0 : frequencyIdFk.hashCode());
		return result;
	}

	/**
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SdmxElementEntity other = (SdmxElementEntity) obj;
		if (classificationIdFk == null) {
			if (other.classificationIdFk != null)
				return false;
		} else if (!classificationIdFk.equals(other.classificationIdFk))
			return false;
		if (dependencyTypeIdFk == null) {
			if (other.dependencyTypeIdFk != null)
				return false;
		} else if (!dependencyTypeIdFk.equals(other.dependencyTypeIdFk))
			return false;
		if (dsdCode == null) {
			if (other.dsdCode != null)
				return false;
		} else if (!dsdCode.equals(other.dsdCode))
			return false;
		if (elementDesc == null) {
			if (other.elementDesc != null)
				return false;
		} else if (!elementDesc.equals(other.elementDesc))
			return false;
		if (elementLabel == null) {
			if (other.elementLabel != null)
				return false;
		} else if (!elementLabel.equals(other.elementLabel))
			return false;
		if (flowTypeIdFk == null) {
			if (other.flowTypeIdFk != null)
				return false;
		} else if (!flowTypeIdFk.equals(other.flowTypeIdFk))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (natureIdFk == null) {
			if (other.natureIdFk != null)
				return false;
		} else if (!natureIdFk.equals(other.natureIdFk))
			return false;
		if (parentElementIdFk == null) {
			if (other.parentElementIdFk != null)
				return false;
		} else if (!parentElementIdFk.equals(other.parentElementIdFk))
			return false;
		if (sectorIdFk == null) {
			if (other.sectorIdFk != null)
				return false;
		} else if (!sectorIdFk.equals(other.sectorIdFk))
			return false;
		if (usageIdFk == null) {
			if (other.usageIdFk != null)
				return false;
		} else if (!usageIdFk.equals(other.usageIdFk))
			return false;
		if (frequencyIdFk == null) {
			if (other.frequencyIdFk != null)
				return false;
		} else if (!frequencyIdFk.equals(other.frequencyIdFk))
			return false;
		return true;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxElementEntity [elementId=" + elementId + ", dsdCode=" + dsdCode + ", elementLabel=" + elementLabel + ", elementVer=" + elementVer + ", classificationIdFk=" + classificationIdFk + ", dependencyTypeIdFk=" + dependencyTypeIdFk + ", flowTypeIdFk=" + flowTypeIdFk + ", natureIdFk=" + natureIdFk + ", sectorIdFk=" + sectorIdFk + ", elementDesc=" + elementDesc + ", parentElementIdFk=" + parentElementIdFk + ", isActive=" + isActive + ", lastUpdatedOn=" + lastUpdatedOn + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifyBy=" + modifyBy + ", modifyOn=" + modifyOn + ", regulatorOwnerIdFk=" + regulatorOwnerIdFk + "]";
	}

	public SdmxElementEntity(Long elementId, Boolean isActive, Boolean isPending) {
		super();
		this.elementId = elementId;
		this.isActive = isActive;
		this.isPending = isPending;
	}

	/**
	 * @return the usageIdFk
	 */
	public SdmxElementUsageEntity getUsageIdFk() {
		return usageIdFk;
	}

	/**
	 * @param usageIdFk the usageIdFk to set
	 */
	public void setUsageIdFk(SdmxElementUsageEntity usageIdFk) {
		this.usageIdFk = usageIdFk;
	}

	/**
	 * @return the frequencyIdFk
	 */
	public SdmxElementFrequencyEntity getFrequencyIdFk() {
		return frequencyIdFk;
	}

	/**
	 * @param frequencyIdFk the frequencyIdFk to set
	 */
	public void setFrequencyIdFk(SdmxElementFrequencyEntity frequencyIdFk) {
		this.frequencyIdFk = frequencyIdFk;
	}

}
