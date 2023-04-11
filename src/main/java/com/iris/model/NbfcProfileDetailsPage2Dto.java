/**
 * 
 */
package com.iris.model;

import java.io.Serializable;

/**
 * @author Pradnya
 *
 */
public class NbfcProfileDetailsPage2Dto implements Serializable {

	private static final long serialVersionUID = 7416981690009858287L;

	//Registered office address details
	private String regAddAddressLine1;
	private String regAddCity;
	private String regOffPincode;
	private String regAddAddressLine2;
	private String regAddState;
	private String isSameAddress;

	//Corporate office address details
	private String corpOffAddressLine1;
	private String corpOffCity;
	private String corpOffPincode;
	private String corpOffAddressLine2;
	private String corpOffState;

	//dir Details
	private String dirName;
	private String dirDesig;
	private String dirAppSinceDt;
	private String dirToIncubmtPosnDt;
	private String dirLocatedCity;
	private String dirSTDCode;
	private String dirLandlineNo1;
	private String dirLandlineNo2;
	private String dirMobNo;
	private String dirOffcEml;
	private String dirGenEml;
	private String dirFaxNo;
	private String dirAltContPrsnNm;
	private String dirAltContPrsnEml;
	private String dirAltContPrsnMobNo;

	//Chief Operating Offc (Head of Operations)
	private String chiefOpOffcName;
	private String chiefOpOffcDesig;
	private String chiefOpOffcAppSinceDt;
	private String chiefOpOffcToIncubmtPosnDt;
	private String chiefOpOffcLocatedCity;
	private String chiefOpOffcSTDCode;
	private String chiefOpOffcLandlineNo1;
	private String chiefOpOffcLandlineNo2;
	private String chiefOpOffcMobNo;
	private String chiefOpOffcEml;
	private String chiefOpOffcGenEml;
	private String chiefOpOffcFaxNo;
	private String chiefOpOffcAltContPrsnNm;
	private String chiefOpOffcAltContPrsnEml;
	private String chiefOpOffcAltContPrsnMobNo;

	//Chief Financial Offc 
	private String chiefFinOffcName;
	private String chiefFinOffcDesig;
	private String chiefFinOffcAppSinceDt;
	private String chiefFinOffcToIncubmtPosnDt;
	private String chiefFinOffcLocatedCity;
	private String chiefFinOffcSTDCode;
	private String chiefFinOffcLandlineNo1;
	private String chiefFinOffcLandlineNo2;
	private String chiefFinOffcMobNo;
	private String chiefFinOffcEml;
	private String chiefFinOffcGenEml;
	private String chiefFinOffcFaxNo;
	private String chiefFinOffcAltContPrsnNm;
	private String chiefFinOffcAltContPrsnEml;
	private String chiefFinOffcAltContPrsnMobNo;

	//Chief / Group Risk  Offc
	private String grpRiskOffcName;
	private String grpRiskOffcDesig;
	private String grpRiskOffcAppSinceDt;
	private String grpRiskOffcToIncubmtPosnDt;
	private String grpRiskOffcLocatedCity;
	private String grpRiskOffcSTDCode;
	private String grpRiskOffcLandlineNo1;
	private String grpRiskOffcLandlineNo2;
	private String grpRiskOffcMobNo;
	private String grpRiskOffcEml;
	private String grpRiskOffcGenEml;
	private String grpRiskOffcFaxNo;
	private String grpRiskOffcAltContPrsnNm;
	private String grpRiskOffcAltContPrsnEml;
	private String grpRiskOffcAltContPrsnMobNo;

	//Chief / Group Compliance Offc
	private String grpCompOffcName;
	private String grpCompOffcDesig;
	private String grpCompOffcAppSinceDt;
	private String grpCompOffcToIncubmtPosnDt;
	private String grpCompOffcLocatedCity;
	private String grpCompOffcSTDCode;
	private String grpCompOffcLandlineNo1;
	private String grpCompOffcLandlineNo2;
	private String grpCompOffcMobNo;
	private String grpCompOffcEml;
	private String grpCompOffcGenEml;
	private String grpCompOffcFaxNo;
	private String grpCompOffcAltContPrsnNm;
	private String grpCompOffcAltContPrsnEml;
	private String grpCompOffcAltContPrsnMobNo;

	//Chief Information Offc (Chief of IT)
	private String chiefITOffcName;
	private String chiefITOffcDesig;
	private String chiefITOffcAppSinceDt;
	private String chiefITOffcToIncubmtPosnDt;
	private String chiefITOffcLocatedCity;
	private String chiefITOffcSTDCode;
	private String chiefITOffcLandlineNo1;
	private String chiefITOffcLandlineNo2;
	private String chiefITOffcMobNo;
	private String chiefITOffcEml;
	private String chiefITOffcGenEml;
	private String chiefITOffcFaxNo;
	private String chiefITOffcAltContPrsnNm;
	private String chiefITOffcAltContPrsnEml;
	private String chiefITOffcAltContPrsnMobNo;

	//Chief Information Security Offc (CISO)
	private String chiefInfoSecOffcName;
	private String chiefInfoSecOffcDesig;
	private String chiefInfoSecOffcAppSinceDt;
	private String chiefInfoSecOffcToIncubmtPosnDt;
	private String chiefInfoSecOffcLocatedCity;
	private String chiefInfoSecOffcSTDCode;
	private String chiefInfoSecOffcLandlineNo1;
	private String chiefInfoSecOffcLandlineNo2;
	private String chiefInfoSecOffcMobNo;
	private String chiefInfoSecOffcEml;
	private String chiefInfoSecOffcGenEml;
	private String chiefInfoSecOffcFaxNo;
	private String chiefInfoSecOffcAltContPrsnNm;
	private String chiefInfoSecOffcAltContPrsnEml;
	private String chiefInfoSecOffcAltContPrsnMobNo;

	//Head of Treasury & Investments
	private String treasuryInvheadName;
	private String treasuryInvheadDesig;
	private String treasuryInvheadAppSinceDt;
	private String treasuryInvheadToIncubmtPosnDt;
	private String treasuryInvheadLocatedCity;
	private String treasuryInvheadSTDCode;
	private String treasuryInvheadLandlineNo1;
	private String treasuryInvheadLandlineNo2;
	private String treasuryInvheadMobNo;
	private String treasuryInvheadOffcEml;
	private String treasuryInvheadGenEml;
	private String treasuryInvheadFaxNo;
	private String treasuryInvheadAltContPrsnNm;
	private String treasuryInvheadAltContPrsnEml;
	private String treasuryInvheadAltContPrsnMobNo;

