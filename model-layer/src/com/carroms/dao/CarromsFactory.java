package com.carroms.dao;

import com.carroms.dto.CarromsDto;

public class CarromsFactory {

	public static CarromsDao carromDao = null;

	private static CarromsDao getDaoInstance() {
		if (carromDao == null) {
			carromDao = new CarromsDao();
		}
		return carromDao;
	}

	public static int insertCarromsMatchData(int matchId,String carromData) throws Exception {
		return getDaoInstance().insertCarromsMatchData(matchId,carromData);
	}

	public static void updateCarromsMatchData(int matchId, String carromData) throws Exception {
		getDaoInstance().updateCarromsMatchData(matchId, carromData);
	}

	public static CarromsDto getCarromsMatchData(int matchId) throws Exception {
		return getDaoInstance().getCarromsMatchData(matchId);	
	}
}
