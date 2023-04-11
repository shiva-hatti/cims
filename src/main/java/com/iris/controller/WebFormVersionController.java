package com.iris.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.HeaderInfoDto;
import com.iris.dto.ReturnTemplateDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.TaxonomyInfoDto;
import com.iris.dto.WebTaxVersionInfoDto;
import com.iris.dto.WebformVersionDto;
import com.iris.exception.ServiceException;
import com.iris.model.UserMaster;
import com.iris.service.impl.UserMasterService;
import com.iris.service.impl.WebFormVersionService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ServiceConstants;

@RestController
@RequestMapping("/service/webFormVersionController")
public class WebFormVersionController {

	private static final Logger Logger = LogManager.getLogger(WebFormVersionController.class);

	@Autowired
	private WebFormVersionService webFormVersionService;
	@Autowired
	private UserMasterService userMasterService;

	@PostMapping(value = "/getTableInfoList")
	public ServiceResponse getTableInfoList(@RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody WebformVersionDto webformVersionDto) {
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", getTableInfoList - START");
		List<HeaderInfoDto> headerInfoDto = new ArrayList<>();
		try {
			headerInfoDto = webFormVersionService.getTableInfo(webformVersionDto.getReturnCode(), webformVersionDto.getLangCode());
			if (CollectionUtils.isEmpty(headerInfoDto)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0712.toString())).build();
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0712.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(headerInfoDto);
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", getTableInfoList - End");
		return response;

	}

	@PostMapping(value = "/getTaxonomyVersionByEndDate")
	public ServiceResponse getTaxonomyVersionByEndDate(@RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody WebformVersionDto webformVersionDto) {
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", getTaxonomyVersionByEndDate - START");
		TaxonomyInfoDto taxonomyInfo = null;
		WebTaxVersionInfoDto webTaxVersionInfoDto = null;
		try {
			webTaxVersionInfoDto = webFormVersionService.getTaxonomyByEndDate(webformVersionDto.getReturnId(), webformVersionDto.getEndDate(), webformVersionDto.getDateFormat());
			if (webTaxVersionInfoDto == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0265.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0265.toString())).build();
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0265.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0265.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(webTaxVersionInfoDto);
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", getTaxonomyVersionByEndDate - End");
		return response;
	}

	@PostMapping(value = "/addEditVersionTableMapping")
	public ServiceResponse addEditVersionTableMapping(@RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody WebformVersionDto webformVersionDto) {
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", addEditVersionTableMapping - START");
		try {
			UserMaster userMaster = userMasterService.getDataById(webformVersionDto.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}
			/*if (!webFormVersionService.addEditVersionMapping(webformVersionDto.getReturnSecMapIdList(), webformVersionDto.getTaxonomyId(), userMaster)) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E0823.toString())
						.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0265.toString())).build();
			} */

		} catch (ServiceException e) {
			Logger.error("Exception while fetching table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0823.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0823.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
	}

	@PostMapping(value = "/getVersionTableInfo")
	public ServiceResponse getVersionTableInfo(@RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody WebformVersionDto webformVersionDto) {
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", getTableInfoList - START");
		List<HeaderInfoDto> headerInfoDto = new ArrayList<>();
		try {
			headerInfoDto = webFormVersionService.getVersionTableInfo(webformVersionDto.getTaxonomyId(), webformVersionDto.getLangCode());
			if (CollectionUtils.isEmpty(headerInfoDto)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0712.toString())).build();
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0712.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(headerInfoDto);
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", getTableInfoList - End");
		return response;

	}

	@PostMapping(value = "/getMappedAndUnMappedTableInfoList")
	public ServiceResponse getMappedAndUnMappedTableInfoList(@RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody WebformVersionDto webformVersionDto) {
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", getMappedAndUnMappedTableInfoList - START");
		ReturnTemplateDto returnTemplateDto = null;
		try {
			returnTemplateDto = webFormVersionService.getMappedUnMappedTableInfo(webformVersionDto.getReturnCode(), webformVersionDto.getLangCode(), webformVersionDto.getTaxonomyId());
			if (Objects.isNull(returnTemplateDto)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0712.toString())).build();
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching mapped and un Mapped table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0712.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching mapped and un Mapped table info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(returnTemplateDto);
		Logger.info(() -> ServiceConstants.JOB_PROCESSING_ID_TXT + jobProcessingId + ", getMappedAndUnMappedTableInfoList - End");
		return response;

	}
}
