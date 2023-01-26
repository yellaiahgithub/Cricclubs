/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.util.Map;

import com.cricket.utility.CommonUtility;

/**
 * @author ganesh
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BattingDto {
	private int matchID;
	private int playerID;
	private int teamId;
	private int runsScored;
	private int ballsFaced;
	private int fours;
	private int sixers;
	private String howOut;
	private String firstName;
	private String lastName;
	private String wicketTaker1;
	private String wicketTaker2;
	private String subtPlyrName;
	private String isOut;
	private String outStringNoLink;
	private String outStringNickNamesNoLink;
	private String outStringCustomReq;
	private int innings;
	private String profilepic_file_path;
	private boolean isSecondary;
	private String nickName;
	private String shortName;

	private int	battingPosition; 
	
	private String battingStyle;
	
	private String playerStrikeRate;
	
	public String getPlayerStrikeRate() {
		return playerStrikeRate;
	}

	public void setPlayerStrikeRate(String playerStrikeRate) {
		this.playerStrikeRate = playerStrikeRate;
	}

	public String getSubtPlyrName() {
		return subtPlyrName;
	}

	public void setSubtPlyrName(String subtPlyrName) {
		this.subtPlyrName = subtPlyrName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOutStringCustomReq() {
		return outStringCustomReq;
	}

	public void setOutStringCustomReq(String outStringCustomReq) {
		this.outStringCustomReq = outStringCustomReq;
	}

	public boolean isSecondary() {
		return isSecondary;
	}

	public void setSecondary(boolean isSecondary) {
		this.isSecondary = isSecondary;

	}

	public void setOutStringNoLink(String outStringNoLink) {
		this.outStringNoLink = outStringNoLink;
	}

	public String getProfilepic_file_path() {
		if(CommonUtility.isNullOrEmpty(profilepic_file_path)) {
			return "/documentsRep/profilePics/no_image.png";
		}
		return profilepic_file_path;
	}

	public void setProfilepic_file_path(String profilepic_file_path) {
		this.profilepic_file_path = profilepic_file_path;
	}

	public int getInnings() {
		return innings == 0 ? 1 : innings;
	}

	public void setInnings(int innings) {
		this.innings = innings;
	}

	public int getPoints(int clubID) {
		if (clubID == 501) {
			return this.getRunsScored() > 0 ? (this.getRunsScored() / 20) : 0;
		}else if(clubID == 8936){
			return this.getRunsScored() > 0 ? this.getRunsScored() : 0;
		}else if(clubID == 17117){
			return this.getPointsBPCL();
		}else if(clubID == 19694){
			return this.getPointsInspireSport();
		}else {
			return this.getPoints();
		}
	}
	private int getPointsBPCL() {
		int totalPoints = 0;
		
		if (runsScored > 0) {
			totalPoints += runsScored;
			totalPoints += fours * 1 + sixers * 2;
		}
		
		if (runsScored >= 30) {
			if (runsScored >= 100) {
				totalPoints += 50;
			} else if (runsScored >= 50) {
				totalPoints += 20;
			} else if (runsScored >= 40) {
				totalPoints += 10;
			} else if (runsScored >= 30) {
				totalPoints += 5;
			}
		}

		return totalPoints;
	}
	private int getPoints() {
		int totalPoints = 0;
		if ("1".equals(isOut) && runsScored == 0) {
			totalPoints += -10;
		}

		if (runsScored > 0) {
			totalPoints += runsScored;
			totalPoints += fours * 1 + sixers * 2;
		}

		if (ballsFaced >= 10 || runsScored >= 10) {
			double sr = (double) this.runsScored / this.ballsFaced;
			sr = sr * 100;
			if (sr > 0 && sr < 50) {
				totalPoints += -10;
			} else if (sr < 100) {
				totalPoints += 0;
			} else if (sr < 125) {
				totalPoints += 10;
			} else if (sr < 150) {
				totalPoints += 20;
			} else if (sr < 175) {
				totalPoints += 30;
			} else if (sr < 200) {
				totalPoints += 40;
			} else {
				totalPoints += 50;
			}
		}

		if (runsScored >= 10) {
			if (runsScored >= 50) {
				totalPoints += runsScored * 2;
			} else if (runsScored >= 40) {
				totalPoints += 40;
			} else if (runsScored >= 30) {
				totalPoints += 30;
			} else if (runsScored >= 20) {
				totalPoints += 20;
			} else if (runsScored >= 10) {
				totalPoints += 10;
			}
		}

		return totalPoints;
	}
	private int getPointsInspireSport() {
		
		int totalPoints = 0;
		if ("1".equals(isOut) && runsScored == 0) {
			totalPoints += -10;
		}

		if (runsScored > 0) {
			totalPoints += runsScored;
			totalPoints += fours * 2 + sixers * 5;
		}

		if (ballsFaced >= 10 || runsScored >= 10) {
			double sr = (double) this.runsScored / this.ballsFaced;
			sr = sr * 100;
			if (sr > 0 && sr < 50) {
				totalPoints += -10;
			} else if (sr > 50 && sr < 75) {
				totalPoints += 0;
			} else if (sr > 75 && sr < 100) {
				totalPoints += 5;
			} else if (sr > 100 && sr < 125) {
				totalPoints += 20;
			} else if (sr > 125 && sr < 150) {
				totalPoints += 30;
			} else if (sr > 150 ) {
				totalPoints += 50;
			} 
		}

		if (runsScored >= 10) {
			if(runsScored >= 100) {
				totalPoints += (runsScored * 2)+50;
			}else if (runsScored >= 50) {
				totalPoints += runsScored * 2;
			} else if (runsScored >= 40) {
				totalPoints += 40;
			} else if (runsScored >= 30) {
				totalPoints += 30;
			} else if (runsScored >= 20) {
				totalPoints += 20;
			} else if (runsScored >= 10) {
				totalPoints += 10;
			}
		}

		return totalPoints;
	}

	/**
	 * @return
	 */
	public int getBallsFaced() {
		return ballsFaced;
	}

	/**
	 * @return
	 */
	public int getFours() {
		return fours;
	}

	/**
	 * @return
	 */
	public String getHowOut() {
		return howOut;
	}

	/**
	 * @return
	 */
	public String getIsOut() {
		return isOut;
	}

	/**
	 * @return
	 */
	public int getMatchID() {
		return matchID;
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
	public int getRunsScored() {
		return runsScored;
	}

	/**
	 * @return
	 */
	public int getSixers() {
		return sixers;
	}

	/**
	 * @return
	 */

	/**
	 * @param i
	 */
	public void setBallsFaced(int i) {
		ballsFaced = i;
	}

	/**
	 * @param i
	 */
	public void setFours(int i) {
		fours = i;
	}

	/**
	 * @param string
	 */
	public void setHowOut(String string) {
		howOut = string;
	}

	/**
	 * @param string
	 */
	public void setIsOut(String string) {
		isOut = string;
	}

	/**
	 * @param i
	 */
	public void setMatchID(int i) {
		matchID = i;
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
	public void setRunsScored(int i) {
		runsScored = i;
	}

	/**
	 * @param i
	 */
	public void setSixers(int i) {
		sixers = i;
	}

	/**
	 * @param string
	 */

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
	public String getFirstName() {
		return (firstName == null) ? "" : firstName;
	}

	/**
	 * @return
	 */
	public String getLastName() {
		return lastName;
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
	public void setLastName(String string) {
		lastName = string;
	}

	public String getPlayerName() {
		String name = this.getFirstName().trim();
		if (this.lastName != null && this.lastName.length() > 0) {
			name += " " + this.lastName.trim();
		}
		return CommonUtility.toDisplayCase(name);
	}

	/*public String getPlayerShortName(){	
		if(this.lastName == null || this.lastName.isEmpty()) {
			return CommonUtility.toDisplayCase(this.getFirstName().trim());
		}	
		return this.getFirstName().trim() + " " + this.getLastName().trim().charAt(0);
	}*/
	
	public String getPlayerShortName(int firstNameFirst) {
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
			name = this.getFirstName().trim().charAt(0) + " " + this.getLastName().trim();
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

	public String getPlayerShortNameLastF() {
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

	public String getPlayerShortNameForT20InterpoFest() {
		return CommonUtility.toDisplayCase(this.getFirstName().charAt(0) + ". " + this.lastName);
	}

	public String getStrikeRate() {

		double sr = 0;
		if (this.ballsFaced != 0) {
			sr = (double) this.runsScored / this.ballsFaced;
		}
		return CommonUtility.Round(sr * 100, 2);
	}

	public String getOutString(Map<String, String> players, int clubId) {
		return buildOutString(players, clubId, true);
	}
	public String getWicketTaker1() {
		return wicketTaker1;
	}

	public void setWicketTaker1(String wicketTaker1) {
		this.wicketTaker1 = wicketTaker1;
	}

	public String getWicketTaker2() {
		return wicketTaker2;
	}

	public void setWicketTaker2(String wicketTaker2) {
		this.wicketTaker2 = wicketTaker2;
	}

	public String buildOutString(Map<String, String> players, int clubId, boolean playerLinkRequired) {
		String out = "not out";
		if (howOut == null ) {
			return "DNB";
		}else if("".equals(this.howOut)){
			return out;
		}

		if ("rt".equals(this.howOut)) {
			out = "Retired Hurt ";
			/*if (!CommonUtility.isNullOrEmptyOrNULL(wicketTaker1) && CommonUtility.stringToInt(wicketTaker1) > 0) {
				out += playerLinkRequired
						? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId)
						: (String) players.get(wicketTaker1);
			}*/
			return out;
		}

		if ("rto".equals(this.howOut)) {
			out = "Retired Out ";
			/*if (!CommonUtility.isNullOrEmptyOrNULL(wicketTaker1) && CommonUtility.stringToInt(wicketTaker1) > 0) {
				out += playerLinkRequired
						? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId)
						: (String) players.get(wicketTaker1);
			}*/
			return out;
		}

		if ("b".equals(this.howOut)) {
			out = "b ";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				
				if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {
					out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1);
				}
			}
			return out;
		}

		if ("ct".equals(this.howOut)) {
			if(wicketTaker1 != null && wicketTaker2 != null && wicketTaker1.equals(wicketTaker2)) {
				out = "c&b ";
				if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
					if(CommonUtility.stringToInt(wicketTaker2) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
						out += "(Sub)"+subtPlyrName;
					}else {	
						out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker2), wicketTaker2, clubId) : (String) players.get(wicketTaker2);
		
					}
				}
				return out;
			}else {
			
			out = "c ";
			if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
				if(CommonUtility.stringToInt(wicketTaker2) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {	
					out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker2), wicketTaker2, clubId) : (String) players.get(wicketTaker2);
	
				}
			}
			out += " b ";
			if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
				out += "(Sub)"+subtPlyrName;
			}else {	
				out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1);
			}
				return out;
		}
		}
		if ("ctw".equals(this.howOut)) {
			if(wicketTaker1 != null && wicketTaker2 != null && wicketTaker1.equals(wicketTaker2)) {
				out = "c&b ";
				if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
					if(CommonUtility.stringToInt(wicketTaker2) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
						out += "(Sub)"+subtPlyrName;
					}else {	
						out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker2), wicketTaker2, clubId) : (String) players.get(wicketTaker2);
		
					}
				}
				return out;
			}else {
			
			out = "c &#8224;";
			if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
				if(CommonUtility.stringToInt(wicketTaker2) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {	
					out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker2), wicketTaker2, clubId) : (String) players.get(wicketTaker2);
			
				}
			}
			out += " b ";
			if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
				out += "(Sub)"+subtPlyrName;
			}else {	
				out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1);
			}
			return out;
		}
		}
		
		if ("ro".equals(this.howOut)) {
			out = "run out (";
			String wt1 = "";
			String wt2 = "";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim()) && !"0".equals(wicketTaker1.trim())) {
				if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {	
					wt1 = playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1);
				}
			}
			if (wicketTaker2 != null && !"".equals(wicketTaker2.trim()) && !"0".equals(wicketTaker2.trim())) {
				if(CommonUtility.stringToInt(wicketTaker2) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {	
					wt2 = (playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker2), wicketTaker2, clubId) : (String) players.get(wicketTaker2));
				}
			}
			
			if (!"".equals(wt1) && !"".equals(wt2) && !wicketTaker1.equals(wicketTaker2)) {
				out += wt1 + "/" + wt2;
			} else {
				if(wt1 != null && wt2 != null && wt1.equals(wt2)) {
					out += wt1;
				}else {
				out += wt1 + wt2;
				}
			}
			out += ") ";
			return out;
		}

		if ("st".equals(this.howOut)) {
			/*out = "Stumped ";*/
			out = "St ";
			if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
				if(CommonUtility.stringToInt(wicketTaker2) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {
					out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker2), wicketTaker2, clubId) : (String) players.get(wicketTaker2);
				}
			}
			out += " b ";
			if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
				out += "(Sub)"+subtPlyrName;
			}else {
				out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1);
			}
			return out;
		}

		if ("ht".equals(this.howOut)) {
			out = "Hit Wicket ";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {
					out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1);
				}
			}
			return out;
		}

		if ("hbt".equals(this.howOut)) {
			out = "Hit Ball Twice ";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {
					out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1);
				}
			}
			return out;
		}

		if ("hdb".equals(this.howOut)) {
			out = "Handled Ball ";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {
					out += playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1);
				}
			}
			return out;
		}

		if ("to".equals(this.howOut)) {
			out = "Timed Out ";
			/*if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				out += playerLinkRequired
						? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId)
						: (String) players.get(wicketTaker1);
			}*/
			return out;
		}

		if ("obf".equals(this.howOut)) {
			out = "Obstructing The Field ";
			return out;
		}

		if ("lbw".equals(this.howOut)) {
			out = "lbw ";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {
					out += "b " + (playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1));
			}
			}
			return out;
		}

		if ("mk".equals(this.howOut)) {
			out = "Run Out (Mankad Out)(";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
					out += "(Sub)"+subtPlyrName;
				}else {
					out += (playerLinkRequired ? CommonUtility.addPlayerLink((String) players.get(wicketTaker1), wicketTaker1, clubId) : (String) players.get(wicketTaker1));
			}}
			out += ") ";
			return out;
		}

		return out;
	}
	
	public String getOutStringCustomReq(Map<String, String> playerMap) {

		String out = "not out";
		if (howOut == null || "".equals(this.howOut)) {
			return "<span>" + out + "</span>";
		}

		String wicketTaker1Name = "";
		String wicketTaker2Name = ""; 
		if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
			if(CommonUtility.stringToInt(wicketTaker1) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
				wicketTaker1Name = "(Sub)"+subtPlyrName;
			}else {
				wicketTaker1Name = playerMap.get(wicketTaker1);
			}
		}
		if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
			if(CommonUtility.stringToInt(wicketTaker2) == -1 && !CommonUtility.isNullOrEmpty(subtPlyrName)) {
				wicketTaker2Name = "(Sub)"+subtPlyrName;
			}else {
				wicketTaker2Name = playerMap.get(wicketTaker2);
			}
			
		}
		
		if ("rt".equals(this.howOut)) {
			out = "<span>Retired Hurt</span>";
			if (CommonUtility.isNullOrEmptyOrNULL(wicketTaker1) && CommonUtility.stringToInt(wicketTaker1) > 0) { out += wicketTaker1Name != null ? "<span class='outname'>" + wicketTaker1Name + "</span>" : "";
			}
			return out;
		}

		if ("rto".equals(this.howOut)) {
			out = "<span>Retired Out</span>";
			if (CommonUtility.isNullOrEmptyOrNULL(wicketTaker1) && CommonUtility.stringToInt(wicketTaker1) > 0) {
				out += wicketTaker1Name != null ? "<span class='outname'>" + wicketTaker1Name + "</span>" : "";
			}
			return out;
		}

		if ("b".equals(this.howOut)) {
			out = "<span>b </span>";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				out += wicketTaker1Name != null ? "<span class='outname'>" + wicketTaker1Name + "</span>" : "";
			}
			return out;
		}

		if ("ct".equals(this.howOut)) {
			out = "<span>c </span>";
			if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
				out += wicketTaker2Name != null ? "<span class='outname'>" + wicketTaker2Name + "</span>" : "";
			}
			out += "<span> b </span>";
			out += wicketTaker1Name != null ? "<span class='outname'>" + wicketTaker1Name + "</span>" : "";
			return out;
		}
		if ("ctw".equals(this.howOut)) {
			out = "<span>c &#8224; </span>";
			if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
				out += wicketTaker2Name != null ? "<span class='outname'>" + wicketTaker2Name + "</span>" : "";
			}
			out += "<span> b </span>";
			out += wicketTaker1Name != null ? "<span class='outname'>" + wicketTaker1Name + "</span>" : "";
			return out;
		}
		if ("ro".equals(this.howOut)) {
			out = "<span>run out </span><span class='outname'>(";
			String wt1 = "";
			String wt2 = "";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim()) && !"0".equals(wicketTaker1.trim())) {
				wt1 = wicketTaker1Name != null ? wicketTaker1Name : "";
			}
			if (wicketTaker2 != null && !"".equals(wicketTaker2.trim()) && !"0".equals(wicketTaker2.trim()) && wicketTaker2 !=wicketTaker1) {
				wt2 = wicketTaker2Name != null ?  wicketTaker2Name : "";
			}
			if (!"".equals(wt1) && !"".equals(wt2)) {
				out += wt1 + "/" + wt2;
			} else {
				out += wt1 + wt2;
			}
			out += ")</span> ";
			return out;
		}

		if ("st".equals(this.howOut)) {
			out = "<span>Stumped </span>";
			if (wicketTaker2 != null && !"".equals(wicketTaker2.trim())) {
				out += "<span class='outname'>";
				out += wicketTaker2Name != null ? wicketTaker2Name : "";
				out += "</span>";
			}
			out += "<span> b </span>";
			
			out += "<span class='outname'>";
			out += wicketTaker1Name != null ? wicketTaker1Name : "";
			out += "</span>";
			return out;
		}

		if ("ht".equals(this.howOut)) {
			out = "<span>Hit Wicket </span>";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				out += "<span class='outname'>";
				out += wicketTaker1Name != null ? wicketTaker1Name : "";
				out += "</span>";
			}
			return out;
		}

		if ("hbt".equals(this.howOut)) {
			out = "<span>Hit Ball Twice  </span>";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				out += "<span class='outname'>";
				out += wicketTaker1Name != null ? wicketTaker1Name : "";
				out += "</span>";
			}
			return out;
		}

		if ("hdb".equals(this.howOut)) {
			out = "<span>Handled Ball  </span>";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				out += "<span class='outname'>";
				out += wicketTaker1Name != null ? wicketTaker1Name : "";
				out += "</span>";
			}
			return out;
		}

		if ("to".equals(this.howOut)) {
			out = "<span>Timed Out </span>";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				out += "<span class='outname'>";
				out += wicketTaker1Name != null ? wicketTaker1Name : "";
				out += "</span>";
			}
			return out;
		}

		if ("obf".equals(this.howOut)) {
			out = "<span>Obstructing The Field </span>";
			return out;
		}

		if ("lbw".equals(this.howOut)) {
			out = "<span>lbw  </span>";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				out += "<span>b </span>" + "<span class='outname'>";
				out += wicketTaker1Name != null ? wicketTaker1Name : "";
				out += "</span>";
			}
			return out;
		}

		if ("mk".equals(this.howOut)) {
			out = "<span>mk </span>";
			if (wicketTaker1 != null && !"".equals(wicketTaker1.trim())) {
				out += "<span>b </span>" + "<span class='outname'>";
				out += wicketTaker1Name != null ? wicketTaker1Name : "";
				out += "</span>";
			}
			return out;
		}

		return out;

	}
	
