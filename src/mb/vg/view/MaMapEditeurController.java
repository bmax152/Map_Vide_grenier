package mb.vg.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import mb.vg.MainVg;
import mb.vg.controller.Requetes;
import mb.vg.model.Emplacement;

public class MaMapEditeurController {

	
	//pane
	@FXML
	private AnchorPane pane1;
	
	@FXML
	private AnchorPane pane2;
	
	@FXML
	private AnchorPane paneMap;
		
	//MenuItem
	@FXML
	private MenuItem miMapResa;
	
	@FXML
	private MenuItem miSave;
	
	//ComboBox
	@FXML
	private ComboBox<Integer> cbMetre;
	
	//Button
	@FXML
	private Button addMapResa;
	
	@FXML
	private Button btChangeName;
	
	@FXML
	private Button btRotation;
	
	@FXML
	private Button buttonUp;
	
	@FXML
	private Button buttonRight;
	
	@FXML
	private Button buttonLeft;
	
	@FXML
	private Button buttonDown;
	
	@FXML
	private Button btErase;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	//TextField
	@FXML
	private TextField tfStep;
	
	@FXML
	private TextField tfNomEmpl;
	
	@FXML
	private TextField tfChangeName;
	
	//Radio button
	@FXML
	private RadioButton radioH;
	
	@FXML
	private RadioButton radioV;
	
	//Map
	@FXML
	private ImageView ivPlan;
	
	//Label
	@FXML
	private Label lbSave;
	String strSave = "*modifications non sauvgardées";
	String titreStage = MainVg.primaryStage.getTitle();
	
	//Liste avec tout les emplacement crées et save
	ArrayList<Emplacement> listEmpl = new ArrayList<Emplacement>();
	
	//Liste des emplacements à supprimer de la BDD quand on save
	ArrayList<Emplacement> listEmplSuppr = new ArrayList<Emplacement>();
	
	
	//Variable utiles pour les déplacement
	double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
	
    //Groupe des bouton toggle des emplacements
	ToggleGroup group = new ToggleGroup();
	
	//Groupe des bouton radio de la rotation H ou V des emplacements
	ToggleGroup rotateGroup = new ToggleGroup();
	
	//le bouton qui est actuellement sélectionner
	Emplacement monBouton;
	
	//Boolean qui detecte une modif pour les saves
	Boolean save;

	@FXML
	public void initialize(){
		
		paneMap.getChildren().removeAll(listEmpl);
		listEmpl.clear();
		makeCbMetre(); 
		makeIniAction();
		placerEmplBdd();
		save = false;
		makeLabelSave();
	}


	public void makeIniAction() {
		miMapResa.setOnAction(
				event -> openMapResa());
		
		miSave.setOnAction(
				event -> saveBdd());
		
		addMapResa.setOnAction(
				event -> ajouterEmpl());
		
		//Définitions des actions des différents boutons
		buttonUp.setOnAction(
				event -> fleche(monBouton, 'y', 'n')); 
		
		buttonRight.setOnAction(
				event -> fleche(monBouton, 'x', 'p')); 
		
		buttonLeft.setOnAction(
				event -> fleche(monBouton, 'x', 'n')); 
		
		buttonDown.setOnAction(
				event -> fleche(monBouton, 'y', 'p')); 
		
		btRotation.setOnAction(
				event -> makeRotate());
		
		btChangeName.setOnAction(
				event -> changeName());
		
		btErase.setOnAction(
				event -> supprEmpl());
		
		btSave.setOnAction(
				event -> saveBdd());
		
		btCancel.setOnAction(
				event -> initialize());
		
		radioH.setToggleGroup(rotateGroup);
		radioV.setToggleGroup(rotateGroup);
		
		MainVg.primaryStage.setOnCloseRequest( event -> {
			alertSave();
			} 
		);
		
	}

	public void makeCbMetre() {
		List<Integer> nbr = new ArrayList<>(); 
		
		for (int i = 1; i < 6; i++) {
			nbr.add(i);
		}
		cbMetre.setItems(FXCollections.observableArrayList(nbr));
	}
	
