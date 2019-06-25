package it.polito.tdp.artsmia.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m= new Model();
		m.creaGrafo();
		
		System.out.println(m.calcolaComponenteConnessa(689));
		
		System.out.println("********************\n");
		for(ArtObject a: m.cercaOggetti(2, 689)) {
			System.out.println(a.toString()+"\n");
		}
	}

}
