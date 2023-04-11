package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.model.Menu;
import com.iris.model.MenuRoleMap;
import com.iris.model.UserRole;
import com.iris.service.impl.MenuRoleMapService;

/**
 * 
 * @author svishwakarma
 *
 */
@RestController
@RequestMapping("/service/menuService")
public class MenuRoleController {
	private static final Logger logger = LoggerFactory.getLogger(MenuRoleController.class);

	@Autowired
	MenuRoleMapService service;

	@GetMapping("/{rollId}")
	public ServiceResponse getAllMenu(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("rollId") Long rollId) {
		logger.info("Request for Menu Role" + jobProcessId);
		List<MenuRoleMap> menuRoleList = service.findByRole(rollId);
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(menuRoleList);
		return response;

	}

	public ServiceResponse getAllMenuUserRole(List<Long> userRoleIds) {
		logger.info("Request for Menu User Role");
		List<MenuRoleMap> menuRoleList = service.getMenuForAllRoles(userRoleIds);
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(menuRoleList);
		return response;
	}

	@GetMapping("getAllMenuByRoleId/{rollId}")
	public ServiceResponse getAllMenuByRoleId(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("rollId") Long rollId) {
		logger.info("Request for Menu Role" + jobProcessId);
		List<MenuRoleMap> menuRoleList = service.findByRole(rollId);
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(prepareResponse(menuRoleList));
		return response;

	}

	private List<MenuRoleMap> prepareResponse(List<MenuRoleMap> menuRoleList) {
		List<MenuRoleMap> menuRoleListNew = new ArrayList<>();

		for (MenuRoleMap menuRoleMap : menuRoleList) {
			MenuRoleMap menuRoleMapNew = new MenuRoleMap();
			menuRoleMapNew.setIsActive(menuRoleMap.getIsActive());
			Menu menu = new Menu();
			menu.setMenuId(menuRoleMap.getMenuIDFk().getMenuId());
			menu.setIsActive(menuRoleMap.getMenuIDFk().getIsActive());
			menu.setIsEntity(menuRoleMap.getMenuIDFk().getIsEntity());
			menu.setIsEntity(menuRoleMap.getMenuIDFk().getIsEntity());
			menuRoleMapNew.setMenuIDFk(menu);

			UserRole userRole = new UserRole();

			userRole.setUserRoleId(menuRoleMap.getUserRoleIdFk().getUserRoleId());
			userRole.setIsActive(menuRoleMap.getUserRoleIdFk().getIsActive());
			userRole.setRoleType(menuRoleMap.getUserRoleIdFk().getRoleType());
			menuRoleMapNew.setUserRoleIdFk(userRole);
			menuRoleListNew.add(menuRoleMapNew);
		}
		return menuRoleListNew;
	}

}
