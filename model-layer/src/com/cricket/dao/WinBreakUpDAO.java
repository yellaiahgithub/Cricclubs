package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.DepositsDto;
import com.cricket.dto.MatchesFantasysDto;
import com.cricket.dto.WinBreakUpDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class WinBreakUpDAO {
	
	public String SELECT_QUERY = "select id,master_contest_id,contest_id,starting_rank,"
			+ "ending_rank,amount,fantasy_currency from winner_breakup ";
	
	private void populateWinBreakUpDto(WinBreakUpDto winBreakUpDto, ResultSet rs) throws SQLException {
		
		winBreakUpDto.setId(rs.getInt("id"));
		winBreakUpDto.setMasterContestId(rs.getInt("master_contest_id"));
		winBreakUpDto.setContestId(rs.getInt("contest_id"));		
		winBreakUpDto.setStartingRank(rs.getInt("starting_rank"));
		winBreakUpDto.setEndingRank(rs.getInt("ending_rank"));		
		winBreakUpDto.setAmount(rs.getFloat("amount"));
		winBreakUpDto.setFantasyCurrency(rs.getString("fantasy_currency"));
		
	}

	protected List<WinBreakUpDto> getWinBreakUps(int contestId, int masterContestId) throws Exception {

		String query = SELECT_QUERY + " where 1=1 ";

		if (contestId > 0) {
			query = query + " and contest_id = ? ";
		}
		if (masterContestId > 0) {
			query = query + " and master_contest_id = ? ";
		}

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WinBreakUpDto> dtoList = new ArrayList<WinBreakUpDto>();
		WinBreakUpDto dto = null;
		try {
			pst = conn.prepareStatement(query);

			int index = 1;

			if (contestId > 0) {
				pst.setInt(index++, contestId);
			}
			if (masterContestId > 0) {
				pst.setInt(index++, masterContestId);
			}
			rs = pst.executeQuery();

			while (rs.next()) {
				dto = new WinBreakUpDto();
				populateWinBreakUpDto(dto, rs);
				dtoList.add(dto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return dtoList;
	}
	
	protected List<WinBreakUpDto> getWinBreakUpsForMastetContestIds(String masterContestIds) throws Exception {

		String query = SELECT_QUERY + " where 1=1 ";

		if (!CommonUtility.isNullOrEmpty(masterContestIds)) {
			query = query + " and master_contest_id in ("+masterContestIds+")";
		}
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WinBreakUpDto> dtoList = new ArrayList<WinBreakUpDto>();
		WinBreakUpDto dto = null;
		try {
			pst = conn.prepareStatement(query);

			rs = pst.executeQuery();

			while (rs.next()) {
				dto = new WinBreakUpDto();
				populateWinBreakUpDto(dto, rs);
				dtoList.add(dto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return dtoList;
	}
	
	protected List<WinBreakUpDto> getMasterWinBreakUpsByNoOfParticipants(int noOfParticipants) throws Exception {

		String query = SELECT_QUERY + " where 1=1 ";

		if (noOfParticipants > 0) {
			query = query + " and master_contest_id in (select id from master_contests where no_of_participants = ? limit 1) ";
		}
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<WinBreakUpDto> dtoList = new ArrayList<WinBreakUpDto>();
		WinBreakUpDto dto = null;
		try {
			pst = conn.prepareStatement(query);

			if (noOfParticipants > 0) {
				pst.setInt(1, noOfParticipants);
			}
			rs = pst.executeQuery();

			while (rs.next()) {
				dto = new WinBreakUpDto();
				populateWinBreakUpDto(dto, rs);
				dtoList.add(dto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return dtoList;
	}
	
	protected void addWinningBreakUpsForDupContest(int oldContestId, int newContestId) throws Exception {
		
		String query = "insert into winner_breakup(contest_id,starting_rank,ending_rank,amount,fantasy_currency) "
				+ " (select ?,starting_rank,ending_rank,amount,fantasy_currency from winner_breakup where contest_id = ?)";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		try {
			pst = conn.prepareStatement(query);
			
			int index=1;
			
			pst.setInt(index++,newContestId);
			pst.setInt(index++,oldContestId);
			
			pst.executeUpdate();			
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected int insertWinningBreakUps(List<WinBreakUpDto> wbrList) throws Exception {
		
		String query = "insert into winner_breakup(master_contest_id,contest_id,starting_rank,"
				+ "ending_rank,amount, fantasy_currency) values (?,?,?,?,?,?)";
		
		int noOfRecordsInserted = 0;		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement(query);
			
			conn.setAutoCommit(false);
			
			for(WinBreakUpDto wbrDto : wbrList) {
				
				int index = 1;
				
				st.setInt(index++, wbrDto.getMasterContestId());
				st.setInt(index++, wbrDto.getContestId());
				st.setInt(index++, wbrDto.getStartingRank());
				if(wbrDto.getEndingRank()>0) {
					st.setInt(index++, wbrDto.getEndingRank());
				}else {
					st.setInt(index++, wbrDto.getStartingRank());
				}
				st.setFloat(index++, wbrDto.getAmount());
				st.setString(index++, wbrDto.getFantasyCurrency());
				
				st.addBatch();
			}
			int[] count = st.executeBatch();			
			conn.commit();
			
			noOfRecordsInserted = count.length;
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);			
		}
		
		return noOfRecordsInserted;
	}
	
	public void deleteMasterWinningBreakUps(int masterContestId) throws Exception {
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");		
		Statement st = null;
		try {
			st = conn.createStatement();			
			st.executeUpdate("delete from winner_breakup where master_contest_id = " + masterContestId);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	public void deleteContestWinningBreakUps(int contestId) throws Exception {
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");		
		Statement st = null;
		try {
			st = conn.createStatement();			
			st.executeUpdate("delete from winner_breakup where contest_id = " + contestId);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	
	
}
