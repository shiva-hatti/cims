/**
 * 
 */
package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Siddique
 *
 */
@Entity
@Table(name = "TBL_GROUP_MASTER")
public class GroupMaster implements Serializable {

	private static final long serialVersionUID = 4821886006189063410L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GROUP_MASTER_ID")
	private Long groupMasterId;

	@Column(name = "GROUP_CODE")
	private String groupCode;

	@Column(name = "GROUP_NAME")
	private String groupName;

	@Column(name = "ALTERNATE_NAME")
	private String alternateName;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "MOBILE_NUMBER")
	private Long mobileNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID_FK")
	private GroupMasterTemp groupIdFk;

	public GroupMaster() {
		// TODO Auto-generated constructor stub
	}

	public GroupMaster(String groupCode, String groupName) {
		this.groupCode = groupCode;
		this.groupName = groupName;

	}

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

	public GroupMasterTemp getGroupIdFk() {
		return groupIdFk;
	}

	public void setGroupIdFk(GroupMasterTemp groupIdFk) {
		this.groupIdFk = groupIdFk;
	}

}
