package mb.vg.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import mb.vg.MainVg;
import mb.vg.controller.Requetes;
import mb.vg.model.Emplacement;
import mb.vg.model.Exposant;

public class MaMapController {

	
	//Variable utiles pour les déplacement
	double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
	    
	//pane
	@FXML
	private AnchorPane pane1;
	
	@FXML
	private AnchorPane pane2;
	
	@FXML
	private AnchorPane paneMap;
		
	//Label info
	@FXML
	private Label nomEmplLabel;
	
	@FXML
	private Label xLabel;
	
	@FXML
	private Label yLabel;
	
	@FXML
	private Label nomLabel;
	
	@FXML
	private Label prenomLabel;
	
	@FXML
	private Label metreLabel;
	
	//ComboBox Résa en attente non placées
	@FXML
	private ComboBox<Exposant> cbEnAttente;
	
	//Menu
	@FXML
	private MenuItem miEditerMap;
	
	@FXML
	private MenuItem miSave;
	
	//Button
	@FXML
	private Button btAddResa;
	
	@FXML
	private Button btnRetirer;
	
	//Liste avec tout les emplacement crées et save
	ArrayList<Emplacement> listEmpl = new ArrayList<Emplacement>();
	
	//Liste d'emplacement attibuer non save
	ArrayList<Emplacement> listToSave = new ArrayList<Emplacement>();
	
	//Liste d'emplacement où nous retirons les resa non save
	ArrayList<Emplacement> listToErase = new ArrayList<Emplacement>();
	
	//Groupe des bouton toggle des emplacements
	ToggleGroup group = new ToggleGroup();
		
	//le bouton qui est actuellement sélectionner
	Emplacement monBouton;
	
	ObservableList<Exposant> enAttente = FXCollections.observableArrayList();
	@FXML
	public void initialize(){
		
		makeAction();
		placerEmplBdd();
		//Construction des la ComboBox des Résa en attentes
		chargementBdd();
	}
	
	public void makeAction() {
		miEditerMap.setOnAction(
				event -> openEditeur());
		
		miSave.setOnAction(
				event -> saveBdd());
		
		btAddResa.setOnAction(
				event -> addResa());
		
		cbEnAttente.setOnAction(
				event -> testAvantAdd());
		
		btnRetirer.setOnAction(
				event -> retirerResa());
	}
	
	public void chargementBdd() {

		enAttente = Requetes.selectEnAttente();
		makeCbResa(enAttente);
	}
	
	public void makeCbResa(ObservableList<Exposant> enAttente) {
		//Evite à une initialisation suite à une save d'avoir deux liste a la suite!!
		cbEnAttente.getItems().clear();
		cbEnAttente.getItems().addAll(enAttente);
		
		//Permet de choisir comment apparait les items dans la CB
		cbEnAttente.setCellFactory(new Callback<ListView<Exposant>,ListCell<Exposant>>(){
	            @Override
	            public ListCell<Exposant> call(ListView<Exposant> p) {
	                final ListCell<Exposant> cell = new ListCell<Exposant>(){
	                    @Override
	                    protected void updateItem(Exposant t, boolean bln) {
	                        super.updateItem(t, bln);
	                        
	                        if(t != null){
	                            setText(t.getNom() + " " + t.getPrenom() + ": " + t.getMetre() + " mètres");
	                        }else{
	                            setText(null);
	                        }
	                    }
	                };
	                return cell;
	            }
	        });
		//Permet de choisir comment apparait l'items selectionner dans la CB
		cbEnAttente.setButtonCell(
			    new ListCell<Exposant>() {
			        @Override
			        protected void updateItem(Exposant t, boolean bln) {
			            super.updateItem(t, bln); 
			            if (bln) {
			                setText("");
			            } else {
			                setText(t.getNom() + " " + t.getPrenom() + ": " + t.getMetre() + " mètres");
			            }
			        }
			    });
	}
	
