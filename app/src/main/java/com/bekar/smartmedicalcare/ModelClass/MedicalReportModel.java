package com.bekar.smartmedicalcare.ModelClass;

import java.io.Serializable;

public class MedicalReportModel implements Serializable {
    private String id;
    private String userId;
    private String drName;
    private String date;
    private String reportPath;

    public MedicalReportModel() {
    }

    public MedicalReportModel(String id, String userId,
                              String drName, String date, String reportPath) {
        this.id = id;
        this.userId = userId;
        this.drName = drName;
        this.date = date;
        this.reportPath = reportPath;
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

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }
}
