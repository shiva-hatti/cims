/**
 * 
 */
package com.iris.sdmx.exceltohtml.validator;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.model.ReturnTemplate;
import com.iris.repository.ReturnTemplateRepository;
import com.iris.sdmx.exceltohtml.bean.ProcessUploadInputBean;
import com.iris.util.constant.ErrorCode;
import com.iris.validator.CIMSCommonValidator;

/**
 * @author apagaria
 *
 */
@Component
public class SdmxUploadFileProcessorValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6472488158507152808L;

	private static final Logger LOGGER = LogManager.getLogger(SdmxUploadFileProcessorValidator.class);

	@Autowired
	private CIMSCommonValidator cimsCommonValidator;

	@Autowired
	ReturnTemplateRepository returnTemplateRepository;

	/**
	 * @param processUploadInputBean
	 * @throws ApplicationException
	 */
	public void processFileValidation(ProcessUploadInputBean processUploadInputBean, Long userId, Long returnTemplateId, String jobProcessingId) throws ApplicationException {
		// Excel File Path
		if (StringUtils.isBlank(processUploadInputBean.getExcelFilePath())) {
			throw new ApplicationException(ErrorCode.E1453.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1453.toString()));
		}

		// HTML File Path
		if (StringUtils.isBlank(processUploadInputBean.getHtmlFilePath())) {
			throw new ApplicationException(ErrorCode.E1454.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1454.toString()));
		}

		// File Name
		if (StringUtils.isBlank(processUploadInputBean.getFileName())) {
			throw new ApplicationException(ErrorCode.E0657.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0657.toString()));
		}

		// Return Code
		if (StringUtils.isBlank(processUploadInputBean.getReturnCode())) {
			throw new ApplicationException(ErrorCode.E0132.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0132.toString()));
		}

		// Sheet Name
		if (StringUtils.isBlank(processUploadInputBean.getSheetInfoBean())) {
			throw new ApplicationException(ErrorCode.E1455.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1455.toString()));
		}

		//Job procession id
		cimsCommonValidator.validateJobProcessingId(jobProcessingId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		// validate Return Template id
		cimsCommonValidator.validateReturnTemplate(returnTemplateId);

		//validate if return template is active
		isReturnTemplateActive(returnTemplateId);
	}

	public void isReturnTemplateActive(Long returnTemplateId) throws ApplicationException {
		ReturnTemplate returnTemplate = returnTemplateRepository.findActiveReturnTemplate(returnTemplateId);

		if (returnTemplate == null) {
			LOGGER.error("Return Template Id not active");
			throw new ApplicationException(ErrorCode.E0053.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0053.toString()));
		}
	}
}
