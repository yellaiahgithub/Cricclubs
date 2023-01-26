package com.cricket.dao;

import java.util.List;

import com.cricket.dto.DocumentDto;

public class DocumentsFactory {
	
	public static int MATCH_DOCUMENT = 2;
	public static int CLUB_DOCUMENT = 1;
	public static int UMPIRE_DOCUMENT = 3;
	
	
	private static DocumentsDAO documentsDao = null;
	
	private static DocumentsDAO getDaoInstance(){
		if(documentsDao == null){
			documentsDao = new DocumentsDAO();
		}
		return documentsDao;
	}

	
	public static List<DocumentDto> getAllDocuments(int limit,int clubId) throws Exception{
		return getDaoInstance().getDocuments(0,0,0,limit,clubId, false);
	}

	public static DocumentDto getDocument(int documentId,int clubId, boolean isAdmin) throws Exception{
		List<?> documents = getDaoInstance().getDocuments(documentId,0,0,0,clubId, isAdmin);
		if(documents !=null && !documents.isEmpty()){
			return (DocumentDto) documents.get(0);
		}
		return null;
	}
	public static void updateDocument(int documentId, String documentName, String documentDescription,int clubId) throws Exception{
		getDaoInstance().updateDocument(documentId,documentName,documentDescription,clubId);
	}

	public static List<DocumentDto> getAllDocuments(int clubId) throws Exception{
		return getDaoInstance().getDocuments(0,0,0,1000,clubId, false);
	}

	public static List<DocumentDto> getMatchDocuments(int matchId, int clubId, int documentType) throws Exception{
		
		if(documentType == MATCH_DOCUMENT) {
			return getDaoInstance().getDocuments(0,matchId,MATCH_DOCUMENT,100,clubId, false);
		}else if(documentType == UMPIRE_DOCUMENT) {
			return getDaoInstance().getDocuments(0,matchId,UMPIRE_DOCUMENT,100,clubId, false);
		}else {
			return getDaoInstance().getDocuments(0,matchId,CLUB_DOCUMENT,100,clubId, false);
		}
		
		
	}

	public static int saveDocument(DocumentDto dto,int clubId, int userId) throws Exception{
		return getDaoInstance().insertDocument(dto, clubId, userId);
	}
	public static int saveMatchDocument(DocumentDto dto,int clubId,int userId) throws Exception{
		if(dto.getAssociationType() > 0) {
			
		}else {
			dto.setAssociationType(MATCH_DOCUMENT);
		}
		
		return getDaoInstance().insertDocument(dto, clubId, userId);
	}
	public static void deleteDocument(int documentId,int clubId) throws Exception{
		getDaoInstance().deleteDocument(documentId, clubId);
	}


	public static List<DocumentDto> getAllDocuments(int clubId, boolean isAdmin) throws Exception {
		return getDaoInstance().getDocuments(0,0,0,1000,clubId, isAdmin);
	}


	public static void hideDocument(int documentId, int clubId, boolean isHide) throws Exception {
		getDaoInstance().hideDocument(documentId, clubId, isHide);
	}
}
