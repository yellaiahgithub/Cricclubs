package com.cricket.scheduler;

import static com.cricket.scheduler.SchedulerConstants.DOUBLE_ROUND_ROBIN;
import static com.cricket.scheduler.SchedulerConstants.HOME_AWAY_FOR_TEAM;
import static com.cricket.scheduler.SchedulerConstants.MAX_GAME_WEEK_COUNT_CHECK;
import static com.cricket.scheduler.SchedulerConstants.ROUND_ROBIN;
import static com.cricket.scheduler.SchedulerConstants.SHUFLE_SCHEDULE;
import static com.cricket.scheduler.SchedulerConstants.TEAM_MATCH_WEEK_CHECK;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import com.cricket.dao.FixturesFactory;
import com.cricket.dao.GroundFactory;
import com.cricket.dto.GroundBookingDto;
import com.cricket.utility.CommonUtility;

public class FixtureScheduleGenerator {

	public static List<GroundDateSlot> getGroundDateWithTimeSlotsAvailable(List<Date> availableDates, List<String> matchTime,
			List<Ground> grounds, Map<Ground, List<Date>> unavailableGroundDatesMap, int clubId) throws Exception {
		
		List<GroundDateSlot> groundDateSlots = new ArrayList<GroundDateSlot>();
		Map<Integer, List<GroundSlotFromToDto>> groundIdSlotsMap = getGroundIdSlotsMap(clubId);

		for (Date date : availableDates) {
			for (Ground ground : grounds) {
				List<Date> datesUnavailble = unavailableGroundDatesMap.get(ground);
				List<GroundSlotFromToDto> groundSpecificSlots = null;
				if (groundIdSlotsMap != null && groundIdSlotsMap.containsKey(ground.getGroundid())) {
					groundSpecificSlots = groundIdSlotsMap.get(ground.getGroundid());
				}
				if (datesUnavailble == null || !datesUnavailble.contains(date)) {
					List<Date> datesWithTime = CalendarUtil.getAvilableDatsWithTime(date, matchTime);
					for (Date dateWithTime : datesWithTime) {
						if (!CommonUtility.isListNullEmpty(groundSpecificSlots)) {
							boolean isSlotInBetween = false;
							for (GroundSlotFromToDto slot : groundSpecificSlots) {
								if (isGroundSlotInbetween(dateWithTime, slot)) {
									isSlotInBetween = true;
									break;
								}
							}
							if (!isSlotInBetween) {
								groundDateSlots.add(new GroundDateSlot(ground, dateWithTime));
							}
						} else {
							groundDateSlots.add(new GroundDateSlot(ground, dateWithTime));
						}
					}
				}
			}
		}
		
		return groundDateSlots;
	}
	
