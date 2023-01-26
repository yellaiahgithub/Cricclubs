/*
 * Created on Mar 13, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.utility;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ApplicationConstants {
	public static String TEAM = "team";
	public static String SELECTED_PLAYERS = "selectedPlayers";
	public static String AVAILABLE_PLAYERS = "availablePlayers";
	public static String MAPPINGS_FILE = "URLMappings.properties";
	public static String USER = "user";
	public static String PLAYER = "player";
	public static String MESSAGE = "displayMessage";
	public static String TARGET = "target";
	public static String COMMENT_TYPE_HOME = "home";
	public static String COMMENT_TYPE_ARTICLE = "article";
	public static String COMMENT_TYPE_NEWS = "news";
	public static String CLUB_ID = "clubId";
	public static String AJAX_SUCCESS = "success";
	public static String OG_IMAGE = "ogImage";
	public static String OG_TITLE = "ogTitle";
	public static String OG_DESCRIPTION = "ogDescription";
	public static String OG_TYPE = "ogType";
	public static String OG_KEYWORDS = "ogKeywords";
	
	public static String CACHE_CLUB = "CLUBCH_";
	public static String CACHE_SHORT_URL = "CLSRTURL_";
	public static String CACHED_CLUBS = "CLKEYS";
	
	public static String BY_TOKEN = "BY_TOKEN";
	public static String BY_UUID = "BY_UUID";
	public static String BY_DEVICE_SERIAL = "BY_DEVICE_SERIAL";
	public static String BY_USER_ID = "BY_USER_ID";
	public static String BY_USER_NAME = "BY_USER_NAME";
	public static String BY_TOKEN_PREVIOUS = "BY_TOKEN_PREVIOUS";
	public static String BY_DEVICE_TYPE = "BY_DEVICE_TYPE";
	public static double G_50_VALUE = 248.8D;
	
	// Type of Audit and Records used in audit table.
	public static final String RECORDS_TYPE_MATCH = "MATCH";
	public static final String RECORDS_TYPE_DELETE_SCORE_CARD = "DELETE_SCORE_CARD";
	public static final String RECORDS_TYPE_PLAYER_INTERNAL_CLUB = "PLAYER_INTERNAL_CLUB";
	
	public static final String AUDIT_TYPE_INSERT = "INSERT";
	public static final String AUDIT_TYPE_INSERT_MULTIPLE = "INSERT_MULTIPLE";
	public static final String AUDIT_TYPE_DELETE = "DELETE";
	public static final String AUDIT_TYPE_UPDATE = "UPDATE";
	// www
	public static final String WEB_ROOT_URL_KEY = "cricclubs.www.root.url";
	// API
	public static final String API_ROOT_URL_KEY = "cricclubs.api.root.url";
	//Payments API
	public static final String PAY_ROOT_URL_KEY = "cricclubs.pay.root.url";
	// AWS Redis
	public static final String REDIS_ENDPOINT_KEY = "cricclubs.awsredis.endpoint";
	public static final String REDIS_PORT_KEY = "cricclubs.awsredis.port";
	// Database
	public static final String DB_HOST_KEY = "cricclubs.db.host";
	public static final String DB_HOST_KEY_READ = "cricclubs.db.host.read";
	public static final String DB_HOST_KEY_CCFANTASY = "ccfantasy.db.host";
	public static final String DB_HOST_KEY_READ_CCFANTASY = "ccfantasy.db.host.read";
	public static final String DB_USER_KEY = "cricclubs.db.user";
	public static final String DB_PASSWORD_KEY = "cricclubs.db.password";
	public static final String FANTASY_DB_USER_KEY = "ccfantasy.db.user";
	public static final String FANTASY_DB_PASSWORD_KEY = "ccfantasy.db.password";
	public static final String DB_POOL_KEY = "cricclubs.db.pool";
	public static final String DB_SQL_LOG_KEY = "cricclubs.db.sqllog";
	public static final String DB_LOG_EXCEPTION_KEY = "cricclubs.db.logexception";
	public static final String DB_POOL_TYPE_KEY = "cricclubs.db.pool.type";
	public static final String DB_TIMEZONE_KEY = "cricclubs.db.timezone";
	
	// AWS SMS
	public static final String AWS_ACCESS_KEY = "cricclubs.aws.sms.accesskey";
	public static final String AWS_SECRET_KEY = "cricclubs.aws.sms.secretkey";
	public static final String AWS_SENDER_ID_KEY = "cricclubs.aws.sms.senderid";
	
	// AWS S3 Bucket
	public static final String AWS_S3_BUCKET = "cricclubs.s3.bucket";
	
	
	// Path for Docs
	public static final String DOC_REPO_PATH_KEY = "cricclubs.docs.path";
	public static final String DOC_REP_PATH = "cricclubs.docrep.path";
	public static final String GOOGLE_DATA_SECURITY_YOUTUBE_PATH = "cricclubs.google.security.key.path";
	// CDN Server URL
	public static final String CDN_SERVER_PATH_KEY = "cricclubs.cdn.server";
	
	
	// Payment Releated Constant
	public static final String SER_PLR_REG = "SER_PLR_REG";
	public static final String SER_TEAM_REG = "SER_TEAM_REG";
	
}
