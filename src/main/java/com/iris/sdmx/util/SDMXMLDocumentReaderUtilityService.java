/**
 * 
 */
package com.iris.sdmx.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.reflect.TypeToken;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.sdmx.bean.DataSet;
import com.iris.sdmx.bean.SDMXDocument;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.exceltohtml.bean.DimensionCodeListValueBean;
import com.iris.sdmx.exceltohtml.bean.DimensionDetailCategories;
import com.iris.sdmx.exceltohtml.bean.ModelOtherDetails;
import com.iris.sdmx.exceltohtml.repo.SdmxModelCodesRepo;
import com.iris.sdmx.model.code.data.SdmxDataModelCodeBean;
import com.iris.sdmx.returnentmapp.bean.SDMXReturnEntityMapp;
import com.iris.sdmx.returnentmapp.repo.SDMXReturnEntityMapRepo;
import com.iris.sdmx.upload.bean.EBRFileUploadService;
import com.iris.sdmx.upload.bean.EbrFileDetailsBean;
import com.iris.sdmx.upload.bean.HashValueDimensionBean;
import com.iris.sdmx.upload.bean.RepDateReturnDataPoint;
import com.iris.sdmx.upload.bean.ReturnDataPoint;
import com.iris.sdmx.upload.bean.SdmxModelCodeLiteBean;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MetaDataCheckConstants;

/**
 * @author sajadhav
 *
 */
@Service
public class SDMXMLDocumentReaderUtilityService {

	private static final Logger LOGGER = LogManager.getLogger(SDMXMLDocumentReaderUtilityService.class);

	@Autowired
	private SdmxModelCodesRepo sdmxModelCodesRepo;

	@Autowired
	private EBRFileUploadService ebrFileUploadService;

	@Autowired
	private SDMXReturnEntityMapRepo sdmxReturnEntityMapRepo;

	public Map<String, List<RepDateReturnDataPoint>> generateDateWiseJsonModified(Map<String, Map<String, List<String>>> dmIdMapForAllElement, Map<String, List<String>> sdmxDataModelCodeBeans) throws Exception {

		if (sdmxDataModelCodeBeans != null) {
			Map<String, List<RepDateReturnDataPoint>> elementAndReportingDateMap = new HashMap<>();

			for (String elementCode : dmIdMapForAllElement.keySet()) {

				Map<String, List<String>> dmIdMapForSingleElement = dmIdMapForAllElement.get(elementCode);

				List<RepDateReturnDataPoint> repDateReturnDataPointMapList = new ArrayList<>();

				for (String reportingDate : dmIdMapForSingleElement.keySet()) {
					RepDateReturnDataPoint repDateReturnDataPointMap = new RepDateReturnDataPoint();
					repDateReturnDataPointMap.setEndDate(reportingDate);

					List<String> dmIdList = dmIdMapForSingleElement.get(reportingDate);

					for (String dmId : dmIdList) {
						List<String> returnCodeList = sdmxDataModelCodeBeans.get(dmId);
						if (CollectionUtils.isEmpty(returnCodeList)) {
							// Temporary added
							LOGGER.error("RETURN CODE EMPTY received for DMID : " + dmId);
							throw new Exception("RETURN CODE EMPTY received for DMID : " + dmId);
						}
						for (String returnCode : returnCodeList) {
							if (repDateReturnDataPointMap.getReturnList() != null) {
								ReturnDataPoint ret = new ReturnDataPoint();
								ret.setReturnCode(returnCode);
								if (repDateReturnDataPointMap.getReturnList().indexOf(ret) == -1) {
									Set<String> dataPoints = new HashSet<>();
									dataPoints.add(dmId);
									ret.setDataPoints(dataPoints);
									repDateReturnDataPointMap.getReturnList().add(ret);
								} else {
									ret = repDateReturnDataPointMap.getReturnList().get(repDateReturnDataPointMap.getReturnList().indexOf(ret));
									if (ret.getDataPoints() != null) {
										ret.getDataPoints().add(dmId);
									} else {
										Set<String> dataPoints = new HashSet<>();
										dataPoints.add(dmId);
										ret.setDataPoints(dataPoints);
									}
								}
							} else {
								List<ReturnDataPoint> returnDataPointMapList = new ArrayList<>();

								ReturnDataPoint returnDataPointMap = new ReturnDataPoint();

								Set<String> dataPoints = new HashSet<>();
								dataPoints.add(dmId);

								returnDataPointMap.setReturnCode(returnCode);
								returnDataPointMap.setDataPoints(dataPoints);

								returnDataPointMapList.add(returnDataPointMap);

								repDateReturnDataPointMap.setReturnList(returnDataPointMapList);
							}
						}
					}

					repDateReturnDataPointMapList.add(repDateReturnDataPointMap);
				}
				elementAndReportingDateMap.put(elementCode, repDateReturnDataPointMapList);
			}
			return elementAndReportingDateMap;
		}
		return null;
	}

