package com.bekar.smartmedicalcare.Activity;

public class MS_Store {
    private String storeId;
    private String storeName;
    private String storeAddress;
    private String storePIN;

    public MS_Store() {
    }

    public MS_Store(String storeId, String storeName, String storeAddress, String storePIN) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storePIN = storePIN;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getStorePIN() {
        return storePIN;
    }
}
