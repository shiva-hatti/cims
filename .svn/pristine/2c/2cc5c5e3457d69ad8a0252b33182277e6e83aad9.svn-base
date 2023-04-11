/**
 * 
 */
package com.iris.sdmx.exceltohtml.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.sdmx.exceltohtml.bean.DimensionCodeListValueBean;
import com.iris.sdmx.exceltohtml.bean.DimensionDetailCategories;
import com.iris.sdmx.exceltohtml.bean.ModelCodeInputBean;
import com.iris.sdmx.exceltohtml.bean.RegexDetails;
import com.iris.sdmx.exceltohtml.bean.SdmxDimensionDetail;
import com.iris.sdmx.exceltohtml.bean.SdmxEleDimTypeMapBean;
import com.iris.sdmx.exceltohtml.bean.SdmxModelCodesBean;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnModelInfoBean;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnModelInfoEntity;
import com.iris.sdmx.exceltohtml.helper.SdmxModelCodesHelper;
import com.iris.sdmx.exceltohtml.service.SdmxModelCodesService;
import com.iris.sdmx.exceltohtml.service.SdmxReturnModelInfoService;
import com.iris.sdmx.exceltohtml.service.SdmxReturnSheetInfoService;
import com.iris.sdmx.exceltohtml.validator.SdmxModelCodeValidator;
import com.iris.sdmx.model.code.data.SdmxDataModelCodeBean;
import com.iris.sdmx.model.code.data.SdmxDataModelCodeRequestBean;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.util.UtilMaster;
import com.iris.util.Validations;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author apagaria
 *
 */
@RestController
@RequestMapping("/service/sdmx/modelCode")
public class SdmxModelCodesController {

	private static final Logger LOGGER = LogManager.getLogger(SdmxModelCodesController.class);

	@Autowired
	private SdmxModelCodeValidator sdmxModelCodeValidator;

	@Autowired
	private SdmxModelCodesService sdmxModelCodesService;

	@Autowired
	private SdmxModelCodesHelper sdmxModelCodesHelper;

	@Autowired
	private SdmxReturnSheetInfoService sdmxReturnSheetInfoService;

