package com.su.controller;

import java.util.Date;
import java.util.List;

import com.su.dao.MomentDao;
import com.su.dao.impl.MomentDaoImpl;
import com.su.models.Moments;
import com.su.util.Md5_1;

public class MomentTest {
	public static void main(String[] args) throws InterruptedException {
		for(int i=10;i<15;i++){
			Moments moment=new Moments();
			Date time=new Date();
			System.out.println("wuyujie123"+time.toString());
			moment.setUuid(Md5_1.GetMD5Code("wuyujie123"+time.toString()));
			moment.setUserName("wuyujie123");
			moment.setUserSex(true);
			moment.setUserLocal("上海大学翔英学院"+i);
			moment.setUserMoment("共享雨伞很好用"+i);
			moment.setMomentTime(time);			
			MomentDao mDao=new MomentDaoImpl();
			Thread.currentThread().sleep(2000);
			mDao.addMoment(moment);	
		}
		/*MomentDao mDao=new MomentDaoImpl();
		List<Moments> mm=	mDao.findLatestMoment();
		Moments m=mm.get(0);
		System.out.println(m.getMomentTime());*/
		
	}
}
