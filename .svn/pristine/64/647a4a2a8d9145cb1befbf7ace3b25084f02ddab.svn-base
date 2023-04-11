/**
 * 
 */
package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * @author Siddique H Khan
 *
 */

@RestController
@RequestMapping("/service/censusLocationMaster")
public class CensusLocationMasterController {

	@Autowired
	private EntityManager em;

	static final Logger logger = LogManager.getLogger(CensusLocationMasterController.class);

	@GetMapping(value = "/fetchCensusData/{censusParameter}")
	public ServiceResponse fetchCensusLocationData(@PathVariable("censusParameter") String pathVariable) {

		String censusStateCode = null;
		String censusDistrictCode = null;
		String censusSubdistrictCode = null;
		String censusVillageCode = null;

		ServiceResponse serviceResponse = null;

		Base64 base64 = new Base64();
		String censusParameter = new String(base64.decode(pathVariable.getBytes()));

		if (StringUtils.isBlank(censusParameter) && !censusParameter.contains(GeneralConstants.TILDA_SEPERATOR.getConstantVal()) && censusParameter.split(GeneralConstants.TILDA_SEPERATOR.getConstantVal()).length != 4) {
			return serviceResponse;
		}

		censusStateCode = censusParameter.split(GeneralConstants.TILDA_SEPERATOR.getConstantVal())[0];
		censusDistrictCode = censusParameter.split(GeneralConstants.TILDA_SEPERATOR.getConstantVal())[1];
		censusSubdistrictCode = censusParameter.split(GeneralConstants.TILDA_SEPERATOR.getConstantVal())[2];
		censusVillageCode = censusParameter.split(GeneralConstants.TILDA_SEPERATOR.getConstantVal())[3];

		logger.info("request received Census location data, censusStateCode : " + censusStateCode + " censusDistrictCode : " + censusDistrictCode + " censusSubdistrictCode : " + censusSubdistrictCode + " censusVillageCode : " + censusVillageCode);

		StringBuilder sb = new StringBuilder();

		sb.append(" select s.CENSUS_STATE_CODE, d.CENSUS_DISTRICT_CODE, c.CENSUS_SUBDISTRICT_CODE, c.CENSUS_VILLAGE_CODE ");
		sb.append(" FROM TBL_STATE_MASTER s, TBL_DISTRICT_MASTER d, TBL_CENSUS_LOCATION_MASTER c ");
		sb.append(" where c.STATE_MASTER_ID_FK = s.ID and c.DISTRICT_MASTER_ID_FK = d.ID ");

		if (StringUtils.isNotBlank(censusStateCode) && StringUtils.isNumeric(censusStateCode)) {
			if (Integer.parseInt(censusStateCode) > 0) {
				sb.append("and  s.CENSUS_STATE_CODE =").append("'" + censusStateCode + "'");
			}
		} else if (StringUtils.isNotBlank(censusStateCode)) {
			sb.append("and  s.CENSUS_STATE_CODE =").append("'" + censusStateCode + "'");
		}

		if (StringUtils.isNotBlank(censusDistrictCode) && StringUtils.isNumeric(censusDistrictCode)) {
			if (Integer.parseInt(censusDistrictCode) > 0) {
				sb.append("and  d.CENSUS_DISTRICT_CODE =").append("'" + censusDistrictCode + "'");
			}
		} else if (StringUtils.isNotBlank(censusDistrictCode)) {
			sb.append("and  d.CENSUS_DISTRICT_CODE =").append("'" + censusDistrictCode + "'");
		}

		if (StringUtils.isNotBlank(censusSubdistrictCode) && StringUtils.isNumeric(censusSubdistrictCode)) {
			if (Integer.parseInt(censusSubdistrictCode) > 0) {
				sb.append("and  c.CENSUS_SUBDISTRICT_CODE =").append("'" + censusSubdistrictCode + "'");
			}
		} else if (StringUtils.isNotBlank(censusSubdistrictCode)) {
			sb.append("and  c.CENSUS_SUBDISTRICT_CODE =").append("'" + censusSubdistrictCode + "'");
		}

		if (StringUtils.isNotBlank(censusVillageCode) && StringUtils.isNumeric(censusVillageCode)) {
			if (Integer.parseInt(censusVillageCode) > 0) {
				sb.append("and  c.CENSUS_VILLAGE_CODE =").append("'" + censusVillageCode + "'");
			}
		} else if (StringUtils.isNotBlank(censusVillageCode)) {
			sb.append("and  c.CENSUS_VILLAGE_CODE =").append("'" + censusVillageCode + "'");
		}

		try {
			List<String> responseList = new ArrayList<>();
			List<Tuple> result = em.createNativeQuery(sb.toString(), Tuple.class).getResultList();

			if (result.isEmpty()) {
				serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				serviceResponse.setResponse(responseList);
				return serviceResponse;
			}

			String censusStateCodesResponse = null;
			String censusDistrictCodeResponse = null;
			String censusSubdistrictCodeResponse = null;
			String censusVillageCodeResponse = null;

			for (Tuple tuple : result) {

				censusStateCodesResponse = (String) tuple.get("CENSUS_STATE_CODE");
				censusDistrictCodeResponse = (String) tuple.get("CENSUS_DISTRICT_CODE");
				censusSubdistrictCodeResponse = (String) tuple.get("CENSUS_SUBDISTRICT_CODE");
				censusVillageCodeResponse = (String) tuple.get("CENSUS_VILLAGE_CODE");
			}

			responseList.add(censusStateCodesResponse + GeneralConstants.TILDA_SEPERATOR.getConstantVal() + censusDistrictCodeResponse + GeneralConstants.TILDA_SEPERATOR.getConstantVal() + censusSubdistrictCodeResponse + GeneralConstants.TILDA_SEPERATOR.getConstantVal() + censusVillageCodeResponse);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(responseList);
			return serviceResponse;
		} catch (Exception e) {
			logger.error(ErrorCode.EC0033.toString(), e);
		}

		return null;
	}

}
