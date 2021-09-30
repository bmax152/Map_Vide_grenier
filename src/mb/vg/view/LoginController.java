package mb.vg.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import mb.vg.MainVg;
import mb.vg.controller.Requetes;

public class LoginController {

	@FXML
	private TextField tfLogin;
	
	@FXML
	private PasswordField pfMdp;
	
	@FXML
	private Button btEnvoyer;

	
	@FXML
	public void initialize(){
		
		btEnvoyer.setOnAction(
				event -> identification());
		
		
		//Pour test
		tfLogin.setText("Admin");
		pfMdp.setText("admin");
	}
	
	private void identification() {
		String log = tfLogin.getText();
		String mdp = pfMdp.getText();
		
//		if(log.equals("Admin") && mdp.equals("admin")) {
//			//On ouvre la prochaine fenetre
//			showMap();
//		}else {
//			alertLogin();
//		}
		
		if(Requetes.login(log, mdp)) {
			//On ouvre la prochaine fenetre
			showMap();
		}else {
			alertLogin();
		}
	}

	public void showMap() {
		MainVg.primaryStage.setTitle("Map des réservations");
		try {
			SplitPane rootLayout;
		    FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(LoginController.class.getResource("../view/MaMap.fxml"));
		    rootLayout = (SplitPane) loader.load();
		    Scene scene = new Scene(rootLayout);
		    MainVg.primaryStage.setScene(scene);   
		    MainVg.primaryStage.setResizable(true);
		    MainVg.primaryStage.setMaximized(true);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	 private void alertLogin() {
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Erreur d'identifiant");
	        alert.setHeaderText(null);
	        alert.setContentText("Le Nom et/ou mot de passe est incorrect");
	 
	        DialogPane dialogPane = alert.getDialogPane();
	        dialogPane.getStylesheets().add(
	        		   getClass().getResource("myDialogs.css").toExternalForm());
	        		dialogPane.getStyleClass().add("myDialogLogin");
	        alert.showAndWait();
	    }
}
