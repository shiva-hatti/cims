package com.iris.rbrToEbr.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.model.UserMaster;
import com.iris.rbrToEbr.bean.CtlEbrElementBean;
import com.iris.rbrToEbr.entity.CtlEbrElementEntity;
import com.iris.rbrToEbr.entity.EbrRbrFlow;
import com.iris.rbrToEbr.service.CtlEbrElementService;
import com.iris.rbrToEbr.validator.CtlEbrElementValidator;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author vjadhav
 *
 */
@Controller
@RestController
@RequestMapping(value = "/service/ctlEbrElement")
public class CtlEbrElementController {

	/**
	 * 
	 */
	@Autowired
	private CtlEbrElementService ctlEbrElementService;

	@Autowired
	private CtlEbrElementValidator ctlEbrElementValidator;

	private static final Logger LOGGER = LogManager.getLogger(CtlEbrElementController.class);

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param roleId
	 * @param langCode
	 * @param ebrRbrFlowMasterBean
	 */
	@PostMapping(value = "/user/{userId}/role/{roleId}/lang/{langCode}/saveCtlEbrElement")
	public ServiceResponse saveEbrRbrFlowMaster(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable(name = "roleId") Long roleId, @PathVariable("langCode") String langCode, @RequestBody List<CtlEbrElementBean> ctlEbrElementBeanList) {
		LOGGER.info("START - Add Ctl ebr element request received with Job Processing ID : " + jobProcessId);
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(false);
		try {
			LOGGER.info("Validation start for Add Ctl ebr element request received with Job Processing ID : " + jobProcessId);
			ctlEbrElementValidator.validateSaveCtlEbrElementRequest(jobProcessId, userId);
			LOGGER.info("Validation end for Add Ctl ebr element request received with Job Processing ID : " + jobProcessId);
			Map<String, Long> idsMap = null;
			if (!CollectionUtils.isEmpty(ctlEbrElementBeanList)) {
				idsMap = new HashMap<>();
				for (CtlEbrElementBean ctlEbrElementBean : ctlEbrElementBeanList) {
					CtlEbrElementEntity ctlEbrElementEntity = new CtlEbrElementEntity();
					BeanUtils.copyProperties(ctlEbrElementBean, ctlEbrElementEntity);

					UserMaster userMaster = new UserMaster();
					userMaster.setUserId(userId);
					EbrRbrFlow controlFk = new EbrRbrFlow();
					controlFk.setControlId(ctlEbrElementBean.getControlFk());
					ctlEbrElementEntity.setControlFk(controlFk);
					ctlEbrElementEntity.setCreatedBy(userMaster);
					ctlEbrElementEntity.setCreatedDate(new Date());

					ctlEbrElementEntity = ctlEbrElementService.add(ctlEbrElementEntity);

					idsMap.put(ctlEbrElementBean.getControlFk() + "_" + ctlEbrElementEntity.getElementCode(), ctlEbrElementEntity.getControlEbrElementId());
				}
				serviceResponseBuilder.setStatus(true);
				serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
				serviceResponseBuilder.setResponse(idsMap);
			} else {
				serviceResponseBuilder.setStatus(false);
				serviceResponseBuilder.setStatusCode(SDMXConstants.FAILURE_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.FAILURE_MESSAGE);
			}

		} catch (ApplicationException applicationException) {
			LOGGER.error("Exception occured while saving Ctl ebr element records " + ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, applicationException);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error("Exception occured while saving Ctl ebr element for job processing Id : " + jobProcessId + "", e);
			serviceResponseBuilder.setStatusCode(ErrorCode.EC0033.toString());
			serviceResponseBuilder.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString()));
		}
		LOGGER.info("END - Add Ctl ebr element request received with Job Processing ID : " + jobProcessId);
		return serviceResponseBuilder.build();
	}

}
