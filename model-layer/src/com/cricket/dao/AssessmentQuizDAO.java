package com.cricket.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.AssessmentQuizDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class AssessmentQuizDAO {

	Logger log = LoggerFactory.getLogger(AssessmentQuizDAO.class);
	
	protected void insertAssessmentQuiz(AssessmentQuizDto assessmentQuizDto, int clubId) throws Exception{
		String query="insert into assessment_quiz(assessment_quiz_id,name,type,no_of_questions,min_questions_to_answer,time_duration,status,"
				+ "created_date,updated_date,created_by,updated_by,image_path) values (?,?,?,?,?,?,?,NOW(),NOW(),?,?,?)";
	
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, assessmentQuizDto.getAssessmentQuizId());
			st.setString(2, assessmentQuizDto.getName());
			st.setInt(3, assessmentQuizDto.getType());
			st.setInt(4, assessmentQuizDto.getNoOfQuestions());
			st.setInt(5, assessmentQuizDto.getMinQuestionsToAnswer());
			st.setInt(6, assessmentQuizDto.getTimeDuration());
			st.setInt(7, assessmentQuizDto.getStatus());
			/*
			 * st.setTimestamp(8, assessmentQuizDto.getCreatedDate()); st.setDate(9,
			 * assessmentQuizDto.getUpdatedDate());
			 */
			st.setInt(8, assessmentQuizDto.getCreatedBy());
			st.setInt(9, assessmentQuizDto.getUpdatedBy());
			st.setString(10, assessmentQuizDto.getImagePath());
			
			st.executeUpdate();
			
		} catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}	
		
	}
	
	protected List<AssessmentQuizDto> getAssessmentQuizAll(int clubId,String assessmentIds) throws Exception{
		List<AssessmentQuizDto> assessmentQuizList= new ArrayList<AssessmentQuizDto>();
		String query="select * from assessment_quiz";
		if(!CommonUtility.isNullOrEmpty(assessmentIds)) {
			query+=" where assessment_quiz_id IN ("+assessmentIds+")";
		}
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			
			rs=st.executeQuery();
			
		while(rs.next()) {
			AssessmentQuizDto assessmentQuiz= new AssessmentQuizDto();
			assessmentQuiz.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
			assessmentQuiz.setName(rs.getString("name"));
			assessmentQuiz.setType(rs.getInt("type"));
			assessmentQuiz.setNoOfQuestions(rs.getInt("no_of_questions"));
			assessmentQuiz.setMinQuestionsToAnswer(rs.getInt("min_questions_to_answer"));
			assessmentQuiz.setTimeDuration(rs.getInt("time_duration"));
			assessmentQuiz.setStatus(rs.getInt("status"));
			assessmentQuiz.setCreatedDate(rs.getDate("created_date"));
			assessmentQuiz.setUpdatedDate(rs.getDate("updated_date"));
			assessmentQuiz.setCreatedBy(rs.getInt("created_by"));
			assessmentQuiz.setUpdatedBy(rs.getInt("updated_by"));
			assessmentQuiz.setImagePath(rs.getString("image_path"));
			
			assessmentQuiz.setClubId(clubId);
		assessmentQuizList.add(assessmentQuiz);	
		}
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		
		return assessmentQuizList;
	}
	
	protected List<AssessmentQuizDto> getActiveAssessments(int status,int clubId) throws Exception{
		List<AssessmentQuizDto> assessmentQuizList= new ArrayList<AssessmentQuizDto>();
		String query="select * from assessment_quiz where status=?";
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, status);
			rs= st.executeQuery();
			
		while(rs.next()) {
			AssessmentQuizDto assessmentQuiz= new AssessmentQuizDto();
			assessmentQuiz.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
			assessmentQuiz.setName(rs.getString("name"));
			assessmentQuiz.setType(rs.getInt("type"));
			assessmentQuiz.setNoOfQuestions(rs.getInt("no_of_questions"));
			assessmentQuiz.setMinQuestionsToAnswer(rs.getInt("min_questions_to_answer"));
			assessmentQuiz.setTimeDuration(rs.getInt("time_duration"));
			assessmentQuiz.setStatus(rs.getInt("status"));
			assessmentQuiz.setCreatedDate(rs.getDate("created_date"));
			assessmentQuiz.setUpdatedDate(rs.getDate("updated_date"));
			assessmentQuiz.setCreatedBy(rs.getInt("created_by"));
			assessmentQuiz.setUpdatedBy(rs.getInt("updated_by"));
			assessmentQuiz.setImagePath(rs.getString("image_path"));
			
			assessmentQuiz.setClubId(clubId);
			
		assessmentQuizList.add(assessmentQuiz);	
		}
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		
		return assessmentQuizList;
	}
	
	protected AssessmentQuizDto getAssessmentQuiz(int assessmentQuizId, int clubId) throws Exception{
		
		AssessmentQuizDto assessmentQuiz= new AssessmentQuizDto();
		String query="select * from assessment_quiz where assessment_quiz_id= ?";
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, assessmentQuizId);
			rs= st.executeQuery();			
			
			while(rs.next()) {
				
				assessmentQuiz.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
				assessmentQuiz.setName(rs.getString("name"));
				assessmentQuiz.setType(rs.getInt("type"));
				assessmentQuiz.setNoOfQuestions(rs.getInt("no_of_questions"));
				assessmentQuiz.setMinQuestionsToAnswer(rs.getInt("min_questions_to_answer"));
				assessmentQuiz.setTimeDuration(rs.getInt("time_duration"));
				assessmentQuiz.setStatus(rs.getInt("status"));
				assessmentQuiz.setCreatedDate(rs.getDate("created_date"));
				assessmentQuiz.setUpdatedDate(rs.getDate("updated_date"));
				assessmentQuiz.setCreatedBy(rs.getInt("created_by"));
				assessmentQuiz.setUpdatedBy(rs.getInt("updated_by"));
				assessmentQuiz.setImagePath(rs.getString("image_path"));
				
				assessmentQuiz.setClubId(clubId);
				
			}
			
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}		
		return assessmentQuiz;		
	}
	
	protected void updateAssessmentQuiz(AssessmentQuizDto assessmentQuizDto, int clubId) throws Exception {
		String query="update assessment_quiz set name=?, type=?, no_of_questions=?, min_questions_to_answer=?, time_duration=?, status=?,  updated_date=NOW(), updated_by=?, image_path=?  where assessment_quiz_id=?";
	
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			
			st.setString(1, assessmentQuizDto.getName());
			st.setInt(2, assessmentQuizDto.getType());
			st.setInt(3, assessmentQuizDto.getNoOfQuestions());
			st.setInt(4, assessmentQuizDto.getMinQuestionsToAnswer());
			st.setInt(5, assessmentQuizDto.getTimeDuration());
			st.setInt(6, assessmentQuizDto.getStatus());
			//st.setDate(7, (Date) assessmentQuizDto.getCreatedDate());
			//st.setDate(8, (Date) assessmentQuizDto.getUpdatedDate());
			
			st.setInt(7, assessmentQuizDto.getUpdatedBy());
			st.setString(8, assessmentQuizDto.getImagePath());
			
			st.setInt(9, assessmentQuizDto.getAssessmentQuizId());
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, st);
		}
	
	}
	
	protected void updateAssessmentActivate(int assessmentQuizId, int status, int clubId) throws Exception {
		String query="update assessment_quiz set  status=? where assessment_quiz_id=?";
	
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			
			st.setInt(1, status);
			st.setInt(2, assessmentQuizId);
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, st);
		}
	
	}
	
	
	protected void deleteAssessmentQuiz(int assessmentQuizId, int clubId) throws Exception {
		
		String query="delete from assessment_quiz where assessment_quiz_id=?";
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st= null;
		
		try {
			st= conn.prepareStatement(query);
			st.setInt(1, assessmentQuizId);
			st.executeUpdate();
			
		} catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
	
	
	
}
