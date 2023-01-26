package com.cricket.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.adconfig.AdConfigGlobalDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class CommonElasticCacheManager {
	
	private static final String CLUB_AD_CONFIG_DATA = "CLUB_AD_CONFIG_DATA";
	private static final String CLUB_MISCELLANEOUS = "CLUB_MISCELLANEOUS";
	
	private static final Logger log = LoggerFactory.getLogger(CommonElasticCacheManager.class);
	
	public static List<AdConfigGlobalDTO> getAdConfigGlobalDTOs()  {
		//String dto = RedisCacheManager.getDataFromCache(getClubKey(clubId));
		String adConfJson = RedisCacheManager.getFromMapInBucket(CLUB_MISCELLANEOUS, CLUB_AD_CONFIG_DATA);
		List<AdConfigGlobalDTO> adConfList = null;
		if(adConfJson != null){
			try {
				adConfList = new ArrayList<AdConfigGlobalDTO>();
				Gson gson = new Gson();
				
				List<Object> listObject = gson.fromJson(adConfJson, List.class);
				for(Object obj : listObject) {
					String jsonObj = gson.toJson(obj);
					AdConfigGlobalDTO adConfig = gson.fromJson(jsonObj, AdConfigGlobalDTO.class);
					adConfList.add(adConfig);
				}
/*				List<Map<String, String>> listFromJson =  gson.fromJson(adConfJson, List.class);
				final ObjectMapper mapper = new ObjectMapper();
				for(Map<String, String> map : listFromJson) {
					AdConfigGlobalDTO pojo = mapper.convertValue(map, AdConfigGlobalDTO.class);
					adConfList.add(pojo);
				}*/
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		return adConfList;
	}
	
	public static void pushadConfigIntoCache(List<AdConfigGlobalDTO> adConfis) {
		Gson gson = new Gson();
		RedisCacheManager.putIntoBucket(CLUB_MISCELLANEOUS, CLUB_AD_CONFIG_DATA, gson.toJson(adConfis));
	}
}


