package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.FantasyTeamPlayerDto;
import com.cricket.utility.DButility;

public class FantasyTeamPlayerDAO {
	
	public String SELECT_QUERY = "select player_id,team_id,player_credits,cc_player_id,"
			+ "player_points from fantasy_team_players ";
	
	private void populateTeamPlayerDto(FantasyTeamPlayerDto teamPlayerDto, ResultSet rs) throws SQLException {
		
		teamPlayerDto.setPlayerId(rs.getLong("player_id"));
		teamPlayerDto.setCcPlayerId(rs.getLong("cc_player_id"));		
		teamPlayerDto.setTeamId(rs.getInt("team_id"));	
		teamPlayerDto.setPlayerPoints(rs.getInt("player_points"));
	}

	protected List<FantasyTeamPlayerDto> getFantasyTeamPlayers(int teamId) throws Exception {
		
		String query = SELECT_QUERY+ " where 1=1 ";			
		if(teamId>0) {
			query = query + " and team_id = ?";
		}
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<FantasyTeamPlayerDto> teamPlayerList = new ArrayList<FantasyTeamPlayerDto>();
		FantasyTeamPlayerDto teamPlayer = null;
		try {
			pst = conn.prepareStatement(query);	
			
			if(teamId>0) {
				pst.setLong(1, teamId);
			}			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				teamPlayer = new FantasyTeamPlayerDto();
				populateTeamPlayerDto(teamPlayer, rs);	
				teamPlayerList.add(teamPlayer);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teamPlayerList;
	}
	
	protected void insertFantasyTeamPlayersCopyMatches(int clubId, int teamId) throws Exception {
		
		String query = "insert into fantasy_team_players(cc_club_id,cc_team_id,cc_player_id,team_id,player_id) "
						+ " select "+clubId+",tp.team_id,tp.player_id,"
						+ "(select id from fantasy_team where cc_club_id = "+clubId+" and cc_team_id = tp.team_id) team_id,"
						+ "(select id from fantasy_player where cc_player_id = tp.player_id) player_id "
						+ " from club"+clubId+".team_player tp where team_id = ?";		
			
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");		
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement(query);
			
			st.setInt(1, teamId);
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);			
		}
		
	}
	
	protected int copyTeamPlayersToFantasy(List<FantasyTeamPlayerDto> teamPlayersList) throws Exception {
		
		String query = "INSERT INTO fantasy_team_players(cc_club_id,cc_team_id,cc_player_id,team_id,player_id) values (?,?,?,?,?)";
		
		int noOfPlayersCopied = 0;
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");		
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement(query);			
			conn.setAutoCommit(false);
			
			for(FantasyTeamPlayerDto teamPlayer : teamPlayersList) {
				
				int index = 1;
				
				st.setInt(index++, teamPlayer.getCcClubId());
				st.setInt(index++, teamPlayer.getCcTeamId());
				st.setLong(index++, teamPlayer.getCcPlayerId());
				st.setInt(index++, teamPlayer.getTeamId());
				st.setLong(index++, teamPlayer.getPlayerId());
				
				st.addBatch();
			}
			int[] count = st.executeBatch();			
			conn.commit();
			
			noOfPlayersCopied = count.length;
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);			
		}
		return noOfPlayersCopied;
	}
	
}
