package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.XbrlWebFormSessionChk;

public interface XbrlWebFormSessionChkRepo extends JpaRepository<XbrlWebFormSessionChk, Long> {

	XbrlWebFormSessionChk findByUserIdFk(Long userName);
}
