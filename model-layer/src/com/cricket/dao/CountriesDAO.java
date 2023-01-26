package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cricket.dto.CountriesDto;
import com.cricket.dto.StateDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class CountriesDAO {
	
	protected List<CountriesDto> getCountries(int countryId, String countryCode)throws Exception {
		
		List<CountriesDto> countryDtoList = new ArrayList<CountriesDto>();
		
		String query = "select id,name,code,mobile_code,currency_id,currency_code,bank_identifier_label,"
				+ "bank_identifier_length,payment_gateways,image_path,fantasy_enabled,display_order "
				+ " from countries where 1=1 ";	
		
		
		if(!CommonUtility.isNullOrEmptyOrNULL(countryCode)) {
			query = query+" and code = ?";
		}else {
			query = query + " order by display_order, name ";
		}
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;		
		try {
			conn = DButility.getFantasyReadConnection("ccfantasy");
			pst = conn.prepareStatement(query);	
			if(!CommonUtility.isNullOrEmptyOrNULL(countryCode)) {
				pst.setString(1, countryCode);
			}			
			rs = pst.executeQuery();			
			while (rs.next()) {				
				CountriesDto countryDto = new CountriesDto();
				
				countryDto.setId(rs.getInt("id"));				
				countryDto.setName(rs.getString("name"));
				countryDto.setCode(rs.getString("code"));
				countryDto.setMobileCode(rs.getString("mobile_code"));
				countryDto.setCurrencyId(rs.getInt("currency_id"));
				countryDto.setCurrencyCode(rs.getString("currency_code"));	
				countryDto.setBankIdentifierLabel(rs.getString("bank_identifier_label"));
				countryDto.setBankIdentifierLength(rs.getInt("bank_identifier_length"));
				countryDto.setPaymentGateways(rs.getString("payment_gateways"));
				countryDto.setImagePath(rs.getString("image_path"));
				countryDto.setFantasyEnabled(rs.getInt("fantasy_enabled"));	
				countryDto.setDisplayOrder(rs.getInt("display_order"));	
				
				countryDtoList.add(countryDto);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return countryDtoList;
	}
	
	protected Map<String, String> getCountryCodeNameMap()throws Exception {		
		
		String query = "select code,name from countries where code is not null and name is not null "
				+ " and code != '' and name != '' order by display_order,name ";	
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;	
		Map<String, String> countryCodeNameMap = new LinkedHashMap<String, String>();
		try {
			conn = DButility.getFantasyReadConnection("ccfantasy");
			pst = conn.prepareStatement(query);	
			rs = pst.executeQuery();			
			while (rs.next()) {				
				countryCodeNameMap.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return countryCodeNameMap;
	}
	
	protected List<StateDto> getStates(String countryCode, String stateName, int stateId)throws Exception {
		
		List<StateDto> stateDtoList = new ArrayList<StateDto>();
		
		String query = "select id,name,code,country_code,fantasy_enabled from states where 1=1 ";
		
		if(!CommonUtility.isNullOrEmpty(countryCode)) {
			query = query + " and country_code = ? ";		
		}
		
		if(!CommonUtility.isNullOrEmpty(stateName)) {
			query = query + " and name = ? ";		
		}
		if(stateId>0) {
			query = query + " and id = ? ";	
		}
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;		
		try {
			conn = DButility.getFantasyReadConnection("ccfantasy");
			pst = conn.prepareStatement(query);	
			
			int index = 1;
			
			if(!CommonUtility.isNullOrEmpty(countryCode)) {
				pst.setString(index++, countryCode);					
			}			
			if(!CommonUtility.isNullOrEmpty(stateName)) {
				pst.setString(index++, stateName);				
			}	
			if(stateId>0) {
				pst.setInt(index++, stateId);		
			}			
			rs = pst.executeQuery();			
			
			while (rs.next()) {				
				StateDto stateDto = new StateDto();
				
				stateDto.setId(rs.getInt("id"));				
				stateDto.setName(rs.getString("name"));
				stateDto.setCode(rs.getString("code"));			
				stateDto.setCountryCode(rs.getString("country_code"));				
				stateDto.setFantasyEnabled(rs.getInt("fantasy_enabled"));	
				
				stateDtoList.add(stateDto);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return stateDtoList;
	}
	
	protected int insertFantasyCountry(CountriesDto countryDto) throws Exception {
		
		String query ="insert into countries(name,code,mobile_code,currency_id,currency_code,"
				+ "bank_identifier_label,bank_identifier_length,fantasy_enabled,payment_gateways,"
				+ "fantasy_enabled,display_order) values (?,?,?,?,?,?,?,?,?,?,?)";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;
		int pointsId = 0;		
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			int index=1;
			
			pst.setString(index++,countryDto.getName());
			pst.setString(index++,countryDto.getCode());
			pst.setString(index++,countryDto.getMobileCode());	
			pst.setInt(index++,countryDto.getCurrencyId());			
			pst.setString(index++,countryDto.getCurrencyCode());			
			pst.setString(index++,countryDto.getBankIdentifierLabel());
			pst.setInt(index++,countryDto.getBankIdentifierLength());
			pst.setString(index++,countryDto.getPaymentGateways());
			pst.setInt(index++,countryDto.getFantasyEnabled());
			pst.setInt(index++,countryDto.getDisplayOrder());
			
			pst.executeUpdate();			
			rs = pst.getGeneratedKeys();
			
			if (rs.next()) {
				pointsId = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return pointsId;
	}
	
	protected void updateFantasyCountry(CountriesDto countryDto) throws Exception {
		
		String query ="update countries set name=?,code=?,mobile_code=?,"
				+ "currency_id=?,currency_code=?,bank_identifier_label=?,bank_identifier_length=?,"
				+ "payment_gateways=?,fantasy_enabled=?,display_order=? where id=?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;		
		try {
			pst = conn.prepareStatement(query);
			
			int index = 1;
			
			pst.setString(index++,countryDto.getName());
			pst.setString(index++,countryDto.getCode());
			pst.setString(index++,countryDto.getMobileCode());	
			pst.setInt(index++,countryDto.getCurrencyId());			
			pst.setString(index++,countryDto.getCurrencyCode());			
			pst.setString(index++,countryDto.getBankIdentifierLabel());
			pst.setInt(index++,countryDto.getBankIdentifierLength());
			pst.setString(index++,countryDto.getPaymentGateways());
			pst.setInt(index++,countryDto.getFantasyEnabled());
			pst.setInt(index++,countryDto.getDisplayOrder());
			
			pst.setInt(index++, countryDto.getId());
			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}		
	}

	protected void deleteFantasyCountry(int countryId) throws Exception {

		String query = "delete from countries where id = ?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement(query);
			if(countryId > 0) {
				pst.setInt(1, countryId);
			}
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
	
	protected int insertFantasyState(StateDto stateDto) throws Exception {
		
		String query ="insert into states(name,code,country_code,fantasy_enabled) values (?,?,?,?)";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;
		int pointsId = 0;		
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			int index=1;
			
			pst.setString(index++,stateDto.getName());
			pst.setString(index++,stateDto.getCode());
			pst.setString(index++,stateDto.getCountryCode());	
			pst.setInt(index++,stateDto.getFantasyEnabled());
			
			pst.executeUpdate();			
			rs = pst.getGeneratedKeys();
			
			if (rs.next()) {
				pointsId = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return pointsId;
	}
	
	protected void updateFantasyState(StateDto stateDto) throws Exception {
		
		String query ="update states set name=?,code=?,country_code=?,fantasy_enabled=? where id=?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;	
		ResultSet rs = null;		
		try {
			pst = conn.prepareStatement(query);
			
			int index = 1;
			
			pst.setString(index++,stateDto.getName());
			pst.setString(index++,stateDto.getCode());
			pst.setString(index++,stateDto.getCountryCode());	
			pst.setInt(index++,stateDto.getFantasyEnabled());
			
			pst.setInt(index++, stateDto.getId());
			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}		
	}
	
	protected void deleteFantasyState(int stateId) throws Exception {

		String query = "delete from states where id = ?";
		
		Connection conn = DButility.getFantasyWriteConnection("ccfantasy");
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement(query);
			if(stateId > 0) {
				pst.setInt(1, stateId);
			}
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}
}
