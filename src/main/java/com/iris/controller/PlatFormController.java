package com.iris.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.model.Platform;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.model.UserRolePlatFormMap;
import com.iris.service.impl.PortalRoleService;
import com.iris.service.impl.PortalService;
import com.iris.service.impl.UserMasterService;

/**
 * 
 * @author svishwakarma
 *
 */
@RestController
@RequestMapping("/service/platForm")
public class PlatFormController {
	private static final Logger Logger = LoggerFactory.getLogger(PlatFormController.class);

	@Autowired
	PortalService service;

	@Autowired
	PortalRoleService portalRoleService;

	@Autowired
	UserMasterService userMasterService;

	@GetMapping("/portals")
	public ServiceResponse getAllPortal(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		Logger.info("Requseting Portals " + jobProcessId);
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(service.getActiveDataFor(null, null));
		return response;

	}

	@GetMapping("/portals/{userid}/{roleId}")
	public ServiceResponse getAllPortal(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userid") Long userId, @PathVariable("roleId") Long roleId) {
		Logger.info("Requseting Portals by userId " + jobProcessId);
		UserMaster user = userMasterService.getDataById(userId);
		if (user.getDepartmentIdFk() != null) {
			boolean isMaster = user.getDepartmentIdFk().getIsMaster();
			if (isMaster) {
				return getAllPortal(jobProcessId);
			}
		}

		List<UserRolePlatFormMap> userPlatFormList = portalRoleService.UserRolePlatFormMapByUser(roleId);
		Map<Long, Platform> userPlatForm = new HashMap<>();
		userPlatFormList.forEach(item -> userPlatForm.put(item.getPlatForm().getPlatFormId(), item.getPlatForm()));
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(userPlatForm.values());
		return response;

	}

	@GetMapping("/portalsMultipleRole/{userid}")
	public ServiceResponse getPortalMultipleRole(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userid") Long userId) {
		Logger.info("Requseting Portals by userId " + jobProcessId);
		UserMaster userMasterObj = userMasterService.getDataById(userId);
		List<Long> userRoleIds = new ArrayList<>();

		Set<UserRoleMaster> usrRoleMstrSet = userMasterObj.getUsrRoleMstrSet();
		for (UserRoleMaster userRoleMastObj : usrRoleMstrSet) {
			if (userRoleMastObj.getIsActive()) {
				userRoleIds.add(userRoleMastObj.getUserRole().getUserRoleId());
			}
		}

		List<UserRolePlatFormMap> userPlatFormList = portalRoleService.getDataByIds(userRoleIds.toArray(new Long[userRoleIds.size()]));
		Map<Long, Platform> userPlatForm = new HashMap<>();
		userPlatFormList.forEach(item -> userPlatForm.put(item.getPlatForm().getPlatFormId(), item.getPlatForm()));
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(userPlatForm.values());
		return response;

	}
}
