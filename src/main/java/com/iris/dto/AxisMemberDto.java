/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author BHAVANA
 *
 */
public class AxisMemberDto implements Serializable {

	private static final long serialVersionUID = -687160999542163000L;
	private String axis;
	private String member;

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}
}
