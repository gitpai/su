package com.su.models;

public class UserLoginSta {
	
	private String nickName;
	private boolean userSex;
	private boolean borrowSta;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public boolean isUserSex() {
		return userSex;
	}
	public void setUserSex(boolean userSex) {
		this.userSex = userSex;
	}
	public boolean isBorrowSta() {
		return borrowSta;
	}
	public void setBorrowSta(boolean borrowSta) {
		this.borrowSta = borrowSta;
	}
	
}
