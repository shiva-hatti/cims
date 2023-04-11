/**
 * 
 */
package com.iris.sdmx.fusion.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dto.SdmxWebserviceUrlDto;
import com.iris.dto.ServiceResponse;
import com.iris.ebrhtmlfilebuilder.bean.FusionValidationResponse;
import com.iris.ebrhtmlfilebuilder.bean.VtlValidationResponse;
import com.iris.model.FileDetails;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.agency.master.repo.SdmxAgencyMasterRepo;
import com.iris.sdmx.codelist.bean.CodeListMasterBean;
import com.iris.sdmx.codelist.bean.CodeListValuesBean;
import com.iris.sdmx.ebrvalidation.bean.ValidateEbrDocInputRequest;
import com.iris.sdmx.ebrvalidation.bean.VtlStatusBean;
import com.iris.sdmx.fusion.bean.CodeListFusion;
import com.iris.sdmx.fusion.bean.CodeListFusionWrapper;
import com.iris.sdmx.fusion.bean.ConceptSchemeWrapper;
import com.iris.sdmx.fusion.bean.DataStructureWrapper;
import com.iris.sdmx.fusion.bean.FusionConstraintsBean;
import com.iris.sdmx.fusion.bean.FusionDecription;
import com.iris.sdmx.fusion.bean.RegistryInterface;
import com.iris.sdmx.fusion.entity.FusionProperties;
import com.iris.sdmx.fusion.entity.SdmxFusionDsdDetailEntity;
import com.iris.sdmx.fusion.repo.FusionPropertiesRepo;
import com.iris.sdmx.fusion.util.FusionPropertiesConstant;
import com.iris.sdmx.util.RestClientResponse;
import com.iris.sdmx.util.SDMXWebServiceConstant;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.constant.GeneralConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author sajadhav
 *
 */
@Service
public class FusionApiService {

	private static final Logger LOGGER = LogManager.getLogger(FusionApiService.class);

	@Autowired
	private FusionPropertiesRepo fusionPropertiesRepo;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SdmxFusionDsdDetailService sdmxFusionDsdDetailService;

	@Autowired
	private SdmxAgencyMasterRepo sdmxAgencyMasterRepo;

	@Autowired
	private GenericService<FusionProperties, Long> fusionPropertiesService;

	private static final String ERROR_TAG = "mes:ErrorMessage";
	private static final String SUCCESS = "Success";
	private static final String AGENCY_NAME_STRING = "{agencyName}";

	public boolean submitDataToFusion(Object object, Integer actionId, Long userId) {
		//if (ResourceUtil.getKeyValue("submitDataToFusion").equalsIgnoreCase(Boolean.TRUE.toString())) {
		if (getFusionPropertyValue(FusionPropertiesConstant.SUBMIT_DATA_TO_FUSION.getConstant()).equalsIgnoreCase(Boolean.TRUE.toString())) {
			LOGGER.debug(" Submit data to fusion ");
			if (object instanceof CodeListFusionWrapper) {
				CodeListFusionWrapper codeListFusionWrapper = (CodeListFusionWrapper) object;
				if (actionId.equals(GeneralConstants.ACTIONID_ADDITION.getConstantIntVal()) || actionId.equals(GeneralConstants.ACTIONID_EDITION.getConstantIntVal())) {
					return submitCodeListDataToFusion(codeListFusionWrapper, userId);
				} else {
					return deleteCodeListData(codeListFusionWrapper, userId);
				}
			} else if (object instanceof ConceptSchemeWrapper) {
				ConceptSchemeWrapper conceptSchemeWrapper = (ConceptSchemeWrapper) object;
				return submitDimensionDataToFusion(conceptSchemeWrapper, userId);
			} else if (object instanceof DataStructureWrapper) {
				DataStructureWrapper dataStructureWrapper = (DataStructureWrapper) object;
				if (actionId.equals(GeneralConstants.ACTIONID_ADDITION.getConstantIntVal()) || actionId.equals(GeneralConstants.ACTIONID_EDITION.getConstantIntVal())) {
					return submitElementDimensionDataToFusion(dataStructureWrapper, userId);
				} else {
					return deleteElementDimensionData(dataStructureWrapper, userId);
				}
			}
			return false;
		} else {
			LOGGER.debug(" Data not submitted to fusion ");
			return true;
		}
	}

