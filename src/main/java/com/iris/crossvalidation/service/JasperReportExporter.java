package com.iris.crossvalidation.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iris.crossvalidation.service.dto.CrossValidationDto;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * This class is responsible for generating reports
 * 
 * @author svishwakarma
 *
 */
public class JasperReportExporter {

	//private static String HTMLREPORT = "crossSubReport.jrxml";//"/crossValidationReport.jrxml";
	private static String HTMLREPORT = "crossValidationReport.jrxml";
	private static String LabelParam = "label";

	private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportExporter.class);

	/**
	 * This method will export the report
	 * 
	 * @param reportType
	 * @param errorJson
	 * @param metaInfo
	 * @param labelInfo
	 * @param outputFile
	 * @throws JRException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String prepareAndExportReport(LabelForReport labelInfo, String outputFile, List<CrossValidationDto> reportInputList) throws JRException, JsonParseException, JsonMappingException, IOException {
		String htmlFileString = "";
		InputStream reportStream = null;
		JRDataSource beanCollectionDataSource = null;
		String csvReportPath = null;

		csvReportPath = JasperReportExporter.HTMLREPORT;

		LOGGER.info("Jasper Report Exporter -- > Csv Report Path - " + csvReportPath);
		reportStream = getClass().getClassLoader().getResourceAsStream(csvReportPath);
		JasperDesign design = JRXmlLoader.load(reportStream);
		beanCollectionDataSource = new JRBeanCollectionDataSource(reportInputList);
		JasperReport jasperReport = JasperCompileManager.compileReport(design);
		Map<String, Object> parameters = new HashMap<>();
		//parameters.put(JasperReportExporter.LabelParam, labelInfo);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);
		JasperExportManager.exportReportToHtmlFile(jasperPrint, outputFile);
		LOGGER.info("Jasper Report Exporter -- > Html file String - " + htmlFileString.length());
		return outputFile;
	}

}
