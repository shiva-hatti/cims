package com.iris.model;

import java.io.Serializable;
import java.util.List;

public class NbfcProfileDetailsPage3Dto implements Serializable {
	private static final long serialVersionUID = 2437040705766909674L;

	private String insuranceBusines;
	private Long whetherNbfcChange;
	private String reasonOfLastChange;
	private String dateOfLastChange;
	private String lastName;
	private Long mutualFundBusiness;
	private Long redioCobrandedCreditCard;
	private String bankInstitution;
	private Long creditInfo;
	private boolean chkCreditInfo;
	private boolean chkEquifax;
	private boolean chkExperian;
	private boolean chkCrifInfo;
	private boolean chkOther;
	private String stockexchanges;
	private String chkOtherText;
	private Long radioHoldingCompany;
	private String permissiondate;
	private Long radioWOSJVabroad;
	private String wOSJV;
	private Long radioForeignDirectInfo;
	private String fiuInd;
	private String reasonNonRegId;
	private Long radioFinancialInt;
	private Long radioCERSAI;
	private String cKYC;
	private Long radioCERSAIMortg;
	private String reasonfurnishNonReg;
	private String nonReg;
	private String cersaIInst;
	private Long radionbfcTakenAction;
	private String lastprofileupdateFile;
	private String pan;
	private String holdingCompanyPAN;
	private String holdingCompanyCIN;
	private String holdingCompanyName;
	private Long radioSEBInorms;
	private boolean chkMLDs;
	private boolean chkNCRPS;
	private boolean chkPDIS;
	private boolean chkDepositoryReceipts;
	private boolean chkPNCPS;
	private boolean chkCPs;
	private boolean chkOtherIfAny;
	private Long radioubsidiaryAssociate;
	private String otherTextChkOther;
	private String shareHolderDetailsListStr;
	private String directorDetailsListStr;
	private Long nbfcTakenmajorcorpAction;
	private List<NbfcShareHolderDetails> shareHolderDetailsList;
	private List<NbfcDirectorTypeDto> nbfcDirectorTypeDtoList;
	private List<SubsidiaryAssociateGroupDto> subsidiaryAssociateGroupList;
	private String subsidiaryAssociateGroupStr;
	private String stockexchangesId;
	private String radioYesNBFCEquity;

	public String getInsuranceBusines() {
		return insuranceBusines;
	}

	public void setInsuranceBusines(String insuranceBusines) {
		this.insuranceBusines = insuranceBusines;
	}

	public Long getWhetherNbfcChange() {
		return whetherNbfcChange;
	}

	public void setWhetherNbfcChange(Long whetherNbfcChange) {
		this.whetherNbfcChange = whetherNbfcChange;
	}

	public String getReasonOfLastChange() {
		return reasonOfLastChange;
	}

	public void setReasonOfLastChange(String reasonOfLastChange) {
		this.reasonOfLastChange = reasonOfLastChange;
	}

	public String getDateOfLastChange() {
		return dateOfLastChange;
	}

