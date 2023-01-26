/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cricket.dto.MatchOfficialsDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MatchOfficialsDAO {

	protected MatchOfficialsDAO() {

	}

	protected List<MatchOfficialsDto> getMatchOfficials(int userId, int clubId, String officialType, String isAlloted,
			String fromDate, String toDate) throws Exception {

		List<MatchOfficialsDto> mofficials = new ArrayList<MatchOfficialsDto>();

		String query = "SELECT id, uv.user_id, official_type, DATE_FORMAT(date_available,'%m/%d/%Y') as date_available, "
				+ " time_available, if(alloted_time is null,'',alloted_time) as atime , CONCAT(uv.f_name,' ',uv.l_name) AS fullname "
				+ "FROM match_officials mo, mcc.umpire_view uv WHERE mo.user_id = uv.user_id and club_id =" + clubId;

		if (userId != 0) {
			query += "\nand uv.user_id = ? ";
		}
		if (isAlloted.equalsIgnoreCase("N")) {
			query += "\nand ( alloted_time is null or alloted_time != time_available) ";
		}
		if (fromDate != null && !"".equals(fromDate.trim())) {
			query += "\n and date_available >= STR_TO_DATE(?,  '%m/%d/%Y') ";
		}

		if (toDate != null && !"".equals(toDate.trim())) {
			query += "\n and date_available <= STR_TO_DATE(?,  '%m/%d/%Y') ";
		}

		if (officialType.equalsIgnoreCase("u") || officialType.equalsIgnoreCase("s")) {
			query += "\n and official_type = ? ";
		}

		if (fromDate != null && !"".equals(fromDate.trim())) {
			query += "\n order by date_available asc ";
		} else if (toDate != null && !"".equals(toDate.trim())) {
			query += "\n order by date_available desc ";
		} else {
			query += "\n order by date_available asc ";
		}

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int i = 1;
			if (userId != 0) {
				pst.setInt(i++, userId);
			}
			if (fromDate != null && !"".equals(fromDate.trim())) {
				pst.setString(i++, fromDate);
			}
			if (toDate != null && !"".equals(toDate.trim())) {
				pst.setString(i++, toDate);
			}
			if (officialType.equalsIgnoreCase("u") || officialType.equalsIgnoreCase("s")) {
				pst.setString(i++, officialType);
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				MatchOfficialsDto mofficial = new MatchOfficialsDto();
				mofficial.setId(rs.getInt("id"));
				mofficial.setUserId(rs.getInt("user_id"));
				mofficial.setOfficialType(rs.getString("official_type"));
				mofficial.setDateAvailable(rs.getString("date_available"));
				mofficial.setTimeAvailable(rs.getString("time_available"));
				mofficial.setAllotedTime(rs.getString("atime"));
				mofficial.setFullName(rs.getString("fullname"));
				mofficials.add(mofficial);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return mofficials;
	}

	protected Map<String, Integer> getNextWeekMatchOfficials(int clubId, String OffialType, String fromDate,
			String toDate) throws Exception {

		Map<String, Integer> mofficials = new LinkedHashMap<String, Integer>();

		String query = "select u.user_id,concat(u.f_name,' ',l_name) name from mcc.umpire_view u "
				+ ", match_officials m where u.user_id = m.user_id";

		if (!CommonUtility.isNullOrEmpty(OffialType)) {
			query += "\nand official_type = ? ";
		}
		if (fromDate != null && !"".equals(fromDate.trim())) {
			query += "\n and date_available >= STR_TO_DATE(?,  '%m/%d/%Y') ";
		}
		if (toDate != null && !"".equals(toDate.trim())) {
			query += "\n and date_available <= STR_TO_DATE(?,  '%m/%d/%Y') ";
		}
		if (fromDate != null && !"".equals(fromDate.trim())) {
			query += "\n order by date_available asc ";
		} else if (toDate != null && !"".equals(toDate.trim())) {
			query += "\n order by date_available desc ";
		} else {
			query += "\n order by date_available asc ";
		}

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int i = 1;
			if (!CommonUtility.isNullOrEmpty(OffialType)) {
				pst.setString(i++, OffialType);
			}
			if (fromDate != null && !"".equals(fromDate.trim())) {
				pst.setString(i++, fromDate);
			}
			if (toDate != null && !"".equals(toDate.trim())) {
				pst.setString(i++, toDate);
			}
			rs = pst.executeQuery();

			while (rs.next()) {
				mofficials.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return mofficials;
	}

	protected void insertMatchOfficials(MatchOfficialsDto mofficial, int clubId) throws Exception {

		String query = "insert into match_officials(user_id, official_type, "
				+ "date_available, time_available) values (?,?,STR_TO_DATE(?,'%m/%d/%Y'),?)";

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		int i = 1;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(i++, mofficial.getUserId());
			pst.setString(i++, mofficial.getOfficialType());
			pst.setString(i++, mofficial.getDateAvailable());
			pst.setString(i++, mofficial.getTimeAvailable());

			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	public void updateMatchOfficials(int mid, String availableTime, String allotTime, int clubId) throws Exception {

		String query = "update match_officials set ";

		if (!CommonUtility.isNullOrEmpty(availableTime)) {
			if (CommonUtility.isNullOrEmpty(allotTime)) {
				query += "time_available = ? ";
			} else {
				query += "time_available = ?, ";
			}
		}
		if (!CommonUtility.isNullOrEmpty(allotTime)) {
			query += "alloted_time = ?";
		}
		if (mid > 0) {
			query += " where id = ? ";
		}
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		int i = 1;
		try {
			pst = conn.prepareStatement(query);

			if (!CommonUtility.isNullOrEmpty(availableTime)) {
				pst.setString(i++, availableTime);
			}
			if (!CommonUtility.isNullOrEmpty(allotTime)) {
				pst.setString(i++, allotTime);
			}
			if (mid > 0) {
				pst.setInt(i++, mid);
			}

			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected void deleteMatchOfficials(int userId, int clubId) throws Exception {

		String query = "delete from match_officials where user_id = " + userId;
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}
	}

	protected List<MatchOfficialsDto> availableMatchOfficialsBasedonDate(int clubId, String fromDate, String toDate)

			throws Exception {

		List<MatchOfficialsDto> dtoList = new ArrayList<MatchOfficialsDto>();

		String query = "select u.user_id,date_available,official_type from mcc.umpire_view u "
				+ ", match_officials m where u.user_id = m.user_id";

		if (fromDate != null && !"".equals(fromDate.trim())) {
			query += "\n and date_available >= STR_TO_DATE(?,  '%m/%d/%Y') ";
		}
		if (toDate != null && !"".equals(toDate.trim())) {
			query += "\n and date_available <= STR_TO_DATE(?,  '%m/%d/%Y') ";
		}
		if (fromDate != null && !"".equals(fromDate.trim())) {
			query += "\n order by date_available asc ";
		} else if (toDate != null && !"".equals(toDate.trim())) {
			query += "\n order by date_available desc ";
		} else {
			query += "\n order by date_available asc ";
		}

		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		// Date date = new Date();

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			pst = conn.prepareStatement(query);
			int i = 1;

			if (fromDate != null && !"".equals(fromDate.trim())) {
				pst.setString(i++, fromDate);
			}
			if (toDate != null && !"".equals(toDate.trim())) {
				pst.setString(i++, toDate);
			}
			rs = pst.executeQuery();

			while (rs.next()) {
				
				MatchOfficialsDto dto = new MatchOfficialsDto();

				String dateConversion = rs.getString("date_available");

				if (!CommonUtility.isNullOrEmpty(dateConversion)) {

					Date date = new SimpleDateFormat("yyy-MM-dd").parse(dateConversion);
					dateConversion = formatter.format(date);
					dto.setOfficialType(rs.getString("official_type"));
					dto.setDateAvailable(dateConversion);
					dto.setUserId(rs.getInt("user_id"));
				}

				dtoList.add(dto);
			}

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return dtoList;
	}

	public int umpireAvailbilityOnDate(int userId, String date, int clubId) {
		
		String query = "SELECT COUNT(*) FROM match_officials WHERE official_type='u' AND user_id=? AND date_available = ?";

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		ResultSet rs = null;
		int count = 0;

		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, userId);
			pst.setString(2, date);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			return 0;
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return count;
	}
}
