package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.iris.dto.ErrorCodeMessageBean;
import com.iris.model.ErrorVersionChannelMapping;
import com.iris.model.ReturnTemplate;

public interface ErrorVersionChannelMappingRepository extends JpaRepository<ErrorVersionChannelMapping, Long> {

	@Query("from ErrorVersionChannelMapping where isActiveForFileBasedFiling=:isActive and returnTemplateIdFk=:returnTemplate")
	List<ErrorVersionChannelMapping> getErrorVersionChannelMappingRecordForFileByStatus(@Param("isActive") Boolean isActiveForFileBasedFiling, ReturnTemplate returnTemplate);

	@Query("from ErrorVersionChannelMapping where isActiveForWebFormBasedFiling=:isActive and returnTemplateIdFk=:returnTemplate")
	List<ErrorVersionChannelMapping> getErrorVersionChannelMappingRecordForWebFormBasedByStatus(@Param("isActive") Boolean isActiveForWebFormBasedFiling, ReturnTemplate returnTemplate);

	@Query("SELECT new com.iris.dto.ErrorCodeMessageBean(B.technicalErrorCode, B.businessErrorCode, A.errorKeyLabelForWebBased) from " + "ErrorCodeLabelMapping A, ErrorCodeDetail B, ErrorVersionChannelMapping C where A.languageIdFk.languageCode=:langCode and " + "C.returnTemplateIdFk.returnTemplateId =:returnTemplateId and C.errorCodeDetailIdFk.errorCodeDetailId=A.errorCodeDetailIdFk.errorCodeDetailId and B.errorCodeDetailId=C.errorCodeDetailIdFk.errorCodeDetailId " + "and C.isActiveForWebFormBasedFiling=1")
	List<ErrorCodeMessageBean> getErrorsRecordForWebFormBasedOnReturnId(@Param("returnTemplateId") Long returnTemplateId, @Param("langCode") String langCode);

	@Procedure(procedureName = "SP_INSERT_ERROR_VERSION_CHANNEL_MAPPING")
	void insertErrorChannnelVesrion(@Param("errorcode_dtl_startid") Integer errorStartId, @Param("errorcode_dtl_endid") Integer errorCodeEndId, @Param("version_returnid") Integer returnTemplate);

	@Query("from ErrorVersionChannelMapping where isActiveForFileBasedFiling= '1' and isActiveForWebFormBasedFiling= '1' and errorCodeDetailIdFk.errorCodeDetailId =:errorCodeDetailId and returnTemplateIdFk.returnTemplateId =:returnTemplateId")
	ErrorVersionChannelMapping getErrorVersionChannelMappingRecordByErrorCodeDetailIdAndRetTempId(@Param("errorCodeDetailId") Long errorCodeDetailId, @Param("returnTemplateId") Long returnTemplateId);

}