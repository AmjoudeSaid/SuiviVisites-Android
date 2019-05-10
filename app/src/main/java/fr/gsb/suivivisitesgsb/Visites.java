package fr.gsb.suivivisitesgsb;

import java.util.Date;

public class Visites {

    // Données ne pouvant être modifiées
    private String id, nom, prenom, adresse, tel;

    // Données à saisir lors de la visite
    private Boolean present;
    private Date date;
    private String motif;
    private float niveauconfiance;
    private String lisibilite;
    private String bilan;

    //Gets
    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTel() {
        return tel;
    }

    public Boolean getPresent() {
        return present;
    }

    public Date getDate() {
        return date;
    }

    public String getMotif() {
        return motif;
    }

    public float getNiveauconfiance() {
        return niveauconfiance;
    }

    public String getLisibilite() {
        return lisibilite;
    }

    public String getBilan() {
        return bilan;
    }

    //Sets
    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNiveauconfiance(float niveauconfiance) {
        this.niveauconfiance = niveauconfiance;
    }

    public void setLisibilite(String lisibilite) {
        this.lisibilite = lisibilite;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    //Constructeur vide
    public Visites() {
    }

    //Constructeur
    public Visites(String id, String nom, String prenom, String adresse, String tel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.tel = tel;

        this.present = false;
        this.date = new Date();
        this.motif = "";
        this.niveauconfiance = 0;
        this.lisibilite = "";
        this.bilan = "";
    }

    //Méthode copieVisite
    public void copieVisite(Visites v){
        this.id = v.id;
        this.nom = v.nom;
        this.prenom = v.prenom;
        this.adresse = v.adresse;
        this.tel = v.tel;
        this.present = v.present;
        this.date = v.date;
        this.motif = v.motif;
        this.niveauconfiance = v.niveauconfiance;
        this.lisibilite = v.lisibilite;
        this.bilan = v.bilan;
    }
}


