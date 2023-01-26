/*

 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.utility;

import static com.cricket.utility.ApplicationConstants.DB_HOST_KEY;
import static com.cricket.utility.ApplicationConstants.DB_HOST_KEY_READ;
import static com.cricket.utility.ApplicationConstants.DB_HOST_KEY_CCFANTASY;
import static com.cricket.utility.ApplicationConstants.DB_HOST_KEY_READ_CCFANTASY;
import static com.cricket.utility.ApplicationConstants.DB_PASSWORD_KEY;
import static com.cricket.utility.ApplicationConstants.DB_POOL_KEY;
import static com.cricket.utility.ApplicationConstants.DB_POOL_TYPE_KEY;
import static com.cricket.utility.ApplicationConstants.DB_SQL_LOG_KEY;
import static com.cricket.utility.ApplicationConstants.DB_USER_KEY;
import static com.cricket.utility.ApplicationConstants.FANTASY_DB_USER_KEY;
import static com.cricket.utility.ApplicationConstants.FANTASY_DB_PASSWORD_KEY;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.configuration.EnviromentProperties;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DButility {
	
	static Logger log = LoggerFactory.getLogger(DButility.class);
	public static String DB_URL = "jdbc:mysql://"+getDBHost()+":3306/";
	public static String DB_URL_READ = "jdbc:mysql://"+getDBHostRead()+":3306/";
	public static String DB_URL_CCFANTASY = "jdbc:mysql://"+getDBHostCCFantasy()+":3306/";
	public static String DB_URL_READ_CCFANTASY = "jdbc:mysql://"+getDBHostReadCCFantasy()+":3306/";
	public static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static String DB_LOG_URL = "jdbc:log4jdbc:mysql://"+getDBHost()+":3306/";
	public static String DB_LOG_URL_READ = "jdbc:log4jdbc:mysql://"+getDBHostRead()+":3306/";
	public static String DB_LOG_URL_CCFANTASY = "jdbc:log4jdbc:mysql://"+getDBHostCCFantasy()+":3306/";
	public static String DB_LOG_URL_READ_CCFANTASY = "jdbc:log4jdbc:mysql://"+getDBHostReadCCFantasy()+":3306/";
	public static String DB_LOG_DRIVER = "net.sf.log4jdbc.DriverSpy";

	public static String DB_USERNAME = getDBUser();
	public static String DB_PASSWORD = getDBPass();
	
	public static String FANTASY_DB_USERNAME = getFantasyDBUser();
	public static String FANTASY_DB_PASSWORD = getFantasyDBPass();
	


	private static final HashMap<String,String> sqlTokens;
	private static Pattern sqlTokenPattern;

	static
	{           
	    //MySQL escape sequences: http://dev.mysql.com/doc/refman/5.1/en/string-syntax.html
	    String[][] search_regex_replacement = new String[][]
	    {
	                //search string     search regex        sql replacement regex
	            {   "\u0000"    ,       "\\x00"     ,       "\\\\0"     },
	            {   "'"         ,       "'"         ,       "\\\\'"     },
	            {   "\""        ,       "\""        ,       "\\\\\""    },
	            {   "\b"        ,       "\\x08"     ,       "\\\\b"     },
	            {   "\n"        ,       "\\n"       ,       "\\\\n"     },
	            {   "\r"        ,       "\\r"       ,       "\\\\r"     },
	            {   "\t"        ,       "\\t"       ,       "\\\\t"     },
	            {   "\u001A"    ,       "\\x1A"     ,       "\\\\Z"     },
	            {   "\\"        ,       "\\\\"      ,       "\\\\\\\\"  }
	    };

	    sqlTokens = new HashMap<String,String>();
	    String patternStr = "";
	    for (String[] srr : search_regex_replacement)
	    {
	        sqlTokens.put(srr[0], srr[2]);
	        patternStr += (patternStr.isEmpty() ? "" : "|") + srr[1];            
	    }
	    sqlTokenPattern = Pattern.compile('(' + patternStr + ')');
	}

	
	public static Connection getDefaultConnection() {
		int DEFAULT_DB_ID = 1;
		return getConnection(DEFAULT_DB_ID);
	}
	
	public static Connection getDefaultReadConnection() {
		int DEFAULT_DB_ID = 1;
		return getReadConnection(DEFAULT_DB_ID);
	}

	public static Connection getConnection(int clubId) {

		return getConnection(clubId, true);
	}

	
	public static Connection getReadConnection(int clubId) {

		return getReadConnection(clubId, true);
	}
	
	public static Connection getConnection(int clubId, boolean selectSchema) {

		Connection conn = null;

		String dbName = getDBName(clubId);

		try {

			if (!isUseDBPool()) {
				Class.forName(isLogSQLPerformance() ? DB_LOG_DRIVER : DB_DRIVER).newInstance();
				conn =
						DriverManager.getConnection((isLogSQLPerformance() ? DB_LOG_URL : DB_URL) + dbName + "?zeroDateTimeBehavior=convertToNull&useSSL=false", DB_USERNAME, DB_PASSWORD);
			} else {

				if (isPoolTypeBoneCP()) {
					conn = PoolDataSource.getInstance().getConnection();
				} else {
					conn = HikariPoolDataSource.getInstance().getConnection();
				}

				if (selectSchema)
					setDBSchema(conn, dbName);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return conn;
	}
	
	public static Connection getReadConnection(int clubId, boolean selectSchema) {

		Connection conn = null;

		String dbName = getDBName(clubId);

		try {

			if (!isUseDBPool()) {
				Class.forName(isLogSQLPerformance() ? DB_LOG_DRIVER : DB_DRIVER).newInstance();
				conn =
						DriverManager.getConnection((isLogSQLPerformance() ? DB_LOG_URL_READ : DB_URL_READ) + dbName + "?zeroDateTimeBehavior=convertToNull&useSSL=false", DB_USERNAME, DB_PASSWORD);
			} else {

				if (isPoolTypeBoneCP()) {
					conn = PoolDataSource.getInstance(DB_LOG_URL_READ, DB_URL_READ, DB_USERNAME, DB_PASSWORD).getConnection();
				} else {
					conn = HikariPoolDataSource.getInstance(DB_LOG_URL_READ, DB_URL_READ, DB_USERNAME, DB_PASSWORD).getConnectionRead();
				}

				if (selectSchema)
					setDBSchema(conn, dbName);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return conn;
	}
	
	public static Connection getFantasyReadConnection(String schema) {

		Connection conn = null;
		try {
			if (!isUseDBPool()) {
				Class.forName(isLogSQLPerformance() ? DB_LOG_DRIVER : DB_DRIVER).newInstance();
				conn = DriverManager.getConnection((isLogSQLPerformance() ? DB_LOG_URL_CCFANTASY : DB_URL_READ_CCFANTASY) + schema + "?zeroDateTimeBehavior=convertToNull&useSSL=false", FANTASY_DB_USERNAME, FANTASY_DB_PASSWORD);
			} else {

				if (isPoolTypeBoneCP()) {
					conn = PoolDataSource.getInstance(DB_LOG_URL_CCFANTASY, DB_URL_READ_CCFANTASY, FANTASY_DB_USERNAME, FANTASY_DB_PASSWORD).getConnection();
				} else {
					conn = FantasyHikariPoolDataSource.getInstance(DB_LOG_URL_READ_CCFANTASY, DB_URL_READ_CCFANTASY, FANTASY_DB_USERNAME, FANTASY_DB_PASSWORD).getConnectionRead();
				}
					setDBSchema(conn, schema);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return conn;
	}
	
	public static Connection getFantasyWriteConnection(String schema) {

		Connection conn = null;
		try {

			if (!isUseDBPool()) {
				Class.forName(isLogSQLPerformance() ? DB_LOG_DRIVER : DB_DRIVER).newInstance();
				conn = DriverManager.getConnection((isLogSQLPerformance() ? DB_LOG_URL_CCFANTASY : DB_URL_CCFANTASY) + schema + "?zeroDateTimeBehavior=convertToNull&useSSL=false", FANTASY_DB_USERNAME, FANTASY_DB_PASSWORD);
			} else {
				if (isPoolTypeBoneCP()) {
					conn = PoolDataSource.getInstance().getConnection();
				} else {
					conn = FantasyHikariPoolDataSource.getInstance().getConnection();
				}
				setDBSchema(conn, schema);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return conn;
	}
	
	private static void setDBSchema(Connection conn, String dbName) {

		Statement stmt = null;

		try {

			stmt = conn.createStatement();

			stmt.execute("USE " + dbName);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e1) {
				}
		}
	}

	public static void closeConnection(Connection conn) {
		try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void dbCloseAll(Connection conn, Statement stmt, ResultSet rs) {
		try {
			closeRs(rs);
			closeStatement(stmt);
			closeConnection(conn);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static void closeRs(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
			}
		}
	}

	public static void closeConnectionAndStatement(Connection conn, Statement stmt) {
		try {
			closeStatement(stmt);
			closeConnection(conn);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}	

	public static String escapeLine(String s) throws Exception {
		String retvalue = s;
		if (s != null && s.indexOf("'") != -1) {
			StringBuffer hold = new StringBuffer();
			char c;
			for (int i = 0; i < s.length(); i++) {
				if ((c = s.charAt(i)) == '\'') {
					hold.append("'");
				} else {
					hold.append(c);
				}
			}
			retvalue = hold.toString();
		}
		return retvalue == null ? "" : retvalue;
	}
	
	public static String escapeString(String s) throws Exception {
		String retvalue = s;
		if (s != null && s.indexOf("'") != -1) {
			StringBuffer hold = new StringBuffer();
			char c;
			for (int i = 0; i < s.length(); i++) {
				if ((c = s.charAt(i)) == '\'') {
					hold.append("'");
				} else {
					hold.append(c);
				}
			}
			retvalue = hold.toString();
		}
		return retvalue == null ? "" : retvalue;
	}
	
	public static String escapeQuotes(String s) throws Exception {
		String retvalue = s;
		if (!CommonUtility.isNullOrEmptyOrNULL(s) && (s.contains("\"")) ) {	
				retvalue =  s.replaceAll("\"", ""); // ignored and delete 
		}
		return CommonUtility.isNullOrEmptyOrNULL(retvalue) ? "" : retvalue.trim();
	}

	public static String getDBName(int clubId) {

		String dbName = "mcc";

		if (clubId != 0 && clubId == 1) {
			dbName = "mcc";
		} else if (clubId > 1) {
			dbName = "club" + clubId;
		}

		return dbName;
	}

	public static String getDBHost() {
		/*
		if (!CommonUtility.isNullOrEmpty(System.getProperty("DatabaseEndpointAddress"))) {
			return System.getProperty("DatabaseEndpointAddress");
		}
		*/
		String dbHost = EnviromentProperties.getEnvProperty(DB_HOST_KEY);
		if (dbHost != null) {
			return dbHost;
		}
		 return "";
	}
	
	public static String getDBHostCCFantasy() {
		/*
		if (!CommonUtility.isNullOrEmpty(System.getProperty("DatabaseEndpointAddress"))) {
			return System.getProperty("DatabaseEndpointAddress");
		}
		*/
		String dbHost = EnviromentProperties.getEnvProperty(DB_HOST_KEY_CCFANTASY);
		if (dbHost != null) {
			return dbHost;
		}
		 return "";
	}

	public static String getDBHostRead() {
		/*
		if (!CommonUtility.isNullOrEmpty(System.getProperty("DatabaseEndpointAddressRead"))) {
			return System.getProperty("DatabaseEndpointAddressRead");
		}
		*/
		String dbHost = EnviromentProperties.getEnvProperty(DB_HOST_KEY_READ);
		if (dbHost != null) {
			return dbHost;
		}
		 return "";
	}
	
	public static String getDBHostReadCCFantasy() {
		/*
		if (!CommonUtility.isNullOrEmpty(System.getProperty("DatabaseEndpointAddressRead"))) {
			return System.getProperty("DatabaseEndpointAddressRead");
		}
		*/
		String dbHost = EnviromentProperties.getEnvProperty(DB_HOST_KEY_READ_CCFANTASY);
		if (dbHost != null) {
			return dbHost;
		}
		 return "";
	}
	
	public static String getDBUser() {
		if (!CommonUtility.isNullOrEmpty(System.getProperty("DbMasterUser"))) {
			return System.getProperty("DbMasterUser");
		}
		String dbUser = EnviromentProperties.getEnvProperty(DB_USER_KEY);
		if (dbUser != null) {
			return dbUser;
		}
		return "";
	}

	public static String getDBPass() {
		if (!CommonUtility.isNullOrEmpty(System.getProperty("DbMasterPassword"))) {
			return System.getProperty("DbMasterPassword");
		}
		String dbPass = EnviromentProperties.getEnvProperty(DB_PASSWORD_KEY);
		if (dbPass != null) {
			return dbPass;
		}
		return "";
	}
	
	public static String getFantasyDBUser() {
		if (!CommonUtility.isNullOrEmpty(System.getProperty("FantasyDbMasterUser"))) {
			return System.getProperty("FantasyDbMasterUser");
		}
		String dbUser = EnviromentProperties.getEnvProperty(FANTASY_DB_USER_KEY);
		if (dbUser != null) {
			return dbUser;
		}
		return "";
	}

	public static String getFantasyDBPass() {
		if (!CommonUtility.isNullOrEmpty(System.getProperty("FantasyDbMasterPassword"))) {
			return System.getProperty("FantasyDbMasterPassword");
		}
		String dbPass = EnviromentProperties.getEnvProperty(FANTASY_DB_PASSWORD_KEY);
		if (dbPass != null) {
			return dbPass;
		}
		return "";
	}

	public static boolean isUseDBPool() {
		String dbPool = EnviromentProperties.getEnvProperty(DB_POOL_KEY);
		if (!CommonUtility.isNullOrEmpty(dbPool)) {
			return new Boolean(dbPool).booleanValue();
		}
		return false;
	}

	public static boolean isPoolTypeBoneCP() {
		String poolType = EnviromentProperties.getEnvProperty(DB_POOL_TYPE_KEY);
		if (CommonUtility.isNullOrEmpty(poolType)) {
			return "bonecp".equalsIgnoreCase(EnviromentProperties.getEnvProperty(DB_POOL_KEY));
		}
		return false;
	}

	public static boolean isLogSQLPerformance() {
		String sqlLog = EnviromentProperties.getEnvProperty(DB_SQL_LOG_KEY);
		if (CommonUtility.isNullOrEmpty(sqlLog)) {
			return new Boolean(sqlLog).booleanValue();
		}
		return false;
	}
	
	public static String escape(String s)
	{
	    Matcher matcher = sqlTokenPattern.matcher(s);
	    StringBuffer sb = new StringBuffer();
	    while(matcher.find())
	    {
	        matcher.appendReplacement(sb, sqlTokens.get(matcher.group(1)));
	    }
	    matcher.appendTail(sb);
	    return sb.toString();
	}

}
