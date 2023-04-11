package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.LanguageMaster;

public interface LanguageMasterRepo extends JpaRepository<LanguageMaster, Integer> {

	List<LanguageMaster> findByIsActiveTrue();

	LanguageMaster findByLanguageCodeAndIsActiveTrue(String languageCode);

	List<LanguageMaster> findByLanguageId(Long languageId);
}
