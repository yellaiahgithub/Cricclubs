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


public class HikariPoolDataSource {
	static Logger log = LoggerFactory.getLogger(HikariPoolDataSource.class);
    private static HikariPoolDataSource     datasource;
    private HikariDataSource connectionPool;
    
    private static HikariPoolDataSource     datasourceRead;
    private HikariDataSource connectionPoolRead;
    
    private HikariPoolDataSource() throws IOException, SQLException, PropertyVetoException {

	
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
        	config.setJdbcUrl((DButility.isLogSQLPerformance()?DButility.DB_LOG_URL:DButility.DB_URL)+ "?zeroDateTimeBehavior=convertToNull&useSSL=false");
        	config.setUsername(DButility.DB_USERNAME);
        	config.setPassword(DButility.DB_PASSWORD);
        	config.addDataSourceProperty("cachePrepStmts", "true");
        	config.addDataSourceProperty("prepStmtCacheSize", "250");
        	config.addDataSourceProperty("prepStmtCacheSqlLimit", "1024");
        	config.setLeakDetectionThreshold(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.leakdetectionthreshold")));
        	config.setMaximumPoolSize(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.maximumpoolsize")));
        	config.setConnectionTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.connectiontimeout")));
        	config.setValidationTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.validationtimeout")));
        	config.setMinimumIdle(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.minidleconnection")));
        	//config.setIdleTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.idletimeout")));
        	connectionPool = new HikariDataSource(config);
        	
        	
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
    }

    private HikariPoolDataSource(String readLogUrl, String readUrl, String userName, String passwrod) throws IOException, SQLException, PropertyVetoException {

    	
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
        	config.setLeakDetectionThreshold(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.leakdetectionthreshold")));
        	config.setMaximumPoolSize(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.maximumpoolsize")));
        	config.setConnectionTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.connectiontimeout")));
        	config.setValidationTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.validationtimeout")));
        	config.setMinimumIdle(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.minidleconnection")));
        	//config.setIdleTimeout(CommonUtility.stringToInt(EnviromentProperties.getEnvProperty("cricclubs.db.idletimeout")));

        	connectionPoolRead = new HikariDataSource(config);
        	
        	
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
    }
    
    public static HikariPoolDataSource getInstance(String readLogUrl, String readUrl, String userName, String passwrod) throws IOException, SQLException, PropertyVetoException {
        if (datasourceRead == null) {
        	datasourceRead = new HikariPoolDataSource(readLogUrl, readUrl, userName, passwrod);
            return datasourceRead;
        } else {
            return datasourceRead;
        }
    }
    public static HikariPoolDataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new HikariPoolDataSource();
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