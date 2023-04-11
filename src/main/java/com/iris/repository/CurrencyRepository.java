package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

	List<Currency> findByCurrencyISOCode(@Param("currencyISOCode") String currencyISOCode);

	Currency findByIsDefault(@Param("isDefault") Boolean isDefault);

	List<Currency> findByIsActiveTrue();

	@Query("FROM Currency where UPPER(currencyShortName) in(:shortName)")
	List<Currency> findCurrencyListByShortName(@Param("shortName") List<String> shortName);

}