	//Nodal Offc for Submission of Supervisory Returns
	private String nodalOffcName;
	private String nodalOffcDesig;
	private String nodalOffcAppSinceDt;
	private String nodalOffcToIncubmtPosnDt;
	private String nodalOffcLocatedCity;
	private String nodalOffcSTDCode;
	private String nodalOffcLandlineNo1;
	private String nodalOffcLandlineNo2;
	private String nodalOffcMobNo;
	private String nodalOffcEml;
	private String nodalOffcGenEml;
	private String nodalOffcFaxNo;
	private String nodalOffcAltContPrsnNm;
	private String nodalOffcAltContPrsnEml;
	private String nodalOffcAltContPrsnMobNo;

	//Statutory Auditor/s Details
	private String nameOfStatAuditFirm;
	private String panOfStatutoryAuditFirm;
	private String regNumberOfAuditFirm;
	private String nameAuditorOrPartner;
	private String statAuditDesig;
	private String statAuditMembershipNo;
	private String statAuditAddr;
	private String statAuditDateOfAppointment;
	private String dateExpiryOfContract;
	private String satutoryAuditorPeriodOfContract;
	private String emailOfstatAudit;
	private String statAuditOfficePhnNo;
	private String rdYesNo;
	private String fromDate;
	private String toDate;
	private String bankersDetailsTableStr;

	public String getRegAddAddressLine1() {
		return regAddAddressLine1;
	}

	public void setRegAddAddressLine1(String regAddAddressLine1) {
		this.regAddAddressLine1 = regAddAddressLine1;
	}

	public String getRegAddCity() {
		return regAddCity;
	}

	public void setRegAddCity(String regAddCity) {
		this.regAddCity = regAddCity;
	}

	public String getRegOffPincode() {
		return regOffPincode;
	}

	public void setRegOffPincode(String regOffPincode) {
		this.regOffPincode = regOffPincode;
	}

	public String getRegAddAddressLine2() {
		return regAddAddressLine2;
	}

	public void setRegAddAddressLine2(String regAddAddressLine2) {
		this.regAddAddressLine2 = regAddAddressLine2;
	}

	public String getRegAddState() {
		return regAddState;
	}

	public void setRegAddState(String regAddState) {
		this.regAddState = regAddState;
	}

	public String getIsSameAddress() {
		return isSameAddress;
	}

	public void setIsSameAddress(String isSameAddress) {
		this.isSameAddress = isSameAddress;
	}

	public String getCorpOffAddressLine1() {
		return corpOffAddressLine1;
	}

	public void setCorpOffAddressLine1(String corpOffAddressLine1) {
		this.corpOffAddressLine1 = corpOffAddressLine1;
	}

	public String getCorpOffCity() {
		return corpOffCity;
	}

	public void setCorpOffCity(String corpOffCity) {
		this.corpOffCity = corpOffCity;
	}

	public String getCorpOffPincode() {
		return corpOffPincode;
	}

	public void setCorpOffPincode(String corpOffPincode) {
		this.corpOffPincode = corpOffPincode;
	}

	public String getCorpOffAddressLine2() {
		return corpOffAddressLine2;
	}

	public void setCorpOffAddressLine2(String corpOffAddressLine2) {
		this.corpOffAddressLine2 = corpOffAddressLine2;
	}

	public String getCorpOffState() {
		return corpOffState;
	}

