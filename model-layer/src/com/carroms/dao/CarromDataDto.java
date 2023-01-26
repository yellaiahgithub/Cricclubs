package com.carroms.dao;

public class CarromDataDto {

	private String selectGame;
	private int boardNumber;
	private String teamOneName;
	private int teamOneSet;
	private int teamOneScore;
	private String teamOneFile;
	private String teamTwoName;
	private int teamTwoSet;
	private int teamTwoScore;
	private String teamTwoFile;	
	
	public String getSelectGame() {
		return selectGame;
	}

	public void setSelectGame(String selectGame) {
		this.selectGame = selectGame;
	}

	public String getTeamOneFile() {
		return teamOneFile;
	}

	public void setTeamOneFile(String teamOneFile) {
		this.teamOneFile = teamOneFile;
	}

	public String getTeamTwoFile() {
		return teamTwoFile;
	}

	public void setTeamTwoFile(String teamTwoFile) {
		this.teamTwoFile = teamTwoFile;
	}

	public int getBoardNumber() {
		return boardNumber;
	}

	public void setBoardNumber(int boardNumber) {
		this.boardNumber = boardNumber;
	}

	public String getTeamOneName() {
		return teamOneName;
	}

	public void setTeamOneName(String teamOneName) {
		this.teamOneName = teamOneName;
	}

	public int getTeamOneSet() {
		return teamOneSet;
	}

	public void setTeamOneSet(int teamOneSet) {
		this.teamOneSet = teamOneSet;
	}

	public int getTeamOneScore() {
		return teamOneScore;
	}

	public void setTeamOneScore(int teamOneScore) {
		this.teamOneScore = teamOneScore;
	}

	public String getTeamTwoName() {
		return teamTwoName;
	}

	public void setTeamTwoName(String teamTwoName) {
		this.teamTwoName = teamTwoName;
	}

	public int getTeamTwoSet() {
		return teamTwoSet;
	}

	public void setTeamTwoSet(int teamTwoSet) {
		this.teamTwoSet = teamTwoSet;
	}

	public int getTeamTwoScore() {
		return teamTwoScore;
	}

	public void setTeamTwoScore(int teamTwoScore) {
		this.teamTwoScore = teamTwoScore;
	}
}
