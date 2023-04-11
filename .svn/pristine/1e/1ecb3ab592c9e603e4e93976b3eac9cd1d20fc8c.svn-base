package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ApplicationTrackingSystemBean;
import com.iris.model.AuditLog;

public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {

	@Query(value = "FROM AuditLog  where menuIdFk.menuId IN (:menuList) and userIdFk.userId= :userId and  date(logTimeStamp) between date(:refStartDate) and date(:refEndDate) order by logTimeStamp desc")
	List<AuditLog> getAuditLogDataByUserId(@Param("userId") Long userId, @Param("menuList") List<Long> menuList, @Param("refStartDate") String refStartDate, @Param("refEndDate") String refEndDate);

	@Query(value = "FROM AuditLog  where menuIdFk.menuId IN (:menuList) and userIdFk.roleType.roleTypeId= :roleTypeId and  date(logTimeStamp) between date(:refStartDate) and date(:refEndDate) order by logTimeStamp desc")
	List<AuditLog> getAuditLogDataByRoleTypeId(@Param("roleTypeId") Long roleTypeId, @Param("menuList") List<Long> menuList, @Param("refStartDate") String refStartDate, @Param("refEndDate") String refEndDate);

	@Query(value = "FROM ApplicationTrackingSystemBean where userName= :userName and  date(logedInTime) between date(:refStartDate) and date(:refEndDate) order by logedInTime desc")
	List<ApplicationTrackingSystemBean> getApplicationTrackingSystem(@Param("userName") String userName, @Param("refStartDate") String refStartDate, @Param("refEndDate") String refEndDate);

	@Query(value = "FROM ApplicationTrackingSystemBean where date(logedInTime) between date(:refStartDate) and date(:refEndDate) order by logedInTime desc")
	List<ApplicationTrackingSystemBean> getApplicationTrackingSystemWithDates(@Param("refStartDate") String refStartDate, @Param("refEndDate") String refEndDate);
}
