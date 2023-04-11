package com.iris.ebr.business.technical.metadata.bean;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iris.ebr.business.technical.metadata.constant.ExcelContstant;

public class ReadReturnCSVForEBRMapping {
	private static final String SAMPLE_CSV_FILE_PATH = "D:\\item_master_R057_ALE_FID.csv";
	private static final Logger LOGGER = LogManager.getLogger(ReadReturnCSVForEBRMapping.class);

	public static void main(String[] args) throws Exception {
		readDataFromCustomSeperator(SAMPLE_CSV_FILE_PATH);
	}

	public static List<CSVItemMappingBean> readDataFromCustomSeperator(String excelFile) throws Exception {

		LOGGER.info("file Name " + excelFile);
		XSSFWorkbook myWorkBook = null;
		XSSFSheet mySheet = null;

		List<CSVItemMappingBean> csvItemMappingBeans = new ArrayList<>();
		csvItemMappingBeans.clear();

		FileInputStream fis = new FileInputStream(excelFile);
		try {
			myWorkBook = new XSSFWorkbook(fis);
			mySheet = myWorkBook.getSheetAt(0);
		} catch (Exception e) {
			LOGGER.info("Item Master reading Error " + e);
		} finally {
			myWorkBook.close();
		}

		CSVItemMappingBean csvItemMappingBean;

		int conceptIndexNum = 0;
		int typeDimIndexNum = 0;
		int typeMemIndexNum = 0;
		int columnNameIndexNum = 0;
		int dimIndexNum = 0;
		int memberIndexNum = 0;
		int tableNameIndexNum = 0;
		int itemCodeIndexNum = 0;
		int templateSheetNoIndexNum = 0;
		int sdmxCellRefIndexNum = 0;

		int count = 0;

		List<String> headerData = new ArrayList<>();

		for (Row row : mySheet) {
			if (count == 0) {

				for (int i = 0; i < row.getLastCellNum(); i++) {
					Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					headerData.add(cell.getStringCellValue());
				}

				for (int j = 0; j < headerData.size(); j++) {
					if (headerData.get(j).equalsIgnoreCase("concept")) {
						conceptIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("typedimension")) {
						typeDimIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("typemember")) {
						typeMemIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("colname")) {
						columnNameIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("dimension")) {
						dimIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("members")) {
						memberIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("tablename")) {
						tableNameIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("item_code")) {
						itemCodeIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("template_sheetno")) {
						templateSheetNoIndexNum = j;
					} else if (headerData.get(j).equalsIgnoreCase("SDMXCellRef")) {
						sdmxCellRefIndexNum = j;
					}
				}
			} else {
				csvItemMappingBean = new CSVItemMappingBean();
				if (row.getCell(conceptIndexNum) != null) {
					csvItemMappingBean.setConcept(row.getCell(conceptIndexNum).getStringCellValue());
				} else {
					csvItemMappingBean.setConcept(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(typeDimIndexNum) != null) {

					Cell cell = row.getCell(typeDimIndexNum);

					if (cell.getCellType() == cell.CELL_TYPE_STRING) {
						csvItemMappingBean.setTypeDimension(row.getCell(typeDimIndexNum).getStringCellValue());
					} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
						int typeIndexTemp = (int) row.getCell(typeDimIndexNum).getNumericCellValue();
						csvItemMappingBean.setTypeDimension(String.valueOf(typeIndexTemp));
					}

				} else {
					csvItemMappingBean.setTypeDimension(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(typeMemIndexNum) != null) {
					csvItemMappingBean.setTypeMember(row.getCell(typeMemIndexNum).getStringCellValue());
				} else {
					csvItemMappingBean.setTypeMember(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(columnNameIndexNum) != null) {
					csvItemMappingBean.setColumnName(row.getCell(columnNameIndexNum).getStringCellValue());
				} else {
					csvItemMappingBean.setColumnName(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(dimIndexNum) != null) {
					csvItemMappingBean.setDimension(row.getCell(dimIndexNum).getStringCellValue());
				} else {
					csvItemMappingBean.setDimension(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(memberIndexNum) != null) {
					csvItemMappingBean.setMember(row.getCell(memberIndexNum).getStringCellValue());
				} else {
					csvItemMappingBean.setMember(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(tableNameIndexNum) != null) {

					Cell cell = row.getCell(tableNameIndexNum);

					if (cell.getCellType() == cell.CELL_TYPE_STRING) {
						csvItemMappingBean.setTableName(row.getCell(tableNameIndexNum).getStringCellValue());

					} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
						int typeIndexTemp1 = (int) row.getCell(tableNameIndexNum).getNumericCellValue();
						csvItemMappingBean.setTableName(String.valueOf(typeIndexTemp1));
					}

					//csvItemMappingBean.setTableName(row.getCell(tableNameIndexNum).getStringCellValue());
				} else {
					csvItemMappingBean.setTableName(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(itemCodeIndexNum) != null) {

					Cell cell = row.getCell(itemCodeIndexNum);

					if (cell.getCellType() == cell.CELL_TYPE_STRING) {
						csvItemMappingBean.setItemCode(row.getCell(itemCodeIndexNum).getStringCellValue());
					} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
						int itemCodeTemp = (int) row.getCell(itemCodeIndexNum).getNumericCellValue();
						csvItemMappingBean.setItemCode(String.valueOf(itemCodeTemp));
					}

					//String itemCodeTemp = Double.toString(row.getCell(itemCodeIndexNum).getNumericCellValue()); 

				} else {
					csvItemMappingBean.setItemCode(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(templateSheetNoIndexNum) != null) {

					Cell cell = row.getCell(templateSheetNoIndexNum);

					if (cell.getCellType() == cell.CELL_TYPE_STRING) {
						csvItemMappingBean.setTemplateSheetNo(row.getCell(templateSheetNoIndexNum).getStringCellValue());
					} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
						int templateSheetNoTemp = (int) row.getCell(templateSheetNoIndexNum).getNumericCellValue();
						csvItemMappingBean.setTemplateSheetNo(String.valueOf(templateSheetNoTemp));
					}

				} else {
					csvItemMappingBean.setTemplateSheetNo(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				}

				if (row.getCell(sdmxCellRefIndexNum) != null) {

					Cell cell = row.getCell(sdmxCellRefIndexNum);

					if (cell.getCellType() == cell.CELL_TYPE_STRING) {
						csvItemMappingBean.setSdmxCellRef(Integer.parseInt(row.getCell(sdmxCellRefIndexNum).getStringCellValue()));
					} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
						int itemCodeTemp = (int) row.getCell(sdmxCellRefIndexNum).getNumericCellValue();
						csvItemMappingBean.setSdmxCellRef(itemCodeTemp);
					}

				} else {
					csvItemMappingBean.setSdmxCellRef(0);
				}

				csvItemMappingBean.setRowNo(count);
				csvItemMappingBeans.add(csvItemMappingBean);
			}

			count++;

		}

		LOGGER.info("RBR CSV file Read And Convert To Bean");
		return csvItemMappingBeans;
	}

}
