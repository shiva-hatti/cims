package com.iris.sdmx.dataCollection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.sdmx.dataCollection.bean.SDMXDataCollectionLimitedFieldBean;
import com.iris.sdmx.dataCollection.entity.SDMXDataCollection;

@Repository
public interface SDMXDataCollectionRepo extends JpaRepository<SDMXDataCollection, Long> {

	/**
	 * @param processCode
	 * @param categoryLable
	 * @return
	 */
	@Query("SELECT dc.dataCode , dc.dataLable, dc.categoryLable FROM SDMXDataCollection dc WHERE dc.sdmxProcessDetailEntityFk.processCode=:processCode and dc.categoryLable=:categoryLable")
	List<SDMXDataCollectionLimitedFieldBean> getSdmxDataCollectionByProcessIdNCategory(String processCode, String categoryLable);

	/**
	 * @param processCode
	 * @return
	 */
	@Query("SELECT distinct new com.iris.sdmx.dataCollection.bean.SDMXDataCollectionLimitedFieldBean(dc.dataCode , dc.dataLable, dc.categoryLable) FROM SDMXDataCollection dc WHERE dc.sdmxProcessDetailEntityFk.processCode=:processCode")
	List<SDMXDataCollectionLimitedFieldBean> getSdmxDataCollectionByProcessId(String processCode);

}
