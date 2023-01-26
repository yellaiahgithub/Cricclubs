package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.AssessmentQuizQuestionsDto;
import com.cricket.utility.DButility;

public class AssessmentQuizQuestionsDAO {
	
	Logger log = LoggerFactory.getLogger(AssessmentQuizQuestionsDAO.class);
	

	protected void insertAssessmentQuizQuestions(List<AssessmentQuizQuestionsDto> assessmentQuizQuestionsList,int clubId) throws Exception {
	String query="insert into assessment_quiz_questions(assessment_quiz_id,question) values (?,?)";

	Connection conn=null;
	PreparedStatement st=null;
	
	try {
		 conn=DButility.getConnection(clubId);
		st=conn.prepareStatement(query);
		conn.setAutoCommit(false);
		
		for(AssessmentQuizQuestionsDto assessmentQuizQuestionsDto:assessmentQuizQuestionsList) {
			int i = 1;
			
			
		st.setInt(i++, assessmentQuizQuestionsDto.getAssessmentQuizId());
		st.setString(i++, assessmentQuizQuestionsDto.getQuestion());
		
		st.addBatch();
		}
		
		st.executeBatch();	
		conn.commit();
	}catch(SQLException e) {
		throw new Exception(e.getMessage());
	}finally {
		DButility.closeConnectionAndStatement(conn, st);
	}
  }
	
	protected List<AssessmentQuizQuestionsDto> getAssessmentQuizQuestionsAll(int clubId) throws Exception{
		List<AssessmentQuizQuestionsDto> assessmentQuizQuestionsDtoList=new ArrayList<AssessmentQuizQuestionsDto>();
		String query="select * from assessment_quiz_questions";
		
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			rs=st.executeQuery();
			
			while(rs.next()) {
				AssessmentQuizQuestionsDto assessmentQuizQuestionsDto=new AssessmentQuizQuestionsDto();
				assessmentQuizQuestionsDto.setAssessmentQuizQuestionsId(rs.getInt("assessment_quiz_questions_id"));
				assessmentQuizQuestionsDto.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
				assessmentQuizQuestionsDto.setQuestion(rs.getString("question"));
				
				
			assessmentQuizQuestionsDtoList.add(assessmentQuizQuestionsDto);
			}
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
				
		return assessmentQuizQuestionsDtoList;
			}
	
	protected List<AssessmentQuizQuestionsDto> getAssessmentQuizQuestions(int assessmentQuizId, int clubId,int noOfQsns) throws Exception{
		List<AssessmentQuizQuestionsDto> assessmentQuizQuestionsDtoList=new ArrayList<AssessmentQuizQuestionsDto>();
		String query="select * from assessment_quiz_questions where assessment_quiz_id=?" ;
		
		if(noOfQsns>0) {
			query +=" order by RAND() LIMIT "+noOfQsns;
		}
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			st=conn.prepareStatement(query);
			st.setInt(1, assessmentQuizId);
			rs=st.executeQuery();
			
			while(rs.next()) {
				AssessmentQuizQuestionsDto assessmentQuizQuestionsDto=new AssessmentQuizQuestionsDto();
				assessmentQuizQuestionsDto.setAssessmentQuizQuestionsId(rs.getInt("assessment_quiz_questions_id"));
				assessmentQuizQuestionsDto.setAssessmentQuizId(rs.getInt("assessment_quiz_id"));
				assessmentQuizQuestionsDto.setQuestion(rs.getString("question"));
				
				assessmentQuizQuestionsDtoList.add(assessmentQuizQuestionsDto);
			}
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
				
		return assessmentQuizQuestionsDtoList;
			}
	
	
	protected void updateAssessmentQuizQuestions(AssessmentQuizQuestionsDto assessmentQuizQuestionsDto,int clubId) throws Exception {
		String query="update assessment_quiz_questions set question=? where assessment_quiz_questions_id=?";
		

		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st=null;
		
		try {
			st=conn.prepareStatement(query);
			st.setString(1, assessmentQuizQuestionsDto.getQuestion());
			
			
			st.setInt(2,assessmentQuizQuestionsDto.getAssessmentQuizQuestionsId());
			
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}
	
protected void deleteAssessmentQuizQuestion(int assessmentQuizQuestionsId, int clubId) throws Exception {
		
		String query="delete from assessment_quiz_questions where assessment_quiz_questions_id=?";
		Connection conn=DButility.getConnection(clubId);
		PreparedStatement st= null;
		
		try {
			st= conn.prepareStatement(query);
			st.setInt(1, assessmentQuizQuestionsId);
			st.executeUpdate();
				
			
			
		} catch(SQLException e) {
			throw new Exception(e.getMessage());
		}finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
		
	}
protected void deleteAssessmentQuizAllQuestions(int assessmentQuizId, int clubId) throws Exception {
	
	String query="delete from assessment_quiz_questions where assessment_quiz_id=?";
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
