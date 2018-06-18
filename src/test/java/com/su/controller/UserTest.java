package com.su.controller;

import java.util.Date;

import com.su.dao.impl.UserDaoImpl;
import com.su.models.User;
import com.su.util.Md5_1;

public class UserTest {
	public static void main(String[] args) {
		UserDaoImpl dao=new UserDaoImpl();
		User user=new User();
		user.setBorrowSta(false);
		user.setNickName("Sybx");
		user.setUserName("18817677902");
		user.setUserSex(true);
		user.setUserAuth(false);
		user.setPassword(Md5_1.GetMD5Code("18817677902"));
		user.setTime(new Date());
		user.setType(1000);
		
		dao.addUser(user);
	}
}
