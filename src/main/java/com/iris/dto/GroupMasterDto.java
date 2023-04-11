/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author Siddique
 *
 */
public class GroupMasterDto implements Serializable {

	private static final long serialVersionUID = 7959000170157503215L;

	private Long groupMasterId;
	private String groupCode;
	private String groupName;
	private String alternateName;
	private String remark;
	private Long mobileNumber;
	private Long groupIdFk;

	public Long getGroupMasterId() {
		return groupMasterId;
	}

	public void setGroupMasterId(Long groupMasterId) {
		this.groupMasterId = groupMasterId;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAlternateName() {
		return alternateName;
	}

	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getGroupIdFk() {
		return groupIdFk;
	}

	public void setGroupIdFk(Long groupIdFk) {
		this.groupIdFk = groupIdFk;
	}

}
