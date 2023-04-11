package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ErrorCodeDetail;

public interface ErrorCodeDetailRepo extends JpaRepository<ErrorCodeDetail, Long> {

	@Query(value = "select * from TBL_ERROR_CODE_DETAIL where  TECHNICAL_ERROR_CODE like %:technicalErrorCodeLike% order by ERROR_CODE_DETAIL_ID desc LIMIT 1", nativeQuery = true)
	ErrorCodeDetail findMaxErrorCodeDetail(String technicalErrorCodeLike);

	@Query(value = "select * from TBL_ERROR_CODE_DETAIL where  TECHNICAL_ERROR_CODE like %:technicalErrorCodeLike%", nativeQuery = true)
	List<ErrorCodeDetail> findErrorCodeDetail(String technicalErrorCodeLike);

	@Query("from ErrorCodeDetail b where  b.technicalErrorCode IN (:staticErrorCodeList)")
	List<ErrorCodeDetail> getStaticErrorCodeLabels(List<String> staticErrorCodeList);

	ErrorCodeDetail findByTechnicalErrorCode(@Param("technicalErrorCode") String technicalErrorCode);

	@Query(value = "select * from TBL_ERROR_CODE_DETAIL where  ERROR_CODE_DETAIL_ID in (select ERROR_CODE_DETAIL_ID_FK from TBL_ERROR_VERSION_CHANNEL_MAPPING where RETURN_TEMPLATE_ID_FK = :returnTemplateId)", nativeQuery = true)
	List<ErrorCodeDetail> findErrorCodeDetailByReturnTemplateId(Long returnTemplateId);

	@Query(value = "select * from TBL_ERROR_CODE_DETAIL where  BUSINESS_ERROR_CODE =:businessErrorCode and TECHNICAL_ERROR_CODE like %:technicalErrorCode% order by ERROR_CODE_DETAIL_ID desc LIMIT 1", nativeQuery = true)
	ErrorCodeDetail findByBusinessErrorCode(@Param("businessErrorCode") String businessErrorCode, @Param("technicalErrorCode") String technicalErrorCode);

}
