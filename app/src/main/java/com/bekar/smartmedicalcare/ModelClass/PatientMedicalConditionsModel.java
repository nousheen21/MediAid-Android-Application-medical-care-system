package com.bekar.smartmedicalcare.ModelClass;

public class PatientMedicalConditionsModel {
    String patientId;
    private String id;
    private String title;
    private String discription;
    private String type;

    public PatientMedicalConditionsModel() {
    }

    public PatientMedicalConditionsModel(String patientId, String id,
                                         String title, String discription) {
        this.patientId = patientId;
        this.id = id;
        this.title = title;
        this.discription = discription;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
