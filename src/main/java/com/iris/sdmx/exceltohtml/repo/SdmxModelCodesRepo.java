package com.iris.sdmx.exceltohtml.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.sdmx.exceltohtml.bean.ModelOutputBean;
import com.iris.sdmx.exceltohtml.bean.SdmxModelCodesBean;
import com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity;
import com.iris.sdmx.model.code.data.SdmxDataModelCodeBean;
import com.iris.sdmx.sdmxDataModelCodesDownloadBean.SdmxModelCodesDownloadBean;
import com.iris.sdmx.upload.bean.SdmxModelCodeLiteBean;

/**
 * @author apagaria
 *
 */
public interface SdmxModelCodesRepo extends JpaRepository<SdmxModelCodesEntity, Long> {

	@Query("from SdmxModelCodesEntity where elementIdFk=:elementIdFk")
	SdmxModelCodesEntity findByElementIdFk(SdmxElementEntity elementIdFk);

	@Query("from SdmxModelCodesEntity where modelCode=:modelCode")
	SdmxModelCodesEntity findByModelCode(String modelCode);

	@Query("from SdmxModelCodesEntity where modelCodesId=:modelCodesId")
	SdmxModelCodesEntity findByModelCodesId(Long modelCodesId);

	@Query("SELECT u.modelCode FROM SdmxModelCodesEntity u where u.modelCode like %:dmModelCode% group by u.modelCode")
	List<String> findDmModelCodes(String dmModelCode);

	@Query("FROM SdmxModelCodesEntity u where u.modelCode=:dmModelCode")
	List<SdmxModelCodesEntity> findEntityByModelCode(String dmModelCode);

	@Query("FROM SdmxModelCodesEntity u where u.modelDimHash=:modelDimHash")
	SdmxModelCodesEntity findEntityByModelCodeHash(String modelDimHash);

	@Query(value = "SELECT * FROM TBL_SDMX_MODEL_CODES u where JSON_CONTAINS(u.MODEL_DIM,?1) LIMIT 1", nativeQuery = true)
	SdmxModelCodesEntity findEntityByModelDim(String modelDim);

	@Query(value = "SELECT * FROM TBL_SDMX_MODEL_CODES u where JSON_CONTAINS(u.MODEL_DIM,?1) and u.ELEMENT_ID_FK=?2", nativeQuery = true)
	List<SdmxModelCodesEntity> findEntityByModelDimNElementId(String modelDim, SdmxElementEntity sdmxElementEntity);

	@Query("SELECT max(u.modelCode) from SdmxModelCodesEntity u where u.modelCode like %:modelCodeStr%")
	String findMaxDMIModelCodes(String modelCodeStr);

	@Query("SELECT max(u.modelCode) from SdmxModelCodesEntity u where u.modelCode NOT like %:modelCodeStr%")
	String findMaxDMModelCodes(String modelCodeStr);

	@Query("SELECT new com.iris.sdmx.exceltohtml.bean.SdmxModelCodesBean(smc.modelCodesId, " + "smc.modelDim, smc.modelDimHash, smc.modelCode,smc.elementIdFk.elementId,smc.elementIdFk.elementVer," + "smc.elementIdFk.elementLabel,smc.elementIdFk.dsdCode, smc.isActive, smc.createdBy.userId, " + "smc.createdBy.userName, smc.createdOn, rgx.regexId, rgx.regex) FROM SdmxModelCodesEntity smc " + "LEFT JOIN Regex rgx on rgx.regexId = smc.regexIdFk.regexId where smc.modelCodesId=" + "(SELECT rmi.modelCodesIdFk FROM SdmxReturnModelInfoEntity rmi where " + "rmi.returnCellRef =:returnCellRef and rmi.isActive=true and rmi.returnSheetInfoIdFk " + "IN (SELECT rsi.returnSheetInfoId FROM SdmxReturnSheetInfoEntity rsi " + "where rsi.returnTemplateIdFk.returnTemplateId =:returnTemplateId and rsi.returnPreviewIdFk =:returnPreviewId))")
	SdmxModelCodesBean findByReturnCellReffNReturnTemplate(Integer returnCellRef, Long returnTemplateId, Long returnPreviewId);

