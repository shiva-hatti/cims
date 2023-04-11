package com.iris.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ChartBeanV2;
import com.iris.dto.ChartNode;
import com.iris.dto.DropDownObject;
import com.iris.dto.EntityDto;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.ReturnByRoleInputDto;
import com.iris.dto.ReturnByRoleOutputDto;
import com.iris.dto.ReturnDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.Category;
import com.iris.model.CategoryLabel;
import com.iris.model.EntityBean;
import com.iris.model.FilingStatus;
import com.iris.model.Frequency;
import com.iris.model.LanguageMaster;
import com.iris.model.SubCategory;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UploadChannel;
import com.iris.model.UserMaster;
import com.iris.repository.DashboardChartSettingRepo;
import com.iris.repository.FilingStatusRepo;
import com.iris.service.impl.ChannelService;
import com.iris.service.impl.ChartServiceV2;
import com.iris.service.impl.LanguageMasterService;
import com.iris.service.impl.UserMasterService;
import com.iris.util.constant.GeneralConstants;

/**
 * 
 * @author pmhatre
 *
 */

@RestController
@RequestMapping(value = "/service/chartController/V2")
public class ChartControllerV2 {

	private static final Logger LOGGER = LogManager.getLogger(ChartControllerV2.class);

	@Autowired
	EntityMasterController entityMasterController;

	@Autowired
	EntityMasterControllerV2 entityMasterControllerV2;

	@Autowired
	FilingStatusRepo filingStatusRepo;
	@Autowired
	ReturnGroupController returnGroupController;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private UserMasterService userMasterService;

	@Autowired
	LanguageMasterService languageMasterService;

	@Autowired
	FilingCalendarController filingCalendarController;

	@Autowired
	DashboardChartSettingRepo dashboardChartSettingRepo;

	@Autowired
	ChartServiceV2 chartServiceV2;

