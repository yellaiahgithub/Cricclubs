package com.cricket.dto;

public class TeamStatsDtoFB {
	
	int goals;
	int Corners;
	int Fouls;
	int RedCards;
	int YellowCards;
	int GoalsSaved;
	int offSides;
	
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
	public int getCorners() {
		return Corners;
	}
	public void setCorners(int corners) {
		Corners = corners;
	}
	public int getFouls() {
		return Fouls;
	}
	public void setFouls(int fouls) {
		Fouls = fouls;
	}
	public int getRedCards() {
		return RedCards;
	}
	public void setRedCards(int redCards) {
		RedCards = redCards;
	}
	public int getYellowCards() {
		return YellowCards;
	}
	public void setYellowCards(int yellowCards) {
		YellowCards = yellowCards;
	}
	public int getGoalsSaved() {
		return GoalsSaved;
	}
	public void setGoalsSaved(int goalsSaved) {
		GoalsSaved = goalsSaved;
	}
	public int getOffSides() {
		return offSides;
	}
	public void setOffSides(int offSides) {
		this.offSides = offSides;
	}
	
}
