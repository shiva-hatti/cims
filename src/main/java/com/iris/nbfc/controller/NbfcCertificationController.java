package com.iris.nbfc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.Nationality;
import com.iris.model.UserMaster;
import com.iris.nbfc.model.NbfcCapitalType;
import com.iris.nbfc.model.NbfcCertificationDetails;
import com.iris.nbfc.model.NbfcCertificationDetailsDto;
import com.iris.nbfc.model.NbfcCompanyClass;
import com.iris.nbfc.model.NbfcCompanyType;
import com.iris.nbfc.model.NbfcCompanyTypeOther;
import com.iris.nbfc.model.NbfcCorRegistrationBean;
import com.iris.nbfc.model.NbfcCorRegistrationStatus;
import com.iris.nbfc.model.NbfcDirectorType;
import com.iris.nbfc.model.NbfcNoteMessages;
import com.iris.nbfc.model.NbfcPageMaster;
import com.iris.nbfc.model.NbfcRegionalOfficeMaster;
import com.iris.nbfc.model.NbfcRocMaster;
import com.iris.nbfc.model.NbfcRoute;
import com.iris.nbfc.model.NbfcShareHolder;
import com.iris.nbfc.model.NbfcStatus;
import com.iris.nbfc.model.NbfcSubPageMaster;
import com.iris.nbfc.model.NbfcTypeCompany;
import com.iris.nbfc.service.NbfcCapitalTypeService;
import com.iris.nbfc.service.NbfcCertificationDetailsService;
import com.iris.nbfc.service.NbfcCorRegistrationStatusService;
import com.iris.nbfc.service.NbfcRouteService;
import com.iris.nbfc.service.NbfcShareHolderService;
import com.iris.nbfc.service.NbfcStatusService;
import com.iris.sdmx.status.entity.AdminStatus;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.MethodConstants;

/**
 * @author pmohite
 */
@RestController
@RequestMapping("/service/nbfcCertiController")
public class NbfcCertificationController {

	private static final Logger Logger = LogManager.getLogger(NbfcCertificationController.class);

	@Autowired
	private GenericService<NbfcCompanyType, Long> nbfcCompanyTypeService;

	@Autowired
	private GenericService<NbfcCompanyTypeOther, Long> nbfcCompanyTypeOtherService;

	@Autowired
	private GenericService<NbfcCompanyClass, Long> nbfcCompanyClassService;

	@Autowired
	private GenericService<NbfcRocMaster, Long> nbfcRocService;

	@Autowired
	private GenericService<NbfcRegionalOfficeMaster, Long> nbfcRegionalOfficeService;

	@Autowired
	private NbfcCertificationDetailsService nbfcCertificationDetailsService;

	@Autowired
	private GenericService<NbfcNoteMessages, Integer> nbfcNoteMessagesService;

	@Autowired
	private NbfcCapitalTypeService nbfcCapitalTypeService;

	@Autowired
	private NbfcShareHolderService nbfcShareHolderService;

	@Autowired
	private NbfcRouteService nbfcRouteService;

	@Autowired
	NbfcStatusService nbfcStatusService;

	@Autowired
	GenericService<NbfcTypeCompany, Long> nbfcTypeCompanyService;

	@Autowired
	GenericService<NbfcPageMaster, Long> nbfcPageMasterService;

	@Autowired
	GenericService<NbfcDirectorType, Long> nbfcDirectorTypeService;

	@Autowired
	GenericService<Nationality, Long> nbfcNationalityService;

	@Autowired
	GenericService<NbfcSubPageMaster, Long> nbfcSubPageMasterService;

	@Autowired
	private NbfcCorRegistrationStatusService nbfcCorRegistrationStatusService;

