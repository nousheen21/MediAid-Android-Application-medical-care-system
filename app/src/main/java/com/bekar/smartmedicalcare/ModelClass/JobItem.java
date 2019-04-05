package com.bekar.smartmedicalcare.ModelClass;

public class JobItem {
    private String id;
    private String jobTitle;
    private String jobDescription;
    private String jobBudget;
    private String jobLocation;
    private String jobClientId;
    private String jobClientName;
    private String jobPostTime;


    public JobItem(String jobTitle, String jobDescription, String jobClientName) {
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobClientName = jobClientName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobBudget() {
        return jobBudget;
    }

    public void setJobBudget(String jobBudget) {
        this.jobBudget = jobBudget;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobClientId() {
        return jobClientId;
    }

    public void setJobClientId(String jobClientId) {
        this.jobClientId = jobClientId;
    }

    public String getJobClientName() {
        return jobClientName;
    }

    public void setJobClientName(String jobClientName) {
        this.jobClientName = jobClientName;
    }

    public String getJobPostTime() {
        return jobPostTime;
    }

    public void setJobPostTime(String jobPostTime) {
        this.jobPostTime = jobPostTime;
    }
}
