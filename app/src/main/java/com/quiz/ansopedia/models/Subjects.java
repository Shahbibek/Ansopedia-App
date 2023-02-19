package com.quiz.ansopedia.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Subjects {
    @SerializedName("subject_name")
    private String subject_name;

    @SerializedName("description")
    private String description;

    @SerializedName("color")
    private String color;

    @SerializedName("image")
    private String image;

    @SerializedName("chapters")
    private List<Chapters> chapters;

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Chapters> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapters> chapters) {
        this.chapters = chapters;
    }
}
