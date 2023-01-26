package com.cricket.dao;

import java.util.List;

import com.cricket.dto.AssessmentQuizQuestionsDto;

public class AssessmentQuizQuestionsFactory {

private static AssessmentQuizQuestionsDAO assessmentQuizQuestionsDAO= null;
	
	private static AssessmentQuizQuestionsDAO getDaoInstance() {
		if(assessmentQuizQuestionsDAO == null) {
			assessmentQuizQuestionsDAO= new AssessmentQuizQuestionsDAO();
		}
		return assessmentQuizQuestionsDAO;
	}
	
	public static void insertAssessmentQuizQuestions(List<AssessmentQuizQuestionsDto> assessmentQuizQuestionsList, int clubId) throws Exception {
		 getDaoInstance().insertAssessmentQuizQuestions(assessmentQuizQuestionsList,clubId);
	}
	
	public static List<AssessmentQuizQuestionsDto> getAssessmentQuizQuestionsAll(int clubId) throws Exception{
		return getDaoInstance().getAssessmentQuizQuestionsAll(clubId);
	}
	
	public static List<AssessmentQuizQuestionsDto> getAssessmentQuizQuestions(int assessmentQuizId, int clubId,int noOfQsns) throws Exception {
		return getDaoInstance().getAssessmentQuizQuestions(assessmentQuizId,clubId,noOfQsns);	
	}
	
	public static void updateAssessmentQuizQuestions(AssessmentQuizQuestionsDto assessmentQuizQuestionsDto,int clubId) throws Exception {
		getDaoInstance().updateAssessmentQuizQuestions(assessmentQuizQuestionsDto,clubId);
	}
	
	public static void deleteAssessmentQuizQuestion(int assessmentQuizQuestionsId,int clubId) throws Exception {
	 getDaoInstance().deleteAssessmentQuizQuestion(assessmentQuizQuestionsId,clubId);
	}
	
	public static void deleteAssessmentQuizAllQuestions(int assessmentQuizId,int clubId) throws Exception {
		getDaoInstance().deleteAssessmentQuizAllQuestions(assessmentQuizId,clubId);
	}

	
}
