package com.cricket.dto;

import java.util.List;

public class PostGraphDto {

	private String postId;	
	private String content="";	
	private String imageUrl="";	
	private String videoUrl="";	
	private String title="";	
	private String createdTime;
	private List<Object> hashTags;	
	private String likedCount="0";	
	private String disLikedCount="0";
	private String sharesCount="0";
	private String commentsCount="0";
	private String timeDifference="";
	private String shareWith="";
	private String updatedTime;
	private String feedDisplayReason="";
	private int postUserId;
	private String postUserName="";
	private String postUserProfilePicPath ="";
	private int postLeagueId;
	private String postLeaguePageId;
	private String postLeagueName="";
	private String postLeagueLogoFilePath="";
	private String isLiked="0";
	private String isDisLiked="0";
	private String imageMeta="";
	private String titleMeta="";
	private String descMeta="";
	private String imageUrlMeta="";
	private int matchId;
	private int clubId;
	private int pollId;
	
	public String getImageMeta() {
		return imageMeta;
	}
	public void setImageMeta(String imageMeta) {
		this.imageMeta = imageMeta;
	}
	public String getTitleMeta() {
		return titleMeta;
	}
	public void setTitleMeta(String titleMeta) {
		this.titleMeta = titleMeta;
	}
	public String getDescMeta() {
		return descMeta;
	}
	public void setDescMeta(String descMeta) {
		this.descMeta = descMeta;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public List<Object> getHashTags() {
		return hashTags;
	}
	public void setHashTags(List<Object> hashTags) {
		this.hashTags = hashTags;
	}
	public String getLikedCount() {
		return likedCount;
	}
	public void setLikedCount(String likedCount) {
		this.likedCount = likedCount;
	}
	public String getDisLikedCount() {
		return disLikedCount;
	}
	public void setDisLikedCount(String disLikedCount) {
		this.disLikedCount = disLikedCount;
	}
	public String getSharesCount() {
		return sharesCount;
	}
	public void setSharesCount(String sharesCount) {
		this.sharesCount = sharesCount;
	}
	public String getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(String commentsCount) {
		this.commentsCount = commentsCount;
	}
	public String getTimeDifference() {
		return timeDifference;
	}
	public void setTimeDifference(String timeDifference) {
		this.timeDifference = timeDifference;
	}
	public String getShareWith() {
		return shareWith;
	}
	public void setShareWith(String shareWith) {
		this.shareWith = shareWith;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	public int getPostUserId() {
		return postUserId;
	}
	public void setPostUserId(int postUserId) {
		this.postUserId = postUserId;
	}
	public String getPostUserName() {
		return postUserName;
	}
	public void setPostUserName(String postUserName) {
		this.postUserName = postUserName;
	}
	public String getPostUserProfilePicPath() {
		return postUserProfilePicPath;
	}
	public void setPostUserProfilePicPath(String postUserProfilePicPath) {
		this.postUserProfilePicPath = postUserProfilePicPath;
	}
	public String getFeedDisplayReason() {
		return feedDisplayReason;
	}
	public void setFeedDisplayReason(String feedDisplayReason) {
		this.feedDisplayReason = feedDisplayReason;
	}
	public int getPostLeagueId() {
		return postLeagueId;
	}
	public void setPostLeagueId(int postLeagueId) {
		this.postLeagueId = postLeagueId;
	}
	public String getPostLeagueName() {
		return postLeagueName;
	}
	public void setPostLeagueName(String postLeagueName) {
		this.postLeagueName = postLeagueName;
	}
	public String getPostLeagueLogoFilePath() {
		return postLeagueLogoFilePath;
	}
	public void setPostLeagueLogoFilePath(String postLeagueLogoFilePath) {
		this.postLeagueLogoFilePath = postLeagueLogoFilePath;
	}
	public String getPostLeaguePageId() {
		return postLeaguePageId;
	}
	public void setPostLeaguePageId(String postLeaguePageId) {
		this.postLeaguePageId = postLeaguePageId;
	}
	public String getIsLiked() {
		return isLiked;
	}
	public void setIsLiked(String isLiked) {
		this.isLiked = isLiked;
	}
	public String getIsDisLiked() {
		return isDisLiked;
	}
	public void setIsDisLiked(String isDisLiked) {
		this.isDisLiked = isDisLiked;
	}
	public String getImageUrlMeta() {
		return imageUrlMeta;
	}
	public void setImageUrlMeta(String imageUrlMeta) {
		this.imageUrlMeta = imageUrlMeta;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public int getPollId() {
		return pollId;
	}
	public void setPollId(int pollId) {
		this.pollId = pollId;
	}
	

}
