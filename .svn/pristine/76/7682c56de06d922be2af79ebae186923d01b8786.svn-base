package com.iris.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.exception.ServiceException;
import com.iris.model.Frequency;
import com.iris.model.Regulator;
import com.iris.model.ReturnGroupMapping;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.repository.ReturnGroupMappingRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;

@Service
public class ReturnGroupMappingService implements GenericService<ReturnGroupMapping, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnGroupMappingService.class);

	@Autowired
	ReturnGroupMappingRepo returnGroupMappingRepo;

	@Autowired
	EntityManager enttyManager;

	@Autowired
	private UserMasterService userMasterService;

	@Override
	public ReturnGroupMapping add(ReturnGroupMapping returnGroupMapping) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ReturnGroupMapping returnGroupMappingBean) throws ServiceException {
		return false;
	}

	@Override
	public List<ReturnGroupMapping> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupMapping> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		return null;
	}

	@Override
	public List<ReturnGroupMapping> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReturnGroupMapping> getActiveDataFor(Class bean, Long id) throws ServiceException {
		List<ReturnGroupMapping> returnGroupMappingList = null;
		try {
			if (bean.isInstance(ReturnGroupMapping.class) && id == null) {
				returnGroupMappingList = returnGroupMappingRepo.getAllActiveData();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return returnGroupMappingList;
	}

	@Override
	public List<ReturnGroupMapping> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ReturnGroupMapping bean) throws ServiceException {

	}

	@Override
	public ReturnGroupMapping getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnGroupMapping> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		return null;
	}

	public List<ReturnGroupMappingDto> prepareReturnGroupMappingListRegulatorUserModified(ReturnGroupMappingRequest returnGroupMappingRequest) {
		StringBuilder stringBuilder;
		UserMaster userMasterObj = null;
		userMasterObj = userMasterService.getDataById(returnGroupMappingRequest.getUserId());

		if (userMasterObj != null && userMasterObj.getDepartmentIdFk() != null && userMasterObj.getDepartmentIdFk().getIsMaster() && returnGroupMappingRequest.getRoleId().equals(GeneralConstants.RBI_SUPER_USER.getConstantLongVal())) {
			stringBuilder = new StringBuilder("SELECT roleReturn.ROLE_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, " + " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, " + " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID " + " FROM TBL_USER_ROLE_RETURN_MAPPING roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join " + " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join " + " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID" + " where ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1 and retTypeMap.IS_ACTIVE = 1 "
			//					+ " and retRegMap.IS_ACTIVE = 1 ");
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																			+ " and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 ");

		} else {
			stringBuilder = new StringBuilder("SELECT roleReturn.ROLE_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, " + " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, " + " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID " + " FROM TBL_USER_ROLE_RETURN_MAPPING roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join " + " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join " + " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID" + " where ROLE_ID_FK = " + returnGroupMappingRequest.getRoleId() + " and ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1 and retTypeMap.IS_ACTIVE = 1 "
			//					+ " and retRegMap.IS_ACTIVE = 1 ");
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																			+ " and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 ");
		}

		if (returnGroupMappingRequest.getFrequencyId() != null) {
			stringBuilder.append("and ret.FREQUENCY_ID_FK =:frequencyId ");
		}

		if (returnGroupMappingRequest.getReturnGroupIds() != null) {
			stringBuilder.append(" and ret.RETURN_GROUP_MAP_ID_FK in (:returnGroupIds)");
		}

		stringBuilder.append("and roleReturn.RETURN_ID_FK = ret.RETURN_ID " + " and groupLable.RETURN_GROUP_MAP_ID_FK = ret.RETURN_GROUP_MAP_ID_FK and reg.REGULATOR_ID = regulatorLable.REGULATOR_ID_FK " + " and retGroup.RETURN_GROUP_MAP_ID = groupLable.RETURN_GROUP_MAP_ID_FK and retLabel.RETURN_ID_FK = ret.RETURN_ID and ret.FREQUENCY_ID_FK = freq.FREQUENCY_ID " + " and retRegMap.RETURN_ID_FK = ret.RETURN_ID and ret.RETURN_ID = retRegMap.RETURN_ID_FK  and retRegMap.REGULATOR_ID_FK = regulatorLable.REGULATOR_ID_FK " + " order by GROUP_LABEL, RETURN_LABEL, RETURN_TYPE_ID asc");

		//		if(returnGroupMappingRequest.getFrequencyId()!= null) {
		//			stringBuilder = new StringBuilder("SELECT roleReturn.ROLE_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, "
		//					+ " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, "
		//					+ " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID "
		//					+ " FROM TBL_USER_ROLE_RETURN_MAPPING roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join "
		//					+ " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join "
		//					+ " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID"
		//					+ " where ROLE_ID_FK = "+returnGroupMappingRequest.getRoleId()+" and ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1 "
		////					+ " and retRegMap.IS_ACTIVE = 1 "
		//					+ "and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 and ret.FREQUENCY_ID_FK = "+returnGroupMappingRequest.getFrequencyId()+" and roleReturn.RETURN_ID_FK = ret.RETURN_ID "
		//					+ " and groupLable.RETURN_GROUP_MAP_ID_FK = ret.RETURN_GROUP_MAP_ID_FK and reg.REGULATOR_ID = regulatorLable.REGULATOR_ID_FK "
		//					+ " and retGroup.RETURN_GROUP_MAP_ID = groupLable.RETURN_GROUP_MAP_ID_FK and retLabel.RETURN_ID_FK = ret.RETURN_ID and ret.FREQUENCY_ID_FK = freq.FREQUENCY_ID "
		//					+ " and retRegMap.RETURN_ID_FK = ret.RETURN_ID and ret.RETURN_ID = retRegMap.RETURN_ID_FK  and retRegMap.REGULATOR_ID_FK = regulatorLable.REGULATOR_ID_FK "
		//					+ " order by GROUP_LABEL, RETURN_LABEL, RETURN_TYPE_ID asc");
		//		}else {
		//			stringBuilder = new StringBuilder("SELECT roleReturn.ROLE_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, "
		//					+ " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, "
		//					+ " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID "
		//					+ " FROM TBL_USER_ROLE_RETURN_MAPPING roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join "
		//					+ " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join "
		//					+ " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID"
		//					+ " where ROLE_ID_FK = "+returnGroupMappingRequest.getRoleId()+" and ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1 "
		////					+ " and retRegMap.IS_ACTIVE = 1 "
		//					+ "and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 and roleReturn.RETURN_ID_FK = ret.RETURN_ID "
		//					+ " and groupLable.RETURN_GROUP_MAP_ID_FK = ret.RETURN_GROUP_MAP_ID_FK and reg.REGULATOR_ID = regulatorLable.REGULATOR_ID_FK "
		//					+ " and retGroup.RETURN_GROUP_MAP_ID = groupLable.RETURN_GROUP_MAP_ID_FK and retLabel.RETURN_ID_FK = ret.RETURN_ID and ret.FREQUENCY_ID_FK = freq.FREQUENCY_ID "
		//					+ " and retRegMap.RETURN_ID_FK = ret.RETURN_ID and ret.RETURN_ID = retRegMap.RETURN_ID_FK  and retRegMap.REGULATOR_ID_FK = regulatorLable.REGULATOR_ID_FK "
		//					+ " order by GROUP_LABEL, RETURN_LABEL, RETURN_TYPE_ID asc");
		//		}

		List<ReturnGroupMappingDto> returnGroupMappingDtoList = new ArrayList<>();

		Query query = enttyManager.createNativeQuery(stringBuilder.toString(), Tuple.class);

		if (returnGroupMappingRequest.getReturnGroupIds() != null) {
			query.setParameter("returnGroupIds", returnGroupMappingRequest.getReturnGroupIds());
		}

		if (returnGroupMappingRequest.getFrequencyId() != null) {
			query.setParameter("frequencyId", returnGroupMappingRequest.getFrequencyId());
		}

		List<Tuple> result = query.getResultList();

		Map<Long, ReturnGroupMappingDto> returnGroupAndReturnMap = new HashMap<>();

		Boolean returnGroupActive = null;
		Boolean freqActive = null;
		Boolean isParent = null;
		Boolean returnActive = null;
		Long returnTypeId = null;

		for (Tuple tuple : result) {
			if ((tuple.get("RET_GROUP_ACTIVE") + "").equals("1")) {
				returnGroupActive = Boolean.TRUE;
			} else {
				returnGroupActive = Boolean.FALSE;
			}

			if (tuple.get("RETURN_TYPE_ID") != null) {
				returnTypeId = ((Integer) tuple.get("RETURN_TYPE_ID")).longValue();
			}

			if ((tuple.get("FREQ_ACTIVE") + "").equals("1")) {
				freqActive = Boolean.TRUE;
			} else {
				freqActive = Boolean.FALSE;
			}

			if ((tuple.get("IS_PARENT") + "").equals("1")) {
				isParent = Boolean.TRUE;
			} else {
				isParent = Boolean.FALSE;
			}

			if ((tuple.get("RET_ACTIVE") + "").equals("1")) {
				isParent = Boolean.TRUE;
			} else {
				isParent = Boolean.FALSE;
			}

			ReturnGroupMappingDto returnGroupMappingDto = new ReturnGroupMappingDto();
			returnGroupMappingDto.setReturnGroupMapId(((Integer) tuple.get("RETURN_GROUP_MAP_ID")).longValue());
			returnGroupMappingDto.setDefaultGroupName((String) tuple.get("GROUP_LABEL"));
			returnGroupMappingDto.setIsActive(returnGroupActive);
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(((Integer) tuple.get("CREATED_BY_FK")).longValue());
			returnGroupMappingDto.setCreatedBy(userMaster);

			ReturnDto returnDto = new ReturnDto();
			returnDto.setReturnId(((Integer) tuple.get("RETURN_ID")).longValue());
			returnDto.setReturnName((String) tuple.get("RETURN_LABEL"));
			returnDto.setReturnCode((String) tuple.get("RETURN_CODE"));
			returnDto.setIsParent(isParent);
			returnDto.setIsActive(returnActive);

			Frequency frequency = new Frequency();
			frequency.setFrequencyId(((Integer) tuple.get("FREQUENCY_ID")).longValue());
			frequency.setFrequencyName((String) tuple.get("FREQUENCY_NAME"));
			frequency.setDescription((String) tuple.get("DESCRIPTION"));
			frequency.setIsActive(freqActive);
			returnDto.setFrequency(frequency);

			Regulator regulator = new Regulator();
			regulator.setRegulatorId(((Integer) tuple.get("REGULATOR_ID")).longValue());
			regulator.setRegulatorName((String) tuple.get("REGULATOR_LABEL"));
			returnDto.setRegulator(regulator);

			if (returnGroupAndReturnMap.containsKey(returnGroupMappingDto.getReturnGroupMapId())) {
				ReturnGroupMappingDto returnGrMappingDto = returnGroupAndReturnMap.get(returnGroupMappingDto.getReturnGroupMapId());

				List<ReturnDto> returnDtoList = returnGrMappingDto.getReturnList();
				ReturnDto listReturnDto = returnDtoList.stream().filter(f -> f.getReturnId().equals(((Integer) tuple.get("RETURN_ID")).longValue())).findAny().orElse(null);
				if (listReturnDto != null) {
					if (returnTypeId != null) {
						listReturnDto.getReturnTypeIds().add(returnTypeId);
					}
				} else {
					if (returnTypeId != null) {
						List<Long> returnTypes = new ArrayList<>();
						returnTypes.add(returnTypeId);
						returnDto.setReturnTypeIds(returnTypes);
					}
					returnDtoList.add(returnDto);
				}

				returnGrMappingDto.setReturnCount(returnDtoList.size());
			} else {
				List<ReturnDto> returnDtoList = new ArrayList<>();

				if (returnTypeId != null) {
					List<Long> returnTypes = new ArrayList<>();
					returnTypes.add(returnTypeId);
					returnDto.setReturnTypeIds(returnTypes);
				}

				returnDtoList.add(returnDto);
				returnGroupAndReturnMap.put(returnGroupMappingDto.getReturnGroupMapId(), returnGroupMappingDto);

				returnGroupMappingDto.setReturnList(returnDtoList);
				returnGroupMappingDto.setReturnCount(returnDtoList.size());
				returnGroupMappingDtoList.add(returnGroupMappingDto);
			}
		}

		return returnGroupMappingDtoList;
	}

	public List<ReturnGroupMappingDto> prepareReturnGroupMappingListUserModified(ReturnGroupMappingRequest returnGroupMappingRequest) {
		StringBuilder stringBuilder;
		UserMaster userMasterObj = null;
		userMasterObj = userMasterService.getDataById(returnGroupMappingRequest.getUserId());
		List<Long> userRoleIds = new ArrayList<>();

		if (userMasterObj != null && userMasterObj.getDepartmentIdFk() != null && userMasterObj.getDepartmentIdFk().getIsMaster() && returnGroupMappingRequest.getRoleId().equals(GeneralConstants.RBI_SUPER_USER.getConstantLongVal())) {
			stringBuilder = new StringBuilder("SELECT roleReturn.ROLE_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, " + " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, " + " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID " + " FROM TBL_USER_ROLE_RETURN_MAPPING roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join " + " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join " + " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID" + " where ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1 and retTypeMap.IS_ACTIVE = 1 and retRegMap.IS_ACTIVE = 1 " + " and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 ");

		} else {

			Set<UserRoleMaster> usrRoleMstrSet = userMasterObj.getUsrRoleMstrSet();
			for (UserRoleMaster userRoleMastObj : usrRoleMstrSet) {
				if (userRoleMastObj.getIsActive()) {
					userRoleIds.add(userRoleMastObj.getUserRole().getUserRoleId());
				}
			}
			stringBuilder = new StringBuilder("SELECT roleReturn.ROLE_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, " + " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, " + " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID " + " FROM TBL_USER_ROLE_RETURN_MAPPING roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join " + " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join " + " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID" + " where ROLE_ID_FK in (:userRoleIdList) and ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retTypeMap.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1" + " and retRegMap.IS_ACTIVE = 1 " + " and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 ");
		}

		if (returnGroupMappingRequest.getFrequencyId() != null) {
			stringBuilder.append("and ret.FREQUENCY_ID_FK =:frequencyId ");
		}

		if (returnGroupMappingRequest.getReturnGroupIds() != null) {
			stringBuilder.append(" and ret.RETURN_GROUP_MAP_ID_FK in (:returnGroupIds)");
		}

		stringBuilder.append("and roleReturn.RETURN_ID_FK = ret.RETURN_ID " + " and groupLable.RETURN_GROUP_MAP_ID_FK = ret.RETURN_GROUP_MAP_ID_FK and reg.REGULATOR_ID = regulatorLable.REGULATOR_ID_FK " + " and retGroup.RETURN_GROUP_MAP_ID = groupLable.RETURN_GROUP_MAP_ID_FK and retLabel.RETURN_ID_FK = ret.RETURN_ID and ret.FREQUENCY_ID_FK = freq.FREQUENCY_ID " + " and retRegMap.RETURN_ID_FK = ret.RETURN_ID and ret.RETURN_ID = retRegMap.RETURN_ID_FK  and retRegMap.REGULATOR_ID_FK = regulatorLable.REGULATOR_ID_FK " + " order by GROUP_LABEL, RETURN_LABEL, RETURN_TYPE_ID asc");

		//		if(returnGroupMappingRequest.getFrequencyId()!= null) {
		//			stringBuilder = new StringBuilder("SELECT roleReturn.ROLE_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, "
		//					+ " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, "
		//					+ " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID "
		//					+ " FROM TBL_USER_ROLE_RETURN_MAPPING roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join "
		//					+ " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join "
		//					+ " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID"
		//					+ " where ROLE_ID_FK = "+returnGroupMappingRequest.getRoleId()+" and ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1 "
		////					+ " and retRegMap.IS_ACTIVE = 1 "
		//					+ "and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 and ret.FREQUENCY_ID_FK = "+returnGroupMappingRequest.getFrequencyId()+" and roleReturn.RETURN_ID_FK = ret.RETURN_ID "
		//					+ " and groupLable.RETURN_GROUP_MAP_ID_FK = ret.RETURN_GROUP_MAP_ID_FK and reg.REGULATOR_ID = regulatorLable.REGULATOR_ID_FK "
		//					+ " and retGroup.RETURN_GROUP_MAP_ID = groupLable.RETURN_GROUP_MAP_ID_FK and retLabel.RETURN_ID_FK = ret.RETURN_ID and ret.FREQUENCY_ID_FK = freq.FREQUENCY_ID "
		//					+ " and retRegMap.RETURN_ID_FK = ret.RETURN_ID and ret.RETURN_ID = retRegMap.RETURN_ID_FK  and retRegMap.REGULATOR_ID_FK = regulatorLable.REGULATOR_ID_FK "
		//					+ " order by GROUP_LABEL, RETURN_LABEL, RETURN_TYPE_ID asc");
		//		}else {
		//			stringBuilder = new StringBuilder("SELECT roleReturn.ROLE_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, "
		//					+ " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, "
		//					+ " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID "
		//					+ " FROM TBL_USER_ROLE_RETURN_MAPPING roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join "
		//					+ " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join "
		//					+ " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID"
		//					+ " where ROLE_ID_FK = "+returnGroupMappingRequest.getRoleId()+" and ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1 "
		////					+ " and retRegMap.IS_ACTIVE = 1 "
		//					+ "and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 and roleReturn.RETURN_ID_FK = ret.RETURN_ID "
		//					+ " and groupLable.RETURN_GROUP_MAP_ID_FK = ret.RETURN_GROUP_MAP_ID_FK and reg.REGULATOR_ID = regulatorLable.REGULATOR_ID_FK "
		//					+ " and retGroup.RETURN_GROUP_MAP_ID = groupLable.RETURN_GROUP_MAP_ID_FK and retLabel.RETURN_ID_FK = ret.RETURN_ID and ret.FREQUENCY_ID_FK = freq.FREQUENCY_ID "
		//					+ " and retRegMap.RETURN_ID_FK = ret.RETURN_ID and ret.RETURN_ID = retRegMap.RETURN_ID_FK  and retRegMap.REGULATOR_ID_FK = regulatorLable.REGULATOR_ID_FK "
		//					+ " order by GROUP_LABEL, RETURN_LABEL, RETURN_TYPE_ID asc");
		//		}

		List<ReturnGroupMappingDto> returnGroupMappingDtoList = new ArrayList<>();

		Query query = enttyManager.createNativeQuery(stringBuilder.toString(), Tuple.class);
		if (!CollectionUtils.isEmpty(userRoleIds)) {
			query.setParameter("userRoleIdList", userRoleIds);

		}
		if (returnGroupMappingRequest.getReturnGroupIds() != null) {
			query.setParameter("returnGroupIds", returnGroupMappingRequest.getReturnGroupIds());
		}

		if (returnGroupMappingRequest.getFrequencyId() != null) {
			query.setParameter("frequencyId", returnGroupMappingRequest.getFrequencyId());
		}

		List<Tuple> result = query.getResultList();

		Map<Long, ReturnGroupMappingDto> returnGroupAndReturnMap = new HashMap<>();

		Boolean returnGroupActive = null;
		Boolean freqActive = null;
		Boolean isParent = null;
		Boolean returnActive = null;
		Long returnTypeId = null;

		for (Tuple tuple : result) {
			if ((tuple.get("RET_GROUP_ACTIVE") + "").equals("1")) {
				returnGroupActive = Boolean.TRUE;
			} else {
				returnGroupActive = Boolean.FALSE;
			}

			if (tuple.get("RETURN_TYPE_ID") != null) {
				returnTypeId = ((Integer) tuple.get("RETURN_TYPE_ID")).longValue();
			}

			if ((tuple.get("FREQ_ACTIVE") + "").equals("1")) {
				freqActive = Boolean.TRUE;
			} else {
				freqActive = Boolean.FALSE;
			}

			if ((tuple.get("IS_PARENT") + "").equals("1")) {
				isParent = Boolean.TRUE;
			} else {
				isParent = Boolean.FALSE;
			}

			if ((tuple.get("RET_ACTIVE") + "").equals("1")) {
				isParent = Boolean.TRUE;
			} else {
				isParent = Boolean.FALSE;
			}

			ReturnGroupMappingDto returnGroupMappingDto = new ReturnGroupMappingDto();
			returnGroupMappingDto.setReturnGroupMapId(((Integer) tuple.get("REGULATOR_ID")).longValue());
			returnGroupMappingDto.setDefaultGroupName((String) tuple.get("REGULATOR_LABEL"));
			returnGroupMappingDto.setIsActive(returnGroupActive);
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(((Integer) tuple.get("CREATED_BY_FK")).longValue());
			returnGroupMappingDto.setCreatedBy(userMaster);

			ReturnDto returnDto = new ReturnDto();
			returnDto.setReturnId(((Integer) tuple.get("RETURN_ID")).longValue());
			returnDto.setReturnName((String) tuple.get("RETURN_LABEL"));
			returnDto.setReturnCode((String) tuple.get("RETURN_CODE"));
			returnDto.setIsParent(isParent);
			returnDto.setIsActive(returnActive);

			Frequency frequency = new Frequency();
			frequency.setFrequencyId(((Integer) tuple.get("FREQUENCY_ID")).longValue());
			frequency.setFrequencyName((String) tuple.get("FREQUENCY_NAME"));
			frequency.setDescription((String) tuple.get("DESCRIPTION"));
			frequency.setIsActive(freqActive);
			returnDto.setFrequency(frequency);

			Regulator regulator = new Regulator();
			regulator.setRegulatorId(((Integer) tuple.get("REGULATOR_ID")).longValue());
			regulator.setRegulatorName((String) tuple.get("REGULATOR_LABEL"));
			returnDto.setRegulator(regulator);

			if (returnGroupAndReturnMap.containsKey(returnGroupMappingDto.getReturnGroupMapId())) {
				ReturnGroupMappingDto returnGrMappingDto = returnGroupAndReturnMap.get(returnGroupMappingDto.getReturnGroupMapId());

				List<ReturnDto> returnDtoList = returnGrMappingDto.getReturnList();
				ReturnDto listReturnDto = returnDtoList.stream().filter(f -> f.getReturnId().equals(((Integer) tuple.get("RETURN_ID")).longValue())).findAny().orElse(null);
				if (listReturnDto != null) {
					if (returnTypeId != null) {
						listReturnDto.getReturnTypeIds().add(returnTypeId);
					}
				} else {
					if (returnTypeId != null) {
						List<Long> returnTypes = new ArrayList<>();
						returnTypes.add(returnTypeId);
						returnDto.setReturnTypeIds(returnTypes);
					}
					returnDtoList.add(returnDto);
				}

				returnGrMappingDto.setReturnCount(returnDtoList.size());
			} else {
				List<ReturnDto> returnDtoList = new ArrayList<>();

				if (returnTypeId != null) {
					List<Long> returnTypes = new ArrayList<>();
					returnTypes.add(returnTypeId);
					returnDto.setReturnTypeIds(returnTypes);
				}

				returnDtoList.add(returnDto);
				returnGroupAndReturnMap.put(returnGroupMappingDto.getReturnGroupMapId(), returnGroupMappingDto);

				returnGroupMappingDto.setReturnList(returnDtoList);
				returnGroupMappingDto.setReturnCount(returnDtoList.size());
				returnGroupMappingDtoList.add(returnGroupMappingDto);
			}
		}

		return returnGroupMappingDtoList;
	}

	public List<ReturnGroupMappingDto> prepareReturnGroupMappingListEntUserModified(ReturnGroupMappingRequest returnGroupMappingRequest) {
		StringBuilder stringBuilder;

		stringBuilder = new StringBuilder("SELECT roleReturn.ENTITY_ID_FK, retGroup.RETURN_GROUP_MAP_ID, groupLable.GROUP_LABEL, retGroup.IS_ACTIVE as RET_GROUP_ACTIVE, retGroup.CREATED_BY_FK, ret.RETURN_ID, ret.RETURN_CODE, retLabel.RETURN_LABEL, ret.IS_PARENT, " + " ret.IS_ACTIVE as RET_ACTIVE, freq.FREQUENCY_ID, freq.FREQUENCY_NAME, freq.DESCRIPTION, freq.IS_ACTIVE as FREQ_ACTIVE, reg.REGULATOR_ID, " + " regulatorLable.REGULATOR_LABEL, retTypeMap.RETURN_TYPE_ID " + " FROM TBL_RETURN_ENTITY_MAPP_NEW roleReturn inner join  TBL_FREQUENCY freq inner join TBL_RETURN_REGULATOR_MAPPING retRegMap inner join TBL_REGULATOR reg inner join TBL_REGULATOR_LABEL regulatorLable inner join " + " TBL_RETURN_LABEL retLabel inner join TBL_RETURN_GROUP_MAPPING retGroup inner join TBL_RETURN_GROUP_LABEL_MAP groupLable inner join " + " TBL_RETURN ret left join TBL_RETURN_RETURN_TYPE_MAPPING retTypeMap on retTypeMap.RETURN_ID = ret.RETURN_ID" + " where ENTITY_ID_FK = :entityId and ret.RETURN_GROUP_MAP_ID_FK is not null and roleReturn.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and reg.IS_ACTIVE = 1 and retGroup.IS_ACTIVE = 1" + " and retRegMap.IS_ACTIVE = 1 " + " and groupLable.LANG_ID_FK = 15 and retLabel.LANG_ID_FK = 15  and regulatorLable.LANGUAGE_ID_FK = 15 and retTypeMap.IS_ACTIVE = 1 ");

		if (returnGroupMappingRequest.getFrequencyId() != null) {
			stringBuilder.append("and ret.FREQUENCY_ID_FK =:frequencyId ");
		}

		if (returnGroupMappingRequest.getReturnGroupIds() != null) {
			stringBuilder.append(" and ret.RETURN_GROUP_MAP_ID_FK in (:returnGroupIds)");
		}

		stringBuilder.append("and roleReturn.RETURN_ID_FK = ret.RETURN_ID " + " and groupLable.RETURN_GROUP_MAP_ID_FK = ret.RETURN_GROUP_MAP_ID_FK and reg.REGULATOR_ID = regulatorLable.REGULATOR_ID_FK " + " and retGroup.RETURN_GROUP_MAP_ID = groupLable.RETURN_GROUP_MAP_ID_FK and retLabel.RETURN_ID_FK = ret.RETURN_ID and ret.FREQUENCY_ID_FK = freq.FREQUENCY_ID " + " and retRegMap.RETURN_ID_FK = ret.RETURN_ID and ret.RETURN_ID = retRegMap.RETURN_ID_FK  and retRegMap.REGULATOR_ID_FK = regulatorLable.REGULATOR_ID_FK " + " order by GROUP_LABEL, RETURN_LABEL, RETURN_TYPE_ID asc");

		List<ReturnGroupMappingDto> returnGroupMappingDtoList = new ArrayList<>();

		Query query = enttyManager.createNativeQuery(stringBuilder.toString(), Tuple.class);

		if (returnGroupMappingRequest.getReturnGroupIds() != null) {
			query.setParameter("returnGroupIds", returnGroupMappingRequest.getReturnGroupIds());
		}

		if (returnGroupMappingRequest.getFrequencyId() != null) {
			query.setParameter("frequencyId", returnGroupMappingRequest.getFrequencyId());
		}

		query.setParameter("entityId", returnGroupMappingRequest.getEntityId());

		List<Tuple> result = query.getResultList();

		Map<Long, ReturnGroupMappingDto> returnGroupAndReturnMap = new HashMap<>();

		Boolean returnGroupActive = null;
		Boolean freqActive = null;
		Boolean isParent = null;
		Boolean returnActive = null;
		Long returnTypeId = null;

		for (Tuple tuple : result) {
			if ((tuple.get("RET_GROUP_ACTIVE") + "").equals("1")) {
				returnGroupActive = Boolean.TRUE;
			} else {
				returnGroupActive = Boolean.FALSE;
			}

			if (tuple.get("RETURN_TYPE_ID") != null) {
				returnTypeId = ((Integer) tuple.get("RETURN_TYPE_ID")).longValue();
			}

			if ((tuple.get("FREQ_ACTIVE") + "").equals("1")) {
				freqActive = Boolean.TRUE;
			} else {
				freqActive = Boolean.FALSE;
			}

			if ((tuple.get("IS_PARENT") + "").equals("1")) {
				isParent = Boolean.TRUE;
			} else {
				isParent = Boolean.FALSE;
			}

			if ((tuple.get("RET_ACTIVE") + "").equals("1")) {
				isParent = Boolean.TRUE;
			} else {
				isParent = Boolean.FALSE;
			}

			ReturnGroupMappingDto returnGroupMappingDto = new ReturnGroupMappingDto();
			returnGroupMappingDto.setReturnGroupMapId(((Integer) tuple.get("REGULATOR_ID")).longValue());
			returnGroupMappingDto.setDefaultGroupName((String) tuple.get("REGULATOR_LABEL"));
			returnGroupMappingDto.setIsActive(returnGroupActive);
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(((Integer) tuple.get("CREATED_BY_FK")).longValue());
			returnGroupMappingDto.setCreatedBy(userMaster);

			ReturnDto returnDto = new ReturnDto();
			returnDto.setReturnId(((Integer) tuple.get("RETURN_ID")).longValue());
			returnDto.setReturnName((String) tuple.get("RETURN_LABEL"));
			returnDto.setReturnCode((String) tuple.get("RETURN_CODE"));
			returnDto.setIsParent(isParent);
			returnDto.setIsActive(returnActive);

			Frequency frequency = new Frequency();
			frequency.setFrequencyId(((Integer) tuple.get("FREQUENCY_ID")).longValue());
			frequency.setFrequencyName((String) tuple.get("FREQUENCY_NAME"));
			frequency.setDescription((String) tuple.get("DESCRIPTION"));
			frequency.setIsActive(freqActive);
			returnDto.setFrequency(frequency);

			Regulator regulator = new Regulator();
			regulator.setRegulatorId(((Integer) tuple.get("REGULATOR_ID")).longValue());
			regulator.setRegulatorName((String) tuple.get("REGULATOR_LABEL"));
			returnDto.setRegulator(regulator);

			if (returnGroupAndReturnMap.containsKey(returnGroupMappingDto.getReturnGroupMapId())) {
				ReturnGroupMappingDto returnGrMappingDto = returnGroupAndReturnMap.get(returnGroupMappingDto.getReturnGroupMapId());

				List<ReturnDto> returnDtoList = returnGrMappingDto.getReturnList();
				ReturnDto listReturnDto = returnDtoList.stream().filter(f -> f.getReturnId().equals(((Integer) tuple.get("RETURN_ID")).longValue())).findAny().orElse(null);
				if (listReturnDto != null) {
					if (returnTypeId != null) {
						listReturnDto.getReturnTypeIds().add(returnTypeId);
					}
				} else {
					if (returnTypeId != null) {
						List<Long> returnTypes = new ArrayList<>();
						returnTypes.add(returnTypeId);
						returnDto.setReturnTypeIds(returnTypes);
					}
					returnDtoList.add(returnDto);
				}

				returnGrMappingDto.setReturnCount(returnDtoList.size());
			} else {
				List<ReturnDto> returnDtoList = new ArrayList<>();

				if (returnTypeId != null) {
					List<Long> returnTypes = new ArrayList<>();
					returnTypes.add(returnTypeId);
					returnDto.setReturnTypeIds(returnTypes);
				}

				returnDtoList.add(returnDto);
				returnGroupAndReturnMap.put(returnGroupMappingDto.getReturnGroupMapId(), returnGroupMappingDto);

				returnGroupMappingDto.setReturnList(returnDtoList);
				returnGroupMappingDto.setReturnCount(returnDtoList.size());
				returnGroupMappingDtoList.add(returnGroupMappingDto);
			}
		}

		return returnGroupMappingDtoList;
	}

}
