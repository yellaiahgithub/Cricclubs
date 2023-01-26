package com.cricket.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.UserAttemptsAqDto;
import com.cricket.utility.DButility;

public class UserAttemptsAqDAO {

	Logger log = LoggerFactory.getLogger(UserAttemptsAqDAO.class);
	
	protected int insertUserAttemptsAq(UserAttemptsAqDto userAttemptsAqDto, int clubId) throws Exception {
		String query="insert into user_attempts_aq(user_id,assessment_quiz_id,no_of_questions_answerred,time_duration,no_of_correct_answers,pass_or_fail,attempt_date) values (?,?,?,?,?,?,NOW())";
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		int userAttemptsAqId=0;
		try {
			st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

			
			st.setInt(1,userAttemptsAqDto.getUserId());
			st.setInt(2, userAttemptsAqDto.getAssessmentQuizId());
			st.setInt(3, userAttemptsAqDto.getNoOfQnsAnswerred());
			st.setInt(4, userAttemptsAqDto.getTimeDurration());
			st.setInt(5, userAttemptsAqDto.getNoOfCorrectAns());
			st.setInt(6, userAttemptsAqDto.getPassOrFail());
				
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			
			if (rs.next()) {
				userAttemptsAqId = rs.getInt(1);
			}	
			
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		return userAttemptsAqId;
	}
	
	protected List<UserAttemptsAqDto> getUserAttemptsAqAll(int assessmentQuizId,int clubId) throws Exception {
		List<UserAttemptsAqDto> userAttemptsAqDtosList= new ArrayList<UserAttemptsAqDto>();
		String query="select * from user_attempts_aq where assessment_quiz_id=?";
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, assessmentQuizId);
			
			rs=st.executeQuery();
			
			while(rs.next()) {
				UserAttemptsAqDto userAttemptsAqDto=new UserAttemptsAqDto();
				
				userAttemptsAqDto.setUserAttemptsAqId(rs.getInt("user_attempts_aq_id"));
				userAttemptsAqDto.setUserId(rs.getInt("user_id"));
				userAttemptsAqDto.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
				userAttemptsAqDto.setNoOfQnsAnswerred(rs.getInt("no_of_questions_answerred"));
				userAttemptsAqDto.setTimeDurration(rs.getInt("time_duration"));
				userAttemptsAqDto.setNoOfCorrectAns(rs.getInt("no_of_correct_answers"));
				userAttemptsAqDto.setPassOrFail(rs.getInt("pass_or_fail"));	
				userAttemptsAqDto.setAttemptDate(rs.getDate("attempt_date"));
			
			userAttemptsAqDtosList.add(userAttemptsAqDto);
			}
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
				
		return userAttemptsAqDtosList;
		
	}
	
	protected UserAttemptsAqDto getUserAttemptsAqByUserAttemptAqId(int userAttemptAqId,int clubId) throws Exception {
		UserAttemptsAqDto userAttemptsAqDto=new UserAttemptsAqDto();
		String query="select * from user_attempts_aq where user_attempts_aq_id=?";
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, userAttemptAqId);
			
			rs=st.executeQuery();
			
			while(rs.next()) {
				
				
				userAttemptsAqDto.setUserAttemptsAqId(rs.getInt("user_attempts_aq_id"));
				userAttemptsAqDto.setUserId(rs.getInt("user_id"));
				userAttemptsAqDto.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
				userAttemptsAqDto.setNoOfQnsAnswerred(rs.getInt("no_of_questions_answerred"));
				userAttemptsAqDto.setTimeDurration(rs.getInt("time_duration"));
				userAttemptsAqDto.setNoOfCorrectAns(rs.getInt("no_of_correct_answers"));
				userAttemptsAqDto.setPassOrFail(rs.getInt("pass_or_fail"));	
				userAttemptsAqDto.setAttemptDate(rs.getDate("attempt_date"));
			
			
			}
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
				
