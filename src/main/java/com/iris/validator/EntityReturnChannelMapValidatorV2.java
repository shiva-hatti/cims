/**
 * 
 */
package com.iris.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.iris.caching.ObjectCache;
import com.iris.dto.EntityReturnChanneMapAppDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.UserDto;
import com.iris.exception.ApplicationException;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * This is a validator class responsible for validate error code detail API parameters
 * 
 * @author sajadhav
 *
 */
@Component
public class EntityReturnChannelMapValidatorV2 {

	public void validateReturnEntityMapDto(ReturnEntityMapDto returnEntityMapDto) throws ApplicationException {
		if (ObjectUtils.isEmpty(returnEntityMapDto.getUserId())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (returnEntityMapDto.getIsActive() == null) {
			throw new ApplicationException(ErrorCode.E1502.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1502.toString()));
		} else if (returnEntityMapDto.getLangCode() == null) {
			returnEntityMapDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
		}
	}

	public void validateReturnEntityMapDto(UserDto userDto) throws ApplicationException {
		if (ObjectUtils.isEmpty(userDto.getUserId())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (userDto.getIsActive() == null) {
			throw new ApplicationException(ErrorCode.E1502.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1502.toString()));
		} else if (userDto.getLangCode() == null) {
			userDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
		}
	}

	public void validateDtoForApproval(EntityReturnChanneMapAppDto entityReturnChanneMapAppDto) throws ApplicationException {
		if (entityReturnChanneMapAppDto.getApprovedBy() == null || ObjectUtils.isEmpty(entityReturnChanneMapAppDto.getApprovedBy().getUserId())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (entityReturnChanneMapAppDto.getEntityReturnChanneMapAppId() == null) {
			throw new ApplicationException(ErrorCode.E1502.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1502.toString()));
		}
	}
}
