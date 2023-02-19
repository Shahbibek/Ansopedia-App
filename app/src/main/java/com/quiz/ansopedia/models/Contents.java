package com.quiz.ansopedia.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Contents {
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("branch")
    private List<Branch> branch;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Branch> getBranch() {
        return branch;
    }

    public void setBranch(List<Branch> branch) {
        this.branch = branch;
    }
}