	@Query(value = "SELECT * FROM TBL_SDMX_MODEL_CODES b where b.MODEL_DIM -> '$.closedDim[*].dimConceptId' like %:dimCode%  or '$.openDimension[*].dimConceptId' like %:dimCode%  or '$.commonDimension[*].dimConceptId' like %:dimCode% ", nativeQuery = true)
	List<SdmxModelCodesEntity> getSdmxModelDataCodeUsingDimAndCodeValue(String dimCode);

	@Query("SELECT new com.iris.sdmx.exceltohtml.bean.ModelOutputBean(cod.elementIdFk.elementId, info.returnCellRef, sheetInfo.returnSheetInfoId," + " ret.returnCode, ret.returnName, sheetInfo.sheetName, sheetInfo.sectionName) " + "  from SdmxModelCodesEntity cod, SdmxReturnModelInfoEntity info, " + "  SdmxReturnSheetInfoEntity sheetInfo, ReturnTemplate temp, Return ret" + "  where cod.elementIdFk.elementId=:elementId and cod.modelCodesId =  info.modelCodesIdFk.modelCodesId " + "	 and info.returnSheetInfoIdFk.returnSheetInfoId = sheetInfo.returnSheetInfoId " + "	 and sheetInfo.returnTemplateIdFk.returnTemplateId = temp.returnTemplateId " + "	 and temp.returnObj.returnId = ret.returnId and cod.isActive = true")
	List<ModelOutputBean> fetchSDMXElementModelMappingData(Long elementId);

	@Query("select distinct new com.iris.sdmx.model.code.data.SdmxDataModelCodeBean(cod.modelCode, ret.returnCode, ele.dsdCode) from SdmxModelCodesEntity cod, " + " SdmxReturnModelInfoEntity modelInfo, SdmxReturnSheetInfoEntity seetInfo, SdmxElementEntity ele, " + " ReturnTemplate templ, Return ret where cod.modelCode in(:dimIdList) and cod.isActive = 1 " + " and ret.isActive = 1" + " and modelInfo.isActive = 1 and ele.isActive = 1 " + " and cod.modelCodesId = modelInfo.modelCodesIdFk.modelCodesId " + " and modelInfo.returnSheetInfoIdFk.returnSheetInfoId = seetInfo.returnSheetInfoId " + " and seetInfo.returnTemplateIdFk.returnTemplateId = templ.returnTemplateId" + " and ele.elementId = cod.elementIdFk.elementId" + " and templ.returnObj.returnId = ret.returnId")
	List<SdmxDataModelCodeBean> fetchDistinctDimIdData(List<String> dimIdList);

	@Query("select distinct new com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity(smc.elementIdFk) from SdmxModelCodesEntity smc " + " where smc.modelCodesId in (select srm.modelCodesIdFk from SdmxReturnModelInfoEntity srm where " + " srm.returnSheetInfoIdFk in (select srsi.returnSheetInfoId from SdmxReturnPreviewEntity srp,SdmxReturnSheetInfoEntity srsi where  " + " srp.returnPreviewTypeId =:returnPreviewId and  " + " srp.returnPreviewTypeId = srsi.returnPreviewIdFk) ) ")
	List<SdmxModelCodesEntity> fetchDistinctElementOfDataTemplate(Long returnPreviewId);

