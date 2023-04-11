package com.iris.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.caching.ObjectCache;
import com.iris.controller.EntityMasterController;
import com.iris.controller.EntityMasterControllerV2;
import com.iris.controller.ReturnGroupController;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ChartBeanV2;
import com.iris.dto.ChartNode;
import com.iris.dto.EntityDto;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.dto.ServiceResponse;
import com.iris.model.UserMaster;
import com.iris.repository.DashboardChartSettingRepo;
import com.iris.util.constant.GeneralConstants;
import org.apache.logging.log4j.Logger;

/**
 * Chart specific Service
 * 
 * @author pmhatre
 *
 */
@Service
public class ChartServiceV2 {

	private static final Logger LOGGER = LogManager.getLogger(ChartServiceV2.class);

	@Autowired
	EntityManager em;

	@Autowired
	private UserMasterService userMasterService;

	@Autowired
	EntityMasterController entityMasterController;

	@Autowired
	EntityMasterControllerV2 entityMasterControllerV2;

	@Autowired
	ReturnGroupController returnGroupController;

	@Autowired
	DashboardChartSettingRepo dashboardChartSettingRepo;

	public ChartNode getFilingData(ChartBeanV2 chartBeanV2, String jobProcessId) throws ParseException {
		LOGGER.info(" getFilingData job {}", jobProcessId);
		Map<String, String> groupMap = new HashMap<>();

		//groupMap.put("1", "TFD.UPLOAD_CHANNEL_ID_FK");
		//groupMap.put("2", "TREG.REGULATOR_ID");
		groupMap.put("3", "TRUD.RETURN_ID_FK");
		groupMap.put("4", "TRUD.ENTITY_ID_FK");
		groupMap.put("5", "TBENT.CATEGORY_ID_FK");
		groupMap.put("6", "TBENT.SUB_CATEGORY_ID_FK");

		Map<String, String> nameMap = new HashMap<>();
		//nameMap.put("1", "chanel");
		//nameMap.put("2", "regLabel");
		nameMap.put("3", "returnName");
		nameMap.put("4", "entityName");
		nameMap.put("5", "category");
		nameMap.put("6", "subCategoryName");

		Map<String, String> pkAliasMap = new HashMap<>();
		pkAliasMap.put("1", "chanelPk");
		pkAliasMap.put("2", "regId");
		pkAliasMap.put("3", "returnval");
		pkAliasMap.put("4", "entity");
		pkAliasMap.put("5", "categoryPk");
		pkAliasMap.put("6", "subCategoryPk");

		String[] orders = StringUtils.isNotBlank(chartBeanV2.getOrder()) ? chartBeanV2.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBeanV2.getUserId());
		if (userMaster != null) {

			if ((userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) && userMaster.getDepartmentIdFk() != null && userMaster.getDepartmentIdFk().getIsMaster()) || (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE.getConstantLongVal()))) {
				deptConsider = true;
			} else {
				orderList = orderList.stream().filter(item -> !StringUtils.equals(item, "2")).collect(Collectors.toList());
			}

		}

		StringBuilder groupByClause = new StringBuilder();
		boolean commaNeeded = false;
		for (String item : orderList) {
			if (commaNeeded) {
				groupByClause.append(" , ");
			}
			groupByClause.append(groupMap.get(item));
			commaNeeded = true;
		}
		List<Long> selectedChanelList = new ArrayList<>();
		String[] allotedChanel = chartBeanV2.getSelectedChanel();
		if (allotedChanel != null && !StringUtils.equals(allotedChanel[0], "null") && !StringUtils.equals(allotedChanel[0], "[]")) {
			Arrays.stream(allotedChanel).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedChanelList.add(Long.valueOf(innerItem))));

		}

		List<String> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBeanV2.getSelectedReturn();
		if (allotedReturn != null && !StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(innerItem)));
		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBeanV2.getSelectedEntity();
		if (allotedEntity != null && !StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBeanV2.getSelectedStatus();
		if (selectedStatus != null && !StringUtils.equals(allotedStatus[0], "null") && !StringUtils.equals(allotedStatus[0], "[]")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBeanV2.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null") && !StringUtils.equals(allotedFilingType[0], "[]")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedDept = new ArrayList<>();
		String[] alloteDept = chartBeanV2.getSelectedDept();
		if (!StringUtils.equals(alloteDept[0], "null") && !StringUtils.equals(alloteDept[0], "[]")) {
			Arrays.stream(alloteDept).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedDept.add(Long.valueOf(innerItem))));

		}

		List<Integer> selectedCategory = new ArrayList<>();
		String[] alloteCategory = chartBeanV2.getSelectedCategory();
		if (!StringUtils.equals(alloteCategory[0], "null") && !StringUtils.equals(alloteCategory[0], "[]")) {
			Arrays.stream(alloteCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedCategory.add(Integer.parseInt(innerItem))));

		}

		List<Integer> selectedSubCategory = new ArrayList<>();
		String[] alloteSubCategory = chartBeanV2.getSelectedSubCategory();
		if (!StringUtils.equals(alloteSubCategory[0], "null") && !StringUtils.equals(alloteSubCategory[0], "[]")) {

			Arrays.stream(alloteSubCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedSubCategory.add(Integer.parseInt(innerItem))));
		}

		boolean filingDateApplicable = false;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) as value , ");
		sb.append(" TBENT.CATEGORY_ID_FK as categoryPk, ");
		sb.append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ");
		sb.append(" TRUD.RETURN_ID_FK as returnval, ");
		sb.append(" TRUD.ENTITY_ID_FK as entity, ");
		sb.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ");
		sb.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ");
		sb.append(" min(TRL.RETURN_LABEL) AS returnName, ");
		sb.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName ");
		sb.append(" FROM TBL_RETURNS_UPLOAD_DETAILS TRUD LEFT OUTER JOIN TBL_FILE_DETAILS TFD ");
		sb.append(" ON TFD.ID=TRUD.FILE_DETAILS_ID_FK left outer join TBL_ENTITY TBENT  ");
		sb.append(" ON TBENT.ENTITY_ID=TRUD.ENTITY_ID_FK left outer join TBL_CATEGORY_LABEL TBCATLBL ");
		sb.append(" ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK ");
		sb.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ");
		sb.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ");
		sb.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ");
		sb.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ");
		sb.append(" left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRUD.ENTITY_ID_FK ");
		sb.append(" AND  TEL.LANGUAGE_ID_FK=:languageId ");

		sb.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRUD.RETURN_ID_FK and TR.IS_ACTIVE=1 ");
		sb.append(" left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRUD.RETURN_ID_FK ");
		sb.append(" AND TRL.LANG_ID_FK=:languageId ");

		//.append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRUD.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ")                                                            
		sb.append(" inner join  (Select Max(mtr.UPLOAD_ID) as UPLOAD_ID from TBL_RETURNS_UPLOAD_DETAILS mtr where mtr.FILING_STATUS_ID_FK=" + GeneralConstants.APPROVED_BY_RBI.getConstantIntVal());
		sb.append("  group by mtr.RETURN_ID_FK ,mtr.ENTITY_ID_FK,mtr.END_DATE,mtr.FIN_YR_FREQUENCY_ID_FK) maxTable ");
		sb.append(" on  maxTable.UPLOAD_ID = TRUD.UPLOAD_ID ");
		sb.append(" where  TFD.UPLOAD_CHANNEL_ID_FK is not null ");

		if (StringUtils.equalsIgnoreCase(chartBeanV2.getSubmitOrPending(), "false")) {
			sb.append(" and TRUD.FILING_STATUS_ID_FK in (9) ");//in clause impromevent
		} else {
			sb.append(" and TRUD.FILING_STATUS_ID_FK=" + GeneralConstants.APPROVED_BY_RBI.getConstantIntVal());
		}
		//		sb.append(" and TRUD.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TR.RETURN_CODE IN (:selectedReturn)");
		sb.append(" and TRUD.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");

		if (chartBeanV2.getFrequency() != null) {
			sb.append(" and TR.FREQUENCY_ID_FK =:frequency ");
		}
		if (!selectedStatus.isEmpty()) {
			sb.append(" and TRUD.FILING_STATUS_ID_FK IN (:selectedStatus)");
		}

		boolean first = true;
		for (Long filingType : selectedFilingType) {
			if (first) {
				sb.append(" and ( ");
				first = false;
			} else {
				sb.append(" or ");
			}
			if (filingType.compareTo(0l) == 0) {
				sb.append(" ( TRUD.UNLOCK_REQUEST_ID_FK is null and  TRUD.REVISION_REQUEST_ID_FK is null ) ");
			} else if (filingType.compareTo(1l) == 0) {
				sb.append(" (  TRUD.UNLOCK_REQUEST_ID_FK is not null ) ");
			} else if (filingType.compareTo(2l) == 0) {
				sb.append(" (  TRUD.REVISION_REQUEST_ID_FK is not null ) ");
			}
		}
		if (!selectedFilingType.isEmpty()) {
			sb.append(" ) ");
		}
		if (StringUtils.isNotBlank(chartBeanV2.getFilingStartDate()) && StringUtils.isNotBlank(chartBeanV2.getFilingDate())) {
			filingDateApplicable = true;
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}
		if (StringUtils.isNotBlank(chartBeanV2.getStartDate()) && StringUtils.isNotBlank(chartBeanV2.getEndDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRUD.END_DATE >=:startDate And TRUD.END_DATE<=:endDate  ");
		}
		sb.append(" group by ").append(groupByClause);

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);

		//Fetching Return
		if (selectedReturn.isEmpty()) {
			ReturnEntityMapDto returnEntityMapDto = new ReturnEntityMapDto();
			returnEntityMapDto.setIsActive(true);
			returnEntityMapDto.setUserId(chartBeanV2.getUserId());
			returnEntityMapDto.setLangCode(chartBeanV2.getLangCode());
			if (chartBeanV2.getRoleId() != null) {
				returnEntityMapDto.setRoleId(chartBeanV2.getRoleId());
			}

			LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingDataV2: starting to fetch returns list: Start Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
			ServiceResponse serviceResponse = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);
			LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingDataV2: starting to fetch returns list: End Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
			List<ReturnEntityOutputDto> returnEntityOutputDtoList = (List<ReturnEntityOutputDto>) serviceResponse.getResponse();
			for (ReturnEntityOutputDto returnGroupMappingDto : returnEntityOutputDtoList) {
				selectedReturn.add(returnGroupMappingDto.getReturnCode());
			}

		}
		query.setParameter("selectedReturn", selectedReturn);

		//Fetching Entity
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setActive(true);
		if (chartBeanV2.getRoleId() != null) {
			entityMasterDto.setRoleId(chartBeanV2.getRoleId());
		}
		entityMasterDto.setUserId(chartBeanV2.getUserId());

		entityMasterDto.setLanguageCode(chartBeanV2.getLangCode());
		LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingData: starting to fetch Entity list: Start Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
		ServiceResponse serviceResponse = entityMasterControllerV2.getEntityMasterList(jobProcessId, entityMasterDto);
		LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingData: starting to fetch Entity list: End Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
		List<EntityDto> entityList = (List<EntityDto>) serviceResponse.getResponse();
		if (entityList != null && !entityList.isEmpty()) {
			if (selectedEntity.isEmpty()) {
				selectedEntity.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			}
			if (selectedCategory.isEmpty()) {
				selectedCategory.addAll(entityList.stream().map(inner -> inner.getCategoryDto().getCategoryId()).collect(Collectors.toList()));
				Set<Integer> set = new LinkedHashSet<>();
				set.addAll(selectedCategory);
				selectedCategory.clear();
				selectedCategory.addAll(set);
			}
			if (selectedSubCategory.isEmpty()) {
				selectedSubCategory.addAll(entityList.stream().map(inner -> inner.getSubCategoryDto().getSubCategoryId()).collect(Collectors.toList()));
				Set<Integer> set = new LinkedHashSet<>();
				set.addAll(selectedSubCategory);
				selectedSubCategory.clear();
				selectedSubCategory.addAll(set);
			}
		}
		LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingData: starting to End Entity list looping: Start Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
		//}

		query.setParameter("selectedEntity", selectedEntity);
		if (!selectedStatus.isEmpty()) {
			query.setParameter("selectedStatus", selectedStatus);
		}
		/*	if(!selectedDept.isEmpty()) {
			query.setParameter("selectedDept", selectedDept);
		}*/
		if (chartBeanV2.getFrequency() != null) {
			query.setParameter("frequency", chartBeanV2.getFrequency());
		}

		if (!selectedSubCategory.isEmpty()) {
			query.setParameter("selectedSubCategory", selectedSubCategory);
		}

		if (!selectedCategory.isEmpty()) {
			query.setParameter("selectedCategory", selectedCategory);
		}
		if (dateFilterApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBeanV2.getStartDate(), chartBeanV2.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBeanV2.getEndDate(), chartBeanV2.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		if (filingDateApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBeanV2.getFilingStartDate(), chartBeanV2.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBeanV2.getFilingDate(), chartBeanV2.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		List<Tuple> result = query.setParameter("languageId", chartBeanV2.getLangId()).getResultList();
		ChartNode node = new ChartNode();
		Set<String> unique = new HashSet<>();
		for (Tuple item : result) {

			ChartNode level1 = new ChartNode();
			level1.setChildren(null);
			level1.setId("filing");
			level1.setName("filing");
			level1.setParent(null);
			if (unique.add(level1.getId())) {
				node.getChildren().add(level1);
			}
			ChartNode level2 = new ChartNode();
			level2.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
			level2.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
			level2.setParent("filing");
			if (unique.add(level2.getId())) {
				node.getChildren().add(level2);
			}

			ChartNode level3 = new ChartNode();
			level3.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			level3.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
			level3.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
			if (unique.add(level3.getId())) {
				node.getChildren().add(level3);
			}

			ChartNode level4 = new ChartNode();
			level4.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
			level4.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
			level4.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			if (unique.add(level4.getId())) {
				node.getChildren().add(level4);
			}

			if (orderList.size() == 4) {
				ChartNode level5 = new ChartNode();
				level5.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
				level5.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));
				level5.setValue((BigInteger) item.get("value"));
				level5.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
				if (unique.add(level5.getId())) {
					node.getChildren().add(level5);
				}

			} else {
				level4.setValue((BigInteger) item.get("value"));
			}
		}

		return node;
	}

	public ChartNode getFilingDataForRevisionReq(ChartBeanV2 chartBeanV2, String jobProcessId) throws ParseException {
		LOGGER.info(" getFilingData job {}", jobProcessId);

		Map<String, String> groupMap = new HashMap<>();

		//groupMap.put("1", "TFD.UPLOAD_CHANNEL_ID_FK");
		//groupMap.put("2", "TREG.REGULATOR_ID");
		groupMap.put("3", "TRR.RETURN_ID_FK"); //3,6,4
		groupMap.put("4", "TRR.ENTITY_ID_FK");
		groupMap.put("5", "TBENT.CATEGORY_ID_FK");
		groupMap.put("6", "TBENT.SUB_CATEGORY_ID_FK");

		Map<String, String> nameMap = new HashMap<>();
		//nameMap.put("1", "chanel");
		//nameMap.put("2", "regLabel");
		nameMap.put("3", "returnName");
		nameMap.put("4", "entityName");
		nameMap.put("5", "category");
		nameMap.put("6", "subCategoryName");

		Map<String, String> pkAliasMap = new HashMap<>();
		pkAliasMap.put("1", "chanelPk");
		pkAliasMap.put("2", "regId");
		pkAliasMap.put("3", "returnval");
		pkAliasMap.put("4", "entity");
		pkAliasMap.put("5", "categoryPk");
		pkAliasMap.put("6", "subCategoryPk");

		chartBeanV2.setOrder("3,5,6,4");

		String[] orders = StringUtils.isNotBlank(chartBeanV2.getOrder()) ? chartBeanV2.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBeanV2.getUserId());
		if (userMaster != null) {

			if ((userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) && userMaster.getDepartmentIdFk() != null && userMaster.getDepartmentIdFk().getIsMaster()) || (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE.getConstantLongVal()))) {
				deptConsider = true;
			} else {
				orderList = orderList.stream().filter(item -> !StringUtils.equals(item, "2")).collect(Collectors.toList());
			}
		}

		Long regulatorId = userMaster.getDepartmentIdFk().getRegulatorId();

		StringBuilder groupByClause = new StringBuilder();
		boolean commaNeeded = false;
		for (String item : orderList) {
			if (commaNeeded) {
				groupByClause.append(" , ");
			}
			groupByClause.append(groupMap.get(item));
			commaNeeded = true;
		}

		List<String> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBeanV2.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(innerItem)));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBeanV2.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Integer> selectedCategory = new ArrayList<>();
		String[] alloteCategory = chartBeanV2.getSelectedCategory();
		if (!StringUtils.equals(alloteCategory[0], "null") && !StringUtils.equals(alloteCategory[0], "[]")) {
			Arrays.stream(alloteCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedCategory.add(Integer.parseInt(innerItem))));

		}

		List<Integer> selectedSubCategory = new ArrayList<>();
		String[] alloteSubCategory = chartBeanV2.getSelectedSubCategory();
		if (!StringUtils.equals(alloteSubCategory[0], "null") && !StringUtils.equals(alloteSubCategory[0], "[]")) {

			Arrays.stream(alloteSubCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedSubCategory.add(Integer.parseInt(innerItem))));
		}

		boolean filingDateApplicable = false;
		StringBuilder sb = new StringBuilder();
		sb.append("select min(part.value) as value , ");
		sb.append("part.categoryPk , ");
		sb.append("part.subCategoryPk , ");
		sb.append("part.returnval , ").append("part.entity , ").append("min(part.category) as category , ").append("min(part.subCategoryName) as subCategoryName , ").append("min(part.returnName) as returnName , ").append("min(part.entityName) as entityName , ").append("part.diff from ( ").append(" SELECT COUNT(*) as value , ").append(" TBENT.CATEGORY_ID_FK as categoryPk, ").append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ").append(" TRR.RETURN_ID_FK as returnval, ").append(" TRR.ENTITY_ID_FK as entity, ");

		sb.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ");
		sb.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ");
		sb.append(" min(TRL.RETURN_LABEL) AS returnName, ");
		sb.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName, ");
		sb.append(" 'total' as diff ");

		sb.append(" FROM TBL_REVISION_REQUEST TRR ");
		sb.append(" left outer join TBL_ENTITY TBENT ON TBENT.ENTITY_ID=TRR.ENTITY_ID_FK  ");
		sb.append(" left outer join TBL_CATEGORY_LABEL TBCATLBL ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK  ");
		sb.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ");

		sb.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ");
		sb.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ");
		sb.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ");

		sb.append(" left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRR.ENTITY_ID_FK ");
		sb.append(" AND  TEL.LANGUAGE_ID_FK=:languageId ");

		sb.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRR.RETURN_ID_FK and TR.IS_ACTIVE=1 ");
		sb.append(" left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRR.RETURN_ID_FK ");
		sb.append(" AND TRL.LANG_ID_FK=:languageId ");

		sb.append(" inner join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRRM.RETURN_ID_FK = TR.RETURN_ID and TRRM.IS_ACTIVE = 1  ");
		sb.append(" and TRR.RETURN_ID_FK = TRRM.RETURN_ID_FK and TR.IS_ACTIVE = 1 ");
		sb.append(" and TRRM.REGULATOR_ID_FK = :regulatorId ");

		//		sb.append(" where TRR.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" where TR.RETURN_CODE IN (:selectedReturn)");
		sb.append(" and TRR.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");

		if (StringUtils.isNotBlank(chartBeanV2.getStartDate()) && StringUtils.isNotBlank(chartBeanV2.getEndDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRR.REPORTING_END_DATE >=:startDate And TRR.REPORTING_END_DATE<=:endDate  ");
		}
		sb.append(" group by ").append(groupByClause);

		sb.append(" union ").append(getPendingRevisionRequestQueryString(chartBeanV2));
		sb.append(" ) part   group by diff ,returnval,categoryPk , subCategoryPk , entity");

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);

		//Fetching Return
		if (selectedReturn.isEmpty()) {

			ReturnEntityMapDto returnEntityMapDto = new ReturnEntityMapDto();
			returnEntityMapDto.setIsActive(true);
			returnEntityMapDto.setUserId(chartBeanV2.getUserId());
			returnEntityMapDto.setLangCode(chartBeanV2.getLangCode());
			if (chartBeanV2.getRoleId() != null) {
				returnEntityMapDto.setRoleId(chartBeanV2.getRoleId());
			}

			LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingDataForRevisionReqV2: starting to fetch Return list: Start Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
			ServiceResponse serviceResponse = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);
			LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingDataForRevisionReqV2: starting to fetch Return list: End Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
			List<ReturnEntityOutputDto> returnEntityOutputDtoList = (List<ReturnEntityOutputDto>) serviceResponse.getResponse();
			for (ReturnEntityOutputDto returnGroupMappingDto : returnEntityOutputDtoList) {
				selectedReturn.add(returnGroupMappingDto.getReturnCode());
			}

		}
		query.setParameter("selectedReturn", selectedReturn);

		//Fetching Entity
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setActive(true);
		if (chartBeanV2.getRoleId() != null) {
			entityMasterDto.setRoleId(chartBeanV2.getRoleId());
		}
		entityMasterDto.setUserId(chartBeanV2.getUserId());

		entityMasterDto.setLanguageCode(chartBeanV2.getLangCode());

		LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingDataForRevisionReqV2: starting to fetch Entity list: Start Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
		ServiceResponse serviceResponse = entityMasterControllerV2.getEntityMasterList(jobProcessId, entityMasterDto);
		LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingDataForRevisionReqV2: starting to fetch Entity list: End Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
		List<EntityDto> entityList = (List<EntityDto>) serviceResponse.getResponse();
		if (entityList != null && !entityList.isEmpty()) {
			if (selectedEntity.isEmpty()) {
				selectedEntity.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			}
			if (selectedCategory.isEmpty()) {
				selectedCategory.addAll(entityList.stream().map(inner -> inner.getCategoryDto().getCategoryId()).collect(Collectors.toList()));
				Set<Integer> set = new LinkedHashSet<>();
				set.addAll(selectedCategory);
				selectedCategory.clear();
				selectedCategory.addAll(set);
			}
			if (selectedSubCategory.isEmpty()) {
				selectedSubCategory.addAll(entityList.stream().map(inner -> inner.getSubCategoryDto().getSubCategoryId()).collect(Collectors.toList()));
				Set<Integer> set = new LinkedHashSet<>();
				set.addAll(selectedSubCategory);
				selectedSubCategory.clear();
				selectedSubCategory.addAll(set);
			}
		}
		LOGGER.info("User:" + chartBeanV2.getUserName() + "******getFilingDataForRevisionReqV2: starting to End Entity list looping: End Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);

		query.setParameter("selectedEntity", selectedEntity);

		query.setParameter("regulatorId", regulatorId);

		if (!selectedSubCategory.isEmpty()) {
			query.setParameter("selectedSubCategory", selectedSubCategory);
		}

		if (!selectedCategory.isEmpty()) {
			query.setParameter("selectedCategory", selectedCategory);
		}
		if (dateFilterApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBeanV2.getStartDate(), chartBeanV2.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBeanV2.getEndDate(), chartBeanV2.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		if (filingDateApplicable) {
			//			Date startDate = DateManip.convertStringToDate(chartBean.getFilingStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBeanV2.getFilingDate(), chartBeanV2.getDateFormat());
			query.setParameter("endDate", endDate);
		}
		List<Tuple> result = query.setParameter("languageId", chartBeanV2.getLangId()).getResultList();
		ChartNode node = new ChartNode();
		Set<String> unique1 = new HashSet<>();
		//Set<String> unique01 = new HashSet<>();
		for (Tuple item : result) {

			ChartNode level0 = new ChartNode();
			level0.setChildren(null);
			level0.setId("filing");
			level0.setName("filing");
			level0.setParent(null);
			if (unique1.add(level0.getId())) {
				node.getChildren().add(level0);
			}

			if (item.get(9).equals("total")) {
				ChartNode level1 = new ChartNode();
				level1.setChildren(null);
				level1.setId("TotalRevReq");
				level1.setName("TotalRevReq");
				level1.setParent("filing");
				if (unique1.add(level1.getId())) {
					node.getChildren().add(level1);
				}

				ChartNode level2 = new ChartNode();
				level2.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
				level2.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
				level2.setParent("TotalRevReq");
				if (unique1.add(level2.getId())) {
					node.getChildren().add(level2);
				}

				ChartNode level3 = new ChartNode();
				level3.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				level3.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
				level3.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
				if (unique1.add(level3.getId())) {
					node.getChildren().add(level3);
				}

				ChartNode level4 = new ChartNode();
				level4.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
				level4.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
				level4.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				if (unique1.add(level4.getId())) {
					node.getChildren().add(level4);
				}

				if (orderList.size() == 4) {
					ChartNode level5 = new ChartNode();
					level5.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
					level5.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));
					level5.setValue((BigInteger) item.get("value"));
					level5.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
					if (unique1.add(level5.getId())) {
						node.getChildren().add(level5);
					}

				} else {
					level4.setValue((BigInteger) item.get("value"));
				}
			} else {
				ChartNode level1 = new ChartNode();
				level1.setChildren(null);
				level1.setId("PendingRevReq");
				level1.setName("PendingRevReq");
				level1.setParent("filing");
				if (unique1.add(level1.getId())) {
					node.getChildren().add(level1);
				}

				ChartNode level2 = new ChartNode();
				level2.setId("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
				level2.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
				level2.setParent("PendingRevReq");
				if (unique1.add(level2.getId())) {
					node.getChildren().add(level2);
				}

				ChartNode level3 = new ChartNode();
				level3.setId("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				level3.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
				level3.setParent("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
				if (unique1.add(level3.getId())) {
					node.getChildren().add(level3);
				}

				ChartNode level4 = new ChartNode();
				level4.setId("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
				level4.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
				level4.setParent("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				if (unique1.add(level4.getId())) {
					node.getChildren().add(level4);
				}

				if (orderList.size() == 4) {
					ChartNode level5 = new ChartNode();
					level5.setId("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
					level5.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));
					level5.setValue((BigInteger) item.get("value"));
					level5.setParent("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
					if (unique1.add(level5.getId())) {
						node.getChildren().add(level5);
					}

				} else {
					level4.setValue((BigInteger) item.get("value"));
				}
			}
		}

		return node;
	}

	public String getPendingRevisionRequestQueryString(ChartBeanV2 chartBeanV2) throws ParseException {
		Map<String, String> groupMap = new HashMap<>();

		//groupMap.put("1", "TFD.UPLOAD_CHANNEL_ID_FK");
		//groupMap.put("2", "TREG.REGULATOR_ID");
		groupMap.put("3", "TRR.RETURN_ID_FK"); //3,6,4
		groupMap.put("4", "TRR.ENTITY_ID_FK");
		groupMap.put("5", "TBENT.CATEGORY_ID_FK");
		groupMap.put("6", "TBENT.SUB_CATEGORY_ID_FK");

		Map<String, String> nameMap = new HashMap<>();
		//nameMap.put("1", "chanel");
		//nameMap.put("2", "regLabel");
		nameMap.put("3", "returnName");
		nameMap.put("4", "entityName");
		nameMap.put("5", "category");
		nameMap.put("6", "subCategoryName");

		Map<String, String> pkAliasMap = new HashMap<>();
		pkAliasMap.put("1", "chanelPk");
		pkAliasMap.put("2", "regId");
		pkAliasMap.put("3", "returnval");
		pkAliasMap.put("4", "entity");
		pkAliasMap.put("5", "categoryPk");
		pkAliasMap.put("6", "subCategoryPk");

		//		pkAliasMap.put("1", "value");
		//		pkAliasMap.put("2", "returnval");
		//		pkAliasMap.put("3", "entity");
		chartBeanV2.setOrder("3,5,6,4");

		String[] orders = StringUtils.isNotBlank(chartBeanV2.getOrder()) ? chartBeanV2.getOrder().split(",") : "3,5,6,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBeanV2.getUserId());
		if (userMaster != null) {

			if ((userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) && userMaster.getDepartmentIdFk() != null && userMaster.getDepartmentIdFk().getIsMaster()) || (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE.getConstantLongVal()))) {
				deptConsider = true;
			} else {
				orderList = orderList.stream().filter(item -> !StringUtils.equals(item, "2")).collect(Collectors.toList());
			}

		}

		StringBuilder groupByClause = new StringBuilder();
		boolean commaNeeded = false;
		for (String item : orderList) {
			if (commaNeeded) {
				groupByClause.append(" , ");
			}
			groupByClause.append(groupMap.get(item));
			commaNeeded = true;
		}

		boolean filingDateApplicable = false;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) as value , ");
		sb.append(" TBENT.CATEGORY_ID_FK as categoryPk, ");
		sb.append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ");
		sb.append(" TRR.RETURN_ID_FK as returnval, ");
		sb.append(" TRR.ENTITY_ID_FK as entity, ");

		sb.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ");
		sb.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ");
		sb.append(" min(TRL.RETURN_LABEL) AS returnName, ");
		sb.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName, ");
		sb.append(" 'pending' as diff ");

		sb.append(" FROM TBL_REVISION_REQUEST TRR ");
		sb.append(" left outer join TBL_ENTITY TBENT ON TBENT.ENTITY_ID=TRR.ENTITY_ID_FK  ");
		sb.append(" left outer join TBL_CATEGORY_LABEL TBCATLBL ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK  ");
		sb.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ");

		sb.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ");
		sb.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ");
		sb.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ");

		sb.append(" left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRR.ENTITY_ID_FK ");
		sb.append(" AND  TEL.LANGUAGE_ID_FK=:languageId ");

		sb.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRR.RETURN_ID_FK and TR.IS_ACTIVE=1 ");
		sb.append(" left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRR.RETURN_ID_FK ");
		sb.append(" AND TRL.LANG_ID_FK=:languageId ");

		sb.append(" inner join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRRM.RETURN_ID_FK = TR.RETURN_ID and TRRM.IS_ACTIVE = 1  ");
		sb.append(" and TRR.RETURN_ID_FK = TRRM.RETURN_ID_FK and TR.IS_ACTIVE = 1 ");
		sb.append(" and TRRM.REGULATOR_ID_FK = :regulatorId ");

		//		sb.append(" where TRR.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" where TR.RETURN_CODE IN (:selectedReturn)");
		sb.append(" and TRR.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");
		sb.append(" and TRR.ACTION_ID_FK = 1");
		sb.append(" and TRR.ADMIN_STATUS_ID_FK = 1");
		sb.append(" and TRR.REVISION_STATUS = 'OPEN'");

		//		if(StringUtils.isNotBlank(chartBean.getFilingDate())) {
		//			filingDateApplicable = true;
		//			sb.append(" and TRR.REPORTING_END_DATE=:endDate  ");
		//		}
		if (StringUtils.isNotBlank(chartBeanV2.getStartDate()) && StringUtils.isNotBlank(chartBeanV2.getEndDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRR.REPORTING_END_DATE >=:startDate And TRR.REPORTING_END_DATE<=:endDate  ");
		}
		sb.append(" group by ").append(groupByClause);
		return sb.toString();
	}

	public ChartNode getPendingFilingData(ChartBeanV2 chartBeanV2, String jobProcessId) throws ParseException {
		LOGGER.info(" getPendingFilingData job {}", jobProcessId);
		Map<String, String> groupMap = new HashMap<>();
		//groupMap.put("2", "TREG.REGULATOR_ID");
		groupMap.put("3", "TREMN.RETURN_ID_FK");
		groupMap.put("4", "TREMN.ENTITY_ID_FK");
		groupMap.put("5", "TBENT.CATEGORY_ID_FK");
		groupMap.put("6", "TBENT.SUB_CATEGORY_ID_FK");

		Map<String, String> nameMap = new HashMap<>();
		//nameMap.put("2", "regLabel");
		nameMap.put("3", "returnName");
		nameMap.put("4", "entityName");
		nameMap.put("5", "category");
		nameMap.put("6", "subCategoryName");

		Map<String, String> pkAliasMap = new HashMap<>();
		//pkAliasMap.put("2", "regId");
		pkAliasMap.put("3", "returnval");
		pkAliasMap.put("4", "entity");
		pkAliasMap.put("5", "categoryPk");
		pkAliasMap.put("6", "subCategoryPk");

		String[] orders = StringUtils.isNotBlank(chartBeanV2.getOrder()) ? chartBeanV2.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBeanV2.getUserId());
		if (userMaster != null) {

			if ((userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) && userMaster.getDepartmentIdFk() != null && userMaster.getDepartmentIdFk().getIsMaster()) || (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE.getConstantLongVal()))) {
				deptConsider = true;
				orderList = orderList.stream().filter(item -> !StringUtils.equalsAny(item, "1")).collect(Collectors.toList());
			} else {
				orderList = orderList.stream().filter(item -> !StringUtils.equalsAny(item, "2", "1")).collect(Collectors.toList());
			}

		}

		StringBuilder groupByClause = new StringBuilder();
		boolean commaNeeded = false;
		for (String item : orderList) {
			if (commaNeeded) {
				groupByClause.append(" , ");
			}
			groupByClause.append(groupMap.get(item));
			commaNeeded = true;
		}
		List<Long> selectedChanelList = new ArrayList<>();
		String[] allotedChanel = chartBeanV2.getSelectedChanel();
		if (!StringUtils.equals(allotedChanel[0], "null") && !StringUtils.equals(allotedChanel[0], "[]")) {
			Arrays.stream(allotedChanel).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedChanelList.add(Long.valueOf(innerItem))));

		}

		List<String> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBeanV2.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(innerItem)));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBeanV2.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBeanV2.getSelectedStatus();
		if (!StringUtils.equals(allotedStatus[0], "null") && !StringUtils.equals(allotedStatus[0], "[]")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBeanV2.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null") && !StringUtils.equals(allotedFilingType[0], "[]")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedDept = new ArrayList<>();
		String[] alloteDept = chartBeanV2.getSelectedDept();
		if (!StringUtils.equals(alloteDept[0], "null") && !StringUtils.equals(alloteDept[0], "[]")) {
			Arrays.stream(alloteDept).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedDept.add(Long.valueOf(innerItem))));

		}

		List<Integer> selectedCategory = new ArrayList<>();
		String[] alloteCategory = chartBeanV2.getSelectedCategory();
		if (!StringUtils.equals(alloteCategory[0], "null") && !StringUtils.equals(alloteCategory[0], "[]")) {
			Arrays.stream(alloteCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedCategory.add(Integer.parseInt(innerItem))));

		}

		List<Integer> selectedSubCategory = new ArrayList<>();
		String[] alloteSubCategory = chartBeanV2.getSelectedSubCategory();
		if (!StringUtils.equals(alloteSubCategory[0], "null") && !StringUtils.equals(alloteSubCategory[0], "[]")) {

			Arrays.stream(alloteSubCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedSubCategory.add(Integer.parseInt(innerItem))));
		}

		StringBuilder sb = new StringBuilder();

		sb.append("SELECT COUNT(*) as value, ");

		sb.append(" TBENT.CATEGORY_ID_FK as categoryPk, ");
		sb.append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ");
		sb.append(" TREMN.RETURN_ID_FK as returnval, ");
		sb.append(" TREMN.ENTITY_ID_FK as entity, ");

		sb.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ");
		sb.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ");
		sb.append(" min(TRL.RETURN_LABEL) AS returnName, ");
		sb.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName ");

		sb.append(" FROM TBL_RETURN_ENTITY_MAPP_NEW TREMN ");
		sb.append("  left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TREMN.ENTITY_ID_FK ");
		sb.append("  AND TEL.LANGUAGE_ID_FK=:languageId ");
		sb.append("  left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TREMN.RETURN_ID_FK ");
		sb.append("  AND TRL.LANG_ID_FK=:languageId ");

		sb.append("  left outer join TBL_ENTITY TBENT  ON TBENT.ENTITY_ID=TREMN.ENTITY_ID_FK ");
		sb.append(" left outer join TBL_CATEGORY_LABEL TBCATLBL ");
		sb.append(" ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK ");
		sb.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ");

		sb.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ");
		sb.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ");
		sb.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ");

		sb.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TREMN.RETURN_ID_FK and TR.IS_ACTIVE=1 ");
		//	.append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TREMN.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ")               																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																	                                        																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																
		//.append("  LEFT OUTER JOIN TBL_REGULATOR TREG ON TREG.REGULATOR_ID=TRRM.REGULATOR_ID_FK AND TREG.IS_ACTIVE=1  ")                             																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																	                                        																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																
		//	.append("  LEFT OUTER JOIN TBL_REGULATOR_LABEL TREGL ON TREGL.REGULATOR_ID_FK=TREG.REGULATOR_ID AND TREGL.LANGUAGE_ID_FK=:languageId ")    																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																	
		sb.append(" left outer join   (SELECT ENTITY_ID_FK, RETURN_ID_FK FROM TBL_RETURNS_UPLOAD_DETAILS TRUD ");
		sb.append("  where TRUD.FILING_STATUS_ID_FK  in (8,9)      ");

		if (StringUtils.isNotBlank(chartBeanV2.getFilingStartDate()) && StringUtils.isNotBlank(chartBeanV2.getFilingDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}
		sb.append(" and TRUD.ISACTIVE=1 ) notToInclude  on notToInclude.ENTITY_ID_FK=TREMN.ENTITY_ID_FK ").append("  and notToInclude.RETURN_ID_FK=TREMN.RETURN_ID_FK").append("  where TREMN.IS_ACTIVE=1 AND notToInclude.RETURN_ID_FK is null ");

		//		sb.append(" and TREMN.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TR.RETURN_CODE IN (:selectedReturn)");
		sb.append(" and TREMN.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");
		/*	if(!selectedDept.isEmpty()) {
			sb.append(" and TREG.REGULATOR_ID IN (:selectedDept)");
		}*/
		sb.append(" and TR.FREQUENCY_ID_FK =:frequency ");

		sb.append(" group by ").append(groupByClause);
		if (StringUtils.equalsIgnoreCase(chartBeanV2.getNotSubmitted(), "true")) {
			sb.append(" union ").append(getFilingDataQueryString(chartBeanV2));
		}

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);

		//Fetching Return
		if (selectedReturn.isEmpty()) {
			ReturnEntityMapDto returnEntityMapDto = new ReturnEntityMapDto();
			returnEntityMapDto.setIsActive(true);
			returnEntityMapDto.setUserId(chartBeanV2.getUserId());
			returnEntityMapDto.setLangCode(chartBeanV2.getLangCode());
			if (chartBeanV2.getRoleId() != null) {
				returnEntityMapDto.setRoleId(chartBeanV2.getRoleId());
			}

			List<ReturnEntityOutputDto> returnGroupMappingDtoList = (List<ReturnEntityOutputDto>) entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);
			for (ReturnEntityOutputDto returnGroupMappingDto : returnGroupMappingDtoList) {
				selectedReturn.add(returnGroupMappingDto.getReturnCode());
			}
		}
		query.setParameter("selectedReturn", selectedReturn);
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setActive(true);
		entityMasterDto.setRoleId(chartBeanV2.getRoleId());
		entityMasterDto.setUserId(chartBeanV2.getUserId());

		entityMasterDto.setLanguageCode(chartBeanV2.getLangCode());

		//Fetching Entity
		ServiceResponse serviceResponse = entityMasterControllerV2.getEntityMasterList(jobProcessId, entityMasterDto);
		List<EntityDto> entityList = (List<EntityDto>) serviceResponse.getResponse();
		if (entityList != null && !entityList.isEmpty()) {
			if (selectedEntity.isEmpty()) {
				selectedEntity.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			}
			if (selectedCategory.isEmpty()) {
				selectedCategory.addAll(entityList.stream().map(inner -> inner.getCategoryDto().getCategoryId()).collect(Collectors.toList()));
				Set<Integer> set = new LinkedHashSet<>();
				set.addAll(selectedCategory);
				selectedCategory.clear();
				selectedCategory.addAll(set);
			}
			if (selectedSubCategory.isEmpty()) {
				selectedSubCategory.addAll(entityList.stream().map(inner -> inner.getSubCategoryDto().getSubCategoryId()).collect(Collectors.toList()));
				Set<Integer> set = new LinkedHashSet<>();
				set.addAll(selectedSubCategory);
				selectedSubCategory.clear();
				selectedSubCategory.addAll(set);
			}
		}

		query.setParameter("selectedCategory", selectedCategory);
		query.setParameter("selectedSubCategory", selectedSubCategory);

		query.setParameter("selectedEntity", selectedEntity);
		if (!selectedStatus.isEmpty()) {
			query.setParameter("selectedStatus", selectedStatus);
		}
		/*if(!selectedDept.isEmpty()) {
			query.setParameter("selectedDept", selectedDept);
		}*/
		query.setParameter("frequency", chartBeanV2.getFrequency());
		if (dateFilterApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBeanV2.getFilingStartDate(), chartBeanV2.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBeanV2.getFilingDate(), chartBeanV2.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		List<Tuple> result = query.setParameter("languageId", chartBeanV2.getLangId()).getResultList();
		ChartNode node = new ChartNode();
		Set<String> unique = new HashSet<>();
		for (Tuple item : result) {

			ChartNode level1 = new ChartNode();
			level1.setChildren(null);
			level1.setId("filing");
			level1.setName("filing");
			level1.setParent(null);
			if (unique.add(level1.getId())) {
				node.getChildren().add(level1);
			}
			ChartNode level2 = new ChartNode();
			level2.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
			level2.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
			level2.setParent("filing");
			if (unique.add(level2.getId())) {
				node.getChildren().add(level2);
			}

			ChartNode level3 = new ChartNode();
			level3.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			level3.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
			level3.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
			if (unique.add(level3.getId())) {
				node.getChildren().add(level3);
			}

			ChartNode level4 = new ChartNode();
			level4.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
			level4.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
			level4.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			if (unique.add(level4.getId())) {
				node.getChildren().add(level4);
			}

			if (orderList.size() == 4) {
				ChartNode level5 = new ChartNode();
				level5.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
				level5.setName(ObjectCache.getLabelKeyValue(chartBeanV2.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));

				level5.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
				level5.setValue((BigInteger) item.get("value"));
				if (unique.add(level5.getId())) {
					node.getChildren().add(level5);
				}

			} else {
				level4.setValue((BigInteger) item.get("value"));
			}

		}

		return node;
	}

	public String getFilingDataQueryString(ChartBeanV2 chartBeanV2) throws ParseException {
		LOGGER.info(" getFilingData Query String job ");
		Map<String, String> groupMap = new HashMap<>();
		//groupMap.put("1", "TFD.UPLOAD_CHANNEL_ID_FK");
		//groupMap.put("2", "TREG.REGULATOR_ID");
		groupMap.put("3", "TRUD.RETURN_ID_FK");
		groupMap.put("4", "TRUD.ENTITY_ID_FK");
		groupMap.put("5", "TBENT.CATEGORY_ID_FK");
		groupMap.put("6", "TBENT.SUB_CATEGORY_ID_FK");

		Map<String, String> nameMap = new HashMap<>();
		//nameMap.put("1", "chanel");
		//nameMap.put("2", "regLabel");
		nameMap.put("3", "returnName");
		nameMap.put("4", "entityName");
		nameMap.put("5", "category");
		nameMap.put("6", "subCategoryName");

		Map<String, String> pkAliasMap = new HashMap<>();
		//pkAliasMap.put("1", "chanelPk");
		//pkAliasMap.put("2", "regId");
		pkAliasMap.put("3", "returnval");
		pkAliasMap.put("4", "entity");
		pkAliasMap.put("5", "categoryPk");
		pkAliasMap.put("6", "subCategoryPk");
		String[] orders = StringUtils.isNotBlank(chartBeanV2.getOrder()) ? chartBeanV2.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBeanV2.getUserId());
		if (userMaster != null) {

			if ((userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) && userMaster.getDepartmentIdFk() != null && userMaster.getDepartmentIdFk().getIsMaster()) || (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE.getConstantLongVal()))) {
				orderList = orderList.stream().filter(item -> !StringUtils.equalsAny(item, "1")).collect(Collectors.toList());
				deptConsider = true;
			} else {
				orderList = orderList.stream().filter(item -> !StringUtils.equals(item, "2")).collect(Collectors.toList());
			}

		}

		StringBuilder groupByClause = new StringBuilder();
		boolean commaNeeded = false;
		for (String item : orderList) {
			if (commaNeeded) {
				groupByClause.append(" , ");
			}
			groupByClause.append(groupMap.get(item));
			commaNeeded = true;
		}

		List<String> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBeanV2.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(innerItem)));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBeanV2.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBeanV2.getSelectedStatus();
		if (!StringUtils.equals(allotedStatus[0], "null") && !StringUtils.equals(allotedStatus[0], "[]")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBeanV2.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null") && !StringUtils.equals(allotedFilingType[0], "[]")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedCategory = new ArrayList<>();
		String[] alloteCategory = chartBeanV2.getSelectedCategory();
		if (!StringUtils.equals(alloteCategory[0], "null") && !StringUtils.equals(alloteCategory[0], "[]")) {
			Arrays.stream(alloteCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedCategory.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedSubCategory = new ArrayList<>();
		String[] alloteSubCategory = chartBeanV2.getSelectedSubCategory();
		if (!StringUtils.equals(alloteSubCategory[0], "null") && !StringUtils.equals(alloteSubCategory[0], "[]")) {

			Arrays.stream(alloteSubCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedSubCategory.add(Long.valueOf(innerItem))));
		}

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) as value, ");
		sb.append(" TBENT.CATEGORY_ID_FK as categoryPk, ");
		sb.append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ");
		sb.append(" TRUD.RETURN_ID_FK as returnval, ");
		sb.append(" TRUD.ENTITY_ID_FK as entity, ");
		sb.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ");
		sb.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ");
		sb.append(" min(TRL.RETURN_LABEL) AS returnName, ");
		sb.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName ");

		sb.append(" FROM TBL_RETURNS_UPLOAD_DETAILS TRUD LEFT OUTER JOIN TBL_FILE_DETAILS TFD ");
		sb.append("  ON TFD.ID=TRUD.FILE_DETAILS_ID_FK left outer join TBL_UPLOAD_CHANNEL TUC  ");
		sb.append("  ON TFD.UPLOAD_CHANNEL_ID_FK=TUC.UPLOAD_CHANNEL_ID AND TUC.IS_ACTIVE=1  ");
		sb.append("  left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRUD.ENTITY_ID_FK ");
		sb.append("  AND TEL.LANGUAGE_ID_FK=:languageId ");

		sb.append(" left outer join TBL_ENTITY TBENT ON TBENT.ENTITY_ID=TRUD.ENTITY_ID_FK ");
		sb.append(" left outer join TBL_CATEGORY_LABEL TBCATLBL ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK ");
		sb.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ");

		sb.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ");
		sb.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ");
		sb.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ");

		sb.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRUD.RETURN_ID_FK and TR.IS_ACTIVE=1 ");
		sb.append("  left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRUD.RETURN_ID_FK ");
		sb.append("  AND TRL.LANG_ID_FK=:languageId ");
		sb.append(" inner join  (Select Max(mtr.UPLOAD_ID) as UPLOAD_ID from TBL_RETURNS_UPLOAD_DETAILS mtr ");
		sb.append("  group by mtr.RETURN_ID_FK ,mtr.ENTITY_ID_FK,mtr.END_DATE,mtr.FIN_YR_FREQUENCY_ID_FK) maxTable ");
		sb.append(" on  maxTable.UPLOAD_ID = TRUD.UPLOAD_ID ");
		//.append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRUD.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ")              																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																                                            																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																
		//.append("  LEFT OUTER JOIN TBL_REGULATOR TREG ON TREG.REGULATOR_ID=TRRM.REGULATOR_ID_FK AND TREG.IS_ACTIVE=1  ")                         																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																                                            																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																
		//.append("  LEFT OUTER JOIN TBL_REGULATOR_LABEL TREGL ON TREGL.REGULATOR_ID_FK=TREG.REGULATOR_ID AND TREGL.LANGUAGE_ID_FK=:languageId ")  																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																                                            																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																
		sb.append("  where  TFD.UPLOAD_CHANNEL_ID_FK is not null ");

		/*if(!selectedChanelList.isEmpty()) {
			sb.append(" and TUC.UPLOAD_CHANNEL_ID IN (:selectedChanelList)");
		}*/
		sb.append(" and TRUD.FILING_STATUS_ID_FK  in ( 9 )");

		//		sb.append(" and TRUD.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TR.RETURN_CODE IN (:selectedReturn)");
		sb.append(" and TRUD.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");
		if (chartBeanV2.getFrequency() != null) {
			sb.append(" and TR.FREQUENCY_ID_FK =:frequency ");
		}
		if (!selectedStatus.isEmpty()) {
			sb.append(" and TRUD.FILING_STATUS_ID_FK IN (:selectedStatus)");
		}
		/*if(!selectedDept.isEmpty()) {
			sb.append(" and TREG.REGULATOR_ID IN (:selectedDept)");
		}*/

		boolean first = true;
		for (Long filingType : selectedFilingType) {
			if (first) {
				sb.append(" and ( ");
				first = false;
			} else {
				sb.append(" or ");
			}
			if (filingType.compareTo(0l) == 0) {
				sb.append(" ( TRUD.UNLOCK_REQUEST_ID_FK is null and  TRUD.REVISION_REQUEST_ID_FK is null ) ");
			} else if (filingType.compareTo(1l) == 0) {
				sb.append(" (  TRUD.UNLOCK_REQUEST_ID_FK is not null ) ");
			} else if (filingType.compareTo(2l) == 0) {
				sb.append(" (  TRUD.REVISION_REQUEST_ID_FK is not null ) ");
			}
		}
		if (!selectedFilingType.isEmpty()) {
			sb.append(" ) ");
		}
		if (StringUtils.isNotBlank(chartBeanV2.getFilingStartDate()) && StringUtils.isNotBlank(chartBeanV2.getFilingDate())) {
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}

		sb.append(" group by ").append(groupByClause);

		return sb.toString();
	}

}
