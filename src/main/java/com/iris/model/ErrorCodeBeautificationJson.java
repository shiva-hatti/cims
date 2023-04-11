package com.iris.model;

import java.io.Serializable;
import java.util.Date;

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
 * @author psheke
 * @date 09/09/2020
 * @version 1.0
 * 
 */
@Entity
@Table(name = "TBL_ERROR_OUTPUT_BEAUTIFICATION_JSON")
public class ErrorCodeBeautificationJson implements Serializable {

	private static final long serialVersionUID = 8489885994337461484L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ERR_OUT_BEAU_JSON_ID")
	private Long errOutBeauJsonId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnObj;

	@Column(name = "BEAUTIFICATION_JSON")
	private String beauJson;

	@Column(name = "CREATED_ON")
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	public Long getErrOutBeauJsonId() {
		return errOutBeauJsonId;
	}

	public void setErrOutBeauJsonId(Long errOutBeauJsonId) {
		this.errOutBeauJsonId = errOutBeauJsonId;
	}

	public Return getReturnObj() {
		return returnObj;
	}

	public void setReturnObj(Return returnObj) {
		this.returnObj = returnObj;
	}

	public String getBeauJson() {
		return beauJson;
	}

	public void setBeauJson(String beauJson) {
		this.beauJson = beauJson;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
