package com.cricket.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.util.Pair;

import com.cricket.ccapi.res.ClubDetailsRes;
import com.cricket.ccapi.res.GraphSerachLeagueResult;
import com.cricket.ccapi.res.GraphSerachPostResult;
import com.cricket.ccapi.res.GraphSerachUserResult;
import com.cricket.ccapi.res.GraphUserSuggestionResponse;
import com.cricket.ccapi.res.PhoneContactUserResponse;
import com.cricket.configuration.Neo4JDBUtility;
import com.cricket.dto.ClubDto;
import com.cricket.utility.CommonUtility;

public class GraphSearchDao {	

	public static List<GraphSerachUserResult> getUserSearchResults(String clubIdsStr, int userId, String searchText, int skip, int limit) throws Exception {
		
		List<GraphSerachUserResult> usersList = null;
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();			
			
			usersList = session.readTransaction(txn -> {
				String query = "MATCH (u:USER) ";
				if (!CommonUtility.isNullOrEmpty(clubIdsStr)) {
					query += "-[r:FOLLOWED]->(l:LEAGUE) ";
				}
				query += " WHERE u.isActive = 1 and u.fullName CONTAINS ($field1) ";
				params.put("field1", searchText.toLowerCase());
				if (!CommonUtility.isNullOrEmpty(clubIdsStr)) {
					query += " and l.clubId IN [$field2] ";
					params.put("field2", clubIdsStr);
				}
				query += " RETURN u, size(()-[:CONNECTED]-(u)) as connectedCount ";

				if (userId > 0) {
					query += " ,size((:USER{userId:$field3})-[:FOLLOWED]->(u)) as isFollowing, "
							+ " size((:USER{userId:$field3})-[:CONNECTREQUESTED]->(u)) as isConnectRequested, "
							+ " size((:USER{userId:$field3})-[:CONNECTED]-(u)) as isConnected ";
					params.put("field3", userId);
				}
				if (skip > 0) {
					query += " skip $field4";
					params.put("field4", skip);
				}
				if (limit > 0) {
					query += " limit $field5";
					params.put("field5", limit);
				}
				Result result = txn.run(query,params);
				List<GraphSerachUserResult> usersList1 = new ArrayList<GraphSerachUserResult>();
				while (result.hasNext()) {
					List<Pair<String, Value>> values = result.next().fields();
					usersList1.add(populateUserResult(values));
				}
				return usersList1;
			});
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return usersList;
	}
	
