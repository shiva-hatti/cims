package com.iris.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.CompanyDto;
import com.iris.dto.GroupMasterBulkUploadDto;
import com.iris.dto.GroupMasterDetailsDto;
import com.iris.dto.GroupMasterTempDto;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.CountryMaster;
import com.iris.model.EntityBean;
import com.iris.model.GroupCompany;
import com.iris.model.GroupMaster;
import com.iris.model.GroupMasterBulk;
import com.iris.model.GroupMasterDetails;
import com.iris.model.GroupMasterTemp;
import com.iris.model.PanStatus;
import com.iris.model.UserMaster;
import com.iris.repository.EntityRepo;
import com.iris.repository.GroupCompanyRepo;
import com.iris.repository.GroupMasterDetailsRepo;
import com.iris.repository.GroupMasterRepo;
import com.iris.repository.GroupMasterTempRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.service.impl.GroupMasterBulkService;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

@RestController
@RequestMapping("/service/groupMaster")
public class GroupMasterController {

	private static final Logger logger = LogManager.getLogger(GroupMasterController.class);

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EntityRepo entityRepo;

	@Autowired
	private GroupMasterTempRepo groupMasterTempRepo;

	@Autowired
	private GroupCompanyRepo groupCompanyRepo;

	@Autowired
	private GroupMasterBulkService groupMasterBulkService;

	@Autowired
	private GroupMasterDetailsRepo groupMasterDetailsRepo;

	@Autowired
	private GroupMasterRepo groupMasterRepo;

