package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.UploadChannel;

public interface ChannelRepo extends JpaRepository<UploadChannel, Long> {

	@Query(value = "FROM UploadChannel  where active = '1' order by displayOrder asc")
	List<UploadChannel> fetchAllActiveChannel();
}
