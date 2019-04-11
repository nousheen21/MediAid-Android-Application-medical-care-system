package com.bekar.smartmedicalcare.Activity;

public class HM_Category {
    private String categoryId;
    private String categoryName;
    private String hospitalName;

    public HM_Category(){

    }

    public HM_Category(String categoryId, String categoryName, String hospitalName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.hospitalName = hospitalName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getHospitalName() {
        return hospitalName;
    }
}
