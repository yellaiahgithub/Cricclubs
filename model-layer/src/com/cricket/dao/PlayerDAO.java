/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dao;

import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.BallByBallExtrasDto;
import com.cricket.dto.BallByBallWicketsBoundaryDto;
import com.cricket.dto.BcmclWaiverRecord;
import com.cricket.dto.BowlerPerformanceDto;
import com.cricket.dto.ClubDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.Pair;
import com.cricket.dto.PlayerAdditionalDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.PlayerMatchesCountDto;
import com.cricket.dto.PlayerTeamDto;
import com.cricket.dto.RunsBreakDownDto;
import com.cricket.dto.UserDto;
import com.cricket.dto.insights.BattingPositionDTO;
import com.cricket.dto.insights.OutTypesBowlingDto;
import com.cricket.dto.insights.OutTypesDto;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.exception.CCErrorConstant;
import com.cricket.exception.CCException;
import com.cricket.helpers.PlayerDtoComparator;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;
import com.cricket.utility.batch.GraphDBBackGroundProcess;
import com.google.gson.Gson;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PlayerDAO {
	private static Logger log = LoggerFactory.getLogger(PlayerDAO.class);
	private static String PLAYER_QUERY = "select p.player_id," + " p.l_name, p.emercency_contact_no, p.emercency_contact_name, p.emercency_contact_rel,"
			+ " p.f_name, p.gender," + "p.batting_style," + "p.phone," + "p.address,"
			+ "p.email," + "p.teams_played," + "p.profile_description, p.can_play_anyteam, p.can_play_within_club,"
			+ "p.testimonal," + "p.season," + "p.date_palyed," + "p.is_Active, p.accepted_terms, p.jersey_number,"
			+ "p.nickname," + "p.bowling_style," + "p.date_of_birth,"+ "p.create_date,"
			+ "p.playing_role,p.custom_id," + "IFNULL(t.team_id,0) team_id,"
			+ "IFNULL(t.team_name,'') team_name, team_code,"
			+ "IFNULL(t.logo_file_path,'') teamlogo_file_path, p.club_id,p.src_player_id, p.profilepic_file_path, p.category "; 
	
	protected int registerPlayer(PlayerDto player, int clubId, String user) throws Exception {
		String firstName = CommonUtility.trimString(CommonUtility.toDisplayCase(DButility.escapeQuotes(DButility.escapeLine(player.getFirstName()))));
		String lastName = CommonUtility.trimString(CommonUtility.toDisplayCase(DButility.escapeQuotes(DButility.escapeLine(player.getLastName()))));
		String nickName = DButility.escapeQuotes(DButility.escapeLine(player.getNickName()));
		String gender = DButility.escapeQuotes(DButility.escapeLine(player.getGender()));
		String email = player.getEmail();
		String battingStyle = player.getBattingStyle();
		String bowlingStyle = player.getBowlingStyle();
		//String address = DButility.escapeLine(player.getAddress());
		String contactNo = player.getContactNo();
		String dateOfBirth = player.getDateOfBirth();
		String teamsPlayed = player.getTeamsPlayed();
		if(clubId == 7683) {
			int custId = getMaxCustomId(clubId);
			player.setSrcPlayerId(String.valueOf(custId+1));
		}
		String customID = DButility.escapeQuotes(DButility.escapeLine(player.getCustomId()));
		
		String profileDesc = player.getProfileDesc();
		String testimonal = player.getTestimonial();
		// String datePlayed = player.getDatePlayed();
		String isActive = player.getIsActive();
		String playingRole = player.getPlayingRole();
		int player_id = 0;

		String query;
		PreparedStatement st = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			query = "insert into player(l_name,f_name,gender,emercency_contact_no, emercency_contact_name, emercency_contact_rel, batting_style,"
					+ "phone,email,teams_played,profile_description,testimonal,season,"
					+ "date_palyed,nickname,bowling_style,date_of_birth,playing_role,"
					+ "src_player_id, jersey_number,category) values (?,?,?,?,?,?,?,?,?,?,?,?,'',' ',?,?,?,?,?,?,?)";

			conn = DButility.getDefaultConnection();
			
			int index = 1;
			
			st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			st.setString(index++, lastName);
			st.setString(index++, firstName);
			st.setString(index++, gender);
			
			st.setString(index++, player.getEmercencyContactNo());
			st.setString(index++, player.getEmercencyContactName());
			st.setString(index++, player.getEmercencyContactRel());
			
			st.setString(index++, battingStyle);
			st.setString(index++, contactNo);
			//st.setString(index++, address);
			st.setString(index++, email);
			st.setString(index++, teamsPlayed);
			st.setString(index++, profileDesc);
			st.setString(index++, testimonal);
			st.setString(index++, nickName);
			st.setString(index++, bowlingStyle);
			st.setString(index++, dateOfBirth);
			st.setString(index++, playingRole);
//			st.setString(index++, customID);
			st.setString(index++, player.getSrcPlayerId());
			st.setString(index++, player.getJerseyNumber());
			st.setString(index++, player.getCategory());
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			
			if (rs.next()) {
				player_id = rs.getInt(1);
			}
			
			/*rs = st.executeQuery("select max(player_id) player_id from player");
			rs.next();
			player_id = rs.getInt("player_id");*/
			if (st != null) {
				st.close();
			}
			String clubQuery = "insert into player_club(player_id,club_id,"
					+ "create_date,is_active,accepted_terms,custom_id) values (?,?, NOW(),?,?,?)" ;					
					
			st = conn.prepareStatement(clubQuery);
			st.setInt(1, player_id);
			st.setInt(2, clubId);
			st.setString(3, isActive);
			st.setString(4, (player.isAcceptedTerms() ? "1" : "0"));
			st.setString(5, customID);
			st.executeUpdate();
			
			//Audit Log Methods Called and parameters sent 
			String playerid = Integer.toString(player_id);
			String clubid = Integer.toString(clubId);
			
			auditPlayer(playerid,clubid,user,"insert",conn);
			auditPlayerClub(playerid,clubid,user,"insert",conn);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if(rs!=null){
				rs.close();
			}
			DButility.closeConnectionAndStatement(conn, st);
		}
		return player_id;
	}
	
	protected List<PlayerDto> getAllPlayersOfClubForPractice(int clubId) {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = "select player_id, l_name, f_name, batting_style, phone, address, email, nickname, bowling_style, playing_role, "
				+ " profilepic_file_path, club_id, jersey_number from player_view where club_id = ? and is_active = 1 limit 100";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			preparePlayerListForPractic(players, rs, true);
		} catch (SQLException e) {
			return players;
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	protected PlayerDto getPlayerBasicDetails(int playerId) throws Exception {
		
		String query = "select player_id, l_name, gender, email, date_of_birth, f_name, batting_style, address, "
				+ "bowling_style, playing_role, profilepic_file_path from player where player_id = ?";
		
		PlayerDto player = null;
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			rs = st.executeQuery();
			
			while (rs.next()) {

				player = new PlayerDto();

				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				player.setEmail(rs.getString("email"));
				player.setGender(rs.getString("gender"));
				player.setDateOfBirth(rs.getString("date_of_birth"));
				player.setAddress(rs.getString("address"));
				player.setBattingStyle(rs.getString("batting_style"));
				player.setBowlingStyle(rs.getString("bowling_style"));
				player.setPlayingRole(rs.getString("playing_role"));
				player.setProfilepic_file_path(rs.getString("profilepic_file_path"));

			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return player;
	}
	
	protected List<PlayerDto> getAllPlayerBasicDetailsHavingEmail() throws Exception {
		
		String query = "select player_id, l_name, f_name, batting_style, address, "
				+ "bowling_style, playing_role, profilepic_file_path from player "
				+ "where email is not null and email != '' and email like '%@%' order by player_id desc";
		
		PlayerDto player = null;
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<PlayerDto> playersList = new ArrayList<PlayerDto>();
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			
			while (rs.next()) {

				player = new PlayerDto();

				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				player.setAddress(rs.getString("address"));
				player.setBattingStyle(rs.getString("batting_style"));
				player.setBowlingStyle(rs.getString("bowling_style"));
				player.setPlayingRole(rs.getString("playing_role"));
				player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
				
				playersList.add(player);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playersList;
	}

	private void preparePlayerListForPractic(List<PlayerDto> players, ResultSet rs, boolean primaryOnly)
			throws SQLException {
		while (rs.next()) {
			PlayerDto player = new PlayerDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setEmail(rs.getString("email"));
			player.setContactNo(rs.getString("phone"));
			player.setAddress(rs.getString("address"));
			player.setBattingStyle(rs.getString("batting_style"));
			player.setBowlingStyle(rs.getString("bowling_style"));
			player.setNickName(rs.getString("nickname"));
			player.setPlayingRole(rs.getString("playing_role"));
			player.setClubId(rs.getInt("club_id"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setJerseyNumber(rs.getString("jersey_number"));
			players.add(player);
			
		}
	}
	
	protected List<PlayerDto> getLeaguPlayersForWeeklySummary(int leagueId, int clubId)
			throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		
		String query = PLAYER_QUERY + " from mcc.player_view p, " + DButility.getDBName(clubId) + ".team_player tp, "
				+ DButility.getDBName(clubId) + ".team t where p.club_id = " + clubId
				+ " and p.is_active = '1' and length(trim(p.email)) > 0 "
				+ "	and p.player_id = tp.player_id and tp.is_secondary is null and "
				+ "	tp.team_id = t.team_id and t.league = "+leagueId+" and p.player_id not in "
				+ "	(select uv.player_id from mcc.user_view uv, mcc.user_notifications un "
				+ "	where uv.club_id = " + clubId + " and uv.user_id = un.user_id and un.weekly_summary = 0 "
				+ "and uv.player_id is not null) ";
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			preparePlayerList(players, rs, true);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	protected List<PlayerDto> getLeaguPlayers(int leagueId, int clubId, String status)
			throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		
		String query = PLAYER_QUERY + " from mcc.player_view p, " + DButility.getDBName(clubId) + ".team_player tp, "
				+ DButility.getDBName(clubId) + ".team t where p.club_id = " + clubId
				+ " and p.is_active = '"+status+"' and p.player_id = tp.player_id and tp.is_secondary is null and "
				+ "	tp.team_id = t.team_id and t.league = "+leagueId+" and p.player_id not in "
				+ "	(select uv.player_id from mcc.user_view uv, mcc.user_notifications un "
				+ "	where uv.club_id = " + clubId + " and uv.user_id = un.user_id and un.weekly_summary = 0 "
				+ "and uv.player_id is not null) ";
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			preparePlayerList(players, rs, true);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	protected List<PlayerDto> getPlayers(PlayerDto dto, int clubId, int limit)
			throws Exception {
		boolean condition = false;
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = PLAYER_QUERY
				+ "from mcc.player_view p left join " + DButility.getDBName(clubId) + ".team_player tp "
						+ "on (p.player_id=tp.player_id and tp.is_secondary is null) left join "
				+ DButility.getDBName(clubId) + ".team t on (t.team_id = tp.team_id) ";
		if (clubId > 0) {
			query += "where p.club_id = " + clubId;
			condition = true;
		} else {
			query += "where 1=1 ";
		}
		
		
		if (dto.getLastName() != null && !"".equals(dto.getLastName())) {
			query += " AND p.l_name like '%" + dto.getLastName() + "%'";
			condition = true;
		}

		if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
			query += " AND p.email = '" + dto.getEmail() + "'";
			condition = true;
		}

		if (dto.getFirstName() != null && !"".equals(dto.getFirstName())) {
			query += " AND p.f_name like '%" + dto.getFirstName() + "%'";			
			condition = true;
		}

		if (dto.getTeamName() != null && !"".equals(dto.getTeamName())) {
			query += " AND t.team_name like '%" + dto.getTeamName() + "%'";
			query = query.replaceAll("left join", "join"); 
			condition = true;
		}

		if (dto.getIsActive() != null && !"".equals(dto.getIsActive())) {
			String subQuery = " AND p.is_active = '" + dto.getIsActive() + "'";
			if(dto.getIsClubAdminApproval()>1) {
				subQuery = " AND (p.is_active = '"+dto.getIsActive()+"' or p.is_active = '"+dto.getIsClubAdminApproval()+"') ";
			}
			query += subQuery;
			condition = true;
		}

		if (!CommonUtility.isNullOrEmpty(dto.getCustomId())) {
			query += " AND p.custom_id = '" + dto.getCustomId() + "'";
			condition = true;
		}

		if (dto.isAvailablePlayersOnly()) {
			query += " AND p.player_id not in (select player_id from " + DButility.getDBName(clubId) + ".team_player tp," + DButility.getDBName(clubId) + ".team t," + DButility.getDBName(clubId)
					+ ".league l where t.team_id = tp.team_id and t.league = "
					+ dto.getLeagueId() + " )";
			condition = true;
		}

		if (dto.isSelectedPlayersOnly()) {
			query += " AND  t.league = "
					+ dto.getLeagueId() ;
			condition = true;
		}

		if (dto.getPlayerID() != 0) {
			query += " AND p.player_id = " + dto.getPlayerID();
			condition = true;
		}

		if (dto.getTeamId() != 0) {
			query += " AND p.player_id in (select player_id from " + DButility.getDBName(clubId) + ".team_player where t.team_id = "
					+ dto.getTeamId() + ")";
			condition = true;
		}

		query += " order by p.f_name,p.l_name,t.team_id desc ";
		
		if(limit > 0) {
			query +="limit "+ limit;
		}

		if(condition == false){
			throw new CCException("ClubId : "+ clubId +" No criteria selected for Player."+new Gson().toJson(dto), CCErrorConstant.NO_CRITERIA_SELECTED_FOR_PLAYERS);
		}
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId, false);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			preparePlayerList(players, rs, true);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	protected int getMaxCustomId(int clubId) throws Exception {
		//UmpireMatchReportDto reports = new UmpireMatchReportDto();
		String query =
				"select max(src_player_id) as count from " +					
						" player_view " +
						"where club_id = ? ";
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int count = 0;

		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			rs = pst.executeQuery();
			while(rs.next())
			{
			count = rs.getInt("count");
			}
			
		} catch (SQLException e) {
			return 0;
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return count;

	}
	
	protected List<PlayerDto> getSearchPlayers(PlayerDto dto, int clubId)
			throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		
		if(!CommonUtility.isNullOrEmptyOrNULL((dto.getResidentID()))) {
			int playerId = getPlayerIdByResidentId(dto.getResidentID(), clubId);
			if(playerId > 0) {
				dto.setPlayerID(playerId);
			}else if(playerId == 0  && CommonUtility.isNullOrEmptyOrNULL(dto.getFirstName()) && CommonUtility.isNullOrEmptyOrNULL(dto.getTeamName())){
				players = null;
				return players;
			}
		}
		String query = PLAYER_QUERY
				+ "from mcc.player_view p left join " + DButility.getDBName(clubId) + ".team_player tp on (p.player_id=tp.player_id and tp.is_secondary is null) left join "
				+ DButility.getDBName(clubId) + ".team t on (t.team_id = tp.team_id) ";
		
		if (!CommonUtility.isNullOrEmpty(dto.getInternalClub())) {
			query += " JOIN "+ DButility.getDBName(clubId) + ".internal_club ic ";
			query += " join  "+ DButility.getDBName(clubId) + ".club_player cp on cp.player_id = p.player_id and cp.internal_club_id = ic.club_id " ;
		}
		
		if(clubId == 21827 || clubId == 21870 || clubId == 50) {
			query += " LEFT JOIN mcc.player_additional pa ON p.player_id = pa.player_id ";
		}
		/*
		 * //join table when role is present write iff if (dto.getRoleId()>0) { query
		 * +=" LEFT JOIN mcc.user_roles pu ON  pu.club_id=p.club_id "; }
		 */
		if (clubId > 0) {
			query += "where p.club_id = " + clubId;
		} else {
			query += "where 1=1 ";
		}
		boolean condition = false;
		
				
		if(clubId == 21827 || clubId == 21870 || clubId == 50) {
			if(!CommonUtility.isNullOrEmpty(dto.getResidentID())) {
				query +=" and pa.resident_id = '"+dto.getResidentID()+"' ";
			}
			if(!CommonUtility.isNullOrEmpty(dto.getPassportNum())) {
				query +=" and pa.passport_num = '"+dto.getPassportNum()+"' ";
			}
			condition = true;
		}
		
		if (dto.getLastName() != null && !"".equals(dto.getLastName())) {
			query += " AND p.l_name like '%" + DButility.escapeLine(dto.getLastName()) + "%'";
			condition = true;
		}

		if (!CommonUtility.isNullOrEmpty(dto.getEmail())) {
			query += " AND p.email = '" + dto.getEmail() + "'";
			condition = true;
		}
		
		if (!CommonUtility.isNullOrEmpty(dto.getBattingStyle())) {
			query += " AND p.batting_style = '" + dto.getBattingStyle() + "'";
			condition = true;
		}
		
		if (!CommonUtility.isNullOrEmpty(dto.getBowlingStyle())) {
			query += " AND p.bowling_style = '" + dto.getBowlingStyle() + "'";
			condition = true;
		}

		if (!CommonUtility.isNullOrEmpty(dto.getPlayingRole())) {
			query += " AND p.playing_role = '" + dto.getPlayingRole()+ "'";
			condition = true;
		}
		
		if (!CommonUtility.isNullOrEmpty(dto.getCanPlayerplayforanyteam())) {
			query += " AND p.can_play_anyteam= '" + dto.getCanPlayerplayforanyteam() + "'";
			condition = true;
		}
		
		if (!CommonUtility.isNullOrEmpty(dto.getGender())) {
			query += " AND p.gender = '" + dto.getGender() + "'";
			condition = true;
		}
		
		if (dto.getPlayerID() > 0) {
			query += " AND p.player_id = '" + dto.getPlayerID() + "'";
			condition = true;
		}
		
		if (!CommonUtility.isNullOrEmpty(dto.getInternalClub())) {
			query += " AND ic.name like '%" + DButility.escapeLine(dto.getInternalClub()) + "%'";
			condition = true;
		}
		
		if (dto.getTeamName() != null && !"".equals(dto.getTeamName())) {
			query += " AND t.team_name like '%" + DButility.escapeLine(dto.getTeamName()) + "%'";
			query = query.replaceAll("left join", "join"); 
			condition = true;
		}

		if (dto.getFirstName() != null && !"".equals(dto.getFirstName())) {
			/*query += " AND (p.f_name like '%" + dto.getFirstName() + "%'";
			query += " OR p.l_name like '%" + dto.getFirstName() + "%')";*/
			query += " AND (CONCAT(p.f_name, ' ', p.l_name) like '%" + DButility.escapeLine(dto.getFirstName()) + "%')";
			condition = true;
		}

		if (dto.getIsActive() != null && !"".equals(dto.getIsActive())) {
			query += " AND p.is_active = '" + dto.getIsActive() + "'";
			condition = true;
		}

		if (!CommonUtility.isNullOrEmpty(dto.getCustomId())) {
			query += " AND p.custom_id = '" + dto.getCustomId() + "'";
			condition = true;
		}		
		if (dto.isAvailablePlayersOnly()) {
			query += " AND p.player_id not in (select player_id from " + DButility.getDBName(clubId) + ".team_player tp," + DButility.getDBName(clubId) + ".team t," + DButility.getDBName(clubId)
					+ ".league l where t.team_id = tp.team_id and t.league = "
					+ dto.getLeagueId() + " )";
			condition = true;
		}

		if (dto.isSelectedPlayersOnly()) {
			query += " AND  t.league = "
					+ dto.getLeagueId() ;
			condition = true;
		}

		if (dto.getPlayerID() != 0) {
			query += " AND p.player_id = " + dto.getPlayerID();
			condition = true;
		}

		if (dto.getTeamId() != 0) {
			query += " AND p.player_id in (select player_id from " + DButility.getDBName(clubId) + ".team_player where t.team_id = "
					+ dto.getTeamId() + ")";
			condition = true;
		}
		
		//search new query	
		if (dto.getRoleId()>0) {
			// query += " AND pu.role=(SELECT roleId  FROM  "+DButility.getDBName(clubId)+".user_role_info WHERE roleName= '" + dto.getRole() + "')";
			query += " AND "+dto.getRoleId()+" IN (SELECT role FROM mcc.user_roles ur, mcc.user u WHERE u.user_id = ur.user_id AND u.player_id = p.player_id and ur.club_id="+clubId+") ";
			condition = true;
		}

		query += " order by p.f_name,p.l_name,t.team_id desc";

		if(condition == false){
			throw new CCException("No criteria selected for Player.", CCErrorConstant.NO_CRITERIA_SELECTED_FOR_PLAYERS);
		}
		//System.out.println("query is "+query);
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId, false);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			preparePlayerList(players, rs, true);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	protected List<PlayerMatchesCountDto> getPlayerMatchesCount(PlayerMatchesCountDto dto, int leagueId, int clubId,boolean clubStructure) throws Exception {
		List<PlayerMatchesCountDto> players = new ArrayList<PlayerMatchesCountDto>();
		
			String query = " SELECT p.player_id, p.f_name, p.l_name,";
				
				if(clubStructure){
					query +=  " c.name club_name,";
				}
					query +=  " t2.team_name home_team,l2.league_name home_division, " 
							+ " t.team_name,l.league_name ,COUNT(*) count "
							+ " FROM mcc.player_view p, team t, ";
				if(clubStructure){
					query += "internal_club c,"	;
				}
					query +=  " league l," 
								+ " match_player_team mpt, team_player tp, team t2, league l2 "
								+ " WHERE mpt.team_id = t.team_id AND ";
				if(clubStructure){	
					query += "t.club_id = c.club_id and ";
				}
					query	+=	"  l.league_id = t.league "
								+ " and tp.player_id = p.player_id and tp.team_id = t2.team_id and "
								+ " t2.league = l2.league_id AND "
								+ " p.club_id = ? AND "
								+ " t.team_id = t2.team_id AND "
								+ " mpt.player_id = p.player_id AND "
								+ " t.league IN ( select league_id from league where parent = ? "
								+ " OR league_id = ? ) "
								+ " AND t2.league IN ( select league_id from league where parent = ? "
								+ " OR league_id = ? ) AND mpt.match_id IN(SELECT match_id FROM matches WHERE match_type <>'p') "
								+ " GROUP BY p.player_id , p.f_name , p.l_name , t.team_name order by 2,3,4,5 ";
			
			
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			int i =1;
			if(clubId >0){
				st.setInt(i++, clubId);
			}
			if(leagueId > 0){
				for (int j=1 ; j<=4 ; j++){
					st.setInt(i++, leagueId);
				}
			}
			rs = st.executeQuery();
			while (rs.next()) {
				PlayerMatchesCountDto player = new PlayerMatchesCountDto();
				player.setPlayerId(rs.getInt("p.player_id"));
				player.setFirstName(rs.getString("p.f_name"));
				player.setLastName(rs.getString("p.l_name"));
				if(clubStructure){
					player.setClubName(rs.getString("club_name"));
				}
				player.setHomeLeagueName(rs.getString("home_team"));
				player.setHomeTeamName(rs.getString("home_division"));
				player.setLeagueName(rs.getString("team_name"));
				player.setTeamName(rs.getString("league_name"));
				player.setCount(rs.getInt("count"));
				players.add(player);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	/**
	 * @param dto
	 * @return
	 */
	protected List<PlayerDto> getActivePlayersForAdminPage(int clubId, int limit, int skip)
			throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query =  " SELECT "
				+ "     p.player_id,"
				+ "     p.l_name,"
				+ "     p.f_name,"
				+ "     p.gender,"
				+ "     p.emercency_contact_no, p.emercency_contact_name, p.emercency_contact_rel, "
				+ "     p.batting_style,"
				+ "     p.phone,"
				+ "     p.address,"
				+ "     p.email,"
				+ "     p.teams_played,"
				+ "     p.profile_description,"
				+ "     p.testimonal,"
				+ "     p.season,"
				+ "     p.date_palyed,"
				+ "     p.is_Active,"
				+ "     p.accepted_terms,"
				+ "     p.can_play_anyteam,"
				+ "     p.can_play_within_club,"
				+ "     p.nickname,"
				+ "     p.bowling_style,"
				+ "     p.date_of_birth,"
				+ "     p.create_date,"
				+ "     p.playing_role,"
				+ "     p.custom_id,"
				+ "     IFNULL(t.team_id, 0) team_id,"
				+ "     IFNULL(t.team_name, '') team_name,IFNULL(t.logo_file_path,'') teamlogo_file_path,IFNULL(t.team_code,'') team_code,"
				+ "     p.club_id,"
				+ "     p.jersey_number,"
				+ "     p.src_player_id,"
				+ "     p.profilepic_file_path,"
				+ "		p.category"
				+ " FROM"
				+ "     mcc.player_view p"
				+ "         JOIN "
			    + DButility.getDBName(clubId) + ".team_player tp ON (p.player_id = tp.player_id"
				+ "         AND tp.is_secondary IS NULL)"
				+ "         JOIN "
			    + DButility.getDBName(clubId) + ".team t ON (t.team_id = tp.team_id)"
				+ " WHERE"
				+ "     p.club_id = " + clubId+" AND p.is_active = '1' "
				+ " AND t.team_id IN (SELECT MAX(team_id) FROM "+DButility.getDBName(clubId)+".team_player WHERE player_id = p.player_id)"
				+ " union"
				+ " SELECT "
				+ "     p.player_id,"
				+ "     p.l_name,"
				+ "     p.f_name,"
				+ "     p.gender,"
				+ "     p.emercency_contact_no, p.emercency_contact_name, p.emercency_contact_rel, "
				+ "     p.batting_style,"
				+ "     p.phone,"
				+ "     p.address,"
				+ "     p.email,"
				+ "     p.teams_played,"
				+ "     p.profile_description,"
				+ "     p.testimonal,"
				+ "     p.season,"
				+ "     p.date_palyed,"
				+ "     p.is_Active,"
				+ "     p.accepted_terms,"
				+ "     p.can_play_anyteam,"
				+ "     p.can_play_within_club,"
				
				+ "     p.nickname,"
				+ "     p.bowling_style,"
				+ "     p.date_of_birth,"
				+ "     p.create_date,"
				+ "     p.playing_role,"
				+ "     p.custom_id,"
				+ "     0 team_id,"
				+ "     '' team_name, '' teamlogo_file_path, '' team_code,"
				+ "     p.club_id,"
				+ "     p.jersey_number,"
				+ "     p.src_player_id,"
				+ "     p.profilepic_file_path,"
				+ "		p.category"
				+ " FROM"
				+ "     mcc.player_view p"
				+ " WHERE"
				+ "     p.club_id = ? AND p.is_active = '1'"
				+ "     and p.player_id not in (select player_id from "+ DButility.getDBName(clubId) + ".team_player)"
				+ "     ORDER BY create_date DESC,3,2  ";
		
		if (limit > 0) {
			if(skip>0) {
				query += " LIMIT " + skip + ", "+ limit;
			}else {
				query += " LIMIT " + limit;
			}
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId, false);
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			rs = pst.executeQuery();
			preparePlayerList(players, rs, true);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return players;
	}

	protected List<PlayerDto> getActiveAvailablePlayers(List<Integer> leagueIds, int clubId, boolean emailOnly)
			throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		StringBuffer query = new StringBuffer("SELECT ");
		query.append("  p.player_id,");
		query.append("     p.l_name,");
		query.append("     p.f_name,");
		query.append("     0 team_id,");
		query.append("  p.custom_id,");
		query.append("     '' team_name,'' teamlogo_file_path,'' team_code ");
		query.append(" FROM");
		query.append("     mcc.player_view p");
		query.append(" WHERE");
		query.append("     p.club_id = ? AND p.is_active = '1' ");
		if ( emailOnly == true) {
			query.append(" and p.email is not null and p.email like '%@%.%' ");  
		}
		query.append("         AND p.player_id NOT IN (SELECT distinct player_id");
		query.append("         FROM");
		query.append("             " + DButility.getDBName(clubId) + ".team_player tp) ");
		query.append(" UNION ALL SELECT ");
		query.append("     p.player_id,");
		query.append("     p.l_name,");
		query.append("     p.f_name,");
		query.append("     t.team_id,");
		query.append("  p.custom_id,");
		query.append("     t.team_name,t.team_code,IFNULL(t.logo_file_path,'') teamlogo_file_path");
		query.append(" FROM");
		query.append("     mcc.player_view p,");
		query.append("     " + DButility.getDBName(clubId) + ".team_player tp,");
		query.append("     " + DButility.getDBName(clubId) + ".team t");
		query.append(" WHERE");
		query.append("     p.club_id = ? AND p.is_active = '1'");
		if (leagueIds != null && !leagueIds.isEmpty() && leagueIds.size() > 0) {
			query.append(" 	   AND p.player_id not in ( select tp1.player_id FROM");
			query.append("     " + DButility.getDBName(clubId) + ".team_player tp1,");
			query.append("     " + DButility.getDBName(clubId) + ".team t1");
			query.append(" WHERE ");
			query.append("         t1.team_id = tp1.team_id");
			query.append("         AND t1.league in("
					+ leagueIds.stream().map(String::valueOf).collect(Collectors.joining(","))
					+ ") )");
		}
		query.append("         AND t.team_id = tp.team_id");
		query.append("         AND p.player_id = tp.player_id ");
		if ( emailOnly == true) {
			query.append(" and p.email is not null and p.email like '%@%.%' ");  
		}
		query.append(" ORDER BY f_name , l_name , team_id desc");

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query.toString());
			int i = 1;
			st.setInt(i++, clubId);
			st.setInt(i++, clubId);
			
			rs = st.executeQuery();
			while (rs.next()) {
				PlayerDto player = new PlayerDto();
				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				player.setTeamId(rs.getInt("team_id"));
				player.setTeamName(rs.getString("team_name"));
				player.setCustomId(rs.getString("custom_id"));
				players.add(player);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}

	/**
	 * @param league 
	 * @param dto
	 * @return
	 */
	protected List<PlayerDto> getPlayersForTeam(int teamId, int internalClubId, int clubId, boolean primaryOnly, String league, Boolean forScoring )
			throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = PLAYER_QUERY + ", IFNULL(tp.is_secondary,'0') is_secondary "
				+ "from mcc.player_view p JOIN " + DButility.getDBName(clubId) + ".team_player tp on (p.player_id=tp.player_id) join " + DButility.getDBName(clubId)
				+ ".team t on (t.team_id = tp.team_id) ";
				
		if(internalClubId > 0 && forScoring) {
			query += "JOIN " + DButility.getDBName(clubId) + ".club_player cp ON (cp.player_id = tp.player_id)";
		}
				
				query += " where p.club_id = ? and p.is_active = 1 " ;

		if (teamId > 0) {
			query += " AND t.team_id = ? ";
			/*if(internalClubId <= 0) {
				query += "or p.can_play_anyteam = 1 ";
			}*/
		}
		boolean goupByCondition = false;
		if (internalClubId > 0 && forScoring) {
			query += " AND (p.can_play_anyteam = 1 or cp.internal_club_id = ?)";
			if(!CommonUtility.isNullOrEmpty(league)){
				query += " AND (p.can_play_anyteam = 1 or t.league >= ?  or (p.can_play_within_club = 1 and cp.internal_club_id = ?))";
				goupByCondition = true;
			}
		}else if (internalClubId > 0 && !forScoring) {
			query += " AND t.club_id = ? ";
		}

		if (primaryOnly) {
			query += " and tp.is_secondary is null";
		}
		if(goupByCondition) {
			query += " group by player_id ";
		}
		query += " order by p.f_name,p.l_name,t.team_id";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId, false);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			int i = 2;
			if (teamId > 0) {
				st.setInt(i++, teamId);
			}

			if (internalClubId > 0) {
				st.setInt(i++, internalClubId);
				if(!CommonUtility.isNullOrEmpty(league)){
					st.setInt(i++, CommonUtility.stringToInt(league));
					st.setInt(i++, internalClubId);
				}
			}
			rs = st.executeQuery();
			preparePlayerList(players, rs, primaryOnly);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}

	/**
	 * @param dto
	 * @return
	 */
	protected List<PlayerDto> getPlayersForMatch(int matchId, int clubId)
			throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = PLAYER_QUERY
				+ "from mcc.player_view p JOIN " + DButility.getDBName(clubId) + ".match_player_team tp on (p.player_id=tp.player_id) join " + DButility.getDBName(clubId)
				+ ".team t on (t.team_id = tp.team_id) "
				+ "where p.club_id = ? and p.is_active <> 2 ";

		query += " AND tp.match_id = ?";

		query += " order by p.f_name,p.l_name,t.team_id";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId, false);
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);
			pst.setInt(2, matchId);
			rs = pst.executeQuery();
			preparePlayerList(players, rs, true);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return players;
	}
	
	protected Map<Integer,Integer> getPlayersBattingPositionMap(String playerIds, int clubId)
			throws Exception {
		String query = "SELECT player_id, batting_position FROM batting WHERE CONCAT(match_id,'-',player_id) IN\r\n"
				+ "(SELECT CONCAT(MAX(match_id),'-',player_id) FROM batting WHERE player_id IN ("+playerIds+") GROUP BY player_id);";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer,Integer> playerBattingPositionValues=null;
		try {
			playerBattingPositionValues=new HashMap<>();
			conn = DButility.getReadConnection(clubId, false);
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while(rs.next()) {
				playerBattingPositionValues.put(rs.getInt("player_id"), rs.getInt("batting_position"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return playerBattingPositionValues;
	}

	private void preparePlayerLiteList(List<PlayerDto> players, ResultSet rs)
			throws SQLException {
		
		//Map<Integer, PlayerDto> map = new HashMap<Integer, PlayerDto>();
		
		while (rs.next()) {
			PlayerDto player = new PlayerDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setGender(rs.getString("gender"));
			player.setEmail(rs.getString("email"));
			player.setContactNo(rs.getString("phone"));
			player.setAddress(rs.getString("address"));
			player.setBattingStyle(rs.getString("batting_style"));
			player.setBowlingStyle(rs.getString("bowling_style"));
			player.setTeamsPlayed(rs.getString("teams_played"));
			player.setProfileDesc(rs.getString("profile_description"));
			player.setTestimonial(rs.getString("testimonal"));
			player.setIsActive(rs.getString("is_Active"));
			player.setAcceptedTerms((rs.getInt("accepted_terms") == 1));
			player.setDateOfBirth(rs.getString("date_of_birth"));
			player.setCreateDate(rs.getDate("create_date"));
			player.setNickName(rs.getString("nickname"));
			player.setPlayingRole(rs.getString("playing_role"));
			player.setSrcPlayerId(rs.getString("src_player_id"));
			player.setCustomId(rs.getString("custom_id"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setCanPlayAnyteam(rs.getBoolean("can_play_anyteam"));
			players.add(player);
			//map.put(player.getPlayerID(), player);
		}
		//players.addAll(map.values());
	}
	
	private void preparePlayerList(List<PlayerDto> players, ResultSet rs, boolean primaryOnly)
			throws SQLException {
		
		//Map<Integer, PlayerDto> map = new HashMap<Integer, PlayerDto>();
		
		while (rs.next()) {
			PlayerDto player = new PlayerDto();
			player.setPlayerID(rs.getInt("player_id"));
			player.setFirstName(rs.getString("f_name"));
			player.setLastName(rs.getString("l_name"));
			player.setGender(rs.getString("gender"));
			player.setEmercencyContactNo(rs.getString("emercency_contact_no"));
			player.setEmercencyContactName(rs.getString("emercency_contact_name"));
			player.setEmercencyContactRel(rs.getString("emercency_contact_rel"));
			player.setEmail(rs.getString("email"));
			player.setContactNo(rs.getString("phone"));
			player.setAddress(rs.getString("address"));
			player.setBattingStyle(rs.getString("batting_style"));
			player.setBowlingStyle(rs.getString("bowling_style"));
			player.setTeamsPlayed(rs.getString("teams_played"));
			player.setProfileDesc(rs.getString("profile_description"));
			player.setTestimonial(rs.getString("testimonal"));
			player.setIsActive(rs.getString("is_Active"));
			player.setAcceptedTerms((rs.getInt("accepted_terms") == 1));
			player.setDateOfBirth(rs.getString("date_of_birth"));
			player.setCreateDate(rs.getDate("create_date"));
			player.setNickName(rs.getString("nickname"));
			player.setPlayingRole(rs.getString("playing_role"));
			player.setTeamId(rs.getInt("team_id"));
			player.setTeamName(rs.getString("team_name"));
			player.setTeamCode(rs.getString("team_code"));
			player.setSrcPlayerId(rs.getString("src_player_id"));
			player.setTeamlogo_file_path(rs.getString("teamlogo_file_path"));
			player.setClubId(rs.getInt("club_id"));
			if (!primaryOnly) {
				player.setSecondary(rs.getInt("is_secondary") > 0);
			}
			player.setCustomId(rs.getString("custom_id"));
			player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
			player.setCanPlayAnyteam(rs.getBoolean("can_play_anyteam"));
			player.setCanPlayWithinClub(rs.getBoolean("can_play_within_club"));
			player.setJerseyNumber(rs.getString("jersey_number"));
			player.setCategory(rs.getString("category"));
			players.add(player);
			//map.put(player.getPlayerID(), player);
		}
		//players.addAll(map.values());
	}

	protected List<PlayerDto> getPlayersByQuery(List<String> qStrings,
			int clubId) throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = "select p.player_id," + " p.l_name," + " p.f_name "
				+ "from player_view p " + "where p.club_id = ? ";
		for (String str : qStrings) {
			query += " AND (p.l_name like '%" + str + "%' OR p.f_name like '%"
					+ str + "%')";
		}

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, clubId);			
			rs = pst.executeQuery();
			while (rs.next()) {
				PlayerDto player = new PlayerDto();
				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				players.add(player);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		Collections.sort(players, new PlayerDtoComparator(true, true, false));
		return players;
	}
	
	protected List<PlayerDto> getPlayerAuditById(int playerId,int clubId) throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = "select pa.player_id,pa.l_name, pa.f_name,pa.batting_style,pa.email,pa.phone,pa.address,"
						+ "pa.teams_played,pa.profile_description,pa.testimonal,pa.season,pa.date_palyed,pa.nickname,"
						+ "pa.bowling_style,pa.date_of_birth,pa.playing_role,pa.custom_id,pa.src_player_id,pa.profilepic_file_path,"
						+ " CASE WHEN pa.change_type = 'insert' THEN 'Created' WHEN pa.change_type = 'delete' THEN 'Deleted' "
						+ " ELSE 'Updated' END change_type,pa.change_timestamp,"
						+ " CONCAT(u.f_name,' ',u.l_name) change_by, "
						+ " pa.change_id from mcc.player_audit pa, mcc.user u where pa.player_id =? and pa.change_by = u.user_id " ;
		

		query += " order by pa.change_timestamp desc";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, playerId);			
			rs = pst.executeQuery();
			while (rs.next()) {

				PlayerDto player = new PlayerDto();
				player.setPlayerID(rs.getInt("player_id"));
				player.setLastName(rs.getString("l_name"));
				player.setFirstName(rs.getString("f_name"));
				player.setBattingStyle(rs.getString("batting_style"));
				player.setEmail(rs.getString("email"));
				player.setContactNo(rs.getString("phone"));
				player.setAddress(rs.getString("address"));
				player.setTeamsPlayed(rs.getString("teams_played"));
				player.setProfileDesc(rs.getString("profile_description"));
				// no variable for change type,chamge by,timestamp so used
				// testimonial,season,nickname
				player.setTestimonial(rs.getString("change_type"));
				player.setSeason(rs.getString("change_timestamp"));
				player.setNickName(rs.getString("change_by"));
				player.setBowlingStyle(rs.getString("bowling_style"));
				player.setDateOfBirth(rs.getString("date_of_birth"));
				player.setPlayingRole(rs.getString("playing_role"));
				player.setSrcPlayerId(rs.getString("src_player_id"));
				player.setCustomId(rs.getString("custom_id"));
				player.setProfilepic_file_path(rs.getString("profilepic_file_path"));
				players.add(player);

			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return players;
	}

	protected void updatePlayer(PlayerDto player, int clubId, String user) throws Exception {
		int playerId = player.getPlayerID();
		String firstName = DButility.escapeQuotes(player.getFirstName());
		String lastName = DButility.escapeQuotes(player.getLastName());
		String gender = DButility.escapeLine(player.getGender());
		String nickName = DButility.escapeLine(player.getNickName());
		String customId = DButility.escapeLine(player.getCustomId());
		String email = player.getEmail();
		String battingStyle = player.getBattingStyle();
		String bowlingStyle = player.getBowlingStyle();
		//String address =player.getAddress();
		String contactNo = player.getContactNo();
		String dateOfBirth = player.getDateOfBirth();
		String teamsPlayed = player.getTeamsPlayed();
		String testimonial = player.getTestimonial();
		String profileDesc = player.getProfileDesc();
		boolean canPlayAnyTeam = player.isCanPlayAnyteam();
		// String season = player.getSeason();
		// String datePlayed = player.getDatePlayed();
		// String isActive = player.getIsActive();
		String playingRole = player.getPlayingRole();
		String category = player.getCategory();

		String query;
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			query = "update player set l_name = ?,f_name= ?, gender=?,"
					+" emercency_contact_no=?, emercency_contact_name=?, emercency_contact_rel=?, "
					+ " batting_style= ?," 
					+ " phone=?," 
					+ " email=?,"
					+ " jersey_number=?,"
					+ " teams_played=?,testimonal=?,"
					+ " profile_description=?,nickname=?,"
					+ " bowling_style =?,playing_role = ?,date_of_birth = ?,"
					+ " can_play_anyteam=?, can_play_within_club = ?, category=? where player_id = ?";
				
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			int index =1;
			pst.setString(index++, lastName);
			pst.setString(index++, firstName);
			pst.setString(index++, gender);
			pst.setString(index++, player.getEmercencyContactNo());
			pst.setString(index++, player.getEmercencyContactName());
			pst.setString(index++, player.getEmercencyContactRel());
			pst.setString(index++, battingStyle);
			pst.setString(index++, contactNo);
			//pst.setString(index++, address);
			pst.setString(index++, email);
			pst.setString(index++, player.getJerseyNumber());
			pst.setString(index++, teamsPlayed);
			pst.setString(index++, DButility.escapeLine(testimonial));
			pst.setString(index++, DButility.escapeLine(profileDesc));
			pst.setString(index++, nickName);
			pst.setString(index++, bowlingStyle);
			pst.setString(index++, playingRole);
			pst.setString(index++, dateOfBirth);
//			pst.setString(index++, customId);
			pst.setBoolean(index++, canPlayAnyTeam);
			pst.setBoolean(index++, player.isCanPlayWithinClub());
			pst.setString(index++, category);
			pst.setInt(index++, playerId);
			pst.executeUpdate();
			
			if(clubId==14202) {
				pst.executeUpdate("update player_club set accepted_terms = " + (player.isAcceptedTerms() ? "1" : "0") 
						+ ", is_active = " + player.getIsActive() + ", custom_id= '" + customId
						+ "' where player_id = " + player.getPlayerID() + " and club_id =" + clubId);
			}else {
				pst.executeUpdate("update player_club set accepted_terms = " + (player.isAcceptedTerms() ? "1" : "0") 
						+" ,custom_id= '" + customId
						+ "' where player_id = " + player.getPlayerID() + " and club_id =" + clubId);
			}
			
			
			//Audit Log Methods Called and parameters sent 
			String playerid = Integer.toString(playerId);
			String clubid = Integer.toString(clubId);
			
			auditPlayer(playerid,clubid,user,"update",conn);
			auditPlayerClub(playerid,clubid,user,"update",conn);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected void updatePlayerBasicDetails(PlayerDto player, String changedBy) throws Exception {

		String query;
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			query = "update player set l_name = ?,f_name= ? ";
			
			if(!CommonUtility.isNullOrEmpty(player.getGender())) {
				query +=", gender=? ";
			}
			if(!CommonUtility.isNullOrEmpty(player.getDateOfBirth())) {
				query +=", date_of_birth = ? ";
			}
			if(!CommonUtility.isNullOrEmpty(player.getBattingStyle())) {
				query +=", batting_style= ? ";
			}
			if(!CommonUtility.isNullOrEmpty(player.getBowlingStyle())) {
				query +=", bowling_style =? ";
			}
			if(!CommonUtility.isNullOrEmpty(player.getPlayingRole())) {
				query +=", playing_role = ? ";
			}
			query += " where player_id = ?";
			
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			
			int index =1;
			
			pst.setString(index++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(player.getLastName()))));
			pst.setString(index++, CommonUtility.trimString(DButility.escapeQuotes(DButility.escapeLine(player.getFirstName()))));
			if(!CommonUtility.isNullOrEmpty(player.getGender())) {
				pst.setString(index++, player.getGender());
			}
			if(!CommonUtility.isNullOrEmpty(player.getDateOfBirth())) {
				pst.setString(index++, player.getDateOfBirth());
			}
			if(!CommonUtility.isNullOrEmpty(player.getBattingStyle())) {
				pst.setString(index++, player.getBattingStyle());
			}
			if(!CommonUtility.isNullOrEmpty(player.getBowlingStyle())) {
				pst.setString(index++, player.getBowlingStyle());
			}
			if(!CommonUtility.isNullOrEmpty(player.getPlayingRole())) {
				pst.setString(index++, player.getPlayingRole());
			}
			pst.setInt(index++, player.getPlayerID());
			
			pst.executeUpdate();
			
			auditPlayerV1(player.getPlayerID(),changedBy,"update",conn);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected boolean checkPlayerByEmail(String email, int clubId)
			throws Exception {
		Connection conn = null;
		PreparedStatement pst = null;
		String query = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			conn = DButility.getDefaultReadConnection();
			query = "select 1 from player_view where email=? and club_id = ?";
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, email);
			pst.setInt(index++, clubId);
			rs = pst.executeQuery();					
			while (rs.next()) {
				exists = true;
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return exists;
	}
	
	
	protected int getPlayerIdByEmail(String email, int clubId)
			throws Exception {
		Connection conn = null;
		PreparedStatement pst = null;
		String query = null;
		ResultSet rs = null;
		int playerId = 0;
		try {
			conn = DButility.getDefaultReadConnection();
			query = "select player_id from player_view where email=? and club_id = ? limit 1";
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, email);
			pst.setInt(index++, clubId);
			rs = pst.executeQuery();
			while (rs.next()) {
				playerId = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return playerId;
	}
	
	
	protected boolean checkPlayerByResidentId(String residentId, String playerId, int clubId, String association)
			throws Exception {
		Connection conn = null;
		PreparedStatement pst = null;
		String query = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			conn = DButility.getDefaultConnection();
			query = "select count(*) as count from player_additional where resident_id=? ";
					
			if(CommonUtility.stringToInt(playerId) > 0){
				query += " and player_id != ?";
			}			
			if(!CommonUtility.isNullOrEmpty(association)){
				query += " and club_id in (select club_id from mcc.club where association = ?)";
			}			
			if(clubId > 0){
				query += " and club_id = ?";
			}	
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, residentId);
			if(CommonUtility.stringToInt(playerId) > 0){
				pst.setString(index++, playerId);
			}
			if(!CommonUtility.isNullOrEmpty(association)){
				pst.setString(index++, association);
			}
			if(clubId > 0){
				pst.setInt(index++, clubId);
			}			
			rs = pst.executeQuery();
			while (rs.next()) {
				if(rs.getInt("count") > 0){
					exists = true;
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return exists;
	}
	
	protected int getPlayerIdByResidentId(String residentId, int clubId) throws Exception {
		Connection conn = null;
		PreparedStatement pst = null;
		String query = null;
		ResultSet rs = null;
		int playerId = 0;
		try {
			conn = DButility.getDefaultReadConnection();
			query = "select player_id as player_id from player_additional where resident_id = ?";
			
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setString(index++, residentId);
			
			rs = pst.executeQuery();
			while (rs.next()) {
				
					playerId = rs.getInt("player_id");
				
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return playerId;
	}

	protected void updatePlayerStatus(int status, int playerId, int clubId, String user)
			throws Exception {

		String query;
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			query = "update player_club set is_Active= ? where club_id =? ";
			if(playerId > 0) {
				query = query +" and player_id = ? ";
			}else if(playerId <=0) {
				if(status == 1) {
					query = query +" and is_active = 3 ";
				}else if( status == 3) {
					query = query +" and is_active = 1 ";
				}				
			}
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setInt(index++, status);
			pst.setInt(index++, clubId);
			if(playerId > 0) {
				pst.setInt(index++, playerId);
			}			
			pst.executeUpdate();
			
			String playerid = Integer.toString(playerId);
			String clubid = Integer.toString(clubId);
			
			if(playerId > 0) {
				//Audit Log Methods Called and parameters sent				
				auditPlayerClub(playerid,clubid,user,"update",conn);
			}else {
				auditPlayerClubAll(clubid,user,"update",conn);				
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	protected String deletePlayer(int playerId, int clubId, String user) throws Exception {

		Connection conn = null;		
		PreparedStatement st = null;
		ResultSet rs = null;
		String message = "";	
		
		try {
			
			UserDto playerUser = UserFactory.getUserByPlayerId(playerId);
			
			String query = "call deletePlayer(?,?,?,@pmc,@message);";
			conn = DButility.getDefaultConnection();			
			
			st = conn.prepareStatement(query);
			
			st.setInt(1, playerId);
			st.setInt(2, clubId);
			st.setString(3, user);
			
			rs = st.executeQuery();			
					
			while(rs.next()) {
				message = rs.getString(1);
			}				
			try {
				UserDto playerUserDB = UserFactory.getUserByPlayerId(playerId);
				if(playerUserDB == null) {
					GraphDBBackGroundProcess.deleteGraphUser(playerUser.getUserID());
				}
			}catch(Exception e) {
				log.error(" Graph User Delete Error "+e.getMessage());
			}
			
		} catch (Exception e) {			
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return message;
	}
	protected int globalPlayerCheckByEmail(String email) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int playerId = 0;
		String query = "select player_id from player_view where email= ?";
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setString(1, email);
			rs = st.executeQuery();
			while (rs.next()) {
				playerId = rs.getInt("player_id");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerId;
	}
	
	protected int playerCheckByEmailCountInClub(String email,int clubId) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int playerId = 0;
		String query = "select count(*) from player_view where email= ? and club_id =?";
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setString(1, email);
			st.setInt(2, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				playerId = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerId;
	}

	protected void registerExistingPlayer(PlayerDto dto, int clubId, String user)
			throws Exception {
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			conn = DButility.getDefaultConnection();
			
			String clubQuery = "insert into player_club(player_id,club_id,create_date,is_active,accepted_terms,custom_id) "
					+ "values (?,?, NOW(),?,?,?)";
			pst = conn.prepareStatement(clubQuery);		
				
			int index =1;
			pst.setInt(index++, dto.getPlayerID());
			pst.setInt(index++, clubId);
			pst.setString(index++, dto.getIsActive());
			pst.setString(index++, (dto.isAcceptedTerms() ? "1" : "0"));
			pst.setString(index++, dto.getCustomId());
			pst.executeUpdate();
			
			//Audit Log Methods Called and parameters sent 
			String playerid = Integer.toString(dto.getPlayerID());
			String clubid = Integer.toString(clubId);
			
			auditPlayerClub(playerid,clubid,user,"insert",conn);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected List<Integer> getClubIdsForPlayers(int playerId) throws Exception {
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Integer> clubs = new ArrayList<Integer>();
		String query = "select club_id from player_club where player_id = ? order by club_id";
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			rs = st.executeQuery();
			while (rs.next()) {
				clubs.add(rs.getInt("club_id"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return clubs;
	}
	
	protected List<Integer> getAssociatedClubIdsForPlayer(int clubId, int playerId) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Integer> clubs = new ArrayList<Integer>();
		String query = "select club_id from mcc.player_view where player_id = ? "
				+ "and club_id in (select c2.club_id from mcc.club c1, mcc.club c2 " + 
				"where c1.association =  c2.association and c1.club_id = ?) ";				
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			st.setInt(2, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				clubs.add(rs.getInt("club_id"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return clubs;
	}

	protected List<String> getLockedSeriesName(int playerId, int clubId) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<String> seriesNames = new ArrayList<String>();

		String query = "SELECT league_name FROM league l, team t, team_player tp, mcc.player p " +
				"WHERE l.league_id = t.league AND tp.team_id = t.team_id AND tp.player_id = p.player_id AND l.player_profile_locked = 1 AND p. player_id = ?";
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			rs = st.executeQuery();
			while (rs.next()) {
				seriesNames.add(rs.getString("league_name"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return seriesNames;
	}

	protected boolean isPlayerPartOfCurrentLeague(String playerId, int clubId) throws Exception {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean isPartofTeam = false;
		try {
			conn = DButility.getDefaultConnection();
			String query = "select 1 from player p, player_club pc, club c, " + DButility.getDBName(clubId) + ".team t, " + DButility.getDBName(clubId) + ".team_player tp" +
					" where p.player_id = pc.player_id and pc.club_id = c.club_id and c.current_league = t.league" +
					" and t.team_id = tp.team_id and tp.player_id=p.player_id and p.player_id = ? and c.club_id = ?";
			pst = conn.prepareStatement(query);
			int index =1;
			pst.setString(index++, playerId);
			pst.setInt(index++, clubId);
			rs = pst.executeQuery();
			while (rs.next()) {
				isPartofTeam = true;
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return isPartofTeam;
	}
	protected boolean isPlayerPartOfLeague(int playerId, int leagueId,int clubId) throws Exception {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean isPartofLeague = false;
		try {
			conn = DButility.getConnection(clubId);
			String query = "select 1 from team_player tp, club7683.team t " +
					" where tp.team_id = t.team_id and tp.player_id = ? and t.league = ?";
			pst = conn.prepareStatement(query);
			int index =1;
			pst.setInt(index++, playerId);
			pst.setInt(index++, leagueId);		
			rs = pst.executeQuery();
			while (rs.next()) {
				isPartofLeague = true;
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return isPartofLeague;
	}

	protected Map<String, Integer> getPlayerCorrections(long playerId, int clubId) throws Exception {

		HashMap<String, Integer> playerCorrections = new HashMap<String, Integer>();
		String query = "select matches,bat_innings,bat_no,bat_balls,bat_runs,bat_hs,bat_100,bat_50,bat_25,bat_4,bat_6,bat_0," +
				"ball_innings,ball_balls,ball_runs,ball_wickets,ball_4w,ball_5w from player_corrections where player_id = ? and club_id = ?";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setLong(1, playerId);
			st.setInt(2, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				playerCorrections.put("matches", rs.getInt("matches"));
				playerCorrections.put("bat_innings", rs.getInt("bat_innings"));
				playerCorrections.put("bat_no", rs.getInt("bat_no"));
				playerCorrections.put("bat_balls", rs.getInt("bat_balls"));
				playerCorrections.put("bat_runs", rs.getInt("bat_runs"));
				playerCorrections.put("bat_hs", rs.getInt("bat_hs"));
				playerCorrections.put("bat_100", rs.getInt("bat_100"));
				playerCorrections.put("bat_50", rs.getInt("bat_50"));
				playerCorrections.put("bat_25", rs.getInt("bat_25"));
				playerCorrections.put("bat_4", rs.getInt("bat_4"));
				playerCorrections.put("bat_6", rs.getInt("bat_6"));
				playerCorrections.put("bat_0", rs.getInt("bat_0"));
				playerCorrections.put("ball_innings", rs.getInt("ball_innings"));
				playerCorrections.put("ball_balls", rs.getInt("ball_balls"));
				playerCorrections.put("ball_runs", rs.getInt("ball_runs"));
				playerCorrections.put("ball_wickets", rs.getInt("ball_wickets"));
				playerCorrections.put("ball_4w", rs.getInt("ball_4w"));
				playerCorrections.put("ball_5w", rs.getInt("ball_5w"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerCorrections;
	}
	
	protected ArrayList<Integer[]> getDuplicatePlayersMap(int clubId, String mergeType) throws Exception {

		ArrayList<Integer[]> players = new ArrayList<>();

		String query = "select min(pv.player_id) minPlayerId, max(player_id) maxPlayerId from mcc.player_view pv "
				+ "where pv.club_id = " + clubId;
		if (!CommonUtility.isNullOrEmptyOrNULL(mergeType)) {
			if (mergeType.equalsIgnoreCase("SRC_PLAYER_ID")) {
				query += " and src_player_id > 0 group by src_player_id having count(*) > 1";
			}
			if (mergeType.equalsIgnoreCase("PLAYER_NAME")) {
				query += " group by concat(upper(trim(pv.f_name)),' ',upper(trim(pv.l_name))) having count(*) > 1";
			}
			if (mergeType.equalsIgnoreCase("PLAYER_NAME_EMAIL")) {
				query += " and pv.email is not null and pv.email != '' "
				+ "group by concat(upper(trim(pv.f_name)),' ',upper(trim(pv.l_name)),' ',upper(trim(pv.email))) having count(*) > 1";
				}
			}

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				players.add(new Integer[] { rs.getInt("minPlayerId"), rs.getInt("maxPlayerId") });
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	protected int verifyPartOfSameMatch(int master, int secondary, int clubId) throws Exception {
		
		String query = "SELECT COUNT(*) no_of_matches FROM match_player_team WHERE player_id = ? "
				+ "AND match_id IN (SELECT match_id FROM match_player_team WHERE player_id = ?)";
				
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection conn = null;
		int noOfMatches = 1;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, master);
			st.setInt(2, secondary);
			rs = st.executeQuery();
			while (rs.next()) {
				noOfMatches = rs.getInt("no_of_matches");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return noOfMatches;
	}
	
	protected boolean mergePlayers(int master, int secondary, int clubId, String user) throws Exception {
		Connection conn = null;
		Statement st = null;
		int auditId = 0;
		try {
			try {
				auditId = auditMergePlayer(master, secondary, clubId, user);
			}catch(Exception e){
				log.debug("Issue with auditing merge for Secondary player " + secondary);			
			}			
			conn = DButility.getConnection(clubId);
					
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch("insert into team_player_audit(team_id,player_id,is_secondary,change_timestamp,change_by,change_type) "
					+ " select team_id,player_id,is_secondary,now(),'"+user +"','delete' from team_player where player_id = "+secondary);
					
			st.addBatch("update team_player set player_id = " + master + " where player_id = " + secondary
					+ " and team_id not in (select team_id from (select tp.team_id from team_player tp where tp.player_id = "
					+ master + ") a)");
			st.addBatch("update club_player set player_id = " + master + " where player_id = " + secondary);
			st.addBatch("update batting set player_id = " + master + " where player_id = " + secondary);
			st.addBatch("update batting set wicket_taker1 = " + master + " where wicket_taker1 = " + secondary);
			st.addBatch("update batting set wicket_taker2 = " + master + " where wicket_taker2 = " + secondary);
			st.addBatch("update team set captain = " + master + " where captain = " + secondary);
			st.addBatch("update team set vice_captain = " + master + " where vice_captain = " + secondary);
			st.addBatch("update matches set man_of_the_match = " + master + " where man_of_the_match = " + secondary);
			st.addBatch("update matches set team_one_captain = " + master + " where team_one_captain = " + secondary);
			st.addBatch("update matches set team_two_captain = " + master + " where team_two_captain = " + secondary);
			st.addBatch("update matches set team_one_vice_captain = " + master + " where team_one_vice_captain = " + secondary);
			st.addBatch("update matches set team_two_vice_captain = " + master + " where team_two_vice_captain = " + secondary);
			st.addBatch("update ball set batsman = " + master + " where batsman = " + secondary);
			st.addBatch("update ball set bowler = " + master + " where bowler = " + secondary);
			st.addBatch("update ball set runner = " + master + " where runner = " + secondary);
			st.addBatch("update ball set out_person = " + master + " where out_person = " + secondary);
			st.addBatch("update ball set wicket_taker_1 = " + master + " where wicket_taker_1 = " + secondary);
			st.addBatch("update ball set wicket_taker_2 = " + master + " where wicket_taker_2 = " + secondary);
			st.addBatch("update articles set user_id = (select user_id from mcc.user where player_id = " + master + ") where user_id = (select user_id from mcc.user where player_id = " + secondary
					+ ")");
			st.addBatch("update comments set user_id = (select user_id from mcc.user where player_id = " + master + ") where user_id = (select user_id from mcc.user where player_id = " + secondary
					+ ")");
			st.addBatch("update matches set last_updated_by = (select user_id from mcc.user where player_id = " + master + ") where last_updated_by = (select user_id from mcc.user where player_id = "
					+ secondary + ")");
			st.addBatch("update match_player_team set player_id = " + master + " where player_id = " + secondary
					+ " and match_id not in (select match_id from (select tp.match_id from match_player_team tp where tp.player_id = " + master + ") a)");
			st.addBatch("update bowling set player_id = " + master + " where player_id = " + secondary);
			st.addBatch("update matches set scorer = (select user_id from mcc.user where player_id = " + master + ") where scorer = (select user_id from mcc.user where player_id = " + secondary + ")");
			st.addBatch("update scorer_log set scorer_id = (select user_id from mcc.user where player_id = " + master + ") where scorer_id = (select user_id from mcc.user where player_id = "
					+ secondary + ")");
			st.addBatch("update player_statistics_summary set player_id = " + master + " where player_id = " + secondary);
			st.addBatch("update player_statistics_summary set wicket_taker1 = " + master + " where wicket_taker1 = " + secondary);
			st.addBatch("update player_statistics_summary set wicket_taker2 = " + master + " where wicket_taker2 = " + secondary);
					
			st.addBatch("delete from match_player_team where player_id = " + secondary);
			//st.addBatch("call mcc.deletePlayer("+secondary+","+clubId+",'"+user+"',"+"@pmc,@message)");
			st.addBatch("update mcc.audit_player_merge set merge_status = 1 where id =" + auditId);
			
				
			st.executeBatch();			
			conn.commit();
			
			deletePlayer(secondary, clubId, user);
			
		} catch (BatchUpdateException be) {
			conn.rollback();
			throw new Exception(be);
		} catch (Exception e) {
			conn.rollback();	
			throw new Exception(e);
		} finally {
			DButility.closeConnectionAndStatement(conn, st);

		}
		return true;
	}

	private static int auditMergePlayer(int primaryPlayerId, int secondaryPlayerId, int secondaryClubId, String mergeBy) throws Exception {

		String query = "";
		int auditMergeId = 0;
		query = "insert into mcc.audit_player_merge(primary_player_id,secondary_player_id,secondary_club_id,"
				+ "merge_date_time,merge_by) values (?,?,?,NOW(),?)";

		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		int index = 1;
		try {
			st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			st.setInt(index++, primaryPlayerId);
			st.setInt(index++, secondaryPlayerId);
			st.setInt(index++, secondaryClubId);
			st.setInt(index++, CommonUtility.stringToInt(mergeBy));

			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				auditMergeId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}		
		Connection conn1 = null;
		Statement st1 = null;
		
		if (auditMergeId > 0) {

			try {
				conn1 = DButility.getDefaultConnection();
				conn1.setAutoCommit(false);
				st1 = conn1.createStatement();

				st1.addBatch("update mcc.audit_player_merge set primary_user_id = (select max(user_id) "
						+ " from mcc.user where player_id = " + primaryPlayerId + " ) where id = "+auditMergeId);
				
				st1.addBatch("update mcc.audit_player_merge set secondary_user_id = ( select max(user_id) "
						+ " from mcc.user where player_id = " + secondaryPlayerId + " ) where id = "+auditMergeId);
				
				
				st1.addBatch("update mcc.audit_player_merge set primary_umpire_id = ( select max(umpire_id) "
						+ " from mcc.user where player_id = " + primaryPlayerId + " ) where id = "+auditMergeId);
				
				st1.addBatch("update mcc.audit_player_merge set secondary_umpire_id = (select max(umpire_id) "
						+ " from mcc.user where player_id = " + secondaryPlayerId + " ) where id = "+auditMergeId);
				
				st1.addBatch("update mcc.audit_player_merge set user_club_ids = (select group_concat(club_id) "
						+ " from mcc.user_club where user_id = (select max(user_id) from mcc.user "
						+ " where player_id = " + secondaryPlayerId + " )) where id = "+auditMergeId);
				
				st1.addBatch("update mcc.audit_player_merge set player_club_ids = ( select group_concat(club_id) "
						+ " from mcc.player_club where player_id = " + secondaryPlayerId + " ) where id = "+auditMergeId);
				
				st1.addBatch("update mcc.audit_player_merge set user_roles = (select group_concat(role) from mcc.user_roles "
						+ " where club_id = "+secondaryClubId +" and user_id = (select max(user_id) from mcc.user_view where "
						+ " player_id = " + secondaryPlayerId + " and club_id = "+secondaryClubId +")) where id = "+auditMergeId);
				
				st1.addBatch("update mcc.audit_player_merge set team_ids = ( select group_concat(team_id) from club"+secondaryClubId+".team_player "
						+ " where player_id = " + secondaryPlayerId + " and team_id not in ( select tp.team_id "
						+ "from club"+secondaryClubId+".team_player tp "
						+ " where tp.player_id = " + primaryPlayerId + " ) ) where id = "+auditMergeId);	
				
				st1.addBatch("update mcc.audit_player_merge set internal_club_ids = ( select group_concat(internal_club_id) "
						+ " from club"+secondaryClubId+".club_player where player_id = " + secondaryPlayerId + " and internal_club_id "
						+ " not in ( select cp.internal_club_id from club"+secondaryClubId+".club_player cp where "
						+ " cp.player_id = " + primaryPlayerId + " )) where id = "+auditMergeId);
				
				st1.addBatch("update mcc.audit_player_merge set match_ids = ( select group_concat(match_id) "
						+ "from club"+secondaryClubId+".match_player_team where player_id = " + secondaryPlayerId 
						+ " and match_id not in ( select mpt.match_id from club"+secondaryClubId+".match_player_team mpt "
								+ " where mpt.player_id = " + primaryPlayerId + " )) where id = "+auditMergeId);
				
				st1.addBatch("update mcc.audit_player_merge set comment_ids = ( select group_concat(comment_id) "
						+ "from club"+secondaryClubId+".comments where user_id = (select max(user_id) "
						+ " from mcc.user where player_id = " + secondaryPlayerId +" ) ) where id = "+auditMergeId );	

				st1.executeBatch();
				conn1.commit();

			} catch (BatchUpdateException be) {
				conn1.rollback();
				throw new Exception(be);
			} catch (Exception e) {
				conn1.rollback();
				throw new Exception(e);
			} finally {
				DButility.closeConnectionAndStatement(conn1, st1);
			}
		}
		return auditMergeId;
	}

	protected boolean checkPlayerByCustomId(String customId,String playerId, int clubId) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "select 1 from player_view where custom_id = ? and club_id = ?";
		
		if(CommonUtility.stringToInt(playerId) > 0){
			query += " and player_id != ? ";
		}
		boolean exists = false;
		try {
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			st.setString(1, customId);
			st.setInt(2, clubId);
			if(CommonUtility.stringToInt(playerId) > 0){
				st.setString(3, playerId);
			}
			rs = st.executeQuery();
			while (rs.next()) {
				exists = true;
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return exists;
	}

	protected int globalPlayerCheckByCustomId(String customId) throws Exception {
		String query = "select player_id from player_view where custom_id = ? ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int playerId = 0;
		try {
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			st.setString(1, customId);
			rs = st.executeQuery();
			while (rs.next()) {
				playerId = rs.getInt("player_id");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerId;
	}

	protected List<String> getPlayerEmailByTeams(List<String> teams, int clubId) throws Exception {
		String query = "select distinct p.email from mcc.player p, team_player tp "
				+ "where tp.player_id = p.player_id and tp.team_id in ( ";
		for (int i=0;i<teams.size();i++) {
			query += (i!=0?",?":"?");
		}
		query += ")";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<String> emails = new ArrayList<String>();
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			int i=1;
			for (String team : teams) {
				st.setString(i++, team);
			}
			rs = st.executeQuery();
			while (rs.next()) {
				emails.add(rs.getString("email"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return emails;
	}

	protected List<String> getCaptainsEmailByTeams(List<String> teams, int clubId) throws Exception {
		String query = "select distinct p.email from mcc.player p, team t where p.player_id = t.captain and t.team_id in ( ";
		for (int i=0;i<teams.size();i++) {
			query += (i!=0?",?":"?");
		}
		query += ") ";
		query += " union select distinct p.email from mcc.player p, team t where p.player_id = t.vice_captain and t.team_id in ( ";
		for (int i=0;i<teams.size();i++) {
			query += (i!=0?",?":"?");
		}
		query += ") ";
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<String> emails = new ArrayList<String>();
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			int i=1;
			for (String team : teams) {
				st.setString(i++, team);
			}
			for (String team : teams) {
				st.setString(i++, team);
			}
			rs = st.executeQuery();
			while (rs.next()) {
				emails.add(rs.getString("email"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return emails;
	}
	
	protected static void auditPlayer(String player_id, String club_id, String user, String operation, Connection conn) throws Exception {
		
		PreparedStatement st = null;
		PreparedStatement st1 = null;
		PreparedStatement st2 = null;
		//Connection conn = null;
		ResultSet rs = null;
		String new_id = null; 
		try {
			//conn = DButility.getDefaultConnection();
			
			String insertQuery = "insert into mcc.player_audit (player_id,l_name, f_name,batting_style,"
					+ "email,phone,address,teams_played,profile_description,testimonal,"
					+ "season,date_palyed,nickname,bowling_style,date_of_birth,playing_role,"
					+ "src_player_id,profilepic_file_path)"
					+"(select player_id,l_name, f_name,batting_style,email,phone,address,"
					+ "teams_played,profile_description,testimonal,season,date_palyed,nickname,"
					+ "bowling_style,date_of_birth,playing_role,src_player_id,profilepic_file_path "
					+ "from mcc.player where player_id =? )";
			st = conn.prepareStatement(insertQuery);
			st.setString(1, player_id);
			st.executeUpdate();
			String GetID = "select LAST_INSERT_ID() as latest_id";
			st1 = conn.prepareStatement(GetID);
			rs = st1.executeQuery();
			while (rs.next()) {
				new_id = rs.getString("latest_id");
			}
			String updateQuery = "update mcc.player_audit set change_timestamp = now(), change_by=?,"
					+ " change_type=? " 
					+ " where change_id = ? ";
			st2 = conn.prepareStatement(updateQuery);
			st2.setString(1, user);
			st2.setString(2, operation);
			st2.setString(3, new_id);
			st2.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if(rs!=null){
				rs.close();
			}
			DButility.closeStatement(st);
			DButility.closeStatement(st1);
			DButility.closeStatement(st2);
			//DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected static void auditPlayerV1(int playerId, String changedBy, String changeType, Connection conn) throws Exception {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			String insertQuery = "insert into mcc.player_audit (player_id,l_name, f_name,batting_style,"
					+ "email,phone,address,teams_played,profile_description,testimonal,"
					+ "season,date_palyed,nickname,bowling_style,date_of_birth,playing_role,"
					+ "src_player_id,profilepic_file_path, change_timestamp, change_type, change_by)"
					+"(select player_id,l_name, f_name,batting_style,email,phone,address,"
					+ "teams_played,profile_description,testimonal,season,date_palyed,nickname,"
					+ "bowling_style,date_of_birth,playing_role,src_player_id,profilepic_file_path,now(),?,? "
					+ "from mcc.player where player_id =? )";
			
			st = conn.prepareStatement(insertQuery);
			st.setString(1, changeType);
			st.setString(2, changedBy);
			st.setInt(3, playerId);
			st.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if(rs!=null){
				rs.close();
			}
			DButility.closeStatement(st);
		}
	}
		
	protected static void auditPlayerClub(String player_id, String club_id, String user, String operation, Connection conn)
			throws Exception {

		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		// Connection conn = null;
		ResultSet rs = null;
		String new_id = null;
		try {
			// conn = DButility.getDefaultConnection();

			String insertQuery = null;
			if (club_id.equals("NA"))
				insertQuery = "insert into mcc.player_club_audit"
						+ "(player_id,club_id,create_date,is_active,accepted_terms,custom_id)"
						+ " (select player_id,club_id,create_date,is_active,accepted_terms,custom_id "
						+ "from mcc.player_club where player_id = ?)";

			else
				insertQuery = "insert into mcc.player_club_audit"
						+ "(player_id,club_id,create_date,is_active,accepted_terms,custom_id)"
						+ " (select player_id,club_id,create_date,is_active,"
						+ "accepted_terms,custom_id from mcc.player_club where player_id = ? and club_id =?)";
			pst = conn.prepareStatement(insertQuery);
			pst.setString(1, player_id);
			if (!club_id.equals("NA")) {
				pst.setString(2, club_id);
			}
			pst.executeUpdate();
			String GetID = "select LAST_INSERT_ID() as latest_id";
			rs = pst.executeQuery(GetID);
			while (rs.next()) {
				new_id = rs.getString("latest_id");
			}
			String updateQuery = "update mcc.player_club_audit set " + "change_timestamp = now(), "
					+ "change_by=?,change_type=?" + " where change_id = ?";
			pst1 = conn.prepareStatement(updateQuery);
			pst1.setString(1, user);
			pst1.setString(2, operation);
//			pst1.setInt(3, customId);
			pst1.setString(3, new_id);
			pst1.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}
			DButility.closeStatement(pst);
			DButility.closeStatement(pst1);
			// DButility.closeConnectionAndStatement(conn, st);
		}
	}		
	
	protected static void auditPlayerClubAll(String club_id, String user, String operation, Connection conn)
			throws Exception {

		PreparedStatement pst = null;		
		try {		
			String insertQuery = null;			
				insertQuery = "insert into mcc.player_club_audit"
						+ "(player_id,club_id,create_date,is_active,accepted_terms,change_timestamp,change_type,change_by, custom_id)"
						+ " (select player_id,club_id,create_date,is_active,accepted_terms, "
						+ "  now(),?,?,custom_id from mcc.player_club where club_id = ?)";
			
			pst = conn.prepareStatement(insertQuery);
			pst.setString(1, operation);
			pst.setString(2, user);		
			pst.setString(3, club_id);
			
			pst.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {			
			DButility.closeStatement(pst);
		}
	}	
	
	protected void updateProfilePicFilePath(String profilepic_file_path, int profileId) throws Exception {

		String query = "update mcc.player set profilepic_file_path = ? where player_id = ?";
		UserDto userDto = UserFactory.getUserByPlayerId(profileId);
		String user = "";
		if( userDto != null) {
			user = Integer.toString(userDto.getUserID());
		}		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, profilepic_file_path.trim());
			st.setInt(2, profileId);
			st.executeUpdate();
			auditPlayer(Integer.toString(profileId),"",user,"update",conn);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected void updatePlayerEmail(String email, int playerId, String updatedBy) throws Exception {

		String query = "update player set email = ? where player_id = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setString(1, email.trim());
			st.setInt(2, playerId);
			st.executeUpdate();
			auditPlayer(playerId+"","",updatedBy,"update",conn);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
			
	protected String getProfilePicFilePath(int playerId) throws Exception {

		String query = "select profilepic_file_path from mcc.player where player_id = ?";

		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String profilepic_file_path = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, playerId);
			rs = st.executeQuery();
			while (rs.next()) {
				profilepic_file_path = rs.getString("profilepic_file_path").trim();
			}

		} catch (SQLException e) {
			// return null;
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return profilepic_file_path;
	}
			
	protected void addStarPlayerToInternalClub(int playerId, int teamId, int leagueId, int internalclubId, int clubId)
			throws Exception {
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			conn = DButility.getConnection(clubId);
			String clubQuery = "insert into club_star_player " + " (player_id,internal_club_id, team_id, league_id) "
					+ " values (?,?,?,?)";
			pst = conn.prepareStatement(clubQuery);
			pst.setInt(1, playerId);
			pst.setInt(2, internalclubId);
			pst.setInt(3, teamId);
			pst.setInt(4, leagueId);

			pst.executeUpdate();

			// Audit Log Methods Called and parameters sent
			// String playerid = Integer.toString(playerId);
			// String internalclubId = Integer.toString(internalclubId);

			// auditPlayerClub(playerid,clubid,user,"update",conn);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
			
			protected void deleteStarPlayer(int playerId, int internalclubId, int clubId) throws Exception {

				PreparedStatement pst = null;
				Connection conn = null;
				try {
					conn = DButility.getConnection(clubId);
					String query = "delete from club_star_player where player_id = ? and internal_club_id = ?";
					pst = conn.prepareStatement(query);
					pst.executeUpdate();
					
					
				} catch (Exception e) {
					throw new Exception(e);
				} finally {
					DButility.closeConnectionAndStatement(conn, pst);
				}
			}
			
			protected List<Integer> getStarPlayersOfInternalClub(int internalclubId, int clubId) throws Exception {
				String query = "select player_id from club_star_player where  internal_club_id = ? ";
				
				Connection conn = null;
				PreparedStatement st = null;
				ResultSet rs = null;
				List<Integer> starPlayers = new ArrayList<Integer>();
				try {
					conn = DButility.getReadConnection(clubId);
					st = conn.prepareStatement(query);
					st.setInt(1, internalclubId);				
					
					rs = st.executeQuery();
					while (rs.next()) {
						starPlayers.add(rs.getInt("player_id"));
					}
				} catch (Exception e) {
					throw new Exception(e);
				} finally {
					DButility.dbCloseAll(conn, st, rs);
				}
				return starPlayers;
			}
			
	protected List<Integer> getStarPlayersByLeagueId(List<Integer> leagueId, int clubId) throws Exception {
		String query = "select player_id from club_star_player ";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < leagueId.size(); i++) {
			sb.append(leagueId.get(i));
			if (i != leagueId.size() - 1)
				sb.append(",");
		}

		query = query + " where  league_id in ( " + sb.toString() + " )";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Integer> starPlayers = new ArrayList<Integer>();
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			// st.setString(1, sb.toString());

			rs = pst.executeQuery();
			while (rs.next()) {
				starPlayers.add(rs.getInt("player_id"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return starPlayers;
	}
			
	protected static void auditTeamPlayer(String player_id, String team_id, String user, String operation, Connection conn)
			throws Exception {

		PreparedStatement pst = null;		
		
		try {
			String insertQuery = null;
			insertQuery = "insert into team_player_audit(team_id,player_id,is_secondary,change_timestamp,change_by,change_type)"
					+ " (select team_id,player_id,is_secondary,now(),?,? from team_player where player_id = ? and team_id = ?)";
			pst = conn.prepareStatement(insertQuery);
			pst.setString(1, user);
			pst.setString(2, operation);
			pst.setString(3, player_id);
			pst.setString(4, team_id);
			pst.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.closeStatement(pst);
			}
	}
			
	protected void updatePlayerAdditional(PlayerDto player, int playerId, int clubId) throws Exception {

		String residentID = player.getResidentID();
		String passportNum = player.getPassportNum();
		String bornInKSA = player.getBornInKSA();
		String passportExpiry = player.getPassportExpiry();
		String KSAFirstEntry = player.getKSAFirstEntry();

		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		Connection conn = null;
		try {

			conn = DButility.getDefaultConnection();

			// st.executeUpdate(query);
			String query = "update player_additional set resident_id = ?,passport_num=?,"
					+ "passport_expiry=?,ksa_firstentry=?,ksa_born=?,club_id=? where player_id = ?";

			pst1 = conn.prepareStatement(query);
			pst1.setString(1, residentID);
			pst1.setString(2, passportNum);
			pst1.setString(3, passportExpiry);
			pst1.setString(4, KSAFirstEntry);
			pst1.setString(5, bornInKSA);
			pst1.setInt(6, clubId);
			pst1.setInt(7, player.getPlayerID());
			
			if (pst1.executeUpdate() == 0) {

				String playerAdditionalQuery = "insert into player_additional(player_id,resident_id,"
						+ "passport_num,passport_expiry,"
						+ " ksa_firstentry,ksa_born,club_id) values (?,?,?,?,?,?,?)";

				pst = conn.prepareStatement(playerAdditionalQuery);
				pst.setInt(1, playerId);
				pst.setString(2, residentID);
				pst.setString(3, passportNum);
				pst.setString(4, passportExpiry);
				pst.setString(5, KSAFirstEntry);
				pst.setString(6, bornInKSA);
				pst.setInt(7, clubId);
				pst.executeUpdate();
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(pst1);
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
			
	protected PlayerDto getPlayerAdditionalByID(int player_id) throws Exception {

		PreparedStatement pst = null;
		Connection conn = null;
		ResultSet rs = null;
		PlayerDto player = new PlayerDto();
		try {
			conn = DButility.getDefaultReadConnection();

			String GetData = "select pa.resident_id, pa.passport_num, pa.passport_expiry, pa.ksa_firstentry, "
					+ " pa.ksa_born,pa.id_proof_path, pa.nationality,pa.id_type,pa.id_number,pa.id_expiry_date,pa.first_entry_date,pa.no_of_years_stay from mcc.player_additional pa where player_id = ?";
			pst = conn.prepareStatement(GetData);
			pst.setInt(1, player_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				player.setResidentID(rs.getString("resident_id"));
				player.setPassportNum(rs.getString("passport_num"));
				player.setPassportExpiry(rs.getString("passport_expiry"));
				player.setKSAFirstEntry(rs.getString("ksa_firstentry"));
				player.setBornInKSA(rs.getString("ksa_born"));
				player.setNationality(rs.getString("nationality"));
				player.setIdTypeOman(rs.getString("id_type"));
				player.setIdNumberOman(rs.getString("id_number"));
				player.setIdExpiryDate(rs.getString("id_expiry_date"));
				player.setIdProofPath(rs.getString("id_proof_path"));	
				player.setOmanFirstEntryDate(rs.getString("first_entry_date"));
				player.setOmanNoOfyearsStay(rs.getString("no_of_years_stay"));	
				}
			return player;

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
			}
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	protected static void main(String[] args) throws Exception{
		PlayerDAO player = new PlayerDAO();
		player.mergePlayers(801259, 801212, 2119, "ccadmin");
	}

	protected int acceptTnCByPlayerId(int playerId, int clubId) throws Exception {
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			conn = DButility.getDefaultConnection();
			String query = "update player_club set accepted_terms = 1 where player_id = ? and club_id = ?";
			
			pst = conn.prepareStatement(query);
			pst.setInt(1, playerId);
			pst.setInt(2, clubId);
			
			return pst.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected static void updateCreateTimeForExistingPlayer(int playerId, int clubId) throws Exception {
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			conn = DButility.getDefaultConnection();
			String query = "update player_club set create_date = NOW() where player_id = ? and club_id = ?";
			
			pst = conn.prepareStatement(query);
			pst.setInt(1, playerId);
			pst.setInt(2, clubId);
			
			pst.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected  List<PlayerDto> getAvailablePlayerFromInternalClub(String internalClubId, int clubId) throws Exception{


		PreparedStatement pst = null;
		Connection conn = null;
		ResultSet rs = null;
		List<PlayerDto> selectedPlayers = new ArrayList<PlayerDto>();
		PlayerDto player = null;
		try {
			String playerFromInterClub = "select pv.player_id, pv.f_name, pv.l_name, pv.email from mcc.player_view pv, club_player pc "
					+ " where pv.is_active = 1 and pv.club_id = ? and pc.player_id = pv.player_id and pc.internal_club_id = ?";
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(playerFromInterClub);
			pst.setInt(1, clubId);
			pst.setString(2, internalClubId);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				player = new PlayerDto();
				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				player.setEmail(rs.getString("email"));
				selectedPlayers.add(player);
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return selectedPlayers;
	
	}
	
	
	
	protected Map<String, List<PlayerDto>> getPlayerForUpdateInternalClub(int clubId, ClubDto internalClub, boolean showAllPlayersBol) throws Exception{

		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		Connection conn = null;
		ResultSet rs = null;
		List<PlayerDto> availablePlayers = new ArrayList<PlayerDto>();
		List<PlayerDto> selectedPlayers = new ArrayList<PlayerDto>();
		Map<String, List<PlayerDto>> playerMap = new HashMap<String, List<PlayerDto>>();
		PlayerDto player = null;
		try {
			String playerFromInterClub = "select pv.player_id, pv.f_name, pv.l_name, pv.email, pv.custom_id from mcc.player_view pv, club_player pc "
					+ " where pv.is_active = 1 and pv.club_id = ? and pc.player_id = pv.player_id and pc.internal_club_id = ? ";
			
			playerFromInterClub += " order by pv.f_name, pv.l_name";
			conn = DButility.getReadConnection(clubId);
			if (internalClub != null && internalClub.getClubId() > 0) {
				pst = conn.prepareStatement(playerFromInterClub);
				pst.setInt(1, clubId);
				pst.setInt(2, internalClub.getClubId());
				rs = pst.executeQuery();
				while (rs.next()) {
					player = new PlayerDto();
					player.setPlayerID(rs.getInt("player_id"));
					player.setFirstName(rs.getString("f_name"));
					player.setLastName(rs.getString("l_name"));
					player.setEmail(rs.getString("email"));
					player.setCustomId(rs.getString("custom_id"));
					selectedPlayers.add(player);
				}
				playerMap.put("selectedPlayers", selectedPlayers);
			}
			DButility.closeRs(rs);

			String playerNotInInternalClub = "select pv.player_id, pv.f_name, pv.l_name, pv.email, pv.custom_id from mcc.player_view pv where pv.is_active = 1 and "
					+ " pv.club_id = ? " ;
					
			if(!showAllPlayersBol) {
				playerNotInInternalClub += " and pv.player_id not in (select player_id from club_player) ";
			}else if(internalClub != null && internalClub.getClubId() > 0){
				playerNotInInternalClub += " and pv.player_id not in (select player_id from club_player where internal_club_id = ?) ";
			}
			
			playerNotInInternalClub += "order by pv.f_name, pv.l_name";
			pst1 = conn.prepareStatement(playerNotInInternalClub);
			pst1.setInt(1, clubId);
			if(showAllPlayersBol && internalClub != null && internalClub.getClubId() > 0){
				pst1.setInt(2, internalClub.getClubId());
			}
			rs = pst1.executeQuery();
			
			while (rs.next()) {
				player = new PlayerDto();
				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				player.setEmail(rs.getString("email"));
				player.setCustomId(rs.getString("custom_id"));
				availablePlayers.add(player);
			}
			playerMap.put("availablePlayers", availablePlayers);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			
			DButility.dbCloseAll(conn, pst, rs);
		}
		return playerMap;
	}

	protected List<PlayerDto> getPlayersOfInternalClubScoring(int internalClubId, int clubId) throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = " SELECT p.player_id,p.l_name,p.f_name FROM mcc.player_view p, club_player cp "
				+ " WHERE p.club_id = ? AND cp.internal_club_id = ? AND "
				+ " cp.player_id = p.player_id GROUP BY p.player_id " + " union all "
				+ " SELECT p.player_id,p.l_name,p.f_name FROM mcc.player_view p WHERE p.club_id = ? "
				+ " AND p.can_play_anyteam = 1 ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId, true);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, internalClubId);
			st.setInt(3, clubId);

			rs = st.executeQuery();

			while (rs.next()) {
				PlayerDto player = new PlayerDto();
				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				players.add(player);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}
	
	protected List<PlayerDto> getPlayersOfTeamOfClubScoring(int teamId, int clubId) throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = " ( SELECT p.player_id,p.l_name,p.f_name, p.jersey_number FROM mcc.player_view p, team_player tp "
				+ " WHERE p.club_id = ? AND tp.team_id = ? AND "
				+ " tp.player_id = p.player_id GROUP BY p.player_id ) " + " union "
				+ " (SELECT p.player_id,p.l_name,p.f_name, p.jersey_number FROM mcc.player_view p WHERE p.club_id = ? "
				+ " AND p.can_play_anyteam = 1 ) order by f_name,l_name ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId, false);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, teamId);
			st.setInt(3, clubId);

			rs = st.executeQuery();

			while (rs.next()) {
				PlayerDto player = new PlayerDto();
				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
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

	protected List<PlayerDto> getPlayersOfInternalClub(int internalClubId, int clubId) throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = " select * from mcc.player_view p, club_player cp where  p.club_id = ? and cp.internal_club_id = ? and p.player_id = cp.player_id";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, internalClubId);

			rs = st.executeQuery();
			
			preparePlayerLiteList(players, rs);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		Collections.sort(players, new PlayerDtoComparator(true, true, false));
		return players;
	}
	
	protected List<PlayerDto> getAvailablePlayersOfInternalClub(int internalClubId, int teamId, int clubId) throws Exception {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		String query = " select * from mcc.player_view p, club_player cp where  p.club_id = ? and cp.internal_club_id = ? "
				+ "and p.player_id = cp.player_id and cp.player_id NOT IN (SELECT player_id FROM team_player WHERE team_id = ?) " ;

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, internalClubId);
			st.setInt(3, teamId);

			rs = st.executeQuery();
			
			preparePlayerLiteList(players, rs);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		Collections.sort(players, new PlayerDtoComparator(true, true, false));
		return players;
	}
	
	protected List<Integer> getPlayersIdOfInternalClub(int internalClubId, int clubId) throws Exception {
		String query = " select p.player_id from mcc.player_view p, club_player cp where  p.club_id = ? and cp.internal_club_id = ? and p.player_id = cp.player_id ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Integer> playerIds = new ArrayList<Integer>();
		try {
			
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, internalClubId);

			rs = st.executeQuery();
			
			while (rs.next()) {
				playerIds.add(rs.getInt("player_id"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerIds;
	}
	
	protected List<Integer> getAllPlayersIdOfLeague(int clubId) throws Exception {
		String query = " select player_id from mcc.player_club where club_id = "+clubId +" order by player_id";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Integer> playerIds = new ArrayList<Integer>();
		try {
			
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);

			rs = st.executeQuery();
			
			while (rs.next()) {
				playerIds.add(rs.getInt("player_id"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerIds;
	}
	
	protected Map<Integer, String> getAllPlayerIdNameMapOfLeague(int clubId) throws Exception {
		
		Map<Integer, String> playerIdNameMap = new HashMap<Integer, String>();
		
		String query = " select distinct player_id,concat(f_name,' ',l_name) player_name  from mcc.player_view where club_id = "+clubId;

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);

			rs = st.executeQuery();
			
			while (rs.next()) {
				playerIdNameMap.put(rs.getInt("player_id"), rs.getString("player_name"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerIdNameMap;
	}

	protected List<String> getUmpiresScoresEmailByClubId(int clubId, String type) throws Exception {

		String query = "select distinct email from umpire_view where type like '%"+type+"%' and club_id = ? and email is not null and email != '' ";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<String> emails = new ArrayList<String>();
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			rs = st.executeQuery();
			while (rs.next()) {
				emails.add(rs.getString("email"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return emails;
	
	}
	

	protected List<PlayerTeamDto> getPlayerTeamHistory(int playerId, int clubId) throws Exception {

		List<PlayerTeamDto> playerTeams = new ArrayList<PlayerTeamDto>();
		PlayerTeamDto playerTeam;
		
		String query = "SELECT t.team_name, l.league_name,tp.change_type,tp.change_timestamp,CONCAT(u.f_name,' ',u.l_name) change_by  FROM team_player_audit tp, team t, league l,mcc.user u  "
				+ " WHERE tp.player_id = " + playerId + " AND tp.team_id = t.team_id AND t.league = l.league_id  AND tp.change_by=u.user_id "
				+ " order by tp.change_timestamp DESC, league_id desc, t.team_id desc";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {	
				playerTeam = new PlayerTeamDto();
				playerTeam.setSeriesName(rs.getString("league_name"));
				playerTeam.setTeamName(rs.getString("team_name"));
				playerTeam.setAction(rs.getString("change_type"));
				playerTeam.setChangedBy(rs.getString("change_by"));
				playerTeam.setModifiedDate(rs.getString("change_timestamp"));
				
				playerTeams.add(playerTeam);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerTeams;
	}

	protected List<PlayerTeamDto> getPlayerTeams(int playerId, int clubId) throws Exception {

		List<PlayerTeamDto> playerTeams = new ArrayList<PlayerTeamDto>();
		PlayerTeamDto playerTeam;

		String query = "  SELECT t.team_name, l.league_name FROM team_player_audit tp, team t, league l  "
				+ "	WHERE tp.player_id = "+playerId+"  AND tp.team_id =  t.team_id AND t.league = l.league_id  "
				+ "	ORDER BY l.league_id DESC, t.team_id DESC ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				playerTeam = new PlayerTeamDto();
				playerTeam.setSeriesName(rs.getString("league_name"));
				playerTeam.setTeamName(rs.getString("team_name"));
//				playerTeam.setAction(rs.getString("change_type"));
//				playerTeam.setChangedBy(rs.getString("change_by"));
//				playerTeam.setModifiedDate(rs.getString("change_timestamp"));
				playerTeams.add(playerTeam);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerTeams;
	}

	protected List<PlayerDto> getPlayersOfProgramSession(int sessionId, int clubId) throws Exception {

		List<PlayerDto> playerDtos = new ArrayList<PlayerDto>();
		PlayerDto playerDto;
		String condition = "";
		if(sessionId > 0) {
			condition = " and sp.session_id = ?";
		}
		
		String query = "select sp.player_id, p.f_name, p.l_name, p.profile_image_path "
				+ "from mcc.academy_player_view p JOIN session_player sp on sp.player_id = p.player_id "
				+ "where p.club_id = ? and p.is_active <> 2"+condition;

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			if(sessionId > 0) {
				st.setInt(2, sessionId);
			}
			rs = st.executeQuery();
			while (rs.next()) {
				playerDto = new PlayerDto();
				playerDto.setFirstName(rs.getString("f_name"));
				playerDto.setLastName(rs.getString("l_name"));
				playerDto.setPlayerID(rs.getInt("player_id"));
				playerDto.setProfilepic_file_path(rs.getString("profile_image_path"));
				playerDtos.add(playerDto);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerDtos;
	
	}

	protected List<PlayerDto> getNonSessionPlayersFromProgram(int clubId, int programId, int sessionId, String restId) throws Exception {
		List<PlayerDto> playerDtos = new ArrayList<PlayerDto>();
		PlayerDto playerDto;

		String query = "select sp.player_id, p.f_name, p.l_name, p.profile_image_path "
				+ "from mcc.academy_player_view p JOIN session_player sp on sp.player_id = p.player_id "
				+ "JOIN program_session ps ON sp.session_id = ps.session_id "
				+ "where p.club_id = ? and p.is_active <> 2 and sp.session_id <> ?  AND ps.program_id = ?"
				+ " GROUP BY player_id";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, sessionId);
			st.setInt(3, programId);
			
			rs = st.executeQuery();
			while (rs.next()) {
				playerDto = new PlayerDto();
				playerDto.setFirstName(rs.getString("f_name"));
				playerDto.setLastName(rs.getString("l_name"));
				playerDto.setPlayerID(rs.getInt("player_id"));
				playerDto.setProfilepic_file_path(rs.getString("profile_image_path"));
				playerDtos.add(playerDto);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerDtos;
	}

	protected List<PlayerDto> getNonSessionPlayers(int clubId, int sessionId, String restId) throws Exception  {
		List<PlayerDto> playerDtos = new ArrayList<PlayerDto>();
		PlayerDto playerDto;

		String query = "SELECT pp.player_id, p.f_name, p.l_name, p.profile_image_path " + 
				"FROM mcc.academy_player_view p JOIN program_player pp " + 
				"ON p.player_id = pp.player_id " + 
				"WHERE pp.player_id NOT IN (SELECT player_id FROM session_player WHERE session_id = ?) " + 
				"AND pp.program_id IN(SELECT program_id FROM program_session WHERE session_id = ?) " + 
				"AND p.club_id = ?";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			
			st.setInt(1, sessionId);
			st.setInt(2, sessionId);
			st.setInt(3, clubId);
			
			rs = st.executeQuery();
			while (rs.next()) {
				playerDto = new PlayerDto();
				playerDto.setFirstName(rs.getString("f_name"));
				playerDto.setLastName(rs.getString("l_name"));
				playerDto.setPlayerID(rs.getInt("player_id"));
				playerDto.setProfilepic_file_path(rs.getString("profile_image_path"));
				playerDtos.add(playerDto);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerDtos;
	}

	protected boolean savePlayersToSession(int clubId, int sessionId, List<Long> playerIds, String restId) {
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			String insertQuery = "insert into session_player (session_id, player_id) values (?, ?)";
			conn = DButility.getConnection(clubId);
			pst = conn.prepareStatement(insertQuery);
			for(Long playerId : playerIds) {
				if(playerId > 0 ) {
					pst.setInt(1, sessionId);
					pst.setInt(2, playerId.intValue());
					pst.addBatch();
				}
			}
			pst.executeBatch();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}finally {
			DButility.dbCloseAll(conn, pst, null);
		}
		return true;
	}

	protected Map<Integer, Integer> getPlayersBySrcplayerId(int clubId, List<Integer> personIdList) throws Exception {
		Map<Integer, Integer> playerDtos = new HashMap<Integer, Integer>();

		String listString = personIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
		String query = "select player_id, concat(f_name, ' ', l_name) fullname, src_player_id from mcc.player where src_player_id in("+listString+");";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				playerDtos.put(rs.getInt("src_player_id"), rs.getInt("player_id"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerDtos;
	}

	protected BattingPositionDTO getPlayerBattingInsightsByPosition(List<Integer> clubIds, List<Integer> series, int playerId) throws Exception {
		
		String schema = "";
		
		String query = ""
				+ "SELECT "
				+ " batting_position, "
				+ " sum(runs_scored) AS runs, "
				+ " (sum(runs_scored) / sum(balls_faced)) * 100 AS strike_rate, "
				+ " count(batting_position) AS innings, "
				+ " sum(runs_scored) / count(NULLIF(how_out, '')) AS average "
				+ "FROM ( ";
		if (!series.isEmpty()) {
			// if we are filtering by series, we can assume clubs is of length 1
			Integer clubId = clubIds.get(0);
			schema = "club"+clubId;
			if(clubId == 1) {
				schema = "mcc";
			}
			query = ""
					+ "SELECT "
					+ " batting.batting_position, "
					+ " sum(batting.runs_scored) AS runs, "
					+ " (sum(batting.runs_scored) / sum(batting.balls_faced)) * 100 AS strike_rate, "
					+ " count(batting.batting_position) AS innings, "
					+ " sum(batting.runs_scored) / count(NULLIF(batting.how_out, '')) AS average "
					+ "FROM "
					+ schema+".batting batting "
					+ "JOIN "+schema+".player_statistics_summary summary "
					+ "ON summary.match_id = batting.match_id AND summary.player_id = batting.player_id "
					+ "WHERE batting.player_id = " + playerId
					+ " and (";

			for (int j = 0; j < series.size(); j++) {
				query += "series_id = " + series.get(j);

				if (j != series.size() - 1) {
					query += " OR ";
				}
				if (j == series.size() - 1) {
					query += ")";
				}
			}

			query += "GROUP BY batting_position "
					+ "ORDER BY batting_position ASC";
		} else {
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT runs_scored, "
						+ "balls_faced, "
						+ "batting_position, "
						+ "how_out "
						+ "FROM "
						+ schema + ".batting "
						+ "WHERE "
						+ "player_id = " + playerId + " ";
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ "GROUP BY batting_position "
					+ "ORDER BY batting_position ASC";
		}

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			return prepareBattingPositionResponse(rs);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected Map<String,Integer> getPlayerBatsmanOutVSBalls(List<Integer> clubIds, List<Integer> series, int playerId) throws Exception {
		
		String schema = "";
		
		String query = ""
				+ "SELECT "
				+ " COUNT(*) AS no_of_outs, "
				+ "CONCAT(((balls_faced) DIV 10)*10,'-',(((balls_faced) DIV 10)+1)*10) AS balls "
				+ "FROM ( ";
		if (!CommonUtility.isListNullEmpty(series)) {
			Integer clubId = clubIds.get(0);
			schema = "club"+clubId;
			if(clubId == 1) {
				schema = "mcc";
			}
			query += "SELECT "
					+ " balls_faced "
					+ "FROM "
					+ schema+".player_statistics_summary "	
					+ "WHERE player_id = " + playerId + " AND how_out IS NOT NULL AND how_out != '' " 
					+ " and (";

			for (int j = 0; j < series.size(); j++) {
				query += "series_id = " + series.get(j);

				if (j != series.size() - 1) {
					query += " OR ";
				}
				if (j == series.size() - 1) {
					query += ")";
				}
			}

		} else {
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " balls_faced "					
						+ "FROM "
						+ schema+".player_statistics_summary "	
						+ "WHERE player_id = " + playerId + " AND how_out IS NOT NULL AND how_out != '' "; 
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ "GROUP BY balls ORDER BY balls_faced";
					
		}

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<String,Integer> outVsBallsMap = new LinkedHashMap<String, Integer>();

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				outVsBallsMap.put(rs.getString("balls"),rs.getInt("no_of_outs"));
			}
			return outVsBallsMap;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected Map<String,Integer> getPlayerBatsmanOutBetweenRuns(List<Integer> clubIds, List<Integer> series, int playerId) throws Exception {
		
		String schema = "";
		
		String query = ""
				+ "SELECT "
				+ " count(*) as no_of_outs, "
				+ " CONCAT(((runs_scored) DIV 10)*10,'-',(((runs_scored) DIV 10)+1)*10) as runs "
				+ "FROM ( ";
		if (!CommonUtility.isListNullEmpty(series)) {
			Integer clubId = clubIds.get(0);
			schema = "club"+clubId;
			if(clubId == 1) {
				schema = "mcc";
			}
			query += "SELECT "
					+ " runs_scored "
					+ "FROM "
					+ schema+".player_statistics_summary "	
					+ "WHERE player_id = " + playerId + " AND how_out IS NOT NULL AND how_out != '' " 
					+ " and (";

			for (int j = 0; j < series.size(); j++) {
				query += "series_id = " + series.get(j);

				if (j != series.size() - 1) {
					query += " OR ";
				}
				if (j == series.size() - 1) {
					query += ")";
				}
			}

		} else {
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " runs_scored "
						+ "FROM "
						+ schema+".player_statistics_summary "	
						+ "WHERE player_id = " + playerId + " AND how_out IS NOT NULL AND how_out != '' " ;
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ "GROUP BY runs order by runs_scored";
					
		}

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<String,Integer> outVsBallsMap = new LinkedHashMap<String, Integer>();

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				outVsBallsMap.put(rs.getString("runs"),rs.getInt("no_of_outs"));
			}
			return outVsBallsMap;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected List<RunsBreakDownDto> getPlayerBatsmanRunsBreakDown(List<Integer> clubIds, int playerId) throws Exception {
		
		String schema = "";		
		List<RunsBreakDownDto> runsBreakDownList = null;
		
		String query = ""
				+ "SELECT "
				+ " count(*) as no_of_times, "
				+ " sum(runs) total, "
				+ " runs "
				+ "FROM ( ";
		
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " CASE WHEN ball_type = 'No Ball of Bat' THEN runs-1 ELSE runs END AS runs "
						+ "FROM "
						+ schema+".ball "	
						+ "WHERE batsman = " + playerId
						+" AND ball_type IN ('No Ball of Bat','Good Ball')" ;
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ "GROUP BY runs order by runs";
					
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();			
			runsBreakDownList = new ArrayList<RunsBreakDownDto>();
			
			while (rs.next()) {
				
				RunsBreakDownDto dto = new RunsBreakDownDto();
				
				dto.setRuns(rs.getString("runs"));
				dto.setNoOfTimes(rs.getInt("no_of_times"));
				dto.setTotal(rs.getInt("total"));
				
				runsBreakDownList.add(dto);
			}
			return runsBreakDownList;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected List<RunsBreakDownDto> getPlayerBowlerRunsBreakDown(List<Integer> clubIds, int playerId) throws Exception {
		
		String schema = "";		
		List<RunsBreakDownDto> runsBreakDownList = null;
		
		String query = ""
				+ "SELECT "
				+ " count(*) as no_of_times, "
				+ " sum(runs) total, "
				+ " runs "
				+ "FROM ( ";
		
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " CASE WHEN  ball_type = 'No Ball of Bat' THEN runs-1 ELSE runs END AS runs "
						+ "FROM "
						+ schema+".ball "	
						+ "WHERE bowler = " + playerId
						+" AND  ball_type IN ('No Ball of Bat','Good Ball')" ;
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ "GROUP BY runs order by runs";
					
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			runsBreakDownList = new ArrayList<RunsBreakDownDto>();
			
			while (rs.next()) {
				
				RunsBreakDownDto dto = new RunsBreakDownDto();
				
				dto.setRuns(rs.getString("runs"));
				dto.setNoOfTimes(rs.getInt("no_of_times"));
				dto.setTotal(rs.getInt("total"));
				
				runsBreakDownList.add(dto);
			}
			return runsBreakDownList;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected List<BowlerPerformanceDto> getBowlerPerformVSDiffPhases(List<Integer> clubIds, int playerId) throws Exception {
		
		String schema = "";		
		List<BowlerPerformanceDto> dtos = null;
		
		String query = ""
				+ "SELECT "
				+ " COUNT(*) AS no_of_overs,"
				+ " SUM(runs) AS runs, "
				+ " round(SUM(runs)/COUNT(*),2) AS economy, "
				+ "  SUM(wickets) AS wickets, "
				+ "  SUM(extras) AS extras, "
				+ " CONCAT((((over) DIV 5)*5)+1,'-',(((over) DIV 5)+1)*5) AS overs "
				+ "FROM ( ";
		
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " over, SUM(IF(ball_type IN ('No Ball of Bat'), runs-1, runs)) AS runs, SUM(IF(bowler=wicket_taker_1, 1, 0)) AS wickets, "
						+ " SUM(IF(ball_type IN ('No Ball','Bye','Leg Bye','No Ball Leg Bye','No Ball Bye','No Ball of Bat'), 1, IF(ball_type='Wide', runs, 0))) AS extras " 
						+ "FROM "
						+ schema+".ball "	
						+ "WHERE bowler = " + playerId +" GROUP BY over, innings_id ";
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ " GROUP BY overs ORDER BY over ";
					
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			dtos = new ArrayList<BowlerPerformanceDto>();
			
			while (rs.next()) {
				
				BowlerPerformanceDto dto = new BowlerPerformanceDto();
				
				dto.setNoOfOvers(rs.getInt("no_of_overs"));
				dto.setRuns(rs.getInt("runs"));
				dto.setEconomy(rs.getFloat("economy"));
				dto.setWickets(rs.getInt("wickets"));
				dto.setExtras(rs.getInt("extras"));
				dto.setOvers(rs.getString("overs"));
				
				dtos.add(dto);
			}
			return dtos;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected List<BallByBallExtrasDto> getBowlerBallByBallExtras(List<Integer> clubIds, int playerId) throws Exception {
		
		String schema = "";		
		
		String query = ""
				+ "SELECT "
				+ " concat('Ball ',ball) as ball_str, "
				+ " SUM(IF(ball_type='Wide',runs,0)) no_of_wides, "
				+ " SUM(IF(ball_type != 'Wide',1,0)) no_of_no_balls "
				+ "FROM ( ";
		
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " ball, "
						+ " runs, "
						+ " ball_type " 
						+ "FROM "
						+ schema+".ball "	
						+ "WHERE bowler = " + playerId +" AND "
						+ "ball_type IN ('Wide','No Ball','No Ball of Bat','No Ball Bye','No Ball Leg Bye') "
						+ "AND ball>0 AND ball<7 ";
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ " GROUP BY ball ORDER BY ball ";
					
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			List<BallByBallExtrasDto> dtos = new ArrayList<BallByBallExtrasDto>();
			
			while (rs.next()) {
				
				BallByBallExtrasDto dto = new BallByBallExtrasDto();
				
				dto.setBall(rs.getString("ball_str"));
				dto.setNoOfWides(rs.getInt("no_of_wides"));
				dto.setNoOfNoBalls(rs.getInt("no_of_no_balls"));
				
				dtos.add(dto);
			}
			return dtos;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected List<BallByBallWicketsBoundaryDto> getBowlerBallByBallWicketsBoundary(List<Integer> clubIds, int playerId) throws Exception {
		
		String schema = "";		
		
		String query = ""
				+ "SELECT "
				+ " concat('Ball ',ball) as ball_str, "
				+ " SUM(no_of_fours+no_of_sixes) as no_of_boundaries, "
				+ " SUM(no_of_wickets) as no_of_wickets "
				+ "FROM ( ";
		
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " ball, "
						+ " SUM(IF(is_four=1,1,0)) no_of_fours, "
						+ " SUM(IF(is_six=1,1,0)) no_of_sixes, "
						+ " SUM(IF(bowler=wicket_taker_1,1,0)) no_of_wickets "
						+ "FROM "
						+ schema+".ball "	
						+ "WHERE bowler = " + playerId +" AND ball> 0 AND ball <7 GROUP BY ball ";
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ " GROUP BY ball ORDER BY ball ";
					
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			List<BallByBallWicketsBoundaryDto> dtos = new ArrayList<BallByBallWicketsBoundaryDto>();
			
			while (rs.next()) {
				
				BallByBallWicketsBoundaryDto dto = new BallByBallWicketsBoundaryDto();
				
				dto.setBall(rs.getString("ball_str"));
				dto.setNoOfBoundaries(rs.getInt("no_of_boundaries"));
				dto.setNoOfWickets(rs.getInt("no_of_wickets"));
				
				dtos.add(dto);
			}
			return dtos;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected Map<Integer,Integer> getBowlerBattingPositionVSWickets(List<Integer> clubIds, List<Integer> series, int playerId) throws Exception {
		
		String schema = "";
		
		String query = ""
				+ "SELECT "
				+ " batting_position, "
				+ " count(*) as no_of_wickets "
				+ "FROM ( ";
		if (!CommonUtility.isListNullEmpty(series)) {
			Integer clubId = clubIds.get(0);
			schema = "club"+clubId;
			if(clubId == 1) {
				schema = "mcc";
			}
			query += "SELECT "
					+ " batting_position "
					+ "FROM "
					+ schema+".batting "	
					+ "WHERE wicket_taker1  = " + playerId  
					+ " and (";

			for (int j = 0; j < series.size(); j++) {
				query += "series_id = " + series.get(j);

				if (j != series.size() - 1) {
					query += " OR ";
				}
				if (j == series.size() - 1) {
					query += ")";
				}
			}

		} else {
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " batting_position "					
						+ "FROM "
						+ schema+".batting "	
						+ "WHERE wicket_taker1 = " + playerId; 
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final "
					+ "GROUP BY batting_position ORDER BY batting_position";
					
		}
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<Integer,Integer> outVsBallsMap = new LinkedHashMap<Integer, Integer>();

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				outVsBallsMap.put(rs.getInt("batting_position"),rs.getInt("no_of_wickets"));
			}
			return outVsBallsMap;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected Map<String,Integer> getBowlerExtrasAnalysis(List<Integer> clubIds, List<Integer> series, int playerId) throws Exception {
		String schema = "";
		String query = ""
				+ "SELECT "
				+ " sum(wides) as no_of_wides, "
				+ " sum(no_balls) as no_of_no_balls "
				+ "FROM ( ";
		if (!CommonUtility.isListNullEmpty(series)) {
			Integer clubId = clubIds.get(0);
			schema = "club"+clubId;
			if(clubId == 1) {
				schema = "mcc";
			}
			query += "SELECT "
					+ " wides, no_balls "
					+ "FROM "
					+ schema+".bowling "	
					+ "WHERE player_id  = " + playerId  
					+ " and (";

			for (int j = 0; j < series.size(); j++) {
				query += "series_id = " + series.get(j);

				if (j != series.size() - 1) {
					query += " OR ";
				}
				if (j == series.size() - 1) {
					query += ")";
				}
			}

		} else {
			for (int i = 0; i < clubIds.size(); i++) {
				
				schema = "club"+clubIds.get(i);
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}				
				query += "SELECT "
						+ " wides, no_balls  "					
						+ "FROM "
						+ schema+".bowling "	
						+ "WHERE player_id = " + playerId; 
				if (i != clubIds.size() - 1) {
					query += " UNION ALL ";
				}
			}
			query += ") AS final ";
					
		}
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<String,Integer> outVsBallsMap = new LinkedHashMap<String, Integer>();

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				outVsBallsMap.put("Wides",rs.getInt("no_of_wides"));
				outVsBallsMap.put("No Balls",rs.getInt("no_of_no_balls"));
			}
			return outVsBallsMap;
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	private BattingPositionDTO prepareBattingPositionResponse(ResultSet rs) throws SQLException {
		BattingPositionDTO battingPositionResponse = new BattingPositionDTO();

		while (rs.next()) {
			int battingPosition = rs.getInt("batting_position");
			int runs = rs.getInt("runs");
			Object strikeRate = rs.getObject("strike_rate");
			int innings = rs.getInt("innings");
			Object average = rs.getObject("average");
			
			if(battingPosition>0 && battingPosition<12) {
				if(average == null) {
					battingPositionResponse.getAverages()[battingPosition - 1] = -1;
				} else {
					BigDecimal response = (BigDecimal) average;
					battingPositionResponse.getAverages()[battingPosition - 1] = response.floatValue();
				}

				if(strikeRate == null) {
					battingPositionResponse.getStrikeRates()[battingPosition - 1] = 0;
				} else {
					BigDecimal response = (BigDecimal) strikeRate;
					battingPositionResponse.getStrikeRates()[battingPosition - 1] = response.floatValue();
				}
				battingPositionResponse.getInnings()[battingPosition - 1] = innings;
				battingPositionResponse.getRuns()[battingPosition - 1] = runs;
			}
		}
		return battingPositionResponse;
	}
	

	protected OutTypesDto getPlayerBattingOutTypes(List<Integer> clubIds, List<Integer> series, int playerId) throws Exception {
		
		String schema = "";
		
		String query = ""
				+ "SELECT "
				+ " Count(how_out) AS count, "
				+ " how_out "
				+ "FROM ( ";

		if (!series.isEmpty()) {
			// if we are filtering by series, we can assume clubs is of length 1
			Integer clubId = clubIds.get(0);
			schema = "club"+clubId;
			if(clubId == 1) {
				schema = "mcc";
			}
			query += "SELECT stats.how_out "
					+ "FROM "
					+ schema+ ".player_statistics_summary stats "
					+ "WHERE "
					+ "stats.player_id = " + playerId + " "
					+ "AND how_out <> '' "
					+ "AND how_out IS NOT NULL AND (";
			for (int j = 0; j < series.size(); j++) {
				query += "stats.series_id = " + series.get(j);

				if (j != series.size() - 1) {
					query += " OR ";
				}
				if (j == series.size() - 1) {
					query += ")";
				}
			}
		} else {
			for (int i = 0; i < clubIds.size(); i++) {
				schema = "club"+clubIds.get(i);
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}
				query += "SELECT stats.how_out "
						+ "FROM "
						+ schema + ".player_statistics_summary stats "
						+ "WHERE "
						+ "stats.player_id = " + playerId + " "
						+ "AND how_out <> '' "
						+ "AND how_out IS NOT NULL ";
				if (i != clubIds.size() - 1) {
					query += "UNION ALL ";
				}
			}
		}

		query += ") AS final "
				+ "GROUP BY how_out";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			return prepareOutTypesResponse(rs);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected OutTypesBowlingDto getPlayerBowlingOutTypes(List<Integer> clubIds, List<Integer> series, int playerId) throws Exception {
		
		String schema = "";
		
		String query = ""
				+ "SELECT "
				+ " Count(how_out) AS count, "
				+ " how_out "
				+ "FROM ( ";

		if (!series.isEmpty()) {
			// if we are filtering by series, we can assume clubs is of length 1
			Integer clubId = clubIds.get(0);
			schema = "club"+clubId;
			if(clubId == 1) {
				schema = "mcc";
			}
			query += "SELECT stats.how_out "
					+ "FROM "
					+ schema+ ".player_statistics_summary stats "
					+ "WHERE "
					+ "stats.wicket_taker1 = " + playerId + " "
					+ "AND how_out IN ('b','lbw','ctw','ct','st') (";
			for (int j = 0; j < series.size(); j++) {
				query += "stats.series_id = " + series.get(j);

				if (j != series.size() - 1) {
					query += " OR ";
				}
				if (j == series.size() - 1) {
					query += ")";
				}
			}
		} else {
			for (int i = 0; i < clubIds.size(); i++) {
				schema = "club"+clubIds.get(i);
				if(clubIds.get(i) == 1) {
					schema = "mcc";
				}
				query += "SELECT stats.how_out "
						+ "FROM "
						+ schema + ".player_statistics_summary stats "
						+ "WHERE "
						+ "stats.wicket_taker1 = " + playerId + " "
						+ "AND how_out IN ('b','lbw','ctw','ct','st') ";
				if (i != clubIds.size() - 1) {
					query += "UNION ALL ";
				}
			}
		}

		query += ") AS final "
				+ "GROUP BY how_out";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			return prepareBowlingOutTypesResponse(rs);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	private OutTypesBowlingDto prepareBowlingOutTypesResponse(ResultSet rs) throws SQLException {
		OutTypesBowlingDto outtypes = new OutTypesBowlingDto();
		while (rs.next()) {
			String outType = rs.getString("how_out");
			switch (outType) {
				case "b":
					outtypes.setBowled(rs.getInt("count"));
					break;
				case "ct":
					outtypes.setCaught(rs.getInt("count"));
					break;
				case "st":
					outtypes.setStumped(rs.getInt("count"));
					break;
				case "lbw":
					outtypes.setLbw(rs.getInt("count"));
					break;
				case "ctw":
					outtypes.setCaughtBehind(rs.getInt("count"));
					break;	
				default:
					break;
			}
		}
		return outtypes;
	}

	private OutTypesDto prepareOutTypesResponse(ResultSet rs) throws SQLException {
		OutTypesDto outtypes = new OutTypesDto();
		while (rs.next()) {
			String outType = rs.getString("how_out");
			switch (outType) {
				case "b":
					outtypes.setBowled(rs.getInt("count"));
					break;
				case "ct":
					outtypes.setCaught(rs.getInt("count"));
					break;
				case "st":
					outtypes.setStumped(rs.getInt("count"));
					break;
				case "lbw":
					outtypes.setLbw(rs.getInt("count"));
					break;
				case "ctw":
					outtypes.setCaughtBehind(rs.getInt("count"));
					break;
				case "ro":
					outtypes.setRunout(rs.getInt("count"));
					break;
				case "obf":
					outtypes.setObjectingField(rs.getInt("count"));
					break;
				case "mk":
					outtypes.setMankading(rs.getInt("count"));
					break;
				case "rto":
					outtypes.setRetiredOut(rs.getInt("count"));
					break;
				case "rt":
					outtypes.setRetiredHurt(rs.getInt("count"));
					break;
				case "ht":
					outtypes.setHitWicket(rs.getInt("count"));
					break;
				default:
					break;
			}
		}
		return outtypes;
	}
	
	protected List<PlayerDto> getPlayersBySearchString(String qStrings, String association, int clubId, int limit) throws Exception {
		
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		
		String query = "select p.f_name, p.l_name, p.player_id, p.club_id, c.name from player_view p, club c"
				+ " where p.is_active = 1 and concat(p.f_name, ' ', p.l_name) "
				+ "like '%" + DButility.escape(qStrings.toLowerCase()) +  "%' ";
		
		if(!CommonUtility.isNullOrEmpty(association)) {
			query += " and p.club_id = c.club_id and c.association = ? ";
		}else if(clubId>0){
			query += " and p.club_id = c.club_id and p.club_id = ? ";
		}
		if(limit>0) {
			query += " limit ?";
		}
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.prepareStatement(query);
			int index = 1;
			if(!CommonUtility.isNullOrEmpty(association)) {
				st.setString(index++, association);
			}else if(clubId>0){
				st.setInt(index++, clubId);
			}
			if(limit>0) {
				st.setInt(index++, limit);
			}			
			rs = st.executeQuery();			
			while (rs.next()) {
				
				PlayerDto player = new PlayerDto();
				
				player.setPlayerID(rs.getInt("player_id"));
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				player.setClubId(rs.getInt("club_id"));
				player.setClubName(rs.getString("name"));
				
				players.add(player);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		Collections.sort(players, new PlayerDtoComparator(true, true, false));
		
		return players;
	}

	protected List<Integer> getPlayersPlayedMatches(int clubId, LeagueDto league) throws Exception {
		List<Integer> playerDtos = new ArrayList<Integer>();
		String condition = "";
		
		condition = league.getParent() > 0 ? "SELECT league_id FROM league WHERE parent = "+league.getParent() :league.getLeagueId()+"";
		
		String query = "SELECT DISTINCT mpt.player_id FROM match_player_team mpt, " 
				+" team t WHERE mpt.team_id = t.team_id  " 
				+" AND t.league IN("+condition+");";
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				playerDtos.add(rs.getInt("player_id"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return playerDtos;
	}
	
//	protected List<PlayerDto> getPlayersNotPlayedMatches(int clubId, LeagueDto league, String teamName) throws Exception {
//
//		List<PlayerDto> players = new ArrayList<PlayerDto>();
//		String condition = "";
//		condition = league.getParent() > 0 ? "SELECT league_id FROM league WHERE parent = "+league.getParent() :league.getLeagueId()+"";
//
//		String query = " SELECT * FROM mcc.player_view WHERE player_id IN(SELECT DISTINCT tp.player_id FROM team_player tp, team t " 
//						+ " WHERE tp.team_id = t.team_id AND t.league IN("+condition+") AND tp.player_id NOT IN(SELECT player_id FROM match_player_team mpt, "
//						+ " team lt WHERE mpt.team_id = lt.team_id AND lt.league IN("+condition+"))) AND club_id = "+clubId+" " 
//						+ " UNION " 
//						+ " SELECT * FROM mcc.player_view WHERE player_id IN(SELECT DISTINCT tp.player_id FROM team_player tp, team t WHERE tp.team_id = t.team_id " 
//						+ " AND t.league IN("+condition+") AND tp.player_id IN(SELECT player_id FROM match_player_team mpt, "
//						+ " team lt WHERE mpt.team_id = lt.team_id AND lt.league IN("+condition+") AND lt.team_name= ?)) AND club_id = "+clubId+";" ;
//
//		Connection conn = null;
//		PreparedStatement st = null;
//		ResultSet rs = null;
//		try {
//			conn = DButility.getReadConnection(clubId);
//			st = conn.prepareStatement(query);
//			st.setString(1, teamName);
//			rs = st.executeQuery();
//			
//			preparePlayerLiteList(players, rs);
//		} catch (SQLException e) {
//			throw new Exception(e.getMessage());
//		} finally {
//			DButility.dbCloseAll(conn, st, rs);
//		}
//		Collections.sort(players, new PlayerDtoComparator(true, true, false));
//		return players;
//	}

	protected List<Integer> getPlayerBattingRunsBallByBall(int clubId, int playerId, int matchId) throws Exception{

		List<Integer> ballByBall = new ArrayList<>();

		String query = "SELECT " +
				"CASE when ball_type = 'Good Ball' then ball.runs " +
				"when ball_type = 'No Ball of Bat' then ball.runs - ball.no_ball_custom_runs " +
				"End as runs " +
				"from ball " +
				"WHERE " +
				"ball.innings_id in (select innings_id from innings WHERE match_id = ?) "  +
				"AND ball.batsman = ? " +
				"AND (ball.ball_type = 'Good Ball' " +
				"OR ball.ball_type = 'No Ball of Bat') ";


		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			st.setInt(2, playerId);
			rs = st.executeQuery();

			while (rs.next()) {
				ballByBall.add(rs.getInt(1));
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return ballByBall;
	}

	protected Map<String, Integer> getPlayerBattingRunsBreakdownInMatch(int clubId, int matchId, int playerId) throws Exception {
		String query = "SELECT runs, count(runs) as count from ( " +
				"SELECT CASE when ball_type = 'Good Ball' then ball.runs " +
				"when ball_type = 'No Ball of Bat' then ball.runs - ball.no_ball_custom_runs " +
				"End as runs " +
				"from ball " +
				"WHERE " +
				"ball.innings_id in (select innings_id from innings WHERE match_id = ?) "  +
				"AND ball.batsman = ? " +
				"AND (ball.ball_type = 'Good Ball' " +
				"OR ball.ball_type = 'No Ball of Bat')) tb " +
				"GROUP BY runs";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			st.setInt(2, playerId);
			rs = st.executeQuery();

			Map<String, Integer> response = new HashMap<>();
			while (rs.next()) {
				response.put(rs.getString("runs"), rs.getInt("count"));
			}

			return response;

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	protected double[] getBatsmanDotBallPercentagePerBallInAMatch(int clubId, int matchId, int playerId) throws Exception {
		String query = ""
				+ "SELECT "
				+ "ball, "
				+ "count(ball) as count "
				+ "FROM ball "
				+ "WHERE "
				+ "ball.innings_id in (SELECT innings_id FROM innings WHERE match_id = ?) AND batsman = ? and ball_type = 'Good Ball' and runs = 0 and out_method = '' "
				+ "GROUP BY ball";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			st.setInt(2, playerId);
			rs = st.executeQuery();

			int[] response = new int[6];
			while (rs.next()) {
				response[rs.getInt("ball") - 1] = rs.getInt("count");
			}

			return calculatePercentageGivenAListOfNumbers(response);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	protected double[] getBowlerBoundryBallPercentagePerBallInAMatch(int clubId, int matchId, int playerId) throws Exception {
		String query = ""
				+ "SELECT "
				+ "ball, "
				+ "count(ball) as count "
				+ "FROM ball "
				+ "WHERE "
				+ "ball.innings_id in (SELECT innings_id FROM innings WHERE match_id = ?) and bowler = ? and (is_six or is_four) "
				+ "GROUP BY ball";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			st.setInt(2, playerId);

			rs = st.executeQuery();

			int[] response = new int[6];
			while (rs.next()) {
				response[rs.getInt("ball") - 1] = rs.getInt("count");
			}

			return calculatePercentageGivenAListOfNumbers(response);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected double[] getBowlerDotballPercentagePerBall(int clubId, int matchId, int playerId) throws Exception {
		String query = ""
				+ "SELECT "
				+ "ball, "
				+ "count(ball) as count "
				+ "FROM ball "
				+ "WHERE "
				+ "ball.innings_id in (SELECT innings_id FROM innings WHERE match_id = ?) and bowler = ? and ball_type = 'Good Ball' and runs = 0 and out_method = '' "
				+ "GROUP BY ball";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			st.setInt(2, playerId);

			rs = st.executeQuery();

			int[] response = new int[6];
			while (rs.next()) {
				response[rs.getInt("ball") - 1] = rs.getInt("count");
			}

			return calculatePercentageGivenAListOfNumbersTwoDecimals(response);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	protected List<String> getPlayerBowlingBallByBallInAMatch(int clubId, int matchId, int playerId) throws Exception {
		String query = ""
				+ "SELECT CASE "
				+ "when (out_method <> '' and runs = 0) then 'W' "
				+ "when out_method <> '' and runs <> 0 then CONCAT(runs,'W') "
				+ "when ball_type = 'No Ball of Bat' or ball_type = 'No Ball Bye' or ball_type = 'No Ball' then CONCAT(runs,'nb') "
				+ "when ball_type = 'Bye' or ball_type = 'Leg Bye' then 0 "
				+ "when ball_type = 'Wide' THEN CONCAT(runs,'wd') "
				+ "When ball_type = 'Good Ball' Then runs "
				+ "End as runsText "
				+ "from ball  WHERE ball.innings_id in (SELECT innings_id FROM innings WHERE match_id = ?) and bowler = ? "
				+ "and (ball_type <> 'Auto Comment Ball' or ball_type <> 'Bowler Count Ball' or ball_type <> 'No Count Ball' "
				+ "or ball_type <> 'Add Penalties' or ball_type <> 'Remove Penalties') ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			st.setInt(2, playerId);
			rs = st.executeQuery();

			List<String> response = new ArrayList<>();
			while (rs.next()) {
				response.add(rs.getString("runsText"));
			}

			return response;

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected List<Pair> getPlayerBowlingBallByBallMapInAMatch(int clubId, int matchId, int playerId) throws Exception {
		
		String query = ""
				+ "SELECT runs, CASE "
				+ "when (out_method <> '' and runs = 0) then 'W' "
				+ "when out_method <> '' and runs <> 0 then CONCAT(runs,'W') "
				+ "when ball_type = 'No Ball of Bat' or ball_type = 'No Ball Bye' or ball_type = 'No Ball' then CONCAT(runs,'nb') "
				+ "when ball_type = 'Bye' or ball_type = 'Leg Bye' then 0 "
				+ "when ball_type = 'Wide' THEN CONCAT(runs,'wd') "
				+ "When ball_type = 'Good Ball' Then runs "
				+ "End as runsText "
				+ "from ball  WHERE ball.innings_id in (SELECT innings_id FROM innings WHERE match_id = ?) and bowler = ? "
				+ "and (ball_type <> 'Auto Comment Ball' or ball_type <> 'Bowler Count Ball' or ball_type <> 'No Count Ball' "
				+ "or ball_type <> 'Add Penalties' or ball_type <> 'Remove Penalties') ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			st.setInt(2, playerId);
			rs = st.executeQuery();

			List<Pair> ballByBallRunsMap = new ArrayList<Pair>();
			while (rs.next()) {
				if(!CommonUtility.isNullOrEmpty(rs.getString("runsText"))) {
					Pair pair = new Pair();
					pair.setValues1(rs.getString("runs"));
					pair.setValues2(rs.getString("runsText"));
					
					ballByBallRunsMap.add(pair);
				}
			}
			return ballByBallRunsMap;

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected double[] getBatsmanBoundryBallPercentagePerBallInAMatch(int clubId, int matchId, int playerId) throws Exception {
		String query = ""
				+ "SELECT "
				+ "ball, "
				+ "count(ball) as count "
				+ "FROM ball "
				+ "WHERE "
				+ "ball.innings_id in (SELECT innings_id FROM innings WHERE match_id = ?) and batsman = ? and (is_six or is_four) "
				+ "GROUP BY ball";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, matchId);
			st.setInt(2, playerId);

			rs = st.executeQuery();

			int[] response = new int[6];
			while (rs.next()) {
				response[rs.getInt("ball") - 1] = rs.getInt("count");
			}

			return calculatePercentageGivenAListOfNumbersTwoDecimals(response);

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
	protected PlayerAdditionalDto checkOmanPlayer(String idType, String idNumber, int clubId) throws Exception {
		PlayerAdditionalDto additionalDto = null;
		
		String query = "select player_id, id_type, id_number, id_expiry_date, id_proof_path, first_entry_date, "
				+ " no_of_years_stay, season_year, nationality from mcc.player_additional where id_type = ? and id_number =? ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		PreparedStatement st1 = null;
		ResultSet rs1 = null;
		try {
			conn = DButility.getReadConnection(clubId);
			st = conn.prepareStatement(query);
			st.setString(1, idType);
			st.setString(2, idNumber);

			rs = st.executeQuery();
			while (rs.next()) {
				additionalDto = new PlayerAdditionalDto();
				additionalDto.setPlayerId(rs.getInt("player_id"));
				additionalDto.setClubId(clubId+"");
				additionalDto.setFirstEntryDate(rs.getString("first_entry_date"));
				additionalDto.setIdExpiryDate(rs.getString("id_expiry_date"));
				additionalDto.setIdNumber(rs.getString("id_number"));
				additionalDto.setIdProofPath(rs.getString("id_proof_path"));
				additionalDto.setIdType(rs.getString("id_type"));
				additionalDto.setLastPlayedSeries("");
				additionalDto.setLastPlayedTeam("");
				additionalDto.setNoOfYearsStay(rs.getString("no_of_years_stay"));
				additionalDto.setSeasonYear(rs.getInt("season_year"));
				additionalDto.setNationality(rs.getString("nationality"));
			}
			if(additionalDto != null && additionalDto.getPlayerId() > 0) {
				query = "SELECT team_name, (SELECT league_name FROM league WHERE league_id = t.league) divisionName " 
							+" FROM team t WHERE team_id IN(SELECT MAX(team_id) FROM team_player WHERE player_id = ?)";
				st1 = conn.prepareStatement(query);
				st1.setInt(1, additionalDto.getPlayerId());
				
				rs1 = st1.executeQuery();
				while(rs1.next()) {
					additionalDto.setLastPlayedSeries(rs1.getString("divisionName"));
					additionalDto.setLastPlayedTeam(rs1.getString("team_name"));
				}
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
			DButility.dbCloseAll(conn, st1, rs1);
		}
		return additionalDto;
	}

	private double[] calculatePercentageGivenAListOfNumbers(int[] numbers) {
		int sum = Arrays.stream(numbers).sum();
		if(sum>0) {
			return Arrays.stream(numbers).mapToDouble(number -> ((float)number/(float)sum) * 100.0).toArray();
		}else {
			return null;
		}
	}
	
	private double[] calculatePercentageGivenAListOfNumbersTwoDecimals(int[] numbers) {
		
		DecimalFormat df = new DecimalFormat("#.##");      
		int sum = Arrays.stream(numbers).sum();
		if(sum>0) {
			return Arrays.stream(numbers).mapToDouble(number -> Double.valueOf(df.format(((float)number/(float)sum) * 100.0))).toArray();
		}else {
			return null;
		}
	}

	protected boolean checkTeamPlayerJerseyAvailability(String teamId, String jerseyNo, int clubId) throws Exception {
		
		String query = "SELECT player_id, jersey_number FROM mcc.player_view pv WHERE pv.club_id = ? " 
					+ " AND player_id IN(SELECT player_id FROM team_player WHERE team_id = ?) AND jersey_number = ?";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, CommonUtility.stringToInt(teamId));
			st.setInt(3,CommonUtility.stringToInt(jerseyNo));
			
			rs = st.executeQuery();
			while (rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return true;
	}
	
	protected boolean checkTeamPlayerJerseyAvailabilityForUpdation(String teamId, String jerseyNo, int clubId, int playerId, int seasonYear) throws Exception {
		
		String query = "SELECT pv.player_id, pv.jersey_number FROM mcc.player_view pv " + 
				"left join mcc.player_additional pa on pa.player_id = pv.player_id and pa.club_id = pv.club_id " + 
				"WHERE pv.club_id = ? " + 
				"AND pv.player_id IN(SELECT player_id FROM team_player WHERE team_id = ?) AND pv.jersey_number = ? and ( pv.player_id <> ? or pa.season_year = ? ) ";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getConnection(clubId);
			st = conn.prepareStatement(query);
			st.setInt(1, clubId);
			st.setInt(2, CommonUtility.stringToInt(teamId));
			st.setInt(3,CommonUtility.stringToInt(jerseyNo));
			st.setInt(4, playerId);
			st.setInt(5, seasonYear);
			
			rs = st.executeQuery();
			while (rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return true;
	}
	
	protected void saveUpdatePlayerAdditional(PlayerAdditionalDto player, int playerId, int clubId) throws Exception {
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		Connection conn = null;
		try {

			conn = DButility.getDefaultConnection();

			// st.executeUpdate(query);
			String query = "update player_additional set resident_id = ?,passport_num=?,"
					+ "passport_expiry=?,ksa_firstentry=?,ksa_born=?,club_id=?, id_type=?, id_number=?, id_expiry_date=?, id_proof_path=?, "
					+ " first_entry_date=? , no_of_years_stay = ?, season_year=?, nationality=? where player_id = ?";

			pst1 = conn.prepareStatement(query);
			pst1.setString(1, player.getResidentId());
			pst1.setString(2, player.getPassportNumber());
			pst1.setString(3, player.getPassportExpiry());
			pst1.setString(4, player.getKsaFirstEntry());
			pst1.setString(5, player.getKsaBorn());
			pst1.setInt(6, clubId);
			pst1.setString(7, player.getIdType());
			pst1.setString(8, player.getIdNumber());
			pst1.setString(9, player.getIdExpiryDate());
			pst1.setString(10, player.getIdProofPath());
			pst1.setString(11,player.getFirstEntryDate());
			pst1.setString(12, player.getNoOfYearsStay());
			pst1.setInt(13, player.getSeasonYear());
			pst1.setString(14, player.getNationality());
			pst1.setInt(15, player.getPlayerId());
			
			if (pst1.executeUpdate() == 0) {

				String playerAdditionalQuery = "insert into player_additional(player_id,resident_id,"
						+ "passport_num,passport_expiry,"
						+ " ksa_firstentry,ksa_born,club_id, id_type, id_number, id_expiry_date, id_proof_path, first_entry_date,"
						+ " no_of_years_stay, season_year,nationality) "
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				pst = conn.prepareStatement(playerAdditionalQuery);
				pst.setInt(1, playerId);
				pst.setString(2, player.getResidentId());
				pst.setString(3, player.getPassportNumber());
				pst.setString(4, player.getPassportExpiry());
				pst.setString(5, player.getKsaFirstEntry());
				pst.setString(6, player.getKsaBorn());
				pst.setInt(7, clubId);
				pst.setString(8, player.getIdType());
				pst.setString(9, player.getIdNumber());
				pst.setString(10, player.getIdExpiryDate());
				pst.setString(11, player.getIdProofPath());
				pst.setString(12, player.getFirstEntryDate());
				pst.setString(13, player.getNoOfYearsStay());
				pst.setInt(14, player.getSeasonYear());
				pst.setString(15, player.getNationality());
				
				pst.executeUpdate();
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(pst1);
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected void saveBcmclWaiverData(int clubId, String data, String playerId) throws Exception {
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			String insertQuery = "insert into mcc.bcmcl_waiver(player_id, waiver_data, status) values(?,?,?);";
			
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(insertQuery);
			pst.setString(1,  playerId);
			pst.setString(2, data);
			pst.setInt(3, 0);
			
			pst.executeUpdate();
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected List<BcmclWaiverRecord> getBcmclWaiverRecords(int clubId, String playerId) throws Exception {
		List<BcmclWaiverRecord> recordList = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String selectQuery = "select id, create_time, player_id, waiver_data, status, year(create_time) data_year from mcc.bcmcl_waiver where player_id = ? ;";
			conn = DButility.getDefaultConnection();
			
			st = conn.prepareStatement(selectQuery);
			st.setInt(1, Integer.parseInt(playerId));
			
			rs = st.executeQuery();
			recordList = new ArrayList<BcmclWaiverRecord>();
			while (rs.next()) {
				BcmclWaiverRecord record = new BcmclWaiverRecord();
				record.setId(rs.getInt("id"));
				record.setPlayerId(rs.getString("player_id"));
				record.setData(rs.getString("waiver_data"));
				record.setStatus(rs.getInt("status"));
				record.setCreationDate(rs.getString("create_time"));
				record.setYear(rs.getString("data_year"));
				recordList.add(record);
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return recordList;
	}

	protected void adminUpdateBcmclWaiverData(int clubId, String data, String playerWaiverId) throws Exception {
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			String updateQuery = "update mcc.bcmcl_waiver set waiver_data = ?, status = ? where id = ?;";
			
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(updateQuery);
			pst.setString(1, data);
			pst.setInt(2, 1);
			pst.setString(3,  playerWaiverId);
			
			pst.executeUpdate();
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected List<BcmclWaiverRecord> getBcmclWaiverRecordsByStatus(int clubId, int status, int internalClubId) throws Exception {
		List<BcmclWaiverRecord> recordList = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String selectQuery = "select bw.id, bw.create_time, bw.player_id, bw.waiver_data, bw.status, year(bw.create_time) data_year, "
					+ " concat(pv.f_name, ' ', pv.l_name) player_name  from mcc.bcmcl_waiver bw left join mcc.player_view pv on bw.player_id = pv.player_id "
					+ " JOIN club"+clubId+".club_player cp ON pv.player_id = cp.player_id where bw.status = ? and pv.club_id = ? AND cp.internal_club_id = ?;";
			conn = DButility.getDefaultConnection();
			
			st = conn.prepareStatement(selectQuery);
			st.setInt(1, status);
			st.setInt(2, clubId);
			st.setInt(3, internalClubId);
			
			rs = st.executeQuery();
			recordList = new ArrayList<BcmclWaiverRecord>();
			while (rs.next()) {
				BcmclWaiverRecord record = new BcmclWaiverRecord();
				record.setId(rs.getInt("id"));
				record.setPlayerId(rs.getString("player_id"));
				record.setData(rs.getString("waiver_data"));
				record.setStatus(rs.getInt("status"));
				record.setCreationDate(rs.getString("create_time"));
				record.setYear(rs.getString("data_year"));
				record.setPlayerName(rs.getString("player_name"));
				recordList.add(record);
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return recordList;
	}

	protected BcmclWaiverRecord getBcmclWaiverRecordById(int clubId, String playerId, String recordId) throws Exception {
		BcmclWaiverRecord record = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String selectQuery = "select bw.id, bw.create_time, bw.player_id, bw.waiver_data, bw.status, year(bw.create_time) data_year, "
					+ " concat(pv.f_name, ' ', pv.l_name) player_name  from mcc.bcmcl_waiver bw left join mcc.player_view pv on bw.player_id = pv.player_id "
					+ " where bw.id = ? and bw.player_id = ? and pv.club_id = ?;";
			conn = DButility.getDefaultConnection();
			
			st = conn.prepareStatement(selectQuery);
			st.setString(1, recordId);
			st.setString(2, playerId);
			st.setInt(3, clubId);
			
			rs = st.executeQuery();
			while (rs.next()) {
				record = new BcmclWaiverRecord();
				record.setId(rs.getInt("id"));
				record.setPlayerId(rs.getString("player_id"));
				record.setData(rs.getString("waiver_data"));
				record.setStatus(rs.getInt("status"));
				record.setCreationDate(rs.getString("create_time"));
				record.setYear(rs.getString("data_year"));
				record.setPlayerName(rs.getString("player_name"));
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return record;
	}

	public List<PlayerDto> findUserByNameAndEmail(String fName, String lName, String email, int clubId) throws Exception {
		
		String query = " Select f_name, l_name, email, player_id, is_active, accepted_terms "
				+ " from mcc.player_view  where email= ? and f_name = ? and l_name = ? ";
		
		if(clubId != 0){
			query += "and u.club_id = ?";
		}
		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		try {
			st = conn.prepareStatement(query);
			int index =1;
			st.setString(index++, email);
			st.setString(index++, fName);
			st.setString(index++, lName);
			if(clubId != 0){
				st.setInt(index++, clubId);
			}
			rs = st.executeQuery();
			while (rs.next()) {
				PlayerDto player = new PlayerDto();
				player.setFirstName(rs.getString("f_name"));
				player.setLastName(rs.getString("l_name"));
				player.setEmail(rs.getString("email"));
				player.setPlayerID(rs.getInt("player_id"));
				player.setIsActive(rs.getString("is_active"));
				player.setAcceptedTerms((rs.getInt("accepted_terms") == 1));
				players.add(player);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}

	public List<Integer> getMinimumMatchesToPlayForPlayer(int leagueId,int teamid1,int teamid2, int minMatchesToPlay, int clubId) throws Exception {
		// TODO Auto-generated method stub
		List<Integer> players = new ArrayList();
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
//		String query="SELECT tp.player_id, COUNT(DISTINCT(mpt.match_id)) no_of_matches "
//				+ "FROM team_player tp "
//				+ "LEFT JOIN match_player_team mpt ON mpt.player_id = tp.player_id "
//				+ "LEFT JOIN matches m ON mpt.match_id = m.match_id AND m.match_type = 'l' "
//				+ "WHERE tp.team_id IN (?, ?) "
//				+ "GROUP BY tp.player_id HAVING COUNT(DISTINCT(mpt.match_id))<? ";
			String query ="SELECT mpt.player_id, COUNT(DISTINCT(mpt.match_id)) no_of_matches "
					+ "FROM match_player_team mpt JOIN matches m ON mpt.match_id = m.match_id "
					+ "AND m.match_type = 'l' WHERE mpt.team_id IN (?, ?) GROUP BY mpt.player_id HAVING COUNT(DISTINCT(mpt.match_id))>=?";
		
		conn = DButility.getConnection(clubId);
		
		st = conn.prepareStatement(query);
		st.setInt(1, teamid1);
		st.setInt(2, teamid2);
		st.setInt(3, minMatchesToPlay);
		
		rs = st.executeQuery();
		while(rs.next())
		{
			players.add(rs.getInt("player_id"));
		}
		
		
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return players;
	}

	protected int checkPlayerExistsInSeries(int playerID, int captainSeries, int clubId) {
		// TODO Auto-generated method stub
		
		String query = "select count(*) as count from team_player tp, team t where tp.team_id = t.team_id and tp.player_id =? and t.league = ?";
		
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		int count = 0;

		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, playerID);
			pst.setInt(2, captainSeries);
			rs = pst.executeQuery();
			while(rs.next())
			{
			count = rs.getInt("count");
			}
			
		} catch (SQLException e) {
			return 0;
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return count;
	}
	
	
	protected int checkPlayerExistsInSeries(int playerID, String captainSeries, int clubId) {
		// TODO Auto-generated method stub
		
		String query = "select count(*) as count from team_player tp, team t where tp.team_id = t.team_id and tp.player_id =? and t.league in ("+captainSeries+")";
		
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		int count = 0;

		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, playerID);

			rs = pst.executeQuery();
			while(rs.next())
			{
			count = rs.getInt("count");
			}
			
		} catch (SQLException e) {
			return 0;
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return count;
	}

	protected int checkSimilarJerseyOman(String jerseyNo, int clubId) throws Exception {
		// TODO Auto-generated method stub
		String query = "select count(*) as count from mcc.player_view where jersey_number = ? AND club_id = ? ";

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		int count = 0;

		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, jerseyNo);
			pst.setInt(2, clubId);
			rs = pst.executeQuery();
			while(rs.next()){
				count = rs.getInt("count");
			}

		} catch (SQLException e) {
			return 0;
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return count;
	}

	protected int getPlayerAddtionalyEmailForOman(String email) {
		// TODO Auto-generated method stub
		String query = "select count(*) as count from mcc.player_additional where admin_email = ? ";

		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int count = 0;

		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, email);
			
			rs = pst.executeQuery();
			while(rs.next()){
				count = rs.getInt("count");
			}

		} catch (SQLException e) {
			return 0;
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return count;
	}
	
	protected Map<Integer, String> getCustomIdFromPlayerAdditional() throws Exception {

		String query = "select p.player_id,pa.id_number from mcc.player p,mcc.player_additional pa where p.player_id=pa.player_id";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<Integer, String> map = new HashMap<>();
		try {
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				map.put(rs.getInt("player_id"), rs.getString("id_number"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return map;
	}

	
//	protected String getCustomIdFromPlayerAdditionalForplayer(String playerId) throws Exception {
//		String query = "select id_number from mcc.player_additional where player_id= ?";
//		Connection conn = null;
//		PreparedStatement st = null;
//		ResultSet rs = null;
//		String idNumber = "";
//		try {
//			conn = DButility.getDefaultConnection();
//			st = conn.prepareStatement(query);
//			st.setString(1, playerId);
//			rs = st.executeQuery();
//			while (rs.next()) {
//				idNumber = rs.getString("id_number");
//			}
//		} catch (SQLException e) {
//			throw new Exception(e.getMessage());
//		} finally {
//			DButility.dbCloseAll(conn, st, rs);
//		}
//		return idNumber;
//	}
}
