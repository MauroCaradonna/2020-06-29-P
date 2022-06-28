package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private Map<Integer, Match> matchIdMap;
	private Map<Integer, Player> playerIdMap;
	private Graph<Match, DefaultWeightedEdge> grafo;
	private List<Match> migliore; 
	private int pesoPercorso;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.matchIdMap = new HashMap<Integer, Match>();
		this.dao.listAllMatches(matchIdMap);
		this.playerIdMap = new HashMap<Integer, Player>();
		this.dao.listAllPlayers(playerIdMap);
	}
	
	public Graph<Match, DefaultWeightedEdge> creagrafo(int min, int mese){
		this.grafo = new SimpleWeightedGraph<Match, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.dao.listAllMatches(matchIdMap, mese));
		
		List<MatchMatchPlayer> mmtList = this.dao.listAllPesi(matchIdMap, mese, min);
		for(MatchMatchPlayer mmt : mmtList)
			Graphs.addEdgeWithVertices(this.grafo, mmt.getMatch1(), mmt.getMatch2(), mmt.getnPlayer());
		return this.grafo;
	}
	
	public List<MatchMatchPlayer> getCoppie(){
		List<MatchMatchPlayer> result = new ArrayList<MatchMatchPlayer>();
		int max = 0;
		for(DefaultWeightedEdge edge : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(edge) > max)
				max = (int) this.grafo.getEdgeWeight(edge);
		}
		for(DefaultWeightedEdge edge : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(edge) == max)
				result.add(new MatchMatchPlayer(this.grafo.getEdgeSource(edge), this.grafo.getEdgeTarget(edge), max));
		}
		return result;
	}
	
	public List<Match> getPercorso(Match match1, Match match2){
		this.migliore = new ArrayList<Match>();
		this.pesoPercorso = 0;
		List<Match> parziale = new ArrayList<Match>();
		parziale.add(match1);
		List<Match> matches = Graphs.neighborListOf(grafo, match1);
		this.cerca(parziale, 1, matches, match2);
		return migliore;
	}

	private void cerca(List<Match> parziale, int l, List<Match> matches, Match match2) {
		if(parziale.get(parziale.size() - 1).equals(match2)) {
			int peso = peso(parziale);
			if(peso > this.pesoPercorso) {
				this.pesoPercorso = peso;
				this.migliore = new ArrayList<Match>(parziale);
			}
		}else {
			if(l == this.grafo.vertexSet().size())
				return;
			
			for(Match adiacente : matches) {
				if(possoAggiungere(adiacente, parziale)) {
					List<Match> adiacenti = Graphs.neighborListOf(grafo, adiacente);
					parziale.add(adiacente);
					cerca(parziale, l+1, adiacenti, match2);
					parziale.remove(parziale.size() - 1);
				}
			}
		}
	}

	private boolean possoAggiungere(Match daAggiungere, List<Match> parziale) {
		for(Match match: parziale) {
			if((match.getTeamAwayID()==daAggiungere.getTeamAwayID() && match.getTeamHomeID()==daAggiungere.getTeamHomeID()) ||(match.getTeamHomeID()==daAggiungere.getTeamAwayID() && match.getTeamAwayID()==daAggiungere.getTeamHomeID()))
				return false;
		}
		return true;
	}

	private int peso(List<Match> parziale) {
		int peso = 0;
		for(int i = 0; i< parziale.size() - 1; i++) {
			for(int j = 0; j< parziale.size(); j++) 
				peso += this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i), parziale.get(j)));
		}
		return peso;
	}
	
	public int getPesoPercorso() {
		return this.pesoPercorso;
	}
}
