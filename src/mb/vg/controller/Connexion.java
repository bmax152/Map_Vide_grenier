package mb.vg.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

	static String chaineDeConnection = "jdbc:mysql://localhost:3306/bddvg2020v2";
	static String user = "root";
	static String password = "";
	
	public static Connection connection() {
		
		System.out.println("Chargement du pilote JBDC MySQL et acces � la BDD!");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver Charg�");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.out.println("Driver non charg� car Introuvable");
			return null;
		}
		
		
		try {
			Connection connect = DriverManager.getConnection(chaineDeConnection, user, password);
			System.out.println("Connexion � la bdd �tablie");
			return connect;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connexion � la Bdd non �tablie!");
		}
		return null;
		}
}
