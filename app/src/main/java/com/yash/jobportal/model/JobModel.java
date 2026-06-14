package com.yash.jobportal.model;

public class JobModel {

    private String jobId;
    private String title;
    private String company;
    private String location;
    private String salary;
    private String experience;
    private String skills;
    private String description;

    // Empty constructor required for Firebase
    public JobModel() {
    }

    // Constructor for HomeFragment (4 fields)
    public JobModel(String title,
                    String company,
                    String location,
                    String salary) {

        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
    }

    // Constructor for Firestore (8 fields)
    public JobModel(String jobId,
                    String title,
                    String company,
                    String location,
                    String salary,
                    String experience,
                    String skills,
                    String description) {

        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.experience = experience;
        this.skills = skills;
        this.description = description;
    }

    public String getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getSalary() {
        return salary;
    }

    public String getExperience() {
        return experience;
    }

    public String getSkills() {
        return skills;
    }

    public String getDescription() {
        return description;
    }

    // Setters (Firestore ke liye useful)
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}