/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.PlayerFactory;
import com.cricket.dao.TeamFactory;
import com.cricket.dao.UserFactory;
import com.cricket.utility.CommonUtility;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UserDto implements Serializable {
	static Logger log = LoggerFactory.getLogger(UserDto.class);
	private static final long serialVersionUID = 1054488058792213753L;

	private int userID;
	private String userName = "";
	private String password = "";
	private String fname = "";
	private String lname = "";
	private String gender;
	private String dateOfBirth;
	private String email = "";
	private String phone = "";
	private String address = "";
	private String city = "";
	private String state = "";
	private String countryCode = "";
	private String postalCode = "";
	private int isActive;
	private String teamOffRole;
	private int playerID;
	private int umpireID;
	private String clubName;
	private String accessLevel = "";
	private int clubId;
	private boolean isCaptain = false;
	private String playerProfileSearch = "";
	private String token;
	private List<Integer> roles = new ArrayList<Integer>();
	private List<Integer> seriesAdmin = new ArrayList<Integer>();
	private List<Integer> teams = null;
	private List<Integer> internalClubIds = null;
	private List<Integer> captainTeams = null;
	private List<Integer> adminClubs = null;
	private String profileImagePath = "";
	private String userTypeId;
	private String userType;
	private String backGroundImagePath;
	private int noOfMatchesStreaming;
	private String addRequestFrom;
	
	
	private int mobileValidation=0;
	private int emailValidation=0;
	
	public int getMobileValidation() {
		return mobileValidation;
	}

	public void setMobileValidation(int mobileValidation) {
		this.mobileValidation = mobileValidation;
	}

	public int getEmailValidation() {
		return emailValidation;
	}

	public void setEmailValidation(int emailValidation) {
		this.emailValidation = emailValidation;
	}
	
	private Map<Integer, String> rolesMap = new TreeMap<Integer, String>();

	{
		/* rolesMap.put(0, "Admin"); */
		rolesMap.put(1, "League Admin");
		rolesMap.put(2, "Team Admin");
		rolesMap.put(3, "Team Scorer");
		rolesMap.put(4, "League Scorer");
		rolesMap.put(5, "Series Admin");
		rolesMap.put(6, "Scheduler");
		rolesMap.put(7, "Umpire Admin");
		rolesMap.put(8, "Scorer Admin");
	}

	public String getTeamOffRole() {
		return teamOffRole;
	}

	public void setTeamOffRole(String teamOffRole) {
		this.teamOffRole = teamOffRole;
	}

	public UserDto() {
		super();
	}

	public UserDto(String userName, String password, String email, int isActive, int playerID, int umpireID,
			String fname, String lname, String phone, String address, String city, String state, String postalCode,
			String profileImagePath) {
		super();
		this.userName = userName;
		this.password = password;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.isActive = isActive;
		this.playerID = playerID;
		this.umpireID = umpireID;
		this.profileImagePath = profileImagePath;
	}
	

	public String getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<Integer> getSeriesAdmin() {
		return seriesAdmin;
	}

	public void setSeriesAdmin(List<Integer> seriesAdmin) {
		this.seriesAdmin = seriesAdmin;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getPlayerProfileSearch() {
		return playerProfileSearch;
	}

	public void setPlayerProfileSearch(String playerProfileSearch) {
		this.playerProfileSearch = playerProfileSearch;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	/**
	 * @return
	 */
	public String getAccessLevel() {
		return accessLevel;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @return
	 */
	public int getIsActive() {
		return isActive;
	}

	/**
	 * @return
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * @return
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param string
	 */
	public void setAccessLevel(String string) {
		accessLevel = string;
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
	public void setFname(String string) {
		fname = string;
	}

	/**
	 * @param string
	 */
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	/**
	 * @param string
	 */
	public void setLname(String string) {
		lname = string;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string) {
		password = string;
	}

	/**
	 * @param i
	 */
	public void setPlayerID(int i) {
		playerID = i;
	}

	/**
	 * @param i
	 */
	public void setUserID(int i) {
		userID = i;
	}

	/**
	 * @param string
	 */
	public void setUserName(String string) {
		userName = string;
	}

	/**
	 * @param string
	 */
	public boolean isAdmin(int seriesId) {
		if ("1".equals(this.accessLevel) || "99".equals(this.accessLevel)) {
			return true;
		}

		if (this.roles != null && this.roles.contains(1)) {
			return true;
		}

		if (this.seriesAdmin.contains(seriesId)) {
			return true;
		}

		return false;
	}

	/**
	 * @param string
	 */
	public boolean isAdmin() {
		if ("1".equals(this.accessLevel) || "99".equals(this.accessLevel)) {
			return true;
		}

		if (this.roles != null && this.roles.contains(1)) {
			return true;
		}
		return false;
	}

	public boolean isFantasyAdmin() {
		if ("88".equals(this.accessLevel)) {
			return true;
		}		
		return false;
	}

	public boolean isScheduler() {
		if (this.roles != null && this.roles.contains(6)) {
			return true;
		}
		return false;
	}

	public boolean isSeriesAdmin(int seriesId) {
		if (!CommonUtility.isListNullEmpty(seriesAdmin) && this.seriesAdmin.contains(seriesId)) {
			return true;
		}
		return false;
	}
	
	public boolean isSeriesAdmin() {
		return !CommonUtility.isListNullEmpty(seriesAdmin);
	}

	public boolean isSuperAdmin() {
		if ("99".equals(this.accessLevel)) {
			return true;
		}
		return false;
	}

	/**
	 * @param string
	 */
	public boolean isLeagueScorer() {

		if (this.roles != null && this.roles.contains(4)) {
			return true;
		}
		return false;
	}

	/**
	 * @param string
	 */
	public boolean isPlayer() {
		if (this.playerID != 0) {
			return true;
		}
		return false;
	}

	/**
	 * @param string
	 */
	public boolean isUmpire() {
		if (this.umpireID != 0) {
			return true;
		}
		return false;
	}
	
	public boolean isUmpireAdmin() {
		if (this.roles != null && this.roles.contains(7)) {
			return true;
		}
		return false;
	}

	public boolean isScorerAdmin() {
		if (this.roles != null && this.roles.contains(8)) {
			return true;
		}
		return false;
	}
	
	public boolean isCaptain() {
		if (isCaptain) {
			return true;
		}

		if (this.roles != null && this.roles.contains(2)) {
			return true;
		}

		if (!getAdminClubs().isEmpty()) {
			return true;
		}

		return false;
	}
	
	public boolean isClubAdmin() {		
		if (!getAdminClubs().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isTeamAdmin() {

		if (this.roles != null && this.roles.contains(2)) {
			return true;
		}

		if (!getAdminClubs().isEmpty()) {
			return true;
		}

		return false;
	}

	public boolean isCaptain(int teamId, int clubId) {
		if (isTeamAdmin() && getTeams().contains(teamId)) {
			return true;
		} else if (isCaptain && getCaptainTeams().contains(teamId)) {
			return true;
		} else if (clubId > 0 && this.playerID > 0) {
			return getAdminClubs().contains(clubId);
		}
		return false;
	}

	public void setIsCaptain(boolean isCaptain) {
		this.isCaptain = isCaptain;
	}

	public List<Integer> getRoles() {
		if (roles == null)
			roles = new ArrayList<Integer>();
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}

	public Map<Integer, String> getRolesMap() {
		return rolesMap;
	}

	public void setRolesMap(Map<Integer, String> rolesMap) {
		this.rolesMap = rolesMap;
	}

	public String getRolesString() {
		String roles = "";
		if (this.roles != null && !this.roles.isEmpty()) {
			boolean first = false;
			for (Integer role : this.roles) {
				roles += (first ? "," : "") + role;
				first = true;
			}
		}
		return roles;
	}

	public String getRolesDisplayString() {
		String roles = "";
		if (this.roles != null && !this.roles.isEmpty()) {
			boolean first = false;
			for (Integer role : this.roles) {
				roles += (first ? "," : "") + this.rolesMap.get(role);
				first = true;
			}
		} else {
			if (isCaptain()) {
				roles = "Captain";
			} else if (isAdmin(-99)) {
				roles = "Admin";
			} else if (isPlayer()) {
				roles = "Player";
			}
		}
		return roles;
	}

	public List<Integer> getTeams() {
		return getTeams(this.clubId);
	}
	
	public List<Integer> getTeams(int clubId) {
		if (teams == null) {
			teams = new ArrayList<Integer>();
			try {
				teams = TeamFactory.getTeamOfficialTeamIds(this.userID, clubId);
				if (this.playerID > 0) {
					PlayerDto pdto = PlayerFactory.getPlayerById(this.getPlayerID(), clubId);
					if (pdto != null && "1".equals(pdto.getIsActive())) {
						List<Integer> playerTeams = TeamFactory.getTeamIdsByPlayerId(playerID, clubId);
						if(playerTeams != null && playerTeams.size() > 0) {
							teams.addAll(playerTeams);
							teams = teams.stream().distinct().collect(Collectors.toList());
						}
					}
				} 
			}catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return teams;
	}

	public List<Integer> getInternalClubIds() {
		if (internalClubIds == null) {
			if (this.playerID == 0) {
				internalClubIds = new ArrayList<Integer>();
			} else {
				try {
					internalClubIds = ClubFactory.getInternalClubIdsOfPlayer(this.playerID, clubId);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return internalClubIds;
	}

	public void setTeams(List<Integer> teams) {
		this.teams = teams;
	}

	public boolean isScorer(FixtureDto fixture) {
		if (fixture == null || fixture.getTeamOne() == 0 || fixture.getTeamTwo() == 0 || this.roles == null) {
			return false;
		}

		if (this.roles.contains(4) || isAdmin(fixture.getLeagueId())) {
			return true;
		}
		if ((this.roles.contains(2) || this.roles.contains(3)) && this.getTeams() != null
				&& (this.getTeams().contains(fixture.getTeamOne()) || this.getTeams().contains(fixture.getTeamTwo()))) {
			return true;
		}

		if (isCaptain && this.getCaptainTeams() != null && (this.getCaptainTeams().contains(fixture.getTeamOne())
				|| this.getCaptainTeams().contains(fixture.getTeamTwo()))) {
			return true;
		}
		if (this.isUmpire()
				&& (fixture.getUmpire1Id() == this.getUmpireID() || fixture.getUmpire2Id() == this.getUmpireID())) {
			return true;
		}

		return false;
	}

	public boolean isScorerOrPlayer(FixtureDto fixture) {
		return this.getTeams().contains(fixture.getTeamOne()) || this.getTeams().contains(fixture.getTeamTwo())
				|| this.getInternalClubIds().contains(fixture.getInternalClubOne())
				|| this.getInternalClubIds().contains(fixture.getInternalClubTwo()) || this.isScorer(fixture);
	}

	public boolean isScorer(int teamOne, int teamTwo) {
		if (teamOne == 0 || teamTwo == 0 || this.roles == null) {
			return false;
		}

		if (this.roles.contains(4)) {
			return true;
		}
		if ((this.roles.contains(2) || this.roles.contains(3)) && this.getTeams() != null
				&& (this.getTeams().contains(teamOne) || this.getTeams().contains(teamTwo))) {
			return true;
		}

		return false;
	}

	public boolean isScorer() {

		if (this.roles.contains(4) || this.roles.contains(2) || this.roles.contains(3) || this.isUmpire()) {
			return true;
		}

		if (!getAdminClubs().isEmpty()) {
			return true;
		}

		return false;
	}

	public int getUmpireID() {
		return umpireID;
	}

	public void setUmpireID(int umpireID) {
		this.umpireID = umpireID;
	}

	public String getFullName() {
		if(!CommonUtility.isNullOrEmpty(this.fname)) {
			this.fname = this.fname.trim();
		}
		if(!CommonUtility.isNullOrEmpty(this.lname)) {
			this.lname = this.lname.trim();
		}
		String fullname = this.fname + " " + this.lname;
		return CommonUtility.toDisplayCase(!CommonUtility.isNullOrEmptyOrNULL(fullname) ? fullname.trim() : "");
	
		
	}

	public List<Integer> getAdminClubs() {
		if (adminClubs == null) {
			try {
				adminClubs = ClubFactory.getClubAdminIdsByPlayerId(playerID, this.clubId);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				adminClubs = new ArrayList<Integer>();
			}
		}
		return adminClubs;
	}

	public List<Integer> getCaptainTeams() {
		if (captainTeams == null) {
			try {
				captainTeams = UserFactory.getCaptainteams(playerID, this.clubId);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				captainTeams = new ArrayList<Integer>();
			}
		}
		return captainTeams;
	}

	public void setAdminClubs(List<Integer> adminClubs) {
		this.adminClubs = adminClubs;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
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

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getBackGroundImagePath() {
		return backGroundImagePath;
	}

	public void setBackGroundImagePath(String backGroundImagePath) {
		this.backGroundImagePath = backGroundImagePath;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getNoOfMatchesStreaming() {
		return noOfMatchesStreaming;
	}

	public void setNoOfMatchesStreaming(int noOfMatchesStreaming) {
		this.noOfMatchesStreaming = noOfMatchesStreaming;
	}

	public String getAddRequestFrom() {
		return addRequestFrom;
	}

	public void setAddRequestFrom(String addRequestFrom) {
		this.addRequestFrom = addRequestFrom;
	}

}
