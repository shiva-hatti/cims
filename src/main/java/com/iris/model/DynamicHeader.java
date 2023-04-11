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
@Table(name = "TBL_DYNAMIC_HEADER")
public class DynamicHeader implements Serializable {

	private static final long serialVersionUID = 2309477368686164182L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HEADER_ID")
	private Integer headerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@Column(name = "HEADER_DEFAULT_NAME")
	private String headerDefaultName;

	@Column(name = "HEADER_ORDER")
	private Float headerOrder;

	@OneToMany(mappedBy = "headerIdFk")
	private Set<DynamicHeaderLabel> dynamicHeaderLabel;

	@Column(name = "IS_HIDDEN")
	private Boolean isHidden;

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public Set<DynamicHeaderLabel> getDynamicHeaderLabel() {
		return dynamicHeaderLabel;
	}

	public void setDynamicHeaderLabel(Set<DynamicHeaderLabel> dynamicHeaderLabel) {
		this.dynamicHeaderLabel = dynamicHeaderLabel;
	}

	public Integer getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	public Return getReturnIdFk() {
		return returnIdFk;
	}

	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

	public String getHeaderDefaultName() {
		return headerDefaultName;
	}

	public void setHeaderDefaultName(String headerDefaultName) {
		this.headerDefaultName = headerDefaultName;
	}

	public Float getHeaderOrder() {
		return headerOrder;
	}

	public void setHeaderOrder(Float headerOrder) {
		this.headerOrder = headerOrder;
	}

}
