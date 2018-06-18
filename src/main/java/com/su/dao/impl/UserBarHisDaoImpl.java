package com.su.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.su.dao.UserBarHisDao;
import com.su.models.User;
import com.su.models.UserBarHistory;
import com.su.util.MySessionFactory;

public class UserBarHisDaoImpl implements UserBarHisDao {

	@Override
	public void addBarHis(UserBarHistory userBrHistory) {
		Session session=MySessionFactory.getInstance().openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			session.saveOrUpdate(userBrHistory);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		} finally {
			
			session.close();
					}	
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserBarHistory findLatestHis(String userName) {
		Session session=MySessionFactory.getInstance().openSession();	
		
		try{
		String hql="from UserBarHistory where user_name=:name order by borrow_time desc";	
		// "from CycLock order by time desc";
		Query query= session.createQuery(hql);
		query.setParameter("name", userName);
		@SuppressWarnings("unchecked")
		List <UserBarHistory> userBarHistory=query.list();	
		System.out.println("users.size():"+userBarHistory.size());
		if(userBarHistory.size()==0)
		{			
			
		    return null;	
		}
		else{
			
			return userBarHistory.get(0);	
			
			}
		}finally{		
			session.close();
			}

		}
	}


