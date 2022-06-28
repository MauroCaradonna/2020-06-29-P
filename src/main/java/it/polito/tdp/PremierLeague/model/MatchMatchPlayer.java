package it.polito.tdp.PremierLeague.model;

public class MatchMatchPlayer {
	private Match match1;
	private Match match2;
	private int nPlayer;
	public MatchMatchPlayer(Match match1, Match match2, int nPlayer) {
		super();
		this.match1 = match1;
		this.match2 = match2;
		this.nPlayer = nPlayer;
	}
	public Match getMatch1() {
		return match1;
	}
	public void setMatch1(Match match1) {
		this.match1 = match1;
	}
	public Match getMatch2() {
		return match2;
	}
	public void setMatch2(Match match2) {
		this.match2 = match2;
	}
	public int getnPlayer() {
		return nPlayer;
	}
	public void setnPlayer(int nPlayer) {
		this.nPlayer = nPlayer;
	}
	

	
}
