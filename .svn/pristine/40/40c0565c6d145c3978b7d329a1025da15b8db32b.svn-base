
package com.iris.dynamicDropDown.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.iris.controller.ConceptTypedDomain;
import com.iris.util.Validations;

/**
 * @author BHAVANA
 *
 */
@Entity
@Table(name = "TBL_DROP_DOWN_TYPES")
public class DropDownType implements Serializable {
	private static final long serialVersionUID = 6532438437725520190L;
	@Id
	@Column(name = "DROP_DOWN_TYPE_ID")
	private Long dropdownTypeId;

	@Column(name = "DROP_DOWN_TYPE_CODE")
	private String dropdownTypeCode;

	@Column(name = "DROP_DOWN_TYPE_NAME")
	private String dropdownTypeName;

	@Column(name = "RETURN_CODE")
	private String returnCode;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "IS_FORM_API")
	private boolean isFormAPI;

	@OneToMany(mappedBy = "dropDownTypeIdFk", fetch = FetchType.EAGER)
	private Set<ConceptTypedDomain> conceptTypedDomainFk;

	@OneToMany(mappedBy = "dropDownTypeIdFk", fetch = FetchType.EAGER)
	private Set<DropDownValues> dropDownValuesFk;

	public Long getDropdownTypeId() {
		return dropdownTypeId;
	}

	public void setDropdownTypeId(Long dropdownTypeId) {
		this.dropdownTypeId = dropdownTypeId;
	}

	public String getDropdownTypeCode() {
		return dropdownTypeCode;
	}

	public void setDropdownTypeCode(String dropdownTypeCode) {
		this.dropdownTypeCode = Validations.trimInput(dropdownTypeCode);
	}

	public String getDropdownTypeName() {
		return dropdownTypeName;
	}

	public void setDropdownTypeName(String dropdownTypeName) {
		this.dropdownTypeName = Validations.trimInput(dropdownTypeName);
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = Validations.trimInput(returnCode);
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isFormAPI() {
		return isFormAPI;
	}

	public void setFormAPI(boolean isFormAPI) {
		this.isFormAPI = isFormAPI;
	}

	/**
	 * @return the conceptTypedDomainFk
	 */
	public Set<ConceptTypedDomain> getConceptTypedDomainFk() {
		return conceptTypedDomainFk;
	}

	/**
	 * @param conceptTypedDomainFk the conceptTypedDomainFk to set
	 */
	public void setConceptTypedDomainFk(Set<ConceptTypedDomain> conceptTypedDomainFk) {
		this.conceptTypedDomainFk = conceptTypedDomainFk;
	}

	/**
	 * @return the dropDownValuesFk
	 */
	public Set<DropDownValues> getDropDownValuesFk() {
		return dropDownValuesFk;
	}

	/**
	 * @param dropDownValuesFk the dropDownValuesFk to set
	 */
	public void setDropDownValuesFk(Set<DropDownValues> dropDownValuesFk) {
		this.dropDownValuesFk = dropDownValuesFk;
	}

	//	/**
	//	 * @return the conceptTypedDomainFk
	//	 */
	//	public ConceptTypedDomain getConceptTypedDomainFk() {
	//		return conceptTypedDomainFk;
	//	}
	//
	//
	//	/**
	//	 * @param conceptTypedDomainFk the conceptTypedDomainFk to set
	//	 */
	//	public void setConceptTypedDomainFk(ConceptTypedDomain conceptTypedDomainFk) {
	//		this.conceptTypedDomainFk = conceptTypedDomainFk;
	//	}
	//
	//
	//	/**
	//	 * @return the dropDownValuesFk
	//	 */
	//	public DropDownValues getDropDownValuesFk() {
	//		return dropDownValuesFk;
	//	}
	//
	//
	//	/**
	//	 * @param dropDownValuesFk the dropDownValuesFk to set
	//	 */
	//	public void setDropDownValuesFk(DropDownValues dropDownValuesFk) {
	//		this.dropDownValuesFk = dropDownValuesFk;
	//	}
	//	
}
