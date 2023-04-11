/**
 * 
 */
package com.iris.sdmx.dimesnsion.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.dto.ReturnDto;
import com.iris.sdmx.agency.master.bean.SdmxAgencyMasterBean;
import com.iris.sdmx.codelist.bean.CodeListMasterBean;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.elementdimensionmapping.bean.AttachmentBean;

/**
 * @author sajadhav
 *
 */

@JsonInclude(Include.NON_DEFAULT)
public class DimensionMasterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5554517917510594581L;

	private Long dimensionId;

	private String dimensionCode;

	private String dimensionName;

	private DimensionMasterBean parentDimensionBean;

	private String dimensionType;

	private Long dimensionTypeId;

	private CodeListMasterBean codeListMasterBean;

	private String dimensionDesc;

	private Boolean isActive;

	private List<DimensionMasterBean> dimesnionMasterBeans;

	private Long userId;

	private Long roleId;

	private String langCode;

	private int actionId;

	private RegexBean regEx;

	private Integer minLength;

	private Integer maxLength;

	private Boolean isCommon;

	private Boolean isMandatory;

	private String dataType;

	private AttachmentBean attachmentBean;

	private List<SdmxElementBean> elements;

	private List<ReturnDto> modelMapping;

	private Date lastUpdatedOn;

	private Long lastUpdatedOnInLong;

	private String agencyMasterCode;
	private Boolean addApproval;

	private Boolean editApproval;

	private SdmxAgencyMasterBean sdmxAgencyMasterBean;

	private String conceptVersion;

	public Long getLastUpdatedOnInLong() {
		return lastUpdatedOnInLong;
	}

	public void setLastUpdatedOnInLong(Long lastUpdatedOnInLong) {
		this.lastUpdatedOnInLong = lastUpdatedOnInLong;
	}

	/**
	 * @return the modelMapping
	 */
	public List<ReturnDto> getModelMapping() {
		return modelMapping;
	}

	/**
	 * @param modelMapping the modelMapping to set
	 */
	public void setModelMapping(List<ReturnDto> modelMapping) {
		this.modelMapping = modelMapping;
	}

	/**
	 * @return the elements
	 */
	public List<SdmxElementBean> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<SdmxElementBean> elements) {
		this.elements = elements;
	}

	/**
	 * @return the minLength
	 */
	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the regEx
	 */
	public RegexBean getRegEx() {
		return regEx;
	}

	/**
	 * @param regEx the regEx to set
	 */
	public void setRegEx(RegexBean regEx) {
		this.regEx = regEx;
	}

	/**
	 * @return the actionId
	 */
	public int getActionId() {
		return actionId;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return langCode;
	}

	/**
	 * @param langCode the langCode to set
	 */
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	/**
	 * @return the dimensionTypeId
	 */
	public Long getDimensionTypeId() {
		return dimensionTypeId;
	}

	/**
	 * @param dimensionTypeId the dimensionTypeId to set
	 */
	public void setDimensionTypeId(Long dimensionTypeId) {
		this.dimensionTypeId = dimensionTypeId;
	}

	/**
	 * @return the dimensionId
	 */
	public Long getDimensionId() {
		return dimensionId;
	}

	/**
	 * @param dimensionId the dimensionId to set
	 */
	public void setDimensionId(Long dimensionId) {
		this.dimensionId = dimensionId;
	}

	/**
	 * @return the dimensionName
	 */
	public String getDimensionName() {
		return dimensionName;
	}

	/**
	 * @param dimensionName the dimensionName to set
	 */
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}

	/**
	 * @return the dimensionCode
	 */
	public String getDimensionCode() {
		return dimensionCode;
	}

	/**
	 * @param dimensionCode the dimensionCode to set
	 */
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}

	/**
	 * @return the dimensionType
	 */
	public String getDimensionType() {
		return dimensionType;
	}

	/**
	 * @param dimensionType the dimensionType to set
	 */
	public void setDimensionType(String dimensionType) {
		this.dimensionType = dimensionType;
	}

	/**
	 * @return the dimensionDesc
	 */
	public String getDimensionDesc() {
		return dimensionDesc;
	}

	/**
	 * @param dimensionDesc the dimensionDesc to set
	 */
	public void setDimensionDesc(String dimensionDesc) {
		this.dimensionDesc = dimensionDesc;
	}

	/**
	 * @return the codeListMasterBean
	 */
	public CodeListMasterBean getCodeListMasterBean() {
		return codeListMasterBean;
	}

	/**
	 * @param codeListMasterBean the codeListMasterBean to set
	 */
	public void setCodeListMasterBean(CodeListMasterBean codeListMasterBean) {
		this.codeListMasterBean = codeListMasterBean;
	}

	/**
	 * @return the dimesnionMasterBeans
	 */
	public List<DimensionMasterBean> getDimesnionMasterBeans() {
		return dimesnionMasterBeans;
	}

	/**
	 * @param dimesnionMasterBeans the dimesnionMasterBeans to set
	 */
	public void setDimesnionMasterBeans(List<DimensionMasterBean> dimesnionMasterBeans) {
		this.dimesnionMasterBeans = dimesnionMasterBeans;
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
	 * @return the parentDimensionBean
	 */
	public DimensionMasterBean getParentDimensionBean() {
		return parentDimensionBean;
	}

	/**
	 * @param parentDimensionBean the parentDimensionBean to set
	 */
	public void setParentDimensionBean(DimensionMasterBean parentDimensionBean) {
		this.parentDimensionBean = parentDimensionBean;
	}

	public Boolean getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(Boolean isCommon) {
		this.isCommon = isCommon;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the attachmentBean
	 */
	public AttachmentBean getAttachmentBean() {
		return attachmentBean;
	}

	/**
	 * @param attachmentBean the attachmentBean to set
	 */
	public void setAttachmentBean(AttachmentBean attachmentBean) {
		this.attachmentBean = attachmentBean;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * @return the addApproval
	 */
	public Boolean getAddApproval() {
		return addApproval;
	}

	/**
	 * @param addApproval the addApproval to set
	 */
	public void setAddApproval(Boolean addApproval) {
		this.addApproval = addApproval;
	}

	/**
	 * @return the editApproval
	 */
	public Boolean getEditApproval() {
		return editApproval;
	}

	/**
	 * @param editApproval the editApproval to set
	 */
	public void setEditApproval(Boolean editApproval) {
		this.editApproval = editApproval;
	}

	/**
	 * @return the agencyMasterCode
	 */
	public String getAgencyMasterCode() {
		return agencyMasterCode;
	}

	/**
	 * @param agencyMasterCode the agencyMasterCode to set
	 */
	public void setAgencyMasterCode(String agencyMasterCode) {
		this.agencyMasterCode = agencyMasterCode;
	}

	/**
	 * @return the sdmxAgencyMasterBean
	 */
	public SdmxAgencyMasterBean getSdmxAgencyMasterBean() {
		return sdmxAgencyMasterBean;
	}

	/**
	 * @param sdmxAgencyMasterBean the sdmxAgencyMasterBean to set
	 */
	public void setSdmxAgencyMasterBean(SdmxAgencyMasterBean sdmxAgencyMasterBean) {
		this.sdmxAgencyMasterBean = sdmxAgencyMasterBean;
	}

	/**
	 * @return the conceptVersion
	 */
	public String getConceptVersion() {
		return conceptVersion;
	}

	/**
	 * @param conceptVersion the conceptVersion to set
	 */
	public void setConceptVersion(String conceptVersion) {
		this.conceptVersion = conceptVersion;
	}

}
