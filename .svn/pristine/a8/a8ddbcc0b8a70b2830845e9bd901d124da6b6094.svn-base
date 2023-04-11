package com.iris.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.TaxonomyDTO;
import com.iris.model.XbrlTaxonomy;
import com.iris.repository.XbrlTaxonomyRepo;

@RestController
@RequestMapping("/xbrlTaxonomy/downloadXbrlTaxonomy")
public class DownloadXbrlTaxonomy {
	static final Logger logger = LogManager.getLogger(DownloadXbrlTaxonomy.class);
	@Autowired
	private XbrlTaxonomyRepo xbrlTaxonomyRepo;

	@GetMapping(value = "/fetchLatestXbrlTxonomy")
	public ServiceResponse fetchLatestXbrlTxonomy(@RequestHeader("JobProcessingId") String jobProcessId) {
		try {
			XbrlTaxonomy xbrlTaxonomy = xbrlTaxonomyRepo.findByLatestTaxonomyByValidFromDate();
			TaxonomyDTO taxonomyDTO = new TaxonomyDTO();
			if (xbrlTaxonomy != null) {
				//taxonomyDTO.setTaxId(xbrlTaxonomy.getXbrltaxonomyId());
				taxonomyDTO.setValidFromDate(DateManip.convertDateToString(xbrlTaxonomy.getValidFromDate(), ObjectCache.getDateFormat()));
				taxonomyDTO.setVersionDesc(xbrlTaxonomy.getVersionDesc());
				taxonomyDTO.setVersionNum(xbrlTaxonomy.getVersionNumber());
				taxonomyDTO.setEntryPointName(xbrlTaxonomy.getFileName());//For FileName
				taxonomyDTO.setReturnReturnTypeId(xbrlTaxonomy.getCreatedOn().getTime());
				if (xbrlTaxonomy.getActive()) {
					taxonomyDTO.setIsActive(1);
				} else {
					taxonomyDTO.setIsActive(0);
				}
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(taxonomyDTO)).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getMessage()).setStatusMessage(e.getMessage()).build();
		}
	}
}
