package com.cricket.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.utility.CommonUtility;

public class MsgProperties {

	private static final Logger log = LoggerFactory.getLogger(MsgProperties.class);
	private static Properties properties = null;
	
	private static Properties getDaoInstance(){
		if(properties == null){
			properties = new Properties();
			try {
				String fileName = CommonUtility.isNullOrEmpty(System.getProperty("msgProperties"))?"en":System.getProperty("msgProperties");
				fileName = fileName + ".properties";
				InputStream stream = EnviromentProperties.class.getResourceAsStream(fileName);
				properties.load(stream);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				log.error("Issue while reading Msg property file. " + e.getMessage());
			}
		}
		return properties;
	}
	
	public static String getEnvProperty(String key) {
		return getDaoInstance().getProperty(key);
	}

}
