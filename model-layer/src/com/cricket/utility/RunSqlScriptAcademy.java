package com.cricket.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dao.ClubFactory;
import com.ibatis.common.jdbc.ScriptRunner;

public class RunSqlScriptAcademy {
	public static void main(String[] args) throws ClassNotFoundException,
	SQLException {


String aSQLScriptFilePath = "C:\\documentsRep\\academySql.txt";
List<Integer> academies = new ArrayList<Integer>();
try {
	academies = ClubFactory.getAcademyClubs();
} catch (Exception e1) {
	e1.printStackTrace();
}		


for (Integer academy: academies) {
	
	Connection con = DButility.getConnection(academy);

	try {
		ScriptRunner sr = new ScriptRunner(con, false, false);
		Reader reader = new BufferedReader(new FileReader(aSQLScriptFilePath));
		sr.runScript(reader);
		System.out.println("Exceuted for the Club - "+academy);
	} catch (Exception e) {
		System.err.println("Failed to Execute for club" + academy	+ " The error is " + e.getMessage());
	}finally{
		DButility.closeConnection(con);
	}
}
}
}