	@GetMapping(value = "/getAllNbfcCompanyType", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getAllNbfcCompanyType(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcCompanyType> nbfcCompanyTypeList = null;
		try {
			nbfcCompanyTypeList = nbfcCompanyTypeService.getActiveDataFor(NbfcCompanyType.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcCompanyType list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0862.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0862.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcCompanyType list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcCompanyTypeList)) {
			Logger.error("Exception while fetching nbfcCompanyType list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0862.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0862.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcCompanyTypeList);
			return response;
		}
	}

	@GetMapping(value = "/getAllNbfcCompanyTypeOther", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getAllNbfcCompanyTypeOther(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcCompanyTypeOther> nbfcCompanyTypeOtherList = null;
		try {
			nbfcCompanyTypeOtherList = nbfcCompanyTypeOtherService.getActiveDataFor(NbfcCompanyTypeOther.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcCompanyTypeOther list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0863.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0863.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcCompanyTypeOther list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcCompanyTypeOtherList)) {
			Logger.error("Exception while fetching nbfcCompanyTypeOtherlist info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0863.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0863.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcCompanyTypeOtherList);
			return response;
		}
	}

	@GetMapping(value = "/getAllNbfcCompanyClass", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getAllNbfcCompanyClass(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcCompanyClass> nbfcCompanyClassList = null;
		try {
			nbfcCompanyClassList = nbfcCompanyClassService.getActiveDataFor(NbfcCompanyClass.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcCompanyClasslist info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0864.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0864.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcCompanyClass list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcCompanyClassList)) {
			Logger.error("Exception while fetching nbfcCompanyClass list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0864.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0864.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcCompanyClassList);
			return response;
		}
	}

	@GetMapping(value = "/getAllNbfcRoc", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getAllNbfcRoc(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcRocMaster> nbfcRocList = null;
		try {
			nbfcRocList = nbfcRocService.getActiveDataFor(NbfcRocMaster.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcRoc list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0865.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0865.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcRoc list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcRocList)) {
			Logger.error("Exception while fetching nbfcRoc list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0865.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0865.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcRocList);
			return response;
		}
	}

	@GetMapping(value = "/getAllNbfcRegionalOffice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getAllNbfcRegionalOffice(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcRegionalOfficeMaster> nbfcRegionalOfficeMasterList = null;
		try {
			nbfcRegionalOfficeMasterList = nbfcRegionalOfficeService.getActiveDataFor(NbfcRegionalOfficeMaster.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcRegionalOfficeMaster list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0866.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0866.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcRegionalOfficeMaster list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcRegionalOfficeMasterList)) {
			Logger.error("Exception while fetching nbfcRegionalOfficeMaster list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0866.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0866.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcRegionalOfficeMasterList);
			return response;
		}
	}

	/**
	 * save the ReturnGroupMapping. This method is to save ReturnGroupMapping .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingDto
	 * @return
	 */
	@PostMapping(value = "/saveNbfcCertificationDetails")
	public ServiceResponse saveNbfcCertificationDetails(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NbfcCertificationDetailsDto nbfcCertificationDetailsDto) {
		NbfcCertificationDetails nbfcCertificationDetails = new NbfcCertificationDetails();
		List<NbfcCertificationDetails> nbfcCertificationDetailsList;
		Boolean flag = false;
		ServiceResponse response = null;
		try {
			Map<String, List<Long>> columnValueMap = new HashMap<>();
			List<Long> entityIdList = new ArrayList<>();
			entityIdList.add(nbfcCertificationDetailsDto.getCorRegIdFk());
			columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), entityIdList);
			List<Long> pageNoList = new ArrayList<>();
			pageNoList.add(nbfcCertificationDetailsDto.getPageNo());
			columnValueMap.put(ColumnConstants.FORM_PAGE_NO.getConstantVal(), pageNoList);
			if (nbfcCertificationDetailsDto.getPageNo().equals(11L)) {
				List<Long> subPageNoList = new ArrayList<>();
				subPageNoList.add(nbfcCertificationDetailsDto.getSubPageNo());
				columnValueMap.put(ColumnConstants.SUB_FORM_PAGE_NO.getConstantVal(), subPageNoList);
				if (!Objects.isNull(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()) && nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId() != 0) {
					List<Long> companyOtherTypeIdList = new ArrayList<>();
					companyOtherTypeIdList.add(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
					columnValueMap.put(ColumnConstants.COMAPNY_OTHER_TYPE_ID.getConstantVal(), companyOtherTypeIdList);
				}
			}
			nbfcCertificationDetailsList = nbfcCertificationDetailsService.getDataByColumnLongValue(columnValueMap, MethodConstants.GET_NBFC_CERTIFICATION_DETAILS_BY_ENTITY_ID.getConstantVal());
			if (!CollectionUtils.isEmpty(nbfcCertificationDetailsList) && nbfcCertificationDetailsList.get(0) != null) {
				nbfcCertificationDetails = nbfcCertificationDetailsList.get(0);
				if (nbfcCertificationDetailsDto.getPageNo().equals(13L) && !nbfcCertificationDetails.getNbfcCompanyTypeOtherIdFk().getCompanyTypeOtherId().equals(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()))) {
					NbfcCompanyType nbfcCompanyType = new NbfcCompanyType();
					nbfcCompanyType.setCompanyTypeId(nbfcCertificationDetailsDto.getNbfcCompanyTypeId());
					nbfcCertificationDetails.setNbfcCompanyTypeIdFk(nbfcCompanyType);
					NbfcCompanyTypeOther nbfcCompanyTypeOther = new NbfcCompanyTypeOther();
					nbfcCompanyTypeOther.setCompanyTypeOtherId(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
					nbfcCertificationDetails.setNbfcCompanyTypeOtherIdFk(nbfcCompanyTypeOther);
				}
				if ((nbfcCertificationDetailsDto.getPageNo().equals(14L) || nbfcCertificationDetailsDto.getPageNo().equals(15L)) && !Objects.isNull(nbfcCertificationDetailsDto.getNbfcCompanyTypeId())) {
					if (nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId() == 0) {
						NbfcCompanyType nbfcCompanyType = new NbfcCompanyType();
						nbfcCompanyType.setCompanyTypeId(nbfcCertificationDetailsDto.getNbfcCompanyTypeId());
						nbfcCertificationDetails.setNbfcCompanyTypeIdFk(nbfcCompanyType);
						nbfcCertificationDetails.setNbfcCompanyTypeOtherIdFk(null);
					} else {
						NbfcCompanyTypeOther nbfcCompanyTypeOther = new NbfcCompanyTypeOther();
						NbfcCompanyType nbfcCompanyType = new NbfcCompanyType();
						nbfcCompanyTypeOther.setCompanyTypeOtherId(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
						nbfcCompanyType.setCompanyTypeId(nbfcCertificationDetailsDto.getNbfcCompanyTypeId());
						nbfcCertificationDetails.setNbfcCompanyTypeIdFk(nbfcCompanyType);
						nbfcCertificationDetails.setNbfcCompanyTypeOtherIdFk(nbfcCompanyTypeOther);
					}
				}
				nbfcCertificationDetails.setFormPageJsonValue(nbfcCertificationDetailsDto.getFormPageJsonValue());
				nbfcCertificationDetails.setUpdatedOn(new Date());

				flag = nbfcCertificationDetailsService.update(nbfcCertificationDetails);
				if (Boolean.TRUE.equals(flag)) {
					response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
					UserMaster user = new UserMaster();
					user.setUserId(nbfcCertificationDetailsDto.getNbfcUserIdFk());
					nbfcCertificationDetails.setUserIdFk(user);
					user.setUserId(nbfcCertificationDetails.getCorRegIdFk().getUser().getUserId());
					nbfcCertificationDetails.getCorRegIdFk().setUser(user);
					response.setResponse(nbfcCertificationDetails);
				} else {
					response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
				}
				Logger.info("request completed to save NbfcEntityBean for job processingid" + jobProcessId);
				return response;
			} else {
				nbfcCertificationDetails.setFormPageJsonValue(nbfcCertificationDetailsDto.getFormPageJsonValue());
				NbfcCorRegistrationBean nbfcCorRegistrationBean = new NbfcCorRegistrationBean();
				nbfcCorRegistrationBean.setCorRegistrationId(nbfcCertificationDetailsDto.getCorRegIdFk());
				nbfcCertificationDetails.setCorRegIdFk(nbfcCorRegistrationBean);
				UserMaster user = new UserMaster();
				user.setUserId(nbfcCertificationDetailsDto.getNbfcUserIdFk());
				nbfcCertificationDetails.setUserIdFk(user);
				nbfcCertificationDetails.setCreatedOn(new Date());
				NbfcPageMaster nbfcPageMaster = new NbfcPageMaster();
				nbfcPageMaster.setPageMasterId(nbfcCertificationDetailsDto.getPageNo());
				if (nbfcCertificationDetailsDto.getPageNo().equals(11L)) {
					NbfcSubPageMaster nbfcSubPageMaster = new NbfcSubPageMaster();
					nbfcSubPageMaster.setSubPageMasterId(nbfcCertificationDetailsDto.getSubPageNo());
					nbfcCertificationDetails.setNbfcSubPageMaster(nbfcSubPageMaster);
					if (!Objects.isNull(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()) && nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId() != 0) {
						NbfcCompanyTypeOther nbfcCompanyTypeOther = new NbfcCompanyTypeOther();
						nbfcCompanyTypeOther.setCompanyTypeOtherId(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
						nbfcCertificationDetails.setNbfcCompanyTypeOtherIdFk(nbfcCompanyTypeOther);
					}
				}
				if (nbfcCertificationDetailsDto.getPageNo().equals(13L) && !Objects.isNull(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()) && nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId() != 0) {
					NbfcCompanyType nbfcCompanyType = new NbfcCompanyType();
					nbfcCompanyType.setCompanyTypeId(nbfcCertificationDetailsDto.getNbfcCompanyTypeId());
					nbfcCertificationDetails.setNbfcCompanyTypeIdFk(nbfcCompanyType);
					NbfcCompanyTypeOther nbfcCompanyTypeOther = new NbfcCompanyTypeOther();
					nbfcCompanyTypeOther.setCompanyTypeOtherId(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
					nbfcCertificationDetails.setNbfcCompanyTypeOtherIdFk(nbfcCompanyTypeOther);
				}
				if ((nbfcCertificationDetailsDto.getPageNo().equals(14L) || nbfcCertificationDetailsDto.getPageNo().equals(15L)) && !Objects.isNull(nbfcCertificationDetailsDto.getNbfcCompanyTypeId())) {
					if (nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId() == 0) {
						NbfcCompanyType nbfcCompanyType = new NbfcCompanyType();
						nbfcCompanyType.setCompanyTypeId(nbfcCertificationDetailsDto.getNbfcCompanyTypeId());
						nbfcCertificationDetails.setNbfcCompanyTypeIdFk(nbfcCompanyType);
					} else {
						NbfcCompanyTypeOther nbfcCompanyTypeOther = new NbfcCompanyTypeOther();
						NbfcCompanyType nbfcCompanyType = new NbfcCompanyType();
						nbfcCompanyTypeOther.setCompanyTypeOtherId(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
						nbfcCompanyType.setCompanyTypeId(nbfcCertificationDetailsDto.getNbfcCompanyTypeId());
						nbfcCertificationDetails.setNbfcCompanyTypeIdFk(nbfcCompanyType);
						nbfcCertificationDetails.setNbfcCompanyTypeOtherIdFk(nbfcCompanyTypeOther);
					}
				}
				nbfcCertificationDetails.setNbfcPageMaster(nbfcPageMaster);
				nbfcCertificationDetails = nbfcCertificationDetailsService.add(nbfcCertificationDetails);
			}
		} catch (ServiceException e) {
			Logger.error("Request object not proper to save ");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0903.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0903.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while saving NbfcEntityBean info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0903.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0903.toString())).build();
		}
		response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(nbfcCertificationDetails);
		Logger.info("request completed to save NbfcEntityBean for job processingid" + jobProcessId);
		return response;
	}

	@GetMapping(value = "/getNbfcNoteMessages/{pageNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcNoteMessages(@PathVariable(value = "pageNo") Long pageNo, @RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcNoteMessages> nbfcNoteMessagesList = null;
		try {
			Map<String, List<Long>> columnValueMap = new HashMap<>();
			List<Long> pageId = new ArrayList<>();
			pageId.add(pageNo);
			columnValueMap.put(ColumnConstants.FORM_PAGE_NO.getConstantVal(), pageId);
			nbfcNoteMessagesList = nbfcNoteMessagesService.getDataByColumnLongValue(columnValueMap, MethodConstants.GET_NBFC_NOTE_MESSAGES_BY_NBFC_PAGE_MASTER.getConstantVal());
		} catch (ServiceException e) {
			Logger.error("Exception while fetching NBFC Note Messages info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0836.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0836.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching NBFC Note Messages info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcNoteMessagesList)) {
			Logger.error("Exception while fetching NBFC Note Messages info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0836.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0836.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcNoteMessagesList);
			return response;
		}
	}

	@PostMapping(value = "/getNbfcCertificationDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcCertificationDetails(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NbfcCertificationDetailsDto nbfcCertificationDetailsDto) {
		ServiceResponse response;
		List<NbfcCertificationDetails> nbfcCertificationDetailsList = new ArrayList<>();
		NbfcCertificationDetails nbfcCertificationDetails = null;
		try {
			if (nbfcCertificationDetailsDto.getNbfcUserIdFk() != null && nbfcCertificationDetailsDto.getPageNo() != null) {
				Map<String, List<Long>> columnValueMap = new HashMap<>();
				List<Long> userIdList = new ArrayList<>();
				userIdList.add(nbfcCertificationDetailsDto.getNbfcUserIdFk());
				columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), userIdList);
				List<Long> pageNoList = new ArrayList<>();
				pageNoList.add(nbfcCertificationDetailsDto.getPageNo());
				columnValueMap.put(ColumnConstants.FORM_PAGE_NO.getConstantVal(), pageNoList);
				if (nbfcCertificationDetailsDto.getPageNo().equals(11L)) {
					List<Long> subPageNoList = new ArrayList<>();
					subPageNoList.add(nbfcCertificationDetailsDto.getSubPageNo());
					columnValueMap.put(ColumnConstants.SUB_FORM_PAGE_NO.getConstantVal(), subPageNoList);
					if (!Objects.isNull(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()) && nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId() != 0) {
						List<Long> companyTypeIdList = new ArrayList<>();
						companyTypeIdList.add(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeId()));
						columnValueMap.put(ColumnConstants.COMAPNY_TYPE_ID.getConstantVal(), companyTypeIdList);
						List<Long> companyOtherTypeIdList = new ArrayList<>();
						companyOtherTypeIdList.add(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
						columnValueMap.put(ColumnConstants.COMAPNY_OTHER_TYPE_ID.getConstantVal(), companyOtherTypeIdList);
					}
				}
				if (nbfcCertificationDetailsDto.getPageNo().equals(13L)) {
					List<Long> companyTypeIdList = new ArrayList<>();
					companyTypeIdList.add(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeId()));
					columnValueMap.put(ColumnConstants.COMAPNY_TYPE_ID.getConstantVal(), companyTypeIdList);
					List<Long> companyOtherTypeIdList = new ArrayList<>();
					companyOtherTypeIdList.add(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
					columnValueMap.put(ColumnConstants.COMAPNY_OTHER_TYPE_ID.getConstantVal(), companyOtherTypeIdList);
				}
				if ((nbfcCertificationDetailsDto.getPageNo().equals(14L) || nbfcCertificationDetailsDto.getPageNo().equals(15L))) {
					List<Long> companyOtherTypeIdList = new ArrayList<>();
					List<Long> companyTypeIdList = new ArrayList<>();
					companyTypeIdList.add(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeId()));
					companyOtherTypeIdList.add(Long.valueOf(nbfcCertificationDetailsDto.getNbfcCompanyTypeOtherId()));
					columnValueMap.put(ColumnConstants.COMAPNY_TYPE_ID.getConstantVal(), companyTypeIdList);
					columnValueMap.put(ColumnConstants.COMAPNY_OTHER_TYPE_ID.getConstantVal(), companyOtherTypeIdList);
				}
				nbfcCertificationDetailsList = nbfcCertificationDetailsService.getDataByColumnLongValue(columnValueMap, MethodConstants.GET_NBFC_CERTIFICATION_DETAILS_BY_USER_ID.getConstantVal());
			} else if (nbfcCertificationDetailsDto.getNbfcUserIdFk() != null && nbfcCertificationDetailsDto.getCorRegIdFk() != null) {
				Map<String, List<Long>> columnValueMap = new HashMap<>();
				List<Long> userIdList = new ArrayList<>();
				userIdList.add(nbfcCertificationDetailsDto.getNbfcUserIdFk());
				columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), userIdList);
				List<Long> entityIdList = new ArrayList<>();
				entityIdList.add(nbfcCertificationDetailsDto.getCorRegIdFk());
				columnValueMap.put(ColumnConstants.ENTITY_BEAN.getConstantVal(), entityIdList);
				nbfcCertificationDetailsList = nbfcCertificationDetailsService.getDataByColumnLongValue(columnValueMap, MethodConstants.GET_ALL_NBFC_CERTIFICATION_DETAILS_BY_USER_ID.getConstantVal());
			} else {
				Logger.error("Exception while fetching NbfcCertification Details info for job processingid " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching NbfcCertification Details info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching NbfcCertification Details info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (!CollectionUtils.isEmpty(nbfcCertificationDetailsList)) {
			if (nbfcCertificationDetailsDto.getCorRegIdFk() != null) {
				for (NbfcCertificationDetails nbfcCertificationDetailsData : nbfcCertificationDetailsList) {
					UserMaster user = new UserMaster();
					user.setUserId(nbfcCertificationDetailsData.getUserIdFk().getUserId());
					nbfcCertificationDetailsData.setUserIdFk(user);
					user = new UserMaster();
					user.setUserId(nbfcCertificationDetailsData.getCorRegIdFk().getUser().getUserId());
					nbfcCertificationDetailsData.getCorRegIdFk().setUser(user);
				}
				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(nbfcCertificationDetailsList);
				return response;
			} else {
				nbfcCertificationDetails = nbfcCertificationDetailsList.get(0);
				if (nbfcCertificationDetails == null) {
					Logger.error("Exception while fetching NbfcCertification   Details info for job processingid " + jobProcessId);
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
				} else {
					UserMaster user = new UserMaster();
					user.setUserId(nbfcCertificationDetails.getUserIdFk().getUserId());
					nbfcCertificationDetails.setUserIdFk(user);
					user = new UserMaster();
					user.setUserId(nbfcCertificationDetails.getCorRegIdFk().getUser().getUserId());
					nbfcCertificationDetails.getCorRegIdFk().setUser(user);
					response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
					response.setResponse(nbfcCertificationDetails);
					return response;
				}
			}
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcCertificationDetails);
			return response;
		}
	}

	@GetMapping(value = "/getNbfcCapitalType", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcCapitalType(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcCapitalType> nbfcCapitalTypeList = null;
		try {
			nbfcCapitalTypeList = nbfcCapitalTypeService.getActiveDataFor(NbfcCapitalType.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcCapitalTypelist info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0966.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0966.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcCapitalType list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcCapitalTypeList)) {
			Logger.error("Exception while fetching nbfcCapitalType list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0966.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0966.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcCapitalTypeList);
			return response;
		}
	}

	@GetMapping(value = "/getNbfcShareHolder", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcShareHolder(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcShareHolder> nbfcShareHolderList = null;
		try {
			nbfcShareHolderList = nbfcShareHolderService.getActiveDataFor(NbfcShareHolder.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcShareHolderlist info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0965.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0965.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcShareHolder list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcShareHolderList)) {
			Logger.error("Exception while fetching nbfc share holder list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0965.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0965.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcShareHolderList);
			return response;
		}
	}

	@GetMapping(value = "/getNbfcRoute", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcRoute(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcRoute> nbfcRouteList = null;
		try {
			nbfcRouteList = nbfcRouteService.getActiveDataFor(NbfcRoute.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcRoutelist info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0967.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0967.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcRoute list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcRouteList)) {
			Logger.error("Exception while fetching nbfc Route list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0967.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0967.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcRouteList);
			return response;
		}
	}

	@GetMapping(value = "/getNbfcstatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcstatus(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcStatus> nbfcStatusList = null;
		try {
			nbfcStatusList = nbfcStatusService.getActiveDataFor(NbfcStatus.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcStatuslist info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0968.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0968.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfcStstus list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcStatusList)) {
			Logger.error("Exception while fetching nbfc Status list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0968.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0968.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcStatusList);
			return response;
		}
	}

	@GetMapping(value = "/getNbfcCompanyType", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcCompanyType(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcTypeCompany> nbfcTypeCompanyList = null;
		try {
			nbfcTypeCompanyList = nbfcTypeCompanyService.getActiveDataFor(NbfcTypeCompany.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcTypeCompanyList info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0862.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0862.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfc CompanyType list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcTypeCompanyList)) {
			Logger.error("Exception while fetching nbfc CompanyType list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0862.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0862.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcTypeCompanyList);
			return response;
		}
	}

	@GetMapping(value = "/getNbfcDirectorType", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcDirectorType(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcDirectorType> nbfcDirectorTypeList = null;
		try {
			nbfcDirectorTypeList = nbfcDirectorTypeService.getActiveDataFor(NbfcDirectorType.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching nbfcDirectorTypeList info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0862.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0862.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfc DirectorType list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcDirectorTypeList)) {
			Logger.error("Exception while fetching nbfc DirectorType list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0862.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0862.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcDirectorTypeList);
			return response;
		}
	}

	@GetMapping(value = "/getNationality", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNationality(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<Nationality> nbfcNationalityList = null;
		List<Nationality> responseNationalityList = new ArrayList<>();
		try {
			nbfcNationalityList = nbfcNationalityService.getActiveDataFor(Nationality.class, null);
			Nationality nationalityObj = null;
			for (Nationality nationality : nbfcNationalityList) {
				nationalityObj = new Nationality();
				nationalityObj.setNationalityId(nationality.getNationalityId());
				nationalityObj.setNationalityName(nationality.getNationalityName());
				nationalityObj.setNationalityCode(nationality.getNationalityCode());
				responseNationalityList.add(nationalityObj);
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching getNationality info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0862.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0862.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching nbfc Nationality list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(responseNationalityList)) {
			Logger.error("Exception while fetching nbfc Nationality list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0862.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0862.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(responseNationalityList);
			return response;
		}
	}

	@PostMapping(value = "/getLastSavedNbfcCertificationDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getLastSavedNbfcCertificationDetails(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NbfcCertificationDetailsDto nbfcCertificationDetailsDto) {
		ServiceResponse response;
		List<NbfcCertificationDetails> nbfcCertificationDetailsList = new ArrayList<>();
		NbfcCertificationDetails nbfcCertificationDetails = null;
		try {
			if (nbfcCertificationDetailsDto.getNbfcUserIdFk() != null && nbfcCertificationDetailsDto.getCorRegIdFk() != null) {
				Map<String, List<Long>> columnValueMap = new HashMap<>();
				List<Long> userIdList = new ArrayList<>();
				userIdList.add(nbfcCertificationDetailsDto.getNbfcUserIdFk());
				columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), userIdList);
				//				List<Long> entityBeanList = new ArrayList<>();
				//				entityBeanList.add(nbfcCertificationDetailsDto.getNbfcEntityBeanIdFk());
				//				columnValueMap.put(ColumnConstants.ENTITY_BEAN.getConstantVal(), entityBeanList);
				nbfcCertificationDetailsList = nbfcCertificationDetailsService.getDataByColumnLongValue(columnValueMap, MethodConstants.GET_LAST_SAVED_NBFC_CERTIFICATION_DETAILS_BY_ID.getConstantVal());
			} else {
				Logger.error("Exception while fetching Last Saved NbfcCertification Details info for job processingid " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching Last Saved NbfcCertification Details info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching Last Saved NbfcCertification Details info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (!CollectionUtils.isEmpty(nbfcCertificationDetailsList)) {
			nbfcCertificationDetails = nbfcCertificationDetailsList.get(0);
			if (nbfcCertificationDetails == null) {
				Logger.error("Exception while fetching Last Saved NbfcCertification Details info for job processingid " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
			} else {
				UserMaster user = new UserMaster();
				user.setUserId(nbfcCertificationDetails.getUserIdFk().getUserId());
				nbfcCertificationDetails.setUserIdFk(user);
				user = new UserMaster();
				user.setUserId(nbfcCertificationDetails.getCorRegIdFk().getUser().getUserId());
				nbfcCertificationDetails.getCorRegIdFk().setUser(user);
				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(nbfcCertificationDetails);
				return response;
			}
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcCertificationDetails);
			return response;
		}
	}

	@GetMapping(value = "/getNbfcPageNameList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcPageNameList(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcPageMaster> nbfcPageMasterList = null;
		try {
			nbfcPageMasterList = nbfcPageMasterService.getAllDataFor(NbfcPageMaster.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching NbfcPageMaster list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1147.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1147.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching NbfcPageMaster list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcPageMasterList)) {
			Logger.error("Exception while fetching NbfcPageMaster list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1147.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1147.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcPageMasterList);
			return response;
		}
	}

	@GetMapping(value = "/getNbfcSubPageNameList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcSubPageNameList(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		List<NbfcSubPageMaster> nbfcPageMasterList = null;
		try {
			nbfcPageMasterList = nbfcSubPageMasterService.getAllDataFor(NbfcSubPageMaster.class, null);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching NbfcSubPageMaster list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1148.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1148.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching NbfcSubPageMaster list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (CollectionUtils.isEmpty(nbfcPageMasterList)) {
			Logger.error("Exception while fetching NbfcSubPageMaster list info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1148.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1148.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcPageMasterList);
			return response;
		}
	}

	/**
	 * save the ReturnGroupMapping. This method is to save ReturnGroupMapping .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingDto
	 * @return
	 */
	@PostMapping(value = "/removeNbfcPagesAsValueChangesOnPage1")
	public ServiceResponse removeNbfcPagesAsValueChangesOnPage1(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NbfcCertificationDetailsDto nbfcCertificationDetailsDto) {
		ServiceResponse response = null;
		try {
			Map<String, List<Long>> columnValueMap = new HashMap<>();
			List<Long> entityIdList = new ArrayList<>();
			entityIdList.add(nbfcCertificationDetailsDto.getCorRegIdFk());
			columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), entityIdList);
			List<Long> pageNoList = new ArrayList<>();
			pageNoList.add(nbfcCertificationDetailsDto.getPageNo());
			columnValueMap.put(ColumnConstants.FORM_PAGE_NO.getConstantVal(), pageNoList);
			if (nbfcCertificationDetailsDto.getPageNo().equals(11L)) {
				List<Long> startPageNoList = new ArrayList<>();
				startPageNoList.add(nbfcCertificationDetailsDto.getStartPageNo());
				columnValueMap.put(ColumnConstants.START_FORM_PAGE_NO.getConstantVal(), startPageNoList);
				List<Long> endPageNoList = new ArrayList<>();
				endPageNoList.add(nbfcCertificationDetailsDto.getEndPageNo());
				columnValueMap.put(ColumnConstants.END_FORM_PAGE_NO.getConstantVal(), endPageNoList);
			}
			nbfcCertificationDetailsService.deleteDetails(columnValueMap);
		} catch (ServiceException e) {
			Logger.error("Request object not proper to save ");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0903.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0903.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while saving NbfcEntityBean info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0903.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0903.toString())).build();
		}
		response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		Logger.info("request completed to save NbfcEntityBean for job processingid" + jobProcessId);
		return response;
	}

	@PostMapping(value = "/saveNbfcCorRegistrationStatus")
	public ServiceResponse saveNbfcCorRegistrationStatus(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NbfcCertificationDetailsDto nbfcCertificationDetailsDto) {
		NbfcCorRegistrationStatus nbfcCorRegistrationStatus = new NbfcCorRegistrationStatus();
		NbfcCorRegistrationBean nbfcCorRegistrationBean = new NbfcCorRegistrationBean();
		UserMaster userMaster = new UserMaster();
		AdminStatus adminStatus = new AdminStatus();
		ServiceResponse response = null;
		Date currentTimeStamp = new Date(System.currentTimeMillis());
		try {
			if (!Objects.isNull(nbfcCertificationDetailsDto)) {
				nbfcCorRegistrationBean.setCorRegistrationId(nbfcCertificationDetailsDto.getCorRegIdFk());
				userMaster.setUserId(nbfcCertificationDetailsDto.getNbfcUserIdFk());
				adminStatus.setAdminStatusId(nbfcCertificationDetailsDto.getRegStatusId());
				nbfcCorRegistrationStatus.setCorRegIdFk(nbfcCorRegistrationBean);
				nbfcCorRegistrationStatus.setUserIdFk(userMaster);
				nbfcCorRegistrationStatus.setSubmittedOn(currentTimeStamp);
				nbfcCorRegistrationStatus.setAdminStatusIdFk(adminStatus);
				nbfcCorRegistrationStatus = nbfcCorRegistrationStatusService.add(nbfcCorRegistrationStatus);
			}
		} catch (ServiceException e) {
			Logger.error("Request object not proper to save ");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0903.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0903.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while saving NbfcCorRegistrationStatus info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0903.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0903.toString())).build();
		}
		response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(nbfcCorRegistrationStatus);
		Logger.info("request completed to save NbfcCorRegistrationStatus for job processingid" + jobProcessId);
		return response;
	}

	@PostMapping(value = "/getNbfcCorRegistrationStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcCorRegistrationStatus(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NbfcCertificationDetailsDto nbfcCertificationDetailsDto) {
		ServiceResponse response;
		List<NbfcCorRegistrationStatus> nbfcCorRegistrationStatusList = new ArrayList<>();
		NbfcCorRegistrationStatus nbfcCorRegistrationStatus = null;
		try {
			if (nbfcCertificationDetailsDto.getNbfcUserIdFk() != null && nbfcCertificationDetailsDto.getCorRegIdFk() != null) {
				Map<String, List<Long>> columnValueMap = new HashMap<>();
				List<Long> userIdList = new ArrayList<>();
				userIdList.add(nbfcCertificationDetailsDto.getNbfcUserIdFk());
				columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), userIdList);
				List<Long> entityBeanList = new ArrayList<>();
				entityBeanList.add(nbfcCertificationDetailsDto.getCorRegIdFk());
				columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), entityBeanList);
				nbfcCorRegistrationStatusList = nbfcCorRegistrationStatusService.getDataByColumnLongValue(columnValueMap, null);
			} else {
				Logger.error("Exception while fetching Nbfc Cor Registration Status info for job processingid " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching Nbfc Cor Registration Status info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching Nbfc Cor Registration Status info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (!CollectionUtils.isEmpty(nbfcCorRegistrationStatusList)) {
			nbfcCorRegistrationStatus = nbfcCorRegistrationStatusList.get(0);
			if (nbfcCorRegistrationStatus == null) {
				Logger.error("Exception while fetching Nbfc Cor Registration Status info for job processingid " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0902.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0902.toString())).build();
			} else {
				UserMaster user = new UserMaster();
				NbfcCorRegistrationBean nbfcCorRegistrationBean = new NbfcCorRegistrationBean();
				AdminStatus adminStatus = new AdminStatus();
				user.setUserId(nbfcCorRegistrationStatus.getUserIdFk().getUserId());
				nbfcCorRegistrationStatus.setUserIdFk(user);
				user = new UserMaster();
				nbfcCorRegistrationBean.setCorRegistrationId(nbfcCorRegistrationStatus.getCorRegIdFk().getCorRegistrationId());
				nbfcCorRegistrationStatus.setCorRegIdFk(nbfcCorRegistrationBean);
				adminStatus.setAdminStatusId(nbfcCorRegistrationStatus.getAdminStatusIdFk().getAdminStatusId());
				adminStatus.setStatus(nbfcCorRegistrationStatus.getAdminStatusIdFk().getStatus());
				adminStatus.setStatusCode(nbfcCorRegistrationStatus.getAdminStatusIdFk().getStatusCode());
				nbfcCorRegistrationStatus.setAdminStatusIdFk(adminStatus);
				nbfcCorRegistrationStatus.setSubmittedOn(nbfcCorRegistrationStatus.getSubmittedOn());
				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(nbfcCorRegistrationStatus);
				return response;
			}
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			response.setResponse(nbfcCorRegistrationStatus);
			return response;
		}
	}
}