		return userAttemptsAqDto;
		
	}
	
	protected List<UserAttemptsAqDto>  getUserAttemptsAq(int userId, int clubId,int assessmentId) throws Exception {
		List<UserAttemptsAqDto> userAttemptsAqList= new ArrayList<>();
		
		String query="select * from user_attempts_aq where user_id=? ";
		
		if(assessmentId>0) {
			query += " and assessment_quiz_id=? ";
		}
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, userId);
			if(assessmentId>0) {
				st.setInt(2, assessmentId);
			}
			rs=st.executeQuery();
			
			while(rs.next()) {
				UserAttemptsAqDto userAttemptsAqDto=new UserAttemptsAqDto();
				
				userAttemptsAqDto.setUserAttemptsAqId(rs.getInt("user_attempts_aq_id"));
				userAttemptsAqDto.setUserId(rs.getInt("user_id"));
				userAttemptsAqDto.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
				userAttemptsAqDto.setNoOfQnsAnswerred(rs.getInt("no_of_questions_answerred"));
				userAttemptsAqDto.setTimeDurration(rs.getInt("time_duration"));
				userAttemptsAqDto.setNoOfCorrectAns(rs.getInt("no_of_correct_answers"));
				userAttemptsAqDto.setPassOrFail(rs.getInt("pass_or_fail"));	
				userAttemptsAqDto.setAttemptDate(rs.getDate("attempt_date"));
			
				userAttemptsAqList.add(userAttemptsAqDto);
			
			}
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
				
		return userAttemptsAqList;
	}
	
	
	protected void updateUserAttemptsAq(UserAttemptsAqDto userAttemptsAqDto, int clubId) throws Exception{
		String query="update user_attempts_aq set user_id=?,assessment_quiz_id=?,no_of_questions_answerred=?,time_duration=?,no_of_correct_answers=?,pass_or_fail=?,attempt_date=NOW() where user_attempts_aq_id=?";
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		
		try {
			st=conn.prepareStatement(query);
			
			st.setInt(1,userAttemptsAqDto.getUserId());
			st.setInt(2, userAttemptsAqDto.getAssessmentQuizId());
			st.setInt(3, userAttemptsAqDto.getNoOfQnsAnswerred());
			st.setInt(4, userAttemptsAqDto.getTimeDurration());
			st.setInt(5, userAttemptsAqDto.getNoOfCorrectAns());
			st.setInt(6, userAttemptsAqDto.getPassOrFail());
			//st.setDate(7, (Date) userAttemptsAqDto.getAttemptDate());	
			
			st.setInt(8, userAttemptsAqDto.getUserAttemptsAqId());
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	
	
	}
	
	protected void deleteUserAttemptsAq(int userAttemptsAqId, int clubId) throws Exception {
		String query="delete from user_attempts_aq where user_attempts_aq_id=?";
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st= null;
		
		try {
			st= conn.prepareStatement(query);
			st.setInt(1, userAttemptsAqId);
			st.executeUpdate();
			
		} catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	protected int getCountOfAssessmentAttempts(int assessmentQuizId,int clubId) throws Exception {
		String query="select count(*) from user_attempts_aq where assessment_quiz_id=?";
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		int count=0;
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, assessmentQuizId);
			
			rs=st.executeQuery();
			while(rs.next()) {
				count=rs.getInt("count(*)");
			}
			
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		return count;
	}
	
	protected List<UserAttemptsAqDto> getUserPassedAttempts(int userId, int clubId) throws Exception {
		List<UserAttemptsAqDto> userAttemptsAqList= new ArrayList<>();
		
		
		String query="select * from user_attempts_aq where user_id=? and  pass_or_fail=1";
		
		
		
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, userId);
			
			rs=st.executeQuery();
			
			while(rs.next()) {
				UserAttemptsAqDto userAttemptsAqDto=new UserAttemptsAqDto();
				userAttemptsAqDto.setUserAttemptsAqId(rs.getInt("user_attempts_aq_id"));
				
				userAttemptsAqDto.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
				
				userAttemptsAqDto.setAttemptDate(rs.getDate("attempt_date"));
				
				userAttemptsAqDto.setClubId(clubId);
				
		userAttemptsAqList.add(userAttemptsAqDto);	
			
			}
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
				
		return userAttemptsAqList;
	}
	
	
	
}
