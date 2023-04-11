package com.iris.controller;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.Validations;

@Entity
@Table(name = "TBL_DASHBOARD_CHART_SETTING")
public class DashboardChartSettingBean implements Serializable {

	private static final long serialVersionUID = -544693022856512975L;

	@Id
	@Column(name = "DASHBOARD_CHART_ID")
	private Integer dashboardChartId;

	@Column(name = "DASHBOARD_CHART_ITEMS")
	private String dashboardItems;

	@Column(name = "DASHBOARD_CHART_ITEMS_ORDER")
	private Integer dashboardChartItemsOrder;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "REPORT_TYPE")
	private boolean reportType;

	public Integer getDashboardChartId() {
		return dashboardChartId;
	}

	public void setDashboardChartId(Integer dashboardChartId) {
		this.dashboardChartId = dashboardChartId;
	}

	public String getDashboardItems() {
		return dashboardItems;
	}

	public void setDashboardItems(String dashboardItems) {
		this.dashboardItems = Validations.trimInput(dashboardItems);
	}

	public Integer getDashboardChartItemsOrder() {
		return dashboardChartItemsOrder;
	}

	public void setDashboardChartItemsOrder(Integer dashboardChartItemsOrder) {
		this.dashboardChartItemsOrder = dashboardChartItemsOrder;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
