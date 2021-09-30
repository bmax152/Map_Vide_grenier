package mb.vg.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

	static String chaineDeConnection = "jdbc:mysql://localhost:3306/bddvg2020v2";
	static String user = "root";
	static String password = "";
	
	public static Connection connection() {
		
		System.out.println("Chargement du pilote JBDC MySQL et acces à la BDD!");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver Chargé");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.out.println("Driver non chargé car Introuvable");
			return null;
		}
		
		
		try {
			Connection connect = DriverManager.getConnection(chaineDeConnection, user, password);
			System.out.println("Connexion à la bdd établie");
			return connect;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connexion à la Bdd non établie!");
		}
		return null;
		}
}
