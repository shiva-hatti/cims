
package com.iris.sdmx.exceltohtml.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.sdmx.exceltohtml.bean.ProcessUploadInputBean;
import com.iris.sdmx.exceltohtml.service.SdmxUploadFileProcessorService;
import com.iris.sdmx.exceltohtml.validator.SdmxUploadFileProcessorValidator;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author apagaria
 *
 */
@RestController
@RequestMapping("/service/sdmx/fileProcessor")
public class SdmxUploadFileProcessorController {

	/**
	 * 
	 */
	@Autowired
	private SdmxUploadFileProcessorService sdmxUploadFileProcessorService;

	/**
	 * 
	 */
	@Autowired
	private SdmxUploadFileProcessorValidator sdmxUploadFileProcessorValidator;

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxUploadFileProcessorController.class);

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param returnTemplateId
	 * @param processUploadInputBean
	 * @return
	 */
	@PutMapping("/user/{userId}/return/{returnTemplateId}/uploadFile")
	public ServiceResponse processUploadedFile(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("returnTemplateId") Long returnTemplateId, @RequestBody ProcessUploadInputBean processUploadInputBean) {
		LOGGER.info("START - Process Upload File request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			// Validate Request
			LOGGER.debug("Job Processing Id - " + jobProcessId + ", @processUploadedFile Validation START");
			sdmxUploadFileProcessorValidator.processFileValidation(processUploadInputBean, userId, returnTemplateId, jobProcessId);
			LOGGER.debug("Job Processing Id - " + jobProcessId + ", @processUploadedFile Validation END");
			// Process Files
			sdmxUploadFileProcessorService.processUploadedFile(processUploadInputBean, userId, returnTemplateId, jobProcessId);

			// Response
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
		} catch (ApplicationException aex) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId + ", Error Code - " + aex.getErrorCode() + ", Error Msg - " + aex.getErrorMsg(), aex);
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId, aex);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(aex.getErrorCode()).setStatusMessage(aex.getErrorMsg());
		} catch (Exception ex) {
			// ex.printStackTrace();
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId, ex);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Process Upload File request processed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

}
