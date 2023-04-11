package com.iris.nbfc.model;

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
 * @author pmohite
 */
@Entity
@Table(name = "TBL_NBFC_NOTE_MSG")
public class NbfcNoteMessages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NBFC_NOTE_MSG_ID")
	private int noteMsgId;

	@Column(name = "NOTE_MESSAGE")
	private String noteMsg;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "NOTE_MESSAGE_BIL")
	private String noteMsgBil;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NBFC_PAGE_MASTER_ID_FK")
	private NbfcPageMaster nbfcPageMaster;

	/**
	 * @return the noteMsgId
	 */
	public int getNoteMsgId() {
		return noteMsgId;
	}

	/**
	 * @param noteMsgId the noteMsgId to set
	 */
	public void setNoteMsgId(int noteMsgId) {
		this.noteMsgId = noteMsgId;
	}

	/**
	 * @return the noteMsg
	 */
	public String getNoteMsg() {
		return noteMsg;
	}

	/**
	 * @param noteMsg the noteMsg to set
	 */
	public void setNoteMsg(String noteMsg) {
		this.noteMsg = noteMsg;
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
	 * @return the noteMsgBil
	 */
	public String getNoteMsgBil() {
		return noteMsgBil;
	}

	/**
	 * @param noteMsgBil the noteMsgBil to set
	 */
	public void setNoteMsgBil(String noteMsgBil) {
		this.noteMsgBil = noteMsgBil;
	}

	/**
	 * @return the nbfcPageMaster
	 */
	public NbfcPageMaster getNbfcPageMaster() {
		return nbfcPageMaster;
	}

	/**
	 * @param nbfcPageMaster the nbfcPageMaster to set
	 */
	public void setNbfcPageMaster(NbfcPageMaster nbfcPageMaster) {
		this.nbfcPageMaster = nbfcPageMaster;
	}

}
