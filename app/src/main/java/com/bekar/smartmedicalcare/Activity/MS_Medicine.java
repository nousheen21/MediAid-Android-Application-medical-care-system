package com.bekar.smartmedicalcare.Activity;

public class MS_Medicine {
    private String medicineId;
    private String medicineName;
    private String companyName;
    private String medicinePrice;
    private String storeName;
    private String storeAddress;
    private String available;

    public MS_Medicine(){

    }

    public MS_Medicine(String medicineId, String medicineName, String companyName,
                       String medicinePrice, String storeName, String storeAddress, String available) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.companyName = companyName;
        this.medicinePrice = medicinePrice;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.available = available;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getMedicinePrice() {
        return medicinePrice;
    }

    public String getStoreName() { return storeName; }

    public String getStoreAddress() { return storeAddress; }

    public String getAvailable() {
        return available;
    }
}
