package com.su.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.su.dao.MomentDao;
import com.su.models.Comment;
import com.su.models.Moments;
import com.su.models.Umbrella;
import com.su.util.MySessionFactory;

public class MomentDaoImpl implements MomentDao{

	@Override
	public List<Moments> findLatestMoment() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Session session =MySessionFactory.getInstance().openSession();
	    
		try{
			String hql="from Moments order by moment_Time desc";	
			Query query  = session.createQuery(hql);
			List<Moments> moments=query.list();
			if(moments.size()==0){
				return null;
			}else if(moments.size()>=10){
				List<Moments> momentLatest=new ArrayList<>();
				for(int i=0;i<10;i++){	
					momentLatest.add(moments.get(i));
				}
				return momentLatest;
			}else
				return moments;		
		}finally{
			session.close();
		}	
	}

	@Override
	public void addMoment(Moments monment) {
		// TODO Auto-generated method stub
		Session session = MySessionFactory.getInstance().openSession();
		Transaction tx = session.beginTransaction();
		System.out.println("开始存储");
		try {
			session.saveOrUpdate(monment);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		} finally {
			
			session.close();
					}		
	}

	@Override
	public List<Comment> findComments(String uuid) {
		// TODO Auto-generated method stub
		Session session=MySessionFactory.getInstance().openSession();
		String hql="from Comment where uuid=:uuid order by comment_time desc";
		Query query=session.createQuery(hql);
		query.setString("uuid", uuid);
		@SuppressWarnings("unchecked")
		List<Comment> comments=query.list();		
		return comments;
	}

	@Override
	public void addComment(Comment comment) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Session session = MySessionFactory.getInstance().openSession();
		Transaction tx = session.beginTransaction();
		System.out.println("开始存储");
		try {
			session.saveOrUpdate(comment);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		} finally {
			
			session.close();
					}	
		
	}

	
}
