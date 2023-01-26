package com.cricket.dao;

import java.util.List;

import com.cricket.dto.PlayerSeriesPaymentInfoDto;
import com.cricket.utility.CommonUtility;

public class PlayerSeriesPaymentInfoFactory {

	private static PlayerSeriesPaymentInfoDao playerSeriesPaymentInfoDao = null;
	
	private static PlayerSeriesPaymentInfoDao getDaoInstance(){
		if(playerSeriesPaymentInfoDao == null){
			playerSeriesPaymentInfoDao = new PlayerSeriesPaymentInfoDao();
		}
		return playerSeriesPaymentInfoDao;
	}
	
	public static int createPlayerSeriesPaymentInfo(PlayerSeriesPaymentInfoDto playerSeriesPaymentInfoDto) throws Exception{
		return getDaoInstance().createPlayerSeriesPaymentInfo(playerSeriesPaymentInfoDto);
	}
	
	public static PlayerSeriesPaymentInfoDto getTeamSeriesPaymentInfo(int clubId, int playerId, int typeId, String type, String paymentStatus) throws Exception{
		return getDaoInstance().getTeamSeriesPaymentInfo(clubId, playerId, typeId, type, paymentStatus);
	}
	
	public static PlayerSeriesPaymentInfoDto getPlayerSeriesPaymentInfo(int clubId, int playerId, int typeId, String type) throws Exception{
		List<PlayerSeriesPaymentInfoDto> playerSerPayInfos = getDaoInstance().getPlayerSeriesPaymentInfo(clubId, playerId, typeId, type);
		if(!CommonUtility.isListNullEmpty(playerSerPayInfos) && playerSerPayInfos.size() > 0) {
			return playerSerPayInfos.get(0);
		}
		return null;
	}
	
	public static List<PlayerSeriesPaymentInfoDto> getPlayerSeriesPaymentInfos(int clubId, int playerId, int typeId, String type) throws Exception{
		return getDaoInstance().getPlayerSeriesPaymentInfo(clubId, playerId, typeId, type);
	}
	
	public static List<PlayerSeriesPaymentInfoDto> getPlayerSeriesPaymentInfosByTypeId(int clubId, int leagueId, String type) throws Exception{
		return getDaoInstance().getPlayerSeriesPaymentInfosByTypeId(clubId, leagueId, type);
	}
	
	public static int updatePaymentStatus(int clubid, int id, String txnId, String playerId, String paymentStatus,
			String type) throws Exception {
		return getDaoInstance().updatePaymentStatus(clubid, id, playerId, paymentStatus, type, txnId);
		
	}
}
