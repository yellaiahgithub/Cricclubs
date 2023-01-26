package com.cricket.beans;

public class TypeOfRuns {
	private int singles;
	private int doubles;
	private int threes;
	private int fours;
	private int fives;
	private int sixers;
	private int extras;

	public int getSingles() {
		return singles;
	}

	public void setSingles(int singles) {
		this.singles = singles;
	}

	public int getDoubles() {
		return doubles;
	}

	public void setDoubles(int doubles) {
		this.doubles = doubles;
	}

	public int getThrees() {
		return threes;
	}

	public void setThrees(int threes) {
		this.threes = threes;
	}

	public int getFours() {
		return fours;
	}

	public void setFours(int fours) {
		this.fours = fours;
	}

	public int getFives() {
		return fives;
	}

	public void setFives(int fives) {
		this.fives = fives;
	}

	public int getSixers() {
		return sixers;
	}

	public void setSixers(int sixers) {
		this.sixers = sixers;
	}

	public int getExtras() {
		return extras;
	}

	public void setExtras(int extras) {
		this.extras = extras;
	}
	
	public void addRuns(int runs){
		if(runs == 1){
			this.singles += runs;
		}else if(runs == 2){
			this.doubles += runs;
		}else if(runs == 3){
			this.threes += runs;
		}else if(runs == 4){
			this.fours += runs;
		}else if(runs == 5){
			this.fives += runs;
		}else if(runs == 6){
			this.sixers += runs;
		}
	}
}
