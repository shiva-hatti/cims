/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.EntityLabelBean;

/**
 * @author sajadhav
 *
 */
public interface EntityLabelRepo extends JpaRepository<EntityLabelBean, Long> {

	@Query(value = "SELECT a FROM EntityLabelBean a, EntityBean b, LanguageMaster c where a.entityBean.entityId = b.entityId and c.languageId = a.languageMaster.languageId and c.isActive = 1 and b.isActive = 1 and c.languageCode IN :languageCode")
	List<EntityLabelBean> getDataLangCode(List<String> languageCode);

	@Query(value = "SELECT a FROM EntityLabelBean a, EntityBean b, LanguageMaster c where a.entityBean.entityId = b.entityId and c.languageId = a.languageMaster.languageId and c.isActive = 1 and b.isActive = 1 and b.entityId IN :entityIdLists and c.languageCode =:languageCodes")
	List<EntityLabelBean> getEntityDataByEntityIdAndLangCode(@Param("entityIdLists") List<Long> entityIdLists, @Param("languageCodes") String languageCodes);

	@Query(value = "SELECT a FROM EntityLabelBean a, EntityBean b, LanguageMaster c where (upper(a.entityNameLabel) like %:likeString% or upper(b.entityCode) like %:likeString%) and a.entityBean.entityId = b.entityId and c.languageId = a.languageMaster.languageId and c.isActive = 1 and b.isActive =:isActive and b.entityId =:entitIds and c.languageCode =:languageCodes and b.subCategory.subCategoryCode IN :subCategoryLists order by b.entityName asc ")
	List<EntityLabelBean> getEntityDataWithSubCategory(@Param("entitIds") Long entitIds, @Param("languageCodes") String languageCodes, @Param("subCategoryLists") List<String> subCategoryLists, @Param("isActive") Boolean isActive, @Param("likeString") String likeString);

	@Query(value = "SELECT a FROM EntityLabelBean a, EntityBean b, LanguageMaster c where a.entityBean.entityId = b.entityId and c.languageId = a.languageMaster.languageId and c.isActive = 1 and b.isActive =:isActive and b.entityId =:entitIds and c.languageCode =:languageCodes and b.category.categoryCode =:categoryCodes")
	List<EntityLabelBean> getEntityData(@Param("entitIds") Long entitIds, @Param("categoryCodes") String categoryCodes, @Param("languageCodes") String languageCodes, @Param("isActive") Boolean isActive);

	@Query(value = "SELECT a FROM EntityLabelBean a, EntityBean b, LanguageMaster c where  (upper(a.entityNameLabel) like %:likeString% or upper(b.entityCode) like %:likeString%) and a.entityBean.entityId = b.entityId and c.languageId = a.languageMaster.languageId and c.isActive = 1 and b.isActive =:isActives and b.entityId =:entitIds and c.languageCode =:languageCodes and b.category.categoryCode =:categoryCodes and b.subCategory.subCategoryCode IN :subCategoryLists order by b.entityName asc ")
	List<EntityLabelBean> getEntityDataWithSubCategoryAndCateCode(@Param("entitIds") Long entitIds, @Param("categoryCodes") String categoryCodes, @Param("languageCodes") String languageCodes, @Param("subCategoryLists") List<String> subCategoryLists, @Param("isActives") Boolean isActives, @Param("likeString") String likeString);

	@Query(value = "SELECT a FROM EntityLabelBean a, EntityBean b, LanguageMaster c where (upper(a.entityNameLabel) like %:likeString% or upper(b.entityCode) like %:likeString%) and a.entityBean.entityId = b.entityId and c.languageId = a.languageMaster.languageId and c.isActive = 1 and b.isActive =:isActives and b.entityId =:entitIds and c.languageCode =:languageCodes and b.category.categoryCode =:categoryCodes order by b.entityName asc ")
	List<EntityLabelBean> getEntityDataWithCateCode(@Param("entitIds") Long entitIds, @Param("categoryCodes") String categoryCodes, @Param("languageCodes") String languageCodes, @Param("isActives") Boolean isActives, @Param("likeString") String likeString);

	@Query(value = "SELECT a FROM EntityLabelBean a, EntityBean b, LanguageMaster c where (upper(a.entityNameLabel) like %:likeString% or upper(b.entityCode) like %:likeString%) and a.entityBean.entityId = b.entityId and c.languageId = a.languageMaster.languageId and c.isActive = 1 and b.isActive =:isActives and b.entityId =:entitIds and c.languageCode =:languageCodes  order by b.entityName asc ")
	List<EntityLabelBean> getEntityData(@Param("entitIds") Long entitIds, @Param("languageCodes") String languageCodes, @Param("isActives") Boolean isActives, @Param("likeString") String likeString);

	@Query(value = "SELECT a FROM EntityLabelBean a, EntityBean b, LanguageMaster c where (upper(a.entityNameLabel) like %:likeString% or upper(b.entityCode) like %:likeString%) and a.entityBean.entityId = b.entityId " + " and c.languageId = a.languageMaster.languageId " + " and c.isActive = 1 " + " and b.category.isActive = 1 " + " and b.subCategory.isActive = 1 " + " and b.isActive =:isActives and b.entityId =:entitIds and c.languageCode =:languageCodes  order by b.entityName asc ")
	Page<EntityLabelBean> getEntityData(@Param("entitIds") Long entitIds, @Param("languageCodes") String languageCodes, @Param("isActives") Boolean isActives, @Param("likeString") String likeString, Pageable pageable);
}
