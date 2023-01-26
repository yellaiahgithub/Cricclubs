package com.cricket.beans;

import java.util.List;

public class ParScoreBall {
	
	private String oversBowled;
	private String oversRemaining;
	private List<String> data;
	
	public String getOversBowled() {
		return oversBowled;
	}
	public void setOversBowled(String oversBowled) {
		this.oversBowled = oversBowled;
	}
	public String getOversRemaining() {
		return oversRemaining;
	}
	public void setOversRemaining(String oversRemaining) {
		this.oversRemaining = oversRemaining;
	}
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
}
