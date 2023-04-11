/**
 * 
 */
package com.iris.sdmx.elementdimensionmapping.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.elementdimensionmapping.controller.Attachment;
import com.iris.sdmx.elementdimensionmapping.repo.AttachmentRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author sajadhav
 *
 */
@Service
public class AttachmentService implements GenericService<Attachment, Long> {

	@Autowired
	private AttachmentRepo attachmentRepo;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1458319347876850197L;

	@Override
	public Attachment add(Attachment entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(Attachment entity) throws ServiceException {
		return false;
	}

	@Override
	public List<Attachment> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public Attachment getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<Attachment> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<Attachment> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<Attachment> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<Attachment> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<Attachment> getAllDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean == null && id == null) {
				return attachmentRepo.findAll();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

	@Override
	public void deleteData(Attachment bean) throws ServiceException {

	}

}
