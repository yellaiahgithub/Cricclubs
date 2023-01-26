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

import com.cricket.dto.LiveScoreFeedDto;
import com.cricket.utility.DButility;

public class LiveScoreFeedDAO {
	static Logger log = LoggerFactory.getLogger(LiveScoreFeedDAO.class);
	protected LiveScoreFeedDAO(){
		
	}

	protected LiveScoreFeedDto getLiveScoreFeed(String deviceId) throws Exception {
		LiveScoreFeedDto feedDto = null;
		
		String selectQuery = "SELECT * "  
				+ "FROM mcc.live_score_feed WHERE device_id = ? order by 1 desc limit 1;";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(selectQuery);
			pst.setString(1, deviceId);
			rs = pst.executeQuery();
			while(rs.next()) {
				feedDto = new LiveScoreFeedDto();
				
				feedDto.setId(rs.getInt("id"));
				feedDto.setDeviceId(rs.getString("device_id"));
				feedDto.setCreatedDate(rs.getString("create_date"));
				feedDto.setEmailId(rs.getString("email"));
				feedDto.setClubId(rs.getInt("club_id"));
				feedDto.setMatchId(rs.getInt("match_id"));
				feedDto.setStreamKey(rs.getString("feed_key"));
				feedDto.setServer(rs.getString("feed_url"));
				feedDto.setRecording(rs.getInt("recording"));
				feedDto.setStreaming(rs.getInt("streaming"));
				feedDto.setUpdatedDate(rs.getString("update_date"));
				feedDto.setUpdatedBy(rs.getInt("update_by"));
				feedDto.setFixtureId(rs.getInt("fixture_id"));
				feedDto.setBroadCastId(rs.getString("broad_cast_id"));
				feedDto.setYoutubePublicUrl(rs.getString("youtube_public_url"));
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return feedDto;
	}

	public LiveScoreFeedDto getLiveScoreFeedForFxiture(String deviceId, int fixtureId, int clubId) {

		LiveScoreFeedDto feedDto = null;
		
		String selectQuery = "SELECT id, device_id, create_date, email, club_id, match_id, feed_key, feed_url, "
				+ "recording, streaming, update_date, update_by, broad_cast_id "  
				+ "FROM mcc.live_score_feed WHERE device_id = ? and fixture_id = ? and club_id = ? order by 1 desc limit 1;";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(selectQuery);
			pst.setString(1, deviceId);
			pst.setInt(2, fixtureId);
			pst.setInt(3, clubId);
			rs = pst.executeQuery();
			while(rs.next()) {
				feedDto = new LiveScoreFeedDto();
				
				feedDto.setId(rs.getInt("id"));
				feedDto.setDeviceId(rs.getString("device_id"));
				feedDto.setCreatedDate(rs.getString("create_date"));
				feedDto.setEmailId(rs.getString("email"));
				feedDto.setClubId(rs.getInt("club_id"));
				feedDto.setMatchId(rs.getInt("match_id"));
				feedDto.setStreamKey(rs.getString("feed_key"));
				feedDto.setServer(rs.getString("feed_url"));
				feedDto.setRecording(rs.getInt("recording"));
				feedDto.setStreaming(rs.getInt("streaming"));
				feedDto.setUpdatedDate(rs.getString("update_date"));
				feedDto.setUpdatedBy(rs.getInt("update_by"));
				feedDto.setBroadCastId(rs.getString("broad_cast_id"));
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return feedDto;
	}
	
	public LiveScoreFeedDto getLiveScoreFeedByStreamKey(String streamKey, int fixtureId, int clubId) {

		LiveScoreFeedDto feedDto = null;
		
		String selectQuery = "SELECT id, device_id, create_date, email, club_id, "
				+ "match_id, fixture_id, feed_key, feed_url, "
				+ "recording, streaming, broad_cast_id, update_date, update_by "  
				+ "FROM mcc.live_score_feed WHERE feed_key = ? order by 1 desc limit 1;";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;		
		try {
			pst = conn.prepareStatement(selectQuery);
			pst.setString(1, streamKey);
			rs = pst.executeQuery();
			while(rs.next()) {
				feedDto = new LiveScoreFeedDto();
				
				feedDto.setId(rs.getInt("id"));
				feedDto.setDeviceId(rs.getString("device_id"));
				feedDto.setCreatedDate(rs.getString("create_date"));
				feedDto.setEmailId(rs.getString("email"));
				feedDto.setClubId(rs.getInt("club_id"));
				feedDto.setMatchId(rs.getInt("match_id"));
				feedDto.setFixtureId(rs.getInt("fixture_id"));
				feedDto.setStreamKey(rs.getString("feed_key"));
				feedDto.setServer(rs.getString("feed_url"));
				feedDto.setRecording(rs.getInt("recording"));
				feedDto.setStreaming(rs.getInt("streaming"));
				feedDto.setBroadCastId(rs.getString("broad_cast_id"));
				feedDto.setUpdatedDate(rs.getString("update_date"));
				feedDto.setUpdatedBy(rs.getInt("update_by"));
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return feedDto;
	}
	
	public LiveScoreFeedDto getLiveScoreFeedForFxiture(int fixtureId, int clubId) {

		LiveScoreFeedDto feedDto = null;
		
		String selectQuery = "SELECT id, device_id, create_date, email, club_id, fixture_id,  match_id, feed_key, feed_url, youtube_public_url, "
				+ "recording, streaming, update_date, update_by, streaming_by, status  "  
				+ "FROM mcc.live_score_feed WHERE fixture_id = ? and club_id = ? order by 1 desc limit 1;";
		
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = conn.prepareStatement(selectQuery);
			pst.setInt(1, fixtureId);
			pst.setInt(2, clubId);
			rs = pst.executeQuery();
			while(rs.next()) {
				feedDto = new LiveScoreFeedDto();
				
				feedDto.setId(rs.getInt("id"));
				feedDto.setDeviceId(rs.getString("device_id"));
				feedDto.setCreatedDate(rs.getString("create_date"));
				feedDto.setEmailId(rs.getString("email"));
				feedDto.setClubId(rs.getInt("club_id"));
				feedDto.setMatchId(rs.getInt("match_id"));
				feedDto.setFixtureId(rs.getInt("fixture_id"));
				feedDto.setStreamKey(rs.getString("feed_key"));
				feedDto.setServer(rs.getString("feed_url"));
				feedDto.setYoutubePublicUrl(rs.getString("youtube_public_url"));
				feedDto.setRecording(rs.getInt("recording"));
				feedDto.setStreaming(rs.getInt("streaming"));
				feedDto.setUpdatedDate(rs.getString("update_date"));
				feedDto.setUpdatedBy(rs.getInt("update_by"));
				feedDto.setStreamingBy(rs.getString("streaming_by"));
				feedDto.setStatus(rs.getInt("status"));
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return feedDto;
	
	}
	
	public List<LiveScoreFeedDto> getInProgressLiveScoreFeeds(int clubId) {

		String selectQuery = "SELECT id, device_id, create_date, email, club_id, fixture_id,  match_id, feed_key, feed_url, youtube_public_url, "
				+ "recording, streaming, update_date, update_by, streaming_by, status, broad_cast_id  "  
				+ "FROM mcc.live_score_feed WHERE club_id = ? and status = 1 order by 1 desc";
		
		Connection conn = DButility.getDefaultReadConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<LiveScoreFeedDto> liveScoreDtos = new ArrayList<LiveScoreFeedDto>();		
		try {
			pst = conn.prepareStatement(selectQuery);
			pst.setInt(1, clubId);
			rs = pst.executeQuery();
			while(rs.next()) {
				
				LiveScoreFeedDto feedDto = new LiveScoreFeedDto();
				
				feedDto.setId(rs.getInt("id"));
				feedDto.setDeviceId(rs.getString("device_id"));
				feedDto.setCreatedDate(rs.getString("create_date"));
				feedDto.setEmailId(rs.getString("email"));
				feedDto.setClubId(rs.getInt("club_id"));
				feedDto.setMatchId(rs.getInt("match_id"));
				feedDto.setFixtureId(rs.getInt("fixture_id"));
				feedDto.setStreamKey(rs.getString("feed_key"));
				feedDto.setServer(rs.getString("feed_url"));
				feedDto.setYoutubePublicUrl(rs.getString("youtube_public_url"));
				feedDto.setRecording(rs.getInt("recording"));
				feedDto.setStreaming(rs.getInt("streaming"));
				feedDto.setUpdatedDate(rs.getString("update_date"));
				feedDto.setUpdatedBy(rs.getInt("update_by"));
				feedDto.setStreamingBy(rs.getString("streaming_by"));
				feedDto.setStatus(rs.getInt("status"));
				feedDto.setBroadCastId(rs.getString("broad_cast_id"));
				
				liveScoreDtos.add(feedDto);				
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			DButility.dbCloseAll(conn, pst, rs);
		}
		return liveScoreDtos;	
	}
	
	public int createLiveScoreFeedForFixture(LiveScoreFeedDto liveScoreFeedDto) throws Exception {

		String query ="INSERT INTO live_score_feed(device_id,club_id,fixture_id,feed_key, broad_cast_id, youtube_public_url, streaming_by, status)VALUES(?,?,?,?,?,?,?,?)";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int id = 0;
		try {
			pst = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			int index = 1;
			pst.setString(index++, liveScoreFeedDto.getDeviceId());
			//pst.setString(index++, liveScoreFeedDto.getEmailId());
			pst.setInt(index++, liveScoreFeedDto.getClubId());
			pst.setInt(index++, liveScoreFeedDto.getFixtureId());
			pst.setString(index++, liveScoreFeedDto.getStreamKey());
			pst.setString(index++, liveScoreFeedDto.getBroadCastId());
			pst.setString(index++, "https://www.youtube.com/watch?v=" +liveScoreFeedDto.getBroadCastId());
			pst.setString(index++, liveScoreFeedDto.getStreamingBy());
			pst.setInt(index++, liveScoreFeedDto.getStatus());
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		return id;
	}
	
	public void updateLiveScoreFeedForFixture(LiveScoreFeedDto liveScoreFeedDto) throws Exception {

		String query ="UPDATE live_score_feed set feed_key = ?, broad_cast_id = ?, youtube_public_url = ?, "
				+ "streaming_by = ? where club_id = ? and fixture_id = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			
			pst.setString(index++, liveScoreFeedDto.getStreamKey());
			pst.setString(index++, liveScoreFeedDto.getBroadCastId());
			pst.setString(index++, "https://www.youtube.com/watch?v=" +liveScoreFeedDto.getBroadCastId());
			pst.setString(index++, liveScoreFeedDto.getStreamingBy());
			pst.setInt(index++, liveScoreFeedDto.getClubId());
			pst.setInt(index++, liveScoreFeedDto.getFixtureId());
			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
	}
	
	public void updateMatchIdLiveScoreFeedForFixture(LiveScoreFeedDto liveScoreFeedDto) throws Exception {

		String query ="update live_score_feed set match_id = ? where id = ?";
		Connection conn = DButility.getDefaultConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			int index = 1;
			pst.setInt(index++, liveScoreFeedDto.getMatchId());
			pst.setInt(index++, liveScoreFeedDto.getId());
			pst.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		}finally{
			DButility.dbCloseAll(conn, pst, rs);
		}
		
	}
}
