/**
 * 
 */
package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author akhandagale
 *
 */

@Entity
@Table(name = "TBL_ETL_CONCEPTS_INFO")
public class ETLConceptsInfo implements Serializable {

	private static final long serialVersionUID = -1393484879450176251L;

	@Id
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnObj;

	@Column(name = "CONCEPT")
	private String concept;

	@Transient
	private String returnCode;

	@Transient
	private Long returnId;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the returnObj
	 */
	public Return getReturnObj() {
		return returnObj;
	}

	/**
	 * @param returnObj the returnObj to set
	 */
	public void setReturnObj(Return returnObj) {
		this.returnObj = returnObj;
	}

	/**
	 * @return the concept
	 */
	public String getConcept() {
		return concept;
	}

	/**
	 * @param concept the concept to set
	 */
	public void setConcept(String concept) {
		this.concept = concept;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

}
