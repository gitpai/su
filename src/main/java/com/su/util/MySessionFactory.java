package com.su.util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

@SuppressWarnings("deprecation")
public class MySessionFactory {

	private  SessionFactory sessionFactory;
	private static MySessionFactory Instance=null;
	
	private MySessionFactory()
	{
		Configuration cfg = new Configuration().configure(); 
		sessionFactory = cfg.buildSessionFactory();
	}
	public static MySessionFactory getInstance()
	{
		if(Instance==null)
		{
			Instance =new MySessionFactory();
		}
		return Instance;
	}
	
	public Session openSession()
	{
		return sessionFactory.openSession();
	}
}
