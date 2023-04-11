/**
 * 
 */
package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Siddique H Khan
 *
 */

@Entity
@Table(name = "TBL_STATUS_MASTER")
public class StatusMaster implements Serializable {

	private static final long serialVersionUID = -8455077528005452335L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "STATUS_NAME")
	private String statusName;

	@Column(name = "STATUS_CODE")
	private String statusCode;

	@Column(name = "STATUS_NAME_BIL")
	private String statusNameBil;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusNameBil() {
		return statusNameBil;
	}

	public void setStatusNameBil(String statusNameBil) {
		this.statusNameBil = statusNameBil;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
