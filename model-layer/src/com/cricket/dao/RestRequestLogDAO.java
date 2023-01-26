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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.RestRequestLogDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RestRequestLogDAO {

	protected RestRequestLogDAO() {

	}

	protected void insertRequestLog(RestRequestLogDto request) throws Exception {

		String query =
				"insert into rest_request_log(date,ip,request_url,parameters,club_id,other_info) values (?,?,?,?,?,?)";

		Connection conn = DButility.getDefaultConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			int i = 1;
			st.setTimestamp(i++, new Timestamp(new java.util.Date().getTime()));
			st.setString(i++, request.getIp());
			st.setString(i++, request.getRequestURL());
			st.setString(i++, request.getParameters());
			st.setInt(i++, request.getClubId());
			st.setString(i++, request.getOtherInfo());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, st);
		}

	}

	public List<RestRequestLogDto> getRequestData(String startdate, String endDate, String clubId, String ip) throws Exception {
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<RestRequestLogDto> data = new ArrayList<RestRequestLogDto>();
		String query = "SELECT club_id,request_url,ip,parameters,other_info,date  FROM rest_request_log where 1=1 ";

		if (!CommonUtility.isNullOrEmpty(startdate)) {
			query += " AND date >= ? ";
		}

		if (!CommonUtility.isNullOrEmpty(endDate)) {
			query += " AND date <= ? ";
		}

		if (!CommonUtility.isNullOrEmpty(clubId)) {
			query += " AND club_id = ? ";
		}

		if (!CommonUtility.isNullOrEmpty(ip)) {
			query += " AND ip = ? ";
		}
		query += " order by date desc LIMIT 2000 ";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			int i = 1;
			if (!CommonUtility.isNullOrEmpty(startdate)) {
				st.setString(i++, startdate);
			}

			if (!CommonUtility.isNullOrEmpty(endDate)) {
				st.setString(i++, endDate);
			}

			if (!CommonUtility.isNullOrEmpty(clubId)) {
				st.setString(i++, clubId);
			}

			if (!CommonUtility.isNullOrEmpty(ip)) {
				st.setString(i++, ip);
			}
			
			rs = st.executeQuery();
			while (rs.next()) {
				RestRequestLogDto row = new RestRequestLogDto();
				row.setClubId(rs.getInt("club_id"));
				row.setRequestURL(rs.getString("request_url"));
				row.setIp(rs.getString("ip"));
				row.setParameters(rs.getString("parameters"));
				row.setOtherInfo(rs.getString("other_info"));
				row.setDate(rs.getTimestamp("date"));
				data.add(row);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return data;
	}

}
