package com.cricket.utility;

import static com.cricket.utility.ApplicationConstants.REDIS_ENDPOINT_KEY;
import static com.cricket.utility.ApplicationConstants.REDIS_PORT_KEY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.configuration.EnviromentProperties;
import com.cricket.mailService.Notifier;
import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCacheManager {
	
	private static JedisPool jedisPool ;
	public static boolean isRedisAvailable = false;
	
	private static final Logger log = LoggerFactory.getLogger(RedisCacheManager.class);
	
	private static Jedis getClient(){
		if(jedisPool != null){
			try {			
				return jedisPool.getNumIdle()>0? jedisPool.getResource():null;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		 return null;
	}
	
	private static Jedis getMandatoryClient(){
		
		if(jedisPool != null){
			try {			
				return jedisPool.getResource();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		 return null;
	}
	
	static {
		try {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxTotal(1000);
		    poolConfig.setMaxIdle(100);
		    poolConfig.setMinIdle(4);
		    poolConfig.setTestOnBorrow(true);
		    poolConfig.setTestOnReturn(true);
		    poolConfig.setTestWhileIdle(true);
		    poolConfig.setMinEvictableIdleTimeMillis(1000);
		    poolConfig.setTimeBetweenEvictionRunsMillis(1000);
		    poolConfig.setNumTestsPerEvictionRun(3);
		    poolConfig.setBlockWhenExhausted(true);
		    String redisEndPoint = CommonUtility.isNullOrEmpty(System.getProperty("RedisPrimaryAddress")) ? EnviromentProperties.getEnvProperty(REDIS_ENDPOINT_KEY):System.getProperty("RedisPrimaryAddress");
		    String redisPort =  CommonUtility.isNullOrEmpty(System.getProperty("RedisPrimaryPort"))?EnviromentProperties.getEnvProperty(REDIS_PORT_KEY):System.getProperty("RedisPrimaryPort");
		    
			if(redisEndPoint != null && redisPort != null && CommonUtility.stringToInt(redisPort) >0){
		        jedisPool = new JedisPool(poolConfig, redisEndPoint,  CommonUtility.stringToInt(redisPort), 700);
		        isRedisAvailable = true;
			}
			//jedisPool = new JedisPool(poolConfig,"localhost", 6379);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static void shutdown(){
		if(jedisPool != null )jedisPool.close();
	}
	
	public static void setDataIntoCache(String key, Object value){
		Jedis client = null;
		try {
			key = key.toUpperCase();
			client = getClient();
			if(client != null){
				Gson gson = new Gson();
				client.set(key, gson.toJson(value));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	public static void setNewArrayIntoCache(String key, String[] array, int expiryTime) throws Exception{
		Jedis client = null;
		try {
			client = getMandatoryClient();
			if(client != null){
				client.del(key);
				client.lpush(key, array);
				client.expire(key, expiryTime);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			handleException(e);
			throw new Exception(e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	public static void setExistingArrayIntoCache(String key, String[] array, int expiryTime) throws Exception{
		Jedis client = null;
		try {
			client = getMandatoryClient();
			if(client != null){
				client.lpush(key, array);
				client.expire(key, expiryTime);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			handleException(e);
			throw new Exception(e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	public static void addValueToExistingArayInCache(String key, String value) throws Exception{
		Jedis client = null;
		try {
			client = getMandatoryClient();
			if(client != null){
				client.lpush(key, value);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			handleException(e);
			throw new Exception(e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	public static List<String> getListOfValuesbyKeyRange(String key, int start, int stop) throws Exception{
		Jedis client = null;
		try {
			client = getMandatoryClient();
			if(client != null){
				return client.lrange(key, start, stop);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			handleException(e);
			throw new Exception(e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return null;
	}
	
	public static List<String> getValuesOfKey(String key) throws Exception{
		Jedis client = null;
		try {
			client = getMandatoryClient();
			if(client != null){
				List<String> values = null;
				String keyType = client.type(key);
				if("string".equalsIgnoreCase(keyType)) {
					values = new ArrayList<String>();
					values.add(client.get(key));
				}else if("list".equalsIgnoreCase(keyType)){
					values = client.lrange(key, 0, -1);
				}
				return values;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			handleException(e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return null;
	}
	
	public static List<String> getAllListOfValuesbyKey(String key) throws Exception{
		Jedis client = null;
		try {
			client = getMandatoryClient();
			if(client != null){
				return client.lrange(key, 0, -1);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return null;
	}
	
	public static boolean isConnected(){
		return (jedisPool != null);
	}
	
	public static boolean isAlive() {
		Jedis client = null;
		try {
			client = getClient();
			String pong = client.ping();
			if("PONG".equalsIgnoreCase(pong)) {
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return false;
	}
	
	public static String getByKey(String key){
		Jedis client = null;
		try {
			key = key.toUpperCase();
			client = getClient();
			if(client != null){
				return client.get(key);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return null;
	}
	
	public static String getFantasyContestToken(String key) throws Exception{
		Jedis client = null;
		try {
			client = getMandatoryClient();
			if(client != null){
				return client.lpop(key);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			handleException(e);
			throw new Exception(e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return null;
	}
	
	public static long getFantasyContestTokenSize(String key) throws Exception{
		Jedis client = null;
		try {
			client = getMandatoryClient();
			if(client != null){
				return client.llen(key);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			handleException(e);
			throw new Exception(e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return 0;
	}
	
	public static Map<String, String> getFromBucket(String key){
		Jedis client = null;
		try {
			key = key.toUpperCase();
			client = getClient();
			if(client != null){
				return client.hgetAll(key);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			/*client.shutdown();
			client = null;*/
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return null;
	}

	public static String getFromMapInBucket(String key, String field){
		Jedis client = null;
		try {
			key = key.toUpperCase();
			field = field.toUpperCase();
			client = getClient();
			if(client != null){
				return client.hget(key, field);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return null;
	}
	
	public static void putIntoBucket(String key, String field, String value){
		Jedis client = null;
		try {
			key = key.toUpperCase();
			field = field.toUpperCase();
			client = getClient();
			if(client != null){
				client.hset(key, field, value);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		
	}
	
	public static void putMergeCode(String mergeCodeKey, String mergeCode, int expiryTime){
		Jedis client = null;
		try {
			client = getClient();
			if(client != null){
				client.set(mergeCodeKey, mergeCode);
				client.expire(mergeCodeKey, expiryTime);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	public static void putKeyValue(String key, String value, int expiryTime){
		Jedis client = null;
		try {
			client = getClient();
			if(client != null){
				client.set(key, value);
				client.expire(key, expiryTime);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	public static void putKeyValueForMap(Map<String, List<String>> map, int expiryTime){
		Jedis client = null;
		try {
			client = getClient();
			if(client != null){
				for(String str : map.keySet()) {
					String[] strArrays = map.get(str).toArray(new String[map.get(str).size()]);
					client.del(str);
					client.lpush(str, strArrays);
					client.expire(str, expiryTime);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	public static void putKeyFantasy(String matchId, String roleMap, int expiryTime){
		Jedis client = null;
		try {
			client = getClient();
			if(client != null){
				client.set(matchId, roleMap);
				client.expire(matchId, expiryTime);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	public static String getValueByKeyFantasy(String key) {
		
		Jedis client = null;
		String value = null;
		try {
			client = getClient();
			if(client != null){
				value = client.get(key);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return value;
	}
	
	public static String getMergeCode(String mergeCodeKey){
		Jedis client = null;
		String mergeCode = null;
		try {
			mergeCodeKey = mergeCodeKey.toUpperCase();
			client = getClient();
			if(client != null){
				mergeCode = client.get(mergeCodeKey);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return mergeCode;
	}
	
	public static String getValueByKey(String key){
		Jedis client = null;
		String mergeCode = null;
		try {
			key = key.toUpperCase();
			client = getClient();
			if(client != null){
				mergeCode = client.get(key);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
		return mergeCode;
	}
	
	public static String getDataFromCache(String key) {
		Jedis client = null;
	try {
		key = key.toUpperCase();
		client = getClient();
		key = key.toUpperCase();
		if(client != null)
		return client.get(key);
		return null;
	} catch (Exception e) {
		log.error(e.getMessage(), e);
		return null;
	}finally{
		if (client != null) {
			client.close();
        }
	}
	}
	public static void updateDataIntoCache(String key, Object value){
		Jedis client = null;
	try {
		key = key.toUpperCase();
		client = getClient();
		if(client != null){
			Gson gson = new Gson();
			client.set(key, gson.toJson(value));
		}
		//client.set(key, CACHE_TIMEOUT, value);
	} catch (Exception e) {
		log.error(e.getMessage(), e);
	}finally{
		if (client != null) {
			client.close();
        }
	}
	}
	
	public static void delete(){


		Jedis client = null;
	try {
		client = getClient();
		if(client != null)
		client.flushAll();
	} catch (Exception e) {
		log.error(e.getMessage(), e);
	}finally{
		if (client != null) {
			client.close();
        }
	}
	
	
	}
	
	public static void deleteFromBucket(String key){

		Jedis client = null;
	try {
		key = key.toUpperCase();
		client = getClient();
		if(client != null)
		client.del(key);
	} catch (Exception e) {
		log.error(e.getMessage(), e);
	}finally{
		if (client != null) {
			client.close();
        }
	}
	
	}
	public static void deleteFieldFromBucket(String key, String field){

		Jedis client = null;
	try {
		if(key != null && field != null){
			key = key.toUpperCase();
			field = field.toUpperCase();
			client = getClient();
			if(client != null)
			 client.hdel(key, field);
		}
	} catch (Exception e) {
		log.error(e.getMessage(), e);
	}finally{
		if (client != null) {
			client.close();
        }
	}
	
	}
	public static void deleteDataFromCache(String key){
			Jedis client = null;
		try {
			key = key.toUpperCase();
			client = getClient();
			if(client != null)
			client.del(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			if (client != null) {
				client.close();
	        }
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Object getDataByKeys(Set<String> keys) throws IOException{
		Jedis client = null;
	try {
		client = getClient();
		if(client != null){
			Map<String, Object> map = new HashMap<String, Object>();
			for(String key : keys){
				key = key.toUpperCase();
				Object value = client.get(key);
				if(value != null){
					map.put(key, value);
				}
			}
			return map;
		}
		return null;
	} catch (Exception e) {
		log.error(e.getMessage(), e);
		return null;
	}finally{
		if (client != null) {
			client.close();
        }
	}
	}
	
	private static void handleException(Exception e) {
		try {
			Notifier.sendEmail("admin@cricclubs.com", "support@cricclubs.com,ln@cricclubs.com", "Exception - Inform to Dev team.",
					"Fantasy Process Cache Error:" + e.getMessage() + "\n" + e.getStackTrace()[0], "");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
