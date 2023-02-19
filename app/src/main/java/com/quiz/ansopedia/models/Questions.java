package com.quiz.ansopedia.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Questions {
    @SerializedName("question_title")
    private String question_title;

    @SerializedName("options")
    private List<Options> options;

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }
}
