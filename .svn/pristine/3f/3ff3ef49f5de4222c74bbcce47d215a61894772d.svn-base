/**
 * 
 */
package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.ETLConceptsInfo;
import com.iris.service.impl.ETLConceptInfoService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;

/**
 * @author akhandagale
 *
 */
@RestController
@RequestMapping("/service/customJsonProcessController")
public class CustomJsonProcessController {

	@Autowired
	private ETLConceptInfoService eTLConceptInfoService;

	static final Logger LOGGER = LogManager.getLogger(CustomJsonProcessController.class);

	@PostMapping(value = "/getConceptDetailsForCustomJson")
	public ServiceResponse getConceptDetailsForCustomJson() {
		try {
			LOGGER.info("CustomJsonProcessController :: getConceptDetailsForCustomJson Starts");

			List<ETLConceptsInfo> eTLConceptsInfoList = eTLConceptInfoService.getAllDataFor(null, null);
			List<ETLConceptsInfo> newList = new ArrayList<>();
			if (!CollectionUtils.isEmpty(eTLConceptsInfoList)) {
				for (ETLConceptsInfo obj : eTLConceptsInfoList) {
					ETLConceptsInfo newObj = new ETLConceptsInfo();
					newObj.setId(obj.getId());
					newObj.setConcept(obj.getConcept());
					newObj.setReturnCode(obj.getReturnObj().getReturnCode());
					newObj.setReturnId(obj.getReturnObj().getReturnId());
					newList.add(newObj);
				}
				LOGGER.info("CustomJsonProcessController :: getConceptDetailsForCustomJson Ends");
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(newList).build();
			}

		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
		return null;
	}

}
