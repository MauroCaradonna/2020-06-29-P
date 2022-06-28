/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.MatchMatchPlayer;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Month> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	txtResult.clear();
    	List<MatchMatchPlayer> mmtList = this.model.getCoppie();
    	for(MatchMatchPlayer mmt : mmtList)
    		txtResult.appendText(mmt.getMatch2() +" - "+mmt.getMatch1()+ " : "+mmt.getnPlayer()+"\n");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Graph<Match, DefaultWeightedEdge> grafo = this.model.creagrafo(Integer.parseInt(txtMinuti.getText()), cmbMese.getValue().getValue());
    	txtResult.setText("N. vertici : " +grafo.vertexSet().size()+"\n");
    	txtResult.appendText("N. archi : " +grafo.edgeSet().size()+"\n");
    	
    	cmbM1.getItems().addAll(grafo.vertexSet());
    	cmbM2.getItems().addAll(grafo.vertexSet());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	List<Match> percorso = this.model.getPercorso(cmbM1.getValue(), cmbM2.getValue());
    	txtResult.clear();
    	for(Match match : percorso)
    		txtResult.appendText(match+"\n");
    	txtResult.appendText("Peso totale : "+this.model.getPesoPercorso());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        for(int i = 1; i <= 12; i++)
        	cmbMese.getItems().add(Month.of(i));
    }
    
    public void setModel(Model model) {
    	this.model = model;
  
    }
    
    
}
