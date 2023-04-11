/**
 * 
 */
package com.iris.service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.caching.ObjectCache;
import com.iris.dto.OwnerReturn;
import com.iris.exception.ApplicationException;
import com.iris.model.ReturnRegulatorMapping;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.service.impl.ReturnRegulatorMappingService;
import com.iris.service.impl.UserMasterService;
import com.iris.service.impl.UserRoleReturnMappingService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * @author apagaria
 *
 */
@Service
public class ReturnRegulatorControllerServiceV2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 891081803458408653L;

	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnRegulatorControllerServiceV2.class);

	/**
	 * 
	 */
	@Autowired
	private UserMasterService userMasterService;

	/**
	 * 
	 */
	@Autowired
	private ReturnRegulatorMappingService returnRegulatorMappingService;

	/**
	 * 
	 */
	@Autowired
	private UserRoleReturnMappingService userRoleReturnMappingService;

	/**
	 * @param fetchReturnByUserRoleNRegulatorRequestV2
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	public Set<OwnerReturn> fetchReturnByUserRoleNRegulatorRequestV2(Long userId, String jobProcessId) throws ApplicationException {
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", FetchReturnByUserRoleNRegulatorRequestV2 service process start ");
		// Fetch User by user id
		UserMaster userMaster = userMasterService.getDataById(userId);

		if (userMaster.getRoleType().getRoleTypeId() != 1) {
			// User is not a regulator user E0801
			LOGGER.error(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", User is not regulator type ");
			throw new ApplicationException(ErrorCode.E0801.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0801.toString()));
		}
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", User is a regulator ");

		// isAdmin and role list
		Set<Long> roleIdSet = new HashSet<>();
		Boolean isDeptAdmin = fetchRoleSetAndIsAdminByUser(userMaster, jobProcessId, roleIdSet);
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", Fetched Department admin " + isDeptAdmin + ",  roleIdSet " + roleIdSet);

		// Fetch Department Detail
		Long regulatorId = 0L;
		Boolean isMainDepartment = false;
		if (userMaster.getDepartmentIdFk() != null && userMaster.getDepartmentIdFk().getRegulatorId() != null) {
			regulatorId = userMaster.getDepartmentIdFk().getRegulatorId();
			isMainDepartment = userMaster.getDepartmentIdFk().getIsMaster();
			LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", Fetched regulator id " + regulatorId + ",  isMainDepartment " + isMainDepartment);
		} else {
			// User is not mapped with any department/regulator E0803
			LOGGER.error(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", User is not mapped with any department ");
			throw new ApplicationException(ErrorCode.E0803.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0803.toString()));
		}

		// Fetch return list by role id and department
		Set<OwnerReturn> ownerReturnSet = null;
		if (isDeptAdmin) {
			if (isMainDepartment) {
				ownerReturnSet = userRoleReturnMappingService.getUserRoleReturnDataByRoleIdAndActiveFlag(roleIdSet, true);
			} else {
				ownerReturnSet = returnRegulatorMappingService.getMappedReturnListByRegulatorIdAndIsActiveStatus(roleIdSet, regulatorId, true);
			}
		}

		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", FetchReturnByUserRoleNRegulatorRequestV2 service process end ");
		return ownerReturnSet;
	}

	/**
	 * @param returnRegulatorMappingSet
	 * @param jobProcessId
	 * @return
	 */
	public Set<OwnerReturn> convertReturnRegulatorToOwnerReturn(Set<ReturnRegulatorMapping> returnRegulatorMappingSet, String jobProcessId) {
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", convertReturnRegulatorToOwnerReturn converter process Start ");
		if (!CollectionUtils.isEmpty(returnRegulatorMappingSet)) {
			Set<OwnerReturn> ownerReturnSet = new HashSet<>();
			for (ReturnRegulatorMapping returnRegulatorMapping : returnRegulatorMappingSet) {
				OwnerReturn ownerReturn = new OwnerReturn();
				ownerReturn.setReturnCode(returnRegulatorMapping.getReturnIdFk().getReturnCode());
				ownerReturn.setReturnId(returnRegulatorMapping.getReturnIdFk().getReturnId());
				ownerReturn.setReturnName(returnRegulatorMapping.getReturnIdFk().getReturnName());
				ownerReturnSet.add(ownerReturn);
			}
			LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", convertReturnRegulatorToOwnerReturn converter process end, ownerReturnSet " + ownerReturnSet);
			return ownerReturnSet;
		}
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", convertReturnRegulatorToOwnerReturn converter process end as return regulator set is blank ");
		return null;
	}

	/**
	 * Fetch is Admin and role id set from user master
	 * 
	 * @param userMaster
	 * @param jobProcessId
	 * @param roleIdSet
	 * @param isDeptAdmin
	 * @throws ApplicationException
	 */
	private Boolean fetchRoleSetAndIsAdminByUser(UserMaster userMaster, String jobProcessId, Set<Long> roleIdSet) throws ApplicationException {
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", fetchRoleSetAndIsAdminByUser start ");
		Boolean isDeptAdmin = false;
		Set<UserRoleMaster> userRoleMasterSet = userMaster.getUsrRoleMstrSet();
		if (!CollectionUtils.isEmpty(userRoleMasterSet)) {
			for (UserRoleMaster userRoleMaster : userRoleMasterSet) {
				if ("1".equalsIgnoreCase(userRoleMaster.getUserRole().getDeptAdmin())) {
					roleIdSet.add(userRoleMaster.getUserRole().getUserRoleId());
					isDeptAdmin = true;
				}
			}
		} else {
			// User is not mapped with any role E0481
			LOGGER.error(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", User is not mapped with any role ");
			throw new ApplicationException(ErrorCode.E0481.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString()));
		}
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", fetchRoleSetAndIsAdminByUser end ");
		return isDeptAdmin;
	}

}
