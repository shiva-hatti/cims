package com.iris.model;

import java.util.List;

/**
 * @author pradnya m
 *
 */
public class JsonEncodeInputDto {
	private List<String> allEncodedJson;
	private Long entityId;

	public List<String> getAllEncodedJson() {
		return allEncodedJson;
	}

	public void setAllEncodedJson(List<String> allEncodedJson) {
		this.allEncodedJson = allEncodedJson;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
}
