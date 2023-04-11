package com.iris.sdmx.dataType.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.sdmx.dataType.FieldDataType;

@Repository
public interface DataTypeRepo extends JpaRepository<FieldDataType, Long> {

	@Query("FROM FieldDataType fdt")
	List<FieldDataType> getAllFielddataType();

}
