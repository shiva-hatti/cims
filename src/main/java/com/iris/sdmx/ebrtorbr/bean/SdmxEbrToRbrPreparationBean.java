/**
 * 
 */
package com.iris.sdmx.ebrtorbr.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sajadhav
 *
 */
public class SdmxEbrToRbrPreparationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8363275137973436450L;

	private Long ebrToRbrPreparationId;

	private Long entityId;

	private Date startDate;

	private Date endDate;

	private Long returnId;

	private String mandateDatapointExpectedJson;

	private String mandateDatapointReceivedJson;

	private String optionalDatapointExpectedJson;

	private String optionalDatapointReceivedJson;

	private Date reportPreStartOn;

	private Date reportPreEndOn;

	private int isFilingDone;

	private Date createdOn;

	private Date lastUpdatedOn;

	private Integer returnPropertyValId;

	public SdmxEbrToRbrPreparationBean(Long entityId, Date startDate, Date endDate, Long returnId, String mandateDatapointExpectedJson, String mandateDatapointReceivedJson, String optionalDatapointExpectedJson, String optionalDatapointReceivedJson, Date reportPreStartOn, Date reportPreEndOn, Integer returnPropertyValId) {
		this.entityId = entityId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.returnId = returnId;
		this.mandateDatapointExpectedJson = mandateDatapointExpectedJson;
		this.mandateDatapointReceivedJson = mandateDatapointReceivedJson;
		this.optionalDatapointExpectedJson = optionalDatapointExpectedJson;
		this.optionalDatapointReceivedJson = optionalDatapointReceivedJson;
		this.reportPreStartOn = reportPreStartOn;
		this.reportPreEndOn = reportPreEndOn;
		this.returnPropertyValId = returnPropertyValId;
	}

	public Long getEbrToRbrPreparationId() {
		return ebrToRbrPreparationId;
	}

	public void setEbrToRbrPreparationId(Long ebrToRbrPreparationId) {
		this.ebrToRbrPreparationId = ebrToRbrPreparationId;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getReturnId() {
		return returnId;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public String getMandateDatapointExpectedJson() {
		return mandateDatapointExpectedJson;
	}

	public void setMandateDatapointExpectedJson(String mandateDatapointExpectedJson) {
		this.mandateDatapointExpectedJson = mandateDatapointExpectedJson;
	}

	public String getMandateDatapointReceivedJson() {
		return mandateDatapointReceivedJson;
	}

	public void setMandateDatapointReceivedJson(String mandateDatapointReceivedJson) {
		this.mandateDatapointReceivedJson = mandateDatapointReceivedJson;
	}

	public String getOptionalDatapointExpectedJson() {
		return optionalDatapointExpectedJson;
	}

	public void setOptionalDatapointExpectedJson(String optionalDatapointExpectedJson) {
		this.optionalDatapointExpectedJson = optionalDatapointExpectedJson;
	}

	public String getOptionalDatapointReceivedJson() {
		return optionalDatapointReceivedJson;
	}

	public void setOptionalDatapointReceivedJson(String optionalDatapointReceivedJson) {
		this.optionalDatapointReceivedJson = optionalDatapointReceivedJson;
	}

	public Date getReportPreStartOn() {
		return reportPreStartOn;
	}

	public void setReportPreStartOn(Date reportPreStartOn) {
		this.reportPreStartOn = reportPreStartOn;
	}

	public Date getReportPreEndOn() {
		return reportPreEndOn;
	}

	public void setReportPreEndOn(Date reportPreEndOn) {
		this.reportPreEndOn = reportPreEndOn;
	}

	public int getIsFilingDone() {
		return isFilingDone;
	}

	public void setIsFilingDone(int isFilingDone) {
		this.isFilingDone = isFilingDone;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Integer getReturnPropertyValId() {
		return returnPropertyValId;
	}

	public void setReturnPropertyValId(Integer returnPropertyValId) {
		this.returnPropertyValId = returnPropertyValId;
	}

}
