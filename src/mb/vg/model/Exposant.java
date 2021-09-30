package mb.vg.model;

public class Exposant {

	private String nom;
	private String prenom;
	private int metre;
	private int idRes;
	private String statut;
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public int getMetre() {
		return metre;
	}
	public void setMetre(int metre) {
		this.metre = metre;
	}
	public int getIdRes() {
		return idRes;
	}
	public void setIdRes(int idRes) {
		this.idRes = idRes;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public Exposant(String nom, String prenom, int metre, int idRes, String statut) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.metre = metre;
		this.idRes = idRes;
		this.statut = statut;
	}
	
	
}
