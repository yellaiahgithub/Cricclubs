package com.cricket.dto;

import java.io.Serializable;
import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FantasyUserDto implements Serializable {
	
	private static final long serialVersionUID = 1054488058792213753L;

	private long   userId;
	private String userName = "";
	private String password = "";
	private String name = "";
	private String fname = "";
	private String lname = "";
	private String displayName = "";
	private String gender = "";
	private Date   dob;
	private String email = "";
	private String phone = "";	
	private String mobileCode="";
	private String state = "";
	private String countryCode = "";	
	private String profileImagePath = "";
	private String token;
	private int    kycStatus;	
	private int    status;
	private float  contestWalletBalance;	
	private float  withdrawableBalance;
	private float  bonusWalletBonus;	
	private int    coinsBalance;	
	private int    termsAcceptance;
	private String referralCode;
	private int	   noOfContestsPlayed;
	private int	   noOfContestsWon;
	private int	   noOfMatchesPlayed; 
	private String	   fantasyCurrency;
	private String userRole;
	
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getProfileImagePath() {
		return profileImagePath;
	}
	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getKycStatus() {
		return kycStatus;
	}
	public void setKycStatus(int kycStatus) {
		this.kycStatus = kycStatus;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public float getContestWalletBalance() {
		return contestWalletBalance;
	}
	public void setContestWalletBalance(float contestWalletBalance) {
		this.contestWalletBalance = contestWalletBalance;
	}
	public float getWithdrawableBalance() {
		return withdrawableBalance;
	}
	public void setWithdrawableBalance(float withdrawableBalance) {
		this.withdrawableBalance = withdrawableBalance;
	}
	public float getBonusWalletBonus() {
		return bonusWalletBonus;
	}
	public void setBonusWalletBonus(float bonusWalletBonus) {
		this.bonusWalletBonus = bonusWalletBonus;
	}
	public int getCoinsBalance() {
		return coinsBalance;
	}
	public void setCoinsBalance(int coinsBalance) {
		this.coinsBalance = coinsBalance;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getTermsAcceptance() {
		return termsAcceptance;
	}
	public void setTermsAcceptance(int termsAcceptance) {
		this.termsAcceptance = termsAcceptance;
	}
	public String getReferralCode() {
		return referralCode;
	}
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
	public int getNoOfContestsPlayed() {
		return noOfContestsPlayed;
	}
	public void setNoOfContestsPlayed(int noOfContestsPlayed) {
		this.noOfContestsPlayed = noOfContestsPlayed;
	}
	public int getNoOfContestsWon() {
		return noOfContestsWon;
	}
	public void setNoOfContestsWon(int noOfContestsWon) {
		this.noOfContestsWon = noOfContestsWon;
	}
	public int getNoOfMatchesPlayed() {
		return noOfMatchesPlayed;
	}
	public void setNoOfMatchesPlayed(int noOfMatchesPlayed) {
		this.noOfMatchesPlayed = noOfMatchesPlayed;
	}
	
	public String getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
	public String getFantasyCurrency() {
		return fantasyCurrency;
	}
	public void setFantasyCurrency(String fantasyCurrency) {
		this.fantasyCurrency = fantasyCurrency;
	}
	
}