	//	public Map<String, Map<String, List<String>>> generateDateWiseJson(SDMXDocument sdmxDocument){
	//		
	//		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	//		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
	//		
	//		List<DataSet> dataSets = sdmxDocument.getDataSets();
	//		Set<String> dmIdSet = new HashSet<>();
	//		
	//		Map<String, List<String>> dmIdMap = new HashMap<>();
	//		dataSets.forEach(f -> {
	//			List<Map<String, String>> lisvalueMap = f.getData();	
	//			lisvalueMap.forEach(k -> {
	//				try {
	//					String keyDate = df2.format(df1.parse(k.get("TIME_PERIOD"))); 
	//					if(dmIdMap.containsKey(keyDate)) {
	//						List<String> list = dmIdMap.get(keyDate);
	//						list.add(k.get("DMID"));
	//						dmIdSet.add(k.get("DMID"));
	//						dmIdMap.put(keyDate, list);
	//					}else {
	//						List<String> list = new ArrayList<>();
	//						list.add(k.get("DMID"));
	//						dmIdSet.add(k.get("DMID"));
	//						dmIdMap.put(keyDate, list);
	//					}	
	//				}catch(Exception e) {
	//					LOGGER.error("Exception : ", e);
	//				}
	//			});
	//		});
	//		
	//		
	//		Map<String, List<String>> sdmxDataModelCodeBeans = getSdmxReturnModelCodeBeans(dmIdSet);
	//		
	//		if(sdmxDataModelCodeBeans!= null) {
	//			Map<String, Map<String, List<String>>> finalMapToBePrepared = new HashMap<>();
	//
	//			for (String reportingDate : dmIdMap.keySet()) {
	//				List<String> dmIdList = dmIdMap.get(reportingDate);
	//				
	//				Map<String, List<String>> returnCodeAndDMIDMap = new HashMap<>();
	//				
	//				for (String dmId : dmIdList) {
	//					List<String> returnCodeList =  sdmxDataModelCodeBeans.get(dmId);
	//					for (String returnCode : returnCodeList) {
	//						if(returnCodeAndDMIDMap.containsKey(returnCode)) {
	//							List<String> dmIds =  returnCodeAndDMIDMap.get(returnCode);
	//							dmIds.add(dmId);
	//							returnCodeAndDMIDMap.put(returnCode, dmIds);
	//						}else {
	//							List<String> dmIds = new ArrayList<>();
	//							dmIds.add(dmId);
	//							returnCodeAndDMIDMap.put(returnCode, dmIds);
	//						}
	//					}
	//				}
	//				finalMapToBePrepared.put(reportingDate, returnCodeAndDMIDMap);
	//			}			
	//			System.out.println(new Gson().toJson(finalMapToBePrepared));
	//			return finalMapToBePrepared;
	//		}
	//		return null;
	//	}

	private Map<String, List<String>> getSdmxReturnModelCodeBeans(Set<String> dmIdSet, Map<String, List<String>> dbDimCodeAndReturnListMap, Set<String> dbDmidAndElementCodeSet) {

		List<SdmxDataModelCodeBean> sdmxDataModelCodeBeans = sdmxModelCodesRepo.fetchDistinctDimIdData(new ArrayList<>(dmIdSet));

		if (!CollectionUtils.isEmpty(sdmxDataModelCodeBeans)) {
			for (SdmxDataModelCodeBean sdmxDataModelCodeBean : sdmxDataModelCodeBeans) {
				if (dbDimCodeAndReturnListMap.containsKey(sdmxDataModelCodeBean.getModelCode())) {
					List<String> returnCodeList = dbDimCodeAndReturnListMap.get(sdmxDataModelCodeBean.getModelCode());
					returnCodeList.add(sdmxDataModelCodeBean.getReturnCode());
					dbDimCodeAndReturnListMap.put(sdmxDataModelCodeBean.getModelCode(), returnCodeList);
				} else {
					List<String> returnCodeList = new ArrayList<>();
					returnCodeList.add(sdmxDataModelCodeBean.getReturnCode());
					dbDimCodeAndReturnListMap.put(sdmxDataModelCodeBean.getModelCode(), returnCodeList);
				}
				dbDmidAndElementCodeSet.add(sdmxDataModelCodeBean.getDsdCode() + "~" + sdmxDataModelCodeBean.getModelCode());
			}
			return dbDimCodeAndReturnListMap;
		}
		return null;
	}

