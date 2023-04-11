package com.iris.service.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.iris.dto.EntityFilingPendingBean;
import com.iris.dto.EntityFilingPendingData;
import com.iris.dto.MISPendToListBean;
import com.iris.dto.UserRoleReturnMappingDto;
import com.iris.util.AESV2;
import com.iris.util.constant.ErrorConstants;

/**
 * @author pradnyam
 */

@Service
public class MISPendingEmailData {

	@Autowired
	private EntityManager em;

	static final Logger LOGGER = LogManager.getLogger(MISPendingEmailData.class);

	public EntityFilingPendingBean getRequiredDataForEmailSending(EntityFilingPendingBean pendingFilingEntities, String jobProcessId) {

		List<EntityFilingPendingData> entityFilingPendingDataList = pendingFilingEntities.getEntityFilingPendingDataList();
		List<String> entityCodeList = entityFilingPendingDataList.stream().map(EntityFilingPendingData::getEntityCode).collect(Collectors.toList());
		List<String> returnCodeList = entityFilingPendingDataList.stream().map(EntityFilingPendingData::getReturnCode).collect(Collectors.toList());

		//Getting To Email List
		List<Tuple> result = getToEmailList(pendingFilingEntities, entityCodeList, returnCodeList);

		//Getting CC Email List
		Map<String, Set<String>> returnCodeAndCCEmailIds = getCCEmailList(returnCodeList);

		List<EntityFilingPendingData> newEntityFilingPendingDataList = new ArrayList<>();

		// result contains entity Code and return code and email ids
		// so searching the entity code and return code combination in the selected records and setting the email to list to the 
		// respective object

		for (Tuple item : result) {
			String entityCodeDB = item.get(0) != null ? item.get(0).toString() : "";
			String returnCodeDB = item.get(1) != null ? item.get(1).toString() : "";
			String companyEmailDB = item.get(2) != null ? item.get(2).toString() : "";
			String returnProperty = item.get(3) != null ? item.get(3).toString() : "";

			//Decrypting email address if encrypted else setting the same
			String tempEmail = "";
			try {
				tempEmail = AESV2.getInstance().decrypt(companyEmailDB);
				companyEmailDB = tempEmail;
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				//***** Important Note : If Exception Occures then mail Id is not in the Encrypted format so just keeping the same mail Id as getting
			}

			List<EntityFilingPendingData> entityFilingPendingDataListFiltered = entityFilingPendingDataList.stream().filter(e -> e.getEntityCode().equals(entityCodeDB) && e.getReturnCode().equals(returnCodeDB)).collect(Collectors.toList());

			for (EntityFilingPendingData entityFilingPendingData : entityFilingPendingDataListFiltered) {
				if (entityFilingPendingData.getEntityCode().equals(entityCodeDB) && entityFilingPendingData.getReturnCode().equals(returnCodeDB)) {
					if (entityFilingPendingData.getToEmailIdList() == null || entityFilingPendingData.getToEmailIdList().isEmpty()) {
						List<String> toMailList = new ArrayList<>();
						toMailList.add(companyEmailDB);
						entityFilingPendingData.setToEmailIdList(toMailList);
					} else {
						if (!entityFilingPendingData.getToEmailIdList().contains(companyEmailDB)) {
							entityFilingPendingData.getToEmailIdList().add(companyEmailDB);
						}
					}

					entityFilingPendingData.setReturnProperty(returnProperty);

					newEntityFilingPendingDataList.add(entityFilingPendingData);
				}

				//Setting CC List if not set , if already set then skipping (using same loop for setting the cc list)
				if (returnCodeAndCCEmailIds.containsKey(entityFilingPendingData.getReturnCode()) && (entityFilingPendingData.getCcEmailIdList() == null || entityFilingPendingData.getCcEmailIdList().isEmpty())) {
					entityFilingPendingData.setCcEmailIdList(new ArrayList<>(returnCodeAndCCEmailIds.get(entityFilingPendingData.getReturnCode())));
				}
			}
		}
		pendingFilingEntities.setEntityFilingPendingDataList(newEntityFilingPendingDataList);
		return pendingFilingEntities;

	}

