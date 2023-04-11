package com.iris.sdmx.element.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.sdmx.dimesnsion.bean.DimensionMasterBean;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;
import com.iris.sdmx.element.bean.ElementApprovalOutputBean;
import com.iris.sdmx.element.bean.ElementListRequestBean;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.element.bean.SdmxElementBeanForTemp;
import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.sdmx.element.entity.SdmxElementTempEntity;
import com.iris.sdmx.element.repo.SdmxElementRepo;
import com.iris.sdmx.element.repo.SdmxElementTempRepo;
import com.iris.sdmx.userMangement.bean.ApprovalInputBean;
import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ErrorCode;
/**
 * @author vjadhav
 *
 */
import com.iris.util.constant.ErrorConstants;

@Service
@Transactional
public class ElementApprovalService implements GenericService<SdmxElementTempEntity, Long> {

	private static final Logger LOGGER = LogManager.getLogger(ElementApprovalService.class);

	@Autowired
	private SdmxElementTempRepo sdmxElementTempRepo;

	@Autowired
	private SdmxElementService sdmxElementService;

	@Autowired
	private SdmxElementRepo sdmxElementRepo;

	@Override
	public SdmxElementTempEntity add(SdmxElementTempEntity entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SdmxElementTempEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxElementTempEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxElementTempEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementTempEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementTempEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementTempEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementTempEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementTempEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxElementTempEntity bean) throws ServiceException {

	}

	public List<ElementApprovalOutputBean> getRequests(ApprovalInputBean requestInputBean) {
		boolean isDeptAdmin;
		boolean isMainDept;
		Long elementId;
		List<ElementApprovalOutputBean> allRequestBeanList = new ArrayList<>();
		List<ElementApprovalOutputBean> rggAdminRequestList = new ArrayList<>();
		List<ElementApprovalOutputBean> deptAdminRequestList = new ArrayList<>();
		List<SdmxElementTempEntity> allRequestEntityList = new ArrayList<>();
		ElementApprovalOutputBean elementApprovalOutputBean;

		allRequestEntityList = sdmxElementTempRepo.findByAdminStatusId(requestInputBean.getAdminStatusId());

		isDeptAdmin = requestInputBean.getIsDeptAdmin();
		isMainDept = requestInputBean.getIsMainDept();

		if (!allRequestEntityList.isEmpty()) {
			for (SdmxElementTempEntity requestObj : allRequestEntityList) {
				elementApprovalOutputBean = new ElementApprovalOutputBean();
				elementApprovalOutputBean.setActionStatusId(requestObj.getActionStatusFk().getActionId());
				elementApprovalOutputBean.setActionStatusLabel(requestObj.getActionStatusFk().getActionName());
				elementApprovalOutputBean.setCreatedBy(requestObj.getCreatedBy().getUserId());
				elementApprovalOutputBean.setCreatedByName(requestObj.getCreatedBy().getUserName());
				elementApprovalOutputBean.setCreatedOn(requestObj.getCreatedOn());
				elementApprovalOutputBean.setCreatedOnLong(requestObj.getCreatedOn().getTime());
				elementApprovalOutputBean.setDsdCode(requestObj.getDsdCode());

				if (requestObj.getElementIdFk() != null) {
					elementApprovalOutputBean.setElementIdFk(requestObj.getElementIdFk().getElementId());
					elementApprovalOutputBean.setAgencyCode(requestObj.getElementIdFk().getAgencyMaster().getAgencyMasterCode());
					elementApprovalOutputBean.setAgencyLabel(requestObj.getElementIdFk().getAgencyMaster().getAgencyMasterLabel());
				}

				elementApprovalOutputBean.setElementTempId(requestObj.getElementTempId());
				elementApprovalOutputBean.setElementVer(requestObj.getElementVer());
				elementApprovalOutputBean.setSdmxElementEntityJson(new Gson().toJson(requestObj.getSdmxElementEntity()));
				elementApprovalOutputBean.setStatusId(requestObj.getSdmxStatusEntity().getAdminStatusId());
				elementApprovalOutputBean.setStatusLabel(requestObj.getSdmxStatusEntity().getStatus());
				allRequestBeanList.add(elementApprovalOutputBean);
			}
		}

		if (isDeptAdmin) {
			if (isMainDept) { // RGG admin
				if (!allRequestBeanList.isEmpty()) {
					for (ElementApprovalOutputBean requestObj : allRequestBeanList) {

						if (requestObj.getActionStatusId() == 1) {
							rggAdminRequestList.add(requestObj);

						} else {
							elementId = requestObj.getElementIdFk();
							SdmxElementEntity sdmxElementBean = sdmxElementRepo.findByElementId(elementId);
							if (sdmxElementBean != null) {
								if (sdmxElementBean.getRegulatorOwnerIdFk().getRegulatorCode().equalsIgnoreCase(requestInputBean.getDeptCode())) {
									rggAdminRequestList.add(requestObj);
								}
							}

						}

					}
				}

				return rggAdminRequestList;

			} else { // Dept admin
				if (!allRequestBeanList.isEmpty()) {
					for (ElementApprovalOutputBean requestObj : allRequestBeanList) {

						elementId = requestObj.getElementIdFk();
						SdmxElementEntity sdmxElementBean = sdmxElementRepo.findByElementId(elementId);
						if (sdmxElementBean != null) {
							if (sdmxElementBean.getRegulatorOwnerIdFk().getRegulatorCode().equalsIgnoreCase(requestInputBean.getDeptCode())) {
								deptAdminRequestList.add(requestObj);
							}
						}

					}
				}

				return deptAdminRequestList;

			}
		}
		return null;
	}

