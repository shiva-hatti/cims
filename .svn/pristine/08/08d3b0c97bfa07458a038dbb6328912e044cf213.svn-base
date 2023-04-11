/**
 * 
 */
package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.nbfc.model.NbfcCorRegistrationBean;

/**
 * @author Siddique
 *
 */
public interface NbfcCorRegistrationRepo extends JpaRepository<NbfcCorRegistrationBean, Long> {

	@Query(value = " FROM NbfcCorRegistrationBean where compPan =:panNumber and isActive =1 order by createdOn desc")
	List<NbfcCorRegistrationBean> fetchBorrowerNameData(@Param("panNumber") String panNumber);

	@Query(value = " FROM NbfcCorRegistrationBean where compPan =:panNumber and isActive =1 and status in('NSDL-PENDING','NSDL-APPROVED','USER-VERIFIED') order by createdOn desc")
	List<NbfcCorRegistrationBean> fetchBorrowerNameDataWithRejectedStatus(@Param("panNumber") String panNumber);

	@Query(value = " FROM NbfcCorRegistrationBean where corRegistrationId in :corRegistrationId and isActive =1 ")
	List<NbfcCorRegistrationBean> getDataByIds(@Param("corRegistrationId") Long[] corRegistrationId);

}
