/**
 * 
 */
package com.iris.sdmx.fusion.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_SDMX_FUSION_PROPERTIES")
public class FusionProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1527375109569258823L;

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "KEY")
	private String key;

	@Column(name = "VALUE")
	private String value;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