	public void setDateOfLastChange(String dateOfLastChange) {
		this.dateOfLastChange = dateOfLastChange;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getMutualFundBusiness() {
		return mutualFundBusiness;
	}

	public void setMutualFundBusiness(Long mutualFundBusiness) {
		this.mutualFundBusiness = mutualFundBusiness;
	}

	public Long getRedioCobrandedCreditCard() {
		return redioCobrandedCreditCard;
	}

	public void setRedioCobrandedCreditCard(Long redioCobrandedCreditCard) {
		this.redioCobrandedCreditCard = redioCobrandedCreditCard;
	}

	public Long getCreditInfo() {
		return creditInfo;
	}

	public void setCreditInfo(Long creditInfo) {
		this.creditInfo = creditInfo;
	}

	public boolean isChkCreditInfo() {
		return chkCreditInfo;
	}

	public void setChkCreditInfo(boolean chkCreditInfo) {
		this.chkCreditInfo = chkCreditInfo;
	}

	public boolean isChkEquifax() {
		return chkEquifax;
	}

	public void setChkEquifax(boolean chkEquifax) {
		this.chkEquifax = chkEquifax;
	}

	public boolean isChkExperian() {
		return chkExperian;
	}

	public void setChkExperian(boolean chkExperian) {
		this.chkExperian = chkExperian;
	}

	public boolean isChkCrifInfo() {
		return chkCrifInfo;
	}

	public void setChkCrifInfo(boolean chkCrifInfo) {
		this.chkCrifInfo = chkCrifInfo;
	}

	public boolean isChkOther() {
		return chkOther;
	}

	public void setChkOther(boolean chkOther) {
		this.chkOther = chkOther;
	}

	public String getStockexchanges() {
		return stockexchanges;
	}

	public void setStockexchanges(String stockexchanges) {
		this.stockexchanges = stockexchanges;
	}

	public String getChkOtherText() {
		return chkOtherText;
	}

	public void setChkOtherText(String chkOtherText) {
		this.chkOtherText = chkOtherText;
	}

	public Long getRadioHoldingCompany() {
		return radioHoldingCompany;
	}

	public void setRadioHoldingCompany(Long radioHoldingCompany) {
		this.radioHoldingCompany = radioHoldingCompany;
	}

	public Long getRadioWOSJVabroad() {
		return radioWOSJVabroad;
	}

	public void setRadioWOSJVabroad(Long radioWOSJVabroad) {
		this.radioWOSJVabroad = radioWOSJVabroad;
	}

	public String getwOSJV() {
		return wOSJV;
	}

	public void setwOSJV(String wOSJV) {
		this.wOSJV = wOSJV;
	}

	public Long getRadioForeignDirectInfo() {
		return radioForeignDirectInfo;
	}

	public void setRadioForeignDirectInfo(Long radioForeignDirectInfo) {
		this.radioForeignDirectInfo = radioForeignDirectInfo;
	}

	public String getFiuInd() {
		return fiuInd;
	}

	public void setFiuInd(String fiuInd) {
		this.fiuInd = fiuInd;
	}

	public String getReasonNonRegId() {
		return reasonNonRegId;
	}

	public void setReasonNonRegId(String reasonNonRegId) {
		this.reasonNonRegId = reasonNonRegId;
	}

	public Long getRadioFinancialInt() {
		return radioFinancialInt;
	}

	public void setRadioFinancialInt(Long radioFinancialInt) {
		this.radioFinancialInt = radioFinancialInt;
	}

	public Long getRadioCERSAI() {
		return radioCERSAI;
	}

	public void setRadioCERSAI(Long radioCERSAI) {
		this.radioCERSAI = radioCERSAI;
	}

	public String getcKYC() {
		return cKYC;
	}

	public void setcKYC(String cKYC) {
		this.cKYC = cKYC;
	}

	public Long getRadioCERSAIMortg() {
		return radioCERSAIMortg;
	}

	public void setRadioCERSAIMortg(Long radioCERSAIMortg) {
		this.radioCERSAIMortg = radioCERSAIMortg;
	}

	public String getReasonfurnishNonReg() {
		return reasonfurnishNonReg;
	}

	public void setReasonfurnishNonReg(String reasonfurnishNonReg) {
		this.reasonfurnishNonReg = reasonfurnishNonReg;
	}

	public String getNonReg() {
		return nonReg;
	}

	public void setNonReg(String nonReg) {
		this.nonReg = nonReg;
	}

	public Long getRadionbfcTakenAction() {
		return radionbfcTakenAction;
	}

	public void setRadionbfcTakenAction(Long radionbfcTakenAction) {
		this.radionbfcTakenAction = radionbfcTakenAction;
	}

	public String getLastprofileupdateFile() {
		return lastprofileupdateFile;
	}

	public void setLastprofileupdateFile(String lastprofileupdateFile) {
		this.lastprofileupdateFile = lastprofileupdateFile;
	}

	public String getHoldingCompanyPAN() {
		return holdingCompanyPAN;
	}

	public void setHoldingCompanyPAN(String holdingCompanyPAN) {
		this.holdingCompanyPAN = holdingCompanyPAN;
	}

	public String getHoldingCompanyCIN() {
		return holdingCompanyCIN;
	}

	public void setHoldingCompanyCIN(String holdingCompanyCIN) {
		this.holdingCompanyCIN = holdingCompanyCIN;
	}

	public String getHoldingCompanyName() {
		return holdingCompanyName;
	}

	public void setHoldingCompanyName(String holdingCompanyName) {
		this.holdingCompanyName = holdingCompanyName;
	}

	public Long getRadioSEBInorms() {
		return radioSEBInorms;
	}

	public void setRadioSEBInorms(Long radioSEBInorms) {
		this.radioSEBInorms = radioSEBInorms;
	}

	public boolean isChkMLDs() {
		return chkMLDs;
	}

	public void setChkMLDs(boolean chkMLDs) {
		this.chkMLDs = chkMLDs;
	}

	public boolean isChkNCRPS() {
		return chkNCRPS;
	}

	public void setChkNCRPS(boolean chkNCRPS) {
		this.chkNCRPS = chkNCRPS;
	}

	public boolean isChkPDIS() {
		return chkPDIS;
	}

	public void setChkPDIS(boolean chkPDIS) {
		this.chkPDIS = chkPDIS;
	}

	public boolean isChkDepositoryReceipts() {
		return chkDepositoryReceipts;
	}

	public void setChkDepositoryReceipts(boolean chkDepositoryReceipts) {
		this.chkDepositoryReceipts = chkDepositoryReceipts;
	}

	public boolean isChkPNCPS() {
		return chkPNCPS;
	}

	public void setChkPNCPS(boolean chkPNCPS) {
		this.chkPNCPS = chkPNCPS;
	}

	public boolean isChkCPs() {
		return chkCPs;
	}

	public void setChkCPs(boolean chkCPs) {
		this.chkCPs = chkCPs;
	}

	public boolean isChkOtherIfAny() {
		return chkOtherIfAny;
	}

	public void setChkOtherIfAny(boolean chkOtherIfAny) {
		this.chkOtherIfAny = chkOtherIfAny;
	}

	public Long getRadioubsidiaryAssociate() {
		return radioubsidiaryAssociate;
	}

	public void setRadioubsidiaryAssociate(Long radioubsidiaryAssociate) {
		this.radioubsidiaryAssociate = radioubsidiaryAssociate;
	}

	public String getOtherTextChkOther() {
		return otherTextChkOther;
	}

	public void setOtherTextChkOther(String otherTextChkOther) {
		this.otherTextChkOther = otherTextChkOther;
	}

	public String getShareHolderDetailsListStr() {
		return shareHolderDetailsListStr;
	}

	public void setShareHolderDetailsListStr(String shareHolderDetailsListStr) {
		this.shareHolderDetailsListStr = shareHolderDetailsListStr;
	}

	public String getDirectorDetailsListStr() {
		return directorDetailsListStr;
	}

	public void setDirectorDetailsListStr(String directorDetailsListStr) {
		this.directorDetailsListStr = directorDetailsListStr;
	}

	public Long getNbfcTakenmajorcorpAction() {
		return nbfcTakenmajorcorpAction;
	}

	public void setNbfcTakenmajorcorpAction(Long nbfcTakenmajorcorpAction) {
		this.nbfcTakenmajorcorpAction = nbfcTakenmajorcorpAction;
	}

	public List<NbfcShareHolderDetails> getShareHolderDetailsList() {
		return shareHolderDetailsList;
	}

	public void setShareHolderDetailsList(List<NbfcShareHolderDetails> shareHolderDetailsList) {
		this.shareHolderDetailsList = shareHolderDetailsList;
	}

	public List<NbfcDirectorTypeDto> getNbfcDirectorTypeDtoList() {
		return nbfcDirectorTypeDtoList;
	}

	public void setNbfcDirectorTypeDtoList(List<NbfcDirectorTypeDto> nbfcDirectorTypeDtoList) {
		this.nbfcDirectorTypeDtoList = nbfcDirectorTypeDtoList;
	}

	public String getBankInstitution() {
		return bankInstitution;
	}

	public void setBankInstitution(String bankInstitution) {
		this.bankInstitution = bankInstitution;
	}

	public String getPermissiondate() {
		return permissiondate;
	}

	public void setPermissiondate(String permissiondate) {
		this.permissiondate = permissiondate;
	}

	public String getCersaIInst() {
		return cersaIInst;
	}

	public void setCersaIInst(String cersaIInst) {
		this.cersaIInst = cersaIInst;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public List<SubsidiaryAssociateGroupDto> getSubsidiaryAssociateGroupList() {
		return subsidiaryAssociateGroupList;
	}

	public void setSubsidiaryAssociateGroupList(List<SubsidiaryAssociateGroupDto> subsidiaryAssociateGroupList) {
		this.subsidiaryAssociateGroupList = subsidiaryAssociateGroupList;
	}

	public String getSubsidiaryAssociateGroupStr() {
		return subsidiaryAssociateGroupStr;
	}

	public void setSubsidiaryAssociateGroupStr(String subsidiaryAssociateGroupStr) {
		this.subsidiaryAssociateGroupStr = subsidiaryAssociateGroupStr;
	}

	public String getStockexchangesId() {
		return stockexchangesId;
	}

	public void setStockexchangesId(String stockexchangesId) {
		this.stockexchangesId = stockexchangesId;
	}

	public String getRadioYesNBFCEquity() {
		return radioYesNBFCEquity;
	}

	public void setRadioYesNBFCEquity(String radioYesNBFCEquity) {
		this.radioYesNBFCEquity = radioYesNBFCEquity;
	}
}