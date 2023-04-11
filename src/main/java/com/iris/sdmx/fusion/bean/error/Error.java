package com.iris.sdmx.fusion.bean.error;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sajadhav
 */
@XmlRootElement(name = "Error")
public class Error implements Serializable {

	private static final long serialVersionUID = 5329092771911964631L;

	private ErrorMessage errorMessage;

	/**
	 * @return the errorMessage
	 */
	@XmlElement(name = "ErrorMessage", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message")
	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

}