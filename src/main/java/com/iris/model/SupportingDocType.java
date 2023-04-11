package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_SUPPORTING_DOC_TYPE")
public class SupportingDocType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4864871534541920014L;

	@Id
	@Column(name = "DOC_TYPE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long docTypeId;

	@Column(name = "DOC_TYPE")
	private String docType;

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

}
