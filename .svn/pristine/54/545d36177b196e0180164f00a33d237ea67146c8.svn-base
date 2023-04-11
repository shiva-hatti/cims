/**
 * 
 */
package com.iris.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.model.IsinMaster;
import com.iris.repository.IsinMasterRepository;

/**
 * @author Siddique
 *
 */

@RestController
@RequestMapping("/service/isinMaster")
public class IsinMasterController {

	static final Logger logger = LogManager.getLogger(IsinMasterController.class);

	@Autowired
	private IsinMasterRepository isinMasterRepository;

	@GetMapping(value = "/fetchIsinMasterData")
	public ServiceResponse fetchIsinMasterData() {

		logger.info("fetch fetchIsinMasterData method started ");

		ServiceResponse serviceResponse = null;

		List<IsinMaster> listOfIsinMaster = null;

		listOfIsinMaster = isinMasterRepository.fetchAllRecords();

		Set<String> isinCodeSet = listOfIsinMaster.stream().map(IsinMaster::getIsin).collect(Collectors.toSet());
		logger.info("fetch isinMaster  size" + isinCodeSet.size());
		serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		serviceResponse.setResponse(isinCodeSet);
		return serviceResponse;

	}

}
