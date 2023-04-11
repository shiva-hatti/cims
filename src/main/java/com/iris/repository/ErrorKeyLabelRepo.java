package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ErrorKeyLabel;

public interface ErrorKeyLabelRepo extends JpaRepository<ErrorKeyLabel, Long> {

	@Query(value = "SELECT new com.iris.model.ErrorKeyLabel(errorKey.errorKey, lable.errorKeyLable, lable.languageIdFk.languageCode, errorKey.errorCode)  FROM ErrorKeyLabel lable , ErrorKey errorKey where lable.languageIdFk.languageId = :languageId" + " and lable.errorIdFk.errorId = errorKey.errorId")
	List<ErrorKeyLabel> loadAllErrorKeyLableByLanguageId(@Param("languageId") Long languageId);

	@Query(value = "SELECT new com.iris.model.ErrorKeyLabel(b.errorKey, a.errorKeyLable, c.languageCode) FROM ErrorKeyLabel a, " + " ErrorKey b, LanguageMaster c " + " where a.errorIdFk.errorId = b.errorId " + " and c.languageId = a.languageIdFk.languageId " + " and c.isActive = 1 " + " and c.languageCode IN :languageCode " + " and b.errorKey IN :labelKeys ")
	List<ErrorKeyLabel> getDataByLabelKeyAndLangCode(@Param("labelKeys") List<String> labelKeys, @Param("languageCode") List<String> languageCode);

}
