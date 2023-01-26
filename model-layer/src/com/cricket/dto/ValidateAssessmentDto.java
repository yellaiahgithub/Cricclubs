package com.cricket.dto;

import java.util.HashMap;
import java.util.Map;

public class ValidateAssessmentDto {
	
	public int assessmnetQuizId;
	
	public int timeTakenToComplete;
	
	//Map<assessmentQuizQuestionsId,optionSelected>
	public Map<Integer,String> questionAnswerMap=new HashMap<Integer,String>();

	public int getAssessmnetQuizId() {
		return assessmnetQuizId;
	}

	public void setAssessmnetQuizId(int assessmnetQuizId) {
		this.assessmnetQuizId = assessmnetQuizId;
	}

	public int getTimeTakenToComplete() {
		return timeTakenToComplete;
	}

	public void setTimeTakenToComplete(int timeTakenToComplete) {
		this.timeTakenToComplete = timeTakenToComplete;
	}

	public Map<Integer, String> getQuestionAnswerMap() {
		return questionAnswerMap;
	}

	public void setQuestionAnswerMap(Map<Integer, String> questionAnswerMap) {
		this.questionAnswerMap = questionAnswerMap;
	}
	
		

}
