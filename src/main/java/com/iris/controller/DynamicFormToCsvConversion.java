package com.iris.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.iris.dto.ApiDetails;
import com.iris.dto.ColumnBean;
import com.iris.dto.DynamicFormBean;
import com.iris.dto.ElementBean;
import com.iris.dto.HeaderBean;
import com.iris.dto.ReturnCsvInfo;
import com.iris.dto.TableBean;
import com.iris.dto.TableElementMetaInfo;
import com.iris.exception.ServiceException;
import com.iris.model.Return;
import com.iris.model.ReturnTableMap;
import com.iris.model.ReturnTemplate;
import com.iris.repository.ReturnTableMapRepo;
import com.iris.service.impl.DynamicEleColService;
import com.iris.service.impl.ReturnTableMapService;
import com.iris.util.constant.CsvConversionConstants;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Siddique
 *
 */
@Component
public class DynamicFormToCsvConversion {

	static final Logger logger = LogManager.getLogger(DynamicFormToCsvConversion.class);

	@Autowired
	private DynamicEleColService dynamicEleColService;

	@Autowired
	private ReturnTableMapService returnTableMapService;

	@Autowired
	private ReturnTableMapRepo returnTableMapRepo;

	public static final long ABSTRACT_FIELD_ID = 9;
	public static final long TEXT_FIELD_ID = 1;
	public static final long REPORTING_END_DATE_POSITION = 6;

	//	public static void main(String[] args) throws IOException {

