package com.iris.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.iris.caching.ObjectCache;
import com.iris.dto.ReturnByRoleInputDto;
import com.iris.exception.ApplicationException;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * This is a validator class responsible for validate error code detail API parameters
 * 
 * @author bthakare
 *
 */
@Component
public class DownloadReturnTempValidator {

	/**
	 * @param returnChannelMapReqDto
	 * @throws ApplicationException
	 */
	public void validateReturnTempMapReqDto(ReturnByRoleInputDto returnByRoleInputDto) throws ApplicationException {
		if (ObjectUtils.isEmpty(returnByRoleInputDto.getUserId())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (returnByRoleInputDto.getIsActive() == null) {
			throw new ApplicationException(ErrorCode.E1685.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1685.toString()));
		} else if (returnByRoleInputDto.getLangCode() == null) {
			returnByRoleInputDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
		}
	}

}
