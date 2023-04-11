package com.iris.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_RETURN_SECTION_MAP")
public class ReturnSectionMap implements Serializable {

	private static final long serialVersionUID = -6374846772336929625L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_SECTION_MAP_ID")
	private Long returnSectionMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@OneToMany(mappedBy = "returnSectionMapIdFk")
	private Set<ReturnSectionMapLabel> retSecLabelSet;

	@Column(name = "ORDER_NO")
	private Integer orderNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HEADER_ID_FK")
	private DynamicHeader headerIdFk;

	/**
	 * @return the returnSectionMapId
	 */
	public Long getReturnSectionMapId() {
		return returnSectionMapId;
	}

	public DynamicHeader getHeaderIdFk() {
		return headerIdFk;
	}

	public void setHeaderIdFk(DynamicHeader headerIdFk) {
		this.headerIdFk = headerIdFk;
	}

	/**
	 * @param returnSectionMapId the returnSectionMapId to set
	 */
	public void setReturnSectionMapId(Long returnSectionMapId) {
		this.returnSectionMapId = returnSectionMapId;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the retSecLabelSet
	 */
	public Set<ReturnSectionMapLabel> getRetSecLabelSet() {
		return retSecLabelSet;
	}

	/**
	 * @param retSecLabelSet the retSecLabelSet to set
	 */
	public void setRetSecLabelSet(Set<ReturnSectionMapLabel> retSecLabelSet) {
		this.retSecLabelSet = retSecLabelSet;
	}

	/**
	 * @return the orderNo
	 */
	public Integer getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the returnIdFk
	 */
	public Return getReturnIdFk() {
		return returnIdFk;
	}

	/**
	 * @param returnIdFk the returnIdFk to set
	 */
	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

}
