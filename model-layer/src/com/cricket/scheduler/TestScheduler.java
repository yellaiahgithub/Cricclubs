package com.cricket.scheduler;

import java.util.ArrayList;
import java.util.List;

public class TestScheduler {

	
	public static void main (String a[]){
		List<String> list = new ArrayList<String>();
		list.add("IND");
		list.add("PAK"); 
		list.add("AUS"); 
		list.add("NWZ"); 
		list.add("ENG");
		list.add("USA");
		list.add("CAN");
		list.add("JAP");
		list.add("CHN");
		list.add("AFG");
		
		listMatches(list);
	}
	
	
	static void listMatches(List<String> teamList)
	{
	    if (teamList.size() % 2 != 0)
	    {
	    	teamList.add("Bye"); // If odd number of teams add a dummy
	    }

	    int numDays = (teamList.size() - 1); // Days needed to complete tournament
	    int halfSize = teamList.size() / 2;

	    List<String> teams = new ArrayList<String>();

	    teams.addAll(teamList); // Add teams to List and remove the first team
	    teams.remove(0);

	    int teamsSize = teams.size();

	    for (int day = 0; day < numDays; day++)
	    {

	        int teamIdx = day % teamsSize;


	        for (int idx = 1; idx < halfSize; idx++)
	        {               
	            int firstTeam = (day + idx) % teamsSize;
	            int secondTeam = (day  + teamsSize - idx) % teamsSize;
	        }
	    }
	}
}
