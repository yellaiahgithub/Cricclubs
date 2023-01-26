package com.cricket.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LiveScoreOverlayBean {
	private int view = 1;
	private Map<String, Object> values = new TreeMap<String, Object>();
	private boolean isSecondInningsStarted = false;
	private boolean isSuperOver = false;
	private boolean isSuperOverSecondInningsStarted = false;
	private List<String> balls = new ArrayList<String>();
	private boolean is2XCricket = false;
	
	public Map<String, Object> getValues() {
		return values;
	}
	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
	public boolean isSecondInningsStarted() {
		return isSecondInningsStarted;
	}
	public void setSecondInningsStarted(boolean isSecondInningsStarted) {
		this.isSecondInningsStarted = isSecondInningsStarted;
	}
	public List<String> getBalls() {
		return balls;
	}
	public void setBalls(List<String> balls) {
		this.balls = balls;
	}
	public boolean isIs2XCricket() {
		return is2XCricket;
	}
	public void setIs2XCricket(boolean is2xCricket) {
		is2XCricket = is2xCricket;
	}
	public int getView() {
		return view;
	}
	public void setView(int view) {
		this.view = view;
	}
	public boolean isSuperOver() {
		return isSuperOver;
	}
	public void setSuperOver(boolean isSuperOver) {
		this.isSuperOver = isSuperOver;
	}
	public boolean isSuperOverSecondInningsStarted() {
		return isSuperOverSecondInningsStarted;
	}
	public void setSuperOverSecondInningsStarted(boolean isSuperOverSecondInningsStarted) {
		this.isSuperOverSecondInningsStarted = isSuperOverSecondInningsStarted;
	}
	
}