	public void approveRejectElementRecord(ApprovalInputBean requestInputBean) throws ApplicationException {
		Long actionId;
		Long elementId;

		if (requestInputBean.getAdminStatusId() == 3) {
			actionId = requestInputBean.getActionId();

			int updatedRow = sdmxElementTempRepo.approveRejectRequest(requestInputBean.getModTablePkId(), requestInputBean.getComments(), requestInputBean.getAdminStatusId());

			if (updatedRow < 1) {
				throw new ApplicationException(ErrorCode.EC0013.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
			}

			if (actionId == 2 || actionId == 4) {
				elementId = requestInputBean.getMasterTablePkId();

				sdmxElementRepo.setIsPending(elementId, Boolean.FALSE);

			}

		} else if (requestInputBean.getAdminStatusId() == 4) {
			actionId = requestInputBean.getActionId();

			int updatedRow = sdmxElementTempRepo.approveRejectRequest(requestInputBean.getModTablePkId(), requestInputBean.getComments(), requestInputBean.getAdminStatusId());

			if (updatedRow < 1) {
				throw new ApplicationException(ErrorCode.EC0013.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
			}

			if (actionId == 1) {
				addElement(requestInputBean);

			} else if (actionId == 2) {

				sdmxElementRepo.setIsPending(requestInputBean.getMasterTablePkId(), Boolean.FALSE);
				editElement(requestInputBean);

			} else if (actionId == 4) {

				sdmxElementRepo.setIsPending(requestInputBean.getMasterTablePkId(), Boolean.FALSE);
				deleteElement(requestInputBean);

			}

		}

	}

	public void addElement(ApprovalInputBean requestInputBean) {
		SdmxElementTempEntity SdmxElementTempObj = sdmxElementTempRepo.findByElementTempId(requestInputBean.getModTablePkId());
		String elementJson = SdmxElementTempObj.getSdmxElementEntity();

		SdmxElementBeanForTemp elementAddObject = JsonUtility.getGsonObject().fromJson(elementJson, SdmxElementBeanForTemp.class);

		SdmxElementBean sdmxElementBean = new SdmxElementBean();
		BeanUtils.copyProperties(elementAddObject, sdmxElementBean);
		sdmxElementBean.setCreatedBy(SdmxElementTempObj.getCreatedBy().getUserId());
		sdmxElementService.addEntityByBean(sdmxElementBean);
		Long elementId = sdmxElementRepo.findByDsdCodeAndVerAndIsActive(sdmxElementBean.getDsdCode(), sdmxElementBean.getElementVer(), Boolean.TRUE, sdmxElementBean.getAgencyMasterCode());
		sdmxElementTempRepo.setElementId(requestInputBean.getModTablePkId(), elementId);
	}

	public void editElement(ApprovalInputBean requestInputBean) {
		SdmxElementTempEntity SdmxElementTempObj = sdmxElementTempRepo.findByElementTempId(requestInputBean.getModTablePkId());
		String elementJson = SdmxElementTempObj.getSdmxElementEntity();

		SdmxElementBeanForTemp elementEditObject = JsonUtility.getGsonObject().fromJson(elementJson, SdmxElementBeanForTemp.class);

		SdmxElementBean sdmxElementBean = new SdmxElementBean();
		BeanUtils.copyProperties(elementEditObject, sdmxElementBean);
		sdmxElementBean.setCreatedBy(SdmxElementTempObj.getCreatedBy().getUserId());
		sdmxElementService.editEntityByBean(sdmxElementBean, sdmxElementBean.getElementId());
		//sdmxElementRepo.activateInactivateElement(sdmxElementBean.getElementId(), Boolean.TRUE);

	}

	public void deleteElement(ApprovalInputBean requestInputBean) {
		SdmxElementTempEntity SdmxElementTempObj = sdmxElementTempRepo.findByElementTempId(requestInputBean.getModTablePkId());
		String elementJson = SdmxElementTempObj.getSdmxElementEntity();

		SdmxElementBeanForTemp elementDeleteObject = JsonUtility.getGsonObject().fromJson(elementJson, SdmxElementBeanForTemp.class);
		sdmxElementService.delinkParentFromChild(elementDeleteObject.getElementId());
		sdmxElementService.deleteEntityById(elementDeleteObject.getElementId());

	}

	public boolean isElementArrayPending(List<ElementListRequestBean> elementRequestBeanList) {
		List<SdmxElementEntity> sdmxElementMastersList = new ArrayList<>();
		try {
			List<String> elementList = elementRequestBeanList.stream().map(f -> f.getElementCode()).collect(Collectors.toList());
			sdmxElementMastersList = sdmxElementRepo.findByDsdCodeInAndIsActive(elementList, true);

			if (!sdmxElementMastersList.isEmpty()) {
				for (ElementListRequestBean obj : elementRequestBeanList) {
					for (SdmxElementEntity sdmxElementEntity : sdmxElementMastersList) {
						if (obj.getElementCode().equals(sdmxElementEntity.getDsdCode()) && obj.getElementVersion().equals(sdmxElementEntity.getElementVer()) && sdmxElementEntity.getIsActive().equals(Boolean.TRUE) && sdmxElementEntity.getIsPending().equals(Boolean.TRUE)) {
							return true;
						}

					}
				}

			}
			return false;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}

	}

}
