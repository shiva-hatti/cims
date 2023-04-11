package com.iris.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.iris.model.EntityBean;
import com.iris.model.GroupMasterTemp;

public interface GroupMasterTempRepo extends JpaRepository<GroupMasterTemp, Integer> {

	@Query("From GroupMasterTemp where groupCode like (:groupCode) order by groupId desc")
	List<GroupMasterTemp> findByGroupCodeOrderByGroupIdDesc(@Param("groupCode") String groupCode);

	GroupMasterTemp findTopByGroupNameOrderByCreatedOnDesc(@Param("groupName") String groupName);

	GroupMasterTemp findTopByGroupCodeOrderByCreatedOnDesc(@Param("groupCode") String groupCode);

	GroupMasterTemp findTopByGroupCodeAndVerificationStatusOrderByCreatedOnDesc(String groupCode, Integer verificationStatus);

	List<GroupMasterTemp> findByVerificationStatusAndEntityBeanOrderByCreatedOnDesc(Integer verificationStatus, EntityBean entityBean);

	@Query("From GroupMasterTemp where groupId in (select max(a.groupId) from GroupMasterTemp a where a.verificationStatus = :verificationStatus and a.groupCode in " + "(select distinct b.groupCode from GroupMasterTemp b where b.entityBean.entityId = :entityId) " + " group by a.groupCode) and verificationStatus = :verificationStatus order by createdOn desc")
	List<GroupMasterTemp> findLatestGroup(@Param("verificationStatus") Integer verificationStatus, @Param("entityId") Long entityId);

	@Query(value = "From GroupMasterTemp  where groupId IN (Select MAX(groupId) from GroupMasterTemp  where groupCode IN (:groupCodeSet) group by groupCode)")
	List<GroupMasterTemp> getDataByGroupCode(@Param("groupCodeSet") Set<String> groupCodeSet);

	@Query(value = "From GroupMasterTemp  where groupId IN (Select MAX(groupId) from GroupMasterTemp  where groupName IN (:groupNameSet) group by groupCode)")
	List<GroupMasterTemp> getDataByGroupName(@Param("groupNameSet") Set<String> groupNameSet);

	List<GroupMasterTemp> findByVerificationStatusOrderByCreatedOnAsc(Integer verificationStatus);

	@Query("From GroupMasterTemp where groupId in (select max(a.groupId) from GroupMasterTemp a where a.verificationStatus = :verificationStatus and a.groupCode in " + "(select distinct b.groupCode from GroupMasterTemp b)" + " group by a.groupCode) and verificationStatus = :verificationStatus order by createdOn desc")
	List<GroupMasterTemp> findLatestGroupList(@Param("verificationStatus") Integer verificationStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update GroupMasterTemp SET verificationStatus = :verificationStatus, comment = :comment, approvedOn = current_timestamp, approvedByFk.userId = :approvedByFk  " + " where groupCode in (:groupCode) and verificationStatus = 0")
	int updateVerificationStatus(@Param("verificationStatus") Integer verificationStatus, @Param("comment") String comment, @Param("approvedByFk") Long approvedByFk, @Param("groupCode") List<String> groupCode);

	@Query("From GroupMasterTemp where groupId in (select max(a.groupId) from GroupMasterTemp a where a.groupCode in (:groupCode) group by a.groupCode) " + " and (verificationStatus = 1 or verificationStatus = 2)")
	List<GroupMasterTemp> getGroupList(@Param("groupCode") List<String> groupCode);

	@Query(value = "Select MAX(approvedOn) from GroupMasterTemp")
	Date getMaxApprovedOnDate();

	@Query("From GroupMasterTemp where groupCode IN (:groupCode)")
	List<GroupMasterTemp> getGroupCode(@Param("groupCode") List<String> groupCode);
}
