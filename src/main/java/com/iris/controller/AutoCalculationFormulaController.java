package com.iris.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.AutoCalFormulaBean;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.AutoCalculationFormula;
import com.iris.repository.AutoCalculationFormulaRepo;
import com.iris.util.constant.ErrorCode;

@RestController
@RequestMapping("/service/autoCalculationFormulaController")
public class AutoCalculationFormulaController {

	static final Logger LOGGER = LogManager.getLogger(AutoCalculationFormulaController.class);

	@Autowired
	private AutoCalculationFormulaRepo autoCalculationFormulaRepo;

	@PostMapping(value = "/getFormulaByReturnTemplateId/{returnId}/{returnTemplateId}")
	public ServiceResponse getFormulaByReturnTemplateId(@RequestHeader("JobProcessingId") String JobProcessingId, @PathVariable Long returnId, @PathVariable Long returnTemplateId) {
		AutoCalFormulaBean autoCalFormulaBean = new AutoCalFormulaBean();
		try {
			LOGGER.info("getFormulaByReturnTemplateId method started " + returnId);

			if (returnId == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0480.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0480.toString())).build();
			}

			if (returnTemplateId == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1456.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0480.toString())).build();
			}
			AutoCalculationFormula autoCal = autoCalculationFormulaRepo.findByAutoCalVesrionReturnTemplateFkReturnTemplateId(returnTemplateId);
			if (autoCal != null) {
				autoCalFormulaBean.setFormulaJson(autoCal.getFormulaJson());
				autoCalFormulaBean.setCrossElrJson(autoCal.getCrossElrJson());
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(autoCalFormulaBean)).build();
		} catch (Exception e) {
			LOGGER.info("getFormulaByReturnTemplateId ", e);
			return new ServiceResponseBuilder().setStatus(false).setResponse(null).build();
		}
	}

}