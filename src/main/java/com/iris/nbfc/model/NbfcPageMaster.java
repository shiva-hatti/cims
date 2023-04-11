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
@Table(name = "TBL_NBFC_PAGE_MASTER")
public class NbfcPageMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NBFC_PAGE_MASTER_ID")
	private Long pageMasterId;

	@Column(name = "PAGE_NAME")
	private String pageName;

	@Column(name = "PAGE_NAME_BIL")
	private String pageNameBil;

	@Column(name = "PAGE_ACTION_NAME")
	private String pageActionName;

	/**
	 * @return the pageMasterId
	 */
	public Long getPageMasterId() {
		return pageMasterId;
	}

	/**
	 * @param pageMasterId the pageMasterId to set
	 */
	public void setPageMasterId(Long pageMasterId) {
		this.pageMasterId = pageMasterId;
	}

	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * @return the pageNameBil
	 */
	public String getPageNameBil() {
		return pageNameBil;
	}

	/**
	 * @param pageNameBil the pageNameBil to set
	 */
	public void setPageNameBil(String pageNameBil) {
		this.pageNameBil = pageNameBil;
	}

	/**
	 * @return the pageActionName
	 */
	public String getPageActionName() {
		return pageActionName;
	}

	/**
	 * @param pageActionName the pageActionName to set
	 */
	public void setPageActionName(String pageActionName) {
		this.pageActionName = pageActionName;
	}

}
