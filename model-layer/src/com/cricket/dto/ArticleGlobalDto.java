package com.cricket.dto;

import java.util.Date;

import com.cricket.utility.CommonUtility;

public class ArticleGlobalDto {

	private int articleId;
	private String title;
	private Date date;
	private String article;
    private int limit = 20;
	private String image = "";
	private String comment;
	private String pageNumber;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    public String getShortDescription(){
		String articleContent = CommonUtility
				.removeHTMLTags(getArticle());
		if (articleContent != null && articleContent.length() > 500) {
			articleContent = articleContent.substring(0, 499)
					+ "[...]";
		}
		return articleContent;
	

    }

}
