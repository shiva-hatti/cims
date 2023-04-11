/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author sajadhav
 *
 */
public class UserRolePlatformDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187790007170685960L;

	private Long userRoleId;

	private String userRoleName;

	private Long platformId;

	private String platformName;

	public UserRolePlatformDto(Long userRoleId, String userRoleName, Long platformId, String platformName) {
		this.userRoleId = userRoleId;
		this.userRoleName = userRoleName;
		this.platformId = platformId;
		this.platformName = platformName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return Boolean.FALSE;
		} else {
			if (this.getClass() != obj.getClass()) {
				return Boolean.FALSE;
			} else {
				UserRolePlatformDto roleDto = (UserRolePlatformDto) obj;
				return userRoleId.equals((roleDto.getUserRoleId()));
			}
		}
	}

	@Override
	public int hashCode() {
		return userRoleId.hashCode();
	}

	/**
	 * @return the userRoleId
	 */
	public Long getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param userRoleId the userRoleId to set
	 */
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * @return the userRoleName
	 */
	public String getUserRoleName() {
		return userRoleName;
	}

	/**
	 * @param userRoleName the userRoleName to set
	 */
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	/**
	 * @return the platformId
	 */
	public Long getPlatformId() {
		return platformId;
	}

	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	/**
	 * @return the platformName
	 */
	public String getPlatformName() {
		return platformName;
	}

	/**
	 * @param platformName the platformName to set
	 */
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	@Override
	public String toString() {
		return "UserRolePlatformDto [userRoleId=" + userRoleId + ", userRoleName=" + userRoleName + ", platformId=" + platformId + ", platformName=" + platformName + "]";
	}

}
