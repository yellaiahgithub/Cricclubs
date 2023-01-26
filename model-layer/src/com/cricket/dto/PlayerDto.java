/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.io.Serializable;
import java.util.Date;

import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.utility.CommonUtility;

/**
 * @author ganesh
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PlayerDto implements Comparable<Object>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4677480746955708501L;

	public static int ACTIVE = 1;
	public static int NEW_PLAYER = 2;
	public static int INACTIVE = 3;
	public static int NEW_PLAYER_CLUB_ADMIN = 4;

	private int playerID;
	private String firstName;
	private String lastName;
	private String jerseyNumber;
	private String emercencyContactNo = "";
	private String emercencyContactName = "";
	private String emercencyContactRel = "";
	private String dateOfBirth = "";
	private String gender;
	private String nickName = "";
	private String battingStyle = "";
	private String bowlingStyle = "";
	private String canPlayerplayforanyteam = "";
	private String teamsPlayed = "";
	private String profileDesc = "";
	private String testimonial = "";
	private String season = "";
	private String datePlayed = "";
	private Date createDate;
	private String isActive = "";
	private String contactNo = "";
	private String address = "";
	private String email = "";
	private String playingRole = "";
	private boolean availablePlayersOnly = false;
	private boolean selectedPlayersOnly = false;
	private int teamId;
	private String teamName = "";
	private String teamCode = "";
	private int points;
	private int battingPoints;
	private int bowlingPoints;
	private int fieldingPoints;
	private int otherPoints;
	private int matchesPlayed;
	private int leagueId;
	private boolean acceptedTerms;
	private int clubId;
	private boolean isSecondary;
	private String customId;
	private String srcPlayerId;
	private String sourceSite;
	private int sourceLeagueId;
	private String lockedSeriesName;
	private String profilepic_file_path;
	private String teamlogo_file_path;
	private String residentID = "";
	private String passportNum = "";
	private String bornInKSA = "";
	private String passportExpiry = "";
	private String KSAFirstEntry = "";
	private boolean canPlayAnyteam;
	private boolean canPlayWithinClub;
	private String internalClub;
	private boolean isInternalClubAdd;
	private String clubName;
	private String category;
	private int isClubAdminApproval;
	private int roleId;
	private String nationality;
	private String idTypeOman;
	private String idNumberOman;
	private String idExpiryDate;
	private String idProofPath;
	private String omanFirstEntryDate;
	private String omanNoOfyearsStay;
	
	public static int getACTIVE() {
		return ACTIVE;
	}

	public static void setACTIVE(int aCTIVE) {
		ACTIVE = aCTIVE;
	}

	public static int getNEW_PLAYER() {
		return NEW_PLAYER;
	}

	public static void setNEW_PLAYER(int nEW_PLAYER) {
		NEW_PLAYER = nEW_PLAYER;
	}

	public static int getINACTIVE() {
		return INACTIVE;
	}

	public static void setINACTIVE(int iNACTIVE) {
		INACTIVE = iNACTIVE;
	}

	public static int getNEW_PLAYER_CLUB_ADMIN() {
		return NEW_PLAYER_CLUB_ADMIN;
	}

	public static void setNEW_PLAYER_CLUB_ADMIN(int nEW_PLAYER_CLUB_ADMIN) {
		NEW_PLAYER_CLUB_ADMIN = nEW_PLAYER_CLUB_ADMIN;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getJerseyNumber() {
		return jerseyNumber;
	}

	public String getEmercencyContactNo() {
		return emercencyContactNo;
	}

	public void setEmercencyContactNo(String emercencyContactNo) {
		this.emercencyContactNo = emercencyContactNo;
	}

	public String getEmercencyContactName() {
		return emercencyContactName;
	}

	public void setEmercencyContactName(String emercencyContactName) {
		this.emercencyContactName = emercencyContactName;
	}

	public String getEmercencyContactRel() {
		return emercencyContactRel;
	}

	public void setEmercencyContactRel(String emercencyContactRel) {
		this.emercencyContactRel = emercencyContactRel;
	}

	public boolean isCanPlayWithinClub() {
		return canPlayWithinClub;
	}

	public void setCanPlayWithinClub(boolean canPlayWithinClub) {
		this.canPlayWithinClub = canPlayWithinClub;
	}

	public void setJerseyNumber(String jerseyNumber) {
		this.jerseyNumber = jerseyNumber;
	}

	public String getInternalClub() {
		return internalClub;
	}

	public void setInternalClub(String internalClub) {
		this.internalClub = internalClub;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isCanPlayAnyteam() {
		return canPlayAnyteam;
	}

	public void setCanPlayAnyteam(boolean canPlayAnyteam) {
		this.canPlayAnyteam = canPlayAnyteam;
	}

	public String getSrcPlayerId() {
		return srcPlayerId;
	}

	public void setSrcPlayerId(String srcPlayerId) {
		this.srcPlayerId = srcPlayerId;
	}

	public String getResidentID() {
		return residentID;
	}

	public void setResidentID(String residentID) {
		this.residentID = residentID;
	}

	public String getPassportNum() {
		return passportNum;
	}

	public void setPassportNum(String passportNum) {
		this.passportNum = passportNum;
	}

	public String getBornInKSA() {
		return bornInKSA;
	}

	public void setBornInKSA(String bornInKSA) {
		this.bornInKSA = bornInKSA;
	}

	public String getPassportExpiry() {
		return passportExpiry;
	}

	public void setPassportExpiry(String passportExpiry) {
		this.passportExpiry = passportExpiry;
	}

	public String getKSAFirstEntry() {
		return KSAFirstEntry;
	}

	public void setKSAFirstEntry(String kSAFirstEntry) {
		KSAFirstEntry = kSAFirstEntry;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getTeamlogo_file_path() {
		return teamlogo_file_path;
	}

	public void setTeamlogo_file_path(String teamlogo_file_path) {
		this.teamlogo_file_path = teamlogo_file_path;
	}

	public String getProfilepic_file_path() {
		if (CommonUtility.isNullOrEmpty(profilepic_file_path)) {
			if ("F".equalsIgnoreCase(this.gender)) {
				return "/documentsRep/profilePics/default-female-Image.jpg";
			} else {
				return "/documentsRep/profilePics/no_image.png";
			}

		}
		return profilepic_file_path;
	}

	public String getDBProfilepic_file_path() {
		return profilepic_file_path;
	}

	public String getRealProfilepicFilePath() {
		return profilepic_file_path;
	}

	public void setProfilepic_file_path(String profilepic_file_path) {
		this.profilepic_file_path = profilepic_file_path;
	}

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	public int getBattingPoints() {
		return battingPoints;
	}

	public void setBattingPoints(int battingPoints) {
		this.battingPoints = battingPoints;
	}

	public int getBowlingPoints() {
		return bowlingPoints;
	}

	public void setBowlingPoints(int bowlingPoints) {
		this.bowlingPoints = bowlingPoints;
	}

	public int getFieldingPoints() {
		return fieldingPoints;
	}

	public void setFieldingPoints(int fieldingPoints) {
		this.fieldingPoints = fieldingPoints;
	}

	public int getOtherPoints() {
		return otherPoints;
	}

	public void setOtherPoints(int otherPoints) {
		this.otherPoints = otherPoints;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return
	 */
	public String getBattingStyle() {
		return battingStyle;
	}

	/**
	 * @return
	 */
	public String getBowlingStyle() {
		return bowlingStyle;
	}

	public String getCanPlayerplayforanyteam() {

		return canPlayerplayforanyteam;
	}

	/**
	 * @return
	 */
	public String getContactNo() {
		return contactNo;
	}

	/**
	 * @return
	 */
	public String getDatePlayed() {
		return datePlayed;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		if (CommonUtility.isNullOrEmpty(email) || "null".equals(email)) {
			return "";
		}
		return email;
	}

	/**
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @return
	 */
	public int getPlayerID() {
		return playerID;
	}

	public String getPlayerOrCustomID(ClubDto club) {
		if (club.isAllowCustomPlayerId()) {
			if (!CommonUtility.isNullOrEmpty(this.customId)) {
				return this.customId;
			}
		}
		return playerID + "";
	}

	public String getPlayerOrCustomID(ClubDtoLite club) {
		if (club.isAllowCustomPlayerId()) {
			if (!CommonUtility.isNullOrEmpty(this.customId)) {
				return this.customId;
			}
		}
		return playerID + "";
	}

	/**
	 * @return
	 */
	public String getProfileDesc() {
		return profileDesc;
	}

	/**
	 * @return
	 */
	public String getSeason() {
		return season;
	}

	/**
	 * @return
	 */
	public String getTeamsPlayed() {
		return teamsPlayed;
	}

	/**
	 * @return
	 */
	public String getTestimonial() {
		return testimonial;
	}

	/**
	 * @param string
	 */
	public void setAddress(String string) {
		address = string;
	}

	/**
	 * @param string
	 */
	public void setBattingStyle(String string) {
		battingStyle = string;
	}

	/**
	 * @param string
	 */
	public void setBowlingStyle(String string) {
		bowlingStyle = string;
	}

	public void setCanPlayerplayforanyteam(String string) {
		canPlayerplayforanyteam = string;
	}

	/**
	 * @param string
	 */
	public void setContactNo(String string) {
		contactNo = string;
	}

	/**
	 * @param string
	 */
	public void setDatePlayed(String string) {
		datePlayed = string;
	}

	/**
	 * @param string
	 */
	public void setEmail(String string) {
		email = string;
	}

	/**
	 * @param string
	 */
	public void setFirstName(String string) {
		firstName = string;
	}

	/**
	 * @param string
	 */
	public void setIsActive(String string) {
		isActive = string;
	}

	/**
	 * @param string
	 */
	public void setLastName(String string) {
		lastName = string;
	}

	/**
	 * @param string
	 */
	public void setNickName(String string) {
		nickName = string;
	}

	/**
	 * @param string
	 */
	public void setPlayerID(int playerid) {
		playerID = playerid;
	}

	/**
	 * @param string
	 */
	public void setProfileDesc(String string) {
		profileDesc = string;
	}

	/**
	 * @param string
	 */
	public void setSeason(String string) {
		season = string;
	}

	/**
	 * @param string
	 */
	public void setTeamsPlayed(String string) {
		teamsPlayed = string;
	}

	/**
	 * @param string
	 */
	public void setTestimonial(String string) {
		testimonial = string;
	}

	/**
	 * @return
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param string
	 */
	public void setDateOfBirth(String string) {
		dateOfBirth = string;
	}

	/**
	 * @return
	 */
	public boolean isAvailablePlayersOnly() {
		return availablePlayersOnly;
	}

	/**
	 * @param b
	 */
	public void setAvailablePlayersOnly(boolean b) {
		availablePlayersOnly = b;
	}

	/**
	 * @return
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * @param i
	 */
	public void setTeamId(int i) {
		teamId = i;
	}

	/**
	 * @return
	 */
	public String getPlayingRole() {
		return playingRole;
	}

	/**
	 * @param string
	 */
	public void setPlayingRole(String string) {
		playingRole = string;
	}

	/**
	 * @return
	 */
	public String getTeamName() {
		return teamName == null ? "" : teamName;
	}

	/**
	 * @param string
	 */
	public void setTeamName(String string) {
		teamName = string;
	}

	public String getPlayerRoleImage() {

		if ("".equals(this.playingRole)) {
			return "";
		}
		if ("Batsman".equals(this.playingRole)) {
			return CommonUtility.getCDN() + "/images/batsman.png";
		}
		if ("Bowler".equals(this.playingRole)) {
			return CommonUtility.getCDN() + "/images/bowler.png";
		}
		if ("All Rounder".equals(this.playingRole)) {
			return CommonUtility.getCDN() + "/images/allrounder.png";
		}
		if ("Wicket Keeper".equals(this.playingRole)) {
			return CommonUtility.getCDN() + "/images/wicketkeeper.png";
		}

		return "";
	}

	public int compareTo(Object o1) {
		return (((PlayerDto) o1).getPoints()) - this.points;
	}

	public String getFullName() {
		if (!CommonUtility.isNullOrEmpty(this.firstName)) {
			this.firstName = this.firstName.trim();
		}
		if (!CommonUtility.isNullOrEmpty(this.lastName)) {
			this.lastName = this.lastName.trim();
		}
		String fullname = this.firstName + " " + this.lastName;
		return CommonUtility.toDisplayCase(!CommonUtility.isNullOrEmptyOrNULL(fullname) ? fullname.trim() : "");
	}

	public int getMOMCount() {
		if (otherPoints == 0) {
			return 0;
		} else {
			return otherPoints / 50;
		}
	}

	public boolean isSelectedPlayersOnly() {
		return selectedPlayersOnly;
	}

	public void setSelectedPlayersOnly(boolean selectedPlayersOnly) {
		this.selectedPlayersOnly = selectedPlayersOnly;
	}

	public boolean isVerified() {
		if (!CommonUtility.isNullOrEmpty(this.email)) {
			return true;
		}
		return false;
	}

	public boolean isAcceptedTerms() {
		return acceptedTerms;
	}

	public void setAcceptedTerms(boolean acceptedTerms) {
		this.acceptedTerms = acceptedTerms;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public boolean isSecondary() {
		return isSecondary;
	}

	public void setSecondary(boolean isSecondary) {
		this.isSecondary = isSecondary;
	}

	public String getCustomId() {
		return (customId == null || "null".equals(customId)) ? "" : customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getLockedSeriesName() {
		return lockedSeriesName;
	}

	public void setLockedSeriesName(String lockedSeriesName) {
		this.lockedSeriesName = lockedSeriesName;
	}

	public boolean isLocked() {
		return !CommonUtility.isNullOrEmpty(lockedSeriesName);
	}

	@Override
	public boolean equals(Object player) {
		return this.playerID == ((PlayerDto) player).playerID;
	}

	public boolean isInternalClubAdd() {
		return isInternalClubAdd;
	}

	public void setInternalClubAdd(boolean isInternalClubAdd) {
		this.isInternalClubAdd = isInternalClubAdd;
	}

	public String getPlayerShortName(int firstNameFirst) throws Exception {
		if (firstNameFirst == 1) {
			return getPlayerShortLastName();
		}
		return getPlayerShortFirstName();
	}

	public String getPlayerShortLastName() {
		String name = "";
		if (CommonUtility.isNullOrEmptyOrNULL(this.lastName)) {
			return CommonUtility.toDisplayCase(this.getFirstName().trim());
		}
		if (!CommonUtility.isNullOrEmpty(this.getFirstName()) && this.getFirstName().length() > 0) {
			name = this.getLastName().trim() + " " + this.getFirstName().trim().charAt(0);
			name = CommonUtility.toDisplayCase(name);
		}
		if (CommonUtility.isNullOrEmpty(this.getFirstName()) && !CommonUtility.isNullOrEmpty(this.getLastName())) {
			return CommonUtility.toDisplayCase(this.getLastName().trim());
		}
		return name;
	}

	public String getPlayerShortFirstName() {
		if (CommonUtility.isNullOrEmptyOrNULL(this.lastName)) {
			return CommonUtility.toDisplayCase(this.getFirstName().trim());
		}
		return this.getFirstName().trim() + " " + this.getLastName().trim().charAt(0);

	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSourceSite() {
		return sourceSite;
	}

	public void setSourceSite(String sourceSite) {
		this.sourceSite = sourceSite;
	}

	public int getSourceLeagueId() {
		return sourceLeagueId;
	}

	public void setSourceLeagueId(int sourceLeagueId) {
		this.sourceLeagueId = sourceLeagueId;
	}

	public int getIsClubAdminApproval() {
		return isClubAdminApproval;
	}

	public void setIsClubAdminApproval(int isClubAdminApproval) {
		this.isClubAdminApproval = isClubAdminApproval;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getIdTypeOman() {
		return idTypeOman;
	}

	public void setIdTypeOman(String idTypeOman) {
		this.idTypeOman = idTypeOman;
	}

	public String getIdNumberOman() {
		return idNumberOman;
	}

	public void setIdNumberOman(String idNumberOman) {
		this.idNumberOman = idNumberOman;
	}

	public String getIdExpiryDate() {
		return idExpiryDate;
	}

	public void setIdExpiryDate(String idExpiryDate) {
		this.idExpiryDate = idExpiryDate;
	}

	public String getIdProofPath() {
		return idProofPath;
	}

	public void setIdProofPath(String idProofPath) {
		this.idProofPath = idProofPath;
	}

	public String getOmanFirstEntryDate() {
		return omanFirstEntryDate;
	}

	public void setOmanFirstEntryDate(String omanFirstEntryDate) {
		this.omanFirstEntryDate = omanFirstEntryDate;
	}

	public String getOmanNoOfyearsStay() {
		return omanNoOfyearsStay;
	}

	public void setOmanNoOfyearsStay(String omanNoOfyearsStay) {
		this.omanNoOfyearsStay = omanNoOfyearsStay;
	}

}
