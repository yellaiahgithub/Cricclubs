package com.cricket.dao;

import java.util.List;

import com.cricket.dto.GalleryDto;
import com.cricket.dto.GalleryRecordDto;

public class GalleryFactory {

	private static GalleryDAO galleryDao = null;

	private static GalleryDAO getDaoInstance() {
		if (galleryDao == null) {
			galleryDao = new GalleryDAO();
		}
		return galleryDao;
	}

	public static boolean saveGallery(GalleryDto galleryDto, GalleryRecordDto grDto) {
		return getDaoInstance().saveGallery(galleryDto, grDto);
	}

	public static boolean deleteGalleryByid(int id) {
		return getDaoInstance().deleteGalleryByid(id);
	}

	// clubId, associationType, associationId, teamId, ballNum, overNum, galleryType

	public static List<GalleryDto> getGalleryDataForPlayer(int clubId, int playerId, String gallerytype) {
		return getDaoInstance().getGalleryData(clubId, "player", playerId, 0, 0, 0, gallerytype);
	}

	public static List<GalleryDto> getGalleryDataForTeam(int clubId, int teamId, String gallerytype) {
		return getDaoInstance().getGalleryData(clubId, "team", teamId, 0, 0, 0, gallerytype);
	}

	public static List<GalleryDto> getGalleryDataForMatch(int clubId, int matchId, String galleryType) {
		return getDaoInstance().getGalleryData(clubId, "match", matchId, 0, 0, 0, galleryType);
	}
	
	public static List<GalleryDto> getGalleryDataForSeries(int clubId, int teamId, String gallerytype) {
		return getDaoInstance().getGalleryData(clubId, "series", teamId, 0, 0, 0, gallerytype);
	}
	
}
