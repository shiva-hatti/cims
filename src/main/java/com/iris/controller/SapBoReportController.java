package com.iris.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.CategoryInfo;
import com.iris.dto.SapBoDeatilsBean;
import com.iris.dto.SapBoDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.EntityBean;
import com.iris.model.EntityLabelBean;
import com.iris.model.HiveDimBank;
import com.iris.model.HiveDimIndianAgent;
import com.iris.model.HiveDimNbfc;
import com.iris.model.HiveDimPrimaryDealer;
import com.iris.model.Return;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.SapBoDetails;
import com.iris.repository.EntityRepo;
import com.iris.repository.HiveDimBankRepo;
import com.iris.repository.HiveDimIndianAgentRepo;
import com.iris.repository.HiveDimNbfcRepo;
import com.iris.repository.HiveDimPrimaryDealerRepo;
import com.iris.repository.ReturnRepo;
import com.iris.repository.ReturnUploadDetailsRepository;
import com.iris.repository.SapBoReportRepo;
import com.iris.service.impl.ReturnUploadDetailsService;
import com.iris.service.impl.SapBoReportService;
import com.iris.util.UtilMaster;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author psawant
 * @version 1.0
 * @date 10/09/2020
 */
@RestController
@RequestMapping("/service/sapBoReport")
public class SapBoReportController {

	static final Logger logger = LogManager.getLogger(SapBoReportController.class);

	@Autowired
	private SapBoReportService sapBoReportService;

	@Autowired
	private ReturnRepo returnRepo;

	@Autowired
	private EntityRepo entityRepo;

	@Autowired
	private SapBoReportRepo sapBoReportRepo;

	@Autowired
	private HiveDimBankRepo hiveDimBankRepo;

	@Autowired
	private HiveDimIndianAgentRepo hiveDimIndianAgentRepo;

	@Autowired
	private HiveDimPrimaryDealerRepo hiveDimPrimaryDealerRepo;

	@Autowired
	private HiveDimNbfcRepo hiveDimNbfcRepo;

	@Autowired
	private ReturnUploadDetailsService returnUploadDetailsService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ReturnUploadDetailsRepository returnUploadDetailsRepository;

