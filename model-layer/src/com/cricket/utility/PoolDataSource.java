package com.cricket.utility;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;


public class PoolDataSource {
	
	private static Logger log = LoggerFactory.getLogger(PoolDataSource.class);
    private static PoolDataSource     datasource;
    private BoneCP connectionPool;
    
    private PoolDataSource() throws IOException, SQLException, PropertyVetoException {

	
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName((DButility.isLogSQLPerformance()?DButility.DB_LOG_DRIVER:DButility.DB_DRIVER));
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            return;
        }

        try {
            // setup the connection pool using BoneCP Configuration
            BoneCPConfig config = new BoneCPConfig();
            // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setJdbcUrl((DButility.isLogSQLPerformance()?DButility.DB_LOG_URL:DButility.DB_URL)+ "?zeroDateTimeBehavior=convertToNull");
            config.setUsername(DButility.DB_USERNAME);
            config.setPassword(DButility.DB_PASSWORD);
            config.setMinConnectionsPerPartition(3);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(3);
            config.setStatementsCacheSize(500);
            
            // setup the connection pool
            connectionPool = new BoneCP(config);
            
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            return;
        }
    }
    
    private PoolDataSource(String readLogUrl, String readUrl, String userName, String passwrod) throws IOException, SQLException, PropertyVetoException {

    	
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName((DButility.isLogSQLPerformance()?DButility.DB_LOG_DRIVER:DButility.DB_DRIVER));
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            return;
        }

        try {
            // setup the connection pool using BoneCP Configuration
            BoneCPConfig config = new BoneCPConfig();
            // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setJdbcUrl((DButility.isLogSQLPerformance()?readLogUrl:readUrl)+ "?zeroDateTimeBehavior=convertToNull");
            config.setUsername(userName);
            config.setPassword(passwrod);
            config.setMinConnectionsPerPartition(3);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(3);
            config.setStatementsCacheSize(500);
            
            // setup the connection pool
            connectionPool = new BoneCP(config);
            
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            return;
        }
    }
    public static PoolDataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new PoolDataSource();
            return datasource;
        } else {
            return datasource;
        }
    }
    public static PoolDataSource getInstance(String readLogUrl, String readUrl, String userName, String passwrod) throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new PoolDataSource(readLogUrl, readUrl, userName, passwrod);
            return datasource;
        } else {
            return datasource;
        }
    }
    public Connection getConnection() throws SQLException {

    	return this.connectionPool.getConnection();
    }

}