	private void openEditeur() {
		MainVg.primaryStage.setTitle("Edition de la Map");
		try {
			SplitPane rootLayout;
		    FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(LoginController.class.getResource("../view/MaMapEditeur.fxml"));
		    rootLayout = (SplitPane) loader.load();
		    Scene scene = new Scene(rootLayout);
		    MainVg.primaryStage.setScene(scene);  
		    MainVg.primaryStage.hide();
		    MainVg.primaryStage.setMaximized(true);
		    MainVg.primaryStage.show();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	private void addResa() {
		Exposant choix = cbEnAttente.getValue();
		
		if(choix != null && monBouton != null) {
			
			monBouton.setIdResa(choix.getIdRes());
			monBouton.setNomResa(choix.getNom());
			monBouton.setPrenomResa(choix.getPrenom());
			monBouton.setStatus("En cour");
			monBouton.getStyleClass().add("nonSave");
			nomLabel.setText(choix.getNom());
			prenomLabel.setText(choix.getPrenom());
			listToSave.add(monBouton);
			enAttente.remove(choix);
			cbEnAttente.getItems().clear();
			makeCbResa(enAttente);
		}
	}
	
	private void placerEmplBdd() {
    	listEmpl = Requetes.selectEmplBdd();
    	if(listEmpl != null) {
    		for(Emplacement empl : listEmpl) {
    			empl.place(empl);
    			definitionButton(empl);	
    		}
    	}
    }
	
	public void definitionButton(Emplacement empl) {
		
		String statut = empl.getStatus();
		System.out.println(statut);
		if (statut.equalsIgnoreCase("en attente")) {
			empl.getStyleClass().add("libre");
		}else {
			empl.getStyleClass().add("enCour");
		}
		
		empl.setToggleGroup(group);
		if(!listEmpl.contains(empl)) {
			listEmpl.add(empl);
		}
		empl.setOnMousePressed(mousePressedEventHandler);
		paneMap.getChildren().add(empl);
	}
	
	private void testAvantAdd() {
		
		Exposant choix = cbEnAttente.getValue();
		if(monBouton != null && choix != null) {
			String statut = monBouton.getStatus();
			int metre = monBouton.getMetre();
			int metreChoix = choix.getMetre();
			if(statut.equalsIgnoreCase("En attente") && metre == metreChoix) {
				btAddResa.setDisable(false);
			}else {
				btAddResa.setDisable(true);
			}
		}
	}
	
	EventHandler<MouseEvent> mousePressedEventHandler = 
	        new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent t) {
	            orgSceneX = t.getSceneX();
	            orgSceneY = t.getSceneY();
	            orgTranslateX = ((ToggleButton)(t.getSource())).getTranslateX();
	            orgTranslateY = ((ToggleButton)(t.getSource())).getTranslateY();
	            monBouton = ((Emplacement)t.getSource());
	            String str1 = String.valueOf(monBouton.getX());
	            String str2 = String.valueOf(monBouton.getY());
	            String str3 = String.valueOf(monBouton.getMetre());

	            nomEmplLabel.setText(monBouton.getNom());
	            xLabel.setText(str1);
	            yLabel.setText(str2);
	            metreLabel.setText(str3);
	            nomLabel.setText(monBouton.getNomResa());
	            prenomLabel.setText(monBouton.getPrenomResa());
	            //test si l'emplacement selec correspond à l'exposant selec
	            testAvantAdd();
	            //test si l'emplcement est reserve pour pouvoir supprimer la resa
	            String statut = monBouton.getStatus();
	            if(statut.equalsIgnoreCase("En cour")) {
	            	btnRetirer.setDisable(false);
	            }else {
	            	btnRetirer.setDisable(true);
	            }
	        }
	    };
	    
	    public void retirerResa() {
	    	//Requetes.retirerResa(monBouton);
	    	listToErase.add(monBouton);
	    	monBouton.getStyleClass().remove("enCour");
	    	monBouton.getStyleClass().add("erase");
	    }
	    
	    public void saveBdd() {
	    	if(listToSave != null) {
	    		Requetes.saveResa(listToSave);
	    		for (Emplacement empl : listToSave) {
	    			empl.getStyleClass().remove("nonSave");
					empl.getStyleClass().add("enCour");
				}
	    		listToSave.clear();
	    	}
	    	if(listToErase != null) {
    			for (Emplacement emplErase : listToErase) {
    				Requetes.retirerResa(emplErase);
    				emplErase.getStyleClass().remove("erase");
    				emplErase.getStyleClass().add("libre");
				}
    			listToErase.clear();
    		}
	    	initialize();
	    }
	//
}
