package com.generatecsvfiles.service.OracleGTMDeniedParty.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.generatecsvfiles.dto.OracleGTMDeniedParties;
import com.generatecsvfiles.utility.JDBCUtil;


/**
 * @author Shashank S.
 */
public class DataPublishDaoImpl implements DataPublishDao {

	Connection con =null;
	PreparedStatement ps =null;
	ResultSet rs = null;
	
	SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat dateParserMMDD = new SimpleDateFormat("MM/dd/yyyy");
	
	private static final String GET_DATE_BY_DATE_STATE = "SELECT DATE FROM DATE_CONTROL WHERE DATE_STATE= ?";

	
	private static final String GET_ENTIRE_DPL_DATA = "select * from (SELECT ROW_NUMBER() OVER ( ORDER BY DPL_ID ) AS RowNum,"
			+ " DPL_ID,DPL_STATUS,DPL_NAME,DPL_ADDRESS_1" + ",DPL_ADDRESS_2,DPL_ADDRESS_3,DPL_STATE,"
			+ "DPL_COUNTRY,DPL_COUNTRY_OF_ORIGIN,DPL_SEE_ALSO,DPL_PRIVILEGES,DPL_EFFECTIVE_DATE,DPL_EXPIRY_DATE,"
			+ "DPL_FRC_DATE,DPL_FRC,"
			+ "DPL_CATEGORY,DPL_AGENCIES,DPL_TYPE,CREATED_ON ,CREATED_BY,MODIFIED_ON,MODIFIED_BY,DPL_ZIP,DPL_ADDL_INFO,DPL_URL,"
			+ "DPL_ENTITY_TYPE,DPL_IMAGE,APPROVED_DATE, DPL_DUNS, DPL_CAGE_CODE, DPL_COUNTRY_NAME  FROM DENIED_PARTIES_LIST WHERE DPL_TYPE IN (";

	
	private static final String EASE_INCREMENTAL_DATA = "select * from (SELECT ROW_NUMBER() OVER ( ORDER BY DPL_ID ) AS RowNum,"
			+ " DPL_ID,DPL_STATUS,DPL_NAME,DPL_ADDRESS_1" + ",DPL_ADDRESS_2,DPL_ADDRESS_3,DPL_STATE,"
			+ "DPL_COUNTRY,DPL_COUNTRY_OF_ORIGIN,DPL_SEE_ALSO,DPL_PRIVILEGES,DPL_EFFECTIVE_DATE,DPL_EXPIRY_DATE,"
			+ "DPL_FRC_DATE,DPL_FRC,"
			+ "DPL_CATEGORY,DPL_AGENCIES,DPL_TYPE,CREATED_ON ,CREATED_BY,MODIFIED_ON,MODIFIED_BY,DPL_ZIP,DPL_ADDL_INFO,DPL_URL,"
			+ "DPL_ENTITY_TYPE,DPL_IMAGE,APPROVED_DATE, DPL_DUNS, DPL_CAGE_CODE,DPL_COUNTRY_NAME  FROM DENIED_PARTIES_LIST WHERE DPL_TYPE IN (:dpltypes) 	and DPL_STATUS IN (:dplStatus) "
			+ "AND APPROVED_DATE BETWEEN :startDate AND :endDate "
			+ " ) as rowResult where RowNum >= :start and RowNum <= :end ";


	private static final String COUNT_DENIED_PARTY_LIST_BY_DPLTYPES_STATUS = "select count(DPL_ID) FROM DENIED_PARTIES_LIST WHERE DPL_TYPE IN (";

	private static final String COUNT_INC_DENIED_PARTY_LIST_BY_DPLTYPES_STATUS = "select count(DPL_ID)  FROM DENIED_PARTIES_LIST "
			+ " WHERE DPL_TYPE IN (:dpltypes) and DPL_STATUS IN (:dplStatus) "
			+ " AND APPROVED_DATE BETWEEN :startDate AND :endDate";

