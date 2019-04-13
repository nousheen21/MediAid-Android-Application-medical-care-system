package com.bekar.smartmedicalcare.Activity;

public class MS_SupplierReg {
    private String supplierId;
    private String supplierName;
    private String supplierCompany;
    private String supplierPIN;

    public MS_SupplierReg(){
    }

    public MS_SupplierReg(String supplierId, String supplierName, String supplierCompany, String supplierPIN) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierCompany = supplierCompany;
        this.supplierPIN = supplierPIN;
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

    public String getSupplierPIN() {
        return supplierPIN;
    }
}