	private void openMapResa() {
		MainVg.primaryStage.setTitle("Map des réservations");
		try {
			SplitPane rootLayout;
		    FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(LoginController.class.getResource("../view/MaMap.fxml"));
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
	
	private void ajouterEmpl() {
		String namePerso = tfNomEmpl.getText();
		Integer nbrMetre = cbMetre.getValue();
		RadioButton rotateButton = (RadioButton) rotateGroup.getSelectedToggle();	

		if(!(namePerso == "") || !(nbrMetre == null)) {
			Emplacement empl = new Emplacement(namePerso, 150d, 150d, nbrMetre, "Libre", "", "", 0, 0, false);
			if(rotateButton.getText().equals("V")) {
				empl.setButtonRotate(true);
			}
			empl.place(empl);
			definitionButton(empl);	
			save = true;
			makeLabelSave();
		}
	}
	
	public void definitionButton(Emplacement test) {
	
		test.getStyleClass().add("libre");		
		test.setOnMousePressed(mousePressedEventHandler);
		test.setOnMouseDragged(mouseDraggedEventHandler);
		test.setOnMouseClicked(mouseDraggedEventHandler);
		test.setToggleGroup(group);
		if(!listEmpl.contains(test)) {
			listEmpl.add(test);
		}
		paneMap.getChildren().add(test);
	}
	
	public void makeRotate() {
		
		if(monBouton != null) {
			if(monBouton.isRotate()) {
				monBouton.setButtonRotate(false);
				monBouton.getTransforms().add(new Rotate(90, 0, 0));
				
			}else {
				monBouton.setButtonRotate(true);
				monBouton.getTransforms().add(new Rotate(270, 0, 0));
			}
		}
		save = true;
		makeLabelSave();
	}
	
	public void fleche(Emplacement test, char dir, char signe) {
		
		Translate translate = new Translate(); 
		
		double val = Double.parseDouble(tfStep.getText());
		
		//Evite des décalage à cause de la rotation
		if(test.isRotate()) {
			test.getTransforms().add(new Rotate(90, 0, 0));
		}
		//
		
		if (signe == 'p') {
			val = Double.parseDouble(tfStep.getText());
		} else {
			val = -Double.parseDouble(tfStep.getText());
		}
		
		if(dir == 'y') {
			translate.setY(val);
			test.setY(test.getY() + val);
		} else {
			translate.setX(val);
			test.setX(test.getX() + val);
		}
		
  
	    test.getTransforms().add(translate); 
	    
	    //
		if(test.isRotate()) {
			test.getTransforms().add(new Rotate(270, 0, 0));
		}
		save = true;
		makeLabelSave();
	}
	
	private void changeName() {
		
		if(tfChangeName.getText() != null) {		
			monBouton.setNom(tfChangeName.getText());
			monBouton.setText(tfChangeName.getText());
		}
		save = true;
	}
	
	private void alertSave() {
		if(save) {
			//System.out.println("Modification à enregistrer");
		}
		
	}
	//
	//Sert a récupérer les coordonnes du bouton
		EventHandler<MouseEvent> mousePressedEventHandler = 
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		            orgSceneX = t.getSceneX();
		            orgSceneY = t.getSceneY();
		            orgTranslateX = ((ToggleButton)(t.getSource())).getTranslateX();
		            orgTranslateY = ((ToggleButton)(t.getSource())).getTranslateY();
		            
		        }
		    };
		    
		    //Rend le boutons draggable
		    EventHandler<MouseEvent> mouseDraggedEventHandler = 
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		            double offsetX = t.getSceneX() - orgSceneX;
		            double offsetY = t.getSceneY() - orgSceneY;
		            double newTranslateX = orgTranslateX + offsetX;
		            double newTranslateY = orgTranslateY + offsetY;
		            
		            double posX = ((Emplacement)t.getSource()).getLayoutX() + newTranslateX;
		            double posY = ((Emplacement)t.getSource()).getLayoutY() + newTranslateY;
		            
		            //Récupér la taille de l'image View
		            double widthPlan = ivPlan.getFitWidth();
		    		double heightPlan = ivPlan.getFitHeight();
		            
		            //On ne peut pas poser le bouton en dehors de l'image
		            if(0 < posX && posX < widthPlan &&  0 < posY && posY < heightPlan) {
		            	((ToggleButton)(t.getSource())).setTranslateX(newTranslateX);
			            ((ToggleButton)(t.getSource())).setTranslateY(newTranslateY);

			            //Actualise l'emplacement x et y du bouton durant le déplacement
			            ((Emplacement)t.getSource()).setX(posX);
			            ((Emplacement)t.getSource()).setY(posY);

		            }
		            
		            monBouton = ((Emplacement)t.getSource());
		            tfChangeName.setText(monBouton.getNom());
		            save = true;
		            makeLabelSave();
		        }
		    };
		    
		    private void supprEmpl() {
		    	if(monBouton != null) {
		    		paneMap.getChildren().remove(monBouton);
		    		listEmpl.remove(monBouton);
		    		listEmplSuppr.add(monBouton);
		    		save = true;
		    		makeLabelSave();
		    	}
		    	
		    }
		    
		    private void supprEmplBdd(ArrayList<Emplacement> list) {
		    	
		    	for (Emplacement emplacement : list) {
					if(emplacement.getIdButton() != 0) {
						Requetes.eraseBdd(emplacement);
					}
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
		    
		    private void saveBdd() {
		    	Requetes.saveEmplEditeur(listEmpl);
		    	supprEmplBdd(listEmplSuppr);
		    	initialize();
		    }
		    
		    private void makeLabelSave() {
		    	
		    	if(save) {
		    		lbSave.setText(strSave);
		    		MainVg.primaryStage.setTitle(titreStage + "*");
		    		
		    	}else {
		    		lbSave.setText("");
		    		MainVg.primaryStage.setTitle(titreStage);
		    	}
		    }
		    
		   
	//
}
