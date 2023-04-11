package com.iris.pan.magmt.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.NSDLPanVerifDto;
import com.iris.dto.PanMasterNSDLRespDto;
import com.iris.dto.PanSupportingInfo;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.NSDLPanVerif;
import com.iris.model.PanMapping;
import com.iris.model.PanMaster;
import com.iris.pan.magmt.service.PanVeriService;
import com.iris.repository.PanMappingRepo;
import com.iris.repository.PanMasterRepo;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.NsdlPanVerfStatusEnum;
import com.iris.util.constant.PanMappingStatusEnum;

/**
 * @author pradnyam
 */
@RestController
@RequestMapping("/service/panMngtController")
public class PanMgmtController {
	private static final Logger Logger = LogManager.getLogger(PanMgmtController.class);

	@Autowired
	PanVeriService panVeriService;

	@Autowired
	PanMasterRepo panMasterRepo;

	@Autowired
	PanMappingRepo panMappingRepo;

	@GetMapping(value = "/processPANVerification/getPendingRecordsToVerify/{recordToBeProcessed}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getPendingRecordsForNsdlVerification(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable(name = "recordToBeProcessed") Integer recordToBeProcessed) {
		try {
			Logger.info("Request received to get getPendingRecordsForNsdlVerification : " + jobProcessId);
			List<NSDLPanVerif> nsdlPanVeriList = panVeriService.getNsdlPendingPanRecords(NsdlPanVerfStatusEnum.PEND_NSDL_VERIFY.getStaus(), recordToBeProcessed);
			Logger.info("PAN Verification Process done successfully:" + jobProcessId);
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.PENDING_NSDL_VERI_PAN_LIST_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(nsdlPanVeriList).build();
		} catch (Exception e) {
			Logger.error("Exception while processing PAN for NSDL verification for job processingid : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1293.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1293.toString())).build();
		}
	}

	@PostMapping(value = "/processPANVerification/updateVerificationStatusAndInsertPanMaster", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse nsdlVerificationProcess(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody List<PanMasterNSDLRespDto> panMasterNSDLRespDtoList) {

		List<PanMaster> panMasterTempListToInsertUpdate = new ArrayList<>();
		try {
			//			//-------------------Update pass status in the TBL_NSDL_PAN_VERF table for verified PAN
			List<PanMasterNSDLRespDto> verifiedPanMsterList = panMasterNSDLRespDtoList.stream().filter(e -> e.getStatus().equals(NsdlPanVerfStatusEnum.NSDL_PASS.getStaus())).collect(Collectors.toList());
			List<String> verifiedPanList = verifiedPanMsterList.stream().map(PanMasterNSDLRespDto::getPanNumber).collect(Collectors.toList());

			if (verifiedPanMsterList != null && !verifiedPanMsterList.isEmpty()) {
				//Fetching data from database if already exists
				List<PanMaster> panMasterListFromDB = panMasterRepo.getRecordsByPanNumList(verifiedPanList);

				//Insert Records into PAN master which are verified from NSDL API
				for (PanMasterNSDLRespDto panMasterNSDLRespDto : verifiedPanMsterList) {

					//Checking if record for ENTITY Id, Return Id , Reference End Date is already present into the db or not, 
					//if Present then update the json or else Insert the new row
					PanMaster panMasterDB = panMasterListFromDB.stream().filter(p -> p.getPanNumber().equalsIgnoreCase(panMasterNSDLRespDto.getPanNumber())).findAny().orElse(null);

					if (panMasterDB == null) {
						panMasterDB = new PanMaster();
					}
					panMasterDB.setPanNumber(panMasterNSDLRespDto.getPanNumber().toUpperCase());
					panMasterDB.setBorrowerName(panMasterNSDLRespDto.getBorrowerName());
					panMasterDB.setBorrowerTitle(panMasterNSDLRespDto.getBorrowerTitle());
					panMasterDB.setBorrowerMobile(panMasterNSDLRespDto.getBorrowerMobile());
					panMasterDB.setBorrowerAlternateName(panMasterNSDLRespDto.getBorrowerAlternateName());
					panMasterDB.setRbiGenerated(Boolean.FALSE);
					panMasterDB.setIsActive(Boolean.TRUE);
					panMasterTempListToInsertUpdate.add(panMasterDB);
				}
				panMasterRepo.saveAll(panMasterTempListToInsertUpdate);
			}

			for (PanMasterNSDLRespDto panMasterNSDLRespDto : panMasterNSDLRespDtoList) {
				try {
					panVeriService.updatePanRecordsBasedOnNsdlVerification(panMasterNSDLRespDto.getPanNumber(), panMasterNSDLRespDto.getStatus(), new Date(panMasterNSDLRespDto.getVerifiedOnLong()), panMasterNSDLRespDto.getNsdlResp());
				} catch (Exception e) {
					Logger.error("Exception while updating status in the TBL_NSDL_PAN_VERF : PAN no is:" + panMasterNSDLRespDto.getPanNumber() + " ,jobProcessId " + jobProcessId, e);

				}
			}

		} catch (Exception e) {
			Logger.error("Exception while processing PAN for NSDL verification for job processingid : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1293.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1293.toString())).build();
		}
		Logger.info("PAN Verification Process done successfully:" + jobProcessId);
		return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.NSDL_VERIFIED_PAN_INSERTED_SUCCESSFULLY.getConstantVal()).setResponse(panMasterTempListToInsertUpdate).build();
	}

	@PostMapping(value = "/mappingRBIPAN/updatePANStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse updatePANStatus(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody List<NSDLPanVerifDto> nsdlPanVerificationList) {

		List<String> allPanList = null;
		try {
			//			//-------------------Update pass status in the TBL_NSDL_PAN_VERF table for verified PAN

			if (nsdlPanVerificationList == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1295.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1295.toString())).build();
			}

			List<NSDLPanVerifDto> nsdlPassPanList = nsdlPanVerificationList.stream().filter(e -> e.getStatus().equals(NsdlPanVerfStatusEnum.NSDL_PASS.getStaus())).collect(Collectors.toList());
			List<NSDLPanVerifDto> nsdlFailPanList = nsdlPanVerificationList.stream().filter(e -> e.getStatus().equals(NsdlPanVerfStatusEnum.NSDL_FAIL.getStaus())).collect(Collectors.toList());

			for (int i = 0; i < nsdlPassPanList.size(); i++) {
				//PanSupportingInfo
				PanSupportingInfo panSupportingInfo = new Gson().fromJson(nsdlPassPanList.get(i).getSupportingInfo(), PanSupportingInfo.class);
				Long refId = panSupportingInfo.getRefId() != null || !panSupportingInfo.getRefId().isEmpty() ? Long.parseLong(panSupportingInfo.getRefId()) : 0L;
				PanMapping panMappingData = null;
				if (refId != 0) {
					panMappingData = panMappingRepo.getDataByPanMappingIdAndIsActiveTrue(refId);
				}

				if (panMappingData != null && refId != 0) {
					List<String> panList = new ArrayList<String>();
					panList.add(panMappingData.getActualPanNumber());
					List<PanMaster> panMasterList = panMasterRepo.getRecordsByPanNumList(panList);
					if (panMasterList != null && !panMasterList.isEmpty()) {
						if (panMasterList.get(0).getBorrowerName().equals(panMappingData.getBorrowerName())) {
							panMappingRepo.updateVerificationStatusOfActualPan(PanMappingStatusEnum.RBI_PENDING.getStatus(), panList);
						} else {
							panMappingRepo.updateVerificationStatusOfActualPan(PanMappingStatusEnum.NSDL_REJECTED.getStatus(), panList);
						}
					} else {
						panMappingRepo.updateVerificationStatusOfActualPan(PanMappingStatusEnum.NSDL_REJECTED.getStatus(), panList);
					}
				} else {
					List<String> panList = new ArrayList<String>();
					panList.add(nsdlPassPanList.get(i).getActualPanNumber());
					panMappingRepo.updateVerificationStatusOfActualPan(PanMappingStatusEnum.NSDL_REJECTED.getStatus(), panList);
				}
			}

			//updating the status NSDL_REJECTED in TBL_PAN_MAPPING
			if (nsdlFailPanList != null && !nsdlFailPanList.isEmpty()) {
				List<String> failedPanList = nsdlFailPanList.stream().map(NSDLPanVerifDto::getActualPanNumber).collect(Collectors.toList());
				panMappingRepo.updateVerificationStatusOfActualPan(PanMappingStatusEnum.NSDL_REJECTED.getStatus(), failedPanList);
			}

			if (nsdlPanVerificationList != null && !nsdlPanVerificationList.isEmpty()) {
				allPanList = nsdlPanVerificationList.stream().map(NSDLPanVerifDto::getActualPanNumber).collect(Collectors.toList());
				panVeriService.updateSubTaskStatus(allPanList, Boolean.TRUE);
				Logger.info("updated SubTaskStatus of " + allPanList.size() + " PANs :" + NsdlPanVerfStatusEnum.NSDL_VERIF_IN_PROCESS.getStaus() + " in the TBL_NSDL_PAN_VERF " + jobProcessId);
			}

		} catch (Exception e) {
			Logger.error("Exception while processing RBI PAN Mapping : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1292.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1292.toString())).build();
		}
		Logger.info("PAN Verification Process done successfully:" + jobProcessId);
		return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RBI_PAN_PROCESSED_SUCCESSFULLY.getConstantVal()).setResponse(GeneralConstants.RBI_PAN_PROCESSED_SUCCESSFULLY.getConstantVal()).build();
	}
}
