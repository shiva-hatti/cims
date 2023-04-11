/**
 * 
 */
package com.iris.sdmx.ebrvalidation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.iris.model.FilingStatus;
import com.iris.repository.FileDetailsRepo;
import com.iris.sdmx.ebrvalidation.bean.EbrFileDetails;
import com.iris.sdmx.ebrvalidation.bean.EbrFileDetailsInputRequest;
import com.iris.sdmx.ebrvalidation.bean.UpdateEbrFilingStatusInputRequest;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionStoredJson;
import com.iris.sdmx.elementdimensionmapping.entity.ElementDimension;
import com.iris.sdmx.status.service.SdmxFileActivityLogService;
import com.iris.sdmx.upload.bean.SdmxModelCodeLiteBean;
import com.iris.util.JsonUtility;

/**
 * @author sajadhav
 *
 */
@Service
public class EbrDocValidationService {

	static final Logger LOGGER = LogManager.getLogger(EbrDocValidationService.class);

	@Autowired
	private FileDetailsRepo fileDetailsRepo;

	@Autowired
	private SdmxFileActivityLogService activityDetailLogService;

	@Autowired
	private EntityManager entManger;

	@Transactional
	public List<EbrFileDetails> getEbrFileDetailsRecordByStatusWithTheUpdate(EbrFileDetailsInputRequest ebrFileDetailsInputRequest) {
		try {
			ebrFileDetailsInputRequest.getFileTypeList().replaceAll(String::toUpperCase);

			List<EbrFileDetails> ebrFileDetailsList = fileDetailsRepo.getEbrDocRecordsByStatusForUpdate(ebrFileDetailsInputRequest.getFilingStatusIdList(), ebrFileDetailsInputRequest.getFileTypeList(), PageRequest.of(0, ebrFileDetailsInputRequest.getTotalRecordCount()));

			if (!CollectionUtils.isEmpty(ebrFileDetailsList)) {

				if (ebrFileDetailsInputRequest.getFillingStatusIdToBeChanged() != null) {
					FilingStatus changedFilingStatus = new FilingStatus();
					changedFilingStatus.setFilingStatusId(ebrFileDetailsInputRequest.getFillingStatusIdToBeChanged());

					long[] fileDetailsId = ebrFileDetailsList.stream().mapToLong(f -> f.getFileDetailsId()).toArray();

					fileDetailsRepo.updateRecordStatus(fileDetailsId, changedFilingStatus);
				}

				return ebrFileDetailsList;
			}
			return Collections.emptyList();
		} catch (Exception e) {
			LOGGER.error("Exception in service class : ", e);
			throw e;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateEbrFilingValidationStatus(UpdateEbrFilingStatusInputRequest updateEbrFilingStatusInputRequest, String jobProcessingId) throws Exception {
		// Step1 insert into Activity log table
		activityDetailLogService.saveActivityDetailLog(updateEbrFilingStatusInputRequest.getSdmxActivityDetailLogRequest(), jobProcessingId, updateEbrFilingStatusInputRequest.getUserId());

		// step 2 update Filing status
		long fileDetailsIds[] = { updateEbrFilingStatusInputRequest.getFileDetailsId() };
		FilingStatus filingStatus = new FilingStatus();
		filingStatus.setFilingStatusId(updateEbrFilingStatusInputRequest.getFilingStatusId());
		fileDetailsRepo.updateRecordStatus(fileDetailsIds, filingStatus, updateEbrFilingStatusInputRequest.getVtlRequestId());
		fileDetailsRepo.updateElementRecordStatus(fileDetailsIds, filingStatus);
	}

	public List<ElementDimensionStoredJson> fetchElementDimenionMappingData(List<SdmxModelCodeLiteBean> sdmModelCodeLiteBeans, String jobProcessId) {

		StringBuilder str = new StringBuilder("select new com.iris.sdmx.elementdimensionmapping.entity.ElementDimension(ed.elementDimensionJson) from ElementDimension ed where ");
		int i = 0;

		for (SdmxModelCodeLiteBean sdmxModelCodeLiteBean : sdmModelCodeLiteBeans) {
			if (i > 0) {
				str.append("or");
			}
			str.append("(ed.element.dsdCode = '" + sdmxModelCodeLiteBean.getDsdCode() + "' and ed.element.elementVer = '" + sdmxModelCodeLiteBean.getDsdVersion() + "')");
			i++;
		}

		List<ElementDimension> elementDimensions = entManger.createQuery(str.toString()).getResultList();

		if (!CollectionUtils.isEmpty(elementDimensions)) {

			List<ElementDimensionStoredJson> elementDimensionStoredJsons = new ArrayList<>();

			for (ElementDimension elementDimension : elementDimensions) {
				elementDimensionStoredJsons.add(JsonUtility.getGsonObject().fromJson(elementDimension.getElementDimensionJson(), ElementDimensionStoredJson.class));
			}

			return elementDimensionStoredJsons;
		}

		return Collections.emptyList();

	}
}
