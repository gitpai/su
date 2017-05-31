package com.su.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
	public static int ADMIN =1000;
	public static int NORMAL=100;
	public static String ADMIN_STR="管理员";
	public static String NORMAL_STR="普通成员";
	
	public User() {
		super();
		type=NORMAL;	
	}
	
	public static String get(int type) {
		switch(type) {
		case 100:return NORMAL_STR;
		case 1000:return ADMIN_STR;
		}
		return "";
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "user_id",unique=true)
	private int id;	
	@Column(name="user_name",unique=true)
	private String userName;
	@Column(name="user_type")
	private int type;
	@Column(name="reg_time")
	private Date time; 
	@Column(name="user_pwd")
	private String password;
	@Column(name="borrow_sta")
	private boolean borrowSta;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isBorrowSta() {
		return borrowSta;
	}
	public void setBorrowSta(boolean borrowSta) {
		this.borrowSta = borrowSta;
	}
	

}
