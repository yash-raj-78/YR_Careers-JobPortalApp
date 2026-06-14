package com.yash.jobportal.model;

public class ApplicationModel {

    private String userId;
    private String jobId;
    private String status;

    public ApplicationModel() {
    }

    public ApplicationModel(String userId,
                            String jobId,
                            String status) {

        this.userId = userId;
        this.jobId = jobId;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }
}