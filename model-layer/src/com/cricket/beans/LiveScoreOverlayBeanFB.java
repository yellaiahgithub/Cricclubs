package com.cricket.beans;

import java.util.Map;
import java.util.TreeMap;

public class LiveScoreOverlayBeanFB {
	
	private int view = 1;
	private Map<String, Object> values = new TreeMap<String, Object>();
	
	public int getView() {
		return view;
	}
	public void setView(int view) {
		this.view = view;
	}		
	public Map<String, Object> getValues() {
		return values;
	}
	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
}
