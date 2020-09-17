package com.generatecsvfiles.service.OracleGTMDeniedParty.Dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.generatecsvfiles.dto.OracleGTMDeniedParties;



/**
 * @author Shashank S.
 */
public interface DataPublishDao {


	public Integer easeEntiredataCount(List<String> dplStatus, List<String> dplTypes);

	public Integer easeIncrementalDataCount(List<String> dplStatus, List<String> dplTypes,Date lastDataPusblisDate,Date currentDataPusblisDate);

	public List<OracleGTMDeniedParties> getEaseEntireData(List<String> dplStatus, List<String> dplTypes, Integer start,
			Integer end);

	public List<OracleGTMDeniedParties> getEaseIncrementalData(List<String> dplStatus, List<String> dplTypes, Integer start,
			Integer end,Date lastDataPusblisDate,Date currentDataPusblisDate);

	 public Date getDateByDateState(String dateState);
}
