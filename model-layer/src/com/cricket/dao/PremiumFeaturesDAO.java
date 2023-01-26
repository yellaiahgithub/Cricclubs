/*
 * Created on May 08, 2016
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

import com.cricket.dto.PremiumFeatureDto;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

/**
 * @author vk
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PremiumFeaturesDAO {
	

	private static String SELECT_PREMIUM_FEATURES_QUERY = "select feature_id, feature_name, feature_display_title, feature_desc, feature_category, standard_feature, standard_feature, effective_st_dt, status, creation_date, update_date from mcc.premium_features";
		
	/**
	 * @param dto
	 * @return
	 */
	protected PremiumFeatureDto getPremiumFeature(int featureId)
			throws Exception {
		
		List<PremiumFeatureDto> premiumFeatures = new ArrayList<PremiumFeatureDto>();
		
		premiumFeatures = getPremiumFeatures(featureId, (String) null, true, true);
		
		if (premiumFeatures.size() == 0) {
			throw new Exception("Premium Feature not found");
		} else if (premiumFeatures.size() > 0)
			return (PremiumFeatureDto)premiumFeatures.get(0);
		else
			throw new Exception("More than one Premium Feature with Same Id available");
	}

	/**
	 * @param dto
	 * @return
	 */
	protected List<PremiumFeatureDto> getPremiumFeatures(int featureId, String featureCategory, boolean standardFeaturesOnly, boolean activeOnly)
			throws Exception {
		List<PremiumFeatureDto> premiumFeatures = new ArrayList<PremiumFeatureDto>();
		
		String query = SELECT_PREMIUM_FEATURES_QUERY;
		
		boolean whereAdded = false;
		
		if (featureId > 0) {
			query += " where feature_id = ?";
			whereAdded = true;
		} 		
		
		if (!CommonUtility.isNullOrEmpty(featureCategory)) {
			
			if (whereAdded)
				query += " and ";
			else
				query += " where ";
			
			query += "feature_category = ?";
			whereAdded = true;
		} 		
	
		if (standardFeaturesOnly) {

			if (whereAdded)
				query += " and ";
			else
				query += " where ";
			
			query += "standard_feature = ?";
			whereAdded = true;
		} 
		
		if (activeOnly) {

			if (whereAdded)
				query += " and ";
			else
				query += " where ";
			
			query += "status = ?";
			whereAdded = true;
		}
		
		query += " order by feature_id desc";

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = DButility.getDefaultReadConnection();
			
			
			st = conn.prepareStatement(query);
			int index = 1;
			
			
			if (featureId > 0) {
				
				st.setInt(index++, featureId);
			} 		
			//int featureId, String featureCategory, boolean standardFeaturesOnly, boolean activeOnly
			if (!CommonUtility.isNullOrEmpty(featureCategory)) {
				
				st.setString(index++, featureCategory);
			} 		
	
			if (standardFeaturesOnly) {
				st.setString(index++, ""+standardFeaturesOnly);
			} 
			
			if (activeOnly) {
				st.setString(index++, "ACTIVE");
			}
			
			rs = st.executeQuery();
			preparePremiumFeaturesList(premiumFeatures, rs);
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			DButility.dbCloseAll(conn, st, rs);
		}
		return premiumFeatures;
	}

	private void preparePremiumFeaturesList(List<PremiumFeatureDto> premiumFeatures, ResultSet rs)
			throws SQLException {
		
		while (rs.next()) {
			
			PremiumFeatureDto premiumFeatureDto = new PremiumFeatureDto();
			
			premiumFeatureDto.setFeatureId(rs.getInt("feature_id"));
			premiumFeatureDto.setFeatureName(rs.getString("feature_name"));
			premiumFeatureDto.setFeatureDisplayTitle(rs.getString("feature_display_title"));
			premiumFeatureDto.setFeatureDesc(rs.getString("feature_desc"));
			premiumFeatureDto.setFeatureCategory(rs.getString("feature_category"));
			premiumFeatureDto.setStandardFeature(new Boolean(rs.getString("standard_feature")).booleanValue());;
			premiumFeatureDto.setEffectiveStartDate(rs.getTimestamp("effective_st_dt"));
			premiumFeatureDto.setStatus(rs.getString("status"));
			
			premiumFeatureDto.setCreationDate(rs.getTimestamp("creation_date"));
			premiumFeatureDto.setUpdateDate(rs.getTimestamp("update_date"));
			
			premiumFeatures.add(premiumFeatureDto);
		}
	}
}