	public static List<Fixtures> getFixtureWithDatesMapping(List<Fixtures> fixtures, List<GroundDateSlot> groundDateSlotsWithTime, 
			Map<Team, List<Date>> unavailableTeams,	List<String> configuration, int numberOfMatchPerWeek, int maxGamePerWeek, 
			Map<Integer, Integer> teamInternalClubIdMap,Map<Integer, List<ClubFixtureFromToDate>> clubIdFixtureFromToDateTimeMap, 
			List<Integer> divsList, int seriesId) {
		
		List<Fixtures> finalFixtureWithGroundTime = new ArrayList<Fixtures>();
		Map<Team, List<GroundDateSlot>> teamGroundMap = new HashMap<Team, List<GroundDateSlot>>();
		Fixtures dummyFixture = new Fixtures(new Team(-1, "Dummy"), new Team(-1, "Dummy"));

		Collections.sort(groundDateSlotsWithTime, new ShortGameSlotByGameDate());
		Iterator<GroundDateSlot> grdItr = groundDateSlotsWithTime.iterator();

		while (grdItr.hasNext()) {

			GroundDateSlot groundSlot = grdItr.next();
			
			Date currentGameDate = groundSlot.getGameDate();
			currentGameDate = DateUtils.truncate(currentGameDate, Calendar.DATE);

			Iterator<Fixtures> fxItr = fixtures.iterator();
			
			while (fxItr.hasNext()) {
				
				Fixtures fixture = fxItr.next();
				
				List<Date> team1ExclusiveDates = null;
				List<Date> team2ExclusiveDates = null;
				
				if(unavailableTeams != null) {
					team1ExclusiveDates = unavailableTeams.get(fixture.getTeam1());
					team2ExclusiveDates = unavailableTeams.get(fixture.getTeam2());
				}

				if (isFixtureHasOneSameTeam(fixture, dummyFixture)) {
					continue;
				}
				boolean isMatchDateValid = false;
				boolean isMaxGameCheck = true;
				boolean isTeamExclusiveDate = false;
				if ( (team1ExclusiveDates != null && team1ExclusiveDates.contains(currentGameDate)) 
						|| (team2ExclusiveDates != null && team2ExclusiveDates.contains(currentGameDate))) {
					isTeamExclusiveDate = true;
				}
				isMatchDateValid = isDateValid(finalFixtureWithGroundTime, fixture, groundSlot, configuration, numberOfMatchPerWeek);
				if (isMatchDateValid && configuration.contains(MAX_GAME_WEEK_COUNT_CHECK)) {
					isMaxGameCheck = checkForMaxGamesThisWeek(finalFixtureWithGroundTime, groundSlot.getGameDate(),	maxGamePerWeek);
				}
				boolean isClubFixtureDateInBetween = false;
				if(clubIdFixtureFromToDateTimeMap != null) {
					isClubFixtureDateInBetween = getIsClubFixtureDateInBetween(teamInternalClubIdMap, clubIdFixtureFromToDateTimeMap, groundSlot, fixture);
				}
				if (isMatchDateValid && isMaxGameCheck && !isTeamExclusiveDate && !isClubFixtureDateInBetween) {
					
					Fixtures localFixture = new Fixtures(fixture.getTeam1(), fixture.getTeam2(), fixture.getGround(), fixture.getGameDate());

					localFixture.setGround(groundSlot.getGround());
					localFixture.setGameDate(groundSlot.getGameDate());
					finalFixtureWithGroundTime.add(localFixture);

					fxItr.remove();
					grdItr.remove();

					updateTeamGroundMap(groundSlot, teamGroundMap, localFixture);

					addTeamExclusiveDate(unavailableTeams, currentGameDate, fixture.getTeam1(), team1ExclusiveDates);
					addTeamExclusiveDate(unavailableTeams, currentGameDate, fixture.getTeam2(), team2ExclusiveDates);

					grdItr = groundDateSlotsWithTime.iterator();

					break;
				}
			}
		}
		return finalFixtureWithGroundTime;
	}

	public static List<Fixtures> mapGroundWithFixture(List<Fixtures> fixturesWithTime, List<GroundDateSlot> groundDateSlots, List<String> configuration) {

		List<Fixtures> finalFixtureWithGroundTime = new ArrayList<Fixtures>();
		Map<Team, List<GroundDateSlot>> teamGroundMap = new HashMap<Team, List<GroundDateSlot>>();
		Iterator<Fixtures> fixItr = fixturesWithTime.iterator();
		
		while (fixItr.hasNext()) {
			
			Fixtures fx = fixItr.next();
			Iterator<GroundDateSlot> grdItr = groundDateSlots.iterator();
			
			while (grdItr.hasNext()) {
				
				GroundDateSlot groundSlot = grdItr.next();
				
				if (groundSlot.getGameDate().equals(fx.getGameDate())) {

					boolean isGroundValid = isGroundValid(groundSlot, teamGroundMap, finalFixtureWithGroundTime, fx, configuration);
					
					if (isGroundValid) {
						
						fx.setGround(groundSlot.getGround());
						finalFixtureWithGroundTime.add(fx);
						grdItr.remove();
						fixItr.remove();
						updateTeamGroundMap(groundSlot, teamGroundMap, fx);
						grdItr = groundDateSlots.iterator();
						fixItr = fixturesWithTime.iterator();
						break;
					}
				}
			}
		}
		if (fixturesWithTime != null && !fixturesWithTime.isEmpty()) {
			finalFixtureWithGroundTime.addAll(fixturesWithTime);
		}
		return finalFixtureWithGroundTime;
	}

