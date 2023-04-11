
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.dto.EntityReturnChanneMapAppDto;
import com.iris.model.EntityReturnChanMapApproval;

/**
 * @author sajadhav
 *
 */
public interface EntityReturnChanMapAppRepo extends JpaRepository<EntityReturnChanMapApproval, Long> {

	@Query(value = "select new com.iris.dto.EntityReturnChanneMapAppDto" + " ( approval.returnEntityChanMapAppId, cat.categoryId, cat.categoryCode, catLab.categoryLabel, subCat.subCategoryId, subCat.subCategoryCode, subCategoryLabel.subCategoryLabel, " + " ent.entityId, ent.entityCode,  ent.ifscCode, entLabel.entityNameLabel, " + "  approval.returnEntityChanMapJson, usr.userId, usr.userName,  approval.createdOn)" + " from EntityReturnChanMapApproval approval, Category cat, CategoryLabel catLab, " + " SubCategory subCat, SubCategoryLabel subCategoryLabel, UserMaster usr,  " + " LanguageMaster lang " + " left join EntityBean ent on ent.entityId = approval.entity.entityId and approval.isActive =:isActive " + " left join EntityLabelBean entLabel on entLabel.entityBean.entityId = ent.entityId and entLabel.languageMaster.languageId = lang.languageId" + " where  approval.approvalStatus.adminStatusId =:adminStatusId " + " and lang.languageCode =:langCode " + " and catLab.langIdFk.languageId = lang.languageId " + " and subCategoryLabel.langIdFk.languageId = lang.languageId " + " and catLab.categoryIdFk.categoryId = cat.categoryId " + " and subCategoryLabel.subCatIdFk.subCategoryId = subCat.subCategoryId " + " and approval.category.categoryId = cat.categoryId " + " and approval.subCategory.subCategoryId = subCat.subCategoryId " + " and approval.createdBy.userId = usr.userId and approval.createdBy.userId <>:userId ")
	List<EntityReturnChanneMapAppDto> getDataForApproval(@Param("adminStatusId") Long adminStatusId, @Param("isActive") Boolean isActive, @Param("userId") Long userId, @Param("langCode") String langCode);

	EntityReturnChanMapApproval getDataByReturnEntityChanMapAppId(Long returnEntityChanMapAppId);

	@Query(value = "select new com.iris.dto.EntityReturnChanneMapAppDto" + " (approval.returnEntityChanMapAppId)" + " from EntityReturnChanMapApproval approval where  approval.approvalStatus.adminStatusId =:adminStatusId " + " and approval.category.categoryId =:catId " + " and approval.subCategory.subCategoryId =:subCatId and approval.isActive =:isActive")
	EntityReturnChanneMapAppDto isRecordPendingForApproval(@Param("adminStatusId") Long adminStatusId, @Param("isActive") Boolean isActive, @Param("catId") Long catId, @Param("subCatId") Long subCatId);

	@Query(value = "select new com.iris.dto.EntityReturnChanneMapAppDto" + " (approval.returnEntityChanMapAppId)" + " from EntityReturnChanMapApproval approval where  approval.approvalStatus.adminStatusId =:adminStatusId " + " and approval.category.categoryId =:catId " + " and approval.subCategory.subCategoryId =:subCatId and approval.isActive =:isActive and approval.entity.entityId =:entityId")
	EntityReturnChanneMapAppDto isRecordPendingForApproval(@Param("adminStatusId") Long adminStatusId, @Param("isActive") Boolean isActive, @Param("catId") Long catId, @Param("subCatId") Long subCatId, @Param("entityId") Long entityId);
}
