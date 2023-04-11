package com.iris.sdmx.dimesnsion.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.iris.sdmx.dimesnsion.entity.DimensionType;

@Repository
public interface DimensionTypeRepo extends JpaRepository<DimensionType, Long> {

	List<DimensionType> findByIsActiveTrue();
}
