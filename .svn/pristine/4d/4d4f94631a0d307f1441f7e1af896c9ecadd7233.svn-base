package com.iris.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Transient;

import com.iris.util.Validations;

/**
 * @author psawant
 * @version 1.0
 * @date 26/09/2016
 */
@Entity
@Table(name = "TBL_GENERAL_NOTIFICATION")
public class GeneralNotification implements Serializable {

	private static final long serialVersionUID = 3918777271346836036L;

	@Id
	//   	@SequenceGenerator(name = "NOTIFICATION_ID_GENERATOR", sequenceName = "TBL_GENERAL_NOTIFICATION_SEQ", allocationSize = 1)
	//   	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NOTIFICATION_ID")
	private long notificationId;

	@Column(name = "NOTIFICATION_CONTENTS")
	private String notificationContents;

	@Column(name = "FILE1")
	private String file1;

	@Column(name = "FILE2")
	private String file2;

	@Column(name = "ENTITY_ID_LIST")
	private String entityIDList;

	@Column(name = "AUDITOR_ID_LIST")
	private String auditorIdList;

	@Column(name = "USER_ROLE_ID_LIST")
	private String userRoleIdList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_UPDATED_BY")
	private UserMaster lastUpdatedBy;

	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	@Column(name = "SUBJECT")
	private String subject;

	/*	@Transient
		private File file1File;*/

	@Transient
	private String file1FileName;

	/*	@Transient
		private File file2File;*/

	@Transient
	private String file2FileName;

	@Transient
	private String subCatId;

	@Transient
	private String catIds;

	@Transient
	private String entIds;

	@Transient
	private String updatDateStr;

	@Transient
	private String lastUpdatName;

	/**
	 * @return the notificationId
	 */
	public long getNotificationId() {
		return notificationId;
	}

	/**
	 * @param notificationId the notificationId to set
	 */
	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the notificationContents
	 */
	public String getNotificationContents() {
		return notificationContents;
	}

	/**
	 * @param notificationContents the notificationContents to set
	 */
	public void setNotificationContents(String notificationContents) {
		this.notificationContents = Validations.trimInput(notificationContents);
	}

	/**
	 * @return the entityIDList
	 */
	public String getEntityIDList() {
		return entityIDList;
	}

	/**
	 * @param entityIDList the entityIDList to set
	 */
	public void setEntityIDList(String entityIDList) {
		this.entityIDList = Validations.trimInput(entityIDList);
	}

	/**
	 * @return the auditorIdList
	 */
	public String getAuditorIdList() {
		return auditorIdList;
	}

	/**
	 * @param auditorIdList the auditorIdList to set
	 */
	public void setAuditorIdList(String auditorIdList) {
		this.auditorIdList = Validations.trimInput(auditorIdList);
	}

	/**
	 * @return the userRoleIdList
	 */
	public String getUserRoleIdList() {
		return userRoleIdList;
	}

	/**
	 * @param userRoleIdList the userRoleIdList to set
	 */
	public void setUserRoleIdList(String userRoleIdList) {
		this.userRoleIdList = Validations.trimInput(userRoleIdList);
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public UserMaster getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(UserMaster lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @return the lastUpdatedOn
	 */
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * @param lastUpdatedOn the lastUpdatedOn to set
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * @return the file1FileName
	 */
	public String getFile1FileName() {
		return file1FileName;
	}

	/**
	 * @param file1FileName the file1FileName to set
	 */
	public void setFile1FileName(String file1FileName) {
		this.file1FileName = Validations.trimInput(file1FileName);
	}

	/**
	 * @return the file2FileName
	 */
	public String getFile2FileName() {
		return file2FileName;
	}

	/**
	 * @param file2FileName the file2FileName to set
	 */
	public void setFile2FileName(String file2FileName) {
		this.file2FileName = Validations.trimInput(file2FileName);
	}

	/**
	 * @return the file1
	 */
	public String getFile1() {
		return file1;
	}

	/**
	 * @param file1 the file1 to set
	 */
	public void setFile1(String file1) {
		this.file1 = Validations.trimInput(file1);
	}

	/**
	 * @return the file2
	 */
	public String getFile2() {
		return file2;
	}

	/**
	 * @param file2 the file2 to set
	 */
	public void setFile2(String file2) {
		this.file2 = Validations.trimInput(file2);
	}

	/**
	 * @return the subCatId
	 */
	public String getSubCatId() {
		return subCatId;
	}

	/**
	 * @param subCatId the subCatId to set
	 */
	public void setSubCatId(String subCatId) {
		this.subCatId = Validations.trimInput(subCatId);
	}

	/**
	 * @return the catIds
	 */
	public String getCatIds() {
		return catIds;
	}

	/**
	 * @param catIds the catIds to set
	 */
	public void setCatIds(String catIds) {
		this.catIds = Validations.trimInput(catIds);
	}

	/**
	 * @return the entIds
	 */
	public String getEntIds() {
		return entIds;
	}

	/**
	 * @param entIds the entIds to set
	 */
	public void setEntIds(String entIds) {
		this.entIds = Validations.trimInput(entIds);
	}

	/**
	 * @return the updatDateStr
	 */
	public String getUpdatDateStr() {
		return updatDateStr;
	}

	/**
	 * @param updatDateStr the updatDateStr to set
	 */
	public void setUpdatDateStr(String updatDateStr) {
		this.updatDateStr = Validations.trimInput(updatDateStr);
	}

	/**
	 * @return the lastUpdatName
	 */
	public String getLastUpdatName() {
		return lastUpdatName;
	}

	/**
	 * @param lastUpdatName the lastUpdatName to set
	 */
	public void setLastUpdatName(String lastUpdatName) {
		this.lastUpdatName = Validations.trimInput(lastUpdatName);
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = Validations.trimInput(subject);
	}

}