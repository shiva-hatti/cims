/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.iris.model.FileDetails;

/**
 * @author sajadhav
 *
 */
public class FileDetailLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6149524129431714942L;

	private List<FileDetails> fileDetailsList;

	public List<FileDetails> getFileDetailsList() {
		return fileDetailsList;
	}

	public void setFileDetailsList(List<FileDetails> fileDetailsList) {
		this.fileDetailsList = fileDetailsList;
	}

}
