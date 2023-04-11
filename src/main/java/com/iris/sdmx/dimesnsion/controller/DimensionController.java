/**
 * 
 */
package com.iris.sdmx.dimesnsion.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.ebr.metadata.flow.constant.EbrMetadataFlowContstant;
import com.iris.ebr.metadata.flow.service.EbrMetadataFlowService;
import com.iris.exception.ApplicationException;
import com.iris.model.RoleType;
import com.iris.model.UserMaster;
import com.iris.repository.UserRoleRepo;
import com.iris.sdmx.dimesnsion.bean.DimensionMasterBean;
import com.iris.sdmx.dimesnsion.bean.DimensionRequestBean;
import com.iris.sdmx.dimesnsion.bean.DimensionTypeDto;
import com.iris.sdmx.dimesnsion.bean.RegexBean;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;
import com.iris.sdmx.dimesnsion.entity.DimensionMasterMod;
import com.iris.sdmx.dimesnsion.entity.DimensionType;
import com.iris.sdmx.dimesnsion.entity.Regex;
import com.iris.sdmx.dimesnsion.repo.RegexRepo;
import com.iris.sdmx.dimesnsion.service.DimensionService;
import com.iris.sdmx.dimesnsion.service.DimensionTypeService;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionBean;
import com.iris.sdmx.elementdimensionmapping.entity.ElementDimension;
import com.iris.sdmx.elementdimensionmapping.service.ElementDimensionService;
import com.iris.sdmx.fusion.controller.FusionApiController;
import com.iris.sdmx.fusion.service.FusionApiService;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.UtilMaster;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author sajadhav
 *
 */
@Controller
@RestController
@RequestMapping(value = "/service/dimensionController")
public class DimensionController {

