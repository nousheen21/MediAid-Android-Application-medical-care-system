package com.bekar.smartmedicalcare.ModelClass;

import java.io.Serializable;

public class JobCreateModel implements Serializable {
    private String jobPosterId;
    private String jobPosterType;
    private String jobId;
    private String jobPostName;
    private String jobTitle;
    private String jobDiscription;
    private String jobAddress;
    private int jobBudget;
    private long jobCreateDate;
    private boolean jobStatus;

    public JobCreateModel() {
    }

    public JobCreateModel(String id,
                          String jobPostName, String jobTitle,
                          String jobDiscription,
                          String jobAddress, int jobBudget,
                          long jobCreateDate) {
        this.jobPosterId = id;
        this.jobPostName = jobPostName;
        this.jobTitle = jobTitle;
        this.jobDiscription = jobDiscription;
        this.jobAddress = jobAddress;
        this.jobBudget = jobBudget;
        this.jobCreateDate = jobCreateDate;

        this.jobStatus=true;
    }

    public String getId() {
        return jobPosterId;
    }

    public void setId(String id) {
        this.jobPosterId = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobPostName() {
        return jobPostName;
    }

    public void setJobPostName(String jobPostName) {
        this.jobPostName = jobPostName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDiscription() {
        return jobDiscription;
    }

    public void setJobDiscription(String jobDiscription) {
        this.jobDiscription = jobDiscription;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public int getJobBudget() {
        return jobBudget;
    }

    public void setJobBudget(int jobBudget) {
        this.jobBudget = jobBudget;
    }

    public long getJobCreateDate() {
        return jobCreateDate;
    }

    public void setJobCreateDate(long jobCreateDate) {
        this.jobCreateDate = jobCreateDate;
    }

    public String getJobPosterId() {
        return jobPosterId;
    }

    public void setJobPosterId(String jobPosterId) {
        this.jobPosterId = jobPosterId;
    }

    public boolean isJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(boolean jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobPosterType() {
        return jobPosterType;
    }

    public void setJobPosterType(String jobPosterType) {
        this.jobPosterType = jobPosterType;
    }
}
