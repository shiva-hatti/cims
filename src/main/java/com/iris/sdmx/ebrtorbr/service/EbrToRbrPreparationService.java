/**
 * 
 */
package com.iris.sdmx.ebrtorbr.service;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.controller.DateValidationsController;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.FilingCalendarDto;
import com.iris.dto.FillingEndDatesBean;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.FilingStatus;
import com.iris.model.Frequency;
import com.iris.model.Return;
import com.iris.model.ReturnProperty;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.UploadChannel;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.model.WorkFlowMasterBean;
import com.iris.rbrToEbr.entity.EbrRbrFlow;
import com.iris.rbrToEbr.entity.EbrRbrFlowMaster;
import com.iris.rbrToEbr.repo.EbrRbrFlowMasterRepo;
import com.iris.rbrToEbr.repo.EbrRbrFlowRepo;
import com.iris.repository.ReturnPropertyRepository;
import com.iris.repository.ReturnRepo;
import com.iris.repository.ReturnTemplateRepository;
import com.iris.repository.ReturnUploadDetailsRepository;
import com.iris.sdmx.ebrtorbr.bean.ElementDataPointBean;
import com.iris.sdmx.ebrtorbr.bean.ReturnDataPointBean;
import com.iris.sdmx.ebrtorbr.entity.SdmxEbrToRbrPreparation;
import com.iris.sdmx.ebrtorbr.repo.SdmxEbrToRbrPreparationRepo;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnModelMappingBean;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnPreviewEntity;
import com.iris.sdmx.exceltohtml.repo.SdmxReturnModelInfoRepo;
import com.iris.sdmx.exceltohtml.repo.SdmxReturnPreviewRepo;
import com.iris.sdmx.returnentmapp.bean.SDMXReturnEntityMapp;
import com.iris.sdmx.returnentmapp.repo.SDMXReturnEntityMapRepo;
import com.iris.sdmx.upload.bean.RepDateReturnDataPoint;
import com.iris.sdmx.upload.bean.ReturnDataPoint;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.service.FilingCalendarModifiedService;
import com.iris.service.GenericService;
import com.iris.util.AESV2;
import com.iris.util.DateAndTimeArithmetic;
import com.iris.util.DateUtil;
import com.iris.util.JsonUtility;
import com.iris.util.constant.FilingStatusConstants;
import com.iris.util.constant.UploadFilingConstants;

/**
 * @author sajadhav
 *
 */
@Service
public class EbrToRbrPreparationService implements GenericService<SdmxEbrToRbrPreparation, Long> {

	private static final Logger LOGGER = LogManager.getLogger(EbrToRbrPreparationService.class);

	@Autowired
	private SdmxReturnModelInfoRepo sdmxReturnModelInfoRepo;

	@Autowired
	private ReturnPropertyRepository returnPropertyRepo;

	@Autowired
	private ReturnRepo returnRepo;

	@Autowired
	private SDMXReturnEntityMapRepo returnEntityMapRepo;

	@Autowired
	private ReturnTemplateRepository returnTemplateRepository;

	@Autowired
	private SdmxReturnPreviewRepo sdmxReturnPreviewRepo;

	@Autowired
	private SdmxEbrToRbrPreparationRepo sdmxEbrToRbrPreparationRepo;

	@Autowired
	private FilingCalendarModifiedService filingCalendarModifiedService;

	@Autowired
	private EntityManager em;

	@Autowired
	private EbrRbrFlowMasterRepo ebrRbrFlowMasterRepo;

	@Autowired
	private EbrRbrFlowRepo ebrRbrFlowRepo;

	@Autowired
	private ReturnUploadDetailsRepository returnsUploadDetailsRepo;

	private static final String DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";

	@Override
	public SdmxEbrToRbrPreparation add(SdmxEbrToRbrPreparation entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SdmxEbrToRbrPreparation entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxEbrToRbrPreparation> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxEbrToRbrPreparation getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxEbrToRbrPreparation> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxEbrToRbrPreparation> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxEbrToRbrPreparation> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxEbrToRbrPreparation> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxEbrToRbrPreparation> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxEbrToRbrPreparation bean) throws ServiceException {

	}

	@Transactional(rollbackOn = Exception.class)
	public void watchForAnySDMXFilingReceived(SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation) throws Exception {
		Map<String, Set<String>> mandateReceivedDmidsUsingFiling = new HashMap<>();
		Map<String, Set<String>> optionalReceivedDmidsUsingFiling = new HashMap<>();

		FilingCalendarDto filingCalendarDto = getFilingWindowStatus(sdmxEbrToRbrPreparation);
		boolean isAnyDataPointReceived = false;
		boolean isAllDmidsReceived = false;
		ReturnsUploadDetails returnsUploadDetails = null;
		if (filingCalendarDto != null && filingCalendarDto.getFilingStatus().equals(UploadFilingConstants.FILING_WINDOW_OPEN)) {
			Map<String, Set<String>> mandateDmidsToBeReceived = getMandateNonReceivedDmids(sdmxEbrToRbrPreparation);
			Map<String, Set<String>> optionalDmidsToBeReceived = getOptionalNonReceivedDmids(sdmxEbrToRbrPreparation);

			Set<Long> elementAuditIdsUsedForFiling = new HashSet<>();
			if (mandateDmidsToBeReceived.size() > 0 || optionalDmidsToBeReceived.size() > 0) {
				Set<Long> existingElementIds = getExistingElementIds(sdmxEbrToRbrPreparation);

				isAllDmidsReceived = getDmidsWhichAreFiledByEntity(mandateDmidsToBeReceived, optionalDmidsToBeReceived, sdmxEbrToRbrPreparation, mandateReceivedDmidsUsingFiling, optionalReceivedDmidsUsingFiling, elementAuditIdsUsedForFiling, existingElementIds);

				// update mandateReceivedjson and optionalReceivedjson
				if (mandateReceivedDmidsUsingFiling.size() > 0) {
					isAnyDataPointReceived = true;
					ReturnDataPointBean mandateReceiveReturnDataPointBean = getMandateReceivedReturnDataPointBean(sdmxEbrToRbrPreparation, mandateReceivedDmidsUsingFiling);
					sdmxEbrToRbrPreparation.setMandateDatapointReceivedJson(JsonUtility.getGsonObject().toJson(mandateReceiveReturnDataPointBean));
				}

				if (optionalReceivedDmidsUsingFiling.size() > 0) {
					isAnyDataPointReceived = true;
					ReturnDataPointBean optionalReceiveReturnDataPointBean = getOptionalReceivedReturnDataPointBean(sdmxEbrToRbrPreparation, optionalReceivedDmidsUsingFiling);
					sdmxEbrToRbrPreparation.setOptionalDatapointReceivedJson(JsonUtility.getGsonObject().toJson(optionalReceiveReturnDataPointBean));
				}

				if (!elementAuditIdsUsedForFiling.isEmpty() && sdmxEbrToRbrPreparation.getSdmxElementId() != null && !sdmxEbrToRbrPreparation.getSdmxElementId().equals("")) {
					String str[] = sdmxEbrToRbrPreparation.getSdmxElementId().split(",");
					for (String string : str) {
						elementAuditIdsUsedForFiling.add(Long.parseLong(string));
					}
				}

				if (!elementAuditIdsUsedForFiling.isEmpty()) {
					sdmxEbrToRbrPreparation.setSdmxElementId(elementAuditIdsUsedForFiling.stream().map(Object::toString).collect(Collectors.joining(",")));
				}

				if (isAllDmidsReceived) {
					sdmxEbrToRbrPreparation.setIsFilingDone("1");
					sdmxEbrToRbrPreparation.setReportPreEndOn(new Date());
					returnsUploadDetails = getReturnsUploadDetailsBean(sdmxEbrToRbrPreparation);
					returnsUploadDetails.setSdmxEbrToRbrPreparation(sdmxEbrToRbrPreparation);
					sdmxEbrToRbrPreparation.setReturnsUploadDetails(returnsUploadDetails);
					sdmxEbrToRbrPreparation.setLastUpdatedOn(new Date());

					sdmxEbrToRbrPreparationRepo.save(sdmxEbrToRbrPreparation);
					List<EbrRbrFlow> ebrRbrFlows = prepareControlTableObject(returnsUploadDetails, sdmxEbrToRbrPreparation.getEbrToRbrPreparationId());
					ebrRbrFlowRepo.saveAll(ebrRbrFlows);

					SdmxEbrToRbrPreparation newSdmxEbrToRbrPreparation = new SdmxEbrToRbrPreparation();
					newSdmxEbrToRbrPreparation.setRoleId(sdmxEbrToRbrPreparation.getRoleId());
					newSdmxEbrToRbrPreparation.setReportPreStartOn(new Date());
					newSdmxEbrToRbrPreparation.setReturnObj(sdmxEbrToRbrPreparation.getReturnObj());
					newSdmxEbrToRbrPreparation.setEntity(sdmxEbrToRbrPreparation.getEntity());
					newSdmxEbrToRbrPreparation.setCreatedOn(new Date());
					newSdmxEbrToRbrPreparation.setStartDate(sdmxEbrToRbrPreparation.getStartDate());
					newSdmxEbrToRbrPreparation.setIsFilingDone("0");
					newSdmxEbrToRbrPreparation.setEndDate(sdmxEbrToRbrPreparation.getEndDate());
					newSdmxEbrToRbrPreparation.setMandateDatapointExpectedJson(sdmxEbrToRbrPreparation.getMandateDatapointExpectedJson());
					newSdmxEbrToRbrPreparation.setOptionalDatapointExpectedJson(sdmxEbrToRbrPreparation.getOptionalDatapointExpectedJson());
					sdmxEbrToRbrPreparationRepo.save(newSdmxEbrToRbrPreparation);
				} else {
					if (isAnyDataPointReceived) {
						sdmxEbrToRbrPreparation.setLastUpdatedOn(new Date());
						sdmxEbrToRbrPreparationRepo.save(sdmxEbrToRbrPreparation);
					}
				}
			}
		} else if (filingCalendarDto != null && filingCalendarDto.getFilingStatus().equals(UploadFilingConstants.FILING_WINDOW_CLOSED)) {
			sdmxEbrToRbrPreparation.setIsFilingDone("2");
			sdmxEbrToRbrPreparationRepo.save(sdmxEbrToRbrPreparation);
		} else {
			LOGGER.info("Filing window not yet open");
		}
	}

