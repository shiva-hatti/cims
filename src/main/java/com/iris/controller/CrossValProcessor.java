package com.iris.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iris.exception.ServiceException;
import com.iris.model.CrossValidationStatus;
import com.iris.model.Return;
import com.iris.model.ReturnDet;
import com.iris.model.ReturnGroupMapping;
import com.iris.repository.ReturnGroupMappingRepo;
import com.iris.repository.ReturnRepo;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;

/**
 * @author psheke
 * @date 11/11/2020
 */
@Service
public class CrossValProcessor implements Serializable {

	private static final long serialVersionUID = -1604207852063745597L;
	@Autowired
	public ReturnGroupMappingRepo returnGroupMapRepo;

	@Autowired
	public ReturnRepo returnRepo;

	@Autowired
	EntityManager em;
	static final Logger LOGGER = LogManager.getLogger(CrossValProcessor.class);

	public List<ReturnGroupMapping> getSetList() {
		LOGGER.info("Fetching Return Group applicable for Cross Validation: CrossValProcessor.getSetList");
		List<ReturnGroupMapping> setList;
		try {

			setList = returnGroupMapRepo.getSetList();

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);

		}

		return setList;
	}

	public List<ReturnDet> fetchReturnListbyGroupMapId(String returnGroupMapId) {
		LOGGER.info("Fetching Return List By Group Map Id: CrossValProcessor.fetchReturnListbyGroupMapId");
		List<ReturnDet> returnDetList = new ArrayList<>();
		ReturnDet retDet;
		Long returnGrpMapId = Long.valueOf(returnGroupMapId);
		try {
			List<Return> returnList = returnRepo.fetchReturnNameListbyGroupMapId(returnGrpMapId);

			for (Return ret : returnList) {
				retDet = new ReturnDet();
				retDet.setReturnCode(ret.getReturnCode());
				retDet.setReturnId(ret.getReturnId());
				retDet.setReturnName(ret.getReturnName());
				returnDetList.add(retDet);
			}
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);

		}
		return returnDetList;
	}

	public List<CrossValidationStatus> getCrossValidationList(String returnGroupMapId, String reportDate, String auditStatus, List<Long> selectedEntity, List<ReturnDet> returnList) {
		LOGGER.info("Fetching  Cross Validation Status List: CrossValProcessor.getCrossValidationList");
		List<CrossValidationStatus> crossValList = new ArrayList<>();
		List<Integer> auditStatusList = new ArrayList<>();
		CrossValidationStatus crossValObj;
		String[] crossValUploadIds = null;
		List<String> crossValUploadIdList = new ArrayList<>();
		List<String> uploadIdList = new ArrayList<>();
		try {
			if (auditStatus.equalsIgnoreCase(GeneralConstants.AUDITED.getConstantVal())) {
				auditStatusList.add(Integer.valueOf(1));

			} else if (auditStatus.equalsIgnoreCase(GeneralConstants.UNAUDITED.getConstantVal())) {
				auditStatusList.add(Integer.valueOf(2));

			} else if (auditStatus.equalsIgnoreCase(GeneralConstants.ALL.getConstantVal())) {
				auditStatusList.add(Integer.valueOf(1));
				auditStatusList.add(Integer.valueOf(2));

			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			try {
				date = sdf.parse(reportDate);
			} catch (ParseException e) {
				LOGGER.error(ErrorCode.EC0033.toString());
				throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			}

			StringBuilder sb = new StringBuilder();

			sb.append("	select originaData.*,TSC.SUB_CATEGORY_NAME, TE.ENTITY_NAME,TE.ENTITY_CODE,crossValidationStatus.crossStatus,crossValidationStatus.crossValUploadIds as crossValUploadIds ").append(" from  (select group_concat(retUpd.FILING_STATUS_ID_FK) as FilingStatus, group_concat(retUpd.RETURN_ID_FK) as returnIds,retUpd.ENTITY_ID_FK,retUpd.RETURN_PROP_VAL_ID_FK,TR.RETURN_GROUP_MAP_ID_FK, ").append(" group_concat(retUpd.UPLOAD_ID order by retUpd.UPLOAD_ID )  as uploadIds from ( select group_concat(STATUS) as crossStatus,concat( group_concat( DISTINCT REF_RETURN_UPLOAD_ID ORDER BY REF_RETURN_UPLOAD_ID) ,',' , group_concat(  DISTINCT CMP_RETURN_UPLOAD_ID  ORDER BY CMP_RETURN_UPLOAD_ID )) as uploadIds, ").append(" ts.GROUP_NAME as GROUP_NAME,ts.BANK_WORKING_CODE as BANK_WORKING_CODE,ts.RETURN_AUDIT_FLAG AS RETURN_AUDIT_FLAG  from TBL_CROSS_VALIDATION_STATUS ts where ts.REPORTING_PERIOD=:endDate ").append(" and ts.RETURN_AUDIT_FLAG IN(:auditStatusList) and ts.GROUP_NAME='SET1' GROUP BY ts.GROUP_NAME,ts.BANK_WORKING_CODE,ts.RETURN_AUDIT_FLAG ) as crossValidationStatus,TBL_RETURNS_UPLOAD_DETAILS retUpd ").append(" LEFT JOIN TBL_RETURN TR ON retUpd.RETURN_ID_FK =TR.RETURN_ID  where UPLOAD_ID in (select filingStatusWithEight.UPLOAD_ID FROM (select max(a.UPLOAD_ID) as UPLOAD_ID,a.RETURN_ID_FK as RETURN_ID_FK,a.ENTITY_ID_FK as ENTITY_ID_FK,a.RETURN_PROP_VAL_ID_FK  as RETURN_PROP_VAL_ID_FK FROM TBL_RETURNS_UPLOAD_DETAILS a ").append(" LEFT JOIN TBL_RETURN b ON a.RETURN_ID_FK =b.RETURN_ID WHERE a.FILING_STATUS_ID_FK=8 and b.RETURN_GROUP_MAP_ID_FK=:returnGroupMapId and a.RETURN_PROP_VAL_ID_FK IN(:auditStatusList) and a.END_DATE=:endDate AND a.ENTITY_ID_FK IN(:selectedEntity) ").append(" group by a.RETURN_ID_FK ,a.ENTITY_ID_FK,a.RETURN_PROP_VAL_ID_FK order by a.RETURN_ID_FK) as filingStatusWithEight union ").append(" select filingStatusWithoutEight.UPLOAD_ID FROM (select max(a.UPLOAD_ID) as UPLOAD_ID,a.RETURN_ID_FK as RETURN_ID_FK,a.ENTITY_ID_FK as ENTITY_ID_FK,a.RETURN_PROP_VAL_ID_FK  as RETURN_PROP_VAL_ID_FK FROM TBL_RETURNS_UPLOAD_DETAILS a ").append(" LEFT JOIN TBL_RETURN b ON a.RETURN_ID_FK =b.RETURN_ID WHERE a.FILING_STATUS_ID_FK=8 and b.RETURN_GROUP_MAP_ID_FK=:returnGroupMapId and a.RETURN_PROP_VAL_ID_FK IN(:auditStatusList) and a.END_DATE=:endDate AND a.ENTITY_ID_FK IN(:selectedEntity) ").append(" group by a.RETURN_ID_FK ,a.ENTITY_ID_FK,a.RETURN_PROP_VAL_ID_FK order by a.RETURN_ID_FK)  as filingStatusWithEight , (select max(a.UPLOAD_ID) as UPLOAD_ID,a.RETURN_ID_FK as RETURN_ID_FK  FROM TBL_RETURNS_UPLOAD_DETAILS a ").append(" LEFT JOIN TBL_RETURN b ON a.RETURN_ID_FK =b.RETURN_ID WHERE a.FILING_STATUS_ID_FK in(1,3,5,6,7,10,14,15) and a.UPLOAD_ID not in(select innerTable.UPLOAD_ID from TBL_RETURNS_UPLOAD_DETAILS innerTable ").append(" inner join (select max(a.UPLOAD_ID) as UPLOAD_ID,a.RETURN_ID_FK as RETURN_ID_FK,a.ENTITY_ID_FK as ENTITY_ID_FK,a.RETURN_PROP_VAL_ID_FK  as RETURN_PROP_VAL_ID_FK FROM TBL_RETURNS_UPLOAD_DETAILS a ").append(" LEFT JOIN TBL_RETURN b ON a.RETURN_ID_FK =b.RETURN_ID WHERE a.FILING_STATUS_ID_FK=8 and b.RETURN_GROUP_MAP_ID_FK=:returnGroupMapId and a.RETURN_PROP_VAL_ID_FK IN(:auditStatusList) and a.END_DATE=:endDate AND a.ENTITY_ID_FK IN(:selectedEntity) ").append(" group by a.RETURN_ID_FK ,a.ENTITY_ID_FK,a.RETURN_PROP_VAL_ID_FK order by a.RETURN_ID_FK) as filingStatusWithEight on filingStatusWithEight.RETURN_ID_FK =innerTable.RETURN_ID_FK and  filingStatusWithEight.ENTITY_ID_FK = innerTable.ENTITY_ID_FK ").append(" and filingStatusWithEight.RETURN_PROP_VAL_ID_FK =innerTable.RETURN_PROP_VAL_ID_FK ) and b.RETURN_GROUP_MAP_ID_FK=:returnGroupMapId and a.RETURN_PROP_VAL_ID_FK IN(:auditStatusList) and a.END_DATE=:endDate AND a.ENTITY_ID_FK IN(:selectedEntity) group by a.RETURN_ID_FK ,a.ENTITY_ID_FK,a.RETURN_PROP_VAL_ID_FK ").append(" order by a.RETURN_ID_FK) as filingStatusWithoutEight) group by TR.RETURN_GROUP_MAP_ID_FK,retUpd.ENTITY_ID_FK,retUpd.RETURN_PROP_VAL_ID_FK,TR.RETURN_GROUP_MAP_ID_FK) originaData left join TBL_ENTITY TE ON originaData.ENTITY_ID_FK=TE.ENTITY_ID ").append(" LEFT JOIN TBL_SUB_CATEGORY  TSC ON TE.SUB_CATEGORY_ID_FK=TSC.SUB_CATEGORY_ID left join (select group_concat(STATUS) as crossStatus,group_concat( DISTINCT upload_id  ORDER BY upload_id ) as crossValUploadIds, ").append(" ts.GROUP_NAME as GROUP_NAME,ts.BANK_WORKING_CODE as BANK_WORKING_CODE,ts.RETURN_AUDIT_FLAG AS RETURN_AUDIT_FLAG  from (Select Max(innercross1.REF_RETURN_UPLOAD_ID)  as upload_id from TBL_CROSS_VALIDATION_STATUS innercross1 where innercross1.REPORTING_PERIOD=:endDate ").append(" GROUP BY innercross1.BANK_WORKING_CODE,innercross1.RETURN_AUDIT_FLAG,innercross1.REF_RETURN_CODE union Select Max(innercross2.CMP_RETURN_UPLOAD_ID) as upload_id from TBL_CROSS_VALIDATION_STATUS innercross2 where innercross2.REPORTING_PERIOD=:endDate ").append(" GROUP BY innercross2.BANK_WORKING_CODE,innercross2.RETURN_AUDIT_FLAG,innercross2.CMP_RETURN_CODE)uploadIdList,TBL_CROSS_VALIDATION_STATUS ts where ts.REPORTING_PERIOD=:endDate ").append(" and ts.RETURN_AUDIT_FLAG IN(:auditStatusList) and ts.GROUP_NAME='SET1' GROUP BY ts.GROUP_NAME,ts.BANK_WORKING_CODE,ts.RETURN_AUDIT_FLAG) as crossValidationStatus  on crossValidationStatus.GROUP_NAME='SET1' ").append(" AND crossValidationStatus.RETURN_AUDIT_FLAG = originaData.RETURN_PROP_VAL_ID_FK AND crossValidationStatus.BANK_WORKING_CODE = TE.ENTITY_CODE ");

			List<Tuple> result = em.createNativeQuery(sb.toString(), Tuple.class).setParameter("returnGroupMapId", returnGroupMapId).setParameter("auditStatusList", auditStatusList).setParameter("endDate", date).setParameter("selectedEntity", selectedEntity).getResultList();

			Map<String, String> retDetMap;

			for (Tuple tuple : result) {
				crossValObj = new CrossValidationStatus();
				retDetMap = new LinkedHashMap<>();
				String[] filingStatusIds = tuple.get(0).toString().split(",");
				String[] returnIds = tuple.get(1).toString().split(",");
				if (tuple.get(3) == Integer.valueOf(1)) {
					crossValObj.setAuditStatus(GeneralConstants.AUDITED.getConstantVal());
				} else if (tuple.get(3) == Integer.valueOf(2)) {
					crossValObj.setAuditStatus(GeneralConstants.UNAUDITED.getConstantVal());
				}

				for (int i = 0; i < returnIds.length; i++) {

					if (filingStatusIds[i].equalsIgnoreCase("8")) {
						retDetMap.put(returnIds[i], GeneralConstants.COMPLETED.getConstantVal());
					} else {
						retDetMap.put(returnIds[i], GeneralConstants.PENDING.getConstantVal());
					}
				}

				for (ReturnDet retDet : returnList) {
					if (!retDetMap.containsKey(retDet.getReturnId().toString())) {
						retDetMap.put(retDet.getReturnId().toString(), GeneralConstants.STATUS_NA.getConstantVal());
					}
				}
				String[] uploadIds = tuple.get(5).toString().split(",");
				uploadIdList = Arrays.asList(uploadIds);
				crossValObj.setUploadIds(tuple.get(5).toString());
				crossValObj.setBankGroupName(tuple.get(6).toString());
				crossValObj.setBankName(tuple.get(7).toString());
				crossValObj.setBankCode(tuple.get(8).toString());
				crossValObj.setReturnDetMap(retDetMap);

				List<String> crossValStatusList = new ArrayList<>();
				if (tuple.get(9) != null) {
					String[] crossVals = tuple.get(9).toString().split(",");
					crossValStatusList = Arrays.asList(crossVals);

				}
				if (tuple.get(10) != null) {
					crossValUploadIds = tuple.get(10).toString().split(",");
					crossValUploadIdList = Arrays.asList(crossValUploadIds);
				}
				boolean pending = false;
				boolean showNA = false;

				for (String ret : retDetMap.values()) {
					if (ret.equalsIgnoreCase(GeneralConstants.COMPLETED.getConstantVal())) {
						pending = true;
					} else {
						showNA = true;
						break;
					}
				}

				if (crossValUploadIdList != null && uploadIdList != null) {
					if (crossValUploadIdList.size() < uploadIdList.size()) {
						for (String id : crossValUploadIdList) {
							if (!uploadIdList.contains(id)) {
								pending = true;
								break;
							} else {
								pending = false;
							}
						}
					} else if (crossValUploadIdList.size() == uploadIdList.size()) {
						for (String id : uploadIdList) {
							if (!crossValUploadIdList.contains(id)) {
								pending = true;
								break;
							} else {
								pending = false;
							}
						}

					}

				}

				if (pending) {
					crossValObj.setCrossValidationStatus(GeneralConstants.PENDING.getConstantVal());
				}
				if (showNA) {
					crossValObj.setCrossValidationStatus(GeneralConstants.STATUS_NA.getConstantVal());
				}
				if (crossValStatusList != null && !crossValStatusList.isEmpty() && !pending && !showNA) {
					if (crossValStatusList.contains(GeneralConstants.FAILED.getConstantVal())) {
						crossValObj.setCrossValidationStatus(GeneralConstants.FAILED.getConstantVal());

					} else {
						crossValObj.setCrossValidationStatus(GeneralConstants.SUCCESS.getConstantVal());
					}
				}

				if (crossValObj.getCrossValidationStatus().equalsIgnoreCase(GeneralConstants.SUCCESS.getConstantVal())) {
					crossValObj.setReportingToRBI(GeneralConstants.COMPLETED.getConstantVal());
				} else if (crossValObj.getCrossValidationStatus().equalsIgnoreCase(GeneralConstants.STATUS_NA.getConstantVal())) {
					crossValObj.setReportingToRBI(GeneralConstants.STATUS_NA.getConstantVal());

				} else {
					crossValObj.setReportingToRBI(GeneralConstants.PENDING.getConstantVal());
				}
				crossValList.add(crossValObj);
			}

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);

		}
		return crossValList;

	}
}