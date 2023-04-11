package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.AutoCalculationFormula;

public interface AutoCalculationFormulaRepo extends JpaRepository<AutoCalculationFormula, Long> {

	List<AutoCalculationFormula> findByReturnIdFkReturnIdAndIsActiveTrue(Long returnId);

	AutoCalculationFormula findByAutoCalVesrionReturnTemplateFkReturnTemplateId(Long returnTemplateId);

	@Query(value = "SELECT * FROM TBL_AUTO_CALCULATION_FORMULA where IS_ACTIVE = '1' and RETURN_ID_FK =:returnId order by AUTO_CAL_FORMULA_ID desc LIMIT 1", nativeQuery = true)
	AutoCalculationFormula findByReturnIdFkReturnIdAndIsActiveTrueAndOrderByautoCalFormulaId(Long returnId);

	@Query("FROM AutoCalculationFormula where isActive = '1' and autoCalFormulaId=:autoCalFormulaId")
	AutoCalculationFormula findByAutoCalFormulaIdAutoCalFormulaIdAndIsActiveTrue(@Param("autoCalFormulaId") Long returnTemplateId);

	@Query("SELECT new com.iris.model.AutoCalculationFormula(a.autoCalFormulaId, a.formulaJson, a.crossElrJson)  " + " FROM AutoCalculationFormula a, AutoCalVersionMap b, ReturnTemplate c " + " where c.isActive = '1' and c.saveFormulaAsDraft = '0' and b.returnTempIdFk.returnTemplateId = c.returnTemplateId " + " and a.returnIdFk.returnId=:returnId  and  b.autoCalFormulaFk = a.autoCalFormulaId " + " and a.isActive = '1' and b.isActive = '1' order by a.autoCalFormulaId desc")
	List<AutoCalculationFormula> getDataByReturn(@Param("returnId") Long returnId);
}