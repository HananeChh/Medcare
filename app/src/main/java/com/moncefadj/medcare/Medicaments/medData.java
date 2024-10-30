package com.moncefadj.medcare.Medicaments;

public class medData {
    private String nomMed ;
    private String descriptionMed ;
    private String tempsMed ,tempsMed2 , tempsMed3;

    public medData(){

    }

    public medData(String nomMed,String descriptionMed,String tempsMed,String tempsMed2,String tempsMed3) {
        this.nomMed = nomMed;
        this.descriptionMed = descriptionMed;
        this.tempsMed = tempsMed;
        this.tempsMed2 = tempsMed2;
        this.tempsMed3 = tempsMed3;

    }

    public String getNomMed() {
        return nomMed;
    }

    public void setNomMed(String nomMed) {
        this.nomMed = nomMed;
    }

    public String getDescriptionMed() {
        return descriptionMed;
    }

    public void setDescriptionMed(String descriptionMed) {
        this.descriptionMed = descriptionMed;
    }


    public String getTempsMed() {
        return tempsMed;
    }

    public void setTempsMed(String tempsMed) {
        this.tempsMed = tempsMed;
    }

    public String getTempsMed2() {
        return tempsMed2;
    }

    public void setTempsMed2(String tempsMed2) {
        this.tempsMed2 = tempsMed2;
    }

    public void setTempsMed3(String tempsMed3) {
        this.tempsMed3 = tempsMed3;
    }

    public String getTempsMed3() {
        return tempsMed3;
    }

}
