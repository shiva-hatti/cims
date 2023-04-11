
package com.iris.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * @author bthakare
 *
 */
@Entity
@Table(name = "TBL_RETURN_PROPERTY")
public class ReturnProperty implements Serializable {

	private static final long serialVersionUID = -1958528355714822465L;

	@Id
	@Column(name = "RETURN_PROPERTY_ID")
	private Integer returnProprtyId;

	@Column(name = "RETURN_PROPERTY")
	private String returnProperty;

	@OneToMany(mappedBy = "returnProprtyIdFK")
	private List<ReturnPropertyValue> returnPropertyValList;

	/**
	 * @return the returnPropertyValList
	 */
	public List<ReturnPropertyValue> getReturnPropertyValList() {
		return returnPropertyValList;
	}

	/**
	 * @param returnPropertyValList the returnPropertyValList to set
	 */
	public void setReturnPropertyValList(List<ReturnPropertyValue> returnPropertyValList) {
		this.returnPropertyValList = returnPropertyValList;
	}

	public Integer getReturnProprtyId() {
		return returnProprtyId;
	}

	public void setReturnProprtyId(Integer returnProprtyId) {
		this.returnProprtyId = returnProprtyId;
	}

	public String getReturnProperty() {
		return returnProperty;
	}

	public void setReturnProperty(String returnProperty) {
		this.returnProperty = Validations.trimInput(returnProperty);
	}

}
