package com.quiz.ansopedia.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserDetail{

	@SerializedName("coins")
	private int coins;

	@SerializedName("socialProfiles")
	private List<String> socialProfiles;

	@SerializedName("UserRoles")
	private List<String> userRoles;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("isAccountVerified")
	private boolean isAccountVerified;

	@SerializedName("_id")
	private String id;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("designation")
	private String designation;

	@SerializedName("email")
	private String email;

	public void setCoins(int coins){
		this.coins = coins;
	}

	public int getCoins(){
		return coins;
	}

	public void setSocialProfiles(List<String> socialProfiles){
		this.socialProfiles = socialProfiles;
	}

	public List<String> getSocialProfiles(){
		return socialProfiles;
	}

	public void setUserRoles(List<String> userRoles){
		this.userRoles = userRoles;
	}

	public List<String> getUserRoles(){
		return userRoles;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setIsAccountVerified(boolean isAccountVerified){
		this.isAccountVerified = isAccountVerified;
	}

	public boolean isIsAccountVerified(){
		return isAccountVerified;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}

	public void setDesignation(String designation){
		this.designation = designation;
	}

	public String getDesignation(){
		return designation;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}