package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author pradnyam
 * @date 15/04/2021
 * @version 1.0
 * 
 */

@Entity
@Table(name = "TBL_MIS_PENDING_MAIL_SENT_HIST")
@JsonInclude(Include.NON_NULL)
public class MISPendingMailSentHist implements Serializable {

	/**
	 * @author pradnyam
	 */
	private static final long serialVersionUID = -3365519663048455944L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MIS_PEND_MAIL_SENT_HIST_ID")
	private Long misPendingMailSentHistId;

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnObj;

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityObj;

	@Column(name = "REPORTING_END_DATE")
	private Date reportingEndDate;

	@Column(name = "MAIL_SENT_HIST_ID_FK_LIST")
	private String mailSentHistIdFKListJson;

	public Long getMisPendingMailSentHistId() {
		return misPendingMailSentHistId;
	}

	public void setMisPendingMailSentHistId(Long misPendingMailSentHistId) {
		this.misPendingMailSentHistId = misPendingMailSentHistId;
	}

	public Return getReturnObj() {
		return returnObj;
	}

	public void setReturnObj(Return returnObj) {
		this.returnObj = returnObj;
	}

	public Date getReportingEndDate() {
		return reportingEndDate;
	}

	public void setReportingEndDate(Date reportingEndDate) {
		this.reportingEndDate = reportingEndDate;
	}

	public String getMailSentHistIdFKListJson() {
		return mailSentHistIdFKListJson;
	}

	public void setMailSentHistIdFKListJson(String mailSentHistIdFKListJson) {
		this.mailSentHistIdFKListJson = mailSentHistIdFKListJson;
	}

	public EntityBean getEntityObj() {
		return entityObj;
	}

	public void setEntityObj(EntityBean entityObj) {
		this.entityObj = entityObj;
	}

}
