package com.cricket.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.cricket.dao.ArticlesFactory;
import com.cricket.dao.ClubFactory;
import com.cricket.dto.ArticleDto;

public class UpdateOldArticleImagePaths {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		int maxClubID = 0;
		try {
			maxClubID = ClubFactory.getMaxClubID();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for (int i = 1; i <= maxClubID; i++) {
			
			Connection conn = DButility.getConnection(i);
			
			PreparedStatement pst = null;
			try {
				System.out.println("========= Articles Update running for the club id "+i+" =============== ");
				
				List<ArticleDto> articles = ArticlesFactory.getArticles(null, i);
				if (!CommonUtility.isListNullEmpty(articles)) {
					
					System.out.println("Articles are there and update running for the club id "+i);
					
					for (ArticleDto article : articles) {
						
						String query = "update articles set image='/documentsRep/article/" + i + "-"
								+ article.getArticleId() + "-article.jpg' where article_id = "+article.getArticleId();
						
						pst = conn.prepareStatement(query);
						pst.executeUpdate();
					}
				}else {
					
					System.out.println("No Articles found for the club id "+i);
				}
			} catch (Exception e) {
				System.err.println("Failed to Execute for club" + i + " The error is " + e.getMessage());
			} finally {
				DButility.closeConnection(conn);
			}
		}
	}
}