package com.bekar.smartmedicalcare.Activity;

public class MS_RequestMedicine {
    private String reqMedicineId;
    private String reqMedicineName;
    private String reqCompanyName;
    private String storeName;

    public MS_RequestMedicine(){

    }

    public MS_RequestMedicine(String reqMedicineId, String reqMedicineName, String reqCompanyName, String storeName) {
        this.reqMedicineId = reqMedicineId;
        this.reqMedicineName = reqMedicineName;
        this.reqCompanyName = reqCompanyName;
        this.storeName = storeName;
    }

    public String getReqMedicineId() {
        return reqMedicineId;
    }

    public String getReqMedicineName() {
        return reqMedicineName;
    }

    public String getReqCompanyName() {
        return reqCompanyName;
    }

    public String getStoreName() {
        return storeName;
    }
}
