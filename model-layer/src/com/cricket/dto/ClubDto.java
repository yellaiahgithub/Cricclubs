/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.LeagueFactory;
import com.cricket.utility.ClubCacheHandler;
import com.cricket.utility.CommonUtility;
import com.google.gson.Gson;

/**
 * @author ganesh
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ClubDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2578149789082736546L;
	private int clubId;
	private String name = "";
	private String lastName = "";
	private String firstName = "";
	private String address = "";
	private String address1 = "";
	private String city = "";
	private String state = "";
	private String zipcode = "";
	private String country = "";
	private String facebookLink = "";
	private String twitterLink = "";
	private String youtubeLink = "";
	private String instagramLink = "";
	private String estimatedCount = "";
	private String phone = "";
	private String about = "";
	private String estableshed = "";
	private String email = "";
	private List<LeagueDto> leagueList = null;
	private List<ClubDto> clubList = null;
	private int isActive = 0;
	private String startupMessage = "";
	private int isStartupMessageEnabled = 0;
	private int isLiveScoringEnabled;
	private String hear;
	private List<SponsorDto> sponsors;
	private String latitude;
	private String longitude;
	private String shortURL;
	private String theme;
	private String bgImage;
	private String canScorerAddPlayer;
	private String disableBannerAd;
	private String doNotShowClubName;
	private String clubSponsorsOnTop;
	private int sponsorsCount;
	private int groundBookingRequired;
	private String playerTerms = "";
	private boolean showPointsTable;
	private boolean allowCustomPlayerId;
	private boolean clubStructureEnabled;
	private boolean removeCCAds;
	private int clubAdmin1;
	private int clubAdmin2;
	private String customDomain;
	private String themeFolder=" ";
	private int playerCommunication;
	private int disableOtherAds;
	private int isPhotoMandatory;
	private Date trialDate;
	private String logo_file_path;
	private int allowOverlay;
	private int allowRegister;
	private String association = "";
	private Date lastUpdatedDate;
	private boolean isPracticeClub;
	private String dateFormate = "MM/dd/yyyy";
	private String timeFormat = "12";
	private String timeZone = "America/Chicago";
	private int club_org_id;
	private String club_Org_Number;
	private String about_encryp_json;
	private String showMatchSlider;
	private int firstNameFirst;
	private boolean isInternalClubLock;
	private boolean disableURLAtCC;
	private boolean isPlayerRegPaymnetAvail;
	private String startupImagePath;
	private int isAcademy;
	private String termsandConditions;
	private String academy_theme;
	private String backGroundImagePath;
	private int hideInternalClub;
	private int isStreamingAllowed;
	private String androidAppLink;
	private int sportId;
	public String getInternalClubId() {
		return internalClubId;
	}

	public void setInternalClubId(String internalClubId) {
		this.internalClubId = internalClubId;
	}

	private String iosAppLink;
	private String srcSite="";
	private int srcLeagueId;
	private int clubAdminPlayerReg;
	private int isUmpireReportMandatory;
	private int isCaptainReportMandatory;
	
	private String internalClubId;
	
	/***
	 * payment related columns : package code/ price/
	 * 
	 */

	private String packageCode;
	private String price;
	private String paymentRefId;
	private int paymentStaus;

	private int clubCurrencyId;
	private String clubCurrency;
	
	
	private String headerColor;
	private String menuColor;
	private int    unlimitedStreaming;	
	private String createDate;
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getAndroidAppLink() {
		return androidAppLink;
	}

	public void setAndroidAppLink(String androidAppLink) {
		this.androidAppLink = androidAppLink;
	}

	public String getIosAppLink() {
		return iosAppLink;
	}

	public void setIosAppLink(String iosAppLink) {
		this.iosAppLink = iosAppLink;
	}

	public int getHideInternalClub() {
		return hideInternalClub;
	}

	public void setHideInternalClub(int hideInternalClub) {
		this.hideInternalClub = hideInternalClub;
	}

	public int getClubCurrencyId() {
		return clubCurrencyId;
	}

	public void setClubCurrencyId(int clubCurrencyId) {
		this.clubCurrencyId = clubCurrencyId;
	}

	public String getClubCurrency() {
		return clubCurrency;
	}

	public void setClubCurrency(String clubCurrency) {
		this.clubCurrency = clubCurrency;
	}

	public String getAcademy_theme() {
		return academy_theme;
	}

	public void setAcademy_theme(String academy_theme) {
		this.academy_theme = academy_theme;
	}

	public String getTermsandConditions() {
		return termsandConditions;
	}

	public void setTermsandConditions(String termsandConditions) {
		this.termsandConditions = termsandConditions;
	}

	public String getInstagramLink() {
		return instagramLink;
	}

	public void setInstagramLink(String instagramLink) {
		this.instagramLink = instagramLink;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public boolean isPlayerRegPaymnetAvail() {
		return isPlayerRegPaymnetAvail;
	}

	public void setPlayerRegPaymnetAvail(boolean isPlayerRegPaymnetAvail) {
		this.isPlayerRegPaymnetAvail = isPlayerRegPaymnetAvail;
	}

	public boolean isDisableURLAtCC() {
		return disableURLAtCC;
	}

	public void setDisableURLAtCC(boolean disableURLAtCC) {
		this.disableURLAtCC = disableURLAtCC;
	}
	
	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPaymentRefId() {
		return paymentRefId;
	}

	public void setPaymentRefId(String paymentRefId) {
		this.paymentRefId = paymentRefId;
	}

	public boolean isInternalClubLock() {
		return isInternalClubLock;
	}

	public void setInternalClubLock(boolean isInternalClubLock) {
		this.isInternalClubLock = isInternalClubLock;
	}

	public int getFirstNameFirst() {
		return firstNameFirst;
	}

	public void setFirstNameFirst(int firstNameFirst) {
		this.firstNameFirst = firstNameFirst;
	}

	public String getTimeZone() {
		if(timeZone != null && !timeZone.isEmpty()) {
			return timeZone;			
		}
		return "America/Chicago";
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	


	public String getClub_Org_Number() {
		return club_Org_Number;
	}

	public void setClub_Org_Number(String club_Org_Number) {
		this.club_Org_Number = club_Org_Number;
	}

	public int getClub_org_id() {
		return club_org_id;
	}

	public void setClub_org_id(int club_org_id) {
		this.club_org_id = club_org_id;
	}

	public String getAbout_encryp_json() {
		return about_encryp_json;
	}

	public void setAbout_encryp_json(String about_encryp_json) {
		this.about_encryp_json = about_encryp_json;
	}

	public String getDateFormate() {
		if(dateFormate != null && !dateFormate.isEmpty()) {
			return dateFormate;
		}
		return "MM/dd/yyyy";
	}

	public void setDateFormate(String dateFormate) {
		this.dateFormate = dateFormate;
	}

	public boolean isPracticeClub() {
		return isPracticeClub;
	}

	public void setPracticeClub(boolean isPracticeClub) {
		this.isPracticeClub = isPracticeClub;
	}

	public ClubDto() {
		super();
	}

	public ClubDto(String name, String city, String state, String zipcode, String country, String phone, String email,
			int isActive) {
		super();
		this.name = name;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.country = country;
		this.phone = phone;
		this.email = email;
		this.isActive = isActive;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getAssociation() {
		return association;
	}

	public void setAssociation(String association) {
		this.association = association;
	}

	public int getAllowRegister() {
		return allowRegister;
	}

	public void setAllowRegister(int allowRegister) {
		this.allowRegister = allowRegister;
	}

	public int getAllowOverlay() {
		return allowOverlay;
	}

	public void setAllowOverlay(int allowOverlay) {
		this.allowOverlay = allowOverlay;
	}

	private Map<String, String> themeMap = new HashMap<String, String>();
	private Map<String, String> themeMap1 = new HashMap<String, String>();
	private Map<String, String> bgImageMap = new HashMap<String, String>();

	{
		themeMap.put("peter_river", "Deep Sea");
		themeMap.put("black", "Outer Space");
		themeMap.put("orange", "Orange");
		themeMap.put("yellow", "Sunrise");
		themeMap.put("alizarin", "Rustic");
		themeMap.put("brown", "Sandtone");
		themeMap.put("brown_2", "Dark Coffee");
		themeMap.put("d1", "Earthy");
		themeMap.put("emerald", "Radiant Emerald");
		themeMap.put("green_sea", "Green Sea");
		themeMap.put("pink", "Electric Pink");
		themeMap.put("silver", "Metallic");
		themeMap.put("dark_green", "Dark Green");
		themeMap.put("royal_blue","Royal Blue");
		themeMap.put("dark_blue", "Dark Blue");
		themeMap.put("blue_yellow", "Vivid Blue with Yellow dash");
		themeMap.put("custom", "custom");

		themeMap1.put("alizarin", "Rustic");
		themeMap1.put("green_sea", "Green Sea");
		themeMap1.put("peter_river", "Deep Sea");
		themeMap1.put("yellow", "Sunrise");
		themeMap1.put("pink", "Electric Pink");
		themeMap1.put("emerald", "Radiant Emerald");
		themeMap1.put("orange", "Orange");
		themeMap1.put("dark_green", "Dark Green");
		themeMap1.put("royal_blue","Royal Blue");
		themeMap1.put("dark_blue", "Dark Blue");
		themeMap1.put("blue_yellow", "Vivid Blue with Yellow dash");
		themeMap1.put("custom", "custom");
		
		bgImageMap.put("", "No Background");
		bgImageMap.put("bg1", "Panoramic Stadium");
		bgImageMap.put("bg2", "Players In Action");
		bgImageMap.put("bg3", "Furious");
		bgImageMap.put("bg4", "Astroid Balls");
	}

	public List<SponsorDto> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<SponsorDto> sponsors) {
		this.sponsors = sponsors;
	}

	public String getHear() {
		return hear;
	}

	public void setHear(String hear) {
		this.hear = hear;
	}
	
	public String getFname() {
		if(!CommonUtility.isNullOrEmpty(hear)) {
			if(hear.contains(",")) {
				String[] names = hear.split(",");
				if(names != null && names.length > 0)  {
					return names[0];
				}
			}
			return hear;
		}
		return name;
	}
	
	public String getLname() {
		if(!CommonUtility.isNullOrEmpty(hear)) {
			if(hear.contains(","));
			String[] names = hear.split(",");
			if(names != null && names.length > 1)  {
				return names[1];
			}
			return hear;
		}
		return name;
	}
	
	public String getStartupMessage() {
		return startupMessage;
	}

	public void setStartupMessage(String startupMessage) {
		this.startupMessage = startupMessage;
	}

	public int getIsStartupMessageEnabled() {
		return isStartupMessageEnabled;
	}

	public void setIsStartupMessageEnabled(int isStartupMessageEnabled) {
		this.isStartupMessageEnabled = isStartupMessageEnabled;
	}

	public String getEmail() {
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private int currentLeague;

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone == null ? "" : phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getEstableshed() {
		return estableshed == null ? "" : estableshed;
	}

	public void setEstableshed(String estableshed) {
		this.estableshed = estableshed;
	}

	public int getCurrentLeague() {
		return currentLeague;
	}

	public void setCurrentLeague(int currentLeague) {
		this.currentLeague = currentLeague;
	}

	public String getAddress1() {
		return address1 == null ? "" : address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getCity() {
		return city;
	}

	public String getJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getPlayerCommunication() {
		return playerCommunication;
	}

	public void setPlayerCommunication(int playerCommunication) {
		this.playerCommunication = playerCommunication;
	}

	public String getFacebookLink() {
		return facebookLink;
	}

	public void setFacebookLink(String facebookLink) {
		this.facebookLink = facebookLink;
	}

	public String getTwitterLink() {
		return twitterLink;
	}

	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
	}

	public String getYoutubeLink() {
		return youtubeLink;
	}

	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}

	public String getEstimatedCount() {
		return estimatedCount;
	}

	public void setEstimatedCount(String estimatedCount) {
		this.estimatedCount = estimatedCount;
	}

	public List<LeagueDto> getLeagueList() throws Exception {
		return this.getLeagueList(null);
	}
	
	public List<LeagueDto> getAllLeagueList() throws Exception {
		if (this.leagueList == null) {
			this.leagueList = LeagueFactory.getLeagues(clubId);
		}
		return leagueList ;
	}

	public List<LeagueDto> getLeagueList(UserDto user) throws Exception {
		if (this.leagueList == null) {
			this.leagueList = LeagueFactory.getLeagues(clubId);
		}

		List<LeagueDto> filteredLeagues = new ArrayList<LeagueDto>();
		for (LeagueDto league : this.leagueList) {
			if (!league.isHideSeries()) {
				filteredLeagues.add(league);
			} else if (user != null && user.isAdmin(league.getLeagueId())) {
				filteredLeagues.add(league);
			}
		}

		return filteredLeagues;
	}

	public void setclubListSCC(List<ClubDto> clubList) {
		this.clubList = clubList;
	}

	public List<ClubDto> getClubListSCC() throws Exception {
		return this.getClubListSCC(null);
	}

	public List<ClubDto> getClubListSCC(ClubDto club) throws Exception {
		if (this.clubList == null) {
			this.clubList = ClubFactory.getClubListSCC(club);
		}

		List<ClubDto> filteredclubs = new ArrayList<ClubDto>();
		for (ClubDto clubs : this.clubList) {

			filteredclubs.add(clubs);

		}

		return filteredclubs;
	}

	public List<LeagueDto> getLeagueListWithDivisions() throws Exception {
		List<LeagueDto> filteredLeagues = new ArrayList<LeagueDto>();

		for (LeagueDto league : this.getLeagueList()) {
			if (league.isHasDivisions()) {
				filteredLeagues.addAll(league.getDivisions());
			} else {
				filteredLeagues.add(league);
			}
		}

		return filteredLeagues;
	}

	public String getLeagueName(int leagueId) throws Exception {
		List<LeagueDto> leagues = getLeagueList();
		if (leagues != null) {
			for (LeagueDto league : leagues) {
				if (league.isHasDivisions()) {
					for (LeagueDto division : league.getDivisions()) {
						if (division.getLeagueId() == leagueId) {
							return league.getName() + " - " + division.getName();
						}
					}
				} else {
					if (league.getLeagueId() == leagueId) {
						return league.getName();
					}
				}
			}
		}
		return "";
	}

	public LeagueDto getLeague(int leagueId) throws Exception {
		return getLeague(leagueId, null);
	}
	
	public LeagueDto getLeague(int leagueId, UserDto user) throws Exception {
		List<LeagueDto> leagues = getLeagueList(user);
		if (leagues != null) {
			for (LeagueDto league : leagues) {
				if (league.isHasDivisions()) {
					for (LeagueDto division : league.getDivisions()) {
						if (division.getLeagueId() == leagueId) {
							return division;
						}
					}
				} else {
					if (league.getLeagueId() == leagueId) {
						return league;
					}
				}
			}
		}
		LeagueDto series = LeagueFactory.getLeague(leagueId, clubId);
		if (series != null) {
			List<LeagueDto> seriesList = this.getAllLeagueList();
			seriesList.add(series);
			this.setLeagueList(seriesList);
			return series;
		}
		return null;
	}

	public void setLeagueList(List<LeagueDto> leagueList) {
		this.leagueList = leagueList;
	}

	public LeagueDto getCurrentLeagueObject() throws Exception {
		return getCurrentLeagueObject(null);
	}

	public LeagueDto getCurrentLeagueObject(UserDto user) throws Exception {
		LeagueDto league = null;
		List<LeagueDto> filteredLeagues = getLeagueList(user);

		if (filteredLeagues != null && !filteredLeagues.isEmpty() && this.currentLeague != 0) {
			for (LeagueDto dto : filteredLeagues) {
				if (dto.getLeagueId() == this.currentLeague) {
					return dto;
				}
			}
			return filteredLeagues.get(filteredLeagues.size() - 1);
		}

		return league;
	}

	public String getCurrentLeagueName(UserDto user) throws Exception {

		if (getCurrentLeagueObject(user) != null) {
			getCurrentLeagueObject(user).getName();
		}
		return "";
	}

	public String getCurrentLeagueName() throws Exception {

		if (getCurrentLeagueObject() != null) {
			return getCurrentLeagueObject().getName();
		}
		return "";
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public String getStartupImagePath1() {
		return "/documentsRep/startup/" + this.clubId + "-startup.jpg";
	}

	public SponsorDto getSponsor(String sponsorId) {
		if (sponsorId != null && !"".equals(sponsorId) && !"0".equals(sponsorId) && this.sponsors != null) {
			int sponsor = CommonUtility.stringToInt(sponsorId);
			for (Iterator<SponsorDto> itr = this.sponsors.iterator(); itr.hasNext();) {
				SponsorDto sponsorDto = itr.next();
				if (sponsorDto.getSponsorId() == sponsor) {
					return sponsorDto;
				}
			}

		}
		return null;
	}

	public boolean isLogoExists() {
		try {
			String logoPathUrl = this.getLogo_file_path();
			if (CommonUtility.isNullOrEmpty(logoPathUrl)) {
				logoPathUrl = ClubFactory.getActiveLiteClub(this.clubId, true).getLogo_file_path();
			}
			File dir1 = new File(logoPathUrl);
			if (!dir1.getPath().isEmpty()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	public String getLogoAttributes() throws IOException {
		String returnStr = "";
		try {
			String logoPathUrl = this.getLogo_file_path();
			if (CommonUtility.isNullOrEmpty(logoPathUrl)) {
				logoPathUrl = ClubFactory.getActiveLiteClub(this.clubId, true).getLogo_file_path();
			}
			File dir1 = new File(logoPathUrl);
			if (dir1.exists()) {
				BufferedImage image = ImageIO.read(dir1);
				int width = image.getWidth();
				int height = image.getHeight();
				if (height > 100) {
					width = (width * 100) / height;
					height = 100;
				}

				returnStr += " style='width:" + width + "px;height:" + height + "px;' ";
			} else {
				returnStr += " style='width:" + 100 + "px;height:" + 100 + "px;' ";
			}
		} catch (Exception e) {
			returnStr += " style='width:" + 100 + "px;height:" + 100 + "px;' ";
		}
		return returnStr;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getShortURL() {
		if (CommonUtility.isNullOrEmpty(this.shortURL)) {
			return this.name.replaceAll("\\s", "");
		}
		return shortURL;
	}

	public void setShortURL(String shortURL) {
		if (!CommonUtility.isNullOrEmpty(shortURL)) {
			this.shortURL = shortURL.replaceAll("[^A-Za-z0-9]", "").trim();
		} else {
			this.shortURL = this.name.replaceAll("[^A-Za-z0-9]", "").trim();
		}
	}

	public String getTheme() {
		if (CommonUtility.isNullOrEmpty(theme) || themeMap.get(theme) == null) {
			return "peter_river";
		}
		return theme;
	}

	public String getTheme1() {
		if (CommonUtility.isNullOrEmpty(theme) || themeMap1.get(theme) == null) {
			return "alizarin";
		}
		return theme;
	}

	public String getThemeName() {
		return themeMap.get(getTheme());
	}

	public String getBgImageName() {
		return bgImageMap.get(getBgImage());
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getBgImage() {
		return bgImage;
	}

	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}

	public Map<String, String> getThemeMap() {
		return themeMap;
	}

	public void setThemeMap(Map<String, String> themeMap) {
		this.themeMap = themeMap;
	}

	public Map<String, String> getBgImageMap() {
		return bgImageMap;
	}

	public void setBgImageMap(Map<String, String> bgImageMap) {
		this.bgImageMap = bgImageMap;
	}

	public boolean isLiveScoringEnabled() {
		return (isLiveScoringEnabled == 1);
	}

	public void setIsLiveScoringEnabled(int isLiveScoringEnabled) {
		this.isLiveScoringEnabled = isLiveScoringEnabled;
	}

	public boolean getCanScorerAddPlayer() {
		return "1".equals(canScorerAddPlayer);
	}

	public void setCanScorerAddPlayer(String canScorerAddPlayer) {
		this.canScorerAddPlayer = canScorerAddPlayer;
	}

	public boolean getShowMatchSlider() {
		return "1".equals(showMatchSlider);
	}

	public void setShowMatchSlider(String showMatchSlider) {
		this.showMatchSlider = showMatchSlider;
	}

	public boolean getDisableBannerAd() {
		return "1".equals(disableBannerAd);
	}

	public String getDisableBannerAdS() {
		return disableBannerAd;
	}

	public void setDisableBannerAd(String disableBannerAd) {
		this.disableBannerAd = disableBannerAd;
	}

	public boolean doNotShowClubName() {
		return "1".equals(doNotShowClubName);
	}

	public void setDoNotShowClubName(String doNotShowClubName) {
		this.doNotShowClubName = doNotShowClubName;
	}

	public boolean clubSponsorsOnTop() {
		return "1".equals(clubSponsorsOnTop);
	}

	public void setClubSponsorsOnTop(String clubSponsorsOnTop) {
		this.clubSponsorsOnTop = clubSponsorsOnTop;
	}

	public int getSponsorsCount() {
		return sponsorsCount;
	}

	public void setSponsorsCount(int sponsorsCount) {
		this.sponsorsCount = sponsorsCount;
	}

	public List<SponsorDto> getPremiumSponsors() {
		List<SponsorDto> premiumSponsors = new ArrayList<SponsorDto>();
		if (sponsors != null && !sponsors.isEmpty()) {
			for (SponsorDto sponsor : sponsors) {
				if (sponsor.showOnTop()) {
					premiumSponsors.add(sponsor);
				}
			}
		}

		return premiumSponsors;
	}

	public List<SponsorDto> getRegularSponsors() {
		List<SponsorDto> regularSponsors = new ArrayList<SponsorDto>();
		if (sponsors != null && !sponsors.isEmpty()) {
			for (SponsorDto sponsor : sponsors) {
				if (!sponsor.showOnTop()) {
					regularSponsors.add(sponsor);
				}
			}
		}

		return regularSponsors;
	}

	public String getPlayerTerms() {
		return playerTerms;
	}

	public void setPlayerTerms(String playerTerms) {
		this.playerTerms = playerTerms;
	}

	public boolean isShowPointsTable() {
		return showPointsTable;
	}

	public void setShowPointsTable(boolean showPointsTable) {
		this.showPointsTable = showPointsTable;
	}

	public boolean isAllowCustomPlayerId() {
		return allowCustomPlayerId;
	}

	public void setAllowCustomPlayerId(boolean allowCustomPlayerId) {
		this.allowCustomPlayerId = allowCustomPlayerId;
	}

	public boolean isClubStructureEnabled() {
		return clubStructureEnabled;
	}

	public void setClubStructureEnabled(boolean clubStructureEnabled) {
		this.clubStructureEnabled = clubStructureEnabled;
	}

	public int getClubAdmin2() {
		return clubAdmin2;
	}

	public void setClubAdmin2(int clubAdmin2) {
		this.clubAdmin2 = clubAdmin2;
	}

	public int getClubAdmin1() {
		return clubAdmin1;
	}

	public void setClubAdmin1(int clubAdmin1) {
		this.clubAdmin1 = clubAdmin1;
	}

	public String getCustomDomain() {
		return customDomain;
	}

	public void setCustomDomain(String customDomain) {
		this.customDomain = customDomain;
	}

	public String getThemeFolder() {
		return themeFolder;
	}

	public void setThemeFolder(String themeFolder) {
		this.themeFolder = themeFolder;
	}

	public int getDisableOtherAds() {
		return disableOtherAds;
	}

	public void setDisableOtherAds(int disableOtherAds) {
		this.disableOtherAds = disableOtherAds;
	}

	public Date getTrialDate() {
		return trialDate;
	}

	public void setTrialDate(Date trialDate) {
		this.trialDate = trialDate;
	}

	public boolean isWithinTrial() {
		return CommonUtility.isWithinRange(trialDate, 7);
	}

	public boolean isRemoveCCAds() {
		return removeCCAds;
	}

	public void setRemoveCCAds(boolean removeCCAds) {
		this.removeCCAds = removeCCAds;
	}

	public String getLogo_file_path() {
		return logo_file_path;
	}

	public void setLogo_file_path(String logo_file_path) {
		this.logo_file_path = logo_file_path;
	}

	public int getIsPhotoMandatory() {
		return isPhotoMandatory;
	}

	public void setIsPhotoMandatory(int isPhotoMandatory) {
		this.isPhotoMandatory = isPhotoMandatory;
	}

	public boolean canAssignScorersToFixtures() {
		return "sca".equalsIgnoreCase(association);
	}

	@Override
	public String toString() {
		return "ClubDto [clubId=" + clubId + ", name=" + name + ", city=" + city + ", phone=" + phone + ", email="
				+ email + ", leagueList=" + (leagueList != null ? leagueList.size() : 0) + ", clubList="
				+ (clubList != null ? clubList.size() : 0) + "]";
	}
	
	public boolean isMLC() {
		return this.clubId == 22187;
	}

	public boolean isATCL() {
		return this.clubId == 64;
	}

	public boolean isSCA() {
		return this.clubId == 7683;
	}

	public boolean isBCMCL() {
		return this.clubId == 10126;
	}

	public boolean isSweden() {
		return this.clubId == 8318;
	}

	public boolean isScore360() {
		return this.clubId == 12047;
	}

	public boolean isMCA() {
		return this.clubId == 9262;
	}

	public boolean isEspana() {
		return this.clubId == 13753;
	}

	public boolean isNJSBCL() {
		return this.clubId == 2690;
	}

	public boolean isHotstar() {
		if (this.clubId == 8676 || this.clubId == 8675) {
			return false;
		}
		if (this.country.equalsIgnoreCase("USA")) {
			return true;
		}
		return false;
	}

	public boolean isReportHide() {
		if (this.clubId == 10126) {
			return true;
		}
		return false;
	}

	public LeagueDto getLatestClubStructureEnabledLeague() throws Exception {
		if (this.getCurrentLeagueObject() != null && this.getCurrentLeagueObject().isClubStructureEnabled()) {
			return this.getCurrentLeagueObject();
		} else if (this.getLeagueList() != null) {
			for (LeagueDto league : this.getLeagueList()) {
				if (league.isClubStructureEnabled()) {
					return league;
				}
			}

		}
		return null;
	}

	public int getPaymentStaus() {
		return paymentStaus;
	}

	public void setPaymentStaus(int paymentStaus) {
		this.paymentStaus = paymentStaus;
	}

	public String getStartupImagePath() {
		if(!CommonUtility.isNullOrEmpty(startupImagePath)) {
			return startupImagePath;
		}else {
			return "";
		}	
	}

	public void setStartupImagePath(String startupImagePath) {
		this.startupImagePath = startupImagePath;
	}
	
	public int getIsAcademy() {
		return isAcademy;
	}

	public void setIsAcademy(int isAcademy) {
		this.isAcademy = isAcademy;
	}

	public String getBackGroundImagePath() {
		return backGroundImagePath;
	}

	public void setBackGroundImagePath(String backGroundImagePath) {
		this.backGroundImagePath = backGroundImagePath;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public int getIsStreamingAllowed() {
		return isStreamingAllowed;
	}

	public void setIsStreamingAllowed(int isStreamingAllowed) {
		this.isStreamingAllowed = isStreamingAllowed;
	}

	public String getSrcSite() {
		return srcSite;
	}

	public void setSrcSite(String srcSite) {
		this.srcSite = srcSite;
	}

	public int getSrcLeagueId() {
		return srcLeagueId;
	}

	public void setSrcLeagueId(int srcLeagueId) {
		this.srcLeagueId = srcLeagueId;
	}
	
	public int getClubAdminPlayerReg() {
		return clubAdminPlayerReg;
	}

	public void setClubAdminPlayerReg(int clubAdminPlayerReg) {
		this.clubAdminPlayerReg = clubAdminPlayerReg;
	}

	public int getIsUmpireReportMandatory() {
		return isUmpireReportMandatory;
	}

	public void setIsUmpireReportMandatory(int isUmpireReportMandatory) {
		this.isUmpireReportMandatory = isUmpireReportMandatory;
	}

	public int getIsCaptainReportMandatory() {
		return isCaptainReportMandatory;
	}

	public void setIsCaptainReportMandatory(int isCaptainReportMandatory) {
		this.isCaptainReportMandatory = isCaptainReportMandatory;
	}

	public String getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
	}

	public String getMenuColor() {
		return menuColor;
	}

	public void setMenuColor(String menuColor) {
		this.menuColor = menuColor;
	}

	public int getUnlimitedStreaming() {
		return unlimitedStreaming;
	}

	public void setUnlimitedStreaming(int unlimitedStreaming) {
		this.unlimitedStreaming = unlimitedStreaming;
	}

	public int getSportId() {
		return sportId;
	}

	public String getSportName(int sportId) {
		String sportType = "";
		if (sportId == 1) {
			sportType = "Cricket";
		}
		if (sportId == 2) {
			sportType = "Football";
		}
		return sportType;
	}

	public void setSportId(int sportId) {
		this.sportId = sportId;
	}
	
	public boolean isCricket() {		
		if(this.sportId == 1){
			return true;		
		}		
		return false;		
	}	
	
	public boolean isFootBall() {		
		if(this.sportId == 2){
			return true;		
		}		
		return false;		
	}

	public int getGroundBookingRequired() {
		if (this.groundBookingRequired == 1) {
			return 1;
		}
		return 0;
	}


	public void setGroundBookingRequired(int groundBookingRequired) {
		this.groundBookingRequired = groundBookingRequired;
	}	
	public boolean isPremium() {		
		if(this.themeFolder != null && this.themeFolder.equalsIgnoreCase("premium")){
			return true;		
		}		
		return false;		
	}
	
	public boolean isTheme2() {		
		if(this.themeFolder != null && this.themeFolder.equalsIgnoreCase("theme2")){
			return true;		
		}		
		return false;		
	}

}
