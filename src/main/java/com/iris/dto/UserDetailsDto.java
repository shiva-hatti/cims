/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author sajadhav
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class UserDetailsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7647104622548115350L;

	private Long userId;

	private String userName;

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNo;

	private Integer roleTypeId;

	private RegulatorDto regulatorDto;

	private List<UserRoleDto> userRoleDtos;

	private List<EntityDto> entityDtos;

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * @return the roleTypeId
	 */
	public Integer getRoleTypeId() {
		return roleTypeId;
	}

	/**
	 * @param roleTypeId the roleTypeId to set
	 */
	public void setRoleTypeId(Integer roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	/**
	 * @return the regulatorDto
	 */
	public RegulatorDto getRegulatorDto() {
		return regulatorDto;
	}

	/**
	 * @param regulatorDto the regulatorDto to set
	 */
	public void setRegulatorDto(RegulatorDto regulatorDto) {
		this.regulatorDto = regulatorDto;
	}

	/**
	 * @return the userRoleDtos
	 */
	public List<UserRoleDto> getUserRoleDtos() {
		return userRoleDtos;
	}

	/**
	 * @param userRoleDtos the userRoleDtos to set
	 */
	public void setUserRoleDtos(List<UserRoleDto> userRoleDtos) {
		this.userRoleDtos = userRoleDtos;
	}

	/**
	 * @return the entityDtos
	 */
	public List<EntityDto> getEntityDtos() {
		return entityDtos;
	}

	/**
	 * @param entityDtos the entityDtos to set
	 */
	public void setEntityDtos(List<EntityDto> entityDtos) {
		this.entityDtos = entityDtos;
	}

}
