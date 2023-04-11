package com.iris.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.iris.dto.FileDetailRequestDto;
import com.iris.dto.FilingHistoryDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UserDto;
import com.iris.service.MasterDataAuthenticationServiceV2;
import com.iris.service.impl.FileDetailsService;
import com.iris.service.impl.UserMasterService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.validator.FileDetailsValidatorV2;

/**
 * @author pmohite
 *
 */
@RestController
@RequestMapping("/service/FileDetailsController/V2")
public class FileDetailsControllerV2 {

	static final Logger Logger = LogManager.getLogger(FileDetailsControllerV2.class);

	@Autowired
	private FileDetailsValidatorV2 fileDetailsValidatorV2;

	@Autowired
	private UserMasterService userMasterService;

	@Autowired
	private FileDetailsService fileDetailsService;

	@Autowired
	private MasterDataAuthenticationServiceV2 MasterDataAuthenticationServiceV2;

	/**
	 * Gets the file details. This method is to get all details about the file uploaded by Entity via any Channel. This method also returns uploaded file details based on filters like return code,entity code,status, start date, end date etc..
	 *
	 * @param userName       the user name
	 * @param entityCodeList the entity code list
	 * @param returnCodeList the return code list
	 * @param startDate      the start date
	 * @param endDate        the end date
	 * @param fileStatusList the file status list
	 * @param channelList    the channel list
	 * @return the file details
	 */
	@PostMapping(value = "/getFileDetails")
	public ServiceResponse getFileDetails(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody FileDetailRequestDto fileDetailRequestDto) {
		try {
			Logger.info("Request received to get return filing history record for job processing Id : " + jobProcessId);

			fileDetailsValidatorV2.validateFileDetailsRequestDto(fileDetailRequestDto, jobProcessId);

			// need to be modified
			UserDto userDto = userMasterService.getLightWeightUserDto(fileDetailRequestDto.getUserId());
			List<String> returnCodeList = new ArrayList<>();
			List<String> ifscCodeList = new ArrayList<>();

			if (userDto != null) {
				if (userDto.getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal())) {
					if (fileDetailRequestDto.getReturnCode() == null || fileDetailRequestDto.getIfscCode() == null) {
						return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).setStatusCode(ErrorCode.E0889.toString()).build();
					}
				} else {
					if (userDto.getRegulatorId() != null) {
						if (!userDto.getIsMaster().equals(Boolean.TRUE) && (fileDetailRequestDto.getReturnCode() == null || fileDetailRequestDto.getIfscCode() == null)) {
							return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).setStatusCode(ErrorCode.E0889.toString()).build();
						}
					} else {
						return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).setStatusCode(ErrorCode.E0889.toString()).build();
					}
				}
			} else {
				Logger.error("User Not found in the system for user Id : " + fileDetailRequestDto.getUserId() + " Job processign id : " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).setStatusCode(ErrorCode.E0889.toString()).build();
			}

			Map<String, String> entityIfscCodeAndLabelMap = new HashMap<>();
			if (!MasterDataAuthenticationServiceV2.isAuthorisedRequestWithRespectToEntityIfscCode(fileDetailRequestDto.getUserId(), fileDetailRequestDto.getRoleId(), fileDetailRequestDto.getLangCode(), fileDetailRequestDto.getIfscCode(), entityIfscCodeAndLabelMap, jobProcessId)) {
				Logger.error("Input entity and User entity not matched for job processing id : " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0386.toString())).setStatusCode(ErrorCode.E0386.toString()).build();
			}

			Map<String, String> returnCodeAndLabelMap = new HashMap<>();
			if (!MasterDataAuthenticationServiceV2.isAuthorisedRequestWithRespectToReturnId(fileDetailRequestDto.getUserId(), fileDetailRequestDto.getRoleId(), fileDetailRequestDto.getLangCode(), fileDetailRequestDto.getReturnCode(), returnCodeAndLabelMap, jobProcessId)) {
				Logger.error("Input Return and User return not matched for job processing id : " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0386.toString())).setStatusCode(ErrorCode.E0386.toString()).build();
			}

			if (userDto.getIsMaster() != null && userDto.getIsMaster().equals(Boolean.TRUE) && (fileDetailRequestDto.getReturnCode() == null || fileDetailRequestDto.getIfscCode() == null)) {
				returnCodeAndLabelMap.forEach((k, v) -> returnCodeList.add(k));
				entityIfscCodeAndLabelMap.forEach((k, v) -> ifscCodeList.add(k));
				fileDetailRequestDto.setReturnCode(returnCodeList);
				fileDetailRequestDto.setIfscCode(ifscCodeList);
				fileDetailRequestDto.setUnMappedReturn(true);
			}

			List<FilingHistoryDto> rbrFilingHistoryDtoList = fileDetailsService.getFilingHistoryDataForRBR(fileDetailRequestDto);

			List<FilingHistoryDto> ebrFilingHistoryDtoList = fileDetailsService.getFilingHistoryDataForEbr(fileDetailRequestDto);

			List<FilingHistoryDto> finalFilingHistoryDtoList = new ArrayList<>();

			if (!CollectionUtils.isEmpty(rbrFilingHistoryDtoList)) {
				finalFilingHistoryDtoList.addAll(rbrFilingHistoryDtoList);
			}

			if (!CollectionUtils.isEmpty(ebrFilingHistoryDtoList)) {
				finalFilingHistoryDtoList.addAll(ebrFilingHistoryDtoList);
			}

			if (!finalFilingHistoryDtoList.isEmpty()) {
				Collections.sort(finalFilingHistoryDtoList, new Comparator<FilingHistoryDto>() {
					@Override
					public int compare(FilingHistoryDto o1, FilingHistoryDto o2) {
						return o2.getUploadedDateInLong().compareTo(o1.getUploadedDateInLong());
					}
				});
			}

			// set Label
			finalFilingHistoryDtoList.forEach(f -> {
				if (entityIfscCodeAndLabelMap.get(f.getIfscCode()) != null) {
					f.setEntityName(entityIfscCodeAndLabelMap.get(f.getIfscCode()));
				}
				if (returnCodeAndLabelMap.get(f.getReturnCode()) != null) {
					f.setReturnlabel(returnCodeAndLabelMap.get(f.getReturnCode()));
				}
			});

			Logger.info("Request completed to get Return Filing history record for job processing Id : " + jobProcessId);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(finalFilingHistoryDtoList).build();
		} catch (Exception e) {
			Logger.error("Exception while getting file details info: ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
	}

}
