package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.CurrencyConversion;

public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Long> {

	@Query("From CurrencyConversion cc where cc.currencyIdFk.currencyId=:currencyId order by cc.createdOn desc")
	List<CurrencyConversion> findByCurrencyId(@Param("currencyId") Long currencyId);
}
