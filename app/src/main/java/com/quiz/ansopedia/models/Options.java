package com.quiz.ansopedia.models;

import com.google.gson.annotations.SerializedName;

public class Options{

	@SerializedName("opt_1")
	private String opt1;

	@SerializedName("opt_2")
	private String opt2;

	@SerializedName("opt_3")
	private String opt3;

	@SerializedName("opt_4")
	private String opt4;

	@SerializedName("correct_answer")
	private String correctAnswer;

	@SerializedName("description")
	private String description;

	public void setOpt1(String opt1){
		this.opt1 = opt1;
	}

	public String getOpt1(){
		return opt1;
	}

	public void setOpt2(String opt2){
		this.opt2 = opt2;
	}

	public String getOpt2(){
		return opt2;
	}

	public void setOpt3(String opt3){
		this.opt3 = opt3;
	}

	public String getOpt3(){
		return opt3;
	}

	public void setOpt4(String opt4){
		this.opt4 = opt4;
	}

	public String getOpt4(){
		return opt4;
	}

	public void setCorrectAnswer(String correctAnswer){
		this.correctAnswer = correctAnswer;
	}

	public String getCorrectAnswer(){
		return correctAnswer;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}
}