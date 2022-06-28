package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.MatchMatchPlayer;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(Map<Integer, Player> playerIdMap){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				int id = res.getInt("PlayerID");
				Player player = new Player(id, res.getString("Name"));
				playerIdMap.put(id, player);
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(Map<Integer, Match> matchIdMap){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				int id = res.getInt("m.MatchID");
				Match match = new Match(id, res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				matchIdMap.put(id, match);
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(Map<Integer, Match> matchIdMap, int mese){
		String sql = "SELECT MatchID "
				+ "FROM matches "
				+ "WHERE MONTH(DATE) = ?";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				int id = res.getInt("MatchID");
				result.add(matchIdMap.get(id));

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<MatchMatchPlayer> listAllPesi(Map<Integer, Match> matchIdMap, int mese, int minuti){
		String sql = "SELECT t1.MatchID AS m1,t2.MatchID AS m2, COUNT(DISTINCT t1.PlayerID) AS peso "
				+ "FROM "
				+ "(SELECT m1.MatchID, a1.PlayerID "
				+ "FROM matches m1, actions a1 "
				+ "WHERE MONTH(m1.DATE) = ?  AND m1.MatchID = a1.MatchID AND a1.TimePlayed > ?) t1, "
				+ "(SELECT m1.MatchID, a1.PlayerID "
				+ "FROM matches m1, actions a1 "
				+ "WHERE MONTH(m1.DATE) = ?  AND m1.MatchID = a1.MatchID AND a1.TimePlayed > ?) t2 "
				+ "WHERE t1.MatchID < t2.MatchID AND t1.PlayerID = t2.PlayerID "
				+ "GROUP BY t1.MatchID,t2.MatchID ";
		List<MatchMatchPlayer> result = new ArrayList<MatchMatchPlayer>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setInt(2, minuti);
			st.setInt(3, mese);
			st.setInt(4, minuti);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				int matchId1 = res.getInt("m1");
				int matchId2 = res.getInt("m2");
				int peso = res.getInt("peso");
				MatchMatchPlayer mmt = new MatchMatchPlayer(matchIdMap.get(matchId1), matchIdMap.get(matchId2), peso);		
				result.add(mmt);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
