package com.iris.ebr.metadata.flow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.ebr.metadata.flow.constant.EbrMetadataFlowContstant;
import com.iris.ebr.metadata.flow.entity.EbrMetadataFlow;
import com.iris.ebr.metadata.flow.repo.EbrMetadataFlowRepo;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.rbrToEbr.entity.EbrRbrFlowMaster;
import com.iris.rbrToEbr.repo.EbrRbrFlowMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

@Service
public class EbrMetadataFlowService implements GenericService<EbrMetadataFlow, Long> {

	private static final Logger LOGGER = LogManager.getLogger(EbrMetadataFlowService.class);

	@Autowired
	EbrMetadataFlowRepo ebrMetadataFlowRepo;

	@Autowired
	EbrRbrFlowMasterRepo ebrRbrFlowMasterRepo;

	@Override
	public EbrMetadataFlow add(EbrMetadataFlow entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(EbrMetadataFlow entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<EbrMetadataFlow> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EbrMetadataFlow getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EbrMetadataFlow> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EbrMetadataFlow> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EbrMetadataFlow> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EbrMetadataFlow> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EbrMetadataFlow> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(EbrMetadataFlow bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public void ctlEntryForEbrMetadata(String requestModule) {
		try {
			LOGGER.debug("Control table Entry Start For Metadata : ");
			Date date = new Date();

			if (requestModule == null) {
				LOGGER.debug("Incoming Request is Null : ");
				return;
			}

			if (requestModule.equals(EbrMetadataFlowContstant.ELEMENT.getConstantVal()) || requestModule.equals(EbrMetadataFlowContstant.DIMENSION.getConstantVal())) {

				EbrMetadataFlow ebrMetadataFlow = null;
				List<EbrRbrFlowMaster> ebrRbrFlowMastersForEle = null;
				List<EbrRbrFlowMaster> ebrRbrFlowMastersForDim = null;
				List<EbrMetadataFlow> getDimEntryList = null;
				EbrRbrFlowMaster ebrRbrFlowMaster = new EbrRbrFlowMaster();

				if (requestModule.equals(EbrMetadataFlowContstant.ELEMENT.getConstantVal())) {
					ebrRbrFlowMastersForEle = new ArrayList<>();
					ebrRbrFlowMastersForEle = ebrRbrFlowMasterRepo.getDataByFlowIdd(EbrMetadataFlowContstant.FLOW_ID_FOR_ELEMENTS.getConstantIntVal());
				} else if (requestModule.equals(EbrMetadataFlowContstant.DIMENSION.getConstantVal())) {
					ebrRbrFlowMastersForDim = new ArrayList<>();
					ebrRbrFlowMastersForDim = ebrRbrFlowMasterRepo.getDataByFlowIdd(EbrMetadataFlowContstant.FLOW_ID_FOR_DIMENSIONS.getConstantIntVal());
				}

				if (requestModule.equals(EbrMetadataFlowContstant.ELEMENT.getConstantVal())) {
					LOGGER.debug("Control table Entry  For Elements : ");
					String newJobId = null;
					if (CollectionUtils.isEmpty(ebrRbrFlowMastersForEle)) {
						throw new ApplicationException("Control Table Entry", "Flow Master Not Found For Requested Module " + requestModule);
					}

					ebrRbrFlowMaster = ebrRbrFlowMastersForEle.get(0);
					List<EbrMetadataFlow> checkEntryPresent = ebrMetadataFlowRepo.checkEntryPresent(ebrRbrFlowMaster.getFlowId());
					if (!CollectionUtils.isEmpty(checkEntryPresent)) {
						if (checkEntryPresent.get(0).getStatus() == 0) {
							return;
						} else if (checkEntryPresent.get(0).getStatus() != 0) {
							String jobID = checkEntryPresent.get(0).getMetadataJobId();
							newJobId = jobIdLogic(jobID, EbrMetadataFlowContstant.ELEMENT.getConstantVal());
						}
					} else {
						newJobId = jobIdLogic(null, EbrMetadataFlowContstant.ELEMENT.getConstantVal());
					}

					ebrMetadataFlow = new EbrMetadataFlow();
					ebrMetadataFlow.setMetadataJobId(newJobId);
					ebrMetadataFlow.setMetadataSourceEntityName(EbrMetadataFlowContstant.VIEW_EBR_ELEMENTS.getConstantVal());
					ebrMetadataFlow.setTaskName(EbrMetadataFlowContstant.EBR_ELEMENTS_METADATA_EBR_FNL_REPO.getConstantVal());
					ebrMetadataFlow.setFlowIdfk(ebrRbrFlowMaster.getFlowId());

				} else if (requestModule.equals(EbrMetadataFlowContstant.DIMENSION.getConstantVal()) && CollectionUtils.isEmpty(getDimEntryList)) {
					LOGGER.debug("Control table Entry  For Dimensions : ");
					String newJobId = null;
					if (CollectionUtils.isEmpty(ebrRbrFlowMastersForDim)) {
						throw new ApplicationException("Control Table Entry", "Flow Master Not Found For Requested Module " + requestModule);
					}

					ebrRbrFlowMaster = ebrRbrFlowMastersForDim.get(0);
					List<EbrMetadataFlow> checkEntryPresent = ebrMetadataFlowRepo.checkEntryPresent(ebrRbrFlowMaster.getFlowId());
					if (!CollectionUtils.isEmpty(checkEntryPresent)) {
						if (checkEntryPresent.get(0).getStatus() == 0) {
							return;
						} else if (checkEntryPresent.get(0).getStatus() != 0) {
							String jobID = checkEntryPresent.get(0).getMetadataJobId();
							newJobId = jobIdLogic(jobID, EbrMetadataFlowContstant.DIMENSION.getConstantVal());
						}
					} else {
						newJobId = jobIdLogic(null, EbrMetadataFlowContstant.DIMENSION.getConstantVal());
					}

					ebrMetadataFlow = new EbrMetadataFlow();
					ebrMetadataFlow.setMetadataJobId(newJobId);
					ebrMetadataFlow.setMetadataSourceEntityName(EbrMetadataFlowContstant.VIEW_EBR_DIMENSIONS.getConstantVal());
					ebrMetadataFlow.setTaskName(EbrMetadataFlowContstant.EBR_ELEMENT_DIMENSION_METADATA_EBR_FNL_REPO.getConstantVal());
					ebrMetadataFlow.setFlowIdfk(ebrRbrFlowMaster.getFlowId());
				}

				if (ebrMetadataFlow != null) {
					ebrMetadataFlow.setFlowName(EbrMetadataFlowContstant.ON_GOING_REVERSE_CONV_METADATA.getConstantVal());
					ebrMetadataFlow.setSequence(EbrMetadataFlowContstant.SEQUENCE.getConstantIntVal());
					ebrMetadataFlow.setPriority(EbrMetadataFlowContstant.PRIORITY.getConstantIntVal());
					ebrMetadataFlow.setStatus(EbrMetadataFlowContstant.STATUS.getConstantIntVal());
					ebrMetadataFlow.setCreatedBy(EbrMetadataFlowContstant.SADP.getConstantVal());
					ebrMetadataFlow.setCreatedDate(date);
					ebrMetadataFlowRepo.save(ebrMetadataFlow);
					LOGGER.debug("Control table Metadata Entry Done ");
				}
			}

		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
	}

	public String jobIdLogic(String jobId, String Type) {
		String updatedJobId = null;
		if (jobId == null) {
			if (Type.equals(EbrMetadataFlowContstant.ELEMENT.getConstantVal())) {
				updatedJobId = EbrMetadataFlowContstant.VIEW_EBR_ELEMENTS.getConstantVal() + "_001";
			} else if (Type.equals(EbrMetadataFlowContstant.DIMENSION.getConstantVal())) {
				updatedJobId = EbrMetadataFlowContstant.VIEW_EBR_DIMENSIONS.getConstantVal() + "_001";
			}
		} else {
			String jobIdArray[] = jobId.split("_");
			int currentJobId = Integer.parseInt(jobIdArray[3]);
			int newJobId = currentJobId + 1;
			String applyingJobId = "";
			int length = String.valueOf(newJobId).length();
			if (length == 1) {
				applyingJobId = "00" + newJobId;
			} else if (length == 2) {
				applyingJobId = "0" + newJobId;
			} else if (length == 3 || length > 3) {
				applyingJobId = newJobId + "";
			}

			if (Type.equals(EbrMetadataFlowContstant.ELEMENT.getConstantVal())) {
				updatedJobId = EbrMetadataFlowContstant.VIEW_EBR_ELEMENTS.getConstantVal() + "_" + applyingJobId;
			} else if (Type.equals(EbrMetadataFlowContstant.DIMENSION.getConstantVal())) {
				updatedJobId = EbrMetadataFlowContstant.VIEW_EBR_DIMENSIONS.getConstantVal() + "_" + applyingJobId;
			}
		}
		return updatedJobId;
	}

}
