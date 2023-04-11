package com.iris.service;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.dto.ApplicableCheckDto;

@Service
public class ApplicablityCheckServeice {

	@Autowired
	private EntityManager em;

	public ApplicableCheckDto checkApplicableCuOrCRs(ApplicableCheckDto input) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select COUNT(*) as count, trs.VALIDATION_TYPE as valType  from TBL_RETURN_SET AS trs ").append(" left outer join TBL_RETURN tr on tr.RETURN_ID = trs.RETURN_ID_FK and  ").append(" tr.IS_ACTIVE=true and trs.IS_ACTIVE=true ").append(" where tr.RETURN_CODE =:return").append(" GROUP BY trs.VALIDATION_TYPE ");
		List<Tuple> result = em.createNativeQuery(sb.toString(), Tuple.class).setParameter("return", input.getReturnCode()).getResultList();
		for (Tuple item : result) {
			if (StringUtils.equals((String) item.get("valType"), "CU") && (((BigInteger) item.get("count")).intValue() > 0)) {
				input.setCustomApplicable(true);
			} else if (StringUtils.equals((String) item.get("valType"), "CR") && (((BigInteger) item.get("count")).intValue() > 0)) {
				input.setCrossApplicable(true);
			}
		}
		return input;
	}

}
