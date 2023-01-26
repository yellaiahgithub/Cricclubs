package com.cricket.dao;

import java.util.List;

import com.cricket.dto.FacebookPageDto;

public class FacebookFactory {
	
	private static FacebookDAO facebookDao = null;
	
	private static FacebookDAO getDaoInstance(){
		if(facebookDao == null){
			facebookDao = new FacebookDAO();
		}
		return facebookDao;
	}

	
	public static String getUserAccessToken() throws Exception{
		return getDaoInstance().getUserAccessToken();
	}
	
	public static List<FacebookPageDto> getFacebookPagesOfClub(int clubId) throws Exception{
		return getDaoInstance().getFacebookPagesOfClub(clubId);
	}

	public static String getFacebookPostId(int id, int matchID, int clubId) throws Exception {
		return getDaoInstance().getFacebookPostId(id,matchID,clubId);
	}


	public static void addMatchPost(String postId, int id, int matchID,
			int clubId) throws Exception {
		getDaoInstance().addMatchPost(postId,id,matchID,clubId);
	}
}
