package com.su.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.mysql.fabric.xmlrpc.base.Array;
import com.su.dao.UmbrellaDao;
import com.su.dao.impl.UmbrellaDaoImpl;
import com.su.models.Umbrella;

/**
 * @author Yujie
 *
 */
public class TcpServerFoward extends Thread {
	private static int times= 0;
	private static String key;
	private static Map<String,TcpServerFoward>clients;
	private static ServerSocket serverSocket;
	private Socket socket;
	private static OutputStream out;
	private static final int SERVER_PORT=8989;//端口号
	private static final int BUFFER_SIZE = 512;	
	
	
	public TcpServerFoward(Socket socket) {
		
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
                String devUuid = null;
                UmbrellaDao umbrellaDao =new UmbrellaDaoImpl();
                Umbrella um=null;
                while((recvMsgSize=in.read(receivBuf))!=-1){   
                	byte[] revData;
                	revData=Arrays.copyOfRange(receivBuf, 0, 19);
                	String recDataStr=byteToString(revData);
                	System.out.println(recDataStr);
                	
                	if(receivBuf[0]==0x01&&receivBuf[1]==0x01&&receivBuf[2]==0x23){
                			
                			byte[] uuid;
                			uuid=Arrays.copyOfRange(receivBuf, 3, 19);
                			devUuid=byteToString(uuid);
                			byte[] umSta;
                			umSta=Arrays.copyOfRange(receivBuf, 19, 21);                			
                			um=umbrellaDao.findDeviceByUuid(devUuid);
                			um.setUmbrellaSta(umSta);               			             			
                			 Map<String, Socket> socketMap=SocketStart.getSocketClients();
                			 synchronized(socketMap){
                				 System.out.println(socketMap);
                				 System.out.println(socketMap.hashCode());
                			 if(!socketMap.containsKey(devUuid)){
                				 socketMap.put(devUuid, socket);
                				 um.setStatus(true);
                				 System.out.println("集合未包含当前Socket");
                			 	}else{
                			 		System.out.println("集合已包含当前Socket");
                			 	}  
                				umbrellaDao.addDevice(um);  
                			 }
                		}
                	}
                	//String received=new String(receivBuf,0,recvMsgSize);
                /*	clients=SocketStart.getClients();              	
                	for(Entry<String, TcpServerFoward> vo : clients.entrySet()){ 
                       System.out.println("key-------------"+vo.getKey()); 
                        vo.getValue().send(received,vo.getKey());                       
                    }*/ //实现数据转发
                	//System.out.println(received);
                 
                in.close();    
                
                System.out.println("我这个客户端掉线了");
                um.setStatus(false);
              
                SocketStart.remClients(devUuid);   
                umbrellaDao.addDevice(um);  
                return;
                //clientSocket.close();               
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
		// TODO Auto-generated method stub
		
	}  
	public  static byte[] stringToByte(String uuid){
		
		char [] dataChar=uuid.toCharArray();
		byte [] dataByte=new byte[dataChar.length/2];
	//	System.out.println(dataChar.length/2);
		for(int i=0;i<dataChar.length;i+=2){			
			byte high,low;//临时变量，保存ch的高4位、低四位
				//保存转换后的字符，	
			if(dataChar[i]>'9'){	
				high=(byte) (dataChar[i]-'a');
				high=(byte) (high+0x0a);
				System.out.println(high);
			}else{
				dataChar[i]=(char) (dataChar[i]-'0');
				high=(byte) (dataChar[i]);
				}
				
			if(dataChar[i+1]>'9'){	
					low=(byte) (dataChar[i+1]-'a');
					low=(byte) (low+0x0a);	
					System.out.println(low);
			}else{
					dataChar[i+1]=(char) (dataChar[i+1]-'0');
					low=(byte) (dataChar[i+1]);	
				
			}					
			dataByte[(i+1)/2]=(byte) ((high<<4)+low);	
			//System.out.println(i);
		}	
		return  dataByte;
	}
	public  static String byteToString(byte[] uuid){
		char [] datatxt=new char[uuid.length*2];
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<uuid.length;i++){			
			byte high,low;//临时变量，保存ch的高4位、低四位
				//保存转换后的字符，			
			high=(byte) ((0x0f)&(uuid[i]>>4));
			low=(byte) ((0x0f)&uuid[i]);
			//高4位保存在datatxt[0]中
			if(high<=(0x09))
				datatxt[i]=(char) (high+'0');
			else
				datatxt[i]=(char) (high-0x0a+'a');
			//System.out.print(datatxt[i]);
			sb.append(datatxt[i]);
			//低4位保存在datatxt[1]中
			if(low<=(0x09))
				datatxt[i+1]=(char) (low+'0');
			else
				datatxt[i+1]=(char) (low-0x0a+'a');
			sb.append(datatxt[i+1]);
		}
		/*for(int i=0;i<datatxt.length;i++){
			System.out.print(datatxt[i]);
		}*/
		return sb.toString();
		
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
