package com.iris.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.NbfcEntityProfileDetailsData;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.NbfcCategory;
import com.iris.model.NbfcClassification;
import com.iris.model.NbfcEntityProfileDetails;
import com.iris.model.NbfcProfileAllDetailsBeanDto;
import com.iris.model.NbfcProfileDetailsBean;
import com.iris.model.NbfcProfileDetailsPage1Dto;
import com.iris.model.NbfcProfileDetailsPage2Dto;
import com.iris.model.NbfcProfileDetailsPage3Dto;
import com.iris.model.NbfcProfileDetailsTempBean;
import com.iris.model.NbfcSubsidiaryAssociateGroupBean;
import com.iris.model.UserMaster;
import com.iris.nbfc.model.NbfcCertificationDetailsDto;
import com.iris.repository.EntityRepo;
import com.iris.repository.NbfcCategoryRepo;
import com.iris.repository.NbfcClassificationRepo;
import com.iris.repository.NbfcProfileDetailsTempRepo;
import com.iris.repository.NbfcSubsidiaryAssociateGroupRepo;
import com.iris.repository.NbfcProfileDetailsRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.service.impl.NbfcEntityProfileDetailsService;
import com.iris.util.UtilMaster;
import com.iris.util.constant.ErrorCode;

@RestController
@RequestMapping("/service/nbfcEditProfService")
public class NBFCEditProfilePageController {
	private static final Logger logger = LogManager.getLogger(NBFCEditProfilePageController.class);

	@Autowired
	private NbfcProfileDetailsRepo nbfcProfileDetailsRepo;

	@Autowired
	private NbfcProfileDetailsTempRepo nbfcProfileDetailsTempRepo;

	@Autowired
	private NbfcClassificationRepo nbfcClassificationRepo;

	@Autowired
	private NbfcCategoryRepo nbfcCategoryRepo;

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EntityRepo entityRepo;

	@Autowired
	private NbfcSubsidiaryAssociateGroupRepo nbfcSubsidiaryAssociateGroupRepo;

	@Autowired
	private NbfcEntityProfileDetailsService nbfcEntityProfileDetailsService;

