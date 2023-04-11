package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcNoteMessages;
import com.iris.nbfc.repository.NbfcNoteMessagesRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author pmohite
 */
@Service
public class NbfcNoteMessagesService implements GenericService<NbfcNoteMessages, Integer> {

	@Autowired
	NbfcNoteMessagesRepo nbfcNoteMessagesRepo;

	@Override
	public NbfcNoteMessages add(NbfcNoteMessages entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(NbfcNoteMessages entity) throws ServiceException {
		return false;
	}

	@Override
	public List<NbfcNoteMessages> getDataByIds(Integer[] ids) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public NbfcNoteMessages getDataById(Integer id) throws ServiceException {
		return null;
	}

	@Override
	public List<NbfcNoteMessages> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcNoteMessages> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<Long> pageId = null;
			if (methodName.equalsIgnoreCase(MethodConstants.GET_NBFC_NOTE_MESSAGES_BY_NBFC_PAGE_MASTER.getConstantVal())) {
				pageId = columnValueMap.get(ColumnConstants.FORM_PAGE_NO.getConstantVal());
				return nbfcNoteMessagesRepo.getNbfcNoteMessagesByNbfcPageMaster(pageId.get(0));
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<NbfcNoteMessages> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcNoteMessages> getActiveDataFor(Class bean, Integer id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcNoteMessages> getAllDataFor(Class bean, Integer id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcNoteMessages bean) throws ServiceException {
	}
}
