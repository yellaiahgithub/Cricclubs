package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.PollDto;
import com.cricket.dto.PollOptionDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;


public class PollDAO {
	
	static Logger log = LoggerFactory.getLogger(PollDAO.class);
	
	protected static String POLL_QUERY = "select id,title,poll_desc,image_urls,status,created_by,created_at from poll";

	protected int insertPoll(PollDto pollDto) throws Exception {
		
		String query;
		PreparedStatement st = null;
		PreparedStatement st1 = null;
		Connection conn = null;
		ResultSet rs = null;
		int dbPollId = 0;
		try {
			query = "INSERT INTO poll(title,poll_desc,image_urls,created_by) VALUES (?,?,?,?)";
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			int index = 1;
			st.setString(index++, pollDto.getTitle());
			st.setString(index++, pollDto.getPollDesc());
			st.setString(index++, pollDto.getImageUrlsStr());
			st.setInt(index++, pollDto.getCreatedBy());
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			
			if (rs.next()) {
				dbPollId = rs.getInt(1);
			}
			query = "INSERT INTO poll_options(poll_id,poll_option) VALUES (?,?)";
			st1 = conn.prepareStatement(query);			
			conn.setAutoCommit(false);
			
			for(String option : pollDto.getOptions()) {				
				st1.setInt(1, dbPollId);
				st1.setString(2, option);				
				st1.addBatch();
			}
			st1.executeBatch();			
			conn.commit();		

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeStatement(st1);
			DButility.dbCloseAll(conn, st, rs);
		}
		return dbPollId;
	}
	
	protected void updatePoll(PollDto pollDto, List<String> optionsToRemove, List<String> optionsToAdd) throws Exception {
		
		String query;
		PreparedStatement st = null;
		PreparedStatement st1 = null;
		PreparedStatement st2 = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			query = "update poll set title=?,poll_desc=?,image_urls=?,updated_by=? where id = "+pollDto.getId();
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			
			int index = 1;
			st.setString(index++, pollDto.getTitle());
			st.setString(index++, pollDto.getPollDesc());
			st.setString(index++, pollDto.getImageUrlsStr());
			st.setInt(index++, pollDto.getCreatedBy());
			
			st.executeUpdate();		
			
			if(!CommonUtility.isListNullEmpty(optionsToAdd)) {
				
				query = "INSERT INTO poll_options(poll_id,poll_option) VALUES (?,?)";
				st1 = conn.prepareStatement(query);			
				conn.setAutoCommit(false);
				
				for(String option : optionsToAdd) {
					
					st1.setInt(1, pollDto.getId());
					st1.setString(2, option);
					
					st1.addBatch();
				}
				st1.executeBatch();			
				conn.commit();	
			}
			if(!CommonUtility.isListNullEmpty(optionsToRemove)) {
				
				query = "delete from poll_options where poll_id = ? and poll_option = ?";
				st2 = conn.prepareStatement(query);			
				conn.setAutoCommit(false);
				
				for(String option : optionsToRemove) {
					
					st2.setInt(1, pollDto.getId());
					st2.setString(2, option);
					
					st2.addBatch();
				}
				st2.executeBatch();			
				conn.commit();	
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeStatement(st1);
			DButility.closeStatement(st2);
			DButility.dbCloseAll(conn, st, rs);
		}
	}

