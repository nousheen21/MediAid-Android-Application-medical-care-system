package com.bekar.smartmedicalcare.ModelClass;

import android.widget.ScrollView;

import java.io.Serializable;

public class DoctorOfficeModel implements Serializable {
    private String id;
    private String userId;
    private String officeName;
    private String officeLocation;
    private String officeDescription;
    private String officeContactInfo;
    private String officeTime;
    private String officeDays;
    private boolean appointmentStatus;

    public DoctorOfficeModel() {
    }

    public DoctorOfficeModel(String id, String userId, String officeName,
                             String officeLocation, String officeContactInfo,String officeDescription, String officeTime,
                             String officeDays) {
        this.id = id;
        this.userId = userId;
        this.officeName = officeName;
        this.officeLocation = officeLocation;
        this.officeDescription = officeDescription;
        this.officeContactInfo=officeContactInfo;
        this.officeTime = officeTime;
        this.officeDays = officeDays;
        this.appointmentStatus=false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getOfficeTime() {
        return officeTime;
    }

    public void setOfficeTime(String officeTime) {
        this.officeTime = officeTime;
    }

    public String getOfficeDays() {
        return officeDays;
    }

    public void setOfficeDays(String officeDays) {
        this.officeDays = officeDays;
    }

    public String getOfficeDescription() {
        return officeDescription;
    }

    public void setOfficeDescription(String officeDescription) {
        this.officeDescription = officeDescription;
    }

    public boolean isAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(boolean appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getOfficeContactInfo() {
        return officeContactInfo;
    }

    public void setOfficeContactInfo(String officeContactInfo) {
        this.officeContactInfo = officeContactInfo;
    }
}
