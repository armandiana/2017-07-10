package it.polito.tdp.artsmia.model;

public class ObjectAndCount {
	private int Artoggetto;
	private int nEsibizioni;
	
	public ObjectAndCount(int oggetto, int nEsibizioni) {
		super();
		this.Artoggetto = oggetto;
		this.nEsibizioni = nEsibizioni;
	}

	public int getOggetto() {
		return Artoggetto;
	}

	public void setOggetto(int oggetto) {
		this.Artoggetto = oggetto;
	}

	public int getnEsibizioni() {
		return nEsibizioni;
	}

	public void setnEsibizioni(int nEsibizioni) {
		this.nEsibizioni = nEsibizioni;
	}

	@Override
	public String toString() {
		return "Oggetto=" + Artoggetto + ", Numero Esibizioni=" + nEsibizioni;
	}
		
}
