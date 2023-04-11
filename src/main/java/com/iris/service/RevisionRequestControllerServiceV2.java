/**
 * 
 */
package com.iris.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.RequestApprovalInputBeanV2;
import com.iris.dto.RevisonRequestQueryOutputBean;
import com.iris.model.Category;
import com.iris.model.EntityBean;
import com.iris.model.FrequencyDescription;
import com.iris.model.Return;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.RevisionRequest;
import com.iris.model.SubCategory;
import com.iris.model.UserMaster;
import com.iris.service.impl.RevisionRequestService;

/**
 * @author apagaria
 *
 */
@Service
public class RevisionRequestControllerServiceV2 {

	private static final Logger LOGGER = LoggerFactory.getLogger(RevisionRequestControllerServiceV2.class);

	@Autowired
	private RevisionRequestService revisionRequestService;

	public List<RevisonRequestQueryOutputBean> fetchPendingApprovalRequest(String jobProcessId, RequestApprovalInputBeanV2 requestApprovalInputBeanV2) throws Exception {
		LOGGER.debug(jobProcessId + " Fetch pending approval Request service start");
		String startDateStr = DateManip.formatAppDateTime(new Date(requestApprovalInputBeanV2.getStartDateLong()), "yyyy-MM-dd", "en");
		String endDateStr = DateManip.formatAppDateTime(new Date(requestApprovalInputBeanV2.getEndDateLong()), "yyyy-MM-dd", "en");
		Date startDate = DateManip.convertStringToDate(startDateStr, DateConstants.YYYY_MM_DD.getDateConstants());
		Date endDate = DateManip.convertStringToDate(endDateStr, DateConstants.YYYY_MM_DD.getDateConstants());
		List<RevisonRequestQueryOutputBean> revisonRequestQueryOutputBeanList = revisionRequestService.fetchPendingRequest(requestApprovalInputBeanV2, startDate, endDate);
		LOGGER.debug(jobProcessId + " Fetch pending approval Request service End");
		return revisonRequestQueryOutputBeanList;
	}

	/**
	 * @param revisonRequestQueryOutputBeans
	 * @return
	 */
	public List<RevisionRequest> convertQueryOutputToBean(String jobProcessId, List<RevisonRequestQueryOutputBean> revisonRequestQueryOutputBeans) {
		LOGGER.debug(jobProcessId + " Fetch pending approval Request convertQueryOutputToBean service start");
		List<RevisionRequest> revisionRequestList = null;
		if (!CollectionUtils.isEmpty(revisonRequestQueryOutputBeans)) {
			revisionRequestList = new ArrayList<>();
			for (RevisonRequestQueryOutputBean revisonRequestQueryOutputBean : revisonRequestQueryOutputBeans) {
				RevisionRequest revisionRequest = new RevisionRequest();

				// Return
				Return returns = new Return();
				returns.setReturnId(revisonRequestQueryOutputBean.getReturnId());
				returns.setReturnCode(revisonRequestQueryOutputBean.getReturnCode());
				returns.setReturnName(revisonRequestQueryOutputBean.getReturnName());
				revisionRequest.setReturns(returns);

				// Entity
				// Sub Category
				SubCategory subCategory = new SubCategory();
				subCategory.setSubCategoryId(revisonRequestQueryOutputBean.getSubCategoryId());
				subCategory.setSubCategoryCode(revisonRequestQueryOutputBean.getSubCategoryCode());
				subCategory.setSubCategoryName(revisonRequestQueryOutputBean.getSubCategoryName());

				// Category
				Category category = new Category();
				category.setCategoryId(revisonRequestQueryOutputBean.getCategoryId());
				category.setCategoryCode(revisonRequestQueryOutputBean.getCategoryCode());
				category.setCategoryName(revisonRequestQueryOutputBean.getCategoryName());

				EntityBean entityBean = new EntityBean();
				entityBean.setEntityId(revisonRequestQueryOutputBean.getEntityId());
				entityBean.setEntityCode(revisonRequestQueryOutputBean.getEntityCode());
				entityBean.setEntityName(revisonRequestQueryOutputBean.getEntityName());
				entityBean.setCategory(category);
				entityBean.setSubCategory(subCategory);
				revisionRequest.setEntity(entityBean);

				// Frequency
				FrequencyDescription frequencyDesc = new FrequencyDescription();
				frequencyDesc.setFinYrFrquencyDesc(revisonRequestQueryOutputBean.getFinYrFrquencyDesc());
				revisionRequest.setFrequencyDesc(frequencyDesc);

				// Return Properties
				ReturnPropertyValue returnPropertyVal = new ReturnPropertyValue();
				returnPropertyVal.setReturnProValue(revisonRequestQueryOutputBean.getReturnPropValue());
				revisionRequest.setReturnPropertyVal(returnPropertyVal);

				// User
				UserMaster createdBy = new UserMaster();
				createdBy.setUserId(revisonRequestQueryOutputBean.getUserId());
				createdBy.setUserName(revisonRequestQueryOutputBean.getUserName());
				revisionRequest.setCreatedBy(createdBy);

				// Other details
				revisionRequest.setCreatedOn(revisonRequestQueryOutputBean.getCreatedOn());
				revisionRequest.setYear(revisonRequestQueryOutputBean.getYear());
				revisionRequest.setMonth(revisonRequestQueryOutputBean.getMonth());
				revisionRequest.setReportingDate(revisonRequestQueryOutputBean.getReportingDate());
				revisionRequest.setStartDate(revisonRequestQueryOutputBean.getStartDate());
				revisionRequest.setEndDate(revisonRequestQueryOutputBean.getEndDate());
				revisionRequest.setReasonForRequest(revisonRequestQueryOutputBean.getReasonForRequest());
				revisionRequest.setReasonForRejection(revisonRequestQueryOutputBean.getReasonForRejection());
				revisionRequest.setRevisionRequestId(revisonRequestQueryOutputBean.getRevisionRequestId());
				revisionRequest.setAdminStatusIdFk(revisonRequestQueryOutputBean.getAdminStatusIdFk());

				// add in list
				revisionRequestList.add(revisionRequest);
			}
		}
		LOGGER.debug(jobProcessId + " Fetch pending approval Request convertQueryOutputToBean service end");
		return revisionRequestList;
	}

}
