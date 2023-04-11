package com.iris.webservices.client;

import java.lang.reflect.Type;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.dto.ServiceResponse;
import com.iris.util.UtilMaster;

/**
 * @author psawant
 * @version 1.0
 * @date 10/06/2019
 */
@Service
public class WebServiceResponseReader {

	public Object readServiceResponse(@SuppressWarnings("rawtypes") Class objectClass, String serviceResponse, String responseMediaType) throws Exception {
		if (objectClass == null || UtilMaster.isEmpty(serviceResponse) || UtilMaster.isEmpty(responseMediaType)) {
			throw new Exception("objectClass || serviceResponse || responseMediaType");
		}

		if (objectClass == ServiceResponse.class) {
			ServiceResponse response = null;
			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			Gson gson = new Gson();
			response = gson.fromJson(serviceResponse, listToken);
			return response;
		}
		return null;
	}

}