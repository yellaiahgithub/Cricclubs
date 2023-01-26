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
import java.util.ArrayList;
import java.util.List;

import com.cricket.dto.ClaimProfileDto;
import com.cricket.utility.DButility;

/**
 * @author ganesh
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ClaimProfileDAO {

	protected ClaimProfileDAO() {

	}

	protected List<ClaimProfileDto> getProfileClaims(int clubId, int playerId, int status) throws Exception {
		List<ClaimProfileDto> claims = new ArrayList<ClaimProfileDto>();
		String query =
				"SELECT c.player_id," +
						"c.email," +
						"c.created_date, " +
						"c.approved_date, " +
						"c.approved_by, CONCAT(u.f_name,' ', u.l_name) approved_by_name," +
						"c.status," +
						"CONCAT(p.f_name,' ', p.l_name) player_name, " +
						"p.profilepic_file_path " +
						"FROM " + DButility.getDBName(clubId) + ".claim_profile c join mcc.player p on(p.player_id = c.player_id) left outer join mcc.user u on(u.user_id = c.approved_by)" +
						"where 1=1";

		if (playerId != 0) {
			query += "\nand c.player_id = ? ";
		}

		if (status != 0) {
			query += "\nand c.status = ? ";
		}else{
			query += "\nand c.status != 1";
		}

		Connection conn = DButility.getReadConnection(clubId, false);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index =1;
			if (playerId != 0) {
				pst.setInt(index++, playerId);
				
			} 
			if (status != 0) {
				pst.setInt(index++, status);
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				ClaimProfileDto claim = new ClaimProfileDto();
				claim.setPlayerId(rs.getInt("player_id"));
				claim.setPlayerName(rs.getString("player_name"));
				claim.setProfilepic_file_path(rs.getString("profilepic_file_path"));
				claim.setEmail(rs.getString("email"));
				claim.setCreatedDate(rs.getDate("created_date"));
				claim.setApprovedDate(rs.getDate("approved_date"));
				claim.setApprovedBy(rs.getInt("approved_by"));
				claim.setApprovedByName(rs.getString("approved_by_name"));
				claim.setStatus(rs.getInt("status"));
				claims.add(claim);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return claims;

	}

	protected void insertClaim(ClaimProfileDto claim, int clubId) throws Exception {

		String query = "insert into claim_profile(player_id,email,created_date,status) values ( ?,?,NOW(),?)";
				

		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, claim.getPlayerId());
			pst.setString(2, claim.getEmail());
			pst.setInt(3, claim.getStatus());
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}

	public void updateClaim(ClaimProfileDto claim, int clubId) throws Exception {

		String query ="update claim_profile set approved_date= NOW(), approved_by = ? ,status = ? where player_id = ? and status = 1";
		
				
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1,claim.getApprovedBy());
			pst.setInt(2,claim.getStatus());
			pst.setInt(3,claim.getPlayerId());
			
			
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.closeConnectionAndStatement(conn, pst);
		}

	}
}
