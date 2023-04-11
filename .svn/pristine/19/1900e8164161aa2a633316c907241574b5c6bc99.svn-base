package com.iris.crossvalidation.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.controller.PrepareSendMailController;
import com.iris.crossvalidation.service.dto.CrossValidationDto;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.AttachmentInfoBean;
import com.iris.dto.MailServiceBean;
import com.iris.dto.ServiceResponse;
import com.iris.model.UserEntityRole;
import com.iris.model.UserMaster;
import com.iris.util.FileManager;
import com.iris.util.ResourceUtil;
import com.iris.util.constant.MailConstant;

import net.sf.jasperreports.engine.JRException;

@Service
public class CrossValidationStatusService {
	private static final Logger LOGGER = LogManager.getLogger(CrossValidationStatusService.class);
	@Autowired
	private EntityManager em;
	@Autowired
	private PrepareSendMailController prepareSendMailController;

	public String generateReport(CrossValidationDto input, String source, String userId) throws JRException, IOException {
		JasperReportExporter exporter = new JasperReportExporter();
		String path = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.upload.temp");

		String directory = path + File.separator + userId + File.separator;
		String currentDateTime = DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants()) + "_" + DateManip.getCurrentTime(DateConstants.HH_MM_SS_1.getDateConstants());

		String absolutePath = directory + "output_" + input.getUploadIds().replace(",", "") + currentDateTime + ".html";
		FileManager.makeDirWithParentDir(new File(directory));
		exporter.prepareAndExportReport(null, absolutePath, getDataForScreen(input, source));
		return absolutePath;

	}

	private List<CrossValidationDto> getDataForScreen(CrossValidationDto input, String source) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select TRREF.RETURN_NAME as TRREF_RETURN_NAME, TRCMP.RETURN_NAME AS TRCMP_RETURN_NAME,TCVS.REF_RETURN_UPLOAD_ID as REF_RETURN_UPLOAD_ID,TCVS.REF_RETURN_CODE as REF_RETURN_CODE,TCVS.REF_RETURN_TMP_SHEET_NAME AS REF_RETURN_TMP_SHEET_NAME,TCVS.REF_RETURN_ITEM_NAME AS REF_RETURN_ITEM_NAME,TCVS.REF_VALUE AS REF_VALUE, TCVS.VALIDATION_RULE AS VALIDATION_RULE ").append(" ,TCVS.CMP_RETURN_UPLOAD_ID AS CMP_RETURN_UPLOAD_ID ,  TCVS.CMP_RETURN_CODE AS CMP_RETURN_CODE ,TCVS.CMP_RETURN_TMP_SHEET_NAME AS CMP_RETURN_TMP_SHEET_NAME ,TCVS.CMP_RETURN_ITEM_NAME AS CMP_RETURN_ITEM_NAME,TCVS.CMP_VALUE  AS CMP_VALUE ").append(" from TBL_CROSS_VALIDATION_STATUS TCVS left join TBL_RETURN TRREF on TRREF.RETURN_CODE = TCVS.REF_RETURN_CODE      ").append(" left join TBL_RETURN TRCMP on TRCMP.RETURN_CODE = TCVS.CMP_RETURN_CODE      ").append(" where GROUP_NAME =:groupName ");
		if (StringUtils.isNotBlank(input.getUploadIds())) {
			sb.append(" and REF_RETURN_UPLOAD_ID in (:uploadIds)").append(" and CMP_RETURN_UPLOAD_ID in (:uploadIds)");
		}
		if (StringUtils.equals(source, "scheduler")) {
			sb.append(" and TCVS.IS_MAIL_SENT  is null or TCVS.IS_MAIL_SENT = '0' ");
		}

		Query query = em.createNativeQuery(sb.toString(), Tuple.class).setParameter("groupName", input.getSetName());
		if (StringUtils.isNotBlank(input.getUploadIds())) {
			List<String> uploadIDs = Arrays.asList(input.getUploadIds().split(","));
			query.setParameter("uploadIds", uploadIDs);
		}

		@SuppressWarnings("unchecked")
		List<Tuple> list = query.getResultList();
		Integer previousRefReturn = 0;
		Integer previousCmpReturn = 0;
		int i = 1;
		List<CrossValidationDto> result = new ArrayList<>();

		CrossValidationDto inner = new CrossValidationDto();
		boolean firstTime = true;
		for (Tuple item : list) {
			CrossValidationDto dto = new CrossValidationDto();
			if (previousRefReturn.compareTo((Integer) item.get("REF_RETURN_UPLOAD_ID")) != 0 || previousCmpReturn.compareTo((Integer) item.get("CMP_RETURN_UPLOAD_ID")) != 0) {
				previousRefReturn = (Integer) item.get("REF_RETURN_UPLOAD_ID");
				previousCmpReturn = (Integer) item.get("CMP_RETURN_UPLOAD_ID");
				inner = new CrossValidationDto();
				result.add(inner);
				if (firstTime) {
					firstTime = false;
				} else {
					dto.setPageBreak("pageBreak");
					i = 1;
				}
			}
			String refItem = (String) item.get("REF_RETURN_ITEM_NAME");
			dto.setCmpReturnName((String) item.get("TRCMP_RETURN_NAME"));
			dto.setRefReturnName((String) item.get("TRREF_RETURN_NAME"));
			dto.setRefElementName(refItem);
			dto.setRefReturnCode((String) item.get("REF_RETURN_CODE"));
			dto.setCmpReturnCode((String) item.get("CMP_RETURN_CODE"));
			dto.setRefTemplateName((String) item.get("REF_RETURN_TMP_SHEET_NAME"));
			dto.setRefValue(((BigDecimal) item.get("REF_VALUE")).toPlainString());
			dto.setCompElementName((String) item.get("CMP_RETURN_ITEM_NAME"));
			dto.setCompTemplateName((String) item.get("CMP_RETURN_TMP_SHEET_NAME"));
			dto.setCompValue(((BigDecimal) item.get("CMP_VALUE")).toPlainString());
			dto.setValidationRule((String) item.get("VALIDATION_RULE"));

			dto.setSrNo(String.valueOf(i++));
			inner.getItem().add(dto);
		}

		return result;

	}

	@Transactional(rollbackFor = Exception.class)
	public List<String> generateReportForScheduler(CrossValidationDto input, String jobProcessingId, String schedulerCode) throws Exception {
		JasperReportExporter exporter = new JasperReportExporter();
		List<String> failedGroup = getFailedSets();
		LOGGER.info("Failed Return set count {}", failedGroup.size());
		Integer i = 0;
		for (String item : failedGroup) {
			input.setSetName(item);
			String path = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.upload.temp");

			String directory = path + File.separator + schedulerCode + File.separator;
			String currentDateTime = DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants()) + "_" + DateManip.getCurrentTime(DateConstants.HH_MM_SS_1.getDateConstants());

			String absolutePath = directory + "output_" + currentDateTime + ".html";
			if (FileManager.checkDirExistence(new File(directory))) {
				FileManager.cleanDirectory(new File(directory));
				LOGGER.info("Dirctory cleaned");
			}
			FileManager.makeDirWithParentDir(new File(directory));
			List<CrossValidationDto> list = getDataForScreen(input, "scheduler");
			if (list == null || list.isEmpty()) {
				LOGGER.info("No data to send mail");
				return new ArrayList<>();
			}
			exporter.prepareAndExportReport(null, absolutePath, list);
			LOGGER.info("Report created location {}", absolutePath);
			sendMail(i, jobProcessingId, absolutePath);
			updateDate();
		}
		return failedGroup;

	}

	private void updateDate() {
		StringBuilder sb = new StringBuilder();
		sb.append(" update TBL_CROSS_VALIDATION_STATUS ").append("set IS_MAIL_SENT ='1', ").append("MAIL_SENT_DATE=:cureentDate ").append("where GROUP_NAME = 'SET1' ");
		em.createNativeQuery(sb.toString()).setParameter("cureentDate", new Date()).executeUpdate();
	}

	private List<String> getFailedSets() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct GROUP_NAME from TBL_CROSS_VALIDATION_STATUS where STATUS='Failed' ");
		@SuppressWarnings("unchecked")
		List<Tuple> resultList = em.createNativeQuery(sb.toString(), Tuple.class).getResultList();
		List<String> failedGroup = new ArrayList<>();
		for (Tuple item : resultList) {
			failedGroup.add((String) item.get(0));
		}
		return failedGroup;

	}

	private List<String> getEntityMails() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select TE.* from TBL_USER_ENTITY_ROLE TE ").append("LEFT join  TBL_RETURN_ENTITY_MAPP_NEW TR on TR.ENTITY_ID_FK=TE.ENTITY_ID_FK  ").append(" AND TR.IS_ACTIVE=TE.IS_ACTIVE  ").append(" left join TBL_RETURN RET ON RET.RETURN_ID=TR.RETURN_ID_FK  ").append(" where TE.IS_ACTIVE=true   ").append(" AND RET.RETURN_GROUP_MAP_ID_FK=126 ");
		@SuppressWarnings("unchecked")
		List<UserEntityRole> resultList = em.createNativeQuery(sb.toString(), UserEntityRole.class).getResultList();
		Set<String> toEmailList = new HashSet<>();
		for (UserEntityRole item : resultList) {
			toEmailList.add(item.getCompanyEmail());
		}
		return new ArrayList<>(toEmailList);
	}

	private Set<String> getCCEmails() {
		StringBuilder sb = new StringBuilder();
		sb.append(" Select USER_master.* from TBL_USER_MASTER USER_master ").append("left join TBL_USER_ROLE_MASTER  ROLE_MASTER  ").append("ON USER_master.USER_ID= ROLE_MASTER.USER_MASTER_ID_FK ").append("left join TBL_USER_ROLE ROLETABLE ON ROLETABLE.USER_ROLE_ID = ROLE_MASTER.USER_ROLE_ID_FK ").append("left join TBL_USER_ROLE_RETURN_MAPPING ROLE_RETURN ON ROLE_RETURN.ROLE_ID_FK = ROLE_MASTER.USER_ROLE_ID_FK ").append("LEFT JOIN TBL_RETURN TR ON TR.RETURN_ID = ROLE_RETURN.RETURN_ID_FK ").append("WHERE ROLETABLE.ROLE_TYPE_FK=1 AND  TR.RETURN_GROUP_MAP_ID_FK=126 ");
		@SuppressWarnings("unchecked")
		List<UserMaster> resultList = em.createNativeQuery(sb.toString(), UserMaster.class).getResultList();

		Set<String> ccEmailList = new HashSet<>();
		for (UserMaster item : resultList) {
			ccEmailList.add(item.getPrimaryEmail());
		}
		return ccEmailList;
	}

	private void sendMail(Integer number, String jobProcessingId, String absolutePath) throws IOException {
		List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
		MailServiceBean mailServiceBean = new MailServiceBean();
		List<String> toEmailList = new ArrayList<>();
		List<String> ccEmailList = new ArrayList<>();
		toEmailList = getEntityMails();
		ccEmailList = new ArrayList<>(getCCEmails());
		Map<Integer, List<String>> emailMap = new HashMap<>();
		emailMap.put(MailConstant.EMAIL_TO.getConstantIntVal(), toEmailList);
		emailMap.put(MailConstant.EMAIL_CC.getConstantIntVal(), ccEmailList);
		mailServiceBean.setEmailMap(emailMap);
		mailServiceBean.setAlertId(101l);
		mailServiceBean.setDynamicContentsList(new ArrayList<>());
		mailServiceBean.setUniqueId(Integer.toString(number + 1));
		mailServiceBeanList.add(mailServiceBean);
		List<AttachmentInfoBean> attachmentInfoBean = new ArrayList<>();
		AttachmentInfoBean attachemnet = new AttachmentInfoBean();
		File file = new File(absolutePath);
		attachemnet.setAttachmentName(file.getName());
		attachemnet.setContentType(Files.probeContentType(Paths.get(absolutePath)));
		attachemnet.setAttachment(absolutePath);

		attachmentInfoBean.add(attachemnet);
		mailServiceBean.setAttachmentInfoBean(attachmentInfoBean);
		ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(jobProcessingId, mailServiceBeanList);
		if (serviceResponse.isStatus()) {
			LOGGER.info("Mail sent successfully");
		}
	}

}
