package com.quiz.ansopedia.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Branch {
    @SerializedName("branch_name")
    private String branch_name;

    @SerializedName("subjects")
    private List<Subjects> subjects;

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public List<Subjects> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subjects> subjects) {
        this.subjects = subjects;
    }
}