	private List<Tuple> getToEmailList(EntityFilingPendingBean pendingFilingEntities, List<String> entityCodeList, List<String> returnCodeList) {

		StringBuilder sb = new StringBuilder();

		sb.append(" select entity.ENTITY_CODE,tblReturn.RETURN_CODE,userEntityRole.COMPANY_EMAIL,tblReturn.RETURN_PROPERTY_ID_FK ").append(" from TBL_USER_ENTITY_ROLE userEntityRole,TBL_USER_ROLE_MASTER userRoleMaster , TBL_USER_ROLE_RETURN_MAPPING userRoleReturnMap , ").append(" TBL_ENTITY entity,TBL_RETURN tblReturn , TBL_USER_MASTER userMaster   ").append(" where tblReturn.RETURN_CODE in (:returnCodeList) and tblReturn.IS_ACTIVE = 1 ").append(" and entity.ENTITY_CODE in (:entityCodeList) and entity.IS_ACTIVE = 1 ").append(" and userMaster.IS_ACTIVE = 1 ").append(" and userEntityRole.IS_ACTIVE = 1  ").append(" and userRoleReturnMap.IS_ACTIVE = 1 ").append(" and userRoleMaster.IS_ACTIVE = 1 ").append(" and userEntityRole.USER_ROLE_MASTER_ID_FK = userRoleMaster.USER_ROLE_MASTER_ID ").append(" and userRoleMaster.USER_ROLE_ID_FK = userRoleReturnMap.ROLE_ID_FK  ").append(" and entity.ENTITY_ID = userEntityRole.ENTITY_ID_FK ").append(" and tblReturn.RETURN_ID = userRoleReturnMap.RETURN_ID_FK ").append(" and userMaster.USER_ID = userRoleMaster.USER_MASTER_ID_FK ");

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);

		query.setParameter("returnCodeList", returnCodeList);
		query.setParameter("entityCodeList", entityCodeList);

		List<Tuple> result = query.getResultList();

