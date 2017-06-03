package com.su.controller;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.dao.UmbrellaDao;
import com.su.dao.impl.UmbrellaDaoImpl;
import com.su.models.Umbrella;
import com.su.socket.SocketStart;
import com.su.socket.TcpServerFoward;
import com.su.util.BitUtils;
import com.su.util.NetResult;

@Controller
public class UmbrellaOperController {
	@RequestMapping(value = "/barUm", method ={ RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody NetResult  brrowUm(Locale locale, Model model,String devUuid,String umId,String operate,String userId) {		
		NetResult r=new NetResult();
		System.out.println(devUuid);
		System.out.println(umId);
		System.out.println(operate);
		System.out.println("接收到请求");	
		UmbrellaDao dao=new	 UmbrellaDaoImpl();
	    Umbrella um=dao.findDeviceByUuid(devUuid);
	    byte[] umSta= um.getUmbrellaSta();
	    
		if(operate.equals("borrow")){ 
			byte borro=0x00;
			if(Integer.parseInt(umId)<9){
				
				umSta[0]=BitUtils.setBitValue(umSta[0],Integer.parseInt(umId)-1,borro);
			
			}else{
				umSta[1]=BitUtils.setBitValue(umSta[1],Integer.parseInt(umId)-9,borro);						}	
				}
		if(operate.equals("reback")){ 
			
			System.out.println("raback");
				byte reback=0x01;
			if(Integer.parseInt(umId)<9){				
				umSta[0]=BitUtils.setBitValue(umSta[0],Integer.parseInt(umId)-1,reback);		
			}else{				
				umSta[1]=BitUtils.setBitValue(umSta[1],Integer.parseInt(umId)-9,reback);						}	
				}
		um.setUmbrellaSta(umSta);
		dao.addDevice(um);	
		 Map<String, Socket> sockets= SocketStart.getSocketClients();
		 Socket socket=sockets.get(devUuid);
		 System.out.println("sockets=========="+sockets);
		 System.out.println("socket============"+socket);
		 send(socket,"DevUuid:"+devUuid+"umid"+umId);
				
		return r;
		}
	@RequestMapping(value = "/umbrella-list", method ={ RequestMethod.GET,RequestMethod.POST})
	public String umbrellaList(Locale locale, Model model,String id,HttpServletRequest  request
			) {
		UmbrellaDaoImpl dao=new UmbrellaDaoImpl();
		Map<Integer, Boolean> umbrellaSta=dao.getUmbrellaSta(id);

		model.addAttribute("umbrellaSta", umbrellaSta);		
		model.addAttribute("uuid", id);
		model.addAttribute("umbrellaNum", umbrellaSta.size());		
		return "umbrella-list";
	
	}
	public static void send(Socket socket,String msg){
		try {
			PrintWriter	pOut = new PrintWriter(socket.getOutputStream(), true); 
			
			pOut.println(msg);			
			//out.close();
			//clientSocket.close();
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("发送失败");
		}
	}
}
