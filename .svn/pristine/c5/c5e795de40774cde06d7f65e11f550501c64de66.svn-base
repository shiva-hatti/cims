package com.iris.sdmx.codelist.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.repository.UserRoleRepo;
import com.iris.sdmx.codelist.entity.CodeListMasterMod;
import com.iris.sdmx.codelist.repo.CodeListMasterModRepo;
import com.iris.sdmx.status.repo.AdminStatusRepo;
import com.iris.sdmx.userMangement.UserManagementValidator;
import com.iris.sdmx.userMangement.bean.ApprovalInputBean;
import com.iris.util.UtilMaster;
import com.iris.util.constant.ErrorCode;

/**
 * @author vjadhav
 *
 */

@Component
public class CodeListApprovalValidator {

	private static final Logger LOGGER = LogManager.getLogger(CodeListApprovalValidator.class);

	@Autowired
	private UserManagementValidator userManagementValidator;

	@Autowired
	private AdminStatusRepo adminStatusRepo;

	@Autowired
	private CodeListMasterModRepo codeListMasterModRepo;

	@Autowired
	private UserRoleRepo userRoleRepo;

	private static final Object lock1 = new Object();

	public void validateFetchApprovalRequest(ApprovalInputBean inputBean, ApprovalInputBean validInputBean, String jobProcessId) throws ApplicationException {

		LOGGER.info("START - Validation request for fetch CodeList approval requests with Job Processing ID : " + jobProcessId);

		userManagementValidator.validateApprovalRequest(inputBean, validInputBean, jobProcessId);

		checkIfDeptAdmin(inputBean);

		validateAdminStatusId(inputBean.getAdminStatusId(), validInputBean);

		validateIsMainDept(validInputBean.getIsMainDept());

		LOGGER.info("END - Validation request for fetch CodeList approval requests with Job Processing ID : " + jobProcessId);

	}

	public void validateApproveRejectCodeListRequest(ApprovalInputBean inputBean, ApprovalInputBean validInputBean, String jobProcessId) throws ApplicationException {

		LOGGER.info("START - Validation request for approve/reject Code list requests with Job Processing ID : " + jobProcessId);

		userManagementValidator.validateApproveRejectRequest(inputBean, validInputBean, jobProcessId);

		checkIfDeptAdmin(inputBean);

		validateAdminStatusId(inputBean.getAdminStatusId(), validInputBean);

		validateIsMainDept(validInputBean.getIsMainDept());

		validateclModId(inputBean.getModTablePkId(), inputBean, validInputBean);

		validateComment(inputBean.getComments(), inputBean.getAdminStatusId(), validInputBean);

		syncValidateMethod(validInputBean.getModTablePkId());

		validateUserIdForSelfApproval(inputBean.getUserId(), inputBean.getModTablePkId());

		LOGGER.info("END - Validation request for approve/reject Code list requests with Job Processing ID : " + jobProcessId);

	}

	private void validateAdminStatusId(Long adminStatusId, ApprovalInputBean validInputBean) throws ApplicationException {
		if (adminStatusId == null) {
			throw new ApplicationException(ErrorCode.E1557.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1557.toString()));
		} else if (adminStatusRepo.findByAdminStatusId(adminStatusId) == null) {
			throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
		} else {
			validInputBean.setAdminStatusId(adminStatusId);
		}

	}

	private void validateIsMainDept(Boolean isMainDept) throws ApplicationException {
		if (!isMainDept) {
			throw new ApplicationException(ErrorCode.E0613.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0613.toString()));
		}

	}

	private void validateclModId(Long clModId, ApprovalInputBean inputBean, ApprovalInputBean validInputBean) throws ApplicationException {

		if (clModId == null) {
			throw new ApplicationException(ErrorCode.E1574.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1574.toString()));
		}

		CodeListMasterMod codeListMasterMod = codeListMasterModRepo.findByClMasterModId(clModId);

		if (codeListMasterMod == null) {
			throw new ApplicationException(ErrorCode.E1559.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1559.toString()));
		} else if (inputBean.getAdminStatusId() == 1 || inputBean.getAdminStatusId() == 2) {
			throw new ApplicationException(ErrorCode.E1561.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1561.toString()));
		} else {
			//validInputBean.setClMasterModId(clModId);
			validInputBean.setModTablePkId(clModId);
			validInputBean.setActionId(Long.valueOf(codeListMasterMod.getActionId()));
			//validInputBean.setClIdFk(codeListMasterMod.getCodeListMaster().getClId());
			validInputBean.setMasterTablePkId(codeListMasterMod.getCodeListMaster().getClId());
		}

	}

	private void validateComment(String comment, Long adminStatusid, ApprovalInputBean validInputBean) throws ApplicationException {
		if (adminStatusid == 3 && UtilMaster.isEmpty(comment)) {
			throw new ApplicationException(ErrorCode.E1562.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1562.toString()));
		} else {
			validInputBean.setComments(comment);
		}
	}

	private void syncValidateMethod(Long clModId) throws ApplicationException {
		int adminStatusId;
		synchronized (lock1) {

			adminStatusId = codeListMasterModRepo.findByClMasterModId(clModId).getAdminStatusId();

			if (adminStatusId != 1) {
				throw new ApplicationException(ErrorCode.E1564.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1564.toString()));
			}

		}
	}

	private void validateUserIdForSelfApproval(Long userId, Long clMasterModId) throws ApplicationException {
		CodeListMasterMod codeListMasterMod = codeListMasterModRepo.findByClMasterModId(clMasterModId);
		Long createdBy = codeListMasterMod.getCreatedBy().getUserId();
		if (userId.equals(createdBy)) {
			throw new ApplicationException(ErrorCode.E0203.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0203.toString()));
		}
	}

	private void checkIfDeptAdmin(ApprovalInputBean inputBean) throws ApplicationException {
		if (!userRoleRepo.findByUserRoleIdAndIsActiveTrue(inputBean.getRoleId()).getDeptAdmin().equalsIgnoreCase("1")) {
			throw new ApplicationException(ErrorCode.E0613.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0613.toString()));
		}
	}
}
