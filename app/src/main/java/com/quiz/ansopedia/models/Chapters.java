package com.quiz.ansopedia.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Chapters {
    @SerializedName("chapter_name")
    private String chapter_name;

    @SerializedName("questions")
    private List<Questions> questions;

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
}

