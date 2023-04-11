package com.iris.repository;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.SubCategory;

public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {

	//	@Query("select distinct new com.iris.model.SubCategory(subCat.subCategoryId, subCatLabel.subCategoryLabel, subCat.category.categoryId,subCat.subCategoryCode) from EntityBean en, "
	//			+ " SubCategory subCat, SubCategoryLabel subCatLabel, LanguageMaster lang "
	//			+ " where subCat.isActive =:isActive "
	//			+ " and en.isActive =:isActive"
	//			+ " and lang.isActive =:isActive "
	//			+ " and lang.languageCode =:langCode "
	//			+ " and en.subCategory.subCategoryId = subCat.subCategoryId "
	//			+ " and subCat.subCategoryId = subCatLabel.subCatIdFk.subCategoryId "
	//			+ " and subCatLabel.langIdFk.languageId = lang.languageId order by subCatLabel.subCategoryLabel asc")

	@Query("select distinct new com.iris.model.SubCategory(subCat.subCategoryId, subCatLabel.subCategoryLabel, subCat.category.categoryId,subCat.subCategoryCode) " + " from DeptUserEntityMapping duem, EntityBean en, " + " SubCategory subCat, SubCategoryLabel subCatLabel, LanguageMaster lang " + " where subCat.isActive =:isActive and duem.entity.entityId = en.entityId " + " and en.isActive =:isActive" + " and lang.isActive =:isActive " + " and duem.userIdFk.userId =:userId " + " and duem.isActive =:isActive " + " and lang.languageCode =:langCode " + " and en.subCategory.subCategoryId = subCat.subCategoryId " + " and subCat.subCategoryId = subCatLabel.subCatIdFk.subCategoryId " + " and subCatLabel.langIdFk.languageId = lang.languageId order by subCatLabel.subCategoryLabel asc")
	List<SubCategory> loadSubCategoryForMainDeptUser(@QueryParam("isActive") boolean isActive, @QueryParam("langCode") String langCode, @QueryParam("userId") Long userId);

	@Query("select distinct new com.iris.model.SubCategory(cat.subCategoryId, catLabel.subCategoryLabel, cat.category.categoryId,cat.subCategoryCode) " + " from UserRoleReturnMapping rolRetMap, ReturnEntityMappingNew retEntMap, EntityBean en, " + " SubCategory cat, SubCategoryLabel catLabel, LanguageMaster lang " + " where rolRetMap.roleIdFk.userRoleId =:roleId " + " and rolRetMap.returnIdFk.returnId = retEntMap.returnObj.returnId" + " and en.entityId = retEntMap.entity.entityId" + " and lang.languageCode =:langCode" + " and lang.isActive =:isActive" + " and en.isActive =:isActive" + " and cat.isActive =:isActive" + " and rolRetMap.isActive =:isActive" + " and retEntMap.isActive =:isActive" + " and en.subCategory.subCategoryId = cat.subCategoryId " + " and cat.subCategoryId = catLabel.subCatIdFk.subCategoryId " + " and catLabel.langIdFk.languageId = lang.languageId order by catLabel.subCategoryLabel asc")
	List<SubCategory> loadSubCategoryForNonMainDeptUser(@QueryParam("roleId") Long roleId, @QueryParam("isActive") boolean isActive, @QueryParam("langCode") String langCode);

	@Query("select distinct new com.iris.model.SubCategory(subCat.subCategoryId, subCatLabel.subCategoryLabel, subCat.category.categoryId,subCat.subCategoryCode) " + " from SubCategory subCat, SubCategoryLabel subCatLabel, LanguageMaster lang " + " where subCat.isActive =:isActive  " + " and lang.isActive =:isActive " + " and lang.languageCode =:langCode " + " and subCat.subCategoryId = subCatLabel.subCatIdFk.subCategoryId " + " and subCatLabel.langIdFk.languageId = lang.languageId order by subCatLabel.subCategoryLabel asc")
	List<SubCategory> getAllActiveSubCatByLangCode(@QueryParam("langCode") String langCode, @QueryParam("isActive") boolean isActive);

	@Query(value = "FROM SubCategory where isActive = 1")
	List<SubCategory> findByIsActiveTrue();

	@Query("select distinct new com.iris.model.SubCategory(subCat.subCategoryId, subCatLabel.subCategoryLabel,subCat.category.categoryId,subCat.subCategoryCode) " + " from SubCategory subCat, SubCategoryLabel subCatLabel, LanguageMaster lang, Category cat, CategoryLabel catLabel " + " where subCat.isActive =:isActive  " + " and lang.isActive =:isActive " + " and lang.languageCode =:langCode " + " and subCat.subCategoryId = subCatLabel.subCatIdFk.subCategoryId " + " and subCatLabel.langIdFk.languageId = lang.languageId and subCat.category.categoryId =cat.categoryId " + "  and catLabel.categoryIdFk.categoryName =:categoryName and cat.categoryId = catLabel.categoryIdFk.categoryId")
	List<SubCategory> getSubCatListByCatName(@QueryParam("langCode") String langCode, @QueryParam("isActive") boolean isActive, @QueryParam("categoryName") String categoryName);
}
