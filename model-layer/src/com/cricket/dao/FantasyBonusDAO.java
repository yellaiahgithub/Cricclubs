package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.FantasyBonusDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class FantasyBonusDAO {
	
	static Logger log = LoggerFactory.getLogger(FantasyBonusDAO.class);
	
	public String SELECT_QUERY = "select id,title,code,limit_per_user,bonus_percent,max_bonus,"
			+ "expiry_days,start_date,expiry_date,bonus_for,bonus_for_type,bonus_for_condition,show_in_app,"
			+ "status,created_at,created_by,updated_at,updated_by,description from fantasy_coupons ";
	
	private void populateFantasyBonusDto(FantasyBonusDto bonusDto, ResultSet rs) throws SQLException {
		
		bonusDto.setId(rs.getLong("id"));
		bonusDto.setTitle(rs.getString("title"));
		bonusDto.setCode(rs.getString("code"));		
		bonusDto.setLimitPerUser(rs.getInt("limit_per_user"));
		bonusDto.setBonusPercent(rs.getInt("bonus_percent"));		
		bonusDto.setMaxBonus(rs.getFloat("max_bonus"));
		bonusDto.setExpiryDays(rs.getInt("expiry_days"));
		bonusDto.setStartDate(rs.getDate("start_date"));
		bonusDto.setExpiryDate(rs.getDate("expiry_date"));
		bonusDto.setBonusFor(rs.getString("bonus_for"));
		bonusDto.setBonusForType(rs.getString("bonus_for_type"));
		bonusDto.setBonusForCondition(rs.getFloat("bonus_for_condition"));		
		bonusDto.setStatus(rs.getInt("status"));
		bonusDto.setShowInApp(rs.getInt("show_in_app"));
		bonusDto.setCreatedAt(rs.getDate("created_at"));
		bonusDto.setCreatedBy(rs.getInt("created_by"));
		bonusDto.setUpdatedAt(rs.getDate("updated_at"));
		bonusDto.setUpdatedBy(rs.getInt("updated_by"));
		bonusDto.setDescription(rs.getString("description"));	
	}

	protected List<FantasyBonusDto> getOffers(String bonusCode, int offerId, int status, 
			int showInApp, String bonusFor) throws Exception {
		
		List<FantasyBonusDto> bonusDtos = new ArrayList<FantasyBonusDto>();
		
		String query = SELECT_QUERY + " where 1=1 ";
		
		if(!CommonUtility.isNullOrEmptyOrNULL(bonusCode)) {
			query = query+ " and code = ?";
		}
		if(offerId > 0 ) {
			query = query+ " and id = ?";
		}
		if(status == 0 || status == 1 ) {
			query = query+ " and status = ?";
		}
		if(showInApp == 0 || showInApp == 1 ) {
			query = query+ " and show_in_app = ?";
		}
		if(!CommonUtility.isNullOrEmptyOrNULL(bonusFor)) {
			query = query+ " and bonus_for = ?";
		}
			
		Connection conn = DButility.getFantasyReadConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		FantasyBonusDto bonusDto = null;
		try {
			st = conn.prepareStatement(query);	
			
			int index = 1;
			
			if(!CommonUtility.isNullOrEmptyOrNULL(bonusCode)) {
				st.setString(index++,bonusCode);
			}
			if(offerId > 0 ) {
				st.setInt(index++,offerId);
			}
			if(status == 0 || status == 1 ) {
				st.setInt(index++,status);
			}
			if(showInApp == 0 || showInApp == 1 ) {
				st.setInt(index++,showInApp);
			}
			if(!CommonUtility.isNullOrEmptyOrNULL(bonusFor)) {
				st.setString(index++,bonusFor);
			}
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				bonusDto = new FantasyBonusDto();
				populateFantasyBonusDto(bonusDto,rs);				
				bonusDtos.add(bonusDto);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return bonusDtos;
	}
	
	protected long insertFantasyOffer(FantasyBonusDto offer) throws Exception {

		String query = "insert into fantasy_coupons(title,code,bonus_percent,max_bonus,limit_per_user,"
				+ "expiry_days,start_date,expiry_date,bonus_for,bonus_for_type,bonus_for_condition,"
				+ "status,show_in_app,description,created_by,created_at) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";

		long offerId = 0;
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

			int index = 1;

			st.setString(index++, offer.getTitle());
			st.setString(index++, offer.getCode());
			st.setFloat(index++, offer.getBonusPercent());
			st.setFloat(index++, offer.getMaxBonus());				
			st.setInt(index++, offer.getLimitPerUser());
			st.setInt(index++, offer.getExpiryDays());
			st.setDate(index++, new java.sql.Date(offer.getStartDate().getTime()));	
			st.setDate(index++, new java.sql.Date(offer.getExpiryDate().getTime()));
			st.setString(index++, offer.getBonusFor());
			st.setString(index++, offer.getBonusForType());
			st.setFloat(index++, offer.getBonusForCondition());
			st.setInt(index++, offer.getStatus());
			st.setInt(index++, offer.getShowInApp());
			st.setString(index++, offer.getDescription());
			st.setInt(index++, offer.getCreatedBy());

			st.executeUpdate();
			rs = st.getGeneratedKeys();

			if (rs.next()) {
				offerId = rs.getLong(1);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}

		return offerId;

	}
	
	protected void updateFantasyOffer(FantasyBonusDto offer) throws Exception {

		String query = "update fantasy_coupons set title=?,code=?,bonus_percent=?,max_bonus=?, "
				+ " limit_per_user=?,expiry_days=?,start_date=?, expiry_date=?,bonus_for=?,"
				+ " bonus_for_type=?,bonus_for_condition=?,status=?,show_in_app=?,"
				+ " description=?,updated_by=?,updated_at=NOW() where id=?";

		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);

			int index = 1;

			st.setString(index++, offer.getTitle());
			st.setString(index++, offer.getCode());
			st.setFloat(index++, offer.getBonusPercent());
			st.setFloat(index++, offer.getMaxBonus());				
			st.setInt(index++, offer.getLimitPerUser());
			st.setInt(index++, offer.getExpiryDays());
			st.setDate(index++, new java.sql.Date(offer.getStartDate().getTime()));	
			st.setDate(index++, new java.sql.Date(offer.getExpiryDate().getTime()));	
			st.setString(index++, offer.getBonusFor());
			st.setString(index++, offer.getBonusForType());
			st.setFloat(index++, offer.getBonusForCondition());
			st.setInt(index++, offer.getStatus());
			st.setInt(index++, offer.getShowInApp());
			st.setString(index++, offer.getDescription());
			st.setInt(index++, offer.getUpdatedBy());
			
			st.setLong(index++, offer.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
	}
	
}
