/*
 * Created on Mar 27, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.PlayerDto;
import com.cricket.dto.TeamDetailsDto;
import com.cricket.dto.TeamDto;
import com.cricket.dto.UserDto;
import com.cricket.exception.CCErrorConstant;
import com.cricket.exception.CCException;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;
import com.cricket.utility.MatchUtility;

/**
 * @author ganesh
 * 
 */
public class TeamDAO {

	private static Logger log = LoggerFactory.getLogger(TeamDAO.class);

	protected int insertTeam(TeamDto team, int clubId) throws Exception {

		String teamName = DButility.escapeQuotes(team.getTeamName());
		String teamCode = DButility.escapeQuotes(team.getTeamCode());
		int captain = team.getCaptain();
		int viceCaptain = team.getViceCaptain();
		int wicketKeeper = team.getWicketKeeper();
		String isActive = team.getIsActive();
		String league = team.getLeague();
		List players = team.getPlayersList();
		int group = team.getGroup();
		int groundId = team.getGroundId();
		String logo_file_path = team.getLogo_file_path() == null ? "" : team.getLogo_file_path();
		double penalty_NRR = team.getPenaltyNRR();

		double super_penalty_NRR = team.getsuperPenaltyNRR();
		float rating = team.getRating();
		String penalty_NRR_reason = team.getPenaltyNRRReason() == null ? "" : team.getPenaltyNRRReason();
		String super_penalty_NRR_reason = team.getsuperPenaltyNRRReason() == null ? ""
				: team.getsuperPenaltyNRRReason();

		group = (group > 0) ? group : 1;

		String query = "insert into team(team_name,team_code,captain,vice_captain,wicket_keeper,"
				+ " is_active,date_created,league,"
				+ " group_id,information,club_id, HomeGround, logo_file_path, penalty_NRR,"
				+ " penalty_NRR_reason, super_penalty_NRR,super_penalty_NRR_reason,team_status, custom_team_id, rating,coach_id) values (?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String queryTeamPlayer = "insert into team_player(team_id,player_id) values (?,?)";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st1 = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		try {
			st1 = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			int index = 1;

			st1.setString(index++, teamName);
			st1.setString(index++, teamCode);
			st1.setInt(index++, captain);
			st1.setInt(index++, viceCaptain);
			st1.setInt(index++, wicketKeeper);
			st1.setString(index++, isActive);
			st1.setString(index++, league);
			st1.setInt(index++, group);
			st1.setString(index++, DButility.escapeLine(team.getInformation()));
			st1.setInt(index++, team.getClubId());
			st1.setInt(index++, groundId);
			st1.setString(index++, logo_file_path);
			st1.setDouble(index++, penalty_NRR);
			st1.setString(index++, penalty_NRR_reason);
			// code chnge super_
			st1.setDouble(index++, super_penalty_NRR);
			st1.setString(index++, super_penalty_NRR_reason);

			st1.setString(index++, team.getTeamStatus());
			st1.setString(index++,
					CommonUtility.isNullOrEmptyOrNULL(team.getCustom_team_id()) ? "0" : team.getCustom_team_id());
			st1.setFloat(index++, rating);
			st1.setInt(index++, team.getCoachId());
			st1.executeUpdate();

			rs = st1.getGeneratedKeys();
			int teamId = 0;
			if (rs.next()) {
				teamId = rs.getInt(1);
			}
			if (!CommonUtility.isListNullEmpty(players)) {

				pst = conn.prepareStatement(queryTeamPlayer);

				for (int i = 0; i < players.size(); i++) {
					pst.setInt(1, teamId);
					pst.setInt(2, Integer.parseInt((String) players.get(i)));
					pst.addBatch();
				}
				pst.executeBatch();

				for (int j = 0; j < players.size(); j++) {
					auditTeamPlayer((String) players.get(j), ("" + teamId), team.getUpdatedBy(), "insert", conn);
				}
			}
			teamAudit(teamId, clubId, "insert", team.getUpdatedBy(), conn);
			return teamId;

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(pst);
			DButility.dbCloseAll(conn, st1, rs);
		}
	}

	public static void teamAudit(int fixtureId, int clubId, String operation, String userId, Connection conn)
			throws Exception {

		PreparedStatement st = null;
		PreparedStatement st1 = null;
		PreparedStatement st2 = null;
		ResultSet rs = null;
		String new_id = null;
		if (conn == null) {
			conn = DButility.getConnection(clubId);
		}
		try {
			String insertQuery = "insert into team_audit (team_id, team_name, captain, vice_captain, wicket_keeper, is_active, "
					+ "date_created, league, group_id, penalty, is_locked, penalty_reason, information, "
					+ "club_id, HomeGround, logo_file_path, team_code, penalty_NRR, penalty_NRR_reason, rating, back_ground_image_path,coach_id)"
					+ "(select team_id, team_name, captain, vice_captain, wicket_keeper, is_active, date_created, league, group_id, "
					+ "penalty, is_locked, penalty_reason, information, club_id, HomeGround, logo_file_path, team_code, "
					+ "penalty_NRR, penalty_NRR_reason, rating, back_ground_image_path,coach_id from team where team_id =? )";
			st = conn.prepareStatement(insertQuery);
			st.setInt(1, fixtureId);
			st.executeUpdate();
			String GetID = "select LAST_INSERT_ID() as latest_id";
			st1 = conn.prepareStatement(GetID);
			rs = st1.executeQuery();
			while (rs.next()) {
				new_id = rs.getString("latest_id");
			}
			String updateQuery = "update team_audit set change_date = now(), change_type=?, change_by=? where change_id = ? ";
			st2 = conn.prepareStatement(updateQuery);
			st2.setString(1, operation);
			st2.setString(2, userId);
			st2.setString(3, new_id);
			st2.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}
			DButility.closeStatement(st1);
			DButility.closeStatement(st2);
			DButility.closeConnectionAndStatement(conn, st);
		}
	}

	/**
	 * @param internalClubIds
	 * @return
	 */
	protected List<TeamDto> getTeamsByInternalClub(TeamDto teamConstraint, int clubId, List<Integer> internalClubIds)
			throws Exception {
		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "select team_id,team_name,team_code,captain,vice_captain,wicket_keeper,t.is_active,date_created ,"
				+ " league,CONCAT(p1.f_name , ' ',p1.l_name) captain_name, p1.email c_email,p2.email vc_email,"
				+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id,t.penalty, t.penalty_reason, "
				+ "t.penalty_NRR, t.penalty_NRR_reason, t.information, t.is_locked, t.club_id,  c.name club_name, t.HomeGround, "
				+ " t.logo_file_path, t.rating "
				+ " from team t left outer join mcc.player p1 on(p1.player_id = t.captain) "
				+ "left outer join mcc.player p2 on (p2.player_id = t.vice_captain) "
				+ "left outer join internal_club c on (c.club_id = t.club_id) where 1=1 and t.team_status = 1 ";

		String orderBy = "";
		if (teamConstraint != null) {
			if (teamConstraint.getTeamID() != 0) {
				query += " and team_id = ?";
			}

			if (teamConstraint.getClubId() != 0) {
				query += " and t.club_id = ?";
			}

			if (teamConstraint.getPlayerId() != 0) {
				query += " and team_id in (select team_id from team_player where is_secondary is null and player_id = ?)";
				orderBy = " order by team_id desc";
			}

			if (teamConstraint.getLeague() != null && !"".equals(teamConstraint.getLeague().trim())
					&& CommonUtility.stringToInt(teamConstraint.getLeague()) != 0) {
				query += " and league = ?";
			}

		}
		if (internalClubIds != null && !internalClubIds.isEmpty()) {
			StringBuilder internalClubIdsS = new StringBuilder();
			for (int i = 0; i < internalClubIds.size(); i++) {
				if (i != 0) {
					internalClubIdsS.append(',');
				}
				internalClubIdsS.append(internalClubIds.get(i));
			}
			query += " and t.club_id in ( " + internalClubIdsS + " ) ";
		}

		if (orderBy.equals("")) {
			orderBy = " order by team_name";
		}
		query += orderBy;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			int index = 1;
			if (teamConstraint != null) {
				if (teamConstraint.getTeamID() != 0) {
					pst.setInt(index++, teamConstraint.getTeamID());
				}
				if (teamConstraint.getClubId() != 0) {
					pst.setInt(index++, teamConstraint.getClubId());

				}
				if (teamConstraint.getPlayerId() != 0) {
					pst.setInt(index++, teamConstraint.getPlayerId());
				}
				if (teamConstraint.getLeague() != null && !"".equals(teamConstraint.getLeague().trim())
						&& CommonUtility.stringToInt(teamConstraint.getLeague()) != 0) {
					pst.setString(index++, teamConstraint.getLeague());
				}
			}
			rs = pst.executeQuery();
			while (rs.next()) {

				TeamDto team = new TeamDto();

				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setCaptain(rs.getInt("captain"));
				team.setViceCaptain(rs.getInt("vice_captain"));
				team.setWicketKeeper(rs.getInt("wicket_keeper"));
				team.setCaptainName(rs.getString("captain_name"));
				team.setViceCaptainName(rs.getString("vice_captain_name"));
				team.setLeague(rs.getString("league"));
				team.setGroup(rs.getInt("group_id"));
				team.setLocked(rs.getInt("is_locked") == 1);
				team.setPenalty(rs.getFloat("penalty"));
				team.setPenaltyReason(rs.getString("penalty_reason"));
				team.setPenaltyNRR(rs.getDouble("penalty_NRR"));
				team.setPenaltyNRRReason(rs.getString("penalty_NRR_reason"));
				team.setInformation(rs.getString("information"));
				team.setCaptainEmail(rs.getString("c_email"));
				team.setViceCaptainEmail(rs.getString("vc_email"));
				team.setClubId(rs.getInt("club_id"));
				team.setClubName(rs.getString("club_name"));
				team.setGroundId(rs.getInt("HomeGround"));
				team.setLogo_file_path(rs.getString("logo_file_path"));
				team.setRating(rs.getFloat("rating"));

				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}

	protected List<TeamDto> getTeams(TeamDto teamConstraint, int clubId) throws Exception {

		return getTeams(teamConstraint, clubId, false);

	}

	/**
	 * @return
	 */
	protected List<TeamDto> getTeamsforClubs(TeamDto teamConstraint, int clubId, boolean isPreview) throws Exception {

		// getTeamsforClubs
		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "";
		query = "SELECT tp.team_id, cp.internal_club_id FROM team_player tp, club_player cp, team t "
				+ "WHERE tp.player_id = ? AND tp.player_id= cp.player_id AND t.team_id = tp.team_id "
				+ "AND t.club_id = cp.internal_club_id";

		if (teamConstraint.getPlayerId() != 0) {
			Connection conn = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				conn = DButility.getReadConnection(clubId);
				pst = conn.prepareStatement(query);
				int index = 1;
				if (teamConstraint != null) {
					if (teamConstraint.getPlayerId() != 0) {
						pst.setInt(index++, teamConstraint.getPlayerId());
					}

				}

				rs = pst.executeQuery();
				while (rs.next()) {

					TeamDto team = new TeamDto();
					team.setPlayerId(rs.getInt("player_id"));
					teams.add(team);

				}

			} catch (Exception e) {

			}

		}
		return null;
	}

	protected List<TeamDto> getTeams(TeamDto teamConstraint, int clubId, boolean isPreview) throws Exception {
		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "";
		if (clubId == 14202) {
			query = "select t.team_id,t.team_name,t.team_code,t.captain,t.vice_captain,t.wicket_keeper,t.is_active,date_created, "
					+ "league,td.admin_name , p1.email c_email, p2.email vc_email,CONCAT(p1.f_name , ' ',p1.l_name) captain_name,"
					+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id,t.penalty, t.penalty_reason, t.penalty_NRR, "
					+ "t.penalty_NRR_reason, t.super_penalty, t.super_penalty_reason, t.super_penalty_NRR,t.super_penalty_NRR_reason, "
					+ "t.information, t.is_locked, t.club_id,  c.name club_name, t.HomeGround, t.logo_file_path, t.rating, t.back_ground_image_path "
					+ " from team t left outer join mcc.player p1 on(p1.player_id = t.captain) "
					+ "left outer join mcc.player p2 on (p2.player_id = t.vice_captain) "
					+ "left outer join internal_club c on (c.club_id = t.club_id) "
					+ "left outer join team_details td on (td.team_id = t.team_id)  " + "where 1=1 ";

		} else {
			query = "select team_id,team_name,team_code,captain,vice_captain,wicket_keeper,t.is_active,date_created, "
					+ "league,CONCAT(p1.f_name , ' ',p1.l_name) captain_name, p1.email c_email,p2.email vc_email,"
					+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id,t.penalty, t.penalty_reason, t.penalty_NRR, "
					+ "t.penalty_NRR_reason, t.super_penalty, t.super_penalty_reason, t.super_penalty_NRR,t.super_penalty_NRR_reason, "
					+ "t.information, t.is_locked, t.club_id,  c.name club_name, t.HomeGround, t.logo_file_path, t.rating, t.back_ground_image_path,t.coach_id"
					+ " from team t left outer join mcc.player p1 on(p1.player_id = t.captain) "
					+ "left outer join mcc.player p2 on (p2.player_id = t.vice_captain) "
					+ "left outer join internal_club c on (c.club_id = t.club_id)  where 1=1 ";
		}

		if (!isPreview) {
			query += " and team_status = 1 ";
		}

		String orderBy = "";
		if (teamConstraint != null) {
			if (teamConstraint.getTeamID() != 0) {
				query += " and t.team_id = ?";
			}

			if (teamConstraint.getClubId() != 0) {
				query += " and t.club_id = ?";
			}

			if (teamConstraint.getPlayerId() != 0) {
				query += " and t.team_id in (select team_id from team_player where is_secondary is null and player_id = ?)";
				orderBy = " order by t.team_id desc";
			}

			if (teamConstraint.getLeague() != null && !"".equals(teamConstraint.getLeague().trim())
					&& !"all".equalsIgnoreCase(teamConstraint.getLeague())) {
				if (CommonUtility.stringToInt(teamConstraint.getLeague()) != 0) {
					query += " and league = ?";
				} else if (CommonUtility.isOnlyDigitInString(teamConstraint.getLeague())) {
					query += " and league in (" + teamConstraint.getLeague() + ")";
				}
			}

			/*
			 * if (teamConstraint.getLeague() != null &&
			 * !"".equals(teamConstraint.getLeague().trim()) &&
			 * CommonUtility.stringToInt(teamConstraint.getLeague()) != 0) { query +=
			 * " and league = ?"; }
			 * 
			 * if(teamConstraint.getLeague() != null &&
			 * !"".equals(teamConstraint.getLeague().trim()) &&
			 * CommonUtility.isOnlyDigitInString(teamConstraint.getLeague()) ) { query +=
			 * " and league in (" + teamConstraint.getLeague() + ")"; }
			 */

		}
		if (orderBy.equals("")) {
			orderBy = " order by club_name, team_name";
		}
		query += orderBy;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			int index = 1;
			if (teamConstraint != null) {
				if (teamConstraint.getTeamID() != 0) {
					pst.setInt(index++, teamConstraint.getTeamID());
				}
				if (teamConstraint.getClubId() != 0) {
					pst.setInt(index++, teamConstraint.getClubId());

				}
				if (teamConstraint.getPlayerId() != 0) {
					pst.setInt(index++, teamConstraint.getPlayerId());
				}
				if (teamConstraint.getLeague() != null && !"".equals(teamConstraint.getLeague().trim())
						&& CommonUtility.stringToInt(teamConstraint.getLeague()) != 0) {
					pst.setString(index++, teamConstraint.getLeague());
				}
			}
			rs = pst.executeQuery();
			while (rs.next()) {

				TeamDto team = new TeamDto();

				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setCaptain(rs.getInt("captain"));
				team.setViceCaptain(rs.getInt("vice_captain"));
				team.setWicketKeeper(rs.getInt("wicket_keeper"));
				team.setCaptainName(rs.getString("captain_name"));
				team.setViceCaptainName(rs.getString("vice_captain_name"));
				team.setLeague(rs.getString("league"));
				team.setGroup(rs.getInt("group_id"));
				team.setLocked(rs.getInt("is_locked") == 1);
				team.setPenalty(rs.getFloat("penalty"));
				team.setPenaltyReason(rs.getString("penalty_reason"));
				team.setPenaltyNRR(rs.getDouble("penalty_NRR"));
				team.setPenaltyNRRReason(rs.getString("penalty_NRR_reason"));
				team.setsuperPenalty(rs.getFloat("super_penalty"));
				team.setsuperPenaltyReason(rs.getString("super_penalty_reason"));
				team.setsuperPenaltyNRR(rs.getDouble("super_penalty_NRR"));
				team.setsuperPenaltyNRRReason(rs.getString("super_penalty_NRR_reason"));
				team.setInformation(rs.getString("information"));
				team.setCaptainEmail(rs.getString("c_email"));
				team.setViceCaptainEmail(rs.getString("vc_email"));
				team.setClubId(rs.getInt("club_id"));
				team.setClubName(rs.getString("club_name"));
				team.setGroundId(rs.getInt("HomeGround"));
				team.setLogo_file_path(rs.getString("logo_file_path"));
				team.setRating(rs.getFloat("rating"));
				team.setBackGroundImagePath(rs.getString("back_ground_image_path"));
				team.setCreationDate(rs.getDate("date_created"));
				team.setCoachId(rs.getInt("coach_id"));
				

				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}
	
	protected List<TeamDto> getTeamByPlayerIdOman(int playerId, int clubId) throws Exception {
		
		List<TeamDto> teams = new ArrayList<TeamDto>();
		
		String query = "SELECT t.team_id,t.team_name,t.team_code,t.captain,t.vice_captain,t.wicket_keeper,t.is_active,date_created, "
				+ "league,td.admin_name , p1.email c_email, p2.email vc_email,CONCAT(p1.f_name , ' ',p1.l_name) captain_name, "
				+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id,t.penalty, t.penalty_reason, t.penalty_NRR, "
				+ "t.penalty_NRR_reason, t.super_penalty, t.super_penalty_reason, t.super_penalty_NRR,t.super_penalty_NRR_reason,  "
				+ "t.information, t.is_locked, t.club_id,  c.name club_name, t.HomeGround, t.logo_file_path, t.rating, t.back_ground_image_path  "
				+ "FROM team t LEFT OUTER JOIN mcc.player p1 ON(p1.player_id = t.captain)  "
				+ "LEFT OUTER JOIN mcc.player p2 ON (p2.player_id = t.vice_captain)  "
				+ "LEFT OUTER JOIN internal_club c ON (c.club_id = t.club_id) "
				+ "LEFT OUTER JOIN team_details td ON (td.team_id = t.team_id)   "
				+ "WHERE team_status = 1  AND t.team_id IN (SELECT team_id FROM team_player WHERE is_secondary IS NULL AND player_id = ?)  "
				+ "AND t.league IN (SELECT league_id FROM league WHERE parent IN (178,195)) "
				+ "ORDER BY t.team_id DESC; ";
		
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setInt(1, playerId);			
			rs = pst.executeQuery();
			while (rs.next()) {

				TeamDto team = new TeamDto();

				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setCaptain(rs.getInt("captain"));
				team.setViceCaptain(rs.getInt("vice_captain"));
				team.setWicketKeeper(rs.getInt("wicket_keeper"));
				team.setCaptainName(rs.getString("captain_name"));
				team.setViceCaptainName(rs.getString("vice_captain_name"));
				team.setLeague(rs.getString("league"));
				team.setGroup(rs.getInt("group_id"));
				team.setLocked(rs.getInt("is_locked") == 1);
				team.setPenalty(rs.getFloat("penalty"));
				team.setPenaltyReason(rs.getString("penalty_reason"));
				team.setPenaltyNRR(rs.getDouble("penalty_NRR"));
				team.setPenaltyNRRReason(rs.getString("penalty_NRR_reason"));
				team.setsuperPenalty(rs.getFloat("super_penalty"));
				team.setsuperPenaltyReason(rs.getString("super_penalty_reason"));
				team.setsuperPenaltyNRR(rs.getDouble("super_penalty_NRR"));
				team.setsuperPenaltyNRRReason(rs.getString("super_penalty_NRR_reason"));
				team.setInformation(rs.getString("information"));
				team.setCaptainEmail(rs.getString("c_email"));
				team.setViceCaptainEmail(rs.getString("vc_email"));
				team.setClubId(rs.getInt("club_id"));
				team.setClubName(rs.getString("club_name"));
				team.setGroundId(rs.getInt("HomeGround"));
				team.setLogo_file_path(rs.getString("logo_file_path"));
				team.setRating(rs.getFloat("rating"));
				team.setBackGroundImagePath(rs.getString("back_ground_image_path"));
				team.setCreationDate(rs.getDate("date_created"));
				

				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}


	protected List<TeamDto> getFromAllTeams(TeamDto teamConstraint, int clubId) throws Exception {
		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "select team_id,team_name,team_code,captain,vice_captain,wicket_keeper,t.is_active,date_created ,"
				+ "league,CONCAT(p1.f_name , ' ',p1.l_name) captain_name, p1.email c_email,p2.email vc_email,"
				+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id,t.penalty, t.penalty_reason, t.penalty_NRR, "
				+ "t.penalty_NRR_reason, t.super_penalty, t.super_penalty_reason, t.super_penalty_NRR,t.super_penalty_NRR_reason, "
				+ " t.information, t.is_locked, t.club_id,  c.name club_name, "
				+ " t.HomeGround, t.logo_file_path, t.rating, t.back_ground_image_path "
				+ " from team t left outer join mcc.player p1 on(p1.player_id = t.captain) "
				+ "left outer join mcc.player p2 on (p2.player_id = t.vice_captain) "
				+ " left outer join internal_club c on (c.club_id = t.club_id) where 1=1  ";

		String orderBy = "";
		if (teamConstraint != null) {
			if (teamConstraint.getTeamID() != 0) {
				query += " and team_id = ?";
			}

			if (teamConstraint.getClubId() != 0) {
				query += " and t.club_id = ?";
			}

			if (teamConstraint.getPlayerId() != 0) {
				query += " and team_id in (select team_id from team_player where is_secondary is null and player_id = ?)";
				orderBy = " order by team_id desc";
			}

			if (teamConstraint.getLeague() != null && !"".equals(teamConstraint.getLeague().trim())
					&& !"all".equalsIgnoreCase(teamConstraint.getLeague())) {
				if (CommonUtility.stringToInt(teamConstraint.getLeague()) != 0) {
					query += " and league = ?";
				} else if (CommonUtility.isOnlyDigitInString(teamConstraint.getLeague())) {
					query += " and league in (" + teamConstraint.getLeague() + ")";
				}
			}

			/*
			 * if (teamConstraint.getLeague() != null &&
			 * !"".equals(teamConstraint.getLeague().trim()) &&
			 * CommonUtility.stringToInt(teamConstraint.getLeague()) != 0) { query +=
			 * " and league = ?"; }
			 * 
			 * if(teamConstraint.getLeague() != null &&
			 * !"".equals(teamConstraint.getLeague().trim()) &&
			 * CommonUtility.isOnlyDigitInString(teamConstraint.getLeague()) ) { query +=
			 * " and league in (" + teamConstraint.getLeague() + ")"; }
			 */

		}
		if (orderBy.equals("")) {
			orderBy = " order by club_name, team_name";
		}
		query += orderBy;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			int index = 1;
			if (teamConstraint != null) {
				if (teamConstraint.getTeamID() != 0) {
					pst.setInt(index++, teamConstraint.getTeamID());
				}
				if (teamConstraint.getClubId() != 0) {
					pst.setInt(index++, teamConstraint.getClubId());

				}
				if (teamConstraint.getPlayerId() != 0) {
					pst.setInt(index++, teamConstraint.getPlayerId());
				}
				if (teamConstraint.getLeague() != null && !"".equals(teamConstraint.getLeague().trim())
						&& CommonUtility.stringToInt(teamConstraint.getLeague()) != 0) {
					pst.setString(index++, teamConstraint.getLeague());
				}
			}
			rs = pst.executeQuery();
			while (rs.next()) {

				TeamDto team = new TeamDto();

				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setCaptain(rs.getInt("captain"));
				team.setViceCaptain(rs.getInt("vice_captain"));
				team.setWicketKeeper(rs.getInt("wicket_keeper"));
				;
				team.setCaptainName(rs.getString("captain_name"));
				team.setViceCaptainName(rs.getString("vice_captain_name"));
				team.setLeague(rs.getString("league"));
				team.setGroup(rs.getInt("group_id"));
				team.setLocked(rs.getInt("is_locked") == 1);
				team.setPenalty(rs.getFloat("penalty"));
				team.setPenaltyReason(rs.getString("penalty_reason"));
				team.setPenaltyNRR(rs.getDouble("penalty_NRR"));
				team.setPenaltyNRRReason(rs.getString("penalty_NRR_reason"));
				team.setsuperPenalty(rs.getFloat("super_penalty"));
				team.setsuperPenaltyReason(rs.getString("super_penalty_reason"));
				team.setsuperPenaltyNRR(rs.getDouble("super_penalty_NRR"));
				team.setsuperPenaltyNRRReason(rs.getString("super_penalty_NRR_reason"));
				team.setInformation(rs.getString("information"));
				team.setCaptainEmail(rs.getString("c_email"));
				team.setViceCaptainEmail(rs.getString("vc_email"));
				team.setClubId(rs.getInt("club_id"));
				team.setClubName(rs.getString("club_name"));
				team.setGroundId(rs.getInt("HomeGround"));
				team.setLogo_file_path(rs.getString("logo_file_path"));
				team.setRating(rs.getFloat("rating"));
				team.setBackGroundImagePath(rs.getString("back_ground_image_path"));

				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}

	protected void updateTeam(TeamDto team, int clubId) throws Exception {

		String teamName = DButility.escapeQuotes(team.getTeamName());
		String teamCode = DButility.escapeQuotes(team.getTeamCode());
		int captain = team.getCaptain();
		int viceCaptain = team.getViceCaptain();
		int wicketKeeper = team.getWicketKeeper();
		String isActive = team.getIsActive();
		List selectedPlayers = team.getPlayersList();
		int teamId = team.getTeamID();
		int group = team.getGroup();
		float penalty = team.getPenalty();
		double penaltyNRR = team.getPenaltyNRR();

		float superpenalty = team.getsuperPenalty();
		double superpenaltyNRR = team.getsuperPenaltyNRR();
		int groundId = team.getGroundId();
		int coachId=team.getCoachId();
		group = (group > 0) ? group : 1;
		String query = "update team set team_name = ?, team_code = ?,captain = ? ,vice_captain = ?,wicket_keeper = ?, is_active = ? ,"
				+ "group_id=?, penalty=?, penalty_reason = ?, penalty_NRR= ?, penalty_NRR_reason = ?, super_penalty=?, super_penalty_reason = ?, super_penalty_NRR= ?, super_penalty_NRR_reason = ?,"
				+ "  information = ? , coach_id=?, "
				+ "club_id =?,HomeGround =? where team_id = ?";

		List<Integer> dbTeamPlayersList = getTeamPlayer(teamId, clubId);
		List<Integer> teamGuestPlayersList = getTeamGuestPlayers(teamId, clubId);
		String queryTeamId = "delete from team_player where team_id = " + teamId;

		String queryTeamPlayer = "insert into team_player(team_id,player_id) values (?,?)";
		String updateGuestPlayer = "update team_player set is_secondary = 1 where player_id = ? and team_id = ?";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		PreparedStatement teamPlayersStatement = null;
		ResultSet rs = null;
		try {

			String teamPlayersQuery = "SELECT pv.player_id, is_active FROM mcc.player_view pv JOIN team_player tp "
					+ " ON pv.player_id = tp.player_id "
					+ " WHERE tp.team_id = ? AND pv.club_id = ? AND pv.is_active = ?;";

			teamPlayersStatement = conn.prepareStatement(teamPlayersQuery);
			teamPlayersStatement.setInt(1, teamId);
			teamPlayersStatement.setInt(2, clubId);
			teamPlayersStatement.setInt(3, 2);

			rs = teamPlayersStatement.executeQuery();
			List<Integer> inactiveTeamPlayersList = new ArrayList<Integer>();
			while (rs.next()) {
				inactiveTeamPlayersList.add(rs.getInt("player_id"));
			}

			pst = conn.prepareStatement(query);

			int index = 1;

			pst.setString(index++, teamName);
			pst.setString(index++, teamCode);
			pst.setInt(index++, captain);
			pst.setInt(index++, viceCaptain);
			pst.setInt(index++, wicketKeeper);
			pst.setString(index++, isActive);
			pst.setInt(index++, group);
			pst.setFloat(index++, penalty);
			pst.setString(index++, DButility.escapeLine(team.getPenaltyReason()));
			pst.setDouble(index++, penaltyNRR);
			pst.setString(index++, DButility.escapeLine(team.getPenaltyNRRReason()));
			// code change
			pst.setFloat(index++, superpenalty);
			pst.setString(index++, DButility.escapeLine(team.getsuperPenaltyReason()));
			pst.setDouble(index++, superpenaltyNRR);
			pst.setString(index++, DButility.escapeLine(team.getsuperPenaltyNRRReason()));

			pst.setString(index++, DButility.escapeLine(team.getInformation()));
			pst.setInt(index++, team.getCoachId());
			pst.setInt(index++, team.getClubId());
			pst.setInt(index++, team.getGroundId());
			pst.setInt(index++, teamId);

			pst.executeUpdate();

			pst.executeUpdate(queryTeamId);

			// auditTeamPlayer("%",""+teamId,"","delete",conn);
			pst1 = conn.prepareStatement(queryTeamPlayer);
			if (CommonUtility.isListNullEmpty(selectedPlayers)) {
				selectedPlayers = Collections.EMPTY_LIST;
			}

			for (int i = 0; i < selectedPlayers.size(); i++) {
				int playerId = CommonUtility.stringToInt((String) selectedPlayers.get(i));
				inactiveTeamPlayersList.add(playerId);
			}
			for (Integer playerIdLoop : inactiveTeamPlayersList) {
				if (playerIdLoop > 0) {
					pst1.setInt(1, teamId);
					pst1.setInt(2, playerIdLoop);
					pst1.addBatch();
				}
			}

			pst1.executeBatch();

			if (teamGuestPlayersList != null && teamGuestPlayersList.size() > 0) {
				pst2 = conn.prepareStatement(updateGuestPlayer);
				for (int i = 0; i < teamGuestPlayersList.size(); i++) {
					int playerId = teamGuestPlayersList.get(i);
					if (playerId > 0) {
						pst2.setInt(1, playerId);
						pst2.setInt(2, teamId);
						pst2.addBatch();
					}

				}
				pst2.executeBatch();
			}
			
			for (int i = 0; i < selectedPlayers.size(); i++) {
				int playerId = CommonUtility.stringToInt((String) selectedPlayers.get(i));				
				if (playerId > 0 && !dbTeamPlayersList.contains(playerId)) {
					auditTeamPlayer((String) selectedPlayers.get(i), "" + teamId, team.getUpdatedBy(), "insert", conn);
				}
			}
			for (int i = 0; i < dbTeamPlayersList.size(); i++) {
				int playerId = dbTeamPlayersList.get(i);				
				if (playerId > 0 && !selectedPlayers.contains(playerId+"")) {
					auditTeamPlayer(playerId+"", "" + teamId, team.getUpdatedBy(), "delete", conn);
				}
			}
			if (teamGuestPlayersList != null && teamGuestPlayersList.size() > 0) {
				for (int i = 0; i < teamGuestPlayersList.size(); i++) {
					int playerId = teamGuestPlayersList.get(i);
					if (playerId > 0) {
						auditTeamPlayer(playerId+"", "" + teamId, team.getUpdatedBy(), "update", conn);
					}

				}
			}
			teamAudit(teamId, clubId, "update", team.getUpdatedBy(), conn);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
			DButility.closeConnectionAndStatement(conn, pst1);
			DButility.closeConnectionAndStatement(conn, pst2);
			DButility.dbCloseAll(conn, teamPlayersStatement, rs);
		}

	}

	protected void addPlayerToTeam(TeamDto team, int clubId) throws Exception {

		List players = team.getPlayersList();
		int teamId = team.getTeamID();

		List<Integer> teamPlayersList = getTeamPlayer(teamId, clubId);

		// String queryTeamId = "delete from team_player where team_id = "+ teamId;

		String queryTeamPlayer = "insert into team_player(team_id,player_id) values (?,?)";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		try {

			pst1 = conn.prepareStatement(queryTeamPlayer);
			if (CommonUtility.isListNullEmpty(players)) {
				players = Collections.EMPTY_LIST;
			}

			for (int i = 0; i < players.size(); i++) {
				int playerId = CommonUtility.stringToInt((String) players.get(i));
				if (playerId > 0 && !teamPlayersList.contains(playerId)) {
					pst1.setInt(1, teamId);
					pst1.setInt(2, playerId);
					pst1.addBatch();
				}
			}
			pst1.executeBatch();

			for (int i = 0; i < players.size(); i++) {
				// for(int j=0;j<teamPlayersList.size();j++){
				int playerId = CommonUtility.stringToInt((String) players.get(i));
				if (playerId > 0 && !teamPlayersList.contains(playerId))
					auditTeamPlayer((String) players.get(i), "" + teamId, "", "insert", conn);
				// }
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
			DButility.closeConnectionAndStatement(conn, pst1);
		}
	}

	protected int deletePlayerFromTeam(int teamId, List<Integer> playerIds, int clubId) throws Exception {
		int playerRemoved = 0;

		String deletePlayerFromTeam = "delete from team_player where team_id = ? and player_id in ( ";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {

			for (int playerId : playerIds) {
				if (playerId > 0) {
					deletePlayerFromTeam = deletePlayerFromTeam + playerId + ",";
				}
			}
			deletePlayerFromTeam = deletePlayerFromTeam.substring(0, deletePlayerFromTeam.length() - 1);
			deletePlayerFromTeam = deletePlayerFromTeam + ")";
			pst = conn.prepareStatement(deletePlayerFromTeam);
			pst.setInt(1, teamId);
			playerRemoved = pst.executeUpdate();

			for (int playerId : playerIds) {
				auditTeamPlayer(playerId + "", teamId + "", "" + "", "delete", conn);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);

		}
		return playerRemoved;
	}
	
	protected int deletePlayerFromTeam(int teamId, List<Integer> playerIds, int clubId, String deletedBy) throws Exception {
		int playerRemoved = 0;

		String deletePlayerFromTeam = "delete from team_player where team_id = ? and player_id in ( ";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {

			for (int playerId : playerIds) {
				if (playerId > 0) {
					deletePlayerFromTeam = deletePlayerFromTeam + playerId + ",";
				}
			}
			deletePlayerFromTeam = deletePlayerFromTeam.substring(0, deletePlayerFromTeam.length() - 1);
			deletePlayerFromTeam = deletePlayerFromTeam + ")";
			pst = conn.prepareStatement(deletePlayerFromTeam);
			pst.setInt(1, teamId);
			playerRemoved = pst.executeUpdate();

			for (int playerId : playerIds) {
				auditTeamPlayer(playerId+"", teamId+"", deletedBy, "delete", conn);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);

		}
		return playerRemoved;
	}
	
	protected int deletePlayerFromTeamNew(int teamId, Integer playerId, int clubId, String deletedBy) throws Exception {
		
		int playerRemoved = 0;

		String deletePlayerFromTeam = "delete from team_player where team_id = ? and player_id = ? ";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(deletePlayerFromTeam);
			pst.setInt(1, teamId);
			pst.setInt(2, playerId);
			playerRemoved = pst.executeUpdate();
			if(playerRemoved>0) {
				auditTeamPlayerNew(playerId, teamId, deletedBy, "delete", conn);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);

		}
		return playerRemoved;
	}

	protected int deletePlayerFromAllTeam(int playerId, int clubId, String userId) throws Exception {
		int playerRemoved = 0;
		String getTeamIdsOfPlayer = "select team_id from team_player where player_id = ? ";
		String deletePlayerFromTeam = "delete from team_player where player_id = ? ";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		List<Integer> teamsOfPlayer = new ArrayList<Integer>();
		try {

			pst = conn.prepareStatement(getTeamIdsOfPlayer);
			pst.setInt(1, playerId);
			rs = pst.executeQuery();

			while (rs.next()) {
				teamsOfPlayer.add(rs.getInt("team_id"));
			}

			if (teamsOfPlayer != null && !teamsOfPlayer.isEmpty()) {
				for (int teamId : teamsOfPlayer) {
					auditTeamPlayer(playerId + "", teamId + "", userId + "", "delete", conn);
				}
			}
			pst1 = conn.prepareStatement(deletePlayerFromTeam);
			pst1.setInt(1, playerId);
			playerRemoved = pst1.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
			DButility.closeConnectionAndStatement(conn, pst1);
			DButility.closeRs(rs);
		}
		return playerRemoved;
	}
protected List<TeamDto> getPointsTablefor100BallLeague(String matchType, String leagueName, int clubId,int ballsPerOver)  throws Exception
{

	if (!MatchUtility.isValidateMatchType(matchType))
		return null;
	List<TeamDto> teams = new ArrayList<TeamDto>();
	String query = "	select t.team_id," + "   t.team_name," + "   t.team_code," + "   t.group_id,"
			+ "   t.logo_file_path," + "   t.rating," + "	  t.date_created," + "   ptt.matches," + "   ptt.wins,"
			+ "   ptt.loss," + "	ptt.nr," + "	ptt.abandoned," + "	ptt.tie," + "	ptt.zero_points,"
			+ "	t.penalty," + "	t.penalty_reason," + "	t.penalty_NRR," + "	t.penalty_NRR_reason,"
			+ "	t.super_penalty," + "	t.super_penalty_reason," + "	t.super_penalty_NRR,"
			+ "	t.super_penalty_NRR_reason," + "	ptt.for_runs," + "	ptt.against_runs," + "	ptt.for_wickets,"
			+ "	ptt.against_wickets," + "	ptt.for_balls," + "	ptt.against_balls,"
			+ "	(ptt.wins*0 + ptt.nr) points,"
			+ "	ROUND((if(for_balls = 0,0,(for_runs*?)/for_balls) - if(against_balls = 0,0,(against_runs*?)/against_balls)),4) net_run_rate "
			+ "from (" + "	select team team_id," + "	  sum(penalty) penalty,"
			+ "	  sum(penalty_NRR) penalty_NRR," + "	  sum(super_penalty) super_penalty,"
			+ "	  sum(super_penalty_NRR) super_penalty_NRR," + "   sum(matches) matches," + "   sum(wins) wins,"
			+ "   sum(loss) loss," + "	sum(nr) nr," + "	sum(abandoned) abandoned," + "	sum(tie) tie,"
			+ "	sum(zero_points) zero_points," + "	sum(for_runs) for_runs," + "	sum(against_runs) against_runs,"
			+ "	sum(for_wickets) for_wickets," + "	sum(against_wickets) against_wickets,"
			+ "	sum(for_balls) for_balls," + "	sum(against_balls) against_balls " + "from ("
			+ "	SELECT m1.team_one team," + "	sum(t1.penalty) penalty," + "	sum(t1.penalty_NRR) penalty_NRR,"
			+ "	sum(t1.super_penalty) super_penalty," + "	sum(t1.super_penalty_NRR) super_penalty_NRR,"
			+ "	sum(if(m1.winner >= -1,1,0)) matches,"
			+ "	sum(if(m1.team_one = m1.winner,0,if(m1.winner <= 0,0,1))) loss,"
			+ "	sum(if(m1.team_one = m1.winner,1,0)) wins," 
			+ "	sum(if(m1.winner = 0,if(m1.is_abandoned = 1,1,0),0)) abandoned,"
			+ "	sum(if(m1.winner = 0,if(m1.is_abandoned = 1,1,0),0)) nr,"
			+ "	sum(if(m1.winner = 0,if(m1.is_abandoned = 0,1,0),0)) tie,"
			+ "	sum(if(m1.winner = -1,1,0)) zero_points,"
			+ "	sum(if(m1.is_abandoned = 1,0,if(m1.team_one = m1.batting_first,if(m1.t2_target=0,m1.t1_total,m1.t2_target-1),m1.t2_total))) for_runs,"
			+ "	sum(if(m1.is_abandoned = 1,0,if(m1.team_one = m1.batting_first,m1.t2_total,if(m1.t2_target=0,m1.t1_total,m1.t2_target)))) against_runs,"
			+ "	sum(if(m1.is_abandoned = 1,0,if(m1.team_one = m1.batting_first,m1.t1_wickets,m1.t2_wickets))) for_wickets,"
			+ "	sum(if(m1.is_abandoned = 1,0,if(m1.team_one = m1.batting_first,m1.t2_wickets,m1.t1_wickets))) against_wickets,"
			+ "	sum(if(m1.is_abandoned = 1,if(m1.abandone_type = 'forefeit' and m1.winner>0,if(m1.team_one != m1.winner,m1.overs*?,0),0),if(m1.winner=0,(if(m1.t2_revised_over=0,m1.overs*?,m1.t2_revised_over)),if(m1.team_one = m1.winner,if(m1.team_one = m1.batting_first,(if(m1.t2_revised_over=0,m1.overs*?,m1.t2_revised_over)),m1.t2_balls),(if(m1.t2_revised_over=0,m1.overs*?,m1.t2_revised_over)))))) for_balls,"
			+ "	sum(if(m1.is_abandoned = 1,0,if(m1.winner=0,(if(m1.t2_revised_over=0,m1.overs*?,m1.t2_revised_over)),if(m1.team_one = m1.winner,(if(m1.t2_revised_over=0,m1.overs*?,m1.t2_revised_over)),if(m1.team_one = m1.batting_first,m1.t2_balls,(if(m1.t2_revised_over=0,m1.overs*?,m1.t2_revised_over))))))) against_balls"
			+ "	FROM matches m1, team t1"
			+ "	where m1.team_one = t1.team_id and m1.is_complete = 1 and t1.league = ? "
			+ "	and m1.match_type = ?" + "	group by m1.team_one" + "	union all" + "	SELECT m2.team_two team,"
			+ "	sum(t2.penalty) penalty," + "	sum(t2.penalty_NRR) penalty_NRR,"
			+ "	sum(t2.super_penalty) super_penalty," + "	sum(t2.super_penalty_NRR) super_penalty_NRR,"
			+ "	sum(if(m2.winner >= -1,1,0)) matches,"
			+ "	sum(if(m2.team_two = m2.winner,0,if(m2.winner <= 0,0,1))) loss,"
			+ "	sum(if(m2.team_two = m2.winner,1,0)) wins,"
			+ "	sum(if(m2.winner = 0,if(m2.is_abandoned = 1,1,0),0)) abandoned,"
			+ "	sum(if(m2.winner = 0,if(m2.is_abandoned = 1,1,0),0)) nr,"
			+ "	sum(if(m2.winner = 0,if(m2.is_abandoned = 0,1,0),0)) tie,"
			+ "	sum(if(m2.winner = -1,1,0)) zero_points,"
			+ "	sum(if(m2.is_abandoned = 1,0,if(m2.team_two = m2.batting_first,if(m2.t2_target=0,m2.t1_total,m2.t2_target-1),m2.t2_total))) for_runs,"
			+ "	sum(if(m2.is_abandoned = 1,0,if(m2.team_two = m2.batting_first,m2.t2_total,if(m2.t2_target=0,m2.t1_total,m2.t2_target-1)))) against_runs,"
			+ "	sum(if(m2.is_abandoned = 1,0,if(m2.team_two = m2.batting_first,m2.t1_wickets,m2.t2_wickets))) for_wickets,"
			+ "	sum(if(m2.is_abandoned = 1,0,if(m2.team_two = m2.batting_first,m2.t2_wickets,m2.t1_wickets))) against_wickets,"
			+ "	sum(if(m2.is_abandoned = 1,if(m2.abandone_type = 'forefeit' and m2.winner>0,if(m2.team_two != m2.winner,m2.overs*?,0),0),if(m2.winner=0,(if(m2.t2_revised_over=0,m2.overs*?,m2.t2_revised_over)),if(m2.team_two = m2.winner,if(m2.team_two = m2.batting_first,(if(m2.t2_revised_over=0,m2.overs*?,m2.t2_revised_over)),m2.t2_balls),(if(m2.t2_revised_over=0,m2.overs*?,m2.t2_revised_over)))))) for_balls,"
			+ "	sum(if(m2.is_abandoned = 1,0,if(m2.winner=0,(if(m2.t2_revised_over=0,m2.overs*?,m2.t2_revised_over)),if(m2.team_two = m2.winner,(if(m2.t2_revised_over=0,m2.overs*?,m2.t2_revised_over)),if(m2.team_two = m2.batting_first,m2.t2_balls,(if(m2.t2_revised_over=0,m2.overs*?,m2.t2_revised_over))))))) against_balls"
			+ "	FROM matches m2, team t2"
			+ "	where m2.team_two = t2.team_id and m2.is_complete = 1 and t2.league = ? "
			+ "	and m2.match_type = ?" + "	group by m2.team_two) pt"
			+ "   group by team ) ptt,team t where t.team_id = ptt.team_id"
			+ "	order by group_id asc,points desc,net_run_rate desc";

	Connection conn = null;
	PreparedStatement st = null;
	ResultSet rs = null;
	try {
		conn = DButility.getReadConnection(clubId);
		st = conn.prepareStatement(query);
		int index = 1;
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);	
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setString(index++, leagueName);
		st.setString(index++, matchType);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);
		st.setInt(index++,ballsPerOver);	
		st.setInt(index++,ballsPerOver);		
		st.setInt(index++,ballsPerOver);
		st.setString(index++, leagueName);
		st.setString(index++, matchType);
		rs = st.executeQuery();
		while (rs.next()) {

			TeamDto team = new TeamDto();

			team.setTeamID(rs.getInt("team_id"));
			team.setTeamName(rs.getString("team_name"));
			team.setTeamCode(rs.getString("team_code"));
			team.setWon(rs.getInt("wins"));
			team.setMatches(rs.getInt("matches"));
			team.setLost(rs.getInt("loss"));
			team.setNoResult(rs.getInt("nr"));
			team.setAbandoned(rs.getInt("abandoned"));
			team.setTie(rs.getInt("tie"));
			team.setZeroPoint(rs.getInt("zero_points"));
			team.setBallsFaced(rs.getInt("for_balls"));
			team.setRunsScored(rs.getInt("for_runs"));
			team.setRunsGiven(rs.getInt("against_runs"));
			team.setWicketsLost(rs.getInt("for_wickets"));
			team.setWicketsTaken(rs.getInt("against_wickets"));
			team.setBallsBowled(rs.getInt("against_balls"));
			team.setPoints(rs.getInt("points"));
			team.setNetRunRate(rs.getDouble("net_run_rate"));
			team.setGroup(rs.getInt("group_id"));
			team.setPenalty(rs.getFloat("penalty"));
			team.setPenaltyReason(rs.getString("penalty_reason"));
			team.setPenaltyNRR(rs.getDouble("penalty_NRR"));
			team.setPenaltyNRRReason(rs.getString("penalty_NRR_reason"));
			team.setsuperPenalty(rs.getFloat("super_penalty"));
			team.setsuperPenaltyReason(rs.getString("super_penalty_reason"));
			team.setsuperPenaltyNRR(rs.getDouble("super_penalty_NRR"));
			team.setsuperPenaltyNRRReason(rs.getString("super_penalty_NRR_reason"));
			team.setLogo_file_path(rs.getString("logo_file_path"));
			team.setRating(rs.getFloat("rating"));
			team.setCreationDate(rs.getDate("date_created"));

			teams.add(team);
		}
	} catch (SQLException e) {
		throw new Exception(e.getMessage());
	} finally {
		DButility.dbCloseAll(conn, st, rs);
	}
	return teams;

}
	protected List<TeamDto> getPointsTable(String matchType, String leagueName, int clubId) throws Exception {
		if (!MatchUtility.isValidateMatchType(matchType))
			return null;
		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "	select t.team_id," + "   t.team_name," + "   t.team_code," + "   t.group_id,"
				+ "   t.logo_file_path," + "   t.rating," + "	  t.date_created," + "   ptt.matches," + "   ptt.wins,"
				+ "   ptt.loss," + "	ptt.nr," + "	ptt.abandoned," + "	ptt.tie," + "	ptt.zero_points,"
				+ "	t.penalty," + "	t.penalty_reason," + "	t.penalty_NRR," + "	t.penalty_NRR_reason,"
				+ "	t.super_penalty," + "	t.super_penalty_reason," + "	t.super_penalty_NRR,"
				+ "	t.super_penalty_NRR_reason," + "	ptt.for_runs," + "	ptt.against_runs," + "	ptt.for_wickets,"
				+ "	ptt.against_wickets," + "	ptt.for_balls," + "	ptt.against_balls,"
				+ "	(ptt.wins*0 + ptt.nr) points,"
				+ "	ROUND((if(for_balls = 0,0,(for_runs*6)/for_balls) - if(against_balls = 0,0,(against_runs*6)/against_balls)),4) net_run_rate "
				+ "from (" + "	select team team_id," + "	  sum(penalty) penalty,"
				+ "	  sum(penalty_NRR) penalty_NRR," + "	  sum(super_penalty) super_penalty,"
				+ "	  sum(super_penalty_NRR) super_penalty_NRR," + "   sum(matches) matches," + "   sum(wins) wins,"
				+ "   sum(loss) loss," + "	sum(nr) nr," + "	sum(abandoned) abandoned," + "	sum(tie) tie,"
				+ "	sum(zero_points) zero_points," + "	sum(for_runs) for_runs," + "	sum(against_runs) against_runs,"
				+ "	sum(for_wickets) for_wickets," + "	sum(against_wickets) against_wickets,"
				+ "	sum(for_balls) for_balls," + "	sum(against_balls) against_balls " + "from ("
				+ "	SELECT m1.team_one team," + "	sum(t1.penalty) penalty," + "	sum(t1.penalty_NRR) penalty_NRR,"
				+ "	sum(t1.super_penalty) super_penalty," + "	sum(t1.super_penalty_NRR) super_penalty_NRR,"
				+ "	sum(if(m1.winner >= -1,1,0)) matches,"
				+ "	sum(if(m1.team_one = m1.winner,0,if(m1.winner <= 0,0,1))) loss,"
				+ "	sum(if(m1.team_one = m1.winner,1,0)) wins," 
				+ "	sum(if(m1.winner = 0,if(m1.is_abandoned = 1,1,0),0)) abandoned,"
				+ "	sum(if(m1.winner = 0,if(m1.is_abandoned = 1,1,0),0)) nr,"
				+ "	sum(if(m1.winner = 0,if(m1.is_abandoned = 0,1,0),0)) tie,"
				+ "	sum(if(m1.winner = -1,1,0)) zero_points,"
				+ "	sum(if(m1.is_abandoned = 1,0,if(m1.team_one = m1.batting_first,if(m1.t2_target=0,m1.t1_total,m1.t2_target),m1.t2_total))) for_runs,"
				+ "	sum(if(m1.is_abandoned = 1,0,if(m1.team_one = m1.batting_first,m1.t2_total,if(m1.t2_target=0,m1.t1_total,m1.t2_target)))) against_runs,"
				+ "	sum(if(m1.is_abandoned = 1,0,if(m1.team_one = m1.batting_first,m1.t1_wickets,m1.t2_wickets))) for_wickets,"
				+ "	sum(if(m1.is_abandoned = 1,0,if(m1.team_one = m1.batting_first,m1.t2_wickets,m1.t1_wickets))) against_wickets,"
				+ "	sum(if(m1.is_abandoned = 1,0,if(m1.winner=0,(if(m1.t2_revised_over=0,m1.overs*6,m1.t2_revised_over)),if(m1.team_one = m1.winner,if(m1.team_one = m1.batting_first,(if(m1.t2_revised_over=0,m1.overs*6,m1.t2_revised_over)),m1.t2_balls),(if(m1.t2_revised_over=0,m1.overs*6,m1.t2_revised_over)))))) for_balls,"
				+ "	sum(if(m1.is_abandoned = 1,0,if(m1.winner=0,(if(m1.t2_revised_over=0,m1.overs*6,m1.t2_revised_over)),if(m1.team_one = m1.winner,(if(m1.t2_revised_over=0,m1.overs*6,m1.t2_revised_over)),if(m1.team_one = m1.batting_first,m1.t2_balls,(if(m1.t2_revised_over=0,m1.overs*6,m1.t2_revised_over))))))) against_balls"
				+ "	FROM matches m1, team t1"
				+ "	where m1.team_one = t1.team_id and m1.is_complete = 1 and t1.league = ? "
				+ "	and m1.match_type = ?" + "	group by m1.team_one" + "	union all" + "	SELECT m2.team_two team,"
				+ "	sum(t2.penalty) penalty," + "	sum(t2.penalty_NRR) penalty_NRR,"
				+ "	sum(t2.super_penalty) super_penalty," + "	sum(t2.super_penalty_NRR) super_penalty_NRR,"
				+ "	sum(if(m2.winner >= -1,1,0)) matches,"
				+ "	sum(if(m2.team_two = m2.winner,0,if(m2.winner <= 0,0,1))) loss,"
				+ "	sum(if(m2.team_two = m2.winner,1,0)) wins,"
				+ "	sum(if(m2.winner = 0,if(m2.is_abandoned = 1,1,0),0)) abandoned,"
				+ "	sum(if(m2.winner = 0,if(m2.is_abandoned = 1,1,0),0)) nr,"
				+ "	sum(if(m2.winner = 0,if(m2.is_abandoned = 0,1,0),0)) tie,"
				+ "	sum(if(m2.winner = -1,1,0)) zero_points,"
				+ "	sum(if(m2.is_abandoned = 1,0,if(m2.team_two = m2.batting_first,if(m2.t2_target=0,m2.t1_total,m2.t2_target),m2.t2_total))) for_runs,"
				+ "	sum(if(m2.is_abandoned = 1,0,if(m2.team_two = m2.batting_first,m2.t2_total,if(m2.t2_target=0,m2.t1_total,m2.t2_target)))) against_runs,"
				+ "	sum(if(m2.is_abandoned = 1,0,if(m2.team_two = m2.batting_first,m2.t1_wickets,m2.t2_wickets))) for_wickets,"
				+ "	sum(if(m2.is_abandoned = 1,0,if(m2.team_two = m2.batting_first,m2.t2_wickets,m2.t1_wickets))) against_wickets,"
				+ "	sum(if(m2.is_abandoned = 1,0,if(m2.winner=0,(if(m2.t2_revised_over=0,m2.overs*6,m2.t2_revised_over)),if(m2.team_two = m2.winner,if(m2.team_two = m2.batting_first,(if(m2.t2_revised_over=0,m2.overs*6,m2.t2_revised_over)),m2.t2_balls),(if(m2.t2_revised_over=0,m2.overs*6,m2.t2_revised_over)))))) for_balls,"
				+ "	sum(if(m2.is_abandoned = 1,0,if(m2.winner=0,(if(m2.t2_revised_over=0,m2.overs*6,m2.t2_revised_over)),if(m2.team_two = m2.winner,(if(m2.t2_revised_over=0,m2.overs*6,m2.t2_revised_over)),if(m2.team_two = m2.batting_first,m2.t2_balls,(if(m2.t2_revised_over=0,m2.overs*6,m2.t2_revised_over))))))) against_balls"
				+ "	FROM matches m2, team t2"
				+ "	where m2.team_two = t2.team_id and m2.is_complete = 1 and t2.league = ? "
				+ "	and m2.match_type = ?" + "	group by m2.team_two) pt"
				+ "   group by team ) ptt,team t where t.team_id = ptt.team_id"
				+ "	order by group_id asc,points desc,net_run_rate desc";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			int index = 1;
			st.setString(index++, leagueName);
			st.setString(index++, matchType);
			st.setString(index++, leagueName);
			st.setString(index++, matchType);
			rs = st.executeQuery();
			while (rs.next()) {

				TeamDto team = new TeamDto();

				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setWon(rs.getInt("wins"));
				team.setMatches(rs.getInt("matches"));
				team.setLost(rs.getInt("loss"));
				team.setNoResult(rs.getInt("nr"));
				team.setAbandoned(rs.getInt("abandoned"));
				team.setTie(rs.getInt("tie"));
				team.setZeroPoint(rs.getInt("zero_points"));
				team.setBallsFaced(rs.getInt("for_balls"));
				team.setRunsScored(rs.getInt("for_runs"));
				team.setRunsGiven(rs.getInt("against_runs"));
				team.setWicketsLost(rs.getInt("for_wickets"));
				team.setWicketsTaken(rs.getInt("against_wickets"));
				team.setBallsBowled(rs.getInt("against_balls"));
				team.setPoints(rs.getInt("points"));
				team.setNetRunRate(rs.getDouble("net_run_rate"));
				team.setGroup(rs.getInt("group_id"));
				team.setPenalty(rs.getFloat("penalty"));
				team.setPenaltyReason(rs.getString("penalty_reason"));
				team.setPenaltyNRR(rs.getDouble("penalty_NRR"));
				team.setPenaltyNRRReason(rs.getString("penalty_NRR_reason"));
				team.setsuperPenalty(rs.getFloat("super_penalty"));
				team.setsuperPenaltyReason(rs.getString("super_penalty_reason"));
				team.setsuperPenaltyNRR(rs.getDouble("super_penalty_NRR"));
				team.setsuperPenaltyNRRReason(rs.getString("super_penalty_NRR_reason"));
				team.setLogo_file_path(rs.getString("logo_file_path"));
				team.setRating(rs.getFloat("rating"));
				team.setCreationDate(rs.getDate("date_created"));

				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return teams;
	}
	
	protected List<TeamDto> getPointsTableFB(String matchType, String seriesIdStr, int clubId) throws Exception {
		if (!MatchUtility.isValidateMatchType(matchType))
			return null;
		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "SELECT team_id, team_name, team_code, group_id,logo_file_path, SUM(matches_played) matches, SUM(won) wins, "
				+ "SUM(lost) loss, SUM(draw) draws,  SUM(goals_scored) goals_scored,  SUM(goals_conceded) goals_conceded FROM ( "
				+ "SELECT m1.team_one team_id, t1.team_name, t1.team_code, t1.group_id,t1.logo_file_path, SUM(IF(m1.winner >= -1,1,0)) matches_played, "
				+ "SUM(IF(m1.team_one = m1.winner,1,0)) won,SUM(IF(m1.team_one = m1.winner,0,IF(m1.winner <= 0,0,1))) lost,	"
				+ "SUM(IF(m1.winner = 0,IF(m1.is_abandoned = 0,1,0),0)) draw,SUM(m1.t1_total) goals_scored,	SUM(m1.t2_total) goals_conceded	"
				+ "FROM matches m1, team t1 WHERE m1.team_one = t1.team_id AND m1.is_complete = 1 AND t1.league = ? AND m1.match_type = ? "
				+ "GROUP BY m1.team_one "
				+ "UNION ALL "
				+ "SELECT m2.team_two team_id, t2.team_name, t2.team_code, t2.group_id,t2.logo_file_path, SUM(IF(m2.winner >= -1,1,0)) matches_played, "
				+ "SUM(IF(m2.team_two = m2.winner,1,0)) won,SUM(IF(m2.team_two = m2.winner,0,IF(m2.winner <= 0,0,1))) lost,	"
				+ "SUM(IF(m2.winner = 0,IF(m2.is_abandoned = 0,1,0),0)) draw, SUM(m2.t2_total) goals_scored, SUM(m2.t1_total) goals_conceded "
				+ "FROM matches m2, team t2 WHERE m2.team_two = t2.team_id AND m2.is_complete = 1 AND t2.league = ? AND m2.match_type = ? "
				+ "GROUP BY m2.team_two ) final_query GROUP BY team_id ORDER BY group_id, wins DESC, draws DESC, goals_scored DESC, goals_conceded  ASC  ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			int index = 1;
			st.setString(index++, seriesIdStr);
			st.setString(index++, matchType);
			st.setString(index++, seriesIdStr);
			st.setString(index++, matchType);
			rs = st.executeQuery();
			while (rs.next()) {

				TeamDto team = new TeamDto();

				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setLogo_file_path(rs.getString("logo_file_path"));
				team.setGroup(rs.getInt("group_id"));
				team.setMatches(rs.getInt("matches"));
				team.setWon(rs.getInt("wins"));
				team.setLost(rs.getInt("loss"));
				team.setTie(rs.getInt("draws"));
				team.setGoalsScored(rs.getInt("goals_scored"));
				team.setGoalsConceded(rs.getInt("goals_conceded"));

				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return teams;
	}


	public int getMatchCountForTeam(int teamId, int clubId) throws Exception {
		Connection conn = DButility.getReadConnection(clubId);
		int matchCount = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String query = "select 1 from matches where team_one = ?  OR team_two = ? ";
			st = conn.prepareStatement(query);
			st.setInt(1, teamId);
			st.setInt(2, teamId);
			rs = st.executeQuery();
			/*
			 * rs = st.executeQuery("select 1 from matches where team_one = " + teamId +
			 * " OR team_two = " + teamId);
			 */
			while (rs.next()) {
				matchCount++;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return matchCount;
	}

	public void deleteTeam(int teamId, String userId, int clubId) throws Exception {
		Connection conn = DButility.getConnection(clubId);
		Statement st = null;
		try {
			teamAudit(teamId, clubId, "delete", userId, null);
			st = conn.createStatement();
			st.executeUpdate("delete from team where team_id = " + teamId);
			st.executeUpdate("delete from team_player where team_id = " + teamId);
			auditTeamPlayer("%", "" + teamId, userId, "delete", conn);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}

	protected void insertPlayerToTeam(int teamId, int playerId, int clubId, String isSecondary, String userId)
			throws Exception {
		Connection conn = DButility.getConnection(clubId);
		String updateTeamPlayer = "update team_player set is_secondary = ? where team_id=? and player_id=?";
		PreparedStatement pst1 = null;

		String queryTeamPlayer = "insert into team_player(team_id,player_id,is_secondary) values (?,?,?)";
		PreparedStatement pst = null;
		try {

			pst1 = conn.prepareStatement(updateTeamPlayer);
			if (isSecondary != null && !"".equals(isSecondary)) {
				pst1.setString(1, isSecondary);
			} else {
				pst1.setNull(1, Types.VARCHAR);
			}
			pst1.setInt(2, teamId);
			pst1.setInt(3, playerId);

			if (pst1.executeUpdate() == 0) {
				pst = conn.prepareStatement(queryTeamPlayer);

				pst.setInt(1, teamId);
				pst.setInt(2, playerId);
				if (isSecondary != null && !"".equals(isSecondary)) {
					pst.setString(3, isSecondary);
				} else {
					pst.setNull(3, Types.VARCHAR);
				}
				pst.executeUpdate();

				auditTeamPlayer("" + playerId, "" + teamId, userId, "insert", conn);
			} else {
				auditTeamPlayer("" + playerId, "" + teamId, userId, "update", conn);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}
	
	protected void insertPlayerToTeamNew(int teamId, int playerId, int clubId, String userId)
			throws Exception {
		
		Connection conn = null;
		PreparedStatement pst = null;
		String queryTeamPlayer = "insert into team_player(team_id,player_id) values (?,?)";
		try {
			conn = DButility.getConnection(clubId);

			pst = conn.prepareStatement(queryTeamPlayer);

			pst.setInt(1, teamId);
			pst.setInt(2, playerId);
			pst.executeUpdate();

			auditTeamPlayerNew(playerId, teamId, userId, "insert", conn);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	protected void updateLock(int lock, int teamId, int league, int clubId) throws Exception {
		String query = "update team set is_locked = ?" + " where ";
		if (teamId > 0) {
			query += "team_id =	?";
		} else if (league > 0) {
			query += "league =	?";
		} else {
			throw new CCException("Invalid Lock Parameters Passed.", CCErrorConstant.INPUT_PARAM_MISSING_C);
		}

		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, lock);
			if (teamId > 0) {
				st.setInt(2, teamId);
			} else if (league > 0) {
				st.setInt(2, league);
			}
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}

	}

	protected void addCaptainToTeam(int playerId, int teamId, int clubId) throws Exception {
		String query = "update team set captain = ?" + " where team_id =	? \n";

		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			st.setInt(2, teamId);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}

	}

	protected void addViceCaptainToTeam(int playerId, int teamId, int clubId) throws Exception {
		String query = "update team set vice_captain = ?" + " where team_id =	? \n";

		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			st.setInt(2, teamId);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}

	}

	protected void addWicketKeeperToTeam(int playerId, int teamId, int clubId) throws Exception {
		String query = "update team set wicket_keeper = ?" + " where team_id =	? \n";

		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			st.setInt(2, teamId);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}

	}

	public Map<Integer, Integer> getTeamCounts(int leagueId, int clubId) throws Exception {
		Connection conn = DButility.getConnection(clubId);
		Map<Integer, Integer> teamCount = new HashMap<Integer, Integer>();
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "select tp.team_id,count(tp.player_id) count from team t,team_player tp,mcc.player_view p "
				+ "where tp.team_id = t.team_id and p.player_id =tp.player_id and league = ? and p.club_id = ? and p.is_active=1  group by tp.team_id";
		
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, leagueId);
			st.setInt(2, clubId);
			rs = st.executeQuery();
			/*
			 * rs = st.
			 * executeQuery("select tp.team_id,count(tp.player_id) count from team t,team_player tp,mcc.player p "
			 * + "where tp.team_id = t.team_id and p.player_id =tp.player_id and league =" +
			 * leagueId + " group by tp.team_id");
			 */
			while (rs.next()) {
				teamCount.put(rs.getInt("team_id"), rs.getInt("count"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return teamCount;
	}

	protected void addGroupName(int leagueId, int groupId, String groupName, int clubId) throws Exception {

		String query = "insert into league_group_names(league_id,group_id,group_name) values (?,?,?)";

		/*
		 * "insert into league_group_names(league_id,group_id,group_name) values (" +
		 * leagueId + "," + groupId + ",'" + DButility.escapeLine(groupName) + "')";
		 */

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, leagueId);
			st.setInt(2, groupId);
			st.setString(3, DButility.escapeLine(groupName));
			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}

	}

	protected void deleteGroupName(int leagueId, int groupId, int clubId) throws Exception {

		String query = "delete from league_group_names where league_id =? and group_id = ?";

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, leagueId);
			pst.setInt(2, groupId);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	public Map<Integer, String> getGroupNames(String leagueId, int clubId) throws Exception {
		Connection conn = DButility.getReadConnection(clubId);
		Map<Integer, String> groupNames = new HashMap<Integer, String>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "select group_id,group_name from league_group_names where league_id in ( " + leagueId + ") ";
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				groupNames.put(rs.getInt("group_id"), rs.getString("group_name"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return groupNames;
	}

	protected void updateTeamLogoFilePath(String logo_file_path, int teamId, int clubId) throws Exception {

		String queryTeamName = "select team_name from team where team_id = ?";

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			pst = conn.prepareStatement(queryTeamName);
			pst.setInt(1, teamId);
			rs = pst.executeQuery();
			rs.next();

			String query = "update team set logo_file_path = ? where team_name = ?";
			String teamName = rs.getString("team_name");
			// Connection conn = DButility.getConnection(clubId);
			PreparedStatement st = null;

			st = conn.prepareStatement(query);
			st.setString(1, logo_file_path.trim());
			st.setString(2, teamName);
			st.executeUpdate();
			st.close();
			updateMatchSummaryTeamLogo(logo_file_path, teamName, clubId, teamId);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	private void updateMatchSummaryTeamLogo(String logo_file_path, String teamName, int clubId, int teamId)
			throws Exception {
		try {
			Connection conn = DButility.getDefaultConnection();
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {

				String query = "update summary.match_summary set t1_logopath = ? where t1_name = ? and club_id = ?";

				// Connection conn = DButility.getConnection(clubId);

				PreparedStatement st = null;

				st = conn.prepareStatement(query);
				st.setString(1, logo_file_path.trim());
				st.setString(2, teamName);
				st.setInt(3, clubId);
				st.executeUpdate();

				query = "update summary.match_summary set t2_logopath = ? where t2_name = ? and club_id = ?";

				// Connection conn = DButility.getConnection(clubId);

				st = conn.prepareStatement(query);
				st.setString(1, logo_file_path.trim());
				st.setString(2, teamName);
				st.setInt(3, clubId);
				st.executeUpdate();

				st.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				if (rs != null) {
					rs.close();
				}
				DButility.closeConnectionAndStatement(conn, pst);
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * @return
	 */
	protected List<Integer> getTeamPlayer(int teamId, int clubId) throws Exception {
		List<Integer> teamPlayersList = new ArrayList<Integer>();
		String query = "select player_id from team_player where team_id = ?";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setInt(1, teamId);
			rs = pst.executeQuery();

			while (rs.next()) {
				int teamPlayers = rs.getInt("player_id");
				teamPlayersList.add(teamPlayers);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teamPlayersList;
	}

	protected List<PlayerDto> getTeamPlayersList(int teamId, int clubId) throws Exception {

		List<PlayerDto> players = new ArrayList<PlayerDto>();

		String query = "select p.player_id,p.f_name,p.l_name,p.playing_role,p.profilepic_file_path,p.category,p.jersey_number from mcc.player p, team_player tp "
				+ "where tp.team_id = ? and p.player_id = tp.player_id order by p.f_name,p.l_name";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			if (teamId > 0) {
				st.setInt(1, teamId);
			}
			rs = st.executeQuery();

			while (rs.next()) {

				PlayerDto player = new PlayerDto();

				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				player.setPlayingRole(rs.getString("playing_role"));
				player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
				player.setCategory(rs.getString("category"));
				player.setJerseyNumber(rs.getString("jersey_number"));

				players.add(player);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}

	protected List<Integer> getTeamGuestPlayers(int teamId, int clubId) throws Exception {
		List<Integer> teamPlayersList = new ArrayList<Integer>();
		String query = "select team_id, player_id from team_player where team_id = ? and is_secondary = 1";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setInt(1, teamId);
			rs = pst.executeQuery();

			while (rs.next()) {
				int teamPlayers = rs.getInt("player_id");
				teamPlayersList.add(teamPlayers);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teamPlayersList;
	}

	protected List<Integer> getDistinctTeams(int clubId) throws Exception {
		List<Integer> teamPlayersList = new ArrayList<Integer>();
		String query = "select max(team_id) team_id, team_name from team t group by team_name";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			rs = st.executeQuery();

			while (rs.next()) {
				int teamPlayers = rs.getInt("team_id");
				teamPlayersList.add(teamPlayers);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return teamPlayersList;
	}
	
	public static void auditTeamPlayerNew(int playerId, int teamId, String user, String operation, 
			Connection conn) throws Exception {

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String insertQuery = "insert into team_player_audit(team_id,player_id,change_by,"
					+ "change_type,change_timestamp) values(?,?,?,?,now())";
			pst = conn.prepareStatement(insertQuery);
			int index = 1;
			pst.setInt(index++, teamId);
			pst.setInt(index++, playerId);
			pst.setString(index++, user);
			pst.setString(index++, operation);
			pst.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeStatement(pst);
			DButility.closeRs(rs);
		}
	}

	public static void auditTeamPlayer(String player_id, String team_id, String user, String operation, Connection conn)
			throws Exception {

		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		String new_id = null;
		try {
			// conn = DButility.getDefaultConnection();

			String insertQuery = null;
			insertQuery = "insert into team_player_audit(team_id,player_id,is_secondary)"
					+ " (select team_id,player_id,is_secondary from team_player where player_id = ? and team_id like ?)";
			pst = conn.prepareStatement(insertQuery);
			pst.setString(1, player_id);
			pst.setString(2, team_id);
			pst.executeUpdate();
			String GetID = "select LAST_INSERT_ID() as latest_id";
			rs = pst.executeQuery(GetID);
			while (rs.next()) {
				new_id = rs.getString("latest_id");
			}
			String updateQuery = "update team_player_audit set change_timestamp = now(), change_by= ?,change_type= ? "
					+ " where change_id = ? ";

			pst1 = conn.prepareStatement(updateQuery);
			pst1.setString(1, user);
			pst1.setString(2, operation);
			pst1.setString(3, new_id);
			pst1.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeStatement(pst1);
			DButility.closeStatement(pst);
			DButility.closeRs(rs);
			// DButility.closeConnectionAndStatement(conn, st);
		}
	}

	public List<TeamDto> getTeamsByQuery(String qStr, String league, int clubId) throws Exception {
		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "SELECT team_id, team_name, team_code FROM team WHERE league in (" + league
				+ ") and team_name like '%" + DButility.escape(qStr) + "%' ORDER BY team_name;";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			// pst.setString(1, league);
			rs = pst.executeQuery();
			while (rs.next()) {
				TeamDto team = new TeamDto();
				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}

	public int updateTeamName(TeamDto teamDto, int clubId) throws Exception {

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			String query = "update team set team_name = ? where team_id = ?";
			pst = conn.prepareStatement(query);
			pst.setString(1, teamDto.getTeamName());
			// pst.setString(2, teamDto.getLeague());
			pst.setInt(2, teamDto.getTeamID());
			return pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	public int updateInternalClubId(int teamId, int internalClubId, int clubId) throws Exception {

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			String query = "update team set club_id = ? where team_id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, internalClubId);
			pst.setInt(2, teamId);
			return pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	public List<TeamDto> getAllTeamsPendingApproval(int clubId) throws Exception {

		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "";
		if (clubId == 14202) {
			query = "select t.team_id,t.team_name,t.team_code,t.captain,t.vice_captain,t.wicket_keeper,t.is_active,date_created ,\r\n"
					+ "league,td.admin_name , td.admin_email c_email, td.captain_name, p2.email vc_email, \r\n"
					+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id,t.penalty, t.penalty_reason,  \r\n"
					+ "t.penalty_NRR, t.penalty_NRR_reason, t.information, t.is_locked, t.club_id,  c.name club_name,  \r\n"
					+ "t.HomeGround, t.logo_file_path, t.rating  \r\n" + "from team t \r\n"
					+ "left outer join mcc.player p1 on(p1.player_id = t.captain)  \r\n"
					+ "left outer join mcc.player p2 on (p2.player_id = t.vice_captain)  \r\n"
					+ "left outer join internal_club c on (c.club_id = t.club_id) \r\n"
					+ "left outer join team_details td on (td.team_id = t.team_id)\r\n"
					+ "where 1=1 and team_status = '0'  \r\n" + "order by team_id ";
		} else {
			query = "select t.team_id,t.team_name,t.team_code,t.captain,t.vice_captain,t.wicket_keeper,t.is_active,date_created ,\r\n"
					+ "league,td.admin_name , td.admin_email c_email, td.captain_name, p2.email vc_email, \r\n"
					+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id,t.penalty, t.penalty_reason,  \r\n"
					+ "t.penalty_NRR, t.penalty_NRR_reason, t.information, t.is_locked, t.club_id,  c.name club_name,  \r\n"
					+ "t.HomeGround, t.logo_file_path, t.rating  \r\n" + "from team t \r\n"
					+ "left outer join mcc.player p1 on(p1.player_id = t.captain)  \r\n"
					+ "left outer join mcc.player p2 on (p2.player_id = t.vice_captain)  \r\n"
					+ "left outer join internal_club c on (c.club_id = t.club_id) \r\n"
					+ "left outer join team_details td on (td.team_id = t.team_id)\r\n"
					+ "where 1=1 and team_status = '0'  \r\n" + "order by team_id ";
		}

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {

				TeamDto team = new TeamDto();

				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setCaptain(rs.getInt("captain"));
				team.setViceCaptain(rs.getInt("vice_captain"));
				team.setWicketKeeper(rs.getInt("wicket_keeper"));
				;
				team.setCaptainName(rs.getString("captain_name"));
				team.setViceCaptainName(rs.getString("vice_captain_name"));
				team.setLeague(rs.getString("league"));
				team.setGroup(rs.getInt("group_id"));
				team.setLocked(rs.getInt("is_locked") == 1);
				team.setPenalty(rs.getFloat("penalty"));
				team.setPenaltyReason(rs.getString("penalty_reason"));
				team.setPenaltyNRR(rs.getDouble("penalty_NRR"));
				team.setPenaltyNRRReason(rs.getString("penalty_NRR_reason"));
				team.setInformation(rs.getString("information"));
				team.setCaptainEmail(rs.getString("c_email"));
				team.setViceCaptainEmail(rs.getString("vc_email"));
				team.setClubId(rs.getInt("club_id"));
				team.setClubName(rs.getString("club_name"));
				team.setGroundId(rs.getInt("HomeGround"));
				team.setLogo_file_path(rs.getString("logo_file_path"));
				team.setTeamStatus("0");
				team.setRating(rs.getFloat("rating"));

				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;

	}

	public int approveTeam(int teamId, String userId, int clubId) throws Exception {

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			String query = "update team set team_status = 1 where team_id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, teamId);
			return pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	public void addUserToTeamOfficial(int clubIdParam, int teamId, int userId, String teamOfficialRole)
			throws Exception {

		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DButility.getConnection(clubIdParam);
			String insertQuery = "insert into team_official(user_id,role,team_id) VALUES (?,?,?)";
			pst = conn.prepareStatement(insertQuery);
			pst.setInt(1, userId);
			pst.setString(2, teamOfficialRole);
			pst.setInt(3, teamId);
			pst.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeRs(rs);
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}
	
	public void insertTeamOfficilaByCopyTeamId(int clubId, int copyTeamId, int newTeamId) throws Exception {

		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DButility.getConnection(clubId);
			String insertQuery = "insert into team_official(user_id,role,team_id) select user_id,role,? from team_official where team_id=?";
			pst = conn.prepareStatement(insertQuery);
			pst.setInt(1, newTeamId);
			pst.setInt(2, copyTeamId);

			pst.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeRs(rs);
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	public List<UserDto> getTeamOfficialByTeamId(int teamId, int clubId) throws Exception {

		List<UserDto> teamOfficials = new ArrayList<UserDto>();
		String query = "select uv.user_id, f_name, l_name, IF(profile_image_path IS NULL OR profile_image_path = '',"
				+ "(SELECT profilepic_file_path FROM mcc.player WHERE player_id = uv.player_id), NULL) profile_image_path, "
				+ "t.role, uv.player_id, uv.umpire_id  from mcc.user_view uv join team_official t on t.user_id = uv.user_id  "
				+ "and uv.club_id = ? and t.team_id = ?";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			pst.setInt(2, teamId);
			rs = pst.executeQuery();
			while (rs.next()) {
				UserDto user = new UserDto();
				user.setUserID(rs.getInt("user_id"));
				user.setPlayerID(rs.getInt("player_id"));
				user.setUmpireID(rs.getInt("umpire_id"));
				user.setFname(rs.getString("f_name"));
				user.setLname(rs.getString("l_name"));
				user.setTeamOffRole(rs.getString("role"));
				user.setProfileImagePath(rs.getString("profile_image_path"));
				teamOfficials.add(user);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teamOfficials;

	}

	public List<Integer> getTeamUserIds(int teamId, int clubId) throws Exception {

		List<Integer> teamUserIds = new ArrayList<Integer>();

		String query = "select uv.user_id from mcc.user_view uv, team_player tp "
				+ "where tp.player_id=uv.player_id and uv.club_id=? and tp.team_id=?";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			pst.setInt(2, teamId);
			rs = pst.executeQuery();
			while (rs.next()) {
				teamUserIds.add(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teamUserIds;

	}

	public void removeUserToTeamOfficial(int clubIdParam, int teamId, int userId) throws Exception {
		String query = "delete from team_official where user_id = " + userId + " and team_id = " + teamId;
		Connection conn = DButility.getConnection(clubIdParam);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	public List<String> getTeamOfficialsEmailByTeamIds(List<String> teamIds, int clubId) throws Exception {

		List<String> teamOfficialEmails = new ArrayList<String>();
		String query = "select DISTINCT uv.email from mcc.user_view uv join team_official t on t.user_id = uv.user_id  "
				+ "and uv.club_id = ? and t.team_id in(?)";
		String teamIdsLocal = String.join(",", teamIds);

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			pst.setString(2, teamIdsLocal);
			rs = pst.executeQuery();
			while (rs.next()) {
				teamOfficialEmails.add(rs.getString("email"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teamOfficialEmails;

	}

	public TeamDto getTeamByCustomteamIdAndLeagueId(int groupId, int leagueId, int clubId) throws Exception {
		TeamDto team = null;
		String query = "SELECT team_id, team_name, team_code, custom_team_id, league, club_id FROM team WHERE league = ? and custom_team_id = ? limit 1;";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setString(1, leagueId + "");
			pst.setString(2, groupId + "");
			rs = pst.executeQuery();
			while (rs.next()) {
				team = new TeamDto();
				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setCustom_team_id(rs.getString("custom_team_id"));
				team.setClubId(rs.getInt("club_id"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return team;
	}

	public List<TeamDto> getTeamsByName(String teamName, int clubId) throws Exception {
		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "SELECT team_id, team_name, team_code, custom_team_id, league, club_id, date_created "
				+ " FROM team WHERE team_name = ? AND YEAR(date_created) = YEAR(NOW());";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setString(1, teamName);
			rs = pst.executeQuery();
			while (rs.next()) {
				TeamDto team = new TeamDto();
				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setTeamCode(rs.getString("team_code"));
				team.setCustom_team_id(rs.getString("custom_team_id"));
				team.setClubId(rs.getInt("club_id"));
				team.setCreationDate(rs.getDate("date_created"));
				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}

	public List<TeamDto> getTeamsByNameSearch(String teamName, int clubId) throws Exception {

		List<TeamDto> teams = new ArrayList<TeamDto>();

		String query = "SELECT team_id, team_name, (SELECT league_name FROM league WHERE league_id = t.league ) sereis_name, "
				+ "(SELECT CONCAT(f_name,' ',l_name) FROM mcc.player WHERE player_id = t.captain) captain, "
				+ "(SELECT CONCAT(f_name,' ',l_name) FROM mcc.player WHERE player_id = t.vice_captain) vice_captain "
				+ " FROM team t WHERE team_name LIKE '%" + DButility.escape(teamName) + "%' ORDER BY team_id DESC ;";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {

				TeamDto team = new TeamDto();
				team.setTeamID(rs.getInt("team_id"));
				team.setTeamName(rs.getString("team_name"));
				team.setLeagueName(rs.getString("sereis_name"));
				team.setCaptainName(rs.getString("captain"));
				team.setViceCaptainName(rs.getString("vice_captain"));

				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}

	protected List<TeamDto> getPlayerAllTeams(int playerId, List<Integer> clubIds, int limit) throws Exception {

		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "";
		String schema = "";

		for (int i = 0; i < clubIds.size(); i++) {

			int clubId = clubIds.get(i);

			if (clubId == 1) {
				schema = "mcc";
			} else {
				schema = "club" + clubId;
			}

			query += "select team_id,team_name,team_code,captain,vice_captain,wicket_keeper,t.is_active,date_created ,league,"
					+ "CONCAT(p1.f_name , ' ',p1.l_name) captain_name, p1.email c_email,p2.email vc_email,"
					+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id, " + clubId
					+ " as club_id, t.HomeGround, t.logo_file_path  " + "from " + schema
					+ ".team t left outer join mcc.player p1 on(p1.player_id = t.captain) left outer "
					+ "join mcc.player p2 on (p2.player_id = t.vice_captain), " + schema + ".league l "
					+ "where t.league = l.league_id and l.hide_series = 0 and team_status = 1 and team_id in "
					+ " (select team_id from " + schema + ".team_player where player_id = " + playerId + ") "
					+ " LIMIT " + limit;
			if (i != clubIds.size() - 1) {
				query += " UNION ";
			}
		}

		query = " select team_id,team_name,team_code,captain,vice_captain,wicket_keeper,is_active,date_created,league,"
				+ " captain_name, c_email, vc_email, vice_captain_name,group_id, club_id, HomeGround, "
				+ " logo_file_path from (" + query + ") as final order by date_created desc";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {

				TeamDto team = populateTeamDto(rs);
				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}

	protected List<TeamDto> getPlayerAllCurrentTeams(int playerId, List<Integer> clubIds, int limit) throws Exception {

		List<TeamDto> teams = new ArrayList<TeamDto>();
		String query = "";
		String schema = "";

		for (int i = 0; i < clubIds.size(); i++) {

			int clubId = clubIds.get(i);

			if (clubId == 1) {
				schema = "mcc";
			} else {
				schema = "club" + clubId;
			}

			query += "select team_id,team_name,team_code,captain,vice_captain,wicket_keeper,t.is_active,date_created ,league,"
					+ "CONCAT(p1.f_name , ' ',p1.l_name) captain_name, p1.email c_email,p2.email vc_email,"
					+ "CONCAT(p2.f_name , ' ',p2.l_name) vice_captain_name,t.group_id, " + clubId
					+ " as club_id, t.HomeGround, t.logo_file_path  " + "from " + schema
					+ ".team t left outer join mcc.player p1 on(p1.player_id = t.captain) left outer "
					+ "join mcc.player p2 on (p2.player_id = t.vice_captain), " + schema + ".league l "
					+ "where t.league = l.league_id and l.hide_series = 0 and t.team_status = 1 and t.team_id in "
					+ " (select max(team_id) from " + schema + ".team_player where player_id = " + playerId + ") "
					+ " LIMIT " + limit;
			if (i != clubIds.size() - 1) {
				query += " UNION ";
			}
		}

		query = " select team_id,team_name,team_code,captain,vice_captain,wicket_keeper,is_active,date_created,league,"
				+ " captain_name, c_email, vc_email, vice_captain_name,group_id, club_id, HomeGround, "
				+ " logo_file_path from (" + query + ") as final order by date_created desc";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {

				TeamDto team = populateTeamDto(rs);
				teams.add(team);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teams;
	}

	protected Map<Integer, Integer> getPlayerTeamIdMap(int clubId) throws Exception {

		Map<Integer, Integer> playerTeamIdMap = new HashMap<Integer, Integer>();

		String query = "SELECT max(team_id) team_id, player_id FROM team_player group by player_id order by player_id";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				playerTeamIdMap.put(rs.getInt(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return playerTeamIdMap;
	}

	protected Map<Integer, Integer> getTeamInternalClubIdMap(int clubId) throws Exception {

		Map<Integer, Integer> teamInternalClubIdMap = new HashMap<Integer, Integer>();

		String query = "SELECT team_id, club_id FROM team where club_id>0 order by team_id";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				teamInternalClubIdMap.put(rs.getInt(1), rs.getInt(2));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return teamInternalClubIdMap;
	}

	private TeamDto populateTeamDto(ResultSet rs) throws Exception {

		TeamDto team = new TeamDto();

		team.setTeamID(rs.getInt("team_id"));
		team.setTeamName(rs.getString("team_name"));
		team.setTeamCode(rs.getString("team_code"));
		team.setCaptain(rs.getInt("captain"));
		team.setViceCaptain(rs.getInt("vice_captain"));
		team.setWicketKeeper(rs.getInt("wicket_keeper"));
		;
		team.setCaptainName(rs.getString("captain_name"));
		team.setViceCaptainName(rs.getString("vice_captain_name"));
		team.setLeague(rs.getString("league"));
		team.setGroup(rs.getInt("group_id"));
		team.setCaptainEmail(rs.getString("c_email"));
		team.setViceCaptainEmail(rs.getString("vc_email"));
		team.setClubId(rs.getInt("club_id"));
		team.setGroundId(rs.getInt("HomeGround"));
		team.setLogo_file_path(rs.getString("logo_file_path"));
		team.setCreationDate(rs.getDate("date_created"));

		return team;
	}

	public List<Integer> getTeamOfficialTeamIds(int userId, int clubId) throws Exception {
		List<Integer> teamIds = new ArrayList<Integer>();
		String getTeamIdsOfPlayer = "select user_id, team_id from team_official where user_id = ? ";
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(getTeamIdsOfPlayer);
			pst.setInt(1, userId);
			rs = pst.executeQuery();

			while (rs.next()) {
				teamIds.add(rs.getInt("team_id"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
			DButility.closeRs(rs);
		}
		return teamIds;
	}

	public TeamDetailsDto getTeamDetailsByTeamName(String teamName, int clubId) throws Exception {
		TeamDetailsDto teamDetailsDto = null;
		String selectQuery = "select max(team_id) teamId, team_name from team where lower(team_name) like ? limit 1";
		Connection conn = DButility.getReadConnection(clubId);
		int teamId = 0;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(selectQuery);
			pst.setString(1, teamName + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				teamId = rs.getInt("teamId");
			}
			if (teamId > 0) {
				teamDetailsDto = getTeamDetailsByTeamId(teamId + "", clubId);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
			DButility.closeRs(rs);
		}

		return teamDetailsDto;
	}

	public void saveTeamDetails(TeamDetailsDto teamDetailsDto, int clubId) throws Exception {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DButility.getConnection(clubId);
			String insertQuery = "insert into team_details(team_id, primary_contact_name, primary_contact_email, primary_contact_number,"
					+ "secondary_contact_name, secondary_contact_number, secondary_contact_email, admin_name, admin_number, admin_address, admin_email,"
					+ "captain_name, captain_number, is_omani_national, terms_condition) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(insertQuery);
			pst.setInt(1, teamDetailsDto.getTeamId());
			pst.setString(2, teamDetailsDto.getPrimaryContactName());
			pst.setString(3, teamDetailsDto.getPrimaryContactEmail());
			pst.setString(4, teamDetailsDto.getPrimaryContactNumber());
			pst.setString(5, teamDetailsDto.getSecondaryContactName());
			pst.setString(6, teamDetailsDto.getSecondaryContactNumber());
			pst.setString(7, teamDetailsDto.getSecondaryContactEmail());
			pst.setString(8, teamDetailsDto.getAdminName());
			pst.setString(9, teamDetailsDto.getAdminNumber());
			pst.setString(10, teamDetailsDto.getAdminAddress());
			pst.setString(11, teamDetailsDto.getAdminEmail());
			pst.setString(12, teamDetailsDto.getTeamCaptainName());
			pst.setString(13, teamDetailsDto.getTeamCaptainContact());
			pst.setBoolean(14, teamDetailsDto.isOnlyOmaniNationals());
			pst.setBoolean(15, teamDetailsDto.isTermsConditionsAccepted());
			pst.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeRs(rs);
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	public TeamDetailsDto getTeamDetailsByTeamId(String teamId, int clubId) throws Exception {
		TeamDetailsDto teamDetailsDto = null;
		Connection conn = DButility.getReadConnection(clubId);
		ResultSet rs1 = null;
		PreparedStatement pst1 = null;
		try {
			String selectQuery = "select t.team_name , td.primary_contact_name, td.primary_contact_email, td.primary_contact_number, td.secondary_contact_name, "
					+ "td.secondary_contact_email, td.secondary_contact_number, td.is_omani_national, td.terms_condition, td.captain_name, "
					+ "td.captain_number, td.admin_name, td.admin_number, td.admin_email, td.admin_address "
					+ "from team t " + "left join team_details td on td.team_id = t.team_id " + "where t.team_id = ? ";
			pst1 = conn.prepareStatement(selectQuery);
			pst1.setInt(1, CommonUtility.stringToInt(teamId));
			rs1 = pst1.executeQuery();
			while (rs1.next()) {
				teamDetailsDto = new TeamDetailsDto();
				teamDetailsDto.setTeamId(CommonUtility.stringToInt(teamId));
				teamDetailsDto.setTeamName(rs1.getString("team_name"));
				teamDetailsDto.setAdminAddress(rs1.getString("admin_address"));
				teamDetailsDto.setAdminEmail(rs1.getString("admin_email"));
				teamDetailsDto.setAdminName(rs1.getString("admin_name"));
				teamDetailsDto.setAdminNumber(rs1.getString("admin_number"));
				teamDetailsDto.setOnlyOmaniNationals(rs1.getBoolean("is_omani_national"));
				teamDetailsDto.setPrimaryContactEmail(rs1.getString("primary_contact_email"));
				teamDetailsDto.setPrimaryContactName(rs1.getString("primary_contact_name"));
				teamDetailsDto.setPrimaryContactNumber(rs1.getString("primary_contact_number"));
				teamDetailsDto.setSecondaryContactEmail(rs1.getString("secondary_contact_email"));
				teamDetailsDto.setSecondaryContactName(rs1.getString("secondary_contact_name"));
				teamDetailsDto.setSecondaryContactNumber(rs1.getString("secondary_contact_number"));
				teamDetailsDto.setTeamCaptainName(rs1.getString("captain_name"));
				teamDetailsDto.setTeamCaptainContact(rs1.getString("captain_number"));
				teamDetailsDto.setTermsConditionsAccepted(rs1.getBoolean("terms_condition"));
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst1);
			DButility.closeRs(rs1);
		}
		return teamDetailsDto;
	}

	public void getTeamDetailsByTeamId(int clubId, String apiTeamName, List<Integer> teamIdList) throws Exception {
		String teamIds = teamIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
		if (!CommonUtility.isNullOrEmpty(teamIds) && !CommonUtility.isNullOrEmpty(apiTeamName)) {
			String query = "update team set team_name = ? where team_id in(" + teamIds + ")";
			Connection conn = DButility.getConnection(clubId);
			PreparedStatement pst = null;
			try {
				pst = conn.prepareStatement(query);
				pst.setString(1, apiTeamName);
				pst.executeUpdate();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.closeConnectionAndStatement(conn, pst);
			}
		}
	}

	protected void updateTeamBackGroundImagePath(String backGroundImagePath, int teamId, int clubId, int userId)
			throws Exception {

		String query = "update team set back_ground_image_path = ? where team_id = ?";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);

			int i = 1;
			st.setString(i++, backGroundImagePath.trim());
			st.setInt(i++, teamId);

			st.executeUpdate();

			teamAudit(teamId, clubId, "update", userId + "", conn);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}

	protected void removeUnSyncPlayersFromTeam(List<Integer> dbPlayerIds, int teamId) throws Exception {

		String deletePlayers = "";

		if (dbPlayerIds != null && dbPlayerIds.size() > 0) {
			String commaSeparatedIds = dbPlayerIds.stream().map(String::valueOf).collect(Collectors.joining(","));
			String selectQuery = "SELECT player_id FROM team_player WHERE team_id = ? AND player_id NOT IN("
					+ commaSeparatedIds + ");";
			Connection conn = DButility.getConnection(8318);
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				pst = conn.prepareStatement(selectQuery);
				pst.setInt(1, teamId);
				rs = pst.executeQuery();
				while (rs.next()) {
					deletePlayers += rs.getString("player_id") + ",";
				}
				if (!CommonUtility.isNullOrEmpty(deletePlayers)) {
					deletePlayers = deletePlayers.substring(0, deletePlayers.length() - 1);
					String deleteQuery = "DELETE FROM team_player WHERE team_id = ? AND player_id IN(" + deletePlayers
							+ ");";
					PreparedStatement st = null;
					try {
						st = conn.prepareStatement(deleteQuery);
						st.setInt(1, teamId);
						st.executeUpdate();
					} catch (SQLException e) {
						throw new Exception(e.getMessage());
					} finally {
						DButility.closeConnectionAndStatement(conn, st);
					}
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.closeConnectionAndStatement(conn, pst);
				DButility.closeRs(rs);
			}
		}
	}
	
	
	public List<String>  getPlayerTeamInternalClubsList(int playerId, int clubId) throws Exception{
	 
			String query = " SELECT distinct cp.internal_club_id, tp.team_id  FROM team_player tp, club_player cp, team t "
					+ "WHERE tp.player_id = ? AND tp.player_id= cp.player_id AND t.team_id = tp.team_id AND t.club_id = cp.internal_club_id";
			Connection conn = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
		
			try {
				conn = DButility.getReadConnection(clubId);
				pst = conn.prepareStatement(query);
				pst.setInt(1, playerId);
				rs = pst.executeQuery();
				List<String> clubTeamsList = new ArrayList<String>();
				while (rs.next()) {
					clubTeamsList.add(rs.getInt(1)+"-"+rs.getInt(2));
				}
				return  clubTeamsList;
				
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				DButility.dbCloseAll(conn, pst, rs);
			}
	

		}

	public int getTeamIdFromTeamDetails(int teamId,int clubId) throws Exception {
		Connection conn = DButility.getReadConnection(clubId);
		int count = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String query = "select team_id from team_details where team_id = ?";
			st = conn.prepareStatement(query);
			st.setInt(1, teamId);
			rs = st.executeQuery();
			
			while (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return count;
		
	}

	public void updateteamDetailsByCaptainName(int teamId, int clubId, String captainName) throws Exception {

		Connection conn = DButility.getConnection(clubId);
		String updateTeamCaptain = "update team_details set captain_name = ? where team_id=?";
		PreparedStatement pst = null;

		
		try {

			pst = conn.prepareStatement(updateTeamCaptain);
			
			pst.setString(1, captainName);
			pst.setInt(2, teamId);
		
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	
		
	}
	protected Map<Integer,String> getCoaches( int clubId) throws Exception {

		Map<Integer,String>coaches = new HashMap<Integer,String>();

		String query = "select u.user_id, concat(u.f_name,' ',l_name) name from mcc.umpire_view u where u.type like '%c%' and club_id = "+clubId+"";
				

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			rs = st.executeQuery();

			while (rs.next()) {
				coaches .put(rs.getInt("user_id"), rs.getString("name"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return coaches;
	}
	
}
