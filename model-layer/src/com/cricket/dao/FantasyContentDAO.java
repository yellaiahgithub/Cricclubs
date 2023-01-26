package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cricket.dto.FantasyContentDto;
import com.cricket.dto.FantasyFaqsDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class FantasyContentDAO {
	
	protected String getContentDesc(String name) throws Exception {
		
		String query = " select description from fantasy_content where name = ? ";
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		String contentDesc = null;
		try {
			st = conn.prepareStatement(query);			
			if(!CommonUtility.isNullOrEmptyOrNULL(name)) {
				st.setString(1,name);
			}			
			rs = st.executeQuery();			
			while (rs.next()) {
				contentDesc = rs.getString(1);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return contentDesc;
	}
	
	protected Map<String,String> getContentMap() throws Exception {
		
		String query = " select name, description from fantasy_content ";
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<String,String> contentMap = new HashMap<String, String>();
		try {
			st = conn.prepareStatement(query);			
			rs = st.executeQuery();			
			while (rs.next()) {
				contentMap.put(rs.getString(1), rs.getString(2));
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return contentMap;
	}
	
	protected List<FantasyFaqsDto> getFantasyFaqs() throws Exception {
		
		String query = " select id,question,answer from fantasy_faqs";
		
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		List<FantasyFaqsDto> faqList = new ArrayList<FantasyFaqsDto>();
		FantasyFaqsDto faq = null;
		try {
			st = conn.prepareStatement(query);			
			rs = st.executeQuery();			
			while (rs.next()) {
				
				faq = new FantasyFaqsDto();
				
				faq.setId(rs.getInt(1));
				faq.setQuestion(rs.getString(2));
				faq.setAnswer(rs.getString(3));
				
				faqList.add(faq);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return faqList;
	}	
	
	protected void updateContestDesc(FantasyContentDto contentDto) throws Exception {

		String query = "update fantasy_content set description = ? where name = ?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			if(!CommonUtility.isNullOrEmpty(contentDto.getDescription())) {
				st.setString(1, contentDto.getDescription());
			}
			if(!CommonUtility.isNullOrEmpty(contentDto.getName())) {
				st.setString(2, contentDto.getName());
			}
			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}
	}

}