	private static final Logger LOGGER = LogManager.getLogger(DimensionController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private DimensionService dimensionService;

	@Autowired
	private DimensionTypeService dimensionTypeService;

	@Autowired
	private RegexRepo regExRepo;

	@Autowired
	private FusionApiService fusionApiService;

	@Autowired
	private ElementDimensionService elementDimensionService;

	@Autowired
	private FusionApiController fusionApiController;

	@Autowired
	private UserRoleRepo userRoleRepo;

	@Autowired
	EbrMetadataFlowService ebrMetadataFlowService;

	@PostMapping(value = "/getDimensionRecord")
	public ServiceResponse getDimensionRecord(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DimensionRequestBean dimensionRequestBean) {
		LOGGER.info("Request received to get dimension data for job processing Id : " + jobProcessId + " With Input request : " + JsonUtility.getGsonObject().toJson(dimensionRequestBean));

		try {
			validateInputRequestToFetchDimensionData(jobProcessId, dimensionRequestBean);

			boolean isRBISuperUser = false;
			UserMaster userMaster = userMasterService.getDataById(dimensionRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}
			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				//for Entity User Do nothing
				// return with error response;
				//return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0613.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0613.toString())).build();
			}

			if ((userMaster.getDepartmentIdFk() != null) && userMaster.getDepartmentIdFk().getIsMaster().equals(Boolean.TRUE)) {
				isRBISuperUser = true;
			}

			if (dimensionRequestBean.getIsApprovedRecord().equals(Boolean.FALSE)) {
				if (isRBISuperUser) {
					//					Map<String, Object> codeValueMap = new HashMap<String, Object>();
					//					codeValueMap.put(ColumnConstants.ADMIN_STATUS.getConstantVal(), 1);
					//					List<CodeListMasterMod> codeListMasterModList = codeListmasterModService.getDataByObject(codeValueMap, MethodConstants.GET_CODE_LIST_PENDING_RECORD_BY_ADMIN_STATUS_ID.getConstantVal());
					return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
				} else {
					//Dont have access to this module
					return new ServiceResponseBuilder().setStatus(false).build();
				}
			} else {
				Map<String, Object> columnValueMap = new HashMap<>();
				columnValueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), Boolean.TRUE);
				List<DimensionMaster> dimensionMasterList = dimensionService.getDataByObject(columnValueMap, MethodConstants.GET_DIM_MASTER_RECORD_BY_PARENT_ID_IS_ACTIVE.getConstantVal());
				return new ServiceResponseBuilder().setStatus(true).setResponse(dimensionService.prepareDimensionMasterResponseList(dimensionMasterList)).build();
			}
		} catch (ApplicationException e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(e.getErrorCode())).build();
		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/addEditDeleteDimensionRecord")
	public ServiceResponse addEditDeleteDimensionRecord(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DimensionMasterBean dimensionMasterBean) {

		LOGGER.info("Request received to add code list master data for job processing Id : " + jobProcessId + " With Input request : " + JsonUtility.getGsonObject().toJson(dimensionMasterBean));

		try {
			// get User record
			boolean isApprovalRequired = false;
			Long userId = dimensionMasterBean.getUserId();
			UserMaster userMaster = userMasterService.getDataById(dimensionMasterBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}
			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				// return with error response;
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0386.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0386.toString())).build();
			}

			if (userMaster.getDepartmentIdFk().getIsMaster().equals(Boolean.FALSE)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1575.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1575.toString())).build();
			}

			boolean isSuccess = false;
			if (dimensionMasterBean.getActionId() == GeneralConstants.ACTIONID_ADDITION.getConstantIntVal()) {
				validateInputRequestObject(dimensionMasterBean, jobProcessId);
				isApprovalRequired = dimensionMasterBean.getAddApproval();
				isSuccess = dimensionService.addDimensionData(dimensionMasterBean, isApprovalRequired);
				ebrMetadataFlowService.ctlEntryForEbrMetadata(EbrMetadataFlowContstant.DIMENSION.getConstantVal());
			} else if (dimensionMasterBean.getActionId() == GeneralConstants.ACTIONID_EDITION.getConstantIntVal()) {
				validateInputRequestObject(dimensionMasterBean, jobProcessId);
				isApprovalRequired = dimensionMasterBean.getEditApproval();
				isSuccess = dimensionService.editDimensionData(dimensionMasterBean, isApprovalRequired);
				ebrMetadataFlowService.ctlEntryForEbrMetadata(EbrMetadataFlowContstant.DIMENSION.getConstantVal());
			} else if (dimensionMasterBean.getActionId() == GeneralConstants.ACTIONID_DELETION.getConstantIntVal()) {
				//				validateInputRequestObject(dimensionMasterBean,jobProcessId);
				isApprovalRequired = dimensionMasterBean.getEditApproval();
				isSuccess = dimensionService.deleteDimensionData(dimensionMasterBean, isApprovalRequired);
				if (!isSuccess) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode("EOOO2").setStatusMessage("Partially delted").setResponse(isSuccess).build();
				} else {
					isSuccess = true;
				}

				ebrMetadataFlowService.ctlEntryForEbrMetadata(EbrMetadataFlowContstant.DIMENSION.getConstantVal());
			} else {
				// return with error response
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).build();
			}

			LOGGER.info("Request completed to add code list master data for job processing Id : " + jobProcessId + " With Input request : " + JsonUtility.getGsonObject().toJson(dimensionMasterBean));

			if (isSuccess) {
				if (!isApprovalRequired) {
					//ServiceResponse serviceResponse = fusionApiController.submitDimensionData(dimensionMasterBean.getUserId());
					if (dimensionMasterBean.getActionId() == GeneralConstants.ACTIONID_DELETION.getConstantIntVal()) {
						Map<String, List<DimensionMasterBean>> conceptAgencyDimMap = new HashMap<>();
						conceptAgencyDimMap = getConceptAgencyDimMap(dimensionMasterBean.getDimesnionMasterBeans());
						Iterator<Entry<String, List<DimensionMasterBean>>> it = conceptAgencyDimMap.entrySet().iterator();

						while (it.hasNext()) {
							Map.Entry<String, List<DimensionMasterBean>> set = (Map.Entry<String, List<DimensionMasterBean>>) it.next();
							String conceptVersion = set.getKey().split("~~")[0];
							String agencyCode = set.getKey().split("~~")[1];
							List<DimensionMasterBean> dimensionList = set.getValue();
							ServiceResponse serviceResponse = fusionApiController.submitDimensionDataWithConceptVersion(userId, conceptVersion, agencyCode);
							if (!serviceResponse.isStatus()) {
								LOGGER.error("Dimension API calling failed for job processing Id : " + jobProcessId + "");
							}

						}

					} else {
						ServiceResponse serviceResponse = fusionApiController.submitDimensionDataWithConceptVersion(dimensionMasterBean.getUserId(), dimensionMasterBean.getConceptVersion(), dimensionMasterBean.getAgencyMasterCode());
						if (!serviceResponse.isStatus()) {
							LOGGER.error("Dimension API calling failed for job processing Id : " + jobProcessId + "");
						}
					}

				}

				return new ServiceResponseBuilder().setStatus(true).setStatusMessage(ErrorConstants.RECORD_SAVED_SUCCESSFULLY.getErrorMessage()).build();
			}
		} catch (ApplicationException e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(e.getErrorCode())).build();
		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
		}
		return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
	}

	@PostMapping(value = "/getAllDimensionType")
	public ServiceResponse getAllDimensionType(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DimensionRequestBean dimensionRequestBean) {
		LOGGER.info("Request received to get code list master data for job processing Id : " + jobProcessId);
		try {

			UserMaster userMaster = userMasterService.getDataById(dimensionRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				//for Entity User Do nothing
				// return with error response;
				//return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			List<DimensionType> dimensionTypes = dimensionTypeService.getAllDataFor(null, null);
			List<DimensionTypeDto> responseList = null;

			if (!Validations.isEmpty(dimensionTypes)) {
				responseList = new ArrayList<>();
				for (DimensionType sdmxDimensionType : dimensionTypes) {
					DimensionTypeDto sdmxDimDto = new DimensionTypeDto();
					sdmxDimDto.setDimensionTypeId(sdmxDimensionType.getDimesnionTypeId());
					sdmxDimDto.setDimensionTypeName(sdmxDimensionType.getDimesnsionTypeName());
					sdmxDimDto.setLastUpdatedOn(sdmxDimensionType.getLastUpdatedOn());
					responseList.add(sdmxDimDto);
				}
			}

			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(responseList).build();

		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
		}

		return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ErrorCode.E0808.toString()).setStatusCode(ObjectCache.getErrorCodeKey(ErrorCode.E0808.toString())).build();
	}

	@PostMapping(value = "/isDimensionCodeExist")
	public ServiceResponse getActiveDimCode(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DimensionRequestBean dimensionRequestBean) {
		LOGGER.info("Request received to get code list master data for job processing Id : " + jobProcessId);
		boolean found = false;

		try {

			UserMaster userMaster = userMasterService.getDataById(dimensionRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				// return with error response;
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			found = dimensionService.getSdmxDimensionMasterUsingDimCode(dimensionRequestBean.getDimCode(), dimensionRequestBean.getAgencyMasterCode(), dimensionRequestBean.getConceptVersion());
			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(found).build();

		} catch (Exception e) {

			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
		}

		return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ErrorCode.E0808.toString()).setStatusCode(ObjectCache.getErrorCodeKey(ErrorCode.E0808.toString())).build();
	}

	@GetMapping(value = "/getRegExRecords")
	public ServiceResponse getRegExRecords(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		LOGGER.info("Request received to get code list master data for job processing Id : " + jobProcessId + "");

		try {
			List<Regex> regExList = regExRepo.findAll();
			List<RegexBean> regExBeanList = new ArrayList<>();

			for (Regex regex : regExList) {
				RegexBean regExBean = new RegexBean();
				BeanUtils.copyProperties(regex, regExBean);
				regExBeanList.add(regExBean);
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(regExBeanList).build();
		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

	}

	private void validateInputRequestObject(DimensionMasterBean dimensionMasterBean, String jobProcessId) throws ApplicationException {

		if (UtilMaster.isEmpty(jobProcessId) || UtilMaster.isEmpty(dimensionMasterBean.getDimensionCode()) || UtilMaster.isEmpty(dimensionMasterBean.getDimensionTypeId()) || UtilMaster.isEmpty(dimensionMasterBean.getUserId()) || UtilMaster.isEmpty(dimensionMasterBean.getRoleId()) || UtilMaster.isEmpty(dimensionMasterBean.getLangCode())) {
			throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
		}

	}

	private void validateInputRequestToFetchDimensionData(String jobProcessingId, DimensionRequestBean dimensionRequestBean) throws ApplicationException {
		if (UtilMaster.isEmpty(jobProcessingId) || UtilMaster.isEmpty(dimensionRequestBean.getUserId()) || UtilMaster.isEmpty(dimensionRequestBean.getRoleId()) || UtilMaster.isEmpty(dimensionRequestBean.getIsApprovedRecord())) {
			throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
		}
	}

	@PostMapping(value = "/getCommonOrMandatoryDimension")
	public ServiceResponse getCommonOrMandatoryDimension(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DimensionRequestBean dimensionRequestBean) {
		LOGGER.info("Request received to get code list master data for job processing Id : " + jobProcessId);
		try {

			UserMaster userMaster = userMasterService.getDataById(dimensionRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				// return with error response;
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			List<DimensionMaster> dimensionMasters = dimensionService.getCommonOrMandatoryDimension();
			List<DimensionMasterBean> DimensionMasterBeanList = dimensionService.prepareDimensionCommonData(dimensionMasters);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(DimensionMasterBeanList).build();

		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
		}

		return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ErrorCode.E0808.toString()).setStatusCode(ObjectCache.getErrorCodeKey(ErrorCode.E0808.toString())).build();
	}

	@PostMapping(value = "/getDimensionMapping")
	public ServiceResponse getDimensionMapping(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody List<String> dimensionCodes) {
		try {
			validateGetDimemsionMappingRequestObject(jobProcessId, dimensionCodes);
			return new ServiceResponseBuilder().setStatus(true).setResponse(dimensionService.searchDimensionCode(dimensionCodes)).build();
		} catch (ApplicationException ae) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ae.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(ae.getErrorCode())).build();
		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private void validateGetDimemsionMappingRequestObject(String jobProcessingId, List<String> dimensionCodes) throws ApplicationException {
		if (UtilMaster.isEmpty(jobProcessingId) || UtilMaster.isEmpty(dimensionCodes)) {
			throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
		}
	}

	public Map<String, List<DimensionMasterBean>> getConceptAgencyDimMap(List<DimensionMasterBean> dimensionMasterBeanList) {
		Map<String, List<DimensionMasterBean>> conceptAgencyDimMap = new HashMap<>();
		if (!dimensionMasterBeanList.isEmpty()) {
			for (DimensionMasterBean dimObj : dimensionMasterBeanList) {
				String key = dimObj.getConceptVersion() + "~~" + dimObj.getAgencyMasterCode();
				if (conceptAgencyDimMap.containsKey(key)) {
					List<DimensionMasterBean> dimList = conceptAgencyDimMap.get(key);
					dimList.add(dimObj);
				} else {
					List<DimensionMasterBean> dimList = new ArrayList<>();
					dimList.add(dimObj);
					conceptAgencyDimMap.put(key, dimList);
				}
			}
		}
		return conceptAgencyDimMap;
	}

}
