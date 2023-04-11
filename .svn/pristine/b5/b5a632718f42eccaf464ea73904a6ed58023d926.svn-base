package com.iris.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.DeptUserEntityMapping;
import com.iris.model.EntityBean;
import com.iris.model.FraudInfo;
import com.iris.model.FraudMaster;
import com.iris.model.FraudMasterDto;
import com.iris.model.JsonEncodeInputDto;
import com.iris.model.Return;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.RfaUpdateMaster;
import com.iris.model.UserMaster;
import com.iris.repository.DeptUserEntityMappingRepo;
import com.iris.repository.EntityRepo;
import com.iris.repository.FraudMasterRepo;
import com.iris.repository.ReturnRepo;
import com.iris.repository.RfaUpdateMasterRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.sdmx.status.entity.AdminStatus;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.FraudMasterEnum;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.ServiceConstants;

@RestController
@RequestMapping("/service/fraudMaster")
public class FraudMasterController {

	static final Logger logger = LogManager.getLogger(FraudMasterController.class);

	@Autowired
	private FraudMasterRepo fraudMasterRepo;

	@Autowired
	private EntityRepo entityRepo;

	@Autowired
	private ReturnRepo returnRepo;

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private DeptUserEntityMappingRepo deptUserEntityMappingRepo;

	@Autowired
	private RfaUpdateMasterRepo rfaUpdateMasterRepo;

