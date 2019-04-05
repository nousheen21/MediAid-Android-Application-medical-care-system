package com.bekar.smartmedicalcare.ModelClass;


import java.io.Serializable;

public class JobApplyModel implements Serializable {
    private String id;
    private String jobPosterId;
    private String jobId;
    private String userId;
    private String userName;
    private int budget;
    private String comment;
    private String cvUrl;

    private boolean isAccepted;

    public JobApplyModel() {
    }

    public JobApplyModel(String id, String userId,
                         String userName, int budget,
                         String comment, String cvUrl) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.budget = budget;
        this.comment = comment;
        this.cvUrl = cvUrl;
        this.isAccepted=false;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getJobPosterId() {
        return jobPosterId;
    }

    public void setJobPosterId(String jobPosterId) {
        this.jobPosterId = jobPosterId;

    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
