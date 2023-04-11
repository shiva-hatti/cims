/**
 * 
 */
package com.iris.nbfc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.controller.PANMasterController;
import com.iris.controller.PrepareSendMailController;
import com.iris.dto.DynamicContent;
import com.iris.dto.LabelDto;
import com.iris.dto.MailServiceBean;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.NSDLPanVerif;
import com.iris.model.WebServiceComponentUrl;
import com.iris.nbfc.model.NbfcCorRegistrationBean;
import com.iris.nbfc.model.NsdlPanVerfReqRespDto;
import com.iris.nbfc.service.NbfcCorRegistrationService;
import com.iris.nbfc.service.NsdlPanVerficationService;
import com.iris.service.impl.WebServiceComponentService;
import com.iris.util.AESV2;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.CorRegistrationStatusEnum;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author Siddique H Khan
 *
 */
@RestController
@RequestMapping("/service/nsdlPanVerfController")
public class NsdlPanVerficationController {

	@Autowired
	NsdlPanVerficationService nsdlPanVerficationService;

	@Autowired
	private NbfcCorRegistrationService nbfcCorRegistrationService;

	@Autowired
	private PANMasterController panMasterController;

	@Autowired
	private PrepareSendMailController prepareSendMailController;

	@Autowired
	private WebServiceComponentService webServiceComponentService;

	@Value("${nbfc.verifiaction.api.path}")
	private String nbfcVerificationPath;

	private static final Logger Logger = LogManager.getLogger(NsdlPanVerficationController.class);

	@PostMapping(value = "/getNsdlPanVerfData", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNsdlPanVerfData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NsdlPanVerfReqRespDto nsdlPanVerfRequestDto) {

		try {
			List<NSDLPanVerif> beanList = null;

			Logger.info("NSDL pan verification method started with job processing id : " + jobProcessId);
			if (nsdlPanVerfRequestDto == null) {
				Logger.info("NSDL pan verification method end, null request");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
			}

			Logger.info("NSDL pan verification Module Name : " + nsdlPanVerfRequestDto.getModuleName());
			Logger.info("NSDL pan verification Sub Task  : " + nsdlPanVerfRequestDto.getSubTaskStatus());
			Logger.info("NSDL pan verification Status List : " + nsdlPanVerfRequestDto.getStatusList());
			Logger.info("NSDL pan verification Scheduler Last start time : " + nsdlPanVerfRequestDto.getSchedulerLastRunTime());

			if (StringUtils.isBlank(nsdlPanVerfRequestDto.getModuleName()) || CollectionUtils.isEmpty(nsdlPanVerfRequestDto.getStatusList()) || nsdlPanVerfRequestDto.getSubTaskStatus() == null) {
				Logger.info("NSDL pan verification method end, Invalid input");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}

			if (nsdlPanVerfRequestDto.getSchedulerLastRunTime() != null) {
				Logger.info("job processing id : " + jobProcessId + "scheduler last run time received : " + nsdlPanVerfRequestDto.getSchedulerLastRunTime());
				Logger.info("job processing id : " + jobProcessId + "scheduler last run time received : " + new Date(nsdlPanVerfRequestDto.getSchedulerLastRunTime()));

				Map<String, Object> columnValueMap = new HashMap<>();
				columnValueMap.put(ColumnConstants.MODULE_NAME.getConstantVal(), nsdlPanVerfRequestDto.getModuleName());
				columnValueMap.put(ColumnConstants.SUB_TASK_STATUS.getConstantVal(), nsdlPanVerfRequestDto.getSubTaskStatus());
				columnValueMap.put(ColumnConstants.SCHEDULER_LAST_RUNTIME.getConstantVal(), new Date(nsdlPanVerfRequestDto.getSchedulerLastRunTime()));
				columnValueMap.put(ColumnConstants.STATUS_LIST.getConstantVal(), nsdlPanVerfRequestDto.getStatusList());

				beanList = nsdlPanVerficationService.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_SCHEDULER_LAST_RUNTIME.getConstantVal());

				if (CollectionUtils.isEmpty(beanList)) {
					Logger.info("job processing id : " + jobProcessId + "No Records are available to process");
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
				}

			} else {
				Logger.info("job processing id : " + jobProcessId + "scheduler last run time not received : ");

				Map<String, Object> columnValueMap = new HashMap<>();
				columnValueMap.put(ColumnConstants.MODULE_NAME.getConstantVal(), nsdlPanVerfRequestDto.getModuleName());
				columnValueMap.put(ColumnConstants.SUB_TASK_STATUS.getConstantVal(), nsdlPanVerfRequestDto.getSubTaskStatus());
				columnValueMap.put(ColumnConstants.STATUS_LIST.getConstantVal(), nsdlPanVerfRequestDto.getStatusList());

				beanList = nsdlPanVerficationService.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_WITHOUT_SCHEDULER_LAST_RUNTIME.getConstantVal());

				if (CollectionUtils.isEmpty(beanList)) {
					Logger.info("job processing id : " + jobProcessId + " Scheduler last run time no present No Records are available ");
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
				}
			}
			//			List<NSDLPanVerif> resposeList = beanList.stream().collect(Collectors.toList());

