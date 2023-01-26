package com.cricket.dto;

public class StateDto {
	
	private int id;	
	private String name;	
	private String code;	
	private String countryCode;	
	private int fantasyEnabled;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}	
	public int getFantasyEnabled() {
		return fantasyEnabled;
	}
	public void setFantasyEnabled(int fantasyEnabled) {
		this.fantasyEnabled = fantasyEnabled;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
}
