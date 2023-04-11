/**
 * 
 */
package com.iris.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.LeadBankMaster;
import com.iris.repository.LeadBankMasterRepo;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Shivabasava Hatti
 *
 */
@Service
public class LeadBankMasterService {

	@Autowired
	LeadBankMasterRepo leadBankMasterRepo;

	public List<LeadBankMaster> findLeadBankDetailsByReturnCode() {
		List<LeadBankMaster> leadBankMasterList = new ArrayList<>();
		try {
			leadBankMasterList = leadBankMasterRepo.findLeadBankDetailsByReturnCode();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return leadBankMasterList;
	}

}