	public void setCorpOffState(String corpOffState) {
		this.corpOffState = corpOffState;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getDirDesig() {
		return dirDesig;
	}

	public void setDirDesig(String dirDesig) {
		this.dirDesig = dirDesig;
	}

	public String getDirAppSinceDt() {
		return dirAppSinceDt;
	}

	public void setDirAppSinceDt(String dirAppSinceDt) {
		this.dirAppSinceDt = dirAppSinceDt;
	}

	public String getDirToIncubmtPosnDt() {
		return dirToIncubmtPosnDt;
	}

	public void setDirToIncubmtPosnDt(String dirToIncubmtPosnDt) {
		this.dirToIncubmtPosnDt = dirToIncubmtPosnDt;
	}

	public String getDirLocatedCity() {
		return dirLocatedCity;
	}

	public void setDirLocatedCity(String dirLocatedCity) {
		this.dirLocatedCity = dirLocatedCity;
	}

	public String getDirSTDCode() {
		return dirSTDCode;
	}

	public void setDirSTDCode(String dirSTDCode) {
		this.dirSTDCode = dirSTDCode;
	}

	public String getDirLandlineNo1() {
		return dirLandlineNo1;
	}

	public void setDirLandlineNo1(String dirLandlineNo1) {
		this.dirLandlineNo1 = dirLandlineNo1;
	}

	public String getDirLandlineNo2() {
		return dirLandlineNo2;
	}

	public void setDirLandlineNo2(String dirLandlineNo2) {
		this.dirLandlineNo2 = dirLandlineNo2;
	}

	public String getDirMobNo() {
		return dirMobNo;
	}

	public void setDirMobNo(String dirMobNo) {
		this.dirMobNo = dirMobNo;
	}

	public String getDirOffcEml() {
		return dirOffcEml;
	}

	public void setDirOffcEml(String dirOffcEml) {
		this.dirOffcEml = dirOffcEml;
	}

	public String getDirGenEml() {
		return dirGenEml;
	}

	public void setDirGenEml(String dirGenEml) {
		this.dirGenEml = dirGenEml;
	}

	public String getDirFaxNo() {
		return dirFaxNo;
	}

	public void setDirFaxNo(String dirFaxNo) {
		this.dirFaxNo = dirFaxNo;
	}

	public String getDirAltContPrsnNm() {
		return dirAltContPrsnNm;
	}

	public void setDirAltContPrsnNm(String dirAltContPrsnNm) {
		this.dirAltContPrsnNm = dirAltContPrsnNm;
	}

	public String getDirAltContPrsnEml() {
		return dirAltContPrsnEml;
	}

	public void setDirAltContPrsnEml(String dirAltContPrsnEml) {
		this.dirAltContPrsnEml = dirAltContPrsnEml;
	}

	public String getDirAltContPrsnMobNo() {
		return dirAltContPrsnMobNo;
	}

	public void setDirAltContPrsnMobNo(String dirAltContPrsnMobNo) {
		this.dirAltContPrsnMobNo = dirAltContPrsnMobNo;
	}

	public String getChiefOpOffcName() {
		return chiefOpOffcName;
	}

	public void setChiefOpOffcName(String chiefOpOffcName) {
		this.chiefOpOffcName = chiefOpOffcName;
	}

	public String getChiefOpOffcDesig() {
		return chiefOpOffcDesig;
	}

	public void setChiefOpOffcDesig(String chiefOpOffcDesig) {
		this.chiefOpOffcDesig = chiefOpOffcDesig;
	}

	public String getChiefOpOffcAppSinceDt() {
		return chiefOpOffcAppSinceDt;
	}

	public void setChiefOpOffcAppSinceDt(String chiefOpOffcAppSinceDt) {
		this.chiefOpOffcAppSinceDt = chiefOpOffcAppSinceDt;
	}

	public String getChiefOpOffcToIncubmtPosnDt() {
		return chiefOpOffcToIncubmtPosnDt;
	}

	public void setChiefOpOffcToIncubmtPosnDt(String chiefOpOffcToIncubmtPosnDt) {
		this.chiefOpOffcToIncubmtPosnDt = chiefOpOffcToIncubmtPosnDt;
	}

	public String getChiefOpOffcLocatedCity() {
		return chiefOpOffcLocatedCity;
	}

	public void setChiefOpOffcLocatedCity(String chiefOpOffcLocatedCity) {
		this.chiefOpOffcLocatedCity = chiefOpOffcLocatedCity;
	}

	public String getChiefOpOffcSTDCode() {
		return chiefOpOffcSTDCode;
	}

	public void setChiefOpOffcSTDCode(String chiefOpOffcSTDCode) {
		this.chiefOpOffcSTDCode = chiefOpOffcSTDCode;
	}

	public String getChiefOpOffcLandlineNo1() {
		return chiefOpOffcLandlineNo1;
	}

	public void setChiefOpOffcLandlineNo1(String chiefOpOffcLandlineNo1) {
		this.chiefOpOffcLandlineNo1 = chiefOpOffcLandlineNo1;
	}

	public String getChiefOpOffcLandlineNo2() {
		return chiefOpOffcLandlineNo2;
	}

	public void setChiefOpOffcLandlineNo2(String chiefOpOffcLandlineNo2) {
		this.chiefOpOffcLandlineNo2 = chiefOpOffcLandlineNo2;
	}

	public String getChiefOpOffcMobNo() {
		return chiefOpOffcMobNo;
	}

	public void setChiefOpOffcMobNo(String chiefOpOffcMobNo) {
		this.chiefOpOffcMobNo = chiefOpOffcMobNo;
	}

	public String getChiefOpOffcEml() {
		return chiefOpOffcEml;
	}

	public void setChiefOpOffcEml(String chiefOpOffcEml) {
		this.chiefOpOffcEml = chiefOpOffcEml;
	}

	public String getChiefOpOffcGenEml() {
		return chiefOpOffcGenEml;
	}

	public void setChiefOpOffcGenEml(String chiefOpOffcGenEml) {
		this.chiefOpOffcGenEml = chiefOpOffcGenEml;
	}

	public String getChiefOpOffcFaxNo() {
		return chiefOpOffcFaxNo;
	}

	public void setChiefOpOffcFaxNo(String chiefOpOffcFaxNo) {
		this.chiefOpOffcFaxNo = chiefOpOffcFaxNo;
	}

	public String getChiefOpOffcAltContPrsnNm() {
		return chiefOpOffcAltContPrsnNm;
	}

	public void setChiefOpOffcAltContPrsnNm(String chiefOpOffcAltContPrsnNm) {
		this.chiefOpOffcAltContPrsnNm = chiefOpOffcAltContPrsnNm;
	}

	public String getChiefOpOffcAltContPrsnEml() {
		return chiefOpOffcAltContPrsnEml;
	}

	public void setChiefOpOffcAltContPrsnEml(String chiefOpOffcAltContPrsnEml) {
		this.chiefOpOffcAltContPrsnEml = chiefOpOffcAltContPrsnEml;
	}

	public String getChiefOpOffcAltContPrsnMobNo() {
		return chiefOpOffcAltContPrsnMobNo;
	}

	public void setChiefOpOffcAltContPrsnMobNo(String chiefOpOffcAltContPrsnMobNo) {
		this.chiefOpOffcAltContPrsnMobNo = chiefOpOffcAltContPrsnMobNo;
	}

	public String getChiefFinOffcName() {
		return chiefFinOffcName;
	}

	public void setChiefFinOffcName(String chiefFinOffcName) {
		this.chiefFinOffcName = chiefFinOffcName;
	}

	public String getChiefFinOffcDesig() {
		return chiefFinOffcDesig;
	}

	public void setChiefFinOffcDesig(String chiefFinOffcDesig) {
		this.chiefFinOffcDesig = chiefFinOffcDesig;
	}

	public String getChiefFinOffcAppSinceDt() {
		return chiefFinOffcAppSinceDt;
	}

	public void setChiefFinOffcAppSinceDt(String chiefFinOffcAppSinceDt) {
		this.chiefFinOffcAppSinceDt = chiefFinOffcAppSinceDt;
	}

	public String getChiefFinOffcToIncubmtPosnDt() {
		return chiefFinOffcToIncubmtPosnDt;
	}

	public void setChiefFinOffcToIncubmtPosnDt(String chiefFinOffcToIncubmtPosnDt) {
		this.chiefFinOffcToIncubmtPosnDt = chiefFinOffcToIncubmtPosnDt;
	}

	public String getChiefFinOffcLocatedCity() {
		return chiefFinOffcLocatedCity;
	}

	public void setChiefFinOffcLocatedCity(String chiefFinOffcLocatedCity) {
		this.chiefFinOffcLocatedCity = chiefFinOffcLocatedCity;
	}

	public String getChiefFinOffcSTDCode() {
		return chiefFinOffcSTDCode;
	}

	public void setChiefFinOffcSTDCode(String chiefFinOffcSTDCode) {
		this.chiefFinOffcSTDCode = chiefFinOffcSTDCode;
	}

	public String getChiefFinOffcLandlineNo1() {
		return chiefFinOffcLandlineNo1;
	}

	public void setChiefFinOffcLandlineNo1(String chiefFinOffcLandlineNo1) {
		this.chiefFinOffcLandlineNo1 = chiefFinOffcLandlineNo1;
	}

	public String getChiefFinOffcLandlineNo2() {
		return chiefFinOffcLandlineNo2;
	}

	public void setChiefFinOffcLandlineNo2(String chiefFinOffcLandlineNo2) {
		this.chiefFinOffcLandlineNo2 = chiefFinOffcLandlineNo2;
	}

	public String getChiefFinOffcMobNo() {
		return chiefFinOffcMobNo;
	}

	public void setChiefFinOffcMobNo(String chiefFinOffcMobNo) {
		this.chiefFinOffcMobNo = chiefFinOffcMobNo;
	}

	public String getChiefFinOffcEml() {
		return chiefFinOffcEml;
	}

	public void setChiefFinOffcEml(String chiefFinOffcEml) {
		this.chiefFinOffcEml = chiefFinOffcEml;
	}

	public String getChiefFinOffcGenEml() {
		return chiefFinOffcGenEml;
	}

	public void setChiefFinOffcGenEml(String chiefFinOffcGenEml) {
		this.chiefFinOffcGenEml = chiefFinOffcGenEml;
	}

	public String getChiefFinOffcFaxNo() {
		return chiefFinOffcFaxNo;
	}

	public void setChiefFinOffcFaxNo(String chiefFinOffcFaxNo) {
		this.chiefFinOffcFaxNo = chiefFinOffcFaxNo;
	}

	public String getChiefFinOffcAltContPrsnNm() {
		return chiefFinOffcAltContPrsnNm;
	}

	public void setChiefFinOffcAltContPrsnNm(String chiefFinOffcAltContPrsnNm) {
		this.chiefFinOffcAltContPrsnNm = chiefFinOffcAltContPrsnNm;
	}

	public String getChiefFinOffcAltContPrsnEml() {
		return chiefFinOffcAltContPrsnEml;
	}

	public void setChiefFinOffcAltContPrsnEml(String chiefFinOffcAltContPrsnEml) {
		this.chiefFinOffcAltContPrsnEml = chiefFinOffcAltContPrsnEml;
	}

	public String getChiefFinOffcAltContPrsnMobNo() {
		return chiefFinOffcAltContPrsnMobNo;
	}

	public void setChiefFinOffcAltContPrsnMobNo(String chiefFinOffcAltContPrsnMobNo) {
		this.chiefFinOffcAltContPrsnMobNo = chiefFinOffcAltContPrsnMobNo;
	}

	public String getGrpRiskOffcName() {
		return grpRiskOffcName;
	}

	public void setGrpRiskOffcName(String grpRiskOffcName) {
		this.grpRiskOffcName = grpRiskOffcName;
	}

	public String getGrpRiskOffcDesig() {
		return grpRiskOffcDesig;
	}

	public void setGrpRiskOffcDesig(String grpRiskOffcDesig) {
		this.grpRiskOffcDesig = grpRiskOffcDesig;
	}

	public String getGrpRiskOffcAppSinceDt() {
		return grpRiskOffcAppSinceDt;
	}

	public void setGrpRiskOffcAppSinceDt(String grpRiskOffcAppSinceDt) {
		this.grpRiskOffcAppSinceDt = grpRiskOffcAppSinceDt;
	}

	public String getGrpRiskOffcToIncubmtPosnDt() {
		return grpRiskOffcToIncubmtPosnDt;
	}

	public void setGrpRiskOffcToIncubmtPosnDt(String grpRiskOffcToIncubmtPosnDt) {
		this.grpRiskOffcToIncubmtPosnDt = grpRiskOffcToIncubmtPosnDt;
	}

	public String getGrpRiskOffcLocatedCity() {
		return grpRiskOffcLocatedCity;
	}

	public void setGrpRiskOffcLocatedCity(String grpRiskOffcLocatedCity) {
		this.grpRiskOffcLocatedCity = grpRiskOffcLocatedCity;
	}

	public String getGrpRiskOffcSTDCode() {
		return grpRiskOffcSTDCode;
	}

	public void setGrpRiskOffcSTDCode(String grpRiskOffcSTDCode) {
		this.grpRiskOffcSTDCode = grpRiskOffcSTDCode;
	}

	public String getGrpRiskOffcLandlineNo1() {
		return grpRiskOffcLandlineNo1;
	}

	public void setGrpRiskOffcLandlineNo1(String grpRiskOffcLandlineNo1) {
		this.grpRiskOffcLandlineNo1 = grpRiskOffcLandlineNo1;
	}

	public String getGrpRiskOffcLandlineNo2() {
		return grpRiskOffcLandlineNo2;
	}

	public void setGrpRiskOffcLandlineNo2(String grpRiskOffcLandlineNo2) {
		this.grpRiskOffcLandlineNo2 = grpRiskOffcLandlineNo2;
	}

	public String getGrpRiskOffcMobNo() {
		return grpRiskOffcMobNo;
	}

	public void setGrpRiskOffcMobNo(String grpRiskOffcMobNo) {
		this.grpRiskOffcMobNo = grpRiskOffcMobNo;
	}

	public String getGrpRiskOffcEml() {
		return grpRiskOffcEml;
	}

	public void setGrpRiskOffcEml(String grpRiskOffcEml) {
		this.grpRiskOffcEml = grpRiskOffcEml;
	}

	public String getGrpRiskOffcGenEml() {
		return grpRiskOffcGenEml;
	}

	public void setGrpRiskOffcGenEml(String grpRiskOffcGenEml) {
		this.grpRiskOffcGenEml = grpRiskOffcGenEml;
	}

	public String getGrpRiskOffcFaxNo() {
		return grpRiskOffcFaxNo;
	}

	public void setGrpRiskOffcFaxNo(String grpRiskOffcFaxNo) {
		this.grpRiskOffcFaxNo = grpRiskOffcFaxNo;
	}

	public String getGrpRiskOffcAltContPrsnNm() {
		return grpRiskOffcAltContPrsnNm;
	}

	public void setGrpRiskOffcAltContPrsnNm(String grpRiskOffcAltContPrsnNm) {
		this.grpRiskOffcAltContPrsnNm = grpRiskOffcAltContPrsnNm;
	}

	public String getGrpRiskOffcAltContPrsnEml() {
		return grpRiskOffcAltContPrsnEml;
	}

	public void setGrpRiskOffcAltContPrsnEml(String grpRiskOffcAltContPrsnEml) {
		this.grpRiskOffcAltContPrsnEml = grpRiskOffcAltContPrsnEml;
	}

	public String getGrpRiskOffcAltContPrsnMobNo() {
		return grpRiskOffcAltContPrsnMobNo;
	}

	public void setGrpRiskOffcAltContPrsnMobNo(String grpRiskOffcAltContPrsnMobNo) {
		this.grpRiskOffcAltContPrsnMobNo = grpRiskOffcAltContPrsnMobNo;
	}

	public String getGrpCompOffcName() {
		return grpCompOffcName;
	}

	public void setGrpCompOffcName(String grpCompOffcName) {
		this.grpCompOffcName = grpCompOffcName;
	}

	public String getGrpCompOffcDesig() {
		return grpCompOffcDesig;
	}

	public void setGrpCompOffcDesig(String grpCompOffcDesig) {
		this.grpCompOffcDesig = grpCompOffcDesig;
	}

	public String getGrpCompOffcAppSinceDt() {
		return grpCompOffcAppSinceDt;
	}

	public void setGrpCompOffcAppSinceDt(String grpCompOffcAppSinceDt) {
		this.grpCompOffcAppSinceDt = grpCompOffcAppSinceDt;
	}

	public String getGrpCompOffcToIncubmtPosnDt() {
		return grpCompOffcToIncubmtPosnDt;
	}

	public void setGrpCompOffcToIncubmtPosnDt(String grpCompOffcToIncubmtPosnDt) {
		this.grpCompOffcToIncubmtPosnDt = grpCompOffcToIncubmtPosnDt;
	}

	public String getGrpCompOffcLocatedCity() {
		return grpCompOffcLocatedCity;
	}

	public void setGrpCompOffcLocatedCity(String grpCompOffcLocatedCity) {
		this.grpCompOffcLocatedCity = grpCompOffcLocatedCity;
	}

	public String getGrpCompOffcSTDCode() {
		return grpCompOffcSTDCode;
	}

	public void setGrpCompOffcSTDCode(String grpCompOffcSTDCode) {
		this.grpCompOffcSTDCode = grpCompOffcSTDCode;
	}

	public String getGrpCompOffcLandlineNo1() {
		return grpCompOffcLandlineNo1;
	}

	public void setGrpCompOffcLandlineNo1(String grpCompOffcLandlineNo1) {
		this.grpCompOffcLandlineNo1 = grpCompOffcLandlineNo1;
	}

	public String getGrpCompOffcLandlineNo2() {
		return grpCompOffcLandlineNo2;
	}

	public void setGrpCompOffcLandlineNo2(String grpCompOffcLandlineNo2) {
		this.grpCompOffcLandlineNo2 = grpCompOffcLandlineNo2;
	}

	public String getGrpCompOffcMobNo() {
		return grpCompOffcMobNo;
	}

	public void setGrpCompOffcMobNo(String grpCompOffcMobNo) {
		this.grpCompOffcMobNo = grpCompOffcMobNo;
	}

	public String getGrpCompOffcEml() {
		return grpCompOffcEml;
	}

	public void setGrpCompOffcEml(String grpCompOffcEml) {
		this.grpCompOffcEml = grpCompOffcEml;
	}

	public String getGrpCompOffcGenEml() {
		return grpCompOffcGenEml;
	}

	public void setGrpCompOffcGenEml(String grpCompOffcGenEml) {
		this.grpCompOffcGenEml = grpCompOffcGenEml;
	}

	public String getGrpCompOffcFaxNo() {
		return grpCompOffcFaxNo;
	}

	public void setGrpCompOffcFaxNo(String grpCompOffcFaxNo) {
		this.grpCompOffcFaxNo = grpCompOffcFaxNo;
	}

	public String getGrpCompOffcAltContPrsnNm() {
		return grpCompOffcAltContPrsnNm;
	}

	public void setGrpCompOffcAltContPrsnNm(String grpCompOffcAltContPrsnNm) {
		this.grpCompOffcAltContPrsnNm = grpCompOffcAltContPrsnNm;
	}

	public String getGrpCompOffcAltContPrsnEml() {
		return grpCompOffcAltContPrsnEml;
	}

	public void setGrpCompOffcAltContPrsnEml(String grpCompOffcAltContPrsnEml) {
		this.grpCompOffcAltContPrsnEml = grpCompOffcAltContPrsnEml;
	}

	public String getGrpCompOffcAltContPrsnMobNo() {
		return grpCompOffcAltContPrsnMobNo;
	}

	public void setGrpCompOffcAltContPrsnMobNo(String grpCompOffcAltContPrsnMobNo) {
		this.grpCompOffcAltContPrsnMobNo = grpCompOffcAltContPrsnMobNo;
	}

	public String getChiefITOffcName() {
		return chiefITOffcName;
	}

	public void setChiefITOffcName(String chiefITOffcName) {
		this.chiefITOffcName = chiefITOffcName;
	}

	public String getChiefITOffcDesig() {
		return chiefITOffcDesig;
	}

	public void setChiefITOffcDesig(String chiefITOffcDesig) {
		this.chiefITOffcDesig = chiefITOffcDesig;
	}

	public String getChiefITOffcAppSinceDt() {
		return chiefITOffcAppSinceDt;
	}

	public void setChiefITOffcAppSinceDt(String chiefITOffcAppSinceDt) {
		this.chiefITOffcAppSinceDt = chiefITOffcAppSinceDt;
	}

	public String getChiefITOffcToIncubmtPosnDt() {
		return chiefITOffcToIncubmtPosnDt;
	}

	public void setChiefITOffcToIncubmtPosnDt(String chiefITOffcToIncubmtPosnDt) {
		this.chiefITOffcToIncubmtPosnDt = chiefITOffcToIncubmtPosnDt;
	}

	public String getChiefITOffcLocatedCity() {
		return chiefITOffcLocatedCity;
	}

	public void setChiefITOffcLocatedCity(String chiefITOffcLocatedCity) {
		this.chiefITOffcLocatedCity = chiefITOffcLocatedCity;
	}

	public String getChiefITOffcSTDCode() {
		return chiefITOffcSTDCode;
	}

	public void setChiefITOffcSTDCode(String chiefITOffcSTDCode) {
		this.chiefITOffcSTDCode = chiefITOffcSTDCode;
	}

	public String getChiefITOffcLandlineNo1() {
		return chiefITOffcLandlineNo1;
	}

	public void setChiefITOffcLandlineNo1(String chiefITOffcLandlineNo1) {
		this.chiefITOffcLandlineNo1 = chiefITOffcLandlineNo1;
	}

	public String getChiefITOffcLandlineNo2() {
		return chiefITOffcLandlineNo2;
	}

	public void setChiefITOffcLandlineNo2(String chiefITOffcLandlineNo2) {
		this.chiefITOffcLandlineNo2 = chiefITOffcLandlineNo2;
	}

	public String getChiefITOffcMobNo() {
		return chiefITOffcMobNo;
	}

	public void setChiefITOffcMobNo(String chiefITOffcMobNo) {
		this.chiefITOffcMobNo = chiefITOffcMobNo;
	}

	public String getChiefITOffcEml() {
		return chiefITOffcEml;
	}

	public void setChiefITOffcEml(String chiefITOffcEml) {
		this.chiefITOffcEml = chiefITOffcEml;
	}

	public String getChiefITOffcGenEml() {
		return chiefITOffcGenEml;
	}

	public void setChiefITOffcGenEml(String chiefITOffcGenEml) {
		this.chiefITOffcGenEml = chiefITOffcGenEml;
	}

	public String getChiefITOffcFaxNo() {
		return chiefITOffcFaxNo;
	}

	public void setChiefITOffcFaxNo(String chiefITOffcFaxNo) {
		this.chiefITOffcFaxNo = chiefITOffcFaxNo;
	}

	public String getChiefITOffcAltContPrsnNm() {
		return chiefITOffcAltContPrsnNm;
	}

	public void setChiefITOffcAltContPrsnNm(String chiefITOffcAltContPrsnNm) {
		this.chiefITOffcAltContPrsnNm = chiefITOffcAltContPrsnNm;
	}

	public String getChiefITOffcAltContPrsnEml() {
		return chiefITOffcAltContPrsnEml;
	}

	public void setChiefITOffcAltContPrsnEml(String chiefITOffcAltContPrsnEml) {
		this.chiefITOffcAltContPrsnEml = chiefITOffcAltContPrsnEml;
	}

	public String getChiefITOffcAltContPrsnMobNo() {
		return chiefITOffcAltContPrsnMobNo;
	}

	public void setChiefITOffcAltContPrsnMobNo(String chiefITOffcAltContPrsnMobNo) {
		this.chiefITOffcAltContPrsnMobNo = chiefITOffcAltContPrsnMobNo;
	}

	public String getChiefInfoSecOffcName() {
		return chiefInfoSecOffcName;
	}

	public void setChiefInfoSecOffcName(String chiefInfoSecOffcName) {
		this.chiefInfoSecOffcName = chiefInfoSecOffcName;
	}

	public String getChiefInfoSecOffcDesig() {
		return chiefInfoSecOffcDesig;
	}

	public void setChiefInfoSecOffcDesig(String chiefInfoSecOffcDesig) {
		this.chiefInfoSecOffcDesig = chiefInfoSecOffcDesig;
	}

	public String getChiefInfoSecOffcAppSinceDt() {
		return chiefInfoSecOffcAppSinceDt;
	}

	public void setChiefInfoSecOffcAppSinceDt(String chiefInfoSecOffcAppSinceDt) {
		this.chiefInfoSecOffcAppSinceDt = chiefInfoSecOffcAppSinceDt;
	}

	public String getChiefInfoSecOffcToIncubmtPosnDt() {
		return chiefInfoSecOffcToIncubmtPosnDt;
	}

	public void setChiefInfoSecOffcToIncubmtPosnDt(String chiefInfoSecOffcToIncubmtPosnDt) {
		this.chiefInfoSecOffcToIncubmtPosnDt = chiefInfoSecOffcToIncubmtPosnDt;
	}

	public String getChiefInfoSecOffcLocatedCity() {
		return chiefInfoSecOffcLocatedCity;
	}

	public void setChiefInfoSecOffcLocatedCity(String chiefInfoSecOffcLocatedCity) {
		this.chiefInfoSecOffcLocatedCity = chiefInfoSecOffcLocatedCity;
	}

	public String getChiefInfoSecOffcSTDCode() {
		return chiefInfoSecOffcSTDCode;
	}

	public void setChiefInfoSecOffcSTDCode(String chiefInfoSecOffcSTDCode) {
		this.chiefInfoSecOffcSTDCode = chiefInfoSecOffcSTDCode;
	}

	public String getChiefInfoSecOffcLandlineNo1() {
		return chiefInfoSecOffcLandlineNo1;
	}

	public void setChiefInfoSecOffcLandlineNo1(String chiefInfoSecOffcLandlineNo1) {
		this.chiefInfoSecOffcLandlineNo1 = chiefInfoSecOffcLandlineNo1;
	}

	public String getChiefInfoSecOffcLandlineNo2() {
		return chiefInfoSecOffcLandlineNo2;
	}

	public void setChiefInfoSecOffcLandlineNo2(String chiefInfoSecOffcLandlineNo2) {
		this.chiefInfoSecOffcLandlineNo2 = chiefInfoSecOffcLandlineNo2;
	}

	public String getChiefInfoSecOffcMobNo() {
		return chiefInfoSecOffcMobNo;
	}

	public void setChiefInfoSecOffcMobNo(String chiefInfoSecOffcMobNo) {
		this.chiefInfoSecOffcMobNo = chiefInfoSecOffcMobNo;
	}

	public String getChiefInfoSecOffcEml() {
		return chiefInfoSecOffcEml;
	}

	public void setChiefInfoSecOffcEml(String chiefInfoSecOffcEml) {
		this.chiefInfoSecOffcEml = chiefInfoSecOffcEml;
	}

	public String getChiefInfoSecOffcGenEml() {
		return chiefInfoSecOffcGenEml;
	}

	public void setChiefInfoSecOffcGenEml(String chiefInfoSecOffcGenEml) {
		this.chiefInfoSecOffcGenEml = chiefInfoSecOffcGenEml;
	}

	public String getChiefInfoSecOffcFaxNo() {
		return chiefInfoSecOffcFaxNo;
	}

	public void setChiefInfoSecOffcFaxNo(String chiefInfoSecOffcFaxNo) {
		this.chiefInfoSecOffcFaxNo = chiefInfoSecOffcFaxNo;
	}

	public String getChiefInfoSecOffcAltContPrsnNm() {
		return chiefInfoSecOffcAltContPrsnNm;
	}

	public void setChiefInfoSecOffcAltContPrsnNm(String chiefInfoSecOffcAltContPrsnNm) {
		this.chiefInfoSecOffcAltContPrsnNm = chiefInfoSecOffcAltContPrsnNm;
	}

	public String getChiefInfoSecOffcAltContPrsnEml() {
		return chiefInfoSecOffcAltContPrsnEml;
	}

	public void setChiefInfoSecOffcAltContPrsnEml(String chiefInfoSecOffcAltContPrsnEml) {
		this.chiefInfoSecOffcAltContPrsnEml = chiefInfoSecOffcAltContPrsnEml;
	}

	public String getChiefInfoSecOffcAltContPrsnMobNo() {
		return chiefInfoSecOffcAltContPrsnMobNo;
	}

	public void setChiefInfoSecOffcAltContPrsnMobNo(String chiefInfoSecOffcAltContPrsnMobNo) {
		this.chiefInfoSecOffcAltContPrsnMobNo = chiefInfoSecOffcAltContPrsnMobNo;
	}

	public String getTreasuryInvheadName() {
		return treasuryInvheadName;
	}

	public void setTreasuryInvheadName(String treasuryInvheadName) {
		this.treasuryInvheadName = treasuryInvheadName;
	}

	public String getTreasuryInvheadDesig() {
		return treasuryInvheadDesig;
	}

	public void setTreasuryInvheadDesig(String treasuryInvheadDesig) {
		this.treasuryInvheadDesig = treasuryInvheadDesig;
	}

	public String getTreasuryInvheadAppSinceDt() {
		return treasuryInvheadAppSinceDt;
	}

	public void setTreasuryInvheadAppSinceDt(String treasuryInvheadAppSinceDt) {
		this.treasuryInvheadAppSinceDt = treasuryInvheadAppSinceDt;
	}

	public String getTreasuryInvheadToIncubmtPosnDt() {
		return treasuryInvheadToIncubmtPosnDt;
	}

	public void setTreasuryInvheadToIncubmtPosnDt(String treasuryInvheadToIncubmtPosnDt) {
		this.treasuryInvheadToIncubmtPosnDt = treasuryInvheadToIncubmtPosnDt;
	}

	public String getTreasuryInvheadLocatedCity() {
		return treasuryInvheadLocatedCity;
	}

	public void setTreasuryInvheadLocatedCity(String treasuryInvheadLocatedCity) {
		this.treasuryInvheadLocatedCity = treasuryInvheadLocatedCity;
	}

	public String getTreasuryInvheadSTDCode() {
		return treasuryInvheadSTDCode;
	}

	public void setTreasuryInvheadSTDCode(String treasuryInvheadSTDCode) {
		this.treasuryInvheadSTDCode = treasuryInvheadSTDCode;
	}

	public String getTreasuryInvheadLandlineNo1() {
		return treasuryInvheadLandlineNo1;
	}

	public void setTreasuryInvheadLandlineNo1(String treasuryInvheadLandlineNo1) {
		this.treasuryInvheadLandlineNo1 = treasuryInvheadLandlineNo1;
	}

	public String getTreasuryInvheadLandlineNo2() {
		return treasuryInvheadLandlineNo2;
	}

	public void setTreasuryInvheadLandlineNo2(String treasuryInvheadLandlineNo2) {
		this.treasuryInvheadLandlineNo2 = treasuryInvheadLandlineNo2;
	}

	public String getTreasuryInvheadMobNo() {
		return treasuryInvheadMobNo;
	}

	public void setTreasuryInvheadMobNo(String treasuryInvheadMobNo) {
		this.treasuryInvheadMobNo = treasuryInvheadMobNo;
	}

	public String getTreasuryInvheadOffcEml() {
		return treasuryInvheadOffcEml;
	}

	public void setTreasuryInvheadOffcEml(String treasuryInvheadOffcEml) {
		this.treasuryInvheadOffcEml = treasuryInvheadOffcEml;
	}

	public String getTreasuryInvheadGenEml() {
		return treasuryInvheadGenEml;
	}

	public void setTreasuryInvheadGenEml(String treasuryInvheadGenEml) {
		this.treasuryInvheadGenEml = treasuryInvheadGenEml;
	}

	public String getTreasuryInvheadFaxNo() {
		return treasuryInvheadFaxNo;
	}

	public void setTreasuryInvheadFaxNo(String treasuryInvheadFaxNo) {
		this.treasuryInvheadFaxNo = treasuryInvheadFaxNo;
	}

	public String getTreasuryInvheadAltContPrsnNm() {
		return treasuryInvheadAltContPrsnNm;
	}

	public void setTreasuryInvheadAltContPrsnNm(String treasuryInvheadAltContPrsnNm) {
		this.treasuryInvheadAltContPrsnNm = treasuryInvheadAltContPrsnNm;
	}

	public String getTreasuryInvheadAltContPrsnEml() {
		return treasuryInvheadAltContPrsnEml;
	}

	public void setTreasuryInvheadAltContPrsnEml(String treasuryInvheadAltContPrsnEml) {
		this.treasuryInvheadAltContPrsnEml = treasuryInvheadAltContPrsnEml;
	}

	public String getTreasuryInvheadAltContPrsnMobNo() {
		return treasuryInvheadAltContPrsnMobNo;
	}

	public void setTreasuryInvheadAltContPrsnMobNo(String treasuryInvheadAltContPrsnMobNo) {
		this.treasuryInvheadAltContPrsnMobNo = treasuryInvheadAltContPrsnMobNo;
	}

	public String getNodalOffcName() {
		return nodalOffcName;
	}

	public void setNodalOffcName(String nodalOffcName) {
		this.nodalOffcName = nodalOffcName;
	}

	public String getNodalOffcDesig() {
		return nodalOffcDesig;
	}

	public void setNodalOffcDesig(String nodalOffcDesig) {
		this.nodalOffcDesig = nodalOffcDesig;
	}

	public String getNodalOffcAppSinceDt() {
		return nodalOffcAppSinceDt;
	}

	public void setNodalOffcAppSinceDt(String nodalOffcAppSinceDt) {
		this.nodalOffcAppSinceDt = nodalOffcAppSinceDt;
	}

	public String getNodalOffcToIncubmtPosnDt() {
		return nodalOffcToIncubmtPosnDt;
	}

	public void setNodalOffcToIncubmtPosnDt(String nodalOffcToIncubmtPosnDt) {
		this.nodalOffcToIncubmtPosnDt = nodalOffcToIncubmtPosnDt;
	}

	public String getNodalOffcLocatedCity() {
		return nodalOffcLocatedCity;
	}

	public void setNodalOffcLocatedCity(String nodalOffcLocatedCity) {
		this.nodalOffcLocatedCity = nodalOffcLocatedCity;
	}

	public String getNodalOffcSTDCode() {
		return nodalOffcSTDCode;
	}

	public void setNodalOffcSTDCode(String nodalOffcSTDCode) {
		this.nodalOffcSTDCode = nodalOffcSTDCode;
	}

	public String getNodalOffcLandlineNo1() {
		return nodalOffcLandlineNo1;
	}

	public void setNodalOffcLandlineNo1(String nodalOffcLandlineNo1) {
		this.nodalOffcLandlineNo1 = nodalOffcLandlineNo1;
	}

	public String getNodalOffcLandlineNo2() {
		return nodalOffcLandlineNo2;
	}

	public void setNodalOffcLandlineNo2(String nodalOffcLandlineNo2) {
		this.nodalOffcLandlineNo2 = nodalOffcLandlineNo2;
	}

	public String getNodalOffcMobNo() {
		return nodalOffcMobNo;
	}

	public void setNodalOffcMobNo(String nodalOffcMobNo) {
		this.nodalOffcMobNo = nodalOffcMobNo;
	}

	public String getNodalOffcEml() {
		return nodalOffcEml;
	}

	public void setNodalOffcEml(String nodalOffcEml) {
		this.nodalOffcEml = nodalOffcEml;
	}

	public String getNodalOffcGenEml() {
		return nodalOffcGenEml;
	}

	public void setNodalOffcGenEml(String nodalOffcGenEml) {
		this.nodalOffcGenEml = nodalOffcGenEml;
	}

	public String getNodalOffcFaxNo() {
		return nodalOffcFaxNo;
	}

	public void setNodalOffcFaxNo(String nodalOffcFaxNo) {
		this.nodalOffcFaxNo = nodalOffcFaxNo;
	}

	public String getNodalOffcAltContPrsnNm() {
		return nodalOffcAltContPrsnNm;
	}

	public void setNodalOffcAltContPrsnNm(String nodalOffcAltContPrsnNm) {
		this.nodalOffcAltContPrsnNm = nodalOffcAltContPrsnNm;
	}

	public String getNodalOffcAltContPrsnEml() {
		return nodalOffcAltContPrsnEml;
	}

	public void setNodalOffcAltContPrsnEml(String nodalOffcAltContPrsnEml) {
		this.nodalOffcAltContPrsnEml = nodalOffcAltContPrsnEml;
	}

	public String getNodalOffcAltContPrsnMobNo() {
		return nodalOffcAltContPrsnMobNo;
	}

	public void setNodalOffcAltContPrsnMobNo(String nodalOffcAltContPrsnMobNo) {
		this.nodalOffcAltContPrsnMobNo = nodalOffcAltContPrsnMobNo;
	}

	public String getNameOfStatAuditFirm() {
		return nameOfStatAuditFirm;
	}

	public void setNameOfStatAuditFirm(String nameOfStatAuditFirm) {
		this.nameOfStatAuditFirm = nameOfStatAuditFirm;
	}

	public String getPanOfStatutoryAuditFirm() {
		return panOfStatutoryAuditFirm;
	}

	public void setPanOfStatutoryAuditFirm(String panOfStatutoryAuditFirm) {
		this.panOfStatutoryAuditFirm = panOfStatutoryAuditFirm;
	}

	public String getRegNumberOfAuditFirm() {
		return regNumberOfAuditFirm;
	}

	public void setRegNumberOfAuditFirm(String regNumberOfAuditFirm) {
		this.regNumberOfAuditFirm = regNumberOfAuditFirm;
	}

	public String getNameAuditorOrPartner() {
		return nameAuditorOrPartner;
	}

	public void setNameAuditorOrPartner(String nameAuditorOrPartner) {
		this.nameAuditorOrPartner = nameAuditorOrPartner;
	}

	public String getStatAuditDesig() {
		return statAuditDesig;
	}

	public void setStatAuditDesig(String statAuditDesig) {
		this.statAuditDesig = statAuditDesig;
	}

	public String getStatAuditMembershipNo() {
		return statAuditMembershipNo;
	}

	public void setStatAuditMembershipNo(String statAuditMembershipNo) {
		this.statAuditMembershipNo = statAuditMembershipNo;
	}

	public String getStatAuditAddr() {
		return statAuditAddr;
	}

	public void setStatAuditAddr(String statAuditAddr) {
		this.statAuditAddr = statAuditAddr;
	}

	public String getStatAuditDateOfAppointment() {
		return statAuditDateOfAppointment;
	}

	public void setStatAuditDateOfAppointment(String statAuditDateOfAppointment) {
		this.statAuditDateOfAppointment = statAuditDateOfAppointment;
	}

	public String getDateExpiryOfContract() {
		return dateExpiryOfContract;
	}

	public void setDateExpiryOfContract(String dateExpiryOfContract) {
		this.dateExpiryOfContract = dateExpiryOfContract;
	}

	public String getSatutoryAuditorPeriodOfContract() {
		return satutoryAuditorPeriodOfContract;
	}

	public void setSatutoryAuditorPeriodOfContract(String satutoryAuditorPeriodOfContract) {
		this.satutoryAuditorPeriodOfContract = satutoryAuditorPeriodOfContract;
	}

	public String getEmailOfstatAudit() {
		return emailOfstatAudit;
	}

	public void setEmailOfstatAudit(String emailOfstatAudit) {
		this.emailOfstatAudit = emailOfstatAudit;
	}

	public String getStatAuditOfficePhnNo() {
		return statAuditOfficePhnNo;
	}

	public void setStatAuditOfficePhnNo(String statAuditOfficePhnNo) {
		this.statAuditOfficePhnNo = statAuditOfficePhnNo;
	}

	public String getRdYesNo() {
		return rdYesNo;
	}

	public void setRdYesNo(String rdYesNo) {
		this.rdYesNo = rdYesNo;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getBankersDetailsTableStr() {
		return bankersDetailsTableStr;
	}

	public void setBankersDetailsTableStr(String bankersDetailsTableStr) {
		this.bankersDetailsTableStr = bankersDetailsTableStr;
	}
}
