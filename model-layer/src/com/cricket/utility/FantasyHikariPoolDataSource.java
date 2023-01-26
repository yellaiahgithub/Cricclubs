package com.cricket.utility;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.configuration.EnviromentProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class FantasyHikariPoolDataSource {
	static Logger log = LoggerFactory.getLogger(FantasyHikariPoolDataSource.class);
    private static FantasyHikariPoolDataSource     datasource;
    private HikariDataSource connectionPool;
    
    private static FantasyHikariPoolDataSource     datasourceRead;
    private HikariDataSource connectionPoolRead;
    
    private FantasyHikariPoolDataSource() throws IOException, SQLException, PropertyVetoException {

	
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName((DButility.isLogSQLPerformance()?DButility.DB_LOG_DRIVER:DButility.DB_DRIVER));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }

        try {
            // setup the connection pool using BoneCP Configuration
        	
        	HikariConfig config = new HikariConfig();
        	config.setJdbcUrl((DButility.isLogSQLPerformance()?DButility.DB_LOG_URL_CCFANTASY:DButility.DB_URL_CCFANTASY)+ "?zeroDateTimeBehavior=convertToNull&useSSL=false");
        	config.setUsername(DButility.FANTASY_DB_USERNAME);
        	config.setPassword(DButility.FANTASY_DB_PASSWORD);
        	config.addDataSourceProperty("cachePrepStmts", "true");
        	config.addDataSourceProperty("prepStmtCacheSize", "250");
        	config.addDataSourceProperty("prepStmtCacheSqlLimit", "1024");
        	config.setLeakDetectionThreshold(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.leakdetectionthreshold")));
        	config.setMaximumPoolSize(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.maximumpoolsize")));
        	config.setConnectionTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.connectiontimeout")));
        	config.setValidationTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.validationtimeout")));
        	config.setMinimumIdle(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.minidleconnection")));
        	//config.setIdleTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.idletimeout")));
        	connectionPool = new HikariDataSource(config);
        	
        	
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
    }

    private FantasyHikariPoolDataSource(String readLogUrl, String readUrl, String userName, String passwrod) throws IOException, SQLException, PropertyVetoException {

    	
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName((DButility.isLogSQLPerformance()?DButility.DB_LOG_DRIVER:DButility.DB_DRIVER));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }

        try {
            // setup the connection pool using BoneCP Configuration
        	
        	HikariConfig config = new HikariConfig();
        	config.setJdbcUrl((DButility.isLogSQLPerformance()?readLogUrl:readUrl)+ "?zeroDateTimeBehavior=convertToNull&useSSL=false");
        	config.setUsername(userName);
        	config.setPassword(passwrod);
        	config.addDataSourceProperty("cachePrepStmts", "true");
        	config.addDataSourceProperty("prepStmtCacheSize", "250");
        	config.addDataSourceProperty("prepStmtCacheSqlLimit", "1024");
        	config.setLeakDetectionThreshold(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.leakdetectionthreshold")));
        	config.setMaximumPoolSize(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.maximumpoolsize")));
        	config.setConnectionTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.connectiontimeout")));
        	config.setValidationTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.validationtimeout")));
        	config.setMinimumIdle(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.minidleconnection")));
        	//config.setIdleTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("ccfantasy.db.idletimeout")));

        	connectionPoolRead = new HikariDataSource(config);
        	
        	
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
    }
    
    public static FantasyHikariPoolDataSource getInstance(String readLogUrl, String readUrl, String userName, String passwrod) throws IOException, SQLException, PropertyVetoException {
        if (datasourceRead == null) {
        	datasourceRead = new FantasyHikariPoolDataSource(readLogUrl, readUrl, userName, passwrod);
            return datasourceRead;
        } else {
            return datasourceRead;
        }
    }
    public static FantasyHikariPoolDataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new FantasyHikariPoolDataSource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {

    	return this.connectionPool.getConnection();
    }
    
    public Connection getConnectionRead() throws SQLException {

    	return this.connectionPoolRead.getConnection();
    }

}