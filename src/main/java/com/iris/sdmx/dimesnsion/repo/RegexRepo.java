/**
 * 
 */
package com.iris.sdmx.dimesnsion.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.sdmx.dimesnsion.entity.Regex;

/**
 * @author sajadhav
 *
 */
@Repository
public interface RegexRepo extends JpaRepository<Regex, Long> {

	@Query("SELECT regexId FROM Regex where regex=:regex")
	Integer findRegexIdByRegex(String regex);

}
