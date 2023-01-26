package com.cricket.dao;

import java.util.List;

import com.cricket.dto.ClaimProfileDto;

public class ClaimProfileFactory {
	
	private static ClaimProfileDAO claimProfileDao = null;
	
	private static ClaimProfileDAO getDaoInstance(){
		if(claimProfileDao == null){
			claimProfileDao = new ClaimProfileDAO();
		}
		return claimProfileDao;
	}

	
	public static ClaimProfileDto getInProgressClaimForPlayer(int playerId,int clubId) throws Exception{
		List<ClaimProfileDto> claims = getDaoInstance().getProfileClaims(clubId, playerId, 1);
		if(claims.isEmpty()){
			return null;
		}
		return claims.get(0);
	}

	public static List<ClaimProfileDto> getInProgressClaims(int clubId) throws Exception{
		return getDaoInstance().getProfileClaims(clubId, 0, 1);
	}

	public static List<ClaimProfileDto> getClaimsHistory(int clubId) throws Exception{
		return getDaoInstance().getProfileClaims(clubId,0,0);
	}

	public static void updateClaim(ClaimProfileDto dto,int clubId) throws Exception{
		getDaoInstance().updateClaim(dto, clubId);
	}

	public static void addClaim(ClaimProfileDto dto,int clubId) throws Exception{
		getDaoInstance().insertClaim(dto, clubId);
	}

}