	@Query("select  new com.iris.sdmx.sdmxDataModelCodesDownloadBean.SdmxModelCodesDownloadBean(ret.returnCode,reTemp.versionNumber,reTemp.returnTemplateId,srmie.returnCellRef,see.elementId,see.dsdCode,smce.modelCode,smce.modelDim) from  Return ret, ReturnTemplate reTemp, SdmxReturnSheetInfoEntity srse, SdmxReturnModelInfoEntity srmie,SdmxModelCodesEntity smce, SdmxElementEntity see,SdmxReturnPreviewEntity srpe " + " where ret.returnCode=:tempReturnCode and reTemp.returnObj.returnId=ret.returnId and srse.returnPreviewIdFk=:tempReturnPreviewID and srse.returnPreviewIdFk = srpe.returnPreviewTypeId and " + " srse.returnTemplateIdFk.returnTemplateId=reTemp.returnTemplateId and srmie.returnSheetInfoIdFk.returnSheetInfoId=srse.returnSheetInfoId and srmie.isActive='1' and  " + " smce.modelCodesId=srmie.modelCodesIdFk.modelCodesId and see.elementId=smce.elementIdFk.elementId and see.isActive='1' order by srmie.returnCellRef")
	List<SdmxModelCodesDownloadBean> generateCsvForTemplateView(String tempReturnCode, Long tempReturnPreviewID);

	@Query("select  new com.iris.sdmx.sdmxDataModelCodesDownloadBean.SdmxModelCodesDownloadBean(ret.returnCode,reTemp.versionNumber,reTemp.returnTemplateId,srmie.returnCellRef,see.elementId,see.dsdCode,smce.modelCode,smce.modelDim) from  Return ret, ReturnTemplate reTemp, SdmxReturnSheetInfoEntity srse, SdmxReturnModelInfoEntity srmie,SdmxModelCodesEntity smce, SdmxElementEntity see,SdmxReturnPreviewEntity srpe " + " where  reTemp.returnObj.returnId=ret.returnId and srse.returnPreviewIdFk in (:previewIDList) and srse.returnPreviewIdFk = srpe.returnPreviewTypeId and " + " srse.returnTemplateIdFk.returnTemplateId=reTemp.returnTemplateId and srmie.returnSheetInfoIdFk.returnSheetInfoId=srse.returnSheetInfoId and srmie.isActive='1' and  " + " smce.modelCodesId=srmie.modelCodesIdFk.modelCodesId and see.elementId=smce.elementIdFk.elementId and see.isActive='1' and  smce.elementIdFk.elementId=:eleID order by srmie.returnCellRef")
	List<SdmxModelCodesDownloadBean> generateCsvForElementView(List<Long> previewIDList, Long eleID);

	@Query("select  new com.iris.sdmx.sdmxDataModelCodesDownloadBean.SdmxModelCodesDownloadBean(ret.returnCode,ret.returnName,srpe.ebrVersion,srpe.returnPreviewTypeId)from  Return ret, ReturnTemplate reTemp, SdmxReturnSheetInfoEntity srse, SdmxReturnModelInfoEntity srmie,SdmxModelCodesEntity smce, SdmxElementEntity see,SdmxReturnPreviewEntity srpe " + " where ret.returnCode=reTemp.returnObj.returnCode  and srse.returnPreviewIdFk=srpe.returnPreviewTypeId and " + " srse.returnTemplateIdFk.returnTemplateId=reTemp.returnTemplateId and srmie.returnSheetInfoIdFk.returnSheetInfoId=srse.returnSheetInfoId and srmie.isActive='1' and  " + " srpe.returnTemplateIdFk.returnTemplateId = reTemp.returnTemplateId and smce.modelCodesId=srmie.modelCodesIdFk.modelCodesId and see.elementId=smce.elementIdFk.elementId and see.isActive='1' and smce.elementIdFk.elementId=:eleID ")
	List<SdmxModelCodesDownloadBean> fetchEbrTemplateAccordingElement(Long eleID);

	@Query("SELECT new com.iris.sdmx.exceltohtml.bean.ModelOutputBean(cod.elementIdFk.elementId, info.returnCellRef, sheetInfo.returnSheetInfoId," + " ret.returnCode, ret.returnName, ret.returnId, temp.versionNumber, temp.returnTemplateId, srp.returnPreviewTypeId ," + "  srp.ebrVersion,sheetInfo.sheetName, sheetInfo.sectionName) " + "  from SdmxModelCodesEntity cod, SdmxReturnModelInfoEntity info, " + "  SdmxReturnSheetInfoEntity sheetInfo, ReturnTemplate temp, Return ret, SdmxReturnPreviewEntity srp" + "  where cod.elementIdFk.elementId=:elementId and cod.modelCodesId =  info.modelCodesIdFk.modelCodesId " + "	 and info.returnSheetInfoIdFk.returnSheetInfoId = sheetInfo.returnSheetInfoId " + "	 and sheetInfo.returnPreviewIdFk = srp.returnPreviewTypeId and srp.returnTemplateIdFk.returnTemplateId = temp.returnTemplateId" + "	 and temp.returnObj.returnId = ret.returnId and cod.isActive = true and srp.isActive = '1' order by ret.returnName,temp.versionNumber," + " srp.returnPreviewTypeId,sheetInfo.sheetName,sheetInfo.sectionName")
	List<ModelOutputBean> fetchElementModelMappingData(Long elementId);

