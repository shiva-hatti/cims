package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.ActivityApplicableMenu;

public interface ActivityApplicableMenuRepo extends JpaRepository<ActivityApplicableMenu, Long> {

	List<ActivityApplicableMenu> findByActivityIdFkIsApplicableForEntityTrue();

	List<ActivityApplicableMenu> findByActivityIdFkIsApplicableForDeptTrue();

}
