package com.su.controller;

import java.util.Date;

import com.su.dao.UserBarHisDao;
import com.su.dao.impl.UserBarHisDaoImpl;
import com.su.models.UserBarHistory;

public class HisToryTest {
	public static void main(String[] args) {
	/*	for(int i=0;i<2;i++){
			UserBarHisDao ubhd =new UserBarHisDaoImpl();
			UserBarHistory userBarHistory=new UserBarHistory();
			userBarHistory.setUserName("wuyujie123");
			//userBarHistory.setRebackTime(new Date());
			userBarHistory.setBorrowTime(new Date());
			ubhd.addBarHis(userBarHistory);
		}*/
		UserBarHisDao ubhd =new UserBarHisDaoImpl();
		UserBarHistory userBarHistory=new UserBarHistory();		
		 UserBarHistory us=	ubhd.findLatestHis("wuyujie123");
		 System.out.println(us.getBorrowTime());

		
	}
}
