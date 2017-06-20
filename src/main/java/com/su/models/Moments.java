package com.su.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="moments")
public class Moments {

	//@Column (name="id")//userName+userTime进行Md5都得到;
	//private String moment_id;//外键
	@Column(name="user_name")
	private String userName;

	@Column(name="user_moment")
	private String userMoment;
	
	@Column(name="user_sex")
	private boolean userSex;//true man false woman
	@Column(name="user_local")
	private String userLocal;
	
	@Id
	@Column(name="moment_Time")
	private Date momentTime; 
	
	public Date getMomentTime() {
		return momentTime;
	}
	public void setMomentTime(Date momentTime) {
		this.momentTime = momentTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserMoment() {
		return userMoment;
	}
	public void setUserMoment(String userMoment) {
		this.userMoment = userMoment;
	}
	public boolean isUserSex() {
		return userSex;
	}
	public void setUserSex(boolean userSex) {
		this.userSex = userSex;
	}
	public String getUserLocal() {
		return userLocal;
	}
	public void setUserLocal(String userLocal) {
		this.userLocal = userLocal;
	}
}
