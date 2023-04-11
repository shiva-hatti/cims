package com.iris.formula.gen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FormulaGenRepo extends JpaRepository<FormulaGen, Long> {

	FormulaGen getDataByFormulaId(Long formulaId);

	@Query("FROM FormulaGen where returnTemplateIdFk.returnTemplateId =:returnTemplateId and isActive = '1'")
	List<FormulaGen> findByReturnTemplateIdFkReturnTemplateId(Long returnTemplateId);
}