	@PostMapping(value = "/getUserDeptEntMapping")
	public ServiceResponse getUserDeptEntMapping(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody FraudInfo fraudInfo) {
		logger.info("getUserDeptEntMapping method started " + fraudInfo.getUserId());
		try {
			List<DeptUserEntityMapping> deptUserEntityMappingList = deptUserEntityMappingRepo.getAllActiveDataByUserId(fraudInfo.getUserId());

			if (deptUserEntityMappingList.isEmpty()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}
			List<String> entCodeList = new ArrayList<>();
			for (DeptUserEntityMapping obj : deptUserEntityMappingList) {
				entCodeList.add(obj.getEntity().getEntityCode());
			}
			FraudInfo fraudInfoObj = new FraudInfo();
			fraudInfoObj.setEntCodeList(entCodeList);

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(fraudInfoObj)).build();

		} catch (Exception e) {
			logger.error("getUserDeptEntMapping method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/getInfoOnFraudCode")
	public ServiceResponse getInfoOnFraudCode(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody FraudInfo fraudInfo) {
		logger.info("getInfoOnFraudCode method started " + fraudInfo.getFraudCode());
		try {
			FraudMaster fraudMasterObj = new FraudMaster();
			UserMaster userMaster = userMasterRepo.findByUserId(fraudInfo.getUserId());

			if (userMaster.getRoleType().getRoleTypeId().equals(ServiceConstants.REGULATOR)) {
				List<DeptUserEntityMapping> deptUserEntityMappingList = deptUserEntityMappingRepo.getAllActiveDataByUserId(fraudInfo.getUserId());

				if (deptUserEntityMappingList.isEmpty()) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
				}
				List<Long> entityIds = new ArrayList<>();
				for (DeptUserEntityMapping obj : deptUserEntityMappingList) {
					entityIds.add(obj.getEntity().getEntityId());
				}
				fraudMasterObj = fraudMasterRepo.getInfoOnFraudCodeDept(fraudInfo.getFraudCode(), entityIds);
			} else if (userMaster.getRoleType().getRoleTypeId().equals(ServiceConstants.COMPANY)) {
				EntityBean entityBean = entityRepo.findByEntityCode(fraudInfo.getEntcode());

				fraudMasterObj = fraudMasterRepo.getInfoOnFraudCode(fraudInfo.getFraudCode(), entityBean.getEntityId());
			}

			if (fraudMasterObj == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			if (fraudMasterObj.getAdminStatusIdFk() != null) {
				if (fraudMasterObj.getAdminStatusIdFk().getAdminStatusId() == 4L) {
					if (fraudInfo.getActivityType().equalsIgnoreCase(FraudMasterEnum.DEACTIVATED.getConstantVal()) || fraudInfo.getActivityType().equalsIgnoreCase(FraudMasterEnum.HOLD.getConstantVal())) {
						if (fraudMasterObj.getActivityType().equalsIgnoreCase(FraudMasterEnum.CLOSED.getConstantVal())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1712.toString())).build();
						} else if (fraudMasterObj.getActivityType().equalsIgnoreCase(FraudMasterEnum.DEACTIVATED.getConstantVal())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1708.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1708.toString())).build();
						} else if (fraudMasterObj.getActivityType().equalsIgnoreCase(FraudMasterEnum.HOLD.getConstantVal())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1709.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1709.toString())).build();
						}
					} else if (fraudInfo.getActivityType().equalsIgnoreCase(FraudMasterEnum.CLOSED.getConstantVal())) {
						if (fraudMasterObj.getActivityType().equalsIgnoreCase(FraudMasterEnum.CLOSED.getConstantVal())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1712.toString())).build();
						} else if (fraudMasterObj.getActivityType().equalsIgnoreCase(FraudMasterEnum.DEACTIVATED.getConstantVal())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1708.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1708.toString())).build();
						}
					} else if (fraudInfo.getActivityType().equalsIgnoreCase(FraudMasterEnum.RESUME.getConstantVal())) {
						if (fraudMasterObj.getActivityType().equalsIgnoreCase(FraudMasterEnum.CLOSED.getConstantVal())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1712.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1712.toString())).build();
						} else if (fraudMasterObj.getActivityType().equalsIgnoreCase(FraudMasterEnum.DEACTIVATED.getConstantVal())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1708.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1708.toString())).build();
						} else if (fraudMasterObj.getActivityType().equalsIgnoreCase(FraudMasterEnum.RESUME.getConstantVal())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1710.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1710.toString())).build();
						}
					}
				} else if (fraudMasterObj.getAdminStatusIdFk().getAdminStatusId() == 1L) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0069.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0069.toString())).build();
				}
			} else {
				if (fraudInfo.getActivityType().equalsIgnoreCase(FraudMasterEnum.RESUME.getConstantVal())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1711.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1711.toString())).build();
				}
			}

			FraudInfo fraudInfoObj = new FraudInfo();
			fraudInfoObj.setFraudCode(fraudMasterObj.getFraudCode());
			fraudInfoObj.setJsonEncode(fraudMasterObj.getJsonEncode());
			fraudInfoObj.setFraudMasterId(fraudMasterObj.getFraudMasterId());

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(fraudInfoObj)).build();

		} catch (Exception e) {
			logger.error("getInfoOnFraudCode method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/saveFraudDeactivateClosure")
	public ServiceResponse saveFraudDeactivateClosure(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody FraudInfo fraudInfo) {
		logger.info("saveFraudDeactivateClosure method started " + fraudInfo.getFraudMasterId() + "_" + fraudInfo.getFraudCode());
		try {
			UserMaster userMaster = userMasterRepo.findByUserId(fraudInfo.getModifiedBy());

			FraudMaster fraudMasterObj = fraudMasterRepo.findByFraudMasterId(fraudInfo.getFraudMasterId());

			if (fraudMasterObj.getAdminStatusIdFk() != null) {
				if (fraudMasterObj.getAdminStatusIdFk().getAdminStatusId() == 1L) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0069.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0069.toString())).build();
				}
			}

			fraudMasterObj.setRemark(fraudInfo.getRemark());
			fraudMasterObj.setSupportingDocument(fraudInfo.getSupportingDocument());
			fraudMasterObj.setModifiedOn(new Date());
			fraudMasterObj.setModifiedByFk(userMaster);

			AdminStatus adminStatus = new AdminStatus();
			adminStatus.setAdminStatusId(fraudInfo.getAdminStatusId());
			fraudMasterObj.setAdminStatusIdFk(adminStatus);

			fraudMasterObj.setActivityType(fraudInfo.getActivityType());

			fraudMasterRepo.save(fraudMasterObj);

			return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0015.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0015.toString())).build();

		} catch (Exception e) {
			logger.error("saveFraudDeactivateClosure method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/fraudMasterApprovalData")
	public ServiceResponse fraudMasterApprovalData(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody FraudInfo fraudInfo) {
		logger.info("fraudMasterApprovalData method started " + fraudInfo.getUserId());
		List<FraudInfo> fraudInfoList = new ArrayList<>();
		try {
			UserMaster userMaster = userMasterRepo.findByUserId(fraudInfo.getUserId());

			List<DeptUserEntityMapping> deptUserEntityMappingList = deptUserEntityMappingRepo.getAllActiveDataByUserId(fraudInfo.getUserId());

			if (deptUserEntityMappingList.isEmpty()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			List<Long> entityIds = new ArrayList<>();
			for (DeptUserEntityMapping obj : deptUserEntityMappingList) {
				entityIds.add(obj.getEntity().getEntityId());
			}
			List<String> entActivities = new ArrayList<>();
			entActivities.add(FraudMasterEnum.CLOSED.getConstantVal());
			entActivities.add(FraudMasterEnum.DEACTIVATED.getConstantVal());

			List<FraudMaster> fraudMasterList = new ArrayList<>();
			fraudMasterList = fraudMasterRepo.getFraudApprovalList(userMaster.getUserId(), entityIds, entActivities);

			entActivities = new ArrayList<>();
			entActivities.add(FraudMasterEnum.HOLD.getConstantVal());
			entActivities.add(FraudMasterEnum.RESUME.getConstantVal());

			List<FraudMaster> fraudMasterHoldResumeList = new ArrayList<>();
			fraudMasterHoldResumeList = fraudMasterRepo.getFraudApprovalListHoldResume(userMaster.getUserId(), entityIds, userMaster.getDepartmentIdFk().getRegulatorId(), entActivities);
			fraudMasterList.addAll(fraudMasterHoldResumeList);

			FraudInfo fraudInfoObj;
			if (fraudMasterList.isEmpty()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			} else {
				for (FraudMaster obj : fraudMasterList) {
					fraudInfoObj = new FraudInfo();
					fraudInfoObj.setFraudMasterId(obj.getFraudMasterId());
					fraudInfoObj.setEntcode(obj.getEntityIdFk().getEntityCode());
					fraudInfoObj.setEntname(obj.getEntityIdFk().getEntityName());
					fraudInfoObj.setFraudCode(obj.getFraudCode());
					fraudInfoObj.setJsonEncode(obj.getJsonEncode());
					fraudInfoObj.setActivityType(obj.getActivityType());
					fraudInfoObj.setRemark(obj.getRemark());
					if (obj.getSupportingDocument() != null) {
						fraudInfoObj.setSupportingDocument(obj.getSupportingDocument());
					}
					fraudInfoObj.setModifiedBy(obj.getModifiedByFk().getUserId());
					fraudInfoObj.setModifiedOn(obj.getModifiedOn());
					fraudInfoObj.setModifiedByUserName(obj.getModifiedByFk().getUserName());
					if (obj.getModifiedByFk().getRoleType().getRoleTypeId().equals(ServiceConstants.REGULATOR)) {
						fraudInfoObj.setDeptUser(true);
					}
					fraudInfoList.add(fraudInfoObj);
				}
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(fraudInfoList)).build();

		} catch (Exception e) {
			logger.error("fraudMasterApprovalData method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/approveRejectFraudCode")
	public ServiceResponse approveRejectFraudCode(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody FraudInfo fraudInfo) {
		logger.info("approveRejectFraudCode method started " + fraudInfo.getFraudMasterId());
		try {
			UserMaster userMaster = userMasterRepo.findByUserId(fraudInfo.getReviewedBy());

			FraudMaster fraudMasterObj = fraudMasterRepo.findByFraudMasterId(fraudInfo.getFraudMasterId());

			if (fraudInfo.getComments() != null) {
				fraudMasterObj.setComments(fraudInfo.getComments());
			}
			fraudMasterObj.setReviewedOn(new Date());
			fraudMasterObj.setReviewedByFk(userMaster);

			AdminStatus adminStatus = new AdminStatus();
			adminStatus.setAdminStatusId(fraudInfo.getAdminStatusId());
			fraudMasterObj.setAdminStatusIdFk(adminStatus);

			fraudMasterRepo.save(fraudMasterObj);

			if (fraudInfo.getAdminStatusId() == 4L) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0064.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0064.toString())).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0093.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0093.toString())).build();
			}
		} catch (Exception e) {
			logger.error("approveRejectFraudCode method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/viewFraudRecords")
	public ServiceResponse viewFraudRecords(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody FraudInfo fraudInfo) {
		logger.info("viewFraudRecords method started " + fraudInfo.getUserId());
		List<FraudInfo> fraudInfoList = new ArrayList<>();
		try {
			UserMaster userMaster = userMasterRepo.findByUserId(fraudInfo.getUserId());

			List<FraudMaster> fraudMasterList = new ArrayList<>();
			List<Long> entityIds = new ArrayList<>();

			if (userMaster.getRoleType().getRoleTypeId().equals(ServiceConstants.REGULATOR)) {
				List<DeptUserEntityMapping> deptUserEntityMappingList = deptUserEntityMappingRepo.getAllActiveDataByUserId(fraudInfo.getUserId());

				if (deptUserEntityMappingList.isEmpty()) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
				}
				entityIds = new ArrayList<>();
				for (DeptUserEntityMapping obj : deptUserEntityMappingList) {
					entityIds.add(obj.getEntity().getEntityId());
				}
				fraudMasterList = fraudMasterRepo.getViewFraudList(entityIds, fraudInfo.getAdminStatusId());

			} else if (userMaster.getRoleType().getRoleTypeId().equals(ServiceConstants.COMPANY)) {
				EntityBean entityBean = entityRepo.findByEntityCode(fraudInfo.getEntcode());

				entityIds = new ArrayList<>();
				entityIds.add(entityBean.getEntityId());

				if (fraudInfo.getAdminStatusId() == 5L) {
					fraudMasterList = fraudMasterRepo.getViewFraudAllList(entityIds, GeneralConstants.SADP_IDENTIFIER.getConstantVal());
				} else {
					fraudMasterList = fraudMasterRepo.getViewFraudList(entityIds, fraudInfo.getAdminStatusId());
				}
			}
			FraudInfo fraudInfoObj;
			if (fraudMasterList.isEmpty()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			} else {
				for (FraudMaster obj : fraudMasterList) {
					fraudInfoObj = new FraudInfo();
					fraudInfoObj.setFraudMasterId(obj.getFraudMasterId());
					fraudInfoObj.setEntcode(obj.getEntityIdFk().getEntityCode());
					fraudInfoObj.setEntname(obj.getEntityIdFk().getEntityName());
					fraudInfoObj.setFraudCode(obj.getFraudCode());
					fraudInfoObj.setJsonEncode(obj.getJsonEncode());
					if (obj.getActivityType() != null) {
						fraudInfoObj.setActivityType(obj.getActivityType());
					}
					if (obj.getRemark() != null) {
						fraudInfoObj.setRemark(obj.getRemark());
					}
					if (obj.getSupportingDocument() != null) {
						fraudInfoObj.setSupportingDocument(obj.getSupportingDocument());
					}
					if (obj.getComments() != null) {
						fraudInfoObj.setComments(obj.getComments());
					}
					if (obj.getReturnsUploadDetails() != null) {
						fraudInfoObj.setUploadId(obj.getReturnsUploadDetails().getUploadId());
					}
					fraudInfoObj.setDataPopulatedHive(obj.getDataPopulatedHive());
					fraudInfoObj.setCreatedBy(obj.getCreatedByFk().getUserId());
					fraudInfoObj.setCreatedOn(obj.getCreatedOn());
					fraudInfoObj.setCreatedByUserName(obj.getCreatedByFk().getUserName());

					if (obj.getModifiedByFk() != null) {
						fraudInfoObj.setModifiedBy(obj.getModifiedByFk().getUserId());
						fraudInfoObj.setModifiedOn(obj.getModifiedOn());
						fraudInfoObj.setModifiedByUserName(obj.getModifiedByFk().getUserName());
						if (obj.getModifiedByFk().getRoleType().getRoleTypeId().equals(ServiceConstants.REGULATOR)) {
							fraudInfoObj.setDeptUser(true);
						}
					}
					if (obj.getReviewedByFk() != null) {
						fraudInfoObj.setReviewedBy(obj.getReviewedByFk().getUserId());
						fraudInfoObj.setReviewedOn(obj.getReviewedOn());
						fraudInfoObj.setReviewedByUserName(obj.getReviewedByFk().getUserName());
					}
					fraudInfoList.add(fraudInfoObj);
				}
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(fraudInfoList)).build();

		} catch (Exception e) {
			logger.error("viewFraudRecords method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/insertDataInFraudMaster")
	public ServiceResponse insertDataInFraudMaster(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody FraudInfo fraudInfo) {
		logger.info("insertDataInFraudMaster method started " + fraudInfo.getFraudCode());
		try {
			UserMaster userMaster = userMasterRepo.findByUserId(fraudInfo.getCreatedBy());

			FraudMaster fraudMasterObj = new FraudMaster();
			fraudMasterObj.setFraudCode(fraudInfo.getFraudCode());
			fraudMasterObj.setJsonEncode(fraudInfo.getJsonEncode());

			EntityBean entityBean = entityRepo.findByEntityCode(fraudInfo.getEntcode());
			fraudMasterObj.setEntityIdFk(entityBean);

			Return returnObj = returnRepo.findByReturnCode(fraudInfo.getRetcode());
			fraudMasterObj.setReturnIdFk(returnObj);

			fraudMasterObj.setCreatedOn(new Date());
			fraudMasterObj.setCreatedByFk(userMaster);
			fraudMasterObj.setSourceFlag(FraudMasterEnum.HIVE.getConstantVal());
			fraudMasterObj.setDataPopulatedHive("2");

			fraudMasterRepo.save(fraudMasterObj);

			FraudInfo fraudInfoObj = new FraudInfo();
			fraudInfoObj.setFraudCode(fraudMasterObj.getFraudCode());
			fraudInfoObj.setFraudMasterId(fraudMasterObj.getFraudMasterId());

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(fraudInfoObj)).build();

		} catch (Exception e) {
			logger.error("insertDataInFraudMaster method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/getFraudMasterByEncodedJson")
	public ServiceResponse getFraudMasterByJson(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody JsonEncodeInputDto jsonEncodeInputDto) {
		logger.info("getFraudMasterByJson method started " + new Gson().toJson(jsonEncodeInputDto.getAllEncodedJson()));
		try {

			if (jsonEncodeInputDto.getAllEncodedJson() == null || jsonEncodeInputDto.getAllEncodedJson().isEmpty()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}

			List<FraudMaster> fraudMasterList = fraudMasterRepo.findByJsonEncode(jsonEncodeInputDto.getAllEncodedJson(), jsonEncodeInputDto.getEntityId());

			if (fraudMasterList == null) {
				return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(new ArrayList<FraudMaster>())).build();
			}

			List<FraudMasterDto> fraudMasterOutputList = new ArrayList<>();
			for (FraudMaster fraudMaster : fraudMasterList) {
				FraudMasterDto fraudMasterDto = new FraudMasterDto();
				fraudMasterDto.setFraudCode(fraudMaster.getFraudCode());
				fraudMasterDto.setJsonEncode(fraudMaster.getJsonEncode());
				fraudMasterDto.setFraudMasterId(fraudMaster.getFraudMasterId());
				fraudMasterDto.setDataPopulatedHive(fraudMaster.getDataPopulatedHive());
				fraudMasterOutputList.add(fraudMasterDto);
			}
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(fraudMasterOutputList).build();

		} catch (Exception e) {
			logger.error("getFraudMasterByEncodedJson method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1298.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1298.toString())).build();
		}
	}

	@PostMapping(value = "/getMaxFraudCodeByIfscYearAndQaurter")
	public ServiceResponse getMaxFraudCode(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody List<String> fraudCodeLikeStr) {
		try {
			if (fraudCodeLikeStr == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1660.toString())).build();
			}
			String maxFraudCode = fraudMasterRepo.getMaxFraudCodeByLikeString(fraudCodeLikeStr.get(0));

			return new ServiceResponseBuilder().setStatus(true).setResponse(maxFraudCode).build();

		} catch (Exception e) {
			logger.error("getInfoOnFraudCode method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1297.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1297.toString())).build();
		}
	}

	@PostMapping(value = "/insertFraudMaster")
	public ServiceResponse insertFraudMaster(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody List<FraudMasterDto> fraudMasterDtoList) {
		try {
			if (fraudMasterDtoList == null || fraudMasterDtoList.isEmpty()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			List<FraudMaster> fraudMasterList = new ArrayList<>();
			for (FraudMasterDto fraudMasterDto : fraudMasterDtoList) {
				FraudMaster fraudMaster = new FraudMaster();
				fraudMaster.setFraudMasterId(fraudMasterDto.getFraudMasterId());
				fraudMaster.setFraudCode(fraudMasterDto.getFraudCode());
				fraudMaster.setJsonEncode(fraudMasterDto.getJsonEncode());

				Return returnObj = new Return();
				returnObj.setReturnId(fraudMasterDto.getReturnId());
				fraudMaster.setReturnIdFk(returnObj);

				EntityBean entityBean = new EntityBean();
				entityBean.setEntityId(fraudMasterDto.getEntityId());
				fraudMaster.setEntityIdFk(entityBean);

				UserMaster userMastr = new UserMaster();
				userMastr.setUserId(fraudMasterDto.getCreatedBy());
				fraudMaster.setCreatedByFk(userMastr);

				fraudMaster.setCreatedOn(new Date());
				fraudMaster.setDataPopulatedHive(fraudMasterDto.getDataPopulatedHive());

				ReturnsUploadDetails returnsUploadDetails = new ReturnsUploadDetails();
				returnsUploadDetails.setUploadId(fraudMasterDto.getUploadId());
				fraudMaster.setReturnsUploadDetails(returnsUploadDetails);
				fraudMaster.setSourceFlag("SADP");
				fraudMasterList.add(fraudMaster);
			}

			if (!fraudMasterList.isEmpty()) {
				fraudMasterRepo.saveAll(fraudMasterList);
			}

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(GeneralConstants.FRAUD_CODE_INSERTED_SUCCESSFULLY)).build();

		} catch (Exception e) {
			logger.error("getInfoOnFraudCode method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1296.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1296.toString())).build();
		}
	}

	@PostMapping(value = "/insertDataInRfaUpdateMaster")
	public ServiceResponse insertDataInRfaUpdateMaster(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody RfaBorrowerInfo rfaBorrowerInfo) {
		logger.info("insertDataInRfaUpdateMaster method started " + rfaBorrowerInfo.getBorrowerName());
		try {
			RfaUpdateMaster rfaUpdateMaster = new RfaUpdateMaster();

			EntityBean entityBean = entityRepo.findByEntityCode(rfaBorrowerInfo.getEntcode());
			rfaUpdateMaster.setEntityIdFk(entityBean);

			rfaUpdateMaster.setBorrowerId(rfaBorrowerInfo.getBorrowerId());
			rfaUpdateMaster.setBorrowerName(rfaBorrowerInfo.getBorrowerName());
			rfaUpdateMaster.setBorrowerPan(rfaBorrowerInfo.getBorrowerPan());

			Date date1 = null;
			String date1St = DateManip.formatDate(rfaBorrowerInfo.getDateOfRfaClassification(), "dd/MM/yyyy", "yyyy-MM-dd");
			date1 = DateManip.convertStringToDate(date1St, "yyyy-MM-dd");
			rfaUpdateMaster.setDateOfRfaClassification(date1);

			rfaUpdateMaster.setStatusOfRfa(rfaBorrowerInfo.getStatusOfRfa());

			Date date2 = null;
			String date2St = DateManip.formatDate(rfaBorrowerInfo.getDateOfFraudClassification(), "dd/MM/yyyy", "yyyy-MM-dd");
			date2 = DateManip.convertStringToDate(date2St, "yyyy-MM-dd");
			rfaUpdateMaster.setDateOfFraudClassificationRemoval(date2);

			rfaUpdateMaster.setJsonEncode(rfaBorrowerInfo.getJsonEncode());
			rfaUpdateMaster.setCreatedOn(new Date());

			UserMaster userMaster = userMasterRepo.findByUserId(rfaBorrowerInfo.getCreatedBy());
			rfaUpdateMaster.setCreatedByFk(userMaster);

			rfaUpdateMasterRepo.save(rfaUpdateMaster);

			return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0014.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0014.toString())).build();

		} catch (Exception e) {
			logger.error("insertDataInRfaUpdateMaster method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/checkBorrowerIdStatus")
	public ServiceResponse checkBorrowerIdStatus(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId) {
		logger.info("checkBorrowerIdStatus method started " + jobProcessingId);
		List<RfaUpdateMaster> rfaUpdateMasterList = new ArrayList<>();
		try {
			rfaUpdateMasterList = rfaUpdateMasterRepo.findAll();

			RfaBorrowerInfo rfaBorrowerInfoObj = new RfaBorrowerInfo();

			List<Long> borrowerIdLst = new ArrayList<>();
			for (RfaUpdateMaster obj : rfaUpdateMasterList) {
				borrowerIdLst.add(obj.getBorrowerId());
			}

			rfaBorrowerInfoObj.setBorrowerIdList(borrowerIdLst);

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(rfaBorrowerInfoObj)).build();

		} catch (Exception e) {
			logger.error("checkBorrowerIdStatus method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}
}