package com.iris.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.util.Validations;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;

@Component
public class CIMSCustomizedServiceInterceptor extends HandlerInterceptorAdapter {

	@Value("${appId.allowed}")
	private String appIds;

	private static List<String> allowedAppIdList;

	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {

		if (Validations.isEmpty(arg0.getHeader(GeneralConstants.APP_ID.getConstantVal()))) {
			ServiceResponse response = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.E001.getConstantVal()).setStatusMessage(ErrorConstants.ERROR_NO_APPID_SET_IN_HEADER.getConstantVal()).build();
			arg1.getWriter().write(new ObjectMapper().writeValueAsString(response));
			return false;
		}

		if (Validations.isEmpty(appIds)) {
			ServiceResponse response = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.E001.getConstantVal()).setStatusMessage(ErrorConstants.ERROR_NO_APPID_SET_IN_APP.getConstantVal()).build();
			arg1.getWriter().write(new ObjectMapper().writeValueAsString(response));
			return false;
		}

		if (CollectionUtils.isEmpty(allowedAppIdList)) {
			String[] appIdArr = appIds.split(GeneralConstants.APPID_SEPERATOR.getConstantVal());
			allowedAppIdList = Arrays.asList(appIdArr);
		}

		if (allowedAppIdList.contains(Validations.trimInput(arg0.getHeader(GeneralConstants.APP_ID.getConstantVal())))) {
			return true;
		} else {
			ServiceResponse response = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.E001.getConstantVal()).setStatusMessage(ErrorConstants.ERROR_INVALID_APPID_SET_IN_HEADER.getConstantVal()).build();
			arg1.getWriter().write(new ObjectMapper().writeValueAsString(response));
			return false;
		}
	}

}
