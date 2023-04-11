/**
 * 
 */
package com.iris.controller;

import java.util.ArrayList;
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
import com.iris.dto.LabelDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.ErrorKeyLabel;
import com.iris.model.FieldKeyLabel;
import com.iris.model.GridFormKeyLabel;
import com.iris.service.impl.ErrorKeyLabelService;
import com.iris.service.impl.FieldKeyLabelService;
import com.iris.service.impl.GridFormKeyLabelService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author siddique
 *
 */

@RestController
@RequestMapping("/service/labelMasterService")
public class LabelMasterController {

	@Autowired
	private ErrorKeyLabelService errorKeyLabelService;

	@Autowired
	private FieldKeyLabelService fieldKeyLabelService;

	@Autowired
	private GridFormKeyLabelService gridFormKeyLabelService;

	static final Logger logger = LogManager.getLogger(LabelMasterController.class);

	@PostMapping(value = "/getLabelData")
	public ServiceResponse getLabelDescriptionWithLang(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody LabelDto labelDto) {
		logger.info("request received for processing id", jobProcessId);
		Map<String, String> labelKeyMap = null;
		List<String> languageCodeList = null;
		String langCode = null;
		int labelDtoListSize = 0;
		if (labelDto == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage(GeneralConstants.NULL_BEAN.getConstantVal()).build();
		}
		try {
			labelKeyMap = new HashMap<>();
			Map<String, List<String>> valueMap = new HashMap<>();
			languageCodeList = new ArrayList<>();
			languageCodeList.add(labelDto.getLanguageCode());
			valueMap.put(ColumnConstants.LABEL_KEY.getConstantVal(), labelDto.getLabelKeyList());
			valueMap.put(ColumnConstants.LANGUAGE_CODE.getConstantVal(), languageCodeList);
			List<ErrorKeyLabel> errorKeyValueList = errorKeyLabelService.getDataByColumnValue(valueMap, MethodConstants.GET_LABEL_DATA_BY_KEY_AND_LANG.getConstantVal());
			if (!CollectionUtils.isEmpty(errorKeyValueList)) {
				for (ErrorKeyLabel errorKeyLabel : errorKeyValueList) {
					labelKeyMap.put(errorKeyLabel.getErrorKey(), errorKeyLabel.getErrorKeyLable());
					ObjectCache.putLabelKeyValue(labelDto.getLanguageCode(), errorKeyLabel.getErrorKey(), errorKeyLabel.getErrorKeyLable());
				}
			}

			List<FieldKeyLabel> fieldKeyLabelList = fieldKeyLabelService.getDataByColumnValue(valueMap, MethodConstants.GET_LABEL_DATA_BY_KEY_AND_LANG.getConstantVal());

			if (!CollectionUtils.isEmpty(fieldKeyLabelList)) {
				for (FieldKeyLabel fieldKeyLabel : fieldKeyLabelList) {
					labelKeyMap.put(fieldKeyLabel.getFieldKey(), fieldKeyLabel.getFieldKeyLable());
					ObjectCache.putLabelKeyValue(labelDto.getLanguageCode(), fieldKeyLabel.getFieldKey(), fieldKeyLabel.getFieldKeyLable());
				}
			}

			List<GridFormKeyLabel> gridFormKeyLabelList = gridFormKeyLabelService.getDataByColumnValue(valueMap, MethodConstants.GET_LABEL_DATA_BY_KEY_AND_LANG.getConstantVal());

			if (!CollectionUtils.isEmpty(gridFormKeyLabelList)) {
				for (GridFormKeyLabel gridFormKeyLabel : gridFormKeyLabelList) {
					labelKeyMap.put(gridFormKeyLabel.getGridFromKey(), gridFormKeyLabel.getGridFormKeyLable());
					ObjectCache.putLabelKeyValue(labelDto.getLanguageCode(), gridFormKeyLabel.getGridFromKey(), gridFormKeyLabel.getGridFormKeyLable());
				}
			}
			langCode = labelDto.getLanguageCode();
			labelDtoListSize = labelDto.getLabelKeyList().size();
			labelDto = new LabelDto();
			labelDto.setLanguageCode(langCode);
			labelDto.setLabelKeyMap(labelKeyMap);

		} catch (Exception e) {
			logger.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).build();
		}
		logger.info("request completed for processing id", jobProcessId);
		if (labelKeyMap.size() == labelDtoListSize) {
			return new ServiceResponseBuilder().setStatus(true).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(labelDto).build();
		} else {
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage(GeneralConstants.RECORDS_MISSING.getConstantVal()).setResponse(labelDto).build();
		}
	}
}