	@PostMapping(value = "/addGroup")
	public ServiceResponse insertGroupdetails(@RequestBody GroupMasterTempDto groupMasterTempDto) {

		if (groupMasterTempDto == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
		}

		if (groupMasterTempDto.getEntryType() == null || groupMasterTempDto.getEntryType().intValue() != 1 && groupMasterTempDto.getEntryType() != 2) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0842.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0842.toString())).build();
		}

		if (groupMasterTempDto.getEntryType() == 1) { // This is ADD condition
			if (Validations.isEmpty(groupMasterTempDto.getGroupName())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0847.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0847.toString())).build();
			}

			if (!groupMasterTempDto.getGroupName().toUpperCase().endsWith("GROUP")) {
				String groupName = groupMasterTempDto.getGroupName();
				groupName = groupName + " " + "Group";
				groupMasterTempDto.setGroupName(groupName);
				//System.out.println(groupName);
			}
		}

		if (Validations.isEmpty(groupMasterTempDto.getCompanyNames())) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0843.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0843.toString())).build();
		}

		if (groupMasterTempDto.getMobileNumber() != null && groupMasterTempDto.getMobileNumber().longValue() < 1000000000l || groupMasterTempDto.getMobileNumber().longValue() > 9999999999l) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0844.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0844.toString())).build();
		}

		EntityBean entityBean = null;
		UserMaster userMaster = null;
		if (groupMasterTempDto.getIsBulkUpload().equals(Boolean.TRUE)) {
			entityBean = groupMasterTempDto.getEntityBean();
			userMaster = groupMasterTempDto.getUserMaster();
		} else {
			if (Validations.isEmpty(groupMasterTempDto.getEntityCode())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}
			entityBean = entityRepo.findByEntityCode(groupMasterTempDto.getEntityCode());
			userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(new Long(groupMasterTempDto.getCreatedBy()));
		}

		if (entityBean == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
		}

		if (userMaster == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0594.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0594.toString())).build();
		}

		GroupMasterTemp groupMasterObjDb = null;
		if (groupMasterTempDto.getIsBulkUpload().equals(Boolean.FALSE)) {
			if (groupMasterTempDto.getEntryType() == 1) { // ADD case
				GroupMasterTemp groupMasterObj = groupMasterTempRepo.findTopByGroupNameOrderByCreatedOnDesc(groupMasterTempDto.getGroupName());

				if (groupMasterObj != null) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0845.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0845.toString())).build();
				}
			} else { // Update Case

				if (Validations.isEmpty(groupMasterTempDto.getGroupCode())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0846.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0846.toString())).build();
				}

				groupMasterObjDb = groupMasterTempRepo.findTopByGroupCodeOrderByCreatedOnDesc(groupMasterTempDto.getGroupCode());

				if (groupMasterObjDb == null) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0847.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0847.toString())).build();
				}

				if (groupMasterObjDb.getVerificationStatus() == 0) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0848.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0848.toString())).build();
				}
			}
		}

		String[] cmpSplitArr = groupMasterTempDto.getCompanyNames().split(";");

		List<String> companyList = new ArrayList<>();
		for (String s : cmpSplitArr) {
			if (!Validations.isEmpty(s) && !companyList.contains(s)) {
				companyList.add(s);
			}
		}

		List<GroupCompany> companyDbList = groupCompanyRepo.findByCompanyNameInIgnoreCase(companyList);

		// *
		GroupCompany comp;
		if (companyDbList != null) {

			Map<String, Long> companyMap = companyDbList.stream().collect(Collectors.toMap(GroupCompany::getCompanyName, GroupCompany::getCompanyId));

			Map<String, Long> companyMapUpper = companyMap.keySet().stream().collect(Collectors.toMap(key -> key.toUpperCase(), key -> companyMap.get(key)));

			for (String companyNameObj : companyList) {

				if (!companyMapUpper.containsKey(companyNameObj.toUpperCase())) {
					comp = new GroupCompany();
					comp.setCompanyName(companyNameObj);

					groupCompanyRepo.saveAndFlush(comp);
				}
			}
		} else {
			for (String companyNameObj : companyList) {

				comp = new GroupCompany();
				comp.setCompanyName(companyNameObj);

				groupCompanyRepo.saveAndFlush(comp);
			}
		}

		companyDbList = groupCompanyRepo.findByCompanyNameInIgnoreCase(companyList);
		Map<String, Long> companyMap = companyDbList.stream().collect(Collectors.toMap(GroupCompany::getCompanyName, GroupCompany::getCompanyId));

		Map<String, Long> companyMapUpper = companyMap.keySet().stream().collect(Collectors.toMap(key -> key.toUpperCase(), key -> companyMap.get(key)));

		String runningNum = "0001";

		String groupNameChar = "" + groupMasterTempDto.getGroupName().toUpperCase().charAt(0);
		for (int i = 0; i < groupMasterTempDto.getGroupName().length(); i++) {
			if ((groupMasterTempDto.getGroupName().charAt(i) + "").matches("[A-Za-z]")) {
				groupNameChar = (groupMasterTempDto.getGroupName().charAt(i) + "").toUpperCase();
				break;
			}
		}

		if (groupMasterTempDto.getEntryType() == 1) {
			synchronized (this) {
				String groupGeneratedCode = groupNameChar + "%";
				List<GroupMasterTemp> groupMasterDobjList = groupMasterTempRepo.findByGroupCodeOrderByGroupIdDesc(groupGeneratedCode);

				if (!CollectionUtils.isEmpty(groupMasterDobjList)) {

					GroupMasterTemp groupMasterDbObj = groupMasterDobjList.get(0);

					int currNum = Integer.parseInt(groupMasterDbObj.getGroupCode().substring(1));

					currNum++;

					runningNum = "" + currNum;

					if (runningNum.length() == 1) {
						runningNum = "000" + runningNum;
					} else if (runningNum.length() == 2) {
						runningNum = "00" + runningNum;
					} else if (runningNum.length() == 3) {
						runningNum = "0" + runningNum;
					}
				}
			}

			String generatedGroupCode = groupNameChar + runningNum;
			groupMasterTempDto.setGroupCode(generatedGroupCode);
		}

		Set<GroupCompany> groupCompanySet = new HashSet<>();
		GroupCompany gc;
		for (String compInput : companyList) {
			if (!companyMapUpper.containsKey(compInput.toUpperCase())) {
				System.out.println("Have to handle some errors................");
				break;
			}

			gc = new GroupCompany();
			gc.setCompanyId(companyMapUpper.get(compInput.toUpperCase()));
			gc.setCompanyName(compInput);
			groupCompanySet.add(gc);
		}

		GroupMasterTemp groupMasterTemp = new GroupMasterTemp();

		if (groupMasterTempDto.getIsBulkUpload().equals(Boolean.FALSE)) {
			if (groupMasterTempDto.getEntryType() == 1) {
				groupMasterTemp.setGroupName(groupMasterTempDto.getGroupName());
				groupMasterTemp.setGroupCode(groupMasterTempDto.getGroupCode());
			} else {
				if (groupMasterObjDb != null) {
					groupMasterTemp.setGroupName(groupMasterObjDb.getGroupName());
					groupMasterTemp.setGroupCode(groupMasterObjDb.getGroupCode());

					if (groupMasterObjDb.getGroupIdFk() == null) {
						groupMasterTemp.setGroupIdFk(groupMasterObjDb);
					} else {
						groupMasterTemp.setGroupIdFk(groupMasterObjDb.getGroupIdFk());
					}
				}
			}
		} else {
			if (groupMasterTempDto.getEntryType() == 1) {
				groupMasterTemp.setGroupName(groupMasterTempDto.getGroupName());
				groupMasterTemp.setGroupCode(groupMasterTempDto.getGroupCode());
			} else {
				groupMasterObjDb = new GroupMasterTemp();
				groupMasterObjDb.setGroupId(groupMasterTempDto.getGroupId());
				groupMasterTemp.setGroupIdFk(groupMasterObjDb);
				groupMasterTemp.setGroupName(groupMasterTempDto.getGroupName());
				groupMasterTemp.setGroupCode(groupMasterTempDto.getGroupCode());
			}

		}

		groupMasterTemp.setAlternateName(groupMasterTempDto.getAlternateName());
		groupMasterTemp.setRemark(groupMasterTempDto.getRemark());
		groupMasterTemp.setMobileNumber(groupMasterTempDto.getMobileNumber());
		groupMasterTemp.setVerificationStatus(0);
		groupMasterTemp.setCreatedOn(new Date());
		groupMasterTemp.setEntryType(groupMasterTempDto.getEntryType());
		groupMasterTemp.setEntityBean(entityBean);
		groupMasterTemp.setCreatedBy(userMaster);
		groupMasterTemp.setGroupCompanySet(groupCompanySet);
		groupMasterTemp.setIsBulkUpload(groupMasterTempDto.getIsBulkUpload());

		try {
			groupMasterTempRepo.save(groupMasterTemp);

			System.out.println(groupMasterTemp);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		return new ServiceResponseBuilder().setStatus(true).setResponse(groupMasterTempDto.getGroupCode()).build();
	}

	@GetMapping(value = "/getGroupByStatus/{entityCode}/{verificationStatus}")
	public ServiceResponse getGroupByStatus(@PathVariable("entityCode") String entityCode, @PathVariable("verificationStatus") Integer verificationStatus) {

		if (Validations.isEmpty(entityCode)) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
		}

		if (verificationStatus == null) {
			verificationStatus = new Integer(0);
		}

		EntityBean entityBean = entityRepo.findByEntityCode(entityCode);

		if (entityBean == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
		}

		List<GroupMasterTemp> groupDBList = null;
		if (verificationStatus == 1) {
			groupDBList = groupMasterTempRepo.findLatestGroup(verificationStatus, entityBean.getEntityId());
		} else {
			groupDBList = groupMasterTempRepo.findByVerificationStatusAndEntityBeanOrderByCreatedOnDesc(verificationStatus, entityBean);
		}
		if (groupDBList != null) {

			List<GroupMasterTempDto> latestApprovedGroupListDto = new ArrayList<>();
			GroupMasterTempDto latestApprovedGroupDto;
			for (GroupMasterTemp latestApprovedDbGroup : groupDBList) {

				latestApprovedGroupDto = convertDBObjToDto(latestApprovedDbGroup);
				latestApprovedGroupListDto.add(latestApprovedGroupDto);

			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(latestApprovedGroupListDto)).build();

		} else {
			return new ServiceResponseBuilder().setStatus(true).build();
		}

	}

	// Verified on 16-05-2020
	@GetMapping(value = "/getGroupByGroupId/{groupCode}")
	public ServiceResponse getGroupByGroupId(@PathVariable("groupCode") String groupCode) {

		if (Validations.isEmpty(groupCode)) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0846.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0846.toString())).build();
		}

		GroupMasterTemp groupMasterDbObj = groupMasterTempRepo.findTopByGroupCodeAndVerificationStatusOrderByCreatedOnDesc(groupCode, 0);
		if (groupMasterDbObj != null) {
			GroupMasterTempDto groupMasterDto = convertDBObjToDto(groupMasterDbObj);

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(groupMasterDto)).build();
		}

		groupMasterDbObj = groupMasterTempRepo.findTopByGroupCodeAndVerificationStatusOrderByCreatedOnDesc(groupCode, 1);
		if (groupMasterDbObj != null) {
			GroupMasterTempDto groupMasterDto = convertDBObjToDto(groupMasterDbObj);

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(groupMasterDto)).build();
		}

		groupMasterDbObj = groupMasterTempRepo.findTopByGroupCodeAndVerificationStatusOrderByCreatedOnDesc(groupCode, 2);
		if (groupMasterDbObj != null) {
			GroupMasterTempDto groupMasterDto = convertDBObjToDto(groupMasterDbObj);

			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(groupMasterDto)).build();
		}

		return new ServiceResponseBuilder().setStatus(true).build();
	}

	private GroupMasterTempDto convertDBObjToDto(GroupMasterTemp groupMasterTemp) {

		if (groupMasterTemp == null) {
			return null;
		}

		GroupMasterTempDto groupMasterTempDto = new GroupMasterTempDto();
		groupMasterTempDto.setGroupId(groupMasterTemp.getGroupId());
		groupMasterTempDto.setGroupCode(groupMasterTemp.getGroupCode());
		groupMasterTempDto.setGroupName(groupMasterTemp.getGroupName());
		groupMasterTempDto.setAlternateName(groupMasterTemp.getAlternateName());
		groupMasterTempDto.setRemark(groupMasterTemp.getRemark());
		groupMasterTempDto.setMobileNumber(groupMasterTemp.getMobileNumber());

		if (groupMasterTemp.getApprovedByFk() != null) {
			groupMasterTempDto.setApprovedByUser(groupMasterTemp.getApprovedByFk().getUserName());
			groupMasterTempDto.setApprovedOn(groupMasterTemp.getApprovedOn().getTime());
			groupMasterTempDto.setComment(groupMasterTemp.getComment());
		}

		if (groupMasterTemp.getGroupIdFk() == null) {
			groupMasterTempDto.setCreatedByUser(groupMasterTemp.getCreatedBy().getUserName());
			groupMasterTempDto.setCreatedOn(groupMasterTemp.getCreatedOn().getTime());
		} else {
			groupMasterTempDto.setModifiedByUser(groupMasterTemp.getCreatedBy().getUserName());
			groupMasterTempDto.setModifiedOn(groupMasterTemp.getCreatedOn().getTime());

			groupMasterTempDto.setCreatedByUser(groupMasterTemp.getGroupIdFk().getCreatedBy().getUserName());
			groupMasterTempDto.setCreatedOn(groupMasterTemp.getGroupIdFk().getCreatedOn().getTime());
		}

		groupMasterTempDto.setVerificationStatus(groupMasterTemp.getVerificationStatus());
		groupMasterTempDto.setEntryType(groupMasterTemp.getEntryType());

		if (!CollectionUtils.isEmpty(groupMasterTemp.getGroupCompanySet())) {
			List<CompanyDto> companyDtoList = new ArrayList<>();
			CompanyDto comDto;
			for (GroupCompany company : groupMasterTemp.getGroupCompanySet()) {
				comDto = new CompanyDto();

				comDto.setCompanyId(company.getCompanyId());
				comDto.setCompanyName(company.getCompanyName());
				companyDtoList.add(comDto);
			}
			groupMasterTempDto.setCompanyDtoList(companyDtoList);
		}

		return groupMasterTempDto;

	}

	@PostMapping("/addGroupMasterBulkData")
	public ServiceResponse addGroupMasterBulkData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody GroupMasterBulkUploadDto groupMasterBulkUploadDto) {

		logger.info("Request received to add pan master bulk data for jobProcessingId" + jobProcessId);
		try {
			if (groupMasterBulkUploadDto == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}

			UserMaster userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(groupMasterBulkUploadDto.getCreatedBy());

			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0594.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0594.toString())).build();
			}

			//		EntityBean entityBean = entityRepo.findByEntityId(new Long(panMasterBulkDto.getEntityId()));
			EntityBean entityBean = entityRepo.findByEntityCode(groupMasterBulkUploadDto.getEntityCode());
			PanStatus panStatus = new PanStatus();
			panStatus.setPanStatusId(1l);
			GroupMasterBulk groupMasterBulk = new GroupMasterBulk();
			groupMasterBulk.setFileName(groupMasterBulkUploadDto.getFileName());
			groupMasterBulk.setOriginalFileName(groupMasterBulkUploadDto.getOriginalFileName());
			groupMasterBulk.setCreatedBy(userMaster);
			groupMasterBulk.setCreatedOn(new Date());
			groupMasterBulk.setEntityBean(entityBean);
			groupMasterBulk.setIsActive(true);
			groupMasterBulk.setIsProcessed(false);
			groupMasterBulk.setStatusId(panStatus);

			groupMasterBulkService.add(groupMasterBulk);

			groupMasterBulkUploadDto.setId(groupMasterBulk.getId());
		} catch (Exception e) {
			logger.error("Exception occoured while saving data for jobProcessingId: " + jobProcessId + " Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.info("Request completed to add pan master bulk data for jobProcessingId" + jobProcessId);
		return new ServiceResponseBuilder().setStatus(true).setResponse(groupMasterBulkUploadDto).build();
	}

	@RequestMapping(value = "/getGroupMasterBulkData/{entityCode}/{langCode}", method = RequestMethod.GET)
	public ServiceResponse getGroupMasterBulkData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("entityCode") String entityCode, @PathVariable("langCode") String langCode) {
		try {
			if (Validations.isEmpty(entityCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}

			EntityBean entityBean = entityRepo.findByEntityCode(entityCode);

			if (entityBean == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}

			// checking pan
			List<GroupMasterBulk> groupBulkDBList = null;

			Map<String, Object> columnValueMap = new HashMap<>();
			columnValueMap.put(ColumnConstants.ENT_CODE.getConstantVal(), entityCode);
			columnValueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), langCode);

			groupBulkDBList = groupMasterBulkService.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_ENTITY_CODE_AND_LANG_CODE.getConstantVal());

			if (!CollectionUtils.isEmpty(groupBulkDBList)) {

				List<GroupMasterBulkUploadDto> groupMasterBulkDtoList = convertPanBulkDbListObject(groupBulkDBList);

				return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(groupMasterBulkDtoList)).build();

			} else {
				return new ServiceResponseBuilder().setStatus(true).build();
			}
		} catch (Exception e) {
			logger.error("Exception occoured while getting data of pan bulk upload data for jobProcessingId:Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private List<GroupMasterBulkUploadDto> convertPanBulkDbListObject(List<GroupMasterBulk> groupBulkDBList) {
		List<GroupMasterBulkUploadDto> groupMasterBulkList = new ArrayList<>();
		GroupMasterBulkUploadDto groupMasterBulkUploadDto = null;
		for (GroupMasterBulk groupMB : groupBulkDBList) {
			groupMasterBulkUploadDto = new GroupMasterBulkUploadDto();

			groupMasterBulkUploadDto.setId(groupMB.getId());
			groupMasterBulkUploadDto.setFileName(groupMB.getFileName());
			groupMasterBulkUploadDto.setCreatedBy(groupMB.getCreatedBy().getUserId());
			groupMasterBulkUploadDto.setUserName(groupMB.getCreatedBy().getUserName());

			groupMasterBulkUploadDto.setCreatedOn(groupMB.getCreatedOn().getTime());
			groupMasterBulkUploadDto.setTotalRecords(groupMB.getTotalRecords());
			groupMasterBulkUploadDto.setNumberOfSuccessfull(groupMB.getNumOfSuccessfull());

			groupMasterBulkUploadDto.setStatus(groupMB.getStatusId().getPanStatusId());
			groupMasterBulkUploadDto.setStatusDesc(groupMB.getStatusId().getStatus());

			groupMasterBulkUploadDto.setCreatedBy(groupMB.getCreatedBy().getUserId());

			groupMasterBulkUploadDto.setEntityCode(groupMB.getEntityBean().getEntityCode());
			groupMasterBulkUploadDto.setEntityId(groupMB.getEntityBean().getEntityId());

			if (groupMB.getProcessStartTime() != null) {
				groupMasterBulkUploadDto.setProcessStartTime(groupMB.getProcessStartTime().getTime());
			}
			if (groupMB.getProcessEndTime() != null) {
				groupMasterBulkUploadDto.setProcessEndTime(groupMB.getProcessEndTime().getTime());
			}
			groupMasterBulkUploadDto.setOriginalFileName(groupMB.getOriginalFileName());
			groupMasterBulkList.add(groupMasterBulkUploadDto);
		}

		return groupMasterBulkList;
	}

	@GetMapping(value = "/getGroupList/{verificationStatus}")
	public ServiceResponse getGroupList(@PathVariable("verificationStatus") Integer verificationStatus) {
		if (verificationStatus == null) {
			verificationStatus = new Integer(0);
		}

		List<GroupMasterTemp> groupDBList = null;
		if (verificationStatus == 1) {
			groupDBList = groupMasterTempRepo.findLatestGroupList(verificationStatus);
		} else {
			groupDBList = groupMasterTempRepo.findByVerificationStatusOrderByCreatedOnAsc(verificationStatus);
		}

		if (!CollectionUtils.isEmpty(groupDBList)) {
			List<GroupMasterTempDto> groupListDto = new ArrayList<>();
			GroupMasterTempDto groupDto;
			for (GroupMasterTemp groupDbPan : groupDBList) {
				groupDto = convertDBObjToDto(groupDbPan);
				groupListDto.add(groupDto);
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(groupListDto)).build();
		} else {
			return new ServiceResponseBuilder().setStatus(true).build();
		}
	}

	//Error code change 
	@PostMapping(value = "/approveGroup")
	public ServiceResponse approveGroup(@RequestBody GroupMasterTempDto groupMasterTempDto) {

		if (groupMasterTempDto == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
		}

		if (groupMasterTempDto.getGroupList() == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0846.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0846.toString())).build();
		}

		if (groupMasterTempDto.getVerificationStatus() == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0958.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0958.toString())).build();
		}

		if (groupMasterTempDto.getVerificationStatus() != 1 && groupMasterTempDto.getVerificationStatus() != 2) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0959.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0959.toString())).build();
		}

		if (groupMasterTempDto.getVerificationStatus() == 2 && groupMasterTempDto.getComment() == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0199.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0199.toString())).build();
		}

		UserMaster userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(new Long(groupMasterTempDto.getApprovedByFk()));

		if (userMaster == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0594.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0594.toString())).build();
		}

		try {
			List<String> groupStringList = new ArrayList<>(Arrays.asList(groupMasterTempDto.getGroupList()));
			System.out.println(groupStringList);

			List<GroupMasterTemp> groupObj = groupMasterTempRepo.getGroupList(groupStringList);

			Map<Integer, List<String>> groupWithStatus = new HashMap<>();
			if (!CollectionUtils.isEmpty(groupObj)) {

				List<String> groupList;
				for (GroupMasterTemp groupTmp : groupObj) {
					if (groupWithStatus.containsKey(groupTmp.getVerificationStatus())) {
						groupList = groupWithStatus.get(groupTmp.getVerificationStatus());
					} else {
						groupList = new ArrayList<>();
					}
					if (!groupList.contains(groupTmp.getGroupCode())) {
						groupList.add(groupTmp.getGroupCode());

						groupStringList.remove(groupTmp.getGroupCode());
					}
					groupWithStatus.put(groupTmp.getVerificationStatus(), groupList);
				}
			}
			if (!CollectionUtils.isEmpty(groupStringList)) {
				int dbResp = groupMasterTempRepo.updateVerificationStatus(groupMasterTempDto.getVerificationStatus(), groupMasterTempDto.getComment(), new Long(groupMasterTempDto.getApprovedByFk()), groupStringList);
				//System.out.println(dbResp);

				groupWithStatus.put(0, groupStringList);

				//
				if (groupMasterTempDto.getVerificationStatus() == 1) {

					List<GroupMaster> masterDBgroupList = groupMasterRepo.getGroupCode(groupStringList);

					if (!CollectionUtils.isEmpty(masterDBgroupList)) {

						for (GroupMaster groupCodedbObj : masterDBgroupList) {

							groupMasterRepo.updateGroup(groupCodedbObj.getRemark(), groupCodedbObj.getAlternateName(), groupCodedbObj.getMobileNumber(), groupCodedbObj.getGroupIdFk().getGroupId(), groupCodedbObj.getGroupCode());

							groupStringList.remove(groupCodedbObj.getGroupCode());

						}
					}
					if (!CollectionUtils.isEmpty(groupStringList) && groupStringList != null) {
						List<GroupMasterTemp> dBgroupList = groupMasterTempRepo.getGroupCode(groupStringList);
						if (!CollectionUtils.isEmpty(dBgroupList)) {

							List<GroupMaster> groupMasterList = new ArrayList<>();
							GroupMaster groupMasterObj;
							for (GroupMasterTemp groupMasterTemp : dBgroupList) {
								groupMasterObj = new GroupMaster();
								groupMasterObj.setGroupIdFk(groupMasterTemp);
								groupMasterObj.setGroupCode(groupMasterTemp.getGroupCode());
								groupMasterObj.setGroupName(groupMasterTemp.getGroupName());
								groupMasterObj.setAlternateName(groupMasterTemp.getAlternateName());
								groupMasterObj.setMobileNumber(groupMasterTemp.getMobileNumber());
								groupMasterObj.setRemark(groupMasterTemp.getRemark());

								groupMasterList.add(groupMasterObj);
							}

							try {
								groupMasterRepo.saveAll(groupMasterList);
							} catch (Exception e) {
								logger.error("Exception : ", e);
								return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
							}

						}
					}

				}
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(groupWithStatus)).build();
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@RequestMapping(value = "/getGroupMasterData/", method = RequestMethod.GET)
	public ServiceResponse getGroupMasterData(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		try {
			logger.info("Request received to get Group master data");
			List<GroupMasterDetails> groupMasterDetailsList = groupMasterDetailsRepo.getPanMasterData();

			if (!CollectionUtils.isEmpty(groupMasterDetailsList)) {

				List<GroupMasterDetailsDto> panMasterDtoList = convertGroupMasterDbListToDto(groupMasterDetailsList);

				return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(panMasterDtoList)).build();

			} else {
				return new ServiceResponseBuilder().setStatus(true).build();
			}
		} catch (Exception e) {
			logger.error("Exception occoured while getting data of group master  jobProcessingId:Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private List<GroupMasterDetailsDto> convertGroupMasterDbListToDto(List<GroupMasterDetails> groupMasterDetailsList) {
		List<GroupMasterDetailsDto> groupMasterDtoList = new ArrayList<>();
		try {
			GroupMasterDetailsDto groupMasterDetailsDto;
			for (GroupMasterDetails groupMasterDetails : groupMasterDetailsList) {
				groupMasterDetailsDto = new GroupMasterDetailsDto();
				groupMasterDetailsDto.setId(groupMasterDetails.getId());
				groupMasterDetailsDto.setFileName(groupMasterDetails.getFileName());
				groupMasterDetailsDto.setCreatedOn(groupMasterDetails.getCreatedOn().getTime());
				groupMasterDetailsDto.setStatus(groupMasterDetails.getStatus());
				if (groupMasterDetails.getProcessStartTime() != null) {
					groupMasterDetailsDto.setProcessStartTime(groupMasterDetails.getProcessStartTime().getTime());
				}

				if (groupMasterDetails.getProcessEndTime() != null) {
					groupMasterDetailsDto.setProcessEndTime(groupMasterDetails.getProcessEndTime().getTime());
				}

				groupMasterDetailsDto.setTotalRecords(groupMasterDetails.getTotalRecords());
				groupMasterDetailsDto.setModifiedFileName(groupMasterDetails.getModifiedFileName());
				groupMasterDtoList.add(groupMasterDetailsDto);
				if (groupMasterDetailsDto.getStatus() == 3) {
					return groupMasterDtoList;
				}
				if (groupMasterDtoList.size() == 2) {
					return groupMasterDtoList;
				}
			}
		} catch (Exception e) {
			logger.error("Exception occoured while convertPanMasterDbListToDto" + e);
		}
		return groupMasterDtoList;
	}

	@PostMapping("/getGroupMasterCodeAndName/{groupCode}")
	public ServiceResponse getGroupMasterCodeAndName(@PathVariable(name = "groupCode") String groupCode) {
		try {
			logger.info("Request received to get Group master data");
			Option option = null;
			List<Option> optionList = new ArrayList<>();
			Options options = new Options();
			//List<GroupMaster> listOfActiveGroupData  = groupMasterRepo.findAll(Sort.by(Sort.Direction.ASC, "groupName"));
			List<GroupMaster> listOfActiveGroupData = groupMasterRepo.getGroupDetails(groupCode.toUpperCase());
			if (CollectionUtils.isEmpty(listOfActiveGroupData)) {
				logger.error("No data found of group master");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1518.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1518.toString())).build();
			}

			for (GroupMaster groupMaster : listOfActiveGroupData) {
				option = new Option();
				option.setKey(groupMaster.getGroupCode());
				option.setValue(groupMaster.getGroupName());
				optionList.add(option);
			}
			options.setOptionList(optionList);
			return new ServiceResponseBuilder().setStatus(true).setResponse(options).build();
		} catch (Exception e) {
			logger.error("Exception occoured while getting data of group master  jobProcessingId:Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}
}
