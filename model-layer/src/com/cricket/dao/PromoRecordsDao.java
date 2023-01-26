package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.PromoRecords;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

public class PromoRecordsDao {
	private static Logger log = LoggerFactory.getLogger(PromoRecordsDao.class);
	public void saveUpdatePromoRecords(List<PromoRecords> promoRecords, int clubId, int userId) throws Exception {
		String insertQuery;
		String updateQuery;
		PreparedStatement insertPst = null;
		PreparedStatement updatePst = null;
		Connection conn = null;
		
		try {
			conn = DButility.getConnection(clubId);
			//conn.setAutoCommit(false);
			
			updateQuery = " UPDATE promo_codes SET promo_code = ?, start_date = ?, end_date = ?, percentage_discount = ?, flat_discount = ?, "
					+ " updated_date = now(), updated_by = ? WHERE (id = ?) " ;
			
			insertQuery = "INSERT INTO promo_codes (promo_code, start_date, end_date, percentage_discount, flat_discount, type, type_id, created_by, updated_by, "
					+ " created_date, updated_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now()) ";
			
			updatePst = conn.prepareStatement(updateQuery);
			insertPst = conn.prepareStatement(insertQuery);
			
			for (int i = 0; i < promoRecords.size(); i++) {
				PromoRecords dto = promoRecords.get(i);
				if(dto.getId() > 0) {
					int index = 1;
					updatePst.setString(index++, dto.getPromoCode());
					updatePst.setDate(index++, CommonUtility.utilToSqlDate(dto.getStartDate()));
					updatePst.setDate(index++, CommonUtility.utilToSqlDate(dto.getEndDate()));
					updatePst.setInt(index++, dto.getPercentageDiscuont());
					updatePst.setInt(index++, dto.getFlatDiscount());
					updatePst.setString(index++, userId+"");
					updatePst.setInt(index++, dto.getId());
					updatePst.addBatch();			

				}else {
					int index = 1;
					insertPst.setString(index++, dto.getPromoCode());
					insertPst.setDate(index++, CommonUtility.utilToSqlDate(dto.getStartDate()));
					insertPst.setDate(index++, CommonUtility.utilToSqlDate(dto.getEndDate()));
					insertPst.setInt(index++, dto.getPercentageDiscuont());
					insertPst.setInt(index++, dto.getFlatDiscount());
					insertPst.setString(index++, dto.getType());
					insertPst.setString(index++, dto.getTypeId());
					insertPst.setString(index++, userId+"");
					insertPst.setString(index++, userId+"");
					
					insertPst.addBatch();	
				}
				
			}
			try {
				insertPst.executeBatch();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			try {
				updatePst.executeBatch();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			//conn.commit();
			//conn.setAutoCommit(true);

		} catch (Exception e) {
			//conn.rollback();
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, insertPst);
			DButility.closeStatement(updatePst);
		}

	}

	public List<PromoRecords> getPromoRecords(int clubId, int typeId, String type, boolean showAll) throws Exception {

		String query = "select  * from promo_codes where  "
				+ " type = ? and type_id = ? ";
		
		if(!showAll) {
			query += " and DATE_FORMAT(start_date,'%Y-%m-%d')  <=  DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d') and DATE_FORMAT(end_date,'%Y-%m-%d')  >=  DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')";
		}
		
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		PromoRecords promoRecord = null;
		List<PromoRecords>  promoRecords = new ArrayList<PromoRecords>();
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, type);
			pst.setInt(2, typeId);
			rs = pst.executeQuery();
			while (rs.next()) {
				promoRecord = populatePromoRecords(rs);
				promoRecords.add(promoRecord);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return promoRecords;

	
	}

	private PromoRecords populatePromoRecords(ResultSet rs) throws SQLException {
		PromoRecords pr = new PromoRecords();
		pr.setId(rs.getInt("id"));
		pr.setPromoCode(rs.getString("promo_code"));
		pr.setStartDate(rs.getDate("start_date"));
		pr.setStartDates(CommonUtility.dateToString(rs.getDate("start_date")));
		pr.setEndDate(rs.getDate("end_date"));
		pr.setEndDates(CommonUtility.dateToString(rs.getDate("end_date")));
		pr.setPercentageDiscuont(rs.getInt("percentage_discount"));
		pr.setFlatDiscount(rs.getInt("flat_discount"));
		pr.setType(rs.getString("type"));
		pr.setTypeId(rs.getString("type_id"));
		
		
		return pr;
	}

	public int deletePromoCode(int clubId, int typeId, int id, String type) throws Exception {
		int result = 0;

		String query = "delete from promo_codes where id = ? and type_id = ? and type = ?";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			int idx = 1;
			pst.setInt(idx++, id);
			pst.setInt(idx++, typeId);
			pst.setString(idx++, type);
			
			result = pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
		return result;
	}

	

}