	//		DynamicFormToCsvConversion dynamicFormToCsvConversion = new DynamicFormToCsvConversion();
	//		
	//		// R208
	//		String jsonString = "{\"jsonFormData\":{\"dynamicTableList\":[{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":2525,\"eleLabel\":\"Return Name\",\"eleTag\":\"ReturnName\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":false,\"eleOrder\":1.0},{\"eleId\":2526,\"eleLabel\":\"Return Code\",\"eleTag\":\"ReturnCode\",\"regex\":\"/[^A-Za-z0-9]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumeric\",\"isParent\":false,\"eleOrder\":2.0},{\"eleId\":2527,\"eleLabel\":\"Reporting Institution\",\"eleTag\":\"ReportingInstitution\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":3.0},{\"eleId\":2528,\"eleLabel\":\"Bank Code\",\"eleTag\":\"BankCode\",\"regex\":\"/[^A-Za-z0-9]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumeric\",\"isParent\":false,\"eleOrder\":4.0},{\"eleId\":2529,\"eleLabel\":\"Reporting Start Date\",\"eleTag\":\"ReportingStartDate\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":5.0},{\"eleId\":2530,\"eleLabel\":\"Reporting End Date\",\"eleTag\":\"ReportingEndDate\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":6.0},{\"eleId\":2531,\"eleLabel\":\"Reporting Frequency\",\"eleTag\":\"ReportingFrequency\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":false,\"eleOrder\":7.0}],\"dataInfo\":{},\"tableLabel\":\"Filing Information\",\"tableId\":8049,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":204,\"isTransposeInCSVLayout\":true},{\"langList\":[],\"columnList\":[{\"colId\":119,\"colTag\":\"Rslakh\",\"colLabel\":\"Amount (Rs.)\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":2.0,\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\"}],\"elementList\":[{\"eleId\":121,\"eleLabel\":\"Purchases during the week\",\"eleTag\":\"Purchasesduringtheweek\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":1.0},{\"eleId\":2564,\"eleLabel\":\"In primary market\",\"eleTag\":\"in1\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":9,\"dataTypeId\":0,\"isParent\":true,\"parentId\":\"121\",\"eleOrder\":2.0},{\"eleId\":111,\"eleLabel\":\"Total\",\"eleTag\":\"Tota\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2564\",\"eleOrder\":1.0},{\"eleId\":138,\"eleLabel\":\"Of which exempted\",\"eleTag\":\"Ofwhichexempted\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2564\",\"eleOrder\":2.0},{\"eleId\":2565,\"eleLabel\":\"In secondary market\",\"eleTag\":\"In2\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":9,\"dataTypeId\":0,\"isParent\":true,\"parentId\":\"121\",\"eleOrder\":3.0},{\"eleId\":131,\"eleLabel\":\"Total\",\"eleTag\":\"Total1\",\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\",\"isParent\":false,\"parentId\":\"2565\",\"eleOrder\":1.0},{\"eleId\":2569,\"eleLabel\":\"Of which exempted\",\"eleTag\":\"Ofexempted\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2565\",\"eleOrder\":2.0},{\"eleId\":2566,\"eleLabel\":\"Sales during the Week\",\"eleTag\":\"Sales1\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":9,\"dataTypeId\":0,\"isParent\":true,\"eleOrder\":4.0},{\"eleId\":1198,\"eleLabel\":\"Total\",\"eleTag\":\"Total12\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2566\",\"eleOrder\":1.0},{\"eleId\":2568,\"eleLabel\":\"Of which exempted\",\"eleTag\":\"Ofwhich\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2566\",\"eleOrder\":2.0},{\"eleId\":2567,\"eleLabel\":\"Net Investments during the week - Total\",\"eleTag\":\"Netnvestments\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":6.0},{\"eleId\":2570,\"eleLabel\":\"Outstanding balance at week end - Total\",\"eleTag\":\"Outstandingba\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":7.0}],\"dataInfo\":{},\"tableLabel\":\"Equity Investments in Capital Market* (Weekly)\",\"tableId\":8050,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":205,\"isTransposeInCSVLayout\":true}],\"headerList\":[{\"headerId\":204,\"headerLabel\":\"Filing Information\",\"headerOrder\":1.0},{\"headerId\":205,\"headerLabel\":\"Equity Investments in Capital Market* (Weekly)\",\"headerOrder\":2.0}]},\"structureLabel\":{\"Total12\":\"Total\",\"ReturnCode\":\"Return Code\",\"Total1\":\"Total\",\"ReportingStartDate\":\"Reporting Start Date\",\"Outstandingba\":\"Outstanding balance at week end - Total\",\"8049\":\"Filing Information\",\"ReportingFrequency\":\"Reporting Frequency\",\"Rslakh\":\"Amount (Rs.)\",\"8050\":\"Equity Investments in Capital Market* (Weekly)\",\"BankCode\":\"Bank Code\",\"Ofexempted\":\"Of which exempted\",\"In2\":\"In secondary market\",\"in1\":\"In primary market\",\"Sales1\":\"Sales during the Week\",\"Tota\":\"Total\",\"ReportingInstitution\":\"Reporting Institution\",\"Netnvestments\":\"Net Investments during the week - Total\",\"ReportingEndDate\":\"Reporting End Date\",\"Purchasesduringtheweek\":\"Purchases during the week\",\"ReturnName\":\"Return Name\",\"Ofwhichexempted\":\"Of which exempted\",\"Ofwhich\":\"Of which exempted\"}}";
	//
	//		// R165
	////		String jsonString= "{\"jsonFormData\":{\"dynamicTableList\":[{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":2686,\"eleLabel\":\"Overall Borrowing Limit(10 times of NOF)\",\"eleTag\":\"OverallBorrowingLimi\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":4.0},{\"eleId\":3220,\"eleLabel\":\"Audited Balance Sheet dated\",\"eleTag\":\"AuditedBalanceSheetdateddate\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":3,\"dataTypeId\":0,\"isParent\":true,\"eleOrder\":4.5},{\"eleId\":2687,\"eleLabel\":\"NOF as per the Audited Balance Sheet dated\",\"eleTag\":\"aspertheAuditedBalanceSheetdatezx\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":5.0},{\"eleId\":2688,\"eleLabel\":\"Amount of Borrowing outstanding at the end of Month\",\"eleTag\":\"Amountofborrwingoutstandingatend\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":6.0}],\"dataInfo\":{},\"tableLabel\":\"Monthly Consolidated Return on Aggregate Resources Raised\",\"tableId\":738,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":127,\"isTransposeInCSVLayout\":true},{\"langList\":[],\"columnList\":[{\"colId\":2825,\"colTag\":\"Amount156\",\"colLabel\":\"Amount\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":1.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0}],\"elementList\":[{\"eleId\":2690,\"eleLabel\":\"�Instruments under �Umbrella Limit�\",\"eleTag\":\"instrumentdx\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":1.0},{\"eleId\":635,\"eleLabel\":\"Term Deposits\",\"eleTag\":\"TermDeposits\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2690\",\"eleOrder\":1.0},{\"eleId\":2692,\"eleLabel\":\"Term Money Borrowings\",\"eleTag\":\"termmoneyborrowingsd\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2690\",\"eleOrder\":2.0},{\"eleId\":2693,\"eleLabel\":\"Certificate of Deposits  [ CDs]\",\"eleTag\":\"CertificateofDepositssc\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2690\",\"eleOrder\":3.0},{\"eleId\":2694,\"eleLabel\":\"Inter- Corporate Deposits [ICDs]\",\"eleTag\":\"intercorporatedepositf\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2690\",\"eleOrder\":4.0},{\"eleId\":1046,\"eleLabel\":\"Commercial paper\",\"eleTag\":\"CommercialP\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":false,\"parentId\":\"2690\",\"eleOrder\":5.0},{\"eleId\":2697,\"eleLabel\":\"Total of \\u0027Instruments under �Umbrella Limit�\",\"eleTag\":\"Totalhbv\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":2.0},{\"eleId\":2696,\"eleLabel\":\"Bonds\",\"eleTag\":\"Bondsc\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":3.0},{\"eleId\":2699,\"eleLabel\":\"Total of Instruments under �Umbrella Limit� and Bonds\",\"eleTag\":\"totalinsbonds\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":4.0}],\"dataInfo\":{},\"tableLabel\":\"Resources raised during the Reporting Month\",\"tableId\":740,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":127,\"isTransposeInCSVLayout\":true},{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":2525,\"eleLabel\":\"Return Name\",\"eleTag\":\"ReturnName\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":true,\"eleOrder\":1.0},{\"eleId\":2526,\"eleLabel\":\"Return Code\",\"eleTag\":\"ReturnCode\",\"regex\":\"/[^A-Za-z0-9]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumeric\",\"isParent\":true,\"eleOrder\":2.0},{\"eleId\":2527,\"eleLabel\":\"Reporting Institution\",\"eleTag\":\"ReportingInstitution\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":true,\"eleOrder\":3.0},{\"eleId\":2528,\"eleLabel\":\"Bank Code\",\"eleTag\":\"BankCode\",\"regex\":\"/[^A-Za-z0-9]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumeric\",\"isParent\":true,\"eleOrder\":4.0},{\"eleId\":2529,\"eleLabel\":\"Reporting Start Date\",\"eleTag\":\"ReportingStartDate\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":true,\"eleOrder\":5.0},{\"eleId\":2530,\"eleLabel\":\"Reporting End Date\",\"eleTag\":\"ReportingEndDate\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":true,\"eleOrder\":6.0},{\"eleId\":2531,\"eleLabel\":\"Reporting Frequency\",\"eleTag\":\"ReportingFrequency\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":true,\"eleOrder\":7.0}],\"dataInfo\":{},\"tableLabel\":\"Filing Information\",\"tableId\":8042,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":197},{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":3221,\"eleLabel\":\"Total Amount Raised during the Month (Rs)\",\"eleTag\":\"totalamountraised\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":1.0},{\"eleId\":3222,\"eleLabel\":\"Cumulative Amount Raised during the Current FY\",\"eleTag\":\"cumulativeamountraisedd\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":2.0}],\"dataInfo\":{},\"tableLabel\":\"Monthly return on resources raised through Bonds\",\"tableId\":8096,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":244,\"isTransposeInCSVLayout\":true},{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":3436,\"eleLabel\":\"Type of Instrument\",\"eleTag\":\"TypeofInstrument13\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":2,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":1.0},{\"eleId\":3223,\"eleLabel\":\"Date of Issue of Bonds #\",\"eleTag\":\"DateofIssueofBondsdate\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":3,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":2.0},{\"eleId\":3224,\"eleLabel\":\"Amount raised during the Month (Rs. Crore) @\",\"eleTag\":\"AmountraisedduringtheMonthhs\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":3.0},{\"eleId\":3225,\"eleLabel\":\"Maturity\",\"eleTag\":\"Maturityy\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":4.0},{\"eleId\":3435,\"eleLabel\":\"Options\",\"eleTag\":\"Optionscall\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":2,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":5.0},{\"eleId\":3227,\"eleLabel\":\"Interest Rate (% Per Annum)\",\"eleTag\":\"InterestRatee\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":6.0},{\"eleId\":3228,\"eleLabel\":\"Annualised YTM Offered\",\"eleTag\":\"AnnualisedYTMOfferedd\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":7.0},{\"eleId\":3229,\"eleLabel\":\"Annualised YTM on GOI Securities (of equal residual maturity at the time of issue of bond)\",\"eleTag\":\"AnnualisedYTMonGOISecurities\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":8.0},{\"eleId\":3230,\"eleLabel\":\"Yield (basis points) above GOI Securities\",\"eleTag\":\"Yieldds\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":10.0}],\"dataInfo\":{},\"tableLabel\":\"A. Public Issue of Bonds\",\"tableId\":8097,\"langAxis\":false,\"isRepeatable\":true,\"isTranspose\":true,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":244,\"isTransposeInCSVLayout\":true},{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":3434,\"eleLabel\":\"Type of Instrument\",\"eleTag\":\"TypeofInstrument11\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":2,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":1.0},{\"eleId\":3223,\"eleLabel\":\"Date of Issue of Bonds #\",\"eleTag\":\"DateofIssueofBondsdate\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":3,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":2.0},{\"eleId\":3224,\"eleLabel\":\"Amount raised during the Month (Rs. Crore) @\",\"eleTag\":\"AmountraisedduringtheMonthhs\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":3.0},{\"eleId\":3225,\"eleLabel\":\"Maturity\",\"eleTag\":\"Maturityy\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":4.0},{\"eleId\":3435,\"eleLabel\":\"Options\",\"eleTag\":\"Optionscall\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":2,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":5.0},{\"eleId\":3227,\"eleLabel\":\"Interest Rate (% Per Annum)\",\"eleTag\":\"InterestRatee\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":6.0},{\"eleId\":3228,\"eleLabel\":\"Annualised YTM Offered\",\"eleTag\":\"AnnualisedYTMOfferedd\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":7.0},{\"eleId\":3229,\"eleLabel\":\"Annualised YTM on GOI Securities (of equal residual maturity at the time of issue of bond)\",\"eleTag\":\"AnnualisedYTMonGOISecurities\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":8.0},{\"eleId\":3230,\"eleLabel\":\"Yield (basis points) above GOI Securities\",\"eleTag\":\"Yieldds\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":9.0}],\"dataInfo\":{},\"tableLabel\":\"B. Private Placement of Bonds\",\"tableId\":8098,\"langAxis\":false,\"isRepeatable\":true,\"isTranspose\":true,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":244,\"isTransposeInCSVLayout\":true},{\"langList\":[],\"columnList\":[{\"colId\":3439,\"colTag\":\"AmountraisedduringtheMonthh\",\"colLabel\":\"Amount raised during the Month (Rs. Crore) @\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":1.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3440,\"colTag\":\"Maturity12\",\"colLabel\":\"Maturity\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":2.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3441,\"colTag\":\"InterestRate12\",\"colLabel\":\"Interest Rate (% Per Annum)\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":3.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3442,\"colTag\":\"AnnualisedYTMOffered12\",\"colLabel\":\"Annualised YTM Offered\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":4.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3443,\"colTag\":\"AnnualisedYTMonGOISecurities12\",\"colLabel\":\"Annualised YTM on GOI Securities (of equal residual maturity at the time of issue of bond)\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":5.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3444,\"colTag\":\"Yieldbasispoints12\",\"colLabel\":\"Yield (basis points) above GOI Securities\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":6.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0}],\"elementList\":[{\"eleId\":3437,\"eleLabel\":\"Sub-Total (A)\",\"eleTag\":\"SubTotalA\",\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\",\"isParent\":false,\"eleOrder\":1.0}],\"dataInfo\":{},\"tableLabel\":\"A. Public Issue of Bonds - Sub Total\",\"tableId\":8146,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":244,\"isTransposeInCSVLayout\":false},{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":3446,\"eleLabel\":\"Type of Instrument\",\"eleTag\":\"TypeofInstrument33\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":2,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":1.0},{\"eleId\":3223,\"eleLabel\":\"Date of Issue of Bonds #\",\"eleTag\":\"DateofIssueofBondsdate\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":3,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":2.0},{\"eleId\":3224,\"eleLabel\":\"Amount raised during the Month (Rs. Crore) @\",\"eleTag\":\"AmountraisedduringtheMonthhs\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":3.0},{\"eleId\":3225,\"eleLabel\":\"Maturity\",\"eleTag\":\"Maturityy\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":4.0},{\"eleId\":3435,\"eleLabel\":\"Options\",\"eleTag\":\"Optionscall\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":2,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":5.0},{\"eleId\":3227,\"eleLabel\":\"Interest Rate (% Per Annum)\",\"eleTag\":\"InterestRatee\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":6.0},{\"eleId\":3228,\"eleLabel\":\"Annualised YTM Offered\",\"eleTag\":\"AnnualisedYTMOfferedd\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":7.0},{\"eleId\":3229,\"eleLabel\":\"Annualised YTM on GOI Securities (of equal residual maturity at the time of issue of bond)\",\"eleTag\":\"AnnualisedYTMonGOISecurities\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":8.0},{\"eleId\":3230,\"eleLabel\":\"Yield (basis points) above GOI Securities\",\"eleTag\":\"Yieldds\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.numeric\",\"isParent\":false,\"eleOrder\":9.0}],\"dataInfo\":{},\"tableLabel\":\"B. Private Placement of Bonds -2\",\"tableId\":8147,\"langAxis\":false,\"isRepeatable\":true,\"isTranspose\":true,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":244,\"isTransposeInCSVLayout\":true},{\"langList\":[],\"columnList\":[{\"colId\":3439,\"colTag\":\"AmountraisedduringtheMonthh\",\"colLabel\":\"Amount raised during the Month (Rs. Crore) @\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":1.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3440,\"colTag\":\"Maturity12\",\"colLabel\":\"Maturity\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":2.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3441,\"colTag\":\"InterestRate12\",\"colLabel\":\"Interest Rate (% Per Annum)\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":3.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3442,\"colTag\":\"AnnualisedYTMOffered12\",\"colLabel\":\"Annualised YTM Offered\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":4.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3443,\"colTag\":\"AnnualisedYTMonGOISecurities12\",\"colLabel\":\"Annualised YTM on GOI Securities (of equal residual maturity at the time of issue of bond)\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":6.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3444,\"colTag\":\"Yieldbasispoints12\",\"colLabel\":\"Yield (basis points) above GOI Securities\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":7.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0}],\"elementList\":[{\"eleId\":3445,\"eleLabel\":\"Sub-Total (B)\",\"eleTag\":\"SubTotalB\",\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\",\"isParent\":false,\"eleOrder\":1.0}],\"dataInfo\":{},\"tableLabel\":\"B. Private Placement of Bonds - Sub Total\",\"tableId\":8148,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":244,\"isTransposeInCSVLayout\":false},{\"langList\":[],\"columnList\":[{\"colId\":3439,\"colTag\":\"AmountraisedduringtheMonthh\",\"colLabel\":\"Amount raised during the Month (Rs. Crore) @\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":1.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3440,\"colTag\":\"Maturity12\",\"colLabel\":\"Maturity\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":2.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3441,\"colTag\":\"InterestRate12\",\"colLabel\":\"Interest Rate (% Per Annum)\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":3.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3442,\"colTag\":\"AnnualisedYTMOffered12\",\"colLabel\":\"Annualised YTM Offered\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":4.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3443,\"colTag\":\"AnnualisedYTMonGOISecurities12\",\"colLabel\":\"Annualised YTM on GOI Securities (of equal residual maturity at the time of issue of bond)\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":5.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":3444,\"colTag\":\"Yieldbasispoints12\",\"colLabel\":\"Yield (basis points) above GOI Securities\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":6.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0}],\"elementList\":[{\"eleId\":3447,\"eleLabel\":\"Grand Total (A+B)\",\"eleTag\":\"GrandTotalAB\",\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\",\"isParent\":false,\"eleOrder\":1.0}],\"dataInfo\":{},\"tableLabel\":\"Grand Total (A+B)\",\"tableId\":8149,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":244,\"isTransposeInCSVLayout\":false}],\"headerList\":[{\"headerId\":197,\"headerLabel\":\"Filing Information\",\"headerOrder\":1.1},{\"headerId\":127,\"headerLabel\":\"Monthly Return on Aggregate Resources Raised - Annexure II\",\"headerOrder\":3.0},{\"headerId\":244,\"headerLabel\":\"Monthly return on resources raised through Bonds - Annexure III\",\"headerOrder\":4.0}]},\"structureLabel\":{\"TypeofInstrument11\":\"Type of Instrument\",\"TypeofInstrument13\":\"Type of Instrument\",\"termmoneyborrowingsd\":\"Term Money Borrowings\",\"CertificateofDepositssc\":\"Certificate of Deposits  [ CDs]\",\"InterestRate12\":\"Interest Rate (% Per Annum)\",\"AnnualisedYTMonGOISecurities\":\"Annualised YTM on GOI Securities (of equal residual maturity at the time of issue of bond)\",\"GrandTotalAB\":\"Grand Total (A+B)\",\"8097\":\"A. Public Issue of Bonds\",\"8098\":\"B. Private Placement of Bonds\",\"Amountofborrwingoutstandingatend\":\"Amount of Borrowing outstanding at the end of Month\",\"8096\":\"Monthly return on resources raised through Bonds\",\"AmountraisedduringtheMonthh\":\"Amount raised during the Month (Rs. Crore) @\",\"Bondsc\":\"Bonds\",\"CommercialP\":\"Commercial paper\",\"AmountraisedduringtheMonthhs\":\"Amount raised during the Month (Rs. Crore) @\",\"OverallBorrowingLimi\":\"Overall Borrowing Limit(10 times of NOF)\",\"8146\":\"A. Public Issue of Bonds - Sub Total\",\"ReturnCode\":\"Return Code\",\"8149\":\"Grand Total (A+B)\",\"8147\":\"B. Private Placement of Bonds -2\",\"ReportingFrequency\":\"Reporting Frequency\",\"8148\":\"B. Private Placement of Bonds - Sub Total\",\"AnnualisedYTMOfferedd\":\"Annualised YTM Offered\",\"Totalhbv\":\"Total of \\u0027Instruments under �Umbrella Limit�\",\"Maturityy\":\"Maturity\",\"TermDeposits\":\"Term Deposits\",\"TypeofInstrument33\":\"Type of Instrument\",\"Optionscall\":\"Options\",\"Yieldds\":\"Yield (basis points) above GOI Securities\",\"ReportingStartDate\":\"Reporting Start Date\",\"intercorporatedepositf\":\"Inter- Corporate Deposits [ICDs]\",\"Yieldbasispoints12\":\"Yield (basis points) above GOI Securities\",\"AnnualisedYTMOffered12\":\"Annualised YTM Offered\",\"ReportingEndDate\":\"Reporting End Date\",\"SubTotalA\":\"Sub-Total (A)\",\"SubTotalB\":\"Sub-Total (B)\",\"738\":\"Monthly Consolidated Return on Aggregate Resources Raised \",\"instrumentdx\":\"�Instruments under �Umbrella Limit�\",\"Maturity12\":\"Maturity\",\"Amount156\":\"Amount\",\"BankCode\":\"Bank Code\",\"aspertheAuditedBalanceSheetdatezx\":\"NOF as per the Audited Balance Sheet dated\",\"8042\":\"Filing Information\",\"AnnualisedYTMonGOISecurities12\":\"Annualised YTM on GOI Securities (of equal residual maturity at the time of issue of bond)\",\"DateofIssueofBondsdate\":\"Date of Issue of Bonds #\",\"ReportingInstitution\":\"Reporting Institution\",\"740\":\"Resources raised during the Reporting Month \",\"totalamountraised\":\"Total Amount Raised during the Month (Rs)\",\"InterestRatee\":\"Interest Rate (% Per Annum)\",\"cumulativeamountraisedd\":\"Cumulative Amount Raised during the Current FY\",\"ReturnName\":\"Return Name\",\"AuditedBalanceSheetdateddate\":\"Audited Balance Sheet dated\",\"totalinsbonds\":\"Total of Instruments under �Umbrella Limit� and Bonds\"}}";
	//
	//		// R011
	////		String jsonString= "{\"jsonFormData\":{\"dynamicTableList\":[{\"langList\":[],\"columnList\":[{\"colId\":1881,\"colTag\":\"distributed1\",\"colLabel\":\"Loan distributed to individual (SEP-I)\",\"level\":\"1\",\"child\":\"22,21\",\"colOrder\":1.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1882,\"colTag\":\"distributed2\",\"colLabel\":\"Loan distributed to groups(SEP-G)\",\"level\":\"1\",\"child\":\"1848,1890,23\",\"colOrder\":2.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1883,\"colTag\":\"Linkage1\",\"colLabel\":\"Total loan disbursed under SHG Bank Linkage\",\"level\":\"1\",\"child\":\"1852,1856,25\",\"colOrder\":3.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":31,\"colTag\":\"OutofSHGsLoandisbursedtoWomenSHGs\",\"colLabel\":\"Out of SHGs Loan disbursed to Women SHGs\",\"level\":\"1\",\"child\":\"1857,1905,1889\",\"colOrder\":4.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":32,\"colTag\":\"Achievement\",\"colLabel\":\"Achievement %\",\"level\":\"1\",\"child\":\"1891,1892\",\"colOrder\":5.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1886,\"colTag\":\"Subsidy2\",\"colLabel\":\"Total Subsidy given under NULM (SEP-I + SEP-G+SHGs)\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":6.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1887,\"colTag\":\"Additional2\",\"colLabel\":\"Additional Subvention to WSHG\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":7.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1888,\"colTag\":\"Physical2\",\"colLabel\":\"Physical Coverage (in nos.)\",\"level\":\"1\",\"child\":\"1895,39,1894,36\",\"colOrder\":8.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":21,\"colTag\":\"No\",\"colLabel\":\"No.\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":1.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":22,\"colTag\":\"Amt\",\"colLabel\":\"Amount\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":2.0,\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\"},{\"colId\":23,\"colTag\":\"NoofGroups\",\"colLabel\":\"No. of Groups\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":3.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1890,\"colTag\":\"Benificiaries1\",\"colLabel\":\"No. of Benificiaries\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":4.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1848,\"colTag\":\"AmtB\",\"colLabel\":\"Amount\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":5.0,\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\"},{\"colId\":25,\"colTag\":\"NoofSHGs\",\"colLabel\":\"No. of SHGs\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":6.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1856,\"colTag\":\"BenificiariesG\",\"colLabel\":\"No. of Benificiaries\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":7.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1852,\"colTag\":\"AmtE\",\"colLabel\":\"Amount\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":8.0,\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\"},{\"colId\":1889,\"colTag\":\"WSHGs1\",\"colLabel\":\"No of WSHGs\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":9.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1905,\"colTag\":\"Benificiariesnew\",\"colLabel\":\"No. of Benificiaries\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":10.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1857,\"colTag\":\"AmtH\",\"colLabel\":\"Amount\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":11.0,\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\"},{\"colId\":1891,\"colTag\":\"benificiariesunder\",\"colLabel\":\"Total no. of benificiaries under NULM\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":12.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1892,\"colTag\":\"disbursedunder1\",\"colLabel\":\"Total Amount disbursed under NULM\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":13.0,\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\"},{\"colId\":36,\"colTag\":\"Women\",\"colLabel\":\"Women\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":14.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1894,\"colTag\":\"scheduldd\",\"colLabel\":\"SC/STs\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":15.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":1895,\"colTag\":\"Differently4\",\"colLabel\":\"Differently abled\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":16.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":39,\"colTag\":\"Minorities\",\"colLabel\":\"Minorities\",\"level\":\"2\",\"child\":\"null\",\"colOrder\":17.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0}],\"elementList\":[{\"eleId\":911,\"eleLabel\":\"ANDAMAN AND NICOBAR\",\"eleTag\":\"ANDAMANNICOBAR1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":1.0},{\"eleId\":912,\"eleLabel\":\"ANDHRA PRADESH\",\"eleTag\":\"ANDHRAPRADESH1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":2.0},{\"eleId\":913,\"eleLabel\":\"ARUNACHAL PRADESH\",\"eleTag\":\"ARUNACHALPRADESH1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":3.0},{\"eleId\":914,\"eleLabel\":\"ASSAM\",\"eleTag\":\"ASSAM1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":4.0},{\"eleId\":915,\"eleLabel\":\"BIHAR\",\"eleTag\":\"BIHAR1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":5.0},{\"eleId\":118,\"eleLabel\":\"CHANDIGARH\",\"eleTag\":\"CHANDIGARH\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":6.0},{\"eleId\":917,\"eleLabel\":\"CHHATTISGARH\",\"eleTag\":\"CHHATTISGARH1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":7.0},{\"eleId\":918,\"eleLabel\":\"DADRA AND NAGAR HAVELI\",\"eleTag\":\"DADRANAGARHAVELI\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":8.0},{\"eleId\":919,\"eleLabel\":\"DAMAN AND DIU\",\"eleTag\":\"DAMANDIU1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":9.0},{\"eleId\":920,\"eleLabel\":\"DELHI\",\"eleTag\":\"DELHI1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":10.0},{\"eleId\":921,\"eleLabel\":\"GOA\",\"eleTag\":\"GOA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":11.0},{\"eleId\":922,\"eleLabel\":\"GUJARAT\",\"eleTag\":\"GUJARAT1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":12.0},{\"eleId\":923,\"eleLabel\":\"HARYANA\",\"eleTag\":\"HARYANA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":13.0},{\"eleId\":924,\"eleLabel\":\"HIMACHAL PRADESH\",\"eleTag\":\"HIMACHALRADESH1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":14.0},{\"eleId\":925,\"eleLabel\":\"JAMMU AND KASHMIR\",\"eleTag\":\"JAMMUKASHMIR1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":15.0},{\"eleId\":926,\"eleLabel\":\"JHARKHAND\",\"eleTag\":\"JHARKHAND1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":16.0},{\"eleId\":927,\"eleLabel\":\"KARNATAKA\",\"eleTag\":\"KARNATAKA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":17.0},{\"eleId\":928,\"eleLabel\":\"KERALA\",\"eleTag\":\"KERALA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":18.0},{\"eleId\":929,\"eleLabel\":\"LAKSHADWEEP\",\"eleTag\":\"LAKSHADWEEP1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":19.0},{\"eleId\":930,\"eleLabel\":\"MADHYA PRADESH\",\"eleTag\":\"MADHYAPRADESH1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":20.0},{\"eleId\":931,\"eleLabel\":\"MAHARASHTRA\",\"eleTag\":\"MAHARASHTRA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":21.0},{\"eleId\":932,\"eleLabel\":\"MANIPUR\",\"eleTag\":\"MANIPUR1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":22.0},{\"eleId\":933,\"eleLabel\":\"MEGHALAYA\",\"eleTag\":\"MEGHALAYA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":23.0},{\"eleId\":934,\"eleLabel\":\"MIZORAM\",\"eleTag\":\"MIZORAM1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":24.0},{\"eleId\":935,\"eleLabel\":\"NAGALAND\",\"eleTag\":\"NAGALAND1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":25.0},{\"eleId\":936,\"eleLabel\":\"ORISSA\",\"eleTag\":\"ORISSA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":26.0},{\"eleId\":937,\"eleLabel\":\"PUDUCHERRY\",\"eleTag\":\"PUDUCHERRY1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":27.0},{\"eleId\":938,\"eleLabel\":\"PUNJAB\",\"eleTag\":\"PUNJAB1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":28.0},{\"eleId\":939,\"eleLabel\":\"RAJASTHAN\",\"eleTag\":\"RAJASTHAN1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":29.0},{\"eleId\":940,\"eleLabel\":\"SIKKIM\",\"eleTag\":\"SIKKIM1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":30.0},{\"eleId\":941,\"eleLabel\":\"TAMIL NADU\",\"eleTag\":\"TAMILNADU1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":31.0},{\"eleId\":2288,\"eleLabel\":\"TELANGANA\",\"eleTag\":\"TELANGANA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":32.0},{\"eleId\":942,\"eleLabel\":\"TRIPURA\",\"eleTag\":\"TRIPURA1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":33.0},{\"eleId\":943,\"eleLabel\":\"UTTARAKHAND\",\"eleTag\":\"UTTARAKHAND1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":34.0},{\"eleId\":944,\"eleLabel\":\"UTTAR PRADESH\",\"eleTag\":\"UTTARPRADESH1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":35.0},{\"eleId\":945,\"eleLabel\":\"WEST BENGAL\",\"eleTag\":\"WESTBENGAL1\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":36.0},{\"eleId\":2279,\"eleLabel\":\"Grand Total\",\"eleTag\":\"Grandd\",\"regex\":\"/[^0-9]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":2,\"regXName\":\"error.numeric\",\"isParent\":true,\"eleOrder\":37.0}],\"dataInfo\":{},\"tableLabel\":\"NULM Progress\",\"tableId\":457,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":true,\"headerId\":80},{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":2525,\"eleLabel\":\"Return Name\",\"eleTag\":\"ReturnName\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":false,\"eleOrder\":1.0},{\"eleId\":2526,\"eleLabel\":\"Return Code\",\"eleTag\":\"ReturnCode\",\"regex\":\"/[^A-Za-z0-9]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumeric\",\"isParent\":false,\"eleOrder\":2.0},{\"eleId\":2527,\"eleLabel\":\"Reporting Institution\",\"eleTag\":\"ReportingInstitution\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":3.0},{\"eleId\":2528,\"eleLabel\":\"Bank Code\",\"eleTag\":\"BankCode\",\"regex\":\"/[^A-Za-z0-9]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumeric\",\"isParent\":false,\"eleOrder\":4.0},{\"eleId\":2529,\"eleLabel\":\"Reporting Start Date\",\"eleTag\":\"ReportingStartDate\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":5.0},{\"eleId\":2530,\"eleLabel\":\"Reporting End Date\",\"eleTag\":\"ReportingEndDate\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":6.0},{\"eleId\":2531,\"eleLabel\":\"Reporting Frequency\",\"eleTag\":\"ReportingFrequency\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":false,\"eleOrder\":7.0}],\"dataInfo\":{},\"tableLabel\":\"Filing Information\",\"tableId\":679,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":104}],\"headerList\":[{\"headerId\":104,\"headerLabel\":\"Filing Information\",\"headerOrder\":1.0},{\"headerId\":80,\"headerLabel\":\"Quarterly Statement showing Cumulative Progress under NULM\",\"headerOrder\":2.0}]},\"structureLabel\":{\"AmtE\":\"Amount\",\"MAHARASHTRA1\":\"MAHARASHTRA\",\"NoofSHGs\":\"No. of SHGs\",\"AmtB\":\"Amount\",\"WESTBENGAL1\":\"WEST BENGAL\",\"UTTARPRADESH1\":\"UTTAR PRADESH\",\"OutofSHGsLoandisbursedtoWomenSHGs\":\"Out of SHGs Loan disbursed to Women SHGs\",\"WSHGs1\":\"No of WSHGs\",\"JHARKHAND1\":\"JHARKHAND\",\"ORISSA1\":\"ORISSA\",\"Minorities\":\"Minorities\",\"GUJARAT1\":\"GUJARAT\",\"ANDHRAPRADESH1\":\"ANDHRA PRADESH\",\"MADHYAPRADESH1\":\"MADHYA PRADESH\",\"679\":\"Filing Information\",\"Achievement\":\"Achievement %\",\"CHANDIGARH\":\"CHANDIGARH\",\"TAMILNADU1\":\"TAMIL NADU\",\"ANDAMANNICOBAR1\":\"ANDAMAN AND NICOBAR\",\"Women\":\"Women\",\"Linkage1\":\"Total loan disbursed under SHG Bank Linkage\",\"ReturnCode\":\"Return Code\",\"Benificiaries1\":\"No. of Benificiaries\",\"distributed1\":\"Loan distributed to individual (SEP-I)\",\"TELANGANA1\":\"TELANGANA\",\"distributed2\":\"Loan distributed to groups(SEP-G)\",\"ASSAM1\":\"ASSAM\",\"ReportingFrequency\":\"Reporting Frequency\",\"Amt\":\"Amount\",\"LAKSHADWEEP1\":\"LAKSHADWEEP\",\"KARNATAKA1\":\"KARNATAKA\",\"Benificiariesnew\":\"No. of Benificiaries\",\"DAMANDIU1\":\"DAMAN AND DIU\",\"MIZORAM1\":\"MIZORAM\",\"RAJASTHAN1\":\"RAJASTHAN\",\"disbursedunder1\":\"Total Amount disbursed under NULM\",\"ARUNACHALPRADESH1\":\"ARUNACHAL PRADESH\",\"CHHATTISGARH1\":\"CHHATTISGARH\",\"JAMMUKASHMIR1\":\"JAMMU AND KASHMIR\",\"AmtH\":\"Amount\",\"MANIPUR1\":\"MANIPUR\",\"HARYANA1\":\"HARYANA\",\"ReportingStartDate\":\"Reporting Start Date\",\"TRIPURA1\":\"TRIPURA\",\"UTTARAKHAND1\":\"UTTARAKHAND\",\"457\":\"NULM Progress\",\"ReportingEndDate\":\"Reporting End Date\",\"BIHAR1\":\"BIHAR\",\"GOA1\":\"GOA\",\"MEGHALAYA1\":\"MEGHALAYA\",\"BenificiariesG\":\"No. of Benificiaries\",\"KERALA1\":\"KERALA\",\"NAGALAND1\":\"NAGALAND\",\"No\":\"No.\",\"NoofGroups\":\"No. of Groups\",\"PUNJAB1\":\"PUNJAB\",\"Grandd\":\"Grand Total\",\"scheduldd\":\"SC/STs\",\"BankCode\":\"Bank Code\",\"Additional2\":\"Additional Subvention to WSHG\",\"SIKKIM1\":\"SIKKIM\",\"DADRANAGARHAVELI\":\"DADRA AND NAGAR HAVELI\",\"Differently4\":\"Differently abled\",\"ReportingInstitution\":\"Reporting Institution\",\"Physical2\":\"Physical Coverage (in nos.)\",\"ReturnName\":\"Return Name\",\"HIMACHALRADESH1\":\"HIMACHAL PRADESH\",\"Subsidy2\":\"Total Subsidy given under NULM (SEP-I + SEP-G+SHGs)\",\"DELHI1\":\"DELHI\",\"benificiariesunder\":\"Total no. of benificiaries under NULM\",\"PUDUCHERRY1\":\"PUDUCHERRY\"}}";
	//
	//		// R247
	////		String jsonString= "{\"jsonFormData\":{\"dynamicTableList\":[{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":2525,\"eleLabel\":\"Return Name\",\"eleTag\":\"ReturnName\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":false,\"eleOrder\":1.0},{\"eleId\":2526,\"eleLabel\":\"Return Code\",\"eleTag\":\"ReturnCode\",\"regex\":\"/[^A-Za-z0-9]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumeric\",\"isParent\":false,\"eleOrder\":2.0},{\"eleId\":2527,\"eleLabel\":\"Reporting Institution\",\"eleTag\":\"ReportingInstitution\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":3.0},{\"eleId\":2528,\"eleLabel\":\"Bank Code\",\"eleTag\":\"BankCode\",\"regex\":\"/[^A-Za-z0-9]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumeric\",\"isParent\":false,\"eleOrder\":4.0},{\"eleId\":2529,\"eleLabel\":\"Reporting Start Date\",\"eleTag\":\"ReportingStartDate\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":5.0},{\"eleId\":2530,\"eleLabel\":\"Reporting End Date\",\"eleTag\":\"ReportingEndDate\",\"regex\":\"/[^A-Za-z0-9!@#$%^*(),.\\u003d+:;_ ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphaNumericSpecial.space\",\"isParent\":false,\"eleOrder\":6.0},{\"eleId\":2531,\"eleLabel\":\"Reporting Frequency\",\"eleTag\":\"ReportingFrequency\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":50,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":false,\"eleOrder\":7.0}],\"dataInfo\":{},\"tableLabel\":\"Filing Information\",\"tableId\":8060,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":213},{\"langList\":[],\"columnList\":[{\"colId\":2659,\"colTag\":\"CurrentYearl\",\"colLabel\":\"Current Year\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":1.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0},{\"colId\":2658,\"colTag\":\"PreviousYeark\",\"colLabel\":\"Previous Year\",\"level\":\"1\",\"child\":\"null\",\"colOrder\":2.0,\"maxLength\":0,\"minLength\":0,\"fieldTypeId\":0,\"dataTypeId\":0}],\"elementList\":[{\"eleId\":2660,\"eleLabel\":\"Opening balance of DEA Fund as on 01.04.yyyy\",\"eleTag\":\"Openingbalancee\",\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\",\"isParent\":true,\"eleOrder\":1.0},{\"eleId\":2661,\"eleLabel\":\"Add:Amounts transferred to DEA fund during the year yyyy-yy\",\"eleTag\":\"Amountshtransferred\",\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\",\"isParent\":true,\"eleOrder\":2.0},{\"eleId\":2662,\"eleLabel\":\"Less:Amounts reimbursed by DEA Fund towards claim during the year yyyy-yy\",\"eleTag\":\"Amountsnreimbursed\",\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\",\"isParent\":true,\"eleOrder\":3.0},{\"eleId\":2663,\"eleLabel\":\"Closing balance of DEA Fund as on 31.03.yyyy\",\"eleTag\":\"Closingbalanceq\",\"regex\":\"/[REGX_DECIMAL]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":3,\"regXName\":\"error.decimal.no\",\"isParent\":true,\"eleOrder\":4.0}],\"dataInfo\":{},\"tableLabel\":\"Annual Certificate\",\"tableId\":8061,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":214},{\"langList\":[],\"columnList\":[],\"elementList\":[{\"eleId\":2834,\"eleLabel\":\"Place\",\"eleTag\":\"Place12\",\"regex\":\"/[^A-Za-z ]/g\",\"maxLength\":15,\"minLength\":1,\"isMandatory\":false,\"fieldTypeId\":1,\"dataTypeId\":1,\"regXName\":\"error.alphabet.space\",\"isParent\":false,\"eleOrder\":1.0},{\"eleId\":3165,\"eleLabel\":\"Date\",\"eleTag\":\"Date1\",\"maxLength\":0,\"minLength\":0,\"isMandatory\":false,\"fieldTypeId\":3,\"dataTypeId\":0,\"isParent\":false,\"eleOrder\":2.0}],\"dataInfo\":{},\"tableLabel\":\"Signatory Information\",\"tableId\":8071,\"langAxis\":false,\"isRepeatable\":false,\"isTranspose\":false,\"isCheckApplied\":false,\"isMultipleColumn\":false,\"headerId\":221}],\"headerList\":[{\"headerId\":213,\"headerLabel\":\"Filing Information\",\"headerOrder\":1.0},{\"headerId\":214,\"headerLabel\":\"Annual Certificate\",\"headerOrder\":2.0},{\"headerId\":221,\"headerLabel\":\"Signatory Information\",\"headerOrder\":3.0}]},\"structureLabel\":{\"ReturnCode\":\"Return Code\",\"ReportingStartDate\":\"Reporting Start Date\",\"ReportingFrequency\":\"Reporting Frequency\",\"Amountsnreimbursed\":\"Less:Amounts reimbursed by DEA Fund towards claim during the year yyyy-yy\",\"8060\":\"Filing Information\",\"8071\":\"Signatory Information\",\"8061\":\"Annual Certificate\",\"BankCode\":\"Bank Code\",\"CurrentYearl\":\"Current Year\",\"Place12\":\"Place\",\"Closingbalanceq\":\"Closing balance of DEA Fund as on 31.03.yyyy\",\"Openingbalancee\":\"Opening balance of DEA Fund as on 01.04.yyyy\",\"Date1\":\"Date\",\"ReportingInstitution\":\"Reporting Institution\",\"PreviousYeark\":\"Previous Year\",\"ReportingEndDate\":\"Reporting End Date\",\"ReturnName\":\"Return Name\",\"Amountshtransferred\":\"Add:Amounts transferred to DEA fund during the year yyyy-yy\"}}";
	//		System.out.println(jsonString);
	//		DynamicFormBean dynamicFormBean = new Gson().fromJson(jsonString, DynamicFormBean.class);
	//		dynamicFormToCsvConversion.createNonXbrlCsvFile(dynamicFormBean, "R208", "D:\\abc.txt", "D:\\metaInfo.txt", null);

