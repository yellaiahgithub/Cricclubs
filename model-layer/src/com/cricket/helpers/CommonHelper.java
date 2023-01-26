package com.cricket.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cricket.dao.TeamFactory;
import com.cricket.dto.LeagueDto;
import com.cricket.dto.TeamDto;
import com.cricket.dto.lite.GroupBean;
import com.cricket.dto.lite.TeamBean;
import com.cricket.utility.CommonUtility;

public class CommonHelper {

	public List<GroupBean> createGroupsFromTeams(List<TeamDto> teams, LeagueDto league, String groupName, int clubId)
			throws Exception {
		List<GroupBean> groups = new ArrayList<GroupBean>();
		if (league != null) {
			Map<Integer, String> groupNames = TeamFactory.getGroupNames(league.getLeagueId(), clubId);
			if (league.getGroups() == 1) {
				GroupBean singleGroup = new GroupBean();
				singleGroup.setGroupName("");
				List<TeamBean> teamBeans = new ArrayList<TeamBean>();
				for (TeamDto team : teams) {
					teamBeans.add(getBeanFromTeam(team, clubId));
				}
				singleGroup.setTeams(teamBeans);
				groups.add(singleGroup);
			} else {
				for (int i = 1; i <= league.getGroups(); i++) {
					GroupBean group = new GroupBean();
					String name = groupNames.get(i);
					if (name == null) {
						name = groupName + CommonUtility.numberToAlphabet(i);
					}
					group.setGroupName(name);
					group.setGroupId(i);
					groups.add(group);
				}

				for (TeamDto team : teams) {
					GroupBean group = getGroupForTeam(team, groups);
					if (group.getTeams() == null) {
						group.setTeams(new ArrayList<TeamBean>());
					}
					group.getTeams().add(getBeanFromTeam(team, clubId));
				}
			}
		}
		return groups;
	}
	
	private TeamBean getBeanFromTeam(TeamDto team, int clubId) {
		TeamBean teamBean = new TeamBean();
		teamBean.setTeam(team);
		if (team.isLogoExists(clubId)) {
			teamBean.setImage(clubId + "-teamLogo-" + team.getTeamID() + ".jpg");
		} else {
			teamBean.setImage("default.jpg");
		}

		return teamBean;
	}
	private GroupBean getGroupForTeam(TeamDto team, List<GroupBean> groups) {
		for (GroupBean group : groups) {
			if (team.getGroup() == group.getGroupId()) {
				return group;
			}
		}
		return null;
	}
}