	private static boolean isGroundSlotInbetween(Date dateWithTime, GroundSlotFromToDto slot) {
		if (slot.getFromDateTime() != null && slot.getToDateTime() != null) {
			if (dateWithTime.compareTo(slot.getFromDateTime()) >= 0) {
				if (dateWithTime.compareTo(slot.getToDateTime()) <= 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isClubSlotInbetween(GroundDateSlot slot, ClubFixtureFromToDate dto) {
		if (dto.getFromDateTime() != null && dto.getToDateTime() != null) {
			if (slot.getGameDate().compareTo(dto.getFromDateTime()) >= 0) {
				if (slot.getGameDate().compareTo(dto.getToDateTime()) <= 0) {
					return true;
				}
			}
		}
		return false;
	}

	private static Map<Integer, List<GroundSlotFromToDto>> getGroundIdSlotsMap(int clubId) throws Exception{

		Map<Integer, List<GroundSlotFromToDto>> groundIdSlotsMap = new HashMap<Integer, List<GroundSlotFromToDto>>();
		
		List<GroundBookingDto> bookedGrounds = GroundFactory.getFutureSelectedGrounds(clubId, 2, 1);
		
		if(!CommonUtility.isListNullEmpty(bookedGrounds)) {
				
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			
			for(GroundBookingDto ground : bookedGrounds) {
					
				GroundSlotFromToDto gss = new GroundSlotFromToDto();
				
				int groundId = CommonUtility.stringToInt(ground.getTitleId());
				
				if (groundIdSlotsMap.get(groundId) == null) {
					groundIdSlotsMap.put(groundId, new ArrayList<GroundSlotFromToDto>());
				}

				List<GroundSlotFromToDto> groundSpecificSlots = groundIdSlotsMap.get(groundId);
					
				gss.setGroundId(groundId);
				gss.setFromDateTime(sdf.parse(ground.getStart()));
				gss.setToDateTime(sdf.parse(ground.getEnd()));
					
				groundSpecificSlots.add(gss);
			}
		}
		List<GroundSlotFromToDto> futureBookedGroundSlots =  FixturesFactory.getFutureBookedGroundSlots(clubId);
		
		if (!CommonUtility.isListNullEmpty(futureBookedGroundSlots)) {

			for (GroundSlotFromToDto dto : futureBookedGroundSlots) {

				if (groundIdSlotsMap.get(dto.getGroundId()) == null) {
					groundIdSlotsMap.put(dto.getGroundId(), new ArrayList<GroundSlotFromToDto>());
				}

				List<GroundSlotFromToDto> groundSpecificSlots = groundIdSlotsMap.get(dto.getGroundId());

				String fixtureDate = dto.getFixtureDate();
				String fixtureTime = dto.getFixtureTime();

				Date fixtureFromDateTime = null;
				Date fixtureToDateTime = null;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aa", Locale.getDefault());
				fixtureFromDateTime = simpleDateFormat.parse(fixtureDate + " " + fixtureTime);
				if (fixtureFromDateTime != null) {
					int noOfHours = Math.round((dto.getMaxOvers() * 2 * 5) / 60);
					fixtureToDateTime = DateUtils.addHours(fixtureFromDateTime, noOfHours);
				}
				dto.setFromDateTime(fixtureFromDateTime);
				dto.setToDateTime(fixtureToDateTime);

				groundSpecificSlots.add(dto);
			}
		}
		return groundIdSlotsMap;
	}
	
	private static void addTeamExclusiveDate(Map<Team, List<Date>> unavailableTeams, Date currentGameDate, 
			Team team, List<Date> teamExclusiveDates) {
		
		if(CommonUtility.isListNullEmpty(teamExclusiveDates)) {
			teamExclusiveDates = new ArrayList<Date>();
			teamExclusiveDates.add(currentGameDate);
			unavailableTeams.put(team, teamExclusiveDates);
			
		}else {
			teamExclusiveDates.add(currentGameDate);
		}
	}

	private static boolean checkForMaxGamesThisWeek(List<Fixtures> finalFixtureWithGroundTime, Date date,
			int maxGamePerWeek) {

		Date startDateOfWeek = CalendarUtil.getStartDateOfCurWeek(date);
		Date endDateOfWeek = CalendarUtil.getEndDateOfCurWeek(date);
		int countOfThisWeekGame = 0;
		for (Fixtures cFx : finalFixtureWithGroundTime) {
			if (startDateOfWeek.before(cFx.getGameDate()) && endDateOfWeek.after(cFx.getGameDate())) {
				countOfThisWeekGame = countOfThisWeekGame + 1;
			}
		}
		return (countOfThisWeekGame < maxGamePerWeek);
	}

	private static boolean isDateValid(List<Fixtures> finalFixtureWithGroundTime, Fixtures fixture,
			GroundDateSlot groundSlot, List<String> configuration, int numberOfMatchPerWeek) {

		for (Fixtures fx : finalFixtureWithGroundTime) {
			if (fx.getGameDate().equals(groundSlot.getGameDate()) && groundSlot.getGround().equals(fx.getGround()))
				return false;
		}

		if (configuration != null && configuration.contains(TEAM_MATCH_WEEK_CHECK) && numberOfMatchPerWeek > 0) {

			return isMatchDateValidForThisWeek(finalFixtureWithGroundTime, fixture, groundSlot.getGameDate(),
					numberOfMatchPerWeek);
		} else {
			return true;
		}
	}

	private static void updateTeamGroundMap(GroundDateSlot groundSlot, Map<Team, List<GroundDateSlot>> teamGroundMap,
			Fixtures fixture) {

		List<GroundDateSlot> team1Slots = teamGroundMap.get(fixture.getTeam1()) == null
				? new ArrayList<GroundDateSlot>()
				: teamGroundMap.get(fixture.getTeam1());
		team1Slots.add(groundSlot);
		teamGroundMap.put(fixture.getTeam1(), team1Slots);
		List<GroundDateSlot> team2Slots = teamGroundMap.get(fixture.getTeam2()) == null
				? new ArrayList<GroundDateSlot>()
				: teamGroundMap.get(fixture.getTeam2());
		team2Slots.add(groundSlot);
		teamGroundMap.put(fixture.getTeam2(), team2Slots);
	}

	private static boolean isGroundValid(GroundDateSlot groundSlot, Map<Team, List<GroundDateSlot>> teamGroundMap,
			List<Fixtures> finalFixtureWithGroundTime, Fixtures fixture, List<String> configuration) {

		boolean isValidGroundForT1 = false;
		boolean isValidGroundForT2 = false;

		if (configuration.contains(HOME_AWAY_FOR_TEAM)) {
			Team team1 = fixture.getTeam1();
			Team team2 = fixture.getTeam2();
			if (groundSlot.getGround().equals(team1.getHomeGround())
					|| groundSlot.getGround().equals(team2.getHomeGround())) {

				List<GroundDateSlot> team1GroundsMapped = teamGroundMap.get(team1);
				List<GroundDateSlot> team2GroundsMapped = teamGroundMap.get(team2);
				int team1HomeGroundCount = 0;
				int team1AwayGroundCount = 0;
				int team2HomeGroundCount = 0;
				int team2AwayGroundCount = 0;
				if (team1GroundsMapped != null) {
					for (GroundDateSlot groundMapped : team1GroundsMapped) {
						if (team1.getHomeGround() != null && team1.getHomeGround().equals(groundMapped.getGround())) {
							++team1HomeGroundCount;
						} else {
							++team1AwayGroundCount;
						}
					}
				} else {
					isValidGroundForT1 = true;
				}
				if (team2GroundsMapped != null) {
					for (GroundDateSlot groundMapped : team2GroundsMapped) {
						if (team2.getHomeGround() != null && team2.getHomeGround().equals(groundMapped.getGround())) {
							++team2HomeGroundCount;
						} else {
							++team2AwayGroundCount;
						}
					}
				} else {
					isValidGroundForT2 = true;
				}
				if (groundSlot.getGround().equals(team1.getHomeGround())) {
					if (team1HomeGroundCount <= team1AwayGroundCount) {
						isValidGroundForT1 = true;
					}
				} else {
					if (team1HomeGroundCount >= team1AwayGroundCount) {
						isValidGroundForT1 = true;
					}
				}
				if (groundSlot.getGround().equals(team2.getHomeGround())) {
					if (team2HomeGroundCount <= team2AwayGroundCount) {
						isValidGroundForT2 = true;
					}
				} else {
					if (team2HomeGroundCount >= team2AwayGroundCount) {
						isValidGroundForT2 = true;
					}
				}

				if (groundSlot.getGround().equals(team1.getHomeGround())) {
					return ((team1HomeGroundCount - team1AwayGroundCount) <= (team2HomeGroundCount
							- team2AwayGroundCount));
				} else if (groundSlot.getGround().equals(team2.getHomeGround())) {
					return ((team1HomeGroundCount - team1AwayGroundCount) >= (team2HomeGroundCount
							- team2AwayGroundCount));
				}
			}
		} else {
			return true;
		}
		return false;
	}

	public static void mapDatesWithFixture(List<Fixtures> fixtures, List<Date> availableDates, List<String> matchTime,
			/* Map<String, String> */ List<String> configutaion, int numberOfMatchPerWeek) {

		List<Date> availableDatesWithTime = CalendarUtil.getAvilableDatsWithTime(availableDates, matchTime);

		if (configutaion != null && configutaion.contains(SHUFLE_SCHEDULE))
			Collections.shuffle(fixtures);

		if (configutaion != null && configutaion.contains(TEAM_MATCH_WEEK_CHECK) && numberOfMatchPerWeek > 0) {
			mapFixtureDateWithTeamOnceWeekCondition(fixtures, availableDatesWithTime, matchTime, numberOfMatchPerWeek);
		} else {
			mapFixtureDateWithTeam(fixtures, availableDatesWithTime, matchTime);

		}

	}

	private static void mapFixtureDateWithTeam(List<Fixtures> fixtures, List<Date> availableDatesWithTime,
			List<String> matchTime) {
		int dateIndex = 0;
		for (Fixtures fixture : fixtures) {
			fixture.setGameDate(availableDatesWithTime.get(dateIndex++));
		}

	}

	private static void mapFixtureDateWithTeamOnceWeekCondition(List<Fixtures> fixtures,
			List<Date> availableDatesWithTime, List<String> matchTime, int numberOfMatchPerWeek) {

		List<Fixtures> fixtureWithDate = new ArrayList<>();
		for (Fixtures fixture : fixtures) {
			// TeamMapKey teamMapKey = new TeamMapKey(fixture);
			// Collections.sort(availableDatesWithTime);
			Iterator<Date> itr = availableDatesWithTime.iterator();
			while (itr.hasNext()) {
				Date gameDate = itr.next();
				boolean countThisWeekMatch = isMatchDateValidForThisWeek(fixtureWithDate, fixture, gameDate,
						numberOfMatchPerWeek);
				if (countThisWeekMatch) {
					fixture.setGameDate(gameDate);
					itr.remove();
					fixtureWithDate.add(fixture);
					break;
				}
			}
		}
		Collections.sort(fixtures, new ShortFixtureByMatchDate());
	}

	private static boolean isMatchDateValidForThisWeek(List<Fixtures> fixtureWithDate, Fixtures fixture, Date gameDate,
			int numberOfMatchPerWeek) {

		int team1CountForWeek = 0;
		int team2CountForWeek = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(gameDate); // Now use today date.
		c.add(Calendar.DATE, -3); // Adding 5 days
		Date startRange = c.getTime();
		c.add(Calendar.DATE, 5); // Adding 5 days
		Date endRange = c.getTime();

		for (Fixtures fx : fixtureWithDate) {

			if (isFixtureHasOneSameTeam(fx, fixture) && fx.getGameDate().after(startRange)
					&& fx.getGameDate().before(endRange)) {
				if (fx.getTeam1().equals(fixture.getTeam1()) || fx.getTeam2().equals(fixture.getTeam1())) {
					team1CountForWeek = team1CountForWeek + 1;
				}
				if (fx.getTeam1().equals(fixture.getTeam2()) || fx.getTeam2().equals(fixture.getTeam2())) {
					team2CountForWeek = team2CountForWeek + 1;
				}

			}
		}
		if (team1CountForWeek < numberOfMatchPerWeek && team2CountForWeek < numberOfMatchPerWeek)
			return true;

		return false;
	}

	private static boolean isFixtureHasOneSameTeam(Fixtures fx, Fixtures fixture) {

		if (fx.getTeam1().equals(fixture.getTeam1()) || fx.getTeam2().equals(fixture.getTeam1())
				|| fx.getTeam1().equals(fixture.getTeam2()) || fx.getTeam2().equals(fixture.getTeam2()))
			return true;

		return false;
	}

	public static int getNoOfDatesRequired(int totalMatch, int matchPerDay, int noOfUnAvailableDates) {
		return (int) (Math.ceil((double) totalMatch / matchPerDay)) + noOfUnAvailableDates;
	}

	public static List<Fixtures> getFixturesByListOfTeam(List<Team> teams, String typeOfSchdule) {
		List<Fixtures> fixtures = new ArrayList<Fixtures>();

		switch (typeOfSchdule) {

		case ROUND_ROBIN:
			fixtures = generateFixturesForTeams(teams, fixtures);
			break;
		case DOUBLE_ROUND_ROBIN:
			fixtures = generateFixturesForTeams(teams, fixtures);
			fixtures.addAll(fixtures);
			break;
		default:
			fixtures = generateFixturesForTeams(teams, fixtures);
			break;
		}

		return fixtures;

	}

	private static List<Fixtures> generateFixturesForTeams(List<Team> teamList, List<Fixtures> fixtures) {

		if (teamList.size() % 2 != 0) {
			teamList.add(new Team(-1, "Dummy")); // If odd number of teams add a dummy
		}

		int numDays = (teamList.size() - 1); // Days needed to complete tournament
		int halfSize = teamList.size() / 2;

		List<Team> teams = new ArrayList<Team>();

		teams.addAll(teamList); // Add teams to List and remove the first team
		teams.remove(0);

		int teamsSize = teams.size();

		for (int day = 0; day < numDays; day++) {

			int teamIdx = day % teamsSize;

			fixtures.add(new Fixtures(teams.get(teamIdx), teamList.get(0)));
			for (int idx = 1; idx < halfSize; idx++) {
				int firstTeam = (day + idx) % teamsSize;
				int secondTeam = (day + teamsSize - idx) % teamsSize;
				fixtures.add(new Fixtures(teams.get(firstTeam), teams.get(secondTeam)));
			}
		}
		return fixtures;
	}
	
	private static boolean getIsClubFixtureDateInBetween(Map<Integer, Integer> teamInternalClubIdMap,
			Map<Integer, List<ClubFixtureFromToDate>> clubIdFixtureFromToDateTimeMap, GroundDateSlot groundSlot, Fixtures fixture) {
		
		boolean isSlotInBetween = false;
		
		if (clubIdFixtureFromToDateTimeMap != null) {

			int team1 = fixture.getTeam1().getTeamid();
			int team2 = fixture.getTeam2().getTeamid();
			
			int internalClubId1 = 0;
			int internalClubId2 = 0;
			
			if(team1>0 && teamInternalClubIdMap.containsKey(team1)) {
				internalClubId1 = teamInternalClubIdMap.get(team1);
			}
			if(team2>0 && teamInternalClubIdMap.containsKey(team2)) {
				internalClubId2 = teamInternalClubIdMap.get(team2);
			}

			List<ClubFixtureFromToDate> team1ClubFixtureDates = null;
			List<ClubFixtureFromToDate> team2ClubFixtureDates = null;
			
			if(internalClubId1>0 && clubIdFixtureFromToDateTimeMap.containsKey(internalClubId1)) {
				team1ClubFixtureDates = clubIdFixtureFromToDateTimeMap.get(internalClubId1);
			}
			if(internalClubId2>0 && clubIdFixtureFromToDateTimeMap.containsKey(internalClubId2)) {
				team2ClubFixtureDates = clubIdFixtureFromToDateTimeMap.get(internalClubId2);
			}
			
			if(!CommonUtility.isListNullEmpty(team1ClubFixtureDates)) {
				for(ClubFixtureFromToDate dto : team1ClubFixtureDates) {
					if (isClubSlotInbetween(groundSlot, dto)) {
						isSlotInBetween = true;
						break;
					}
				}
			}
			if(!isSlotInBetween && !CommonUtility.isListNullEmpty(team2ClubFixtureDates)) {
				for(ClubFixtureFromToDate dto : team2ClubFixtureDates) {
					if (isClubSlotInbetween(groundSlot, dto)) {
						isSlotInBetween = true;
						break;
					}
				}
			}
		}
		return isSlotInBetween;
	}
}
