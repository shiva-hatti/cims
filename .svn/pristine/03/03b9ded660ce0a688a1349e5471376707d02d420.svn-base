package com.iris.sdmx.dataCollection.entity;

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

import com.iris.sdmx.status.entity.SdmxProcessDetailEntity;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_DATA_COLLECTION")
public class SDMXDataCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1207358208356416185L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DATA_COLLECTION_ID")
	private Integer dataCollectionId;

	@Column(name = "DATA_CODE")
	private String dataCode;

	@Column(name = "DATA_LABLE")
	private String dataLable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SDMX_PROCESS_DETAIL_ENTITY_FK")
	private SdmxProcessDetailEntity sdmxProcessDetailEntityFk;

	@Column(name = "CATEGORY_LABLE")
	private String categoryLable;

	@Column(name = "DATA_DISCRIPTION")
	private String dataDiscription;

	/**
	 * @return the dataCollectionId
	 */
	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	/**
	 * @param dataCollectionId the dataCollectionId to set
	 */
	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	/**
	 * @return the dataCode
	 */
	public String getDataCode() {
		return dataCode;
	}

	/**
	 * @param dataCode the dataCode to set
	 */
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	/**
	 * @return the dataLable
	 */
	public String getDataLable() {
		return dataLable;
	}

	/**
	 * @param dataLable the dataLable to set
	 */
	public void setDataLable(String dataLable) {
		this.dataLable = dataLable;
	}

	/**
	 * @return the sdmxProcessDetailEntityFk
	 */
	public SdmxProcessDetailEntity getSdmxProcessDetailEntityFk() {
		return sdmxProcessDetailEntityFk;
	}

	/**
	 * @param sdmxProcessDetailEntityFk the sdmxProcessDetailEntityFk to set
	 */
	public void setSdmxProcessDetailEntityFk(SdmxProcessDetailEntity sdmxProcessDetailEntityFk) {
		this.sdmxProcessDetailEntityFk = sdmxProcessDetailEntityFk;
	}

	/**
	 * @return the categoryLable
	 */
	public String getCategoryLable() {
		return categoryLable;
	}

	/**
	 * @param categoryLable the categoryLable to set
	 */
	public void setCategoryLable(String categoryLable) {
		this.categoryLable = categoryLable;
	}

	/**
	 * @return the dataDiscription
	 */
	public String getDataDiscription() {
		return dataDiscription;
	}

	/**
	 * @param dataDiscription the dataDiscription to set
	 */
	public void setDataDiscription(String dataDiscription) {
		this.dataDiscription = dataDiscription;
	}
}