package com.iris.sdmx.userMangement;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.repository.RegulatorRepo;
import com.iris.repository.UserRoleMasterRepo;
import com.iris.repository.UserRoleRepo;
import com.iris.sdmx.userMangement.bean.ApprovalInputBean;
import com.iris.service.impl.UserMasterService;
import com.iris.util.constant.ErrorCode;

/**
 * @author vjadhav
 *
 */

@Component
public class UserManagementValidator {

	private static final Logger LOGGER = LogManager.getLogger(UserManagementValidator.class);

	@Autowired
	private UserMasterService userMasterService;

	@Autowired
	private UserRoleMasterRepo userRoleMasterRepo;

	@Autowired
	private UserRoleRepo userRoleRepo;

	@Autowired
	private RegulatorRepo regulatorRepo;

	public void validateApprovalRequest(ApprovalInputBean inputBean, ApprovalInputBean validInputBean, String jobProcessId) throws ApplicationException {

		Long inputDeptId = null;
		Long deptId = null;

		if (inputBean.getUserId() == null) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		}

		UserMaster userMaster = userMasterService.getDataById(inputBean.getUserId());

		if (userMaster == null) {
			throw new ApplicationException(ErrorCode.E0638.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString()));
		} else {
			validInputBean.setUserId(inputBean.getUserId());
		}

		if (userMaster.getRoleType().getRoleTypeId() != 1) {
			throw new ApplicationException(ErrorCode.E0613.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0613.toString()));
		}

		if (inputBean.getRoleId() == null) {
			throw new ApplicationException(ErrorCode.E1555.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1555.toString()));
		}

		boolean invalidRoleId = true;
		List<UserRoleMaster> roleList = userRoleMasterRepo.findByUserMasterUserId(inputBean.getUserId());
		for (UserRoleMaster roleObj : roleList) {
			if (roleObj.getUserRole().getUserRoleId().longValue() == inputBean.getRoleId().longValue()) {
				invalidRoleId = false;
				break;
			}

		}
		if (invalidRoleId) {
			throw new ApplicationException(ErrorCode.E0481.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString()));
		} else {
			validInputBean.setRoleId(inputBean.getRoleId());
			validInputBean.setIsDeptAdmin(Boolean.TRUE);
		}

		deptId = userMaster.getDepartmentIdFk().getRegulatorId();

		if (StringUtils.isEmpty(inputBean.getDeptCode())) {
			throw new ApplicationException(ErrorCode.E1556.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1556.toString()));
		} else if (deptId != null) {
			inputDeptId = regulatorRepo.findByRegulatorCode(inputBean.getDeptCode(), true).getRegulatorId();
			if (deptId.longValue() != inputDeptId.longValue()) {
				throw new ApplicationException(ErrorCode.E1565.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1565.toString()));
			} else {
				validInputBean.setDeptCode(inputBean.getDeptCode());
				validInputBean.setDeptId(inputDeptId);
				validInputBean.setIsMainDept(regulatorRepo.findByRegulatorCode(inputBean.getDeptCode(), true).getIsMaster());
			}
		}

	}

	public void validateApproveRejectRequest(ApprovalInputBean inputBean, ApprovalInputBean validInputBean, String jobProcessId) throws ApplicationException {

		Long inputDeptId = null;
		Long deptId = null;
		if (inputBean.getUserId() == null) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		}

		UserMaster userMaster = userMasterService.getDataById(inputBean.getUserId());

		if (userMaster == null) {
			throw new ApplicationException(ErrorCode.E0638.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString()));
		} else {
			validInputBean.setUserId(inputBean.getUserId());
		}

		String deptCode = null;
		if (userMaster.getDepartmentIdFk() != null) {
			deptCode = userMaster.getDepartmentIdFk().getRegulatorCode();
		}

		if (userMaster.getRoleType().getRoleTypeId() != 1) {
			throw new ApplicationException(ErrorCode.E0613.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0613.toString()));
		}

		if (inputBean.getRoleId() == null) {
			throw new ApplicationException(ErrorCode.E1555.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1555.toString()));
		}

		boolean invalidRoleId = true;
		List<UserRoleMaster> roleList = userRoleMasterRepo.findByUserMasterUserId(inputBean.getUserId());
		for (UserRoleMaster roleObj : roleList) {
			if (roleObj.getUserRole().getUserRoleId().longValue() == inputBean.getRoleId().longValue()) {
				invalidRoleId = false;
				break;
			}

		}
		if (invalidRoleId) {
			throw new ApplicationException(ErrorCode.E0481.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString()));
		} else {
			validInputBean.setRoleId(inputBean.getRoleId());
			validInputBean.setIsDeptAdmin(Boolean.TRUE);
		}

		deptId = userMaster.getDepartmentIdFk().getRegulatorId();

		if (StringUtils.isEmpty(deptCode)) {
			throw new ApplicationException(ErrorCode.E1556.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1556.toString()));
		} else if (deptId != null) {
			inputDeptId = regulatorRepo.findByRegulatorCode(deptCode, true).getRegulatorId();
			if (deptId.longValue() != inputDeptId.longValue()) {
				throw new ApplicationException(ErrorCode.E1565.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1565.toString()));
			} else {
				validInputBean.setDeptCode(deptCode);
				validInputBean.setDeptId(inputDeptId);
				validInputBean.setIsMainDept(regulatorRepo.findByRegulatorCode(deptCode, true).getIsMaster());
			}
		}
	}

}
