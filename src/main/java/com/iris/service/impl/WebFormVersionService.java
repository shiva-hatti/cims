package com.iris.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.dto.HeaderInfoDto;
import com.iris.dto.ReturnTemplateDto;
import com.iris.dto.TableInfoDto;
import com.iris.dto.WebTaxVersionInfoDto;
import com.iris.exception.ServiceException;
import com.iris.model.DynamicHeader;
import com.iris.model.DynamicHeaderLabel;
import com.iris.model.Return;
import com.iris.model.ReturnSectionMap;
import com.iris.model.ReturnSectionMapLabel;
import com.iris.model.ReturnSectionVersionMap;
import com.iris.model.ReturnTemplate;
import com.iris.model.UserMaster;
import com.iris.model.WebformVersionMap;
import com.iris.repository.ReturnSectionVersionMapRepo;
import com.iris.repository.WebFormVersionRepo;
import com.iris.service.GenericService;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class WebFormVersionService implements GenericService<WebformVersionMap, Long> {

	@Autowired
	private ReturnSectionMapService returnSectionMapService;

	@Autowired
	private ReturnTemplateService taxonomyService;

	@Autowired
	private WebFormVersionRepo webFormVersionRepo;

	@Autowired
	private ReturnService returnService;

	@Autowired
	private ReturnSectionVersionMapRepo returnSectionVersionMapRepo;

	public List<HeaderInfoDto> getTableInfo(String returnCode, String langCode) {
		List<HeaderInfoDto> headerInfoDtoList = new ArrayList<>();
		try {
			Map<String, Object> columnValueMap = new HashMap<>();
			Return ret = getReturnByReturnCode(returnCode);
			columnValueMap.put(ColumnConstants.RETURNID.getConstantVal(), ret.getReturnId().toString());
			List<ReturnSectionMap> returnSectionMapList = returnSectionMapService.getDataByObject(columnValueMap, MethodConstants.GET_RETURN_TABLE_DATA.getConstantVal());
			Map<DynamicHeader, List<ReturnSectionMap>> groupByHeaderMap = returnSectionMapList.stream().collect(Collectors.groupingBy(ReturnSectionMap::getHeaderIdFk));
			HeaderInfoDto headerInfoDto = null;
			TableInfoDto tableInfoDto = null;
			List<TableInfoDto> tableInfoDtoList = null;
			for (Map.Entry<DynamicHeader, List<ReturnSectionMap>> entry : groupByHeaderMap.entrySet()) {
				if (entry.getKey().getIsHidden().booleanValue()) {
					continue;
				}
				headerInfoDto = new HeaderInfoDto();
				headerInfoDto.setDynamicHeaderId(entry.getKey().getHeaderId());
				headerInfoDto.setDynamicHeaderLabel(Validations.trimInput(getLangLabelHeader(entry.getKey().getDynamicHeaderLabel(), langCode)));
				tableInfoDtoList = new ArrayList<>();
				for (ReturnSectionMap returnSecMap : entry.getValue()) {
					tableInfoDto = new TableInfoDto();
					tableInfoDto.setReturnSecMapId(returnSecMap.getReturnSectionMapId());
					tableInfoDto.setReturnSecMapLabel(Validations.trimInput(getLangLabelTable(returnSecMap.getRetSecLabelSet(), langCode)));
					tableInfoDtoList.add(tableInfoDto);
				}
				Collections.sort(tableInfoDtoList, new Comparator<TableInfoDto>() {
					public int compare(final TableInfoDto object1, final TableInfoDto object2) {
						return object1.getReturnSecMapLabel().toLowerCase().compareTo(object2.getReturnSecMapLabel().toLowerCase());
					}
				});
				headerInfoDto.setTableInfoDtoList(tableInfoDtoList);
				headerInfoDtoList.add(headerInfoDto);
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return headerInfoDtoList;
	}

	public Return getReturnByReturnCode(String returnCode) {
		Map<String, List<String>> columnValueMap = new HashMap<>();
		List<String> valueList = new ArrayList<>();
		valueList.add(returnCode);
		columnValueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), valueList);

		List<Return> returnList = returnService.getDataByColumnValue(columnValueMap, MethodConstants.GET_RETURN_DATA_BY_CODE.getConstantVal());

		Return returnObj = null;
		if (!CollectionUtils.isEmpty(returnList)) {
			returnObj = returnList.get(0);
			return returnObj;
		}
		return null;
	}

	public WebTaxVersionInfoDto getTaxonomyByEndDate(Long returnId, String endDate, String dateFormat) {
		ReturnTemplate taxonomy = null;
		WebTaxVersionInfoDto webTaxVersionInfoDto = null;
		try {

			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueListReturn = new ArrayList<String>();
			valueListReturn.add(returnId.toString());
			List<String> valueListEndDate = new ArrayList<String>();
			valueListEndDate.add(endDate);
			List<String> valueListDateFormat = new ArrayList<String>();
			valueListDateFormat.add(dateFormat);
			valueMap.put(ColumnConstants.RETURNID.getConstantVal(), valueListReturn);
			valueMap.put(ColumnConstants.ENDDATE.getConstantVal(), valueListEndDate);
			valueMap.put(ColumnConstants.SESSION_DB_DATE_FORMAT.getConstantVal(), valueListDateFormat);
			taxonomy = taxonomyService.getDataByColumnValue(valueMap, MethodConstants.GET_TAXONOMY_BY_RETURN_ID_AND_FROM_DATE.getConstantVal()).stream().findFirst().orElse(null);

			if (taxonomy != null) {
				webTaxVersionInfoDto = new WebTaxVersionInfoDto();
				List<WebformVersionMap> webFormVersionMapList = webFormVersionRepo.findByTaxonomyIdFkReturnTemplateId(taxonomy.getReturnTemplateId());
				List<Long> returnSecMapIdsList = new ArrayList<Long>();
				for (WebformVersionMap webformVersionMapBean : webFormVersionMapList) {
					if (!webformVersionMapBean.isActive()) {
						continue;
					}
					returnSecMapIdsList.add(webformVersionMapBean.getRetSecMap().getReturnSectionMapId());
				}
				webTaxVersionInfoDto.setReturnSecMapIdsList(returnSecMapIdsList);
				webTaxVersionInfoDto.setVersionNo(taxonomy.getVersionNumber());
				webTaxVersionInfoDto.setFileName(taxonomy.getFormulaFileName());
				webTaxVersionInfoDto.setReturnTemplateId(taxonomy.getReturnTemplateId().intValue());
				//responseString = StringUtils.join(returnSecMapIdsList, GeneralConstants.COMMA.getConstantVal());
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return webTaxVersionInfoDto;
	}

	@Transactional(rollbackOn = Exception.class)
	public boolean addEditVersionMapping(String returnSecMapIds, ReturnTemplate taxonomy, UserMaster userMaser) {
		boolean flag = false;
		try {
			List<Long> returnSecMapIdList = Stream.of(returnSecMapIds.split(",")).map(Long::parseLong).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(returnSecMapIdList)) {
				Long id = returnSecMapIdList.stream().findFirst().orElse(null);
				if (id == null) {
					return flag;
				}
				ReturnSectionMap returnSecMap = returnSectionMapService.getDataById(id);
				Map<String, Object> columnValueMap = new HashMap<>();
				Return ret = getReturnByReturnCode(returnSecMap.getReturnIdFk().getReturnCode());
				columnValueMap.put(ColumnConstants.RETURNID.getConstantVal(), ret.getReturnId().toString());
				List<ReturnSectionMap> returnSectionMapList = returnSectionMapService.getDataByObject(columnValueMap, MethodConstants.GET_RETURN_TABLE_DATA.getConstantVal());

				for (ReturnSectionMap retSec : returnSectionMapList) {
					if (retSec.getHeaderIdFk().getIsHidden().booleanValue()) {
						returnSecMapIdList.add(retSec.getReturnSectionMapId());
					}
				}
			}

			//List<WebformVersionMap> webFormVersionMapList = webFormVersionRepo.findByTaxonomyIdFkTaxonomyId(taxonomyId);
			List<WebformVersionMap> webFormVersionMapList = webFormVersionRepo.findByTaxonomyIdFkReturnTemplateId(taxonomy.getReturnTemplateId());

			WebformVersionMap webformVersionMap = null;
			//Taxonomy taxonomy = taxonomyService.getDataById(taxonomyId);
			if (CollectionUtils.isEmpty(webFormVersionMapList)) {
				webFormVersionMapList = new ArrayList<>();
				for (Long retSecMapId : returnSecMapIdList) {
					webformVersionMap = new WebformVersionMap();
					webformVersionMap.setTaxonomyIdFk(taxonomy);
					webformVersionMap.setRetSecMap(returnSectionMapService.getDataById(retSecMapId));
					webformVersionMap.setActive(true);
					webformVersionMap.setCreatedBy(userMaser);
					webformVersionMap.setCreatedOn(new Date());
					webFormVersionMapList.add(webformVersionMap);
				}
			} else {
				List<Long> existingIdsList = new ArrayList<>();
				for (WebformVersionMap webFormVersionMapBean : webFormVersionMapList) {
					if (returnSecMapIdList.contains(webFormVersionMapBean.getRetSecMap().getReturnSectionMapId())) {
						webFormVersionMapBean.setActive(true);
						existingIdsList.add(webFormVersionMapBean.getRetSecMap().getReturnSectionMapId());
					} else {
						webFormVersionMapBean.setActive(false);
					}
					webFormVersionMapBean.setModifiedBy(userMaser);
					webFormVersionMapBean.setModifiedOn(new Date());
					webFormVersionRepo.save(webFormVersionMapBean);
				}
				List<Long> newIdsList = (List<Long>) org.apache.commons.collections4.CollectionUtils.subtract(returnSecMapIdList, existingIdsList);
				if (!CollectionUtils.isEmpty(newIdsList)) {
					webFormVersionMapList = new ArrayList<>();
				}
				for (Long retSecMapId : newIdsList) {
					webformVersionMap = new WebformVersionMap();
					webformVersionMap.setTaxonomyIdFk(taxonomy);
					webformVersionMap.setRetSecMap(returnSectionMapService.getDataById(retSecMapId));
					webformVersionMap.setActive(true);
					webformVersionMap.setCreatedBy(userMaser);
					webformVersionMap.setCreatedOn(new Date());
					webFormVersionMapList.add(webformVersionMap);
				}
			}
			flag = !CollectionUtils.isEmpty(webFormVersionRepo.saveAll(webFormVersionMapList));
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return flag;
	}

	public List<HeaderInfoDto> getVersionTableInfo(Long taxonomyId, String langCode) {
		List<HeaderInfoDto> headerInfoDtoList = new ArrayList<>();
		try {
			List<WebformVersionMap> webFormVersionMapList = webFormVersionRepo.findByTaxonomyIdFkReturnTemplateId(taxonomyId);

			if (!CollectionUtils.isEmpty(webFormVersionMapList)) {
				List<ReturnSectionMap> returnSectionMapList = webFormVersionMapList.stream().map(x -> x.getRetSecMap()).collect(Collectors.toList());
				Map<DynamicHeader, List<ReturnSectionMap>> groupByHeaderMap = returnSectionMapList.stream().collect(Collectors.groupingBy(ReturnSectionMap::getHeaderIdFk));
				HeaderInfoDto headerInfoDto = null;
				TableInfoDto tableInfoDto = null;
				List<TableInfoDto> tableInfoDtoList = null;
				for (Map.Entry<DynamicHeader, List<ReturnSectionMap>> entry : groupByHeaderMap.entrySet()) {
					if (entry.getKey().getIsHidden().booleanValue()) {
						continue;
					}
					headerInfoDto = new HeaderInfoDto();
					headerInfoDto.setDynamicHeaderId(entry.getKey().getHeaderId());
					headerInfoDto.setDynamicHeaderLabel(Validations.trimInput(getLangLabelHeader(entry.getKey().getDynamicHeaderLabel(), langCode)));
					tableInfoDtoList = new ArrayList<>();
					for (ReturnSectionMap returnSecMap : entry.getValue()) {
						tableInfoDto = new TableInfoDto();
						tableInfoDto.setReturnSecMapId(returnSecMap.getReturnSectionMapId());
						tableInfoDto.setReturnSecMapLabel(Validations.trimInput(getLangLabelTable(returnSecMap.getRetSecLabelSet(), langCode)));
						tableInfoDtoList.add(tableInfoDto);
					}
					Collections.sort(tableInfoDtoList, new Comparator<TableInfoDto>() {
						public int compare(final TableInfoDto object1, final TableInfoDto object2) {
							return object1.getReturnSecMapLabel().toLowerCase().compareTo(object2.getReturnSecMapLabel().toLowerCase());
						}

					});
					headerInfoDto.setTableInfoDtoList(tableInfoDtoList);
					headerInfoDtoList.add(headerInfoDto);
				}
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return headerInfoDtoList;
	}

	public String getLangLabelHeader(Set<DynamicHeaderLabel> dynaHeaderLabelList, String langCode) {
		for (DynamicHeaderLabel dynaHeaderLabel : dynaHeaderLabelList) {
			if (dynaHeaderLabel.getLangIdFk().getLanguageCode().equals(langCode)) {
				return dynaHeaderLabel.getHeaderLabel();
			}
		}

		DynamicHeaderLabel label = dynaHeaderLabelList.stream().findFirst().orElse(null);

		if (label != null) {
			return label.getHeaderIdFk().getHeaderDefaultName();
		}

		return null;
	}

	public String getLangLabelTable(Set<ReturnSectionMapLabel> returnSectionMapLabelList, String langCode) {
		for (ReturnSectionMapLabel returnSectionMapLabel : returnSectionMapLabelList) {
			if (returnSectionMapLabel.getLangIdFk().getLanguageCode().equals(langCode)) {
				return returnSectionMapLabel.getSectionHeadingLabel();
			}
		}
		return null;
	}

	@Override
	public WebformVersionMap add(WebformVersionMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(WebformVersionMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<WebformVersionMap> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebformVersionMap getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WebformVersionMap> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WebformVersionMap> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WebformVersionMap> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WebformVersionMap> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WebformVersionMap> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(WebformVersionMap bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public ReturnTemplateDto getMappedUnMappedTableInfo(String returnCode, String langCode, Long retTempId) {
		List<HeaderInfoDto> mappedHeaderInfoDtoList = new ArrayList<>();
		List<HeaderInfoDto> unMappedHeaderInfoDtoList = new ArrayList<>();
		ReturnTemplateDto returnTemplateDto = new ReturnTemplateDto();
		List<ReturnSectionMap> mappedReturnSectionMapList = new ArrayList<>();
		List<ReturnSectionMap> unMappedReturnSectionMapList = new ArrayList<>();
		try {
			Map<String, Object> columnValueMap = new HashMap<>();
			Return ret = getReturnByReturnCode(returnCode);
			columnValueMap.put(ColumnConstants.RETURNID.getConstantVal(), ret.getReturnId().toString());
			List<ReturnSectionMap> returnSectionMapList = returnSectionMapService.getDataByObject(columnValueMap, MethodConstants.GET_RETURN_TABLE_DATA.getConstantVal());
			List<ReturnSectionVersionMap> returnSectionVersionMapList = returnSectionVersionMapRepo.findByReturnTemplate(retTempId);
			for (ReturnSectionVersionMap returnSectionVersionMap : returnSectionVersionMapList) {
				for (ReturnSectionMap returnSectionMap : returnSectionMapList) {
					if (returnSectionMap.getReturnSectionMapId().equals(returnSectionVersionMap.getReturnSecIdFk().getReturnSectionMapId())) {
						mappedReturnSectionMapList.add(returnSectionMap);
					} else {
						unMappedReturnSectionMapList.add(returnSectionMap);
					}
				}
			}
			Map<DynamicHeader, List<ReturnSectionMap>> groupByHeaderMap = mappedReturnSectionMapList.stream().collect(Collectors.groupingBy(ReturnSectionMap::getHeaderIdFk));
			HeaderInfoDto headerInfoDto = null;
			TableInfoDto tableInfoDto = null;
			Set<Long> tableIdSet = new HashSet<>();
			List<TableInfoDto> tableInfoDtoList = null;
			for (Map.Entry<DynamicHeader, List<ReturnSectionMap>> entry : groupByHeaderMap.entrySet()) {
				if (entry.getKey().getIsHidden().booleanValue()) {
					continue;
				}
				headerInfoDto = new HeaderInfoDto();
				headerInfoDto.setDynamicHeaderId(entry.getKey().getHeaderId());
				headerInfoDto.setDynamicHeaderLabel(Validations.trimInput(getLangLabelHeader(entry.getKey().getDynamicHeaderLabel(), langCode)));
				tableInfoDtoList = new ArrayList<>();
				for (ReturnSectionMap returnSecMap : entry.getValue()) {
					if (!tableIdSet.contains(returnSecMap.getReturnSectionMapId())) {
						tableInfoDto = new TableInfoDto();
						tableInfoDto.setReturnSecMapId(returnSecMap.getReturnSectionMapId());
						tableInfoDto.setReturnSecMapLabel(Validations.trimInput(getLangLabelTable(returnSecMap.getRetSecLabelSet(), langCode)));
						tableInfoDtoList.add(tableInfoDto);
					}
					tableIdSet.add(returnSecMap.getReturnSectionMapId());
				}
				Collections.sort(tableInfoDtoList, new Comparator<TableInfoDto>() {
					public int compare(final TableInfoDto object1, final TableInfoDto object2) {
						return object1.getReturnSecMapLabel().toLowerCase().compareTo(object2.getReturnSecMapLabel().toLowerCase());
					}
				});
				headerInfoDto.setTableInfoDtoList(tableInfoDtoList);
				mappedHeaderInfoDtoList.add(headerInfoDto);
			}
			tableIdSet = new HashSet<>();
			groupByHeaderMap = unMappedReturnSectionMapList.stream().collect(Collectors.groupingBy(ReturnSectionMap::getHeaderIdFk));
			for (Map.Entry<DynamicHeader, List<ReturnSectionMap>> entry : groupByHeaderMap.entrySet()) {
				if (entry.getKey().getIsHidden().booleanValue()) {
					continue;
				}
				headerInfoDto = new HeaderInfoDto();
				headerInfoDto.setDynamicHeaderId(entry.getKey().getHeaderId());
				headerInfoDto.setDynamicHeaderLabel(Validations.trimInput(getLangLabelHeader(entry.getKey().getDynamicHeaderLabel(), langCode)));
				tableInfoDtoList = new ArrayList<>();
				for (ReturnSectionMap returnSecMap : entry.getValue()) {
					if (!tableIdSet.contains(returnSecMap.getReturnSectionMapId())) {
						tableInfoDto = new TableInfoDto();
						tableInfoDto.setReturnSecMapId(returnSecMap.getReturnSectionMapId());
						tableInfoDto.setReturnSecMapLabel(Validations.trimInput(getLangLabelTable(returnSecMap.getRetSecLabelSet(), langCode)));
						tableInfoDtoList.add(tableInfoDto);
					}
					tableIdSet.add(returnSecMap.getReturnSectionMapId());
				}
				Collections.sort(tableInfoDtoList, new Comparator<TableInfoDto>() {
					public int compare(final TableInfoDto object1, final TableInfoDto object2) {
						return object1.getReturnSecMapLabel().toLowerCase().compareTo(object2.getReturnSecMapLabel().toLowerCase());
					}
				});
				headerInfoDto.setTableInfoDtoList(tableInfoDtoList);
				unMappedHeaderInfoDtoList.add(headerInfoDto);
			}
			returnTemplateDto.setMappedHeaderInfoList(mappedHeaderInfoDtoList);
			returnTemplateDto.setUnMappedHeaderInfoList(unMappedHeaderInfoDtoList);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return returnTemplateDto;
	}
}
