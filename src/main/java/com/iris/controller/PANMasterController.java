package com.iris.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.DummyPanDto;
import com.iris.dto.LabelDto;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.PanMappingDto;
import com.iris.dto.PanMappingPagableDto;
import com.iris.dto.PanMasterBulkDto;
import com.iris.dto.PanMasterDetailsDto;
import com.iris.dto.PanMasterTempDto;
import com.iris.dto.PanMasterTempPagableDto;
import com.iris.dto.PanSupportingInfo;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.DynamicDropDownBean;
import com.iris.model.EntityBean;
import com.iris.model.InstitutionType;
import com.iris.model.NSDLPanVerif;
import com.iris.model.PanMapping;
import com.iris.model.PanMaster;
import com.iris.model.PanMasterBulk;
import com.iris.model.PanMasterDetails;
import com.iris.model.PanMasterTemp;
import com.iris.model.PanStatus;
import com.iris.model.SupportingDocType;
import com.iris.model.UserMaster;
import com.iris.nbfc.repository.NsdlPanVerificationRepo;
import com.iris.repository.EntityRepo;
import com.iris.repository.InstitutionTypeRepo;
import com.iris.repository.PanMappingRepo;
import com.iris.repository.PanMasterDetailsRepo;
import com.iris.repository.PanMasterRepo;
import com.iris.repository.PanMasterTempRepo;
import com.iris.repository.SupportingDocTypeRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.service.impl.PanMasterBulkService;
import com.iris.util.FileManager;
import com.iris.util.ResourceUtil;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.util.constant.NsdlPanVerfStatusEnum;
import com.iris.util.constant.PanMappingStatusEnum;

@RestController
@RequestMapping("/service/panMaster")
public class PANMasterController {

	private static final Logger logger = LogManager.getLogger(PANMasterController.class);
	private static final String calendarFormat = "en";

	@Autowired
	private PanMasterTempRepo panMasterTempRepo;

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EntityRepo entityRepo;

	@Autowired
	private PanMasterBulkService panMasterBulkService;

	@Autowired
	private PanMasterRepo panMasterRepo;

	private static final Object lock1 = new Object();

	@Autowired
	private PanMasterDetailsRepo panMasterDetailsRepo;

	@Autowired
	private InstitutionTypeRepo institutionTypeRepo;

	@Autowired
	private SupportingDocTypeRepo supportingDocTypeRepo;
	private static final String DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";

	@Autowired
	private PanMappingRepo panMappingRepo;

	@Autowired
	private NsdlPanVerificationRepo nsdlNSDLPanVerifRepo;

	@Autowired
	private EntityManager em;

	@RequestMapping(value = "/getPANInfo/{panNumber}", method = RequestMethod.GET)
	public ServiceResponse getPANInfo(@PathVariable("panNumber") String panNumber) {

		boolean panVerify = validatePanWithRegex(panNumber);

		if (!panVerify) {
			return new ServiceResponseBuilder().setStatus(panVerify).setStatusCode(ErrorCode.E0764.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0764.toString())).setResponse(panNumber).build();
		}

		PanMasterTemp panMasterDbObj = panMasterTempRepo.findTopByPanNumberAndVerificationStatusOrderByCreatedOnDesc(panNumber, 0);
		if (panMasterDbObj != null) {

			PanMasterTempDto panDto = convertDBObjToDto(panMasterDbObj);

			return new ServiceResponseBuilder().setStatus(panVerify).setResponse(new Gson().toJson(panDto)).build();
		}

		panMasterDbObj = panMasterTempRepo.findTopByPanNumberAndVerificationStatusOrderByCreatedOnDesc(panNumber, 1);
		if (panMasterDbObj != null) {

			PanMasterTempDto panDto = convertDBObjToDto(panMasterDbObj);

			return new ServiceResponseBuilder().setStatus(panVerify).setResponse(new Gson().toJson(panDto)).build();
		}

		panMasterDbObj = panMasterTempRepo.findTopByPanNumberAndVerificationStatusOrderByCreatedOnDesc(panNumber, 2);
		if (panMasterDbObj != null) {

			PanMasterTempDto panDto = convertDBObjToDto(panMasterDbObj);

			return new ServiceResponseBuilder().setStatus(panVerify).setResponse(new Gson().toJson(panDto)).build();
		}

