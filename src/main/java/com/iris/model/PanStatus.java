/**
 * 
 */
package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Siddique
 *
 */

@Entity
@Table(name = "TBL_PAN_STATUS")
@JsonInclude(Include.NON_NULL)
public class PanStatus implements Serializable {

	private static final long serialVersionUID = -8487656433019723738L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PAN_STATUS_ID")
	private Long panStatusId;

	@Column(name = "STATUS")
	private String status;

	/**
	 * @return the panStatusId
	 */
	public Long getPanStatusId() {
		return panStatusId;
	}

	/**
	 * @param panStatusId the panStatusId to set
	 */
	public void setPanStatusId(Long panStatusId) {
		this.panStatusId = panStatusId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
