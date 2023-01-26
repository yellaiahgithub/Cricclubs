/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.promotions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cricket.utility.CommonLogic;
import com.cricket.utility.CommonUtility;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LatestMatchSummaryDto implements Serializable {
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String  match_Id;
		private String team_1_name;
		private String team_2_name;
		private String team_1_abbrev_name;
		private String team_2_abbrev_name;
		private String start_time;
		private String is_ongoing;
		private String winner_team;
		private String innings_number;
		private String batting_team_abbrev;
		private Scorecard scorecard;
		
		
		
		public String getTeam_1_name() {
			return team_1_name;
		}
		public void setTeam_1_name(String team_1_name) {
			this.team_1_name = team_1_name;
		}
		public String getTeam_2_name() {
			return team_2_name;
		}
		public void setTeam_2_name(String team_2_name) {
			this.team_2_name = team_2_name;
		}
		public String getTeam_1_abbrev_name() {
			return team_1_abbrev_name;
		}
		public void setTeam_1_abbrev_name(String team_1_abbrev_name) {
			this.team_1_abbrev_name = team_1_abbrev_name;
		}
		public String getTeam_2_abbrev_name() {
			return team_2_abbrev_name;
		}
		public void setTeam_2_abbrev_name(String team_2_abbrev_name) {
			this.team_2_abbrev_name = team_2_abbrev_name;
		}
		public String getStart_time() {
			return start_time;
		}
		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}		
		
		public String getWinner_team() {
			return winner_team;
		}
		public void setWinner_team(String winner_team) {
			this.winner_team = winner_team;
		}
		
		public String getBatting_team_abbrev() {
			return batting_team_abbrev;
		}
		public void setBatting_team_abbrev(String batting_team_abbrev) {
			this.batting_team_abbrev = batting_team_abbrev;
		}
		public Scorecard getScorecard() {
			return scorecard;
		}
		public void setScorecard(Scorecard scorecard) {
			this.scorecard = scorecard;
		}
		public String getMatch_Id() {
			return match_Id;
		}
		public void setMatch_Id(String match_Id) {
			this.match_Id = match_Id;
		}
		public String getIs_ongoing() {
			return is_ongoing;
		}
		public void setIs_ongoing(String is_ongoing) {
			this.is_ongoing = is_ongoing;
		}
		public String getInnings_number() {
			return innings_number;
		}
		public void setInnings_number(String innings_number) {
			this.innings_number = innings_number;
		}
		
		
		/*"match_id": "1234",
		"team_1_name": "Chennai Super Kings",
		"team_2_name": "Kolkata Knight Riders",
		"team_1_abbrev_name": "CSK",
		"team_2_abbrev_name": "KKR",
		"start_time": "1553080115",
		"is_ongoing": "true",
		"winner_team": "",
		"scoreboard": {
		"innings_number": "2",
		"batting_team_abbrev": "CSK",
		"team_1_score":

		{ "runs": "128", "wickets": "5", "completed_overs": "12", "current_over_balls": "4" }
		,
		"team_2_score":

		{ "runs": "180", "wickets": "10", "completed_overs": "18", "current_over_balls": "5" }
		}*/
}