	public static final String query = " SELECT " + " DPL_ID,DPL_STATUS, "
			+ " CONVERT(VARCHAR(10),DENIED_PARTIES_LIST.MODIFIED_ON,101) MODIFIED_ON,DPL_NAME, "
			+ " DPL_ADDRESS_1,DPL_ADDRESS_2,DPL_COUNTRY COUNTRY_ISO_CODE, " + " DPL_COUNTRY_OF_ORIGIN COO_ISO_CODE, "
			+ " CONVERT(VARCHAR(10),DPL_EFFECTIVE_DATE,101) EFFECTIVE_DATE,DPL_EXPIRY_DATE, "
			+ " CONVERT(VARCHAR(10),DPL_FRC_DATE,101) FRC_DATE,DPL_FRC,DPL_AGENCIES, "
			+ " DPL_CATEGORY,DPL_TYPE,DPL_SEE_ALSO,DPL_PRIVILEGES " + " FROM " + " DENIED_PARTIES_LIST " + " WHERE "
			+ " DENIED_PARTIES_LIST.DPL_STATUS IN ('A','D') " + " AND "
			+ " DENIED_PARTIES_LIST.DPL_TYPE NOT IN ('GSA','FRB','LEA','COS','DOD','DEL','NCC','ABL"
			+ "','TREAS','WBD','DOT','USMS','RCMP','IPL','DOD','HHS','FDA','ATK','ATK2','FLS','GEC','HDS',"
			+ "'MTI','OCR','SWISS','XOM','DOJ') " + " AND "
			+ " CONVERT(VARCHAR(10),DENIED_PARTIES_LIST.MODIFIED_ON,101) > DATEADD(DAY,-3,CONVERT(VARCHAR(10),SYSDATETIME(),101))";

	
	@Override
	public List<OracleGTMDeniedParties> getDPLEntireData(List<String> dplStatus, List<String> dplTypes, Integer start,
			Integer end) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dpltypes", dplTypes);
		parameters.put("dplStatus", dplStatus);
		parameters.put("start", start);
		parameters.put("end", end);
		List<OracleGTMDeniedParties> oracleGTMParsedData = new ArrayList<OracleGTMDeniedParties>();

