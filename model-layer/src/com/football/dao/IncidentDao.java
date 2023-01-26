package com.football.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dao.ClubFactory;
import com.cricket.dto.ClubDto;
import com.cricket.dto.GoalIncidentDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;
import com.football.dto.IncidentDto;
import com.football.dto.PlayerMatchDtoFB;

public class IncidentDao {
	
	protected String SELECT_QUERY = "SELECT id,match_id,mins_of_game,additional_mins,incident_type,"
			+ "player1_id,player2_id,team1_goals,team2_goals,comment,client_id,time_saved,created_time FROM incidents  ";
	
	
	protected List<IncidentDto> getIncidentsForFootBallMatch(String matchIdsStr, long clientId, 
			int incidentId, String incidentType, int clubId, boolean isScoring) throws Exception {
		
		List<IncidentDto> dtoList = new ArrayList<IncidentDto>();
		
		String query = SELECT_QUERY + " WHERE 1=1 ";

		if(!CommonUtility.isNullOrEmpty(matchIdsStr)) {
			query += " and match_id in ("+matchIdsStr+") "; 
		}
		if(!CommonUtility.isNullOrEmpty(incidentType)) {
			query += " and incident_type like ('%?%') "; 
		}
		if(clientId>0) {
			query += " and client_id =? "; 
		}
		if(incidentId>0) {
			query += " and id =? "; 
		}
		query += " order by id ";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if(isScoring) {
				conn = DButility.getConnection(clubId, true);
			}else {
				conn = DButility.getReadConnection(clubId, true);
			}
			st = conn.prepareStatement(query);
			
			int index = 1;
			if(!CommonUtility.isNullOrEmpty(incidentType)) {
				st.setString(index++, incidentType);  
			}
			if(clientId>0) {
				st.setLong(index++, clientId); 
			}
			if(incidentId>0) {
				st.setLong(index++, incidentId);  
			}
			rs = st.executeQuery();
			populateIncidents(rs, dtoList);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return dtoList;
	}
	
	protected List<GoalIncidentDto> getGoalScoredIncidentsForMatches(String matchIdsStr, Integer clubId) throws Exception {

		List<GoalIncidentDto> goalIncidents = new ArrayList<GoalIncidentDto>();

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "";

		query += "SELECT m.match_id, i.player1_id player_id,  "
				+ "( SELECT CONCAT(f_name,' ',l_name) FROM mcc.player WHERE player_id = i.player1_id ) player_name, "
				+ "i.mins_of_game, "
				+ "(SELECT team_id FROM team_player WHERE player_id = i.player1_id AND team_id IN (m.team_one, m.team_two) ) player_team_id "
				+ "FROM incidents i, matches m, team_player tp "
				+ "WHERE i.match_id = m.match_id AND i.incident_type LIKE '%Goal Scored%' "
				+ "AND (m.team_one = tp.team_id OR m.team_two = tp.team_id)  " + "AND m.match_id IN ( " + matchIdsStr+ ") " 
				+ "GROUP BY i.player1_id, i.match_id, i.mins_of_game order by i.match_id, i.mins_of_game";

		try {
			conn = DButility.getConnection(clubId, true);
			st = conn.prepareCall(query);

			rs = st.executeQuery();

			while (rs.next()) {
				goalIncidents.add(populateGoalIncidents(rs));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return goalIncidents;
	}
	
	
	protected int insertIncident(IncidentDto dto, int clubId) throws Exception {

		String query= "insert into incidents(match_id,mins_of_game,additional_mins,incident_type,player1_id,"
				+ "player2_id,comment,client_id,team1_goals,team2_goals,"
				+ "time_saved,created_time) values (?,?,?,?,?,?,?,?,?,?,?,now())";
		
		int incidentId = 0;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = DButility.getConnection(clubId, true);
			st = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
			
			int index = 1;
			
			st.setInt(index++, dto.getMatchId());
			st.setString(index++, dto.getMinsOfGame());
			st.setString(index++, dto.getAdditionalMins());
			st.setString(index++, dto.getIncidentType());
			st.setInt(index++, dto.getPlayer1Id());
			st.setInt(index++, dto.getPlayer2Id());
			st.setString(index++, dto.getComment());
			st.setLong(index++, dto.getClientId());
			st.setInt(index++, dto.getTeam1Goals());
			st.setInt(index++, dto.getTeam2Goals());
			st.setString(index++, dto.getTimeSaved());
			
			st.executeUpdate();
			
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				incidentId = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}		
		return incidentId;
	}
	
	protected int updateIncident(IncidentDto dto, int clubId) throws Exception {

		String query= "update incidents set match_id=?,mins_of_game=?,additional_mins=?,incident_type=?,"
				+ "player1_id=?,player2_id=?,comment=?,time_saved=?,updated_time=now() where client_id = ?";
		
		int rowsUpdated = 0;
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			conn = DButility.getConnection(clubId, true);
			st = conn.prepareStatement(query);
			
			int index = 1;
			
			st.setInt(index++, dto.getMatchId());
			st.setString(index++, dto.getMinsOfGame());
			st.setString(index++, dto.getAdditionalMins());
			st.setString(index++, dto.getIncidentType());
			st.setInt(index++, dto.getPlayer1Id());
			st.setInt(index++, dto.getPlayer2Id());
			st.setString(index++, dto.getComment());
			st.setString(index++, dto.getTimeSaved());
			st.setLong(index++, dto.getClientId());
			
			rowsUpdated = st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}		
		return rowsUpdated;
	}
	
