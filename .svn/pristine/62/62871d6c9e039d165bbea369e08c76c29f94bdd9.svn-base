/**
 * 
 */
package com.iris.sdmx.fusion.controller;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.ebrhtmlfilebuilder.HtmlFileBuilder;
import com.iris.ebrhtmlfilebuilder.bean.EBRFilingDataBean;
import com.iris.ebrhtmlfilebuilder.bean.VtlValidationResponse;
import com.iris.ebrhtmlfilebuilder.utility.Constants;
import com.iris.exception.ApplicationException;
import com.iris.fileDataExtract.ExtractFileData;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.sdmx.ebrvalidation.bean.VtlStatusBean;
import com.iris.sdmx.fusion.service.FusionApiService;
import com.iris.sdmx.sdmxDataModelCodesDownloadBean.ZipUtils;
import com.iris.sdmx.status.entity.SdmxFileActivityLog;
import com.iris.sdmx.status.service.SdmxFileActivityLogService;
import com.iris.service.impl.FileDetailsService;
import com.iris.util.DateUtil;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.FilingStatusConstants;
import com.iris.util.constant.GeneralConstants;

/**
 * @author sajadhav
 *
 */
@RestController
@RequestMapping(value = "/vtlValidationController")
public class VTLValidationController {

	private static final Logger LOGGER = LogManager.getLogger(VTLValidationController.class);

	@Autowired
	private FileDetailsService fileDetailService;

	@Autowired
	private FusionApiService fusionApiService;

