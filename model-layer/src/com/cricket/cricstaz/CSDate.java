package com.cricket.cricstaz;

public class CSDate {
	String Day;
	String Month;
	String Year;
	
	public CSDate() {
		super();
	}
	
	public CSDate(String day, String month, String year) {
		super();
		Day = day;
		Month = month;
		Year = year;
	}
	
	public String getDay() {
		return Day;
	}
	public void setDay(String day) {
		Day = day;
	}
	public String getMonth() {
		return Month;
	}
	public void setMonth(String month) {
		Month = month;
	}
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	
}
