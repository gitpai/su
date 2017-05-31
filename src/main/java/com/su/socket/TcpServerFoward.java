package com.su.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class TcpServerFoward extends Thread {
	private static int times= 0;
	private static String key;
	private static Map<String,TcpServerFoward>clients;
	private static ServerSocket serverSocket;
	private Socket socket;
	private static OutputStream out;
	private static final int SERVER_PORT=15687;//端口号
	private static final int BUFFER_SIZE = 512;	
	
	
	public TcpServerFoward(Socket socket,String key) {
		this.key=key;
		this.socket=socket;	
	}
	
	
	public  static ServerSocket getServerSocketInstance(){
	ServerSocket	serverSocket = null;
		try {		
			serverSocket = new ServerSocket(SERVER_PORT);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		return serverSocket;
		
		
	}
/*	public static void infoFoward(){
		byte[] receivBuf=new byte[BUFFER_SIZE];  
		int recvMsgSize;
		boolean state=true;
		
	}*/
	@Override
	public void run() {
		System.out.println("times:"+times++);
		byte[] receivBuf=new byte[BUFFER_SIZE];  
		int recvMsgSize;
		
		boolean state=true;
        try {  
            while(true){  
        
                SocketAddress clientAddress = socket.getRemoteSocketAddress();  
                System.out.println("Handling client at "+ clientAddress);                
                InputStream in =socket.getInputStream(); 
                OutputStream out= socket.getOutputStream();     
           
                while((recvMsgSize=in.read(receivBuf))!=-1){  
                
                	String received=new String(receivBuf,0,recvMsgSize);
                	clients=SocketStart.getClients();              	
                	for(Entry<String, TcpServerFoward> vo : clients.entrySet()){ 
                       System.out.println("key-------------"+vo.getKey()); 
                        vo.getValue().send(received,vo.getKey());                       
                    }
                	System.out.println(received);
                }  
                in.close();    
                System.out.println("我这个客户端掉线了");
                SocketStart.remClients(key);               
                return;
                //clientSocket.close();               
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
		// TODO Auto-generated method stub
		
	}  
	public void send(String msg,String key){
		try {
			PrintWriter	pOut = new PrintWriter(socket.getOutputStream(), true); 
			
			pOut.println(msg);			
			//out.close();
			//clientSocket.close();
		} catch (Exception e) {
			SocketStart.remClients(key);
			e.printStackTrace();
			System.out.println("发送失败");
		}
	}

	
	
	
}
