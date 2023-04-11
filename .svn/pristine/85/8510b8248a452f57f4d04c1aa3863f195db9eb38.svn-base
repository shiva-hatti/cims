package com.iris.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ChartBean;
import com.iris.dto.ChartNode;
import com.iris.dto.DropDownObject;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
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
import com.iris.service.impl.ChartService;
import com.iris.service.impl.LanguageMasterService;
import com.iris.service.impl.UserMasterService;
import com.iris.util.constant.GeneralConstants;

/**
 * 
 * @author svishwakarma
 *
 */

@RestController
@RequestMapping(value = "/service/chartController")
public class ChartController {

	private static final Logger Logger = LoggerFactory.getLogger(ChartController.class);

	@Autowired
	private ChartService chartService;

	@Autowired
	EntityMasterController entityMasterController;

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

	@PostMapping(value = "/getFilingData")
	public ServiceResponse getFilingData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ChartBean chartBean) throws ParseException {
		Logger.info("getting the filing count Job {}", jobProcessId);
		ChartNode node;
		if (StringUtils.equalsIgnoreCase(chartBean.getNotUploaded(), "true") && StringUtils.equalsIgnoreCase(chartBean.getSubmitOrPending(), "false")) {
			node = chartService.getPendingFilingData(chartBean, jobProcessId);
		} else {
			if (StringUtils.equalsIgnoreCase(chartBean.getSubmitOrPending(), "true")) {
				String[] status = { "null" };
				chartBean.setSelectedStatus(status);
				chartBean.setFilingDate(null);
				chartBean.setFilingStartDate(null);
			}
			if (StringUtils.equalsIgnoreCase(chartBean.getSubmitOrPending(), "false")) {
				//chartBean.setFrequency(null);
				//String[] status = {"null"}; 
				//chartBean.setSelectedStatus(status);
				chartBean.setStartDate(null);
				chartBean.setEndDate(null);
			}
			node = chartService.getFilingData(chartBean, jobProcessId);
		}

		return new ServiceResponseBuilder().setStatus(true).setResponse(node.getChildren()).build();
	}

	@PostMapping(value = "/getFilingDataRevReq")
	public ServiceResponse getFilingDataForRevisionRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ChartBean chartBean) throws ParseException {
		Logger.info("getting the filing count Job {}", jobProcessId);
		ChartNode node;

		node = chartService.getFilingDataForRevisionReq(chartBean, jobProcessId);

		return new ServiceResponseBuilder().setStatus(true).setResponse(node.getChildren()).build();
	}

	@GetMapping(value = "/getDashboardChartSetting")
	public ServiceResponse getDashboardChartSetting(@RequestHeader(name = "JobProcessingId") String jobProcessId) throws ParseException {
		Logger.info("getting the chart Setting info Job {}", jobProcessId);
		List<DashboardChartSettingBean> dashboardChartSettingBeanList = dashboardChartSettingRepo.findByIsActiveTrue();
		if (CollectionUtils.isEmpty(dashboardChartSettingBeanList)) {
			return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
		}
		dashboardChartSettingBeanList.sort((DashboardChartSettingBean s1, DashboardChartSettingBean s2) -> s1.getDashboardChartItemsOrder().compareTo(s2.getDashboardChartItemsOrder()));

		List<DashboardChartSettingBean> dashboardChartSettingRevReqBeanList = dashboardChartSettingRepo.findByIsActiveTrueForRevisionReq();
		if (CollectionUtils.isEmpty(dashboardChartSettingRevReqBeanList)) {
			return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
		}
		dashboardChartSettingRevReqBeanList.sort((DashboardChartSettingBean s1, DashboardChartSettingBean s2) -> s1.getDashboardChartItemsOrder().compareTo(s2.getDashboardChartItemsOrder()));

		Map<String, List<DashboardChartSettingBean>> dashboardSettingBeanMap = new HashMap<>();
		dashboardSettingBeanMap.put(GeneralConstants.FILING_HISTORY_CHART_SETTING.getConstantVal(), dashboardChartSettingBeanList);
		dashboardSettingBeanMap.put(GeneralConstants.REVISION_REQUEST_CHART_SETTING.getConstantVal(), dashboardChartSettingRevReqBeanList);
		//return new ServiceResponseBuilder().setStatus(true).setResponse(dashboardChartSettingBeanList).build();
		return new ServiceResponseBuilder().setStatus(true).setResponse(dashboardSettingBeanMap).build();
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/initFilingData")
	public ServiceResponse initFilingData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ChartBean chartBean) {
		Logger.info("getting the initFilingData  Job {}", jobProcessId);
		LanguageMaster localeCode = languageMasterService.getDataById(chartBean.getLangId());
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setActive(true);
		entityMasterDto.setRoleId(chartBean.getRoleId());
		entityMasterDto.setUserId(chartBean.getUserId());

		entityMasterDto.setLanguageCode(localeCode.getLanguageCode());
		List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
		if (entityList != null && !entityList.isEmpty()) {

			List<DropDownObject> entityInList = entityList.stream().map(item -> {
				DropDownObject dto = new DropDownObject();
				dto.setKey(item.getEntityId());
				if (item.getEntityLabelSet() != null) {
					item.getEntityLabelSet().forEach(inner -> {

						if (inner.getLanguageMaster().getLanguageId().equals(chartBean.getLangId())) {
							dto.setDisplay(inner.getEntityNameLabel());
						}
					});
				} else {
					dto.setDisplay(item.getEntityName());
				}

				return dto;
			}).collect(Collectors.toList());
			chartBean.setEntityListOfDrill(entityInList);
			chartBean.setCategoryList(entityList);
		}
		ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
		returnGroupMappingRequest.setIsActive(true);
		returnGroupMappingRequest.setUserId(chartBean.getUserId());
		returnGroupMappingRequest.setLangId(localeCode.getLanguageId());
		returnGroupMappingRequest.setRoleId(chartBean.getRoleId());

		List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
		Set<DropDownObject> deptSet = new HashSet<>();
		for (ReturnGroupMappingDto item : returnList) {
			chartBean.getReturnListOfDrill().addAll(item.getReturnList());
			List<DropDownObject> dept = item.getReturnList().stream().filter(a -> a.getRegulator() != null).map(inner -> {
				DropDownObject dto = new DropDownObject();
				dto.setKey(inner.getRegulator().getRegulatorId());
				dto.setDisplay(inner.getRegulator().getRegulatorName());
				return dto;

			}).collect(Collectors.toList());
			deptSet.addAll(dept);
		}
		List<ReturnDto> returnListSorted = chartBean.getReturnListOfDrill().stream().sorted((object1, object2) -> object1.getReturnName().compareTo(object2.getReturnName())).collect(Collectors.toList());
		chartBean.setReturnListOfDrill(returnListSorted);
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
		if (userMaster != null && ((userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) && userMaster.getDepartmentIdFk() != null && userMaster.getDepartmentIdFk().getIsMaster()) || (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE.getConstantLongVal())))) {
			chartBean.getDeptList().addAll(deptSet);

		}

		List<FilingStatus> fileStatusList = filingStatusRepo.findAll();
		fileStatusList = fileStatusList.stream().filter(item -> item.getFilingStatusId() != GeneralConstants.APPROVED_BY_RBI.getConstantIntVal()).collect(Collectors.toList());
		List<DropDownObject> fileStatusDrop = new ArrayList<>();
		if (fileStatusList != null && !fileStatusList.isEmpty()) {
			fileStatusDrop = fileStatusList.stream().map(item -> {
				DropDownObject dto = new DropDownObject();
				dto.setKey(Long.valueOf(item.getFilingStatusId()));
				dto.setDisplay(ObjectCache.getLabelKeyValue(localeCode.getLanguageCode(), item.getStatus()));
				return dto;

			}).collect(Collectors.toList());

		}
		chartBean.setFilingStatusList(fileStatusDrop);
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
		chartBean.setChanelList(uploadChannelDrop);
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
		chartBean.setTypeOfFilingList(typeOfFilingDrop);
		chartBean.setFreqList((List<Frequency>) filingCalendarController.getAllFrequencyList(jobProcessId).getResponse());
		return new ServiceResponseBuilder().setStatus(true).setResponse(chartBean).build();

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