	public boolean validateSdmxDocumentAndGetDmIds(SDMXDocument sdmxDocument, EbrFileDetailsBean ebrFileAuditInputBean, Map<String, Map<String, List<String>>> dmIdMapForAllElement, Map<String, Map<Boolean, Set<String>>> fieldCheckListMap, Map<String, List<String>> dbDimCodeAndReturnListMap) {

		boolean isValidationFailed = false;
		final String TIME_PERIOD = "TIME_PERIOD";
		final String DMID = "DMID";
		final String INDEPENDENT = "INDEPENDENT";
		Set<String> documentDmidSet = new HashSet<>();
		Set<String> documentDmidAndElementCodeSet = new HashSet<>();
		Set<String> dbDmidAndElementCodeSet = new HashSet<>();

		if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
			if (sdmxDocument.getDataSets() == null || sdmxDocument.getDataSets().get(0).getStructureRef() == null || sdmxDocument.getDataSets().get(0).getStructureRef().getSender() == null) {
				isValidationFailed = true;
				ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0646.toString(), fieldCheckListMap);
			} else {
				if (sdmxDocument.getDataSets().get(0).getStructureRef().getSender().equals(ebrFileAuditInputBean.getEntityCode())) {
					ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldCheckListMap);
				} else {
					isValidationFailed = true;
					ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0644.toString(), fieldCheckListMap);
				}
			}
		} else if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.CSV.getConstantVal())) {
			if (sdmxDocument.getDataSets().size() > 1) {
				isValidationFailed = true;
				ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.ELEMENT_CHECK.getConstantVal(), false, ErrorCode.E0249.toString(), fieldCheckListMap);
			} else {
				ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.ELEMENT_CHECK.getConstantVal(), true, null, fieldCheckListMap);
			}
		}

		if (isValidationFailed) {
			return isValidationFailed;
		}

		List<DataSet> dataSets = sdmxDocument.getDataSets();
		Set<String> reportingEndDateErrorSet = new HashSet<>();

		Set<String> dmidNotFoundErrorSet = new HashSet<>();
		Set<String> multipleDmidInDBErrorSet = new HashSet<>();
		Set<String> multipleDmidInDocErrorSet = new HashSet<>();
		Set<String> elementCodeExistenceErrorSet = new HashSet<>();

		//		Set<String> dependencyTypeErrorSet = new HashSet<>();
		Set<String> elementCodeAndVersionErrorSet = new HashSet<>();

		List<SdmxElementBean> sdmxElementBeanList = new ArrayList<>();

		dataSets.forEach(f -> {
			SdmxElementBean sdmxElementBean = new SdmxElementBean();
			sdmxElementBean.setDsdCode(f.getStructureRef().getCode());
			sdmxElementBean.setElementVer(f.getStructureRef().getVersion());
			sdmxElementBeanList.add(sdmxElementBean);
		});

		List<HashValueDimensionBean> hashValueDimensionBeans = ebrFileUploadService.getHashValueDimensionBean(sdmxElementBeanList);

		ModelOtherDetails modelOtherDetails = new ModelOtherDetails();
		modelOtherDetails.setDependencyType(INDEPENDENT);

		Map<String, List<SdmxModelCodeLiteBean>> hashAndDmidValueMap = getHashAndDmidValueMap(hashValueDimensionBeans, dataSets, modelOtherDetails);
		List<String> notPresentHashValues = null;
		int i = 1;
		for (DataSet f : dataSets) {
			i = 1;
			String elementCode = f.getStructureRef().getCode();
			String elementVersion = f.getStructureRef().getVersion();

			HashValueDimensionBean hashValueDimensionBean = new HashValueDimensionBean();
			hashValueDimensionBean.setDsdId(elementCode);
			hashValueDimensionBean.setElementVersion(elementVersion);

			if (hashValueDimensionBeans.indexOf(hashValueDimensionBean) == -1) {
				elementCodeExistenceErrorSet.add("Element and version combination not exist" + ":" + elementCode);
			} else {
				hashValueDimensionBean = hashValueDimensionBeans.get(hashValueDimensionBeans.indexOf(hashValueDimensionBean));

				Map<String, List<String>> dmIdMap = new HashMap<>();
				List<Map<String, String>> lisvalueMap = f.getData();

				for (Map<String, String> k : lisvalueMap) {
					notPresentHashValues = new ArrayList<>();
					String keyDate = null;
					try {
						if (k.get(TIME_PERIOD) == null) {
							reportingEndDateErrorSet.add(ErrorCode.E0257.toString() + ":" + elementCode);
						} else {
							if (!k.get(TIME_PERIOD).toUpperCase().contains("T")) {
								keyDate = DateManip.formatDate(k.get(TIME_PERIOD), DateConstants.YYYY_MM_DD.getDateConstants(), DateConstants.DD_MM_YYYY.getDateConstants());
								if (keyDate == null) {
									reportingEndDateErrorSet.add(ErrorCode.E0258.toString() + ":" + elementCode);
								}
							} else {
								LOGGER.error("Date Contains T character ");
								reportingEndDateErrorSet.add(ErrorCode.E0258.toString() + ":" + elementCode);
							}
						}
					} catch (Exception e) {
						LOGGER.error("Exception ", e);
						reportingEndDateErrorSet.add(ErrorCode.E0258.toString() + ":" + elementCode);
					}

					List<String> dimIds = null;

					List<SdmxModelCodeLiteBean> sdmModelCodesLiteBeans = null;
					Integer groupNo;
					if (hashValueDimensionBean != null) {

						hashValueDimensionBean.getDimensionDetailsCategoriesGroupWise().sort((DimensionDetailCategories c1, DimensionDetailCategories c2) -> c1.getGroupNo().compareTo(c2.getGroupNo()));
						String hashValue = null;
						for (DimensionDetailCategories dimensionDetailCategories : hashValueDimensionBean.getDimensionDetailsCategoriesGroupWise()) {
							if (dimensionDetailCategories.getOpenDimension() != null) {
								for (DimensionCodeListValueBean dimensionCodeListValueBean : dimensionDetailCategories.getOpenDimension()) {
									dimensionCodeListValueBean.setClValueCode(k.get(dimensionCodeListValueBean.getDimConceptId()));
								}
							}

							if (dimensionDetailCategories.getClosedDim() != null) {
								for (DimensionCodeListValueBean dimensionCodeListValueBean : dimensionDetailCategories.getClosedDim()) {
									dimensionCodeListValueBean.setClValueCode(k.get(dimensionCodeListValueBean.getDimConceptId()));
								}
							}

							// sorting open dimension
							if (!CollectionUtils.isEmpty(dimensionDetailCategories.getOpenDimension())) {
								dimensionDetailCategories.getOpenDimension().sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
							}

							// sorting closed dimension
							if (!CollectionUtils.isEmpty(dimensionDetailCategories.getClosedDim())) {
								dimensionDetailCategories.getClosedDim().sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
							}

							dimensionDetailCategories.setDsdId(elementCode);
							dimensionDetailCategories.setElementVersion(elementVersion);
							dimensionDetailCategories.setModelOtherDetails(modelOtherDetails);
							groupNo = dimensionDetailCategories.getGroupNo();
							dimensionDetailCategories.setGroupNo(null);
							hashValue = getHashCodeValueForDimensionDetailsCategoriedObject(dimensionDetailCategories);
							dimensionDetailCategories.setGroupNo(groupNo);

							sdmModelCodesLiteBeans = hashAndDmidValueMap.get(hashValue);

							if (!CollectionUtils.isEmpty(sdmModelCodesLiteBeans)) {
								dimIds = new ArrayList<>();
								for (SdmxModelCodeLiteBean sdmxModelCodeLiteBean : sdmModelCodesLiteBeans) {
									if (sdmxModelCodeLiteBean.getDsdCode().equals(elementCode) && sdmxModelCodeLiteBean.getDsdVersion().equals(elementVersion)) {
										// Here need to add cell reference condition also 
										dimIds.add(sdmxModelCodeLiteBean.getModelCode());
									}
								}

								if (!CollectionUtils.isEmpty(dimIds)) {
									break;
								} else {
									notPresentHashValues.add(hashValue);
								}
							} else {
								notPresentHashValues.add(hashValue);
							}
						}

						if (CollectionUtils.isEmpty(dimIds)) {
							LOGGER.error("------------------------------------------------------");
							LOGGER.error("HASH Value Not Foud for DSD Code = " + elementCode + " OBS position : " + i);
							LOGGER.error("Not Present Hash Value as Below");
							notPresentHashValues.forEach((e) -> {
								LOGGER.error(new String(Base64.getDecoder().decode(e)));
							});
							dmidNotFoundErrorSet.add(ErrorCode.E0259.toString() + ":" + elementCode);
						} /*
							 * else if (dimIds.size() > 1) {
							 * multipleDmidInDBErrorSet.add(ErrorCode.E1682.toString() + ":" + elementCode);
							 * }
							 */
						/*else if (documentDmidSet.contains(dimIds.get(0))) {
							LOGGER.error("------------------------------------------------------");
							LOGGER.error("Dupliate HASH Value Foubd for DSD Code = " + elementCode + " OBS position : " + i);
							LOGGER.error("Duplicate Hash Value as Below");
							LOGGER.error(new String(Base64.getDecoder().decode(hashValue)));
							multipleDmidInDocErrorSet.add(ErrorCode.E1683.toString() + ":" + elementCode);
						}*/ else {
							if (dmIdMap.containsKey(keyDate)) {
								List<String> list = dmIdMap.get(keyDate);
								list.add(dimIds.get(0));
								dmIdMap.put(keyDate, list);
							} else {
								List<String> list = new ArrayList<>();
								list.add(dimIds.get(0));
								dmIdMap.put(keyDate, list);
							}
							documentDmidSet.add(dimIds.get(0));
							documentDmidAndElementCodeSet.add(elementCode + "~" + k.get(DMID));
						}
					} else {
						elementCodeAndVersionErrorSet.add(ErrorCode.E1684.toString() + ":" + elementCode);
					}
					i++;
				}
				dmIdMapForAllElement.put(elementCode, dmIdMap);
			}

		}

		if (!documentDmidSet.isEmpty()) {
			getSdmxReturnModelCodeBeans(documentDmidSet, dbDimCodeAndReturnListMap, dbDmidAndElementCodeSet);
		}

		if (!reportingEndDateErrorSet.isEmpty()) {
			reportingEndDateErrorSet.forEach(f -> ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, f, fieldCheckListMap));
		} else {
			ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldCheckListMap);
		}

		if (!dmidNotFoundErrorSet.isEmpty() || !multipleDmidInDBErrorSet.isEmpty() || !multipleDmidInDocErrorSet.isEmpty() || !elementCodeAndVersionErrorSet.isEmpty() || !elementCodeExistenceErrorSet.isEmpty()) {
			if (!dmidNotFoundErrorSet.isEmpty()) {
				dmidNotFoundErrorSet.forEach(f -> ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.DMID_CHECK.getConstantVal(), false, f, fieldCheckListMap));
			}

			if (!multipleDmidInDBErrorSet.isEmpty()) {
				multipleDmidInDBErrorSet.forEach(f -> ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.DMID_CHECK.getConstantVal(), false, f, fieldCheckListMap));
			}

			if (!multipleDmidInDocErrorSet.isEmpty()) {
				multipleDmidInDocErrorSet.forEach(f -> ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.DMID_CHECK.getConstantVal(), false, f, fieldCheckListMap));
			}

			if (!elementCodeAndVersionErrorSet.isEmpty()) {
				elementCodeAndVersionErrorSet.forEach(f -> ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.DMID_CHECK.getConstantVal(), false, f, fieldCheckListMap));
			}

			if (!elementCodeExistenceErrorSet.isEmpty()) {
				elementCodeExistenceErrorSet.forEach(f -> ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.DMID_CHECK.getConstantVal(), false, f, fieldCheckListMap));
			}
		} else {
			ebrFileUploadService.insertValueIntoStatusMap(MetaDataCheckConstants.DMID_CHECK.getConstantVal(), true, null, fieldCheckListMap);
		}

		return !reportingEndDateErrorSet.isEmpty() || !elementCodeExistenceErrorSet.isEmpty() || !dmidNotFoundErrorSet.isEmpty() || !multipleDmidInDBErrorSet.isEmpty() || !multipleDmidInDocErrorSet.isEmpty() || !elementCodeAndVersionErrorSet.isEmpty();
	}

	private Map<String, List<SdmxModelCodeLiteBean>> getHashAndDmidValueMap(List<HashValueDimensionBean> hashValueDimensionBeans, List<DataSet> dataSets, ModelOtherDetails modelOtherDetails) {

		List<String> hashValues = new ArrayList<>();

		for (DataSet f : dataSets) {
			String elementCode = f.getStructureRef().getCode();
			String elementVersion = f.getStructureRef().getVersion();

			List<Map<String, String>> lisvalueMap = f.getData();

			for (Map<String, String> k : lisvalueMap) {
				// get DMID Logic
				HashValueDimensionBean hashValueDimensionBean = new HashValueDimensionBean();
				hashValueDimensionBean.setDsdId(elementCode);
				hashValueDimensionBean.setElementVersion(elementVersion);

				if (hashValueDimensionBeans.indexOf(hashValueDimensionBean) != -1) {
					hashValueDimensionBean = hashValueDimensionBeans.get(hashValueDimensionBeans.indexOf(hashValueDimensionBean));
				} else {
					hashValueDimensionBean = null;
				}

				Integer groupNo;
				if (hashValueDimensionBean != null) {

					hashValueDimensionBean.getDimensionDetailsCategoriesGroupWise().sort((DimensionDetailCategories c1, DimensionDetailCategories c2) -> c1.getGroupNo().compareTo(c2.getGroupNo()));

					for (DimensionDetailCategories dimensionDetailCategories : hashValueDimensionBean.getDimensionDetailsCategoriesGroupWise()) {
						if (dimensionDetailCategories.getOpenDimension() != null) {
							for (DimensionCodeListValueBean dimensionCodeListValueBean : dimensionDetailCategories.getOpenDimension()) {
								dimensionCodeListValueBean.setClValueCode(k.get(dimensionCodeListValueBean.getDimConceptId()));
							}
						}

						if (dimensionDetailCategories.getClosedDim() != null) {
							for (DimensionCodeListValueBean dimensionCodeListValueBean : dimensionDetailCategories.getClosedDim()) {
								dimensionCodeListValueBean.setClValueCode(k.get(dimensionCodeListValueBean.getDimConceptId()));
							}
						}

						// sorting open dimension
						if (!CollectionUtils.isEmpty(dimensionDetailCategories.getOpenDimension())) {
							dimensionDetailCategories.getOpenDimension().sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
						}

						// sorting closed dimension
						if (!CollectionUtils.isEmpty(dimensionDetailCategories.getClosedDim())) {
							dimensionDetailCategories.getClosedDim().sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
						}

						dimensionDetailCategories.setDsdId(elementCode);
						dimensionDetailCategories.setElementVersion(elementVersion);
						dimensionDetailCategories.setModelOtherDetails(modelOtherDetails);
						groupNo = dimensionDetailCategories.getGroupNo();
						dimensionDetailCategories.setGroupNo(null);
						hashValues.add(getHashCodeValueForDimensionDetailsCategoriedObject(dimensionDetailCategories));
						dimensionDetailCategories.setGroupNo(groupNo);
					}
				}
			}
		}

		Map<String, List<SdmxModelCodeLiteBean>> hashDmidMaps = new HashMap<>();
		if (!hashValues.isEmpty()) {
			List<SdmxModelCodeLiteBean> sdmxModelCodeEntitys = sdmxModelCodesRepo.getDMIdByHashIn(hashValues);
			if (!CollectionUtils.isEmpty(sdmxModelCodeEntitys)) {
				for (SdmxModelCodeLiteBean sdmxModelCodesEntity : sdmxModelCodeEntitys) {
					if (hashDmidMaps.containsKey(sdmxModelCodesEntity.getHashCode())) {
						hashDmidMaps.get(sdmxModelCodesEntity.getHashCode()).add(sdmxModelCodesEntity);
					} else {
						List<SdmxModelCodeLiteBean> dmids = new ArrayList<>();
						dmids.add(sdmxModelCodesEntity);
						hashDmidMaps.put(sdmxModelCodesEntity.getHashCode(), dmids);
					}
				}
			}
		}

		return hashDmidMaps;
	}

	private String getHashCodeValueForDimensionDetailsCategoriedObject(DimensionDetailCategories dimensionDetailCategories) {
		String jsonString = JsonUtility.getGsonObject().toJson(dimensionDetailCategories);

		SortedMap<String, Object> retMap = JsonUtility.getGsonObject().fromJson(jsonString, new TypeToken<TreeMap<String, Object>>() {
		}.getType());

		jsonString = JsonUtility.getGsonObject().toJson(retMap);

		return Base64.getEncoder().encodeToString(jsonString.getBytes());
	}

	public SDMXReturnEntityMapp isMappingExistForReturnAndEntity(String returnCode, String entityCode) {
		try {
			return sdmxReturnEntityMapRepo.isMappingExistForReturnAndEntity(entityCode, returnCode);
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}
		return null;
	}

}
