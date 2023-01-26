package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.FreeContestDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class FreeContestDAO {
	
	public String SELECT_QUERY = "select id,match_id,contest_name,no_of_participants,no_of_winners,"
			+ "entry_points,total_prize_points,max_entries,status,allow_multi_entry,type,created_at,updated_at from free_contests ";
	
	private void populateContestDto(FreeContestDto contestDto, ResultSet rs) throws SQLException {
		
		contestDto.setId(rs.getInt("id"));
		contestDto.setMatchId(rs.getInt("match_id"));
		contestDto.setContestName(rs.getString("contest_name"));		
		contestDto.setNoOfParticipants(rs.getInt("no_of_participants"));		
		contestDto.setNoOfWinners(rs.getInt("no_of_winners"));		
		contestDto.setEntryPoints(rs.getInt("entry_points"));				
		contestDto.setTotalPrizePoints(rs.getInt("total_prize_points"));
		contestDto.setMaxEntries(rs.getInt("max_entries"));		
		contestDto.setStatus(rs.getInt("status"));
		contestDto.setAllowMultiEntry(rs.getInt("allow_multi_entry"));
		contestDto.setType(rs.getString("type"));		
		contestDto.setCreatedAt(rs.getDate("created_at"));		
		contestDto.setUpdatedAt(rs.getDate("updated_at"));
		contestDto.setNoOfUsersJoined(rs.getInt("no_of_user_joined"));
		contestDto.setNoOfUserEntries(rs.getInt("no_of_user_entries"));		
	}

	protected List<FreeContestDto> getFreeContests(int contestId, int matchId, long userId, String contestName) throws Exception {
		
		String query = " SELECT id,match_id,contest_name,no_of_participants,no_of_winners,type, "
				+ "entry_points,total_prize_points,max_entries,status,allow_multi_entry,type,created_at,updated_at, "
				+ "(SELECT COUNT(*) FROM contest_participants WHERE contest_id = c.id) no_of_user_joined, "				
				+ "( SELECT count(*) FROM contest_participants WHERE contest_id = c.id AND user_id = ?) no_of_user_entries "
				+ "FROM free_contests c where 1=1 ";			
				
		if(contestId>0) {
			query = query + " and id = ?";
		}		
		if(matchId>0) {
			query = query + " and match_id = ?";
		}		
		if(CommonUtility.isNullOrEmpty(contestName)) {
			query = query + " and contest_name = ?";
		}		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<FreeContestDto> contestsList = new ArrayList<FreeContestDto>();
		FreeContestDto dbContest = null;
		try {
			pst = conn.prepareStatement(query);	
			
			int index = 1;	
			
			pst.setLong(index++, userId);			
			
			if(contestId>0) {
				pst.setInt(index++, contestId);
			}		
			if(matchId>0) {
				pst.setInt(index++, matchId);
			}			
			if(CommonUtility.isNullOrEmpty(contestName)) {
				pst.setString(index++, contestName);
			}
			rs = pst.executeQuery();			
			while (rs.next()) {
				dbContest = new FreeContestDto();
				populateContestDto(dbContest, rs);	
				contestsList.add(dbContest);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestsList;
	}
	
	protected int insertFreeContest(FreeContestDto fcDto) throws Exception {
		
		String query ="insert into free_contests(match_id,contest_name,no_of_participants,no_of_winners,"  
					+ "entry_points,total_prize_points,max_entries,allow_multi_entry,type,created_at) "
					+ "values ( ?,?,?,?,?,?,?,?,?,NOW()";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;
		int contestId = 0;		
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			int index = 1;
			
			pst.setLong(index++,fcDto.getMatchId());
			pst.setString(index++,fcDto.getContestName());
			pst.setInt(index++,fcDto.getNoOfParticipants());
			pst.setInt(index++,fcDto.getNoOfWinners());
			pst.setInt(index++,fcDto.getEntryPoints());
			pst.setInt(index++,fcDto.getTotalPrizePoints());
			pst.setInt(index++,fcDto.getMaxEntries());
			pst.setInt(index++,fcDto.getAllowMultiEntry());
			pst.setString(index++,fcDto.getType());
			
			pst.executeUpdate();			
			rs = pst.getGeneratedKeys();
			
			if (rs.next()) {
				contestId = rs.getInt(1);
			}		
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestId;
	}
	
}
