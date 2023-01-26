package com.cricket.dto;

import java.util.Date;

public class Ticket {
	
	private long id;
	private String type;
	private String typeId;
	private String raisedBy;
	private int status = 1;
	private String actionTakenBy;
	private String act_by;
	private String act_email;
	private String req_by;
	private String req_email;
	private String title;
	private String comments;
	private Date createdDate;
	private Date actionTakenDate;
	
	
	public Ticket() {
		super();
	}
	
	public Ticket(String type, String typeId, String raisedBy,
			String title, String comments) {
		super();
		this.type = type;
		this.typeId = typeId;
		this.raisedBy = raisedBy;
		this.title = title;
		this.comments = comments;
	}
	
	public Ticket(long id, String type, String typeId, String raisedBy, int status, String actionTakenBy,
			String title, String comments, Date createdDate, Date actionTakenDate) {
		super();
		this.id = id;
		this.type = type;
		this.typeId = typeId;
		this.raisedBy = raisedBy;
		this.status = status;
		this.actionTakenBy = actionTakenBy;
		this.title = title;
		this.comments = comments;
		this.createdDate = createdDate;
		this.actionTakenDate = actionTakenDate;
	}
	
	
	public Ticket(long id, String type, String typeId, String raisedBy, int status, String actionTakenBy, String act_by,
			String act_email, String req_by, String req_email, String title, String comments, Date createdDate,
			Date actionTakenDate) {
		super();
		this.id = id;
		this.type = type;
		this.typeId = typeId;
		this.raisedBy = raisedBy;
		this.status = status;
		this.actionTakenBy = actionTakenBy;
		this.act_by = act_by;
		this.act_email = act_email;
		this.req_by = req_by;
		this.req_email = req_email;
		this.title = title;
		this.comments = comments;
		this.createdDate = createdDate;
		this.actionTakenDate = actionTakenDate;
	}

	public String getAct_by() {
		return act_by;
	}

	public void setAct_by(String act_by) {
		this.act_by = act_by;
	}

	public String getAct_email() {
		return act_email;
	}

	public void setAct_email(String act_email) {
		this.act_email = act_email;
	}

	public String getReq_by() {
		return req_by;
	}

	public void setReq_by(String req_by) {
		this.req_by = req_by;
	}

	public String getReq_email() {
		return req_email;
	}

	public void setReq_email(String req_email) {
		this.req_email = req_email;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getRaisedBy() {
		return raisedBy;
	}
	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}
	public String getStatusString() {
		String sts = "New";
		if(this.status == 1) {
			sts = "New";
		}else if(this.status == 2) {
			sts = "Approved";
		}else if(this.status == 3) {
			sts = "Rejected";
		}
		return sts;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getActionTakenBy() {
		return actionTakenBy;
	}
	public void setActionTakenBy(String actionTakenBy) {
		this.actionTakenBy = actionTakenBy;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getActionTakenDate() {
		return actionTakenDate;
	}
	public void setActionTakenDate(Date actionTakenDate) {
		this.actionTakenDate = actionTakenDate;
	}
	
}
