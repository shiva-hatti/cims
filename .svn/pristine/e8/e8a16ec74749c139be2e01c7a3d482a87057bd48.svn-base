package com.iris.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This is the Email Group bean class with Hibernate mapping.
 * 
 * @author Kishor Joshi
 * @date 02/12/2015
 */
@Entity
@Table(name = "TBL_EMAIL_GROUP")
public class EmailGroup implements Serializable {

	private static final long serialVersionUID = -4579123768926853129L;

	@Id
	@Column(name = "GROUP_ID")
	private Long groupId;

	@Column(name = "GROUP_NAME")
	private String groupName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@OneToMany(mappedBy = "emailGroup", fetch = FetchType.EAGER)
	private Set<EmailGroupConfigure> emailGroupSet;

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	 * @return the emailGroupSet
	 */
	public Set<EmailGroupConfigure> getEmailGroupSet() {
		return emailGroupSet;
	}

	/**
	 * @param emailGroupSet the emailGroupSet to set
	 */
	public void setEmailGroupSet(Set<EmailGroupConfigure> emailGroupSet) {
		this.emailGroupSet = emailGroupSet;
	}

}