/*	public String getWicketTaker1() {
		return wicketTaker1;
	}

	public void setWicketTaker1(String wicketTaker1) {
		this.wicketTaker1 = wicketTaker1;
	}

	public String getWicketTaker2() {
		return wicketTaker2;
	}

	public void setWicketTaker2(String wicketTaker2) {
		this.wicketTaker2 = wicketTaker2;
	} */

	public String getOutStringNoLink() {
		return outStringNoLink;
	}

	public void setOutStringNoLink(Map<String, String> players, int clubId) {
		this.outStringNoLink = buildOutString(players, clubId, false);
	}
	
	public String getOutStringNickNameNoLink() {
		return outStringNickNamesNoLink;
	}
	
	public void setOutStringNickNameNoLink(Map<String, String> players, int clubId) {
		this.outStringNickNamesNoLink = buildOutString(players, clubId, false);
	}
	
	@Override
	public boolean equals(Object battingDto) {
		if(this.getPlayerID() == ((BattingDto)battingDto).getPlayerID()) {
			return true;
		}
		return false;
	}
	
	public String getOutWithFullString() {
		String out = "not out";
		if (howOut == null) {
			return "DNB";
		} else if ("".equals(this.howOut)) {
			return out;
		}

		if ("rt".equals(this.howOut)) {
			return "Retired Hurt ";
		}

		if ("rto".equals(this.howOut)) {
			return "Retired Out ";
		}

		if ("b".equals(this.howOut)) {
			return "Bowled ";
		}

		if ("ct".equals(this.howOut)) {
			
			if (wicketTaker1 != null && wicketTaker2 != null && wicketTaker1.equals(wicketTaker2)) {
				return "Caught & bowled ";
			} else {
				return "Caught";
			}
		}
		if ("ctw".equals(this.howOut)) {
			if (wicketTaker1 != null && wicketTaker2 != null && wicketTaker1.equals(wicketTaker2)) {

				return "Caught & Bowled ";
			} else {

				return "Caught";
			}
		}

		if ("ro".equals(this.howOut)) {

			return "Run Out ";
		}

		if ("st".equals(this.howOut)) {

			return "Stumped ";
		}

		if ("ht".equals(this.howOut)) {

			return "Hit Wicket ";
		}

		if ("hbt".equals(this.howOut)) {

			return "Hit Ball Twice ";
		}

		if ("hdb".equals(this.howOut)) {

			return "Handled Ball ";
		}

		if ("to".equals(this.howOut)) {

			return "Timed Out ";
		}

		if ("obf".equals(this.howOut)) {

			return "Obstructing The Field ";
		}

		if ("lbw".equals(this.howOut)) {
			return "lbw ";
		}

		if ("mk".equals(this.howOut)) {
			return "Run Out ";
		}

		return out;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getBattingPosition() {
		return battingPosition;
	}

	public void setBattingPosition(int battingPosition) {
		this.battingPosition = battingPosition;
	}

	public String getBattingStyle() {
		return battingStyle;
	}

	public void setBattingStyle(String battingStyle) {
		this.battingStyle = battingStyle;
	}

}
