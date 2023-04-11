package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iris.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

}
