package com.iris.rbrToEbr.service;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.Return;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.RoleType;
import com.iris.model.UserMaster;
import com.iris.rbrToEbr.bean.EbrDataConversionLogBean;
import com.iris.rbrToEbr.bean.EbrRbrFlowBean;
import com.iris.rbrToEbr.controller.EbrDataConversionController;
import com.iris.rbrToEbr.entity.EbrDataConversionLog;
import com.iris.rbrToEbr.entity.EbrRbrConvData;
import com.iris.rbrToEbr.entity.EbrRbrFlow;
import com.iris.rbrToEbr.entity.EbrRbrFlowMaster;
import com.iris.rbrToEbr.repo.EbrDataConversionLogRepo;
import com.iris.rbrToEbr.repo.EbrRbrConvRepo;
import com.iris.rbrToEbr.repo.EbrRbrFlowMasterRepo;
import com.iris.rbrToEbr.repo.EbrRbrFlowRepo;
import com.iris.repository.EntityRepo;
import com.iris.repository.ReturnPropertyValueRepository;
import com.iris.repository.ReturnRepo;
import com.iris.repository.UserEntityRoleRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

@Service
public class EbrDataConversionService implements GenericService<EbrDataConversionLog, Long> {

	private static final Logger LOGGER = LogManager.getLogger(EbrDataConversionService.class);

	@Autowired
	private EbrDataConversionLogRepo ebrDataConversionLogRepo;

	@Autowired
	private ReturnRepo returnRepo;

	@Autowired
	private EntityRepo entityRepo;

	@Autowired
	private EbrRbrFlowRepo ebrRbrFlowRepo;

	@Autowired
	private EbrRbrFlowMasterRepo ebrRbrFlowMasterRepo;

	@Autowired
	private ReturnPropertyValueRepository returnPropertyValueRepo;

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EbrRbrConvRepo ebrRbrConvRepo;

	@Autowired
	private UserEntityRoleRepo userEntityRoleRepo;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Override
	public EbrDataConversionLog add(EbrDataConversionLog entity) throws ServiceException {

		return null;
	}

	@Override
	public boolean update(EbrDataConversionLog entity) throws ServiceException {

		return false;
	}

	@Override
	public List<EbrDataConversionLog> getDataByIds(Long[] ids) throws ServiceException {

		return null;
	}

	@Override
	public EbrDataConversionLog getDataById(Long id) throws ServiceException {

		return null;
	}

