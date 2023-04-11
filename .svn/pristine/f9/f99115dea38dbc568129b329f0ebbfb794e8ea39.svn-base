/**
 * 
 */
package com.iris.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.iris.caching.ObjectCache;
import com.iris.dto.ViewRevisionRequestInputBeanV2;
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
public class ViewRevisionRequestValidatorV2 {

	/**
	 * @param viewRevisionRequestInputBeanV2
	 * @throws ApplicationException
	 */
	public void validateRequestObjectOfViewRevisionRequest(ViewRevisionRequestInputBeanV2 viewRevisionRequestInputBeanV2) throws ApplicationException {
		if (ObjectUtils.isEmpty(viewRevisionRequestInputBeanV2.getUserId())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (viewRevisionRequestInputBeanV2.getIsActive() == null) {
			throw new ApplicationException(ErrorCode.E1685.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1685.toString()));
		} else if (viewRevisionRequestInputBeanV2.getLangCode() == null) {
			viewRevisionRequestInputBeanV2.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
		}
	}
}