		try {
			//?) and DPL_STATUS IN (?) as rowResult where RowNum >= ? and RowNum <= ? "
			con = JDBCUtil.getConnection();
			StringBuilder dpltypesBuilder = new StringBuilder();
			StringBuilder dplStatusBuilder = new StringBuilder();
			for( int i = 0 ; i < dplTypes.size(); i++ ) {
				dpltypesBuilder.append("?,");
			}
			for( int i = 0 ; i < dplStatus.size(); i++ ) {
				dplStatusBuilder.append("?,");
			}
			
			String stmt = GET_ENTIRE_DPL_DATA + dpltypesBuilder.deleteCharAt( dpltypesBuilder.length() -1 ).toString() + ") and DPL_STATUS IN (" + dplStatusBuilder.deleteCharAt( dplStatusBuilder.length() -1 ).toString() + ")) as rowResult where RowNum >= ? and RowNum <= ?";
			ps = con.prepareStatement(stmt);
			
			int index = 1;
			for( Object o : dplTypes ) {
			   ps.setObject(  index++, o ); // or whatever it applies 
			}
			for( Object o : dplStatus ) {
			    ps.setObject(  index++, o ); // or whatever it applies 
			}
			ps.setObject(  index++, start );
			ps.setObject(  index++, end);
			

			rs = ps.executeQuery();
			
			while (rs.next()) {

				OracleGTMDeniedParties oracleGTMDeniedParties = new OracleGTMDeniedParties();
				oracleGTMDeniedParties.setDplId("900_" + rs.getInt("DPL_ID"));
				if (StringUtils.isBlank(rs.getString("DPL_ENTITY_TYPE"))
						|| rs.getString("DPL_ENTITY_TYPE").trim().equalsIgnoreCase("Individual")) {
					oracleGTMDeniedParties.setFirstName(rs.getString("DPL_NAME"));
				}
				else if (rs.getString("DPL_ENTITY_TYPE").trim().equalsIgnoreCase("Firm")
						|| rs.getString("DPL_ENTITY_TYPE").trim().equalsIgnoreCase("AIRCRAFT")) {
					oracleGTMDeniedParties.setCompanyName(rs.getString("DPL_NAME"));
				}
				else if (rs.getString("DPL_ENTITY_TYPE").trim().equalsIgnoreCase("Vessel")) {
					oracleGTMDeniedParties.setVessleName(rs.getString("DPL_NAME").trim());
				}
				oracleGTMDeniedParties.setDplAddress1(rs.getString("DPL_ADDRESS_1"));
				oracleGTMDeniedParties.setDplAddress2(rs.getString("DPL_ADDRESS_2"));
				if (StringUtils.isNotBlank(rs.getString("DPL_ADDRESS_3"))) {
					oracleGTMDeniedParties.setCity(rs.getString("DPL_ADDRESS_3").trim());
				}
				else {
					oracleGTMDeniedParties.setCity(null);
				}
				oracleGTMDeniedParties.setProvince(rs.getString("DPL_STATE"));
				oracleGTMDeniedParties.setPostalCode(rs.getString("DPL_ZIP"));
				if (StringUtils.isBlank(rs.getString("DPL_COUNTRY_NAME"))) {
					oracleGTMDeniedParties.setCountryName("UNKNOWN");
				}
				else {
					oracleGTMDeniedParties.setCountryName(rs.getString("DPL_COUNTRY_NAME"));
				}
				// parse date here
				if (StringUtils.isNotBlank(rs.getString("DPL_EFFECTIVE_DATE"))) {
					oracleGTMDeniedParties.setDplEffectiveDate(StringUtils.upperCase(dateFormatter
							.format(dateParser.parse(StringUtils.trim(rs.getString("DPL_EFFECTIVE_DATE"))))));

				}
				else {
					if(StringUtils.isNotBlank(rs.getString("CREATED_ON"))) {
						oracleGTMDeniedParties.setDplEffectiveDate(StringUtils.upperCase(dateFormatter
								.format(dateParser.parse(StringUtils.trim(rs.getString("CREATED_ON"))))));
					}else {
						oracleGTMDeniedParties.setDplEffectiveDate(null);
					}
					
				}
				if (StringUtils.isNotBlank(rs.getString("DPL_EXPIRY_DATE"))) {
					if (rs.getString("DPL_EXPIRY_DATE").trim().contains("Indef")
							|| rs.getString("DPL_EXPIRY_DATE").trim().contains("UNTIL")) {
						oracleGTMDeniedParties.setDplExpiryDate("99991231000000");
					}
					else {
						oracleGTMDeniedParties.setDplExpiryDate(StringUtils.upperCase(dateFormatter
								.format(dateParserMMDD.parse(StringUtils.trim(rs.getString("DPL_EXPIRY_DATE"))))));
					}

				}
				
				oracleGTMDeniedParties.setDeniedCode(rs.getString("DPL_CATEGORY"));
				if (StringUtils.isNotBlank(rs.getString("CREATED_ON"))) {
					oracleGTMDeniedParties.setEntryDate(StringUtils.upperCase(
							dateFormatter.format(dateParser.parse(StringUtils.trim(rs.getString("CREATED_ON"))))));
				}
				else {
					oracleGTMDeniedParties.setEntryDate(null);
				}
				oracleGTMDeniedParties.setEntryId(rs.getInt("DPL_ID"));
				oracleGTMDeniedParties.setAgencyCode(rs.getString("DPL_TYPE"));
				oracleGTMDeniedParties.setRulingVolume(rs.getString("DPL_FRC"));
				
				if (StringUtils.isNotBlank(rs.getString("DPL_FRC_DATE"))) {
					oracleGTMDeniedParties.setFederalRegDate(StringUtils.upperCase(
							dateFormatter.format(dateParser.parse(StringUtils.trim(rs.getString("DPL_FRC_DATE"))))));
				}
				else {
					oracleGTMDeniedParties.setFederalRegDate(null);
				}

				if (StringUtils.isNotBlank(rs.getString("DPL_PRIVILEGES"))
						&& StringUtils.isNotBlank(rs.getString("DPL_ADDL_INFO"))) {
					oracleGTMDeniedParties.setNotes(rs.getString("DPL_PRIVILEGES")+";"+" "+ rs.getString("DPL_ADDL_INFO"));
				}
				else if (StringUtils.isNotBlank(rs.getString("DPL_PRIVILEGES"))
						&& StringUtils.isBlank(rs.getString("DPL_ADDL_INFO"))) {
					oracleGTMDeniedParties.setNotes(rs.getString("DPL_ADDL_INFO"));
				}
				else {
					oracleGTMDeniedParties.setNotes(null);
				}
				oracleGTMDeniedParties.setGtmDateVersionId("900");
				if (rs.getString("DPL_STATUS").equals("A")) {
					oracleGTMDeniedParties.setIsInUse("Y");
				}
				else if (rs.getString("DPL_STATUS").equals("D")) {
					oracleGTMDeniedParties.setIsInUse("N");
				}

				oracleGTMDeniedParties.setDplURL(rs.getString("DPL_URL"));
				oracleGTMDeniedParties.setDomainName("PUBLIC");
				
				if (StringUtils.isNotBlank(rs.getString("DPL_COUNTRY"))) {
					oracleGTMDeniedParties.setCountry1IsoCode(rs.getString("DPL_COUNTRY"));
				}
				else {
					oracleGTMDeniedParties.setCountry1IsoCode("XX");
				}
				oracleGTMDeniedParties.setDplSeeAlso(rs.getString("DPL_SEE_ALSO"));

				oracleGTMParsedData.add(oracleGTMDeniedParties);
				
			}
			

		}
		catch (SQLException e) {
			System.out.println("SQl Exception while getting entire data for Oracle GTM");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("Exception while getting entire data for Oracle GTM");
			e.printStackTrace();
		}