	public static List<GraphSerachLeagueResult> getLeagueSearchResults(String clubIdsStr, int userId, String searchText, int skip, int limit) throws Exception {
		
		List<GraphSerachLeagueResult> leaguesList = null;
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();			
			
			leaguesList = session.readTransaction(txn -> {
				String query = "MATCH (l:PAGE) WHERE ( toLower(l.clubName) CONTAINS toLower($field1) or toLower(l.shortUrl) CONTAINS toLower($field1)) "
						+ " and l.isAcademy = 0";
				params.put("field1", searchText);
				if (!CommonUtility.isNullOrEmpty(clubIdsStr)) {
					query += " and l.clubId in [$field2] ";
					params.put("field2", clubIdsStr);
				}
				query += " RETURN l, size(()-[:FOLLOWED]->(l)) as followersCount  ";

				if (userId > 0) {
					query += " ,size((:USER{userId: $field3})-[:FOLLOWED]->(l)) as isFollowing ";
					params.put("field3", userId);
				}
				if (skip > 0) {
					query += " skip ";
					params.put("field4", skip);
				}
				if (limit > 0) {
					query += " limit $field5";
					params.put("field5", limit);
				}
				Result result = txn.run(query,params);
				List<GraphSerachLeagueResult> leaguesList1 = new ArrayList<GraphSerachLeagueResult>();
				while (result.hasNext()) {
					GraphSerachLeagueResult dto = new GraphSerachLeagueResult();
					List<Pair<String, Value>> values = result.next().fields();
					for (Pair<String, Value> keyValue : values) {
						if ("l".equals(keyValue.key())) {

							Value value = keyValue.value();

							dto.setClubId(value.get("clubId").asInt());
							dto.setClubName(value.get("clubName").asString());
							dto.setPageId(value.get("pageId").asString());
							try {
								ClubDto club = ClubFactory.getClub(dto.getClubId());
								dto.setLocation(club.getCity() + ", " + club.getState() + ", " + club.getCountry());
								dto.setSport(club.getSportName(club.getSportId()));
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (!CommonUtility.isNullOrEmptyOrNULL(value.get("logoFilePath").asString())) {
								dto.setLogoFilePath(value.get("logoFilePath").asString());
							}
						}
						if ("followersCount".equals(keyValue.key())) {
							Value value = keyValue.value();
							dto.setFollowersCount(value.asInt());
						}
						if ("isFollowing".equals(keyValue.key())) {
							Value value = keyValue.value();
							dto.setIsFollowing(value.asInt());
						}
					}
					leaguesList1.add(dto);
				}
				return leaguesList1;
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return leaguesList;
	}
	
	public static List<GraphSerachLeagueResult> getAcademySearchResults(String clubIdsStr, int userId, String searchText, int skip, int limit) throws Exception {
		
		List<GraphSerachLeagueResult> leaguesList = null;
		try {
			Map<String, Object> params=new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();			
			
			leaguesList = session.readTransaction(txn -> {
				String query = "MATCH (l:PAGE) WHERE toLower(l.clubName) CONTAINS toLower($field1) and l.isAcademy = 1 ";
				params.put("field1", searchText);
				if (!CommonUtility.isNullOrEmpty(clubIdsStr)) {
					query += " and l.clubId in [$field2] ";
					params.put("field2", clubIdsStr);
				}
				query += " RETURN l, size(()-[:FOLLOWED]->(l)) as followersCount ";
				if (userId > 0) {
					query += " ,size((:USER{userId:$field3})-[:FOLLOWED]->(l)) as isFollowing ";
					params.put("field3", userId);
				}
				if (skip > 0) {
					query += " skip $skip";
					params.put("field4", skip);
				}
				if (limit > 0) {
					query += " limit $field5";
					params.put("field5", limit);
				}
				Result result = txn.run(query, params);
				List<GraphSerachLeagueResult> leaguesList1 = new ArrayList<GraphSerachLeagueResult>();
				while (result.hasNext()) {
					GraphSerachLeagueResult dto = new GraphSerachLeagueResult();
					List<Pair<String, Value>> values = result.next().fields();
					for (Pair<String, Value> keyValue : values) {
						if ("l".equals(keyValue.key())) {

							Value value = keyValue.value();

							dto.setClubId(value.get("clubId").asInt());
							dto.setClubName(value.get("clubName").asString());
							dto.setPageId(value.get("pageId").asString());
							try {
								ClubDto club = ClubFactory.getClub(dto.getClubId());
								dto.setLocation(club.getCity() + ", " + club.getState() + ", " + club.getCountry());
							} catch (Exception e) {
								e.printStackTrace();
							}
							dto.setLogoFilePath(value.get("logoFilePath").asString());
						}
						if ("followersCount".equals(keyValue.key())) {
							Value value = keyValue.value();
							dto.setFollowersCount(value.asInt());
						}
						if ("isFollowing".equals(keyValue.key())) {
							Value value = keyValue.value();
							dto.setIsFollowing(value.asInt());
						}
					}
					leaguesList1.add(dto);
				}
				return leaguesList1;
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return leaguesList;
	}

	public static List<GraphSerachPostResult> getPostSearchResults(String searchText, int skip, int limit)
			throws Exception {

		List<GraphSerachPostResult> postsList = null;
		try {
			Map<String, Object> params = new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();
			postsList = session.readTransaction(txn -> {

				String query = "MATCH (u:USER)-[:POSTED]-(p:POST) WHERE toLower(p.content) CONTAINS toLower($field1) "
						+ " RETURN p, u, size(()-[:LIKED]->(p)) as likedCount, size(()-[:DISLIKED]->(p)) as disLikedCount, "
						+ " size(()-[:BELONGS]->(p)) as commentsCount, size(()-[:SHARED]->(p)) as sharedCount "
						+ " UNION "
						+ " MATCH (u:LEAGUE)-[:POSTED]-(p:POST) WHERE toLower(p.content) CONTAINS toLower($field1) "
						+ " RETURN p, u, size(()-[:LIKED]->(p)) as likedCount, size(()-[:DISLIKED]->(p)) as disLikedCount, "
						+ " size(()-[:BELONGS]->(p)) as commentsCount, size(()-[:SHARED]->(p)) as sharedCount ";
				params.put("field1", searchText);
				if (skip > 0) {
					query += " skip $field2";
					params.put("field2", skip);
				}
				if (limit > 0) {
					query += " limit $field3";
					params.put("field3", limit);
				}
				Result result = txn.run(query,params);
				List<GraphSerachPostResult> postsList1 = new ArrayList<GraphSerachPostResult>();
				while (result.hasNext()) {
					List<Pair<String, Value>> values = result.next().fields();
					postsList1.add(getPostDetails(values));
				}
				return postsList1;
			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return postsList;
	}
	
	public static List<PhoneContactUserResponse> getUsersByPhoneContacts(int userId, List<String> phones, int skip,
			int limit) throws Exception {

		List<PhoneContactUserResponse> usersList = null;
		try {
			Map<String, Object> params = new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();

			usersList = session.readTransaction(txn -> {
				String query = "MATCH (u:USER) ";
				query += " WHERE u.phone in [";
				for (String str : phones) {
					query += "'" + str + "',";
				}
				query = query.substring(0, query.length() - 1);
				query += "] RETURN u, size((:USER{userId:$field1})-[:FOLLOWED]->(u)) as isFollowing ";
				params.put("field1", userId);
				if (skip > 0) {
					query += " skip $field2";
					params.put("field2", skip);
				}
				if (limit > 0) {
					query += " limit $field3";
					params.put("field3", limit);
				}
				Result result = txn.run(query, params);
				List<PhoneContactUserResponse> usersList1 = new ArrayList<PhoneContactUserResponse>();
				while (result.hasNext()) {
					List<Pair<String, Value>> values = result.next().fields();
					usersList1.add(populateUserForPhoneContact(values));
				}
				return usersList1;
			});

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return usersList;
	}
	
	public static List<GraphUserSuggestionResponse> getUserSuggestionsFromTeam(int playerId, 
			List<Integer> playerIds, int limit) throws Exception {

		List<GraphUserSuggestionResponse> usersList = null;
		try {
			Map<String, Object> params = new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();

			usersList = session.readTransaction(txn -> {
				String query = "MATCH (u:USER) where u.playerId in [";
				for (Integer pid : playerIds) {
					query += pid + ",";
				}
				query = query.substring(0, query.length() - 1);
				query += " ] RETURN u, size((:USER{playerId: $field1})-[:FOLLOWED]->(u)) as isFollowing,"
						+ " size((:USER)-[:FOLLOWED]->(u)) as followersCount ";
				params.put("field1", playerId);
				if (limit > 0) {
					query += " limit $field2";
					params.put("field2", limit);
				}
				Result result = txn.run(query,params);
				List<GraphUserSuggestionResponse> usersList1 = new ArrayList<GraphUserSuggestionResponse>();
				while (result.hasNext()) {
					List<Pair<String, Value>> values = result.next().fields();
					usersList1.add(populateUserSuggestionResult(values));
				}
				return usersList1;
			});

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return usersList;
	}
	
	private static GraphSerachUserResult populateUserResult(List<Pair<String, Value>> values) {
		
		GraphSerachUserResult dto = new GraphSerachUserResult();
		
		for (Pair<String,Value> keyValue : values) {
			
		    if ("u".equals(keyValue.key())) {  
		    	
		        Value value = keyValue.value();
		        if(!CommonUtility.isNullOrEmptyOrNULL(value.get("userId").toString())) {
		          dto.setUserId(value.get("userId").asLong());
		        }
				dto.setFirstName(value.get("firstName").asString());
				dto.setLastName(value.get("lastName").asString());
				dto.setProfilePicPath(value.get("profileImagePath").asString());
				String email = value.get("email").asString();
				String phone = value.get("phone").asString();
				if(CommonUtility.isNullOrEmpty(email) && CommonUtility.isNullOrEmpty(phone)) {
					dto.setClaimStatus("notVerified");
					
				}else {
					dto.setClaimStatus("Verified");
				}
				dto.setLocation("");
		    } 
		    if ("isFollowing".equals(keyValue.key())) { 
		    	Value value = keyValue.value();
			    dto.setIsFollowing(value.toString());
		    } else if ("isConnectRequested".equals(keyValue.key())) { 
		    	 Value value = keyValue.value();
		    	 dto.setIsConnectRequested(value.toString());
		    } else if ("isConnected".equals(keyValue.key())) { 
		    	 Value value = keyValue.value();
		    	 dto.setIsConnected(value.toString());
		    } else if ("connectedCount".equals(keyValue.key())) { 
		    	 Value value = keyValue.value();
		    	 dto.setConnectedCount(value.toString());
		    }
		}
		return dto;
	}
	
	private static GraphUserSuggestionResponse populateUserSuggestionResult(List<Pair<String, Value>> values) {
		
		GraphUserSuggestionResponse dto = new GraphUserSuggestionResponse();
		
		for (Pair<String,Value> keyValue : values) {
			
		    if ("u".equals(keyValue.key())) {  
		    	
		        Value value = keyValue.value();
		        if(!CommonUtility.isNullOrEmptyOrNULL(value.get("userId").toString())) {
		          dto.setUserId(value.get("userId").asLong());
		        }
				dto.setFirstName(value.get("firstName").asString());
				dto.setLastName(value.get("lastName").asString());
				dto.setProfilePicPath(value.get("profileImagePath").asString());
				dto.setLocation("");
				String email = value.get("email").asString();
				String phone = value.get("phone").asString();
				if(CommonUtility.isNullOrEmpty(email) && CommonUtility.isNullOrEmpty(phone)) {
					dto.setClaimStatus("notVerified");					
				}else {
					dto.setClaimStatus("Verified");
				}
		    } else if ("followersCount".equals(keyValue.key())) { 
		    	 Value value = keyValue.value();
		    	 dto.setFollowersCount(value.toString());
		    } else if("isFollowing".equals(keyValue.key())) {
		    	Value value = keyValue.value();
		    	 dto.setIsFollowing(value.toString());
		    }
		}
		return dto;
	}	
	
	private static PhoneContactUserResponse populateUserForPhoneContact(List<Pair<String, Value>> values) {
		
		PhoneContactUserResponse dto = new PhoneContactUserResponse();
		
		for (Pair<String,Value> keyValue : values) {
			
		    if ("u".equals(keyValue.key())) {  
		    	
		        Value value = keyValue.value();
		        if(!CommonUtility.isNullOrEmptyOrNULL(value.get("userId").toString())) {
		          dto.setUserId(value.get("userId").asLong());
		        }
				dto.setFirstName(value.get("firstName").asString());
				dto.setLastName(value.get("lastName").asString());
				dto.setProfilePicPath(value.get("profileImagePath").asString());
				dto.setPhoneNumber(value.get("phone").asString());
				dto.setLocation("");
		    } 
		    if ("isFollowing".equals(keyValue.key())) { 
		    	Value value = keyValue.value();
			    dto.setIsFollowing(value.toString());
		    } 
		}
		return dto;
	}	
	
	public static ClubDetailsRes getLeagueDetails(int clubId, int userId) throws Exception {
		try {
			
			ClubDetailsRes clubRes = null;
			Map<String, Object> params = new HashMap<>();
			Driver driver = Neo4JDBUtility.getDriver();
			Session session = driver.session();		
			
			String query = "MATCH (c:LEAGUE {clubId: $field1}) return c, size(()-[:FOLLOWED]->(c)) as followersCount, "
					+ "size((:USER{userId: &field2})-[:FOLLOWED]->(c)) as following";
			params.put("field1", clubId);
			params.put("field2", userId);
			clubRes = session.readTransaction(txn ->{
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
				return dto;
			});
			
			ClubDto club = ClubFactory.getClub(clubId);
			
			clubRes.setLogoFilePath(club.getLogo_file_path());
			clubRes.setLocation(club.getAddress());
			clubRes.setCurrentSeries(club.getCurrentLeagueName());	
			clubRes.setAboutClub(club.getAbout());
			if(CommonUtility.isNullOrEmpty(club.getBackGroundImagePath())) {
				clubRes.setBackGroundImagePath("/documentsRep/ccapi/media/ccconnect_club_default_back_ground_image.png");
			}else {
				clubRes.setBackGroundImagePath(club.getBackGroundImagePath());
			}
			
			return clubRes;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}	
	
	private static GraphSerachPostResult getPostDetails(List<Pair<String, Value>> values) {
		
		GraphSerachPostResult dto = new GraphSerachPostResult();
		
		for (Pair<String,Value> keyValue : values) {
		    if ("p".equals(keyValue.key())) {  
		        Value value = keyValue.value();
		        populatePostDto(dto, value);					       
		    } else if ("u".equals(keyValue.key())) {  
		    	  Value value = keyValue.value();
		    	  populateUserDetailsForPost(dto, value);
		    }else if ("likedCount".equals(keyValue.key())) {  
		    	  Value value = keyValue.value();
		    	  dto.setLikedCount(value.toString());
		    } else if ("disLikedCount".equals(keyValue.key())) {  
		    	  Value value = keyValue.value();
		    	  dto.setDisLikedCount(value.toString());
		    } else if ("commentsCount".equals(keyValue.key())) {  
		    	  Value value = keyValue.value();
		    	  dto.setCommentsCount(value.toString());
		    } else if ("sharedCount".equals(keyValue.key())) {  
		    	  Value value = keyValue.value();
		    	  dto.setSharedCount(value.toString());
		    }
		}
		return dto;
	}
	
	private static void populatePostDto(GraphSerachPostResult dto, Value value) {
		
		dto.setPostId(value.get("postId").asString());
		dto.setTitle(value.get("title").asString());
		dto.setImageUrl(value.get("imageUrl").asString());
		dto.setVideoUrl(value.get("videoUrl").asString());
		dto.setContent(value.get("content").asString());
		dto.setCreatedTime(value.get("createdTime").toString().replaceAll("\"", ""));
		try {
			if(value.get("hashTags") != null) {
				List<Object> hashTags = value.get("hashTags").asList();		
				dto.setHashTags(hashTags);
			}			
		}catch(Exception e) {
			
		}
	}
	private static void populateUserDetailsForPost(GraphSerachPostResult dto, Value value) {
		
		if(!CommonUtility.isNullOrEmptyOrNULL(value.get("userId").toString())) {
			dto.setPostUserId(value.get("userId").asInt());
			dto.setPostUserName(value.get("firstName").asString()+" "+value.get("lastName").asString());
			dto.setPostUserProfilePicPath(value.get("profileImagePath").asString());
		}else if(!CommonUtility.isNullOrEmptyOrNULL(value.get("clubId").toString())) {
			dto.setPostLeagueId(value.get("clubId").asInt());
			dto.setPostLeaguePageId(value.get("pageId").asString());
			dto.setPostLeagueName(value.get("clubName").asString());
			dto.setPostLeagueLogoFilePath(value.get("logoFilePath").asString());
		}		
	}
	
}
