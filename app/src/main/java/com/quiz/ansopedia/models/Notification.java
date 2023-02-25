package com.quiz.ansopedia.models;

import com.google.gson.annotations.SerializedName;

public class Notification{

	@SerializedName("scope")
	private String scope;

	@SerializedName("_id")
	private String id;

	@SerializedName("time")
	private String time;

	@SerializedName("title")
	private String title;

	@SerializedName("message")
	private String message;

	public void setScope(String scope){
		this.scope = scope;
	}

	public String getScope(){
		return scope;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}