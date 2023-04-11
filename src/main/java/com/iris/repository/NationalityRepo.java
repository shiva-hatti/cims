package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.Nationality;

public interface NationalityRepo extends JpaRepository<Nationality, Long> {
	List<Nationality> findByIsActiveTrue();
}
