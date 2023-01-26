package com.cricket.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.cricket.dao.MatchTeamsFactory;
import com.cricket.dto.MatchTeamsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import jersey.repackaged.com.google.common.base.MoreObjects;

public class GuavaCacheManager {
	
	private static int timeOut = 1;
	
	public static void main(String args[]) {
		//guavaTest();	
		getTeamsByMatch(67);
	}
	
	private static void getTeamsByMatch(int matchId) {
		   
	      //create a cache for employees based on their employee id
	      LoadingCache<Integer, List<MatchTeamsDto>> matchTeamsCache = 
	         CacheBuilder.newBuilder()
	         .maximumSize(5)                             // maximum 100 records can be cached
	         .expireAfterAccess(timeOut, TimeUnit.SECONDS)      // cache will expire after 30 minutes of access
	         .build(new CacheLoader<Integer, List<MatchTeamsDto>>() {  // build the cacheloader
	            
	            @Override
	            public List<MatchTeamsDto> load(Integer matchId) throws Exception {
	               //make the expensive call
	               return getFromDB(matchId);
	            }
				private List<MatchTeamsDto> getFromDB(Integer matchId) {
					
					List<MatchTeamsDto> mtDtos = new ArrayList<MatchTeamsDto>();
					try {						
						mtDtos =  MatchTeamsFactory.getMatchTeamDtosByUserAndMatchId(0, matchId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					System.out.println("#### From Database ####");
					
					return mtDtos;
				} 
	         });

	      try {			
	         //on first invocation, cache will be populated with corresponding
	         //employee record
	    	  
				for (int i = 0; i <= 10; i++) {

					System.out.println("Invocation # " + i);

					ObjectMapper mapper = new ObjectMapper();

					List<MatchTeamsDto> matchTeamsList1 = matchTeamsCache.get(67);

					for (MatchTeamsDto mtDto : matchTeamsList1) {

						if (mtDto.getUserId() == 1551625) {
							try {
								String jsonString = mapper.writeValueAsString(mtDto);
								System.out.println("ResultingJSONstring = " + jsonString);
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
						}
					}
				}
	         
	      } catch (ExecutionException e) {
	         e.printStackTrace();
	      }
	   
	}
	
	private static void guavaTest() {

		   
	      //create a cache for employees based on their employee id
	      LoadingCache<String, Employee> employeeCache = 
	         CacheBuilder.newBuilder()
	         .maximumSize(100)                             // maximum 100 records can be cached
	         .expireAfterAccess(timeOut, TimeUnit.MINUTES)      // cache will expire after 30 minutes of access
	         .build(new CacheLoader<String, Employee>() {  // build the cacheloader
	            
	            @Override
	            public Employee load(String empId) throws Exception {
	               //make the expensive call
	               return getFromDatabase(empId);
	            } 
	         });

	      try {			
	         //on first invocation, cache will be populated with corresponding
	         //employee record
	         System.out.println("Invocation #1");
	         System.out.println(employeeCache.get("100"));
	         System.out.println(employeeCache.get("103"));
	         System.out.println(employeeCache.get("110"));
	         
	         //second invocation, data will be returned from cache
	         System.out.println("Invocation #2");
	         System.out.println(employeeCache.get("100"));
	         System.out.println(employeeCache.get("103"));
	         System.out.println(employeeCache.get("110"));

	      } catch (ExecutionException e) {
	         e.printStackTrace();
	      }
	   
	}

	   private static Employee getFromDatabase(String empId) {
	   
	      Employee e1 = new Employee("Mahesh", "Finance", "100");
	      Employee e2 = new Employee("Rohan", "IT", "103");
	      Employee e3 = new Employee("Sohan", "Admin", "110");

	      Map<String, Employee> database = new HashMap<String, Employee>();
	      
	      database.put("100", e1);
	      database.put("103", e2);
	      database.put("110", e3);
	      
	      System.out.println("Database hit for" + empId);
	      
	      return database.get(empId);		
	   }
	}

	class Employee {
	   String name;
	   String dept;
	   String emplD;

	   public Employee(String name, String dept, String empID) {
	      this.name = name;
	      this.dept = dept;
	      this.emplD = empID;
	   }
	   
	   public String getName() {
	      return name;
	   }
	   
	   public void setName(String name) {
	      this.name = name;
	   }
	   
	   public String getDept() {
	      return dept;
	   }
	   
	   public void setDept(String dept) {
	      this.dept = dept;
	   }
	   
	   public String getEmplD() {
	      return emplD;
	   }
	   
	   public void setEmplD(String emplD) {
	      this.emplD = emplD;
	   }

	   @Override
	   public String toString() {
	      return MoreObjects.toStringHelper(Employee.class)
	      .add("Name", name)
	      .add("Department", dept)
	      .add("Emp Id", emplD).toString();
	   }	
	
}