	public boolean submitCodeListData(CodeListMasterBean codeListMasterBean) {

		List<FusionProperties> fusionProperties = fusionPropertiesRepo.findAll();
		List<AgencyMaster> agencyMaster = sdmxAgencyMasterRepo.findByIsActiveTrue();
		Map<String, String> agencyMap = agencyMaster.stream().collect(Collectors.toMap(AgencyMaster::getAgencyMasterCode, AgencyMaster::getAgencyFusionName));

		if (!CollectionUtils.isEmpty(agencyMaster)) {
			CodeListFusionWrapper codeListFusionWrapper = createCodeListFusionJson(codeListMasterBean, fusionProperties.stream().collect(Collectors.toMap(FusionProperties::getKey, FusionProperties::getValue)), agencyMap);
			return submitDataToFusion(codeListFusionWrapper, GeneralConstants.ACTIONID_ADDITION.getConstantIntVal(), codeListMasterBean.getUserId());
		}

		return false;
	}

	public boolean generateDsdCallToFusion(String agencyMasterCode) throws IOException {
		List<AgencyMaster> agencyMaster = sdmxAgencyMasterRepo.findByIsActiveTrue();
		Map<String, String> agencyMap = agencyMaster.stream().collect(Collectors.toMap(AgencyMaster::getAgencyMasterCode, AgencyMaster::getAgencyFusionName));
		AgencyMaster agencyMasterTesmp = sdmxAgencyMasterRepo.findByAgencyCode(agencyMasterCode);

		String fusionAgencyName = agencyMap.get(agencyMasterCode);

		if (!StringUtils.isEmpty(fusionAgencyName) && agencyMasterTesmp != null) {
			return submitGenerateDsdToFusion(3L, fusionAgencyName, agencyMasterTesmp);
		}

		return false;
	}

	private CodeListFusionWrapper createCodeListFusionJson(CodeListMasterBean codeListMasterBean, Map<String, String> fusionPropertiesMap, Map<String, String> agencyMap) {

		CodeListFusionWrapper codeListWrapper = new CodeListFusionWrapper();

		CodeListFusion codeListFusion = new CodeListFusion();

		/*
		 * codeListFusion.setAgencyId(fusionPropertiesMap.get(FusionPropertiesConstant.
		 * AGENCY_ID.getConstant()));
		 */
		codeListFusion.setAgencyId(agencyMap.get(codeListMasterBean.getAgencyMasterCode()));
		codeListFusion.setVersion(codeListMasterBean.getClVersion());
		codeListFusion.setFinal(Boolean.parseBoolean(fusionPropertiesMap.get(FusionPropertiesConstant.IS_FINAL.getConstant())));
		codeListFusion.setPartial(Boolean.parseBoolean(fusionPropertiesMap.get(FusionPropertiesConstant.IS_PARTIAL.getConstant())));
		codeListFusion.setValidityType(fusionPropertiesMap.get(FusionPropertiesConstant.VALIDITY_TYPE.getConstant()));
		codeListFusion.setId(codeListMasterBean.getClCode());
		codeListFusion.setUrn(fusionPropertiesMap.get(FusionPropertiesConstant.CODE_LIST_URN.getConstant()) + agencyMap.get(codeListMasterBean.getAgencyMasterCode()) + ":" + codeListMasterBean.getClCode() + "(" + codeListMasterBean.getClVersion() + ")");

		List<FusionDecription> names = new ArrayList<>();
		FusionDecription fusionName = new FusionDecription();
		fusionName.setLocale(fusionPropertiesMap.get(FusionPropertiesConstant.LOCALE.getConstant()));
		fusionName.setValue(codeListMasterBean.getClLable());
		names.add(fusionName);

		codeListFusion.setNames(names);

		List<FusionDecription> descriptions = new ArrayList<>();
		FusionDecription fusionDecription = new FusionDecription();
		fusionDecription.setLocale(fusionPropertiesMap.get(FusionPropertiesConstant.LOCALE.getConstant()));
		fusionDecription.setValue(codeListMasterBean.getClDesc());
		descriptions.add(fusionDecription);

		codeListFusion.setDescriptions(descriptions);

		List<CodeListFusion> items = new ArrayList<>();

		if (!CollectionUtils.isEmpty(codeListMasterBean.getCodeListValues())) {
			for (CodeListValuesBean codeListValuesBean : codeListMasterBean.getCodeListValues()) {

				if (codeListValuesBean.getIsActive().equals(Boolean.FALSE)) {
					continue;
				}

				CodeListFusion codeListFusionItem = new CodeListFusion();

				codeListFusionItem.setId(codeListValuesBean.getClValueCode());
				codeListFusionItem.setUrn(fusionPropertiesMap.get(FusionPropertiesConstant.CODE_LIST_VALUE_URN.getConstant()) + agencyMap.get(codeListMasterBean.getAgencyMasterCode()) + ":" + codeListMasterBean.getClCode() + "(" + codeListMasterBean.getClVersion() + ")." + codeListValuesBean.getClValueCode());

				names = new ArrayList<>();
				fusionName = new FusionDecription();
				fusionName.setLocale(fusionPropertiesMap.get(FusionPropertiesConstant.LOCALE.getConstant()));
				fusionName.setValue(codeListValuesBean.getClValueLable());
				names.add(fusionName);

				codeListFusionItem.setNames(names);

				descriptions = new ArrayList<>();
				fusionDecription = new FusionDecription();
				fusionDecription.setLocale(fusionPropertiesMap.get(FusionPropertiesConstant.LOCALE.getConstant()));
				fusionDecription.setValue(codeListValuesBean.getClValueDesc());
				descriptions.add(fusionDecription);

				codeListFusionItem.setDescriptions(descriptions);

				if (codeListValuesBean.getParentClValueCode() != null) {
					codeListFusionItem.setParentCode(codeListValuesBean.getParentClValueCode());
				}

				items.add(codeListFusionItem);
			}
		}

		codeListFusion.setItems(items);

		codeListWrapper.setCodelist(Arrays.asList(codeListFusion));
		return codeListWrapper;
	}

