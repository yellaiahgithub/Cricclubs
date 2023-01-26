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

import com.cricket.dto.ContestDto;
import com.cricket.dto.ContestDtoMX;
import com.cricket.dto.MasterContestsTokenDto;
import com.cricket.dto.parser.TeamDtoCH;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class ContestDAO {
	static Logger log = LoggerFactory.getLogger(ContestDAO.class);
	public String SELECT_QUERY = "select id,master_contest_id,match_id,tournament_id,fantasy_currency,no_of_participants,"
			+ "(select match_title from fantasy_matches where id = c.match_id ) match_title,"
			+ "minimum_participants,no_of_winners,contest_type,contest_name,entry_fee,total_prize_amount,total_coins,"
			+ "max_bonus_amount,max_entries,invite_code,status,auto_create_contest,contest_level,created_at,updated_at from contests c";

	private void populateContestDto(ContestDto contestDto, ResultSet rs) throws SQLException {

		contestDto.setId(rs.getInt("id"));
		contestDto.setMasterContestId(rs.getInt("master_contest_id"));
		contestDto.setMatchId(rs.getInt("match_id"));
		try {
			contestDto.setMatchTitle(rs.getString("match_title"));
		}catch(Exception e) {
			
		}
		contestDto.setTournamentId(rs.getInt("tournament_id"));
		contestDto.setFantasyCurrency(rs.getString("fantasy_currency"));
		contestDto.setNoOfParticipants(rs.getInt("no_of_participants"));
		contestDto.setMinimumParticipants(rs.getInt("minimum_participants"));
		contestDto.setNoOfWinners(rs.getInt("no_of_winners"));
		contestDto.setContestType(rs.getString("contest_type"));
		contestDto.setContestName(rs.getString("contest_name"));
		contestDto.setEntryFee(rs.getFloat("entry_fee"));
		contestDto.setTotalPrizeAmount(rs.getFloat("total_prize_amount"));
		contestDto.setTotalCoins(rs.getInt("total_coins"));
		contestDto.setMaxBonusAmount(rs.getFloat("max_bonus_amount"));
		contestDto.setMaxEntries(rs.getInt("max_entries"));
		contestDto.setInviteCode(rs.getString("invite_code"));
		contestDto.setStatus(rs.getInt("status"));
		contestDto.setAutoCreateContest(rs.getInt("auto_create_contest"));
		contestDto.setCreatedAt(rs.getDate("created_at"));
		contestDto.setUpdatedAt(rs.getDate("updated_at"));
		contestDto.setContestLevel(rs.getString("contest_level"));
		try {
			contestDto.setNoOfUsersJoined(rs.getInt("no_of_users_joined"));
		} catch (Exception e) {
			
		}
		try {
			contestDto.setNoOfUserEntries(rs.getInt("no_of_user_entries"));
		} catch (Exception e) {

		}
	}

	protected List<ContestDto> getContests(ContestDto contest, int matchStatus) throws Exception {
		
		String query = " SELECT id,master_contest_id,match_id,tournament_id,fantasy_currency,no_of_participants,"
				+ "minimum_participants,no_of_winners,contest_type,contest_name,entry_fee, total_prize_amount,total_coins,"
				+ "max_bonus_amount,max_entries,invite_code,status,auto_create_contest,contest_level,created_at,updated_at,no_of_users_joined, ";
		
		if(MatchesFantasyFactory.ARCHIVED == matchStatus) {
			query += "( SELECT count(*) FROM contest_participants_bkp WHERE contest_id = c.id AND user_id = ?) no_of_user_entries ";
			query += " FROM contests_bkp c where 1=1 ";
		}else {
			query += "( SELECT count(*) FROM contest_participants WHERE contest_id = c.id AND user_id = ?) no_of_user_entries ";
			query += " FROM contests c where 1=1 ";
		}
		
		if (contest.getId() > 0) {
			query = query + " and id = ?";
		}
		if (contest.getMasterContestId() > 0) {
			query = query + " and master_contest_id = ?";
		}
		if (contest.getMatchId() > 0) {
			query = query + " and match_id = ?";
		}
		if (contest.getTournamentId() > 0) {
			query = query + " and tournament_id = ?";
		}
		if (!CommonUtility.isNullOrEmpty(contest.getContestType())) {
			query = query + " and contest_type = ?";
		}
		if (!CommonUtility.isNullOrEmpty(contest.getContestLevel())) {
			query = query + " and contest_level = ?";
		}
		if (contest.getStatus() > 0) {
			query = query + " and status = ?";
		}
		if (!CommonUtility.isNullOrEmpty(contest.getInviteCode())) {
			query = query + " and invite_code = ? ";
		}
		if (!CommonUtility.isNullOrEmpty(contest.getFantasyCurrency())) {
			query = query + " and fantasy_currency  = ? and (no_of_users_joined < no_of_participants or contest_type = 'Mega') ";
		}
		Connection conn = null;
		
		if(contest.getId() == 0) {
			conn = DButility.getFantasyReadConnection("ccfantasy");
		}else {
			conn = DButility.getFantasyWriteConnection("ccfantasy");
		}
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContestDto> contestsList = new ArrayList<ContestDto>();
		ContestDto dbContest = null;
		try {
			pst = conn.prepareStatement(query);

			int index = 1;
			
			pst.setLong(index++, contest.getUserId());

			if (contest.getId() > 0) {
				pst.setInt(index++, contest.getId());
			}
			if (contest.getMasterContestId() > 0) {
				pst.setInt(index++, contest.getMasterContestId());
			}
			if (contest.getMatchId() > 0) {
				pst.setInt(index++, contest.getMatchId());
			}
			if (contest.getTournamentId() > 0) {
				pst.setInt(index++, contest.getTournamentId());
			}
			if (!CommonUtility.isNullOrEmpty(contest.getContestType())) {
				pst.setString(index++, contest.getContestType());
			}
			if (!CommonUtility.isNullOrEmpty(contest.getContestLevel())) {
				pst.setString(index++, contest.getContestLevel());
			}
			if (contest.getStatus() > 0) {
				pst.setInt(index++, contest.getStatus());
			}
			if (!CommonUtility.isNullOrEmpty(contest.getInviteCode())) {
				pst.setString(index++, contest.getInviteCode());
			}
			if (!CommonUtility.isNullOrEmpty(contest.getFantasyCurrency())) {
				pst.setString(index++, contest.getFantasyCurrency());
			}

			rs = pst.executeQuery();

			while (rs.next()) {
				dbContest = new ContestDto();
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
	
	protected Map<Integer,List<ContestDtoMX>> getContestListMX(int status) throws Exception {
		
		String query = " SELECT distinct(c.master_contest_id), fm.id, c.contest_type "
				+ "FROM fantasy_matches fm, contests c where fm.id = c.match_id "
				+ "and c.fantasy_currency = 'GEMS' and fm.status = 1 order by fm.id";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer,List<ContestDtoMX>> contestsListMap = new HashMap<Integer, List<ContestDtoMX>>();
		ContestDtoMX dbContest = null;
		try {
			conn = DButility.getFantasyWriteConnection("ccfantasy");
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				dbContest = new ContestDtoMX();
				dbContest.setContestId(rs.getInt(2)+"_"+rs.getInt(1));
				dbContest.setContestName(rs.getString(3));
				List<ContestDtoMX> contestList = contestsListMap.get(rs.getInt(2));
				if(CommonUtility.isListNullEmpty(contestList)) {
					contestList = new ArrayList<ContestDtoMX>();
					contestsListMap.put(rs.getInt(2), contestList);
				}
				contestList.add(dbContest);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestsListMap;
	}
	
	protected List<ContestDto> getUnfilledContestList(int fantasyMatchId, int contestId) throws Exception {
		
		String query = " SELECT id, master_contest_id, contest_type, auto_create_contest, no_of_participants, "
				+ "no_of_users_joined FROM contests where 1=1 ";
		
		if(fantasyMatchId>0) {
			query += " and match_id = ? ";
		}
		
		if(contestId>0) {
			query += " and id = ? ";
		}
		
		query += " and no_of_users_joined<no_of_participants and fantasy_currency = 'GEMS' and status = 1";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContestDto> contestList = new ArrayList<ContestDto>();
		try {
			conn = DButility.getFantasyWriteConnection("ccfantasy");
			pst = conn.prepareStatement(query);
			
			int index = 1;
			if(fantasyMatchId>0) {
				pst.setInt(index++, fantasyMatchId);
			}			
			if(contestId>0) {
				pst.setInt(index++, contestId);
			}
			
			rs = pst.executeQuery();
			while (rs.next()) {
				
				ContestDto dto = new ContestDto();
				
				dto.setId(rs.getInt("id"));
				dto.setMasterContestId(rs.getInt("master_contest_id"));
				dto.setContestType(rs.getString("contest_type"));
				dto.setAutoCreateContest(rs.getInt("auto_create_contest"));
				dto.setNoOfParticipants(rs.getInt("no_of_participants"));
				dto.setNoOfUsersJoined(rs.getInt("no_of_users_joined"));
				
				contestList.add(dto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestList;
	}
	
	protected int getMaxContestIdByMasterAndMatchId(int fantasyMatchId, int masterContestId) throws Exception {
		
		String query = " SELECT max(id) FROM contests where match_id = ? and master_contest_id = ?";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int maxContestId = 0;
		try {
			conn = DButility.getFantasyWriteConnection("ccfantasy");
			pst = conn.prepareStatement(query);
			pst.setInt(1, fantasyMatchId);
			pst.setInt(2, masterContestId);
			rs = pst.executeQuery();
			while (rs.next()) {				
				maxContestId = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return maxContestId;
	}
	
	protected List<MasterContestsTokenDto> getMasterContestListForTokens(int fantasyMatchId) throws Exception {
		
		String query = " SELECT c.master_contest_id, fm.id, fm.match_title, c.contest_type, c.auto_create_contest, c.no_of_participants, "
				+ "COUNT(*) no_of_contests, SUM(c.no_of_users_joined)  no_of_users_joined "
				+ "FROM fantasy_matches fm, contests c WHERE fm.id = c.match_id AND c.fantasy_currency = 'GEMS' AND fm.id = ? "
				+ "GROUP BY c.master_contest_id, fm.id, fm.match_title, c.contest_type, c.no_of_participants, c.contest_type, c.auto_create_contest";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<MasterContestsTokenDto> contestsList = new ArrayList<MasterContestsTokenDto>();
		try {
			conn = DButility.getFantasyWriteConnection("ccfantasy");
			pst = conn.prepareStatement(query);
			pst.setInt(1, fantasyMatchId);
			rs = pst.executeQuery();
			while (rs.next()) {
				MasterContestsTokenDto dto = new MasterContestsTokenDto();
				
				dto.setMasterContestId(rs.getInt("master_contest_id"));
				dto.setMatchId(rs.getInt("id"));
				dto.setMatchTitle(rs.getString("match_title"));
				dto.setContestType(rs.getString("contest_type"));
				dto.setAutoCreateContest(rs.getInt("auto_create_contest"));
				dto.setNoOfParticipants(rs.getInt("no_of_participants"));
				dto.setNoOfContests(rs.getInt("no_of_contests"));
				dto.setNoOfUsersJoined(rs.getInt("no_of_users_joined"));
				
				contestsList.add(dto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestsList;
	}
	
	
	protected List<ContestDto> getContestsForGEMSCurrency(int matchId) throws Exception {
		
		String query = " SELECT max(id) id,master_contest_id,match_id,tournament_id,fantasy_currency,no_of_participants,"
				+ "minimum_participants,no_of_winners,contest_type,contest_name,entry_fee, total_prize_amount,total_coins,"
				+ "max_bonus_amount,max_entries,invite_code,status,auto_create_contest,contest_level,created_at,updated_at "
				+ "FROM contests c where status = 1 and match_id = ? and fantasy_currency  = 'GEMS' "
				+ "GROUP BY master_contest_id";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContestDto> contestsList = new ArrayList<ContestDto>();
		ContestDto dbContest = null;
		try {
			pst = conn.prepareStatement(query);

			int index = 1;
						
			if (matchId > 0) {
				pst.setInt(index++, matchId);
			}
			rs = pst.executeQuery();

			while (rs.next()) {
				dbContest = new ContestDto();
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
	
	protected Integer getAvailableContestId(int matchId, int masterContestId, String contestType, String fantasyCurrency) throws Exception {
		
		String query = " SELECT c.id, c.no_of_participants, c.no_of_users_joined "
				+ "FROM contests c WHERE c.auto_create_contest = 1 AND c.status = 1 ";
		
		if (matchId > 0) {
			query = query + " and c.match_id = ?";
		}
		if (masterContestId > 0) {
			query = query + " and c.master_contest_id = ?";
		}
		if (!CommonUtility.isNullOrEmpty(contestType)) {
			query = query + " and c.contest_type = ?";
		}
		if (!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
			query = query + " and c.fantasy_currency = ?";
		}
		query = query + " and no_of_participants > no_of_users_joined ORDER BY c.id limit 1";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		int avaialbleContestId = 0;
		try {
			pst = conn.prepareStatement(query);

			int index = 1;
			if (matchId > 0) {
				pst.setInt(index++, matchId);
			}
			if (masterContestId > 0) {
				pst.setInt(index++, masterContestId);
			}
			if (!CommonUtility.isNullOrEmpty(contestType)) {
				pst.setString(index++, contestType);
			}
			if (!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
				pst.setString(index++, fantasyCurrency);
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				avaialbleContestId = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return avaialbleContestId;
	}
	
	protected Map<Integer, Integer> getContestIdJoinedUsersCountMap(String contestIdStr) throws Exception {
		
		String query = " SELECT contest_id, COUNT(*) no_of_users_joined "
				+ "FROM ccfantasy.contest_participants WHERE contest_id in ("+contestIdStr+") group by contest_id";

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer, Integer> contestIdJoinedUsersCountMap = new HashMap<Integer, Integer>();
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				contestIdJoinedUsersCountMap.put(rs.getInt(1), rs.getInt(2));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestIdJoinedUsersCountMap;
	}
	
	protected Integer getMaxContestIdByContestType(int matchId, int masterContestId, String contestType, String fantasyCurrency) throws Exception {
		
		String query = " SELECT MAX(id) FROM contests WHERE auto_create_contest = 1 AND status = 1 and match_id = ? "
				+ " and master_contest_id = ? and contest_type = ? and fantasy_currency = ? ";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		int contestId = 0;
		try {
			pst = conn.prepareStatement(query);

			int index = 1;
			if (matchId > 0) {
				pst.setInt(index++, matchId);
			}
			if (masterContestId > 0) {
				pst.setInt(index++, masterContestId);
			}
			if (!CommonUtility.isNullOrEmpty(contestType)) {
				pst.setString(index++, contestType);
			}
			if (!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
				pst.setString(index++, fantasyCurrency);
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				contestId = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestId;
	}
	
	protected List<ContestDto> getContestsForFantasyWeb(ContestDto contest, String sortBy) throws Exception {

		String query = " SELECT id,master_contest_id,match_id,tournament_id,fantasy_currency,no_of_participants,"
				+ "minimum_participants,no_of_winners,contest_type,contest_name,entry_fee,"
				+ "total_prize_amount,total_coins,max_bonus_amount,max_entries,invite_code,status,auto_create_contest,contest_level,created_at,updated_at, "
				+ "no_of_users_joined, ( SELECT count(*) FROM contest_participants WHERE contest_id = c.id AND user_id = ?) no_of_user_entries "
				+ "FROM contests c where 1=1 ";

		if (contest.getId() > 0) {
			query = query + " and id = ?";
		}
		if (contest.getMasterContestId() > 0) {
			query = query + " and master_contest_id = ?";
		}
		if (contest.getMatchId() > 0) {
			query = query + " and match_id = ?";
		}
		if (contest.getTournamentId() > 0) {
			query = query + " and tournament_id = ?";
		}
		if (!CommonUtility.isNullOrEmpty(contest.getContestType())) {
			query = query + " and contest_type = ?";
		}
		if (!CommonUtility.isNullOrEmpty(contest.getContestLevel())) {
			query = query + " and contest_level = ?";
		}
		if (contest.getStatus() > 0) {
			query = query + " and status = ?";
		}
		if (!CommonUtility.isNullOrEmpty(contest.getInviteCode())) {
			query = query + " and invite_code = ? ";
		}
		if (!CommonUtility.isNullOrEmpty(sortBy)) {
			query = query + " order by "+ sortBy;
		}
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContestDto> contestsList = new ArrayList<ContestDto>();
		ContestDto dbContest = null;
		try {
			pst = conn.prepareStatement(query);

			int index = 1;

			pst.setLong(index++, contest.getUserId());

			if (contest.getId() > 0) {
				pst.setInt(index++, contest.getId());
			}
			if (contest.getMasterContestId() > 0) {
				pst.setInt(index++, contest.getMasterContestId());
			}
			if (contest.getMatchId() > 0) {
				pst.setInt(index++, contest.getMatchId());
			}
			if (contest.getTournamentId() > 0) {
				pst.setInt(index++, contest.getTournamentId());
			}
			if (!CommonUtility.isNullOrEmpty(contest.getContestType())) {
				pst.setString(index++, contest.getContestType());
			}
			if (!CommonUtility.isNullOrEmpty(contest.getContestLevel())) {
				pst.setString(index++, contest.getContestLevel());
			}
			if (contest.getStatus() > 0) {
				pst.setInt(index++, contest.getStatus());
			}
			if (!CommonUtility.isNullOrEmpty(contest.getInviteCode())) {
				pst.setString(index++, contest.getInviteCode());
			}

			rs = pst.executeQuery();

			while (rs.next()) {
				dbContest = new ContestDto();
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
	
	protected Map<Integer, String> getContestIdCurrencyMap(String contestIds) throws Exception {

		String query = " SELECT id, fantasy_currency FROM contests where id in ("+contestIds+")";
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer, String> contestIdCurrencyMap = new HashMap<Integer, String>();
		try {
			conn = DButility.getFantasyReadConnection("ccfantasy");
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {
				contestIdCurrencyMap.put(rs.getInt(1), rs.getString(2));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestIdCurrencyMap;
	}
	
	protected Map<Integer, String> getAutoCreateContestTypeMap(int matchId) throws Exception {

		String query = "SELECT DISTINCT master_contest_id, contest_type FROM contests "
				+ " WHERE match_id = ? AND fantasy_currency = 'GEMS' AND auto_create_contest = 1";
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer, String> masterContestIdTypeMap = new HashMap<Integer, String>();
		try {
			conn = DButility.getFantasyWriteConnection("ccfantasy");
			pst = conn.prepareStatement(query);
			pst.setInt(1, matchId);
			rs = pst.executeQuery();

			while (rs.next()) {
				masterContestIdTypeMap.put(rs.getInt(1), rs.getString(2));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return masterContestIdTypeMap;
	}
	
	protected Integer getMasteContestIdByContestId(int contestId, int isAutoCreateContest) throws Exception {

		String query = "SELECT master_contest_id FROM contests WHERE id = ? and auto_create_contest = ?";
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int masterContestId = 0;
		try {
			conn = DButility.getFantasyWriteConnection("ccfantasy");
			pst = conn.prepareStatement(query);
			pst.setInt(1, contestId);
			pst.setInt(2, isAutoCreateContest);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				masterContestId = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return masterContestId;
	}

	protected List<ContestDto> getContestsForAdmin(int contestId, int contestStatus, String ContestType, String matchTitle, int limit) throws Exception {

		String query = "select c.id,c.master_contest_id,c.match_id,c.tournament_id,c.fantasy_currency,c.no_of_participants,"
				+ "fm.match_title,c.minimum_participants,c.no_of_winners,c.contest_type,c.contest_name,c.entry_fee,"
				+ "c.total_prize_amount,c.total_coins,c.max_bonus_amount,c.max_entries,c.invite_code,"
				+ "c.status,c.created_at,c.updated_at,c.auto_create_contest,c.contest_level,"
				+ "no_of_users_joined from contests c,fantasy_matches fm where c.match_id=fm.id ";

		if (contestId > 0) {
			query = query + " and c.id = ?";
		}
		if (!CommonUtility.isNullOrEmpty(ContestType)) {
			query = query + " and c.contest_type = ?";
		}
		if (contestStatus > 0) {
			query = query + " and c.status = ?";
		}
		if (!CommonUtility.isNullOrEmpty(matchTitle)) {
			query = query + " and fm.match_title = ? ";
		}
		query = query + " order by c.id desc ";
		if(limit > 0) {
			query = query + " limit ? ";
		}
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContestDto> contestsList = new ArrayList<ContestDto>();
		ContestDto dbContest = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			if (contestId > 0) {
				pst.setInt(index++, contestId);
			}
			if (!CommonUtility.isNullOrEmpty(ContestType)) {
				pst.setString(index++, ContestType);
			}
			if (contestStatus > 0) {
				pst.setInt(index++, contestStatus);
			}
			if (!CommonUtility.isNullOrEmpty(matchTitle)) {
				pst.setString(index++, matchTitle);
			}
			if(limit > 0) {
				pst.setInt(index++, limit);
			}
			rs = pst.executeQuery();

			while (rs.next()) {
				dbContest = new ContestDto();
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
	
	protected List<Map<String, String>> getContestsStatsForAdminByMatch(int matchId, String fantasyCurrency) throws Exception {

		String query = " SELECT c.match_id, c.master_contest_id master, c.contest_type, c.fantasy_currency currency, COUNT(*) AS contests, "
				+ " SUM(IF(c.status = 5, 1, 0)) AS canceled, (SELECT COUNT(distinct user_id) FROM contest_participants cp, contests ct "
				+ " WHERE ct.match_id = ? and cp.contest_id = ct.id  and ct.master_contest_id = c.master_contest_id and ct.contest_type = c.contest_type) users, "
				+ " (SELECT COUNT(distinct user_id) FROM contest_participants cp, contests ct WHERE ct.match_id = ? and cp.contest_id = ct.id  and ct.master_contest_id = c.master_contest_id "
				+ " and ct.contest_type = c.contest_type and user_id not in (SELECT distinct user_id FROM contest_participants cp, contests ct where cp.contest_id = ct.id and ct.match_id < ?)) new_users, "
				+ " (SELECT COUNT(*) FROM contest_participants cp, contests ct WHERE ct.match_id = ? and cp.contest_id = ct.id "
				+ " and ct.master_contest_id = c.master_contest_id and ct.contest_type = c.contest_type) entries FROM contests c "
				+ " WHERE c.match_id = ? ";
		
		if(!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
			query += " and c.fantasy_currency = ?";
		}
		query += " GROUP BY c.match_id , c.master_contest_id , c.contest_type,c.fantasy_currency order by currency desc, entries desc; ";

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, matchId);
			pst.setInt(2, matchId);
			pst.setInt(3, matchId);
			pst.setInt(4, matchId);
			pst.setInt(5, matchId);
			if(!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
				pst.setString(6, fantasyCurrency);
			}
			rs = pst.executeQuery();
			Map<String, String> map = null;
			while (rs.next()) {
				
				map = new HashMap<String, String>();
				map.put("match_id", rs.getString("match_id"));
				map.put("master", rs.getString("master"));
				map.put("contest_type", rs.getString("contest_type"));
				map.put("currency", rs.getString("currency"));
				map.put("contests", rs.getString("contests"));
				map.put("canceled", rs.getString("canceled"));
				map.put("users", rs.getString("users"));
				map.put("entries", rs.getString("entries"));
				map.put("newUsers", rs.getString("new_users"));
				res.add(map);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return res;
	}

	protected List<Map<String, String>> getContestsStatsForAdmin(String fantasyCurrency) throws Exception {

		String query = "SELECT fm.id, fm.match_title, fm.match_time time, COUNT(c.id) AS contests, SUM(IF(c.status = 5, 1, 0)) AS canceled, "
				+ "(SELECT COUNT(distinct user_id) FROM contest_participants cp, contests ct WHERE cp.contest_id = ct.id and ct.match_id = fm.id) users, "
				+ "(SELECT COUNT(*) FROM contest_participants cp, contests ct WHERE cp.contest_id = ct.id and ct.match_id = fm.id) entries from fantasy_matches fm, "
				+ "contests c where fm.id = c.match_id "; 
		
		if(!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
			query += " and c.fantasy_currency = ? " ;
		}
		query += " group by fm.id, fm.match_title, fm.match_date, fm.match_time order by match_date desc limit 100; ";
		
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			if(!CommonUtility.isNullOrEmpty(fantasyCurrency)) {
				pst.setString(1, fantasyCurrency);
			}
			rs = pst.executeQuery();
			Map<String, String> map = null;
			while (rs.next()) {
				
				map = new HashMap<String, String>();
				map.put("matchId", rs.getString("id"));
				map.put("matchTitle", rs.getString("match_title"));
				map.put("time", rs.getString("time"));
				map.put("contests", rs.getString("contests"));
				map.put("canceled", rs.getString("canceled"));
				map.put("users", rs.getString("users"));
				map.put("entries", rs.getString("entries"));
				res.add(map);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return res;
	}
	
	protected List<ContestDto> getUserContests(long userId, int matchId, int matchStatus) throws Exception {

		String query = " SELECT c.id,c.match_id,no_of_participants,minimum_participants,no_of_winners,contest_type,contest_name, "
				+ " entry_fee,max_bonus_amount,fantasy_currency,total_prize_amount,total_coins,max_entries,c.status,invite_code,no_of_users_joined, "
				+ "c.contest_level, count(*) no_of_user_entries, min(cp.rank) user_rank ";
		
		if(MatchesFantasyFactory.ARCHIVED == matchStatus) {
			query += " FROM contests_bkp c, contest_participants_bkp ";
		}else {
			query += " FROM contests c, contest_participants ";
		}
		query +=  " cp  WHERE c.id = cp.contest_id and c.status in (1,3,4,5,6) " ;
		
		if(userId>0) {
			query = query+" and cp.user_id = ?";
		}
		if(matchId>0) {
			query = query+" and c.match_id = ?";
		}
		
		query = query+" group by c.id order by c.id desc ";

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContestDto> contestsList = new ArrayList<ContestDto>();
		ContestDto contestDto = null;
		try {
			pst = conn.prepareStatement(query);
			if (userId > 0) {
				pst.setLong(1, userId);
			}
			if (matchId > 0) {
				pst.setLong(2, matchId);
			}
			rs = pst.executeQuery();
			while (rs.next()) {

				contestDto = new ContestDto();

				contestDto.setId(rs.getInt("id"));
				contestDto.setMatchId(rs.getInt("match_id"));
				contestDto.setNoOfParticipants(rs.getInt("no_of_participants"));
				contestDto.setMinimumParticipants(rs.getInt("minimum_participants"));
				contestDto.setNoOfWinners(rs.getInt("no_of_winners"));
				contestDto.setContestType(rs.getString("contest_type"));
				contestDto.setContestName(rs.getString("contest_name"));
				contestDto.setEntryFee(rs.getFloat("entry_fee"));
				contestDto.setMaxBonusAmount(rs.getFloat("max_bonus_amount"));
				contestDto.setFantasyCurrency(rs.getString("fantasy_currency"));
				contestDto.setTotalPrizeAmount(rs.getFloat("total_prize_amount"));
				contestDto.setTotalCoins(rs.getInt("total_coins"));
				contestDto.setMaxEntries(rs.getInt("max_entries"));
				contestDto.setStatus(rs.getInt("status"));
				contestDto.setInviteCode(rs.getString("invite_code"));
				contestDto.setNoOfUsersJoined(rs.getInt("no_of_users_joined"));
				contestDto.setNoOfUserEntries(rs.getInt("no_of_user_entries"));
				contestDto.setUserRank(rs.getInt("user_rank"));
				contestDto.setContestLevel(rs.getString("contest_level"));

				contestsList.add(contestDto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestsList;
	}
	
	protected List<Integer> getMasterContestIdsForMatch(int matchId) throws Exception {

		String query = "SELECT distinct(master_contest_id) as master_contest_id "
				+ "FROM contests where match_id ="+matchId;

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Integer> masterContestIds = new ArrayList<Integer>();
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				masterContestIds.add(rs.getInt("master_contest_id"));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return masterContestIds;
	}
	
	protected List<ContestDto> getUserContestsForFantasyWeb(long userId, int matchId, String filterBy, String sortBy) throws Exception {

		String query = " SELECT c.id,c.match_id,no_of_participants,minimum_participants,no_of_winners,contest_type,contest_name, "
				+ " entry_fee,max_bonus_amount,fantasy_currency,total_prize_amount,total_coins,max_entries,c.status,invite_code, "
				+ " no_of_users_joined,c.contest_level, count(*) no_of_user_entries, min(cp.rank) user_rank "
				+ "FROM contests c, contest_participants cp WHERE c.id = cp.contest_id and c.status in (1,3,4,5,6) " ;
		
		if(userId>0) {
			query = query+" and cp.user_id = ?";
		}
		if(matchId>0) {
			query = query+" and c.match_id = ?";
		}
		if(!CommonUtility.isNullOrEmpty(filterBy)) {
			query = query+" and c.contest_type = ?";
		}
		if(!CommonUtility.isNullOrEmpty(sortBy)) {
			query = query+" group by c.id order by "+ sortBy;
		}else {
			query = query+" group by c.id order by c.id desc ";
		}

		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContestDto> contestsList = new ArrayList<ContestDto>();
		ContestDto contestDto = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			if (userId > 0) {
				pst.setLong(index++, userId);
			}
			if (matchId > 0) {
				pst.setLong(index++, matchId);
			}
			if(!CommonUtility.isNullOrEmpty(filterBy)) {
				pst.setString(index++, filterBy);
			}
			rs = pst.executeQuery();
			while (rs.next()) {

				contestDto = new ContestDto();

				contestDto.setId(rs.getInt("id"));
				contestDto.setMatchId(rs.getInt("match_id"));
				contestDto.setNoOfParticipants(rs.getInt("no_of_participants"));
				contestDto.setMinimumParticipants(rs.getInt("minimum_participants"));
				contestDto.setNoOfWinners(rs.getInt("no_of_winners"));
				contestDto.setContestType(rs.getString("contest_type"));
				contestDto.setContestName(rs.getString("contest_name"));
				contestDto.setEntryFee(rs.getFloat("entry_fee"));
				contestDto.setMaxBonusAmount(rs.getFloat("max_bonus_amount"));
				contestDto.setFantasyCurrency(rs.getString("fantasy_currency"));
				contestDto.setTotalPrizeAmount(rs.getFloat("total_prize_amount"));
				contestDto.setTotalCoins(rs.getInt("total_coins"));
				contestDto.setMaxEntries(rs.getInt("max_entries"));
				contestDto.setStatus(rs.getInt("status"));
				contestDto.setInviteCode(rs.getString("invite_code"));
				contestDto.setNoOfUsersJoined(rs.getInt("no_of_users_joined"));
				contestDto.setNoOfUserEntries(rs.getInt("no_of_user_entries"));
				contestDto.setUserRank(rs.getInt("user_rank"));
				contestDto.setContestLevel(rs.getString("contest_level"));

				contestsList.add(contestDto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestsList;
	}

	protected int insertContest(ContestDto contest) throws Exception {

		String query = "insert into contests(master_contest_id,match_id,tournament_id,fantasy_currency,"
				+ "no_of_participants,minimum_participants,no_of_winners,contest_type,contest_name,"
				+ "entry_fee,total_prize_amount,total_coins,max_entries,max_bonus_amount,"
				+ "invite_code,status,auto_create_contest,contest_level,created_at) "
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";

		int contestId = 0;
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

			int index = 1;

			st.setInt(index++, contest.getMasterContestId());
			st.setInt(index++, contest.getMatchId());
			st.setInt(index++, contest.getTournamentId());
			st.setString(index++, contest.getFantasyCurrency());
			st.setInt(index++, contest.getNoOfParticipants());
			st.setInt(index++, contest.getMinimumParticipants());
			st.setInt(index++, contest.getNoOfWinners());
			st.setString(index++, contest.getContestType());
			st.setString(index++, contest.getContestName());
			st.setFloat(index++, contest.getEntryFee());
			st.setFloat(index++, contest.getTotalPrizeAmount());
			st.setInt(index++, contest.getTotalCoins());
			st.setInt(index++, contest.getMaxEntries());
			st.setFloat(index++, contest.getMaxBonusAmount());
			st.setString(index++, contest.getInviteCode());
			st.setInt(index++, contest.getStatus());
			st.setInt(index++, contest.getAutoCreateContest());
			st.setString(index++, contest.getContestLevel());
			
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
	
	protected List<Integer> insertContests(ContestDto contest, int count) throws Exception {

		String query = "insert into contests(master_contest_id,match_id,tournament_id,fantasy_currency,"
				+ "no_of_participants,minimum_participants,no_of_winners,contest_type,contest_name,"
				+ "entry_fee,total_prize_amount,total_coins,max_entries,max_bonus_amount,"
				+ "invite_code,status,auto_create_contest,contest_level,created_at) "
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";

		List<Integer> dbContestIds = new ArrayList<Integer>();
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			conn.setAutoCommit(false);
			
			for (int i=0; i<count; i++) {
				int index = 1;
				
				st.setInt(index++, contest.getMasterContestId());
				st.setInt(index++, contest.getMatchId());
				st.setInt(index++, contest.getTournamentId());
				st.setString(index++, contest.getFantasyCurrency());
				st.setInt(index++, contest.getNoOfParticipants());
				st.setInt(index++, contest.getMinimumParticipants());
				st.setInt(index++, contest.getNoOfWinners());
				st.setString(index++, contest.getContestType());
				st.setString(index++, contest.getContestName());
				st.setFloat(index++, contest.getEntryFee());
				st.setFloat(index++, contest.getTotalPrizeAmount());
				st.setInt(index++, contest.getTotalCoins());
				st.setInt(index++, contest.getMaxEntries());
				st.setFloat(index++, contest.getMaxBonusAmount());
				st.setString(index++, contest.getInviteCode());
				st.setInt(index++, contest.getStatus());
				st.setInt(index++, contest.getAutoCreateContest());
				st.setString(index++, contest.getContestLevel());
				
				st.addBatch();
			}
			
			st.executeBatch();			
			conn.commit();
			rs = st.getGeneratedKeys();

			while (rs.next()) {
				dbContestIds.add( rs.getInt(1));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}

		return dbContestIds;

	}
	
	protected void updateContest(ContestDto contest) throws Exception {

		String query = "update contests set fantasy_currency=?,contest_type=?,no_of_participants=?,no_of_winners=?,entry_fee=?,"
				+ "total_prize_amount=?,total_coins=?,max_entries=?,max_bonus_amount=?, minimum_participants = ?, "
				+ " status=?, auto_create_contest = ?, contest_level=? where  id=?";

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);

			int index = 1;

			st.setString(index++, contest.getFantasyCurrency());
			st.setString(index++, contest.getContestType());
			st.setInt(index++, contest.getNoOfParticipants());
			st.setInt(index++, contest.getNoOfWinners());
			st.setFloat(index++, contest.getEntryFee());
			st.setFloat(index++, contest.getTotalPrizeAmount());
			st.setInt(index++, contest.getTotalCoins());
			st.setInt(index++, contest.getMaxEntries());
			st.setFloat(index++, contest.getMaxBonusAmount());
			st.setInt(index++, contest.getMinimumParticipants());
			st.setInt(index++, contest.getStatus());
			st.setInt(index++, contest.getAutoCreateContest());
			st.setString(index++, contest.getContestLevel());
			st.setInt(index++, contest.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected void updateContestStatus(int contestId, int status) throws Exception {

		String query = "update contests set status = ? where 1=1 ";
		
		if(contestId>0) {
			query = query+" and id = ?";
		}
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			int index = 1;
			if(status>0) {
				st.setInt(index++, status);
			}
			if(contestId>0) {
				st.setInt(index++, contestId);
			}
			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);;
		}
	}	
	protected void updateAllContestStatusForMatch(int matchId, int status) throws Exception {

		String query = "update contests set status = ? where match_id = ? ";
		
		if(status == 6 ) {
			query = query+" and status = 4 ";	
		}
		if(status == 3 ) {
			query = query+" and status = 6 ";	
		}
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			int index = 1;
			if(status>0) {
				st.setInt(index++, status);
			}
			st.setInt(index++, matchId);
			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);;
		}
	}
	
	protected void updateSeriesLevelContestStatus(int tournamentId, int status) throws Exception {

		String query = "update contests set status = ? where contest_level = 'series' and status = 4 "
				+ " and match_id in (select id from fantasy_matches where tournament_id = ?) ";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			
			int index = 1;
			
			if(status>0) {
				st.setInt(index++, status);
			}
			if(tournamentId>0) {
				st.setInt(index++, tournamentId);
			}
			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);;
		}
	}
	
	protected int getContestByMatchAndMasterId(int masterContestId, int matchId) throws Exception {

		String query = " SELECT id FROM contests c where 1=1 ";

		if (masterContestId>0) {
			query = query + " and master_contest_id = ?";
		}
		if (matchId>0) {
			query = query + " and match_id = ?";
		}
		
		query = query + " limit 1";
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		int contestId = 0;
		
		try {
			pst = conn.prepareStatement(query);

			int index = 1;

			if (masterContestId>0) {
				pst.setInt(index++, masterContestId);
			}
			if (matchId>0) {
				pst.setInt(index++, matchId);
			}

			rs = pst.executeQuery();

			while (rs.next()) {
				contestId = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestId;
	}
	
	protected void deleteContest(int contestId) throws Exception {

		String query = "delete from contests where id = ?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement(query);
			if(contestId > 0) {
				pst.setInt(1, contestId);
			}
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected void activateInactivateContests(String contestIds, int status) throws Exception {

		String query = "update contests set status=? where id in ("+contestIds+")";

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement(query);
			if(status > 0) {
				pst.setInt(1, status);
			}
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected String distributePrizeAmount(int matchId) throws Exception {

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		PreparedStatement st1 = null;
		ResultSet rs1 = null;
		String message = null;
		
		try {
			String query = "call distribute_prize_amount_mx(?,'CASH',@message);";			
			st = conn.prepareStatement(query);			
			st.setInt(1, matchId);			
			rs = st.executeQuery();
			query = "call distribute_prize_amount_mx(?,'COINS',@message);";
			st1 = conn.prepareStatement(query);			
			st1.setInt(1, matchId);	
			rs1 = st1.executeQuery();
			while (rs1.next()) {
				message = rs1.getString(1);
			}
			
		} catch (Exception e) {	
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(st1);
			DButility.closeRs(rs1);
			DButility.dbCloseAll(conn, st, rs);
		}
		return message;
	}
	
	protected String updateContestsStatusAfterMatchStarted(int matchId) throws Exception {

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		String message = null;
		
		try {
			String query = "call update_contest_status_after_match_started(?,@message);";			
			st = conn.prepareStatement(query);			
			st.setInt(1, matchId);			
			rs = st.executeQuery();
			
			while (rs.next()) {
				message = rs.getString(1);
			}
			
		} catch (Exception e) {	
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return message;
	}
	
	protected String updateContestsStatusAfterMatchStarted(int matchId, int masterContestId) throws Exception {

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		String message = null;
		
		try {
			String query = "call update_contest_status_after_match_started_v1(?,?,@message);";			
			st = conn.prepareStatement(query);			
			st.setInt(1, matchId);	
			st.setInt(2, masterContestId);	
			rs = st.executeQuery();
			
			while (rs.next()) {
				message = rs.getString(1);
			}
			
		} catch (Exception e) {	
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return message;
	}
	
	protected String adjustContestParticipants(int matchId) throws Exception {

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		String message = null;
		
		try {
			String query = "call adjust_contest_participants(?,@message);";			
			st = conn.prepareStatement(query);			
			st.setInt(1, matchId);			
			rs = st.executeQuery();
			
			while (rs.next()) {
				message = rs.getString(1);
			}
			
		} catch (Exception e) {	
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return message;
	}
	
	protected String adjustContestParticipants(int matchId, int masterContestId) throws Exception {

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		String message = null;
		
		try {
			String query = "call adjust_contest_participants_v1(?,?,@message);";			
			st = conn.prepareStatement(query);			
			st.setInt(1, matchId);
			st.setInt(2, masterContestId);
			rs = st.executeQuery();
			
			while (rs.next()) {
				message = rs.getString(1);
			}
			
		} catch (Exception e) {	
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return message;
	}
	
	protected String updateSeriesLevelContestsStatusAfterMatchStarted(int tournamentId) throws Exception {

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		String message = null;
		
		try {
			String query = "call update_series_level_contest_status_after_match_started(?,@message);";			
			st = conn.prepareStatement(query);			
			st.setInt(1, tournamentId);			
			rs = st.executeQuery();
			
			while (rs.next()) {
				message = rs.getString(1);
			}
			
		} catch (Exception e) {	
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return message;
	}
	
	protected Map<Integer, String> getContestStatusMap() throws Exception {

		String query = " SELECT value, status from contest_status";
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer, String> contestStatusMap = new HashMap<Integer, String>();		
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				contestStatusMap.put(rs.getInt(1),rs.getString(2));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return contestStatusMap;
	}
	
	protected String teamEntryForAutoCreateContest(int oldContestId, int matchId, int matchTeamId, long userId, 
			int masterContestId, String contestType, String fantasyCurrency) throws Exception {

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		String cpId = null;
		
		try {
			String query = "call team_entry_for_auto_create_contest(?,?,?,?,?,?,?,@contestParticipantId);";			
			st = conn.prepareStatement(query);	
			
			int index = 1;
			st.setInt(index++, oldContestId);			
			st.setInt(index++, matchId);
			st.setInt(index++, matchTeamId);
			st.setLong(index++, userId);
			st.setInt(index++, masterContestId);
			st.setString(index++, contestType);
			st.setString(index++, fantasyCurrency);			
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				cpId = rs.getString(1);
			}
			
		} catch (Exception e) {	
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return cpId;
	}
	
	protected void insertFantasyScoreCardError(String desc, String message) throws Exception {

		String query = "insert into procedure_debug_log(message,value) values(?,?)";
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DButility.getFantasyWriteConnection("ccfantasy");
			st = conn.prepareStatement(query);
			st.setString(1, desc);
			st.setString(2, message);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected Integer getUserCountByMasterContestId(int seriesId, int masterContestid, int newUserCount) throws Exception {
		
		String query = " SELECT COUNT(DISTINCT(cp.user_id)) FROM contest_participants_bkp cp, contests_bkp c "
				+ "WHERE cp.contest_id = c.id AND c.fantasy_currency = 'GEMS' "
				+ "AND c.tournament_id=? AND master_contest_id = ? AND cp.user_id ";
		if(newUserCount == 1) {
			query += " NOT IN ";
		}else {
			query += " IN ";
		}
		query += "(SELECT cp.user_id FROM contest_participants_bkp cp, contests_bkp c "
				+ "WHERE cp.contest_id = c.id AND c.fantasy_currency = 'GEMS' "
				+ "AND c.tournament_id<? AND c.tournament_id>66 AND master_contest_id=?)  ";
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer noOfUsers = 0;
		try {
			conn = DButility.getFantasyReadConnection("ccfantasy");
			pst = conn.prepareStatement(query);

			int index = 1;			
			pst.setInt(index++, seriesId);
			pst.setInt(index++, masterContestid);
			pst.setInt(index++, seriesId);			
			pst.setInt(index++, masterContestid);
			rs = pst.executeQuery();
			while (rs.next()) {
				noOfUsers = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return noOfUsers;
	}
	
}
