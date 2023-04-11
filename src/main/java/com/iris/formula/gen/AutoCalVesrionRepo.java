package com.iris.formula.gen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoCalVesrionRepo extends JpaRepository<AutoCalVesrion, Long> {

	List<AutoCalVesrion> findByReturnTemplateFkReturnTemplateId(Long returnTemplateId);

}
