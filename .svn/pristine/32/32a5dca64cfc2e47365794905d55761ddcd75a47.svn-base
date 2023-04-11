/**
 * 
 */
package com.iris.sdmx.lockrecord.helper;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.model.UserMaster;
import com.iris.sdmx.lockrecord.bean.SdmxLockRecordBean;
import com.iris.sdmx.lockrecord.bean.SdmxLockRecordSetBean;
import com.iris.sdmx.lockrecord.entity.SdmxLockRecordEntity;
import com.iris.service.impl.UserMasterService;

/**
 * @author apagaria
 *
 */
@Component
public class SdmxLockRecordHelper {

	/**
	 * 
	 */
	@Autowired
	private UserMasterService userMasterService;

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxLockRecordHelper.class);

	/**
	 * @param sdmxLockRecordSetBean
	 * @param sdmxLockRecordEntity
	 */
	public void convertSetBeanToEntity(SdmxLockRecordSetBean sdmxLockRecordSetBean, SdmxLockRecordEntity sdmxLockRecordEntity, Long userId, String jobProcessingId, Long lockRecordId, String recordDetailEncodedStr) {
		LOGGER.info("@convertSetBeanToEntity START - Job Processing Id - " + jobProcessingId);
		BeanUtils.copyProperties(sdmxLockRecordSetBean, sdmxLockRecordEntity);

		// Json Sorting and Encoding
		if (!StringUtils.isBlank(recordDetailEncodedStr)) {
			sdmxLockRecordEntity.setRecordDetailEncode(recordDetailEncodedStr);
		}

		sdmxLockRecordEntity.setRecordDetailJson(new String(Base64.decodeBase64(recordDetailEncodedStr)));

		// Locked User
		UserMaster lockedUser = null;
		if (sdmxLockRecordSetBean.getLockedBy() != null) {
			lockedUser = userMasterService.getDataById(sdmxLockRecordSetBean.getLockedBy());
			sdmxLockRecordEntity.setLockedBy(lockedUser);
		}

		// Created By
		if (userId != null) {
			if (sdmxLockRecordSetBean.getLockedBy() == userId) {
				sdmxLockRecordEntity.setCreatedBy(lockedUser);
			} else {
				lockedUser = new UserMaster();
				lockedUser.setUserId(userId);
				sdmxLockRecordEntity.setCreatedBy(lockedUser);
			}
			// Created On
			sdmxLockRecordEntity.setCreatedOn(new Date());
		}

		// Is Active
		sdmxLockRecordEntity.setIsActive(Boolean.TRUE);
		sdmxLockRecordEntity.setRecordLockPeriod(0L);

		if (lockRecordId != null) {
			sdmxLockRecordEntity.setLockRecordId(lockRecordId);
		}

		sdmxLockRecordEntity.setReleasedBy(null);
		sdmxLockRecordEntity.setReleasedOn(null);
		LOGGER.info("@convertSetBeanToEntity END - Job Processing Id - " + jobProcessingId);
	}

	/**
	 * @param sdmxLockRecordEntity
	 * @param sdmxLockRecordBean
	 * @param jobProcessingId
	 */
	public static void fetchBeanFromEntity(SdmxLockRecordEntity sdmxLockRecordEntity, SdmxLockRecordBean sdmxLockRecordBean, String jobProcessingId) {
		LOGGER.info("@fetchBeanFromEntity START - Job Processing Id - " + jobProcessingId);
		BeanUtils.copyProperties(sdmxLockRecordEntity, sdmxLockRecordBean);

		// Locked By User
		if (sdmxLockRecordEntity.getLockedBy() != null) {
			sdmxLockRecordBean.setLockedBy(sdmxLockRecordEntity.getLockedBy().getUserId());
			sdmxLockRecordBean.setLockedByUserName(sdmxLockRecordEntity.getLockedBy().getUserName());
		}

		// Created By
		if (sdmxLockRecordEntity.getCreatedBy() != null) {
			sdmxLockRecordBean.setCreatedBy(sdmxLockRecordEntity.getCreatedBy().getUserId());
			sdmxLockRecordBean.setCreatedByUserName(sdmxLockRecordEntity.getCreatedBy().getUserName());
		}

		// Released By
		if (sdmxLockRecordEntity.getReleasedBy() != null) {
			sdmxLockRecordBean.setReleasedBy(sdmxLockRecordEntity.getReleasedBy().getUserId());
			sdmxLockRecordBean.setReleasedByUserName(sdmxLockRecordEntity.getReleasedBy().getUserName());
		}

		// Released On
		if (sdmxLockRecordEntity.getReleasedOn() != null) {
			sdmxLockRecordBean.setReleasedOn(sdmxLockRecordEntity.getReleasedOn());
		}

		//Lock record Id
		if (sdmxLockRecordEntity.getLockRecordId() != null) {
			sdmxLockRecordBean.setLockRecordId(sdmxLockRecordEntity.getLockRecordId());
		}

		//module Id
		if (sdmxLockRecordEntity.getModuleId() != null) {
			sdmxLockRecordBean.setModuleId(sdmxLockRecordEntity.getModuleId());
		}

		//record Detail Json
		if (sdmxLockRecordEntity.getRecordDetailJson() != null) {
			sdmxLockRecordBean.setRecordDetailJson(sdmxLockRecordEntity.getRecordDetailJson());
		}

		//created on
		if (sdmxLockRecordEntity.getCreatedOn() != null) {
			sdmxLockRecordBean.setCreatedOn(sdmxLockRecordEntity.getCreatedOn());
		}
		LOGGER.debug("@fetchBeanFromEntity - Job Processing Id - " + jobProcessingId + ", Transformed Data - " + sdmxLockRecordBean);
		LOGGER.info("@fetchBeanFromEntity END - Job Processing Id - " + jobProcessingId);
	}

	public static String sortingNBase64FromRecordDetailJsonString(String recordDetailEncodeJsonStr, String jobProcessingId) {
		LOGGER.info("@fetchBeanFromEntity Start - Job Processing Id - " + jobProcessingId);
		LOGGER.debug("@fetchBeanFromEntity - Job Processing Id - " + jobProcessingId + ", recordDetailJson - " + recordDetailEncodeJsonStr);
		Gson gson = new Gson();
		String recordDetailEncodedStr = null;
		// Sort the json
		SortedMap<String, Long> retMap = gson.fromJson(new String(Base64.decodeBase64(recordDetailEncodeJsonStr)), new TypeToken<TreeMap<String, Long>>() {
		}.getType());
		// base 64 conversion
		recordDetailEncodedStr = Base64.encodeBase64String(gson.toJson(retMap).getBytes());
		LOGGER.debug("@fetchBeanFromEntity - Job Processing Id - " + jobProcessingId + ", recordDetailJson - " + recordDetailEncodedStr);
		LOGGER.info("@fetchBeanFromEntity END - Job Processing Id - " + jobProcessingId);
		return recordDetailEncodedStr;
	}
}
