package mb.vg.model;

import java.io.Serializable;

import javafx.scene.Cursor;
import javafx.scene.control.ToggleButton;
import javafx.scene.transform.Rotate;

public class Emplacement extends ToggleButton implements Serializable {

	private String nom;
	private double x;
	private double y;
	private int metre;
	private String status;
	private String nomResa;
	private String prenomResa;
	private int idButton;
	private int idResa;
	private boolean rotate;
	
	public void place(Emplacement myButton) {
		
		myButton.setText(this.nom);
		myButton.setLayoutX(this.getX());
		myButton.setLayoutY(this.getY());
		myButton.setPrefWidth(30.5*metre);
		myButton.setPrefHeight(58);
		myButton.setCursor(Cursor.HAND);
		if(myButton.rotate == true) {
			myButton.getTransforms().add(new Rotate(270, 0, 0));
		}
	}
	


	public Emplacement(String nom, double x, double y, int metre, String status, String nomResa, String prenomResa, int idButton, int idResa, boolean rotate) {
		super();
		this.nom = nom;
		this.x = x;
		this.y = y;
		this.metre = metre;
		this.status = status;
		this.nomResa = nomResa;
		this.prenomResa = prenomResa;
		this.idButton = idButton;
		this.idResa = idResa;
		this.rotate = rotate;
	}


	public int getIdButton() {
		return idButton;
	}



	public void setIdButton(int idButton) {
		this.idButton = idButton;
	}



	public String getNomResa() {
		return nomResa;
	}



	public void setNomResa(String nomResa) {
		this.nomResa = nomResa;
	}



	public String getPrenomResa() {
		return prenomResa;
	}



	public void setPrenomResa(String prenomResa) {
		this.prenomResa = prenomResa;
	}



	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}



	public int getMetre() {
		return metre;
	}



	public void setMetre(int metre) {
		this.metre = metre;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isRotate() {
		return rotate;
	}

	public void setButtonRotate(boolean rotate) {
		this.rotate = rotate;
	}

	public int getIdResa() {
		return idResa;
	}

	public void setIdResa(int idResa) {
		this.idResa = idResa;
	}



	@Override
	public String toString() {
		return "Emplacement [metre=" + metre + ", nomResa=" + nomResa + ", prenomResa=" + prenomResa + "]";
	}
	
	
}
