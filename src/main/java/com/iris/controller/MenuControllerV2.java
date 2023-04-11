package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.dto.MenuDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.Menu;
import com.iris.repository.MenuRepo;

@RestController
@RequestMapping("/service/menu/V2")
public class MenuControllerV2 {

	static final Logger logger = LogManager.getLogger(MenuControllerV2.class);

	@Autowired
	private MenuRepo menuRepo;

	@PostMapping(value = "/getMenuListOnRoleList")
	public ServiceResponse getMenuListOnRoleList(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody MenuDto menuDto) {
		try {
			logger.info("getMenuList method started");

			List<Menu> menuList = menuRepo.getMenuListOnRoleList(menuDto.getRoleIdLst());

			List<MenuDto> menuDtoList = new ArrayList<>();
			if (!CollectionUtils.isEmpty(menuList)) {
				for (Menu menu : menuList) {
					MenuDto obj = new MenuDto();
					obj.setMenuId(menu.getMenuId());
					if (menu.getParentMenu() != null) {
						obj.setParentMenuId(menu.getParentMenu().getMenuId());
					}
					obj.setOrderNo(menu.getOrderNo());
					menuDtoList.add(obj);
				}
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(menuDtoList)).build();
		} catch (Exception e) {
			logger.error("getMenuList method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setResponse(null).build();
		}
	}

}