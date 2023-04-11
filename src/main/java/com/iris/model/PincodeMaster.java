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
 * @author :Pradnya Mhatre
 *
 */

@Entity
@Table(name = "TBL_PINCODE_MASTER")
public class PincodeMaster implements Serializable {

	private static final long serialVersionUID = 4560824444349163622L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "PINCODE")
	private Integer pincode;

	@Column(name = "IS_ACTIVE")
	private String isActive;

	//	@ManyToOne
	//	//@JoinColumn(name = "CITY_ID_FK")
	//	//@JoinColumn(name = "CITY_ID_FK")
	//	private Long cityIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CITY_ID_FK")
	private CityMaster cityIdFk;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public CityMaster getCityIdFk() {
		return cityIdFk;
	}

	public void setCityIdFk(CityMaster cityIdFk) {
		this.cityIdFk = cityIdFk;
	}

}
