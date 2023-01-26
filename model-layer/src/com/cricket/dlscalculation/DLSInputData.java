package com.cricket.dlscalculation;

public class DLSInputData {

	private int ballsDecidedAtStart;
	private int ballsAtStartOfPlay;
	private int ballsPlayed;
	private int revisedBalls;
	private int wicketLost;
	
	public int getBallsLost(){
		return ballsAtStartOfPlay - revisedBalls;
	}
	
	public int getBallsLeft(){
		return revisedBalls - ballsPlayed;
	}

	public int getBallsDecidedAtStart() {
		return ballsDecidedAtStart;
	}
	public void setBallsDecidedAtStart(int ballsDecidedAtStart) {
		this.ballsDecidedAtStart = ballsDecidedAtStart;
	}
	public int getBallsAtStartOfPlay() {
		return ballsAtStartOfPlay;
	}
	public void setBallsAtStartOfPlay(int ballsAtStartOfPlay) {
		this.ballsAtStartOfPlay = ballsAtStartOfPlay;
	}
	public int getBallsPlayed() {
		return ballsPlayed;
	}
	public void setBallsPlayed(int ballsPlayed) {
		this.ballsPlayed = ballsPlayed;
	}
	public int getRevisedBalls() {
		return revisedBalls;
	}
	public void setRevisedBalls(int revisedBalls) {
		this.revisedBalls = revisedBalls;
	}
	public int getWicketLost() {
		return wicketLost;
	}
	public void setWicketLost(int wicketLost) {
		this.wicketLost = wicketLost;
	}

	public DLSInputData(int ballsDecidedAtStart, int ballsAtStartOfPlay,
			int ballsPlayed, int revisedBalls, int wicketLost) {
		super();
		this.ballsDecidedAtStart = ballsDecidedAtStart;
		this.ballsAtStartOfPlay = ballsAtStartOfPlay;
		this.ballsPlayed = ballsPlayed;
		this.revisedBalls = revisedBalls;
		this.wicketLost = wicketLost;
	}
	public DLSInputData() {
		super();
	}
	
}