	@Autowired
	private SdmxReturnModelInfoService sdmxReturnModelInfoService;

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param returnCellRef
	 * @param returnTemplateId
	 * @return
	 */
	@GetMapping("/user/{userId}/returnCell/{returnCellRef}/returnTemplate/{returnTemplateId}/returnPreviewId/{returnPreviewId}")
	public ServiceResponse fetchEntityByReturnCellNReturnTemplate(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("returnCellRef") Integer returnCellRef, @PathVariable("returnTemplateId") Long returnTemplateId, @PathVariable("returnPreviewId") Long returnPreviewId) {
		LOGGER.info("START - Fetch Return Preview request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			sdmxModelCodeValidator.validatefetchEntityByReturnCellNReturnTemplate(userId, jobProcessId, returnCellRef, returnTemplateId);
			SdmxModelCodesBean sdmxModelCodesBean = sdmxModelCodesService.findByReturnCellReffNReturnTemplate(returnCellRef, returnTemplateId, returnPreviewId);

			if (sdmxModelCodesBean != null) {
				Boolean isMandatory = sdmxReturnModelInfoService.fetchIsMandatoryByReturnTemplateNCellRefId(returnCellRef, returnTemplateId, returnPreviewId);
				sdmxModelCodesBean.setIsMandatoryCell(isMandatory);
				DimensionDetailCategories dimensionDetailCategories = new Gson().fromJson(sdmxModelCodesBean.getModelDim(), DimensionDetailCategories.class);
				if (dimensionDetailCategories.getRegexDetails() != null) {
					RegexDetails regexDetails = dimensionDetailCategories.getRegexDetails();
					sdmxModelCodesBean.setRegexMinLength(regexDetails.getMinLength());
					sdmxModelCodesBean.setRegexMaxLength(regexDetails.getMaxLength());
				}
				List<SdmxDimensionDetail> sdmxDimensionDetailList = new ArrayList<>();
				// Close Dimension
				if (!CollectionUtils.isEmpty(dimensionDetailCategories.getClosedDim())) {
					List<DimensionCodeListValueBean> closedDim = dimensionDetailCategories.getClosedDim();
					for (DimensionCodeListValueBean dimensionCodeListValueBean : closedDim) {
						SdmxDimensionDetail sdmxDimensionDetail = new SdmxDimensionDetail();
						sdmxDimensionDetail.setDimensionCode(dimensionCodeListValueBean.getDimConceptId());
						sdmxDimensionDetail.setSelectedCodeListValue(dimensionCodeListValueBean.getClValueCode());
						sdmxDimensionDetailList.add(sdmxDimensionDetail);
					}
				}
				// Open Dimension
				if (!CollectionUtils.isEmpty(dimensionDetailCategories.getOpenDimension())) {
					List<DimensionCodeListValueBean> openDim = dimensionDetailCategories.getOpenDimension();
					for (DimensionCodeListValueBean dimensionCodeListValueBean : openDim) {
						SdmxDimensionDetail sdmxDimensionDetail = new SdmxDimensionDetail();
						sdmxDimensionDetail.setDimensionCode(dimensionCodeListValueBean.getDimConceptId());
						sdmxDimensionDetail.setSelectedCodeListValue(dimensionCodeListValueBean.getClValueCode());
						sdmxDimensionDetailList.add(sdmxDimensionDetail);
					}
				}

				// Common Dimension
				if (!CollectionUtils.isEmpty(dimensionDetailCategories.getCommonDimension())) {
					List<DimensionCodeListValueBean> commonDim = dimensionDetailCategories.getCommonDimension();
					for (DimensionCodeListValueBean dimensionCodeListValueBean : commonDim) {
						SdmxDimensionDetail sdmxDimensionDetail = new SdmxDimensionDetail();
						sdmxDimensionDetail.setDimensionCode(dimensionCodeListValueBean.getDimConceptId());
						sdmxDimensionDetail.setSelectedCodeListValue(dimensionCodeListValueBean.getClValueCode());
						sdmxDimensionDetailList.add(sdmxDimensionDetail);
					}
				}

				// Model Other details
				if (dimensionDetailCategories.getModelOtherDetails() != null) {
					sdmxModelCodesBean.setIsDependentCell(dimensionDetailCategories.getModelOtherDetails().getDependencyType());
				}

				sdmxModelCodesBean.setModelDim(new Gson().toJson(sdmxDimensionDetailList));
				serviceResponseBuilder.setResponse(new Gson().toJson(sdmxModelCodesBean));
				serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
			} else {
				serviceResponseBuilder.setStatus(false);
				serviceResponseBuilder.setStatusCode(ErrorCode.EC0013.toString());
				serviceResponseBuilder.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
			}
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch Return Preview request completed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@GetMapping("/user/{userId}/dmModelCode/{dmModelCode}")
	public ServiceResponse fetchEntityByDmCode(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("dmModelCode") String dmModelCode) {
		LOGGER.info("START - Fetch Return Preview request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			sdmxModelCodeValidator.validatefetchEntityByDmModelCode(userId, jobProcessId, dmModelCode);
			SdmxModelCodesBean sdmxModelCodesBean = sdmxModelCodesService.findEntityByDmModelCodes(dmModelCode);
			if (sdmxModelCodesBean != null) {
				DimensionDetailCategories dimensionDetailCategories = new Gson().fromJson(sdmxModelCodesBean.getModelDim(), DimensionDetailCategories.class);
				List<SdmxDimensionDetail> sdmxDimensionDetailList = new ArrayList<>();
				// Close Dimension
				if (!CollectionUtils.isEmpty(dimensionDetailCategories.getClosedDim())) {
					List<DimensionCodeListValueBean> closedDim = dimensionDetailCategories.getClosedDim();
					for (DimensionCodeListValueBean dimensionCodeListValueBean : closedDim) {
						SdmxDimensionDetail sdmxDimensionDetail = new SdmxDimensionDetail();
						sdmxDimensionDetail.setDimensionCode(dimensionCodeListValueBean.getDimConceptId());
						sdmxDimensionDetail.setSelectedCodeListValue(dimensionCodeListValueBean.getClValueCode());
						sdmxDimensionDetailList.add(sdmxDimensionDetail);
					}
				}
				// Open Dimension
				if (!CollectionUtils.isEmpty(dimensionDetailCategories.getOpenDimension())) {
					List<DimensionCodeListValueBean> openDim = dimensionDetailCategories.getOpenDimension();
					for (DimensionCodeListValueBean dimensionCodeListValueBean : openDim) {
						SdmxDimensionDetail sdmxDimensionDetail = new SdmxDimensionDetail();
						sdmxDimensionDetail.setDimensionCode(dimensionCodeListValueBean.getDimConceptId());
						sdmxDimensionDetail.setSelectedCodeListValue(dimensionCodeListValueBean.getClValueCode());
						sdmxDimensionDetailList.add(sdmxDimensionDetail);
					}
				}

				// Common Dimension
				if (!CollectionUtils.isEmpty(dimensionDetailCategories.getCommonDimension())) {
					List<DimensionCodeListValueBean> commonDim = dimensionDetailCategories.getCommonDimension();
					for (DimensionCodeListValueBean dimensionCodeListValueBean : commonDim) {
						SdmxDimensionDetail sdmxDimensionDetail = new SdmxDimensionDetail();
						sdmxDimensionDetail.setDimensionCode(dimensionCodeListValueBean.getDimConceptId());
						sdmxDimensionDetail.setSelectedCodeListValue(dimensionCodeListValueBean.getClValueCode());
						sdmxDimensionDetailList.add(sdmxDimensionDetail);
					}
				}
				sdmxModelCodesBean.setModelDim(new Gson().toJson(sdmxDimensionDetailList));
				serviceResponseBuilder.setResponse(new Gson().toJson(sdmxModelCodesBean));
				serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
			} else {
				serviceResponseBuilder.setStatus(false);
				serviceResponseBuilder.setStatusCode(ErrorCode.EC0013.toString());
				serviceResponseBuilder.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
			}
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch Return Preview request completed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param dmModelCode
	 * @return
	 */
	@GetMapping("/user/{userId}/fetchdmcodes/{dmCode}")
	public ServiceResponse fetchEntityByReturnCellNReturnTemplate(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("dmCode") String dmModelCode) {
		LOGGER.info("START - Fetch Return Preview request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			sdmxModelCodeValidator.validateFetchDmCodeRequest(userId, jobProcessId, dmModelCode);
			List<String> dmCodeList = sdmxModelCodesService.findDmModelCodes(dmModelCode);
			serviceResponseBuilder.setResponse(dmCodeList);
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch Return Preview request completed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@GetMapping("/user/{userId}/modelCode/{modelCode}")
	public ServiceResponse fetchEntityByModelCode(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("modelCode") String modelCode) {
		LOGGER.info("START - Fetch Return Preview request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			sdmxModelCodeValidator.validatefetchEntityByModelCode(userId, jobProcessId, modelCode);
			SdmxModelCodesBean sdmxModelCodesBean = sdmxModelCodesService.getBeanByModelCode(modelCode);
			serviceResponseBuilder.setResponse(sdmxModelCodesBean);
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch Return Preview request completed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@DeleteMapping("/user/{userId}/returnCell/{returnCellRef}/returnTemplate/{returnTemplateId}/returnPreview/{returnPreviewId}")
	public ServiceResponse deleteEntityByModelCode(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("returnCellRef") Integer returnCellRef, @PathVariable("returnTemplateId") Long returnTemplateId, @PathVariable("returnPreviewId") Long returnPreviewId) {
		LOGGER.info("START - Fetch Return Preview request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			sdmxModelCodeValidator.validatefetchEntityByReturnCellRef(userId, jobProcessId, returnCellRef);
			if (returnCellRef != null) {
				SdmxReturnModelInfoBean sdmxReturnModelInfoBean2 = sdmxReturnModelInfoService.findEntityByReturnTemplateNCellRef(returnTemplateId, returnCellRef, returnPreviewId);
				sdmxReturnModelInfoService.deleteEntityByBean(sdmxReturnModelInfoBean2.getReturnModelInfoId());
				if (!sdmxReturnModelInfoService.getBeanByModelCode(sdmxReturnModelInfoBean2.getModelCodesIdFk())) {
					sdmxModelCodesService.deleteEntityByBean(sdmxReturnModelInfoBean2.getModelCodesIdFk());
				}
			}
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch Return Preview request completed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping("/user/{userId}/modelCode/returnPreview/{returnPreviewId}")
	public ServiceResponse saveEntity(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @RequestBody ModelCodeInputBean modelCodeInputBean, @PathVariable("returnPreviewId") Long returnPreviewId) {
		LOGGER.info("START - Fetch Return Preview request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			sdmxModelCodeValidator.validateSaveEntityRequest(userId, jobProcessId, modelCodeInputBean);
			SdmxModelCodesBean sdmxModelCodesBean = new SdmxModelCodesBean();
			// DMID Grouping
			SdmxEleDimTypeMapBean sdmxEleDimTypeMapBean = new SdmxEleDimTypeMapBean();
			Long oldModelCodeId = sdmxModelCodesHelper.fetchBeanFromRequest(modelCodeInputBean, userId, sdmxModelCodesBean, sdmxEleDimTypeMapBean);
			Long modelId;
			if (oldModelCodeId > 0) {
				modelId = oldModelCodeId;
			} else {
				modelId = sdmxModelCodesService.saveEntityByBean(sdmxModelCodesBean, userId);
			}

			Long returnSheetInfoId = sdmxReturnSheetInfoService.findIdByOtherDetail(new String(Base64.decodeBase64(modelCodeInputBean.getSheetCode())), new String(Base64.decodeBase64(modelCodeInputBean.getSheetName())), new String(Base64.decodeBase64(modelCodeInputBean.getSectionCode())), new String(Base64.decodeBase64(modelCodeInputBean.getSectionName())), modelCodeInputBean.getReturnTemplateIdFk(), returnPreviewId);

			SdmxReturnModelInfoBean sdmxReturnModelInfoBean2 = sdmxReturnModelInfoService.findByReturnSheetTemplateIdNCellRef(returnSheetInfoId, modelCodeInputBean.getReturnCellRef());
			if (sdmxReturnModelInfoBean2 != null) {
				sdmxReturnModelInfoService.deleteEntityByBean(sdmxReturnModelInfoBean2.getReturnModelInfoId());
				if (!sdmxReturnModelInfoService.getBeanByModelCode(sdmxReturnModelInfoBean2.getModelCodesIdFk())) {
					sdmxModelCodesService.deleteEntityByBean(sdmxReturnModelInfoBean2.getModelCodesIdFk());
				}
			}

			SdmxReturnModelInfoBean sdmxReturnModelInfoBean = new SdmxReturnModelInfoBean();
			sdmxReturnModelInfoBean.setReturnSheetIdFk(returnSheetInfoId);
			sdmxReturnModelInfoBean.setModelCodesIdFk(modelId);
			sdmxReturnModelInfoBean.setReturnCellRef(modelCodeInputBean.getReturnCellRef());
			sdmxReturnModelInfoBean.setIsMandatory(modelCodeInputBean.getIsMandatoryCell());
			if (!UtilMaster.isEmpty(modelCodeInputBean.getCellFormula())) {
				sdmxReturnModelInfoBean.setCellFormula(modelCodeInputBean.getCellFormula());
			}
			sdmxReturnModelInfoService.saveEntityByBean(sdmxReturnModelInfoBean, userId);
			// DMID Grouping
			sdmxModelCodesHelper.saveDMIDGrouping(sdmxEleDimTypeMapBean, new Gson());

			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
			serviceResponseBuilder.setResponse(sdmxModelCodesBean.getModelCode());
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch Return Preview request completed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping(value = "/getSDMXDataModelCodes")
	public ServiceResponse getSDMXDataModelCodes(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody SdmxDataModelCodeRequestBean sdmxDataModelCodeRequestBean) {
		LOGGER.info("Request received to get code list master data for job processing Id : " + jobProcessId);
		try {

			sdmxModelCodeValidator.validateSdmxDataModelCode(sdmxDataModelCodeRequestBean, jobProcessId);
			List<SdmxReturnModelInfoEntity> sdmxReturnModelInfoEntityList = sdmxReturnModelInfoService.getAllDataFor(null, null);
			if (!Validations.isEmpty(sdmxReturnModelInfoEntityList)) {
				List<SdmxDataModelCodeBean> sdmxDataModelCodeBeans = sdmxModelCodesService.prepareDataForSDMXDataModel(sdmxReturnModelInfoEntityList);
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(sdmxDataModelCodeBeans).build();
			}

		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
		}

		return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ErrorCode.E0808.toString()).setStatusCode(ObjectCache.getErrorCodeKey(ErrorCode.E0808.toString())).build();
	}

	@GetMapping(value = "/user/{userId}/checkReturnModelMapping")
	public ServiceResponse checkReturnModelMapping(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @RequestHeader(name = "ReturnTemplateIds") String returnTemplateIds) {
		LOGGER.info("Request received to Check Return Model Mapping START for job processing Id : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {

			sdmxModelCodeValidator.validateCheckReturnModelMapping(jobProcessId, userId);
			List<Long> returnTemplateIdList = new ArrayList<>();
			String[] returnArrayStr = returnTemplateIds.split(",");
			for (String returnIdStr : returnArrayStr) {
				returnTemplateIdList.add(new Long(returnIdStr));
			}
			Map<String, List<Integer>> returnValuesMap = sdmxReturnModelInfoService.fetchReturnMappingCount(returnTemplateIdList);
			if (!MapUtils.isEmpty(returnValuesMap)) {
				serviceResponseBuilder.setStatus(true);
				serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
				serviceResponseBuilder.setResponse(returnValuesMap);
			} else {
				serviceResponseBuilder.setStatus(false);
				serviceResponseBuilder.setStatusCode(SDMXConstants.FAILURE_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.FAILURE_MESSAGE);
			}
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("Request received to Check Return Model Mapping END for job processing Id : " + jobProcessId);
		return serviceResponse;
	}

	/**
	 * @param jobProcessId
	 * @param userId
	 * @return
	 * 
	 *         This is intentionally marked as Get function so we can call from UI page
	 * 
	 */
	@GetMapping("/user/{userId}/modelCode")
	public ServiceResponse deleteUnusedModelCode(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId) {
		LOGGER.info("START - Delete Unused model codes received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			// Validate request
			sdmxModelCodeValidator.validateDeleteUnusedModelCodes(userId, jobProcessId);

			// delete unused model code
			sdmxModelCodesService.deleteUnusedModelCodes(jobProcessId);

			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Delete Unused model codes received with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping("/user/{userId}/modelGroupMapping")
	public ServiceResponse modelGroupMapping(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId) {
		LOGGER.info("START - Model group mapping received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			// Validate request
			sdmxModelCodeValidator.validateDeleteUnusedModelCodes(userId, jobProcessId);

			// delete unused model code
			sdmxModelCodesService.modelGroupMapping(jobProcessId);

			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Model group mapping received with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}
}
