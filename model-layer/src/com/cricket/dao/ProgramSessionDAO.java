/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.beans.AcademySessionBean;
import com.cricket.dto.AcademyPlayerReportDto;
import com.cricket.dto.LiveProgramSessionDto;
import com.cricket.dto.ProgramSessionDto;
import com.cricket.dto.SessionPlayerDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class ProgramSessionDAO {
	private static Logger log = LoggerFactory.getLogger(ProgramSessionDAO.class);
	

	public int createSessionAndAddPlayer(SessionPlayerDto sessionPlayerDto, int clubId, String user) throws Exception {

		String query = "insert into program_session(program_id,created_by, scorer, batch_id) values (?,?,?,?)";	

		String querySessionPlayer = "insert into session_player(session_id,player_id) values (?,?)";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st1 = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		try {
			st1 = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
			int index = 1;
			
			st1.setInt(index++, sessionPlayerDto.getProgramId());
			st1.setString(index++, user);
			st1.setString(index++, user);
			st1.setInt(index++, sessionPlayerDto.getBatchId());
			st1.executeUpdate();
			
			rs = st1.getGeneratedKeys();
			int sessionId = 0;			
			if (rs.next()) {
				sessionId = rs.getInt(1);
			}
			pst = conn.prepareStatement(querySessionPlayer);
			List<Integer> players = sessionPlayerDto.getPlayerIds();
			if(players != null && !players.isEmpty()) {
				for (int i = 0; i < players.size(); i++) {
					pst.setInt(1, sessionId);
					pst.setInt(2, players.get(i));
					pst.addBatch();				
				}
			}
			pst.executeBatch();
			
			return sessionId;

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(pst);
			DButility.dbCloseAll(conn, st1, rs);
		}
	}


	public SessionPlayerDto getSessionInfo(int sessionId, int clubId, String restId) throws Exception {
		
		String query = "select sp.session_id, ps.program_id, sp.player_id, ps.batch_id "
				+ "	from  session_player sp, program_session ps where  ps.session_id = sp.session_id and sp.session_id = ? ";

		SessionPlayerDto sessionPlayerDto = new SessionPlayerDto();
		List<Integer> playerIds = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setInt(1, sessionId);
			rs = pst.executeQuery();
			int programId = 0, batchId = 0; 
			while (rs.next()) {
				playerIds.add(rs.getInt("player_id"));
				if(programId <= 0) {
					programId = rs.getInt("program_id");
				}
				if(batchId <= 0) {
					batchId = rs.getInt("batch_id");
				}
			}
			sessionPlayerDto.setSessionId(sessionId);
			sessionPlayerDto.setProgramId(programId);
			sessionPlayerDto.setPlayerIds(playerIds);
			sessionPlayerDto.setBatchId(batchId);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return sessionPlayerDto;
	
	}

	public List<ProgramSessionDto> getProgramSessions(int programId, int sessionId, int clubId, String isComplete,
			String restId) throws Exception {
		return getProgramSessions(programId, sessionId, clubId, isComplete, restId, 0,0, false);

	}
	
public List<ProgramSessionDto> getProgramSessions(int programId, int sessionId, int clubId, 
		String isComplete, String restId,int batchId, int limit, boolean forScoring) throws Exception {
		
	String query = "select ps.session_id, ps.program_id, p.name program_name, p.uiid, ps.create_time, DATE(ps.create_time) create_date, "
			+ " DATE_FORMAT(create_time,'%l %p') session_time, "
			+ " IF((ps.start_time != '' && ps.start_time IS NOT NULL),ps.start_time,(SELECT start_time FROM program_batch WHERE id = ps.batch_id)) startTime, " 
			+ " IF((ps.end_time != '' && ps.end_time IS NOT NULL),ps.end_time,(SELECT end_time FROM program_batch WHERE id = ps.batch_id)) endTime, "
			+ " (select concat(u.f_name,' ',u.l_name) from mcc.user u where user_id = ps.created_by) created_by, "
			+ " (select concat(u.f_name,' ',u.l_name) from mcc.user u where user_id = ps.scorer) scorer_name, "
			+ " (select count(*) from session_player where session_id = ps.session_id) no_of_players, "
			+ " (SELECT CONCAT(u.f_name,' ',u.l_name) FROM mcc.academy_coach_view u WHERE u.coach_id = p.coach_id " 
			+ " AND u.user_type = 'coach' AND u.club_id = "+clubId+") coach_name, ps.is_complete "
			+ " from program_session ps, program p where ps.program_id = p.id and 1=1 ";	
			
		if(programId>0) {
			query = query+" and ps.program_id = "+programId;
		}
		
		if(sessionId>0) {
			query = query+" and ps.session_id = "+sessionId;
		}
		if (batchId > 0) {
			query = query + " and ps.batch_id = " + batchId;
		}
		if(forScoring == true) {
			query = query+" and ps.is_complete = 0 and ps.create_time >= CURRENT_DATE";
		}
		if(!CommonUtility.isNullOrEmpty(isComplete)) {
			query = query+" and ps.is_complete = "+isComplete;
		}
		if(forScoring == true) {
			query = query+" order by ps.create_time ";
		}
		if(limit>0) {
			query = query+" limit "+ limit;
		}

		List<ProgramSessionDto> programSessions = new ArrayList<ProgramSessionDto>();		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);						
			rs = pst.executeQuery();			 
			while (rs.next()) {
				
				ProgramSessionDto programSessionInfo = new ProgramSessionDto();
				
				programSessionInfo.setSessionId(rs.getInt("session_id"));
				programSessionInfo.setProgramId(rs.getInt("program_id"));
				programSessionInfo.setProgramName(rs.getString("program_name"));
				programSessionInfo.setCoverPhotoPath(rs.getString("uiid"));
				programSessionInfo.setScorer(rs.getString("scorer_name"));
				programSessionInfo.setCreatedBy(rs.getString("created_by"));
				programSessionInfo.setCreateTime((rs.getDate("create_time")));	
				programSessionInfo.setNoOfPlayers(rs.getInt("no_of_players"));	
				programSessionInfo.setCoachName(rs.getString("coach_name"));
				programSessionInfo.setSessionTime(rs.getString("session_time"));
				programSessionInfo.setStartTime(rs.getString("startTime"));
				programSessionInfo.setEndTime(rs.getString("endTime"));
				programSessionInfo.setFixedFormatDate(rs.getString("create_date"));
				programSessionInfo.setIsComplete(rs.getString("is_complete"));
				
				programSessions.add(programSessionInfo);
			}			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return programSessions;	
	}

