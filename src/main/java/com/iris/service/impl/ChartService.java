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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.caching.ObjectCache;
import com.iris.controller.DashboardChartSettingBean;
import com.iris.controller.EntityMasterController;
import com.iris.controller.ReturnGroupController;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ChartBean;
import com.iris.dto.ChartNode;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.model.EntityBean;
import com.iris.model.UserMaster;
import com.iris.repository.DashboardChartSettingRepo;
import com.iris.util.constant.GeneralConstants;

/**
 * Chart specific Service
 * 
 * @author svishwakarma
 *
 */
@Service
public class ChartService {

	private static final Logger Logger = LoggerFactory.getLogger(ChartService.class);

	@Autowired
	EntityManager em;

	@Autowired
	private UserMasterService userMasterService;

	@Autowired
	EntityMasterController entityMasterController;

	@Autowired
	ReturnGroupController returnGroupController;

	@Autowired
	DashboardChartSettingRepo dashboardChartSettingRepo;

	public ChartNode getFilingData_OLD(ChartBean chartBean, String jobProcessId) throws ParseException {
		Logger.info(" getFilingData job {}", jobProcessId);
		Map<String, String> groupMap = new HashMap<>();
		groupMap.put("1", "TFD.UPLOAD_CHANNEL_ID_FK");
		groupMap.put("2", "TREG.REGULATOR_ID");
		groupMap.put("3", "TRUD.RETURN_ID_FK");
		groupMap.put("4", "TRUD.ENTITY_ID_FK");

		Map<String, String> nameMap = new HashMap<>();
		nameMap.put("1", "chanel");
		nameMap.put("2", "regLabel");
		nameMap.put("3", "returnName");
		nameMap.put("4", "entityName");

		Map<String, String> pkAliasMap = new HashMap<>();
		pkAliasMap.put("1", "chanelPk");
		pkAliasMap.put("2", "regId");
		pkAliasMap.put("3", "returnval");
		pkAliasMap.put("4", "entity");
		String[] orders = StringUtils.isNotBlank(chartBean.getOrder()) ? chartBean.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
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
		String[] allotedChanel = chartBean.getSelectedChanel();
		if (!StringUtils.equals(allotedChanel[0], "null") && !StringUtils.equals(allotedChanel[0], "[]")) {
			Arrays.stream(allotedChanel).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedChanelList.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBean.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBean.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBean.getSelectedStatus();
		if (!StringUtils.equals(allotedStatus[0], "null") && !StringUtils.equals(allotedStatus[0], "[]")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBean.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null") && !StringUtils.equals(allotedFilingType[0], "[]")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedDept = new ArrayList<>();
		String[] alloteDept = chartBean.getSelectedDept();
		if (!StringUtils.equals(alloteDept[0], "null") && !StringUtils.equals(alloteDept[0], "[]")) {
			Arrays.stream(alloteDept).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedDept.add(Long.valueOf(innerItem))));

		}

		boolean filingDateApplicable = false;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) as value , ").append(" TFD.UPLOAD_CHANNEL_ID_FK as chanelPk, ");
		if (deptConsider) {
			sb.append(" min(TREGL.REGULATOR_LABEL) as regLabel ,").append(" TREG.REGULATOR_ID as regId ,  ");
		}
		sb.append(" TRUD.RETURN_ID_FK as returnval, ").append(" TRUD.ENTITY_ID_FK as entity,min(TUC.UPLOAD_CHENNEL_DESC) as chanel , ").append(" min(TEL.ENTITY_NAME_LABEL) AS entityName, ").append(" min(TRL.RETURN_LABEL) AS returnName ").append(" FROM TBL_RETURNS_UPLOAD_DETAILS TRUD LEFT OUTER JOIN TBL_FILE_DETAILS TFD ").append("  ON TFD.ID=TRUD.FILE_DETAILS_ID_FK left outer join TBL_UPLOAD_CHANNEL TUC  ").append("  ON TFD.UPLOAD_CHANNEL_ID_FK=TUC.UPLOAD_CHANNEL_ID AND TUC.IS_ACTIVE=1  ").append("  left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRUD.ENTITY_ID_FK ").append("   AND  TEL.LANGUAGE_ID_FK=:languageId ").append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRUD.RETURN_ID_FK and TR.IS_ACTIVE=1 ").append("  left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRUD.RETURN_ID_FK ").append("  AND TRL.LANG_ID_FK=:languageId ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		//.append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRUD.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ") 
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  LEFT OUTER JOIN TBL_REGULATOR TREG ON TREG.REGULATOR_ID=TRRM.REGULATOR_ID_FK AND TREG.IS_ACTIVE=1  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  LEFT OUTER JOIN TBL_REGULATOR_LABEL TREGL ON TREGL.REGULATOR_ID_FK=TREG.REGULATOR_ID AND TREGL.LANGUAGE_ID_FK=:languageId ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  where  TFD.UPLOAD_CHANNEL_ID_FK is not null ");
		if (!selectedChanelList.isEmpty()) {
			sb.append(" and TUC.UPLOAD_CHANNEL_ID IN (:selectedChanelList)");
		}
		if (StringUtils.equalsIgnoreCase(chartBean.getSubmitOrPending(), "false")) {
			sb.append(" and TRUD.FILING_STATUS_ID_FK!=" + GeneralConstants.APPROVED_BY_RBI.getConstantIntVal());
		} else {
			sb.append(" and TRUD.FILING_STATUS_ID_FK=" + GeneralConstants.APPROVED_BY_RBI.getConstantIntVal());
		}
		sb.append(" and TRUD.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TRUD.ENTITY_ID_FK IN (:selectedEntity)");
		if (chartBean.getFrequency() != null) {
			sb.append(" and TR.FREQUENCY_ID_FK =:frequency ");
		}
		if (!selectedStatus.isEmpty()) {
			sb.append(" and TRUD.FILING_STATUS_ID_FK IN (:selectedStatus)");
		}
		if (!selectedDept.isEmpty()) {
			sb.append(" and TREG.REGULATOR_ID IN (:selectedDept)");
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
		if (StringUtils.isNotBlank(chartBean.getFilingStartDate()) && StringUtils.isNotBlank(chartBean.getFilingDate())) {
			filingDateApplicable = true;
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}
		if (StringUtils.isNotBlank(chartBean.getStartDate()) && StringUtils.isNotBlank(chartBean.getEndDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRUD.END_DATE >=:startDate And TRUD.END_DATE<=:endDate  ");
		}
		sb.append(" group by ").append(groupByClause);

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);
		if (!selectedChanelList.isEmpty()) {
			query.setParameter("selectedChanelList", selectedChanelList);
		}
		if (selectedReturn.isEmpty()) {
			ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
			returnGroupMappingRequest.setIsActive(true);
			returnGroupMappingRequest.setUserId(chartBean.getUserId());
			returnGroupMappingRequest.setLangId(chartBean.getLangId());
			returnGroupMappingRequest.setRoleId(chartBean.getRoleId());
			if (chartBean.getFrequency() != null) {
				returnGroupMappingRequest.setFrequencyId(chartBean.getFrequency());
			}

			List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
			for (ReturnGroupMappingDto item : returnList) {
				selectedReturn.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
			}

		}
		query.setParameter("selectedReturn", selectedReturn);
		if (selectedEntity.isEmpty()) {
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setActive(true);
			entityMasterDto.setRoleId(chartBean.getRoleId());
			entityMasterDto.setUserId(chartBean.getUserId());

			entityMasterDto.setLanguageCode(chartBean.getLangCode());
			List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
			if (entityList != null && !entityList.isEmpty()) {
				selectedEntity.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			}
		}
		query.setParameter("selectedEntity", selectedEntity);
		if (!selectedStatus.isEmpty()) {
			query.setParameter("selectedStatus", selectedStatus);
		}
		if (!selectedDept.isEmpty()) {
			query.setParameter("selectedDept", selectedDept);
		}
		if (chartBean.getFrequency() != null) {
			query.setParameter("frequency", chartBean.getFrequency());
		}
		if (dateFilterApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBean.getStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBean.getEndDate(), chartBean.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		if (filingDateApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBean.getFilingStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBean.getFilingDate(), chartBean.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		List<Tuple> result = query.setParameter("languageId", chartBean.getLangId()).getResultList();
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
			level2.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
			level2.setParent("filing");
			if (unique.add(level2.getId())) {
				node.getChildren().add(level2);
			}

			ChartNode level3 = new ChartNode();
			level3.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			level3.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
			level3.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
			if (unique.add(level3.getId())) {
				node.getChildren().add(level3);
			}

			ChartNode level4 = new ChartNode();
			level4.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
			level4.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
			level4.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			if (unique.add(level4.getId())) {
				node.getChildren().add(level4);
			}

			if (orderList.size() == 4) {
				ChartNode level5 = new ChartNode();
				level5.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
				level5.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));
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

	public ChartNode getFilingData(ChartBean chartBean, String jobProcessId) throws ParseException {
		Logger.info(" getFilingData job {}", jobProcessId);
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

		String[] orders = StringUtils.isNotBlank(chartBean.getOrder()) ? chartBean.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
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
		String[] allotedChanel = chartBean.getSelectedChanel();
		if (!StringUtils.equals(allotedChanel[0], "null") && !StringUtils.equals(allotedChanel[0], "[]")) {
			Arrays.stream(allotedChanel).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedChanelList.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBean.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBean.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBean.getSelectedStatus();
		if (!StringUtils.equals(allotedStatus[0], "null") && !StringUtils.equals(allotedStatus[0], "[]")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBean.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null") && !StringUtils.equals(allotedFilingType[0], "[]")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedDept = new ArrayList<>();
		String[] alloteDept = chartBean.getSelectedDept();
		if (!StringUtils.equals(alloteDept[0], "null") && !StringUtils.equals(alloteDept[0], "[]")) {
			Arrays.stream(alloteDept).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedDept.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedCategory = new ArrayList<>();
		String[] alloteCategory = chartBean.getSelectedCategory();
		if (!StringUtils.equals(alloteCategory[0], "null") && !StringUtils.equals(alloteCategory[0], "[]")) {
			Arrays.stream(alloteCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedCategory.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedSubCategory = new ArrayList<>();
		String[] alloteSubCategory = chartBean.getSelectedSubCategory();
		if (!StringUtils.equals(alloteSubCategory[0], "null") && !StringUtils.equals(alloteSubCategory[0], "[]")) {

			Arrays.stream(alloteSubCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedSubCategory.add(Long.valueOf(innerItem))));
		}

		boolean filingDateApplicable = false;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) as value , ").append(" TBENT.CATEGORY_ID_FK as categoryPk, ").append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ").append(" TRUD.RETURN_ID_FK as returnval, ").append(" TRUD.ENTITY_ID_FK as entity, ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TRL.RETURN_LABEL) AS returnName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" FROM TBL_RETURNS_UPLOAD_DETAILS TRUD LEFT OUTER JOIN TBL_FILE_DETAILS TFD ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TFD.ID=TRUD.FILE_DETAILS_ID_FK left outer join TBL_ENTITY TBENT  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TBENT.ENTITY_ID=TRUD.ENTITY_ID_FK left outer join TBL_CATEGORY_LABEL TBCATLBL ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRUD.ENTITY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TEL.LANGUAGE_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRUD.RETURN_ID_FK and TR.IS_ACTIVE=1 ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRUD.RETURN_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND TRL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		//.append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRUD.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ") 
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" inner join  (Select Max(mtr.UPLOAD_ID) as UPLOAD_ID from TBL_RETURNS_UPLOAD_DETAILS mtr where mtr.FILING_STATUS_ID_FK=" + GeneralConstants.APPROVED_BY_RBI.getConstantIntVal())
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  group by mtr.RETURN_ID_FK ,mtr.ENTITY_ID_FK,mtr.END_DATE,mtr.FIN_YR_FREQUENCY_ID_FK) maxTable ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" on  maxTable.UPLOAD_ID = TRUD.UPLOAD_ID ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" where  TFD.UPLOAD_CHANNEL_ID_FK is not null ");
		/*	if(!selectedChanelList.isEmpty()) {
			sb.append(" and TUC.UPLOAD_CHANNEL_ID IN (:selectedChanelList)");
		}*/
		if (StringUtils.equalsIgnoreCase(chartBean.getSubmitOrPending(), "false")) {
			sb.append(" and TRUD.FILING_STATUS_ID_FK in (9) ");//in clause impromevent
		} else {
			sb.append(" and TRUD.FILING_STATUS_ID_FK=" + GeneralConstants.APPROVED_BY_RBI.getConstantIntVal());
		}
		sb.append(" and TRUD.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TRUD.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");

		if (chartBean.getFrequency() != null) {
			sb.append(" and TR.FREQUENCY_ID_FK =:frequency ");
		}
		if (!selectedStatus.isEmpty()) {
			sb.append(" and TRUD.FILING_STATUS_ID_FK IN (:selectedStatus)");
		}
		/*
		 * if(!selectedDept.isEmpty()) {
		 * sb.append(" and TREG.REGULATOR_ID IN (:selectedDept)"); }
		 */

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
		if (StringUtils.isNotBlank(chartBean.getFilingStartDate()) && StringUtils.isNotBlank(chartBean.getFilingDate())) {
			filingDateApplicable = true;
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}
		if (StringUtils.isNotBlank(chartBean.getStartDate()) && StringUtils.isNotBlank(chartBean.getEndDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRUD.END_DATE >=:startDate And TRUD.END_DATE<=:endDate  ");
		}
		sb.append(" group by ").append(groupByClause);

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);
		/*
		 * if(!selectedChanelList.isEmpty()) { query.setParameter("selectedChanelList",
		 * selectedChanelList); }
		 */
		if (selectedReturn.isEmpty()) {
			ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
			returnGroupMappingRequest.setIsActive(true);
			returnGroupMappingRequest.setUserId(chartBean.getUserId());
			returnGroupMappingRequest.setLangId(chartBean.getLangId());
			returnGroupMappingRequest.setRoleId(chartBean.getRoleId());
			if (chartBean.getFrequency() != null) {
				returnGroupMappingRequest.setFrequencyId(chartBean.getFrequency());
			}

			List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
			for (ReturnGroupMappingDto item : returnList) {
				selectedReturn.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
			}

		}
		query.setParameter("selectedReturn", selectedReturn);
		//if(selectedEntity.isEmpty()) {
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setActive(true);
		entityMasterDto.setRoleId(chartBean.getRoleId());
		entityMasterDto.setUserId(chartBean.getUserId());

		entityMasterDto.setLanguageCode(chartBean.getLangCode());
		List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
		if (entityList != null && !entityList.isEmpty()) {
			if (selectedEntity.isEmpty()) {
				selectedEntity.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			}
			if (selectedCategory.isEmpty()) {
				selectedCategory.addAll(entityList.stream().map(inner -> inner.getCategory().getCategoryId()).collect(Collectors.toList()));
				Set<Long> set = new LinkedHashSet<>();
				set.addAll(selectedCategory);
				selectedCategory.clear();
				selectedCategory.addAll(set);
			}
			if (selectedSubCategory.isEmpty()) {
				selectedSubCategory.addAll(entityList.stream().map(inner -> inner.getSubCategory().getSubCategoryId()).collect(Collectors.toList()));
				Set<Long> set = new LinkedHashSet<>();
				set.addAll(selectedSubCategory);
				selectedSubCategory.clear();
				selectedSubCategory.addAll(set);
			}
		}
		//}

		query.setParameter("selectedEntity", selectedEntity);
		if (!selectedStatus.isEmpty()) {
			query.setParameter("selectedStatus", selectedStatus);
		}
		/*	if(!selectedDept.isEmpty()) {
			query.setParameter("selectedDept", selectedDept);
		}*/
		if (chartBean.getFrequency() != null) {
			query.setParameter("frequency", chartBean.getFrequency());
		}

		if (!selectedSubCategory.isEmpty()) {
			query.setParameter("selectedSubCategory", selectedSubCategory);
		}

		if (!selectedCategory.isEmpty()) {
			query.setParameter("selectedCategory", selectedCategory);
		}
		if (dateFilterApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBean.getStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBean.getEndDate(), chartBean.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		if (filingDateApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBean.getFilingStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBean.getFilingDate(), chartBean.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		List<Tuple> result = query.setParameter("languageId", chartBean.getLangId()).getResultList();
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
			level2.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
			level2.setParent("filing");
			if (unique.add(level2.getId())) {
				node.getChildren().add(level2);
			}

			ChartNode level3 = new ChartNode();
			level3.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			level3.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
			level3.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
			if (unique.add(level3.getId())) {
				node.getChildren().add(level3);
			}

			ChartNode level4 = new ChartNode();
			level4.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
			level4.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
			level4.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			if (unique.add(level4.getId())) {
				node.getChildren().add(level4);
			}

			if (orderList.size() == 4) {
				ChartNode level5 = new ChartNode();
				level5.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
				level5.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));
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

	public ChartNode getFilingDataForRevisionReq(ChartBean chartBean, String jobProcessId) throws ParseException {
		Logger.info(" getFilingData job {}", jobProcessId);
		//		Map<String,String> groupMap = new HashMap<>();
		//
		//		groupMap.put("1", "TRR.RETURN_ID_FK"); 
		//		groupMap.put("2", "TBENT.CATEGORY_ID_FK");
		//		groupMap.put("3", "TBENT.SUB_CATEGORY_ID_FK");
		//		groupMap.put("4", "TRR.ENTITY_ID_FK");
		//
		//		Map<String,String> nameMap = new HashMap<>();
		//		nameMap.put("1", "returnName");
		//		nameMap.put("2", "entityName");
		//		nameMap.put("3", "category");
		//		nameMap.put("4", "subCategoryName");
		//
		//		Map<String,String> pkAliasMap = new HashMap<>();
		//		pkAliasMap.put("1", "returnval");
		//		pkAliasMap.put("2", "categoryPk");
		//		pkAliasMap.put("3", "subCategoryPk");
		//		pkAliasMap.put("4", "entity");
		//		pkAliasMap.put("5", "chanelPk");
		//		pkAliasMap.put("6", "regId");
		//
		//		//chartBean.setOrder("3,5,6,4");

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
		chartBean.setOrder("3,5,6,4");

		//		List<DashboardChartSettingBean> dashboardChartSettingRevReqBeanList = dashboardChartSettingRepo.findByIsActiveTrueForRevisionReq();
		//		dashboardChartSettingRevReqBeanList.sort((DashboardChartSettingBean s1, DashboardChartSettingBean s2) -> s1.getDashboardChartItemsOrder().compareTo(s2.getDashboardChartItemsOrder()));
		//		
		//		String order="";
		//		for(int i=0; i<dashboardChartSettingRevReqBeanList.size() ;i++) {
		//			if(i==0) {
		//				order = dashboardChartSettingRevReqBeanList.get(i).getDashboardChartItemsOrder().toString();
		//			} else {
		//				order += ","+ dashboardChartSettingRevReqBeanList.get(i).getDashboardChartItemsOrder();
		//			}
		//		}
		//		
		//		chartBean.setOrder(order);
		//		
		//		System.out.println("dashboardChartSettingRevReqBeanList:"+dashboardChartSettingRevReqBeanList);
		String[] orders = StringUtils.isNotBlank(chartBean.getOrder()) ? chartBean.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
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

		List<Long> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBean.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBean.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedCategory = new ArrayList<>();
		String[] alloteCategory = chartBean.getSelectedCategory();
		if (!StringUtils.equals(alloteCategory[0], "null") && !StringUtils.equals(alloteCategory[0], "[]")) {
			Arrays.stream(alloteCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedCategory.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedSubCategory = new ArrayList<>();
		String[] alloteSubCategory = chartBean.getSelectedSubCategory();
		if (!StringUtils.equals(alloteSubCategory[0], "null") && !StringUtils.equals(alloteSubCategory[0], "[]")) {

			Arrays.stream(alloteSubCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedSubCategory.add(Long.valueOf(innerItem))));
		}

		boolean filingDateApplicable = false;
		StringBuilder sb = new StringBuilder();
		sb.append("select min(part.value) as value , ").append("part.categoryPk , ").append("part.subCategoryPk , ").append("part.returnval , ").append("part.entity , ").append("min(part.category) as category , ").append("min(part.subCategoryName) as subCategoryName , ").append("min(part.returnName) as returnName , ").append("min(part.entityName) as entityName , ").append("part.diff from ( ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" SELECT COUNT(*) as value , ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" TBENT.CATEGORY_ID_FK as categoryPk, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" TRR.RETURN_ID_FK as returnval, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" TRR.ENTITY_ID_FK as entity, ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TRL.RETURN_LABEL) AS returnName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" 'total' as diff ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" FROM TBL_REVISION_REQUEST TRR ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_ENTITY TBENT ON TBENT.ENTITY_ID=TRR.ENTITY_ID_FK  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_CATEGORY_LABEL TBCATLBL ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRR.ENTITY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TEL.LANGUAGE_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRR.RETURN_ID_FK and TR.IS_ACTIVE=1 ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRR.RETURN_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND TRL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" inner join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRRM.RETURN_ID_FK = TR.RETURN_ID and TRRM.IS_ACTIVE = 1  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" and TRR.RETURN_ID_FK = TRRM.RETURN_ID_FK and TR.IS_ACTIVE = 1 ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" and TRRM.REGULATOR_ID_FK = :regulatorId ");

		sb.append(" where TRR.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TRR.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");

		if (StringUtils.isNotBlank(chartBean.getStartDate()) && StringUtils.isNotBlank(chartBean.getEndDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRR.REPORTING_END_DATE >=:startDate And TRR.REPORTING_END_DATE<=:endDate  ");
		}
		sb.append(" group by ").append(groupByClause);

		sb.append(" union ").append(getPendingRevisionRequestQueryString(chartBean));
		sb.append(" ) part   group by diff ,returnval,categoryPk , subCategoryPk , entity");

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);

		if (selectedReturn.isEmpty()) {
			ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
			returnGroupMappingRequest.setIsActive(true);
			returnGroupMappingRequest.setUserId(chartBean.getUserId());
			returnGroupMappingRequest.setLangId(chartBean.getLangId());
			returnGroupMappingRequest.setRoleId(chartBean.getRoleId());
			//			if(chartBean.getFrequency()!=null) {
			//				returnGroupMappingRequest.setFrequencyId(chartBean.getFrequency());
			//			}

			List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
			for (ReturnGroupMappingDto item : returnList) {
				selectedReturn.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
			}

		}
		query.setParameter("selectedReturn", selectedReturn);
		//		if(selectedEntity.isEmpty()) {
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setActive(true);
		entityMasterDto.setRoleId(chartBean.getRoleId());
		entityMasterDto.setUserId(chartBean.getUserId());

		entityMasterDto.setLanguageCode(chartBean.getLangCode());
		List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
		if (entityList != null && !entityList.isEmpty()) {
			if (selectedEntity.isEmpty()) {
				selectedEntity.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			}
			if (selectedCategory.isEmpty()) {
				selectedCategory.addAll(entityList.stream().map(inner -> inner.getCategory().getCategoryId()).collect(Collectors.toList()));
				Set<Long> set = new LinkedHashSet<>();
				set.addAll(selectedCategory);
				selectedCategory.clear();
				selectedCategory.addAll(set);
			}
			if (selectedSubCategory.isEmpty()) {
				selectedSubCategory.addAll(entityList.stream().map(inner -> inner.getSubCategory().getSubCategoryId()).collect(Collectors.toList()));
				Set<Long> set = new LinkedHashSet<>();
				set.addAll(selectedSubCategory);
				selectedSubCategory.clear();
				selectedSubCategory.addAll(set);
			}
		}
		//		}

		query.setParameter("selectedEntity", selectedEntity);

		query.setParameter("regulatorId", regulatorId);

		if (!selectedSubCategory.isEmpty()) {
			query.setParameter("selectedSubCategory", selectedSubCategory);
		}

		if (!selectedCategory.isEmpty()) {
			query.setParameter("selectedCategory", selectedCategory);
		}
		if (dateFilterApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBean.getStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBean.getEndDate(), chartBean.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		if (filingDateApplicable) {
			//			Date startDate = DateManip.convertStringToDate(chartBean.getFilingStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBean.getFilingDate(), chartBean.getDateFormat());
			query.setParameter("endDate", endDate);
		}
		List<Tuple> result = query.setParameter("languageId", chartBean.getLangId()).getResultList();
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
				level2.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
				level2.setParent("TotalRevReq");
				if (unique1.add(level2.getId())) {
					node.getChildren().add(level2);
				}

				ChartNode level3 = new ChartNode();
				level3.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				level3.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
				level3.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
				if (unique1.add(level3.getId())) {
					node.getChildren().add(level3);
				}

				ChartNode level4 = new ChartNode();
				level4.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
				level4.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
				level4.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				if (unique1.add(level4.getId())) {
					node.getChildren().add(level4);
				}

				if (orderList.size() == 4) {
					ChartNode level5 = new ChartNode();
					level5.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
					level5.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));
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
				level2.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
				level2.setParent("PendingRevReq");
				if (unique1.add(level2.getId())) {
					node.getChildren().add(level2);
				}

				ChartNode level3 = new ChartNode();
				level3.setId("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				level3.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
				level3.setParent("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
				if (unique1.add(level3.getId())) {
					node.getChildren().add(level3);
				}

				ChartNode level4 = new ChartNode();
				level4.setId("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
				level4.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
				level4.setParent("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				if (unique1.add(level4.getId())) {
					node.getChildren().add(level4);
				}

				if (orderList.size() == 4) {
					ChartNode level5 = new ChartNode();
					level5.setId("Pending" + pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
					level5.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));
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

	public String getPendingRevisionRequestQueryString(ChartBean chartBean) throws ParseException {
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
		chartBean.setOrder("3,5,6,4");

		String[] orders = StringUtils.isNotBlank(chartBean.getOrder()) ? chartBean.getOrder().split(",") : "3,5,6,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
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
		sb.append("SELECT COUNT(*) as value , ").append(" TBENT.CATEGORY_ID_FK as categoryPk, ").append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ").append(" TRR.RETURN_ID_FK as returnval, ").append(" TRR.ENTITY_ID_FK as entity, ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TRL.RETURN_LABEL) AS returnName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" 'pending' as diff ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" FROM TBL_REVISION_REQUEST TRR ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_ENTITY TBENT ON TBENT.ENTITY_ID=TRR.ENTITY_ID_FK  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_CATEGORY_LABEL TBCATLBL ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRR.ENTITY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TEL.LANGUAGE_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRR.RETURN_ID_FK and TR.IS_ACTIVE=1 ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRR.RETURN_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND TRL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" inner join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRRM.RETURN_ID_FK = TR.RETURN_ID and TRRM.IS_ACTIVE = 1  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" and TRR.RETURN_ID_FK = TRRM.RETURN_ID_FK and TR.IS_ACTIVE = 1 ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" and TRRM.REGULATOR_ID_FK = :regulatorId ");

		sb.append(" where TRR.RETURN_ID_FK IN (:selectedReturn)");
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
		if (StringUtils.isNotBlank(chartBean.getStartDate()) && StringUtils.isNotBlank(chartBean.getEndDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRR.REPORTING_END_DATE >=:startDate And TRR.REPORTING_END_DATE<=:endDate  ");
		}
		sb.append(" group by ").append(groupByClause);
		return sb.toString();
	}

	public ChartNode getPendingFilingData(ChartBean chartBean, String jobProcessId) throws ParseException {
		Logger.info(" getPendingFilingData job {}", jobProcessId);
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

		String[] orders = StringUtils.isNotBlank(chartBean.getOrder()) ? chartBean.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
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
		String[] allotedChanel = chartBean.getSelectedChanel();
		if (!StringUtils.equals(allotedChanel[0], "null") && !StringUtils.equals(allotedChanel[0], "[]")) {
			Arrays.stream(allotedChanel).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedChanelList.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBean.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBean.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBean.getSelectedStatus();
		if (!StringUtils.equals(allotedStatus[0], "null") && !StringUtils.equals(allotedStatus[0], "[]")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBean.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null") && !StringUtils.equals(allotedFilingType[0], "[]")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedDept = new ArrayList<>();
		String[] alloteDept = chartBean.getSelectedDept();
		if (!StringUtils.equals(alloteDept[0], "null") && !StringUtils.equals(alloteDept[0], "[]")) {
			Arrays.stream(alloteDept).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedDept.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedCategory = new ArrayList<>();
		String[] alloteCategory = chartBean.getSelectedCategory();
		if (!StringUtils.equals(alloteCategory[0], "null") && !StringUtils.equals(alloteCategory[0], "[]")) {
			Arrays.stream(alloteCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedCategory.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedSubCategory = new ArrayList<>();
		String[] alloteSubCategory = chartBean.getSelectedSubCategory();
		if (!StringUtils.equals(alloteSubCategory[0], "null") && !StringUtils.equals(alloteSubCategory[0], "[]")) {

			Arrays.stream(alloteSubCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedSubCategory.add(Long.valueOf(innerItem))));
		}

		StringBuilder sb = new StringBuilder();

		sb.append("SELECT COUNT(*) as value, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		/*if(deptConsider) {
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																			sb.append(" min(TREGL.REGULATOR_LABEL) as regLabel ,")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																			.append(" TREG.REGULATOR_ID as regId,  ");
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		}*/

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" TBENT.CATEGORY_ID_FK as categoryPk, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" TREMN.RETURN_ID_FK as returnval, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" TREMN.ENTITY_ID_FK as entity, ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TRL.RETURN_LABEL) AS returnName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" FROM TBL_RETURN_ENTITY_MAPP_NEW TREMN ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TREMN.ENTITY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  AND TEL.LANGUAGE_ID_FK=:languageId ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TREMN.RETURN_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  AND TRL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  left outer join TBL_ENTITY TBENT  ON TBENT.ENTITY_ID=TREMN.ENTITY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_CATEGORY_LABEL TBCATLBL ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TREMN.RETURN_ID_FK and TR.IS_ACTIVE=1 ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		//	.append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TREMN.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ") 
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		//.append("  LEFT OUTER JOIN TBL_REGULATOR TREG ON TREG.REGULATOR_ID=TRRM.REGULATOR_ID_FK AND TREG.IS_ACTIVE=1  ") 
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		//	.append("  LEFT OUTER JOIN TBL_REGULATOR_LABEL TREGL ON TREGL.REGULATOR_ID_FK=TREG.REGULATOR_ID AND TREGL.LANGUAGE_ID_FK=:languageId ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join   (SELECT ENTITY_ID_FK, RETURN_ID_FK FROM TBL_RETURNS_UPLOAD_DETAILS TRUD ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  where TRUD.FILING_STATUS_ID_FK  in (8,9)      ");
		if (StringUtils.isNotBlank(chartBean.getFilingStartDate()) && StringUtils.isNotBlank(chartBean.getFilingDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}
		sb.append(" and TRUD.ISACTIVE=1 ) notToInclude  on notToInclude.ENTITY_ID_FK=TREMN.ENTITY_ID_FK ").append("  and notToInclude.RETURN_ID_FK=TREMN.RETURN_ID_FK").append("  where TREMN.IS_ACTIVE=1 AND notToInclude.RETURN_ID_FK is null ");

		sb.append(" and TREMN.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TREMN.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");
		/*	if(!selectedDept.isEmpty()) {
			sb.append(" and TREG.REGULATOR_ID IN (:selectedDept)");
		}*/
		sb.append(" and TR.FREQUENCY_ID_FK =:frequency ");

		sb.append(" group by ").append(groupByClause);
		if (StringUtils.equalsIgnoreCase(chartBean.getNotSubmitted(), "true")) {
			sb.append(" union ").append(getFilingDataQueryString(chartBean));
		}

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);

		if (selectedReturn.isEmpty()) {
			ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
			returnGroupMappingRequest.setIsActive(true);
			returnGroupMappingRequest.setUserId(chartBean.getUserId());
			returnGroupMappingRequest.setLangId(chartBean.getLangId());
			returnGroupMappingRequest.setRoleId(chartBean.getRoleId());
			if (chartBean.getFrequency() != null) {
				returnGroupMappingRequest.setFrequencyId(chartBean.getFrequency());
			}
			List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
			for (ReturnGroupMappingDto item : returnList) {
				selectedReturn.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
			}

		}
		query.setParameter("selectedReturn", selectedReturn);
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setActive(true);
		entityMasterDto.setRoleId(chartBean.getRoleId());
		entityMasterDto.setUserId(chartBean.getUserId());

		entityMasterDto.setLanguageCode(chartBean.getLangCode());
		List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
		if (entityList != null && !entityList.isEmpty()) {
			if (selectedEntity.isEmpty()) {
				selectedEntity.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			}
			if (selectedCategory.isEmpty()) {
				selectedCategory.addAll(entityList.stream().map(inner -> inner.getCategory().getCategoryId()).collect(Collectors.toList()));
				Set<Long> set = new LinkedHashSet<>();
				set.addAll(selectedCategory);
				selectedCategory.clear();
				selectedCategory.addAll(set);
			}
			if (selectedSubCategory.isEmpty()) {
				selectedSubCategory.addAll(entityList.stream().map(inner -> inner.getSubCategory().getSubCategoryId()).collect(Collectors.toList()));
				Set<Long> set = new LinkedHashSet<>();
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
		query.setParameter("frequency", chartBean.getFrequency());
		if (dateFilterApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBean.getFilingStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBean.getFilingDate(), chartBean.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		List<Tuple> result = query.setParameter("languageId", chartBean.getLangId()).getResultList();
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
			level2.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
			level2.setParent("filing");
			if (unique.add(level2.getId())) {
				node.getChildren().add(level2);
			}

			ChartNode level3 = new ChartNode();
			level3.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			level3.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
			level3.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
			if (unique.add(level3.getId())) {
				node.getChildren().add(level3);
			}
			/*if(orderList.size()==3) {
				ChartNode level4 = new ChartNode();
				level4.setId(pkAliasMap.get(orderList.get(0))+(Integer) item.get(pkAliasMap.get(orderList.get(0)))+pkAliasMap.get(orderList.get(1))+(Integer) item.get(pkAliasMap.get(orderList.get(1)))+pkAliasMap.get(orderList.get(2))+(Integer) item.get(pkAliasMap.get(orderList.get(2))));
				level4.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(),(String) item.get(nameMap.get(orderList.get(2)))));
				level4.setParent(pkAliasMap.get(orderList.get(0))+(Integer) item.get(pkAliasMap.get(orderList.get(0)))+pkAliasMap.get(orderList.get(1))+(Integer) item.get(pkAliasMap.get(orderList.get(1))));
				level4.setValue((BigInteger) item.get("value"));
				if(unique.add(level4.getId())) {
					node.getChildren().add(level4);
				}
			}else {
				level3.setValue((BigInteger) item.get("value"));
			}*/

			ChartNode level4 = new ChartNode();
			level4.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
			level4.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
			level4.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			if (unique.add(level4.getId())) {
				node.getChildren().add(level4);
			}

			if (orderList.size() == 4) {
				ChartNode level5 = new ChartNode();
				level5.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))) + pkAliasMap.get(orderList.get(3)) + (Integer) item.get(pkAliasMap.get(orderList.get(3))));
				level5.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(3)))));

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

	public String getFilingDataQueryString(ChartBean chartBean) throws ParseException {
		Logger.info(" getFilingData Query String job ");
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
		String[] orders = StringUtils.isNotBlank(chartBean.getOrder()) ? chartBean.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
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
		/*List<Long> selectedChanelList = new ArrayList<>();
		String[] allotedChanel = chartBean.getSelectedChanel();
		if(!StringUtils.equals(allotedChanel[0],"null")) {
			Arrays.stream(allotedChanel).forEach(item->Arrays.stream(item.substring(1,item.length()-1).replace("\"", "").split(","))
					.forEach(innerItem->selectedChanelList.add(Long.valueOf(innerItem))));
		
		}*/

		List<Long> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBean.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null") && !StringUtils.equals(allotedReturn[0], "[]")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBean.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null") && !StringUtils.equals(allotedEntity[0], "[]")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBean.getSelectedStatus();
		if (!StringUtils.equals(allotedStatus[0], "null") && !StringUtils.equals(allotedStatus[0], "[]")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBean.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null") && !StringUtils.equals(allotedFilingType[0], "[]")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedCategory = new ArrayList<>();
		String[] alloteCategory = chartBean.getSelectedCategory();
		if (!StringUtils.equals(alloteCategory[0], "null") && !StringUtils.equals(alloteCategory[0], "[]")) {
			Arrays.stream(alloteCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedCategory.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedSubCategory = new ArrayList<>();
		String[] alloteSubCategory = chartBean.getSelectedSubCategory();
		if (!StringUtils.equals(alloteSubCategory[0], "null") && !StringUtils.equals(alloteSubCategory[0], "[]")) {

			Arrays.stream(alloteSubCategory).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedSubCategory.add(Long.valueOf(innerItem))));
		}

		/*List<Long> selectedDept = new ArrayList<>();
		String[] alloteDept = chartBean.getSelectedDept();
		if(!StringUtils.equals(alloteDept[0],"null")) {
			Arrays.stream(alloteDept).forEach(item->Arrays.stream(item.substring(1,item.length()-1).replace("\"", "").split(","))
					.forEach(innerItem->selectedDept.add(Long.valueOf(innerItem))));
		
		}*/

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) as value, ").append(" TBENT.CATEGORY_ID_FK as categoryPk, ").append(" TBENT.SUB_CATEGORY_ID_FK as subCategoryPk, ").append(" TRUD.RETURN_ID_FK as returnval, ").append(" TRUD.ENTITY_ID_FK as entity, ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TBCATLBL.CATEGORY_LABEL) AS category, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TSCATLBL.SUB_CATEGORY_LABEL) AS subCategoryName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TRL.RETURN_LABEL) AS returnName, ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" min(TEL.ENTITY_NAME_LABEL) AS entityName ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" FROM TBL_RETURNS_UPLOAD_DETAILS TRUD LEFT OUTER JOIN TBL_FILE_DETAILS TFD ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  ON TFD.ID=TRUD.FILE_DETAILS_ID_FK left outer join TBL_UPLOAD_CHANNEL TUC  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  ON TFD.UPLOAD_CHANNEL_ID_FK=TUC.UPLOAD_CHANNEL_ID AND TUC.IS_ACTIVE=1  ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRUD.ENTITY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  AND TEL.LANGUAGE_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_ENTITY TBENT ON TBENT.ENTITY_ID=TRUD.ENTITY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_CATEGORY_LABEL TBCATLBL ON TBCATLBL.CATEGORY_ID_FK = TBENT.CATEGORY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TBCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_SUB_CATEGORY_LABEL TSCATLBL ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" ON TSCATLBL.SUB_CATEGORY_ID_FK = TBENT.SUB_CATEGORY_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" AND  TSCATLBL.LANG_ID_FK=:languageId ")

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRUD.RETURN_ID_FK and TR.IS_ACTIVE=1 ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRUD.RETURN_ID_FK ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  AND TRL.LANG_ID_FK=:languageId ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" inner join  (Select Max(mtr.UPLOAD_ID) as UPLOAD_ID from TBL_RETURNS_UPLOAD_DETAILS mtr ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  group by mtr.RETURN_ID_FK ,mtr.ENTITY_ID_FK,mtr.END_DATE,mtr.FIN_YR_FREQUENCY_ID_FK) maxTable ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append(" on  maxTable.UPLOAD_ID = TRUD.UPLOAD_ID ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		//.append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRUD.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ") 
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		//.append("  LEFT OUTER JOIN TBL_REGULATOR TREG ON TREG.REGULATOR_ID=TRRM.REGULATOR_ID_FK AND TREG.IS_ACTIVE=1  ") 
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		//.append("  LEFT OUTER JOIN TBL_REGULATOR_LABEL TREGL ON TREGL.REGULATOR_ID_FK=TREG.REGULATOR_ID AND TREGL.LANGUAGE_ID_FK=:languageId ")
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		.append("  where  TFD.UPLOAD_CHANNEL_ID_FK is not null ");
		/*if(!selectedChanelList.isEmpty()) {
			sb.append(" and TUC.UPLOAD_CHANNEL_ID IN (:selectedChanelList)");
		}*/
		sb.append(" and TRUD.FILING_STATUS_ID_FK  in ( 9 )");

		sb.append(" and TRUD.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TRUD.ENTITY_ID_FK IN (:selectedEntity)");
		sb.append(" and TBCATLBL.CATEGORY_ID_FK IN (:selectedCategory)");
		sb.append(" and TSCATLBL.SUB_CATEGORY_ID_FK IN (:selectedSubCategory)");
		if (chartBean.getFrequency() != null) {
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
		if (StringUtils.isNotBlank(chartBean.getFilingStartDate()) && StringUtils.isNotBlank(chartBean.getFilingDate())) {
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}

		sb.append(" group by ").append(groupByClause);

		return sb.toString();
	}

	public ChartNode getPendingFilingData_OLD(ChartBean chartBean, String jobProcessId) throws ParseException {
		Logger.info(" getPendingFilingData job {}", jobProcessId);
		Map<String, String> groupMap = new HashMap<>();
		groupMap.put("2", "TREG.REGULATOR_ID");
		groupMap.put("3", "TREMN.RETURN_ID_FK");
		groupMap.put("4", "TREMN.ENTITY_ID_FK");

		Map<String, String> nameMap = new HashMap<>();
		nameMap.put("2", "regLabel");
		nameMap.put("3", "returnName");
		nameMap.put("4", "entityName");

		Map<String, String> pkAliasMap = new HashMap<>();
		pkAliasMap.put("2", "regId");
		pkAliasMap.put("3", "returnval");
		pkAliasMap.put("4", "entity");
		String[] orders = StringUtils.isNotBlank(chartBean.getOrder()) ? chartBean.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
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
		String[] allotedChanel = chartBean.getSelectedChanel();
		if (!StringUtils.equals(allotedChanel[0], "null")) {
			Arrays.stream(allotedChanel).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedChanelList.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBean.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBean.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBean.getSelectedStatus();
		if (!StringUtils.equals(allotedStatus[0], "null")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBean.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedDept = new ArrayList<>();
		String[] alloteDept = chartBean.getSelectedDept();
		if (!StringUtils.equals(alloteDept[0], "null")) {
			Arrays.stream(alloteDept).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedDept.add(Long.valueOf(innerItem))));

		}

		StringBuilder sb = new StringBuilder();

		sb.append("SELECT COUNT(*) as value, ");
		if (deptConsider) {
			sb.append(" min(TREGL.REGULATOR_LABEL) as regLabel ,").append(" TREG.REGULATOR_ID as regId,  ");
		}
		sb.append(" TREMN.RETURN_ID_FK as returnval, ").append(" TREMN.ENTITY_ID_FK as entity, ").append(" min(TEL.ENTITY_NAME_LABEL) AS entityName, ").append(" min(TRL.RETURN_LABEL) AS returnName ").append(" FROM TBL_RETURN_ENTITY_MAPP_NEW TREMN ").append("  left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TREMN.ENTITY_ID_FK ").append("  AND TEL.LANGUAGE_ID_FK=:languageId ").append("  left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TREMN.RETURN_ID_FK ").append("  AND TRL.LANG_ID_FK=:languageId ").append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TREMN.RETURN_ID_FK and TR.IS_ACTIVE=1 ").append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TREMN.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ").append("  LEFT OUTER JOIN TBL_REGULATOR TREG ON TREG.REGULATOR_ID=TRRM.REGULATOR_ID_FK AND TREG.IS_ACTIVE=1  ").append("  LEFT OUTER JOIN TBL_REGULATOR_LABEL TREGL ON TREGL.REGULATOR_ID_FK=TREG.REGULATOR_ID AND TREGL.LANGUAGE_ID_FK=:languageId ").append("  where TREMN.IS_ACTIVE=1 AND TREMN.RETURN_ID_FK not in ").append(" (SELECT distinct RETURN_ID_FK FROM TBL_RETURNS_UPLOAD_DETAILS TRUD where TRUD.FILING_STATUS_ID_FK =").append(GeneralConstants.APPROVED_BY_RBI.getConstantIntVal());
		if (StringUtils.isNotBlank(chartBean.getFilingStartDate()) && StringUtils.isNotBlank(chartBean.getFilingDate())) {
			dateFilterApplicable = true;
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}
		sb.append(" and TRUD.ENTITY_ID_FK in (:selectedEntity) ");
		sb.append(" and TRUD.ISACTIVE=1 )   and TREMN.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TREMN.ENTITY_ID_FK IN (:selectedEntity)");
		if (!selectedDept.isEmpty()) {
			sb.append(" and TREG.REGULATOR_ID IN (:selectedDept)");
		}
		sb.append(" and TR.FREQUENCY_ID_FK =:frequency ");

		sb.append(" group by ").append(groupByClause);
		if (StringUtils.equalsIgnoreCase(chartBean.getNotSubmitted(), "true")) {
			sb.append(" union ").append(getFilingDataQueryString(chartBean));
		}

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);

		if (selectedReturn.isEmpty()) {
			ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
			returnGroupMappingRequest.setIsActive(true);
			returnGroupMappingRequest.setUserId(chartBean.getUserId());
			returnGroupMappingRequest.setLangId(chartBean.getLangId());
			returnGroupMappingRequest.setRoleId(chartBean.getRoleId());
			if (chartBean.getFrequency() != null) {
				returnGroupMappingRequest.setFrequencyId(chartBean.getFrequency());
			}
			List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
			for (ReturnGroupMappingDto item : returnList) {
				selectedReturn.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
			}

		}
		query.setParameter("selectedReturn", selectedReturn);
		if (selectedEntity.isEmpty()) {
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setActive(true);
			entityMasterDto.setRoleId(chartBean.getRoleId());
			entityMasterDto.setUserId(chartBean.getUserId());

			entityMasterDto.setLanguageCode(chartBean.getLangCode());
			List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
			if (entityList != null && !entityList.isEmpty()) {
				selectedEntity.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			}
		}
		query.setParameter("selectedEntity", selectedEntity);
		if (!selectedStatus.isEmpty()) {
			query.setParameter("selectedStatus", selectedStatus);
		}
		if (!selectedDept.isEmpty()) {
			query.setParameter("selectedDept", selectedDept);
		}
		query.setParameter("frequency", chartBean.getFrequency());
		if (dateFilterApplicable) {
			Date startDate = DateManip.convertStringToDate(chartBean.getFilingStartDate(), chartBean.getDateFormat());
			Date endDate = DateManip.convertStringToDate(chartBean.getFilingDate(), chartBean.getDateFormat());
			query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		}
		List<Tuple> result = query.setParameter("languageId", chartBean.getLangId()).getResultList();
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
			level2.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(0)))));
			level2.setParent("filing");
			if (unique.add(level2.getId())) {
				node.getChildren().add(level2);
			}

			ChartNode level3 = new ChartNode();
			level3.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
			level3.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(1)))));
			level3.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))));
			if (unique.add(level3.getId())) {
				node.getChildren().add(level3);
			}
			if (orderList.size() == 3) {
				ChartNode level4 = new ChartNode();
				level4.setId(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))) + pkAliasMap.get(orderList.get(2)) + (Integer) item.get(pkAliasMap.get(orderList.get(2))));
				level4.setName(ObjectCache.getLabelKeyValue(chartBean.getLangCode(), (String) item.get(nameMap.get(orderList.get(2)))));
				level4.setParent(pkAliasMap.get(orderList.get(0)) + (Integer) item.get(pkAliasMap.get(orderList.get(0))) + pkAliasMap.get(orderList.get(1)) + (Integer) item.get(pkAliasMap.get(orderList.get(1))));
				level4.setValue((BigInteger) item.get("value"));
				if (unique.add(level4.getId())) {
					node.getChildren().add(level4);
				}
			} else {
				level3.setValue((BigInteger) item.get("value"));
			}

		}

		return node;
	}

	public String getFilingDataQueryString_OLD(ChartBean chartBean) throws ParseException {
		Logger.info(" getFilingData Query String job ");
		Map<String, String> groupMap = new HashMap<>();
		groupMap.put("1", "TFD.UPLOAD_CHANNEL_ID_FK");
		groupMap.put("2", "TREG.REGULATOR_ID");
		groupMap.put("3", "TRUD.RETURN_ID_FK");
		groupMap.put("4", "TRUD.ENTITY_ID_FK");

		Map<String, String> nameMap = new HashMap<>();
		nameMap.put("1", "chanel");
		nameMap.put("2", "regLabel");
		nameMap.put("3", "returnName");
		nameMap.put("4", "entityName");

		Map<String, String> pkAliasMap = new HashMap<>();
		pkAliasMap.put("1", "chanelPk");
		pkAliasMap.put("2", "regId");
		pkAliasMap.put("3", "returnval");
		pkAliasMap.put("4", "entity");
		String[] orders = StringUtils.isNotBlank(chartBean.getOrder()) ? chartBean.getOrder().split(",") : "1,2,3,4".split(",");
		List<String> orderList = Arrays.asList(orders);
		Boolean dateFilterApplicable = false;
		boolean deptConsider = false;
		UserMaster userMaster = userMasterService.getDataById(chartBean.getUserId());
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
		List<Long> selectedChanelList = new ArrayList<>();
		String[] allotedChanel = chartBean.getSelectedChanel();
		if (!StringUtils.equals(allotedChanel[0], "null")) {
			Arrays.stream(allotedChanel).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedChanelList.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedReturn = new ArrayList<>();
		String[] allotedReturn = chartBean.getSelectedReturn();
		if (!StringUtils.equals(allotedReturn[0], "null")) {
			Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedReturn.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedEntity = new ArrayList<>();
		String[] allotedEntity = chartBean.getSelectedEntity();
		if (!StringUtils.equals(allotedEntity[0], "null")) {
			Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedEntity.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedStatus = new ArrayList<>();
		String[] allotedStatus = chartBean.getSelectedStatus();
		if (!StringUtils.equals(allotedStatus[0], "null")) {
			Arrays.stream(allotedStatus).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedStatus.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedFilingType = new ArrayList<>();
		String[] allotedFilingType = chartBean.getSelectedTypeOfFiling();
		if (!StringUtils.equals(allotedFilingType[0], "null")) {
			Arrays.stream(allotedFilingType).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedFilingType.add(Long.valueOf(innerItem))));

		}

		List<Long> selectedDept = new ArrayList<>();
		String[] alloteDept = chartBean.getSelectedDept();
		if (!StringUtils.equals(alloteDept[0], "null")) {
			Arrays.stream(alloteDept).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> selectedDept.add(Long.valueOf(innerItem))));

		}

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) as value, ");
		if (deptConsider) {
			sb.append(" min(TREGL.REGULATOR_LABEL) as regLabel ,").append(" TREG.REGULATOR_ID as regId,  ");
		}
		sb.append(" TRUD.RETURN_ID_FK as returnval, ").append(" TRUD.ENTITY_ID_FK as entity, ").append(" min(TEL.ENTITY_NAME_LABEL) AS entityName, ").append(" min(TRL.RETURN_LABEL) AS returnName ").append(" FROM TBL_RETURNS_UPLOAD_DETAILS TRUD LEFT OUTER JOIN TBL_FILE_DETAILS TFD ").append("  ON TFD.ID=TRUD.FILE_DETAILS_ID_FK left outer join TBL_UPLOAD_CHANNEL TUC  ").append("  ON TFD.UPLOAD_CHANNEL_ID_FK=TUC.UPLOAD_CHANNEL_ID AND TUC.IS_ACTIVE=1  ").append("  left outer join TBL_ENTITY_LABEL TEL ON TEL.ENTITY_ID_FK=TRUD.ENTITY_ID_FK ").append("  AND TEL.LANGUAGE_ID_FK=:languageId ").append(" left outer join TBL_RETURN TR on TR.RETURN_ID=TRUD.RETURN_ID_FK and TR.IS_ACTIVE=1 ").append("  left outer join TBL_RETURN_LABEL TRL ON TRL.RETURN_ID_FK =  TRUD.RETURN_ID_FK ").append("  AND TRL.LANG_ID_FK=:languageId ").append(" left outer join TBL_RETURN_REGULATOR_MAPPING TRRM ON TRUD.RETURN_ID_FK=TRRM.RETURN_ID_FK AND TRRM.IS_ACTIVE=1  ").append("  LEFT OUTER JOIN TBL_REGULATOR TREG ON TREG.REGULATOR_ID=TRRM.REGULATOR_ID_FK AND TREG.IS_ACTIVE=1  ").append("  LEFT OUTER JOIN TBL_REGULATOR_LABEL TREGL ON TREGL.REGULATOR_ID_FK=TREG.REGULATOR_ID AND TREGL.LANGUAGE_ID_FK=:languageId ").append("  where  TFD.UPLOAD_CHANNEL_ID_FK is not null ");
		if (!selectedChanelList.isEmpty()) {
			sb.append(" and TUC.UPLOAD_CHANNEL_ID IN (:selectedChanelList)");
		}
		sb.append(" and TRUD.FILING_STATUS_ID_FK!=" + GeneralConstants.APPROVED_BY_RBI.getConstantIntVal());

		sb.append(" and TRUD.RETURN_ID_FK IN (:selectedReturn)");
		sb.append(" and TRUD.ENTITY_ID_FK IN (:selectedEntity)");
		if (chartBean.getFrequency() != null) {
			sb.append(" and TR.FREQUENCY_ID_FK =:frequency ");
		}
		if (!selectedStatus.isEmpty()) {
			sb.append(" and TRUD.FILING_STATUS_ID_FK IN (:selectedStatus)");
		}
		if (!selectedDept.isEmpty()) {
			sb.append(" and TREG.REGULATOR_ID IN (:selectedDept)");
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
		if (StringUtils.isNotBlank(chartBean.getFilingStartDate()) && StringUtils.isNotBlank(chartBean.getFilingDate())) {
			sb.append(" and TRUD.START_DATE =:startDate And TRUD.END_DATE=:endDate  ");
		}

		sb.append(" group by ").append(groupByClause);

		return sb.toString();
	}

}
