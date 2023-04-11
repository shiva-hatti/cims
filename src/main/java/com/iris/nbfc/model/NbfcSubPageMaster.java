package com.iris.nbfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author pmohite
 */
@Entity
@Table(name = "TBL_NBFC_SUB_PAGES_MASTER")
public class NbfcSubPageMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NBFC_SUB_PAGE_MASTER_ID")
	private Long subPageMasterId;

	@Column(name = "SUB_PAGE_NAME")
	private String subPageName;

	@Column(name = "SUB_PAGE_NAME_BIL")
	private String subPageNameBil;

	@Column(name = "SUB_PAGE_ACTION_NAME")
	private String subPageActionName;

	/**
	 * @return the subPageMasterId
	 */
	public Long getSubPageMasterId() {
		return subPageMasterId;
	}

	/**
	 * @param subPageMasterId the subPageMasterId to set
	 */
	public void setSubPageMasterId(Long subPageMasterId) {
		this.subPageMasterId = subPageMasterId;
	}

	/**
	 * @return the subPageName
	 */
	public String getSubPageName() {
		return subPageName;
	}

	/**
	 * @param subPageName the subPageName to set
	 */
	public void setSubPageName(String subPageName) {
		this.subPageName = subPageName;
	}

	/**
	 * @return the subPageNameBil
	 */
	public String getSubPageNameBil() {
		return subPageNameBil;
	}

	/**
	 * @param subPageNameBil the subPageNameBil to set
	 */
	public void setSubPageNameBil(String subPageNameBil) {
		this.subPageNameBil = subPageNameBil;
	}

	/**
	 * @return the subPageActionName
	 */
	public String getSubPageActionName() {
		return subPageActionName;
	}

	/**
	 * @param subPageActionName the subPageActionName to set
	 */
	public void setSubPageActionName(String subPageActionName) {
		this.subPageActionName = subPageActionName;
	}

}
