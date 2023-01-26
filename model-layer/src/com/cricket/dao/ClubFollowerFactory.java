package com.cricket.dao;

import java.util.List;

public class ClubFollowerFactory {

	private static ClubFollowerDAO clubFollowerDAO = null;

	private static ClubFollowerDAO getDaoInstance() {
		if (clubFollowerDAO == null) {
			clubFollowerDAO = new ClubFollowerDAO();
		}
		return clubFollowerDAO;
	}

	public static List<Integer> getUserFollowingClubs(int userId) throws Exception {
		return getDaoInstance().getUserFollowingClubs(userId);
	}
	
	public static void addFollowingRelation(int clubId, int userId) throws Exception {
		getDaoInstance().addFollowingRelation(clubId, userId);
	}
	
	public static void deleteFollowingRelation(int clubId, int userId) throws Exception {
		getDaoInstance().deleteFollowingRelation(clubId, userId);
	}

}
