package com.cricket.beans;

public class HealthCheckResp {

	
	private boolean dbStatus ;
	private boolean dbReadReplicaStatus;
	private boolean cacheStatus;
	private boolean graphDBStatus;
	
	public boolean isDbStatus() {
		return dbStatus;
	}
	public void setDbStatus(boolean dbStatus) {
		this.dbStatus = dbStatus;
	}
	public boolean isCacheStatus() {
		return cacheStatus;
	}
	public void setCacheStatus(boolean cacheStatus) {
		this.cacheStatus = cacheStatus;
	}
	public boolean isDbReadReplicaStatus() {
		return dbReadReplicaStatus;
	}
	public void setDbReadReplicaStatus(boolean dbReadReplicaStatus) {
		this.dbReadReplicaStatus = dbReadReplicaStatus;
	}
	public boolean isGraphDBStatus() {
		return graphDBStatus;
	}
	public void setGraphDBStatus(boolean graphDBStatus) {
		this.graphDBStatus = graphDBStatus;
	}
	
}
