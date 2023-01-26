package com.cricket.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Value;
import org.neo4j.driver.util.Pair;

import com.cricket.ccapi.res.ClubDetailsRes;
import com.cricket.ccapi.res.UserBasicResponse;
import com.cricket.configuration.Neo4JDBUtility;
import com.cricket.dto.ClubDto;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.utility.CommonUtility;

public class LeagueGraphDao {	
	
	public static String createLeaguePage(int clubId) throws Exception {
		
		String message = "";
		
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();
			
			UUID uuid = UUID.randomUUID();
			String pageId = uuid.toString();
			
			ClubDtoLite club = ClubFactory.getLiteClubDetails(clubId);
			
			String query = "CREATE (c:LEAGUE:PAGE) SET c.pageId = $field1, c.clubId = $field2, "
					+ "c.clubName = $field3, c.isAcademy = $field4, c.logoFilePath= $field5, c.shortUrl= $field6 ";
			params.put("field1", pageId);
			params.put("field2", club.getClubId());
			params.put("field3", club.getName());
			params.put("field4", club.getIsAcademy());
			params.put("field5", club.getLogo_file_path());
			params.put("field6", club.getShortURL());
			message = session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					Result result = tx.run(query, params);
					return "League Page Created";
				}
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return message;
	}	
	
	public static String updateLeaguePage(int clubId) throws Exception {
		
		String message = "";
		
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();
			
			ClubDtoLite club = ClubFactory.getLiteClubFromDB(clubId, true);
			
			String query = "MATCH (c:LEAGUE{clubId: $field5 }) SET c.clubName = $field1, c.isAcademy = $field2, c.logoFilePath= $field3, c.shortUrl= $field4";
			params.put("field1", club.getName());
			params.put("field2", club.getIsAcademy());
			params.put("field3", club.getLogo_file_path());
			params.put("field4", club.getShortURL());
			params.put("field5", club.getClubId());
			message = session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					Result result = tx.run(query,params);
					return "League Page Updated";
				}
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return message;
	}	
	
	public static String updateLeaguePage(ClubDto club) throws Exception {
		
		String message = "";
		
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();
			
			String query = "MATCH (c:LEAGUE{clubId: $field4}) SET c.clubName = $field1, c.isAcademy = $field2, c.shortUrl= $field3";
			params.put("field1", club.getName());
			params.put("field2", club.getIsAcademy());
			params.put("field3", club.getShortURL());
			params.put("field4", club.getClubId());
			message = session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					Result result = tx.run(query, params);
					return "League Page Updated";
				}
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return message;
	}
	
	public static String updateGraphLeagueLogoPath(String filePath, int clubId) throws Exception {
		
		String message = "";
		
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();
			
			String query = "MATCH (c:LEAGUE{clubId:$field1}) SET c.logoFilePath= $field2";
			params.put("field1", clubId);
			params.put("field2", filePath);
			message = session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					Result result = tx.run(query, params);
					return "League Page Logo Path Updated";
				}
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return message;
	}
	
	public static String deleteLeaguePage(int clubId) throws Exception {
		
		String message = "";
		
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();
			String query = "MATCH (c:LEAGUE{clubId:$field1}) SET c.clubName = 'deleted_$field1";
			params.put("field1", clubId);
			message = session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					Result result = tx.run(query,params);
					return "League Page Deleted";
				}
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return message;
	}	
	
	public static ClubDetailsRes getLeagueDetails(int clubId, int userId) throws Exception {
		try {
			
			ClubDetailsRes clubRes = null;
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();		
			
			clubRes = session.readTransaction(txn ->{
				String query="";
				 query = "MATCH (c:LEAGUE {clubId:$field1}) return c, size(()-[:FOLLOWED]->(c)) as followersCount ";
				params.put("field1", clubId);
				if(userId>0) {
					query += ",size((:USER{userId: $field2 })-[:FOLLOWED]->(c)) as following";
					params.put("field2", userId);
				}	
				ClubDetailsRes dto = new ClubDetailsRes();
				Result result = txn.run(query,params);
				while(result.hasNext()){					
					List<Pair<String, Value>> values = result.next().fields();
					for (Pair<String,Value> keyValue : values) {
					    if ("c".equals(keyValue.key())) { 					    	
					        Value value = keyValue.value();
					        
					        dto.setClubId(value.get("clubId").asInt());
							dto.setClubName(value.get("clubName").asString());
							dto.setPageId(value.get("pageId").asString());
					    }
					    if ("followersCount".equals(keyValue.key())) { 
					    	  Value value = keyValue.value();
					    	  dto.setFollowersCount(value.asInt());
					    }
					    if(userId>0) {
					    	if ("following".equals(keyValue.key())) { 
						    	Value value = keyValue.value();
						    	if(value.asInt()>0) {
						    		dto.setFollowing(1);
						    	}else {
						    		dto.setFollowing(0);
						    	}					    	 
						    }
					    }
					}
				}
				return dto;
			});			
			return clubRes;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}	
	
	public static boolean isGraphDBConnected() throws Exception {
		
		try {
			
			boolean isConnected = false;
			
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();		
			
			String query = "MATCH (n:LEAGUE{clubId:1}) RETURN n.clubId";
			
			isConnected = session.readTransaction(txn ->{
				boolean isConnected1 = false;
				Result result = txn.run(query);
				while(result.hasNext()){					
					List<Pair<String, Value>> values = result.next().fields();
					for (Pair<String,Value> keyValue : values) {
					    if ("n.clubId".equals(keyValue.key())) { 					    	
					        Value value = keyValue.value();
					        if(value.toString().equalsIgnoreCase("1")) {
					        	isConnected1 = true;
					        }
					    }					    
					}
				}
				return isConnected1;
			});			
			return isConnected;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}	
	
	public static int getClubIdByPageId(String pageId) throws Exception {
		
		try {			
			int clubId = 0;
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();		
			
			String query = "MATCH (n:LEAGUE{pageId:$field1}) RETURN n.clubId";
			params.put("field1",pageId);
			clubId = session.readTransaction(txn ->{
				int clubId1 = 0;
				Result result = txn.run(query,params);
				while(result.hasNext()){					
					List<Pair<String, Value>> values = result.next().fields();
					for (Pair<String,Value> keyValue : values) {
					    if ("n.clubId".equals(keyValue.key())) { 
					        Value value = keyValue.value();
					        clubId1 = value.asInt();
					    } 
					}
				}
				return clubId1;
			});			
			return clubId;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}	
	
	public static String followUnFollowLeaguePage(int userId, String pageIdToFollow, String action) throws Exception {
		
		String txnMessage = "Failure";
		
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();		
			
			txnMessage = session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					String query = "";
					if ("FOLLOWED".equalsIgnoreCase(action)) {
						query = "MATCH (u:USER {userId: $field1}), (p:PAGE {pageId: $field2 }) MERGE (u)-[r:FOLLOWED]->(p) RETURN id(r);";
						params.put("field1", userId);
						params.put("field2", pageIdToFollow);
					} else if ("UNFOLLOWED".equalsIgnoreCase(action)) {
						query = "MATCH (u:USER {userId: $field1})-[r:FOLLOWED]->(p:PAGE {pageId: $field2 }) DELETE r return id(r)";
						params.put("field1", userId);
						params.put("field2", pageIdToFollow);
					}
					Result result = tx.run(query, params);

					String message = "Failed";

					while (result.hasNext()) {

						List<Pair<String, Value>> values = result.next().fields();

						for (Pair<String, Value> keyValue : values) {

							if ("id(r)".equals(keyValue.key())) {
								Value value = keyValue.value();
								if (!CommonUtility.isNullOrEmpty(value.toString())
										&& CommonUtility.stringToInt(value.toString()) > 0) {
									message = "Success";
								}
							}
						}
					}
					return message;
				}
			});			
		
		} catch (Exception e) {
			throw new Exception(e.getMessage());			
		}
		return txnMessage;	
	}
	
	public static String followUnFollowLeagueByClubId(String userId, String clubId, String action) throws Exception {
		
		String txnMessage = "Failure";
		
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();		
			
			txnMessage = session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					String query = "";
					if ("FOLLOWED".equalsIgnoreCase(action)) {
						query = "MATCH (u:USER {userId:$field1}),(l:LEAGUE {clubId:$field2}) MERGE (u)-[r:FOLLOWED]->(l) RETURN id(r);";
						params.put("field1", userId);
						params.put("field2", clubId);
					} else if ("UNFOLLOWED".equalsIgnoreCase(action)) {
						query = "MATCH (u:USER {userId:$field1})-[r:FOLLOWED]->(l:LEAGUE{clubId:$field2}) DELETE r return id(r)";
						params.put("field1", userId);
						params.put("field2", clubId);
					}
					Result result = tx.run(query, params);

					String message = "Failed";

					while (result.hasNext()) {

						List<Pair<String, Value>> values = result.next().fields();

						for (Pair<String, Value> keyValue : values) {

							if ("id(r)".equals(keyValue.key())) {
								Value value = keyValue.value();
								if (!CommonUtility.isNullOrEmpty(value.toString())
										&& CommonUtility.stringToInt(value.toString()) > 0) {
									message = "Success";
								}
							}
						}
					}
					return message;
				}
			});			
		
		} catch (Exception e) {
			throw new Exception(e.getMessage());			
		}
		return txnMessage;	
	}
	
	public static List<UserBasicResponse> getUsersFollowingLeaguePage(int clubId, String pageId, int limit, int skip) throws Exception {
		
		List<UserBasicResponse> connectionsList = null;
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();			
			
			connectionsList = session.readTransaction(txn ->{
				String query = "MATCH (u:USER)-[:FOLLOWED]->(l:LEAGUE{clubId: $field1 }) RETURN u";
				params.put("field1", clubId);
				if(skip>0) {
					query += " skip $field2";
					params.put("field2", skip);
				}
				if(limit>0) {
					query += " limit $field3";
					params.put("field3", limit);
				}
				Result result = txn.run(query,params);
				List<UserBasicResponse> connectionsList1 = new ArrayList<UserBasicResponse>();
				while(result.hasNext()){
					List<Pair<String, Value>> values = result.next().fields();										
					connectionsList1.add(getUserResponse(values));
				}
				return connectionsList1;
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return connectionsList;
	}
	
	private static UserBasicResponse getUserResponse(List<Pair<String, Value>> values) {
		
		UserBasicResponse dto = new UserBasicResponse();
		
		for (Pair<String,Value> keyValue : values) {
			
		    if ("u".equals(keyValue.key())) {  
		    	
		        Value value = keyValue.value();
		        if(!CommonUtility.isNullOrEmptyOrNULL(value.get("userId").toString())) {
		        	  dto.setUserId(value.get("userId").asLong());
		        }
				dto.setFirstName(value.get("firstName").asString());
				dto.setLastName(value.get("lastName").asString());
				dto.setProfileImagePath(value.get("profileImagePath").asString());
									       
		    } 
		}
		return dto;
	}	
	
	public static List<Integer> getGraphDBClubIds(int limit, int skip) throws Exception {

		List<Integer> userIds = null;
		try {
			Map<String, Object> params = new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();

			userIds = session.readTransaction(txn -> {
				String query = "MATCH (l:LEAGUE) RETURN l.clubId ORDER BY l.clubId DESC";
				if (skip > 0) {
					query += " skip $field1";
					params.put("field1", skip);
				}
				if (limit > 0) {
					query += " limit $field2";
					params.put("field2", limit);
				}
				Result result = txn.run(query, params);
				List<Integer> userIds1 = new ArrayList<Integer>();
				while (result.hasNext()) {
					List<Pair<String, Value>> values = result.next().fields();
					userIds1.add(getLeagueClubId(values));
				}
				return userIds1;
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return userIds;
	}
	
	private static Integer getLeagueClubId(List<Pair<String, Value>> values) {
		
		int clubId = 0;
		
		for (Pair<String,Value> keyValue : values) {
			
		    if ("l.clubId".equals(keyValue.key())) { 
		        Value value = keyValue.value();
		        clubId = value.asInt();
		    } 
		}
		return clubId;
	}
	
	public static void main(String[] args) throws Exception {
		
		LeagueGraphDao.createLeaguePage(24392);
	}
}
