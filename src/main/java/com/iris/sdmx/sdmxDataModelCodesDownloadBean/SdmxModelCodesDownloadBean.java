package com.iris.sdmx.sdmxDataModelCodesDownloadBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iris.sdmx.element.bean.ElementListBean;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnPreviewBean;

public class SdmxModelCodesDownloadBean implements Serializable {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = 7771483442652351024L;

	private Long userId;

	private Long roleId;

	private String langCode;

	private String dataTemplateName;

	private String templateName;

	private String templateNameWithoutVersion;

	private String ebrVersion;

	private Long templateID;

	private Set<Long> returnIdList;

	private String returnName;

	private String returnCode;

	private Long returnPreviewTypeId;

	private Long elementID;

	private String elementCode;

	private List<Long> elementList;

	private int isTemplateView;

	private List<String> returnCodeList;

	private List<Long> returnPreviewTypeIdList;

	private List<ElementListBean> sdmxElementList;

	private Map<String, SdmxElementBean> independentElementDetails;

	private Map<String, SdmxElementBean> dependentElementDetails;

	private List<SdmxElementBean> independentElementList = new ArrayList<SdmxElementBean>();
	private List<SdmxElementBean> dependentElementList = new ArrayList<SdmxElementBean>();

	private List<SdmxModelCodesDownloadBean> independentTemplateList = new ArrayList<SdmxModelCodesDownloadBean>();
	private List<SdmxModelCodesDownloadBean> dependentTemplateList = new ArrayList<SdmxModelCodesDownloadBean>();

	private boolean checkForDataFilteration = false;

	private long returnID;

	private String rbrVersion;

	private long agencyID;

	//private List<SdmxElementBean> sdmxElementList;

	private List<String> eleCodeList;

	private List<SdmxReturnPreviewBean> sdmxReturnPreviewBeans;

	private String versionNumber;
	private Long returnTemplateId;
	private Integer returnCellRef;
	private String dsdCode;
	private Long elementId;
	private String modelCode;
	private String modelDim;
	private String elementVersion;

	public SdmxModelCodesDownloadBean(String returnCode, String versionNumber, Long returnTemplateId, Integer returnCellRef, Long elementId, String dsdCode, String modelCode, String modelDim) {
		this.returnCode = returnCode;
		this.versionNumber = versionNumber;
		this.returnTemplateId = returnTemplateId;
		this.returnCellRef = returnCellRef;
		this.dsdCode = dsdCode;
		this.elementId = elementId;
		this.modelCode = modelCode;
		this.modelDim = modelDim;

	}

	public SdmxModelCodesDownloadBean(String returnCode, String returnName, String ebrVersion, Long returnPreviewTypeId) {
		this.returnCode = returnCode;
		this.returnName = returnName;
		this.ebrVersion = ebrVersion;
		this.returnPreviewTypeId = returnPreviewTypeId;

	}

	public SdmxModelCodesDownloadBean(String elementCode, String elementVersion) {
		this.elementCode = elementCode;
		this.elementVersion = elementVersion;
	}

