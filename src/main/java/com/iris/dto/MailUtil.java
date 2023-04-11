package com.iris.dto;

import java.util.List;

import com.iris.model.WebServiceComponentUrl;
import com.iris.webservices.client.CIMSRestWebserviceClient;

public class MailUtil {
	public static String sendMail(WebServiceComponentUrl webServiceComponent, List<MailInfoBean> list) throws Exception {
		CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();
		return restServiceClient.callRestWebService(webServiceComponent, list);
	}

	public static String sendMail(MailInfoBean mailInfoBean) throws Exception {
		CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();
		return restServiceClient.callRestWebService(null, mailInfoBean);
	}

}
