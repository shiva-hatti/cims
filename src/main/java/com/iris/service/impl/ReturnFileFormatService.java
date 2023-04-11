/**
 * 
 */
package com.iris.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ReturnFileFormatMap;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;

/**
 * @author pmohite
 *
 */

@Service
public class ReturnFileFormatService implements GenericService<ReturnFileFormatMap, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnFileFormatService.class);

	@Autowired
	DataSource datasource;

	@Override
	public ReturnFileFormatMap add(ReturnFileFormatMap returnFileFormatMap) throws ServiceException {
		try (Connection con = datasource.getConnection(); CallableStatement stmt = con.prepareCall(GeneralConstants.SP_RETURN_FILE_FORMAT_MAP.getConstantVal());) {
			stmt.setLong(1, returnFileFormatMap.getReturnBean().getReturnId());
			stmt.setString(2, returnFileFormatMap.getFileFormatIdString());
			stmt.registerOutParameter(3, Types.INTEGER);
			stmt.executeQuery();
			int number = stmt.getInt(3);
			if (number > 0) {
				returnFileFormatMap = null;
			} else {
				LOGGER.debug("SP_RETURN_FILE_FORMAT_MAP Procedure executed successfully.");
			}
		} catch (SQLException e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return returnFileFormatMap;
	}

	@Override
	public boolean update(ReturnFileFormatMap entity) throws ServiceException {
		return false;
	}

	@Override
	public List<ReturnFileFormatMap> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ReturnFileFormatMap getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnFileFormatMap> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnFileFormatMap> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnFileFormatMap> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReturnFileFormatMap> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReturnFileFormatMap> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ReturnFileFormatMap bean) throws ServiceException {
	}

	public List<ReturnFileFormatMap> findByReturnIdFkReturnIdAndIsActiveTrue(Long returnId) {
		return null;
	}

}
