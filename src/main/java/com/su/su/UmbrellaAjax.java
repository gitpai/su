package com.su.su;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.su.models.UmbrellaNear;
import com.su.models.User;
import com.su.socket.SocketStart;
import com.su.socket.TcpServerFoward;
import com.su.util.BitUtils;

/**
 * @author Yujie
 *
 */
@Controller
public class UmbrellaAjax {
	/**
	 * 
	 * ��ȡ�����ɡ����Ϣ
	 * @param locale
	 * @param model
	 * @param lon
	 * @param lat
	 * @return
	 */
	@RequestMapping(value ="/getNearUmbrella", method ={ RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody List getNearUmbrella(Locale locale, Model model,double lon,double lat) {
		NetResult r=new NetResult();		
		UmbrellaDao dao=new  UmbrellaDaoImpl();	
		List<UmbrellaNear> nearDevice =dao.findNearDevice(lon, lat);	 
		return nearDevice;			
	}
	/**
	 * ����ָ��Uuid��ȡɡ�ܿ���ɡ��Ϣ 
	 * @param locale
	 * @param model
	 * @param lon
	 * @param lat
	 * @return
	 */
	@RequestMapping(value ="/getUmbrellaSta", method ={ RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Map<Integer, Boolean> getUmbrellaSta(Locale locale, Model model,String uuid) {			
		UmbrellaDao dao=new  UmbrellaDaoImpl();	
		Map<Integer, Boolean> umbrellaSta=dao.getUmbrellaSta(uuid);		
		return umbrellaSta;			
	}
	/**
	 * 
	 * �軹ɡ
	 * @param locale
	 * @param model
	 * @param devUuid
	 * @param umId
	 * @param operate
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/barUmAdmin-mobile", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult adminBarOper(Locale locale, Model model, String devUuid, String umId, String operate,
			HttpSession session
	){
		System.out.println("devUuid"+devUuid+"umId"+umId+"operate"+operate);
		NetResult r = new NetResult();		
		Map<String, Socket> sockets = SocketStart.getSocketClients();// ��ȡ��ǰSocket�б�
		Socket socket = sockets.get(devUuid);
		byte[] uuidByte=TcpServerFoward.stringToByte(devUuid);
		byte[] umOperate=new byte[22];	//�·�Э��
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
			r.setContent("��ɡ��δ����");
			System.out.println("ɡ��δ����");
			return r;
		}
		
		
		try {
			if(operate.equals("borrow")){			
				Thread.sleep(5000);		
			}else if(operate.equals("reback")){
				Thread.sleep(10000);
			}
		
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
		User user = userDao.findByName((String) session.getAttribute("admin")); // Ѱ�ҵ�ǰ��ɡ�û�	
		//	User user = userDao.findByName(admin); // Ѱ�ҵ�ǰ��ɡ�û�
		if (operate.equals("borrow")){
		
			if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
				r.setStatus(1);
				r.setContent("��ɡ�ɹ�");				
				user.setBorrowSta(false);				
			}else{
				r.setStatus(0);
				r.setContent("��ɡʧ��");											
			}
			
		}else if(operate.equals("reback")){
			if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
				r.setStatus(1);
				r.setContent("��ɡ�ɹ�");				
				user.setBorrowSta(true);				
			}else{
				r.setStatus(0);
				r.setContent("��ɡʧ��");	
			}
			
		}			
		userDao.addUser(user);
		return r;
	}	
		/*if (operate.equals("borrow")) {
			if (Integer.parseInt(umId) < 9) {
				if (BitUtils.getBitValue(staBefore[0], Integer.parseInt(umId)) != BitUtils.getBitValue(staAfter[0],Integer.parseInt(umId))) {
					r.setStatus(0);
					r.setContent("��ɡ�ɹ�");
					UserDao userDao = new UserDaoImpl();
					User user = userDao.findByName((String) session.getAttribute("admin")); // Ѱ�ҵ�ǰ��ɡ�û�
					user.setBorrowSta(false);
					userDao.addUser(user);
				}
			} else if (8 < Integer.parseInt(umId) && Integer.parseInt(umId) < 12) {
				if (BitUtils.getBitValue(staBefore[1], Integer.parseInt(umId)) != BitUtils.getBitValue(staAfter[1],
						Integer.parseInt(umId))) {
					r.setStatus(0);
					r.setContent("��ɡ�ɹ�");
					UserDao userDao = new UserDaoImpl();
					User user = userDao.findByName((String) session.getAttribute("admin")); // Ѱ�ҵ�ǰ��ɡ�û�
					user.setBorrowSta(false);
					userDao.addUser(user);
				}

			}

		} else if (operate.equals("reback")) {
			if (BitUtils.getBitValue(staBefore[0], Integer.parseInt(umId)) != BitUtils.getBitValue(staAfter[0],
					Integer.parseInt(umId))) {
				r.setStatus(0);
				r.setContent("��ɡ�ɹ�");
				UserDao userDao = new UserDaoImpl();
				User user = userDao.findByName((String) session.getAttribute("admin")); // Ѱ�ҵ�ǰ��ɡ�û�
				user.setBorrowSta(true);
				userDao.addUser(user);
			} else if (8 < Integer.parseInt(umId) && Integer.parseInt(umId) < 12) {
				if (BitUtils.getBitValue(staBefore[1], Integer.parseInt(umId)) != BitUtils.getBitValue(staAfter[1],
						Integer.parseInt(umId))) {
					r.setStatus(0);
					r.setContent("��ɡ�ɹ�");
					UserDao userDao = new UserDaoImpl();
					User user = userDao.findByName((String) session.getAttribute("admin")); // Ѱ�ҵ�ǰ��ɡ�û�
					user.setBorrowSta(true);
					userDao.addUser(user);
				}

			}

		}*/
			
		
		
		
		/*byte[] operateResult = new byte[50];
		try {
			InputStream socketInput = socket.getInputStream();
			System.out.println("whileǰ");
			while (socketInput.read(operateResult) != -1) {
				System.out.println("��ʼ������Ϣ");  
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
						System.out.println("�����ɹ�");
						UmbrellaDao dao = new UmbrellaDaoImpl();
						UserDao userDao = new UserDaoImpl();
						User user = userDao.findByName((String)session.getAttribute("admin")); // Ѱ�ҵ�ǰ��ɡ�û�
						Umbrella um = dao.findDeviceByUuid(devUuid); // Ѱ���û���ɡ�豸
						byte[] umSta = um.getUmbrellaSta();

					if (operate.equals("borrow")) {
							byte borro = 0x00;
							if (Integer.parseInt(umId) < 9) {

								umSta[0] = BitUtils.setBitValue(umSta[0], Integer.parseInt(umId) - 1, borro);

							} else {
								umSta[1] = BitUtils.setBitValue(umSta[1], Integer.parseInt(umId) - 9, borro);
							}
							r.setStatus(0);
							r.setContent("��ɡ�ɹ�");
						}
						if (operate.equals("reback")) {

							System.out.println("reback");
							byte reback = 0x01;
							if (Integer.parseInt(umId) < 9) {
								umSta[0] = BitUtils.setBitValue(umSta[0], Integer.parseInt(umId) - 1, reback);
							} else {
								umSta[1] = BitUtils.setBitValue(umSta[1], Integer.parseInt(umId) - 9, reback);
							}
							r.setStatus(1);
							r.setContent("��ɡ�ɹ�");
						}
						um.setUmbrellaSta(umSta);
						dao.addDevice(um); // ������ɡ״̬
						user.setBorrowSta(false);
						userDao.addUser(user);
						
					} else if (operSta == 0x00) {
						r.setStatus(0);
						r.setContent("����ʧ��");
					}
					
					break;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	
	
	public static void send(Socket socket,byte [] operate){
		PrintWriter	pOut=null;
		OutputStream outPutStream=null;
		
		try {
			outPutStream=socket.getOutputStream();	
			outPutStream.write(operate);
			//outPutStream.close();
			//out.close();
			//clientSocket.close();
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("����ʧ��");
		}finally{
			
		}
	}
}
