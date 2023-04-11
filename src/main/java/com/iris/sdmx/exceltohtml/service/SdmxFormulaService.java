package com.iris.sdmx.exceltohtml.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.format.CellFormatResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iris.caching.ObjectCache;
import com.iris.dto.DataTemplateUploadValidationDetail;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.sdmx.exceltohtml.bean.DimensionDetailCategories;
import com.iris.sdmx.exceltohtml.bean.FormulaBean;
import com.iris.sdmx.exceltohtml.bean.FormulaValidationBean;
import com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnModelInfoEntity;
import com.iris.sdmx.exceltohtml.repo.SdmxModelCodesRepo;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;

@Service
public class SdmxFormulaService implements GenericService<SdmxReturnModelInfoEntity, Long> {
	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxFormulaService.class);

	@Autowired
	private SdmxModelCodesRepo sdmxModelCodesRepo;

	@Override
	public SdmxReturnModelInfoEntity add(SdmxReturnModelInfoEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxReturnModelInfoEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxReturnModelInfoEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxReturnModelInfoEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public Map<Integer, String> getCellRefFormulaMapping(String xlsFilePath, String returnCode, Long returnPreviewId, DataTemplateUploadValidationDetail dataTemplateUploadValidationDetail) throws Exception {
		LOGGER.info("START - @getCellRefFormulaMapping");
		Map<Integer, List<FormulaBean>> cellRefFormulaMap = new HashMap<>();
		Map<Integer, String> cellRefFormulaJsonMap = new HashMap<>();
		FormulaValidationBean formulaValidationBean;
		List<FormulaValidationBean> formulaValidationBeanList;

		try {
			Map<Integer, List<String>> elementCellToModelMap = new HashMap<>();
			elementCellToModelMap = getElementCellToModelMap(returnPreviewId);
			if (elementCellToModelMap == null || elementCellToModelMap.isEmpty()) {
				/*
				 * formulaValidationBean = new FormulaValidationBean();
				 * formulaValidationBeanList = new ArrayList<>();
				 * formulaValidationBean.setErrorMsgCode(ErrorCode.E1268.toString());
				 * formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.
				 * E1268.toString())); formulaValidationBeanList.add(formulaValidationBean);
				 * dataTemplateUploadValidationDetail.setFormulaValidationBeanList(
				 * formulaValidationBeanList);
				 */
				//LOGGER.error("No model codes found in the system");
				//throw new ApplicationException(ErrorCode.E1268.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1268.toString()));
				LOGGER.debug("No model codes found in the system");
				return null;
			}
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			FormulaBean formulaBean;
			List<String> dependentList;
			InputStream in = new FileInputStream(xlsFilePath);
			int counter = 1;
			boolean checkFlag;
			List<String> innerList;
			String tokenVal;
			Map<String, List<String>> calDMIDMap = new HashMap<>();
			List<String> calDMIDCellRefList;
			Map<String, String> evalDMIds = new HashMap<>();
			StringTokenizer st;
			StringBuilder formula;
			List<Integer> mandatoryCellIds = new ArrayList<>();
			List<String> formulaErrorLogList = new ArrayList<>();

			try (Workbook wb = WorkbookFactory.create(in)) {
				Sheet sheet = wb.getSheetAt(2);
				Iterator<Row> rows = sheet.rowIterator();
				while (rows.hasNext()) {
					Row row = rows.next();
					if (row.getRowNum() == 0) {
						counter++;
						continue;
					}
					formulaBean = new FormulaBean();
					Iterator<Cell> cells = row.cellIterator();
					checkFlag = true;
					int lhsCount;
					dependentList = new ArrayList<>();
					while (cells.hasNext()) {
						Cell cell = cells.next();
						int cellNum = cell.getColumnIndex();
						if (cell != null) {
							CellStyle style = cell.getCellStyle();
							CellFormat cf = CellFormat.getInstance(style.getDataFormatString());
							CellFormatResult result = cf.apply(cell);
							String content = StringUtils.trim(result.text);
							formulaBean.setRoundingUpTo("2");
							formulaBean.setErrorCode("ERR0000" + counter);
							formulaBean.setErrorMessage("TEST");
							formulaBean.setErrorType("E");
							formulaBean.setCsvLineNum(counter);
							try {
								switch (cellNum) {
								case 0:
									formulaBean.setFormulaType(content);
									break;

								case 1:
									st = new StringTokenizer(checkContent(content).toString());
									formula = new StringBuilder("");

									lhsCount = st.countTokens();
									while (st.hasMoreTokens()) {
										tokenVal = st.nextToken().trim();
										if (tokenVal.startsWith("#")) {
											if (elementCellToModelMap.containsKey(Integer.parseInt(tokenVal.split("#")[1]))) {
												innerList = elementCellToModelMap.get(Integer.parseInt(tokenVal.split("#")[1]));
												// formula.append(" #" + innerList.get(0));
												formula.append(" " + tokenVal);
												if (formulaBean.getFormulaType().equals("EVALUATION") && innerList.get(1).equals("1")) {
													// dependentList.add(innerList.get(0));
													dependentList.add(tokenVal);
													evalDMIds.put(innerList.get(0), "[" + formulaBean.getFormulaType() + "] Cell number: " + tokenVal + ", Row number:" + counter);
												}

												if (formulaBean.getFormulaType().equals("CALCULATION")) {

													if (calDMIDMap.containsKey(innerList.get(0))) {
														calDMIDCellRefList = calDMIDMap.get(innerList.get(0));
													} else {
														calDMIDCellRefList = new ArrayList<>();
													}
													calDMIDCellRefList.add(tokenVal);
													calDMIDMap.put(innerList.get(0), calDMIDCellRefList);

												}
											} else {
												checkFlag = false;
												formulaValidationBean = new FormulaValidationBean();
												formulaValidationBeanList = new ArrayList<>();
												formulaValidationBean.setErrorMsgCode(ErrorCode.E1269.toString());
												formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1269.toString()));
												formulaValidationBean.setCellRef(tokenVal.replace("#", ""));
												formulaValidationBean.setSheetRow(counter);
												formulaValidationBeanList.add(formulaValidationBean);
												dataTemplateUploadValidationDetail.setFormulaValidationBeanList(formulaValidationBeanList);
												LOGGER.error("No model found for cell reference: " + tokenVal + " at row: " + counter);
												throw new ApplicationException(ErrorCode.E1269.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));

											}
										} else {
											tokenVal = tokenVal.replace("ROUND", "numpy.ROUND");
											tokenVal = tokenVal.replace("MAX", "numpy.MAX");
											tokenVal = tokenVal.replace("MIN", "numpy.MIN");
											formula.append(" " + tokenVal);
										}
									}

									formulaBean.setLHS(formula.toString());
									break;

								case 2:
									formulaBean.setEXP(content);
									break;

								case 3:
									st = new StringTokenizer(checkContent(content).toString());
									formula = new StringBuilder("");

									while (st.hasMoreTokens()) {
										tokenVal = st.nextToken().trim();
										if (tokenVal.startsWith("#") && tokenVal.length() > 1) {
											if (elementCellToModelMap.containsKey(Integer.parseInt(tokenVal.split("#")[1]))) {
												innerList = elementCellToModelMap.get(Integer.parseInt(tokenVal.split("#")[1]));
												// formula.append(" #" + innerList.get(0));
												formula.append(" " + tokenVal);

												if (innerList.get(1).equals("1")) {
													// dependentList.add(innerList.get(0));
													dependentList.add(tokenVal);
													evalDMIds.put(innerList.get(0), "[" + formulaBean.getFormulaType() + "] Cell number: " + tokenVal + ", Row number:" + counter);

												} else {
													if (formulaBean.getFormulaType().equals("CALCULATION")) {

														if (StringUtils.isEmpty(formulaBean.getDependentWhiteCell())) {
															// formulaBean.setDependantWhiteCell(innerList.get(0));
															formulaBean.setDependentWhiteCell(tokenVal);

														}

														mandatoryCellIds.add(Integer.parseInt(tokenVal.replaceFirst("#", "")));
													}
												}

											} else {
												checkFlag = false;
												formulaValidationBean = new FormulaValidationBean();
												formulaValidationBeanList = new ArrayList<>();
												formulaValidationBean.setErrorMsgCode(ErrorCode.E1269.toString());
												formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1269.toString()));
												formulaValidationBean.setCellRef(tokenVal.replace("#", ""));
												formulaValidationBean.setSheetRow(counter);
												formulaValidationBeanList.add(formulaValidationBean);
												dataTemplateUploadValidationDetail.setFormulaValidationBeanList(formulaValidationBeanList);
												throw new ApplicationException(ErrorCode.E1269.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));

											}
										} else {
											tokenVal = tokenVal.replace("ROUND", "numpy.ROUND");
											tokenVal = tokenVal.replace("MAX", "numpy.MAX");
											tokenVal = tokenVal.replace("MIN", "numpy.MIN");
											formula.append(" " + tokenVal);
										}
									}

									formulaBean.setRHS(formula.toString());
									break;

								default:
									break;

								}
							} catch (NumberFormatException nfe) {

								LOGGER.error("Invalid Formula");
								formulaValidationBeanList = new ArrayList<>();
								formulaValidationBean = new FormulaValidationBean();
								formulaValidationBean.setErrorMsgCode(ErrorCode.E1281.toString());
								formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1281.toString()));
								formulaValidationBean.setSheetRow(counter);
								formulaValidationBeanList.add(formulaValidationBean);
								dataTemplateUploadValidationDetail.setFormulaValidationBeanList(formulaValidationBeanList);
								throw new ApplicationException(ErrorCode.E1281.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));

							}

						}
						if (!checkFlag) {
							break;
						}
					}

					formulaBean.setDependentList(dependentList);
					if (formulaBean != null && !StringUtils.isBlank(formulaBean.getFormulaType())) {

						counter++;
						String[] arr = formulaBean.getLHS().trim().split(" ");
						String cellReference = arr[0].replace("#", "");
						Integer key = null;
						if (!StringUtils.isEmpty(cellReference.trim())) {
							key = Integer.parseInt(cellReference.trim());
						}
						if (cellRefFormulaMap.containsKey(key)) {
							List<FormulaBean> formulaList = cellRefFormulaMap.get(key);
							formulaList.add(formulaBean);
						} else {
							List<FormulaBean> formulaList = new ArrayList<>();
							formulaList.add(formulaBean);
							cellRefFormulaMap.put(key, formulaList);
						}

					}
				}
				formulaValidationBeanList = new ArrayList<>();
				Iterator<Map.Entry<Integer, List<FormulaBean>>> itr = cellRefFormulaMap.entrySet().iterator();

				while (itr.hasNext()) {
					Map.Entry<Integer, List<FormulaBean>> entry = itr.next();
					Integer cellRef = entry.getKey();
					List<FormulaBean> formulaList = entry.getValue();
					String formulaJson = gson.toJson(formulaList);
					cellRefFormulaJsonMap.put(cellRef, formulaJson);
				}

				Iterator<Map.Entry<Integer, List<String>>> itr2 = elementCellToModelMap.entrySet().iterator();
				while (itr2.hasNext()) {
					Map.Entry<Integer, List<String>> entry2 = itr2.next();
					Integer cellRef = entry2.getKey();
					String isDependent = entry2.getValue().get(1);
					/*
					 * if (isDependent.equals("1")) { if
					 * (cellRefFormulaJsonMap.containsKey(cellRef)) { if
					 * (!cellRefFormulaJsonMap.get(cellRef).contains(ColumnConstants.CALCULATION.
					 * getConstantVal())) {
					 * LOGGER.error("CALCULATION not present for Dependent Cell reference : " +
					 * cellRef); formulaErrorLogList.
					 * add("CALCULATION not present for Dependent Cell reference : " + cellRef);
					 * 
					 * formulaValidationBean = new FormulaValidationBean();
					 * formulaValidationBean.setErrorMsgCode(ErrorCode.E1279.toString());
					 * formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.
					 * E1279.toString()));
					 * formulaValidationBean.setCellRef(cellRef.toString().replace("#", ""));
					 * formulaValidationBean.setDmId(elementCellToModelMap.get(cellRef).get(0));
					 * formulaValidationBeanList.add(formulaValidationBean);
					 * 
					 * } } else {
					 * LOGGER.error("CALCULATION not present for Dependent Cell reference " +
					 * cellRef); formulaErrorLogList.
					 * add("CALCULATION not present for Dependent Cell reference : " + cellRef);
					 * 
					 * formulaValidationBean = new FormulaValidationBean();
					 * formulaValidationBean.setErrorMsgCode(ErrorCode.E1279.toString());
					 * formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.
					 * E1279.toString()));
					 * formulaValidationBean.setCellRef(cellRef.toString().replace("#", ""));
					 * formulaValidationBean.setDmId(elementCellToModelMap.get(cellRef).get(0));
					 * formulaValidationBeanList.add(formulaValidationBean);
					 * 
					 * } } else
					 */
					if (!isDependent.equals("1")) {
						if (cellRefFormulaJsonMap.containsKey(cellRef)) {
							if (cellRefFormulaJsonMap.get(cellRef).contains(ColumnConstants.CALCULATION.getConstantVal())) {
								LOGGER.error("CALCULATION not allowed for Independent cell reference : " + cellRef);
								formulaErrorLogList.add("CALCULATION not allowed for Independent cell reference : " + cellRef);

								formulaValidationBean = new FormulaValidationBean();
								formulaValidationBean.setErrorMsgCode(ErrorCode.E1280.toString());
								formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1280.toString()));
								formulaValidationBean.setCellRef(cellRef.toString().replace("#", ""));
								formulaValidationBean.setDmId(elementCellToModelMap.get(cellRef).get(0));
								formulaValidationBeanList.add(formulaValidationBean);

							}
						}
					}

				}
				// formula logs

				for (Map.Entry<String, String> map : evalDMIds.entrySet()) {

					if (!calDMIDMap.containsKey(map.getKey())) {
						formulaValidationBean = new FormulaValidationBean();
						formulaValidationBean.setErrorMsgCode(ErrorCode.E1270.toString());
						formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1270.toString()));
						formulaValidationBean.setDmId(map.getKey());
						formulaValidationBeanList.add(formulaValidationBean);

						formulaErrorLogList.add("No CALCULATION node found for DMID: " + map.getKey() + ":::" + map.getValue());
					}
				}

				for (Map.Entry<String, List<String>> map : calDMIDMap.entrySet()) {
					if (map.getValue().size() > 1) {
						formulaValidationBean = new FormulaValidationBean();
						formulaValidationBean.setErrorMsgCode(ErrorCode.E1271.toString());
						formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1271.toString()));
						formulaValidationBean.setCellRef(map.getValue().get(0).toString().replace("#", ""));
						formulaValidationBean.setDmId(map.getKey());
						formulaValidationBeanList.add(formulaValidationBean);

						formulaErrorLogList.add("[CALCULATION] Duplicate cell reference: " + map.getValue() + " for DMID: " + map.getKey());
					}
				}
				if (!formulaErrorLogList.isEmpty()) {
					dataTemplateUploadValidationDetail.setFormulaValidationBeanList(formulaValidationBeanList);
					for (String formulaLog : formulaErrorLogList) {
						LOGGER.error(formulaLog);
					}
					throw new ApplicationException(ErrorCode.E0692.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));

				}
				// formula logs
			} catch (IOException ex) {
				LOGGER.error("ERROR ocurred while reading cellreference and formula json Map -" + ex);
				// throw new ApplicationException(ErrorCode.E0692.toString(),
				// ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));
			}

		} catch (Exception e) {
			LOGGER.error("ERROR ocurred while creating cellreference and formula json Map -", e);
			throw new ApplicationException(ErrorCode.E0692.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));
		}
		LOGGER.info("END - @getCellRefFormulaMapping");
		return cellRefFormulaJsonMap;

	}

	public Map<Integer, List<String>> getElementCellToModelMap(Long returnPreviewId) {
		Map<Integer, List<String>> elementCellToModelMap = new HashMap<>();
		List<SdmxModelCodesEntity> modelDimList = new ArrayList<>();
		modelDimList = sdmxModelCodesRepo.fetchModelDim(returnPreviewId);
		List<String> innerList;
		int dependentFlag;

		if (modelDimList != null) {
			if (!modelDimList.isEmpty()) {
				for (SdmxModelCodesEntity modelDim : modelDimList) {
					innerList = new ArrayList<>();
					innerList.add(modelDim.getModelCode());
					DimensionDetailCategories modelDimJson = JsonUtility.getGsonObject().fromJson(modelDim.getModelDim(), DimensionDetailCategories.class);
					if (modelDimJson.getModelOtherDetails().getDependencyType().equals(ColumnConstants.DEPENDENT.getConstantVal())) {
						dependentFlag = 1;
						LOGGER.debug("Dependent DMID: " + modelDim.getModelCode() + " Cell:" + modelDim.getReturnCellRef());

					} else {
						dependentFlag = 0;
					}
					innerList.add("" + dependentFlag);
					elementCellToModelMap.put(modelDim.getReturnCellRef(), innerList);

				}
			}
		}
		LOGGER.info("Number of records in elementCellToModelMap " + elementCellToModelMap.size());
		return elementCellToModelMap;
	}

	private static StringBuilder checkContent(String val) {
		StringBuilder finalVal = new StringBuilder();
		String internalChar;
		for (int i = 0; i < val.length(); i++) {
			internalChar = "" + val.charAt(i);
			if (internalChar.equalsIgnoreCase("(") || internalChar.equalsIgnoreCase(")") || internalChar.equalsIgnoreCase("+") || internalChar.equalsIgnoreCase("-") || internalChar.equalsIgnoreCase(",")) {
				internalChar = " " + val.charAt(i) + " ";
			}

			finalVal.append(internalChar);
		}
		return finalVal;
	}

}
