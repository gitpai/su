package com.su.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="userBarHis")
public class UserBarHistory {
	
	@Column(name="user_name")
	private String userName;
	
	@Id
	@Column(name="borrow_time")
	private Date borrowTime; 
	
	@Column(name="reback_time")
	private Date rebackTime;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getBorrowTime() {
		return borrowTime;
	}
	public void setBorrowTime(Date borrowTime) {
		this.borrowTime = borrowTime;
	}
	public Date getRebackTime() {
		return rebackTime;
	}
	public void setRebackTime(Date rebackTime) {
		this.rebackTime = rebackTime;
	} 
}
