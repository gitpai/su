package com.su.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.su.dao.UserBarHisDao;
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
		// TODO Auto-generated method stub
		return null;
	}

}
