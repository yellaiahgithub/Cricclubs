package com.cricket.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cricket.dto.UmpireDto;
import com.cricket.utility.CommonUtility;

public class UmpireFactory {
	
	private static UmpireDAO umpireDao = null;
	
	
	private static UmpireDAO getDaoInstance(){
		if(umpireDao == null){
			umpireDao = new UmpireDAO();
		}
		return umpireDao;
	}
	
	public static int registerUmpire(UmpireDto umpire,int clubId) throws Exception{
		return getDaoInstance().registerUmpire(umpire,clubId);
	}
	
	public static List<UmpireDto> getUmpires(int clubId) throws Exception{
		return getDaoInstance().getUmpires(0,0,clubId);
	}
	public static List<UmpireDto> getAllUmpiresList() throws Exception{
		return getDaoInstance().getAllUmpiresList();
	}

	public static void updateUmpire(UmpireDto umpire,int clubId) throws Exception{
		getDaoInstance().updateUmpire(umpire,clubId);
	}
	public static List<UmpireDto> getAllActiveUmpires(int clubId) throws Exception {
		return getDaoInstance().getUmpires(0,UmpireDto.ACTIVE,clubId);
	}
	public static List<UmpireDto> getAllInactiveUmpires(int clubId) throws Exception {
		return getDaoInstance().getUmpires(0,UmpireDto.INACTIVE,clubId);
	}
	public static List<UmpireDto> getAllNewUmpires(int clubId) throws Exception {
		return getDaoInstance().getUmpires(0,UmpireDto.NEW_UMPIRE,clubId);
	}
	public static Map<String,Integer> getAllScorerNames(int clubId) throws Exception {
		return getDaoInstance().getAllScorerNames(UmpireDto.ACTIVE,clubId);
	}
	public static Map<String,Integer> getScorerNameIdMap(int clubId) throws Exception {
		return getDaoInstance().getScorerNameIdMap(UmpireDto.ACTIVE,clubId);
	}
	public static Map<Integer,Integer> getAllotedPaperScorers(int clubId) throws Exception {
		return getDaoInstance().getAllotedPaperScorers(clubId);
	}
	public static Map<String,Integer> getAllUmpireNames(int clubId) throws Exception {
		return getDaoInstance().getAllUmpireNames(UmpireDto.ACTIVE,clubId,0);
	}
	public static Map<Integer, String> getUmpireIdNameMap(int clubId) throws Exception {
		return getDaoInstance().getUmpireIdNameMap(UmpireDto.ACTIVE,clubId,0);
	}
	public static Map<String,Integer> getUmpireName(int clubId,int umpireId) throws Exception {
		return getDaoInstance().getAllUmpireNames(UmpireDto.ACTIVE,clubId,umpireId);
	}
	//Gives only Active Umpires List
	public static List<UmpireDto> getAllActiveUmpiresList(int clubId) throws Exception {
		List<UmpireDto> umpiresList = getDaoInstance().getUmpires(0,UmpireDto.ACTIVE,clubId);
		List<UmpireDto> umpires = new ArrayList<UmpireDto>();
		if(!umpiresList.isEmpty()){
			for(UmpireDto dto : umpiresList) {
				if(dto != null && !CommonUtility.isNullOrEmpty(dto.getType()) &&  dto.getType().contains("u")) {
					umpires.add(dto);
				}
			}
		}		
		return umpires;
}
	public static Map<String,Integer> getUmpiresForNextWeekFixtures(int clubId) throws Exception {
		Calendar calf = Calendar.getInstance();
		calf.setTime(new Date());
		
		//to Get Next Monday
		
		if(calf.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			calf.add(Calendar.DATE,7);		
		}else {
			while(calf.get(Calendar.DAY_OF_WEEK)!= Calendar.MONDAY) {
				calf.add(Calendar.DATE, 1);			
			}
		}		
			
		Calendar calt = Calendar.getInstance();
		calt.setTime(calf.getTime());		
		calt.add(Calendar.DATE, 6);	
		return getDaoInstance().getUmpiresForNextWeekFixtures(UmpireDto.ACTIVE,CommonUtility.dateToString(calf.getTime()), CommonUtility.dateToString(calt.getTime()),clubId);
	}

	public static UmpireDto getUmpireById(int umpireId,int clubId) throws Exception {

		List<UmpireDto> umpires = getDaoInstance().getUmpires(umpireId,0,clubId);
		if(!umpires.isEmpty()){
			return (UmpireDto) umpires.get(0);
		}
		return null;
	}
	public static List<UmpireDto> getOnlyActiveUmpiresById(int umpireId,int clubId) throws Exception {
		return getDaoInstance().getOnlyActiveUmpiresById(umpireId,UmpireDto.ACTIVE,clubId);		
	}
	public static Map<String, Integer> getUmpireActSchReport(int umpireId, int seriesId, int clubId) throws Exception {
		return getDaoInstance().getUmpireActSchReport(umpireId,seriesId,clubId);
	}
	public static boolean checkUmpireByEmail(String email,int clubId) throws Exception {
		return getDaoInstance().umpireCheckByEmail(email,clubId) > 0;
	}
	public static void updateUmpireStatus(int status,int umpireId, int clubId) throws Exception{
		getDaoInstance().updateUmpireStatus(status,umpireId, clubId);
	}
	public static void deleteUmpire(int umpireId, int clubId, String User) throws Exception {
		getDaoInstance().deleteUmpire(umpireId, clubId, User);
	}
	public static int globalUmpireCheckByEmail(String email) throws Exception {
		return getDaoInstance().umpireCheckByEmail(email,0);
	}
	public static int umpireCheckByEmailCountInClub(String email,int clubId) throws Exception {
		return getDaoInstance().umpireCheckByEmailCount(email,clubId);
	}
	public static void addUmpireToClub(UmpireDto dto, int clubId) throws Exception {
		getDaoInstance().registerExistingUmpire(dto,clubId);
	}
	public static UmpireDto getUmpireByUserId(int umpireUserId,int clubId) throws Exception {

		List<UmpireDto> umpires = getDaoInstance().getUmpiresByUserId(umpireUserId,0,clubId);
		if(!umpires.isEmpty()){
			return (UmpireDto) umpires.get(0);
		}
		return null;
	}
	public static List<UmpireDto> getOnlyUmpires(int clubId) throws Exception {
		return getDaoInstance().getOnlyUmpires(UmpireDto.ACTIVE,clubId);
	}
	
	public static List<Integer> getAllClubIdsForUmpire(int umpireId) throws Exception {
		return getDaoInstance().getAllClubIdsForUmpire(umpireId);
	}
}
