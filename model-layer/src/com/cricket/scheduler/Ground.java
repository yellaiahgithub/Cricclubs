package com.cricket.scheduler;

public class Ground {
	
	private int groundid;
	private String groundName;	
	
	
	public Ground(int groundid, String groundName) {
		super();
		this.groundid = groundid;
		this.groundName = groundName;
	}
	
	public String getGroundName() {
		return groundName;
	}
	public void setGroundName(String groundName) {
		this.groundName = groundName;
	}
	public int getGroundid() {
		return groundid;
	}
	public void setGroundid(int groundid) {
		this.groundid = groundid;
	}

	@Override
	public String toString() {
		return "[GID" + groundid + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + groundid;
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
		Ground other = (Ground) obj;
		if (groundid != other.groundid)
			return false;
		return true;
	}	
	
	
}
