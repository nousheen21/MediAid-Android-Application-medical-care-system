package com.bekar.smartmedicalcare.Activity;

public class MS_Supplier {
    private String supplierId;
    private String supplierName;
    private String supplierCompany;
    private String storeName;

    public MS_Supplier(){
    }

    public MS_Supplier(String supplierId, String supplierName, String supplierCompany, String storeName) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierCompany = supplierCompany;
        this.storeName = storeName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierCompany() {
        return supplierCompany;
    }

    public String getStoreName() {
        return storeName;
    }
}
