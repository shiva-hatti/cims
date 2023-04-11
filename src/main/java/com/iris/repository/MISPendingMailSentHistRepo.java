package com.iris.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.MISPendingMailSentHist;

public interface MISPendingMailSentHistRepo extends JpaRepository<MISPendingMailSentHist, Long> {

	@Query("from MISPendingMailSentHist MPMSH,EntityBean eb,Return ret where " + " eb.entityId = MPMSH.entityObj.entityId and eb.entityCode IN (:entityCodeList) " + " and  ret.returnId = MPMSH.returnObj.returnId and ret.returnCode IN (:returnCodeList) and MPMSH.reportingEndDate IN(:endDateList)")
	List<MISPendingMailSentHist> getDataByEntityReturnEndDate(@Param("returnCodeList") List<String> returnCodeList, @Param("entityCodeList") List<String> entityCodeList, @Param("endDateList") List<Date> endDateList);

}
