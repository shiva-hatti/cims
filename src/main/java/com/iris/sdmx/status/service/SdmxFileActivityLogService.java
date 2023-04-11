/**
 * 
 */
package com.iris.sdmx.status.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.sdmx.status.bean.SdmxActivityDetailLogRequest;
import com.iris.sdmx.status.bean.SdmxFileActivityLogBean;
import com.iris.sdmx.status.entity.SdmxFileActivityLog;
import com.iris.sdmx.status.repo.SdmxFileActivityLogRepo;
import com.iris.sdmx.upload.helper.SdmxFileActivityLogHelper;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = false)
public class SdmxFileActivityLogService implements GenericService<SdmxFileActivityLog, Long> {

	@Autowired
	private SdmxFileActivityLogRepo sdmxActivityDetailLogRepo;

	@Autowired
	private SdmxFileActivityLogHelper sdmxActivityDetailLogHelper;

	@Override
	@Transactional(readOnly = false)
	public SdmxFileActivityLog add(SdmxFileActivityLog entity) throws ServiceException {
		return sdmxActivityDetailLogRepo.save(entity);
	}

	@Transactional(readOnly = false)
	public List<SdmxFileActivityLog> addAll(List<SdmxFileActivityLog> sdmxFileActivityLogList) throws ServiceException {
		return sdmxActivityDetailLogRepo.saveAll(sdmxFileActivityLogList);
	}

	/**
	 * @param sdmxActivityDetailLogRequest
	 * @param jobProcessingId
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = false)
	public SdmxFileActivityLogBean saveActivityDetailLog(SdmxActivityDetailLogRequest sdmxActivityDetailLogRequest, String jobProcessingId, Long userId) {
		SdmxFileActivityLog sdmxFileActivityLog = new SdmxFileActivityLog();
		sdmxActivityDetailLogHelper.convertRequestToEntity(sdmxActivityDetailLogRequest, sdmxFileActivityLog, userId, jobProcessingId);
		sdmxFileActivityLog.setJobProcessingId(jobProcessingId);
		sdmxFileActivityLog = add(sdmxFileActivityLog);
		SdmxFileActivityLogBean sdmxActivityDetailLogBean = new SdmxFileActivityLogBean();
		sdmxActivityDetailLogHelper.convetEntityToBean(sdmxFileActivityLog, userId, jobProcessingId, sdmxActivityDetailLogBean);
		return sdmxActivityDetailLogBean;
	}

	public List<SdmxFileActivityLogBean> saveActivityDetailLogList(List<SdmxActivityDetailLogRequest> sdmxActivityDetailLogRequestList, String jobProcessingId) {
		List<SdmxFileActivityLog> sdmxFileActivityLogList = sdmxActivityDetailLogHelper.convertRequestToList(sdmxActivityDetailLogRequestList, jobProcessingId);
		sdmxFileActivityLogList = addAll(sdmxFileActivityLogList);
		return sdmxActivityDetailLogHelper.convetActivityLogListToBeanList(sdmxFileActivityLogList, jobProcessingId);
	}

	@Override
	public boolean update(SdmxFileActivityLog entity) throws ServiceException {
		sdmxActivityDetailLogRepo.save(entity);
		return false;
	}

	@Override
	public List<SdmxFileActivityLog> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxFileActivityLog getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxFileActivityLog> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxFileActivityLog> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxFileActivityLog> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxFileActivityLog> getActiveDataFor(Class bean, Long id) throws ServiceException {

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxFileActivityLog> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxFileActivityLog bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public SdmxFileActivityLog getDataByFileDetailsIdAndProcessCode(Long fileDetailsId, String processCode) {
		return sdmxActivityDetailLogRepo.fetchSdmxFileActivity(fileDetailsId, processCode);
	}

}