	private Set<Long> getExistingElementIds(SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation) {

		List<SdmxEbrToRbrPreparation> sdmxEbrToRbrPreparations = sdmxEbrToRbrPreparationRepo.getExisitngFilingRecord(sdmxEbrToRbrPreparation.getEntity().getEntityId(), sdmxEbrToRbrPreparation.getReturnObj().getReturnId(), sdmxEbrToRbrPreparation.getEndDate());

		Set<Long> elementAuditIds = new HashSet<>();

		sdmxEbrToRbrPreparations.stream().forEach(f -> {
			if (f.getSdmxElementId() != null) {
				for (String string : f.getSdmxElementId().split(",")) {
					elementAuditIds.add(Long.parseLong(string));
				}
			}
		});

		return elementAuditIds;
	}

	@Transactional(rollbackOn = Exception.class)
	public int watchForAnySDMXFilingReceived() throws Exception {
		//		List<SdmxEbrToRbrPreparation>  ebrToRbrPreparation = sdmxEbrToRbrPreparationRepo.getNotCompletedEBRFiling();
		//		Map<String, Set<String>> mandateReceivedDmidsUsingFiling;
		//		Map<String, Set<String>> optionalReceivedDmidsUsingFiling;
		//		
		//		//Count logic
		//		for (SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation : ebrToRbrPreparation) {
		//				if(sdmxEbrToRbrPreparation.getMandateDatapointExpectedJson() == null) {
		//					continue;
		//				}
		//				
		//				mandateReceivedDmidsUsingFiling = new HashMap<>();
		//				optionalReceivedDmidsUsingFiling = new HashMap<>();
		//				FilingCalendarDto filingCalendarDto = getFilingWindowStatus(sdmxEbrToRbrPreparation);
		//				if(filingCalendarDto!= null && filingCalendarDto.getFilingStatus().equals(UploadFilingConstants.FILING_WINDOW_OPEN)) {
		//					Map<String, Set<String>> mandateDmidsToBeReceived = getMandateNonReceivedDmids(sdmxEbrToRbrPreparation);
		//					Map<String, Set<String>> optionalDmidsToBeReceived = getOptionalNonReceivedDmids(sdmxEbrToRbrPreparation);
		//
		//					if(mandateDmidsToBeReceived.size() > 0  || optionalDmidsToBeReceived.size() > 0) {
		//						boolean isAllDmidsReceived = getDmidsWhichAreFiledByEntity(mandateDmidsToBeReceived, optionalDmidsToBeReceived, sdmxEbrToRbrPreparation, mandateReceivedDmidsUsingFiling, optionalReceivedDmidsUsingFiling);
		//						
		//						// update mandateReceivedjson and optionalReceivedjson
		//						if(mandateReceivedDmidsUsingFiling.size() > 0) {
		//							ReturnDataPointBean mandateReceiveReturnDataPointBean = getMandateReceivedReturnDataPointBean(sdmxEbrToRbrPreparation, mandateReceivedDmidsUsingFiling);
		//							sdmxEbrToRbrPreparation.setMandateDatapointReceivedJson(JsonUtility.getGsonObject().toJson(mandateReceiveReturnDataPointBean));
		//						}
		//						
		//						if(optionalReceivedDmidsUsingFiling.size() > 0) {
		//							ReturnDataPointBean optionalReceiveReturnDataPointBean = getOptionalReceivedReturnDataPointBean(sdmxEbrToRbrPreparation, optionalReceivedDmidsUsingFiling);
		//							sdmxEbrToRbrPreparation.setOptionalDatapointReceivedJson(JsonUtility.getGsonObject().toJson(optionalReceiveReturnDataPointBean));
		//						}
		//
		//						sdmxEbrToRbrPreparation.setLastUpdatedOn(new Date());
		//						if(isAllDmidsReceived) {
		//							sdmxEbrToRbrPreparation.setIsFilingDone("1");
		//							sdmxEbrToRbrPreparation.setReportPreEndOn(new Date());
		//							
		//							ReturnsUploadDetails returnsUploadDetails = getReturnsUploadDetailsBean(sdmxEbrToRbrPreparation);
		//							prepareControlTableObject(returnsUploadDetails);
		//							returnsUploadDetails.setSdmxEbrToRbrPreparation(sdmxEbrToRbrPreparation);
		//							sdmxEbrToRbrPreparation.setReturnsUploadDetails(returnsUploadDetails);
		//							sdmxEbrToRbrPreparationRepo.save(sdmxEbrToRbrPreparation);	
		//						}
		//					}
		//				}else if(filingCalendarDto!= null && filingCalendarDto.getFilingStatus().equals(UploadFilingConstants.FILING_WINDOW_CLOSED)) {
		//					sdmxEbrToRbrPreparation.setIsFilingDone("2");
		//					sdmxEbrToRbrPreparationRepo.save(sdmxEbrToRbrPreparation);					
		//				}else {
		//					System.out.println("Filing window not yet open");
		//				}
		//		}
		//		
		//		return ebrToRbrPreparation.size();
		return 0;
	}

	@Transactional(rollbackOn = Exception.class)
	public int insertReturnAndEntityWiseFilingEntry(Date currentDate) throws Exception {
		List<Return> returnsList = returnRepo.getActiveReturns();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DD_SLASH_MM_SLASH_YYYY);

		Map<Long, String> returnAndReportingDateMap = getReturnAndReportingDateMap(returnsList, currentDate, simpleDateFormat);

