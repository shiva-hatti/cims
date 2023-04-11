/**
 * 
 */
package com.iris.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.UploadChannel;
import com.iris.service.impl.ChannelService;

/**
 * @author Siddique
 *
 */

@RestController
@RequestMapping("/service/uploadChennels")
public class UploadChannelMaster {
	static final Logger logger = LogManager.getLogger(UploadChannelMaster.class);

	@Autowired
	private ChannelService channelService;

	@GetMapping(value = "/getAllChannelList")
	public ServiceResponse getAllChannelList(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		logger.info("Request received to get channel list for processing id", jobProcessId);
		try {
			List<UploadChannel> uploadChannelList = channelService.getActiveDataFor(UploadChannel.class, null);

			if (CollectionUtils.isEmpty(uploadChannelList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage("There is no Active channels").build();
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(uploadChannelList).build();
		} catch (Exception e) {
			logger.error("Exception occoured while fatching data for processing id", jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Exception occoured while fatching data").build();
		}

	}

}
