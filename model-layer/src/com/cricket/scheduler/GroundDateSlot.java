package com.cricket.scheduler;

import java.util.Comparator;
import java.util.Date;

public class GroundDateSlot {

	private Ground ground;
	private Date gameDate;
	
	public Ground getGround() {
		return ground;
	}
	
	public GroundDateSlot(Ground ground, Date gameDate) {
		this.ground = ground;
		this.gameDate = gameDate;
	}

	public void setGround(Ground ground) {
		this.ground = ground;
	}
	public Date getGameDate() {
		return gameDate;
	}
	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}

	@Override
	public String toString() {
		return "GSLT [ G : " + ground + ", D : " + gameDate
				+ "]";
	}
	
	
}


class ShortGameSlotByGameDate  implements Comparator<GroundDateSlot> 
{ 
  
	@Override
	public int compare(GroundDateSlot o1, GroundDateSlot o2) {
		return o1.getGameDate().compareTo(o2.getGameDate());
	} 

}
class ShortGameSlotByGroundId  implements Comparator<GroundDateSlot> 
{ 
  
	@Override
	public int compare(GroundDateSlot o1, GroundDateSlot o2) {
		return o1.getGround().getGroundid() - o2.getGround().getGroundid();
	} 

}
