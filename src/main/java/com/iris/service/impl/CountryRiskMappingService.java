package com.iris.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.CountryRiskMapping;
import com.iris.repository.CountryRiskMappingRepo;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Shivabasava Hatti
 *
 */
@Service
public class CountryRiskMappingService {
	@Autowired
	private CountryRiskMappingRepo countryRiskMappingRepo;

	public List<CountryRiskMapping> findCountryRiskMappingDetailsByReturnCode(String returnCode) {
		List<CountryRiskMapping> countryRiskMappingList = new ArrayList<>();
		try {
			countryRiskMappingList = countryRiskMappingRepo.findCountryRiskMappingDetailsByReturnCode(returnCode);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return countryRiskMappingList;
	}
}
