package com.su.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.su.dao.MomentDao;
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
			String hql="from Moments";	
			Query query  = session.createQuery(hql);
			List<Moments> moments=query.list();
			if(moments.size()==0){
				return null;
			}
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
		System.out.println("¿ªÊ¼´æ´¢");
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

	
}
