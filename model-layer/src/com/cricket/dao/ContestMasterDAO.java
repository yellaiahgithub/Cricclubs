package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.ContestMasterDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class ContestMasterDAO {
	static Logger log = LoggerFactory.getLogger(ContestMasterDAO.class);
	public String SELECT_QUERY = "select id,fantasy_currency,no_of_participants,minimum_participants,"
			+ "no_of_winners,contest_type,entry_fee,total_prize_amount,total_coins,max_entries,status,"
			+ "auto_create_contest,created_at,updated_at from master_contests ";
	
	private void populateMasterContestDto(ContestMasterDto contestDto, ResultSet rs) throws SQLException {
		
		contestDto.setId(rs.getInt("id"));		
		contestDto.setFantasyCurrency(rs.getString("fantasy_currency"));		
		contestDto.setNoOfParticipants(rs.getInt("no_of_participants"));
		contestDto.setMinimumParticipants(rs.getInt("minimum_participants"));
		contestDto.setNoOfWinners(rs.getInt("no_of_winners"));		
		contestDto.setContestType(rs.getString("contest_type"));
		contestDto.setEntryFee(rs.getFloat("entry_fee"));		
		contestDto.setTotalPrizeAmount(rs.getFloat("total_prize_amount"));
		contestDto.setTotalCoins(rs.getInt("total_coins"));
		contestDto.setMaxBnousPercent(rs.getInt("max_bonus_percent"));
		contestDto.setMaxBonusAmount(rs.getFloat("max_bonus_amount"));
		contestDto.setMaxEntries(rs.getInt("max_entries"));		
		contestDto.setStatus(rs.getInt("status"));		
		contestDto.setAutoCreateContest(rs.getInt("auto_create_contest"));
		contestDto.setCreatedAt(rs.getDate("created_at"));		
		contestDto.setUpdatedAt(rs.getDate("updated_at"));		
	
	}

	protected List<ContestMasterDto> getMasterContests(int contestId, int noOfParticipants, 
			String fantasyCurrency, int limit) throws Exception {
		
		String query = " SELECT id,fantasy_currency,no_of_participants,minimum_participants,no_of_winners,contest_type, "
				+ "entry_fee,total_prize_amount,total_coins,max_entries,status,created_at,"
				+ "updated_at,max_bonus_amount,max_bonus_percent,auto_create_contest "
				+ " FROM master_contests c where 1=1 ";			
				
		if(contestId>0) {
			query = query + " and id = ?";
		}	
		if(noOfParticipants>0) {
			query = query + " and no_of_participants = ? ";
		}
		if(!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
			query = query + " and fantasy_currency = ? ";
		}
		if(limit>0) {
			query = query + " limit ?";
		}
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContestMasterDto> contestsList = new ArrayList<ContestMasterDto>();
		ContestMasterDto dbContest = null;
		try {
			pst = conn.prepareStatement(query);	
			
			int index = 1;
			if(contestId>0) {
				pst.setInt(index++, contestId);
			}
			if(noOfParticipants>0) {
				pst.setInt(index++, noOfParticipants);
			}
			if(!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
				pst.setString(index++, fantasyCurrency);
			}
			if(limit>0) {
				pst.setInt(index++, limit);
			}
			rs = pst.executeQuery();
			
			while (rs.next()) {
				dbContest = new ContestMasterDto();
				populateMasterContestDto(dbContest, rs);	
				contestsList.add(dbContest);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestsList;
	}	
	
	protected List<Integer> getUsedMasterContestIds(String matchIds) throws Exception {
		
		String query = " SELECT id FROM master_contests mc where 1=1 ";			
				
		if(!CommonUtility.isNullOrEmpty(matchIds)) {
			query = query + " and id not in (select master_contest_id from contests where match_id in ("+matchIds+") ) ";
		}		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Integer> usedMasterContestIds = new ArrayList<Integer>();
		try {
			pst = conn.prepareStatement(query);	
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				usedMasterContestIds.add(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return usedMasterContestIds;
	}	
	
	protected int insertMasterContest(ContestMasterDto  contestDto) throws Exception {
		
		String query = "insert into master_contests(fantasy_currency,no_of_participants,minimum_participants,"
				+ "no_of_winners,contest_type, entry_fee,total_prize_amount,total_coins,max_entries,"
				+ "max_bonus_amount,auto_create_contest,status,created_at) values (?,?,?,?,?,?,?,?,?,?,?,1,NOW())";		
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;		
		ResultSet rs = null;
		int contestId = 0;
		
		try {			
			int index = 1;
			st = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);	
			
			st.setString(index++, contestDto.getFantasyCurrency());
			st.setInt(index++, contestDto.getNoOfParticipants());
			st.setInt(index++, contestDto.getMinimumParticipants());
			st.setInt(index++, contestDto.getNoOfWinners());
			st.setString(index++, contestDto.getContestType());
			st.setFloat(index++, contestDto.getEntryFee());
			st.setFloat(index++, contestDto.getTotalPrizeAmount());
			st.setInt(index++, contestDto.getTotalCoins());
			st.setInt(index++, contestDto.getMaxEntries());
			st.setFloat(index++, contestDto.getMaxBonusAmount());
			st.setInt(index++, contestDto.getAutoCreateContest());
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			
			if (rs.next()) {
				contestId = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {	
			
			DButility.dbCloseAll(conn, st, rs);
		}		
		return contestId;
	}
	
	protected void updateMasterContest(ContestMasterDto contest) throws Exception {
		
		String query ="update master_contests set fantasy_currency=?,contest_type=?,no_of_participants=?,minimum_participants=?,"
				+ "no_of_winners=?,entry_fee=?,total_prize_amount=?,total_coins=?,max_entries=?,"
				+ "max_bonus_amount=?,auto_create_contest=? where id=?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;	
		ResultSet rs = null;		
		try {
			st = conn.prepareStatement(query);
			
			int index = 1;
			
			st.setString(index++, contest.getFantasyCurrency());
			st.setString(index++, contest.getContestType());
			st.setInt(index++, contest.getNoOfParticipants());	
			st.setInt(index++, contest.getMinimumParticipants());	
			st.setInt(index++, contest.getNoOfWinners());	
			st.setFloat(index++, contest.getEntryFee());
			st.setFloat(index++, contest.getTotalPrizeAmount());
			st.setInt(index++, contest.getTotalCoins());
			st.setInt(index++, contest.getMaxEntries());
			st.setFloat(index++, contest.getMaxBonusAmount());	
			st.setInt(index++, contest.getAutoCreateContest());
			st.setInt(index++,contest.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, st, rs);
		}		
	}
	
	protected List<Integer> getMasterContestSizeValues(String fantasyCurrency) throws Exception {
		
		String query = " select distinct(no_of_participants) FROM master_contests where 1 = 1 ";
		
		if(!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
			query = query + " and fantasy_currency = ? ";
		}
		query = query + " order by no_of_participants ";
				
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Integer> masterContestSizeValues = new ArrayList<Integer>();
		
		try {
			pst = conn.prepareStatement(query);	
			
			if(!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
				pst.setString(1,fantasyCurrency);
			}
			rs = pst.executeQuery();
			
			while (rs.next()) {
				masterContestSizeValues.add(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return masterContestSizeValues;
	}	
	
	protected List<String> getContestTypes() throws Exception {
		
		String query = " select distinct(contest_type) FROM master_contests order by contest_type";			
				
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> contestTypes = new ArrayList<String>();
		
		try {
			pst = conn.prepareStatement(query);	
			rs = pst.executeQuery();
			
			while (rs.next()) {
				contestTypes.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestTypes;
	}
	
	protected Map<Integer, String> getMasterContestIdTypeMap(String fantasyCurrency) throws Exception {
		
		String query = " select id, contest_type FROM master_contests where fantasy_currency='"+fantasyCurrency+"'";			
				
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer, String> masterContestIdTypeMap = new HashMap<Integer, String>();
		try {
			pst = conn.prepareStatement(query);	
			rs = pst.executeQuery();
			
			while (rs.next()) {
				masterContestIdTypeMap.put(rs.getInt(1),rs.getString(2));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return masterContestIdTypeMap;
	}
	
	public void deleteMasterContest(int masterContestId) throws Exception {

		String query = "delete from master_contests where id = ?";

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement(query);
			if(masterContestId > 0) {
				pst.setInt(1, masterContestId);
			}
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	public int bulkUpdateMasterContest(List<ContestMasterDto> cntsMDto) throws Exception {

		String query =  "update master_contests set fantasy_currency = ?, no_of_participants = ?, "
				+ " minimum_participants = ?, contest_type = ?, entry_fee = ?, max_bonus_amount = ?, "
				+ " max_entries  = ?, updated_at = now(), auto_create_contest = ? where id = ?";
		
		int recordsUpdated = 0;	
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement(query);
			
			conn.setAutoCommit(false);
			
			for(ContestMasterDto dto : cntsMDto) {
				
				int index = 1;
				
				st.setString(index++, dto.getFantasyCurrency());
				st.setInt(index++, dto.getNoOfParticipants());
				//st.setInt(index++, dto.getNoOfWinners());
				st.setInt(index++, dto.getMinimumParticipants());
				st.setString(index++, dto.getContestType());
				st.setFloat(index++, dto.getEntryFee());
				//st.setFloat(index++, dto.getTotalPrizeAmount());
				st.setFloat(index++, dto.getMaxBonusAmount());
				st.setInt(index++, dto.getMaxEntries());
				st.setInt(index++, dto.getAutoCreateContest());

				st.setInt(index++, dto.getId());

				st.addBatch();
			}
			int[] count = st.executeBatch();			
			conn.commit();
			
			recordsUpdated = count.length;
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);			
		}		
		return recordsUpdated;		
	}
		
		
}
