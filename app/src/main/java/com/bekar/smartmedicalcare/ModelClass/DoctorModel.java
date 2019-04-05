package com.bekar.smartmedicalcare.ModelClass;

public class DoctorModel {
    private String id;
    private String userId;
    private String fullName;
    private String gender;
    private String type;
    private String mobile;
    private String speciality;
    private String profilePic;
    private boolean isActive;

    public DoctorModel() {
    }

    public DoctorModel( String userId, String fullName,
                       String type, String mobile,
                       String speciality, String profilePic) {
        this.userId=userId;
        this.fullName = fullName;
        this.type = type;
        this.mobile = mobile;
        this.speciality = speciality;
        this.profilePic=profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
