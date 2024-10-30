package com.moncefadj.medcare.DataClasses;

public class SpecialtiesData {
    Integer categoryLogo;
    String  categoryName;


    public SpecialtiesData(Integer categoryLogo, String categoryName ) {
        this.categoryLogo = categoryLogo;
        this.categoryName = categoryName;
    }

    public Integer getCategorieLogo() {
        return categoryLogo;
    }

    public String getCategorieName() {
        return categoryName;
    }
}
