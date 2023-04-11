package com.iris.sdmx.dataType.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.RoleType;
import com.iris.model.UserMaster;
import com.iris.sdmx.dataType.FieldDataType;
import com.iris.sdmx.dataType.dto.FieldDataTypeDto;
import com.iris.sdmx.dataType.service.DataTypeService;
import com.iris.sdmx.dimesnsion.bean.DimensionRequestBean;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * @author sdhone
 *
 */

@RestController
@RequestMapping(value = "/service/fieldDataTypes")
public class DataTypeController {

	private static final Logger LOGGER = LogManager.getLogger(DataTypeController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private DataTypeService dataTypeService;

	@PostMapping(value = "/getAllFieldDataTypes")
	public ServiceResponse getAllDimensionType(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DimensionRequestBean dimensionRequestBean) {
		LOGGER.info("Request received to get code list master data for job processing Id : " + jobProcessId);
		try {

			UserMaster userMaster = userMasterService.getDataById(dimensionRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				//For Entity User 
				// return with error response;
				//return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			List<FieldDataType> fieldDataTypes = dataTypeService.getAllDataFor(null, null);
			List<FieldDataTypeDto> fieldDataTypeDtos = new ArrayList<>();
			for (FieldDataType fieldDataType : fieldDataTypes) {
				FieldDataTypeDto fieldDataTypeDto = new FieldDataTypeDto();
				fieldDataTypeDto.setDataTypeId(fieldDataType.getDataTypeId());
				fieldDataTypeDto.setDataTypeName(fieldDataType.getDataTypeName());
				fieldDataTypeDtos.add(fieldDataTypeDto);
			}

			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(fieldDataTypeDtos).build();

		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
		}

		return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ErrorCode.E0808.toString()).setStatusCode(ObjectCache.getErrorCodeKey(ErrorCode.E0808.toString())).build();
	}

}
