package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iris.model.EmailSetting;

@Repository
public interface EmailSettingRepo extends JpaRepository<EmailSetting, Long> {

	List<EmailSetting> findAll();
}