	@GetMapping(value = "/getAllSapBoDetails")
	public ServiceResponse getAllSapBoDetails(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId) {
		try {
			logger.info("getAllSapBoDetails method started");
			List<SapBoDetails> sapBoDetailsList = sapBoReportService.getAllDataFor(null, null);
			SapBoDeatilsBean sapBoDeatilsBean = new SapBoDeatilsBean();
			List<SapBoDeatilsBean> sapBoDeatilsBeanList = new ArrayList<>();
			for (SapBoDetails sapBoDetails : sapBoDetailsList) {
				sapBoDeatilsBean = new SapBoDeatilsBean();
				sapBoDeatilsBean.setUsername(sapBoDetails.getUsername());
				sapBoDeatilsBean.setAuthenticateUrl(sapBoDetails.getAuthenticateUrl());
				sapBoDeatilsBean.setBankNameKey(sapBoDetails.getBankNameKey());
				sapBoDeatilsBean.setiDocIdKey(sapBoDetails.getiDocIdKey());
				sapBoDeatilsBean.setPassword(sapBoDetails.getPassword());
				sapBoDeatilsBean.setPeriodEndedKey(sapBoDetails.getPeriodEndedKey());
				sapBoDeatilsBean.setSerSessionKey(sapBoDetails.getSerSessionKey());
				sapBoDeatilsBean.setReportUrl(sapBoDetails.getReportUrl());
				sapBoDeatilsBean.setIdentifier(sapBoDetails.getIdentifier());
				if (!Validations.isEmpty(sapBoDetails.getPeriodStartKey())) {
					sapBoDeatilsBean.setPeriodStartKey(sapBoDetails.getPeriodStartKey());
				}
				if (!Validations.isEmpty(sapBoDetails.getReturnCode())) {
					sapBoDeatilsBean.setReturnCode(sapBoDetails.getReturnCode());
				}
				if (sapBoDetails.getUpdatedBy() != null) {
					sapBoDeatilsBean.setUploadedBy(sapBoDetails.getUpdatedBy().getUserName());
				}
				if (sapBoDetails.getUpdatedOn() != null) {
					sapBoDeatilsBean.setUploadedDate(sapBoDetails.getUpdatedOn());
				}
				sapBoDeatilsBean.setPkId(sapBoDetails.getSapBoDetailsId());
				sapBoDeatilsBeanList.add(sapBoDeatilsBean);
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(sapBoDeatilsBeanList)).build();
		} catch (Exception e) {
			logger.error("getAllSapBoDetails method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setResponse(null).build();
		}
	}

	@PostMapping(value = "/updateSapBoDetails")
	public ServiceResponse updateSapBoDetails(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody SapBoDetails sapBoDetailsObj) {
		logger.info("updateSapBoDetails method started");
		try {
			SapBoDetails sapBoDeatils = sapBoReportService.getDataById(sapBoDetailsObj.getSapBoDetailsId());
			sapBoDeatils.setUsername(sapBoDetailsObj.getUsername());
			sapBoDeatils.setPassword(sapBoDetailsObj.getPassword());
			sapBoDeatils.setAuthenticateUrl(sapBoDetailsObj.getAuthenticateUrl());
			sapBoDeatils.setBankNameKey(sapBoDetailsObj.getBankNameKey());
			sapBoDeatils.setiDocIdKey(sapBoDetailsObj.getiDocIdKey());
			sapBoDeatils.setPeriodEndedKey(sapBoDetailsObj.getPeriodEndedKey());
			sapBoDeatils.setSerSessionKey(sapBoDetailsObj.getSerSessionKey());
			sapBoDeatils.setReportUrl(sapBoDetailsObj.getReportUrl());
			if (!Validations.isEmpty(sapBoDetailsObj.getPeriodStartKey())) {
				sapBoDeatils.setPeriodStartKey(sapBoDetailsObj.getPeriodStartKey());
			} else {
				sapBoDeatils.setPeriodStartKey(null);
			}
			if (!Validations.isEmpty(sapBoDetailsObj.getReturnCode())) {
				sapBoDeatils.setReturnCode(sapBoDetailsObj.getReturnCode());
			} else {
				sapBoDeatils.setReturnCode(null);
			}
			sapBoDeatils.setIdentifier(sapBoDetailsObj.getIdentifier());
			sapBoDeatils.setUpdatedBy(sapBoDetailsObj.getUpdatedBy());
			sapBoDeatils.setUpdatedOn(DateManip.getCurrentDateTime());
			sapBoReportService.update(sapBoDeatils);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(sapBoDetailsObj)).build();
		} catch (Exception e) {
			logger.error("updateSapBoDetails method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setResponse(null).build();
		}
	}

	@PostMapping(value = "/getSapBoReportDetails/{returnCode}/{entityCode}")
	public ServiceResponse getSapBoReportDetails(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @PathVariable String returnCode, @PathVariable String entityCode) {
		logger.info("getSapBoReportDetails method started " + returnCode + "_" + entityCode);
		try {
			if (Validations.isEmpty(returnCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0784.toString())).build();
			}
			if (Validations.isEmpty(entityCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0183.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0183.toString())).build();
			}

			List<SapBoDetails> sapBoDetailsList = sapBoReportRepo.getAllSadpDataWithReturnCode(GeneralConstants.SADP_IDENTIFIER.getConstantVal());
			String returnCodes = null;
			List<String> returnCodesList = null;
			SapBoDeatilsBean sapBoDeatilsBean = new SapBoDeatilsBean();
			SapBoDetails sapBoDetails = null;
			for (SapBoDetails sapBoObj : sapBoDetailsList) {
				if (!Validations.isEmpty(sapBoObj.getReturnCode())) {
					returnCodes = sapBoObj.getReturnCode();
					returnCodesList = Stream.of(returnCodes.split(",")).map(String::valueOf).collect(Collectors.toList());

					if (!returnCodesList.contains(returnCode)) {
						continue;
					}
					sapBoDetails = sapBoObj;
					break;
				}
			}
			if (sapBoDetails == null) {
				sapBoDetails = sapBoReportRepo.getAllSadpData(GeneralConstants.SADP_IDENTIFIER.getConstantVal());
			}
			sapBoDeatilsBean.setUsername(sapBoDetails.getUsername());
			sapBoDeatilsBean.setPassword(sapBoDetails.getPassword());
			sapBoDeatilsBean.setAuthenticateUrl(sapBoDetails.getAuthenticateUrl());
			sapBoDeatilsBean.setReportUrl(sapBoDetails.getReportUrl());
			sapBoDeatilsBean.setiDocIdKey(sapBoDetails.getiDocIdKey());
			sapBoDeatilsBean.setPeriodEndedKey(sapBoDetails.getPeriodEndedKey());
			sapBoDeatilsBean.setBankNameKey(sapBoDetails.getBankNameKey());
			sapBoDeatilsBean.setSerSessionKey(sapBoDetails.getSerSessionKey());

			if (!Validations.isEmpty(sapBoDetails.getPeriodStartKey())) {
				sapBoDeatilsBean.setPeriodStartKey(sapBoDetails.getPeriodStartKey());
			}

			String entLevel = "";
			EntityBean entityBean = entityRepo.findByEntityCode(entityCode);
			if (!Validations.isEmpty(entityBean.getOpLevel1())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel1(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(entityBean.getOpLevel2())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel2(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(entityBean.getOpLevel3())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel3(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(entityBean.getOpLevel4())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel4(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(entityBean.getOpLevel5())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel5(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			for (EntityLabelBean entLblObj : entityBean.getEntityLabelSet()) {
				if (entLblObj.getLanguageMaster().getLanguageCode().equals(GeneralConstants.DEFAULT_LANG_CODE.getConstantVal())) {
					sapBoDeatilsBean.setEntityName(entLevel + URLEncoder.encode(entLblObj.getEntityNameLabel(), StandardCharsets.UTF_8.toString()));
					break;
				}
			}
			sapBoDeatilsBean.setEntityName(entLevel);
			sapBoDeatilsBean.setEntityCode(entityCode);

			Return returnObj = returnRepo.findByReturnCode(returnCode);
			boolean catFlag = false;
			if (!Validations.isEmpty(returnObj.getReturnDocId())) {
				try {
					SapBoDto sapBoDto = new Gson().fromJson(returnObj.getReturnDocId(), SapBoDto.class);
					if (!CollectionUtils.isEmpty(sapBoDto.getCategoryInfo())) {
						for (CategoryInfo catObj : sapBoDto.getCategoryInfo()) {
							if (catObj.getCategoryCode().equalsIgnoreCase(entityBean.getCategory().getCategoryCode())) {
								boolean contains = Arrays.stream(catObj.getSubCatInfo()).anyMatch(entityBean.getSubCategory().getSubCategoryCode()::equals);
								if (contains) {
									if (!Validations.isEmpty(catObj.getiDocId())) {
										catFlag = true;
										sapBoDeatilsBean.setReturnDocId(catObj.getiDocId());
										logger.info("If category and sub category both matches then iDocId is: " + catObj.getiDocId());
									} else {
										logger.info("If category and sub category both matches but iDocId is null");
									}
									break;
								} else {
									if (!Validations.isEmpty(catObj.getGenericIDocId())) {
										catFlag = true;
										sapBoDeatilsBean.setReturnDocId(catObj.getGenericIDocId());
										logger.info("If category matches and sub category not matches then iDocId is: " + catObj.getGenericIDocId());
									} else {
										logger.info("If category matches and sub category not matches and iDocId null");
									}
								}
							}
						}
						if (!catFlag) {
							sapBoDeatilsBean.setReturnDocId(sapBoDto.getGenericIDocId());
						}
					} else {
						sapBoDeatilsBean.setReturnDocId(sapBoDto.getGenericIDocId());
					}
				} catch (Exception e) {
					logger.debug("error in sapBoDto object while converting json to bean ");
					sapBoDeatilsBean.setReturnDocId(returnObj.getReturnDocId());
					logger.info("If category list empty then iDocId is: " + returnObj.getReturnDocId());
				}
			}
			sapBoDeatilsBean.setReturnCode(returnCode);

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(sapBoDeatilsBean)).build();
		} catch (Exception e) {
			logger.error("getSapBoReportDetails method end ", e);
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E1660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1660.toString())).build();
		}
	}

	@PostMapping(value = "/getSapBoReportDetails/V2/{returnCode}/{entityCode}/{endDate}")
	public ServiceResponse getSapBoReportDetailsV2(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @PathVariable String returnCode, @PathVariable String entityCode, @PathVariable String endDate) {
		logger.info("getSapBoReportDetails method started " + returnCode + "_" + entityCode);
		try {
			if (Validations.isEmpty(returnCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0784.toString())).build();
			}
			if (Validations.isEmpty(entityCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0183.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0183.toString())).build();
			}

			List<SapBoDetails> sapBoDetailsList = sapBoReportRepo.getAllSadpDataWithReturnCode(GeneralConstants.SADP_IDENTIFIER.getConstantVal());
			String returnCodes = null;
			List<String> returnCodesList = null;
			SapBoDeatilsBean sapBoDeatilsBean = new SapBoDeatilsBean();
			SapBoDetails sapBoDetails = null;
			for (SapBoDetails sapBoObj : sapBoDetailsList) {
				if (!Validations.isEmpty(sapBoObj.getReturnCode())) {
					returnCodes = sapBoObj.getReturnCode();
					returnCodesList = Stream.of(returnCodes.split(",")).map(String::valueOf).collect(Collectors.toList());

					if (!returnCodesList.contains(returnCode)) {
						continue;
					}
					sapBoDetails = sapBoObj;
					break;
				}
			}
			if (sapBoDetails == null) {
				sapBoDetails = sapBoReportRepo.getAllSadpData(GeneralConstants.SADP_IDENTIFIER.getConstantVal());
			}
			sapBoDeatilsBean.setUsername(sapBoDetails.getUsername());
			sapBoDeatilsBean.setPassword(sapBoDetails.getPassword());
			sapBoDeatilsBean.setAuthenticateUrl(sapBoDetails.getAuthenticateUrl());
			sapBoDeatilsBean.setReportUrl(sapBoDetails.getReportUrl());
			sapBoDeatilsBean.setiDocIdKey(sapBoDetails.getiDocIdKey());
			sapBoDeatilsBean.setPeriodEndedKey(sapBoDetails.getPeriodEndedKey());
			sapBoDeatilsBean.setBankNameKey(sapBoDetails.getBankNameKey());
			sapBoDeatilsBean.setSerSessionKey(sapBoDetails.getSerSessionKey());

			if (!Validations.isEmpty(sapBoDetails.getPeriodStartKey())) {
				sapBoDeatilsBean.setPeriodStartKey(sapBoDetails.getPeriodStartKey());
			}

			Date endDt = null;
			endDate = endDate.replaceAll("~", "/");
			String endD = DateManip.formatDate(endDate, "dd/MM/yyyy", "yyyy-MM-dd");
			endDt = DateManip.convertStringToDate(endD, "yyyy-MM-dd");

			// fetch entity name as per effective dates
			HiveDimBank hiveDimBankObj = hiveDimBankRepo.getEntityByEntityCode(entityCode, endDt);
			String entLevel = "";
			if (hiveDimBankObj != null) {
				entLevel = getEntityName(hiveDimBankObj.getOpLevel1(), hiveDimBankObj.getOpLevel2(), hiveDimBankObj.getOpLevel3(), hiveDimBankObj.getOpLevel4(), hiveDimBankObj.getOpLevel5(), hiveDimBankObj.getBankName());
			} else {
				HiveDimIndianAgent hiveDimIndianAgentObj = hiveDimIndianAgentRepo.getEntityByEntityCode(entityCode, endDt);
				if (hiveDimIndianAgentObj != null) {
					entLevel = getEntityName(null, null, null, null, null, hiveDimIndianAgentObj.getIndianAgentName());
				} else {
					HiveDimNbfc hiveDimNbfcObj = hiveDimNbfcRepo.getEntityByEntityCode(entityCode, endDt);
					if (hiveDimNbfcObj != null) {
						entLevel = getEntityName(hiveDimNbfcObj.getOpLevel1(), hiveDimNbfcObj.getOpLevel2(), hiveDimNbfcObj.getOpLevel3(), hiveDimNbfcObj.getOpLevel4(), hiveDimNbfcObj.getOpLevel5(), hiveDimNbfcObj.getNbfcName());
					} else {
						HiveDimPrimaryDealer hiveDimPrimaryDealerObj = hiveDimPrimaryDealerRepo.getEntityByEntityCode(entityCode, endDt);
						if (hiveDimPrimaryDealerObj != null) {
							entLevel = getEntityName(null, null, null, null, null, hiveDimPrimaryDealerObj.getPrimaryDealerName());
						}
					}
				}
			}

			EntityBean entityBean = entityRepo.findByEntityCode(entityCode);
			sapBoDeatilsBean.setEntityName(entLevel);
			sapBoDeatilsBean.setEntityCode(entityCode);

			Return returnObj = returnRepo.findByReturnCode(returnCode);
			boolean catFlag = false;
			if (!Validations.isEmpty(returnObj.getReturnDocId())) {
				try {
					SapBoDto sapBoDto = new Gson().fromJson(returnObj.getReturnDocId(), SapBoDto.class);
					if (!CollectionUtils.isEmpty(sapBoDto.getCategoryInfo())) {
						for (CategoryInfo catObj : sapBoDto.getCategoryInfo()) {
							if (catObj.getCategoryCode().equalsIgnoreCase(entityBean.getCategory().getCategoryCode())) {
								boolean contains = Arrays.stream(catObj.getSubCatInfo()).anyMatch(entityBean.getSubCategory().getSubCategoryCode()::equals);
								if (contains) {
									if (!Validations.isEmpty(catObj.getiDocId())) {
										catFlag = true;
										sapBoDeatilsBean.setReturnDocId(catObj.getiDocId());
										logger.info("If category and sub category both matches then iDocId is: " + catObj.getiDocId());
									} else {
										logger.info("If category and sub category both matches but iDocId is null");
									}
									break;
								} else {
									if (!Validations.isEmpty(catObj.getGenericIDocId())) {
										catFlag = true;
										sapBoDeatilsBean.setReturnDocId(catObj.getGenericIDocId());
										logger.info("If category matches and sub category not matches then iDocId is: " + catObj.getGenericIDocId());
									} else {
										logger.info("If category matches and sub category not matches and iDocId null");
									}
								}
							}
						}
						if (!catFlag) {
							sapBoDeatilsBean.setReturnDocId(sapBoDto.getGenericIDocId());
						}
					} else {
						sapBoDeatilsBean.setReturnDocId(sapBoDto.getGenericIDocId());
					}
				} catch (Exception e) {
					logger.debug("error in sapBoDto object while converting json to bean ");
					sapBoDeatilsBean.setReturnDocId(returnObj.getReturnDocId());
					logger.info("If category list empty then iDocId is: " + returnObj.getReturnDocId());
				}
			}
			sapBoDeatilsBean.setReturnCode(returnCode);

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(sapBoDeatilsBean)).build();
		} catch (Exception e) {
			logger.error("getSapBoReportDetails method end ", e);
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E1660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1660.toString())).build();
		}
	}

	private String getEntityName(String opLevel1, String opLevel2, String opLevel3, String opLevel4, String opLevel5, String bankName) {
		String entName = "";
		try {
			String entLevel = "";
			if (!Validations.isEmpty(opLevel1)) {
				entLevel += URLEncoder.encode(opLevel1, StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(opLevel2)) {
				entLevel += URLEncoder.encode(opLevel2, StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(opLevel3)) {
				entLevel += URLEncoder.encode(opLevel3, StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(opLevel4)) {
				entLevel += URLEncoder.encode(opLevel4, StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(opLevel5)) {
				entLevel += URLEncoder.encode(opLevel5, StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			entName = entLevel + URLEncoder.encode(bankName, StandardCharsets.UTF_8.toString());
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return null;
		}
		return entName;
	}

	@PostMapping(value = "/getSapBoReportDetailsForEBRPilot/{returnCode}/{entityCode}")
	public ServiceResponse getSapBoReportDetailsForEBRPilot(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @PathVariable String returnCode, @PathVariable String entityCode) {
		logger.info("getSapBoReportDetailsForEBRPilot method started " + returnCode + "_" + entityCode);
		try {
			String idocId;
			if (Validations.isEmpty(returnCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0784.toString())).build();
			}

			List<SapBoDetails> sapBoDetailsList = sapBoReportRepo.getAllSadpDataWithReturnCode(GeneralConstants.SADP_IDENTIFIER.getConstantVal());
			String returnCodes = null;
			List<String> returnCodesList = null;
			SapBoDeatilsBean sapBoDeatilsBean = new SapBoDeatilsBean();
			SapBoDetails sapBoDetails = null;
			for (SapBoDetails sapBoObj : sapBoDetailsList) {
				if (!Validations.isEmpty(sapBoObj.getReturnCode())) {
					returnCodes = sapBoObj.getReturnCode();
					returnCodesList = Stream.of(returnCodes.split(",")).map(String::valueOf).collect(Collectors.toList());

					if (!returnCodesList.contains(returnCode)) {
						continue;
					}
					sapBoDetails = sapBoObj;
					break;
				}
			}
			if (sapBoDetails == null) {
				sapBoDetails = sapBoReportRepo.getAllSadpData(GeneralConstants.SADP_IDENTIFIER.getConstantVal());
			}

			idocId = getIdocId(returnCode);

			sapBoDeatilsBean.setUsername(sapBoDetails.getUsername());
			sapBoDeatilsBean.setPassword(sapBoDetails.getPassword());
			sapBoDeatilsBean.setAuthenticateUrl(sapBoDetails.getAuthenticateUrl());
			sapBoDeatilsBean.setReportUrl(sapBoDetails.getReportUrl());
			sapBoDeatilsBean.setiDocIdKey(sapBoDetails.getiDocIdKey());
			sapBoDeatilsBean.setPeriodEndedKey(sapBoDetails.getPeriodEndedKey());
			sapBoDeatilsBean.setBankNameKey(sapBoDetails.getBankNameKey());
			sapBoDeatilsBean.setSerSessionKey(sapBoDetails.getSerSessionKey());
			if (idocId != null) {
				sapBoDeatilsBean.setReturnDocId(idocId);
			}
			if (!Validations.isEmpty(sapBoDetails.getPeriodStartKey())) {
				sapBoDeatilsBean.setPeriodStartKey(sapBoDetails.getPeriodStartKey());
			}

			String entLevel = "";
			EntityBean entityBean = entityRepo.findByEntityCode(entityCode);
			if (!Validations.isEmpty(entityBean.getOpLevel1())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel1(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(entityBean.getOpLevel2())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel2(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(entityBean.getOpLevel3())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel3(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(entityBean.getOpLevel4())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel4(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			if (!Validations.isEmpty(entityBean.getOpLevel5())) {
				entLevel += URLEncoder.encode(entityBean.getOpLevel5(), StandardCharsets.UTF_8.toString()) + GeneralConstants.ENT_LEVEL_SEPERATOR.getConstantVal();
			}
			sapBoDeatilsBean.setEntityName(entLevel + URLEncoder.encode(entityBean.getEntityName(), StandardCharsets.UTF_8.toString()));

			sapBoDeatilsBean.setEntityCode(entityCode);
			sapBoDeatilsBean.setReturnCode(returnCode);

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(sapBoDeatilsBean)).build();
		} catch (Exception e) {
			logger.error("getSapBoReportDetailsForEBRPilot method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setResponse(null).build();
		}
	}

	@PostMapping(value = "/checkLatestFilingForSapBoReport/{entityCode}/{uploadId}/{langCode}")
	public ServiceResponse checkLatestFilingForSapBoReport(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @PathVariable String entityCode, @PathVariable Long uploadId, @PathVariable String langCode) {
		logger.info("checkLatestFilingForSapBoReport method started " + uploadId + "_" + entityCode);
		try {
			if (UtilMaster.isEmpty(entityCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0183.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0183.toString())).build();
			}

			ReturnsUploadDetails returnsUploadDetailsObj = returnUploadDetailsRepository.findByEntityCodeUploadId(uploadId, entityCode);
			if (returnsUploadDetailsObj == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}

			Map<String, List<String>> valueMap = new HashMap<>();

			EntityBean entityBean = entityRepo.findByEntityCode(entityCode);
			List<String> valueList = new ArrayList<>();
			valueList.add(entityBean.getEntityId() + "");
			valueMap.put(ColumnConstants.ENTITYID.getConstantVal(), valueList);

			Return returnObj = returnRepo.findByReturnCode(returnsUploadDetailsObj.getReturnObj().getReturnCode());
			valueList = new ArrayList<>();
			valueList.add(returnObj.getReturnId() + "");
			valueMap.put(ColumnConstants.RETURNID.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getStartDate() + "");
			valueMap.put(ColumnConstants.STARTDT.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEndDate() + "");
			valueMap.put(ColumnConstants.ENDDT.getConstantVal(), valueList);

			List<ReturnsUploadDetails> returnsUploadDetailsList = returnUploadDetailsService.getDataByColumnValue(valueMap, MethodConstants.GET_FILING_RETURN_ENTITY_WISE.getConstantVal());

			if (CollectionUtils.isEmpty(returnsUploadDetailsList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1658.toString())).setResponse(null).build();
			}
			SapBoDeatilsBean sapBoDeatilsBean = new SapBoDeatilsBean();
			sapBoDeatilsBean.setUploadId(returnsUploadDetailsList.get(0).getUploadId());
			sapBoDeatilsBean.setUploadedDate(returnsUploadDetailsList.get(0).getUploadedDate());

			if (returnsUploadDetailsList.get(0).getUploadId().equals(uploadId)) {
				return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(sapBoDeatilsBean)).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1659.toString()).setStatusMessage(returnsUploadDetailsList.get(0).getUploadId() + ObjectCache.getLabelKeyValue(langCode, ObjectCache.getErrorCodeKey(ErrorCode.E1659.toString())) + " " + DateManip.convertDateToString(sapBoDeatilsBean.getUploadedDate(), ObjectCache.getDateFormat() + " " + ObjectCache.getTimeFormat())).setResponse(uploadId + ObjectCache.getLabelKeyValue(langCode, ObjectCache.getErrorCodeKey(ErrorCode.E1659.toString())) + " " + DateManip.convertDateToString(sapBoDeatilsBean.getUploadedDate(), ObjectCache.getDateFormat() + " " + ObjectCache.getTimeFormat())).build();
			}
		} catch (Exception e) {
			logger.error("checkLatestFilingForSapBoReport method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1658.toString())).setResponse(null).build();
		}
	}

	public String getIdocId(String returnCode) {
		ResultSet rs = null;
		String idocId = null;
		String query = "SELECT * FROM TBL_SDMX_IDOC_ID WHERE RETURN_CODE=?";
		try (Connection connection = dataSource.getConnection(); PreparedStatement pstmt = connection.prepareStatement(query);) {
			pstmt.setString(1, returnCode);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				idocId = rs.getString("IDOC_ID");
			}

		} catch (Exception e) {
			logger.error("Exception : ", e);
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("Exception : ", e);
					idocId = null;
				}
			}
		}
		return idocId;
	}
}