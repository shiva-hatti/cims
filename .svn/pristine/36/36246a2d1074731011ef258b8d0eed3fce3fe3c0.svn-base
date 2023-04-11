/**
 * 
 */
package com.iris.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.FilingPanDto;
import com.iris.dto.FilingPanNSDL;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.service.impl.FilingPanService;
import com.iris.util.constant.ErrorCode;

/**
 * @author sajadhav
 *
 */
@RestController
@RequestMapping(value = "/service/filingPanController")
public class FilingPanController {
	private static final Logger LOGGER = LogManager.getLogger(FilingPanController.class);

	@Autowired
	private FilingPanService filingPanService;

	@PostMapping(value = "/insertRecordIntoFilingQueue")
	public ServiceResponse insertRecordIntoFilingQueue(@RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody FilingPanDto filingPanDto) {

		LOGGER.info(jobProcessingId + " Request Received to insertRecordIntoFilingQueue");
		try {
			validateInputDto(filingPanDto);
			boolean flag = filingPanService.insertRecordIntoFilingQueue(filingPanDto);
			if (!flag) {
				LOGGER.info(jobProcessingId + " Request completed insertRecordIntoFilingQueue with flag false (Pan and filing no already present in the filign queue");
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(flag).build();
		} catch (ApplicationException ae) {
			LOGGER.error(jobProcessingId + " Exception occured to insertRecordIntoFilingQueue ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ae.getErrorCode()).setStatusMessage(ae.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error(jobProcessingId + " Exception occured to insertRecordIntoFilingQueue ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private void validateInputDto(FilingPanDto filingPanDto) throws ApplicationException {
		if (ObjectUtils.isEmpty(filingPanDto.getPanNo())) {
			throw new ApplicationException(ErrorCode.E1390.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1390.toString()));
		} else if (filingPanDto.getUploadId() == null) {
			throw new ApplicationException(ErrorCode.E1697.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1697.toString()));
		}
	}

	@PostMapping(value = "/updateFilingNSDLPanStatus")
	public ServiceResponse updateFilingNSDLPanStatus(@RequestHeader(name = "AppId") String jobProcessingId, @RequestBody List<FilingPanNSDL> filingPanNSDLList) {
		try {
			validateInputDtoToUpdateFilingNSDLStatus(filingPanNSDLList);
			filingPanService.updateFilingNSDLPanStatus(filingPanNSDLList);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		} catch (ApplicationException ae) {
			LOGGER.error(jobProcessingId + " Exception occured to fetch insertRecordIntoFilingQueue ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ae.getErrorCode()).setStatusMessage(ae.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error(jobProcessingId + " Exception occured to fetch insertRecordIntoFilingQueue ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

	}

	private void validateInputDtoToUpdateFilingNSDLStatus(List<FilingPanNSDL> filingPanNSDLList) throws ApplicationException {
		for (FilingPanNSDL filingPanNSDL : filingPanNSDLList) {
			if (filingPanNSDL.getFilingPanId() == null || filingPanNSDL.getNsdlPanVerifId() == null || filingPanNSDL.getStatus() == null || filingPanNSDL.getUploadId() == null) {
				throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
			}
		}
	}
}
