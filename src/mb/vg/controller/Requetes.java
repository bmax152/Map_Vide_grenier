package mb.vg.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mb.vg.model.Emplacement;
import mb.vg.model.Exposant;


public class Requetes {
	Connection cx = null;
	Statement st = null;
	ResultSet rs = null;
	
	public static void select() {
		
		try {
			Requetes connecBdd = new Requetes();
			connecBdd.cx = Connexion.connection();
			connecBdd.st = connecBdd.cx.createStatement();
			connecBdd.rs = connecBdd.st.executeQuery("SELECT * FROM utilisateur");
			while (connecBdd.rs.next()) {
				String nom = connecBdd.rs.getString("NOM_UTIL");
				System.out.println(nom);
			}
			connecBdd.rs.close();
			connecBdd.st.close();
			connecBdd.cx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean login(String login, String psw) {
		try {
			Requetes connecBdd = new Requetes();
			connecBdd.cx = Connexion.connection();
			PreparedStatement prepare = connecBdd.cx.prepareStatement("SELECT * FROM utilisateur WHERE NOM_UTIL LIKE ? AND MDP_UTIL LIKE ? AND ID_ROL = 1");
			prepare.setString(1, login);
			prepare.setString(2, psw);
			connecBdd.rs = prepare.executeQuery();
			boolean connected = false;
			
			while (connecBdd.rs.next()) {
				connected = true;
			}
			
			connecBdd.rs.close();
			connecBdd.cx.close();
			return connected;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static void saveEmplEditeur(ArrayList<Emplacement> list) {
		try {
			Requetes connecBdd = new Requetes();
			connecBdd.cx = Connexion.connection();
			
			String upd = "UPDATE emplacement SET LABEL_EMP = ?, METRE_EMP = ?, XMAP_EMP = ?, YMAP_EMP = ?, ROTATE_EMP = ? WHERE ID_EMP = ?";
			String crea = "INSERT INTO emplacement(LABEL_EMP, METRE_EMP, XMAP_EMP, YMAP_EMP, ROTATE_EMP) VALUE (?,?, ?, ?, ?)";
			String choix = "";
			for (Emplacement empl : list) {
				
				if(empl.getIdButton() == 0) {
					choix = crea;
				}else {
					choix = upd;
				}
				PreparedStatement prepare = connecBdd.cx.prepareStatement(choix);
				String nom = empl.getNom();
				double x = empl.getX();
				double y = empl.getY();
				boolean rotate = empl.isRotate();
				int idBut = empl.getIdButton();
				int metre = empl.getMetre();
				
				prepare.setString(1, nom);
				prepare.setInt(2, metre);
				prepare.setDouble(3, x);
				prepare.setDouble(4, y);
				prepare.setBoolean(5, rotate);
				if(idBut != 0) {					
					prepare.setInt(6, idBut);
				}
				prepare.executeUpdate();
			}	
			connecBdd.cx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//On récupere seulement les empl pour l'éditeur de map, on ne récupere donc pas les infos 
	//des résas
	public static ArrayList<Emplacement> selectEmplBdd() {
		try {
			ArrayList<Emplacement> listBddEmp = new ArrayList<Emplacement>();
			int idEmpl;
			String label;
			double x;
			double y;
			int metre;
			boolean rotate;
			String statut;
			String nomResa;
			String prenomResa;
			int idResa;
			
			Requetes connecBdd = new Requetes();
			connecBdd.cx = Connexion.connection();
			connecBdd.st = connecBdd.cx.createStatement();
			connecBdd.rs = connecBdd.st.executeQuery("SELECT * FROM emplacement "
														+ "LEFT JOIN reservation ON reservation.ID_EMP = emplacement.ID_EMP "
														+ "LEFT JOIN exposant ON reservation.ID_RES = exposant.ID_RES");
			while (connecBdd.rs.next()) {
	
				idEmpl = connecBdd.rs.getInt("ID_EMP");
				label = connecBdd.rs.getString("LABEL_EMP");
				metre = connecBdd.rs.getInt("METRE_EMP");
				x = connecBdd.rs.getDouble("XMAP_EMP");
				y = connecBdd.rs.getDouble("YMAP_EMP");
				rotate = connecBdd.rs.getBoolean("ROTATE_EMP");
				statut = connecBdd.rs.getString("STATUTRESERVATION_RES");
				if(statut == null) {
					statut = "En attente";
				}
				nomResa = connecBdd.rs.getString("NOM_EXP");
				prenomResa = connecBdd.rs.getString("PRENOM_EXP");
				idResa = connecBdd.rs.getInt("ID_RES");
				
				Emplacement attente = new Emplacement(label, x, y, metre, statut, nomResa, prenomResa, idEmpl, idResa, rotate);
				listBddEmp.add(attente);
			}
			connecBdd.rs.close();
			connecBdd.st.close();
			connecBdd.cx.close();
			return listBddEmp;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void eraseBdd(Emplacement empl) {
		try {
			int idBut = empl.getIdButton();
			Requetes connecBdd = new Requetes();
			connecBdd.cx = Connexion.connection();
			PreparedStatement prepare = connecBdd.cx.prepareStatement("DELETE FROM emplacement WHERE ID_EMP = ?");
			prepare.setInt(1, idBut);
			prepare.executeUpdate();
			
			connecBdd.cx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ObservableList<Exposant> selectEnAttente() {
		try {
			//Redefinir la liste comme personne et non emplacement
			ObservableList<Exposant> listBddEnAttente = FXCollections.observableArrayList();
			String nomResa;
			String prenomResa;
			int idResa;
			int metre;

			
			Requetes connecBdd = new Requetes();
			connecBdd.cx = Connexion.connection();
			connecBdd.st = connecBdd.cx.createStatement();
			connecBdd.rs = connecBdd.st.executeQuery("SELECT * FROM exposant JOIN reservation WHERE exposant.ID_RES = reservation.ID_RES AND reservation.STATUTRESERVATION_RES LIKE 'En attente'");
			while (connecBdd.rs.next()) {
	
				nomResa = connecBdd.rs.getString("NOM_EXP");
				prenomResa = connecBdd.rs.getString("PRENOM_EXP");
				idResa = connecBdd.rs.getInt("ID_RES");
				metre = connecBdd.rs.getInt("NBREEMPLRESERVE_RES");
				Exposant expo = new Exposant( nomResa, prenomResa, metre, idResa, "En attente");
				listBddEnAttente.add(expo);
				
			}
			connecBdd.rs.close();
			connecBdd.st.close();
			connecBdd.cx.close();
			return listBddEnAttente;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void updateBddAll(ArrayList<Emplacement> list) {		
			
			try {
				Requetes connecBdd = new Requetes();
				connecBdd.cx = Connexion.connection();
				
				PreparedStatement prepare = connecBdd.cx.prepareStatement("UPDATE reservation SET STATUTRESERVATION_RES = ?, ID_EMP = ? WHERE ID_RES = ?");
				
				for (Emplacement empl : list) {
			
					int id = empl.getIdButton();
		
					prepare.setString(1, "En cour");
					prepare.setInt(2, id);
					prepare.executeUpdate();
				}

				connecBdd.cx.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	public static void saveResa(ArrayList<Emplacement> list) {
		try {
			Requetes connecBdd = new Requetes();
			connecBdd.cx = Connexion.connection();
			
			PreparedStatement prepare = connecBdd.cx.prepareStatement("UPDATE reservation SET STATUTRESERVATION_RES = ?, ID_EMP = ? WHERE ID_RES = ?");
			
			for (Emplacement empl : list) {
		
				int id = empl.getIdButton();
				int idRes = empl.getIdResa();
	
				prepare.setString(1, "En cour");
				prepare.setInt(2, id);
				prepare.setInt(3, idRes);
				prepare.executeUpdate();
			}

			connecBdd.cx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void retirerResa(Emplacement empl) {
		try {
			Requetes connecBdd = new Requetes();
			connecBdd.cx = Connexion.connection();
			
			PreparedStatement prepare = connecBdd.cx.prepareStatement("UPDATE reservation SET STATUTRESERVATION_RES = ?, ID_EMP = ? WHERE ID_RES = ?");
			
			int idRes = empl.getIdResa();

			prepare.setString(1, "En attente");
			prepare.setNull(2, java.sql.Types.NULL);
			prepare.setInt(3, idRes);
			prepare.executeUpdate();
			
			connecBdd.cx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
