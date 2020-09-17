package com.generatecsvfiles.service.OracleGTMDeniedParty;

import java.util.Date;
import java.util.List;

import com.generatecsvfiles.dto.OracleGTMDeniedParties;


/**
 * @author Shashank S.
 */
public interface OracleGTMDataPublishService {

	/**
	 * This function is used to initialize detailed column of add specification
	 * page
	 */

	public void generateOracleGTMData(OracleGTMExportCriteria xmlExportCriteria);

	public Boolean ftpOracleGTMData(OracleGTMExportCriteria xmlExportCriteria);

	public List<OracleGTMDeniedParties> generateOracleGTMCSV(String type, List<String> dplTypes,
			Date lastDataPusblisDate, Date currentDataPusblisDate, OracleGTMExportCriteria xmlExportCriteria);

	public void generateOracleGTMDataFromConfigFile();
}
