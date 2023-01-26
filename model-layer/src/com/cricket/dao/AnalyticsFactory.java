package com.cricket.dao;

import java.util.List;

import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.utility.batch.AnalyticsDataRow;
import com.cricket.utility.batch.LoadAnalytics;

public class AnalyticsFactory {
	
	private static AnalyticsDAO analyticsDao = null;
	
	private static AnalyticsDAO getDaoInstance(){
		if(analyticsDao == null){
			analyticsDao = new AnalyticsDAO();
		}
		return analyticsDao;
	}
	
	public static void insertAnalyticsData(List<AnalyticsDataRow> rows) throws Exception{
		getDaoInstance().insertAnalytics(rows);
	}
	
	public static void deleteAnalyticsData(String date) throws Exception{
		getDaoInstance().deleteAnalytics(date);
	}

	public static List<AnalyticsDataRow> getData(String startDate, String endDate, String groupBy) throws Exception {
		return getDaoInstance().getAnalyticsData(startDate, endDate,groupBy);
	}
	
	public static List<String> getDataColumns(String startDate, String endDate, String groupBy) throws Exception {
		return getDaoInstance().getAnalyticsDataColumns(startDate, endDate,groupBy);
	}

	public static String getClubName(int clubId) throws Exception{
		if(clubId == LoadAnalytics.MOBILE){
			return "Mobile App";
		}else if(clubId == LoadAnalytics.HOME_PAGE){
			return "CricClubs Home";
		}else if(clubId == 0){
			return "Unknown/Inactive Clubs";
		}else{
			ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
			if(club != null){
				return "<a target='_new' href='/"+club.getShortURL()+"'>"+club.getName()+"</a>";
			}
			return ""+clubId;
		}
	}

}
