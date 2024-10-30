package com.moncefadj.medcare.Medicaments;

public class medDataDb extends medData{
    String datedebut , datefin, instruction ;
    public medDataDb(){

    }
    public medDataDb(String nomMed, String descriptionMed, String tempsMed, String tempsMed2, String tempsMed3, String datedebut , String datefin , String instruction) {
        super(nomMed, descriptionMed, tempsMed, tempsMed2, tempsMed3);
        this.datedebut = datedebut ;
        this.datefin = datefin ;
        this.instruction = instruction;
    }

    public String getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(String datedebut) {
        this.datedebut = datedebut;
    }

    public String getDatefin() {
        return datefin;
    }

    public void setDatefin(String datefin) {
        this.datefin = datefin;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}

