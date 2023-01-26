/*created on Feb 11 2022
*/

package com.cricket.dto;

import java.util.Date;

public class AssessmentQuizDto {

	private int assessmentQuizId;
	private String name;
	private int type;
	private int noOfQuestions;
	private int minQuestionsToAnswer;
	private int timeDuration;
	private int status;
	private Date createdDate;
	private Date updatedDate;
	private int createdBy;
	private int updatedBy;
	private String imagePath;
	private int clubId;
	private String clubName;
	
	public int getAssessmentQuizId() {
		return assessmentQuizId;
	}
	public void setAssessmentQuizId(int assessmentQuizId) {
		this.assessmentQuizId = assessmentQuizId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNoOfQuestions() {
		return noOfQuestions;
	}
	public void setNoOfQuestions(int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}
	public int getMinQuestionsToAnswer() {
		return minQuestionsToAnswer;
	}
	public void setMinQuestionsToAnswer(int minQuestionsToAnswer) {
		this.minQuestionsToAnswer = minQuestionsToAnswer;
	}
	public int getTimeDuration() {
		return timeDuration;
	}
	public void setTimeDuration(int timeDuration) {
		this.timeDuration = timeDuration;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	
	
	
}
