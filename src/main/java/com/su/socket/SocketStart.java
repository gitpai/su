package com.su.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;



/**
 * 创建一个多线程的Servlet
 */

public class SocketStart  extends HttpServlet implements Runnable {
	private static Map<String,TcpServerFoward>clients=new HashMap<String,TcpServerFoward>();
	private static Map<String,Socket>socketClients=new HashMap<String,Socket>();
	private static int key=0;
	public static Map<String,TcpServerFoward>getClients() {
		return clients;
	}



	public static void setClients(HashMap<String,TcpServerFoward> clients) {
		SocketStart.clients = clients;
	}

	public static void remClients(String key) {
		
		clients.remove(key);
		System.out.println("然后现在还有");
		for(Entry<String, TcpServerFoward> vo : clients.entrySet()){ 
            System.out.println("key-------------"+vo.getKey());                                
         }
	}

	public static void remClients(TcpServerFoward tf) {
		
		clients.remove(tf);
		
	}

	@Override
	public void init() throws ServletException {
		SocketStart mySocket = new SocketStart();		
		Thread t1 = new Thread(mySocket);
		t1.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket=TcpServerFoward.getServerSocketInstance();
		System.out.println("已创建Tcp对象");
		//TcpServerFoward server = TcpServerFoward.getTCPServerThreadInstance();
		while(true){
			
			Socket clientSocket; 
			try {
				Thread.sleep(500);
				
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				 
				 clientSocket= serverSocket.accept();
				 
				 
				 System.out.println("获取客户端");
				 
				 TcpServerFoward tf= new TcpServerFoward(clientSocket,key+"");
				 //socketClients.put(key+"", clientSocket);
				 clients.put(key+"",tf);
				 key++;
				 tf.start();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
	}

	

}