		return new ServiceResponseBuilder().setStatus(true).build();

	}

	@RequestMapping(value = "/getPANInfoForMapping/{panNumber}", method = RequestMethod.GET)
	public ServiceResponse getPanInfoForMapping(@PathVariable("panNumber") String panNumber) {

		boolean panVerify = validatePanWithRegex(panNumber);

		if (!panVerify) {
			return new ServiceResponseBuilder().setStatus(panVerify).setStatusCode(ErrorCode.E0764.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0764.toString())).setResponse(panNumber).build();
		}

		PanMaster panRecord = panMasterRepo.findByPanNumber(panNumber);

		if (panRecord != null) {
			return new ServiceResponseBuilder().setStatus(panVerify).setResponse(new Gson().toJson(panRecord)).build();
		}

		return new ServiceResponseBuilder().setStatus(false).build();

	}

	@RequestMapping(value = "/getPANMapping/{panNumber}", method = RequestMethod.GET)
	public ServiceResponse getPanMapping(@PathVariable("panNumber") String panNumber) {

		if (panNumber == null) {
			return new ServiceResponseBuilder().setStatus(false).build();
		}

		PanMapping panMappingRecord = panMappingRepo.findByPanNumber(panNumber);

		if (panMappingRecord != null) {
			PanMappingDto panDto = new PanMappingDto();

			panDto.setPanMappingId(panMappingRecord.getPanMappingId());
			panDto.setActualPanNumber(panMappingRecord.getActualPanNumber());
			panDto.setDummyPanNumber(panMappingRecord.getDummyPanNumber());
			panDto.setStatus(panMappingRecord.getStatus());
			panDto.setIsActive(panMappingRecord.getIsActive());
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(panDto)).build();
		}
		return new ServiceResponseBuilder().setStatus(false).build();

	}

	@PostMapping(value = "/getNSDLPanInfo")
	public ServiceResponse getNSDLPanInfo(@RequestBody List<DummyPanDto> dummyPanDtoList) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select PAN_NUMBER, BORROWER_NAME, BORROWER_TITLE, BORROWER_MOBILE  from TBL_PAN_MASTER_DUMMY where PAN_NUMBER IN :panList");
			List<String> panList = dummyPanDtoList.stream().map(m -> m.getActualPanNumber()).collect(Collectors.toList());
			Query query = em.createNativeQuery(sb.toString(), Tuple.class);
			query.setParameter("panList", panList);

			List<Tuple> tupleList = query.getResultList();
			List<DummyPanDto> outputList = new ArrayList<>();

			for (Tuple tuple : tupleList) {
				DummyPanDto dummyPanDto = new DummyPanDto();
				dummyPanDto.setActualPanNumber((String) tuple.get("PAN_NUMBER"));
				dummyPanDto.setBorrowerName((String) tuple.get("BORROWER_NAME"));
				dummyPanDto.setBorrowerTitle((String) tuple.get("BORROWER_TITLE"));
				dummyPanDto.setBorrowerMobile((String) tuple.get("BORROWER_MOBILE"));
				outputList.add(dummyPanDto);
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(outputList).build();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occoured while getting data of pan master  jobProcessingId:Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	// checking pan format
	private boolean validatePanWithRegex(String panNumber) {
		if (panNumber == null || panNumber.isEmpty()) {
			return false;
		}
		boolean flag = false;
		if (panNumber.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	private PanMappingDto convertDBObjToDtoMapping(PanMapping panMappingDBObj) {
		if (panMappingDBObj == null) {
			return null;
		}

		PanMappingDto panDto = new PanMappingDto();

		panDto.setPanMappingId(panMappingDBObj.getPanMappingId());
		panDto.setCreatedBy(panMappingDBObj.getCreatedBy().getUserName());
		panDto.setCreatedOn(panMappingDBObj.getCreatedOn().getTime());

		panDto.setActualPanNumber(panMappingDBObj.getActualPanNumber());
		panDto.setDummyPanId(panMappingDBObj.getDummyPanId().getPanMasterId());
		panDto.setDummyPanNumber(panMappingDBObj.getDummyPanNumber());
		panDto.setComment(panMappingDBObj.getComment());
		panDto.setStatus(panMappingDBObj.getStatus());
		panDto.setIsActive(panMappingDBObj.getIsActive());
		panDto.setPanMappingId(panMappingDBObj.getPanMappingId());
		panDto.setRequestedByEntityId(panMappingDBObj.getRequestedByEntityId().getEntityId());
		panDto.setEntityListUsedForFilling(panMappingDBObj.getEntityListUsedForFilling());
		panDto.setActualPanBorrowerName(panMappingDBObj.getBorrowerName());
		//panDto.setDummyPanBorrowerName(panMappingDBObj.getDummyPanId().getBorrowerName());
		if (panMappingDBObj.getApprovedByFk() != null) {
			panDto.setApprovedByUserName(panMappingDBObj.getApprovedByFk().getUserName());
			panDto.setApprovedOn(panMappingDBObj.getApprovedOn().getTime());

		}
		return panDto;
	}

	private PanMasterTempDto convertDBObjToDto(PanMasterTemp panTempDBObj) {
		if (panTempDBObj == null) {
			return null;
		}

		PanMasterTempDto panDto = new PanMasterTempDto();
		try {
			panDto.setPanNumber(panTempDBObj.getPanNumber());
			panDto.setBorrowerName(panTempDBObj.getBorrowerName());
			panDto.setBorrowerAlternateName(panTempDBObj.getBorrowerAlternateName());
			panDto.setBorrowerTitle(panTempDBObj.getBorrowerTitle());
			panDto.setBorrowerMobile(panTempDBObj.getBorrowerMobile());
			panDto.setVerificationStatus(panTempDBObj.getVerificationStatus());
			panDto.setCreatedBy(panTempDBObj.getCreatedBy().getUserId().intValue());
			panDto.setEntryType(panTempDBObj.getEntryType());
			panDto.setRbiGenerated(panTempDBObj.isRbiGenerated());
			panDto.setComment(panTempDBObj.getComment());
			panDto.setStatus(panTempDBObj.getStatus());

			SimpleDateFormat formatter = new SimpleDateFormat(DD_SLASH_MM_SLASH_YYYY);
			if (panTempDBObj.getBorrowerDob() != null) {
				String dateOfBirth = formatter.format(panTempDBObj.getBorrowerDob().getTime());
				panDto.setDateOfBirth(dateOfBirth);
			}
			if (panTempDBObj.getInstitutionType() != null) {
				panDto.setInstitutionTypeName(panTempDBObj.getInstitutionType().getInstTypeName());
			}
			if (panTempDBObj.getSupportingDocType() != null) {
				panDto.setSupportDocFileType(panTempDBObj.getSupportingDocType().getDocType());
			}
			if (panTempDBObj.getSupportingDocIdentityNum() != null) {
				panDto.setSupportingDocIdentityNumber(panTempDBObj.getSupportingDocIdentityNum());
			}
			panDto.setSupportDocName(panTempDBObj.getSupportDocName());
			if (panTempDBObj.getApprovedByFk() != null) {
				panDto.setApprovedByUser(panTempDBObj.getApprovedByFk().getUserName());
				panDto.setApprovedOn(panTempDBObj.getApprovedOn().getTime());

			}

			if (panTempDBObj.getIsBulkUpload() != null) {
				panDto.setIsBulkUpload(panTempDBObj.getIsBulkUpload());
			}

			if (panTempDBObj.getPanIdFk() == null) {
				panDto.setCreatedOn(panTempDBObj.getCreatedOn().getTime());

				panDto.setCreatedUser(panTempDBObj.getCreatedBy().getUserName());
			} else {
				panDto.setModifiedOn(panTempDBObj.getCreatedOn().getTime());
				panDto.setCreatedOn(panTempDBObj.getPanIdFk().getCreatedOn().getTime());

				panDto.setCreatedUser(panTempDBObj.getPanIdFk().getCreatedBy().getUserName());
				panDto.setModifiedUser(panTempDBObj.getCreatedBy().getUserName());
			}

		} catch (Exception e) {
			logger.error("Exception occoured while convertPanMasterDbListToDto" + e);
		}
		return panDto;
	}

	@RequestMapping(value = "/dummyPanToActualPanMapping", method = RequestMethod.POST)
	public ServiceResponse insertDummyPanMappingDetails(@RequestBody PanMappingDto panMappingDto) {
		if (panMappingDto == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0760.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0760.toString())).build();
		}
		Gson gson = new Gson();
		NSDLPanVerif nsdlPanVerif;
		PanMapping panMappingObj = new PanMapping();
		panMappingObj.setIsActive(true);
		EntityBean entityBean = null;
		UserMaster userMaster = null;
		userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(new Long(panMappingDto.getCreatedBy()));
		entityBean = entityRepo.findByEntityCode(panMappingDto.getEntityCode());

		if (entityBean == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
		}

		if (userMaster == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0594.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0594.toString())).build();
		}

		panMappingObj.setCreatedBy(userMaster);
		panMappingObj.setCreatedOn(new Date());
		panMappingObj.setRequestedByEntityId(entityBean);

		PanMaster panMasterDummy = panMasterRepo.findByPanNumber(panMappingDto.getDummyPanNumber());

		PanMaster panMasterActual = panMasterRepo.findByPanNumber(panMappingDto.getActualPanNumber());

		panMappingObj.setActualPanId(panMasterActual);
		panMappingObj.setDummyPanId(panMasterDummy);

		panMappingObj.setActualPanNumber(panMappingDto.getActualPanNumber());
		panMappingObj.setDummyPanNumber(panMappingDto.getDummyPanNumber());
		panMappingObj.setBorrowerName(panMappingDto.getActualPanBorrowerName());

		if (panMasterActual != null && panMasterActual.getBorrowerName().equalsIgnoreCase(panMappingDto.getActualPanBorrowerName())) {
			panMappingObj.setStatus(PanMappingStatusEnum.RBI_PENDING.getStatus());
			panMappingRepo.save(panMappingObj);
		} else {
			panMappingObj.setStatus(PanMappingStatusEnum.NSDL_PENDING.getStatus());
			PanMapping panMap = panMappingRepo.save(panMappingObj);
			nsdlPanVerif = new NSDLPanVerif();
			nsdlPanVerif.setActualPanNumber(panMappingDto.getActualPanNumber());
			nsdlPanVerif.setIsActive(Boolean.TRUE);
			nsdlPanVerif.setModuleName(GeneralConstants.RBI_NSDL_PAN_MAP.getConstantVal());
			nsdlPanVerif.setStatus(NsdlPanVerfStatusEnum.PEND_NSDL_VERIFY.getStaus());
			nsdlPanVerif.setCreatedOn(new Date());
			nsdlPanVerif.setSubTaskStatus(Boolean.FALSE);

			PanSupportingInfo panSupInfo = new PanSupportingInfo();
			panSupInfo.setRefId(panMap.getPanMappingId().toString());
			String panSupportInfoJson = gson.toJson(panSupInfo);
			nsdlPanVerif.setSupportingInfo(panSupportInfoJson);
			nsdlNSDLPanVerifRepo.save(nsdlPanVerif);
		}

		return new ServiceResponseBuilder().setStatus(true).setResponse("1").build();
	}

	// insertPan into Database
	@RequestMapping(value = "/addPan", method = RequestMethod.POST)
	public ServiceResponse insertBorrowerDetails(@RequestBody PanMasterTempDto panMasterTempDto) {
		if (panMasterTempDto == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0760.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0760.toString())).build();
		}

		if (panMasterTempDto.getEntryType() == null || panMasterTempDto.getEntryType().intValue() != 1 && panMasterTempDto.getEntryType().intValue() != 2) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0811.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0811.toString())).build();
		}

		if (panMasterTempDto.getRbiGenerated() == null) {
			panMasterTempDto.setRbiGenerated(Boolean.FALSE);
		}

		if (Validations.isEmpty(panMasterTempDto.getBorrowerName())) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0765.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0765.toString())).build();
		}

		if (panMasterTempDto.getBorrowerMobile() != null && (panMasterTempDto.getBorrowerMobile().longValue() < 1000000000l || panMasterTempDto.getBorrowerMobile().longValue() > 9999999999l)) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0766.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0766.toString())).build();
		}

		EntityBean entityBean = null;
		UserMaster userMaster = null;
		if (panMasterTempDto.getIsBulkUpload().equals(Boolean.TRUE)) {
			entityBean = panMasterTempDto.getEntityBean();
			userMaster = panMasterTempDto.getUserMaster();
		} else {
			if (Validations.isEmpty(panMasterTempDto.getEntityCode())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}
			entityBean = entityRepo.findByEntityCode(panMasterTempDto.getEntityCode());
			userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(new Long(panMasterTempDto.getCreatedBy()));
		}
		if (entityBean == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
		}

		if (userMaster == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0594.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0594.toString())).build();
		}

		PanMasterTemp panMasterParent = null;

		if (panMasterTempDto.getRbiGenerated()) { // This is RBI generated PAN condition
			if (panMasterTempDto.getEntryType().intValue() == 2) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0811.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0811.toString())).build();
			}
			/*PS
			 * if (panMasterTempDto.getInstitutionType() == null ||
			 * panMasterTempDto.getInstitutionType().intValue() < 0 ||
			 * panMasterTempDto.getInstitutionType().intValue() > 4) { return new
			 * ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0812.
			 * toString())
			 * .setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0812.toString())).
			 * build(); }
			 */

			char institutionTypeCharacter = ' ';
			if (panMasterTempDto.getInstitutionType().intValue() == 1) {
				institutionTypeCharacter = 'G';
			} else if (panMasterTempDto.getInstitutionType().intValue() == 2) {
				institutionTypeCharacter = 'F';
			} else if (panMasterTempDto.getInstitutionType().intValue() == 3) {
				institutionTypeCharacter = 'S';
			} else if (panMasterTempDto.getInstitutionType().intValue() == 4) {
				institutionTypeCharacter = 'C';
			} else if (panMasterTempDto.getInstitutionType().intValue() == 5) {
				institutionTypeCharacter = 'I';
			} else if (panMasterTempDto.getInstitutionType().intValue() == 6) {
				institutionTypeCharacter = 'T';
			} else if (panMasterTempDto.getInstitutionType().intValue() == 7) {
				institutionTypeCharacter = 'O';
			}

			String rbiGeneratedPan;

			String runningNum = "0001";

			String borrowerNameChar = "" + panMasterTempDto.getBorrowerName().toUpperCase().charAt(0);
			for (int i = 0; i < panMasterTempDto.getBorrowerName().length(); i++) {
				if ((panMasterTempDto.getBorrowerName().charAt(i) + "").matches("[A-Za-z]")) {
					borrowerNameChar = (panMasterTempDto.getBorrowerName().charAt(i) + "").toUpperCase();
					break;
				}
			}

			synchronized (lock1) {
				rbiGeneratedPan = "RBIU" + borrowerNameChar + "%" + institutionTypeCharacter;

				PanMasterTemp panMasterDbObj = panMasterTempRepo.findTopByPanNumberOrderByPanNumberDescContaining(rbiGeneratedPan); // only first recird

				if (panMasterDbObj != null) {

					int currNum = Integer.parseInt(panMasterDbObj.getPanNumber().substring(5, 9));

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

				rbiGeneratedPan = "RBIU" + borrowerNameChar + runningNum + institutionTypeCharacter;

				panMasterTempDto.setPanNumber(rbiGeneratedPan);
				String newAttachementFileName = "";
				if (panMasterTempDto.getSupportDocFilePath() != null) {

					String date = DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS");
					date = date.replaceAll("[:]", "-");
					date = date.replaceAll("[ ]", "_");

					newAttachementFileName = entityBean.getEntityId() + "_" + entityBean.getEntityCode().trim() + "_" + userMaster.getUserId() + "_" + date.trim() + "." + panMasterTempDto.getSupportDocFileType();

					String uploadAttachmentPath = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.pan.SupportDoc") + File.separator + newAttachementFileName;

					FileManager fileManageObj = new FileManager();
					fileManageObj.copyFile(new File(panMasterTempDto.getSupportDocFilePath()), new File(uploadAttachmentPath));

				}
				try {
					PanMasterTemp panMasterTemp = new PanMasterTemp();
					panMasterTemp.setPanNumber(panMasterTempDto.getPanNumber());
					panMasterTemp.setBorrowerName(panMasterTempDto.getBorrowerName());
					panMasterTemp.setBorrowerAlternateName(panMasterTempDto.getBorrowerAlternateName());
					panMasterTemp.setBorrowerTitle(panMasterTempDto.getBorrowerTitle());
					panMasterTemp.setBorrowerMobile(panMasterTempDto.getBorrowerMobile());
					panMasterTemp.setVerificationStatus(0);
					panMasterTemp.setCreatedBy(userMaster);
					panMasterTemp.setCreatedOn(new Date());
					panMasterTemp.setEntryType(panMasterTempDto.getEntryType());
					panMasterTemp.setRbiGenerated(panMasterTempDto.getRbiGenerated());
					panMasterTemp.setEntityBean(entityBean);
					panMasterTemp.setPanIdFk(panMasterParent);
					panMasterTemp.setIsBulkUpload(panMasterTempDto.getIsBulkUpload());
					panMasterTemp.setIsActive(Boolean.TRUE);
					panMasterTemp.setSupportingDocIdentityNum(panMasterTempDto.getSupportingDocIdentityNumber());
					if (panMasterTempDto.getSupportingDocTypeId() != -1) {
						Optional<SupportingDocType> supportDocType = supportingDocTypeRepo.findById(Long.valueOf("" + panMasterTempDto.getSupportingDocTypeId()));
						if (supportDocType.isPresent()) {
							panMasterTemp.setSupportingDocType(supportDocType.get());
						}

						panMasterTemp.setSupportDocName(newAttachementFileName);
					}

					panMasterTemp.setStatus(panMasterTempDto.getStatus());
					InstitutionType institutionType = new InstitutionType();
					institutionType.setInstTypeId(panMasterTempDto.getInstitutionType().longValue());
					panMasterTemp.setInstitutionType(institutionType);
					if (!Validations.isEmpty(panMasterTempDto.getDateOfBirth())) {
						panMasterTemp.setBorrowerDob(DateManip.convertStringToDate(panMasterTempDto.getDateOfBirth(), DD_SLASH_MM_SLASH_YYYY));
					}
					panMasterTempRepo.save(panMasterTemp);
				} catch (Exception e) {
					logger.error("Exception : ", e);
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
				}
			}

		} else { // This is NSDL generated PAN condition
			if (Validations.isEmpty(panMasterTempDto.getPanNumber())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0760.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0760.toString())).build();
			}

			if (!validatePanWithRegex(panMasterTempDto.getPanNumber())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0764.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0764.toString())).build();
			}

			if (panMasterTempDto.getBorrowerMobile() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0837.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0837.toString())).build();
			}

			if (panMasterTempDto.getIsBulkUpload().equals(Boolean.FALSE)) {
				PanMasterTemp panMasterDbObj = panMasterTempRepo.findTopByPanNumberOrderByCreatedOnDesc(panMasterTempDto.getPanNumber());
				if (panMasterTempDto.getEntryType().intValue() == 1) { // this is ADD condition
					if (panMasterDbObj != null) {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0761.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0761.toString())).build();
					}
				} else { // this is UPDATE condition
					if (panMasterDbObj == null || panMasterDbObj.getVerificationStatus().intValue() == 0) {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0760.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0760.toString())).build();
					}
					panMasterParent = panMasterTempRepo.findByPanNumberAndEntryType(panMasterTempDto.getPanNumber(), 1);
				}
			}
			if (panMasterTempDto.getIsBulkUpload().equals(Boolean.TRUE) && panMasterTempDto.getEntryType() == 2) {
				panMasterParent = new PanMasterTemp();
				panMasterParent.setPanId(panMasterTempDto.getPanId());
			}

			PanMasterTemp panMasterTemp = new PanMasterTemp();
			panMasterTemp.setPanNumber(panMasterTempDto.getPanNumber());
			panMasterTemp.setBorrowerName(panMasterTempDto.getBorrowerName());
			panMasterTemp.setBorrowerAlternateName(panMasterTempDto.getBorrowerAlternateName());
			panMasterTemp.setBorrowerTitle(panMasterTempDto.getBorrowerTitle());
			panMasterTemp.setBorrowerMobile(panMasterTempDto.getBorrowerMobile());
			panMasterTemp.setVerificationStatus(0);
			panMasterTemp.setCreatedBy(userMaster);
			panMasterTemp.setCreatedOn(new Date());
			panMasterTemp.setEntryType(panMasterTempDto.getEntryType());
			panMasterTemp.setRbiGenerated(panMasterTempDto.getRbiGenerated());
			panMasterTemp.setEntityBean(entityBean);
			panMasterTemp.setPanIdFk(panMasterParent);
			panMasterTemp.setIsBulkUpload(panMasterTempDto.getIsBulkUpload());
			panMasterTemp.setIsActive(Boolean.TRUE);

			if (panMasterTempDto.getEntryType() == 1) {
				panMasterTemp.setStatus(panMasterTempDto.getStatus());
			} else {
				panMasterTemp.setStatus(panMasterParent.getStatus());
			}

			try {

				panMasterTempRepo.save(panMasterTemp);
			} catch (Exception e) {
				logger.error("Exception : ", e);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}

		}

		return new ServiceResponseBuilder().setStatus(true).setResponse(panMasterTempDto.getPanNumber()).build();
	}

	// to Retrieve PAN Status
	@RequestMapping(value = "/getNDLSPANByStatus/{status}/{startRecordIndex}", method = RequestMethod.GET)
	public ServiceResponse getNDLSPANByStatus(@PathVariable("status") String status, @PathVariable("startRecordIndex") Integer startRecordIndex) {
		logger.info("request received getNDLSPANByStatus");

		if (status == null) {
			status = PanMappingStatusEnum.RBI_PENDING.getStatus();
		}

		//EntityBean entityBean = entityRepo.findByEntityCode(entityCode);

		/*
		 * if (entityBean == null) { return new
		 * ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.
		 * toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.
		 * toString())).build(); }
		 */
		//logger.info("getNDLSPANByStatus , entityBean" + entityBean.getEntityCode());
		// checking pan
		List<PanMasterTemp> panDBList = null;
		Pageable pageable;

		//setting values in paegable interface, 1st parameter is page number and 2nd is fetch size
		pageable = PageRequest.of(startRecordIndex, 5000);

		Long totalCount = null;
		PanMasterTempPagableDto panMasterTempPagableDto = new PanMasterTempPagableDto();
		if (status.equalsIgnoreCase(PanMappingStatusEnum.APPROVED.getStatus())) {
			totalCount = panMasterTempRepo.getTotalCount(status);
			panDBList = panMasterTempRepo.findLatestPan(status, pageable);
		} else {
			totalCount = panMasterTempRepo.getTotalCount(status);
			panDBList = panMasterTempRepo.findByVerificationStatusAndEntityBeanOrderByCreatedOnDesc(status, pageable);
		}
		panMasterTempPagableDto.setTotalCount(totalCount);
		logger.info("getNDLSPANByStatus , panDBList");
		if (panDBList != null) {

			List<PanMasterTempDto> latestApprovedPanListDto = new ArrayList<>();
			//			PanMasterTempDto latestApprovedPanDto;
			//			logger.info("getNDLSPANByStatus , panDBList for loop start, panDBList  "+ panDBList.size());
			//			System.out.println("getNDLSPANByStatus , panDBList for loop start, syso");
			//			for (PanMasterTemp latestApprovedDbPan : panDBList) {
			//
			//				latestApprovedPanDto = convertDBObjToDto(latestApprovedDbPan);
			//				latestApprovedPanListDto.add(latestApprovedPanDto);
			//
			//			}
			//			logger.info("getNDLSPANByStatus , panDBList for loop end");
			//			System.out.println("getNDLSPANByStatus , panDBList for loop end, syso");

			logger.info("getNDLSPANByStatus , panDBList for loop start, panDBList  " + panDBList.size());
			panDBList.stream().forEach(f -> {
				PanMasterTempDto latestApprovedPanDto = convertDBObjToDto(f);
				latestApprovedPanListDto.add(latestApprovedPanDto);
			});

			logger.info("getNDLSPANByStatus , panDBList for loop end");
			panMasterTempPagableDto.setPanMasterTempDtoList(latestApprovedPanListDto);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(panMasterTempPagableDto)).build();

		} else {
			return new ServiceResponseBuilder().setStatus(true).build();
		}

	}

	@RequestMapping(value = "/getDummyPANMappingByStatus/{status}/{startRecordIndex}", method = RequestMethod.GET)
	public ServiceResponse getDummyPANMappingByStatus(@PathVariable("status") Integer status, @PathVariable("startRecordIndex") Integer startRecordIndex) {
		logger.info("request received getDummyPANMappingByStatus");

		if (status == null) {
			status = new Integer(0);
		}
		ArrayList<String> verificationStatus = new ArrayList<>();
		if (status.equals(0)) {
			verificationStatus.add(PanMappingStatusEnum.RBI_PENDING.getStatus());
			verificationStatus.add(PanMappingStatusEnum.NSDL_PENDING.getStatus());

		} else if (status.equals(1)) {
			verificationStatus.add(PanMappingStatusEnum.APPROVED.getStatus());
		} else if (status.equals(2)) {
			verificationStatus.add(PanMappingStatusEnum.REJECTED.getStatus());
			verificationStatus.add(PanMappingStatusEnum.NSDL_REJECTED.getStatus());
		}
		//EntityBean entityBean = entityRepo.findByEntityCode(entityCode);

		/*
		 * if (entityBean == null) { return new
		 * ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.
		 * toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.
		 * toString())).build(); }
		 */
		//logger.info("getDummyPANMappingByStatus , entityBean" + entityBean.getEntityCode());
		// checking pan
		List<PanMapping> panDBList = null;
		Pageable pageable;

		//setting values in paegable interface, 1st parameter is page number and 2nd is fetch size
		pageable = PageRequest.of(startRecordIndex, 5000);

		Long totalCount = null;
		PanMappingPagableDto panMappingPagableDto = new PanMappingPagableDto();

		totalCount = panMappingRepo.getTotalCount(verificationStatus);
		panDBList = panMappingRepo.findByStatusAndRequestedByEntityIdOrderByCreatedOnDesc(verificationStatus, pageable);
		//			panDBList = panMasterTempRepo.findLatestPan(verificationStatus, entityBean.getEntityId(), pageable);
		//} else {
		//totalCount = panMappingRepo.getTotalCount( entityBean.getEntityId());
		//panDBList = panMappingRepo.findByStatusAndRequestedByEntityIdOrderByCreatedOnDesc(entityBean.getEntityId(), pageable);
		//			panDBList = panMappingRepo.findByRequestedByEntityId( entityBean.getEntityId());
		//}
		panMappingPagableDto.setTotalCount(totalCount);
		logger.info("getDummyPANMappingByStatus , panDBList");
		if (panDBList != null) {

			List<PanMappingDto> latestApprovedPanListDto = new ArrayList<>();
			//			PanMasterTempDto latestApprovedPanDto;
			//			logger.info("getNDLSPANByStatus , panDBList for loop start, panDBList  "+ panDBList.size());
			//			System.out.println("getNDLSPANByStatus , panDBList for loop start, syso");
			//			for (PanMasterTemp latestApprovedDbPan : panDBList) {
			//
			//				latestApprovedPanDto = convertDBObjToDto(latestApprovedDbPan);
			//				latestApprovedPanListDto.add(latestApprovedPanDto);
			//
			//			}
			//			logger.info("getNDLSPANByStatus , panDBList for loop end");
			//			System.out.println("getNDLSPANByStatus , panDBList for loop end, syso");

			logger.info("getDummyPANMappingByStatus , panDBList for loop start, panDBList  " + panDBList.size());
			panDBList.stream().forEach(f -> {
				PanMappingDto latestApprovedPanDto = convertDBObjToDtoMapping(f);
				latestApprovedPanListDto.add(latestApprovedPanDto);
			});

			logger.info("getDummyPANMappingByStatus , panDBList for loop end");
			panMappingPagableDto.setPanMappingDtoList(latestApprovedPanListDto);
			System.out.println(new Gson().toJson(latestApprovedPanListDto));
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(panMappingPagableDto)).build();

		} else {
			return new ServiceResponseBuilder().setStatus(true).build();
		}

	}

	@PostMapping("/addPanMasterBulkData")
	public ServiceResponse addPanMasterBulkData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody PanMasterBulkDto panMasterBulkDto) {

		logger.info("Request received to add pan master bulk data for jobProcessingId" + jobProcessId);
		try {
			if (panMasterBulkDto == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}

			UserMaster userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(panMasterBulkDto.getCreatedBy());

			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0594.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0594.toString())).build();
			}

			//		EntityBean entityBean = entityRepo.findByEntityId(new Long(panMasterBulkDto.getEntityId()));
			EntityBean entityBean = entityRepo.findByEntityCode(panMasterBulkDto.getEntityCode());
			PanStatus panStatus = new PanStatus();
			panStatus.setPanStatusId(1l);
			PanMasterBulk panMasterBulk = new PanMasterBulk();
			panMasterBulk.setFileName(panMasterBulkDto.getFileName());
			panMasterBulk.setOriginalFileName(panMasterBulkDto.getOriginalFileName());
			panMasterBulk.setCreatedBy(userMaster);
			panMasterBulk.setCreatedOn(new Date());
			panMasterBulk.setEntityBean(entityBean);
			panMasterBulk.setIsActive(true);
			panMasterBulk.setIsProcessed(false);
			panMasterBulk.setStatusId(panStatus);

			panMasterBulkService.add(panMasterBulk);

			panMasterBulkDto.setId(panMasterBulk.getId());
		} catch (Exception e) {
			logger.error("Exception occoured while saving data for jobProcessingId: " + jobProcessId + " Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.info("Request completed to add pan master bulk data for jobProcessingId" + jobProcessId);
		return new ServiceResponseBuilder().setStatus(true).setResponse(panMasterBulkDto).build();
	}

	@RequestMapping(value = "/getPanMasterBulkData/{entityCode}/{langCode}", method = RequestMethod.GET)
	public ServiceResponse getPanMasterBulkData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("entityCode") String entityCode, @PathVariable("langCode") String langCode) {
		try {
			if (Validations.isEmpty(entityCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}

			EntityBean entityBean = entityRepo.findByEntityCode(entityCode);

			if (entityBean == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0108.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())).build();
			}

			// checking pan
			List<PanMasterBulk> panBulkDBList = null;

			Map<String, Object> columnValueMap = new HashMap<>();
			columnValueMap.put(ColumnConstants.ENT_CODE.getConstantVal(), entityCode);
			columnValueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), langCode);

			panBulkDBList = panMasterBulkService.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_ENTITY_CODE_AND_LANG_CODE.getConstantVal());

			if (!CollectionUtils.isEmpty(panBulkDBList)) {

				List<PanMasterBulkDto> panMasterBulkDtoList = convertPanBulkDbListObject(panBulkDBList);

				return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(panMasterBulkDtoList)).build();

			} else {
				return new ServiceResponseBuilder().setStatus(true).build();
			}
		} catch (Exception e) {
			logger.error("Exception occoured while getting data of pan bulk upload data for jobProcessingId:Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private List<PanMasterBulkDto> convertPanBulkDbListObject(List<PanMasterBulk> panBulkDBList) {

		List<PanMasterBulkDto> panMasterBulkList = new ArrayList<PanMasterBulkDto>();
		PanMasterBulkDto panMasterBulkDto = null;
		for (PanMasterBulk panMB : panBulkDBList) {
			panMasterBulkDto = new PanMasterBulkDto();

			panMasterBulkDto.setId(panMB.getId());
			panMasterBulkDto.setFileName(panMB.getFileName());
			panMasterBulkDto.setCreatedBy(panMB.getCreatedBy().getUserId());
			panMasterBulkDto.setUserName(panMB.getCreatedBy().getUserName());

			//panMasterBulk.setCreatedOn(panMB.getCreatedOn());
			panMasterBulkDto.setCreatedOn(panMB.getCreatedOn().getTime());
			panMasterBulkDto.setTotalRecords(panMB.getTotalRecords());
			panMasterBulkDto.setNumberOfSuccessfull(panMB.getNumOfSuccessfull());

			panMasterBulkDto.setStatus(panMB.getStatusId().getPanStatusId());
			panMasterBulkDto.setStatusDesc(panMB.getStatusId().getStatus());

			panMasterBulkDto.setCreatedBy(panMB.getCreatedBy().getUserId());

			panMasterBulkDto.setEntityCode(panMB.getEntityBean().getEntityCode());
			panMasterBulkDto.setEntityId(panMB.getEntityBean().getEntityId());

			if (panMB.getProcessStartTime() != null) {
				panMasterBulkDto.setProcessStartTime(panMB.getProcessStartTime().getTime());
			}
			if (panMB.getProcessEndTime() != null) {
				panMasterBulkDto.setProcessEndTime(panMB.getProcessEndTime().getTime());
			}
			panMasterBulkDto.setOriginalFileName(panMB.getOriginalFileName());
			panMasterBulkList.add(panMasterBulkDto);
		}

		return panMasterBulkList;
	}

	@RequestMapping(value = "/getPanMasterData/", method = RequestMethod.GET)
	public ServiceResponse getPanMasterData(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		try {
			logger.info("Request received to get pan master data");
			List<PanMasterDetails> panMasterDetailsList = panMasterDetailsRepo.getPanMasterData();

			if (!CollectionUtils.isEmpty(panMasterDetailsList)) {

				List<PanMasterDetailsDto> panMasterDtoList = convertPanMasterDbListToDto(panMasterDetailsList);

				return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(panMasterDtoList)).build();

			} else {
				return new ServiceResponseBuilder().setStatus(true).build();
			}
		} catch (Exception e) {
			logger.error("Exception occoured while getting data of pan master  jobProcessingId:Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@GetMapping(value = "/getRBIGeneratedPanList/{status}")
	public ServiceResponse getRBIGeneratedPanList(@PathVariable("status") String status) {
		if (status == null) {
			status = PanMappingStatusEnum.RBI_PENDING.getStatus();
		}

		List<PanMasterTemp> panDBList = panMasterTempRepo.findByStatusAndRbiGeneratedTrueOrderByCreatedOnAsc(status);

		if (!CollectionUtils.isEmpty(panDBList)) {
			List<PanMasterTempDto> rbiGeneratedPanListDto = new ArrayList<>();
			PanMasterTempDto rbiGeneratedPanDto;
			for (PanMasterTemp rbiGeneratedDbPan : panDBList) {
				rbiGeneratedPanDto = convertDBObjToDto(rbiGeneratedDbPan);
				rbiGeneratedPanListDto.add(rbiGeneratedPanDto);
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(rbiGeneratedPanListDto)).build();
		} else {
			return new ServiceResponseBuilder().setStatus(true).build();
		}
	}

	@GetMapping(value = "/getRBIMappedPanList/{verificationStatus}")
	public ServiceResponse getRBIMappedPanList(@PathVariable("verificationStatus") String verificationStatus) {

		List<PanMapping> panDBList = panMappingRepo.findByStatusOrderByCreatedOnDesc(verificationStatus);

		if (!CollectionUtils.isEmpty(panDBList)) {
			List<PanMappingDto> rbiGeneratedPanListDto = new ArrayList<>();
			PanMappingDto rbiGeneratedPanDto;
			for (PanMapping rbiGeneratedDbPan : panDBList) {
				rbiGeneratedPanDto = convertDBObjToDtoMapping(rbiGeneratedDbPan);
				rbiGeneratedPanListDto.add(rbiGeneratedPanDto);
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(rbiGeneratedPanListDto)).build();
		} else {
			return new ServiceResponseBuilder().setStatus(true).build();
		}
	}

	@PostMapping(value = "/approveRbiGeberatedPan")
	public ServiceResponse approveRbiGeberatedPan(@RequestBody PanMasterTempDto panMasterTempDto) {

		if (panMasterTempDto == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
		}

		if (panMasterTempDto.getPanNumber() == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0760.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0760.toString())).build();
		}

		if (panMasterTempDto.getStatus().equalsIgnoreCase(PanMappingStatusEnum.REJECTED.getStatus()) && panMasterTempDto.getComment() == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0199.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0199.toString())).build();
		}

		UserMaster userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(new Long(panMasterTempDto.getApprovedByFk()));

		if (userMaster == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0594.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0594.toString())).build();
		}

		try {

			int dbResp = panMasterTempRepo.updateStatus(panMasterTempDto.getStatus(), panMasterTempDto.getComment(), new Long(panMasterTempDto.getApprovedByFk()), panMasterTempDto.getPanNumber());
			System.out.println(dbResp);

			if (panMasterTempDto.getStatus().equalsIgnoreCase(PanMappingStatusEnum.APPROVED.getStatus())) {
				PanMasterTemp panObj = panMasterTempRepo.getPanDetails(panMasterTempDto.getPanNumber());
				if (panObj != null) {
					PanMaster panMasterObj;
					panMasterObj = new PanMaster();
					panMasterObj.setPanNumber(panObj.getPanNumber());
					logger.info("pan Number temp id " + panObj.getPanId());
					logger.info("pan Number to insert in master table " + panMasterObj.getPanNumber());
					panMasterObj.setBorrowerName(panObj.getBorrowerName());
					panMasterObj.setBorrowerTitle(panObj.getBorrowerTitle());
					panMasterObj.setBorrowerMobile(panObj.getBorrowerMobile());
					panMasterObj.setBorrowerAlternateName(panObj.getBorrowerAlternateName());
					panMasterObj.setRbiGenerated(panObj.getRbiGenerated());
					panMasterObj.setInstitutionType(panObj.getInstitutionType());
					panMasterObj.setBorrowerDob(panObj.getBorrowerDob());
					panMasterObj.setSupportingDocType(panObj.getSupportingDocType());
					panMasterObj.setSupportingDocIdentityNum(panObj.getSupportingDocIdentityNum());
					UserMaster userModifyObj = new UserMaster();
					userModifyObj.setUserId(panMasterTempDto.getLastModifiedByUserId());
					panMasterObj.setLastModifiedBy(userModifyObj);
					panMasterObj.setLastModifiedOn(DateManip.getCurrentDateTime());
					panMasterObj.setSupportDocName(panObj.getSupportDocName());
					panMasterObj.setIsActive(true);

					//panMasterObj.setStatus(panObj.getStatus());

					panMasterRepo.save(panMasterObj);
				}

			}

			return new ServiceResponseBuilder().setStatus(true).build();
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

	}

	@PostMapping(value = "/approveRbiPanToActualPanMapping")
	public ServiceResponse approveRbiPanMapping(@RequestBody PanMappingDto panMappingDto) {

		if (panMappingDto == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
		}

		if (panMappingDto.getStatus() == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0958.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0958.toString())).build();
		}

		UserMaster userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(new Long(panMappingDto.getApprovedByFk()));

		if (userMaster == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0594.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0594.toString())).build();
		}

		try {
			//List<String> panStringList = new ArrayList<>(Arrays.asList(panMappingDto.getPanList()));
			//System.out.println(panStringList);

			//List<PanMapping> panObj = panMappingRepo.getPanList(panStringList, 0);
			/*
			 *  }
			 */

			int count = panMappingRepo.updateVerificationStatus(panMappingDto.getStatus(), panMappingDto.getComment(), new Long(panMappingDto.getApprovedByFk()), panMappingDto.getPanMappingId());
			if (count > 0) {
				String panNumber = panMappingRepo.findDummyPanNumber(panMappingDto.getPanMappingId());

				if (panNumber != null && panMappingDto.getStatus().equalsIgnoreCase(PanMappingStatusEnum.APPROVED.getStatus())) {

					int status = panMasterRepo.updatePanStatus(panNumber, new Long(panMappingDto.getApprovedByFk()));
				}
			}

			//			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(panWithStatus)).build();
			return new ServiceResponseBuilder().setStatus(true).build();
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

	}

	private List<PanMasterDetailsDto> convertPanMasterDbListToDto(List<PanMasterDetails> panMasterDetailsList) {
		List<PanMasterDetailsDto> panMasterDtoList = new ArrayList<>();
		try {
			PanMasterDetailsDto panMasterDetailsDto;
			for (PanMasterDetails panMasterDetails : panMasterDetailsList) {
				panMasterDetailsDto = new PanMasterDetailsDto();
				panMasterDetailsDto.setId(panMasterDetails.getId());
				panMasterDetailsDto.setFileName(panMasterDetails.getFileName());
				panMasterDetailsDto.setCreatedOn(panMasterDetails.getCreatedOn().getTime());
				panMasterDetailsDto.setStatus(panMasterDetails.getStatus());
				if (panMasterDetails.getProcessStartTime() != null) {
					panMasterDetailsDto.setProcessStartTime(panMasterDetails.getProcessStartTime().getTime());
				}

				if (panMasterDetails.getProcessEndTime() != null) {
					panMasterDetailsDto.setProcessEndTime(panMasterDetails.getProcessEndTime().getTime());
				}

				panMasterDetailsDto.setTotalRecords(panMasterDetails.getTotalRecords());
				panMasterDetailsDto.setModifiedFileName(panMasterDetails.getModifiedFileName());
				panMasterDtoList.add(panMasterDetailsDto);
				if (panMasterDetailsDto.getStatus() == 3) {
					return panMasterDtoList;
				}
				if (panMasterDtoList.size() == 2) {
					return panMasterDtoList;
				}
			}
		} catch (Exception e) {
			logger.error("Exception occoured while convertPanMasterDbListToDto" + e);
		}
		return panMasterDtoList;
	}

	@PostMapping(value = "/getPanMasterDetails")
	public ServiceResponse getPanMasterDetails() {
		ServiceResponse response = null;
		try {

			Options options = new Options();
			List<PanMaster> panMasterList = panMasterRepo.getPanMasterDataWithCorporateName();
			if (CollectionUtils.isEmpty(panMasterList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (PanMaster panMasterValues : panMasterList) {
				option = new Option();
				option.setKey(String.valueOf(panMasterValues.getPanMasterId()));
				option.setValue(panMasterValues.getPanNumber());
				listOfOption.add(option);
			}

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;

		} catch (NullArgumentException nae) {
			logger.error("Exception occoured while featch pan master list" + nae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@PostMapping(value = "/getPanMasterDataWithCorporateName")
	public ServiceResponse getPanMasterDataWithCorporateName() {
		ServiceResponse response = null;
		try {

			Options options = new Options();
			List<PanMaster> panMasterList = panMasterRepo.getPanMasterDataWithCorporateName();
			if (CollectionUtils.isEmpty(panMasterList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (PanMaster panMasterValues : panMasterList) {
				option = new Option();
				option.setKey(panMasterValues.getPanMasterId() + "~" + panMasterValues.getBorrowerName());
				option.setValue(panMasterValues.getPanNumber());
				listOfOption.add(option);
			}

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;

		} catch (NullArgumentException nae) {
			logger.error("Exception occoured while featch pan master list" + nae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	// this method would use to fetch the pan info with the help of pan number and borrower name as a input parameter 
	@PostMapping(value = "/getPanInfoForNonXBRLReturns")
	public ServiceResponse getPanInfoForNonXBRLReturns(@RequestHeader(name = "JobProcessingId") String JobProcessingId, @RequestBody LabelDto labelDto) {
		logger.info("Request received to get pan info for non xbrl return: " + JobProcessingId);

		try {
			if (!CollectionUtils.isEmpty(labelDto.getLabelKeyMap())) {
				Set<String> panNumSet = new HashSet<>();

				labelDto.getLabelKeyMap().forEach((k, v) -> {
					if (StringUtils.isNotBlank(v)) {

						String[] panNumBorrowerNameArr = v.split(GeneralConstants.TILDA_SEPERATOR.getConstantVal());

						PanMaster panMaster = panMasterRepo.getPanInfoByPanNumAndBorrowerName(panNumBorrowerNameArr[0], panNumBorrowerNameArr[1]);

						if (labelDto.getIsPanAndBorrowerRequiredInResponse() != null && labelDto.getIsPanAndBorrowerRequiredInResponse()) {
							panNumSet.add(panMaster.getPanNumber().toUpperCase() + GeneralConstants.TILDA_SEPERATOR.getConstantVal() + panMaster.getBorrowerName().toUpperCase());
						} else if (panMaster != null) {
							panNumSet.add(panMaster.getPanNumber().toUpperCase());
						}
					}
				});

				if (!CollectionUtils.isEmpty(panNumSet)) {
					logger.info("Request completed to get pan info for non xbrl return: panMasterList" + panNumSet.size());
					return new ServiceResponseBuilder().setStatus(Boolean.TRUE).setResponse(new Gson().toJson(panNumSet)).build();
				}
			}
		} catch (Exception e) {
			logger.info("Exception occoured while getting pan info for non xbrl return" + e);
		}
		logger.info("Request completed to get pan info for non xbrl return: Blank response");
		return new ServiceResponseBuilder().setStatus(Boolean.TRUE).build();

	}

	@PostMapping("/getCustomizePanMasterDetails/{panNumber}")
	public ServiceResponse getCustomizePanMasterDetails(@PathVariable(name = "panNumber") String panNumber) {
		ServiceResponse response = null;
		try {

			Options options = new Options();
			List<PanMaster> panMasterList = new ArrayList<>();
			if (!Validations.isEmpty(panNumber) && panNumber.length() >= 3) {
				panMasterList = panMasterRepo.getCustomisedPanMasterData(panNumber.toUpperCase());
			}
			if (CollectionUtils.isEmpty(panMasterList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (PanMaster panMasterValues : panMasterList) {
				option = new Option();
				option.setKey(panMasterValues.getPanNumber());
				option.setValue(panMasterValues.getBorrowerName());
				listOfOption.add(option);
			}

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;

		} catch (Exception nae) {
			logger.error("Exception occoured while featch CustomizePanMasterDetails list" + nae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@RequestMapping(value = "/validatePanAndGetCorporateName/{panNumber}", method = RequestMethod.GET)
	public ServiceResponse validatePanAndGetCorporateName(@PathVariable("panNumber") String panNumber) {
		ServiceResponse response = null;
		try {
			if (Validations.isEmpty(panNumber)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0760.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0760.toString())).build();
			}

			PanMaster panMaster = panMasterRepo.findByPanNumber(panNumber.toUpperCase());
			if (panMaster == null) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(panMaster.getBorrowerName());
			return response;

		} catch (NullArgumentException nae) {
			logger.error("Exception occoured while featch pan master list" + nae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@GetMapping(value = "/fetchInstitutionTypeList")
	public ServiceResponse fetchInstitutionTypeList(@RequestHeader(name = "AppId") String jobProcessingId, @RequestHeader(name = "UUID") String uuid) {
		ServiceResponse serviceResponse = null;
		try {
			logger.info("API call start of fetch Institution Type DropdownList");

			DynamicDropDownBean option;

			List<DynamicDropDownBean> optionList = new ArrayList<>();

			List<InstitutionType> institutionTypeList = institutionTypeRepo.getInstitutionTypeList();
			if (CollectionUtils.isEmpty(institutionTypeList)) {
				logger.error("Exception while fetching Institution Type drop down , if drop down type list is empty");
				serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
				serviceResponse.setResponse(null);
				return serviceResponse;
			}
			for (InstitutionType institutionTypeObj : institutionTypeList) {
				option = new DynamicDropDownBean();
				option.setKey(institutionTypeObj.getInstTypeId());
				option.setValue(institutionTypeObj.getInstTypeName());
				optionList.add(option);
			}

			String jsonResult = new Gson().toJson(optionList);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(jsonResult);

		} catch (Exception e) {
			logger.error("Error in API call fetch Institution Type DropdownList" + e);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return serviceResponse;
	}

	@GetMapping(value = "/fetchSupportingDocTypeList")
	public ServiceResponse fetchSupportingDocTypeList(@RequestHeader(name = "AppId") String jobProcessingId, @RequestHeader(name = "UUID") String uuid) {
		ServiceResponse serviceResponse = null;
		try {
			logger.info("API call start of fetch Supporting Doc Type DropdownList");

			DynamicDropDownBean option;

			List<DynamicDropDownBean> optionList = new ArrayList<>();

			List<SupportingDocType> supportingDocTypeList = supportingDocTypeRepo.getSupportingDocTypeList();
			if (CollectionUtils.isEmpty(supportingDocTypeList)) {
				logger.error("Exception while fetching ISupporting Doc Type drop down , if drop down type list is empty");
				serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
				serviceResponse.setResponse(null);
				return serviceResponse;
			}
			for (SupportingDocType supportingDocTypeObj : supportingDocTypeList) {
				option = new DynamicDropDownBean();
				option.setKey(Long.valueOf(supportingDocTypeObj.getDocTypeId()));
				option.setValue(supportingDocTypeObj.getDocType());
				optionList.add(option);
			}

			String jsonResult = new Gson().toJson(optionList);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(jsonResult);

		} catch (Exception e) {
			logger.error("Error in API call fetch Supporting Doc Type DropdownList" + e);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return serviceResponse;
	}

	@PostMapping("/getCustomizePanMasterDetailsByBorrowerName/{borrowerName}")
	public ServiceResponse getCustomizePanMasterDetailsByBorrowerName(@PathVariable(name = "borrowerName") String borrowerName) {
		ServiceResponse response = null;
		logger.info("API call start of getCustomizePanMasterDetailsByBorrowerName");
		try {

			Options options = new Options();
			List<PanMaster> panMasterList = new ArrayList<>();
			if (!Validations.isEmpty(borrowerName) && borrowerName.length() >= 3) {
				panMasterList = panMasterRepo.getCustomisedPanMasterDataByBorrowerName(borrowerName);
			}
			if (CollectionUtils.isEmpty(panMasterList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (PanMaster panMasterValues : panMasterList) {
				option = new Option();
				option.setKey(panMasterValues.getBorrowerName());
				option.setValue(panMasterValues.getPanNumber());
				listOfOption.add(option);
			}

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;

		} catch (Exception nae) {
			logger.error("Exception occoured while featch getCustomizePanMasterDetailsByBorrowerName list" + nae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

}