		if (!returnAndReportingDateMap.isEmpty()) {

			Map<Integer, List<Integer>> returnPropertyMap = getReturnPropertyMap();

			Set<Long> returnIds = getReturnIdsWhichNeedsToBeConsideredForFiling(returnAndReportingDateMap, currentDate);

			Map<Long, Long> returnAndReturnPreviewIdMap = getReturnAndReturnPreviewIdMap(currentDate, returnIds);

			if (!CollectionUtils.isEmpty(returnAndReturnPreviewIdMap)) {
				Map<Long, ReturnDataPointBean> mandatoryDataPointMap = new HashMap<>();
				Map<Long, ReturnDataPointBean> optionalDataPointMap = new HashMap<>();

				prepareReturnModelMappings(mandatoryDataPointMap, optionalDataPointMap, returnAndReturnPreviewIdMap);

				Set<Long> mandatoryReturnIds = mandatoryDataPointMap.keySet().stream().collect(Collectors.toSet());
				Set<Long> optionalReturnIds = optionalDataPointMap.keySet().stream().collect(Collectors.toSet());

				Set<Long> allReturnIds = new HashSet<>();
				if (!CollectionUtils.isEmpty(mandatoryReturnIds)) {
					allReturnIds.addAll(mandatoryReturnIds);
				}

				if (!CollectionUtils.isEmpty(optionalReturnIds)) {
					allReturnIds.addAll(optionalReturnIds);
				}

				if (!CollectionUtils.isEmpty(mandatoryReturnIds)) {
					List<SDMXReturnEntityMapp> returnEntityMappingNews = returnEntityMapRepo.getReturnEntityMappingData(new ArrayList<>(allReturnIds));

					List<SdmxEbrToRbrPreparation> sdmxEbrToRbrPreparations = new ArrayList<>();
					Calendar c = Calendar.getInstance();
					Date startDate;
					Date endDate;
					Date reportPreDate;
					String mandatoryJson;
					String optionalJson;
					for (SDMXReturnEntityMapp returnEntityMappingNew : returnEntityMappingNews) {
						mandatoryJson = null;
						optionalJson = null;
						startDate = simpleDateFormat.parse(returnAndReportingDateMap.get(returnEntityMappingNew.getReturnObj().getReturnId()).split("~")[1]);
						endDate = simpleDateFormat.parse(returnAndReportingDateMap.get(returnEntityMappingNew.getReturnObj().getReturnId()).split("~")[0]);
						c.setTime(endDate);
						c.add(Calendar.DATE, 1);
						reportPreDate = c.getTime();

						if (mandatoryDataPointMap.get(returnEntityMappingNew.getReturnObj().getReturnId()) != null) {
							mandatoryJson = JsonUtility.getGsonObject().toJson(mandatoryDataPointMap.get(returnEntityMappingNew.getReturnObj().getReturnId()));
						} else {
							continue;
						}

						if (optionalDataPointMap.get(returnEntityMappingNew.getReturnObj().getReturnId()) != null) {
							optionalJson = JsonUtility.getGsonObject().toJson(optionalDataPointMap.get(returnEntityMappingNew.getReturnObj().getReturnId()));
						}

						SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation = getEbrRbrPreparationBean(returnEntityMappingNew, startDate, endDate, reportPreDate, mandatoryJson, optionalJson, null);
						sdmxEbrToRbrPreparations.add(sdmxEbrToRbrPreparation);
					}

					sdmxEbrToRbrPreparationRepo.saveAll(sdmxEbrToRbrPreparations);
					return sdmxEbrToRbrPreparations.size();
				}
			}
		}
		return 0;
	}

	private Map<Long, Long> getReturnAndReturnPreviewIdMap(Date currentDate, Set<Long> returnIds) {
		List<SdmxReturnPreviewEntity> returnPreviewList = sdmxReturnPreviewRepo.fethEBRReturnPreview(currentDate, returnIds);

		Map<Long, Long> returnAndReturnPreviewId = new HashMap<>();

		for (SdmxReturnPreviewEntity returnPreview : returnPreviewList) {
			returnAndReturnPreviewId.put(returnPreview.getReturnId(), returnPreview.getReturnPreviewTypeId());
		}

		return returnAndReturnPreviewId;
	}

	private Set<Long> getReturnIdsWhichNeedsToBeConsideredForFiling(Map<Long, String> returnAndReportingDateMap, Date currentDate) {
		List<Long> returnIds = Arrays.asList(returnAndReportingDateMap.keySet().toArray(new Long[0]));
		List<Long> dbReturnIds = sdmxEbrToRbrPreparationRepo.checkMappingExistForReportingDateAndReturnIds(returnIds, DateManip.convertDateToString(currentDate, DateConstants.YYYY_MM_DD.getDateConstants()));
		if (!CollectionUtils.isEmpty(dbReturnIds)) {
			return returnIds.stream().filter(i -> !dbReturnIds.contains(i)).collect(Collectors.toSet());
		}
		return new HashSet<>(returnIds);
	}

	private FilingCalendarDto getFilingWindowStatus(SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation) {
		Integer returnPropertyValId = null;
		if (sdmxEbrToRbrPreparation.getReturnPropertyVal() != null) {
			returnPropertyValId = sdmxEbrToRbrPreparation.getReturnPropertyVal().getReturnProprtyValId();
		}
		try {
			FilingCalendarDto filingCalendarDto = new FilingCalendarDto();
			String currentDate = DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants());

			DateValidationsController dateValidationsControlObj = new DateValidationsController();
			Map<Long, Map<Long, FillingEndDatesBean>> formFillingWindowMap = filingCalendarModifiedService.fetchFormFillingEndDatesDates(sdmxEbrToRbrPreparation.getReturnObj(), sdmxEbrToRbrPreparation.getEntity(), DateUtil.getStringFormattedDate(sdmxEbrToRbrPreparation.getEndDate(), DateConstants.DD_MM_YYYY.getDateConstants()), returnPropertyValId);

			if (formFillingWindowMap != null && !formFillingWindowMap.isEmpty()) {
				// --- If Filling window is defined in the application Fetch end
				// dates map
				Map<Long, FillingEndDatesBean> fillingWindowEndDatesMap = formFillingWindowMap.get(sdmxEbrToRbrPreparation.getReturnObj().getFrequency().getFrequencyId());
				String startDateCalculated = null;
				String endDateCalculated = null;

				if (!CollectionUtils.isEmpty(fillingWindowEndDatesMap)) {
					startDateCalculated = fillingWindowEndDatesMap.get(0L).getStartDate();
					endDateCalculated = fillingWindowEndDatesMap.get(0L).getGraceDaysDate();

					filingCalendarDto.setStartDate(startDateCalculated);
					filingCalendarDto.setEndDate(endDateCalculated);

					if (DateManip.getDayDiff(currentDate, startDateCalculated, DateConstants.DD_MM_YYYY.getDateConstants()) > 0) {
						filingCalendarDto.setFilingStatus(UploadFilingConstants.FILING_WINDOW_NOT_YET_OPEN);
					} else if (dateValidationsControlObj.getDayDiff(currentDate, startDateCalculated, DateConstants.DD_MM_YYYY.getDateConstants()) <= 0 && dateValidationsControlObj.getDayDiff(currentDate, endDateCalculated, DateConstants.DD_MM_YYYY.getDateConstants()) >= 0) {
						filingCalendarDto.setFilingStatus(UploadFilingConstants.FILING_WINDOW_OPEN);
					} else if (dateValidationsControlObj.getDayDiff(currentDate, startDateCalculated, DateConstants.DD_MM_YYYY.getDateConstants()) <= 0 && dateValidationsControlObj.getDayDiff(currentDate, endDateCalculated, DateConstants.DD_MM_YYYY.getDateConstants()) == -1) {// This 1 should be taken from
																																																																						// constant
						filingCalendarDto.setFilingStatus(UploadFilingConstants.FILING_WINDOW_OPEN);
					} else {
						filingCalendarDto.setFilingStatus(UploadFilingConstants.FILING_WINDOW_CLOSED);
					}
				} else {
					filingCalendarDto.setFilingStatus(UploadFilingConstants.FILING_WINDOW_NOT_YET_OPEN);
				}
			} else {
				filingCalendarDto.setFilingStatus(UploadFilingConstants.FILING_WINDOW_NOT_DEFINED);
			}
			// Temporary added
			filingCalendarDto.setFilingStatus(UploadFilingConstants.FILING_WINDOW_OPEN);

			return filingCalendarDto;
		} catch (ServiceException e) {
			LOGGER.error("Exception : ", e);
		} catch (ParseException e) {
			LOGGER.error("Exception : ", e);
		}
		return null;

	}

	private List<EbrRbrFlow> prepareControlTableObject(ReturnsUploadDetails returnsUploadDetails, Long ebrRbrPreparionId) {
		List<EbrRbrFlowMaster> ebrRbrFlowMasters = ebrRbrFlowMasterRepo.getDataByFlowIdd(7);
		String temporaryJobId = returnsUploadDetails.getEntity().getIfscCode() + "_" + returnsUploadDetails.getReturnObj().getReturnCode() + "_" + DateUtil.getStringFormattedDate(returnsUploadDetails.getEndDate(), "ddMMyyyy");

		String jobId = getJobId(temporaryJobId);

		List<EbrRbrFlow> ebrRbrFlows = new ArrayList<>();
		Integer uploadId = returnsUploadDetailsRepo.getUploadIdForSDMXId(ebrRbrPreparionId);
		for (EbrRbrFlowMaster ebrRbrFlowMaster : ebrRbrFlowMasters) {
			EbrRbrFlow ebrRbrFlow = new EbrRbrFlow();
			ebrRbrFlow.setJobId(jobId);
			ebrRbrFlow.setEntityCode(returnsUploadDetails.getEntity().getEntityCode());
			ebrRbrFlow.setReturnCode(returnsUploadDetails.getReturnObj().getReturnCode());
			if (returnsUploadDetails.getReturnPropertyValue() != null) {
				ebrRbrFlow.setAuditStatus(returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId());
			}
			ebrRbrFlow.setFrequency(returnsUploadDetails.getFrequency().getFrequencyName());
			ebrRbrFlow.setReportingPeriod(returnsUploadDetails.getEndDate());
			ebrRbrFlow.setFlowIdfk(ebrRbrFlowMaster.getFlowId());
			ebrRbrFlow.setFlowName(ebrRbrFlowMaster.getFlowName());
			ebrRbrFlow.setTaskName(ebrRbrFlowMaster.getTaskName());
			ebrRbrFlow.setSequence(ebrRbrFlowMaster.getSequence());
			ebrRbrFlow.setPriority(ebrRbrFlowMaster.getPriority());
			ebrRbrFlow.setStatus(0);
			ebrRbrFlow.setAuditStatus(1);
			ebrRbrFlow.setCreatedBy(returnsUploadDetails.getUploadedBy().getUserName());
			ebrRbrFlow.setCreatedDate(new Date());
			ebrRbrFlow.setUploadId(uploadId);
			ebrRbrFlows.add(ebrRbrFlow);
		}

		return ebrRbrFlows;
	}

	public String getJobId(String temporaryJobId) {
		String startQueryString = "SELECT JOB_ID FROM TBL_CTL_EBR_RBR_FLOW where JOB_ID like '%";
		String endQueryString = "%' order by JOB_ID desc limit 1";
		StringBuilder query = new StringBuilder();
		query.append(startQueryString);
		query.append(temporaryJobId);
		query.append(endQueryString);
		List<Tuple> tuples = em.createNativeQuery(query.toString(), Tuple.class).getResultList();

		String strPattern = "^0+(?!$)";

		if (tuples.size() > 0) {
			for (Tuple tuple : tuples) {
				String jobId = tuple.get("JOB_ID").toString();
				int length = jobId.split("_").length;
				String number = jobId.split("_")[length - 1];
				number = number.replaceAll(strPattern, "");

				Integer numberIntger = Integer.parseInt(number);
				number = numberIntger + 1 + "";

				if (number.length() == 1) {
					return temporaryJobId + "_" + "00" + number;
				} else if (number.length() == 2) {
					return temporaryJobId + "_" + "0" + number;
				} else {
					return temporaryJobId + "_" + number;
				}
			}
		} else {
			return temporaryJobId + "_001";
		}
		return strPattern;
	}

	private ReturnsUploadDetails getReturnsUploadDetailsBean(SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation) {
		ReturnsUploadDetails returnsUploadDetails = new ReturnsUploadDetails();

		returnsUploadDetails.setReturnObj(sdmxEbrToRbrPreparation.getReturnObj());
		returnsUploadDetails.setEntity(sdmxEbrToRbrPreparation.getEntity());

		FilingStatus filingStatus = new FilingStatus();
		filingStatus.setFilingStatusId(8);
		returnsUploadDetails.setFilingStatus(filingStatus);

		returnsUploadDetails.setStartDate(sdmxEbrToRbrPreparation.getStartDate());
		returnsUploadDetails.setEndDate(sdmxEbrToRbrPreparation.getEndDate());
		returnsUploadDetails.setUploadedDate(new Timestamp(new Date().getTime()));

		UserMaster userMaster = new UserMaster();
		userMaster.setUserId(sdmxEbrToRbrPreparation.getUserId());
		userMaster.setUserName(sdmxEbrToRbrPreparation.getUserName());
		returnsUploadDetails.setUploadedBy(userMaster);

		returnsUploadDetails.setActive("1");
		returnsUploadDetails.setNillable(false);
		returnsUploadDetails.setCurrentWFStep(1);

		WorkFlowMasterBean workflowMaterBean = new WorkFlowMasterBean();
		workflowMaterBean.setWorkflowId(2L);
		returnsUploadDetails.setWorkFlowMaster(workflowMaterBean);

		UserRole userRole = new UserRole();
		userRole.setUserRoleId(sdmxEbrToRbrPreparation.getRoleId());
		returnsUploadDetails.setUploadUsrRole(userRole);

		Frequency frequency = new Frequency();
		frequency.setFrequencyId(sdmxEbrToRbrPreparation.getReturnObj().getFrequency().getFrequencyId());
		frequency.setFrequencyName(ObjectCache.getLabelKeyValue("en", sdmxEbrToRbrPreparation.getReturnObj().getFrequency().getFrequencyName()));
		returnsUploadDetails.setFrequency(frequency);

		returnsUploadDetails.setReportingCurrency("INR");
		returnsUploadDetails.setConversionRate(1);

		UploadChannel uploadChannel = new UploadChannel();
		uploadChannel.setUploadChannelId(5L);
		returnsUploadDetails.setUploadChannel(uploadChannel);

		if (sdmxEbrToRbrPreparation.getReturnPropertyVal() != null) {
			ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
			returnPropertyValue.setReturnProprtyValId(sdmxEbrToRbrPreparation.getReturnPropertyVal().getReturnProprtyValId());
			returnsUploadDetails.setReturnPropertyValue(returnPropertyValue);
		}

		return returnsUploadDetails;
	}

	private ReturnDataPointBean getOptionalReceivedReturnDataPointBean(SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation, Map<String, Set<String>> optionalReceivedDmidsUsingFiling) {
		ReturnDataPointBean optionalReceiveReturnDataPointBean;

		if (sdmxEbrToRbrPreparation.getOptionalDatapointReceivedJson() != null) {
			optionalReceiveReturnDataPointBean = JsonUtility.getGsonObject().fromJson(sdmxEbrToRbrPreparation.getOptionalDatapointReceivedJson(), ReturnDataPointBean.class);
		} else {
			optionalReceiveReturnDataPointBean = new ReturnDataPointBean();
		}

		boolean isDsdCodeFound;
		int totalCount = 0;
		for (Map.Entry<String, Set<String>> optionalReceivedFiling : optionalReceivedDmidsUsingFiling.entrySet()) {
			isDsdCodeFound = false;

			if (optionalReceiveReturnDataPointBean.getDataPointList() != null) {
				for (ElementDataPointBean elementDataPointBean : optionalReceiveReturnDataPointBean.getDataPointList()) {
					if (elementDataPointBean.getDsdCode().equalsIgnoreCase(optionalReceivedFiling.getKey())) {
						isDsdCodeFound = true;
						elementDataPointBean.getDataPoints().addAll(optionalReceivedFiling.getValue());
						totalCount += elementDataPointBean.getDataPoints().size();
						elementDataPointBean.setTotalDataPoints(elementDataPointBean.getDataPoints().size());
					}
				}

				if (!isDsdCodeFound) {
					ElementDataPointBean elementDataPointBean = new ElementDataPointBean();
					elementDataPointBean.setDsdCode(optionalReceivedFiling.getKey());
					elementDataPointBean.setDataPoints(optionalReceivedFiling.getValue());
					totalCount += elementDataPointBean.getDataPoints().size();
					elementDataPointBean.setTotalDataPoints(elementDataPointBean.getDataPoints().size());
					optionalReceiveReturnDataPointBean.getDataPointList().add(elementDataPointBean);
				}
			} else {
				ElementDataPointBean elementDataPointBean = new ElementDataPointBean();
				elementDataPointBean.setDsdCode(optionalReceivedFiling.getKey());
				elementDataPointBean.setDataPoints(optionalReceivedFiling.getValue());
				totalCount += elementDataPointBean.getDataPoints().size();
				elementDataPointBean.setTotalDataPoints(elementDataPointBean.getDataPoints().size());
				List<ElementDataPointBean> elementDataPointBeans = new ArrayList<>();
				elementDataPointBeans.add(elementDataPointBean);
				optionalReceiveReturnDataPointBean.setDataPointList(elementDataPointBeans);
			}
		}

		optionalReceiveReturnDataPointBean.setTotalDataPoints(optionalReceiveReturnDataPointBean.getDataPointList().stream().map(ElementDataPointBean::getDataPoints).filter(rs -> rs != null).mapToInt(Set::size).sum());
		return optionalReceiveReturnDataPointBean;
	}

	private ReturnDataPointBean getMandateReceivedReturnDataPointBean(SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation, Map<String, Set<String>> mandateReceivedDmidsUsingFiling) {
		ReturnDataPointBean mandateReceiveReturnDataPointBean;
		if (sdmxEbrToRbrPreparation.getMandateDatapointReceivedJson() != null) {
			mandateReceiveReturnDataPointBean = JsonUtility.getGsonObject().fromJson(sdmxEbrToRbrPreparation.getMandateDatapointReceivedJson(), ReturnDataPointBean.class);
		} else {
			mandateReceiveReturnDataPointBean = new ReturnDataPointBean();
		}

		boolean isDsdCodeFound;

		for (Map.Entry<String, Set<String>> manDateReceivedFiling : mandateReceivedDmidsUsingFiling.entrySet()) {
			isDsdCodeFound = false;
			if (mandateReceiveReturnDataPointBean.getDataPointList() != null) {
				for (ElementDataPointBean elementDataPointBean : mandateReceiveReturnDataPointBean.getDataPointList()) {
					if (elementDataPointBean.getDsdCode().equalsIgnoreCase(manDateReceivedFiling.getKey())) {
						isDsdCodeFound = true;
						elementDataPointBean.getDataPoints().addAll(manDateReceivedFiling.getValue());
						elementDataPointBean.setTotalDataPoints(elementDataPointBean.getDataPoints().size());
					}
				}

				if (!isDsdCodeFound) {
					ElementDataPointBean elementDataPointBean = new ElementDataPointBean();
					elementDataPointBean.setDsdCode(manDateReceivedFiling.getKey());
					elementDataPointBean.setDataPoints(manDateReceivedFiling.getValue());
					elementDataPointBean.setTotalDataPoints(elementDataPointBean.getDataPoints().size());
					mandateReceiveReturnDataPointBean.getDataPointList().add(elementDataPointBean);
				}
			} else {
				ElementDataPointBean elementDataPointBean = new ElementDataPointBean();
				elementDataPointBean.setDsdCode(manDateReceivedFiling.getKey());
				elementDataPointBean.setDataPoints(manDateReceivedFiling.getValue());
				elementDataPointBean.setTotalDataPoints(manDateReceivedFiling.getValue().size());
				List<ElementDataPointBean> elementDataPointList = new ArrayList<>();
				elementDataPointList.add(elementDataPointBean);
				mandateReceiveReturnDataPointBean.setDataPointList(elementDataPointList);
			}
		}

		mandateReceiveReturnDataPointBean.setTotalDataPoints(mandateReceiveReturnDataPointBean.getDataPointList().stream().map(ElementDataPointBean::getDataPoints).filter(rs -> rs != null).mapToInt(Set::size).sum());
		return mandateReceiveReturnDataPointBean;
	}

	public boolean getDmidsWhichAreFiledByEntity(Map<String, Set<String>> mandateNonReceivedDmids, Map<String, Set<String>> optionalNonReceivedDmids, SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation, Map<String, Set<String>> mandateReceivedDmids, Map<String, Set<String>> optionalReceivedDmids, Set<Long> elementIdList, Set<Long> existingElementIdList) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append(" SELECT eleAud.ELEMENT_AUDIT_ID, eleAud.ELEMENT_CODE, eleAud.ELE_RETURN_REF, fileAud.USER_ID_FK, fileAud.ROLE_ID_FK, usr.USER_NAME  " + " FROM TBL_ELEMENT_AUDIT eleAud, TBL_FILE_DETAILS fileAud, TBL_USER_MASTER usr" + " where UPPER(fileAud.IFSC_CODE) = '" + sdmxEbrToRbrPreparation.getEntity().getIfscCode().toUpperCase() + "'" + " and eleAud.STATUS = " + FilingStatusConstants.EBR_ETL_SUCCESS_ID.getConstantIntVal() + " and fileAud.FILING_STATUS_ID_FK = " + FilingStatusConstants.EBR_ETL_SUCCESS_ID.getConstantIntVal() + " ");

		if (!existingElementIdList.isEmpty()) {
			stringBuffer.append(" and eleAud.ELEMENT_AUDIT_ID not in (" + existingElementIdList.stream().map(Object::toString).collect(Collectors.joining(",")) + ")");
		}

		if (sdmxEbrToRbrPreparation.getReturnPropertyVal() != null) {
			stringBuffer.append(" and eleAud.RETURN_PROPERTY_VAL_ID_FK  = " + sdmxEbrToRbrPreparation.getReturnPropertyVal().getReturnProprtyValId() + "");
		} else {
			stringBuffer.append(" and eleAud.RETURN_PROPERTY_VAL_ID_FK is null ");
		}
		stringBuffer.append(" and usr.USER_ID = fileAud.USER_ID_FK and fileAud.ID = eleAud.FILE_DETAILS_ID_FK");
		stringBuffer.append(" and JSON_SEARCH(ELE_RETURN_REF -> '$[*].endDate', 'one','" + simpleDateFormat.format(sdmxEbrToRbrPreparation.getEndDate()) + "')");
		stringBuffer.append(" and JSON_SEARCH(ELE_RETURN_REF -> '$[*].returnList[*].returnCode', 'one','" + sdmxEbrToRbrPreparation.getReturnObj().getReturnCode() + "')");
		stringBuffer.append(" and (");

		int i = 0;

		for (Map.Entry<String, Set<String>> valueMap : mandateNonReceivedDmids.entrySet()) {
			for (String dmid : valueMap.getValue()) {
				if (i == 0) {
					stringBuffer.append(" JSON_SEARCH(ELE_RETURN_REF -> '$[*].returnList[*].dataPoints[*]', 'one','" + dmid + "')");
				} else {
					stringBuffer.append(" or ");
					stringBuffer.append(" JSON_SEARCH(ELE_RETURN_REF -> '$[*].returnList[*].dataPoints[*]', 'one','" + dmid + "')");
				}
				i++;
			}
		}

		for (Map.Entry<String, Set<String>> valueMap : optionalNonReceivedDmids.entrySet()) {
			for (String dmid : valueMap.getValue()) {
				if (i == 0) {
					stringBuffer.append(" JSON_SEARCH(ELE_RETURN_REF -> '$[*].returnList[*].dataPoints[*]', 'one','" + dmid + "')");
				} else {
					stringBuffer.append(" or ");
					stringBuffer.append(" JSON_SEARCH(ELE_RETURN_REF -> '$[*].returnList[*].dataPoints[*]', 'one','" + dmid + "')");
				}
				i++;
			}
		}
		stringBuffer.append(")");
		Query query = em.createNativeQuery(stringBuffer.toString(), Tuple.class);
		List<Tuple> result = query.getResultList();

		Type typeToken = new TypeToken<List<RepDateReturnDataPoint>>() {
		}.getType();
		List<RepDateReturnDataPoint> repDateReturnDataPointMapList;
		RepDateReturnDataPoint repDateReturnDataPoint;

		Map<String, Set<String>> receivedDataPoints = new HashMap<>();

		for (Tuple item : result) {
			if (item.get("ELEMENT_CODE") != null && mandateNonReceivedDmids.containsKey(item.get("ELEMENT_CODE")) || optionalNonReceivedDmids.containsKey(item.get("ELEMENT_CODE"))) {
				if (item.get("ELE_RETURN_REF") != null) {
					elementIdList.add(Long.parseLong(item.get("ELEMENT_AUDIT_ID").toString()));
					repDateReturnDataPointMapList = JsonUtility.getGsonObject().fromJson(item.get("ELE_RETURN_REF").toString(), typeToken);

					repDateReturnDataPoint = repDateReturnDataPointMapList.stream().filter(f -> f.getEndDate().equalsIgnoreCase(simpleDateFormat.format(sdmxEbrToRbrPreparation.getEndDate()))).findAny().orElse(null);

					ReturnDataPoint returnDataPoint = repDateReturnDataPoint.getReturnList().stream().filter(f -> f.getReturnCode().equalsIgnoreCase(sdmxEbrToRbrPreparation.getReturnObj().getReturnCode())).findAny().orElse(null);

					if (returnDataPoint != null) {
						if (receivedDataPoints.containsKey(item.get("ELEMENT_CODE").toString())) {
							Set<String> existingDataPoints = receivedDataPoints.get(item.get("ELEMENT_CODE").toString());
							existingDataPoints.addAll(returnDataPoint.getDataPoints());
							receivedDataPoints.put(item.get("ELEMENT_CODE").toString(), existingDataPoints);
						} else {
							receivedDataPoints.put(item.get("ELEMENT_CODE").toString(), returnDataPoint.getDataPoints());
						}
					}
				}

				sdmxEbrToRbrPreparation.setUserId(Long.parseLong(item.get("USER_ID_FK") + ""));
				try {
					sdmxEbrToRbrPreparation.setUserName(AESV2.getInstance().decrypt(item.get("USER_NAME").toString()));
				} catch (Exception e) {
					LOGGER.error("Exception ", e);
				}
				sdmxEbrToRbrPreparation.setRoleId(Long.parseLong(item.get("ROLE_ID_FK") + ""));
			}
		}

		boolean allDmidsReceived = true;

		if (!mandateNonReceivedDmids.isEmpty()) {
			for (Map.Entry<String, Set<String>> valueMap : mandateNonReceivedDmids.entrySet()) {
				Set<String> receivedDmids = receivedDataPoints.get(valueMap.getKey());
				if (receivedDmids != null) {
					for (String nonReceivedDmid : valueMap.getValue()) {
						if (receivedDmids.contains(nonReceivedDmid)) {
							if (mandateReceivedDmids.containsKey(valueMap.getKey())) {
								Set<String> dmids = mandateReceivedDmids.get(valueMap.getKey());
								dmids.add(nonReceivedDmid);
								mandateReceivedDmids.put(valueMap.getKey(), dmids);
							} else {
								Set<String> dmids = new HashSet<>();
								dmids.add(nonReceivedDmid);
								mandateReceivedDmids.put(valueMap.getKey(), dmids);
							}
						} else {
							allDmidsReceived = false;
						}
					}
				} else {
					allDmidsReceived = false;
				}
			}
		} else {
			allDmidsReceived = false;
		}

		for (Map.Entry<String, Set<String>> valueMap : optionalNonReceivedDmids.entrySet()) {
			Set<String> receivedDmids = receivedDataPoints.get(valueMap.getKey());
			if (receivedDmids != null) {
				for (String nonReceivedDmid : valueMap.getValue()) {
					if (receivedDmids.contains(nonReceivedDmid)) {
						if (optionalReceivedDmids.containsKey(valueMap.getKey())) {
							Set<String> dmids = optionalReceivedDmids.get(valueMap.getKey());
							dmids.add(nonReceivedDmid);
							optionalReceivedDmids.put(valueMap.getKey(), dmids);
						} else {
							Set<String> dmids = new HashSet<>();
							dmids.add(nonReceivedDmid);
							optionalReceivedDmids.put(valueMap.getKey(), dmids);
						}
					}
				}
			}
		}

		return allDmidsReceived;
	}

	private Map<String, Set<String>> getOptionalNonReceivedDmids(SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation) {
		Map<String, Set<String>> dataPointMap = new HashMap<>();

		if (sdmxEbrToRbrPreparation.getOptionalDatapointExpectedJson() != null) {
			ReturnDataPointBean optionalReturnDataPointBean = JsonUtility.getGsonObject().fromJson(sdmxEbrToRbrPreparation.getOptionalDatapointExpectedJson(), ReturnDataPointBean.class);
			List<ElementDataPointBean> optionalElementDataPointBeanList = optionalReturnDataPointBean.getDataPointList();

			if (sdmxEbrToRbrPreparation.getOptionalDatapointReceivedJson() != null) {
				ReturnDataPointBean optionalReceiveReturnDataPointBean = JsonUtility.getGsonObject().fromJson(sdmxEbrToRbrPreparation.getOptionalDatapointReceivedJson(), ReturnDataPointBean.class);
				List<ElementDataPointBean> receivedElementDataPointBeanList = optionalReceiveReturnDataPointBean.getDataPointList();
				for (ElementDataPointBean optionalEleDataPointBean : optionalElementDataPointBeanList) {

					ElementDataPointBean receivedEleDataPointBean = new ElementDataPointBean();
					receivedEleDataPointBean.setDsdCode(optionalEleDataPointBean.getDsdCode());

					if (receivedElementDataPointBeanList.indexOf(receivedEleDataPointBean) > -1) {
						receivedEleDataPointBean = receivedElementDataPointBeanList.get(receivedElementDataPointBeanList.indexOf(receivedEleDataPointBean));

						// DSD code found
						for (String dataPoint : optionalEleDataPointBean.getDataPoints()) {
							if (!receivedEleDataPointBean.getDataPoints().contains(dataPoint)) {
								if (!dataPointMap.containsKey(optionalEleDataPointBean.getDsdCode())) {
									Set<String> dataPoints = new HashSet<>();
									dataPoints.add(dataPoint);
									dataPointMap.put(optionalEleDataPointBean.getDsdCode(), dataPoints);
								} else {
									Set<String> dataPoints = dataPointMap.get(optionalEleDataPointBean.getDsdCode());
									dataPoints.add(dataPoint);
									dataPointMap.put(optionalEleDataPointBean.getDsdCode(), dataPoints);
								}
							}
						}
					} else {
						// DSD code not found
						dataPointMap.put(optionalEleDataPointBean.getDsdCode(), optionalEleDataPointBean.getDataPoints());
					}
				}
			} else {
				optionalElementDataPointBeanList.forEach(f -> {
					dataPointMap.put(f.getDsdCode(), f.getDataPoints());
				});
			}
		}
		return dataPointMap;
	}

	private Map<String, Set<String>> getMandateNonReceivedDmids(SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation) {
		ReturnDataPointBean mandateExpectedReturnDataPointBean = JsonUtility.getGsonObject().fromJson(sdmxEbrToRbrPreparation.getMandateDatapointExpectedJson(), ReturnDataPointBean.class);
		List<ElementDataPointBean> mandateElementDataPointBeanList = mandateExpectedReturnDataPointBean.getDataPointList();
		Map<String, Set<String>> dataPointMap = new HashMap<>();

		if (sdmxEbrToRbrPreparation.getMandateDatapointReceivedJson() != null) {
			ReturnDataPointBean mandateReceiveReturnDataPointBean = JsonUtility.getGsonObject().fromJson(sdmxEbrToRbrPreparation.getMandateDatapointReceivedJson(), ReturnDataPointBean.class);
			List<ElementDataPointBean> receivedElementDataPointBeanList = mandateReceiveReturnDataPointBean.getDataPointList();
			for (ElementDataPointBean manDateEleDataPointBean : mandateElementDataPointBeanList) {

				ElementDataPointBean receivedEleDataPointBean = new ElementDataPointBean();
				receivedEleDataPointBean.setDsdCode(manDateEleDataPointBean.getDsdCode());

				if (receivedElementDataPointBeanList.indexOf(receivedEleDataPointBean) > -1) {
					receivedEleDataPointBean = receivedElementDataPointBeanList.get(receivedElementDataPointBeanList.indexOf(receivedEleDataPointBean));
					// DSD code found
					for (String dataPoint : manDateEleDataPointBean.getDataPoints()) {
						if (!receivedEleDataPointBean.getDataPoints().contains(dataPoint)) {
							if (!dataPointMap.containsKey(manDateEleDataPointBean.getDsdCode())) {
								Set<String> dataPoints = new HashSet<>();
								dataPoints.add(dataPoint);
								dataPointMap.put(manDateEleDataPointBean.getDsdCode(), dataPoints);
							} else {
								Set<String> dataPoints = dataPointMap.get(manDateEleDataPointBean.getDsdCode());
								dataPoints.add(dataPoint);
								dataPointMap.put(manDateEleDataPointBean.getDsdCode(), dataPoints);
							}
						}
					}
				} else {
					// DSD code not found
					dataPointMap.put(manDateEleDataPointBean.getDsdCode(), manDateEleDataPointBean.getDataPoints());
				}
			}
		} else {
			mandateElementDataPointBeanList.forEach(f -> {
				dataPointMap.put(f.getDsdCode(), f.getDataPoints());
			});
		}
		return dataPointMap;
	}

	private Map<Integer, List<Integer>> getReturnPropertyMap() {
		List<ReturnProperty> returnProperties = returnPropertyRepo.getAllData();
		Map<Integer, List<Integer>> returnPropertyMap = new HashMap<>();

		returnProperties.forEach(f -> {
			returnPropertyMap.put(f.getReturnProprtyId(), f.getReturnPropertyValList().stream().map(ReturnPropertyValue::getReturnProprtyValId).collect(Collectors.toList()));
		});

		return returnPropertyMap;
	}

	private Map<Long, String> getReturnAndReportingDateMap(List<Return> returnsList, Date currentDate, SimpleDateFormat simpleDateFormat) throws ParseException {

		Map<Long, String> returnAndReportingDateMap = new HashMap<>();

		String returnDate = null;
		for (Return returnObj : returnsList) {
			com.iris.util.DateUtilsParser.Frequency fequencyEnum = com.iris.util.DateUtilsParser.Frequency.getEnumByfreqPeriod(returnObj.getFrequency().getFrequencyCode());
			if (fequencyEnum != null) {
				returnDate = DateAndTimeArithmetic.getDate(currentDate, false, fequencyEnum);
				if (!StringUtils.isEmpty(returnDate)) {
					returnAndReportingDateMap.put(returnObj.getReturnId(), simpleDateFormat.format(currentDate) + "~" + returnDate);
				}
			} else {
				LOGGER.error("Frequency Enum Null got for Frequency Code : " + returnObj.getFrequency().getFrequencyCode());
			}
		}
		return returnAndReportingDateMap;
	}

	private SdmxEbrToRbrPreparation getEbrRbrPreparationBean(SDMXReturnEntityMapp returnEntityMappingNew, Date startDate, Date endDate, Date reportPrepDate, String mandateDataPointExpectedJson, String optionalDataPointExpectedJson, Integer returnPropertyValId) {

		SdmxEbrToRbrPreparation sdmxEbrToRbrPreparation = new SdmxEbrToRbrPreparation();
		sdmxEbrToRbrPreparation.setCreatedOn(new Date());
		EntityBean entityBean = new EntityBean();
		entityBean.setEntityId(returnEntityMappingNew.getEntity().getEntityId());
		sdmxEbrToRbrPreparation.setEntity(entityBean);

		Return returnObj = new Return();
		returnObj.setReturnId(returnEntityMappingNew.getReturnObj().getReturnId());
		sdmxEbrToRbrPreparation.setReturnObj(returnObj);

		sdmxEbrToRbrPreparation.setEndDate(endDate);
		sdmxEbrToRbrPreparation.setStartDate(startDate);
		sdmxEbrToRbrPreparation.setMandateDatapointExpectedJson(mandateDataPointExpectedJson);
		sdmxEbrToRbrPreparation.setOptionalDatapointExpectedJson(optionalDataPointExpectedJson);
		sdmxEbrToRbrPreparation.setReportPreStartOn(reportPrepDate);

		sdmxEbrToRbrPreparation.setIsFilingDone("0");

		sdmxEbrToRbrPreparation.setCreatedOn(new Date());

		if (returnPropertyValId != null) {
			ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
			returnPropertyValue.setReturnProprtyValId(returnPropertyValId);
			sdmxEbrToRbrPreparation.setReturnPropertyVal(returnPropertyValue);
		}

		return sdmxEbrToRbrPreparation;
	}

	private void prepareReturnModelMappings(Map<Long, ReturnDataPointBean> mandatoryDataPointMap, Map<Long, ReturnDataPointBean> optionalDataPointMap, Map<Long, Long> returnAndReturnPreviewId) {

		List<Long> returnPreviewIds = returnAndReturnPreviewId.values().stream().collect(Collectors.toList());

		Map<Long, Map<String, Set<String>>> mandatoryReturnModelMap = getMandatoryReturnModelMap(returnPreviewIds);

		Map<Long, Map<String, Set<String>>> optionalReturnModelMap = getOptionalReturnModelMap(returnPreviewIds);

		Integer totalDataPoint;
		for (Long key : mandatoryReturnModelMap.keySet()) {
			totalDataPoint = 0;
			ReturnDataPointBean returnDataPoint = new ReturnDataPointBean();
			List<ElementDataPointBean> elementDataPoints = new ArrayList<>();

			Map<String, Set<String>> elementDataPointMap = mandatoryReturnModelMap.get(key);

			for (String dsdCode : elementDataPointMap.keySet()) {
				ElementDataPointBean elementDataPoint = new ElementDataPointBean();
				elementDataPoint.setDsdCode(dsdCode);

				if (elementDataPointMap.get(dsdCode) != null) {
					totalDataPoint += elementDataPointMap.get(dsdCode).size();
					elementDataPoint.setTotalDataPoints(elementDataPointMap.get(dsdCode).size());
					elementDataPoint.setDataPoints(elementDataPointMap.get(dsdCode));
				}

				elementDataPoints.add(elementDataPoint);
			}

			returnDataPoint.setDataPointList(elementDataPoints);
			returnDataPoint.setTotalDataPoints(totalDataPoint);
			mandatoryDataPointMap.put(key, returnDataPoint);
		}

		for (Long key : optionalReturnModelMap.keySet()) {
			totalDataPoint = 0;
			ReturnDataPointBean returnDataPoint = new ReturnDataPointBean();
			List<ElementDataPointBean> elementDataPoints = new ArrayList<>();

			Map<String, Set<String>> elementDataPointMap = optionalReturnModelMap.get(key);

			for (String dsdCode : elementDataPointMap.keySet()) {
				ElementDataPointBean elementDataPoint = new ElementDataPointBean();
				elementDataPoint.setDsdCode(dsdCode);

				if (elementDataPointMap.get(dsdCode) != null) {
					totalDataPoint += elementDataPointMap.get(dsdCode).size();
					elementDataPoint.setTotalDataPoints(elementDataPointMap.get(dsdCode).size());
					elementDataPoint.setDataPoints(elementDataPointMap.get(dsdCode));
				}

				elementDataPoints.add(elementDataPoint);
			}

			returnDataPoint.setDataPointList(elementDataPoints);
			returnDataPoint.setTotalDataPoints(totalDataPoint);
			optionalDataPointMap.put(key, returnDataPoint);
		}
	}

	private Map<Long, Map<String, Set<String>>> getOptionalReturnModelMap(List<Long> returnPreviewIds) {
		List<SdmxReturnModelMappingBean> optionalReturnModelMappings = getNonMandataorySdmxReturnModelMapping(returnPreviewIds);

		Map<Long, Map<String, Set<String>>> optionalReturnModelMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(optionalReturnModelMappings)) {
			for (SdmxReturnModelMappingBean optionalSdmxReturnModelMappingBean : optionalReturnModelMappings) {
				if (optionalReturnModelMap.containsKey(optionalSdmxReturnModelMappingBean.getReturnId())) {
					Map<String, Set<String>> elementDMIDsMap = optionalReturnModelMap.get(optionalSdmxReturnModelMappingBean.getReturnId());
					if (elementDMIDsMap.containsKey(optionalSdmxReturnModelMappingBean.getDsdCode())) {
						elementDMIDsMap.get(optionalSdmxReturnModelMappingBean.getDsdCode()).add(optionalSdmxReturnModelMappingBean.getModelCode());
					} else {
						Set<String> dmidsList = new HashSet<>();
						dmidsList.add(optionalSdmxReturnModelMappingBean.getModelCode());
						elementDMIDsMap.put(optionalSdmxReturnModelMappingBean.getDsdCode(), dmidsList);
					}
				} else {
					Map<String, Set<String>> elementDMIDsMap = new HashMap<>();
					Set<String> dmidsList = new HashSet<>();
					dmidsList.add(optionalSdmxReturnModelMappingBean.getModelCode());
					elementDMIDsMap.put(optionalSdmxReturnModelMappingBean.getDsdCode(), dmidsList);
					optionalReturnModelMap.put(optionalSdmxReturnModelMappingBean.getReturnId(), elementDMIDsMap);
				}
			}
		}

		return optionalReturnModelMap;
	}

	private Map<Long, Map<String, Set<String>>> getMandatoryReturnModelMap(List<Long> returnTemplateIds) {
		List<SdmxReturnModelMappingBean> mandatoryReturnModelMappings = getMandataorySdmxReturnModelMapping(returnTemplateIds);

		Map<Long, Map<String, Set<String>>> mandatoryReturnModelMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(mandatoryReturnModelMappings)) {
			for (SdmxReturnModelMappingBean mandatorySdmxReturnModelMappingBean : mandatoryReturnModelMappings) {
				if (mandatoryReturnModelMap.containsKey(mandatorySdmxReturnModelMappingBean.getReturnId())) {
					Map<String, Set<String>> elementDMIDsMap = mandatoryReturnModelMap.get(mandatorySdmxReturnModelMappingBean.getReturnId());
					if (elementDMIDsMap.containsKey(mandatorySdmxReturnModelMappingBean.getDsdCode())) {
						elementDMIDsMap.get(mandatorySdmxReturnModelMappingBean.getDsdCode()).add(mandatorySdmxReturnModelMappingBean.getModelCode());
					} else {
						Set<String> dmidsList = new HashSet<>();
						dmidsList.add(mandatorySdmxReturnModelMappingBean.getModelCode());
						elementDMIDsMap.put(mandatorySdmxReturnModelMappingBean.getDsdCode(), dmidsList);
					}
				} else {
					Map<String, Set<String>> elementDMIDsMap = new HashMap<>();
					Set<String> dmidsList = new HashSet<>();
					dmidsList.add(mandatorySdmxReturnModelMappingBean.getModelCode());
					elementDMIDsMap.put(mandatorySdmxReturnModelMappingBean.getDsdCode(), dmidsList);
					mandatoryReturnModelMap.put(mandatorySdmxReturnModelMappingBean.getReturnId(), elementDMIDsMap);
				}
			}
		}

		return mandatoryReturnModelMap;
	}

	private List<SdmxReturnModelMappingBean> getNonMandataorySdmxReturnModelMapping(List<Long> returnPreviewIds) {
		String queryString = "SELECT ret.RETURN_ID, modelInfo.RETURN_CELL_REF, modeCodes.MODEL_CODE, modelInfo.IS_MANDATORY, ele.DSD_CODE, ele.ELEMENT_VER from   " + " TBL_SDMX_RETURN_SHEET_INFO sheet, TBL_SDMX_RETURN_MODEL_INFO modelInfo,  " + " TBL_SDMX_MODEL_CODES modeCodes, TBL_RETURN_TEMPLATE temp, TBL_RETURN ret, " + "TBL_SDMX_ELEMENT ele, TBL_SDMX_RETURN_PREVIEW retPrev  " + " where sheet.RETURN_SHEET_INFO_ID = modelInfo.RETURN_SHEET_INFO_ID_FK  " + " and ele.ELEMENT_ID = modeCodes.ELEMENT_ID_FK  " + " and sheet.RETURN_TEMPLATE_ID_FK = temp.RETURN_TEMPLATE_ID  " + " " + "and retPrev.RETURN_PREVIEW_TYPE_ID = sheet.RETURN_PREVIEW_ID_FK " + " and ret.RETURN_ID = temp.RETURN_ID_FK " + "and modeCodes.MODEL_CODES_ID = modelInfo.MODEL_CODES_ID_FK  " + " and modelInfo.IS_ACTIVE = 1 and modeCodes.IS_ACTIVE = 1 " + "and ( JSON_EXTRACT(MODEL_DIM, '$.modelOtherDetails.dependencyType') = '" + SDMXConstants.DEPENDENT + "' " + " or IS_MANDATORY = 0 ) " + "and sheet.RETURN_PREVIEW_ID_FK IN (:returnPreviewId)";

		Query query = em.createNativeQuery(queryString);

		query.setParameter("returnPreviewId", returnPreviewIds);

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		List<SdmxReturnModelMappingBean> sdmxReturnModelMappingBeans = new ArrayList<>();

		for (Object[] result : results) {
			SdmxReturnModelMappingBean sdmxReturnModelMappingBean = new SdmxReturnModelMappingBean();
			if (!StringUtils.isEmpty(result[0])) {
				sdmxReturnModelMappingBean.setReturnId(Long.parseLong(result[0].toString()));
			}
			if (!StringUtils.isEmpty(result[1])) {
				sdmxReturnModelMappingBean.setReturnCellRef(Integer.parseInt(result[1].toString()));

			}
			if (!StringUtils.isEmpty(result[2])) {
				sdmxReturnModelMappingBean.setModelCode((String) result[2]);

			}
			if (!StringUtils.isEmpty(result[3])) {
				sdmxReturnModelMappingBean.setMandatory(Boolean.parseBoolean(result[3].toString()));
			}
			if (!StringUtils.isEmpty(result[4])) {
				sdmxReturnModelMappingBean.setDsdCode((String) result[4]);
			}
			if (!StringUtils.isEmpty(result[5])) {
				sdmxReturnModelMappingBean.setElementVersion((String) result[5]);
			}

			sdmxReturnModelMappingBeans.add(sdmxReturnModelMappingBean);
		}
		return sdmxReturnModelMappingBeans;
	}

	@SuppressWarnings("unchecked")
	private List<SdmxReturnModelMappingBean> getMandataorySdmxReturnModelMapping(List<Long> returnPreviewIds) {
		String queryString = "SELECT ret.RETURN_ID, modelInfo.RETURN_CELL_REF, modeCodes.MODEL_CODE, modelInfo.IS_MANDATORY, ele.DSD_CODE, ele.ELEMENT_VER from TBL_SDMX_RETURN_SHEET_INFO sheet," + " TBL_SDMX_RETURN_MODEL_INFO modelInfo, " + " TBL_SDMX_MODEL_CODES modeCodes, " + " TBL_RETURN_TEMPLATE temp, TBL_RETURN ret, TBL_SDMX_ELEMENT ele," + " TBL_SDMX_RETURN_PREVIEW retPrev" + " where sheet.RETURN_SHEET_INFO_ID = modelInfo.RETURN_SHEET_INFO_ID_FK " + " and ele.ELEMENT_ID = modeCodes.ELEMENT_ID_FK " + "" + " and sheet.RETURN_TEMPLATE_ID_FK = temp.RETURN_TEMPLATE_ID " + " and retPrev.RETURN_PREVIEW_TYPE_ID = sheet.RETURN_PREVIEW_ID_FK" + "" + " and ret.RETURN_ID = temp.RETURN_ID_FK and modeCodes.MODEL_CODES_ID = modelInfo.MODEL_CODES_ID_FK " + " and modelInfo.IS_ACTIVE = 1" + " and modeCodes.IS_ACTIVE = 1 and ( JSON_EXTRACT(MODEL_DIM, '$.modelOtherDetails.dependencyType') = '" + SDMXConstants.INDEPENDENT + "'" + " and IS_MANDATORY = 1 ) " + "and sheet.RETURN_PREVIEW_ID_FK IN (:returnPreviewId)";

		Query query = em.createNativeQuery(queryString);

		query.setParameter("returnPreviewId", returnPreviewIds);

		List<Object[]> results = query.getResultList();

		List<SdmxReturnModelMappingBean> sdmxReturnModelMappingBeans = new ArrayList<>();

		for (Object[] result : results) {
			SdmxReturnModelMappingBean sdmxReturnModelMappingBean = new SdmxReturnModelMappingBean();
			if (!StringUtils.isEmpty(result[0])) {
				sdmxReturnModelMappingBean.setReturnId(Long.parseLong(result[0].toString()));
			}
			if (!StringUtils.isEmpty(result[1])) {
				sdmxReturnModelMappingBean.setReturnCellRef(Integer.parseInt(result[1].toString()));

			}
			if (!StringUtils.isEmpty(result[2])) {
				sdmxReturnModelMappingBean.setModelCode((String) result[2]);

			}
			if (!StringUtils.isEmpty(result[3])) {
				sdmxReturnModelMappingBean.setMandatory(Boolean.parseBoolean(result[3].toString()));
			}
			if (!StringUtils.isEmpty(result[4])) {
				sdmxReturnModelMappingBean.setDsdCode((String) result[4]);
			}
			if (!StringUtils.isEmpty(result[5])) {
				sdmxReturnModelMappingBean.setElementVersion((String) result[5]);
			}

			sdmxReturnModelMappingBeans.add(sdmxReturnModelMappingBean);
		}

		return sdmxReturnModelMappingBeans;
	}

}
