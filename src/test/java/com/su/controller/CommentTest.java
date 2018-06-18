package com.su.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.su.dao.MomentDao;
import com.su.dao.impl.MomentDaoImpl;
import com.su.models.Comment;
import com.su.models.Moments;

public class CommentTest {
	public static void main(String[] args) throws InterruptedException {
		MomentDao mDao=new MomentDaoImpl();
		List<Moments> moments=	mDao.findLatestMoment();
		for (int i = 0; i < moments.size(); i++) {
		System.out.println(moments.get(i).getUuid());		
		}
		
		/*System.out.println( moments.get(3).getUuid());
		System.out.println( moments.get(4).getUuid());
		System.out.println(mDao.findComments(moments.get(4).getUuid()).size());	*/
		/*	for(int i=14;i<25;i++){
			MomentDao mDao=new MomentDaoImpl();
			Comment comment=new Comment();
			comment.setUuid("59bab35453fab61594de083c42db2dc9");
			comment.setComment("天气很好啊");
			comment.setUserName("兀玉洁");
			comment.setCommentTime(new Date());
			Thread.currentThread().sleep(1000);
			mDao.addComment(comment);
			
		}*/
	}
}