	public SdmxModelCodesDownloadBean() {
		// TODO Auto-generated constructor stub
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
	 * @return the dataTemplateName
	 */
	public String getDataTemplateName() {
		return dataTemplateName;
	}

	/**
	 * @param dataTemplateName the dataTemplateName to set
	 */
	public void setDataTemplateName(String dataTemplateName) {
		this.dataTemplateName = dataTemplateName;
	}

	/**
	 * @return the ebrVersion
	 */
	public String getEbrVersion() {
		return ebrVersion;
	}

	/**
	 * @param ebrVersion the ebrVersion to set
	 */
	public void setEbrVersion(String ebrVersion) {
		this.ebrVersion = ebrVersion;
	}

	/**
	 * @return the returnIdList
	 */
	public Set<Long> getReturnIdList() {
		return returnIdList;
	}

	/**
	 * @param returnIdList the returnIdList to set
	 */
	public void setReturnIdList(Set<Long> returnIdList) {
		this.returnIdList = returnIdList;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the templateID
	 */
	public Long getTemplateID() {
		return templateID;
	}

	/**
	 * @param templateID the templateID to set
	 */
	public void setTemplateID(Long templateID) {
		this.templateID = templateID;
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
	 * @return the returnPreviewTypeId
	 */
	public Long getReturnPreviewTypeId() {
		return returnPreviewTypeId;
	}

	/**
	 * @param returnPreviewTypeId the returnPreviewTypeId to set
	 */
	public void setReturnPreviewTypeId(Long returnPreviewTypeId) {
		this.returnPreviewTypeId = returnPreviewTypeId;
	}

	/**
	 * @return the elementID
	 */
	public Long getElementID() {
		return elementID;
	}

	/**
	 * @param elementID the elementID to set
	 */
	public void setElementID(Long elementID) {
		this.elementID = elementID;
	}

	/**
	 * @return the elementCode
	 */
	public String getElementCode() {
		return elementCode;
	}

	/**
	 * @param elementCode the elementCode to set
	 */
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}

	/**
	 * @return the elementList
	 */

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @return the elementList
	 */
	public List<Long> getElementList() {
		return elementList;
	}

	/**
	 * @param elementList the elementList to set
	 */
	public void setElementList(List<Long> elementList) {
		this.elementList = elementList;
	}

	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the returnTemplateId
	 */
	public Long getReturnTemplateId() {
		return returnTemplateId;
	}

	/**
	 * @param returnTemplateId the returnTemplateId to set
	 */
	public void setReturnTemplateId(Long returnTemplateId) {
		this.returnTemplateId = returnTemplateId;
	}

	/**
	 * @return the returnCellRef
	 */
	public Integer getReturnCellRef() {
		return returnCellRef;
	}

	/**
	 * @param returnCellRef the returnCellRef to set
	 */
	public void setReturnCellRef(Integer returnCellRef) {
		this.returnCellRef = returnCellRef;
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
	 * @return the modelCode
	 */
	public String getModelCode() {
		return modelCode;
	}

	/**
	 * @param modelCode the modelCode to set
	 */
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	/**
	 * @return the modelDim
	 */
	public String getModelDim() {
		return modelDim;
	}

	/**
	 * @param modelDim the modelDim to set
	 */
	public void setModelDim(String modelDim) {
		this.modelDim = modelDim;
	}

	/**
	 * @return the elementVersion
	 */
	public String getElementVersion() {
		return elementVersion;
	}

	/**
	 * @param elementVersion the elementVersion to set
	 */
	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
	}

	/**
	 * @return the isTemplateView
	 */
	public int getIsTemplateView() {
		return isTemplateView;
	}

	/**
	 * @param isTemplateView the isTemplateView to set
	 */
	public void setIsTemplateView(int isTemplateView) {
		this.isTemplateView = isTemplateView;
	}

	/**
	 * @return the returnCodeList
	 */
	public List<String> getReturnCodeList() {
		return returnCodeList;
	}

	/**
	 * @param returnCodeList the returnCodeList to set
	 */
	public void setReturnCodeList(List<String> returnCodeList) {
		this.returnCodeList = returnCodeList;
	}

	/**
	 * @return the returnPreviewTypeIdList
	 */
	public List<Long> getReturnPreviewTypeIdList() {
		return returnPreviewTypeIdList;
	}

	/**
	 * @param returnPreviewTypeIdList the returnPreviewTypeIdList to set
	 */
	public void setReturnPreviewTypeIdList(List<Long> returnPreviewTypeIdList) {
		this.returnPreviewTypeIdList = returnPreviewTypeIdList;
	}

	/**
	 * @return the eleCodeList
	 */
	public List<String> getEleCodeList() {
		return eleCodeList;
	}

	/**
	 * @param eleCodeList the eleCodeList to set
	 */
	public void setEleCodeList(List<String> eleCodeList) {
		this.eleCodeList = eleCodeList;
	}

	/**
	 * @return the sdmxElementList
	 */
	public List<ElementListBean> getSdmxElementList() {
		return sdmxElementList;
	}

	/**
	 * @param sdmxElementList the sdmxElementList to set
	 */
	public void setSdmxElementList(List<ElementListBean> sdmxElementList) {
		this.sdmxElementList = sdmxElementList;
	}

	/**
	 * @return the sdmxReturnPreviewBeans
	 */
	public List<SdmxReturnPreviewBean> getSdmxReturnPreviewBeans() {
		return sdmxReturnPreviewBeans;
	}

	/**
	 * @param sdmxReturnPreviewBeans the sdmxReturnPreviewBeans to set
	 */
	public void setSdmxReturnPreviewBeans(List<SdmxReturnPreviewBean> sdmxReturnPreviewBeans) {
		this.sdmxReturnPreviewBeans = sdmxReturnPreviewBeans;
	}

	/**
	 * @return the independentElementDetails
	 */
	public Map<String, SdmxElementBean> getIndependentElementDetails() {
		return independentElementDetails;
	}

	/**
	 * @param independentElementDetails the independentElementDetails to set
	 */
	public void setIndependentElementDetails(Map<String, SdmxElementBean> independentElementDetails) {
		this.independentElementDetails = independentElementDetails;
	}

	/**
	 * @return the dependentElementDetails
	 */
	public Map<String, SdmxElementBean> getDependentElementDetails() {
		return dependentElementDetails;
	}

	/**
	 * @param dependentElementDetails the dependentElementDetails to set
	 */
	public void setDependentElementDetails(Map<String, SdmxElementBean> dependentElementDetails) {
		this.dependentElementDetails = dependentElementDetails;
	}

	/**
	 * @return the independentElementList
	 */
	public List<SdmxElementBean> getIndependentElementList() {
		return independentElementList;
	}

	/**
	 * @param independentElementList the independentElementList to set
	 */
	public void setIndependentElementList(List<SdmxElementBean> independentElementList) {
		this.independentElementList = independentElementList;
	}

	/**
	 * @return the dependentElementList
	 */
	public List<SdmxElementBean> getDependentElementList() {
		return dependentElementList;
	}

	/**
	 * @param dependentElementList the dependentElementList to set
	 */
	public void setDependentElementList(List<SdmxElementBean> dependentElementList) {
		this.dependentElementList = dependentElementList;
	}

	/**
	 * @return the independentTemplateList
	 */
	public List<SdmxModelCodesDownloadBean> getIndependentTemplateList() {
		return independentTemplateList;
	}

	/**
	 * @param independentTemplateList the independentTemplateList to set
	 */
	public void setIndependentTemplateList(List<SdmxModelCodesDownloadBean> independentTemplateList) {
		this.independentTemplateList = independentTemplateList;
	}

	/**
	 * @return the dependentTemplateList
	 */
	public List<SdmxModelCodesDownloadBean> getDependentTemplateList() {
		return dependentTemplateList;
	}

	/**
	 * @param dependentTemplateList the dependentTemplateList to set
	 */
	public void setDependentTemplateList(List<SdmxModelCodesDownloadBean> dependentTemplateList) {
		this.dependentTemplateList = dependentTemplateList;
	}

	/**
	 * @return the templateNameWithoutVersion
	 */
	public String getTemplateNameWithoutVersion() {
		return templateNameWithoutVersion;
	}

	/**
	 * @param templateNameWithoutVersion the templateNameWithoutVersion to set
	 */
	public void setTemplateNameWithoutVersion(String templateNameWithoutVersion) {
		this.templateNameWithoutVersion = templateNameWithoutVersion;
	}

	/**
	 * @return the checkForDataFilteration
	 */
	public boolean isCheckForDataFilteration() {
		return checkForDataFilteration;
	}

	/**
	 * @param checkForDataFilteration the checkForDataFilteration to set
	 */
	public void setCheckForDataFilteration(boolean checkForDataFilteration) {
		this.checkForDataFilteration = checkForDataFilteration;
	}

	/**
	 * @return the returnID
	 */
	public long getReturnID() {
		return returnID;
	}

	/**
	 * @param returnID the returnID to set
	 */
	public void setReturnID(long returnID) {
		this.returnID = returnID;
	}

	/**
	 * @return the rbrVersion
	 */
	public String getRbrVersion() {
		return rbrVersion;
	}

	/**
	 * @param rbrVersion the rbrVersion to set
	 */
	public void setRbrVersion(String rbrVersion) {
		this.rbrVersion = rbrVersion;
	}

	/**
	 * @return the agencyID
	 */
	public long getAgencyID() {
		return agencyID;
	}

	/**
	 * @param agencyID the agencyID to set
	 */
	public void setAgencyID(long agencyID) {
		this.agencyID = agencyID;
	}

}
