package com.cricket.utility;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.FixturesFactory;
import com.cricket.dao.GroundFactory;
import com.cricket.dao.MatchesFactory;
import com.cricket.dao.ScoringFactory;
import com.cricket.dao.TopPerformersSummaryFactory;
import com.cricket.dto.FixtureDto;
import com.cricket.dto.GroundDto;
import com.cricket.dto.MatchDto;
import com.cricket.dto.lite.ClubDtoLite;

public class CopyMatchesToSummary {
	
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		
		int maxClubID = 0;		
		try {
			maxClubID = ClubFactory.getMaxClubID();
		} catch (Exception e1) {			
			e1.printStackTrace();
		}
		//maxClubID = 6265;
		for (int clubId = 1; clubId <= maxClubID; clubId++) {
			
			Connection con = DButility.getConnection(clubId);

			try {				
				ClubDtoLite club = ClubFactory.getActiveLiteClub(clubId, true);
				List<MatchDto> matches = MatchesFactory.getAllMatchs(clubId);
				for(MatchDto match : matches) {
					if(match.getIsComplete() == 1) {
						boolean matchExists = ScoringFactory.isMatchExistsInMatchCenter(clubId, match.getMatchID());						
						if(!matchExists) {
							System.out.println("######   Adding Match and match id is "+match.getMatchID()+" and club id is "+clubId+"  ######");
							GroundDto ground = null;
							try {
								FixtureDto fixture = FixturesFactory.getFixtureForMatch(match.getMatchID(), club.getClubId());
								ground = GroundFactory.getGround(fixture.getGroundId(), club.getClubId());		
							}catch(Exception ex){
								System.out.println("###### Ground Issue ######");
							}
										
					        ScoringFactory.saveSummaryIReportingDB(match,clubId,club,ground);
					        
					        //Top performer update to Match Stats Summary (Remove and Add)
							TopPerformersSummaryFactory.deleteTopPerformersSummary(match.getMatchID(), clubId);
							TopPerformersSummaryFactory.saveTopPerformersSummary(match.getMatchID(), clubId, 1);
							
							//Also call the career stats for all players of the match
							//CareerStatsSummaryFactory.updateCareerStatsSummary(match, clubId);
							
						} 
					}										
				}				
			} catch (Exception e) {
				System.err.println("Failed to Execute for club" + clubId + " The error is " + e.getMessage());
			}finally{
				DButility.closeConnection(con);
			}
		}
	}
}