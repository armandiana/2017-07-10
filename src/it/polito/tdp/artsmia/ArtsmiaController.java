/**
 * Sample Skeleton for 'Artsmia.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxLUN"
	private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaComponenteConnessa"
	private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

	@FXML // fx:id="btnCercaOggetti"
	private Button btnCercaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaOggetti"
	private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="txtObjectId"
	private TextField txtObjectId; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doAnalizzaOggetti(ActionEvent event) {
		txtResult.clear();
		
		this.model.creaGrafo();
		
		this.txtResult.appendText(String.format("Creato grafo con %d Veritci e %d Archi.\n", this.model.getGrafo().vertexSet().size(),
				this.model.getGrafo().edgeSet().size()));
		
	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {
		Integer a=null;
		try {
			a= Integer.parseInt(this.txtObjectId.getText());
		}catch(IllegalArgumentException e) {
			this.txtResult.appendText("Inserire il codice identificativo dell'oggetto nel formato corretto \n");
		}
	
		if(this.model.calcolaComponenteConnessa(a)!=null) {
			this.boxLUN.getItems().clear();
		  
			for(int i=2; i<=this.model.calcolaComponenteConnessa(a); i++) {
				this.boxLUN.getItems().add(i);
			}
			
			this.txtResult.appendText("Numero di comonenti connesse all'oggetto: "+a+" ->"+this.model.calcolaComponenteConnessa(a)+"\n");

		}
		
	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		Integer LUN= null;
		Integer idArtObject=null;
		try {
			idArtObject= Integer.parseInt(this.txtObjectId.getText());
			LUN= this.boxLUN.getValue();
		}catch(Exception e) {
			this.txtResult.appendText("Inserire un codice oggetto e selezionare un valore di LUN.\n");
		}
		List<ArtObject>oggetti=null;
		if(idArtObject!= null && LUN!=null) {
		    oggetti=this.model.cercaOggetti(LUN, idArtObject);
		}
		
		if(oggetti!=null) {
			this.txtResult.appendText("Oggetti connessi a "+idArtObject+": \n");
			for(ArtObject a: oggetti) {
				this.txtResult.appendText(a.toString()+"\n");
			}
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}

	public void setModel(Model model) {
		this.model=model;
	}
}
