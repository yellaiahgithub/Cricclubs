package com.cricket.scheduler;

import java.util.LinkedHashMap;
import java.util.List;

public class SchedulerUtility {

	
	public static String fixtureSelected(SchedulerConfigParams schedulerConfigParams, String fixtureType){
		if(schedulerConfigParams.getFixtureType() == null && "RR".equals(fixtureType)){
			return " selected ";
		}else if(fixtureType.equals(schedulerConfigParams.getFixtureType())){
			return " selected ";
		}
		return "";
	}
	
	public static LinkedHashMap<Integer, String> getDaysMapForFixture(){
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(6, "Friday");
		map.put(7, "Saturday");
		map.put(1, "Sunday");
		map.put(2, "Monday");
		map.put(3, "Tuesday");
		map.put(4, "Wednesday");
		map.put(5, "Thursday");
		return map;
		
	}

	public static LinkedHashMap<String, String> getTimesMapForFixture(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("8:00", "8:00 AM");
		map.put("8:00", "8:00 AM");
        map.put("8:30", "8:30 AM");
        map.put("9:00", "9:00 AM");
        map.put("9:30", "9:30 AM");
        map.put("10:00", "10:00 AM");
        map.put("10:30", "10:30 AM");
        map.put("11:00", "11:00 AM");
        map.put("11:30", "11:30 AM");
        map.put("12:00", "12:00 PM");
        map.put("12:30", "12:30 PM");
        map.put("13:00", "1:00 PM");
        map.put("13:30", "1:30 PM");
        map.put("14:00", "2:00 PM");
        map.put("14:30", "2:30 PM");
        map.put("15:00", "3:00 PM");
        map.put("15:30", "3:30 PM");
        map.put("16:00", "4:00 PM");
        map.put("16:30", "4:30 PM");
        map.put("17:00", "5:00 PM");
        map.put("17:30", "5:30 PM");
        map.put("18:00", "6:00 PM");
        map.put("18:30", "6:30 PM");
        map.put("19:00", "7:00 PM");
        map.put("19:30", "7:30 PM");
        map.put("20:00", "8:00 PM");
        map.put("20:30", "8:30 PM");
        map.put("21:00", "9:00 PM");
        map.put("21:30", "9:30 PM");
        map.put("22:00", "10:00 PM");
        map.put("22:30", "10:30 PM");
        map.put("23:00", "11:00 PM");
        map.put("23:30", "11:30 PM");
        map.put("00:00", "12:00 AM");
        map.put("00:30", "12:30 AM");
        map.put("1:00", "1:00 AM");
        map.put("1:30", "1:30 AM");
        map.put("2:00", "2:00 AM");
        map.put("2:30", "2:30 AM");
        map.put("3:00", "3:00 AM");
        map.put("3:30", "3:30 AM");
        map.put("4:00", "4:00 AM");
        map.put("4:30", "4:30 AM");
        map.put("5:00", "5:00 AM");
        map.put("5:30", "5:30 AM");
        map.put("6:00", "6:00 AM");
        map.put("6:30", "6:30 AM");
        map.put("7:00", "7:00 AM");
        map.put("7:30", "7:30 AM");
		
		return map;
		
	}
	
	public static String daysToPlaySelected(Integer dayNum, List<Integer> daysOnPlaying){
		if((daysOnPlaying == null || daysOnPlaying.isEmpty()) && dayNum == 1){
			return " selected ";
		}else if(daysOnPlaying != null && daysOnPlaying.contains(dayNum)){
			return " selected ";
		}
		return "";
	}
	
	public static String timesToPlaySelected(String time, List<String> matchTimeSelected){
		if((matchTimeSelected == null || matchTimeSelected.isEmpty()) && time == "8:00"){
			return " selected ";
		}else if(matchTimeSelected != null && matchTimeSelected.contains(time)){
			return " selected ";
		}
		return "";
	}
	
	public static boolean isGroundExclutionValid(SchedulerConfigParams schedulerConfigParams){
		if(schedulerConfigParams.getGroundExcluded() != null 
				&& schedulerConfigParams.getGroundExcluded().size()>0 
                && schedulerConfigParams.getGroundExclusionStartDate() != null 
                && schedulerConfigParams.getGroundExclusionEndDate() != null
                && schedulerConfigParams.getGroundExcluded().size() == schedulerConfigParams.getGroundExclusionStartDate().size()
                && schedulerConfigParams.getGroundExclusionStartDate().size() == schedulerConfigParams.getGroundExclusionEndDate().size()){
			
			return true;
		}
		return false;
	}
	
	public static boolean isTeamExclutionValid(SchedulerConfigParams schedulerConfigParams){
		if(schedulerConfigParams.getTeamExcluded() != null 
				&& schedulerConfigParams.getTeamExcluded().size()>0 
                && schedulerConfigParams.getTeamExclusionStartDate() != null 
                && schedulerConfigParams.getTeamExclusionEndDate() != null
                && schedulerConfigParams.getTeamExcluded().size() == schedulerConfigParams.getTeamExclusionStartDate().size()
                && schedulerConfigParams.getTeamExclusionStartDate().size() == schedulerConfigParams.getTeamExclusionEndDate().size()){
			
			return true;
		}
		return false;
	}
	
	public static boolean isSeriesExclutionValid(SchedulerConfigParams schedulerConfigParams){
		if(schedulerConfigParams.getSeriesExclusionStartDate() != null 
				&& schedulerConfigParams.getSeriesExclusionStartDate().size()>0 
                && schedulerConfigParams.getSeriesExclusionEndDate() != null 
                && schedulerConfigParams.getSeriesExclusionEndDate().size()>0               
                && schedulerConfigParams.getSeriesExclusionStartDate().size() == schedulerConfigParams.getSeriesExclusionEndDate().size()){
			
			return true;
		}
		return false;
	}
	
	public static String groundExcludSelected(String id, int groundId){
			if(id != null &&  groundId > 0 && id.equals(String.valueOf(groundId))){
				return " selected ";
		}
		return "";
	}
	
	public static String teamExcludSelected(String id, int groundId){
		if(id != null &&  groundId > 0 && id.equals(String.valueOf(groundId))){
			return " selected ";
	}
	return "";
}
	
}