	private boolean submitDimensionDataToFusion(ConceptSchemeWrapper conceptSchemeWrapper, Long userId) {

		CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(GeneralConstants.ACTION.getConstantVal(), GeneralConstants.REPLACE.getConstantVal());
		headerMap.put(GeneralConstants.FUSION_AUTHORIZATION_KEY.getConstantVal(), GeneralConstants.FUSION_AUTHORIZATION_VALUE.getConstantVal());

		SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.SUBMIT_FUSION_DATA.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

		RestClientResponse restClientResponse;
		try {
			Date dateBeforeCalling = new Date();
			restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWeSerUrlDto, conceptSchemeWrapper, null, headerMap);
			Date dateAfterCalling = new Date();
			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, conceptSchemeWrapper, headerMap, null, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), userId);

			if (restClientResponse.getRestClientResponse() != null) {
				StringReader reader = new StringReader(restClientResponse.getRestClientResponse());
				if (restClientResponse.getRestClientResponse().contains(ERROR_TAG)) {
					return false;
				} else {
					JAXBContext jaxbContext = JAXBContext.newInstance(RegistryInterface.class);
					Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
					RegistryInterface unMarshRegistryInterface = (RegistryInterface) jaxbUnMarshaller.unmarshal(reader);

					if (unMarshRegistryInterface.getSubmitStructureResponse() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage().getStatus().equals(SUCCESS)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception While submitting DimensionData To Fusion : ", e);
		}

		return false;
	}

	private boolean submitElementDimensionDataToFusion(DataStructureWrapper dataStructureWrapper, Long userId) {

		CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(GeneralConstants.ACTION.getConstantVal(), GeneralConstants.REPLACE.getConstantVal());
		headerMap.put(GeneralConstants.FUSION_AUTHORIZATION_KEY.getConstantVal(), GeneralConstants.FUSION_AUTHORIZATION_VALUE.getConstantVal());

		SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.SUBMIT_FUSION_DATA.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

		try {
			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWeSerUrlDto, dataStructureWrapper, null, headerMap);

			Date dateAfterCalling = new Date();
			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, dataStructureWrapper, headerMap, null, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), userId);

			if (restClientResponse.getRestClientResponse() != null) {
				StringReader reader = new StringReader(restClientResponse.getRestClientResponse());
				if (restClientResponse.getRestClientResponse().contains(ERROR_TAG)) {
					return false;
				} else {
					JAXBContext jaxbContext = JAXBContext.newInstance(RegistryInterface.class);
					Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
					RegistryInterface unMarshRegistryInterface = (RegistryInterface) jaxbUnMarshaller.unmarshal(reader);

					if (unMarshRegistryInterface.getSubmitStructureResponse() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage().getStatus().equals(SUCCESS)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while submitting Element Dimension Data To Fusion : ", e);
		}

		return false;
	}

	private boolean deleteCodeListData(CodeListFusionWrapper codeListFusionWrapper, Long userId) {

		String clCode = null;
		String agencyId = null;
		String version = null;
		if (codeListFusionWrapper.getCodelist() != null) {
			clCode = codeListFusionWrapper.getCodelist().get(0).getId();
			agencyId = codeListFusionWrapper.getCodelist().get(0).getAgencyId();
			version = codeListFusionWrapper.getCodelist().get(0).getVersion();
		} else {
			return false;
		}

		CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(GeneralConstants.ACTION.getConstantVal(), GeneralConstants.REPLACE.getConstantVal());
		headerMap.put(GeneralConstants.FUSION_AUTHORIZATION_KEY.getConstantVal(), GeneralConstants.FUSION_AUTHORIZATION_VALUE.getConstantVal());

		SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.DELETE_CODE_LIST_FUSION_DATA.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_DELETE);

		List<String> pathParamList = new ArrayList<>();
		pathParamList.add(agencyId);
		pathParamList.add(clCode);
		pathParamList.add(version);
		try {
			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWeSerUrlDto, null, pathParamList, headerMap);

			Date dateAfterCalling = new Date();
			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, null, headerMap, pathParamList, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), userId);

			if (restClientResponse.getRestClientResponse() != null) {
				StringReader reader = new StringReader(restClientResponse.getRestClientResponse());
				if (restClientResponse.getRestClientResponse().contains(ERROR_TAG)) {
					return false;
				} else {
					JAXBContext jaxbContext = JAXBContext.newInstance(RegistryInterface.class);
					Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
					RegistryInterface unMarshRegistryInterface = (RegistryInterface) jaxbUnMarshaller.unmarshal(reader);

					if (unMarshRegistryInterface.getSubmitStructureResponse() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage().getStatus().equals(SUCCESS)) {
						return true;
					} else {
						return false;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in method deleteCodeListData :  ", e);
		}
		return false;
	}

	private boolean deleteElementDimensionData(DataStructureWrapper dataStructureWrapper, Long userId) {

		String dsdCode = null;
		String agencyId = null;
		String version = null;
		if (dataStructureWrapper.getDataStructure() != null) {
			dsdCode = dataStructureWrapper.getDataStructure().get(0).getId();
			agencyId = dataStructureWrapper.getDataStructure().get(0).getAgencyId();
			version = dataStructureWrapper.getDataStructure().get(0).getVersion();
		} else {
			return false;
		}

		CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(GeneralConstants.ACTION.getConstantVal(), GeneralConstants.REPLACE.getConstantVal());
		headerMap.put(GeneralConstants.FUSION_AUTHORIZATION_KEY.getConstantVal(), GeneralConstants.FUSION_AUTHORIZATION_VALUE.getConstantVal());

		SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.DELETE_ELE_DIMN_FUSION_DATA.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_DELETE);

		List<String> pathParamList = new ArrayList<>();
		pathParamList.add(agencyId);
		pathParamList.add(dsdCode);
		pathParamList.add(version);

		try {
			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWeSerUrlDto, null, pathParamList, headerMap);

			Date dateAfterCalling = new Date();
			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, null, headerMap, pathParamList, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), userId);
			if (restClientResponse.getRestClientResponse() != null) {
				StringReader reader = new StringReader(restClientResponse.getRestClientResponse());
				if (restClientResponse.getRestClientResponse().contains(ERROR_TAG)) {
					return false;
				} else {
					JAXBContext jaxbContext = JAXBContext.newInstance(RegistryInterface.class);
					Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
					RegistryInterface unMarshRegistryInterface = (RegistryInterface) jaxbUnMarshaller.unmarshal(reader);

					if (unMarshRegistryInterface.getSubmitStructureResponse() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage().getStatus().equals(SUCCESS)) {
						return true;
					} else {
						return false;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Method deleteElementDimensionData : ", e);
		}
		return false;
	}

	private boolean submitCodeListDataToFusion(CodeListFusionWrapper codeListFusionWrapper, Long userId) {
		CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(GeneralConstants.ACTION.getConstantVal(), GeneralConstants.REPLACE.getConstantVal());
		headerMap.put(GeneralConstants.FUSION_AUTHORIZATION_KEY.getConstantVal(), GeneralConstants.FUSION_AUTHORIZATION_VALUE.getConstantVal());

		SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.SUBMIT_FUSION_DATA.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

		try {
			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWeSerUrlDto, codeListFusionWrapper, null, headerMap);
			Date dateAfterCalling = new Date();
			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, codeListFusionWrapper, headerMap, null, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), userId);
			if (restClientResponse.getRestClientResponse() != null) {
				StringReader reader = new StringReader(restClientResponse.getRestClientResponse());
				if (restClientResponse.getRestClientResponse().contains(ERROR_TAG)) {
					return false;
				} else {
					JAXBContext jaxbContext = JAXBContext.newInstance(RegistryInterface.class);
					Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
					RegistryInterface unMarshRegistryInterface = (RegistryInterface) jaxbUnMarshaller.unmarshal(reader);

					if (unMarshRegistryInterface.getSubmitStructureResponse() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage() != null && unMarshRegistryInterface.getSubmitStructureResponse().getSubmissionResult().getStatusMessage().getStatus().equals(SUCCESS)) {
						return true;
					} else {
						return false;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception In Method submitCodeListDataToFusion :  ", e);
		}

		return false;
	}

	public ServiceResponse getFusionConstraint(String jobProcessingId, String agencyId, Long userId) {
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			List<String> pathParameterList = new ArrayList<>();
			pathParameterList.add(agencyId);
			pathParameterList.add("all/latest/?format=sdmx-json&detail=full&references=none");

			SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.GET_FUSION_CONSTRAINTS.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_GET);

			sdmxWeSerUrlDto.setComponentUrlPath(sdmxWeSerUrlDto.getComponentUrlPath() + "/" + agencyId + "/all/latest/?format=sdmx-json&detail=full&references=none");

			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWeSerUrlDto, null, null, null);
			Date dateAfterCalling = new Date();
			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, null, null, pathParameterList, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), userId);

			if (restClientResponse.getRestClientResponse() != null) {
				Type listToken = null;
				if (restClientResponse.getStatusCode() == 200) {
					listToken = new TypeToken<FusionConstraintsBean>() {
					}.getType();
					return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setStatusCode(restClientResponse.getStatusCode() + "").setResponse(JsonUtility.getGsonObject().fromJson(restClientResponse.getRestClientResponse(), listToken)).build();
				}
			} else {
				LOGGER.error("Null response received from Fusion Get constraint API :  " + jobProcessingId);
			}
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(restClientResponse.getStatusCode() + "").build();
		} catch (Exception e) {
			LOGGER.error("Exception occured for getting fusion constrain for job processing Id :  " + jobProcessingId, e);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
	}

	public ServiceResponse getFusionValidationResponse(ValidateEbrDocInputRequest validateEbrDocInputRequest, String jobProcessingId) {
		try {
			SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.FUSION_VALIDATION.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Map<String, Object> formDataMap = new HashMap<>();
			formDataMap.put(SDMXWebServiceConstant.UPLOAD_FILE.getConstantVal(), new File(validateEbrDocInputRequest.getInstanceFilePath()));

			List<String> pathParameterList = new ArrayList<>();
			pathParameterList.add(validateEbrDocInputRequest.getInstanceFilePath());

			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndFormData(sdmxWeSerUrlDto, formDataMap, null, null);
			Date dateAfterCalling = new Date();

			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, null, null, pathParameterList, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), validateEbrDocInputRequest.getUserId());

			if (restClientResponse.getRestClientResponse() != null) {

				Type listToken = null;
				if (restClientResponse.getStatusCode() == 200) {
					String responsestring = restClientResponse.getRestClientResponse();
					listToken = new TypeToken<FusionValidationResponse>() {
					}.getType();
					return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setStatusCode(restClientResponse.getStatusCode() + "").setResponse(JsonUtility.getGsonObject().fromJson(responsestring, listToken)).build();
				}

			} else {
				LOGGER.error("Null response received from Fusion Get constraint API :  " + jobProcessingId);
				return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in get Fusio Validation Response Method for Job Processing ID : " + jobProcessingId, e);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return null;
	}

	public ServiceResponse submitSdmxDocumentForVTLValidation(ValidateEbrDocInputRequest validateEbrDocInputRequest, String jobProcessingId) {
		try {
			List<FusionProperties> fusionProperties = fusionPropertiesService.getAllDataFor(null, null);
			if (CollectionUtils.isEmpty(fusionProperties)) {
				return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			}

			Map<String, String> fusionPropertiesMap = fusionProperties.stream().collect(Collectors.toMap(FusionProperties::getKey, FusionProperties::getValue));

			SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.SUBMIT_FILE_FOR_VTL_VALIDATION.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Map<String, Object> formDataMap = new HashMap<>();
			formDataMap.put(SDMXWebServiceConstant.FILE.getConstantVal(), new File(validateEbrDocInputRequest.getInstanceFilePath()));
			formDataMap.put(SDMXWebServiceConstant.OWNER_GROUP_CODE_KEY.getConstantVal(), fusionPropertiesMap.get("VTL_OWNER_GROUP_CODE"));
			formDataMap.put(SDMXWebServiceConstant.CODE_KEY.getConstantVal(), fusionPropertiesMap.get("VTL_CODE"));
			formDataMap.put(SDMXWebServiceConstant.PROJECT_CODE_KEY.getConstantVal(), fusionPropertiesMap.get("VTL_PROJECT_CODE"));
			formDataMap.put(SDMXWebServiceConstant.VERSION_KEY.getConstantVal(), fusionPropertiesMap.get("VTL_VERSION"));
			formDataMap.put(SDMXWebServiceConstant.RETURN_ONLY_PERSISTENT_KEY.getConstantVal(), fusionPropertiesMap.get("VTL_RETURN_ONLY_PERSISTENT"));
			formDataMap.put(SDMXWebServiceConstant.CALLBACK_URL_KEY.getConstantVal(), ((SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.CALLBACK_OF_SUBMIT_FILE_FOR_VTL_VALIDATION_API.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST)).getComponentUrlPath());

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(SDMXWebServiceConstant.AUTHORIZATION.getConstantVal(), ResourceUtil.getKeyValue(SDMXWebServiceConstant.VTL_AUTHORIZATION_KEY.getConstantVal()));

			List<String> pathParameterList = new ArrayList<>();
			pathParameterList.add(validateEbrDocInputRequest.getInstanceFilePath());

			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndFormData(sdmxWeSerUrlDto, formDataMap, null, headerMap);
			Date dateAfterCalling = new Date();

			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, JsonUtility.getGsonObject().toJson(formDataMap), null, pathParameterList, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), validateEbrDocInputRequest.getUserId());

			if (restClientResponse.getRestClientResponse() != null) {

				Type listToken = null;
				if (restClientResponse.getStatusCode() == 200) {
					String responsestring = restClientResponse.getRestClientResponse();
					listToken = new TypeToken<VtlValidationResponse>() {
					}.getType();
					return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setStatusCode(restClientResponse.getStatusCode() + "").setResponse(JsonUtility.getGsonObject().fromJson(responsestring, listToken)).build();
				}

			} else {
				LOGGER.error("Null response received from VTL validation API :  " + jobProcessingId);
				return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in VTL Validation Response Method for Job Processing ID : " + jobProcessingId, e);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return null;
	}

	private String replaceDSDUrl(String urlStr, String fusionAgencyName) {
		return urlStr.replace(AGENCY_NAME_STRING, fusionAgencyName);
	}

	private boolean submitGenerateDsdToFusion(Long userId, String fusionAgencyName, AgencyMaster agencyMaster) throws IOException {
		CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(GeneralConstants.ACTION.getConstantVal(), GeneralConstants.REPLACE.getConstantVal());
		headerMap.put(GeneralConstants.FUSION_AUTHORIZATION_KEY.getConstantVal(), GeneralConstants.FUSION_AUTHORIZATION_VALUE.getConstantVal());

		SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.AGENCY_WISE_GENERATE_DSD_FUSION.getConstantVal() + "~" + "GET");
		SdmxWebserviceUrlDto sdmxWebserviceUrlDto = new SdmxWebserviceUrlDto();
		if (sdmxWeSerUrlDto != null) {
			String changeUrl = sdmxWeSerUrlDto.getComponentUrlPath();
			String newUrl = replaceDSDUrl(changeUrl, fusionAgencyName);

			sdmxWebserviceUrlDto.setComponentUrlPath(newUrl);
			sdmxWebserviceUrlDto.setAcceptType(sdmxWeSerUrlDto.getAcceptType());
			sdmxWebserviceUrlDto.setComponentType(SDMXWebServiceConstant.AGENCY_WISE_GENERATE_DSD_FUSION.getConstantVal());
			sdmxWebserviceUrlDto.setProduceType(sdmxWeSerUrlDto.getProduceType());
			sdmxWebserviceUrlDto.setSdmxWebserviceUrlID(sdmxWeSerUrlDto.getSdmxWebserviceUrlID());
			sdmxWebserviceUrlDto.setUrlHttpMethodType(sdmxWeSerUrlDto.getUrlHttpMethodType());

		}
		String currentDateStr = new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
		String dirPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("fusion.base.path") + File.separator;
		String destinationXlsFilePath = dirPath + "DSD_FILE_" + currentDateStr + ".xml";

		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(destinationXlsFilePath))) {
			Date dateBeforeCalling = new Date();

			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWebserviceUrlDto, null, null, headerMap);

			Date dateAfterCalling = new Date();
			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, null, headerMap, null, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse(), userId);
			if (restClientResponse.getRestClientResponse() != null) {

				writer.write(restClientResponse.getRestClientResponse());

				SdmxFusionDsdDetailEntity sdmxFusionDsdDetailEntity = new SdmxFusionDsdDetailEntity();
				sdmxFusionDsdDetailEntity.setIsActive(true);
				sdmxFusionDsdDetailEntity.setDsdFileName("DSD_FILE_" + currentDateStr + ".xml");
				String maxVersion = sdmxFusionDsdDetailService.findMaxVersionActiveStatus(true, agencyMaster.getAgencyMasterId());
				if (!StringUtils.isBlank(maxVersion)) {
					Double doubleVersion = Double.parseDouble(maxVersion);
					doubleVersion++;
					sdmxFusionDsdDetailEntity.setDsdVer(doubleVersion.toString());
				} else {
					sdmxFusionDsdDetailEntity.setDsdVer("1.0");
				}
				UserMaster user = new UserMaster();
				user.setUserId(3L);
				sdmxFusionDsdDetailEntity.setCreatedBy(user);
				sdmxFusionDsdDetailEntity.setCreatedOn(new Date());
				sdmxFusionDsdDetailEntity.setAgencyMaster(agencyMaster);
				sdmxFusionDsdDetailService.add(sdmxFusionDsdDetailEntity);
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}

		return false;
	}

	public ServiceResponse downloadResultFromVtl(VtlStatusBean vtlStatusBean, String jobProcessingId, Long userId, FileDetails fileDetails) {
		try {
			SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.DOWNLOAD_RESULT_FROM_VTL.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_GET);

			String sdmxWebServiceUrl = JsonUtility.getGsonObject().toJson(sdmxWeSerUrlDto);

			SdmxWebserviceUrlDto sdmxWebserviceUrlDto = JsonUtility.getGsonObject().fromJson(sdmxWebServiceUrl, SdmxWebserviceUrlDto.class);
			sdmxWebserviceUrlDto.setComponentUrlPath(sdmxWebserviceUrlDto.getComponentUrlPath() + "?" + SDMXWebServiceConstant.UUID.getConstantVal() + "=" + vtlStatusBean.getId());

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(SDMXWebServiceConstant.AUTHORIZATION.getConstantVal(), ResourceUtil.getKeyValue(SDMXWebServiceConstant.VTL_AUTHORIZATION_KEY.getConstantVal()));
			headerMap.put(SDMXWebServiceConstant.UUID.getConstantVal(), vtlStatusBean.getId());

			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndGetResponseAsFile(sdmxWebserviceUrlDto, null, null, headerMap);
			Date dateAfterCalling = new Date();

			insertDataIntoSDMXAPILogDetails(sdmxWebserviceUrlDto, null, headerMap, null, dateBeforeCalling, dateAfterCalling, restClientResponse.getFileResponse() + "", userId);

			if (restClientResponse.getFileResponse() != null) {
				if (restClientResponse.getStatusCode() == 200) {
					File file = restClientResponse.getFileResponse();
					File downloadfile = new File(fileDetails.getFilePath() + File.separator + "BUS_VAL_RESULT_" + vtlStatusBean.getId() + ".zip");
					Files.move(file.toPath(), downloadfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					try (FileWriter fr = new FileWriter(file)) {
						fr.flush();
					}
					LOGGER.info("Download file path in downloadResultFromVtl method: " + downloadfile.getPath());
					return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setStatusCode(restClientResponse.getStatusCode() + "").setResponse(downloadfile.getPath()).build();
				}
			} else {
				LOGGER.error("Null response received from downloadResultFromVtl API :  " + jobProcessingId);
				return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			}
			LOGGER.info("In downloadResultFromVtl method end");
		} catch (Exception e) {
			LOGGER.error("Exception occured in get Fusio Validation Response Method for Job Processing ID : " + jobProcessingId, e);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return null;
	}

	public VtlValidationResponse downloadVtlStatusOfExecution(VtlStatusBean vtlStatusBean, String jobProcessingId, Long userId) {
		try {
			SdmxWebserviceUrlDto sdmxWeSerUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.VTL_STATUS_OF_EXECUTION.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(SDMXWebServiceConstant.AUTHORIZATION.getConstantVal(), ResourceUtil.getKeyValue(SDMXWebServiceConstant.VTL_AUTHORIZATION_KEY.getConstantVal()));

			Map<String, Object> formDataMap = new HashMap<>();
			formDataMap.put(SDMXWebServiceConstant.REQUEST_ID.getConstantVal(), vtlStatusBean.getId());

			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Date dateBeforeCalling = new Date();
			RestClientResponse restClientResponse = restServiceClient.callRestWebServiceWithMultipleHeaderAndFormData(sdmxWeSerUrlDto, formDataMap, null, headerMap);
			Date dateAfterCalling = new Date();

			headerMap.put(SDMXWebServiceConstant.REQUEST_ID.getConstantVal(), vtlStatusBean.getId());

			insertDataIntoSDMXAPILogDetails(sdmxWeSerUrlDto, null, headerMap, null, dateBeforeCalling, dateAfterCalling, restClientResponse.getRestClientResponse() + "", userId);

			if (restClientResponse.getRestClientResponse() != null) {
				if (restClientResponse.getStatusCode() == 200) {
					restClientResponse.getRestClientResponse();
					Type listToken = null;
					listToken = new TypeToken<VtlValidationResponse>() {
					}.getType();
					return JsonUtility.getGsonObject().fromJson(restClientResponse.getRestClientResponse(), listToken);
				} else {
					LOGGER.error("status code other than 200 received from  downloadVtlStatusOfExecution API :  " + jobProcessingId);
				}
			} else {
				LOGGER.error("Null response received from downloadResultFromVtl API :  " + jobProcessingId);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in get Fusio Validation Response Method for Job Processing ID : " + jobProcessingId, e);
		}
		return null;
	}

	private Boolean insertDataIntoSDMXAPILogDetails(SdmxWebserviceUrlDto sdmxWeSerUrlDto, Object object, Map<String, String> headerMap, List<String> pathParameters, Date dateBeforeCalling, Date dateAfterCalling, String response, Long userId) {

		try (Connection connection = dataSource.getConnection(); PreparedStatement pstmt = connection.prepareStatement("INSERT INTO TBL_SDMX_API_LOG_DETAILS" + " (USER_ID_FK,COMPONENT_URL_ID_FK,REQUEST_JSON, PATH_VARIABLE, REQUEST_RECEIVED_TIME," + " RESPONSE_SENDING_TIME, HEADER, CREATED_TIME, RESPONSE_JSON, SERVER_IP) " + " VALUES (?,?,?,?,?,?,?,?,?,?)")) {
			if (userId != null) {
				pstmt.setLong(1, userId);
			} else {
				pstmt.setString(1, null);
			}
			pstmt.setLong(2, sdmxWeSerUrlDto.getSdmxWebserviceUrlID());
			if (object != null) {
				pstmt.setString(3, JsonUtility.getGsonObject().toJson(object));
			} else {
				pstmt.setString(3, null);
			}

			if (pathParameters != null) {
				pstmt.setString(4, JsonUtility.getGsonObject().toJson(pathParameters));
			} else {
				pstmt.setString(4, null);
			}

			pstmt.setTimestamp(5, new java.sql.Timestamp(dateBeforeCalling.getTime()));
			pstmt.setTimestamp(6, new java.sql.Timestamp(dateAfterCalling.getTime()));

			if (headerMap != null) {
				pstmt.setString(7, JsonUtility.getGsonObject().toJson(headerMap));
			} else {
				pstmt.setString(7, null);
			}

			pstmt.setTimestamp(8, new java.sql.Timestamp(new Date().getTime()));

			pstmt.setString(9, response);

			try {
				pstmt.setString(10, InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e1) {
				LOGGER.error("Error while getting IP address");
			}

			pstmt.executeUpdate();

			return true;
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			return false;
		}
	}

	/**
	 * @param propertyKey
	 * @return
	 */
	public String getFusionPropertyValue(String propertyKey) {
		List<FusionProperties> fusionProperties = fusionPropertiesRepo.findAll();
		Map<String, String> fusionPropertiesMap = fusionProperties.stream().collect(Collectors.toMap(FusionProperties::getKey, FusionProperties::getValue));
		return fusionPropertiesMap.get(propertyKey);
	}

}
