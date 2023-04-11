package com.iris.ebr.business.technical.metadata.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class TechMetadatareturnDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2989405066320805352L;

	private List<String> hiveColData;
	private List<String> techMetadataRbrColData;
	private Set<String> filterCommonRecord;
	private String returnVersionTableName;
	private String returnCode;

	public List<String> getHiveColData() {
		return hiveColData;
	}

	public void setHiveColData(List<String> hiveColData) {
		this.hiveColData = hiveColData;
	}

	public List<String> getTechMetadataRbrColData() {
		return techMetadataRbrColData;
	}

	public void setTechMetadataRbrColData(List<String> techMetadataRbrColData) {
		this.techMetadataRbrColData = techMetadataRbrColData;
	}

	public Set<String> getFilterCommonRecord() {
		return filterCommonRecord;
	}

	public void setFilterCommonRecord(Set<String> filterCommonRecord) {
		this.filterCommonRecord = filterCommonRecord;
	}

	public String getReturnVersionTableName() {
		return returnVersionTableName;
	}

	public void setReturnVersionTableName(String returnVersionTableName) {
		this.returnVersionTableName = returnVersionTableName;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

}