	@Autowired
	ReturnGroupControllerV2 returnGroupControllerV2;

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/initFilingData")
	public ServiceResponse initFilingData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ChartBeanV2 chartBeanV2) {
		LOGGER.info("getting the initFilingData  Job {}", jobProcessId);
		System.out.println("ChartBeanV2:" + new Gson().toJson(chartBeanV2));
		LanguageMaster localeCode = languageMasterService.getDataById(chartBeanV2.getLangId());
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setActive(true);
		if (chartBeanV2.getRoleId() != null) {
			entityMasterDto.setRoleId(chartBeanV2.getRoleId());
		}
		entityMasterDto.setUserId(chartBeanV2.getUserId());
		entityMasterDto.setLanguageCode(chartBeanV2.getLangCode());

		entityMasterDto.setLanguageCode(localeCode.getLanguageCode());
		List<EntityDto> entityList = (List<EntityDto>) entityMasterControllerV2.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();

		if (entityList != null && !entityList.isEmpty()) {

			List<DropDownObject> entityInList = entityList.stream().map(item -> {
				DropDownObject dto = new DropDownObject();
				dto.setKey(item.getEntityId());
				dto.setDisplay(item.getEntityName());
				return dto;
			}).collect(Collectors.toList());
			chartBeanV2.setEntityListOfDrill(entityInList);
			chartBeanV2.setCategoryList(entityList);
		}

		//----------Fetching Return List
		ReturnByRoleInputDto returnByRoleInputDto = new ReturnByRoleInputDto();
		returnByRoleInputDto.setIsActive(true);
		returnByRoleInputDto.setUserId(chartBeanV2.getUserId());
		returnByRoleInputDto.setLangCode(chartBeanV2.getLangCode());
		if (chartBeanV2.getRoleId() != null) {
			returnByRoleInputDto.setRoleId(chartBeanV2.getRoleId());
		}

		LOGGER.info("User:" + chartBeanV2.getUserName() + "******initFilingDataV2: starting to fetch returns list: Start Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
		ServiceResponse serviceResponse = returnGroupControllerV2.getReturnByRole(jobProcessId, returnByRoleInputDto);
		LOGGER.info("User:" + chartBeanV2.getUserName() + "******initFilingDataV2: starting to fetch returns list: End Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
		List<ReturnByRoleOutputDto> returnEntityOutputDtoList = (List<ReturnByRoleOutputDto>) serviceResponse.getResponse();
		for (ReturnByRoleOutputDto item : returnEntityOutputDtoList) {
			ReturnDto returnDto = new ReturnDto();
			//returnDto.setReturnId(item.getReturnId());
			returnDto.setReturnCode(item.getReturnCode());
			String strRetunName = item.getReturnName();
			returnDto.setReturnName(strRetunName);
			Frequency frequency = new Frequency();
			frequency.setFrequencyCode(item.getFreqCode());
			frequency.setFrequencyName(item.getFreqName());
			returnDto.setFrequency(frequency);
			chartBeanV2.getReturnListOfDrill().add(returnDto);
		}
		//---------------

		Set<DropDownObject> deptSet = new HashSet<>();
		UserMaster userMaster = userMasterService.getDataById(chartBeanV2.getUserId());
		if (userMaster != null && ((userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) && userMaster.getDepartmentIdFk() != null && userMaster.getDepartmentIdFk().getIsMaster()) || (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE.getConstantLongVal())))) {
			chartBeanV2.getDeptList().addAll(deptSet);
		}

		List<FilingStatus> fileStatusList = filingStatusRepo.findAll();
		fileStatusList = fileStatusList.stream().filter(item -> !item.getFilingStatusId().equals(GeneralConstants.APPROVED_BY_RBI.getConstantIntVal())).collect(Collectors.toList());
		List<DropDownObject> fileStatusDrop = new ArrayList<>();
		if (fileStatusList != null && !fileStatusList.isEmpty()) {
			fileStatusDrop = fileStatusList.stream().map(item -> {
				DropDownObject dto = new DropDownObject();
				dto.setKey(Long.valueOf(item.getFilingStatusId()));
				dto.setDisplay(ObjectCache.getLabelKeyValue(localeCode.getLanguageCode(), item.getStatus()));
				return dto;

			}).collect(Collectors.toList());

		}
		chartBeanV2.setFilingStatusList(fileStatusDrop);
		List<UploadChannel> uploadChannelList;
		uploadChannelList = channelService.getActiveDataFor(UploadChannel.class, null);
		List<DropDownObject> uploadChannelDrop = new ArrayList<>();
		if (uploadChannelList != null && !uploadChannelList.isEmpty()) {
			uploadChannelDrop = uploadChannelList.stream().map(item -> {
				DropDownObject dto = new DropDownObject();
				dto.setKey(item.getUploadChannelId());
				dto.setDisplay(ObjectCache.getLabelKeyValue(localeCode.getLanguageCode(), item.getUploadChannelDesc()));
				return dto;

			}).collect(Collectors.toList());

		}
		chartBeanV2.setChanelList(uploadChannelDrop);
		List<DropDownObject> typeOfFilingDrop = new ArrayList<>();
		DropDownObject fresh = new DropDownObject();
		fresh.setDisplay(ObjectCache.getLabelKeyValue(localeCode.getLanguageCode(), "field.fresh.filing"));
		fresh.setKey(0l);
		typeOfFilingDrop.add(fresh);
		DropDownObject unlock = new DropDownObject();
		unlock.setDisplay(ObjectCache.getLabelKeyValue(localeCode.getLanguageCode(), "field.filingMngt.isUnlocked"));
		unlock.setKey(1l);
		typeOfFilingDrop.add(unlock);
		DropDownObject revision = new DropDownObject();
		revision.setDisplay(ObjectCache.getLabelKeyValue(localeCode.getLanguageCode(), "field.filingMngt.isRevised"));
		revision.setKey(2l);
		typeOfFilingDrop.add(revision);
		chartBeanV2.setTypeOfFilingList(typeOfFilingDrop);
		chartBeanV2.setFreqList((List<Frequency>) filingCalendarController.getAllFrequencyList(jobProcessId).getResponse());
		return new ServiceResponseBuilder().setStatus(true).setResponse(chartBeanV2).build();

	}

	@PostMapping(value = "/getFilingData")
	public ServiceResponse getFilingData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ChartBeanV2 chartBeanV2) throws ParseException {
		LOGGER.info("getting the filing count Job {}", jobProcessId);
		ChartNode node;
		if (StringUtils.equalsIgnoreCase(chartBeanV2.getNotUploaded(), "true") && StringUtils.equalsIgnoreCase(chartBeanV2.getSubmitOrPending(), "false")) {
			node = chartServiceV2.getPendingFilingData(chartBeanV2, jobProcessId);
		} else {
			if (StringUtils.equalsIgnoreCase(chartBeanV2.getSubmitOrPending(), "true")) {
				String[] status = { "null" };
				chartBeanV2.setSelectedStatus(status);
				chartBeanV2.setFilingDate(null);
				chartBeanV2.setFilingStartDate(null);
			}
			if (StringUtils.equalsIgnoreCase(chartBeanV2.getSubmitOrPending(), "false")) {
				//chartBean.setFrequency(null);
				//String[] status = {"null"}; 
				//chartBean.setSelectedStatus(status);
				chartBeanV2.setStartDate(null);
				chartBeanV2.setEndDate(null);
			}
			node = chartServiceV2.getFilingData(chartBeanV2, jobProcessId);
		}

		return new ServiceResponseBuilder().setStatus(true).setResponse(node.getChildren()).build();
	}

	@PostMapping(value = "/getFilingDataRevReq")
	public ServiceResponse getFilingDataForRevisionRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ChartBeanV2 chartBeanV2) throws ParseException {
		LOGGER.info("getting the filing count Job {}", jobProcessId);
		ChartNode node;

		node = chartServiceV2.getFilingDataForRevisionReq(chartBeanV2, jobProcessId);

		return new ServiceResponseBuilder().setStatus(true).setResponse(node.getChildren()).build();
	}

	private List<Category> getCategoryList(EntityBean userMappedEntity, Long langId) {
		List<Category> categoryList = new ArrayList<Category>();

		CategoryLabel categoryLable = userMappedEntity.getCategory().getCatLblSet().stream().filter(cat -> cat.getLangIdFk().getLanguageId().equals(langId)).findAny().orElse(null);
		Category category = new Category();
		category.setCategoryId(userMappedEntity.getCategory().getCategoryId());
		category.setCategoryCode(userMappedEntity.getCategory().getCategoryCode());
		if (categoryLable != null) {
			category.setCategoryName(categoryLable.getCategoryLabel());
		} else {
			category.setCategoryName(userMappedEntity.getCategory().getCategoryName());
		}

		Set<SubCategory> subCategorySet = new HashSet<SubCategory>();

		SubCategoryLabel subCategoryLabel = userMappedEntity.getSubCategory().getSubCatLblSet().stream().filter(cat -> cat.getLangIdFk().getLanguageId().equals(langId)).findAny().orElse(null);
		SubCategory subcategory = new SubCategory();
		subcategory.setSubCategoryId(userMappedEntity.getSubCategory().getSubCategoryId());
		subcategory.setSubCategoryCode(userMappedEntity.getSubCategory().getSubCategoryCode());
		if (subCategoryLabel != null) {
			subcategory.setSubCategoryName(subCategoryLabel.getSubCategoryLabel());
		} else {
			subcategory.setSubCategoryName(userMappedEntity.getSubCategory().getSubCategoryName());
		}

		subCategorySet.add(subcategory);
		category.setSubCategory(subCategorySet);

		categoryList.add(category);
		return categoryList;
	}

}
