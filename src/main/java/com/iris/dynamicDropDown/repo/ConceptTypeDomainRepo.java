package com.iris.dynamicDropDown.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.controller.ConceptTypedDomain;

/**
 * @author BHAVANA
 *
 */
public interface ConceptTypeDomainRepo extends JpaRepository<ConceptTypedDomain, Long> {
	@Query("FROM ConceptTypedDomain where dropDownTypeIdFk.dropdownTypeId IN(:dropDownType) and isActive =1 and returnIdFk.returnId =:returnId")
	List<ConceptTypedDomain> findByActiveWithDropDownTypeId(@Param("dropDownType") List<Long> dropDownType, @Param("returnId") Long returnId);

	@Query("FROM ConceptTypedDomain where isActive =1 and returnIdFk.returnId =:returnId and elrTag is not null")
	List<ConceptTypedDomain> findByActiveDataByReturnId(@Param("returnId") Long returnId);

	@Query("FROM ConceptTypedDomain where elrTag LIKE %:elrTagList%  and concept LIKE %:conceptList%  and isActive =1 and returnIdFk.returnId =:returnId")
	List<ConceptTypedDomain> findByActiveDataByReturnIdElrListConceptList(@Param("returnId") Long returnId, @Param("elrTagList") String elrTagList, @Param("conceptList") String conceptList);

	@Query("FROM ConceptTypedDomain where elrTag LIKE %:elrTagList% and isActive =1 and returnIdFk.returnId =:returnId")
	List<ConceptTypedDomain> findByActiveDataByReturnIdElrList(@Param("returnId") Long returnId, @Param("elrTagList") String elrTagList);

	@Query("FROM ConceptTypedDomain where concept LIKE %:conceptList% and isActive =1 and returnIdFk.returnId =:returnId")
	List<ConceptTypedDomain> findByActiveDataByReturnIdConceptList(@Param("returnId") Long returnId, @Param("conceptList") String conceptList);

	@Query("FROM ConceptTypedDomain where isActive =1 and returnIdFk.returnId =:returnId")
	List<ConceptTypedDomain> findByAllActiveDataByReturnId(@Param("returnId") Long returnId);

	@Query("FROM ConceptTypedDomain where dropDownTypeIdFk.dropdownTypeId =:dropDownType and isActive =1 and returnIdFk.returnId =:returnId")
	List<ConceptTypedDomain> findByActiveWithDropDownType(@Param("dropDownType") Long dropDownType, @Param("returnId") Long returnId);
}
