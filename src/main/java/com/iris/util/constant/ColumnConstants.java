package com.iris.util.constant;

public enum ColumnConstants {

	ENTITYID("entityId"),
	ENTITY_CODE("entityCode"),
	IFSC_CODE("ifscCode"),
	RETURNID("returnId"),
	RETURN_CODE("returnCode"),
	FIN_YEAR_FREQ_DESC_ID("finYearFreqDescId"),
	COMPONENTTYPE("componentType"),
	STARTDATE("startDate"),
	ENDDATE("endDate"),
	STARTDT("startDt"),
	ENDDT("endDt"),
	EMAIL_ID("emailId"),
	METHODTYPE("methodType"),
	USER_ID("userId"),
	USER_NAME("userName"),
	STATUS("status"),
	UPLOAD_CHANNEL("uploadChannel"),
	RETURN_USER_ID("returnUserId"),
	ROLEID("roleId"),
	CATEGORY_ID("categoryId"),
	CATEGORY("category"),
	SUB_CATEGORY("subCategory"),
	SUB_CATEGORY_ID("subCategoryId"),
	FILE_DETAILS("fileDetails"),
	UNMAPPED_RETURN("unMappedReturn"),
	FILLING_STATUS("fillingStatus"),
	FILLING_STATUS_ID("fillingStatusId"),
	CHANGED_FILLING_STATUS("changeFillingStatus"),
	CHANGED_FILLING_STATUS_ID("changeFillingStatusId"),
	FILE_TYPE("fileType"),
	TOTAL_RECORD_COUNT("totalRecordCount"),
	LABEL_KEY("labelKey"),
	LANGUAGE_CODE("languageCode"),
	IS_ACTIVE("isActive"),
	LANG_CODE("langCode"),
	LANG_ID("langId"),
	ENT_CODE_LIST("entCodeList"),
	USER_ROLE_MASTER_ID("userRoleMasterId"),
	RETURN_GROUP_ID("returnGroupId"),
	CAT_CODE("categoryCode"),
	CAT_CODE_LIST("categoryCodeList"),
	SUB_CAT_CODE("subCategoryCode"),
	ENTITY_ID_LIST("entityIdList"),
	ENT_CODE("entCode"),
	RETURN_ID_ARRAY("returnIdArray"),
	LIKE_STRING("likeString"),
	RETURN_PROPERTY_VAL_ID("returnPropertyValId"),
	WEB_CHANNEL("webChannel"),
	EMAIL_CHANNEL("emailChannel"),
	API_CHANNEL("apiChannel"),
	IS_CHANNEL_TO_CONSIDER("isChanelToConsider"),
	STS_CAHNNEL("stsChannel"),
	ACTIVITY_IDS("activityIds"),
	SUB_CAT_CODE_LIST("subCategoryCodeList"),
	REGULATORID("regulatorId"),
	FROM_YEAR("fromYear"),
	TO_YEAR("toYear"),
	RETURN_ID_LIST("returnIdList"),
	CHANNEL_ID("channelId"),
	SESSION_DB_DATE_FORMAT("SessionDbDateFormat"),
	FREQUENCY_ID("frequencyId"),
	SUB_RET_ID_LIST("SubRetIdList"),
	RETURN_TYPE("RETURN_TYPE"),
	VALID_FROM_DATE("VALID_FROM_DATE"),
	IS_ACTIVE_FOR_WEB_FORM_BASED_FILING("isActiveForWebFormBasedFiling"),
	IS_ACTIVE_FOR_FILE_BASED_FILING("isActiveForFileBasedFiling"),
	PAN_NUMBER("panNumber"),
	COMPANY_TYPE("companyType"),
	RETURN_TYPE_SECTION("RETURN_TYPE_SECTION"),
	RET_TEMP_VERSION("ret_Temp_version"),
	PAGEABLE("PAGEABLE"),
	FETCHSIZE("FETCHSIZE"),
	FORM_PAGE_NO("formPageNo"),
	WORKFLOW_MASTER_IDS("workflowMasterIds"),
	ENTITY_BEAN("entityBeanId"),
	SUB_FORM_PAGE_NO("subFormPageNo"),
	COMAPNY_OTHER_TYPE_ID("companyOtherTypeId"),
	COMAPNY_TYPE_ID("companyTypeId"),
	ADMIN_STATUS("adminStatus"),
	CL_CODE("clCode"),
	CL_VERSION("clVersion"),
	STATE_CODE("stateCode"), //added by pradnya
	CITY_ID("cityId"), //added by pradnya
	PARENT_DIMENSION_ID("parentDimensionId"),
	ENTITY_NAME("entityName"),
	START_FORM_PAGE_NO("startFormPageNo"),
	END_FORM_PAGE_NO("endFormPageNo"),
	MIS_REPORT_TYPE("misReportType"),
	END_DATE_LIST("endDateList"),
	STATE_NAME("stateName"),
	CITY_NAME("cityName"),
	REVISION_STATUS_ID_LIST("revisionStatusIdList"),
	SUB_CATEGORY_ID_LIST("subCategoryIDList"),
	CATEGORY_ID_LIST("categoryIDList"),
	UNLOCK_STATUS_ID_LIST("unlockStatusIdList"),
	CATEGORY_NAME("categoryName"),
	RECORD_TO_BE_PROCESSED_COUNT("RECORD_TO_BE_PROCESSED_COUNT"),
	USER_MASTER_OBJ("userMasterObj"),
	MENU_ID("menuId"),
	SUB_CATEGORY_CODE_LIST("subCategoryCodeList"),
	CATEGORY_CODE_LIST("categoryCodeList"),
	ENTITY_CODE_LIST("ENTITY_CODE_LIST"),
	BRANCH_WORKING_CODE("branchWorkingCode"),
	COUNTRY_CODE("countryCode"),
	CENSUS_STATE_CODE("censusStateCode"),
	CENSUS_DISTRICT_CODE("censusDistrictCode"),
	CENSUS_SUBDISTRICT_CODE("censusSubdistrictCode"),
	CENSUS_VILLAGE_CODE("censusVillageCode"),
	RETURN_CODE_LIST("returnCodeList"),
	DEPENDENT("DEPENDENT"),
	CALCULATION("CALCULATION"),
	CHANGE_PAN_VERI_STATUS("changePanVeriStatus"),
	PASS_PAN_VERI_STATUS("passPanVeriStatus"),
	MODULE_NAME("moduleName"),
	SUB_TASK_STATUS("subTaskStatus"),
	SCHEDULER_LAST_RUNTIME("schedulerLastRunTime"),
	STATUS_LIST("statusList"),
	FRAUD_CODE_PARTIAL_FORMAT("fraudCodePartialFormat");

	String columnName;

	private ColumnConstants(String columnName) {
		this.columnName = columnName;
	}

	public String getConstantVal() {
		return columnName;
	}

}