			if (CollectionUtils.isNotEmpty(beanList)) {
				List<NSDLPanVerif> resposeList = beanList.stream().map(inner -> {
					NSDLPanVerif dto = new NSDLPanVerif();
					dto.setActualPanNumber(inner.getActualPanNumber());
					dto.setIsActive(inner.getIsActive());
					dto.setCreatedOn(inner.getCreatedOn());
					dto.setModuleName(inner.getModuleName());
					dto.setNsdlPanVerifId(inner.getNsdlPanVerifId());
					dto.setNsdlResponse(inner.getNsdlResponse());
					dto.setNsdlVerifiedOn(inner.getNsdlVerifiedOn());
					dto.setStatus(inner.getStatus());
					dto.setSubTaskStatus(inner.getSubTaskStatus());
					dto.setSupportingInfo(inner.getSupportingInfo());
					return dto;

				}).collect(Collectors.toList());

				return new ServiceResponseBuilder().setStatus(true).setResponse(resposeList).build();
			}
		} catch (Exception e) {
			Logger.error("Exception while fetching NSDL pan varify details info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		return null;

	}

	@PostMapping(value = "/processNSDLRecordsAndSendEmail")
	public ServiceResponse processNSDLRecordsAndSendEmail(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NsdlPanVerfReqRespDto nsdlPanVerfRequestDto) {

		try {
			Logger.info("Request received for process nsdl records and send email start: job processing id : " + jobProcessId);
			if (nsdlPanVerfRequestDto == null) {
				Logger.info("NSDL pan process end, null request : job processing id :" + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
			}

			if (CollectionUtils.isEmpty(nsdlPanVerfRequestDto.getNsdlPanVarifyIdList())) {
				Logger.info("NSDL pan process end, null request : job processing id :" + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
			}

			if (CollectionUtils.isEmpty(nsdlPanVerfRequestDto.getNsdlPassRecords()) && CollectionUtils.isEmpty(nsdlPanVerfRequestDto.getNsdlFailedRecords())) {
				Logger.info("NSDL pan process end, null request : job processing id : " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}

			Logger.info("NSDL pan process, Input request is valid job processing id : " + jobProcessId);

			List<NbfcCorRegistrationBean> nsdlApproveRecords = new ArrayList<>();
			List<NbfcCorRegistrationBean> nsdlRejectedRecords = new ArrayList<>();

			// processing nsdl passed records
			if (!CollectionUtils.isEmpty(nsdlPanVerfRequestDto.getNsdlPassRecords())) {
				Logger.info("NSDL pan varify NSDL pass records : " + nsdlPanVerfRequestDto.getNsdlPassRecords().size());

				Long longArray[] = nsdlPanVerfRequestDto.getNsdlPassRecords().toArray(new Long[0]);
				List<NbfcCorRegistrationBean> corRegBeanList = nbfcCorRegistrationService.getDataByIds(longArray);

				if (CollectionUtils.isNotEmpty(corRegBeanList)) {
					LabelDto LabelDto = new LabelDto();
					Map<String, String> panAndBorrowerList = createRequestedMap(corRegBeanList);
					LabelDto.setLabelKeyMap(panAndBorrowerList);
					LabelDto.setIsPanAndBorrowerRequiredInResponse(true);

					Logger.info("getting data from pan master controller start");
					ServiceResponse serviceResp = panMasterController.getPanInfoForNonXBRLReturns(jobProcessId, LabelDto);
					Logger.info("getting data from pan master controller end");
					if (serviceResp.isStatus()) {
						Gson gson = new Gson();
						@SuppressWarnings("unchecked")
						List<String> panNumSet = (List<String>) gson.fromJson((String) serviceResp.getResponse(), List.class);

						if (CollectionUtils.isNotEmpty(panNumSet)) {
							for (NbfcCorRegistrationBean bean : corRegBeanList) {
								if (panNumSet.contains(bean.getCompPan() + GeneralConstants.TILDA_SEPERATOR.getConstantVal() + bean.getCompName())) {

									String otp = getOtp(jobProcessId);
									if (StringUtils.isNotBlank(otp)) {

										bean.setStatus(CorRegistrationStatusEnum.NSDL_APPROVED.getStatus());
										bean.setOtp(AESV2.getInstance().encrypt(otp));
										String verificationToken = bean.getCompPan().concat("," + otp);
										String mailUrl = nbfcVerificationPath + AESV2.getInstance().encrypt(verificationToken);
										mailUrl = mailUrl.replaceAll("\\+", "%2B");
										bean.setVerificationUrl(mailUrl);
										nsdlApproveRecords.add(bean);
									}
								} else {
									Logger.info("Pan and borrower name mapping is not present in the pan master table : Pan number :" + bean.getCompPan() + " : Company name :" + bean.getCompName());
									Logger.info("adding pan and company name combination in nsdl rejected records : Pan Number :" + bean.getCompPan());
									bean.setStatus(CorRegistrationStatusEnum.NSDL_REJECTED.getStatus());
									nsdlRejectedRecords.add(bean);
								}
							}
						} else {
							for (NbfcCorRegistrationBean bean : corRegBeanList) {
								bean.setStatus(CorRegistrationStatusEnum.NSDL_REJECTED.getStatus());
								nsdlRejectedRecords.add(bean);
							}

						}
					}

				}
			}

			// processing nsdl failed records
			if (!CollectionUtils.isEmpty(nsdlPanVerfRequestDto.getNsdlFailedRecords())) {
				Logger.info("NSDL pan varify NSDL failed records : " + nsdlPanVerfRequestDto.getNsdlFailedRecords().size());
				Long longArray[] = nsdlPanVerfRequestDto.getNsdlFailedRecords().toArray(new Long[0]);
				List<NbfcCorRegistrationBean> corRegBeanList = nbfcCorRegistrationService.getDataByIds(longArray);

				if (CollectionUtils.isNotEmpty(corRegBeanList)) {

					for (NbfcCorRegistrationBean bean : corRegBeanList) {
						bean.setStatus(CorRegistrationStatusEnum.NSDL_REJECTED.getStatus());
						nsdlRejectedRecords.add(bean);
					}
				}
			}

			Logger.info("Send email start");
			if (CollectionUtils.isNotEmpty(nsdlRejectedRecords)) {
				nsdlPanVerfRequestDto.setNsdlFailedRecordsResponse(Long.valueOf(nsdlRejectedRecords.size()));
				nbfcCorRegistrationService.updateAll(nsdlRejectedRecords);
				prepareAndSendEmail(nsdlRejectedRecords, false, jobProcessId);
			}

			if (CollectionUtils.isNotEmpty(nsdlApproveRecords)) {
				nsdlPanVerfRequestDto.setNsdlPassedRecordsResponse(Long.valueOf(nsdlApproveRecords.size()));
				nbfcCorRegistrationService.updateAll(nsdlApproveRecords);
				prepareAndSendEmail(nsdlApproveRecords, true, jobProcessId);
			}
			Logger.info("Send email End");
			Logger.info("updatng sub task status starts");
			nsdlPanVerficationService.updateSubTaskStatus(nsdlPanVerfRequestDto.getNsdlPanVarifyIdList(), new Date());
			Logger.info("updatng sub task status end");

		} catch (Exception e) {
			Logger.error("Exception while process nsdl records info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		Logger.info("End of process sending true response");
		return new ServiceResponseBuilder().setStatus(true).setResponse(nsdlPanVerfRequestDto).build();

	}

	private String getOtp(String jobProcessId) {

		// get OTP from login project
		try {

			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.GET_OTP_FOR_NBFC_REG.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_GET);

			Map<String, String> headerMap = new HashMap<>();

			headerMap.put(GeneralConstants.UUID.getConstantVal(), jobProcessId);

			String responseString = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, null, null, headerMap);

			ServiceResponse serviceResponseNew = JsonUtility.getGsonObject().fromJson(responseString, ServiceResponse.class);

			return serviceResponseNew.getResponse().toString();//
		} catch (Exception e) {
			Logger.error("Exception occoured while getting OTP : " + e);

		}
		return null;
	}

	private void prepareAndSendEmail(List<NbfcCorRegistrationBean> nsdlRecords, boolean isApproved, String jobProcessId) {

		Long emailAlertId = 96l;
		String languageCode = "en";
		List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
		MailServiceBean mailServiceBean = null;
		try {
			for (NbfcCorRegistrationBean nbfcCorRegBean : nsdlRecords) {
				mailServiceBean = new MailServiceBean();
				List<DynamicContent> dynamicContents = new ArrayList<>();

				DynamicContent dynamicContent = new DynamicContent();

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reg.nbfcpan"));
				dynamicContent.setValue(nbfcCorRegBean.getCompPan());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reg.nbfcname"));
				dynamicContent.setValue(nbfcCorRegBean.getCompName());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reg.nbfcemail"));
				dynamicContent.setValue(nbfcCorRegBean.getCompEmailId());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reg.nbfcphone"));
				dynamicContent.setValue(nbfcCorRegBean.getCompPhone());
				dynamicContents.add(dynamicContent);

				if (isApproved) {

					dynamicContent = new DynamicContent();
					dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.panMngt.NSDLVerifyStatus"));
					dynamicContent.setValue(nbfcCorRegBean.getStatus());
					dynamicContents.add(dynamicContent);

					dynamicContent = new DynamicContent();
					dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reg.nbfc.link"));
					dynamicContent.setValue(nbfcCorRegBean.getVerificationUrl());
					dynamicContents.add(dynamicContent);
				} else {
					dynamicContent = new DynamicContent();
					dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.panMngt.NSDLVerifyStatus"));
					dynamicContent.setValue(nbfcCorRegBean.getStatus());
					dynamicContents.add(dynamicContent);
				}

				mailServiceBean.setDynamicContentsList(dynamicContents);
				mailServiceBean.setAlertId(emailAlertId);
				mailServiceBean.setEntityCode(null);
				mailServiceBean.setNbfcEntityBeanFk(nbfcCorRegBean.getCorRegistrationId());
				Map<Integer, List<String>> emailMap = new HashMap<>();
				List<String> emailIds = new ArrayList<>();
				emailIds.add(nbfcCorRegBean.getCompEmailId());
				emailMap.put(1, emailIds);
				mailServiceBean.setEmailMap(emailMap);

				mailServiceBeanList.add(mailServiceBean);
			}
			ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(jobProcessId, mailServiceBeanList);
			if (serviceResponse.isStatus()) {
				Logger.info("Mail sent successfully");
			}
		} catch (Exception e) {

		}

	}

	private WebServiceComponentUrl getWebServiceComponentURL(String componentName, String methodType) {

		Map<String, List<String>> valueMap = new HashMap<>();
		List<String> valueList = new ArrayList<>();
		valueList.add(componentName);
		valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

		valueList = new ArrayList<>();
		valueList.add(methodType);
		valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

		WebServiceComponentUrl componentUrl = null;
		try {
			componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);
		} catch (ServiceException e) {
			Logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return componentUrl;
	}

	private Map<String, String> createRequestedMap(List<NbfcCorRegistrationBean> corRegBeanList) {

		Map<String, String> responseMap = new HashMap<>();
		for (NbfcCorRegistrationBean bean : corRegBeanList) {
			responseMap.put(bean.getCompPan() + GeneralConstants.TILDA_SEPERATOR.getConstantVal() + bean.getCompName(), bean.getCompPan() + GeneralConstants.TILDA_SEPERATOR.getConstantVal() + bean.getCompName());

		}

		return responseMap;
	}

}
