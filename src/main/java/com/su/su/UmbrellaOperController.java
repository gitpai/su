package com.su.su;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.dao.UmbrellaDao;
import com.su.dao.UserDao;
import com.su.dao.impl.UmbrellaDaoImpl;
import com.su.dao.impl.UserDaoImpl;
import com.su.models.NetResult;
import com.su.models.Umbrella;
import com.su.models.User;
import com.su.socket.SocketStart;
import com.su.socket.TcpServerFoward;
import com.su.util.BitUtils;
import com.su.util.Md5_1;

/**
 * @author Yujie
 *
 */
@Controller
public class UmbrellaOperController {
	
	/*
	 * 手机端借还伞接口
	 */
	@RequestMapping(value = "/barUmUser", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult brrowUm(Locale locale, Model model, String devUuid, String umId, String operate,
			String userName) {
		NetResult r = new NetResult();
		System.out.println(devUuid);
		System.out.println(umId);
		System.out.println(operate);
		System.out.println("接收到请求");

		Map<String, Socket> sockets = SocketStart.getSocketClients();// 获取当前Socket列表
		Socket socket = sockets.get(devUuid);
		//send(socket, "DevUuid:" + devUuid + "umid" + umId);
		byte[] operateResult = new byte[50];
		try {
			InputStream socketInput = socket.getInputStream();
			while (socketInput.read(operateResult) != -1) {
				byte[] revData;
            	revData=Arrays.copyOfRange(operateResult, 0, 19);
            	String recDataStr= TcpServerFoward.byteToString(revData);
            	System.out.println("Controller====="+recDataStr);
				if (operateResult[0] == 0x01 && operateResult[1] == 0x01 && operateResult[2] == 0x00) {

					byte[] uuid;
					uuid = Arrays.copyOfRange(operateResult, 3, 19);
					devUuid = TcpServerFoward.byteToString(uuid);
					byte operSta = operateResult[19];
					if (operSta == 0x01) {
						UmbrellaDao dao = new UmbrellaDaoImpl();
						UserDao userDao = new UserDaoImpl();
						User user = userDao.findByName(userName); // 寻找当前开伞用户
						Umbrella um = dao.findDeviceByUuid(devUuid); // 寻找用户开伞设备
						byte[] umSta = um.getUmbrellaSta();

						if (operate.equals("borrow")) {
							byte borro = 0x00;
							if (Integer.parseInt(umId) < 9) {

								umSta[0] = BitUtils.setBitValue(umSta[0], Integer.parseInt(umId) - 1, borro);

							} else {
								umSta[1] = BitUtils.setBitValue(umSta[1], Integer.parseInt(umId) - 9, borro);
							}
						}
						if (operate.equals("reback")) {

							System.out.println("raback");
							byte reback = 0x01;
							if (Integer.parseInt(umId) < 9) {
								umSta[0] = BitUtils.setBitValue(umSta[0], Integer.parseInt(umId) - 1, reback);
							} else {
								umSta[1] = BitUtils.setBitValue(umSta[1], Integer.parseInt(umId) - 9, reback);
							}
						}
						um.setUmbrellaSta(umSta);
						dao.addDevice(um); // 更新雨伞状态
						user.setBorrowSta(false);
						userDao.addUser(user);
						r.setStatus(1);

					} else if (operSta == 0x00) {
						r.setStatus(0);
					}
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("sockets==========" + sockets);
		System.out.println("socket============" + socket);

		return r;
	}
	/*
	 * 后台超管借还伞接口
	 */
/*	@RequestMapping(value = "/barUmAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult adminBarOper(Locale locale, Model model, String devUuid, String umId, String operate,
			HttpSession session
	) {
		
		NetResult r = new NetResult();
		System.out.println(devUuid);
		System.out.println(umId);
		System.out.println(operate);
		System.out.println("接收到请求");
	
		Map<String, Socket> sockets = SocketStart.getSocketClients();// 获取当前Socket列表
		Socket socket = sockets.get(devUuid);
		byte[] uuidByte=TcpServerFoward.stringToByte(devUuid);
		byte[] umOperate=new byte[22];
		umOperate [0]=0x01;
		umOperate [1]=0x01;
		umOperate [2]=0x02;
		for(int i=0;i<uuidByte.length;i++){
			umOperate [i+3]=uuidByte[i];	
		}
		umOperate [19]=0x16;
		umOperate [20]=(byte) Integer.parseInt(umId); 		
		if(operate.equals("borrow")){			
			umOperate [21]=0x00;			
		}else if(operate.equals("reback")){
			umOperate [21]=0x01;
		}		
		send(socket, umOperate);
		byte[] operateResult = new byte[50];
		try {
			InputStream socketInput = socket.getInputStream();
			System.out.println("while前");
			while (socketInput.read(operateResult) != -1) {
				System.out.println("开始接收信息");  
				byte[] revData;
            	revData=Arrays.copyOfRange(operateResult, 0, 19);
            	String recDataStr= TcpServerFoward.byteToString(revData);
            	System.out.println("Controller====="+recDataStr);
				if (operateResult[0] == 0x01 && operateResult[1] == 0x01 && operateResult[2] == 0x00) {					
					byte[] uuid;
					uuid = Arrays.copyOfRange(operateResult, 3, 19);
					devUuid = TcpServerFoward.byteToString(uuid);
					byte operSta = operateResult[20];
					if (operSta == 0x01) {
						System.out.println("操作成功");
						UmbrellaDao dao = new UmbrellaDaoImpl();
						UserDao userDao = new UserDaoImpl();
						User user = userDao.findByName((String)session.getAttribute("admin")); // 寻找当前开伞用户
						Umbrella um = dao.findDeviceByUuid(devUuid); // 寻找用户开伞设备
						byte[] umSta = um.getUmbrellaSta();

						if (operate.equals("borrow")) {
							byte borro = 0x00;
							if (Integer.parseInt(umId) < 9) {

								umSta[0] = BitUtils.setBitValue(umSta[0], Integer.parseInt(umId) - 1, borro);

							} else {
								umSta[1] = BitUtils.setBitValue(umSta[1], Integer.parseInt(umId) - 9, borro);
							}
						}
						if (operate.equals("reback")) {

							System.out.println("reback");
							byte reback = 0x01;
							if (Integer.parseInt(umId) < 9) {
								umSta[0] = BitUtils.setBitValue(umSta[0], Integer.parseInt(umId) - 1, reback);
							} else {
								umSta[1] = BitUtils.setBitValue(umSta[1], Integer.parseInt(umId) - 9, reback);
							}
						}
						um.setUmbrellaSta(umSta);
						dao.addDevice(um); // 更新雨伞状态
						user.setBorrowSta(false);
						userDao.addUser(user);
						r.setStatus(1);
					} else if (operSta == 0x00) {
						r.setStatus(0);
					}
					
					break;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("退出循环");
		System.out.println("sockets==========" + sockets);
		System.out.println("socket============" + socket);
		return r;
	}	*/
	
	@RequestMapping(value = "/barUmAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult adminBarOper(Locale locale, Model model, String devUuid, String umId, String operate,String admin,
			HttpSession session
	) {
		System.out.println("devUuid"+devUuid+"umId"+umId+"operate"+operate);
		NetResult r = new NetResult();		
		Map<String, Socket> sockets = SocketStart.getSocketClients();// 获取当前Socket列表
		Socket socket = sockets.get(devUuid);
		byte[] uuidByte=TcpServerFoward.stringToByte(devUuid);
		byte[] umOperate=new byte[22];
		umOperate [0]=0x01;
		umOperate [1]=0x01;
		umOperate [2]=0x03;
		for(int i=0;i<uuidByte.length;i++){
			umOperate [i+3]=uuidByte[i];	
		}
		umOperate [19]=0x16;
		umOperate [20]=(byte) Integer.parseInt(umId); 		
		if(operate.equals("borrow")){			
			umOperate [21]=0x00;			
		}else if(operate.equals("reback")){
			umOperate [21]=0x01;
		}	
		UmbrellaDao dao = new UmbrellaDaoImpl();
		Umbrella umBefore = dao.findDeviceByUuid(devUuid);
		byte[] staBefore=umBefore.getUmbrellaSta();
		try{
			send(socket, umOperate);
		}catch(Exception e){
			r.setStatus(0);
			r.setContent("该伞架未在线");
			System.out.println("伞架未在线");
			return r;
		}
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
	
		Umbrella umAfter = dao.findDeviceByUuid(devUuid); 
		byte[] staAfter=umAfter.getUmbrellaSta();
		int umIndex = 0;
		int umIdDeal=Integer.parseInt(umId);
		if(Integer.parseInt(umId) < 9){
			umIdDeal-=1;
			umIndex=0;
		}
	
		else if(8 < Integer.parseInt(umId) && Integer.parseInt(umId) < 12){
			umIdDeal-=9;
			umIndex=1;
		}			
		System.out.println("umIndex"+umIndex);
		UserDao userDao = new UserDaoImpl();
		//User user = userDao.findByName((String) session.getAttribute("admin")); // 寻找当前开伞用户	
		User user = userDao.findByName(admin); // 寻找当前开伞用户
		if (operate.equals("borrow")){
		
			if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
				r.setStatus(1);
				r.setContent("借伞成功");				
				user.setBorrowSta(false);				
			}else{
				r.setStatus(0);
				r.setContent("借伞失败");											
			}
			
		}else if(operate.equals("reback")){
			if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
				r.setStatus(1);
				r.setContent("还伞成功");				
				user.setBorrowSta(true);				
			}else{
				r.setStatus(0);
				r.setContent("还伞失败");	
			}
			
		}			
		userDao.addUser(user);
		return r;
	}
	
	
	@RequestMapping(value = "/umbrella-list", method = { RequestMethod.GET, RequestMethod.POST })
	public String umbrellaList(Locale locale, Model model, String id, HttpServletRequest request) {
		UmbrellaDaoImpl dao = new UmbrellaDaoImpl();
		System.out.println(id);
		Map<Integer, Boolean> umbrellaSta = dao.getUmbrellaSta(id);
		model.addAttribute("umbrellaSta", umbrellaSta);
		model.addAttribute("uuid", id);
		model.addAttribute("umbrellaNum", umbrellaSta.size());
		return "umbrella-list";	
	}

	
	@RequestMapping(value = "/delete-dev", method ={ RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody NetResult deleteDev(Locale locale, Model model,String uuid) {
		NetResult r=new NetResult();		
		UmbrellaDao dao=new  UmbrellaDaoImpl();	
		System.out.println(uuid);
		dao.deleteDevice(uuid);
		return r;			
	}
	@RequestMapping(value = "/device-add", method ={ RequestMethod.GET,RequestMethod.POST})
	public String deviceAdd(Locale locale, Model model,String id,String name,String devId,Double X,Double Y) {
		if (devId != null) {
			UmbrellaDao dao = new UmbrellaDaoImpl();
			String uuid = Md5_1.GetMD5Code(devId);
			if (dao.findDeviceByUuid(uuid) == null) {
				Umbrella um = new Umbrella();
				um.setName(name);
				um.setStatus(false);
				um.setTime(new Date());
				um.setDevice_uuid(uuid);
				byte[] date = new byte[2];
				date[0] = (byte) 0xff;
				date[1] = 0x03;
				um.setUmbrellaSta(date);
				um.setDevice_lat(Y);
				um.setDevice_lon(X);
				dao.addDevice(um);
			}			
			model.addAttribute("addInfo", "添加伞架设备成功");
		}
		return "device-add";
	
	}
	public static void send(Socket socket,byte [] operate) throws Exception{
		PrintWriter	pOut=null;
		OutputStream outPutStream=null;
		
	
			outPutStream=socket.getOutputStream();	
			outPutStream.write(operate);
			//outPutStream.close();
			//out.close();
			//clientSocket.close();
	
			
		
		
	}
	public static void main(String[] args) {
		UmbrellaDao dao = new UmbrellaDaoImpl();
		Umbrella um = new Umbrella();
			um.setName("伞架测试2");
			um.setStatus(false);
			um.setTime(new Date());
			um.setDevice_uuid("32311");
			byte[] date = new byte[2];
			date[0] = (byte) 0xff;
			date[1] = 0x03;
			um.setUmbrellaSta(date);
			um.setDevice_lat(31.320);
			um.setDevice_lon(121.40669);
			dao.addDevice(um);
	}
}
