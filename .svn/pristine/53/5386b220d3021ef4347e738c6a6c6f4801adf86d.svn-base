package com.iris.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.iris.controller.APIChannelController;
import com.iris.model.ApiLogDetails;
import com.iris.model.WebServiceComponentUrl;
import com.iris.service.impl.WebServiceComponentService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

public class LogApiDetailsUtil {

	@Autowired

	static final Logger logger = LogManager.getLogger(APIChannelController.class);

	public static void addApiLogDetails(ApiLogDetails apiLogDetails) {
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();
			Map<String, List<String>> valueMap = new HashMap<String, List<String>>();
			WebServiceComponentService webServiceComponentService = new WebServiceComponentService();

			List<String> valueList = new ArrayList<>();
			valueList.add(GeneralConstants.API_LOG_DETAILS_COMP.getConstantVal());
			valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
			valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

			WebServiceComponentUrl componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);

			// call web service
			// WebServiceComponentUrl componentUrl = webServiceComponentUrlRepo
			// .findByComponentTypeAndUrlHttpMethodTypeAndIsActiveTrue(
			// GeneralConstants.FILE_DETAILS_COMP.getConstantVal(),
			// RestWebserviceClient.HTTP_METHOD_TYPE_POST);

			String response = restServiceClient.callRestWebService(componentUrl, apiLogDetails, null);

			System.out.println("service response: " + response);
		} catch (Exception e) {
			logger.error("Exception while getting file info: ", e);
		}
	}

}
