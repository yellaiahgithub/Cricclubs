package com.cricket.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cricket.dao.PlayerFactory;

public class GetPlayerStatsFromFile {
	
	public static void main(String[] args) throws Exception {

		System.out.println("Program Start...");
		
		File outFile = new File("C:\\output\\bpl_player_stats_report.csv");
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile()));
		bw.write("Player ID, Player Name, Matches, Hundreds, Fifties, Twenty_Five, Batting SR, Wickets, Bowling Econ, Bowling SR, man_of_matches");
		bw.write("\n");
		
		ArrayList<Integer> playerIds = new ArrayList<Integer>();
		
		playerIds.add(48810);
		playerIds.add(110544);
		playerIds.add(133667);
		playerIds.add(144326);
		playerIds.add(235660);
		playerIds.add(271052);
		playerIds.add(274626);
		playerIds.add(274648);
		playerIds.add(274664);
		playerIds.add(274699);
		playerIds.add(274729);
		playerIds.add(274737);
		playerIds.add(274739);
		playerIds.add(274801);
		playerIds.add(274818);
		playerIds.add(274927);
		playerIds.add(274985);
		playerIds.add(275069);
		playerIds.add(275164);
		playerIds.add(292977);
		playerIds.add(319726);
		playerIds.add(320254);
		playerIds.add(322292);
		playerIds.add(322312);
		playerIds.add(341521);
		playerIds.add(353918);
		playerIds.add(363228);
		playerIds.add(365350);
		playerIds.add(366006);
		playerIds.add(402980);
		playerIds.add(402993);
		playerIds.add(413618);
		playerIds.add(447387);
		playerIds.add(491137);
		playerIds.add(491942);
		playerIds.add(493601);
		playerIds.add(497559);
		playerIds.add(501300);
		playerIds.add(504078);
		playerIds.add(508313);
		playerIds.add(511434);
		playerIds.add(534065);
		playerIds.add(534914);
		playerIds.add(548607);
		playerIds.add(561364);
		playerIds.add(561520);
		playerIds.add(568126);
		playerIds.add(578205);
		playerIds.add(584385);
		playerIds.add(584583);
		playerIds.add(585381);
		playerIds.add(585550);
		playerIds.add(585598);
		playerIds.add(589211);
		playerIds.add(590212);
		playerIds.add(606052);
		playerIds.add(606291);
		playerIds.add(635401);
		playerIds.add(638071);
		playerIds.add(648305);
		playerIds.add(651198);
		playerIds.add(672596);
		playerIds.add(732816);
		playerIds.add(732825);
		playerIds.add(732955);
		playerIds.add(733228);
		playerIds.add(734761);
		playerIds.add(735637);
		playerIds.add(736660);
		playerIds.add(737237);
		playerIds.add(737362);
		playerIds.add(737647);
		playerIds.add(745897);
		playerIds.add(775981);
		playerIds.add(777086);
		playerIds.add(795688);
		playerIds.add(801166);
		playerIds.add(810720);
		playerIds.add(826227);
		playerIds.add(832616);
		playerIds.add(832990);
		playerIds.add(837682);
		playerIds.add(839719);
		playerIds.add(851093);
		playerIds.add(851591);
		playerIds.add(852216);
		playerIds.add(857412);
		playerIds.add(858907);
		playerIds.add(876773);
		playerIds.add(897141);
		playerIds.add(921276);
		playerIds.add(1006651);
		playerIds.add(1007364);
		playerIds.add(1159209);
		playerIds.add(1195819);
		playerIds.add(1247441);
		playerIds.add(1249946);
		playerIds.add(1272913);
		playerIds.add(1272917);
		playerIds.add(1272918);
		playerIds.add(1274116);
		playerIds.add(1274782);
		playerIds.add(1278721);
		playerIds.add(1329149);
		playerIds.add(1364542);
		playerIds.add(1437753);
		playerIds.add(1444312);
		playerIds.add(1513604);
		playerIds.add(1629648);
		playerIds.add(1666689);
		playerIds.add(1667285);
		playerIds.add(1667375);
		playerIds.add(1667492);
		playerIds.add(1667834);
		playerIds.add(1667834);
		playerIds.add(1667966);
		playerIds.add(1668006);
		playerIds.add(1668144);
		playerIds.add(1668227);
		playerIds.add(1668501);
		playerIds.add(1668550);
		playerIds.add(1668553);
		playerIds.add(1668573);
		playerIds.add(1668986);
		playerIds.add(1669026);
		playerIds.add(1692947);
		playerIds.add(1722693);
		playerIds.add(1726446);
		playerIds.add(1726510);
		playerIds.add(1726511);
		playerIds.add(1729466);
		playerIds.add(1730193);
		playerIds.add(1741087);
		playerIds.add(1742662);
		playerIds.add(1742774);
		playerIds.add(1751958);
		playerIds.add(1753463);
		playerIds.add(1753463);
		playerIds.add(1771497);
		playerIds.add(1775441);
		playerIds.add(1794038);
		playerIds.add(1844523);
		playerIds.add(1865558);
		playerIds.add(1959997);
		playerIds.add(1997066);
		playerIds.add(2006910);
		playerIds.add(2007946);
		playerIds.add(2019114);
		playerIds.add(2023151);
		playerIds.add(2026116);
		playerIds.add(2035096);
		playerIds.add(2037390);
		playerIds.add(2037433);
		playerIds.add(2037466);
		playerIds.add(2048154);
		playerIds.add(2053624);
		playerIds.add(2083201);
		playerIds.add(2093527);
		playerIds.add(2096108);
		playerIds.add(2096108);
		playerIds.add(2099214);
		playerIds.add(2117641);
		playerIds.add(2121822);
		playerIds.add(2122103);
		playerIds.add(2138098);
		playerIds.add(2159148);
		playerIds.add(2170095);
		playerIds.add(2186766);
		playerIds.add(2210527);
		playerIds.add(2225986);
		playerIds.add(2252010);

		for (Integer pid : playerIds) {
			if (pid > 0) {
				List<Integer> clubIds = PlayerFactory.getAllPlayerClubIds(pid);

				String query = "SELECT player_id,player_name,SUM(matches) as matches,SUM(hundred) as hundreds,SUM(fifties) as fifties, "
						+ "SUM(twenty_five) twenty_five, ROUND(((SUM(runs_scored )*100)/SUM(balls_faced)),2) batting_strike_rate, "
						+ "SUM(wickets) wickets, ROUND((SUM(runs_given)/(SUM(balls_bowled)/6)),2) bowling_economy, "
						+ "ROUND((SUM(balls_bowled)/SUM(wickets)),2) bowling_strike_rate, SUM(man_of_matches) man_of_matches FROM ( ";

				for (int i = 0; i < clubIds.size(); i++) {
					query += "SELECT " + pid
							+ " player_id, CONCAT(p.f_name,' ',p.l_name) player_name, COUNT(DISTINCT(ps.match_id)) AS matches, "
							+ "SUM(IF(ps.runs_scored > 99, 1, 0)) hundred, SUM(IF(ps.runs_scored > 49,  IF(ps.runs_scored < 75, 1, 0),  0)) fifties, "
							+ "SUM(IF(ps.runs_scored > 24,  IF(ps.runs_scored < 50, 1, 0),  0)) twenty_five, SUM(ps.runs_scored) runs_scored, "
							+ "SUM(ps.balls_faced) balls_faced, SUM(ps.wickets) wickets, SUM(ps.runs_given) runs_given, SUM(ps.balls_bowled) balls_bowled, "
							+ "SUM(ps.man_of_the_match) man_of_matches " + "FROM club" + clubIds.get(i)
							+ ".player_statistics_summary ps, mcc.player p, club" + clubIds.get(i) + ".league l "
							+ "WHERE p.player_id = ps.player_id AND ps.player_id = " + pid
							+ " AND l.series_type = 'Twenty20' "
							+ "AND l.league_id = ps.series_id AND ps.match_date>='2017-02-10'";

					query += (i != clubIds.size() - 1 ? " UNION " : " ) final_query");
				}
				Connection conn = null;
				Statement st = null;
				ResultSet rs = null;

				try {
					conn = DButility.getDefaultReadConnection();
					st = conn.createStatement();
					rs = st.executeQuery(query);

					while (rs.next()) {
						bw.write("\"" + rs.getInt("player_id") + "\",");
						bw.write("\"" + rs.getString("player_name") + "\",");
						bw.write("\"" + rs.getInt("matches") + "\",");
						bw.write("\"" + rs.getInt("hundreds") + "\",");
						bw.write("\"" + rs.getInt("fifties") + "\",");
						bw.write("\"" + rs.getInt("twenty_five") + "\",");
						bw.write("\"" + rs.getString("batting_strike_rate") + "\",");
						bw.write("\"" + rs.getInt("wickets") + "\",");
						bw.write("\"" + rs.getString("bowling_economy") + "\",");
						bw.write("\"" + rs.getString("bowling_strike_rate") + "\",");
						bw.write("\"" + rs.getInt("man_of_matches") + "\"\n");
					}					
					System.out.println("Stats Generated for Player Id - " + pid);

				} catch (Exception e) {
					System.out.println("Issue with getting stats for Player Id - " + pid);
				} finally {
					DButility.dbCloseAll(conn, st, rs);
				}
			}
		}
		bw.close();
		System.out.println("Program End!! ");
	}	
}
