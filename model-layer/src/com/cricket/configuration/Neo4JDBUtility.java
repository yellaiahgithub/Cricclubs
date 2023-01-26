package com.cricket.configuration;

import java.util.concurrent.TimeUnit;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4JDBUtility  {
	
	private static Driver driver;
	
	private static final String URL = EnviromentProperties.getEnvProperty("com.ccconecct.db.neo4j.url");
	private static final String USER_NAME = EnviromentProperties.getEnvProperty("com.ccconecct.db.neo4j.username");
	private static final String PASSWORD =  EnviromentProperties.getEnvProperty("com.ccconecct.db.neo4j.password");

	public static Driver getDriver() {
		if(driver == null) {
			Config config = Config.builder()
		            .withMaxConnectionLifetime(30, TimeUnit.MINUTES)
		            .withMaxConnectionPoolSize(100)
		            .withConnectionAcquisitionTimeout(2, TimeUnit.SECONDS)
		            .withConnectionLivenessCheckTimeout(300, TimeUnit.SECONDS)
		            .build();
			driver = GraphDatabase.driver(URL, AuthTokens.basic(USER_NAME, PASSWORD),config);
		}
		return driver ;
	}
	
}