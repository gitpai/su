package com.su.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.su.dao.UmbrellaDao;
import com.su.models.Umbrella;
import com.su.util.MySessionFactory;



public class UmbrellaDaoImpl implements UmbrellaDao {

	@Override
	public List<Umbrella> findAllDevice() {
		// TODO Auto-generated method stub
		Session session =MySessionFactory.getInstance().openSession();
	    
		try{
			String hql="from Umbrella";	
			Query query  = session.createQuery(hql);
			List<Umbrella> umbrellas=query.list();
			if(umbrellas.size()==0){
				return null;
			}
			return umbrellas;
		}finally{
			session.close();
		}		
	}

	@Override
	public List<Umbrella> findAllAvlDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public void addDevice(Umbrella umbrella) {
		// TODO Auto-generated method stub
		Session session=MySessionFactory.getInstance().openSession();
		Transaction tx= session.beginTransaction();
		try{
			session.saveOrUpdate(umbrella);
			tx.commit();			
		}catch(RuntimeException e){
			tx.rollback();
			throw e;
		}finally{
			session.close();
		}
		
	}

	@Override
	public boolean getDeviceStatus(String uuid) {
	Session session =MySessionFactory.getInstance().openSession();
	    
		try{
			String hql="from Umbrella where uuid=:uuid";			
			Query query  = session.createQuery(hql);
			query.setString("uuid", uuid);
			@SuppressWarnings("unchecked")
			List<Umbrella> umbrellas=query.list();
			Umbrella umbrella=umbrellas.get(0);
			if(umbrella.isStatus()){
				return true;	
			}else{
				return false;
			}
		
		}finally{
			session.close();
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void openUmbrellaById(String deviceId, String umbrellaId) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Map<Integer, Boolean> getUmbrellaSta(String deviceId) {
		// TODO Auto-generated method stub
		Map<Integer, Boolean> umbrellaSta=new HashMap<Integer, Boolean>();
		Session session =MySessionFactory.getInstance().openSession();	
		try{
	
		String hql="from Umbrella where uuid=:uuid";			
		Query query  = session.createQuery(hql);
		query.setString("uuid", deviceId);
		@SuppressWarnings("unchecked")
		List<Umbrella> umbrellas=query.list();
		Umbrella um=umbrellas.get(0);		
		int key=1;
		for (int i = 0; i < um.getUmbrellaSta().length; i++) {			
        byte date=	um.getUmbrellaSta()[i];       
        byte a = date; 
	        for (int j = 0; j < 8; j++)
	        {
	         byte c=a;
	         a=(byte)(a>>1);
	         a=(byte)(a<<1);
	         if(a==c){
	        	 umbrellaSta.put(key, false);//0代表伞不在了
	        
	         }else{
	        	 umbrellaSta.put(key, true);//1代表伞还在
	        
	         }
	         if(key==10){
	        	 return  umbrellaSta ; 
	         }
	         a=(byte)(a>>1);
	         key++;
	         
	        }       
		}
		return  umbrellaSta ;
		}finally{
			session.close();
		}
	}

	@Override
	public void updateDeviceInfo(String deviceId, Umbrella umbrella) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Umbrella findDeviceByUuid(String deviceId) {
	Session session =MySessionFactory.getInstance().openSession();	    
		try{
			String hql="from Umbrella where uuid=:uuid";			
			Query query  = session.createQuery(hql);
			query.setString("uuid", deviceId);
			@SuppressWarnings("unchecked")
			List<Umbrella> umbrellas=query.list();
			Umbrella umbrella=umbrellas.get(0);			
			return umbrella;		
		}finally{
			session.close();
		}
		// TODO Auto-generated method stub
	}

}