	//	}

	public void createNonXbrlCsvFile(DynamicFormBean dynamicFormBean, String Path, String pathToSaveMetaInfoFile, Map<String, List<ReturnCsvInfo>> returnCsvInfoMap, ReturnTemplate returnTemplate) throws IOException {

		logger.info("inside createNonXbrlCsvFile method ");

		List<String> filingInfoTableExclusionElement = new ArrayList<>();
		filingInfoTableExclusionElement.add(CsvConversionConstants.REPORTING_START_DATE.getConstantVal());
		filingInfoTableExclusionElement.add(CsvConversionConstants.LEVEL_OF_ROUNDING.getConstantVal());

		Map<String, String> filingInfoTableLabels = new HashMap<>();
		filingInfoTableLabels.put(CsvConversionConstants.REPORTING_INSTITUTION.getConstantVal(), CsvConversionConstants.BANK_NAME_2.getConstantVal());
		filingInfoTableLabels.put(CsvConversionConstants.REPORTING_FREQUENCY.getConstantVal(), CsvConversionConstants.FREQUENCY_4.getConstantVal());

		String returnCode = returnTemplate.getReturnObj().getReturnCode();

		// path to save the file

		// we have to give the start text while creating object of csvPrinter
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(Path)); CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

			/*
			 * As we have to create the formula xml file, which is based on column position.
			 * we dont have column position in the web form json, so we are creating the below map.
			 * in this map table code(combination of return code and table code eg. R001-T002) will be key and
			 * value will be another map which store column tag as a key and its position as a value.
			 * as we dont have column position, we will use a variable which will increment itself while print any value as column in csv
			 * at the same time same column or element tag will be use as key and its corresponding counter variable as a value.
			 */

