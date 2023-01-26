package com.cricket.dlscalculation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cricket.dao.DLSCalculationFactory;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

public class DLSCalculation {

	
	public static Object calculateDLS(int t1BallsPlayed,int t2BallsPlayed, int t1WicketLost, int t2WicketLost, 
					int totalBalls, int perposedBalls, int team1Scored, int team2Scored, String matchType, boolean isSecondInning,
					double g50Value) throws Exception{
		
		//int balls = 8;
		//int wicketLost = 2;
		/*int t1BallsLeft = totalBalls - t1BallsPlayed;
		int t2BallsLeft = totalBalls - t2BallsPlayed;
		int newTargetScore = team1Scored  +1;
		Float r1Percentange = t1BallsLeft == 0 ? 100.0F : DLSCalculationFactory.getPercentageFromDLSChart(t1BallsLeft, t1WicketLost, matchType);
		Float r2Percentange = t2BallsLeft == 0 ? 0.0F :DLSCalculationFactory.getPercentageFromDLSChart(t2BallsLeft, t2WicketLost, matchType);
		
		if(r2Percentange < r1Percentange){
			newTargetScore = Math.round(team1Scored * r2Percentange / r1Percentange);
		}else if(r2Percentange > r1Percentange){
			Float newPercentage = r2Percentange - r1Percentange;
			Float r2PercentangeLeft = 100.0F - r2Percentange;
			newTargetScore = team1Scored + (Math.round((newPercentage * r2PercentangeLeft/100)));
		}
		
		return null;*/
		
		int t1BallsLeft = totalBalls - t1BallsPlayed;
		Double availResStartT1 = getPercentageFromDLSChart(t1BallsLeft, t1WicketLost, matchType);
		
		int t2BallsLeft = totalBalls - t2BallsPlayed;
		Double availResStartT2 = getPercentageFromDLSChart(t2BallsLeft, t2WicketLost, matchType);
		
		
		return null;
		
	}
 public static Map<String, Object> calculateDLSScore(List<DLSInputData> intruptions, int inningNum, 
		double score, double g50Value, Double totalRsAvailable, String matchType) throws Exception{
		
		Map<String, Object> res = new HashMap<String, Object>();
		Double targetScore = 0.0;
		Double lostR1 = 0.0;
		Double lostR2 = 0.0;
//		Double totalRsAvailable = 100.0;
		
		if(inningNum == 1){
			
			/*if(intruptions != null && !intruptions.isEmpty()){
				totalRsAvailable = getPercentageFromDLSChart(intruptions.get(0).getBallsAtStartOfPlay(), 0, "50");
			}*/
			
			for(DLSInputData intruption: intruptions){
				
				int ballsLeftRS = intruption.getBallsAtStartOfPlay() - intruption.getBallsPlayed();
				int ballsLeftRR = intruption.getBallsAtStartOfPlay() - (intruption.getBallsPlayed()+intruption.getBallsLost());
				
				Double rs1 = getPercentageFromDLSChart(ballsLeftRS, intruption.getWicketLost(), matchType);
				Double rr1 = getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(), matchType);
				
				lostR1 += (rs1 - rr1);
				
			}
			Double totalR1Used = totalRsAvailable - lostR1;
			res.put("totalR1Used", totalR1Used);
			DLSInputData lastRecord = intruptions.get(intruptions.size() -1);
			Double availbleForR2 = getPercentageFromDLSChart(lastRecord.getRevisedBalls(), 0,matchType);
			//Double rr1 = getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(), matchType);
			//Double totalR2Lost = 100 - lostR1;
			if(availbleForR2 > totalR1Used){
				// T=Score+(G50*(R2-R1))/100)+1
				targetScore = score + (g50Value * ((availbleForR2 - totalR1Used)/100.0d));
			}
			else if (totalR1Used > availbleForR2){
				// T=Score*(R2/R1)+1
				targetScore = score * (availbleForR2/totalR1Used);
			}
			else {
				targetScore = score;
			}
		}else if(inningNum == 2){
			/*if(intruptions != null && !intruptions.isEmpty()){
				totalRsAvailable = getPercentageFromDLSChart(intruptions.get(0).getBallsAtStartOfPlay(), 0, "50");
			}*/
			for(DLSInputData intruption: intruptions){
				
				int ballsLeftRS = intruption.getBallsAtStartOfPlay() - intruption.getBallsPlayed();
				int ballsLeftRR = intruption.getBallsAtStartOfPlay() - (intruption.getBallsPlayed()+intruption.getBallsLost());
				
				double rs2 = getPercentageFromDLSChart(ballsLeftRS, intruption.getWicketLost(), matchType);
				double rr2 = getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(), matchType);
				
				lostR2 += (rs2 - rr2);
				
			}
			DLSInputData lastRecord = intruptions.get(intruptions.size() -1);
			Double availbleForR2 = getPercentageFromDLSChart(lastRecord.getBallsAtStartOfPlay(), 0, matchType);
			Double totalR2Used = availbleForR2 - lostR2;
			res.put("totalR2Used", totalR2Used);
			
			
			//Double rr1 = getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(), "50");
			//Double totalR2Lost = 100 - lostR1;
			if(totalR2Used > totalRsAvailable){
				// T=Score+(G50*(R2-R1))/100)+1
				targetScore = score + (g50Value * ((totalR2Used - totalRsAvailable)/100.0d));
			}
			else if (totalRsAvailable > totalR2Used){
				// T=Score*(R2/R1)+1
				targetScore = score * (totalR2Used/totalRsAvailable);
			}
			else {
				targetScore = score;
			}
		}
		res.put("targetScore",  Math.ceil(targetScore));
		return res;
	}
	public static Map<String, Object> calculateDLSScore(List<DLSInputData> intruptions, int inningNum, double score, double g50Value, String matchType) throws Exception{
		
		Map<String, Object> res = new HashMap<String, Object>();
		Double targetScore = 0.0;
		Double lostR1 = 0.0;
		Double lostR2 = 0.0;
		Double totalRsAvailable = 100.0;
		
		if(inningNum == 1){
			
			if(intruptions != null && !intruptions.isEmpty()){
				totalRsAvailable = getPercentageFromDLSChart(intruptions.get(0).getBallsAtStartOfPlay(), 0, matchType);
				for(DLSInputData intruption: intruptions){
					
					int ballsLeftRS = intruption.getBallsAtStartOfPlay() - intruption.getBallsPlayed();
					int ballsLeftRR = intruption.getBallsAtStartOfPlay() - (intruption.getBallsPlayed()+intruption.getBallsLost());
					
					Double rs1 = getPercentageFromDLSChart(ballsLeftRS, intruption.getWicketLost(), matchType);
					Double rr1 = getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(), matchType);
					
					lostR1 += (rs1 - rr1);
					
				}
			}
			
			Double totalR1Used = totalRsAvailable - lostR1;
			res.put("totalR1Used", totalR1Used);
			Double availbleForR2 = totalR1Used;
			if(intruptions.size() > 0) {
				DLSInputData lastRecord = intruptions.get(intruptions.size() -1);
				availbleForR2 = getPercentageFromDLSChart(lastRecord.getRevisedBalls(), 0, matchType);
			}
			//Double rr1 = getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(), matchType);
			//Double totalR2Lost = 100 - lostR1;
			if(availbleForR2 > totalR1Used){
				// T=Score+(G50*(R2-R1))/100)+1
				targetScore = score + (g50Value * ((availbleForR2 - totalR1Used)/100.0d));
			}
			else if (totalR1Used > availbleForR2){
				// T=Score*(R2/R1)+1
				targetScore = score * (availbleForR2/totalR1Used);
			}
			else {
				targetScore = score;
			}
		}else if(inningNum == 2){
			if(intruptions != null && !intruptions.isEmpty()){
				totalRsAvailable = getPercentageFromDLSChart(intruptions.get(0).getBallsAtStartOfPlay(), 0, matchType);
			}
			for(DLSInputData intruption: intruptions){
				
				int ballsLeftRS = intruption.getBallsAtStartOfPlay() - intruption.getBallsPlayed();
				int ballsLeftRR = intruption.getBallsAtStartOfPlay() - (intruption.getBallsPlayed()+intruption.getBallsLost());
				
				Double rs2 = getPercentageFromDLSChart(ballsLeftRS, intruption.getWicketLost(), matchType);
				Double rr2 = getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(), matchType);
				
				lostR2 += (rs2 - rr2);
				
			}
			Double totalR2Used = totalRsAvailable - lostR2;
			res.put("totalR2Used", totalR2Used);
			Double availbleForR1 = totalR2Used;
			if(intruptions.size() > 0) {
				DLSInputData lastRecord = intruptions.get(intruptions.size() -1);
				availbleForR1 = getPercentageFromDLSChart(lastRecord.getBallsDecidedAtStart(), 0, matchType);
			}
			//Double rr1 = getPercentageFromDLSChart(ballsLeftRR, intruption.getWicketLost(), matchType);
			//Double totalR2Lost = 100 - lostR1;
			if(totalR2Used > availbleForR1){
				// T=Score+(G50*(R2-R1))/100)+1
				targetScore = score + (g50Value * ((totalR2Used - availbleForR1)/100.0d));
			}
			else if (availbleForR1 > totalR2Used){
				// T=Score*(R2/R1)+1
				targetScore = score * (totalR2Used/availbleForR1);
			}
			else {
				targetScore = score;
			}
		}
		res.put("targetScore",  Math.ceil(targetScore));
		return res;
	}
	
	public static Double getPercentageFromDLSChart(int ballsLeft, int wicketLost, String matchType) throws Exception{
		return  DLSCalculationFactory.getPercentageFromDLSChart(ballsLeft, wicketLost, matchType);
	}
	
}
