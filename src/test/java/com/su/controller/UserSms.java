package com.su.controller;

import java.util.Random;

import com.su.dao.UserDao;
import com.su.dao.impl.UserDaoImpl;
import com.su.models.UserVerifyCode;

public class UserSms {
	public static void main(String[] args) {
	/*	UserVerifyCode userVerifyCode=new UserVerifyCode();
		userVerifyCode.setUserName("wuyujie123");
		userVerifyCode.setVerfyCode("2333");
		UserDao dao=new UserDaoImpl();
		dao.addUserRegCode(userVerifyCode);
	System.out.println(dao.getUserRegCode("wuyujie123"));*/
	String smsCode="";
	for(int i=0;i<4;i++){
	smsCode+=new Random().nextInt(10)+"";
	}
	System.out.println(smsCode);
	}
}
