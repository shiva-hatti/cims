/**
 * 
 */
package com.iris.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.FilingCalendarModificationHistory;
import com.iris.repository.FilingCalModHistoryRepository;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Shivabasava Hatti
 *
 */
@Service
public class FilingCalModHistoryService {

	@Autowired
	private FilingCalModHistoryRepository filingCalModHistoryRepo;

	public boolean saveFilingCalendarModificationHistory(FilingCalendarModificationHistory filingCalendarModificationHistory) {
		try {
			filingCalModHistoryRepo.save(filingCalendarModificationHistory);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return true;
	}

	public List<FilingCalendarModificationHistory> getFilingCalModHistoryData(List<Integer> filingCalIdList) {
		try {
			return filingCalModHistoryRepo.getFilingCalModHistoryData(filingCalIdList);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}
}
