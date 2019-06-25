package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private List<ArtObject>oggetti;
	private Map<Integer, ArtObject>oggettiIdMap= new HashMap<Integer, ArtObject>();
	
	private Graph<ArtObject, DefaultWeightedEdge>grafo;
	
	private Map<ArtObject, ArtObject>backVisit;
	
	private List<ArtObject>best;
	
	
	
	public Model() {
		//this.oggettiIdMap= new HashMap<Integer, ArtObject>();
		oggetti= new ArrayList<ArtObject>();
	}
	
	public void creaGrafo() {
		this.grafo= new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		ArtsmiaDAO dao= new ArtsmiaDAO();
		
		this.oggetti=dao.listObjects();

		//creo la id MAP
		this.oggettiIdMap= new HashMap<Integer, ArtObject>();
		for(ArtObject a: oggetti) {
			oggettiIdMap.put(a.getId(), a);
		}
		
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, oggetti);
		
		//aggiungo gli archi
		for(ArtObject a: this.grafo.vertexSet()) {
			List<ObjectAndCount>connessi= dao.getConnessione(a, oggettiIdMap);
		
			if(!connessi.isEmpty()) {
				for(ObjectAndCount oc: connessi) {
					ArtObject aa= oggettiIdMap.get(oc.getOggetto());
					if(aa!=null) {
						Graphs.addEdge(this.grafo, a, aa, oc.getnEsibizioni());
					}
				}
			}
		}
		
		System.out.format("Creato grafo con %d VERTICI e %d ARCHI \n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());	
	}
	
	public boolean isObjectValid(int idArtObject) {
		ArtObject ao= this.oggettiIdMap.get(idArtObject);
		if(ao!=null) {
			return true;
		}
		return false;
	}
	
	public Integer calcolaComponenteConnessa(int idArtObject) {
		ArtObject verticePartenza=null;
		List<ArtObject>result= new ArrayList<ArtObject>();
		this.backVisit= new HashMap<ArtObject, ArtObject>();
		
		
		if(isObjectValid(idArtObject)) {
			 verticePartenza= this.oggettiIdMap.get(idArtObject);
		}
		
		if(verticePartenza!=null) {
			GraphIterator<ArtObject, DefaultWeightedEdge>df= new DepthFirstIterator<ArtObject, DefaultWeightedEdge>(this.grafo, verticePartenza);
			df.addTraversalListener(new TraversalListener<ArtObject, DefaultWeightedEdge>() {
				
				@Override
				public void vertexTraversed(VertexTraversalEvent<ArtObject> arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void vertexFinished(VertexTraversalEvent<ArtObject> arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {
					ArtObject source= grafo.getEdgeSource(ev.getEdge());
					ArtObject target= grafo.getEdgeTarget(ev.getEdge());
					
					if( !backVisit.containsKey(target) && backVisit.containsKey(source) ) {

						backVisit.put(target, source) ;

					} else if(!backVisit.containsKey(source) && backVisit.containsKey(target)) {

						backVisit.put(source, target) ;

					}

					
				}
				
				@Override
				public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			backVisit.put(verticePartenza, null);
			
			while(df.hasNext()) {
				result.add(df.next());
			}
			
			return result.size();
		}
			/*List<ArtObject>vicini= new ArrayList<ArtObject>();
			vicini= Graphs.neighborListOf(this.grafo, verticePartenza);
			return vicini.size();
		}
		return null;
			//visita il grafo
			/*Set<ArtObject>visitati= new HashSet<ArtObject>();
			
			DepthFirstIterator<ArtObject, DefaultWeightedEdge>df= new DepthFirstIterator<ArtObject, DefaultWeightedEdge>(grafo, verticePartenza);
			
			while(df.hasNext()) {
				visitati.add(df.next());
			}
			
			return visitati.size();
		}else {
			return  null;
		}*/
		return  null;
	}
	
	public Graph<ArtObject, DefaultWeightedEdge>getGrafo(){
		return this.grafo;
	}
	
	
	
	public List<ArtObject> cercaOggetti(int LUN, int idArtObject){
		//this.best= null;
		
		ArtObject partenza= null;
		
		for(ArtObject p: oggetti) {
			if(p.getId()==idArtObject) {
				partenza=p;
			}
		}

		
		//creiamo la struttura dati per la soluzione parziale
		List<ArtObject>parziale= new ArrayList<ArtObject>();
		parziale.add(partenza);
			
		this.best= new ArrayList<ArtObject>();
		best.add(partenza);
			
		cerca(parziale, 1, LUN);
		
	
		//Collections.sort(best);
		return best;
	}
	
	private void cerca(List<ArtObject>parziale, int livello, int LUN) {
		
		//caso terminale
		if(livello==LUN) {
			//verifico che il peso della soluzione parziale sia maggiore della soluzione migliore
			if(peso(parziale)>peso(best)) {
				best= new ArrayList<ArtObject>(parziale);
			}
			return;
		}
		
		//troviamo i vertici adiacenti all'ultimo vertice inserito nella lista
		ArtObject ultimo= parziale.get(parziale.size()-1);
		
		List<ArtObject>adiacentiUltimo= Graphs.neighborListOf(this.grafo, ultimo);
		
		for(ArtObject prova: adiacentiUltimo) {
			if(!parziale.contains(prova) && prova.getClassification()!=null && prova.getClassification().equals(parziale.get(0).getClassification())) {
				//se arriviamo qui-> abbiamo un candidato, allora proviamo ad aggiungerla alla lista parziale
				parziale.add(prova);
				//richiamo il metodo della ricorsione
				cerca(parziale, livello+1, LUN);
				
				//backtrack
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private int peso(List<ArtObject> parziale) {
		int peso=0;
		
		for(int i=0; i<parziale.size()-1; i++) {
			DefaultWeightedEdge e= this.grafo.getEdge(parziale.get(i), parziale.get(i+1));
			
			if(e!=null) {
				peso+=this.grafo.getEdgeWeight(e);
			}
		}
		
		return peso;
	}
	
	
	
	
	
	

}
