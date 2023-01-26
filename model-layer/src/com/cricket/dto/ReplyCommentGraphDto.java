package com.cricket.dto;

public class ReplyCommentGraphDto {
	
	private String commentId;	
	private String replyId;	
	private String replyMessage;	
	private String createdTime;		
	private String timeDifference;
	private int commentUserId;
	private String commentUserName="";
	private String commentUserProfilePicPath ="";
	
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public String getReplyMessage() {
		return replyMessage;
	}
	public void setReplyMessage(String replyMessage) {
		this.replyMessage = replyMessage;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getTimeDifference() {
		return timeDifference;
	}
	public void setTimeDifference(String timeDifference) {
		this.timeDifference = timeDifference;
	}
	public int getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(int commentUserId) {
		this.commentUserId = commentUserId;
	}
	public String getCommentUserName() {
		return commentUserName;
	}
	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
	public String getCommentUserProfilePicPath() {
		return commentUserProfilePicPath;
	}
	public void setCommentUserProfilePicPath(String commentUserProfilePicPath) {
		this.commentUserProfilePicPath = commentUserProfilePicPath;
	}

}
