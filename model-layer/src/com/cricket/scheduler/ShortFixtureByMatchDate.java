package com.cricket.scheduler;

import java.util.Comparator;

public class ShortFixtureByMatchDate  implements Comparator<Fixtures> 
{ 
  
	@Override
	public int compare(Fixtures o1, Fixtures o2) {
		// TODO Auto-generated method stub
		return o1.getGameDate().compareTo(o2.getGameDate());
	} 

}

