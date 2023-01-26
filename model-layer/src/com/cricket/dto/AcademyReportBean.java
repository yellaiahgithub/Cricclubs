package com.cricket.dto;

import java.sql.Date;

public class AcademyReportBean {

private long id;
	
	private long player_id;
	
	private Date creationDate;
	
	private Date fromDate;

	private Date toDate;
	
	private String reportData;
	
	private AcademyPlayerReportDto data;
	
	private int reportBy;

	public AcademyPlayerReportDto getData() {
		return data;
	}

	public void setData(AcademyPlayerReportDto data) {
		this.data = data;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(long player_id) {
		this.player_id = player_id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public int getReportBy() {
		return reportBy;
	}

	public void setReportBy(int reportBy) {
		this.reportBy = reportBy;
	}
}