	//Get Record from base upon entity code condition with isActive = 1 (Actual SUBMITTED DATA RETURN)
	@PostMapping(value = "/getActualDataSubmitUponEntity/{entityCode}")
	public ServiceResponse getActualDataSubmitUponEntity(@PathVariable("entityCode") String entityCode) {
		try {
			logger.info("request received to get nbfc edit profile page record");
			if (UtilMaster.isEmpty(entityCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}
			NbfcProfileDetailsBean nbfcProfileDetailsBean = nbfcProfileDetailsRepo.getNbfcProfileDetails(entityCode);
			if (nbfcProfileDetailsBean == null) {
				return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
			}
			Gson gson = new Gson();
			NbfcProfileAllDetailsBeanDto nbfcProfileDetailsBeanDto = gson.fromJson(nbfcProfileDetailsBean.getProfilePageJson(), NbfcProfileAllDetailsBeanDto.class);
			return new ServiceResponseBuilder().setStatus(true).setResponse(nbfcProfileDetailsBeanDto).build();

		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	//Get getNbfcEntityProfileDetails Record from base upon entity code 
	@PostMapping(value = "/getNbfcEntityProfileDetails/{entityCode}")
	public ServiceResponse getNbfcEntityProfileDetails(@PathVariable("entityCode") String entityCode) {
		try {
			logger.info("request received to get nbfc edit profile record which getting nbfc record from RBI ");
			if (UtilMaster.isEmpty(entityCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}
			EntityBean entity = entityRepo.findByEntityCode(entityCode);
			NbfcEntityProfileDetails nbfcEntityProfileDetails = nbfcEntityProfileDetailsService.getDataById(entity.getEntityId());
			if (nbfcEntityProfileDetails == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1493.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1493.toString())).build();
			}
			NbfcEntityProfileDetailsData nbfcEntityProfileDetailsData = new NbfcEntityProfileDetailsData();
			nbfcEntityProfileDetailsData.setCorNumber(nbfcEntityProfileDetails.getCorNumber());
			nbfcEntityProfileDetailsData.setCrarAssetRatio(nbfcEntityProfileDetails.getCrarAssetRatio());
			nbfcEntityProfileDetailsData.setDateInCorporatation(nbfcEntityProfileDetails.getDateInCorporatation().getTime());
			nbfcEntityProfileDetailsData.setNbfcCinNumber(nbfcEntityProfileDetails.getNbfcCinNumber());
			nbfcEntityProfileDetailsData.setCorIssueDate(nbfcEntityProfileDetails.getCorIssueDate().getTime());
			nbfcEntityProfileDetailsData.setPanNumber(nbfcEntityProfileDetails.getPanNumber());
			return new ServiceResponseBuilder().setStatus(true).setResponse(nbfcEntityProfileDetailsData).build();

		} catch (Exception e) {
			logger.error("request received to get nbfc edit profile record which getting nbfc record from RBI " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	//Get Save Record from base upon entity code, page no condition with isActive = 1 and isSubmit = 0 (Actual SUBMITTED DATA RETURN)
	@RequestMapping(value = "/getSaveRecordFromNBFCEditProfile/{entityId}/{pageNo}", method = RequestMethod.GET)
	public ServiceResponse getSaveRecordFromNBFCEditProfile(@PathVariable("entityId") Long entityId, @PathVariable("pageNo") Long pageNo) {
		try {

			logger.info("request received to get temp nbfc edit profile page record");
			if (entityId == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}

			if (pageNo == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1494.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1494.toString())).build();
			}
			NbfcProfileDetailsTempBean nbfcProfileDetailsBean = nbfcProfileDetailsTempRepo.getNbfcProfileDetailsTemp(entityId, pageNo);

			if (nbfcProfileDetailsBean != null) {
				NbfcCertificationDetailsDto nbfcCertificationDetailsDto = new NbfcCertificationDetailsDto();
				nbfcCertificationDetailsDto.setFormPageJsonValue(nbfcProfileDetailsBean.getProfilePageJson());
				return new ServiceResponseBuilder().setStatus(true).setResponse(nbfcCertificationDetailsDto).build();
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	//Save Temp record into temp table
	@PostMapping(value = "/saveTempNbfcProfileData")
	public ServiceResponse saveTempNbfcProfileData(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody NbfcCertificationDetailsDto nbfcCertificationDetailsDto) {
		try {
			logger.info("request received to get temp nbfc edit profile page record");
			if (nbfcCertificationDetailsDto == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
			}
			NbfcProfileDetailsTempBean nbfcProfileDetailsBean = null;
			nbfcProfileDetailsBean = nbfcProfileDetailsTempRepo.getNbfcProfileDetailsForNbfc(nbfcCertificationDetailsDto.getCorRegIdFk(), nbfcCertificationDetailsDto.getPageNo());
			if (nbfcProfileDetailsBean == null) {
				nbfcProfileDetailsBean = new NbfcProfileDetailsTempBean();
				EntityBean entityBean = new EntityBean();
				entityBean.setEntityId(nbfcCertificationDetailsDto.getCorRegIdFk());
				nbfcProfileDetailsBean.setEntityBean(entityBean);
				nbfcProfileDetailsBean.setPageNumber(nbfcCertificationDetailsDto.getPageNo());
			}
			nbfcProfileDetailsBean.setIsActive(true);
			nbfcProfileDetailsBean.setIsSubmitted(false);
			nbfcProfileDetailsBean.setProfilePageJson(nbfcCertificationDetailsDto.getFormPageJsonValue());
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(nbfcCertificationDetailsDto.getNbfcUserIdFk());
			nbfcProfileDetailsBean.setLastUpdatedBy(userMaster);
			nbfcProfileDetailsBean.setLastUpdatedOn(DateManip.getCurrentDateTime());
			nbfcProfileDetailsTempRepo.save(nbfcProfileDetailsBean);

			return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
		} catch (ServiceException e) {
			logger.error("Exception while fetching nbfcCompanyType list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}
	}

	//Final Submit record into table
	@PostMapping(value = "/insertNbfcEditProfileData")
	public ServiceResponse insertNbfcEditProfileData(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody NbfcCertificationDetailsDto nbfcCertificationDetailsDto) {
		try {
			logger.info("request received to get temp nbfc edit profile page record");
			if (UtilMaster.isEmpty(nbfcCertificationDetailsDto.getCorRegIdFk())) {
				logger.error("Exception while insert data Actual table entity id not found");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
			}

			NbfcProfileDetailsTempBean nbfcProfileDetailsBean = null;
			nbfcProfileDetailsBean = nbfcProfileDetailsTempRepo.getNbfcProfileDetailsForNbfc(nbfcCertificationDetailsDto.getCorRegIdFk(), 3L);
			if (nbfcProfileDetailsBean == null) {
				nbfcProfileDetailsBean = new NbfcProfileDetailsTempBean();
				EntityBean entityBean = new EntityBean();
				entityBean.setEntityId(nbfcCertificationDetailsDto.getCorRegIdFk());
				nbfcProfileDetailsBean.setEntityBean(entityBean);
				nbfcProfileDetailsBean.setPageNumber(3L);
			}
			nbfcProfileDetailsBean.setIsSubmitted(false);
			nbfcProfileDetailsBean.setIsActive(true);
			nbfcProfileDetailsBean.setProfilePageJson(nbfcCertificationDetailsDto.getFormPageJsonValue());
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(nbfcCertificationDetailsDto.getNbfcUserIdFk());
			nbfcProfileDetailsBean.setLastUpdatedBy(userMaster);
			nbfcProfileDetailsBean.setLastUpdatedOn(DateManip.getCurrentDateTime());
			nbfcProfileDetailsTempRepo.save(nbfcProfileDetailsBean);

			List<Long> pageNoList = new ArrayList<>();
			pageNoList.add(1L);
			pageNoList.add(2L);
			pageNoList.add(3L);
			List<NbfcProfileDetailsTempBean> nbfcProfileDetailsBeanList = nbfcProfileDetailsTempRepo.getNbfcProfileDetailsTempUponPageNoList(nbfcCertificationDetailsDto.getCorRegIdFk(), pageNoList);

			if (CollectionUtils.isEmpty(nbfcProfileDetailsBeanList)) {
				logger.error("Exception while update data into temp table before submit actual table of nbfc details");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}
			NbfcProfileAllDetailsBeanDto nbfcProfileAllDetailsBeanDto = new NbfcProfileAllDetailsBeanDto();

			for (NbfcProfileDetailsTempBean nbfcProfileDetailsTempBean : nbfcProfileDetailsBeanList) {
				if (nbfcProfileDetailsTempBean.getPageNumber() == 1L) {
					NbfcProfileDetailsPage1Dto nbfcProfileDetailsPage1Dto = new Gson().fromJson(nbfcProfileDetailsTempBean.getProfilePageJson(), NbfcProfileDetailsPage1Dto.class);
					nbfcProfileAllDetailsBeanDto.setNbfcProfileDetailsPage1Dto(nbfcProfileDetailsPage1Dto);
				} else if (nbfcProfileDetailsTempBean.getPageNumber() == 2L) {
					NbfcProfileDetailsPage2Dto nbfcProfileDetailsPage2Dto = new Gson().fromJson(nbfcProfileDetailsTempBean.getProfilePageJson(), NbfcProfileDetailsPage2Dto.class);
					nbfcProfileAllDetailsBeanDto.setNbfcProfileDetailsPage2Dto(nbfcProfileDetailsPage2Dto);
				} else if (nbfcProfileDetailsTempBean.getPageNumber() == 3L) {
					NbfcProfileDetailsPage3Dto nbfcProfileDetailsPage3Dto = new Gson().fromJson(nbfcProfileDetailsTempBean.getProfilePageJson(), NbfcProfileDetailsPage3Dto.class);
					nbfcProfileAllDetailsBeanDto.setNbfcProfileDetailsPage3Dto(nbfcProfileDetailsPage3Dto);
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			//Converting the Object to JSONString
			String allProDatajsonString = mapper.writeValueAsString(nbfcProfileAllDetailsBeanDto);

			NbfcProfileDetailsBean nbfcProfileDetailsBeanNew = new NbfcProfileDetailsBean();
			EntityBean entityBean = new EntityBean();
			entityBean.setEntityId(nbfcCertificationDetailsDto.getCorRegIdFk());
			nbfcProfileDetailsBeanNew.setEntityBean(entityBean);
			nbfcProfileDetailsBeanNew.setIsActive(true);
			nbfcProfileDetailsBeanNew.setProfilePageJson(allProDatajsonString);
			userMaster = new UserMaster();
			userMaster = userMasterRepo.findByUserId(nbfcCertificationDetailsDto.getNbfcUserIdFk());
			nbfcProfileDetailsBeanNew.setCreatedByFk(userMaster);
			nbfcProfileDetailsBeanNew.setCreatedOn(DateManip.getCurrentDateTime());
			nbfcProfileDetailsBeanNew = nbfcProfileDetailsRepo.save(nbfcProfileDetailsBeanNew);
			//After Data Submit Temp table is update base with isSubmitted 0 
			if (nbfcProfileDetailsBeanNew.getNbfcProfileDetailId() == null) {
				logger.error("Exception while insert data into NBFC actual table ");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}

			for (NbfcProfileDetailsTempBean nbfcProfileDetailsTempBean : nbfcProfileDetailsBeanList) {
				nbfcProfileDetailsTempBean.setIsSubmitted(true);
				nbfcProfileDetailsTempBean.setIsActive(false);
				nbfcProfileDetailsTempBean.setLastUpdatedBy(userMaster);
				nbfcProfileDetailsTempBean.setLastUpdatedOn(DateManip.getCurrentDateTime());
				nbfcProfileDetailsTempRepo.save(nbfcProfileDetailsTempBean);
			}

			return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();

		} catch (Exception e) {
			logger.error("Exception while update data into temp table in updateTempNbfcProfileData" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@RequestMapping(value = "/updateTempNbfcProfileData/{entityCode}/{pageNo}/{userId}", method = RequestMethod.GET)
	public ServiceResponse updateTempNbfcProfileData(@PathVariable("entityId") Long entityId, @PathVariable("pageNo") Long pageNo, @PathVariable("userId") Long userId) {
		try {
			NbfcProfileDetailsTempBean nbfcProfileDetailsBean = nbfcProfileDetailsTempRepo.getNbfcProfileDetailsTemp(entityId, pageNo);

			if (nbfcProfileDetailsBean != null) {
				nbfcProfileDetailsBean.setIsSubmitted(true);
				nbfcProfileDetailsBean.setIsActive(false);
				nbfcProfileDetailsBean.setLastUpdatedOn(DateManip.getCurrentDateTime());
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
		} catch (Exception e) {
			logger.error("Exception while insert data into temp table in updateTempNbfcProfileData" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@RequestMapping(value = "/getDropDownOfNBFCClassification", method = RequestMethod.GET)
	public ServiceResponse getDropDownOfNBFCClassification(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		logger.info("get DropDownOfNBFCClassification list API start");
		try {
			ServiceResponse response = null;
			List<NbfcClassification> nbfcClassificationList = nbfcClassificationRepo.findAll();
			if (CollectionUtils.isEmpty(nbfcClassificationList)) {
				logger.error("Exception while fetching NbfcClassification list not found");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (NbfcClassification dropDownValues : nbfcClassificationList) {
				option = new Option();
				option.setKey(dropDownValues.getNbfcClassiCode());
				option.setValue(dropDownValues.getNbfcClassiNameEn());
				listOfOption.add(option);
			}

			Options options = new Options();

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			logger.info("get DropDownOfNBFCClassification list API End");
			return response;
		} catch (Exception e) {
			logger.error("Exception while fetching nbfcClassificationList info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@RequestMapping(value = "/getSubsidiaryAssociateGroup", method = RequestMethod.GET)
	public ServiceResponse getSubsidiaryAssociateGroup(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		logger.info("get SubsidiaryAssociateGroup list API start");
		try {
			ServiceResponse response = null;
			List<NbfcSubsidiaryAssociateGroupBean> nbfcSubsidiaryAssociateGroupList = nbfcSubsidiaryAssociateGroupRepo.findAll();
			if (CollectionUtils.isEmpty(nbfcSubsidiaryAssociateGroupList)) {
				logger.error("Exception while fetching getSubsidiaryAssociateGroup list not found");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (NbfcSubsidiaryAssociateGroupBean nbfcSubsidiaryAssociateGroup : nbfcSubsidiaryAssociateGroupList) {
				option = new Option();
				option.setKey(String.valueOf(nbfcSubsidiaryAssociateGroup.getNbfcSubsidiaryAssociateGroupId()));
				option.setValue(nbfcSubsidiaryAssociateGroup.getNbfcSubsidiaryAssociateGroup());
				listOfOption.add(option);
			}

			Options options = new Options();

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			logger.info("get NbfcSubsidiaryAssociateGroup list API End");
			return response;
		} catch (Exception e) {
			logger.error("Exception while fetching NbfcSubsidiaryAssociateGroup info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@RequestMapping(value = "/getDropDownOfNbfcCategory", method = RequestMethod.GET)
	public ServiceResponse getDropDownOfNbfcCategory(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		logger.info("get getDropDownCategory list API start");
		try {
			ServiceResponse response = null;
			List<NbfcCategory> nbfcCategoryyList = nbfcCategoryRepo.findAll();
			if (CollectionUtils.isEmpty(nbfcCategoryyList)) {
				logger.error("Exception while fetching NbfcCategory list not found");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (NbfcCategory nbfcCategory : nbfcCategoryyList) {
				option = new Option();
				option.setKey(String.valueOf(nbfcCategory.getNbfcCategoryId()));
				option.setValue(nbfcCategory.getNbfcCategory());
				listOfOption.add(option);
			}

			Options options = new Options();

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			logger.info("get getDropDownOfNbfcCategory list API End");
			return response;
		} catch (Exception e) {
			logger.error("Exception while fetching getDropDownOfNbfcCategory info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}
}
