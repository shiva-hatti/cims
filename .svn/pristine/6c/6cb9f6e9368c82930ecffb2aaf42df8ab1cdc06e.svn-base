package com.iris.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.iris.dto.ReturnEntityDto;
import com.iris.dto.ReturnEntityFreqDto;
import com.iris.dto.ReturnEntityGroupDto;
import com.iris.dto.ReturnEntityQueryOutputDto;
import com.iris.dto.ReturnGroupOutputDto;

@Service
public class ReturnGroupControllerServiceV2 {
	/**
	 * @param returnEntityMappingNewList
	 * @param returnEntityOutputDtoList
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ReturnGroupOutputDto> convertResultToOutputBean(List<ReturnEntityQueryOutputDto> returnEntityQueryOutputDtoList) {

		Map<String, ReturnGroupOutputDto> returnGroupOutputDtoMap = new HashMap<>();

		for (ReturnEntityQueryOutputDto returnEntityQueryOutputDto : returnEntityQueryOutputDtoList) {
			if (returnGroupOutputDtoMap.containsKey(returnEntityQueryOutputDto.getReturnCode())) {
				ReturnGroupOutputDto returnEntityOutputDto = returnGroupOutputDtoMap.get(returnEntityQueryOutputDto.getReturnCode());
				Set<ReturnEntityDto> returnEntityDtoSet = returnEntityOutputDto.getReturnEntityDtoSet();
				Boolean isEntityExist = false;
				for (ReturnEntityDto returnEntityDto : returnEntityDtoSet) {
					if (returnEntityDto.getEntityCode().equalsIgnoreCase(returnEntityQueryOutputDto.getEntityCode())) {
						isEntityExist = true;
					}
				}
				if (!isEntityExist) {
					ReturnEntityDto returnEntityDto = new ReturnEntityDto();
					returnEntityDto.setEntityCode(returnEntityQueryOutputDto.getEntityCode());
					returnEntityDto.setEntityName(returnEntityQueryOutputDto.getEntityName());
					returnEntityDtoSet.add(returnEntityDto);
					returnEntityOutputDto.setReturnEntityDtoSet(returnEntityDtoSet);
					returnGroupOutputDtoMap.put(returnEntityQueryOutputDto.getReturnCode(), returnEntityOutputDto);
				}
			} else {
				ReturnGroupOutputDto returnGroupOutputDto = new ReturnGroupOutputDto();

				// return detail
				returnGroupOutputDto.setReturnCode(returnEntityQueryOutputDto.getReturnCode());
				returnGroupOutputDto.setReturnName(returnEntityQueryOutputDto.getReturnName());
				returnGroupOutputDto.setReturnId(returnEntityQueryOutputDto.getReturnId());

				// entity detail
				Set<ReturnEntityDto> returnEntityDtoSet = new HashSet<>();
				ReturnEntityDto returnEntityDto = new ReturnEntityDto();
				returnEntityDto.setEntityCode(returnEntityQueryOutputDto.getEntityCode());
				returnEntityDto.setEntityName(returnEntityQueryOutputDto.getEntityName());
				if (!StringUtils.isEmpty(returnEntityQueryOutputDto.getSubCategoryCode())) {
					returnEntityDto.setSubCategoryCode(returnEntityQueryOutputDto.getSubCategoryCode());
					returnEntityDto.setSubCategoryName(returnEntityQueryOutputDto.getSubCategoryName());
				}
				returnEntityDtoSet.add(returnEntityDto);
				returnGroupOutputDto.setReturnEntityDtoSet(returnEntityDtoSet);

				// Freq
				if (!StringUtils.isBlank(returnEntityQueryOutputDto.getFreqCode())) {
					ReturnEntityFreqDto returnEntityFreqDto = new ReturnEntityFreqDto();
					returnEntityFreqDto.setFreqCode(returnEntityQueryOutputDto.getFreqCode());
					returnEntityFreqDto.setFreqName(returnEntityQueryOutputDto.getFreqName());
					returnGroupOutputDto.setReturnEntityFreqDto(returnEntityFreqDto);
				}

				// Group
				if (returnEntityQueryOutputDto.getGroupId() != null) {
					ReturnEntityGroupDto returnEntityGroupDto = new ReturnEntityGroupDto();
					returnEntityGroupDto.setGroupId(returnEntityQueryOutputDto.getGroupId());
					returnEntityGroupDto.setGroupName(returnEntityQueryOutputDto.getGroupName());
					returnGroupOutputDto.setReturnEntityGroupDto(returnEntityGroupDto);
				}

				returnGroupOutputDtoMap.put(returnEntityQueryOutputDto.getReturnCode(), returnGroupOutputDto);
			}

		}
		return new ArrayList(returnGroupOutputDtoMap.values());

	}
}
