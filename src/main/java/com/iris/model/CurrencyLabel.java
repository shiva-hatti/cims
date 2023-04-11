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

/**
 * @author Vaibhav
 * @date 12/12/2015
 * @version 2.0
 */
@Entity
@Table(name = "TBL_CURRENCY_LABEL")
public class CurrencyLabel implements Serializable {
	private static final long serialVersionUID = -6825645144011677463L;

	@Id
	//	@SequenceGenerator(name = "CURRENCY_LABEL_ID_GENERATOR", sequenceName = "TBL_CURRENCY_LABEL_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CURRENCY_LABEL_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CURRENCY_LABEL_ID")
	private Long currencyLabelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Currency_ID_FK")
	private Currency currencyIdFk;

	@Column(name = "CURRENCY_LABEL")
	private String currencyLabel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANG_ID_FK")
	private LanguageMaster langIdFk;

	/**
	 * @return the currencyLabelId
	 */
	public Long getCurrencyLabelId() {
		return currencyLabelId;
	}

	/**
	 * @param currencyLabelId the currencyLabelId to set
	 */
	public void setCurrencyLabelId(Long currencyLabelId) {
		this.currencyLabelId = currencyLabelId;
	}

	/**
	 * @return the currencyIdFk
	 */
	public Currency getCurrencyIdFk() {
		return currencyIdFk;
	}

	/**
	 * @param currencyIdFk the currencyIdFk to set
	 */
	public void setCurrencyIdFk(Currency currencyIdFk) {
		this.currencyIdFk = currencyIdFk;
	}

	/**
	 * @return the currencyLabel
	 */
	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public LanguageMaster getLangIdFk() {
		return langIdFk;
	}

	public void setLangIdFk(LanguageMaster langIdFk) {
		this.langIdFk = langIdFk;
	}

}
