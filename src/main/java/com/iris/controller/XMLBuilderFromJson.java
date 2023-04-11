/**
 * 
 */
package com.iris.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.iris.dto.XmlElementsDto;
import com.iris.util.constant.CsvConversionConstants;

/**
 * @author Siddique
 *
 */
public class XMLBuilderFromJson {

	static final Logger logger = LogManager.getLogger(XMLBuilderFromJson.class);

	//	public static void main(String[] args) {

	//		String json = "{\"R165-T008\":{\"dateCheck\":[{\"errType\":\"error\",\"errCode\":\"R000T00004\",\"formula\":\"0\",\"formulaType\":\"dateCheck\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"regex\":[{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"0\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"0\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"0\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"0\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"0\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"0\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"lengthChk\":[{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"0\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"0\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"0\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"0\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"0\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"0\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"summation\":[{\"errType\":\"error\",\"errCode\":\"ER165T06001352\",\"formula\":\"0\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T06001353\",\"formula\":\"0\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T008\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}]},\"R165-T006\":{\"dateCheck\":[{\"errType\":\"error\",\"errCode\":\"R000T00004\",\"formula\":\"2\",\"formulaType\":\"dateCheck\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"regex\":[{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"3\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"7\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"4\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"6\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"9\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"8\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"lengthChk\":[{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"3\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"7\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"4\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"6\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"9\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"8\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"summation\":[{\"errType\":\"error\",\"errCode\":\"ER165T06001350\",\"formula\":\" 9 \\u003d\\u003d 7 - 8\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T06001351\",\"formula\":\" 9 \\u003d\\u003d 7 - 8\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T006\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}]},\"R165-T005\":{\"dateCheck\":[{\"errType\":\"error\",\"errCode\":\"R000T00004\",\"formula\":\"2\",\"formulaType\":\"dateCheck\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"regex\":[{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"4\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"8\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"7\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"6\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"3\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"9\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"lengthChk\":[{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"4\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"8\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"7\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"6\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"3\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"9\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"summation\":[{\"errType\":\"error\",\"errCode\":\"ER165T060013356\",\"formula\":\" 9 \\u003d\\u003d 7 - 8\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T060013334\",\"formula\":\" 9 \\u003d\\u003d 7 - 8\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T005\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}]},\"R165-T004\":{\"regex\":[{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"2\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T004\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"1\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T004\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"lengthChk\":[{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"2\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T004\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"1\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T004\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}]},\"R165-T003\":{\"regex\":[{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"9\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"6\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"3\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"4\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"7\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"8\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"2\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"5\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"10\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"lengthChk\":[{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"9\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"6\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"3\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"4\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"7\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"8\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"2\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"5\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"10\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"summation\":[{\"errType\":\"error\",\"errCode\":\"ER165T06001333\",\"formula\":\" 8 \\u003d\\u003d 3 + 4 + 5 + 6 + 7\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T06001334\",\"formula\":\" 8 \\u003d\\u003d 3 + 4 + 5 + 6 + 7\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T06001335\",\"formula\":\" 8 \\u003d\\u003d 3 + 4 + 5 + 6 + 7\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T06001336\",\"formula\":\" 8 \\u003d\\u003d 3 + 4 + 5 + 6 + 7\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T06001337\",\"formula\":\" 8 \\u003d\\u003d 3 + 4 + 5 + 6 + 7\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T06001339\",\"formula\":\" 10 \\u003d\\u003d 8 + 9\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"ER165T060013340\",\"formula\":\" 10 \\u003d\\u003d 8 + 9\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T003\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}]},\"R165-T002\":{\"dateCheck\":[{\"errType\":\"error\",\"errCode\":\"R000T00004\",\"formula\":\"2\",\"formulaType\":\"dateCheck\",\"tableCode\":\"R165-T002\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"regex\":[{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"4\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T002\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"3\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T002\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00001\",\"formula\":\"1\",\"condition\":\"/[^0-9]/g\",\"formulaType\":\"regex\",\"tableCode\":\"R165-T002\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"lengthChk\":[{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"4\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T002\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"3\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T002\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false},{\"errType\":\"error\",\"errCode\":\"R000T00002\",\"formula\":\"1\",\"formulaType\":\"lengthChk\",\"tableCode\":\"R165-T002\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}],\"summation\":[{\"errType\":\"error\",\"errCode\":\"ER165T06001354\",\"formula\":\" 1 \\u003d\\u003d 3 *10\",\"formulaType\":\"summation\",\"tableCode\":\"R165-T002\",\"isTotalRowCountAvailable\":false,\"isGrandTotalAvailable\":false}]}}";
	//		Type listToken = new TypeToken<Map<String, Map<String, List<XmlElementsDto>>>>() {
	//
	//		}.getType();
	//		Map<String, Map<String, List<XmlElementsDto>>> dataMap = new Gson().fromJson(json, listToken);
	//		String path = "E:/Sidd/Projects/Docs/RBI/business_Validation_Local/" + "/" + "R165" + "/" + "CSV_FILE" + "/"
	//				+ "1.0.0" + "/formulaXml.xml";
	//
	//		buildXmlFile(dataMap, path);
	//	}

