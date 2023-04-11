package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.Scale;

public interface ScaleRepo extends JpaRepository<Scale, Integer> {

	List<Scale> findAll();

	List<Scale> findByIsActiveTrue();

}