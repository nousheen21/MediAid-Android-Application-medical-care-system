package com.example.smartmedicalcare.ModelClass;

public class PatientInformation {
    private String patientId;
    private String fullName;
    private String age;
    private String gender;
    private String nationality;
    private String occupation;
    private String height;
    private String weight;
    private String averagePressure;
    private String contacAddress;

    public PatientInformation() {
    }

    public PatientInformation(String patientId,
                              String fullName,
                              String age,
                              String gender,
                              String nationality,
                              String occupation,
                              String height,
                              String weight,
                              String averagePressure,
                              String contacAddress) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
        this.occupation = occupation;
        this.height = height;
        this.weight = weight;
        this.averagePressure = averagePressure;
        this.contacAddress = contacAddress;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAveragePressure() {
        return averagePressure;
    }

    public void setAveragePressure(String averagePressure) {
        this.averagePressure = averagePressure;
    }

    public String getContacAddress() {
        return contacAddress;
    }

    public void setContacAddress(String contacAddress) {
        this.contacAddress = contacAddress;
    }
}
