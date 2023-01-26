/*
 * Created on Apr 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.cricket.dto.PlayerDto;
import com.cricket.dto.TeamDto;

/**
 * @author Owner
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CricketHelper {
	public static TeamDto getTeamByTeamId(List teams, long teamId) throws Exception {
		Iterator itr = teams.iterator();
		while (itr.hasNext()) {
			TeamDto teamDto = (TeamDto) itr.next();
			if (teamId == teamDto.getTeamID()) {
				return teamDto;
			}
		}
		return null;
	}

	public static List<PlayerDto> setTeamsForPlayers(List<PlayerDto> players) {

		if (players != null && !players.isEmpty()) {
			LinkedHashMap<String, PlayerDto> playersMap = new LinkedHashMap<String, PlayerDto>();
			Iterator<PlayerDto> itr = players.iterator();
			while (itr.hasNext()) {
				PlayerDto player = (PlayerDto) itr.next();
				if (playersMap.get(String.valueOf(player.getPlayerID())) == null) {
					playersMap.put(String.valueOf(player.getPlayerID()), player);
				} else {
					PlayerDto playerFromMap = (PlayerDto) playersMap.get(String.valueOf(player.getPlayerID()));
					playerFromMap.setTeamId(player.getTeamId());
					playerFromMap.setTeamName(player.getTeamName());
				}
			}
			return new ArrayList<PlayerDto>(playersMap.values());
		}
		return new ArrayList<PlayerDto>();
	}
}