package com.bekar.smartmedicalcare.Activity;

public class HM_Hospital {
    private String hospitalId;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPIN;

    public HM_Hospital() {
    }

    public HM_Hospital(String hospitalId, String hospitalName, String hospitalAddress, String hospitalPIN) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.hospitalPIN = hospitalPIN;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public String getHospitalPIN() {
        return hospitalPIN;
    }
}
