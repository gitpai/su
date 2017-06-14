package com.su.dao.impl;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.su.dao.UmbrellaDao;
import com.su.models.Umbrella;
import com.su.models.UmbrellaNear;
import com.su.util.GPSUtil;
import com.su.util.LocationUtil;
import com.su.util.MySessionFactory;



/**
 * @author Yujie
 *
 */
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
	         if(key==12){
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
			if(umbrellas.size()!=0){
				Umbrella umbrella=umbrellas.get(0);		
				return umbrella;	
			}else{
				return null;
			}		
				
		}finally{
			session.close();
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteDevice(String uuid) {
		Session session =MySessionFactory.getInstance().openSession();
		try{
		String hql="delete from Umbrella where uuid=:uuid";
		Transaction tx= session.beginTransaction();
		Query query= session.createQuery(hql);
		query.setString("uuid", uuid);
		query.executeUpdate();
		tx.commit();
		}finally{
			session.close();
		}
		
		
	}

	@Override
	public void openUmbrellaById(Socket socket, byte[] operate) {
		
		//PrintWriter	pOut=null;
		OutputStream outPutStream=null;
		
		try {
			//outPutStream=socket.getOutputStream();	
			outPutStream.write(operate);
			//outPutStream.close();
			//out.close();
			//clientSocket.close();
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("发送失败");
		}finally{
			
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UmbrellaNear> findNearDevice(double lon, double lat) {
		double[] gps1=GPSUtil.gcj02_To_Bd09(lat, lon);
		// TODO Auto-generated method stub
		List<Umbrella> allUmbrella = findAllDevice();
		List<UmbrellaNear> allNearUmbrella = new ArrayList<>();
		for (int i = 0; i < allUmbrella.size(); i++) {
			Umbrella um = allUmbrella.get(i);
			/*System.out.println(um.isStatus()+"=====status");
			System.out.println(LocationUtil.getDistance(lat, lon, um.getDevice_lat(), um.getDevice_lon()));*/
			//System.out.println(lat+" "+lon+" "+um.getDevice_lat()+" "+um.getDevice_lon());
			if (um.isStatus()) {
				if (LocationUtil.getDistance(gps1[0], gps1[1],um.getDevice_lat(), um.getDevice_lon() ) < 2000) {
					UmbrellaNear umNear=new UmbrellaNear();
					umNear.setDeviceId(um.getDevice_uuid());
					double [] gps=	GPSUtil.bd09_To_Gcj02(um.getDevice_lat(), um.getDevice_lon());
					DecimalFormat df = new DecimalFormat( "0.000000 ");   					
					umNear.setDevice_lon(Double.parseDouble(df.format(gps[0])) );
					umNear.setDevice_lat(Double.parseDouble(df.format(gps[1])));
					allNearUmbrella.add(umNear);
					System.out.println("添加了一个");
				}

				// System.out.println(list.get(i));
			}

		}
		return allNearUmbrella;
	}
}
