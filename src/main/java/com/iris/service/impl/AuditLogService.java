package com.iris.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.exception.ServiceException;
import com.iris.model.ApplicationTrackingSystemBean;
import com.iris.model.AuditLog;
import com.iris.repository.AuditLogRepo;
import com.iris.util.constant.ErrorConstants;

@Service
public class AuditLogService {

	@Autowired
	AuditLogRepo auditLogRepo;

	public List<AuditLog> getAuditLogData(AuditLog auditLog) {
		List<AuditLog> auditLogList = new ArrayList<>();
		try {
			List<Long> convertedMenuList = Arrays.asList(auditLog.getMenuIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			if (Boolean.TRUE.equals(auditLog.getSelectSpecificFlag())) {
				auditLogList = auditLogRepo.getAuditLogDataByUserId(auditLog.getUserIdFk().getUserId(), convertedMenuList, DateManip.formatDate(auditLog.getRefStartDate(), auditLog.getSessionDateFormat(), DateConstants.YYYY_MM_DD.getDateConstants()), DateManip.formatDate(auditLog.getRefEndDate(), auditLog.getSessionDateFormat(), DateConstants.YYYY_MM_DD.getDateConstants()));
			} else {
				auditLogList = auditLogRepo.getAuditLogDataByRoleTypeId(auditLog.getRoleId(), convertedMenuList, DateManip.formatDate(auditLog.getRefStartDate(), auditLog.getSessionDateFormat(), DateConstants.YYYY_MM_DD.getDateConstants()), DateManip.formatDate(auditLog.getRefEndDate(), auditLog.getSessionDateFormat(), DateConstants.YYYY_MM_DD.getDateConstants()));
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return auditLogList;
	}

	public List<ApplicationTrackingSystemBean> getApplicationTrackingSystem(AuditLog auditLog) {
		List<ApplicationTrackingSystemBean> applicationTrackingSystemList = new ArrayList<>();
		try {
			if (Boolean.TRUE.equals(auditLog.getSelectSpecificFlag())) {
				applicationTrackingSystemList = auditLogRepo.getApplicationTrackingSystem(auditLog.getUserIdFk().getUserName(), DateManip.formatDate(auditLog.getRefStartDate(), auditLog.getSessionDateFormat(), DateConstants.YYYY_MM_DD.getDateConstants()), DateManip.formatDate(auditLog.getRefEndDate(), auditLog.getSessionDateFormat(), DateConstants.YYYY_MM_DD.getDateConstants()));
			} else {
				applicationTrackingSystemList = auditLogRepo.getApplicationTrackingSystemWithDates(DateManip.formatDate(auditLog.getRefStartDate(), auditLog.getSessionDateFormat(), DateConstants.YYYY_MM_DD.getDateConstants()), DateManip.formatDate(auditLog.getRefEndDate(), auditLog.getSessionDateFormat(), DateConstants.YYYY_MM_DD.getDateConstants()));
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return applicationTrackingSystemList;
	}
}
