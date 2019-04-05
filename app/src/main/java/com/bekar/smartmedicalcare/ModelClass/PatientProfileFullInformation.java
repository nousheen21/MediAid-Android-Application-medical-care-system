package com.bekar.smartmedicalcare.ModelClass;

public class PatientProfileFullInformation {
    private String id;
    private String userId;
    private String fullName;
    private String type;
    private String gender;
    private String age;
    private String occupation;
    private String nationality;
    private String height;
    private String weight;
    private String averagePresure;
    private String bloodGroup;
    private String address;
    private String email;
    private String password;
    private String mobileNo;
    private String profilePic;

    public PatientProfileFullInformation() {
    }

    public PatientProfileFullInformation(String userId, String fullName, String type) {
        this.userId = userId;
        this.fullName = fullName;
        this.type = type;
    }
    public PatientProfileFullInformation(String id, String fullName, String type,
                                         String email, String password) {
        this.id = id;
        this.fullName = fullName;
        this.type = type;
        this.email = email;
        this.password = password;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public String getAveragePresure() {
        return averagePresure;
    }

    public void setAveragePresure(String averagePresure) {
        this.averagePresure = averagePresure;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
