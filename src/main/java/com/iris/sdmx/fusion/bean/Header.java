/**
 * 
 */
package com.iris.sdmx.fusion.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sajadhav
 *
 */
@XmlRootElement
public class Header implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7696413651448131905L;

	private String id;

	private String test;

	private String prepared;

	private String sender;

	private String receiver;

	/**
	 * @return the id
	 */
	//	@XmlElement(required = true, namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/registry")
	@XmlElement(name = "ID")
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the test
	 */
	@XmlElement(name = "Test")
	public String getTest() {
		return test;
	}

	/**
	 * @param test the test to set
	 */
	public void setTest(String test) {
		this.test = test;
	}

	/**
	 * @return the prepared
	 */
	@XmlElement(name = "Prepared")
	public String getPrepared() {
		return prepared;
	}

	/**
	 * @param prepared the prepared to set
	 */
	public void setPrepared(String prepared) {
		this.prepared = prepared;
	}

	/**
	 * @return the sender
	 */
	@XmlElement(name = "Sender")
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the receiver
	 */
	@XmlElement(name = "Receiver")
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
