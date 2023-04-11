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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * This is the Email Group bean class with Hibernate mapping.
 * 
 * @author Kishor Joshi
 * @date 08/12/2015
 * 
 */
@Entity
@Table(name = "TBL_EMAIL_GROUP_CONFIGURE")
public class EmailGroupConfigure implements Serializable {

	private static final long serialVersionUID = 3427038878410810615L;

	@Id
	/*@SequenceGenerator(name = "MEMBER_ID_GENERATOR", sequenceName = "TBL_EMAIL_GROUP_CONFIGURE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MEMBER_ID_GENERATOR")*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEMBER_ID")
	private Long memberId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID_FK")
	private EmailGroup emailGroup;

	@Column(name = "NAME")
	private String name;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the memberId
	 */
	public Long getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the emailGroup
	 */
	public EmailGroup getEmailGroup() {
		return emailGroup;
	}

	/**
	 * @param emailGroup the emailGroup to set
	 */
	public void setEmailGroup(EmailGroup emailGroup) {
		this.emailGroup = emailGroup;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = Validations.trimInput(name);
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = Validations.trimInput(emailId);
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

}