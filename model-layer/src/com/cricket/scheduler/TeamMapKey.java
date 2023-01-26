package com.cricket.scheduler;

public class TeamMapKey {
	Team team1;
	Team team2;
	
	public TeamMapKey(Fixtures fixture) {
		super();
		this.team1 = fixture.getTeam1();
		this.team2 = fixture.getTeam2();
	}
	
	public TeamMapKey() {
		super();
	}
	
	public TeamMapKey(Team team1, Team team2) {
		super();
		this.team1 = team1;
		this.team2 = team2;
	}



	public Team getTeam1() {
		return team1;
	}
	public void setTeam1(Team team1) {
		this.team1 = team1;
	}
	public Team getTeam2() {
		return team2;
	}
	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((team1 == null) ? 0 : team1.hashCode());
		result = prime * result + ((team2 == null) ? 0 : team2.hashCode());
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
		TeamMapKey other = (TeamMapKey) obj;
		if (team1 == null) {
			if (other.team1 != null)
				return false;
		} else if (!team1.equals(other.team1))
			return false;
		if (team2 == null) {
			if (other.team2 != null)
				return false;
		} else if (!team2.equals(other.team2))
			return false;
		return true;
	}
	

	
}