			Map<String, TableElementMetaInfo> elementColumnPositionMap = new HashMap<>();

			// below header list will store header related info and that headerList from jsonFormData consist all the table info
			List<HeaderBean> headerList = dynamicFormBean.getJsonFormData().getHeaderList();

			//below dynamicTableList stores the actual data (combination of columnList and element List)
			List<TableBean> dynamicTableList = dynamicFormBean.getJsonFormData().getDynamicTableList();

			// we have to iterate the headerList on the basis of header order, so that table sequence should proper in the csv
			Collections.sort(headerList, new Comparator<HeaderBean>() {
				@Override
				public int compare(HeaderBean o1, HeaderBean o2) {
					return o1.getHeaderOrder().compareTo(o2.getHeaderOrder());
				}
			});

			// below counter variable will use to increment the table number like R001-T001, R001-T002 and so on.....
			int counter = 0;

			// below counterForColumnPosition will be used in the inner map to store the column position as a value and column or elemet tag as a key
			int counterForColumnPosition = 0;

			Map<String, String> eleIdAndLabelMap = new HashMap<>();
			Map<String, String> eleIdAndTagMap = new HashMap<>();

			//table code
			String tableCode = StringUtils.EMPTY;

			// header list consist all the table info
			for (HeaderBean headerBean : headerList) {

				// all the table bean have header Id from headerList to know current table bean is belongs to which header bean
				for (TableBean tableBean : dynamicTableList) {

					tableBean.setTransposeInCSVLayout(tableBean.isTranspose());
					// as we have already sorted the heade order above, so now we have to print the table sequentially
					if (headerBean.getHeaderId() == tableBean.getHeaderId() && tableBean.isVisible()) {
						counter++;
						//						if(tableBean.getTableLabel().contains("Table A2")) {
						//							System.out.println(tableBean.getTableLabel());
						//						}
						// as we have to print the table code with the prefic of 00 (T002), we are using below %02d which will format the numbers with 2 prefix of 0. 
						String temp = String.format(CsvConversionConstants.PERCENT_TWO_D.getConstantVal(), counter);
						csvPrinter.printRecord(CsvConversionConstants.TABLE_START_STR.getConstantVal());
						tableCode = returnCode + CsvConversionConstants.HYPHEN.getConstantVal() + CsvConversionConstants.T0.getConstantVal() + temp;
						// we will add all the strings in the recordStringList which we want to print in single row with comma seperated  and later we will call  printRecord(recordStringList), it will print all the value comma seperated present in the list. 
						List<String> recordStringList = new ArrayList<String>();

						recordStringList.add(tableBean.getTableLabel()); // add the table name in the list

						recordStringList.add(tableCode); //add the table code (Retrun code + table code) in the table. eg : R001-T002

						csvPrinter.printRecord(recordStringList); // print the value comma seprated

						// after print the value we have to clear the list.
						recordStringList.clear();

						// elementList will store all the element related info
						List<ElementBean> elementList = tableBean.getElementList();

						logger.info("inside createNonXbrlCsvFile method " + "element list Size : " + elementList.size());

						// columnList will store all the column related info
						List<ColumnBean> columnList = tableBean.getColumnList();

						logger.info("inside createNonXbrlCsvFile method " + "column list Size : " + columnList.size());
						// as we have maximum 3 level parent child hierarchy in the column name, below varible use to store the same.
						String parentChildConcatString = StringUtils.EMPTY;
						String parentChildConcatTagString = StringUtils.EMPTY;
						List<ElementBean> columnPositionList = null;
						List<ReturnCsvInfo> returnCsvInfo = null;
						boolean isTransposeInWebForm = tableBean.isTranspose(); // this will use to create key, coz if element data is transpose than only we are capturing parent-child tag else we are capturing only child tag
						// as we have to save min length, max length, isMandatory, and other info into meta json, below variable will be  used for the same
						ElementBean elementBeans = null;

						if (returnCsvInfoMap != null && returnCsvInfoMap.size() > 0) {
							// getting the supporting data of particular table for example serial number required or not, first column second column etc
							returnCsvInfo = returnCsvInfoMap.get(String.valueOf(tableBean.getTableId()));
						}
						// below if condition will check to transpose the current structure in the csv layout.
						logger.info("inside createNonXbrlCsvFile method " + "is transpose : " + tableBean.isTransposeInCSVLayout());

						if (tableBean.isTransposeInCSVLayout()) {

							// below list adds the elementBean object, which later used to get the element tag info, its other properties like min , max length, ismandatory etc.
							columnPositionList = new ArrayList<>();
							counterForColumnPosition = 0;

							if (!CollectionUtils.isEmpty(columnList)) {
								elementBeans = new ElementBean();
								recordStringList.add(CsvConversionConstants.PARTICULARS.getConstantVal()); // for printing in the csv column lable
								elementBeans.setEleTag(CsvConversionConstants.PARTICULARS.getConstantVal()); // for used in the tag info, later will be use in meta json
								elementBeans.setColPositionOnCsv(++counterForColumnPosition); // tags position in the csv layout
								columnPositionList.add(elementBeans); // add same in the list

								if (CollectionUtils.isEmpty(elementList)) {
									elementBeans = new ElementBean();
									recordStringList.add(CsvConversionConstants.VALUE_LAB.getConstantVal()); // for printing in the csv column lable
									elementBeans.setEleTag(CsvConversionConstants.DEF.getConstantVal()); // for used in the tag info, later will be use in meta json
									elementBeans.setColPositionOnCsv(++counterForColumnPosition); // tags position in the csv layout
									columnPositionList.add(elementBeans); // add same in the list
								}

							}

							//after table start text in the first line, table name and table code in the second line.
							// now we have to print all the column values in the 1 row with comma seperate.
							for (ElementBean elementBean : elementList) {
								logger.info("inside createNonXbrlCsvFile method " + "element label : " + elementBean.getEleLabel());

								// below method returns the string if it is not abstract(it means it is not part of column in the csv header).
								// if the its abstract string then it will returns null.
								String labelToInsertInTheList = getLabel(elementBean, eleIdAndLabelMap, parentChildConcatString, eleIdAndTagMap, parentChildConcatTagString, isTransposeInWebForm, true, elementList);
								if (StringUtils.isNotBlank(labelToInsertInTheList)) {
									// we will add all the element lable in the list at once.
									recordStringList.add(labelToInsertInTheList);
									elementBean.setColPositionOnCsv(++counterForColumnPosition); // here we are adding the column position of the element bean. same info will be used in the proces of meta info json creation.
									columnPositionList.add(elementBean); // and same element bean is adding into another list.
								}
							}

							// print all the records present in the list in single row.
							csvPrinter.printRecord(recordStringList);

							// clear the list
							recordStringList.clear();

							// as we have printed all the values in the columns , now we are checking is it as any row to be print.
							int rowPosition = 0;
							if (!CollectionUtils.isEmpty(columnList)) {

								// according to current table id, we have returnCsvInfo file, which contain some helping info like serial number label, tag, state lable tag.
								if (returnCsvInfo != null) {
									if (returnCsvInfo.get(0).isSerialNumberPresent()) {
										rowPosition++;
										recordStringList.add(returnCsvInfo.get(0).getFirstColumnLabel());
										csvPrinter.printRecord(recordStringList);
										recordStringList.clear();

										// as we have not started the iteration of column list, we have add the meta json info of above serial number with respect to all the element list
										for (ElementBean elementBean : columnPositionList) {
											TableElementMetaInfo tableElementMetaInfo = getTableElementMetaInfo(elementBean, tableCode, rowPosition, returnCsvInfo, null, tableBean.getTableLabel(), elementBean.getEleTag(), tableBean.isRepeatable());
											if (tableElementMetaInfo != null) {
												String elementagInfo = tableBean.getTableId() + CsvConversionConstants.HYPHEN.getConstantVal() + elementBean.getEleTag() + CsvConversionConstants.HYPHEN.getConstantVal() + returnCsvInfo.get(0).getFirstColumnTag();
												elementColumnPositionMap.put(elementagInfo, tableElementMetaInfo);
											}
										}

									}

									// same condition for second column like above one
									if (returnCsvInfo.get(0).isSecondColumnRequired()) {
										rowPosition++;
										recordStringList.add(returnCsvInfo.get(0).getSecondColumnLable());
										csvPrinter.printRecord(recordStringList);
										recordStringList.clear();

										for (ElementBean elementBean : columnPositionList) {
											TableElementMetaInfo tableElementMetaInfo = getTableElementMetaInfo(elementBean, tableCode, rowPosition, returnCsvInfo, null, tableBean.getTableLabel(), elementBean.getEleTag(), tableBean.isRepeatable());
											if (tableElementMetaInfo != null) {
												String elementagInfo = tableBean.getTableId() + CsvConversionConstants.HYPHEN.getConstantVal() + elementBean.getEleTag() + CsvConversionConstants.HYPHEN.getConstantVal() + returnCsvInfo.get(0).getSecondColumnTag();
												elementColumnPositionMap.put(elementagInfo, tableElementMetaInfo);
											}
										}
									}

								}

								List<String> childColIdList = new ArrayList<>(); // this would be use to save the child column ids, later it will use to check if any column have child id's then that value should not part of row of csv. that will use as parent-child column.
								List<String> colWithoutChildList = new ArrayList<>();
								Map<String, TableElementMetaInfo> elementColumnPositionMapTemp = new HashMap<>();
								for (ColumnBean columnBean : columnList) {

									String columnLableToInsertInTheList = getColumnLabel(columnBean, columnList, childColIdList, colWithoutChildList);

									/*
									 *  in this part of code, we are using label of column list to print in the row one by one.
									 * so if above method is returning null and childColIdList doesnt contain current id.
									 * hence it will print in the csv as a row. here we are not storing all the values in the recordStringList because columns of the table already printed
									 * by elemement bean list and column list is using to print each column value as a row lable in new line.
									 */

									// if method returns any value that means it is part of row of , but it have labels like parent child (seperated with !) with its tag value seperated with $
									if (StringUtils.isNotBlank(columnLableToInsertInTheList)) {
										String columnLableToInsertInTheArray[] = new String[columnLableToInsertInTheList.split(CsvConversionConstants.EXC_MARK.getConstantVal()).length];
										columnLableToInsertInTheArray = columnLableToInsertInTheList.split(CsvConversionConstants.EXC_MARK.getConstantVal());

										for (String columnValue : columnLableToInsertInTheArray) {
											rowPosition++;
											recordStringList.add(columnValue.split(CsvConversionConstants.SLASH_DOLLER.getConstantVal())[0]); // we will add all the value in the list together and print all in one by one in new row
											csvPrinter.printRecord(recordStringList);
											recordStringList.clear();

											// here we are preparing meta json with corresponding element tag.
											for (ElementBean elementBean : columnPositionList) {
												TableElementMetaInfo tableElementMetaInfo = getTableElementMetaInfo(elementBean, tableCode, rowPosition, returnCsvInfo, columnBean.getRegex(), tableBean.getTableLabel(), columnBean.getColTag(), tableBean.isRepeatable());
												if (tableElementMetaInfo != null) {
													String elementagInfo = tableBean.getTableId() + CsvConversionConstants.HYPHEN.getConstantVal() + elementBean.getEleTag() + CsvConversionConstants.HYPHEN.getConstantVal() + columnValue.split(CsvConversionConstants.SLASH_DOLLER.getConstantVal())[1];
													checkIfCustomizedFormula(elementagInfo, returnCsvInfo, tableElementMetaInfo);
													elementColumnPositionMapTemp.put(elementagInfo, tableElementMetaInfo);
												}
											}
										}
									} else {
										if (colWithoutChildList.contains(String.valueOf(columnBean.getColId())) && columnBean.getChild().equals(CsvConversionConstants.NULL.getConstantVal())) { // if return string is null and it is not part of childColIdList. that means it is child without parent.

											ColumnBean columnBeans = columnList.stream().filter(f -> String.valueOf(f.getChild()).contains(String.valueOf(columnBean.getColId()))).findAny().orElse(null);
											if (columnBeans == null || !childColIdList.contains(columnBeans.getColId() + CsvConversionConstants.UNDERSCORE.getConstantVal() + columnBean.getColId())) {

												recordStringList.add(columnBean.getColLabel());
												csvPrinter.printRecord(recordStringList);
												recordStringList.clear();
												for (ElementBean elementBean : columnPositionList) {
													TableElementMetaInfo tableElementMetaInfo = getTableElementMetaInfo(elementBean, tableCode, rowPosition, returnCsvInfo, columnBean.getRegex(), tableBean.getTableLabel(), columnBean.getColTag(), tableBean.isRepeatable());
													if (tableElementMetaInfo != null) {
														String elementagInfo = tableBean.getTableId() + CsvConversionConstants.HYPHEN.getConstantVal() + elementBean.getEleTag() + CsvConversionConstants.HYPHEN.getConstantVal() + columnBean.getColTag();
														checkIfCustomizedFormula(elementagInfo, returnCsvInfo, tableElementMetaInfo);
														elementColumnPositionMapTemp.put(elementagInfo, tableElementMetaInfo);
													}
												}
											}
										}
									}

								}

								// adding totalRow count in each TableElementMetaInfo

								addTotalRowCountInEachTableElementMetaInfo(elementColumnPositionMap, rowPosition, elementColumnPositionMapTemp);

							} else {
								// if there is no column list that mean we have prepare the meta json with default column tag value that is DEF
								for (ElementBean elementBean : columnPositionList) {
									TableElementMetaInfo tableElementMetaInfo = getTableElementMetaInfo(elementBean, tableCode, rowPosition, returnCsvInfo, null, tableBean.getTableLabel(), elementBean.getEleTag(), tableBean.isRepeatable());
									if (tableElementMetaInfo != null) {
										String elementagInfo = tableBean.getTableId() + CsvConversionConstants.HYPHEN.getConstantVal() + elementBean.getEleTag() + CsvConversionConstants.HYPHEN.getConstantVal() + CsvConversionConstants.DEF.getConstantVal();
										logger.info("inside createNonXbrlCsvFile method " + elementagInfo);
										elementColumnPositionMap.put(elementagInfo, tableElementMetaInfo);
									}
								}
							}

						} else {

							columnPositionList = new ArrayList<>();
							counterForColumnPosition = 0;
							Boolean addSerialNoString = false; // for some returns we have to add serial numbers in the start of every rows.
							if (!CollectionUtils.isEmpty(columnList) && returnCsvInfo != null) {
								if (returnCsvInfo.get(0).isSerialNumberPresent()) {
									addSerialNoString = true;
									elementBeans = new ElementBean();
									recordStringList.add(returnCsvInfo.get(0).getFirstColumnLabel());
									elementBeans.setEleTag(returnCsvInfo.get(0).getFirstColumnTag());
									elementBeans.setColPositionOnCsv(++counterForColumnPosition);
									columnPositionList.add(elementBeans);
								}
								if (returnCsvInfo.get(0).isSecondColumnRequired()) {
									elementBeans = new ElementBean();
									recordStringList.add(returnCsvInfo.get(0).getSecondColumnLable());
									elementBeans.setEleTag(returnCsvInfo.get(0).getSecondColumnTag());
									elementBeans.setColPositionOnCsv(++counterForColumnPosition);
									columnPositionList.add(elementBeans);
								}
							}

							if (!CollectionUtils.isEmpty(columnList)) {

								if ((!CollectionUtils.isEmpty(elementList) && !tableBean.getTableLabel().equals(CsvConversionConstants.FILING_INFORMATION.getConstantVal()) && returnCsvInfo == null) || (!CollectionUtils.isEmpty(elementList) && !tableBean.getTableLabel().equals(CsvConversionConstants.FILING_INFORMATION.getConstantVal()) && returnCsvInfo != null)) {
									elementBeans = new ElementBean();
									recordStringList.add(CsvConversionConstants.PARTICULARS.getConstantVal());
									elementBeans.setEleTag(CsvConversionConstants.PARTICULARS.getConstantVal());
									//									elementBeans.setRegex("String_regex");
									elementBeans.setColPositionOnCsv(++counterForColumnPosition);
									columnPositionList.add(elementBeans);
								}

								List<String> childColIdList = new ArrayList<>(); // this would be use to save the child column ids, later it will use to check if any column have same child id's present in the list then that column should not part of column of csv. that will use as parent-child column.
								List<String> colmsWithOutChildId = new ArrayList<>(); // this list would add the information of elements who have no child. and to achieve the functionalit of 1 child have multiple parents. 
								for (ColumnBean columnBean : columnList) {
									String columnLableToInsertInTheList = getColumnLabel(columnBean, columnList, childColIdList, colmsWithOutChildId);

									// if columnLableToInsertInTheList is not null that means it is part of columns print in the csv.
									//as return string is ! seperated, we have split and store each string(parent and child) into recordStringList
									if (StringUtils.isNotBlank(columnLableToInsertInTheList)) {
										String columnLableToInsertInTheArray[] = new String[columnLableToInsertInTheList.split(CsvConversionConstants.EXC_MARK.getConstantVal()).length];
										columnLableToInsertInTheArray = columnLableToInsertInTheList.split(CsvConversionConstants.EXC_MARK.getConstantVal());

										for (String columnValue : columnLableToInsertInTheArray) {
											elementBeans = new ElementBean();
											elementBeans.setEleTag(columnValue.split(CsvConversionConstants.SLASH_DOLLER.getConstantVal())[1]);
											elementBeans.setRegex(getChildRegex(columnValue.split(CsvConversionConstants.SLASH_DOLLER.getConstantVal())[1].split(CsvConversionConstants.UNDERSCORE.getConstantVal())[1], columnList));
											elementBeans.setColPositionOnCsv(++counterForColumnPosition);
											columnPositionList.add(elementBeans);
											recordStringList.add(columnValue.split(CsvConversionConstants.SLASH_DOLLER.getConstantVal())[0]); // we will add all the value in the list together and print all in one row
										}
									} else {
										if (colmsWithOutChildId.contains(String.valueOf(columnBean.getColId())) && columnBean.getChild().equals(CsvConversionConstants.NULL.getConstantVal())) { // if return string is null and it is not part of childColIdList. that means it is child without parent.

											// check the element have any parent and if the child is printed with that parent than no need to print that child
											ColumnBean columnBeans = columnList.stream().filter(f -> String.valueOf(f.getChild()).contains(String.valueOf(columnBean.getColId()))).findAny().orElse(null);
											if (columnBeans == null || !childColIdList.contains(columnBeans.getColId() + CsvConversionConstants.UNDERSCORE.getConstantVal() + columnBean.getColId())) {

												recordStringList.add(columnBean.getColLabel());
												elementBeans = new ElementBean();
												elementBeans.setEleTag(columnBean.getColTag());
												elementBeans.setRegex(columnBean.getRegex());
												elementBeans.setColPositionOnCsv(++counterForColumnPosition);
												columnPositionList.add(elementBeans);
											}
										}
									}
								}
								if (CollectionUtils.isNotEmpty(recordStringList)) {
									csvPrinter.printRecord(recordStringList);
									recordStringList.clear();
								}
							} else if (!tableBean.getTableLabel().equals(CsvConversionConstants.FILING_INFORMATION.getConstantVal())) {
								elementBeans = new ElementBean();
								recordStringList.add(CsvConversionConstants.PARTICULARS.getConstantVal());
								elementBeans.setEleTag(CsvConversionConstants.PARTICULARS.getConstantVal());
								elementBeans.setColPositionOnCsv(++counterForColumnPosition);
								columnPositionList.add(elementBeans);

								elementBeans = new ElementBean();
								recordStringList.add(CsvConversionConstants.VALUE_LAB.getConstantVal()); // for printing in the csv column lable
								elementBeans.setEleTag(CsvConversionConstants.DEF.getConstantVal()); // for used in the tag info, later will be use in meta json
								elementBeans.setColPositionOnCsv(++counterForColumnPosition); // tags position in the csv layout
								columnPositionList.add(elementBeans); // add same in the list

								csvPrinter.printRecord(recordStringList);
								recordStringList.clear();

							}

							int index = 0; // index is used in case of serial number is adding in the starting of each row. in such type of returns, last row should not contain serial number
							int rowPosition = 0;
							int columnPositinForFilingInfo = 0;
							if (CollectionUtils.isNotEmpty(elementList)) {
								Map<String, TableElementMetaInfo> elementColumnPositionMapTemp = new HashMap<>();
								for (ElementBean elementBean : elementList) {
									logger.info("inside createNonXbrlCsvFile method " + "element label : " + elementBean.getEleLabel());
									if (!filingInfoTableExclusionElement.contains(elementBean.getEleTag())) {

										String labelToInsertInTheList = getLabel(elementBean, eleIdAndLabelMap, parentChildConcatString, eleIdAndTagMap, parentChildConcatTagString, isTransposeInWebForm, false, elementList);

										if (StringUtils.isNotBlank(labelToInsertInTheList)) {

											if (tableBean.getTableLabel().equals(CsvConversionConstants.FILING_INFORMATION.getConstantVal())) {
												elementBean.setColPositionOnCsv(++columnPositinForFilingInfo);
												if (StringUtils.isNotBlank(filingInfoTableLabels.get(elementBean.getEleTag()))) {
													recordStringList.add(Integer.parseInt(filingInfoTableLabels.get(elementBean.getEleTag()).split(CsvConversionConstants.TILDA.getConstantVal())[1]), filingInfoTableLabels.get(elementBean.getEleTag()).split(CsvConversionConstants.TILDA.getConstantVal())[0]);
												} else {
													recordStringList.add(labelToInsertInTheList);
													if (elementBean.getEleTag().equals(CsvConversionConstants.REPORTING_END_DATE.getConstantVal())) { // added hardcode condition here, because in webform table 1 have more than 6 element. and position of some element is differ from csv
														elementBean.setColPositionOnCsv(REPORTING_END_DATE_POSITION);
														columnPositinForFilingInfo = (int) REPORTING_END_DATE_POSITION;
													}

												}

											} else {
												if (addSerialNoString && index != elementList.size() - 1 && checkIfSerialNumRequired(returnCsvInfo.get(0).isCheckIfSerialNumberRequired(), elementBean.getIsParent())) { //if index and list size are equal that means it is last row if the table and serial number should not print.
													recordStringList.add(String.valueOf(++index));
												}
												rowPosition++;
												recordStringList.add(labelToInsertInTheList);
												csvPrinter.printRecord(recordStringList);
												recordStringList.clear();
											}
										}

										if (CollectionUtils.isNotEmpty(columnList) || CollectionUtils.isNotEmpty(columnPositionList)) {
											for (ElementBean element : columnPositionList) {
												TableElementMetaInfo tableElementMetaInfo = getTableElementMetaInfo(elementBean, tableCode, rowPosition, returnCsvInfo, element.getRegex(), tableBean.getTableLabel(), element.getEleTag(), tableBean.isRepeatable());
												if (tableElementMetaInfo != null) {
													tableElementMetaInfo.setCsvColumnPosition(String.valueOf(element.getColPositionOnCsv()));
													String elementagInfo = tableBean.getTableId() + CsvConversionConstants.HYPHEN.getConstantVal() + elementBean.getEleTag() + CsvConversionConstants.HYPHEN.getConstantVal() + element.getEleTag();
													checkIfCustomizedFormula(elementagInfo, returnCsvInfo, tableElementMetaInfo);
													elementColumnPositionMapTemp.put(elementagInfo, tableElementMetaInfo);
												}
											}
										} else {
											TableElementMetaInfo tableElementMetaInfo = getTableElementMetaInfo(elementBean, tableCode, rowPosition, returnCsvInfo, null, tableBean.getTableLabel(), null, tableBean.isRepeatable());
											if (tableElementMetaInfo != null) {
												String elementagInfo = tableBean.getTableId() + CsvConversionConstants.HYPHEN.getConstantVal() + elementBean.getEleTag() + CsvConversionConstants.HYPHEN.getConstantVal() + CsvConversionConstants.DEF.getConstantVal();
												checkIfCustomizedFormula(elementagInfo, returnCsvInfo, tableElementMetaInfo);
												elementColumnPositionMapTemp.put(elementagInfo, tableElementMetaInfo);
											}
										}
									}
								}

								// adding totalRow count in each TableElementMetaInfo

								addTotalRowCountInEachTableElementMetaInfo(elementColumnPositionMap, rowPosition, elementColumnPositionMapTemp);

								if (returnCsvInfo != null && returnCsvInfo.get(0).isTotalRowCountPresent() && !tableBean.getTableLabel().equals(CsvConversionConstants.FILING_INFORMATION.getConstantVal())) {
									recordStringList.add(returnCsvInfo.get(0).getTotalRowCountLabel());
								}
							} else {
								for (ElementBean element : columnPositionList) {
									TableElementMetaInfo tableElementMetaInfo = getTableElementMetaInfo(element, tableCode, rowPosition, returnCsvInfo, null, tableBean.getTableLabel(), element.getEleTag(), tableBean.isRepeatable());
									if (tableElementMetaInfo != null) {
										String elementagInfo = tableBean.getTableId() + CsvConversionConstants.HYPHEN.getConstantVal() + element.getEleTag();
										checkIfCustomizedFormula(elementagInfo, returnCsvInfo, tableElementMetaInfo);
										elementColumnPositionMap.put(elementagInfo, tableElementMetaInfo);
									}
								}
							}

							if (CollectionUtils.isNotEmpty(recordStringList)) {
								csvPrinter.printRecord(recordStringList);
								recordStringList.clear();
							}
						}

					}
				}
			}