		return result;
	}

	public EntityFilingPendingBean getToAndCCEmailList(EntityFilingPendingBean pendingFilingEntities) {

		List<EntityFilingPendingData> entityFilingPendingDataList = pendingFilingEntities.getEntityFilingPendingDataList();
		List<String> entityCodeList = entityFilingPendingDataList.stream().map(EntityFilingPendingData::getEntityCode).collect(Collectors.toList());
		List<String> returnCodeList = entityFilingPendingDataList.stream().map(EntityFilingPendingData::getReturnCode).collect(Collectors.toList());

		StringBuilder sb = new StringBuilder();
		//*****Getting data from TBL_RETURN_ENTITY_MAPP_NEW for return mapped to entity
		sb.append(" select TEMN.ENTITY_ID_FK,TEMN.RETURN_ID_FK,TUER.USER_ROLE_MASTER_ID_FK , TURM.USER_ROLE_ID_FK,TUER.COMPANY_EMAIL ");
		sb.append(" from TBL_RETURN_ENTITY_MAPP_NEW TEMN,TBL_USER_ENTITY_ROLE TUER,TBL_USER_ROLE_MASTER TURM,TBL_USER_MASTER TUM ");
		sb.append(" where TEMN.ENTITY_ID_FK IN (select ENTITY_ID from TBL_ENTITY where ENTITY_CODE IN (:entityCodeList))  ");
		sb.append(" and TEMN.RETURN_ID_FK IN (select RETURN_ID from TBL_RETURN where RETURN_CODE IN (:returnCodeList))  ");
		sb.append(" and TEMN.IS_ACTIVE = 1 ");
		sb.append(" and TUER.IS_ACTIVE =1 ");
		sb.append(" and TURM.IS_ACTIVE = 1 ");
		sb.append(" and TUM.IS_ACTIVE = 1  ");
		sb.append(" and TEMN.ENTITY_ID_FK = TUER.ENTITY_ID_FK ");
		sb.append(" and TUM.USER_ID = TURM.USER_MASTER_ID_FK ");
		sb.append(" and TUER.USER_ROLE_MASTER_ID_FK = TURM.USER_ROLE_MASTER_ID ");

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);

		query.setParameter("returnCodeList", returnCodeList);
		query.setParameter("entityCodeList", entityCodeList);

		List<Tuple> result = query.getResultList();

		List<MISPendToListBean> misPendToListBeanList = new ArrayList<>();

		for (Tuple item : result) {
			Long entityId = item.get(0) != null ? Long.parseLong(item.get(0).toString()) : null;
			Long returnId = item.get(1) != null ? Long.parseLong(item.get(1).toString()) : null;
			Long userRoleMasterId = item.get(2) != null ? Long.parseLong(item.get(2).toString()) : null;
			Long userRoleId = item.get(3) != null ? Long.parseLong(item.get(3).toString()) : null;
			String companyEmail = item.get(4) != null ? item.get(4).toString() : "";

			MISPendToListBean misPendToListBean = new MISPendToListBean();
			misPendToListBean.setEntityId(entityId);
			misPendToListBean.setReturnId(returnId);
			misPendToListBean.setUserRoleId(userRoleId);
			misPendToListBean.setUserRoleMasterId(userRoleMasterId);
			misPendToListBean.setCompanyEmail(companyEmail);
			misPendToListBeanList.add(misPendToListBean);
		}

		Set<Long> roleIdSet = null;
		if (misPendToListBeanList != null) {
			roleIdSet = misPendToListBeanList.stream().map(MISPendToListBean::getUserRoleId).collect(Collectors.toSet());
		}

		List<Long> roleIdList = new ArrayList<>(roleIdSet);

		//------------Getting data from TBL_USER_ROLE_RETURN_MAPPING for role and return mapping

		sb = new StringBuilder();
		sb.append(" select ROLE_RETURN_ID,ROLE_ID_FK,RETURN_ID_FK,IS_ACTIVE from TBL_USER_ROLE_RETURN_MAPPING ");
		sb.append(" where ROLE_ID_FK in (:roleIdList) ");

		query = em.createNativeQuery(sb.toString(), Tuple.class);

		query.setParameter("roleIdList", roleIdList);

		List<Tuple> result2 = query.getResultList();
		List<UserRoleReturnMappingDto> userRoleReturnMappingDtoList = new ArrayList<>();

		for (Tuple item : result2) {
			Long id = item.get(0) != null ? Long.parseLong(item.get(0).toString()) : null;
			Long roleId = item.get(1) != null ? Long.parseLong(item.get(1).toString()) : null;
			Long returnId = item.get(2) != null ? Long.parseLong(item.get(2).toString()) : null;
			Long isActive = item.get(3) != null ? Long.parseLong(item.get(3).toString()) : null;

			UserRoleReturnMappingDto userRoleReturnMappingDto = new UserRoleReturnMappingDto();
			userRoleReturnMappingDto.setRoleReturnId(id);
			userRoleReturnMappingDto.setRoleIdFk(roleId);
			userRoleReturnMappingDto.setReturnIdFk(returnId);
			userRoleReturnMappingDto.setIsActive(isActive != null && isActive == 0 ? false : true);
			userRoleReturnMappingDtoList.add(userRoleReturnMappingDto);
		}

		for (int i = 0; i < misPendToListBeanList.size(); i++) {
			//****Filtering list by User Role
			Long userRoleId = misPendToListBeanList.get(i).getUserRoleId();
			List<UserRoleReturnMappingDto> userRoleReturnMappingDtoListFiltered = userRoleReturnMappingDtoList.stream().filter(e -> e.getRoleIdFk().equals(userRoleId)).collect(Collectors.toList());
			//******Checking if role is having entry in tbl_user_role_return_mapping table, 
			//******if yes then consider only those returns assigned in this table
			//******otherwise all returns assigned to entity (TBL_RETURN_ENTITY_MAPP_NEW table)
			if (userRoleReturnMappingDtoListFiltered != null && !userRoleReturnMappingDtoListFiltered.isEmpty()) {
				Long retunId = misPendToListBeanList.get(i).getReturnId();
				List<UserRoleReturnMappingDto> checkReturnId = userRoleReturnMappingDtoList.stream().filter(e -> e.getReturnIdFk().equals(retunId) && e.getIsActive().equals(true)).collect(Collectors.toList());
				if (checkReturnId != null && !checkReturnId.isEmpty()) {
					misPendToListBeanList.get(i).setSendMail(true);
				} else {
					misPendToListBeanList.get(i).setSendMail(false);
				}
			} else {
				misPendToListBeanList.get(i).setSendMail(true);
			}

		}

		//Getting CC Email List
		Map<String, Set<String>> returnCodeAndCCEmailIds = getCCEmailList(returnCodeList);

		//****preparing to List and CC List
		//List<EntityFilingPendingData> getEntityFilingPendingDataList = pendingFilingEntities.getEntityFilingPendingDataList();
		for (int j = 0; j < entityFilingPendingDataList.size(); j++) {
			if (entityFilingPendingDataList.get(j) == null) {
				continue;
			}
			Long entityId = entityFilingPendingDataList.get(j).getEntityId();
			Long returnId = entityFilingPendingDataList.get(j).getReturnId();

			List<MISPendToListBean> misPendToListBeanListFiltered = misPendToListBeanList.stream().filter(e -> e.getEntityId().equals(entityId) && e.getReturnId().equals(returnId)).collect(Collectors.toList());

			for (MISPendToListBean misPendToListBean : misPendToListBeanListFiltered) {
				if (misPendToListBean.isSendMail()) {
					String tempEmail = "";
					try {
						tempEmail = AESV2.getInstance().decrypt(misPendToListBean.getCompanyEmail());
					} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
						//***** Important Note : If Exception Occures then mail Id is not in the Encrypted format so just keeping the same mail Id as getting
						tempEmail = misPendToListBean.getCompanyEmail();
					}

					if (entityFilingPendingDataList.get(j).getToEmailIdList() != null && !entityFilingPendingDataList.get(j).getToEmailIdList().isEmpty()) {
						if (!tempEmail.isEmpty() && !entityFilingPendingDataList.get(j).getToEmailIdList().contains(tempEmail)) {
							entityFilingPendingDataList.get(j).getToEmailIdList().add(tempEmail);
						}
					} else {
						List<String> emailToList = new ArrayList<>();
						if (!tempEmail.isEmpty()) {
							emailToList.add(tempEmail);
						}
						if (!emailToList.isEmpty()) {
							entityFilingPendingDataList.get(j).setToEmailIdList(emailToList);
						}
					}
				}
			}

			//Setting CC List if not set , if already set then skipping (using same loop for setting the cc list)
			if (returnCodeAndCCEmailIds.containsKey(entityFilingPendingDataList.get(j).getReturnCode()) && (entityFilingPendingDataList.get(j).getCcEmailIdList() == null || entityFilingPendingDataList.get(j).getCcEmailIdList().isEmpty())) {
				entityFilingPendingDataList.get(j).setCcEmailIdList(new ArrayList<>(returnCodeAndCCEmailIds.get(entityFilingPendingDataList.get(j).getReturnCode())));
			}

		}

		//*********

		pendingFilingEntities.setEntityFilingPendingDataList(entityFilingPendingDataList);

		return pendingFilingEntities;
	}

	private Map<String, Set<String>> getCCEmailList(List<String> returnCodeList) {
		StringBuilder sb = new StringBuilder();

		//		sb.append(" select TR.RETURN_CODE ,TRRM.EMAIL_IDs,TUM.PRIMARY_EMAIL,TUM.IS_ACTIVE from TBL_RETURN_REGULATOR_MAPPING TRRM   ")
		//		.append(" left join TBL_USER_MASTER TUM  on TUM.DEPARTMENT_ID_FK = TRRM.REGULATOR_ID_FK  ")
		//		.append(" left join TBL_RETURN TR on  TR.RETURN_ID = TRRM.RETURN_ID_FK  ")
		//		.append(" left join TBL_USER_ROLE_RETURN_MAPPING TURRM on TR.RETURN_ID = TURRM.RETURN_ID_FK ")
		//		.append(" left join TBL_USER_ROLE_MASTER TURM on TURM.USER_ROLE_ID_FK = TURRM.ROLE_ID_FK ")
		//		.append(" where TR.RETURN_CODE IN (:returnCodeList) ")
		//		.append(" and TR.IS_ACTIVE = 1  ")
		//		.append(" and TRRM.IS_ACTIVE = 1  ")
		//		.append(" and TUM.IS_ACTIVE = 1 ")
		//		.append(" and TURM.IS_ACTIVE = 1 ")
		//		.append(" and TURRM.IS_ACTIVE = 1 ")
		//		.append(" UNION ")
		//		.append(" select TR.RETURN_CODE ,TRRM.EMAIL_IDs,TUM.PRIMARY_EMAIL,TUM.IS_ACTIVE from TBL_RETURN_REGULATOR_MAPPING TRRM   ")
		//		.append(" left join TBL_USER_MASTER TUM  on TUM.DEPARTMENT_ID_FK = TRRM.REGULATOR_ID_FK  ")
		//		.append(" left join TBL_RETURN TR on  TR.RETURN_ID = TRRM.RETURN_ID_FK  ")
		//		.append(" left join TBL_USER_ROLE_RETURN_MAPPING TURRM on TR.RETURN_ID = TURRM.RETURN_ID_FK ")
		//		.append(" left join TBL_USER_ROLE_MASTER TURM on TURM.USER_ROLE_ID_FK = TURRM.ROLE_ID_FK ")
		//		.append(" where TR.RETURN_CODE IN (:returnCodeList) ")
		//		.append(" and TR.IS_ACTIVE = 1  ")
		//		.append(" and TRRM.IS_ACTIVE = 1  ")
		//		.append(" and TURM.IS_ACTIVE = 1 ")
		//		.append(" and TURRM.IS_ACTIVE = 1 ");

		sb.append(" select distinct TR.RETURN_CODE,TRRM.EMAIL_IDs ,TUM.PRIMARY_EMAIL,TUM.IS_ACTIVE   ");
		sb.append(" from TBL_RETURN_REGULATOR_MAPPING TRRM   ");
		sb.append(" left join TBL_RETURN TR ON TR.RETURN_ID = TRRM.RETURN_ID_FK   ");
		sb.append(" left join TBL_USER_ROLE_RETURN_MAPPING TURRM on TR.RETURN_ID = TURRM.RETURN_ID_FK   ");
		sb.append(" left join TBL_USER_ROLE TUR ON TUR.USER_ROLE_ID = TURRM.ROLE_ID_FK   ");
		sb.append(" left join TBL_USER_ROLE_MASTER TURM on TURM.USER_ROLE_ID_FK = TUR.USER_ROLE_ID   ");
		sb.append(" left join TBL_USER_MASTER TUM on TUM.USER_ID = TURM.USER_MASTER_ID_FK and TUM.IS_ACTIVE = 1 and TUM.ROLE_TYPE_FK = 1   ");
		sb.append(" where TR.RETURN_CODE IN (:returnCodeList)   ");
		sb.append(" and TRRM.IS_ACTIVE = 1   ");
		sb.append(" and TR.IS_ACTIVE = 1    ");
		sb.append(" and TUR.IS_ACTIVE = 1   ");
		sb.append(" and TURM.IS_ACTIVE = 1    ");
		sb.append(" and TURRM.IS_ACTIVE = 1   ");

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);
		query.setParameter("returnCodeList", returnCodeList);

		List<Tuple> result = query.getResultList();

		Map<String, Set<String>> returnCodeAndCCEmailIds = new HashMap<>();
		try {
			for (Tuple item : result) {
				String returnCode = item.get(0) != null ? item.get(0).toString() : "";
				String retRegMapEmail = item.get(1) != null ? item.get(1).toString() : "";
				String userMasterPrimEmail = item.get(2) != null ? item.get(2).toString() : "";
				String userIsActive = item.get(3) != null ? item.get(3).toString() : "";
				//If user is not active then userIsActive = 0 but for particular department there is only department owner email id 
				//and no any user is there for that department then userIsActive = "", so it should be acceptable
				if (userIsActive.equals("0")) {
					continue;
				}
				String tempRegMEmail = "";
				String tempUMEmail = "";

				//Decrypting email address if encrypted else setting the same
				try {
					tempUMEmail = AESV2.getInstance().decrypt(userMasterPrimEmail);
					userMasterPrimEmail = tempUMEmail;
				} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
					//***** Important Note : If Exception Occures then mail Id is not in the Encrypted format so just keeping the same mail Id as getting
				}

				//Decrypting email address if encrypted else setting the same
				//Regulator email may contains multiple seperated by comma
				String[] retRegMapEmailArr = retRegMapEmail != null ? retRegMapEmail.split(",") : new String[] { "" };
				for (int i = 0; i < retRegMapEmailArr.length; i++) {
					try {
						tempRegMEmail = AESV2.getInstance().decrypt(retRegMapEmailArr[i]);
						retRegMapEmailArr[i] = tempRegMEmail;
						//						retRegMapEmail = tempRegMEmail;
					} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
					}
				}

				//				if (returnCodeAndCCEmailIds.containsKey(returnCode)) {
				//					for (String retRegEmail : retRegMapEmailArr) {
				//						returnCodeAndCCEmailIds.get(returnCode).add(retRegEmail);
				//					}
				//					returnCodeAndCCEmailIds.get(returnCode).add(userMasterPrimEmail);
				//				} else {
				//					Set<String> arrEmails = new HashSet<>();
				//					for (String retRegEmail : retRegMapEmailArr) {
				//						returnCodeAndCCEmailIds.get(returnCode).add(retRegEmail);
				//					}
				//					arrEmails.add(userMasterPrimEmail);
				//					returnCodeAndCCEmailIds.put(returnCode, arrEmails);
				//				}
				for (String retRegEmail : retRegMapEmailArr) {
					if (returnCodeAndCCEmailIds.containsKey(returnCode)) {
						returnCodeAndCCEmailIds.get(returnCode).add(retRegEmail);
						if (userMasterPrimEmail != null && !userMasterPrimEmail.isEmpty()) {
							returnCodeAndCCEmailIds.get(returnCode).add(userMasterPrimEmail);
						}
					} else {
						Set<String> arrEmails = new HashSet<>();
						arrEmails.add(userMasterPrimEmail);
						if (userMasterPrimEmail != null && !userMasterPrimEmail.isEmpty()) {
							arrEmails.add(retRegEmail);
						}
						returnCodeAndCCEmailIds.put(returnCode, arrEmails);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "getCCEmailList : ", e);
		}
		return returnCodeAndCCEmailIds;
	}
}
