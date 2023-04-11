/**
 * 
 */
package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_RETURN_FILE_FORMAT_MAP")
public class ReturnFileFormatMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_FILE_FORMAT_MAP_ID")
	private Long returnFileFormatMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnBean;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_FORMAT_ID_FK")
	private FileFormat fileFormat;

	@Column(name = "FORMULA_FILE_NAME")
	private String formulaFileName;

	@Column(name = "JSON_TO_READ_FILE")
	private String jsonToReadFile;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "META_DATA_JSON")
	private String metaDataJson;

	public String getJsonForNillStatus() {
		return jsonForNillStatus;
	}

	public void setJsonForNillStatus(String jsonForNillStatus) {
		this.jsonForNillStatus = jsonForNillStatus;
	}

	@Transient
	private String fileFormatIdString;

	@Column(name = "JSON_FOR_NILL_STATUS")
	private String jsonForNillStatus;

	public String getJsonToReadFile() {
		return jsonToReadFile;
	}

	public void setJsonToReadFile(String jsonToReadFile) {
		this.jsonToReadFile = jsonToReadFile;
	}

	public Return getReturnBean() {
		return returnBean;
	}

	public void setReturnBean(Return returnBean) {
		this.returnBean = returnBean;
	}

	public FileFormat getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getFormulaFileName() {
		return formulaFileName;
	}

	public void setFormulaFileName(String formulaFileName) {
		this.formulaFileName = formulaFileName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Long getReturnFileFormatMapId() {
		return returnFileFormatMapId;
	}

	public void setReturnFileFormatMapId(Long returnFileFormatMapId) {
		this.returnFileFormatMapId = returnFileFormatMapId;
	}

	/**
	 * @return the fileFormatIdString
	 */
	public String getFileFormatIdString() {
		return fileFormatIdString;
	}

	/**
	 * @param fileFormatIdString the fileFormatIdString to set
	 */
	public void setFileFormatIdString(String fileFormatIdString) {
		this.fileFormatIdString = fileFormatIdString;
	}

	public String getMetaDataJson() {
		return metaDataJson;
	}

	public void setMetaDataJson(String metaDataJson) {
		this.metaDataJson = metaDataJson;
	}

}
