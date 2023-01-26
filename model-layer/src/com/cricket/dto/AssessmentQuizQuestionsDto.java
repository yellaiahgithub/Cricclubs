/*created on Feb 11 2022
*/

package com.cricket.dto;

public class AssessmentQuizQuestionsDto {

	private int assessmentQuizQuestionsId ;
	private int assessmentQuizId;
	private  String question;
	
	public int getAssessmentQuizQuestionsId() {
		return assessmentQuizQuestionsId;
	}
	public void setAssessmentQuizQuestionsId(int assessmentQuizQuestionsId) {
		this.assessmentQuizQuestionsId = assessmentQuizQuestionsId;
	}
	public int getAssessmentQuizId() {
		return assessmentQuizId;
	}
	public void setAssessmentQuizId(int assessmentQuizId) {
		this.assessmentQuizId = assessmentQuizId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question= question;
	}
	
	
}
