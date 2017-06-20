package com.su.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Yujie
 *
 */
@Entity
@Table(name="userVerify")
public class UserVerifyCode {	
	@Id
	@Column(name="user_name",unique=true)
	private String userName;
	@Column(name="user_vercode")
	private String verfyCode;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getVerfyCode() {
		return verfyCode;
	}
	public void setVerfyCode(String verfyCode) {
		this.verfyCode = verfyCode;
	}
	
}
