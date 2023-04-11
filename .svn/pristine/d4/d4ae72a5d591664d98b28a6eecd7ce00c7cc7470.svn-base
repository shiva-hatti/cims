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

import com.iris.util.Validations;

/**
 * @author psawant
 * @version 1.0
 * @date 04/10/2017
 */
@Entity
@Table(name = "TBL_RETURN_SECTION_MAP_LABEL")
public class ReturnSectionMapLabel implements Serializable {

	private static final long serialVersionUID = -6572122556984631263L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_SECTION_MAP_LABEL_ID")
	private Integer returnSectionMapLabelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_SECTION_MAP_ID_FK")
	private ReturnSectionMap returnSectionMapIdFk;

	@Column(name = "SECTION_HEADING_LABEL")
	private String sectionHeadingLabel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANG_ID_FK")
	private LanguageMaster langIdFk;

	/**
	 * @return the returnSectionMapLabelId
	 */
	public Integer getReturnSectionMapLabelId() {
		return returnSectionMapLabelId;
	}

	/**
	 * @param returnSectionMapLabelId the returnSectionMapLabelId to set
	 */
	public void setReturnSectionMapLabelId(Integer returnSectionMapLabelId) {
		this.returnSectionMapLabelId = returnSectionMapLabelId;
	}

	/**
	 * @return the returnSectionMapIdFk
	 */
	public ReturnSectionMap getReturnSectionMapIdFk() {
		return returnSectionMapIdFk;
	}

	/**
	 * @param returnSectionMapIdFk the returnSectionMapIdFk to set
	 */
	public void setReturnSectionMapIdFk(ReturnSectionMap returnSectionMapIdFk) {
		this.returnSectionMapIdFk = returnSectionMapIdFk;
	}

	/**
	 * @return the sectionHeadingLabel
	 */
	public String getSectionHeadingLabel() {
		return sectionHeadingLabel;
	}

	/**
	 * @param sectionHeadingLabel the sectionHeadingLabel to set
	 */
	public void setSectionHeadingLabel(String sectionHeadingLabel) {
		this.sectionHeadingLabel = Validations.trimInput(sectionHeadingLabel);
	}

	/**
	 * @return the langIdFk
	 */
	public LanguageMaster getLangIdFk() {
		return langIdFk;
	}

	/**
	 * @param langIdFk the langIdFk to set
	 */
	public void setLangIdFk(LanguageMaster langIdFk) {
		this.langIdFk = langIdFk;
	}

}