			if (elementColumnPositionMap != null && MapUtils.isNotEmpty(elementColumnPositionMap) && StringUtils.isNotBlank(pathToSaveMetaInfoFile)) {
				createMetaInfoFile(elementColumnPositionMap, pathToSaveMetaInfoFile);
				insertIntoReturnTableMap(elementColumnPositionMap, returnTemplate.getReturnObj().getReturnId());
			}

			csvPrinter.flush();
			logger.info("inside createNonXbrlCsvFile method " + "file created");
		} catch (Exception e) {
			logger.error("inside createNonXbrlCsvFile method " + "exception occoured" + e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);

		}
	}

	private void checkIfCustomizedFormula(String elementagInfo, List<ReturnCsvInfo> returnCsvInfo, TableElementMetaInfo tableElementMetaInfo) {

		if (returnCsvInfo != null) {

			ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getDefEleTag()) && f.getDefEleTag().equals(elementagInfo)).findAny().orElse(null);

			if (returnCsvInf != null) {

				tableElementMetaInfo.setFormula(returnCsvInf.getDefEleFormula());
				tableElementMetaInfo.setErrorCode(returnCsvInf.getDefEleErrorCode());
				tableElementMetaInfo.setConditionTag(returnCsvInf.getConditionTag());
				tableElementMetaInfo.setFormulaTag(returnCsvInf.getFormulaTag());
				tableElementMetaInfo.setValidationOnParticularRow(true);
				tableElementMetaInfo.setRowPositionCheck(true);
			}
		}
	}

	private String getChildRegex(String colTag, List<ColumnBean> columnList) {

		if (StringUtils.isNotBlank(colTag)) {
			ColumnBean columnBeans = columnList.stream().filter(f -> String.valueOf(f.getColTag()).equals(colTag)).findAny().orElse(null);

			if (columnBeans != null) {
				return columnBeans.getRegex();
			}
		}
		return null;
	}

	private void insertIntoReturnTableMap(Map<String, TableElementMetaInfo> elementColumnPositionMap, Long returnId) {

		Long[] returnIdArr = new Long[1];
		returnIdArr[0] = returnId;
		List<ReturnTableMap> returnTableMapList = returnTableMapService.getDataByIds(returnIdArr);
		Set<String> tableCodeMandList = new HashSet<>();
		Set<String> tableCodeNonMandList = new HashSet<>();
		ReturnTableMap returnTableMap = null;
		Return ret = new Return();
		ret.setReturnId(returnId);
		List<ReturnTableMap> returnTableList = new ArrayList<>();

		for (Map.Entry<String, TableElementMetaInfo> map : elementColumnPositionMap.entrySet()) {
			if (map.getValue().getIsMandatory() && !tableCodeMandList.contains(map.getValue().getCsvTableCode())) {
				tableCodeMandList.add(map.getValue().getCsvTableCode() + CsvConversionConstants.TILDA.getConstantVal() + map.getValue().getTableName());
			} else if (!map.getValue().getIsMandatory() && !tableCodeNonMandList.contains(map.getValue().getCsvTableCode())) {
				tableCodeNonMandList.add(map.getValue().getCsvTableCode() + CsvConversionConstants.TILDA.getConstantVal() + map.getValue().getTableName());
			}
		}

		for (String nonMandTable : tableCodeNonMandList) {

			String tableCode = nonMandTable.split(CsvConversionConstants.TILDA.getConstantVal())[0];
			String tableName = nonMandTable.split(CsvConversionConstants.TILDA.getConstantVal())[1];

			if (tableCodeMandList.contains(nonMandTable)) {

				ReturnTableMap retTableMap = returnTableMapList.stream().filter(f -> f.getTableCode().equals(tableCode)).findAny().orElse(null);

				if (retTableMap == null) {
					returnTableMap = new ReturnTableMap();

					returnTableMap.setReturnIdFk(ret);
					returnTableMap.setIsOptional(false);
					returnTableMap.setTableCode(tableCode);
					returnTableMap.setIsActive(true);
					returnTableMap.setIsBusinessRuleApplication(true);
					returnTableMap.setTableName(tableName);
					returnTableList.add(returnTableMap);
				} else {
					retTableMap.setIsOptional(false);
					retTableMap.setTableCode(tableCode);
					retTableMap.setIsActive(true);
					retTableMap.setIsBusinessRuleApplication(true);
					retTableMap.setTableName(tableName);
					returnTableList.add(retTableMap);
				}

			} else {

				ReturnTableMap retTableMap = returnTableMapList.stream().filter(f -> f.getTableCode().equals(tableCode)).findAny().orElse(null);

				if (retTableMap == null) {
					returnTableMap = new ReturnTableMap();

					returnTableMap.setReturnIdFk(ret);
					returnTableMap.setIsOptional(true);
					returnTableMap.setTableCode(tableCode);
					returnTableMap.setIsActive(true);
					returnTableMap.setIsBusinessRuleApplication(true);
					returnTableMap.setTableName(tableName);
					returnTableList.add(returnTableMap);
				} else {
					retTableMap.setIsOptional(true);
					retTableMap.setTableCode(tableCode);
					retTableMap.setIsActive(true);
					retTableMap.setIsBusinessRuleApplication(true);
					retTableMap.setTableName(tableName);
					returnTableList.add(retTableMap);
				}
			}

		}

		returnTableMapRepo.saveAll(returnTableList);

	}

	private boolean checkIfSerialNumRequired(boolean checkIfSerialNumberRequired, Boolean isParent) {

		if (!checkIfSerialNumberRequired) {
			return true;
		}
		if (checkIfSerialNumberRequired && isParent) {
			return false;
		}
		return true;
	}

	private static void addTotalRowCountInEachTableElementMetaInfo(Map<String, TableElementMetaInfo> elementColumnPositionMap, int rowPosition, Map<String, TableElementMetaInfo> elementColumnPositionMapTemp) {

		elementColumnPositionMapTemp.entrySet().stream().forEach(f -> {
			f.getValue().setTotalRowCount(rowPosition);

			elementColumnPositionMap.put(f.getKey(), f.getValue());

		});

	}

	public TableElementMetaInfo getTableElementMetaInfo(ElementBean elementBean, String tableCode, int rowPosition, List<ReturnCsvInfo> returnCsvInfo, String ColRegex, String tableName, String eleTagForCustomFormula, boolean isRepeatable) {
		TableElementMetaInfo tableElementMetaInfo = new TableElementMetaInfo();
		logger.info(elementBean.getEleTag());
		if (tableCode.contains(CsvConversionConstants.T001.getConstantVal())) {
			tableElementMetaInfo.setCsvColumnPosition(String.valueOf(elementBean.getColPositionOnCsv()));
			tableElementMetaInfo.setCsvTableCode(tableCode);
			tableElementMetaInfo.setCsvRowPosition("1");
			tableElementMetaInfo.setTableName(tableName);

			//			if (elementBean.getIsMandatory() != null && elementBean.getIsMandatory()) {
			//				tableElementMetaInfo.setNullAllow(false);
			//			} else {
			//				tableElementMetaInfo.setNullAllow(true);
			//			}

		} else if (elementBean != null) {
			if (elementBean.getFieldTypeId() != ABSTRACT_FIELD_ID) {
				tableElementMetaInfo.setCsvColumnPosition(String.valueOf(elementBean.getColPositionOnCsv())); // column position of csv
				tableElementMetaInfo.setCsvTableCode(tableCode);
				tableElementMetaInfo.setCsvRowPosition(String.valueOf(rowPosition)); // row position of csv
				tableElementMetaInfo.setIsMandatory(elementBean.getIsMandatory() != null ? elementBean.getIsMandatory() : false);
				if (elementBean.getIsMandatory() != null && elementBean.getIsMandatory()) {
					tableElementMetaInfo.setNullAllow(false);
				} else {
					tableElementMetaInfo.setNullAllow(true);
				}
				tableElementMetaInfo.setMaxLength(elementBean.getMaxLength());
				tableElementMetaInfo.setMinLength(elementBean.getMinLength());
				tableElementMetaInfo.setRegex(getColEleRegex(elementBean.getRegex(), ColRegex));
				tableElementMetaInfo.setFieldTypeId(elementBean.getFieldTypeId());
				tableElementMetaInfo.setTableName(tableName);
				tableElementMetaInfo.setRepeatable(isRepeatable);

				if (elementBean.getFieldTypeId() == 2) {
					getMethodUrlAndMethodType(tableElementMetaInfo, elementBean.getEleTag());
				}

				if (returnCsvInfo != null && checkIfCustomizeMethodUrl(elementBean.getEleTag(), returnCsvInfo)) {
					getCustomMethodUrl(tableElementMetaInfo, returnCsvInfo, elementBean.getEleTag());
				}

				if (returnCsvInfo != null && checkIfCustomizedDateCompareFormula(elementBean.getEleTag(), returnCsvInfo)) {
					getCustomDateCompare(tableElementMetaInfo, returnCsvInfo, elementBean.getEleTag());
				}

				if (returnCsvInfo != null && checkIfCustomizeFormula(elementBean.getEleTag(), returnCsvInfo)) {
					getRowCountValidation(tableElementMetaInfo, returnCsvInfo, elementBean.getEleTag());
				}

				if (returnCsvInfo != null && checkIfUniqueValueCheckFormula(elementBean.getEleTag(), returnCsvInfo)) {
					getUniqueValueFormula(tableElementMetaInfo, returnCsvInfo, elementBean.getEleTag());
				}

				if (returnCsvInfo != null && checkIfCustomizeFormula(eleTagForCustomFormula, returnCsvInfo)) {
					getCustomFormula(tableElementMetaInfo, returnCsvInfo, eleTagForCustomFormula);
				}

				if (returnCsvInfo != null) {
					tableElementMetaInfo.setGrandTotalPresent(returnCsvInfo.get(0).isGrandTotalPresent());
				}
			} else {
				return null;
			}
		}
		return tableElementMetaInfo;
	}

	private void getCustomDateCompare(TableElementMetaInfo tableElementMetaInfo, List<ReturnCsvInfo> returnCsvInfo, String eleTag) {
		ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getDateEleTag()) && f.getDateEleTag().equals(eleTag)).findAny().orElse(null);

		if (returnCsvInf != null) {

			tableElementMetaInfo.setFormula(returnCsvInf.getDefEleFormula());
			tableElementMetaInfo.setConditionForMonthCompare(returnCsvInf.getConditionForMonthCompare());
			tableElementMetaInfo.setConditionForYearCompare(returnCsvInf.getConditionForYearCompare());
			tableElementMetaInfo.setFormulaType(returnCsvInf.getFormulaType());
			tableElementMetaInfo.setConditionTag(returnCsvInf.getConditionTag());
			tableElementMetaInfo.setErrorCodeForMonthCompare(returnCsvInf.getErrorCodeForMonthCompare());
			tableElementMetaInfo.setErrorCodeForYearCompare(returnCsvInf.getErrorCodeForYearCompare());
			tableElementMetaInfo.setFormulaTag(returnCsvInf.getFormulaTag());
			tableElementMetaInfo.setMonthComparison(returnCsvInf.isMonthComparison());
			tableElementMetaInfo.setYearComparison(returnCsvInf.isYearComparison());
			tableElementMetaInfo.setReportedDateFormat(returnCsvInf.getReportedDateFormat());
			tableElementMetaInfo.setNullAllow(returnCsvInf.isNullAllow());
		}

	}

	private boolean checkIfCustomizedDateCompareFormula(String eleTag, List<ReturnCsvInfo> returnCsvInfo) {
		if (StringUtils.isNotBlank(eleTag)) {

			ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getDateEleTag()) && f.getDateEleTag().equals(eleTag)).findAny().orElse(null);

			if (returnCsvInf != null) {
				return true;
			}
		}
		return false;
	}

	private void getUniqueValueFormula(TableElementMetaInfo tableElementMetaInfo, List<ReturnCsvInfo> returnCsvInfo, String eleTag) {

		ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getUniqueValueCheckTag()) && f.getUniqueValueCheckTag().equals(eleTag)).findAny().orElse(null);

		if (returnCsvInf != null && returnCsvInf.getIsUniqueValueCheck() != null) {

			tableElementMetaInfo.setIsUniqueValueCheck(returnCsvInf.getIsUniqueValueCheck());
			tableElementMetaInfo.setErrorCodeForUniqueValueCheck(returnCsvInf.getPanErrorCode());
			tableElementMetaInfo.setFormulaForUniqueValueCheck(returnCsvInf.getPanFormula());
			tableElementMetaInfo.setNullAllow(returnCsvInf.isNullAllow());
			if (returnCsvInf.isPairCheck()) {
				tableElementMetaInfo.setPairCheck(returnCsvInf.isPairCheck());
				tableElementMetaInfo.setSplitType(returnCsvInf.getSplitType());
			}
		}
	}

	private boolean checkIfUniqueValueCheckFormula(String eleTag, List<ReturnCsvInfo> returnCsvInfo) {

		if (StringUtils.isNotBlank(eleTag)) {

			ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getUniqueValueCheckTag()) && f.getUniqueValueCheckTag().equals(eleTag)).findAny().orElse(null);

			if (returnCsvInf != null) {
				return true;
			}
		}
		return false;
	}

	private void getRowCountValidation(TableElementMetaInfo tableElementMetaInfo, List<ReturnCsvInfo> returnCsvInfo, String eleTag) {

		ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getDefEleTag()) && f.getDefEleTag().equals(eleTag)).findAny().orElse(null);

		if (returnCsvInf != null && StringUtils.isNotBlank(returnCsvInf.getMaxRowCount())) {

			tableElementMetaInfo.setErrorCode(returnCsvInf.getDefEleErrorCode());
			tableElementMetaInfo.setMaxRowCount(returnCsvInf.getMaxRowCount());
			tableElementMetaInfo.setRowCountValidation(returnCsvInf.isRowCountValidation());
		}

	}

	private boolean checkIfCustomizeMethodUrl(String eleTag, List<ReturnCsvInfo> returnCsvInfo) {
		if (StringUtils.isNotBlank(eleTag)) {

			ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getPanEleTag()) && f.getPanEleTag().equals(eleTag)).findAny().orElse(null);

			if (returnCsvInf != null) {
				return true;
			}
		}
		return false;
	}

	private void getCustomFormula(TableElementMetaInfo tableElementMetaInfo, List<ReturnCsvInfo> returnCsvInfo, String eleTag) {
		ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getDefEleTag()) && f.getDefEleTag().equals(eleTag)).findAny().orElse(null);

		if (returnCsvInf != null) {
			tableElementMetaInfo.setFormula(returnCsvInf.getDefEleFormula());
			tableElementMetaInfo.setErrorCode(returnCsvInf.getDefEleErrorCode());
			tableElementMetaInfo.setFormulaType(returnCsvInf.getFormulaType());
			tableElementMetaInfo.setGrandTotalPresent(returnCsvInf.isGrandTotalPresent());

		}

	}

	private boolean checkIfCustomizeFormula(String eleTag, List<ReturnCsvInfo> returnCsvInfo) {

		if (StringUtils.isNotBlank(eleTag)) {

			ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getDefEleTag()) && f.getDefEleTag().equals(eleTag)).findAny().orElse(null);

			if (returnCsvInf != null) {
				return true;
			}
		}
		return false;
	}

	private void getCustomMethodUrl(TableElementMetaInfo tableElementMetaInfo, List<ReturnCsvInfo> returnCsvInfo, String eleTag) {

		ReturnCsvInfo returnCsvInf = returnCsvInfo.stream().filter(f -> Objects.nonNull(f.getPanEleTag()) && f.getPanEleTag().equals(eleTag)).findAny().orElse(null);

		if (returnCsvInf != null) {
			tableElementMetaInfo.setMethodURI(returnCsvInf.getPanUrl());
			tableElementMetaInfo.setFormula(returnCsvInf.getPanFormula());
			tableElementMetaInfo.setErrorCode(returnCsvInf.getPanErrorCode());
			tableElementMetaInfo.setFormulaType(returnCsvInf.getFormulaType());
			tableElementMetaInfo.setMethodType(returnCsvInf.getMethodType());
			tableElementMetaInfo.setKeyFetch(returnCsvInf.isKeyFetch());
			tableElementMetaInfo.setNullAllow(returnCsvInf.isNullAllow());
			if (returnCsvInf.isPairCheck()) {
				tableElementMetaInfo.setPairCheck(returnCsvInf.isPairCheck());
				tableElementMetaInfo.setSplitType(returnCsvInf.getSplitType());
			}
		}
	}

	private void getMethodUrlAndMethodType(TableElementMetaInfo tableElementMetaInfo, String eleTag) {

		String dynamicUrl = dynamicEleColService.getDataByElementDefName(eleTag.split(CsvConversionConstants.UNDERSCORE.getConstantVal())[eleTag.split(CsvConversionConstants.UNDERSCORE.getConstantVal()).length - 1]);

		if (StringUtils.isNoneBlank(dynamicUrl)) {

			ApiDetails apiDetails = new Gson().fromJson(dynamicUrl, ApiDetails.class);

			if (apiDetails != null) {
				tableElementMetaInfo.setMethodURI(apiDetails.getUrl());
				tableElementMetaInfo.setMethodType(apiDetails.getMethodType());
			}

		}
	}

	private static String getColEleRegex(String elementRegex, String colRegex) {
		Map<String, String> regexJScriptToJavaMap = getMap();
		//		

		if (StringUtils.isNotBlank(colRegex) && StringUtils.isNotBlank(regexJScriptToJavaMap.get(colRegex))) {
			return regexJScriptToJavaMap.get(colRegex);
		}

		if (StringUtils.isNotBlank(elementRegex) && StringUtils.isNotBlank(regexJScriptToJavaMap.get(elementRegex))) {

			return regexJScriptToJavaMap.get(elementRegex);
		}
		return null;
	}

	private static Map<String, String> getMap() {
		Map<String, String> retMap = new HashMap<>();

		retMap.put("/[^0-9]/g", "[0-9]+"); // 2
		retMap.put("/[^A-Za-z ]/g", "^[\\p{L} .'-]+$"); // alphabets only.. with or without space // 1
		retMap.put("/[REGEX_EMAIL_ADDRESS]/g", "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"); // 16
		retMap.put("/[^A-Za-z0-9 ]/g", "^[a-zA-Z0-9 ]*$"); // 8
		retMap.put("/[REGX_DECIMAL]/g", "^-?[0-9]*\\.?[0-9]{1,2}+$"); // 12
		retMap.put("/([A-Z]){5}([0-9]){4}([A-Z]){1}$/", "([A-Z]){5}([0-9]){4}([A-Z]){1}$"); // pan number // 22
		retMap.put("/[REGEX_POSITIVE_DECIMAL]/g", "^[0-9]*\\.?[0-9]{1,2}+$"); //27
		retMap.put("/[REGEX_MOBILE_NUMBER]/g", "[0-9]+"); // 25
		retMap.put("/[^A-Za-z0-9!@#$%^*(),.=+:;_ ]/g", "[A-Za-z0-9!@#$%^*(),.=+:;_ ]*$"); // text fields with special char // 10
		retMap.put("/[REGEX_AD_CODE]/g", "[0-9]{5}[0-9A-Z]{2}[0-9]{7}"); // 26
		retMap.put("/[REGEX_SPECIAL_NUMERIC]/g", "[0-9!@#$%^*(),.=+:;_]*$"); // 3
		retMap.put("/[^A-Za-z!@#$%^*(),.=+:;_]/g", "[A-Za-z!@#$%^*(),.=+:;_]*$"); // 4
		retMap.put("/[^A-Za-z0-9!@#$%^*(),.=+:;_]/g", "[A-Za-z0-9!@#$%^*(),.=+:;_]*$"); // 5
		retMap.put("/[^0-9!@#$%^*(),.=+:;_]/g", "[0-9!@#$%^*(),.=+:;_]*$"); // 6
		retMap.put("/[^A-Za-z]/g", "[A-Za-z]*$"); // 7
		retMap.put("/[^A-Za-z!@#$%^*(),.=+:;_ ]/g", "[A-Za-z!@#$%^*(),.=+:;_ ]*$"); // 9
		retMap.put("/[^A-Za-z0-9]/g", "[A-Za-z0-9]*$"); // 18
		retMap.put("/[REGEX_TC]/g", "^TC.*$"); // 29
		retMap.put("/[REGEX_TELEPHONE_NO]/g", "[0-9]+"); // 30
		retMap.put("/[REGEX_NO_TRIM_ZERO]/g", "[0-9]+"); // 31
		retMap.put("/[REGEX_CURRENCY_PAIR]/g", "[A-Z]{3}[ ]{1}[-]{1}[ ]{1}[A-Z]{3}"); // 33
		retMap.put("/[REGEX_TIVE_DECIMAL]/g", "^-[0-9]*\\.?[0-9]{1,2}+$"); // 34
		retMap.put("/[REGEX_RATIO]/g", "^[0-9]*\\:?[0-9]+$"); // 35
		return retMap;
	}

	/*
	 * In the columnBean, each bean can have mutiple child ids (seperated by comma), so we have to print the column label with each lables of child id's.
	 * we have to sort the child ids with the help if col order because in each columnBean child ids are not sorted
	 */
	private static void createMetaInfoFile(Map<String, TableElementMetaInfo> elementColumnPositionMap, String pathToSaveMetaInfoFile) throws IOException {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSaveMetaInfoFile))) {
			writer.write(new Gson().toJson(elementColumnPositionMap));
			writer.close();
		} catch (Exception e) {
			logger.error("Exception while writting file int: ", e);
		}

	}

	private static String getColumnLabel(ColumnBean columnBean, List<ColumnBean> columnList, List<String> childColIdList, List<String> colmsWithOutChildId) {

		logger.info(columnBean.getColId());
		// if child id of column bean is null that means it part of column or row postion. but it will check later from the code where this method is calling
		if (!columnBean.getChild().equals(CsvConversionConstants.NULL.getConstantVal())) {
			List<ColumnBean> columnBeanList = new ArrayList<>();
			String columnParentLabel = columnBean.getColLabel();
			String parentColumnTag = columnBean.getColTag();
			String colId = String.valueOf(columnBean.getColId());
			// created string of array, to store each child id
			String childIdArray[] = new String[columnBean.getChild().split(CsvConversionConstants.COMMA_SIGN.getConstantVal()).length];
			childIdArray = columnBean.getChild().split(CsvConversionConstants.COMMA_SIGN.getConstantVal());

			for (String childId : childIdArray) {

				if (!childColIdList.contains(colId + CsvConversionConstants.UNDERSCORE.getConstantVal() + childId)) {

					// adding all the child id in the list
					childColIdList.add(colId + CsvConversionConstants.UNDERSCORE.getConstantVal() + childId);

					// getting the list of child ids column bean from main column list and add that into columnBeanList.
					columnBeanList.add(columnList.stream().filter(f -> String.valueOf(f.getColId()).equals(childId)).findAny().orElse(null));
				}
			}

			//here sorting is required because child ids are not sorted in the child id variable of each column bean
			Collections.sort(columnBeanList, new Comparator<ColumnBean>() {
				@Override
				public int compare(ColumnBean o1, ColumnBean o2) {
					return o1.getColOrder().compareTo(o2.getColOrder());
				}
			});

			String columnValue = StringUtils.EMPTY;
			for (ColumnBean cB : columnBeanList) {

				if (cB.getChild().equals(CsvConversionConstants.NULL.getConstantVal())) {

					// we are iteraing child ids bean and created the sting as parent lable and child lable together.
					// here are creating all the child ids with its parent name. each pair is seperated by ! mark.

					columnValue = columnValue + columnParentLabel + " " + cB.getColLabel() + CsvConversionConstants.DOLLER_SIGN.getConstantVal() + parentColumnTag + CsvConversionConstants.UNDERSCORE.getConstantVal() + cB.getColTag() + CsvConversionConstants.EXC_MARK.getConstantVal();
				} else {
					// 3 lavel parent child hierarchy
					//					String columnValueLevel = "";
					List<ColumnBean> columnBeanLst = new ArrayList<>();
					String childIdArr[] = new String[cB.getChild().split(CsvConversionConstants.COMMA_SIGN.getConstantVal()).length];
					childIdArr = cB.getChild().split(CsvConversionConstants.COMMA_SIGN.getConstantVal());
					String childColsId = String.valueOf(cB.getColId());

					for (String childId : childIdArr) {

						if (!childColIdList.contains(childColsId + CsvConversionConstants.UNDERSCORE.getConstantVal() + childId)) {

							// adding all the child id in the list
							childColIdList.add(childColsId + CsvConversionConstants.UNDERSCORE.getConstantVal() + childId);

							// getting the list of child ids column bean from main column list and add that into columnBeanList.
							columnBeanLst.add(columnList.stream().filter(f -> String.valueOf(f.getColId()).equals(childId)).findAny().orElse(null));
						}
					}
					Collections.sort(columnBeanLst, new Comparator<ColumnBean>() {
						@Override
						public int compare(ColumnBean o1, ColumnBean o2) {
							return o1.getColOrder().compareTo(o2.getColOrder());
						}
					});

					for (ColumnBean colBean : columnBeanLst) {

						columnValue = columnValue + columnParentLabel + " " + cB.getColLabel() + " " + colBean.getColLabel() + CsvConversionConstants.DOLLER_SIGN.getConstantVal() + parentColumnTag + CsvConversionConstants.UNDERSCORE.getConstantVal() + cB.getColTag() + CsvConversionConstants.UNDERSCORE.getConstantVal() + colBean.getColTag() + CsvConversionConstants.EXC_MARK.getConstantVal();

					}

				}
			}

			return columnValue;

		} else {
			// elements without child
			colmsWithOutChildId.add(String.valueOf(columnBean.getColId()));
		}
		return null;
	}

	/*
	 * this method  would be use to return string if is use as a column 
	 * and it will return null, if it is abstract and not a part of column in the csv layout
	 */
	private static String getLabel(ElementBean elementBean, Map<String, String> eleIdAndLabelMap, String parentChildConcatString, Map<String, String> eleIdAndTagMap, String parentChildConcatTagString, boolean isTransposeInWebForm, boolean isTranspose, List<ElementBean> elementList) {

		// below map would store all the element id as a key and its label as a value
		eleIdAndLabelMap.put(String.valueOf(elementBean.getEleId()), elementBean.getEleLabel());
		eleIdAndTagMap.put(String.valueOf(elementBean.getEleId()), elementBean.getEleTag());

		// here we are checking weather the element is abstract or not, if it is then its isParent will be true and parent id would not blank
		// so in this case we have update the map with its parent name and current element name as a value
		if (elementBean.getIsParent().equals(Boolean.TRUE) && StringUtils.isNotBlank(elementBean.getParentId()) && elementBean.getFieldTypeId() != ABSTRACT_FIELD_ID && elementBean.getFieldTypeId() == TEXT_FIELD_ID) {
			eleIdAndLabelMap.computeIfPresent(String.valueOf(elementBean.getEleId()), (k, v) -> eleIdAndLabelMap.get(elementBean.getParentId()) + " " + elementBean.getEleLabel());
			if (isTransposeInWebForm) {
				eleIdAndTagMap.computeIfPresent(String.valueOf(elementBean.getEleId()), (k, v) -> eleIdAndTagMap.get(elementBean.getParentId()) + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementBean.getEleTag());
			}

			parentChildConcatString = eleIdAndLabelMap.get(elementBean.getParentId());

			// if its isParent is false and parentId is not blank that means it is child element which needs to print the column of csv and the column name will be parent label name + child label name
		} else if (elementBean.getIsParent().equals(Boolean.FALSE) && StringUtils.isNotBlank(elementBean.getParentId())) {
			parentChildConcatString = eleIdAndLabelMap.get(elementBean.getParentId()); // here we are getting its parent lable name
			if (isTransposeInWebForm) {
				parentChildConcatTagString = eleIdAndTagMap.get(elementBean.getParentId());
			}
		}

		// any label which is abstract, its abstract field id will be 9, then and that will not part of csv layout. then return null.
		if (elementBean.getFieldTypeId() == ABSTRACT_FIELD_ID || isTranspose) {

			if (elementBean.getFieldTypeId() == ABSTRACT_FIELD_ID) {
				if (StringUtils.isNotBlank(elementBean.getParentId())) {
					eleIdAndLabelMap.computeIfPresent(String.valueOf(elementBean.getEleId()), (k, v) -> eleIdAndLabelMap.get(elementBean.getParentId()) + " " + elementBean.getEleLabel());
					if (isTransposeInWebForm) {
						eleIdAndTagMap.computeIfPresent(String.valueOf(elementBean.getEleId()), (k, v) -> eleIdAndTagMap.get(elementBean.getParentId()) + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementBean.getEleTag());
					}
				}

				return null;

			} else {
				ElementBean elementBeans = elementList.stream().filter(f -> Objects.nonNull(f.getParentId()) && f.getParentId().equals(String.valueOf(elementBean.getEleId()))).findAny().orElse(null);

				if (elementBeans == null) {
					parentChildConcatString = parentChildConcatString + " " + elementBean.getEleLabel();
					if (isTransposeInWebForm) {
						if (StringUtils.isNotBlank(parentChildConcatTagString)) {
							parentChildConcatTagString = parentChildConcatTagString + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementBean.getEleTag();
						} else {
							parentChildConcatTagString = elementBean.getEleTag();
						}

						elementBean.setEleTag(parentChildConcatTagString);
					}

					return parentChildConcatString.trim();
				} else {
					return null;
				}
			}

		} else {
			// here we are getting the child info with parent lable(parent label + child label)
			if (elementBean.getIsParent().equals(Boolean.FALSE) && StringUtils.isNotBlank(elementBean.getParentId())) {
				parentChildConcatString = parentChildConcatString + " " + elementBean.getEleLabel();
				if (isTransposeInWebForm) {
					parentChildConcatTagString = parentChildConcatTagString + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementBean.getEleTag();
					elementBean.setEleTag(parentChildConcatTagString);
				}

				return parentChildConcatString;
			} else {
				if (StringUtils.isNoneBlank(parentChildConcatString)) {
					parentChildConcatString = parentChildConcatString + " " + elementBean.getEleLabel(); // if element have no parent, the we have to direct return the label
				} else {
					parentChildConcatString = parentChildConcatString + elementBean.getEleLabel(); // if element have no parent, the we have to direct return the label
				}

				if (isTransposeInWebForm) {
					parentChildConcatTagString = parentChildConcatTagString + elementBean.getEleTag();
					elementBean.setEleTag(parentChildConcatTagString);
				}

				return parentChildConcatString;
			}

		}
	}
}