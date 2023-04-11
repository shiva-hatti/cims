/**
 * 
 */
package com.iris.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.EmailSetting;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author sajadhav
 */
@RestController
@RequestMapping("/service/emailController")
public class EmailController {

	@Autowired
	GenericService<EmailSetting, Long> emailService;

	static final Logger LOGGER = LogManager.getLogger(EmailController.class);

	@GetMapping(value = "/getEmailSettings")
	public ServiceResponse getEmailSettings() {
		try {
			List<EmailSetting> emailSettingList = emailService.getAllDataFor(null, null);
			return new ServiceResponseBuilder().setStatus(true).setResponse(emailSettingList.get(0)).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			return new ServiceResponseBuilder().setStatus(false).build();
		}
	}

}