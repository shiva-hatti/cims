package com.iris.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.iris.model.PurposeMasterBean;

/**
 * @author bthakare
 *
 */
public interface PurposeMasterRepo extends JpaRepository<PurposeMasterBean, Long> {
	List<PurposeMasterBean> findByIsActiveTrue();

}
