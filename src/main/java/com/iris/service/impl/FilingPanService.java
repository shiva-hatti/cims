package com.iris.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.dto.FilingPanDto;
import com.iris.dto.FilingPanNSDL;
import com.iris.dto.PanSupportingInfo;
import com.iris.exception.ServiceException;
import com.iris.model.FilingPan;
import com.iris.model.FilingStatus;
import com.iris.model.NSDLPanVerif;
import com.iris.model.ReturnsUploadDetails;
import com.iris.nbfc.repository.NsdlPanVerificationRepo;
import com.iris.repository.FilingPanRepo;
import com.iris.repository.ReturnUploadDetailsRepository;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.constant.FilingPanStatusEnum;
import com.iris.util.constant.FilingStatusConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.NsdlPanVerfStatusEnum;
import com.iris.util.constant.PanModuleEnum;

@Service
public class FilingPanService implements GenericService<FilingPan, Long> {

	@Autowired
	private FilingPanRepo filingPanRepo;

	@Autowired
	private NsdlPanVerificationRepo nsdlPanVerificationRepo;

	@Autowired
	private ReturnUploadDetailsRepository returnUploadDetailsRepository;

	@Override
	public FilingPan add(FilingPan entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(FilingPan entity) throws ServiceException {
		return false;
	}

	@Override
	public List<FilingPan> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public FilingPan getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<FilingPan> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<FilingPan> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<FilingPan> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<FilingPan> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<FilingPan> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(FilingPan bean) throws ServiceException {

	}

	@Transactional(rollbackFor = Exception.class)
	public boolean insertRecordIntoFilingQueue(FilingPanDto filingPanDto) {

		FilingPan filingPan = filingPanRepo.findByCompanyPanAndReturnsUploadDetailsUploadId(filingPanDto.getPanNo(), filingPanDto.getUploadId());

		if (filingPan == null) {
			filingPan = new FilingPan();
			ReturnsUploadDetails returnsUploadDetails = new ReturnsUploadDetails();
			returnsUploadDetails.setUploadId(filingPanDto.getUploadId());
			filingPan.setReturnsUploadDetails(returnsUploadDetails);

			filingPan.setCompanyPan(filingPanDto.getPanNo());
			filingPan.setCreatedOn(new Date());
			filingPan.setIsActive(true);
			filingPan.setStatus(FilingPanStatusEnum.NSDL_PENDING.getStatus());
			filingPanRepo.save(filingPan);

			NSDLPanVerif nsdlPanVerif = new NSDLPanVerif();
			nsdlPanVerif.setActualPanNumber(filingPanDto.getPanNo());
			nsdlPanVerif.setStatus(NsdlPanVerfStatusEnum.PEND_NSDL_VERIFY.getStaus());
			PanSupportingInfo panSupportingInfo = new PanSupportingInfo();
			panSupportingInfo.setRefId(filingPan.getFilingPanId() + "");
			panSupportingInfo.setUploadId(filingPanDto.getUploadId());
			nsdlPanVerif.setSupportingInfo(JsonUtility.getGsonObject().toJson(panSupportingInfo));
			nsdlPanVerif.setModuleName(PanModuleEnum.FILING.getModuleCode());
			nsdlPanVerif.setIsActive(true);
			nsdlPanVerif.setCreatedOn(new Date());
			nsdlPanVerif.setSubTaskStatus(false);
			nsdlPanVerificationRepo.save(nsdlPanVerif);
			return true;
		}
		return false;
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateFilingNSDLPanStatus(List<FilingPanNSDL> filingPanNSDLList) {
		Set<Long> uploadIds = new HashSet<>();

		for (FilingPanNSDL filingPanNSDL : filingPanNSDLList) {
			filingPanRepo.updateFilingStatusById(filingPanNSDL.getFilingPanId(), filingPanNSDL.getStatus());
			nsdlPanVerificationRepo.updateNSDLSubTask(filingPanNSDL.getNsdlPanVerifId());
			uploadIds.add(filingPanNSDL.getUploadId());
		}

		for (Long uploadId : uploadIds) {
			List<FilingPan> filingPanList = filingPanRepo.getDataByStatusAndUploadId(uploadId, FilingPanStatusEnum.NSDL_PENDING.getStatus());

			if (CollectionUtils.isEmpty(filingPanList)) {
				long uploadIdArr[] = new long[1];
				uploadIdArr[0] = uploadId;
				FilingStatus filingStatus = new FilingStatus();
				filingStatus.setFilingStatusId(FilingStatusConstants.PAN_PROCESS_COMPLETED.getConstantIntVal());
				returnUploadDetailsRepository.updateReturnUploadDetailsRecordStatus(uploadIdArr, filingStatus, new Date());
			}
		}
	}
}
