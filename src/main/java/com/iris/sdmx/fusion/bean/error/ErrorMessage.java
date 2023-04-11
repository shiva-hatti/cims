/**
 * 
 */
package com.iris.sdmx.fusion.bean.error;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sajadhav
 *
 */
@XmlRootElement
public class ErrorMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3485575794952193336L;

	private String code;

	private String text;

	/**
	 * @return the text
	 */
	@XmlElement(name = "Text", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common")
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the code
	 */
	@XmlAttribute
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
