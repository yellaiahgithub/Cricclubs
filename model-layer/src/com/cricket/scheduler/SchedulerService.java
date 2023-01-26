package com.cricket.scheduler;

import static com.cricket.scheduler.SchedulerConstants.GROUP_PREFIX;
import static com.cricket.scheduler.SchedulerConstants.STRING_SPLIT_REGEX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cricket.dao.GroundFactory;
import com.cricket.dao.LeagueFactory;
import com.cricket.dao.TeamFactory;
import com.cricket.dto.ClubDto;
import com.cricket.dto.GroundDto;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.TeamDto;
import com.cricket.utility.CommonUtility;

public class SchedulerService {

	public SchedulerService() {
		super();
	}

	public List<Fixtures> generateFixture(ClubDto club, LeagueDto leagueDto, String fixtureType, Date matchStartDate,
			List<String> listofMatchTimings, List<Integer> listOfPlayingDays, int gamePerTeamPerWeek, String groundIds,
			List<Date> unavailableDates, Map<Ground, List<Date>> unavailableGrounds, Map<Team, List<Date>> unavailableTeams, 
			List<String> configuration, int maxGamesPerWeek, Map<Integer, Date> teamIdExclusionDateMap, 
			Map<Integer, Integer> teamInternalClubIdMap, Map<Integer, List<ClubFixtureFromToDate>> clubIdFixtureFromToDateTimeMap, 
			List<Integer> higherDivs) throws Exception {

		Map<String, List<TeamDto>> teamMap = getAllTeamsWithGroupByLeague(club,	String.valueOf(leagueDto.getLeagueId()));

		List<GroundDto> allGrounds = getAllGrunds(club, String.valueOf(leagueDto.getLeagueId()));
		List<Ground> grounds = genrateGroundsListByIds(groundIds, allGrounds);
		if (CommonUtility.isListNullEmpty(grounds)) {
			grounds.add(new Ground(0, ""));
		}
		List<Fixtures> fixtures = new ArrayList<Fixtures>();
		
		Map<Integer, List<Fixtures>> groupFixturesMap = new HashMap<Integer, List<Fixtures>>();
		
		int i=0;
		int fixturesSize = 0;
				
		for (Map.Entry<String, List<TeamDto>> entry : teamMap.entrySet()) {
			
			List<TeamDto> teams = entry.getValue();
			List<Team> genratedTeamsOld = genrateTeamListByDto(teams, grounds, teamIdExclusionDateMap);
			List<Team> genratedTeams = new ArrayList<Team>();
			List<Team> teamsWithExcludedDates = new ArrayList<Team>();
			List<Team> teamsWithOutExcludedDates = new ArrayList<Team>();
			if(!CommonUtility.isListNullEmpty(genratedTeamsOld)) {
				for(Team team : genratedTeamsOld) {
					if(team.getExclusionDate() != null) {
						teamsWithExcludedDates.add(team);
					}else {
						teamsWithOutExcludedDates.add(team);
					}
				}
				if(!CommonUtility.isListNullEmpty(teamsWithExcludedDates)) {
					try {
						Collections.sort(teamsWithExcludedDates, (x, y) -> x.getExclusionDate().compareTo(y.getExclusionDate()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					genratedTeams.addAll(teamsWithExcludedDates);
				}
				if(!CommonUtility.isListNullEmpty(teamsWithOutExcludedDates)) {
					genratedTeams.addAll(teamsWithOutExcludedDates);
				}
			}
			List<Fixtures> groupFixtures = FixtureScheduleGenerator.getFixturesByListOfTeam(genratedTeams, fixtureType);			
			groupFixturesMap.put(i++,groupFixtures);
			if(groupFixtures != null && groupFixtures.size()>fixturesSize) {
				fixturesSize = groupFixtures.size();
			}
		}
		
		int noOfGroups = groupFixturesMap.entrySet().size();
		
		if(noOfGroups == 1) {
			fixtures.addAll(groupFixturesMap.get(0));
		}else {			
			for(int j=0; j<fixturesSize ; j++) {
				for(int k=0; k<noOfGroups; k++) {
					if(groupFixturesMap.get(k) != null && j<groupFixturesMap.get(k).size()) {
						fixtures.add(groupFixturesMap.get(k).get(j));
					}
				}
			}
		}
		
		List<Date> availableDates = CalendarUtil.getValidDateList(matchStartDate, listOfPlayingDays);
		CalendarUtil.removeUnavailableDates(availableDates, unavailableDates);
		
		List<GroundDateSlot> datesCanBePlayed = FixtureScheduleGenerator.getGroundDateWithTimeSlotsAvailable(
				availableDates, listofMatchTimings, grounds, unavailableGrounds, club.getClubId());
		
		List<GroundDateSlot> groundsAvailableWithDates = new ArrayList<GroundDateSlot>(datesCanBePlayed);
		
		List<Fixtures> fixturesWithTime = FixtureScheduleGenerator.getFixtureWithDatesMapping(fixtures,
				datesCanBePlayed, unavailableTeams, configuration, gamePerTeamPerWeek, maxGamesPerWeek, 
				teamInternalClubIdMap, clubIdFixtureFromToDateTimeMap, higherDivs, leagueDto.getLeagueId());
		
		fixturesWithTime = FixtureScheduleGenerator.mapGroundWithFixture(fixturesWithTime, groundsAvailableWithDates, configuration);

		return fixturesWithTime;
	}
	
	public Map<String, List<TeamDto>> getAllTeamsWithGroupByLeague(ClubDto club, String league) throws Exception {
		LeagueDto leagueDto = LeagueFactory.getLeague(CommonUtility.stringToInt(league), club.getClubId());
		List<TeamDto> teams = TeamFactory.getTeams(league, club.getClubId());
		Map<String, List<TeamDto>> teamMap = new HashMap<String, List<TeamDto>>();

		if (leagueDto.getGroups() > 1) {
			String groupKey;
			for (TeamDto team : teams) {
				groupKey = GROUP_PREFIX + CommonUtility.numberToAlphabet(team.getGroup());
				List<TeamDto> groupTeams = teamMap.get(groupKey);
				if (groupTeams == null) {
					groupTeams = new ArrayList<TeamDto>();
				}
				groupTeams.add(team);
				teamMap.put(groupKey, groupTeams);
			}
		} else {
			teamMap.put("Group 1", teams);
		}
		TreeMap<String, List<TeamDto>> sorted = new TreeMap<>();
		sorted.putAll(teamMap);
		return sorted;
	}

	public List<GroundDto> getAllGrunds(ClubDto club, String league) throws Exception {
		return GroundFactory.getGrounds(club.getClubId());
	}

	private List<Team> genrateTeamListByDto(List<TeamDto> teamDtos, List<Ground> grounds, Map<Integer, Date> teamIdExclusionDateMap) {
		List<Team> teams = new ArrayList<Team>();
		for (TeamDto team : teamDtos) {
			Team t = new Team(team.getTeamID(), team.getTeamName());
			for (Ground gr : grounds) {
				if (gr.getGroundid() == team.getGroundId()) {
					t.setHomeGround(gr);
				}
			}
			if(teamIdExclusionDateMap.get(team.getTeamID()) != null) {
				t.setExclusionDate(teamIdExclusionDateMap.get(team.getTeamID()));
			}
			teams.add(t);
		}
		return teams;
	}

	public List<Ground> genrateGroundsListByIds(String groundIds, List<GroundDto> allGrounds) {
		String[] idArray = groundIds.split(STRING_SPLIT_REGEX);
		List<Ground> grounds = new ArrayList<Ground>(idArray.length);
		for (String str : idArray) {
			int id = CommonUtility.stringToInt(str);
			if (id > 0) {
				for (GroundDto groudDto : allGrounds) {
					if (groudDto.getGroundId() == id) {
						grounds.add(new Ground(id, groudDto.getName()));
					}
				}

			}
		}
		return grounds;
	}

	public List<Team> genrateTeamListByIds(String teamIds) {
		String[] teamIdArray = teamIds.split(STRING_SPLIT_REGEX);
		List<Team> teams = new ArrayList<Team>(teamIdArray.length);
		for (String str : teamIdArray) {
			int teamId = CommonUtility.stringToInt(str);
			if (teamId > 0) {
				teams.add(new Team(teamId, "T-" + teamId));
			}
		}
		return teams;
	}

	public void swappingForHomeMatches(List<Fixtures> fixtures) {

		for (Fixtures fx : fixtures) {
			if (fx.getGround() != null && fx.getGround().getGroundid() > 0) {
				Team team1 = fx.getTeam1();
				Team team2 = fx.getTeam2();
				Ground g = fx.getGround();

				if (team2.getHomeGround() != null && team2.getHomeGround().equals(g)) {
					fx.setTeam1(fx.getTeam2());
					fx.setTeam2(team1);
				}

			}

		}
	}

}
