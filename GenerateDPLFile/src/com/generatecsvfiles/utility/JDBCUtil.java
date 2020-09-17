package com.generatecsvfiles.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
*
* @author SONU KUMAR
*/
public class JDBCUtil {
	
   //  ORACLE Database credentials
	private static final String ORACLE_JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
	private static final String ORACLE_DB_URL = "jdbc:oracle:thin:@192.168.2.236:1521:EASETEST";
	private static final String ORACLE_USER = "commteam";
	private static final String ORACLE_PASSWORD = "Password!";
	
   //  SQLSERVER Database credentials
	private static final String SQLSERVER_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  
	private static final String SQLSERVER_DB_URL = "jdbc:sqlserver://localhost:1433;DatabaseName=CDRDEV";
	private static final String SQLSERVER_USER = "sa";
	private static final String SQLSERVER_PASSWORD = "ocr";
	
	
	
	private JDBCUtil() {}

	static {
		try {
			Class.forName(PropertyLoader.getKeyValue("JDBC_DRIVER"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		Connection con = null;
		
		try {
			con = DriverManager.getConnection(PropertyLoader.getKeyValue("DB_URL"), PropertyLoader.getKeyValue("DB_USER") , PropertyLoader.getKeyValue("DB_PASSWORD"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void close(ResultSet rs, Statement st, Connection con) {
		try {
			if(rs !=null) {
				rs.close();
			}
			if(st !=null) {
				st.close();
			}
			if(con !=null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