	@PostMapping(value = "/callBackFromVtlStatus")
	public ServiceResponse callBackFromVtlStatus(@RequestBody VtlStatusBean vtlStatusBean) {
		FileDetails fileDetails = null;
		long[] fileDetailsId = new long[1];
		try {
			validateInputRequestForVtlStatus(vtlStatusBean);

			LOGGER.info("Request received from VTL for VTL Status ID : " + JsonUtility.getGsonObject().toJson(vtlStatusBean));

			fileDetails = fileDetailService.getDataByVtlRequestId(vtlStatusBean.getId(), FilingStatusConstants.BUSINESS_VALIDATION_IN_PROCESS.getConstantIntVal());

			if (fileDetails != null) {
				if (vtlStatusBean.getStatus().equalsIgnoreCase(Constants.VTL_SUCCESS_STATUS.getConstantValue())) {
					fileDetailService.updateVtlCallBackStatus(fileDetails.getId(), Constants.VTL_SUCCESS.getIntegerValue());
				} else if (vtlStatusBean.getStatus().equalsIgnoreCase(Constants.VTL_FAILED_STATUS.getConstantValue())) {
					fileDetailService.updateVtlCallBackStatus(fileDetails.getId(), Constants.VTL_FAILED.getIntegerValue());
				}
				return new ServiceResponseBuilder().setStatus(true).build();
			} else {
				LOGGER.error("VTL Status ID record not found");
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Request ID record not found").build();
			}
		} catch (ApplicationException e) {
			LOGGER.error("Request object not proper for VTL status ID : " + vtlStatusBean.getId() + " ", e);
			if (fileDetailsId.length > 0) {
				fileDetailService.updateFileDetailsStatusAndFileActivityLog(fileDetailsId, FilingStatusConstants.TECHNICAL_FAILURE_ID.getConstantIntVal(), true, null);
			}
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(e.getMessage()).build();
		} catch (Exception e) {
			LOGGER.error("Exception in callBackFromVtlStatus, " + vtlStatusBean.getId() + "  Exception is ", e);
			if (fileDetailsId.length > 0) {
				fileDetailService.updateFileDetailsStatusAndFileActivityLog(fileDetailsId, FilingStatusConstants.TECHNICAL_FAILURE_ID.getConstantIntVal(), true, null);
			}
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
	}

	@Scheduled(cron = "${cron.vtlValidationReport}")
	public ServiceResponse prepareVTLValidationReport() {
		List<FileDetails> fileDetailsList = null;
		VtlStatusBean vtlStatusBean = null;
		try {
			LOGGER.info("VTL Validation Report scheuler started");
			String jobProcessingId = UUID.randomUUID().toString();
			HtmlFileBuilder htmlFileBuilder = new HtmlFileBuilder();
			fileDetailsList = fileDetailService.fetchFileDetailsRecordForVTLHtmlCreation(FilingStatusConstants.BUSINESS_VALIDATION_IN_PROCESS.getConstantIntVal());
			if (ObjectUtils.isEmpty(fileDetailsList)) {
				LOGGER.error("Record  Not fund for VTL prepareVTLValidationReport : ");
				return new ServiceResponseBuilder().setStatus(false).build();
			}

			long[] fileDetailsId = new long[1];

			for (FileDetails fileDetails : fileDetailsList) {
				try {
					fileDetailsId[0] = fileDetails.getId();
					fileDetails.setFilePath(ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("filepath.instanceEbr") + File.separator + fileDetails.getEntityCode() + File.separator + DateUtil.getStringFormattedDate(fileDetails.getCreationDate(), DateConstants.DD_MM_YYYY.getDateConstants()) + File.separator + FilenameUtils.getBaseName(fileDetails.getSystemModifiedFileName()));

					vtlStatusBean = new VtlStatusBean();
					vtlStatusBean.setId(fileDetails.getVtlRequestId());

					EBRFilingDataBean ebrFilingDataBean = new EBRFilingDataBean();
					ebrFilingDataBean.setEntityCode(fileDetails.getEntityCode());
					ebrFilingDataBean.setEntityName(fileDetails.getEntityName());
					ebrFilingDataBean.setInstanceFilePath(fileDetails.getFilePath() + File.separator + fileDetails.getSystemModifiedFileName());
					ebrFilingDataBean.setSdmxTemplateFolderPath(ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("filePath.template") + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator);
					ebrFilingDataBean.setUploadedOnInLong(fileDetails.getCreationDate().getTime());

					if (fileDetails.getVtlCallBackStatus().equals(Constants.VTL_SUCCESS.getIntegerValue())) {
						ServiceResponse serviceResponse = fusionApiService.downloadResultFromVtl(vtlStatusBean, jobProcessingId, null, fileDetails);
						Map<String, List<Map<String, String>>> validationFailedDsdMap = new LinkedHashMap<>();

						boolean validationFailed = false;

						if (serviceResponse.getStatusCode().equals("200")) {
							ZipUtils zipUtils = new ZipUtils();
							String zipFilepath = serviceResponse.getResponse() + "";
							String parentFilePath = new File(zipFilepath).getParent();
							String extractedFolderPath = parentFilePath + File.separator + FilenameUtils.getBaseName(new File(zipFilepath).getName());
							FilenameUtils.getBaseName(new File(zipFilepath).getName());
							zipUtils.unZipIt(zipFilepath, extractedFolderPath);

							ExtractFileData extractFileData = new ExtractFileData();

							for (File file : new File(extractedFolderPath).listFiles()) {
								List<Map<String, String>> maps = extractFileData.readVTLGeneratedCSVDocument(file.getPath());
								if (maps.size() > 1) {
									validationFailed = true;
									validationFailedDsdMap.put(FilenameUtils.getBaseName(file.getName()), maps);
								}
							}
						}

						if (validationFailed) {
							htmlFileBuilder.createHtmlFileForVtlFailedOutput(ebrFilingDataBean, validationFailedDsdMap, null);
							fileDetailService.updateFileDetailsStatusAndFileActivityLog(fileDetailsId, FilingStatusConstants.BUSINESS_VALIDATION_FAIL.getConstantIntVal(), validationFailed, Constants.VTL_COMPLETED.getIntegerValue());
						} else {
							fileDetailService.updateFileDetailsStatusAndFileActivityLog(fileDetailsId, FilingStatusConstants.CSV_TO_XML_JSON_TO_XML_ID.getConstantIntVal(), validationFailed, Constants.VTL_COMPLETED.getIntegerValue());
						}
						return new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
					} else {
						VtlValidationResponse vtlValidationResponse = fusionApiService.downloadVtlStatusOfExecution(vtlStatusBean, jobProcessingId, null);

						htmlFileBuilder.createHtmlFileForVtlFailedOutput(ebrFilingDataBean, null, vtlValidationResponse);

						LOGGER.error("FAILED status received from VTL for VTL request ID : " + vtlStatusBean.getId());
						fileDetailService.updateFileDetailsStatusAndFileActivityLog(fileDetailsId, FilingStatusConstants.BUSINESS_VALIDATION_FAIL.getConstantIntVal(), true, Constants.VTL_COMPLETED.getIntegerValue());
					}
				} catch (ApplicationException e) {
					LOGGER.error("Request object not proper for VTL status ID : " + vtlStatusBean.getId() + " ", e);
					if (fileDetailsId.length > 0) {
						fileDetailService.updateFileDetailsStatusAndFileActivityLog(fileDetailsId, FilingStatusConstants.TECHNICAL_FAILURE_ID.getConstantIntVal(), true, Constants.VTL_COMPLETED.getIntegerValue());
					}
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(e.getMessage()).build();
				} catch (Exception e) {
					LOGGER.error("Exception in callBackFromVtlStatus, " + vtlStatusBean.getId() + "  Exception is ", e);
					if (fileDetailsId.length > 0) {
						fileDetailService.updateFileDetailsStatusAndFileActivityLog(fileDetailsId, FilingStatusConstants.TECHNICAL_FAILURE_ID.getConstantIntVal(), true, Constants.VTL_COMPLETED.getIntegerValue());
					}
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
				}
			}
			LOGGER.info("VTL Validation Report scheuler End");
			return new ServiceResponseBuilder().setStatus(true).build();
		} catch (Exception e) {
			LOGGER.error("Exception in prepareVTLValidationReport, Exception is ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage(e.getMessage()).build();
		}
	}

	private void validateInputRequestForVtlStatus(VtlStatusBean vtlStatusBean) throws ApplicationException {

	}

}
