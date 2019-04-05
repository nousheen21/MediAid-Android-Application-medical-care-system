package com.bekar.smartmedicalcare.ModelClass;

public class DoctorProfileInformationModel {
    private  String id;
    private String doctorId;
    private String fullName;
    private String age;
    private String gender;
    private String nationality;
    private String spciality;
    private String contactMobile;
    private String email;
    private String password;
    private String profileImage;

    public DoctorProfileInformationModel() {
    }

    public DoctorProfileInformationModel( String doctorId,
                                         String fullName, String email,
                                         String password) {
        this.id = id;
        this.doctorId = doctorId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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

    public String getSpciality() {
        return spciality;
    }

    public void setSpciality(String spciality) {
        this.spciality = spciality;
    }

    public String getContact() {
        return contactMobile;
    }

    public void setContact(String contact) {
        this.contactMobile = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
