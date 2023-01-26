/*
 * Created on Apr 9, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.util.Date;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CommentDto {
	
private int commentId;
private int userId;
private String userName;
private String comment;
private String commentType;
private long commentTypeId;
private Date date;
private int limit;

/**
 * @return
 */
public String getComment() {
	return comment;
}

/**
 * @return
 */
public int getCommentId() {
	return commentId;
}

/**
 * @return
 */
public Date getDate() {
	return date;
}

/**
 * @return
 */
public int getUserId() {
	return userId;
}

/**
 * @return
 */
public String getUserName() {
	return userName;
}

public String getShortUserName() {
	if(userName != null && !"".equals(userName.trim()) && userName.length() > 12){
		return userName.substring(0,12)+ "..";
	}
	return userName;
}

/**
 * @param string
 */
public void setComment(String string) {
	comment = string;
}

/**
 * @param i
 */
public void setCommentId(int i) {
	commentId = i;
}

/**
 * @param date
 */
public void setDate(Date date) {
	this.date = date;
}

/**
 * @param i
 */
public void setUserId(int i) {
	userId = i;
}

/**
 * @param string
 */
public void setUserName(String string) {
	userName = string;
}

/**
 * @return
 */
public int getLimit() {
	return limit;
}

/**
 * @param i
 */
public void setLimit(int i) {
	limit = i;
}
public String getCommentType() {
	return commentType;
}

public void setCommentType(String commentType) {
	this.commentType = commentType;
}

public long getCommentTypeId() {
	return commentTypeId;
}

public void setCommentTypeId(long commentTypeId) {
	this.commentTypeId = commentTypeId;
}


}
