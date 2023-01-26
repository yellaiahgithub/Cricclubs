package com.carroms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.carroms.dto.CarromsDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;
import com.google.gson.Gson;

public class CarromsDao {

	protected int insertCarromsMatchData(int matchId,String carromsData) throws Exception {

		String query= "insert into carroms_overlay(match_id,carroms_data,created_date) values (?,?,NOW())";
		Connection conn = null;
		PreparedStatement st = null;		
		
		try {
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			
			int index = 1;
			if (matchId != 0) {
				st.setInt(index++, matchId);
			}
			if (!CommonUtility.isNullOrEmpty(carromsData)) {
				st.setString(index++, carromsData);
			}						
			st.executeUpdate();			
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}		
		return matchId;
	}

	protected CarromsDto getCarromsMatchData(int matchId) throws Exception {

		String query = "select match_id, carroms_data from carroms_overlay where match_id= ?";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		CarromsDto carromDto = null;
		
		try {
			pst = conn.prepareStatement(query);
			if (matchId > 0) {
				pst.setInt(1, matchId);
			}

			rs = pst.executeQuery();
			carromDto = new CarromsDto();
			while (rs.next()) {
				
				carromDto.setMatchId(rs.getInt(1));
				
				String  carromsData = rs.getString("carroms_data");
				if (!CommonUtility.isNullOrEmpty(carromsData)) {
					
					CarromDataDto dataDto = new Gson().fromJson(carromsData, CarromDataDto.class);
					
					if (dataDto != null) {
						populateCarromDto(carromDto, dataDto);
					}
				}
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return carromDto;
	}

	protected void updateCarromsMatchData(int matchId, String carromData) throws Exception {

		String query = "update carroms_overlay set carroms_data=?, updated_date=NOW() where match_id=?";
		
		Connection conn = null;
		PreparedStatement st = null;
		
		try {			
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);

			st.setString(1, carromData);
			st.setInt(2, matchId);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	private void populateCarromDto(CarromsDto carromDto, CarromDataDto dataDto) {
		
		carromDto.setBoardNumber(dataDto.getBoardNumber());
		carromDto.setSelectGame(dataDto.getSelectGame());
		carromDto.setTeamOneFile(dataDto.getTeamOneFile());
		carromDto.setTeamOneName(dataDto.getTeamOneName());
		carromDto.setTeamOneScore(dataDto.getTeamOneScore());
		carromDto.setTeamOneSet(dataDto.getTeamOneSet());
		carromDto.setTeamTwoFile(dataDto.getTeamTwoFile());
		carromDto.setTeamTwoName(dataDto.getTeamTwoName());
		carromDto.setTeamTwoScore(dataDto.getTeamTwoScore());
		carromDto.setTeamTwoSet(dataDto.getTeamTwoSet());
	}

}
