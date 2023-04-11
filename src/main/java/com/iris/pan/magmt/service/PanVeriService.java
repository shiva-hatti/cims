package com.iris.pan.magmt.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.NSDLPanVerif;
import com.iris.nbfc.repository.NsdlPanVerificationRepo;
import com.iris.sdmx.ebrvalidation.bean.EbrFileDetails;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.util.constant.NsdlPanVerfStatusEnum;

/**
 * @author pradnyam
 */
@Service
public class PanVeriService implements GenericService<NSDLPanVerif, Long> {
	@Autowired
	NsdlPanVerificationRepo panVeriRepo;

	@Override
	public NSDLPanVerif add(NSDLPanVerif entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(NSDLPanVerif entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<NSDLPanVerif> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NSDLPanVerif getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NSDLPanVerif> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> status = null;
			if (methodName.equalsIgnoreCase(MethodConstants.GET_RECORDS_FOR_PAN_VERIFICATION_AND_UPDATE.getConstantVal())) {
				status = columnValueMap.get(ColumnConstants.STATUS.getConstantVal());
				//				List<NSDLPanVerif> panVeriDataList = panVeriRepo.fetchPanVeriData(status.get(0));
				List<NSDLPanVerif> panVeriDataList = panVeriRepo.fetchPanVeriData(status.get(0), PageRequest.of(0, 5));

				return panVeriDataList;
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<NSDLPanVerif> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NSDLPanVerif> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NSDLPanVerif> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NSDLPanVerif> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(NSDLPanVerif bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public int updatePanStatus(List<String> panNos, String statusToBeUpdated) throws ServiceException {
		if (panNos != null && !panNos.isEmpty()) {
			return panVeriRepo.updatePanVeriRecordStatus(panNos, statusToBeUpdated);
		} else {
			return 0;
		}
	}

	public int updatePanRecordsBasedOnNsdlVerification(String panNo, String statusToBeUpdated, Date verifiedOnDate, String nsdlRsponse) throws ServiceException {
		if (panNo != null && !panNo.isEmpty()) {
			return panVeriRepo.updatePanVeriRecord(panNo, statusToBeUpdated, verifiedOnDate, nsdlRsponse);
		} else {
			return 0;
		}
	}

	public int updateSubTaskStatus(List<String> panNos, boolean statusToBeUpdated) throws ServiceException {
		if (panNos != null && !panNos.isEmpty()) {
			return panVeriRepo.updateSubTaskStatus(panNos, statusToBeUpdated);
		} else {
			return 0;
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public List<NSDLPanVerif> getNsdlPendingPanRecords(String status, Integer noOfRecordsToBeGet) {
		List<NSDLPanVerif> panVeriDataList = panVeriRepo.fetchPanVeriData(status, PageRequest.of(0, noOfRecordsToBeGet));
		if (!CollectionUtils.isEmpty(panVeriDataList)) {
			for (NSDLPanVerif nsdlPanVerif : panVeriDataList) {
				nsdlPanVerif.setStatus(NsdlPanVerfStatusEnum.NSDL_VERIF_IN_PROCESS.getStaus());
				panVeriRepo.save(nsdlPanVerif);
			}
		} else {
			return null;
		}
		return panVeriDataList;
	}

}