	@Override
	public List<EbrDataConversionLog> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		return null;
	}

	@Override
	public List<EbrDataConversionLog> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {

		return null;
	}

	@Override
	public List<EbrDataConversionLog> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		return null;
	}

	@Override
	public List<EbrDataConversionLog> getActiveDataFor(Class bean, Long id) throws ServiceException {

		return null;
	}

	@Override
	public List<EbrDataConversionLog> getAllDataFor(Class bean, Long id) throws ServiceException {

		return null;
	}

	@Override
	public void deleteData(EbrDataConversionLog bean) throws ServiceException {

	}

	public boolean addEbrLog(EbrRbrFlowBean ebrRbrFlowInputBean, Long userId) throws ParseException, ApplicationException {
		int number;
		int flowId = 1;
		String numberString;
		EbrRbrFlow ebrRbrFlowEntity = new EbrRbrFlow();

		String entityCode = ebrRbrFlowInputBean.getEntityCode();
		EntityBean entityBean = entityRepo.findByEntityCode(entityCode);

		Date createdOn = new Date();

		String createdBy = userMasterRepo.findByUserId(userId).getUserName();
		DateFormat originalFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
		DateFormat targetFormat = new SimpleDateFormat("ddMMyyyy");
		Date date = originalFormat.parse(ebrRbrFlowInputBean.getReportingPeriod().toString());
		String formattedDate = targetFormat.format(date); // 20120821

		Return returnBean = returnRepo.getDataByReturnIdAndIsActiveTrue(ebrRbrFlowInputBean.getReturnIdFk());

		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strReportingDatee = formatter.format(ebrRbrFlowInputBean.getReportingPeriod());
		EbrRbrConvData dataBean = ebrRbrConvRepo.checkIfValidRequestNative(returnBean.getReturnCode(), entityCode, strReportingDatee, ebrRbrFlowInputBean.getAuditStatus());
		if (dataBean == null) {
			throw new ApplicationException(ErrorCode.E1526.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1526.toString()));
		}

		String flag = checkRecordsToInsert(entityCode, returnBean.getReturnCode(), ebrRbrFlowInputBean.getReportingPeriod(), ebrRbrFlowInputBean.getAuditStatus(), flowId);
		if (flag.equalsIgnoreCase("NO-ENTRY")) {
			throw new ApplicationException(ErrorCode.E1497.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1497.toString()));
		}

		if (flag.equalsIgnoreCase("ENTRY")) {
			number = 001;
			numberString = String.valueOf(number);
			numberString = "001";

		} else {
			String[] jobIdArray = flag.split("_");
			number = Integer.parseInt(jobIdArray[3]) + 1;
			numberString = String.valueOf(number);
			if (numberString.length() == 1) {
				numberString = "00" + numberString;
			} else if (numberString.length() == 2) {
				numberString = "0" + numberString;
			}
		}

		String jobId = entityBean.getIfscCode() + "_" + returnBean.getReturnCode() + "_" + formattedDate + "_" + numberString;

		List<EbrRbrFlowMaster> flowList = ebrRbrFlowMasterRepo.getDataByFlowIdd(flowId);

		List<EbrRbrFlow> EbrRbrFlowList = new ArrayList<>();
		if (!flowList.isEmpty()) {
			for (EbrRbrFlowMaster flowObj : flowList) {
				EbrRbrFlow entityObj = new EbrRbrFlow();
				entityObj.setFlowIdfk(flowObj.getFlowId());
				entityObj.setFlowName(flowObj.getFlowName());
				entityObj.setTaskName(flowObj.getTaskName());
				entityObj.setSequence(flowObj.getSequence());
				entityObj.setPriority(flowObj.getPriority());
				entityObj.setCreatedDate(createdOn);
				entityObj.setCreatedBy(createdBy);
				entityObj.setEntityCode(entityCode);
				entityObj.setReturnCode(returnBean.getReturnCode());
				entityObj.setFrequency(ObjectCache.getLabelKeyValue("en", returnBean.getFrequency().getFrequencyName()));
				entityObj.setReportingPeriod(ebrRbrFlowInputBean.getReportingPeriod());
				entityObj.setJobId(jobId);
				entityObj.setAuditStatus(ebrRbrFlowInputBean.getAuditStatus());
				entityObj.setStatus(0);
				entityObj.setUploadId(null);

				EbrRbrFlowList.add(entityObj);
			}

		}

		if (!EbrRbrFlowList.isEmpty()) {
			for (EbrRbrFlow inputObj : EbrRbrFlowList) {

				ebrRbrFlowRepo.save(inputObj);
			}

		}

		return true;
	}

	public String checkRecordsToInsert(String entityCode, String returnCode, Date reportingDate, int auditStatus, int flowId) {

		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strReportingDatee = formatter.format(reportingDate);

		List<EbrRbrFlow> recordList = ebrRbrFlowRepo.checkRecordExist1(entityCode, returnCode, reportingDate, auditStatus, flowId);
		if (recordList.isEmpty()) {

			EbrRbrFlow obj = ebrRbrFlowRepo.findTop1ByEntityCodeAndReturnCodeAndReportingPeriodAndAuditStatusOrderByCreatedDateDesc(entityCode, returnCode, reportingDate, auditStatus);

			if (obj == null) {

				return "ENTRY";
			} else {

				return obj.getJobId();
			}
		} else {

			return "NO-ENTRY";
		}

	}

	private Map<Integer, String> prepareEbrProcessMap() {
		Map<Integer, String> statusList = new HashMap<Integer, String>();

		statusList.put(0, "Initiated");
		statusList.put(1, "Running");
		statusList.put(2, "Success");
		statusList.put(3, "failed");

		return statusList;
	}

	public List<EbrRbrFlow> getAllEbrRbrLogs(Long userId, String entityCode) {
		List<EbrRbrFlow> ebrLogList = new ArrayList<>();
		UserMaster userMaster = userMasterService.getDataById(userId);
		RoleType roleType = userMaster.getRoleType();

		if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
			ebrLogList = ebrRbrFlowRepo.getAllLogsForEntity(entityCode);
		} else {
			ebrLogList = ebrRbrFlowRepo.getAllLogs();
		}

		return ebrLogList;

	}

	public List<EbrRbrFlowBean> prepareEbrRbrLogsList(List<EbrRbrFlow> logList) {
		Map<Integer, String> statusMap = prepareEbrProcessMap();

		List<String> jobIdList = new ArrayList<>();

		List<EbrRbrFlowBean> ebrRbrlogBeanList = new ArrayList<>();

		Map<String, List<EbrRbrFlowBean>> jobIdMap = new LinkedHashMap<String, List<EbrRbrFlowBean>>();
		List<EbrRbrFlowBean> ebrFlowList;
		Map<String, EbrRbrFlowMaster> flowMasterMap = new HashMap<>();
		flowMasterMap = getFlowMasterMap();
		if (!logList.isEmpty()) {
			for (EbrRbrFlow Obj : logList) {
				EbrRbrFlowBean beanObj = new EbrRbrFlowBean();
				try {
					beanObj.setReturnCode(Obj.getReturnCode());
					beanObj.setReturnName(returnRepo.findByReturnCode(Obj.getReturnCode()).getReturnName());
				} catch (Exception e) {
					beanObj.setReturnCode("");
					beanObj.setReturnName("");

				}

				try {
					beanObj.setEntityCode(Obj.getEntityCode());
					beanObj.setEntityName(entityRepo.findByEntityCode(Obj.getEntityCode()).getEntityName());
				} catch (Exception e) {
					beanObj.setEntityCode("");
					beanObj.setEntityName("");
					LOGGER.error("Exception ", e);
				}

				beanObj.setReportingPeriod(Obj.getReportingPeriod());
				beanObj.setReportingPeriodLong(Obj.getReportingPeriod().getTime());
				beanObj.setJobId(Obj.getJobId());
				beanObj.setAuditStatus(Obj.getAuditStatus());
				beanObj.setAuditStatuslabel(returnPropertyValueRepo.findByReturnProprtyValId(Obj.getAuditStatus()).getReturnProValue());
				beanObj.setCreatedBy(Obj.getCreatedBy());
				beanObj.setCreatedByName(Obj.getCreatedBy());
				beanObj.setCreatedDate(Obj.getCreatedDate());
				beanObj.setCreatedDateLong(Obj.getCreatedDate().getTime());
				beanObj.setFrequency(Obj.getFrequency());
				beanObj.setUploadId(Obj.getUploadId());
				beanObj.setFlowIdfk(Obj.getFlowIdfk());
				beanObj.setFlowName(Obj.getFlowName());
				beanObj.setTaskName(Obj.getTaskName());
				if (flowMasterMap.containsKey(Obj.getTaskName())) {
					beanObj.setTaskDesc(flowMasterMap.get(Obj.getTaskName()).getTaskDesc());
				}
				beanObj.setSequence(Obj.getSequence());
				beanObj.setPriority(Obj.getPriority());
				beanObj.setStatus(Obj.getStatus());
				beanObj.setStatusLabel(statusMap.get(Obj.getStatus()));

				if (!jobIdMap.containsKey(Obj.getJobId())) {
					ebrFlowList = new ArrayList<>();
					ebrFlowList.add(beanObj);

					jobIdMap.put(Obj.getJobId(), ebrFlowList);

				} else {
					List<EbrRbrFlowBean> ebrFlowListNew = jobIdMap.get(Obj.getJobId());
					ebrFlowListNew.add(beanObj);

					jobIdMap.replace(Obj.getJobId(), ebrFlowListNew);

				}
			}
		}

		jobIdMap.forEach((jobId, list) -> {
			try {
				List<EbrRbrFlowBean> ebrFlowBeanList = list;
				Collections.sort(ebrFlowBeanList);
				boolean initiateFlag = true;
				for (EbrRbrFlowBean bean : ebrFlowBeanList) {
					if (bean.getStatus() != 0) {
						initiateFlag = false;
						break;
					}
				}

				if (initiateFlag) {//for all rows with status 0
					ebrRbrlogBeanList.add(ebrFlowBeanList.get(0));
				} else {
					Collections.sort(ebrFlowBeanList, new Comparator<EbrRbrFlowBean>() {
						@Override
						public int compare(EbrRbrFlowBean o1, EbrRbrFlowBean o2) {
							return new Integer(o2.getSequence()).compareTo(new Integer(o1.getSequence()));
						}
					});

					for (EbrRbrFlowBean ebrRbrFlowBean : ebrFlowBeanList) {
						if (ebrRbrFlowBean.getStatus() != 0) {
							ebrRbrlogBeanList.add(ebrRbrFlowBean);
							break;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});

		return ebrRbrlogBeanList;
	}

	public Map<String, EbrRbrFlowMaster> getFlowMasterMap() {

		Map<String, EbrRbrFlowMaster> flowMasterMap = new HashMap<>();
		List<EbrRbrFlowMaster> flowMasterList = ebrRbrFlowMasterRepo.getAllData();
		if (flowMasterList != null) {
			if (!flowMasterList.isEmpty()) {
				for (EbrRbrFlowMaster ebrRbrFlowMaster : flowMasterList) {
					String key = ebrRbrFlowMaster.getTaskName();
					if (!flowMasterMap.containsKey(key)) {
						flowMasterMap.put(key, ebrRbrFlowMaster);
					}
				}
			}
		}
		return flowMasterMap;
	}

}
