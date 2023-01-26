package com.cricket.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import com.cricket.dao.ClubFactory;
import com.ibatis.common.jdbc.ScriptRunner;

public class RunSqlScriptForWWW {
	
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		
		int maxClubID = 0;
		String aSQLScriptFilePath = "C:\\documentsRep\\sql.txt";
		try {
			maxClubID = ClubFactory.getMaxClubID();
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
		for (int i = 1; i <= maxClubID; i++) {
			Connection con = DButility.getConnection(i);

			try {
				ScriptRunner sr = new ScriptRunner(con, false, false);
				Reader reader = new BufferedReader(new FileReader(aSQLScriptFilePath));
				sr.runScript(reader);
				System.out.println("Exceuted for the Club - "+i);
			} catch (Exception e) {
				System.err.println("Failed to Execute for club" + i	+ " The error is " + e.getMessage());
			}finally{
				DButility.closeConnection(con);
			}
		}
	}
}