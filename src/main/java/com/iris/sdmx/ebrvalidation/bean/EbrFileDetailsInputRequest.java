/**
 * 
 */
package com.iris.sdmx.ebrvalidation.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author sajadhav
 *
 */
public class EbrFileDetailsInputRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8397145517275863590L;

	private List<Integer> filingStatusIdList;

	private Integer totalRecordCount;

	private List<String> fileTypeList;

	private Integer fillingStatusIdToBeChanged;

	/**
	 * @return the filingStatusIdList
	 */
	public List<Integer> getFilingStatusIdList() {
		return filingStatusIdList;
	}

	/**
	 * @param filingStatusIdList the filingStatusIdList to set
	 */
	public void setFilingStatusIdList(List<Integer> filingStatusIdList) {
		this.filingStatusIdList = filingStatusIdList;
	}

	/**
	 * @return the totalRecordCount
	 */
	public Integer getTotalRecordCount() {
		return totalRecordCount;
	}

	/**
	 * @param totalRecordCount the totalRecordCount to set
	 */
	public void setTotalRecordCount(Integer totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	/**
	 * @return the fileTypeList
	 */
	public List<String> getFileTypeList() {
		return fileTypeList;
	}

	/**
	 * @param fileTypeList the fileTypeList to set
	 */
	public void setFileTypeList(List<String> fileTypeList) {
		this.fileTypeList = fileTypeList;
	}

	/**
	 * @return the fillingStatusIdToBeChanged
	 */
	public Integer getFillingStatusIdToBeChanged() {
		return fillingStatusIdToBeChanged;
	}

	/**
	 * @param fillingStatusIdToBeChanged the fillingStatusIdToBeChanged to set
	 */
	public void setFillingStatusIdToBeChanged(Integer fillingStatusIdToBeChanged) {
		this.fillingStatusIdToBeChanged = fillingStatusIdToBeChanged;
	}

}