		return oracleGTMParsedData;
	}

	@Override
	public List<OracleGTMDeniedParties> getDPLIncrementalData(List<String> dplStatus, List<String> dplTypes, Integer start,
			Integer end, Date lastDataPusblisDate, Date currentDataPusblisDate) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dpltypes", dplTypes);
		parameters.put("dplStatus", dplStatus);
		parameters.put("start", start);
		parameters.put("end", end);
		parameters.put("startDate", lastDataPusblisDate);
		parameters.put("endDate", currentDataPusblisDate);
		List<OracleGTMDeniedParties> oracleGTMParsedData = new ArrayList<OracleGTMDeniedParties>();

		try {

			con = JDBCUtil.getConnection();
			ps = con.prepareStatement(GET_ENTIRE_DPL_DATA);
			ps.setString(1, dplTypes.toString());
			ps.setString(2, dplStatus.toString());
			ps.setInt(3, start);
			ps.setInt(4, end);
			rs = ps.executeQuery();
			
			while (rs.next()) {

				OracleGTMDeniedParties oracleGTMDeniedParties = new OracleGTMDeniedParties();
				oracleGTMDeniedParties.setDplId("900_" + rs.getInt("DPL_ID"));
				if (StringUtils.isBlank(rs.getString("DPL_ENTITY_TYPE"))
						|| rs.getString("DPL_ENTITY_TYPE").trim().equalsIgnoreCase("Individual")) {
					oracleGTMDeniedParties.setFirstName(rs.getString("DPL_ENTITY_TYPE"));
				}
				else if (rs.getString("DPL_ENTITY_TYPE").trim().equalsIgnoreCase("Firm")
						|| rs.getString("DPL_ENTITY_TYPE").trim().equalsIgnoreCase("AIRCRAFT")) {
					oracleGTMDeniedParties.setCompanyName(rs.getString("DPL_ENTITY_TYPE"));
				}
				else if (rs.getString("DPL_ENTITY_TYPE").trim().equalsIgnoreCase("Vessel")) {
					oracleGTMDeniedParties.setVessleName(rs.getString("DPL_ENTITY_TYPE").trim());
				}
				oracleGTMDeniedParties.setDplAddress1(rs.getString("DPL_ADDRESS_1"));
				oracleGTMDeniedParties.setDplAddress2(rs.getString("DPL_ADDRESS_2"));
				if (StringUtils.isNotBlank(rs.getString("DPL_ADDRESS_3"))) {
					oracleGTMDeniedParties.setCity(rs.getString("DPL_ADDRESS_3").trim());
				}
				else {
					oracleGTMDeniedParties.setCity(null);
				}
				oracleGTMDeniedParties.setProvince(rs.getString("DPL_STATE"));
				oracleGTMDeniedParties.setPostalCode(rs.getString("DPL_ZIP"));
				if (StringUtils.isBlank(rs.getString("DPL_COUNTRY_NAME"))) {
					oracleGTMDeniedParties.setCountryName("UNKNOWN");
				}
				else {
					oracleGTMDeniedParties.setCountryName(rs.getString("DPL_COUNTRY_NAME"));
				}
				// parse date here
				if (StringUtils.isNotBlank(rs.getString("DPL_EFFECTIVE_DATE"))) {
					oracleGTMDeniedParties.setDplEffectiveDate(StringUtils.upperCase(dateFormatter
							.format(dateParser.parse(StringUtils.trim(rs.getString("DPL_EFFECTIVE_DATE"))))));

				}
				else {
					if(StringUtils.isNotBlank(rs.getString("CREATED_ON"))) {
						oracleGTMDeniedParties.setDplEffectiveDate(StringUtils.upperCase(dateFormatter
								.format(dateParser.parse(StringUtils.trim(rs.getString("CREATED_ON"))))));
					}else {
						oracleGTMDeniedParties.setDplEffectiveDate(null);
					}
					
				}
				if (StringUtils.isNotBlank(rs.getString("DPL_EXPIRY_DATE"))) {
					if (rs.getString("DPL_EXPIRY_DATE").trim().equals("Indef")
							|| rs.getString("DPL_EXPIRY_DATE").equals("UNTIL RESCINDED")) {
						oracleGTMDeniedParties.setDplExpiryDate("99991231000000");
					}
					else {

						oracleGTMDeniedParties.setDplExpiryDate(StringUtils.upperCase(dateFormatter
								.format(dateParser.parse(StringUtils.trim(rs.getString("DPL_EXPIRY_DATE"))))));
					}

				}
				
				oracleGTMDeniedParties.setDeniedCode(rs.getString("DPL_CATEGORY"));
				if (StringUtils.isNotBlank(rs.getString("CREATED_ON"))) {
					oracleGTMDeniedParties.setEntryDate(StringUtils.upperCase(
							dateFormatter.format(dateParser.parse(StringUtils.trim(rs.getString("CREATED_ON"))))));
				}
				else {
					oracleGTMDeniedParties.setEntryDate(null);
				}
				oracleGTMDeniedParties.setEntryId(rs.getInt("DPL_ID"));
				oracleGTMDeniedParties.setAgencyCode(rs.getString("DPL_TYPE"));
				oracleGTMDeniedParties.setRulingVolume(rs.getString("DPL_FRC"));
				
				if (StringUtils.isNotBlank(rs.getString("DPL_FRC_DATE"))) {
					oracleGTMDeniedParties.setFederalRegDate(StringUtils.upperCase(
							dateFormatter.format(dateParser.parse(StringUtils.trim(rs.getString("DPL_FRC_DATE"))))));
				}
				else {
					oracleGTMDeniedParties.setFederalRegDate(null);
				}

				if (StringUtils.isNotBlank(rs.getString("DPL_PRIVILEGES"))
						&& StringUtils.isNotBlank(rs.getString("DPL_ADDL_INFO"))) {
					oracleGTMDeniedParties.setNotes(rs.getString("DPL_PRIVILEGES")+";"+" "+ rs.getString("DPL_ADDL_INFO"));
				}
				else if (StringUtils.isNotBlank(rs.getString("DPL_PRIVILEGES"))
						&& StringUtils.isBlank(rs.getString("DPL_ADDL_INFO"))) {
					oracleGTMDeniedParties.setNotes(rs.getString("DPL_ADDL_INFO"));
				}
				else {
					oracleGTMDeniedParties.setNotes(null);
				}
				oracleGTMDeniedParties.setGtmDateVersionId("900");
				if (rs.getString("DPL_STATUS").equals("A")) {
					oracleGTMDeniedParties.setIsInUse("Y");
				}
				else if (rs.getString("DPL_STATUS").equals("D")) {
					oracleGTMDeniedParties.setIsInUse("N");
				}

				oracleGTMDeniedParties.setDplURL(rs.getString("DPL_URL"));
				oracleGTMDeniedParties.setDomainName("PUBLIC");
				
				if (StringUtils.isNotBlank(rs.getString("DPL_COUNTRY"))) {
					oracleGTMDeniedParties.setCountry1IsoCode(rs.getString("DPL_COUNTRY"));
				}
				else {
					oracleGTMDeniedParties.setCountry1IsoCode("XX");
				}
				oracleGTMDeniedParties.setDplSeeAlso(rs.getString("DPL_SEE_ALSO"));

				oracleGTMParsedData.add(oracleGTMDeniedParties);
			
				
			}
			

		
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return oracleGTMParsedData;
	}

	@Override
	public Integer dplEntiredataCount(List<String> dplStatus, List<String> dplTypes) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dpltypes", dplTypes);
		parameters.put("dplStatus", dplStatus);
		Integer count = 0;

		try {
			//IN (?) and DPL_STATUS IN (?)
			con = JDBCUtil.getConnection();
			StringBuilder dpltypesBuilder = new StringBuilder();
			StringBuilder dplStatusBuilder = new StringBuilder();
			for( int i = 0 ; i < dplTypes.size(); i++ ) {
				dpltypesBuilder.append("?,");
			}
			for( int i = 0 ; i < dplStatus.size(); i++ ) {
				dplStatusBuilder.append("?,");
			}
			
			String stmt = COUNT_DENIED_PARTY_LIST_BY_DPLTYPES_STATUS + dpltypesBuilder.deleteCharAt( dpltypesBuilder.length() -1 ).toString() + ") and DPL_STATUS IN (" + dplStatusBuilder.deleteCharAt( dplStatusBuilder.length() -1 ).toString() + ")";
			ps = con.prepareStatement(stmt);
			
			int index = 1;
			for( Object o : dplTypes ) {
			   ps.setObject(  index++, o ); // or whatever it applies 
			}
			for( Object o : dplStatus ) {
			    ps.setObject(  index++, o ); // or whatever it applies 
			}
			
			//ps.setString(1, dplTypes.toString());
			//ps.setString(2, dplStatus.toString());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				count = rs.getInt(1);
		      } else {
		        System.out.println("error: could not get the record counts");
		      }
		}
		catch (Exception e) {

			e.printStackTrace();
		}

		return count;
	}

	@Override
	public Integer dplIncrementalDataCount(List<String> dplStatus, List<String> dplTypes, Date lastDataPusblisDate,
			Date currentDataPusblisDate) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dpltypes", dplTypes);
		parameters.put("dplStatus", dplStatus);
		parameters.put("startDate", lastDataPusblisDate);
		parameters.put("endDate", currentDataPusblisDate);
		Integer count = 0;

		try {
			con = JDBCUtil.getConnection();
			ps = con.prepareStatement(COUNT_INC_DENIED_PARTY_LIST_BY_DPLTYPES_STATUS);
			ps.setString(1, dplTypes.toString());
			ps.setString(2, dplStatus.toString());
			ps.setString(3, lastDataPusblisDate.toString());
			ps.setString(4, currentDataPusblisDate.toString());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				count = rs.getInt(1);
		        System.out.println("numberOfRows= " + count);
		      } else {
		        System.out.println("error: could not get the record counts");
		      }

		}
		catch (Exception e) {

			e.printStackTrace();
		}

		return count;
	}

	
	@Override
	public Date getDateByDateState(String dateState) {

		Date date = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dateState", dateState);

		try {

			con = JDBCUtil.getConnection();
			ps = con.prepareStatement(GET_DATE_BY_DATE_STATE);
			ps.setString(1, dateState);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				date = rs.getDate(1);
		      } else {
		        System.out.println("error: could not get the record counts");
		      }

		}
		catch (Exception e) {

			e.printStackTrace();
		}

		return date;
	}

}
