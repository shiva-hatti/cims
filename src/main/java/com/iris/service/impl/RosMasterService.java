package com.iris.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.RosMaster;
import com.iris.repository.RosMasterRepo;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Shivabasava Hatti
 *
 */
@Service
public class RosMasterService {
	private static final Logger logger = LogManager.getLogger(RosMasterService.class);
	@Autowired
	private RosMasterRepo rosMasterRepo;

	public List<RosMaster> findRosMasterDetailsByEntityCode(String returnCode, String entityCode) {
		logger.info("Call findRosMasterDetailsByEntityCode Service");
		List<RosMaster> rosMasterDetailsList = new ArrayList<>();
		try {
			rosMasterDetailsList = rosMasterRepo.findRosMasterDetailsByEntityCode(returnCode, entityCode);
		} catch (Exception e) {
			logger.error("Exception in RosMasterService Class : findRosMasterDetailsByEntityCode method " + e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return rosMasterDetailsList;
	}

	public List<RosMaster> findRosMasterDetailsByReturnCode(String returnCode) {
		logger.info("Call findRosMasterDetailsByReturnCode Service");
		List<RosMaster> rosMasterDetailsList = new ArrayList<>();
		try {
			rosMasterDetailsList = rosMasterRepo.findRosMasterDetailsByReturnCode(returnCode);
		} catch (Exception e) {
			logger.error("Exception in RosMasterService Class : findRosMasterDetailsByReturnCode method " + e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return rosMasterDetailsList;
	}
}