	public static void buildXmlFile(Map<String, Map<String, List<XmlElementsDto>>> dataMap, String path) {

		logger.info("request received to build xml");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			// root element
			Element parentRootElement = doc.createElement(CsvConversionConstants.FIELD_VALIDATE_RULE.getConstantVal());
			doc.appendChild(parentRootElement);

			int id = 0;
			Attr attr = null;
			Transformer transformer = null;
			DOMSource source = null;
			for (Map.Entry<String, Map<String, List<XmlElementsDto>>> outerMap : dataMap.entrySet()) {

				for (Map.Entry<String, List<XmlElementsDto>> innerMap : outerMap.getValue().entrySet()) {
					Element rootElement = doc.createElement(CsvConversionConstants.VALIDATE_FORMULA.getConstantVal());
					parentRootElement.appendChild(rootElement);

					id++;
					for (XmlElementsDto xmlElementsDtos : innerMap.getValue()) {

						attr = doc.createAttribute(CsvConversionConstants.ID.getConstantVal());
						attr.setValue(String.valueOf(id));
						rootElement.setAttributeNode(attr);

						attr = doc.createAttribute(CsvConversionConstants.FORMULA_TYPE.getConstantVal());
						attr.setValue(xmlElementsDtos.getFormulaType());
						rootElement.setAttributeNode(attr);

						attr = doc.createAttribute(CsvConversionConstants.TABLE_CODE.getConstantVal());
						attr.setValue(xmlElementsDtos.getTableCode());
						rootElement.setAttributeNode(attr);

						if (xmlElementsDtos.isGrandTotalAvailable()) {
							attr = doc.createAttribute(CsvConversionConstants.GRAND_TOTAL_AVAILABLE.getConstantVal());
							attr.setValue(CsvConversionConstants.TRUE.getConstantVal());
							rootElement.setAttributeNode(attr);
						}

						if (xmlElementsDtos.isTotalRowCountAvailable()) {
							attr = doc.createAttribute(CsvConversionConstants.TOTAL_ROW_COUNT_AVAILABLE.getConstantVal());
							attr.setValue(CsvConversionConstants.TRUE.getConstantVal());
							rootElement.setAttributeNode(attr);
						}

						Element childEleName = doc.createElement(CsvConversionConstants.ELEMENT.getConstantVal());
						attr = doc.createAttribute(CsvConversionConstants.ERROR_TYPE.getConstantVal());
						attr.setValue(xmlElementsDtos.getErrType());
						childEleName.setAttributeNode(attr);

						attr = doc.createAttribute(CsvConversionConstants.FORMULA.getConstantVal());
						attr.setValue(xmlElementsDtos.getFormula());
						childEleName.setAttributeNode(attr);

						attr = doc.createAttribute(CsvConversionConstants.ERR_CODE.getConstantVal());
						attr.setValue(xmlElementsDtos.getErrCode());
						childEleName.setAttributeNode(attr);

						if (StringUtils.isNotBlank(xmlElementsDtos.getCondition())) {
							attr = doc.createAttribute(CsvConversionConstants.CONDITION.getConstantVal());
							attr.setValue(xmlElementsDtos.getCondition());
							childEleName.setAttributeNode(attr);
						}

						if (StringUtils.isNotBlank(xmlElementsDtos.getConditionTag())) {
							attr = doc.createAttribute(CsvConversionConstants.CONDITION_TAG.getConstantVal());
							attr.setValue(xmlElementsDtos.getConditionTag());
							childEleName.setAttributeNode(attr);
						}

						if (StringUtils.isNotBlank(xmlElementsDtos.getFormulaTag())) {
							attr = doc.createAttribute(CsvConversionConstants.FORMULA_TAG.getConstantVal());
							attr.setValue(xmlElementsDtos.getFormulaTag());
							childEleName.setAttributeNode(attr);
						}

						if (xmlElementsDtos.getFormulaType().equals(CsvConversionConstants.LENGTH_CHECK.getConstantVal()) || xmlElementsDtos.getMinLength() > 0 || xmlElementsDtos.getMaxLength() > 0) {
							attr = doc.createAttribute(CsvConversionConstants.MIN_LENGTH.getConstantVal());
							attr.setValue(String.valueOf(xmlElementsDtos.getMinLength()));
							childEleName.setAttributeNode(attr);

							attr = doc.createAttribute(CsvConversionConstants.MAX_LENGTH.getConstantVal());
							attr.setValue(String.valueOf(xmlElementsDtos.getMaxLength()));
							childEleName.setAttributeNode(attr);

						}

						if (xmlElementsDtos.getFormulaType().equals(CsvConversionConstants.VALUE_CHECK.getConstantVal())) {
							attr = doc.createAttribute(CsvConversionConstants.METHOD_URI.getConstantVal());
							attr.setValue(xmlElementsDtos.getMethodURI());
							childEleName.setAttributeNode(attr);

							attr = doc.createAttribute(CsvConversionConstants.METHOD_TYPE.getConstantVal());
							attr.setValue(xmlElementsDtos.getMethodType());
							childEleName.setAttributeNode(attr);

							if (StringUtils.isNotBlank(xmlElementsDtos.getIsKeyFetch())) {
								attr = doc.createAttribute(CsvConversionConstants.IS_KEY_FETCH.getConstantVal());
								attr.setValue(xmlElementsDtos.getIsKeyFetch());
								childEleName.setAttributeNode(attr);
							}

							if (StringUtils.isNotBlank(xmlElementsDtos.getIsPairCheck())) {
								attr = doc.createAttribute(CsvConversionConstants.IS_PAIR_CHECK.getConstantVal());
								attr.setValue(xmlElementsDtos.getIsPairCheck());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.SPLIT_TYPE.getConstantVal());
								attr.setValue(xmlElementsDtos.getSplitType());
								childEleName.setAttributeNode(attr);
							}

						}

						if (xmlElementsDtos.getFormulaType().equals(CsvConversionConstants.VALUE_CHECK_REQ_BODY.getConstantVal())) {
							attr = doc.createAttribute(CsvConversionConstants.METHOD_URI.getConstantVal());
							attr.setValue(xmlElementsDtos.getMethodURI());
							childEleName.setAttributeNode(attr);

							attr = doc.createAttribute(CsvConversionConstants.METHOD_TYPE.getConstantVal());
							attr.setValue(xmlElementsDtos.getMethodType());
							childEleName.setAttributeNode(attr);

							if (StringUtils.isNotBlank(xmlElementsDtos.getIsKeyFetch())) {
								attr = doc.createAttribute(CsvConversionConstants.IS_KEY_FETCH.getConstantVal());
								attr.setValue(xmlElementsDtos.getIsKeyFetch());
								childEleName.setAttributeNode(attr);
							}

						}

						if (xmlElementsDtos.getFormulaType().equals(CsvConversionConstants.ROW_COUNT_COMPARE.getConstantVal())) {
							attr = doc.createAttribute(CsvConversionConstants.MAX_ROW_COUNT.getConstantVal());
							attr.setValue(xmlElementsDtos.getMaxRowCount());
							childEleName.setAttributeNode(attr);

						}

						if (StringUtils.isNotBlank(xmlElementsDtos.getIsRowPositionCheck()) && StringUtils.isNotBlank(xmlElementsDtos.getIsValidationOnParticularRow())) {
							attr = doc.createAttribute(CsvConversionConstants.IS_ROW_POSITION_CHECK.getConstantVal());
							attr.setValue(CsvConversionConstants.TRUE.getConstantVal());
							childEleName.setAttributeNode(attr);

							attr = doc.createAttribute(CsvConversionConstants.IS_VALIDATION_ON_PARTICULAR_ROW.getConstantVal());
							attr.setValue(CsvConversionConstants.TRUE.getConstantVal());
							childEleName.setAttributeNode(attr);

							attr = doc.createAttribute(CsvConversionConstants.IS_NULL_ALLOW.getConstantVal());
							attr.setValue(CsvConversionConstants.TRUE.getConstantVal());
							childEleName.setAttributeNode(attr);

						}
						if (StringUtils.isNotBlank(xmlElementsDtos.getIsNullAllow())) {
							attr = doc.createAttribute(CsvConversionConstants.IS_NULL_ALLOW.getConstantVal());
							attr.setValue(xmlElementsDtos.getIsNullAllow());
							childEleName.setAttributeNode(attr);
						}

						if (xmlElementsDtos.getFormulaType().equals(CsvConversionConstants.UNIQUE_VALUE_CHECK.getConstantVal())) {
							attr = doc.createAttribute(CsvConversionConstants.IS_UNIQUE_VALUE_CHECK.getConstantVal());
							attr.setValue(String.valueOf(xmlElementsDtos.getIsUniqueValueCheck()));
							childEleName.setAttributeNode(attr);
							if (StringUtils.isNotBlank(xmlElementsDtos.getIsPairCheck())) {
								attr = doc.createAttribute(CsvConversionConstants.IS_PAIR_CHECK.getConstantVal());
								attr.setValue(xmlElementsDtos.getIsPairCheck());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.SPLIT_TYPE.getConstantVal());
								attr.setValue(xmlElementsDtos.getSplitType());
								childEleName.setAttributeNode(attr);
							}
						}

						if (xmlElementsDtos.getFormulaType().equals(CsvConversionConstants.DATE_COMP.getConstantVal())) {

							if (StringUtils.isNotBlank(xmlElementsDtos.getIsMonthComparison())) {
								attr = doc.createAttribute(CsvConversionConstants.CONDITION.getConstantVal());
								attr.setValue(xmlElementsDtos.getCondition());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.REPORTED_DATE_FORMAT.getConstantVal());
								attr.setValue(xmlElementsDtos.getReportedDateFormat());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.CONDITION_TAG.getConstantVal());
								attr.setValue(xmlElementsDtos.getConditionTag());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.FORMULA_TAG.getConstantVal());
								attr.setValue(xmlElementsDtos.getFormulaTag());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.IS_MONTH_COMPARISON.getConstantVal());
								attr.setValue(xmlElementsDtos.getIsMonthComparison());
								childEleName.setAttributeNode(attr);
							}
							if (StringUtils.isNotBlank(xmlElementsDtos.getIsYearComparison())) {
								attr = doc.createAttribute(CsvConversionConstants.CONDITION.getConstantVal());
								attr.setValue(xmlElementsDtos.getCondition());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.REPORTED_DATE_FORMAT.getConstantVal());
								attr.setValue(xmlElementsDtos.getReportedDateFormat());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.CONDITION_TAG.getConstantVal());
								attr.setValue(xmlElementsDtos.getConditionTag());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.FORMULA_TAG.getConstantVal());
								attr.setValue(xmlElementsDtos.getFormulaTag());
								childEleName.setAttributeNode(attr);

								attr = doc.createAttribute(CsvConversionConstants.IS_YEAR_COMPARISON.getConstantVal());
								attr.setValue(xmlElementsDtos.getIsYearComparison());
								childEleName.setAttributeNode(attr);
							}
						}

						attr = doc.createAttribute(CsvConversionConstants.FORMULA_ROUNDING.getConstantVal());
						attr.setValue(CsvConversionConstants.NUMERIC_TWO.getConstantVal());

						childEleName.setAttributeNode(attr);
						rootElement.appendChild(childEleName);

					}
				}

			}

			logger.info("File creation completed");
			// write the content into xml file

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));
			transformer.transform(source, result);

			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
		} catch (Exception e) {
			logger.error("Exception occoured while building xml file", e);
		}
	}

}
