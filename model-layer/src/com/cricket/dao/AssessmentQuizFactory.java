package com.cricket.dao;

import java.util.List;

import com.cricket.dto.AssessmentQuizDto;

public class AssessmentQuizFactory {

	private static AssessmentQuizDAO assessmentQuizDAO= null;
	
	private static AssessmentQuizDAO getDaoInstance() {
		if(assessmentQuizDAO == null) {
			assessmentQuizDAO= new AssessmentQuizDAO();
		}
		return assessmentQuizDAO;
	}
	
	public static void insertAssessmentQuiz(AssessmentQuizDto assessmentQuizDto,int clubId) throws Exception {
		 getDaoInstance().insertAssessmentQuiz(assessmentQuizDto,clubId);
	}
	
	public static List<AssessmentQuizDto> getAssessmentQuizAll(int clubId,String assessmentIds) throws Exception{
		return getDaoInstance().getAssessmentQuizAll(clubId,assessmentIds);
	}
	
	public static List<AssessmentQuizDto> getActiveAssessments(int status,int clubId) throws Exception{
		return getDaoInstance().getActiveAssessments(status, clubId);
	}
	
	public static AssessmentQuizDto getAssessmentQuiz(int assessmentQuizId,int clubId) throws Exception {
		return getDaoInstance().getAssessmentQuiz(assessmentQuizId,clubId);	
	}
	
	public static void updateAssessmentQuiz(AssessmentQuizDto assessmentQuizDto,int clubId) throws Exception {
		getDaoInstance().updateAssessmentQuiz(assessmentQuizDto,clubId);
	}
	
	public static void updateAssessmentActivate(int assessmentQuizId,int status,int clubId) throws Exception {
		getDaoInstance().updateAssessmentActivate(assessmentQuizId,status,clubId);
	}
	
	public static void deleteAssessmentQuiz(int assessmentQuizId,int clubId) throws Exception {
		getDaoInstance().deleteAssessmentQuiz(assessmentQuizId,clubId);
	}
	
	
}
