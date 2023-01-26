package com.cricket.dto;

import java.util.List;

public class DeletedMatchDto {
	
	private MatchDto match;
	private FixtureDto fixture;
	private List<InningsDto> innings;
	
	public MatchDto getMatch() {
		return match;
	}
	public void setMatch(MatchDto match) {
		this.match = match;
	}
	public FixtureDto getFixture() {
		return fixture;
	}
	public void setFixture(FixtureDto fixture) {
		this.fixture = fixture;
	}
	public List<InningsDto> getInnings() {
		return innings;
	}
	public void setInnings(List<InningsDto> innings) {
		this.innings = innings;
	}
	
}
