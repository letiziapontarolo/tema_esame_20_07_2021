package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph<User, DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private Map<String, User> userIdMap;
	private List<Arco> archi;
	
	public Model() {
		
		dao = new YelpDao();
	}
	
	public List<Integer> listaAnni() {
		return this.dao.listaAnni();
	}
	
	public void creaGrafo(int numRecensioni, int anno) {
		
		userIdMap = new HashMap<String, User>();
		grafo = new SimpleWeightedGraph<User, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.dao.creaVertici(userIdMap, numRecensioni);
		Graphs.addAllVertices(this.grafo, userIdMap.values());
		
		archi = this.dao.listaArchi(userIdMap, numRecensioni, anno);
		
		for (Arco a : archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getU1(), a.getU2(), a.getGrado());
		}
	}
	
	public List<String> listaUtenti() {
		
		List<String> result = new ArrayList<String>();
		for (User u : this.grafo.vertexSet()) {
			result.add(u.toString());
		}
		
		Collections.sort(result);
		return result;
		
	}
	
	public String utenteSimile(String u) {
		
		String result = "";
		int max = 0;
		for (User utente : this.grafo.vertexSet()) {
			if (utente.toString().equals(u)) {
				List<User> vicini = Graphs.neighborListOf(this.grafo, utente);
				for (User uu : vicini) {
					DefaultWeightedEdge e = this.grafo.getEdge(utente, uu);
					if (this.grafo.getEdgeWeight(e) > max) {
						max = (int)this.grafo.getEdgeWeight(e);
					}
				}
				
				for (User uuu : vicini) {
					if(this.grafo.getEdgeWeight(this.grafo.getEdge(utente, uuu))==max) {
						result = result + uuu.toString() + "    GRADO: " + max + "\n";
					}
				}
				
				
			}
		}
		
		return result;
		
		
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
		}
	
		 public int numeroArchi() {
		return this.grafo.edgeSet().size();
		}

	
}
