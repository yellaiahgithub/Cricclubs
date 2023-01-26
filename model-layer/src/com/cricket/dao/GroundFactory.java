package com.cricket.dao;

import java.util.List;
import java.util.Map;

import com.cricket.dto.GroundBookingDto;
import com.cricket.dto.GroundDto;

public class GroundFactory {
	
	private static GroundDAO groundDAO = null;
	
	private static GroundDAO getDaoInstance(){
		if(groundDAO == null){
			groundDAO = new GroundDAO();
		}
		return groundDAO;
	}

	
	public static List<GroundDto> getGrounds(int clubId) throws Exception{
		return getDaoInstance().getGrounds(clubId,0,0);
	}

	public static GroundDto getGround(int groundId,int clubId) throws Exception{
		
		
		List<GroundDto> grounds = getDaoInstance().getGrounds(clubId,groundId,0);
		if(grounds != null && !grounds.isEmpty()){
			return (GroundDto)grounds.get(0);
		}
		return null;
	}

	public static void createGround(GroundDto ground, int clubId, int userId) throws Exception {
		getDaoInstance().createGround(ground, clubId, userId);
	}
	
	public static void updateGround(GroundDto ground,int clubId, int userId) throws Exception {
		getDaoInstance().updateGround(ground,clubId, userId);
	}
	
	public static void updateGroundFixtureLocationMap(GroundDto ground,int clubId, int userId) throws Exception {
		getDaoInstance().updateGroundFixtureLocationMap(ground,clubId, userId);
	}
	
	public static void updateGroundImagePaths(String imagePaths, int groundId, int clubId) throws Exception {
		getDaoInstance().updateGroundImagePaths(imagePaths, groundId, clubId);
	}

	public static void deleteGround(int groundId,int clubId) throws Exception {
		getDaoInstance().deleteGround(groundId,clubId);
	}


	public static List<String> getGroundEmailsByClubId(int clubId) throws Exception {
		// TODO Auto-generated method stub
		return getDaoInstance().getGroundEmailsByClubId(clubId);
	}


	public static void saveGroundBooking(int clubId, GroundBookingDto groundBookingDto) throws Exception {
		getDaoInstance().saveGroundBooking(clubId, groundBookingDto);
		
	}

	public static List<GroundBookingDto> getSelectedGrounds(int clubId, int approveStatus) throws Exception {
		return getDaoInstance().getSelectedGrounds(clubId, approveStatus, 0);
	}
	
	public static Map<Integer, String> getGroundIdNameMap(int clubId) throws Exception {
		return getDaoInstance().getGroundIdNameMap(clubId);
	}
	public static GroundBookingDto getGroundBookingInfoById(int clubId, int groundBookingId) throws Exception {
		return getDaoInstance().getGroundBookingInfoById(clubId, groundBookingId);
	}
	
	public static List<GroundBookingDto> getFutureSelectedGrounds(int clubId, int approveStatus, int isFuture) throws Exception {
		return getDaoInstance().getSelectedGrounds(clubId, approveStatus, isFuture);
	}

	public static void deleteGroundBookingById(int clubId, int id) throws Exception{
		getDaoInstance().deleteGroundBookingById(clubId, id);
	}


	public static List<GroundDto> getAvailableGrounds(int clubId) throws Exception {
		return getDaoInstance().getAvailableGrounds(clubId);
	}


	public static boolean validateGroundAvailability(int clubId, int groundId, String startDate, String endDate) {
		return getDaoInstance().validateGroundAvailability(clubId, groundId, startDate, endDate);
	}


	public static void updateGroundBookingStatusById(int clubId, int id, String status)  throws Exception {
		getDaoInstance().updateGroundBookingStatusById(clubId, id, status);
	}
}
