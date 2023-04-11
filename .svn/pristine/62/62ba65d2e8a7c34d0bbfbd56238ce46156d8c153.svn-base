package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.GroupCompany;

public interface GroupCompanyRepo extends JpaRepository<GroupCompany, Integer> {

	List<GroupCompany> findByCompanyNameInIgnoreCase(List<String> companyNameList);

}
