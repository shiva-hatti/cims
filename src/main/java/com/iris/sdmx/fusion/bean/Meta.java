package com.iris.sdmx.fusion.bean;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {

	private String id;
	private boolean test;
	private String schema;
	private Date prepared;
	private List<String> contentLanguages;
	private Sender sender;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public boolean getTest() {
		return test;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getSchema() {
		return schema;
	}

	public void setPrepared(Date prepared) {
		this.prepared = prepared;
	}

	public Date getPrepared() {
		return prepared;
	}

	/**
	 * @return the contentLanguages
	 */
	public List<String> getContentLanguages() {
		return contentLanguages;
	}

	/**
	 * @param contentLanguages the contentLanguages to set
	 */
	public void setContentLanguages(List<String> contentLanguages) {
		this.contentLanguages = contentLanguages;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Sender getSender() {
		return sender;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "Meta [id=" + id + ", test=" + test + ", schema=" + schema + ", prepared=" + prepared + ", contentLanguages=" + contentLanguages + ", sender=" + sender + "]";
	}

}