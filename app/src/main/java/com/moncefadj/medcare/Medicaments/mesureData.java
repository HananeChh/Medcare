package com.moncefadj.medcare.Medicaments;

public class mesureData {
    private String nomMesure , time ;
    private String valeur ;

    public mesureData(String nomMesure, String time, String valeur) {
        this.nomMesure = nomMesure;
        this.time = time;
        this.valeur = valeur;
    }
    public mesureData(){

    }

    public String getNomMesure() {
        return nomMesure;
    }

    public void setNomMesure(String nomMesure) {
        this.nomMesure = nomMesure;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}
