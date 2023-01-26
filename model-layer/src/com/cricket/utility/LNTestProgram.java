package com.cricket.utility;

import java.util.ArrayList;
import java.util.List;

import com.cricket.dao.MatchesFactory;
import com.cricket.dao.ScoringFactory;
import com.cricket.dto.MatchDto;

public class LNTestProgram {
	
	public static void main(String[] args) {
		
		try {
			
			MatchDto match = MatchesFactory.getMatch(951, 34733);
			ScoringFactory.completeMatch(match.getMatchID(), 34733);
			
			/*
			 * MatchDto match = MatchesFactory.getMatch(950, 34733);
			 * 
			 * 
			 * ScoringFactory.completeMatch(match.getMatchID(), 34733);
			 * System.out.println("Completed");
			
			int clubId = 32819;
			List<MatchDto> matches = MatchesFactory.getAllMatches(0, clubId);
						
			for(MatchDto match : matches) {
				if(match.getIsComplete()==1) {
					MatchDto mdto = MatchesFactory.getMatch(match.getMatchID(), clubId);
					ScoringFactory.saveMatchRecordsFB(mdto, clubId, false);
					System.out.println("Match Completed");
				}
			}
			 */
			//System.out.println(CommonUtility.decrypt("WAEmji8i438="));
			/*
			 * MatchDto match = MatchesFactory.getMatch(903, 50);
			 * MobilePushNotificationService.setNotificationForOver(match, 50);
			 */
			//PushNotificationBackgroundProcess.pushNotificatonAfterEndOfMatch(match, 11707);
			//Boolean isLiveEventStarted = StreamsApplication.getYoutubeEventStatus("P7vMQPNcU0g");
			//System.out.println(isLiveEventStarted); 
		
			
			//FantasyBackgroundProcess.executeMatchCompleteProcess(1400);
			
			/*			
			
			String env = System.getProperty("env");
			String mxUrl = "https://androidapi.dev.mxplay.com/v1/game/independent/result/notice";
			String gameId = "acd7a91c316d6b488f41fcd2e534c180";

			int resCode = FantasyBackgroundProcess.callMXAPIForPrizeDistribution(1465,101,mxUrl,gameId);
			
			System.out.println(resCode);
			
			ClubDtoLite club = ClubFactory.getLiteClubDetails(31439);
			
			ClubCacheHandler.setClubLiteToCache(club);
						
			 */
			//FantasyBackgroundProcess.updateContestsStatusAfterMatchStarted(1400);
			
			//MatchDto match = MatchesFactory.getMatch(5136, 11707);
			
			//System.out.println(match.getResultForTitle());
			
			//TeamDto team = TeamFactory.getTeamByTeamId(375, 50);
			
			//PushNotificationBackgroundProcess.pushNotificationForTeamFinalize(1547, team, 50);
			

			//PushNotificationBackgroundProcess.weeklySummaryReport(0, 0);


			//byte[] decodedString = Base64.getDecoder().decode("eyJLZXlWYWx1ZSI6IHsidXNlci50aW1lem9uZSI6ICJDU1QiLCJlbnYiOiJwcm9kIn0sICJMaXN0IjogW119");
			
			//String str = "{\"KeyValue\": {\"user.timezone\": \"CST\",\"env\":\"test\",\"jdk.tls.client.protocols\":\"TLSv1.2\"}, \"List\": []}";
					
			//System.out.println(new String(Base64.getEncoder().encode(str.getBytes())));
			
			//System.out.println(new String(decodedString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
