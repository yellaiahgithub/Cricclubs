/*
 * Created on Apr 9, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.util.Date;

import com.cricket.utility.CommonUtility;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NewsDto {
	
private int newsId;
private long userId;
private String userName;
private String bgColor;
private Date date;
private String metaData;
private String news;
private String title;
private String image;
private String videoUrl;
private int isPublished;
private int isInternational;
private String newsDesc;



public String getMetaData() {
	return metaData;
}

public void setMetaData(String metaData) {
	this.metaData = metaData;
}

public String getVideoUrl() {
	return videoUrl;
}

public void setVideoUrl(String videoUrl) {
	this.videoUrl = videoUrl;
}


/***
 * Introduced to fix WEB-264
 * @return
 */
private boolean showOnHome;



private int limit = 100;
private int skip = 0;


public int getLimit() {
	return limit;
}


public void setLimit(int limit) {
	this.limit = limit;
}


public String getFormattedNews(){
	return news;
}


public int getNewsId() {
	return newsId;
}


public void setNewsId(int newsId) {
	this.newsId = newsId;
}


public long getUserId() {
	return userId;
}


public void setUserId(long userId) {
	this.userId = userId;
}


public String getUserName() {
	return userName;
}


public void setUserName(String userName) {
	this.userName = userName;
}


public String getBgColor() {
	return bgColor;
}


public void setBgColor(String bgColor) {
	this.bgColor = bgColor;
}


public Date getDate() {
	return date;
}


public void setDate(Date date) {
	this.date = date;
}


public String getNews() {
	return news;
}


public void setNews(String news) {
	this.news = news;
}


public String getTitle() {
	return title;
}


public void setTitle(String title) {
	this.title = title;
}


public String getImage() {
	return image;
}


public void setImage(String image) {
	this.image = image;
}

public String getShortDescription(){
	String articleContent = CommonUtility.removeHTMLTags(getNews());
	if (articleContent != null && articleContent.length() > 200) {
		articleContent = articleContent.substring(0, 199)
				+ "[...]";
	}
	return articleContent;
}

/***
 * Introduced to fix WEB-264
 * @return
 */
public boolean isShowOnHome() {
	return showOnHome;
}


public void setShowOnHome(boolean showOnHome) {
	this.showOnHome = showOnHome;
}


public int getIsPublished() {
	return isPublished;
}


public void setIsPublished(int isPublished) {
	this.isPublished = isPublished;
}


public int getSkip() {
	return skip;
}


public void setSkip(int skip) {
	this.skip = skip;
}


public int getIsInternational() {
	return isInternational;
}


public void setIsInternational(int isInternational) {
	this.isInternational = isInternational;
}


public String getNewsDesc() {
	return newsDesc;
}


public void setNewsDesc(String newsDesc) {
	this.newsDesc = newsDesc;
}
}
