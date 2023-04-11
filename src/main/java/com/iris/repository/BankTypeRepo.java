package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.BankTypeBean;

public interface BankTypeRepo extends JpaRepository<BankTypeBean, Long> {
	@Query("FROM BankTypeBean order by bankTypeName asc")
	List<BankTypeBean> getBankTypeList();

	@Query("FROM BankTypeBean where bankTypeName = :bankTypeName")
	BankTypeBean getBankTypeByName(@Param("bankTypeName") String bankTypeName);
}