	protected String deleteIncident(long clientId, int incidentId, int clubId) throws Exception {

		String query;
		String message = "";
		PreparedStatement st = null;
		Connection conn = null;
		try {
			conn = DButility.getConnection(clubId, true);
			query = "DELETE FROM incidents WHERE 1=1 ";
			
			if(clientId>0) {
				query += "and client_id= ?";
			}
			if(incidentId>0) {
				query += "and id= ?";
			}
			st = conn.prepareStatement(query);
			int index = 1;
			if(clientId>0) {
				st.setLong(index++, clientId);	
			}
			if(incidentId>0) {
				st.setInt(index++, incidentId);	
			}
			int rec = st.executeUpdate();			
			message = rec+ " Incident Deleted";

		} catch (Exception e) {
			throw new Exception(e.getMessage());
			
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		
		return message;
	}
	
	private void populateIncidents(ResultSet rs, List<IncidentDto> dtoList) throws SQLException {
		
		while (rs.next()) {
			
			IncidentDto dto = new IncidentDto();
			
			dto.setIncidentId(rs.getInt("id"));
			dto.setMatchId(rs.getInt("match_id"));
			dto.setClientId(rs.getLong("client_id"));
			dto.setMinsOfGame(rs.getString("mins_of_game"));
			dto.setAdditionalMins(rs.getString("additional_mins"));
			dto.setIncidentType(rs.getString("incident_type"));
			dto.setPlayer1Id(rs.getInt("player1_id"));
			dto.setPlayer2Id(rs.getInt("player2_id"));	
			dto.setTeam1Goals(rs.getInt("team1_goals"));
			dto.setTeam2Goals(rs.getInt("team2_goals"));
			dto.setComment(rs.getString("comment"));
			dto.setTimeSaved(rs.getString("time_saved"));
			dto.setCreatedTime(rs.getTimestamp("created_time"));
			
			dtoList.add(dto);
		}
	}
	
	public boolean checkScorer(int scorer, int clubId, String sessionId) throws Exception {
		
		String query = "select 1 from matches where scorer = " + scorer + " and scorer_session = '" + sessionId + "'";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean correctScorer = false;
		try {
			conn = DButility.getConnection(clubId, true);
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				correctScorer = true;
			 }
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, ps, rs);
		}
		return correctScorer;
	}
	private GoalIncidentDto populateGoalIncidents(ResultSet rs) throws SQLException {
		
		GoalIncidentDto dto = new GoalIncidentDto();		
		
		dto.setMatchId(rs.getInt("match_id"));
		dto.setPlayerId(rs.getInt("player_id"));
		dto.setPlayerName(rs.getString("player_name"));
		dto.setTeamId(rs.getInt("player_team_id"));
		dto.setIncidentTime(rs.getString("mins_of_game"));

		return dto;
	}
	
	protected int getMatchIdByIncidentClientId(long clientId, int clubId) throws Exception {
		
		String query = " select match_id from incidents where client_id =? ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int matchId = 0;
		try {
			conn = DButility.getReadConnection(clubId, true);
			st = conn.prepareStatement(query);
			st.setLong(1, clientId);
			rs = st.executeQuery();
			while (rs.next()) {
				matchId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matchId;
	}

}
