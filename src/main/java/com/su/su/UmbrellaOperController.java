package com.su.su;

import java.io.File;
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

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.dao.UmbrellaDao;
import com.su.dao.UserBarHisDao;
import com.su.dao.UserDao;
import com.su.dao.impl.UmbrellaDaoImpl;
import com.su.dao.impl.UserBarHisDaoImpl;
import com.su.dao.impl.UserDaoImpl;
import com.su.models.NetResult;
import com.su.models.Umbrella;
import com.su.models.User;
import com.su.models.UserBarHistory;
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
	 * �ֻ��˽軹ɡ�ӿ�
	 */
	@RequestMapping(value = "/barUmUser", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult brrowUm(Locale locale, Model model, String devUuid, String umId, String operate,
			String userName) {
		NetResult r = new NetResult();
		System.out.println(devUuid);
		System.out.println(umId);
		System.out.println(operate);
		System.out.println("���յ�����");

		Map<String, Socket> sockets = SocketStart.getSocketClients();// ��ȡ��ǰSocket�б�
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
						User user = userDao.findByName(userName); // Ѱ�ҵ�ǰ��ɡ�û�
						Umbrella um = dao.findDeviceByUuid(devUuid); // Ѱ���û���ɡ�豸
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
						dao.addDevice(um); // ������ɡ״̬
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
	 * ��̨���ܽ軹ɡ�ӿ�
	 */
/*	@RequestMapping(value = "/barUmAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult adminBarOper(Locale locale, Model model, String devUuid, String umId, String operate,
			HttpSession session
	) {
		
		NetResult r = new NetResult();
		System.out.println(devUuid);
		System.out.println(umId);
		System.out.println(operate);
		System.out.println("���յ�����");
	
		Map<String, Socket> sockets = SocketStart.getSocketClients();// ��ȡ��ǰSocket�б�
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
						dao.addDevice(um); // ������ɡ״̬
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
		
		System.out.println("�˳�ѭ��");
		System.out.println("sockets==========" + sockets);
		System.out.println("socket============" + socket);
		return r;
	}	*/
	
	@RequestMapping(value = "/barUmAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult adminBarOper(Locale locale, Model model, String devUuid, String umId, String operate,String admin,
			HttpSession session
	) {
	//	System.out.println("devUuid"+devUuid+"umId"+umId+"operate"+operate);
		NetResult r = new NetResult();		
		Map<String, Socket> sockets = SocketStart.getSocketClients();// ��ȡ��ǰSocket�б�
		Socket socket = sockets.get(devUuid);
		byte[] uuidByte=TcpServerFoward.stringToByte(devUuid);
		
		UserDao userDao = new UserDaoImpl();	
		User user = userDao.findByName(admin); // Ѱ�ҵ�ǰ��ɡ�û�	
		if(!user.isUserAuth()){ //�û�Ȩ��У��
			r.setStatus(0);
			r.setContent("���û�δ��Ȩ");	
			return r;
		}
		if (operate.equals("borrow")) {
			if (user.isBorrowSta()) {
				UserBarHisDao ubhd = new UserBarHisDaoImpl();
				UserBarHistory us = ubhd.findLatestHis(admin);
				r.setStatus(0);
				r.setContent("���û���" + us.getBorrowTime() + "ʱ���ѽ�ɡ��δ�黹");
				return r;
			}
		}
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
			r.setContent("��ɡ��δ����");
		//	System.out.println("ɡ��δ����");
			return r;
		}				
	/*	try {
			if(operate.equals("borrow")){			
				Thread.sleep(20000);		
			}else if(operate.equals("reback")){
				
				
				
				Thread.sleep(20000);
			}	
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
*/	
			

		int count =0;
		while(count<5){	
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		if (operate.equals("borrow")){
			
			Umbrella umAfter = dao.findDeviceByUuid(devUuid); 
			byte[] staAfter=umAfter.getUmbrellaSta();
			int umIndex = 0;
			int umIdDeal=Integer.parseInt(umId);
			if(Integer.parseInt(umId) < 9){
				umIdDeal-=1;
				umIndex=0;
			}else if(8 < Integer.parseInt(umId) && Integer.parseInt(umId) < 12){
				umIdDeal-=9;
				umIndex=1;
			}	
			if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
				UserBarHisDao ubhd =new UserBarHisDaoImpl();	//������ɡ��ʷ��Ϣ����			
				UserBarHistory us=	new UserBarHistory();   //������ʷ��Ϣ����
				us.setBorrowTime(new Date());               //��ӽ�ɡʱ��
				us.setUserName(admin);
				ubhd.addBarHis(us);						
				r.setStatus(1);
				r.setContent("��ɡ�ɹ�");				
				user.setBorrowSta(true);
				userDao.addUser(user);
				return r;
			}
			
		}else if(operate.equals("reback")){
			Umbrella umAfter = dao.findDeviceByUuid(devUuid); 
			byte[] staAfter=umAfter.getUmbrellaSta();
			int umIndex = 0;
			int umIdDeal=Integer.parseInt(umId);
			if(Integer.parseInt(umId) < 9){
				umIdDeal-=1;
				umIndex=0;
			}else if(8 < Integer.parseInt(umId) && Integer.parseInt(umId) < 12){
				umIdDeal-=9;
				umIndex=1;
			}	
			if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
				UserBarHisDao ubhd =new UserBarHisDaoImpl();
				UserBarHistory us=	ubhd.findLatestHis(admin);
				us.setRebackTime(new Date());	
				ubhd.addBarHis(us);	
				r.setStatus(1);
				r.setContent("��ɡ�ɹ�");				
				user.setBorrowSta(false);	
				userDao.addUser(user);
				return r;
			}
			
		}	
	}
		r.setStatus(0);
		r.setContent("����ʧ��");	
		userDao.addUser(user);
		return r;
	}
	/**
	 * ��ɡ
	 * @param locale
	 * @param model
	 * @param devUuid
	 * @param umId
	 * @param operate
	 * @param admin
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/buyUmbrella", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult buyUmbrella(Locale locale, Model model, String devUuid, String umId, String admin,
			HttpSession session
	) {
	//	System.out.println("devUuid"+devUuid+"umId"+umId+"operate"+operate);
		NetResult r = new NetResult();		
		Map<String, Socket> sockets = SocketStart.getSocketClients();// ��ȡ��ǰSocket�б�
		Socket socket = sockets.get(devUuid);
		byte[] uuidByte=TcpServerFoward.stringToByte(devUuid);
		
		UserDao userDao = new UserDaoImpl();	
		User user = userDao.findByName(admin); // Ѱ�ҵ�ǰ��ɡ�û�	
		if(!user.isUserAuth()){ //�û�Ȩ��У��
			r.setStatus(0);
			r.setContent("���û�δ��Ȩ");	
			return r;
		}	
		byte[] umOperate=new byte[22];
		umOperate [0]=0x01;
		umOperate [1]=0x01;
		umOperate [2]=0x03;
		for(int i=0;i<uuidByte.length;i++){
			umOperate [i+3]=uuidByte[i];	
		}
		umOperate [19]=0x16;
		umOperate [20]=(byte) Integer.parseInt(umId); 		
				
			umOperate [21]=0x00;  //��ɡ��ʱЭ���߽�ɡЭ��			
	
		UmbrellaDao dao = new UmbrellaDaoImpl();
		Umbrella umBefore = dao.findDeviceByUuid(devUuid);
		byte[] staBefore=umBefore.getUmbrellaSta();
		try{
			send(socket, umOperate);
		}catch(Exception e){
			r.setStatus(0);
			r.setContent("��ɡ��δ����");
		//	System.out.println("ɡ��δ����");
			return r;
		}				
	/*	try {		
				Thread.sleep(20000);		
	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//
		int count =0;
		while(count<5){	
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		
		Umbrella umAfter = dao.findDeviceByUuid(devUuid); 
		byte[] staAfter=umAfter.getUmbrellaSta();
		int umIndex = 0;
		int umIdDeal=Integer.parseInt(umId);
		if(Integer.parseInt(umId) < 9){
			umIdDeal-=1;
			umIndex=0;
		}else if(8 < Integer.parseInt(umId) && Integer.parseInt(umId) < 12){
			umIdDeal-=9;
			umIndex=1;
		}			


			if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
				
								
				r.setStatus(1);
				r.setContent("��ɡ�ɹ�");				
				user.setBorrowSta(true);
				return r;
				}
			}
		r.setStatus(0);
		r.setContent("��ɡʧ��");	
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
			model.addAttribute("addInfo", "���ɡ���豸�ɹ�");
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
			um.setName("ɡ�ܲ���2");
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
	
	
	@RequestMapping(value = "/downloadApps", method = RequestMethod.GET) 
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {    
        String ctxPath = request.getSession().getServletContext().getRealPath("");
        //��ȡ�����ļ�¶��
        String downLoadPath = ctxPath+"/resources/apps.apk";
        //System.out.println("----"+downLoadPath);
        File file=new File(downLoadPath);  
        HttpHeaders headers = new HttpHeaders();  
        String fileName=file.getName();//Ϊ�˽������������������  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
    }  
}
