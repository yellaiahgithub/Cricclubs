package com.cricket.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dao.ClubFactory;
import com.cricket.dao.CustomPagesFactory;
import com.cricket.dao.LeagueFactory;
import com.cricket.dao.SponsorFactory;
import com.cricket.dto.ClubDto;
import com.cricket.dto.CustomPageDto;
import com.cricket.dto.SponsorDto;
import com.cricket.dto.lite.ClubDtoLite;
import com.cricket.dto.lite.ClubLiteUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ClubCacheHandler {
	
	private static long IN_MEMORY_CACHETIME_LITE;
	private static final int IN_MEMORY_CACHETIMEOUT_LITE = 2*60*1000;
	private static Map<Integer, ClubDtoLite> CLUBS_LITE_INMEMORY_MAP = new HashMap<Integer, ClubDtoLite>();
	private static Map<Integer, ClubLiteUI> CLUBS_LITE_UI_INMEMORY_MAP = new HashMap<Integer, ClubLiteUI>();
	private static final String CLUBS = "CLUBS";
	private static final String CLUBS_LITE = "CLUBS_LITE";
	private static final String CLUBS_LITE_UI = "CLUBS_LITE_UI";
	private static boolean isRefreshClubInProfress = false;
	
	private static final Logger log = LoggerFactory.getLogger(ClubCacheHandler.class);
	
	public static ClubDto getClubFromCache(int clubId) throws Exception {
		Gson gson = new Gson();
		String dto = RedisCacheManager.getFromMapInBucket(CLUBS, String.valueOf(clubId));
		try{
			return gson.fromJson(dto, ClubDto.class)	;
		}catch(Exception ex){
			log.error(ex.getMessage()); 
			return null;
		}
	}
	
	public static String getClubIdByShortCutURL(String shortUrl){
		if(CommonUtility.isNullOrEmptyOrNULL(shortUrl) || shortUrl.equalsIgnoreCase("utils") || shortUrl.equalsIgnoreCase("theme") ||shortUrl.equalsIgnoreCase("undefined") || shortUrl.equalsIgnoreCase("favicon.ico")) return null;
		String returnValue = null;
		try {
			List<ClubDtoLite> liteClubs = new ArrayList(CLUBS_LITE_INMEMORY_MAP.values());
			refreshClubLiteInMemoryIfRequired();
			for(ClubDtoLite cbl: liteClubs) {
				if(shortUrl.equalsIgnoreCase(cbl.getShortURL())) {
					returnValue = cbl.getClubId()+"";
					break;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e); 
		}
		if(CommonUtility.isNullOrEmptyOrNULL(returnValue)){
			try {
				returnValue = String.valueOf(ClubFactory.getClubIdByShortUrl(shortUrl));
				if(CommonUtility.isNullOrEmptyOrNULL(returnValue) || "0".equals(returnValue)){
					returnValue = null;
				}
			} catch (Exception e) {
				log.error(e.getMessage(),e); 
			}
		}
		
		return returnValue;
	}
	
	private static synchronized void refreshClubLiteInMemoryIfRequired() {
		if ((IN_MEMORY_CACHETIME_LITE + IN_MEMORY_CACHETIMEOUT_LITE) < System.currentTimeMillis()) {
			IN_MEMORY_CACHETIME_LITE = System.currentTimeMillis();
			if (RedisCacheManager.isRedisAvailable) {
				updateLiteClubAndUIFromRedis();
			}
		}

	}

	public static void setClubToCache(ClubDto club)  throws Exception {
		
		RedisCacheManager.putIntoBucket(CLUBS, String.valueOf(club.getClubId()), CommonUtility.objectToJson(club));
		ClubDtoLite clubLDto =new ClubDtoLite(club);
		setClubLiteToCache(clubLDto);
		setClubLiteUIToCache(club);
	}

	public static void setClubLiteToCache(ClubDtoLite clubLDto) {
		try {

			RedisCacheManager.putIntoBucket(CLUBS_LITE, String.valueOf(clubLDto.getClubId()),  CommonUtility.objectToJson(clubLDto));
			CLUBS_LITE_INMEMORY_MAP.put(clubLDto.getClubId(), clubLDto);			
		} catch (Exception e) {
			log.error(e.getMessage(), e); 
		}
	}

	private static void setClubLiteUIToCache(ClubDto club) {
		try {
			List<SponsorDto> sponsors = club.getSponsors();
			List<CustomPageDto> pages = CustomPagesFactory.getAllPages(club.getClubId(), false);
			ClubLiteUI clubLiteUI = new ClubLiteUI();
			clubLiteUI.setClubId(club.getClubId());
			clubLiteUI.setSponsors(sponsors);
			clubLiteUI.setPages(pages);
			CLUBS_LITE_UI_INMEMORY_MAP.put(club.getClubId(), clubLiteUI);
			RedisCacheManager.putIntoBucket(CLUBS_LITE_UI, club.getClubId()+"", CommonUtility.objectToJson(clubLiteUI));
		} catch (Exception e) {
			log.error(e.getMessage(),e); 
		}
	}

	public static void clearClubFromCache(int clubId) throws Exception {
			try {
				RedisCacheManager.deleteFieldFromBucket(CLUBS, String.valueOf(clubId));
				RedisCacheManager.deleteFieldFromBucket(CLUBS_LITE, String.valueOf(clubId));
				RedisCacheManager.deleteFieldFromBucket(CLUBS_LITE_UI, String.valueOf(clubId));
				CLUBS_LITE_INMEMORY_MAP.remove(clubId);
				CLUBS_LITE_UI_INMEMORY_MAP.remove(clubId);
			} catch (Exception e) {
				log.error(e.getMessage(),e); 
			}
	}
	
	public static List<ClubDtoLite> getLiteClubsFromCache() {
		List<ClubDtoLite> clubLiteDtos = null;
		try {
			clubLiteDtos = new ArrayList(CLUBS_LITE_INMEMORY_MAP.values());
			if(clubLiteDtos != null && !clubLiteDtos.isEmpty() && ((IN_MEMORY_CACHETIME_LITE + IN_MEMORY_CACHETIMEOUT_LITE) > System.currentTimeMillis())){
				return clubLiteDtos;
			}
			refreshClubLiteInMemoryIfRequired();
			clubLiteDtos = new ArrayList(CLUBS_LITE_INMEMORY_MAP.values());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return clubLiteDtos;
	}
	
	public static List<ClubDtoLite> getLiteClubsFromINMemory(int clubId)throws Exception {
		List<ClubDtoLite> clubLiteDtos = null;
		List<ClubDtoLite> clubLiteList = null; 
		try {
			clubLiteDtos = new ArrayList(CLUBS_LITE_INMEMORY_MAP.values());	
			if(!CommonUtility.isListNullEmpty(clubLiteDtos) && clubId>0) {
				for(ClubDtoLite club : clubLiteDtos){
					if(club.getClubId()==clubId) {
						clubLiteList = new ArrayList<ClubDtoLite>();
						clubLiteList.add(club);
						return clubLiteList;
					}
				}	
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return clubLiteDtos;
	}

	private static void updateLiteClubAndUIFromRedis() {
		Map<String, String>  idClubMap = RedisCacheManager.getFromBucket(CLUBS_LITE);
		if(idClubMap != null){
			Collection<String> clubsJson =  idClubMap.values();
			if(clubsJson != null && !clubsJson.isEmpty()){
				
				Map<Integer, ClubDtoLite> clubsLiteLocalMap = new HashMap<Integer, ClubDtoLite>();
				Gson gson = new Gson();
				
				for(String clJson : clubsJson){
					ClubDtoLite cl = gson.fromJson(clJson, ClubDtoLite.class);
					//CLUBS_LITE_INMEMORY_MAP.put(cl.getClubId(), cl);
					clubsLiteLocalMap.put(cl.getClubId(), cl);
				}
				CLUBS_LITE_INMEMORY_MAP.putAll(clubsLiteLocalMap);
			}
		}
		Map<String, String>  idClubUIMap = RedisCacheManager.getFromBucket(CLUBS_LITE_UI);
		if(idClubUIMap != null){
			Collection<String> uiClubsJson =  idClubUIMap.values();
			if(uiClubsJson != null && !uiClubsJson.isEmpty()){
				
				Gson gson = new Gson();
				Map<Integer, ClubLiteUI> clubLiteUiLocalMap = new HashMap<Integer, ClubLiteUI>();
				
				for(String clJson : uiClubsJson){
					ClubLiteUI clui = gson.fromJson(clJson, ClubLiteUI.class);
					//CLUBS_LITE_UI_INMEMORY_MAP.put(clui.getClubId(), clui);
					clubLiteUiLocalMap.put(clui.getClubId(), clui);
				}
				CLUBS_LITE_UI_INMEMORY_MAP.putAll(clubLiteUiLocalMap);
			}
		}
	}
	
	public static ClubDtoLite getClubLiteFromCache(int clubId) throws Exception {
		try {
			ClubDtoLite clubDtoLite = CLUBS_LITE_INMEMORY_MAP.get(clubId);
			if (clubDtoLite == null) {
				Gson gson = new Gson();
				String dto = RedisCacheManager.getFromMapInBucket(CLUBS_LITE, String.valueOf(clubId));
				clubDtoLite = gson.fromJson(dto, ClubDtoLite.class);
				if (clubDtoLite != null) {
					CLUBS_LITE_INMEMORY_MAP.put(clubId, clubDtoLite);
				}
				refreshClubLiteInMemoryIfRequired();
			}
			return clubDtoLite;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	

	public static List<SponsorDto> getSponsorsFromCache(int clubId) {

		try {
			ClubLiteUI clubLiteUI = CLUBS_LITE_UI_INMEMORY_MAP.get(clubId);
			if(clubLiteUI != null && clubLiteUI.getSponsors() != null) {
				return clubLiteUI.getSponsors();
			}else {
				clubLiteUI=	getClubLiteUIFromRedis(clubId);
				if(clubLiteUI != null && clubLiteUI.getSponsors() != null) {
					CLUBS_LITE_UI_INMEMORY_MAP.put(clubId, clubLiteUI);
					return clubLiteUI.getSponsors();
				}
			}
			refreshClubLiteInMemoryIfRequired();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	public static ClubLiteUI getClubLiteUI(int clubId) {
		ClubLiteUI clubLiteUI = null;
		try {
			clubLiteUI = CLUBS_LITE_UI_INMEMORY_MAP.get(clubId);
			if(clubLiteUI == null) {
				clubLiteUI = getClubLiteUIFromRedis(clubId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		refreshClubLiteInMemoryIfRequired();
		return clubLiteUI;
	}
	
	public static ClubLiteUI getClubLiteUIFromRedis(int clubId) {
		ClubLiteUI clubLiteUI = null;
		try {
			String dto = RedisCacheManager.getFromMapInBucket(CLUBS_LITE_UI, String.valueOf(clubId));
			if (CommonUtility.isNullOrEmpty(dto)) {
				Gson gson = new Gson();
				clubLiteUI = gson.fromJson(dto, ClubLiteUI.class);
			}
		} catch (JsonSyntaxException e) {
			log.error(e.getMessage(),e);
		}
		return clubLiteUI;
	}
	
	public static void setSponsorsToClubLiteUICache(List<SponsorDto> sponsors, int clubId) {
		if (sponsors != null && !sponsors.isEmpty() && clubId > 0) {
			ClubLiteUI clubLiteUI = CLUBS_LITE_UI_INMEMORY_MAP.get(clubId);
			if(clubLiteUI == null) {
				clubLiteUI = getClubLiteUIFromRedis(clubId);
				if(clubLiteUI == null) {
					clubLiteUI = new ClubLiteUI();
					clubLiteUI.setClubId(clubId);
				}
			}
			clubLiteUI.setSponsors(sponsors);
			Gson gson = new Gson();
			CLUBS_LITE_UI_INMEMORY_MAP.put(clubId, clubLiteUI);
			RedisCacheManager.putIntoBucket(CLUBS_LITE_UI, clubId + "", gson.toJson(clubLiteUI));
		}
	}
	public static void deletePagesFromCache(int clubId) {
		setCustomPagesToClubLiteUICache(null ,clubId);
	}

	public static void setCustomPagesToClubLiteUICache(List<CustomPageDto> customPages, int clubId) {

		if (clubId > 0) {
			ClubLiteUI clubLiteUI = CLUBS_LITE_UI_INMEMORY_MAP.get(clubId);
			if(clubLiteUI == null) {
				clubLiteUI = getClubLiteUIFromRedis(clubId);
				if(clubLiteUI == null) {
					clubLiteUI = new ClubLiteUI();
					clubLiteUI.setClubId(clubId);
				}
			}
			clubLiteUI.setPages(customPages);
			Gson gson = new Gson();
			CLUBS_LITE_UI_INMEMORY_MAP.put(clubId, clubLiteUI);
			RedisCacheManager.putIntoBucket(CLUBS_LITE_UI, clubId + "", gson.toJson(clubLiteUI));
		}
	
	}

	public static List<ClubDto> getClubsFromCache() {
		List<ClubDto> clubDtos = new ArrayList<ClubDto>();
		try {
			Map<String, String> idClubMap;
			idClubMap = RedisCacheManager.getFromBucket(CLUBS);
			if(idClubMap != null){
				Collection<String> clubsJson =  idClubMap.values();
				if(clubsJson != null && !clubsJson.isEmpty()){
					clubDtos = new ArrayList<ClubDto>();
					Gson gson = new Gson();
					for(String clJson : clubsJson){
						clubDtos.add(gson.fromJson(clJson, ClubDto.class));
					}
				}
			}
			if((clubDtos == null || clubDtos.isEmpty()) && !RedisCacheManager.isConnected()){
				clubDtos = ClubFactory.getAllActiveClubsFromDB();
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return clubDtos;
	}
	
	public static String getClubIdByDomain(String domain) {
		String clubId = null;
		try {
			if(CommonUtility.isNullOrEmptyOrNULL(domain) || domain.equalsIgnoreCase("undefined") || domain.equalsIgnoreCase("favicon.ico")|| domain.equalsIgnoreCase("utils")) return null;
			
				domain = domain.contains("www.")?domain.replaceAll("www.", ""):domain;
				domain = domain.contains("WWW.")?domain.replaceAll("WWW.", ""):domain;
				List<ClubDtoLite> clubLiteDtos = new ArrayList(CLUBS_LITE_INMEMORY_MAP.values());
				for(ClubDtoLite cbl: clubLiteDtos) {
					List<String> customDomains = CommonUtility.splitString(cbl.getCustomDomain());
					for(String csDomain : customDomains) {
						if(domain.equalsIgnoreCase(csDomain)) {
							clubId = cbl.getClubId()+"";
							break;
						}
					}
				}
				if(CommonUtility.isNullOrEmptyOrNULL(clubId)){
					try {
						clubId = ClubFactory.getClubIdByCustomDomain(domain)+"";
						
						if(CommonUtility.isNullOrEmptyOrNULL(clubId)) {
							return null; 
						}
					} catch (Exception e) {
						log.error("Exception while getting custom domain from DB - " + domain + "  " + e.getMessage());
						clubId = null;
					}
				}
				refreshClubLiteInMemoryIfRequired();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return clubId;
	}
	
	public static void refreshAllClubCache() throws Exception {
		if (!isRefreshClubInProfress) {
			isRefreshClubInProfress = true;
			List<ClubDto> clubs = ClubFactory.getAllActiveClubsFromDB();
			if (clubs != null && !clubs.isEmpty()) {
				for (ClubDto club : clubs) {
					try {
						setClubLeagueAndRefresh(club);
					}catch(Exception e) {
						log.error("Refresh Cache Issue - ClubId -"+club.getClubId()+e.getMessage(),e);
					}
				}
			}
			isRefreshClubInProfress = false;
		}
	}
	
	public static void setClubLeagueAndRefresh(ClubDto club) throws Exception {
		if(club!=null){
			club.setLeagueList(LeagueFactory.getLeagues(club.getClubId(), 1500));
			club.setSponsors(SponsorFactory.getSponsors(club.getClubId()));
			setClubToCache(club);
		}
	}

	public static List<SponsorDto> getSponsorsFromCache(int clubId, int sponsorId) {
		List<SponsorDto> sponsors = getSponsorsFromCache(clubId);
		if(sponsors != null && !sponsors.isEmpty()) {
			Iterator<SponsorDto> iterator = sponsors.iterator(); 
	        while (iterator.hasNext()) {
	        	SponsorDto sdto = iterator.next();
	        	if(sdto.getSponsorId() != sponsorId) {
	        		iterator.remove();
	        	}
	        }
		}
		refreshClubLiteInMemoryIfRequired();
		return sponsors;
	}

	public static List<CustomPageDto> getCustomPagesFromCache(int clubId, int pageId) {

			try {
				ClubLiteUI clubLiteUI = CLUBS_LITE_UI_INMEMORY_MAP.get(clubId);
				if(clubLiteUI != null && clubLiteUI.getPages() != null) {
					return clubLiteUI.getPages();
				}else {
					clubLiteUI=	getClubLiteUIFromRedis(clubId);
					if(clubLiteUI != null && clubLiteUI.getPages() != null) {
						CLUBS_LITE_UI_INMEMORY_MAP.put(clubId, clubLiteUI);
						return clubLiteUI.getPages();
					}
				}
				refreshClubLiteInMemoryIfRequired();
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
			return null;
		
	}

	public static void setClubLiteUI(ClubLiteUI clubLiteUI) {
		try {
			Gson gson = new Gson();
			CLUBS_LITE_UI_INMEMORY_MAP.put(clubLiteUI.getClubId(), clubLiteUI);
			RedisCacheManager.putIntoBucket(CLUBS_LITE_UI, clubLiteUI.getClubId()+"", gson.toJson(clubLiteUI));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
