package com.su.controller;

import java.util.Date;
import java.util.List;

import com.su.dao.MomentDao;
import com.su.dao.impl.MomentDaoImpl;
import com.su.models.Moments;

public class MomentTest {
	public static void main(String[] args) {
	/*	for(int i=0;i<10;i++){
			Moments moment=new Moments();
			moment.setUserName("wuyujie123");
			moment.setUserSex(true);
			moment.setUserLocal("上海大学翔英学院"+i);
			moment.setUserMoment("共享雨伞很好用"+i);
			moment.setMomentTime(new Date());
			MomentDao mDao=new MomentDaoImpl();
			mDao.addMoment(moment);	
		}*/
		MomentDao mDao=new MomentDaoImpl();
		List<Moments> mm=	mDao.findLatestMoment();
		Moments m=mm.get(0);
		System.out.println(m.getMomentTime());
		
	}
}
