package com.cricket.dto;

public class AcademyPlayerReportDto {

	private int playerId;
	
	private String name;
	
	private String programName;
	
	private String batchName;
	
	private String profileImage;
	
	private int sesssionCount;
	
	private int daysPresent;
	
	private int daysAbsent;
	
	private AcademyPlayerSummaryStats performance;
	
	private String feedBack;
	
	private AcademyPlayerComments comments;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public int getSesssionCount() {
		return sesssionCount;
	}

	public void setSesssionCount(int sesssionCount) {
		this.sesssionCount = sesssionCount;
	}

	public int getDaysPresent() {
		return daysPresent;
	}

	public void setDaysPresent(int daysPresent) {
		this.daysPresent = daysPresent;
	}

	public int getDaysAbsent() {
		return daysAbsent;
	}

	public void setDaysAbsent(int daysAbsent) {
		this.daysAbsent = daysAbsent;
	}

	public AcademyPlayerSummaryStats getPerformance() {
		return performance;
	}

	public void setPerformance(AcademyPlayerSummaryStats performance) {
		this.performance = performance;
	}

	public String getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}

	public AcademyPlayerComments getComments() {
		return comments;
	}

	public void setComments(AcademyPlayerComments comments) {
		this.comments = comments;
	}
}
