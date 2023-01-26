package com.cricket.scheduler;

import java.util.Date;

public class Team implements Comparable<Team>{
	
	private String teamName;
	private int teamid;
	private Ground homeGround;
	private Date exclusionDate;

	public Team() {
		super();
	}

	public Team( int teamid,String teamName) {
		super();
		this.teamName = teamName;
		this.teamid = teamid;
		
	}
	
	public Team( int teamid,String teamName, Ground homeGround) {
		super();
		this.teamName = teamName;
		this.teamid = teamid;
		this.homeGround = homeGround;
		
	}
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getTeamid() {
		return teamid;
	}
	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}
	public Ground getHomeGround() {
		return homeGround;
	}
	public void setHomeGround(Ground homeGround) {
		this.homeGround = homeGround;
	}

	@Override
	public String toString() {
		return "[T : " + teamid + " G" + homeGround + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + teamid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (teamid != other.teamid)
			return false;
		return true;
	}

	@Override
	public int compareTo(Team o) {
		// TODO Auto-generated method stub
		if( this.teamid < o.teamid) return -1;
		else if( this.teamid > o.teamid) return 1;
		else return 0;
	}

	public Date getExclusionDate() {
		return exclusionDate;
	}

	public void setExclusionDate(Date exclusionDate) {
		this.exclusionDate = exclusionDate;
	}
	
}
