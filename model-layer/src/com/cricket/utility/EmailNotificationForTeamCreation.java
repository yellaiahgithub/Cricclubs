package com.cricket.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.PlayerFactory;
import com.cricket.dao.PlayerSeriesPaymentInfoFactory;
import com.cricket.dao.TeamFactory;
import com.cricket.dto.ClubDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.PlayerDto;
import com.cricket.dto.PlayerSeriesPaymentInfoDto;
import com.cricket.dto.TeamDto;

public class EmailNotificationForTeamCreation {

	public static void main(String[] args) throws Exception {
		
		int clubId = 14968;	
		ClubDto club = ClubFactory.getClub(clubId);
		String clubAdminEmail = club.getEmail();
		List<Integer> seriesIds = new ArrayList<Integer>();		
		seriesIds.add(160);
		seriesIds.add(168);
		
		Map<Integer, String> seriesNameIdMap = new HashMap<Integer, String>();
		
		seriesNameIdMap.put(160, "2022 SUPER LEAGUE MENS");
		seriesNameIdMap.put(168, "2022 T20 CHAMPIONSHIP MENS");
		
		List<LeagueDto> leagues = club.getLeagueListWithDivisions();		
		List<LeagueDto> leagueList = new ArrayList<LeagueDto>();
		
		for(LeagueDto l : leagues) {
			if(seriesIds.contains(l.getParent())) {
				leagueList.add(l);
			}
		}
		int counter = 0;
		for(LeagueDto ldto : leagueList) { 
			List<TeamDto> teams = TeamFactory.getTeams(ldto.getLeagueId()+"", clubId);
			
			if(!CommonUtility.isListNullEmpty(teams)) {
				
				for (TeamDto tdto : teams) {
					
					try {
						ClubDto internalClub = ClubFactory.getInternalClub(tdto.getClubId(), clubId);
						if (internalClub != null) {
							String internalClubAdmin1Email = "";
							if (internalClub.getClubAdmin1() > 0) {
								PlayerDto internalClubAdmin1Dto = PlayerFactory.getPlayerById(internalClub.getClubAdmin1(),
										clubId);
								if (internalClubAdmin1Dto != null) {
									internalClubAdmin1Email = internalClubAdmin1Dto.getEmail();
								}
							}
							PlayerDto internalClubAdmin2Dto = null;
							String internalClubAdmin2Email = "";
							if (internalClub.getClubAdmin2() > 0
									&& internalClub.getClubAdmin1() != internalClub.getClubAdmin2()) {
								internalClubAdmin2Dto = PlayerFactory.getPlayerById(internalClub.getClubAdmin2(), clubId);
								if (internalClubAdmin2Dto != null) {
									internalClubAdmin2Email = internalClubAdmin2Dto.getEmail();
								}
							}
							if (!CommonUtility.isNullOrEmpty(internalClubAdmin1Email) || !CommonUtility.isNullOrEmpty(internalClubAdmin2Email)) {
								
								PlayerSeriesPaymentInfoDto pdto = PlayerSeriesPaymentInfoFactory.getTeamSeriesPaymentInfo(clubId, tdto.getTeamID(), ldto.getLeagueId(), "SER_TEAM_REG", "SUCCESS");
								if(pdto != null) {
									String paymentDate = CommonUtility.dateToStringFormatter(pdto.getCreateDate());
									String toEmails="mir@cricketforbundet.no";
									if(!CommonUtility.isNullOrEmpty(internalClubAdmin1Email)) {
										toEmails+= ","+internalClubAdmin1Email;
									}
									if(!CommonUtility.isNullOrEmpty(internalClubAdmin2Email)) {
										toEmails+= ","+internalClubAdmin2Email;
									}
									toEmails = "shashidhar@cricclubs.com";
									String fromEmail = "malikmnaeem@hotmail.com";
									NotificationHelper.sendTeamRegistrationEmailNCF(fromEmail, toEmails, ldto.getName(),
											club.getName(), internalClub.getName(), pdto.getTotalAmount() + "", tdto.getTeamName(),	
											paymentDate,"admin@cricketforbundet.no");

									counter++;
//									}
								}
							}
						}
					}catch(Exception e) {
						System.out.println("Issue sending email to the team name - "+tdto.getTeamName()+" - team Id - "+tdto.getTeamID()+" - Series Id - "+ldto.getLeagueId()
						+" - error - "+e.getMessage());
					}
				}
			}
			
		}
		System.out.println("Total emails sent "+counter);
	}
}