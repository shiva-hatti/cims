package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.FieldKeyLabel;

public interface FieldKeyLabelRepo extends JpaRepository<FieldKeyLabel, Long> {

	@Query(value = "SELECT  new com.iris.model.FieldKeyLabel(fieldKey.fieldKey, lable.fieldKeyLable, lable.languageIdFk.languageCode) FROM FieldKeyLabel lable , FieldKey fieldKey where lable.languageIdFk.languageId = 15\r\n" + "and lable.fieldIdFk.fieldId = fieldKey.fieldId")
	List<FieldKeyLabel> loadAllFiedldKeyLableByLanguageId(@Param("languageId") Long languageId);

	@Query(value = "SELECT  new com.iris.model.FieldKeyLabel(fieldKey.fieldKey, lable.fieldKeyLable, lable.languageIdFk.languageCode) FROM FieldKeyLabel lable , FieldKey fieldKey where lable.languageIdFk.languageCode = languageCode" + " and lable.fieldIdFk.fieldId = fieldKey.fieldId")
	List<FieldKeyLabel> loadAllFiedldKeyLableByLanguageCode(@Param("languageCode") String languageCode);

	@Query(value = "SELECT new com.iris.model.FieldKeyLabel(b.fieldKey, a.fieldKeyLable, c.languageCode) FROM FieldKeyLabel a, FieldKey b, " + " LanguageMaster c where a.fieldIdFk.fieldId = b.fieldId " + " and c.languageId = a.languageIdFk.languageId and c.isActive = 1 " + " and c.languageCode IN :languageCode and b.fieldKey IN :labelKeys")
	List<FieldKeyLabel> getDataByLabelKeyAndLangCode(List<String> labelKeys, List<String> languageCode);
}