	@Query("SELECT modelCode FROM SdmxModelCodesEntity u where u.modelDimHash=:eleDimHash")
	List<String> getDMIdByHash(String eleDimHash);

	@Query("SELECT new com.iris.sdmx.upload.bean.SdmxModelCodeLiteBean(u.modelDimHash, u.modelCode, e.dsdCode, e.elementVer, infoEnt.returnCellRef) " + " FROM SdmxModelCodesEntity u, " + " SdmxElementEntity e, SdmxReturnModelInfoEntity infoEnt, SdmxReturnSheetInfoEntity sheetInfo, SdmxReturnPreviewEntity preview" + " where u.modelDimHash in(:eleDimHash) and preview.isPublished = '1' and preview.isActive = '1' and u.elementIdFk.elementId = e.elementId " + " and u.modelCodesId = infoEnt.modelCodesIdFk.modelCodesId and infoEnt.returnSheetInfoIdFk.returnSheetInfoId = sheetInfo.returnSheetInfoId" + " and sheetInfo.returnPreviewIdFk = preview.returnPreviewTypeId")
	List<SdmxModelCodeLiteBean> getDMIdByHashIn(List<String> eleDimHash);

	@Query("select new com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity(smc.modelDim,smc.modelCode,srm.returnCellRef) from SdmxReturnPreviewEntity srp, SdmxReturnSheetInfoEntity srsi, SdmxReturnModelInfoEntity srm, SdmxModelCodesEntity smc where srp.returnPreviewTypeId = srsi.returnPreviewIdFk  and srsi.returnSheetInfoId = srm.returnSheetInfoIdFk.returnSheetInfoId and srm.modelCodesIdFk.modelCodesId = smc.modelCodesId and srm.isActive=true and srp.returnPreviewTypeId =:returnPreviewId")
	List<SdmxModelCodesEntity> fetchModelDim(Long returnPreviewId);

	@Query("SELECT smce.modelCodesId FROM SdmxModelCodesEntity smce WHERE smce.modelCodesId NOT " + "IN (SELECT modelCodesIdFk FROM SdmxReturnModelInfoEntity)")
	List<Long> fetchUnmappdedModelCodes();

	@Modifying
	@Query("DELETE FROM SdmxModelCodesEntity smce WHERE smce.modelCodesId=:modelCodeId")
	void deleteUnmappdedModelCodes(Long modelCodeId);

	@Query(" SELECT DISTINCT modelCodesId FROM SdmxModelCodesEntity WHERE modelCodesId NOT IN( " + " SELECT DISTINCT srmi.modelCodesIdFk " + " FROM SdmxReturnModelInfoEntity srmi, SdmxReturnSheetInfoEntity srsi, SdmxReturnPreviewEntity srp " + " WHERE srp.isActive = 1 " + " and srsi.returnPreviewIdFk = srp.returnPreviewTypeId " + " and srmi.returnSheetInfoIdFk = srsi.returnSheetInfoId " + " and srmi.isActive = 1 ) ")
	List<Long> fetchdModelCodeIds();

	@Query("SELECT smce.modelCodesId FROM SdmxModelCodesEntity smce")
	List<Long> fetchModelCodesId(Pageable pageable);

	@Query("SELECT smce.modelDimHash FROM SdmxModelCodesEntity smce")
	List<String> fetchModelCodesJson();

}
