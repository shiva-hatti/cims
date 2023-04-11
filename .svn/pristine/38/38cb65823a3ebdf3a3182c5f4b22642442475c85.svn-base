package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.UploadChannel;
import com.iris.repository.ChannelRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

@Service
public class ChannelService implements GenericService<UploadChannel, Long> {

	@Autowired
	ChannelRepo channelRepo;

	@Override
	public UploadChannel add(UploadChannel entity) throws ServiceException {
		try {
			return channelRepo.save(entity);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public boolean update(UploadChannel entity) throws ServiceException {
		return false;
	}

	@Override
	public List<UploadChannel> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public List<UploadChannel> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<UploadChannel> getActiveDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean.equals(UploadChannel.class) && id == null) {
				return channelRepo.fetchAllActiveChannel();
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return null;
	}

	@Override
	public List<UploadChannel> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(UploadChannel bean) throws ServiceException {

	}

	@Override
	public List<UploadChannel> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UploadChannel getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UploadChannel> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
