package com.cricket.dao;

import java.util.List;

import com.cricket.dto.UserNotificationsDto;

public class UserNotificationsFactory {
	
	private static UserNotificationsDAO undao = null;
	
	private static UserNotificationsDAO getDaoInstance(){
		if(undao == null){
			undao = new UserNotificationsDAO();
		}
		return undao;
	}
	public static UserNotificationsDto getUserNotifications(int clubId, int userId) throws Exception {
		
		List<UserNotificationsDto> undtos = getDaoInstance().getUserNotifications(clubId,userId,0);
		UserNotificationsDto undto = new UserNotificationsDto();
		if( undto != null && undtos.size() > 0) {
			undto = undtos.get(0);			
		}
		return undto;
	}
	public static int getIsUserWeeklySummary(int userId) throws Exception {
		return getDaoInstance().getIsUserWeeklySummary(userId);				
	}
	public static List<UserNotificationsDto> getAllUserNotifications(int clubId, int userId) throws Exception {
		return getDaoInstance().getUserNotifications(clubId,userId,0);
	}	
	public static void insertUserNotification(UserNotificationsDto undto) throws Exception{
		getDaoInstance().insertUserNotification(undto);
	}
	public static void updateUserNotification(UserNotificationsDto undto) throws Exception{
		getDaoInstance().updateUserNotification(undto);
	}
}