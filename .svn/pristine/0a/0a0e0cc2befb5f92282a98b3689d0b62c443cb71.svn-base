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

@Entity
@Table(name = "TBL_RETURN_DISTRICT_MAPPING")
public class ReturnDistrictMapping implements Serializable {
	private static final long serialVersionUID = 8162641139181260433L;

	@Id
	@Column(name = "RETURN_DISTRICT_MAP_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long returnDistrictMapId;

	@Column(name = "RETURN_CODE")
	private String returnCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRICT_ID_FK")
	private DistrictMaster districtIdFk;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	public Long getReturnDistrictMapId() {
		return returnDistrictMapId;
	}

	public void setReturnDistrictMapId(Long returnDistrictMapId) {
		this.returnDistrictMapId = returnDistrictMapId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public DistrictMaster getDistrictIdFk() {
		return districtIdFk;
	}

	public void setDistrictIdFk(DistrictMaster districtIdFk) {
		this.districtIdFk = districtIdFk;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
