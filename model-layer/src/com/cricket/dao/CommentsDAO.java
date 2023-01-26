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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cricket.dto.ArticleGlobalDto;
import com.cricket.dto.CommentDto;
import com.cricket.utility.ApplicationConstants;
import com.cricket.utility.CommonUtility;
import com.cricket.utility.DButility;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CommentsDAO {
	
	private static Logger log = LoggerFactory.getLogger(CommentsDAO.class);
	
	protected List<CommentDto> getComments(String commentType,long CommentTypeId,int limit,int clubId) throws Exception { 
		List<CommentDto> comments = new ArrayList<CommentDto>();
		if(CommonUtility.isNullOrEmptyOrNULL(commentType)){
			commentType  = ApplicationConstants.COMMENT_TYPE_HOME;
		}
		String query =
			"select  c.comment_id," +
			"u.user_id, " +
			"CONCAT(u.f_name, ' ', u.l_name) user_name," +
			"comment," +
			"comment_type," +
			"comment_type_id," +
			"date " +
			"from comments c, mcc.user u " +
			"where u.user_id = c.user_id  AND comment_type = ? ";

			if(CommentTypeId > 0){
				query += " AND comment_type_id = ? ";
			}
			
			query += " order by 1 desc";

			if(limit > 0){
				query += " LIMIT ?" ;
			}else{
				query += " LIMIT 100";
			}
		Connection conn = DButility.getReadConnection(clubId);
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(query);
			int i= 1;
			//st.setInt(i++, clubId);
			st.setString(i++, commentType);
			
			if(CommentTypeId > 0){
				st.setLong(i++, CommentTypeId);
			}
			if(limit > 0){
				st.setInt(i++, limit);
			
			}
		    rs = st.executeQuery();
		while (rs.next()) {
			CommentDto comment = new CommentDto();
			comment.setUserId(rs.getInt("user_id"));
			comment.setUserName(rs.getString("user_name"));
			comment.setCommentId(rs.getInt("comment_id"));
			comment.setComment(rs.getString("comment"));
			comment.setCommentType(rs.getString("comment_type"));
			comment.setCommentTypeId(rs.getLong("comment_type_id"));
			comment.setDate(rs.getTimestamp("date"));
			comments.add(comment);
		}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.dbCloseAll(conn, st, rs);
		}
		return comments;

	}

	protected void insertComment(CommentDto comment,int clubId) throws Exception {

		String commentStr = comment.getComment();
		int userId = comment.getUserId();
		

		String query =
			"insert into comments(user_id,comment,comment_type,comment_type_id,date ) values (?,?,?,?,NOW())";
		Connection conn = DButility.getConnection(clubId);
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, userId);
			st.setString(2, DButility.escapeLine(commentStr));
			st.setString(3, comment.getCommentType());
			st.setLong(4, comment.getCommentTypeId());
			
			st.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}finally{
			DButility.closeConnectionAndStatement(conn, st);
		}

	}

	protected void insertCommentGlobal(String comment,int articleId,ArticleGlobalDto articledto) throws Exception {

		String query;

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		PreparedStatement st1=null;
		String existingString="";
		try {
		
			conn = DButility.getDefaultConnection();
			query= "select comment from media.articles_global where article_id =" +articleId;
					
	    st = conn.prepareStatement(query,
				PreparedStatement.RETURN_GENERATED_KEYS);
	      rs=st.executeQuery();
	      
	      while(rs.next()){
	    	   existingString= rs.getString("comment");
	    	   break;
	     }
	      query = "UPDATE  media.articles_global SET comment ='"+existingString+"\n"+comment+"'"  +" where article_id = " +articleId;
			
	       st1 = conn.prepareStatement(query,
					PreparedStatement.RETURN_GENERATED_KEYS);
	       st1.executeUpdate(query);
		
	     
	}
	 catch (SQLException e) {
		throw new Exception(e.getMessage());
	}finally{
		DButility.closeStatement(st1);
		DButility.dbCloseAll(conn, st,rs);
	}
	}
	public void deleteComment(int commentId, int clubId) throws Exception {
		String query =
				"delete from comments where comment_id = ?" ;
			Connection conn = DButility.getConnection(clubId);
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement(query);
				st.setInt(1, commentId);
				st.executeUpdate();

			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			}finally{
				DButility.closeConnectionAndStatement(conn, st);
			}
		
	}
}
