/**
 * 
 */
package com.iris.util.constant;

/**
 * @author sajadhav
 *
 */
public enum ErrorConstants {

	ERROR_MSG_CONTROLLER("Error in Controller. "),
	ERROR_MSG_SERVICE("Error in Service. "),
	ERROR_NO_APPID_SET_IN_HEADER("No app Id found in header request"),
	ERROR_INVALID_APPID_SET_IN_HEADER("Invalid app id in header request"),
	ERROR_NO_JWTTOKEN_SET_IN_HEADER("No JWT token found in header request"),
	ERROR_JWTTOKEN_EXPIRED("JWT token has been expired"),
	ERROR_NO_APPID_SET_IN_APP("No app Id found in App resources"),
	CURRENCY_NOT_FOUND("CURRENCY_NOT_FOUND"),
	CURRENCY_CONVERSION_RATE_NOT_FOUND("CURRENCY_CONVERSION_RATE_NOT_FOUND"),
	DEFAULT_ERROR("DEFAULT_ERROR"),
	E001("E001"),
	E002("E002"),
	E003("E003"),
	E114("E114"),
	DEFAULT_MSG("DEFAULT_ERROR"),
	E004("E004"),
	E005("E005"),
	EMPTY_RECORD("EMPTY_RECORD"),
	RECORD_SAVED_SUCCESSFULLY("RECORD_SAVED_SUCCESSFULLY"),
	PARTAIL_RECORD_SAVED("PARTAIL_RECORD_SAVED"),
	REVISION_REQUEST_NOT_FOUND("error.revisionRequest.required"),
	REVISION_REQUEST_CLOSED("error.revisionRequest.closed"),
	REVISION_REQUEST_EXPIRED("error.revisionRequest.expired"),
	UNLOCK_REQUEST_NOT_FOUND("error.unlockRequest.required"),
	UNLOCK_REQUEST_CLOSED("error.unlockRequest.closed"),
	UNLOCK_REQUEST_EXPIRED("error.unlockRequest.expired"),
	ENTITY_USER_NOT_FOUND("error.user.notFound"),
	USER_NOT_MAPPED_TO_ENTITY("error.userNotMappedToEntity"),
	RETURN_NOT_MAPPED_TO_USER_ROLE("error.returnNotMappedToUserRole"),
	ENTITY_CODE_NOT_FOUND("error.entityCodeNotFound"),
	INALID_EXCEL_FILE_FORMAT("support.file.format.xlsx.xls.BIFF8.format"),
	JSON_SHEET_COUNT_EXCEL_SHEET_COUNT_MISMATCHED("json.sheet.count.mismatched.with.excelsheet.count"),
	FILE_NOT_FOUND("fileNotFound"),
	DATA_NOT_FOUND("error.dataNotFound"),
	SCHEDULER_INFO_NOT_PRESENT("SCHEDULER_INFO_NOT_PRESENT"),
	FILLING_STATUS_NOT_FOUND("Filling status not found"),
	REQUEST_TRANSACTION_ID_NOT_FOUND("Request transaction id not found"),
	RECORD_COUNT_SHOULD_BE_GREATER_THAN_0("Record count should be greater than 0"),
	RECORD_COUNT_NOT_FOUND("RECORD_COUNT_NOT_FOUND"),
	FILE_TYPE_LIST_NOT_FOUND("FIle type list not found"),
	LANGUAG_CODE_NOT_FOUND("Language code not found"),
	CATEGORY_CODE_NOT_FOUND("Category code not found"),
	SUB_CATEGORY_CODE_NOT_FOUND("Sub Category code not found"),
	CATEGORY_ID_NOT_FOUND("Category id not found"),
	SUB_CATEGORY_ID_NOT_FOUND("Sub Category id not found"),
	USR_ID_NOT_FOUND("User id not found"),
	USER_ROLE_ID_NOT_FOUND("error.userRole.notFound"),
	ACTIVE_FLAG_NOT_FOUND("Active flag not found"),
	LANG_CODE_NOT_FOUND("Language code not found"),
	USER_NOT_FOUND("User not found"),
	REQUEST_OBJ_NOT_PROPER("REQUEST_OBJ_NOT_PROPER"),
	RETURN_ID_NOT_FOUND("error.returnIdNotFound", "Return Id not found"),
	RETURN_ID_OR_DEACTIVATE_RETURN_ID_NOT_FOUND("error.returnIdOrDeactivateReturnIdNotFound", "Return Id or deactivate return id combination not found"),
	ENTITY_ID_NOT_FOUND("error.entityIdNotFound", "Entity Id not found"),
	DATE_FORMAT_NOT_FOUND("error.dateformatNotFound", "Date format not found"),
	UPDATE_RETURN_WITH_RET_GRP_ERROR("error.return.returngroup"), // E0771
	ERROR_UPDATING_BUSINESS_VALIDATION_STATUS("error.updating.businessValidation.success.status"),
	E006("E006"),
	E006_MSG("Request Parameter Not Valid"),
	E007("E007"),
	E007_MSG("Authentication Failed"),
	E008("E008"),
	E008_MSG("Invalid Entity Code"),
	E009("E009"),
	E009_MSG("Invalid Return Code"),
	E010("E010"),
	E010_MSG("Invalid File Format"),
	E011("E011"),
	E011_MSG("Supporting file Invalid"),
	E012("E012"),
	E012_MSG("Error while inserting data"),
	E013("E013"),
	E013_MSG("Date Format is not valid"),
	E014("E014"),
	E014_MSG("Frequency not valid"),
	INTERNAL_SERVER_ERROR("E016", "TECHNICAL_ERROR"),
	ACTION_NOT_FOUND("E017", "ACTION_NOT_FOUND_FOR_STEP"),
	E015("E015"),
	E015_MSG("Invalid Department User"),
	E016("E016"),
	E016_MSG("Return List Not found"),
	E017("E017"),
	E017_MSG("No Pending Request"),
	E018("E018"),
	E018_MSG("Empty Bean Received"),
	E027("E027", "error.calculating.filingWindow"),
	E028("E028", "Job processing id can't be blank"),
	E029("E029", "1 or 2 is the only values accepted in channel parameter"),
	E030("E030", "Version Id can't be blank"),
	E031("E031", "ErrorCodeLabel request body can't be blank"),
	E032("E032", "ErrorCode Id's can't be blank");

	private String errorMessage;

	private String errorCode;

	private ErrorConstants(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	private ErrorConstants(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getConstantVal() {
		return errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
