package com.su.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.su.dao.UserDao;
import com.su.models.User;
import com.su.models.UserLoginSta;
import com.su.models.UserVerifyCode;
import com.su.util.JdbcUtil;
import com.su.util.Md5_1;
import com.su.util.MySessionFactory;



/**
 * @author Yujie
 *
 */
public class UserDaoImpl implements UserDao {

	@Override
	public void addUser(User user) {		
		
		Session session = MySessionFactory.getInstance().openSession();
		Transaction tx = session.beginTransaction();
		System.out.println("开始存储");
		try {
			session.saveOrUpdate(user);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		} finally {
			
			session.close();
					}				
	}
	@Override
	public User findByName(String userName) {
	Session session=MySessionFactory.getInstance().openSession();	
	
	try{
	String hql="from User where user_name=:name";	
	Query query= session.createQuery(hql);
	query.setParameter("name", userName);
	@SuppressWarnings("unchecked")
	List <User> users=query.list();	
	System.out.println("users.size():"+users.size());
	if(users.size()==0)
	{			
	
	    return null;	
	}
	else{
		
		return users.get(0);	
		
		}
	}finally{		
		session.close();
		}

	}
	@Override
	public void delete(String userName) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement  stmt=null;
		try{
			conn=JdbcUtil.getConnection();
			String sql="DELETE from users where user_Name=?;";
			stmt=(PreparedStatement) conn.prepareStatement(sql);
			stmt.setString(1, userName);				
			stmt.executeUpdate();
			System.out.println("执行删除操作"+userName);			
		}catch(Exception e){
			e.printStackTrace();			
		}finally{
			JdbcUtil.close(conn, stmt);
		}
	}
	@Override
	public List<User> findAllUser() {
		Session session= MySessionFactory.getInstance().openSession();
		try{
		String hql="from User";
		Query query=session.createQuery(hql);
		List <User> users=query.list();	
		// TODO Auto-generated method stub
		return users;
		}finally{
			session.close();	
		}
		
	}
	@Override
	public List<User> findUserUnAuth() {
		Session session= MySessionFactory.getInstance().openSession();
		try{
		String hql="from User where user_auth=false";
		Query query=session.createQuery(hql);
		List <User> users=query.list();	
		// TODO Auto-generated method stub
		return users;
		}finally{
			session.close();	
		}
	}
	@Override
	public void addUserRegCode(UserVerifyCode userVerifyCode) {
		// TODO Auto-generated method stub
		Session session=MySessionFactory.getInstance().openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(userVerifyCode);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		} finally {
			
			session.close();
					}	
	}
	@Override
	public String getUserRegCode(String phoneNum) {
		// TODO Auto-generated method stub
		 Session session=MySessionFactory.getInstance().openSession();
		 String hql="from UserVerifyCode where user_name=:phoneNum";	
		 Query query=  session.createQuery(hql);
		 query.setString("phoneNum", phoneNum);
		 List<UserVerifyCode> users=  query.list();
		 UserVerifyCode userVerifyCode=	users.get(0);		
		 return userVerifyCode.getVerfyCode();
	}
	
	@Override
	public UserLoginSta getUserLoginSta(String userName) {
		// TODO Auto-generated method stub
			Session session =MySessionFactory.getInstance().openSession();
			String hql="from User where user_name=:userName";
			Query query=	session.createQuery(hql);
			query.setString("userName", userName);
		    List<User> users=	query.list();
		    UserLoginSta userLoginSta=new UserLoginSta();
		    userLoginSta.setBorrowSta(users.get(0).isBorrowSta());
		    userLoginSta.setNickName(users.get(0).getNickName());
		    userLoginSta.setUserSex(users.get(0).isUserSex());		
			return userLoginSta;
	}

	



	
}