public List<LiveProgramSessionDto> getLiveProgramSessions(int clubId, String userType, int userTypeId, int limit) throws Exception {
		
	String query = "select ps.session_id, ps.program_id, p.name program_name, p.uiid, ps.create_time, DATE(ps.create_time) create_date, "
			+ " DATE_FORMAT(create_time,'%l %p') session_time, pb.id batch_id, pb.name batch_name, ps.scorer scorer_id, "
			+ " IF((ps.start_time != '' && ps.start_time IS NOT NULL),ps.start_time,(SELECT start_time FROM program_batch WHERE id = ps.batch_id)) startTime, " 
			+ " IF((ps.end_time != '' && ps.end_time IS NOT NULL),ps.end_time,(SELECT end_time FROM program_batch WHERE id = ps.batch_id)) endTime, "
			+ " (select concat(u.f_name,' ',u.l_name) from mcc.user u where user_id = ps.created_by) created_by, "
			+ " (select concat(u.f_name,' ',u.l_name) from mcc.user u where user_id = ps.scorer) scorer_name, "
			+ " (select count(*) from session_player where session_id = ps.session_id) no_of_players, "
			+ " (SELECT CONCAT(u.f_name,' ',u.l_name) FROM mcc.academy_coach_view u WHERE u.coach_id = p.coach_id " 
			+ " AND u.user_type = 'coach' AND u.club_id = "+clubId+") coach_name, ps.is_complete "
			+ " from program_session ps, program p, program_batch pb where ps.program_id = p.id "
			+ " and ps.program_id = pb.program_id and ps.batch_id = pb.id and p.id = pb.program_id "
			+ " and ps.is_complete = 0 and ps.create_time >= CURRENT_DATE ";
	
		if("coach".equalsIgnoreCase(userType)) {
			query = query+" and p.selected_coaches like ('%"+userTypeId+"%') ";
			
		}else if("staff".equalsIgnoreCase(userType)) {
			query = query+" and p.id in ( select program_id from program_staff where staff_id = "+userTypeId+" ) ";
		}
			
		query = query+" order by ps.create_time ";
		if(limit>0) {
			query = query+" limit "+ limit;
		}

		List<LiveProgramSessionDto> programSessions = new ArrayList<LiveProgramSessionDto>();		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);						
			rs = pst.executeQuery();			 
			while (rs.next()) {
				
				LiveProgramSessionDto programSessionInfo = new LiveProgramSessionDto();
				
				programSessionInfo.setSessionId(rs.getInt("session_id"));
				programSessionInfo.setProgramId(rs.getInt("program_id"));
				programSessionInfo.setProgramName(rs.getString("program_name"));
				programSessionInfo.setCoverPhotoPath(rs.getString("uiid"));
				programSessionInfo.setScorer(rs.getString("scorer_name"));
				programSessionInfo.setScorerId(rs.getInt("scorer_id"));
				programSessionInfo.setCreatedBy(rs.getString("created_by"));
				programSessionInfo.setCreateTime((rs.getDate("create_time")));	
				programSessionInfo.setNoOfPlayers(rs.getInt("no_of_players"));	
				programSessionInfo.setCoachName(rs.getString("coach_name"));
				programSessionInfo.setSessionTime(rs.getString("session_time"));
				programSessionInfo.setStartTime(rs.getString("startTime"));
				programSessionInfo.setEndTime(rs.getString("endTime"));
				programSessionInfo.setFixedFormatDate(rs.getString("create_date"));
				programSessionInfo.setIsComplete(rs.getString("is_complete"));
				programSessionInfo.setBatchId(rs.getInt("batch_id"));
				programSessionInfo.setBatchName(rs.getString("batch_name"));
				
				programSessions.add(programSessionInfo);
			}			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return programSessions;	
	}

	public ProgramSessionDto getProgramSessionInfo(int sessionId, int clubId, String restId) throws Exception {
		String query = "select ps.session_id, ps.program_id, p.name program_name, DATE(ps.create_time) create_time, "
				+ " DATE_FORMAT(create_time,'%l %p') session_time, "
				+ " IF((ps.start_time != '' && ps.start_time IS NOT NULL),ps.start_time,(SELECT start_time FROM program_batch WHERE id = ps.batch_id)) startTime, " 
				+ " IF((ps.end_time != '' && ps.end_time IS NOT NULL),ps.end_time,(SELECT end_time FROM program_batch WHERE id = ps.batch_id)) endTime, "
				+ " (select concat(u.f_name,' ',u.l_name) from mcc.user u where user_id = ps.created_by) created_by, "
				+ " (select concat(u.f_name,' ',u.l_name) from mcc.user u where user_id = ps.scorer) scorer_name, "
				+ " (select count(*) from session_player where session_id = ps.session_id) no_of_players, "
				+ " (SELECT CONCAT(u.f_name,' ',u.l_name) FROM mcc.academy_coach_view u WHERE u.coach_id = p.coach_id "
				+ " AND u.user_type = 'coach' AND u.club_id = " + clubId + ") coach_name "
				+ " from  program_session ps, program p where ps.program_id = p.id and 1=1 ";
		if (sessionId > 0) {
			query = query + " and ps.session_id = ? ";
		}
		ProgramSessionDto programSessionInfo = new ProgramSessionDto();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			if (sessionId > 0) {
				pst.setInt(1, sessionId);
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				programSessionInfo.setSessionId(rs.getInt("session_id"));
				programSessionInfo.setProgramId(rs.getInt("program_id"));
				programSessionInfo.setProgramName(rs.getString("program_name"));
				programSessionInfo.setScorer(rs.getString("scorer_name"));
				programSessionInfo.setCreatedBy(rs.getString("created_by"));
				programSessionInfo.setCreateTime(rs.getDate("create_time"));
				programSessionInfo.setNoOfPlayers(rs.getInt("no_of_players"));
				programSessionInfo.setCoachName(rs.getString("coach_name"));
				programSessionInfo.setSessionTime(rs.getString("session_time"));
				programSessionInfo.setStartTime(rs.getString("startTime"));
				programSessionInfo.setEndTime(rs.getString("endTime"));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return programSessionInfo;
	}

	public AcademyPlayerReportDto getPlayerReportByPlayerId(int playerId, int clubId, String fromDate, 
			String toDate, int programId, int batchId) throws Exception {
		String query = "SELECT uv.player_id, CONCAT(f_name,' ',l_name) playerName,pp.program_id," 
				+ " (SELECT NAME FROM program WHERE id = ?) programName, " 
				+ " pp.batch_id,(SELECT NAME FROM program_batch WHERE id = pp.batch_id AND program_id = ?) batchName, " 
				+ " uv.profile_image_path, (SELECT COUNT(*) FROM program_session ps WHERE ps.program_id = ? " 
				+ " AND ps.batch_id = ? AND DATE(create_time) <= '"+toDate+"') sessionCount, COUNT(*) totalPresents " 
				+ " FROM mcc.user_view uv, program_player pp, attendance a, program_session ps " 
				+ " WHERE uv.player_id = pp.player_id AND uv.user_id = a.user_id " 
				+ " AND a.batch_id = pp.batch_id AND pp.batch_id = ps.batch_id AND pp.program_id = ps.program_id AND pp.batch_id = ? " 
				+ " AND pp.program_id = ? AND uv.player_id = ? AND uv.club_id = ? " 
				+ " AND uv.player_id IS NOT NULL AND a.created_date = ps.create_time AND a.created_date >= '"+fromDate+"' AND a.created_date <= '"+toDate+"';";
		
		
		AcademyPlayerReportDto dto = new AcademyPlayerReportDto();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getReadConnection(clubId);
			pst = conn.prepareStatement(query);
			pst.setInt(1, programId);
			pst.setInt(2, programId);
			pst.setInt(3, programId);
			pst.setInt(4, batchId);
			pst.setInt(5, batchId);
			pst.setInt(6, programId);
			pst.setInt(7, playerId);
			pst.setInt(8, clubId);
			
			rs = pst.executeQuery();
			while (rs.next()) {
				dto.setPlayerId(rs.getInt("player_id"));
				dto.setName(rs.getString("playerName"));
				dto.setProgramName(rs.getString("programName"));
				dto.setBatchName(rs.getString("batchName"));
				dto.setProfileImage(rs.getString("profile_image_path"));
				dto.setSesssionCount(rs.getInt("sessionCount"));
				dto.setDaysPresent(rs.getInt("totalPresents"));
				
				if(dto.getSesssionCount() > 0 ) {
					dto.setDaysAbsent(dto.getSesssionCount()-dto.getDaysPresent());
				}
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return dto;
	}
	
	public String deleteSession(int sessionId, int clubId) throws Exception {
		
		String selectQuery = "select program_id, session_id from program_session where session_id = ? and is_complete = 1 ";

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st1 = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		try {
			st1 = conn.prepareStatement(selectQuery);
			int index = 1;
			st1.setInt(index++, sessionId);
			rs = st1.executeQuery();
			if(rs != null && rs.next() == false) {
				String deleteQuery = "delete from program_session where session_id = ?";
				String deleteQuery1 = "delete from session_player where session_id = ?";
				
				pst = conn.prepareStatement(deleteQuery);
				pst.setInt(1, sessionId);
				pst.executeUpdate();
				
				pst1 = conn.prepareStatement(deleteQuery1);
				pst1.setInt(1, sessionId);
				pst1.executeUpdate();
				
				return "Deleted Successfully";
			}else {
				return "Can't delete completed session.";
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeStatement(pst);
			DButility.closeStatement(pst1);
			DButility.dbCloseAll(conn, st1, rs);
		}
	}

	public boolean editSession(int clubId, int userId, AcademySessionBean sessionBean) throws Exception {
		
		String query = "";
		if(sessionBean.getSessionId() > 0) {
			query = "update program_session set created_by = "+userId+" ";
			if(sessionBean.getScorer() > 0) {
				query +=", scorer = "+sessionBean.getScorer();
			}
			if(!CommonUtility.isNullOrEmptyOrNULL(sessionBean.getLocation())) {
				query += ", location = '"+sessionBean.getLocation()+"' ";
			}
			if(!CommonUtility.isNullOrEmptyOrNULL(sessionBean.getStartTime())) {
				query += ", start_time = '"+sessionBean.getStartTime()+"' ";
			}
			if(!CommonUtility.isNullOrEmptyOrNULL(sessionBean.getEndTime())) {
				query += ", end_time = '"+sessionBean.getEndTime()+"' ";
			}
			if(!CommonUtility.isNullOrEmptyOrNULL(sessionBean.getCreationDate())) {
				query += ", create_time = '"+sessionBean.getCreationDate()+"' ";
			}
			query += "where session_id = "+sessionBean.getSessionId()+";";
		}else {
			query = "insert into program_session(program_id, batch_id, create_time, scorer, location, start_time, end_time) "
					+ " values("+sessionBean.getProgramId()+", "+sessionBean.getBatchId()+",'"+sessionBean.getCreationDate()+"'"
					+ ","+sessionBean.getScorer()+",'"+sessionBean.getLocation()+"','"+sessionBean.getStartTime()+"','"+sessionBean.getEndTime()+"');";
		}

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st1 = null;
		try {
			st1 = conn.prepareStatement(query);
			st1.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st1, null);
		}
	}


	public List<Integer> getActiveAcademies() throws Exception {
		String query = "select club_id, name from mcc.club where is_active = 1 and is_academy=1;";
		
		List<Integer> academyIds = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultConnection();
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				int clubId = rs.getInt("club_id");
				academyIds.add(clubId);	
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return academyIds;
	}


	public Object UpdateSessionScorer(int clubId, int sessionId, int userId) throws Exception {
		String query = "update program_session set scorer = ? where session_id = ?;";

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st1 = null;
		try {
			st1 = conn.prepareStatement(query);
			st1.setInt(1, userId);
			st1.setInt(2, sessionId);
			st1.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st1, null);
		}
	}
	
	public int getUserType(int clubId,String user_type_id,String userType,long programId)
	{
		String tableName = userType.equalsIgnoreCase("coach") ? "program_coach" : userType.equalsIgnoreCase("player") ? "program_player" : "program_staff";
		String columnName = userType.equalsIgnoreCase("coach") ? "coach_id" : userType.equalsIgnoreCase("player") ? "player_id" : "staff_id";
		String query = "select program_id from "+tableName+" where "+columnName+" = "+user_type_id+" and program_id = "+programId;	
		int result = 0 ;
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, ps, null);
		}		
		return result;
	}
	
	
	public void deleteProgramPlayer(int clubId, long programId, long playerId, long batchId){
		String query = "delete from  program_player where program_id = "+programId+" and batch_id ="+batchId+" and player_id ="+playerId;	
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, ps, null);
		}		
	}
}
	

