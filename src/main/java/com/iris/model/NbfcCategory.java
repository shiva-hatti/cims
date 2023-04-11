package com.iris.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_NBFC_CATEGORY")
public class NbfcCategory implements Serializable {

	private static final long serialVersionUID = 8549613666191041143L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NBFC_CATEGORY_ID")
	private Long nbfcCategoryId;

	@Column(name = "NBFC_CATEGORY")
	private String nbfcCategory;

	public Long getNbfcCategoryId() {
		return nbfcCategoryId;
	}

	public void setNbfcCategoryId(Long nbfcCategoryId) {
		this.nbfcCategoryId = nbfcCategoryId;
	}

	public String getNbfcCategory() {
		return nbfcCategory;
	}

	public void setNbfcCategory(String nbfcCategory) {
		this.nbfcCategory = nbfcCategory;
	}

}
