/**
 * 
 */
package com.iris.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.iris.caching.ObjectCache;
import com.iris.dto.FileDetailRequestDto;
import com.iris.dto.FileDetailsDto;
import com.iris.dto.UserDto;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.util.JsonUtility;
import com.iris.util.UtilMaster;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * This is a validator class responsible for validate error code detail API parameters
 * 
 * @author sajadhav
 *
 */
@Component
public class FileDetailsValidatorV2 {

	public void validateFileDetailsRequestDto(FileDetailRequestDto fileDetailsRequestDto, String jobProcessId) throws ApplicationException {

		if (ObjectUtils.isEmpty(jobProcessId)) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (CollectionUtils.isEmpty(fileDetailsRequestDto.getStatus())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (CollectionUtils.isEmpty(fileDetailsRequestDto.getUploadChannelList())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (ObjectUtils.isEmpty(fileDetailsRequestDto.getUserId())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (fileDetailsRequestDto.getLangCode() == null) {
			fileDetailsRequestDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
		}
	}
}