	protected void deletePoll(int pollId) throws Exception {
		
		Statement st = null;
		Connection conn = null;
		try {
			conn = DButility.getDefaultConnection();
			st = conn.createStatement();
			conn.setAutoCommit(false);
			
			st.addBatch("DELETE FROM poll_options WHERE poll_id=" + pollId);
			st.addBatch("DELETE FROM poll_votes WHERE poll_id=" + pollId);
			st.addBatch("DELETE FROM poll WHERE id=" + pollId);
			
			st.executeBatch();			
			conn.commit();	

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected List<PollDto> getPolls(int userId, int pollId, String pollIdsStr) throws Exception{
		
		List<PollDto> polls = new ArrayList<PollDto>();
		String query = "select p.id,title,p.poll_desc,p.image_urls,p.created_by,p.created_at,p.status,"
				+ "(select concat(f_name,' ',l_name) from mcc.user where user_id = p.created_by) user_name, "
				+ " (select count(*) from poll_votes where poll_id = p.id) poll_count ";
		if(userId>0) {
			query += ",(select count(*) from poll_votes where poll_id = p.id and user_id = "+userId+") user_poll_count";
		}
			query += " from poll p where 1 = 1 ";
		
		if(pollId>0) {
			query += " and id = "+pollId;
		}
		if(!CommonUtility.isNullOrEmpty(pollIdsStr)) {
			query += " and id in ("+pollIdsStr+") ";
		}
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getDefaultReadConnection();
		st = conn.createStatement();
		rs = st.executeQuery(query);
		
		while(rs.next()){
			
			PollDto pollDto = new PollDto();
			
			pollDto.setId(rs.getInt("id"));
			pollDto.setTitle(rs.getString("title"));
			pollDto.setPollDesc(rs.getString("poll_desc"));
			pollDto.setCreatedBy(rs.getInt("created_by"));
			pollDto.setImageUrlsStr(rs.getString("image_urls"));
			
			if(userId>0) {
				pollDto.setStatus(rs.getInt("user_poll_count")>0?1:0);
			}else {
				pollDto.setStatus(0);
			}
			pollDto.setPollVotes(rs.getInt("poll_count"));
			pollDto.setCreatedUserName(rs.getString("user_name"));
						
			polls.add(pollDto);
		}
		
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return polls;
	}
	
	protected Map<Integer, List<PollOptionDto>> getPollOptionPollIdMap(int userId, int pollId, String pollIdsStr) throws Exception{
		
		Map<Integer, List<PollOptionDto>> pollIdOptionsMap = new HashMap<Integer, List<PollOptionDto>>();
		
		String query = "select id, poll_id, poll_option from poll_options where 1=1 ";
		
		if(userId>0) {
			query += " and poll_id in ( select id from poll where created_by = "+userId +" )";
		}
		if(pollId>0) {
			query += " and poll_id = "+pollId;
		}
		if(!CommonUtility.isNullOrEmpty(pollIdsStr)) {
			query += " and poll_id in ("+pollIdsStr+") ";
		}
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getDefaultReadConnection();
		st = conn.createStatement();
		rs = st.executeQuery(query);
		
		while(rs.next()){
			
			PollOptionDto dto = new PollOptionDto();
			
			dto.setId(rs.getInt("id"));
			dto.setPollId(rs.getInt("poll_id"));
			dto.setPollOption(rs.getString("poll_option"));
			
			List<PollOptionDto> optionList = pollIdOptionsMap.get(dto.getPollId());
			
			if(CommonUtility.isListNullEmpty(optionList)) {
				optionList = new ArrayList<PollOptionDto>();
				pollIdOptionsMap.put(dto.getPollId(), optionList);
			}						
			optionList.add(dto);
		}
		
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return pollIdOptionsMap;
	}
	
	protected List<String> getPollOptionsByPollId(int pollId) throws Exception{
		
		List<String> pollOptions = new ArrayList<String>();
		
		String query = "select poll_option from poll_options where poll_id = "+pollId;
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
		conn = DButility.getDefaultReadConnection();
		st = conn.createStatement();
		rs = st.executeQuery(query);
		
		while(rs.next()){			
			pollOptions.add(rs.getString(1));			
		}
		
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn,st,rs);
		}
		return pollOptions;
	}
	
	protected void addVoteForPoll(int userId, int pollId, int selectedOption) throws Exception {
		
		String query;
		PreparedStatement st = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			query = "INSERT INTO poll_votes VALUES (?,?,?)";
			conn = DButility.getDefaultConnection();
			st = conn.prepareStatement(query);
			
			int index = 1;
			
			st.setInt(index++, userId);
			st.setInt(index++, pollId);
			st.setInt(index++, selectedOption);
			
			st.execute();

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	protected List<PollPercentageDto> getPollOptionVotesCount(List<Integer> pollIds) throws Exception {

		PollPercentageDto pollPercentageDto = null;
		String stringPollIds="";
		for(Integer pollId : pollIds) {
			 stringPollIds += String.valueOf(pollId) +",";
		}
		if(stringPollIds.endsWith(",")) {
			stringPollIds=stringPollIds.substring(0, stringPollIds.length()-1);
		}
		List<PollPercentageDto> pollPercentagelist = new ArrayList<>();
		String query = "select pv.poll_id,option_selected, round((count(*)/(select count(*) from poll_votes where poll_id = pv.poll_id))*100) as poll_percentage from poll_votes pv, poll_options po "
				+ "where pv.poll_id = po.poll_id and po.id = pv.option_selected and pv.poll_id in (" + stringPollIds + ")"
				+ " group by poll_id,option_selected";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DButility.getDefaultReadConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				pollPercentageDto = new PollPercentageDto();
				pollPercentageDto.setPollId(rs.getInt("poll_id"));
				pollPercentageDto.setOptionSelected(rs.getInt("option_selected"));
				pollPercentageDto.setPollsPercentage(rs.getInt("poll_percentage"));
				pollPercentagelist.add(pollPercentageDto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return pollPercentagelist;
	}
}
