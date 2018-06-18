package com.su.su;

import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.su.models.UserLoginSta;
import com.su.models.UserVerifyCode;
import com.su.socket.SocketStart;
import com.su.socket.TcpServerFoward;
import com.su.util.BitUtils;
import com.su.util.Md5_1;
import com.su.util.SmsDemo;

/**
 * @author Yujie
 *
 */
@Controller
public class UserAjax {

	@RequestMapping(value = "/register-mobile", method = {RequestMethod.POST, RequestMethod.GET})
	public  @ResponseBody NetResult register(Locale locale, Model model,			
			@RequestParam(value="userName",required=true)  String userName,		
			@RequestParam(value="password",required=true)  String password,
			@RequestParam(value="userSex",required=true)   boolean userSex,
			@RequestParam(value="nickName",required=true)  String nickName,
			@RequestParam(value="smsCode",required=true)   String smsCode
			) {
		NetResult r=new NetResult();
		UserDaoImpl dao=new UserDaoImpl();	   
		if(dao.findByName(userName)==null){
			String smsCodeLocal=dao.getUserRegCode(userName);//获取数据库验证码
			if(smsCodeLocal.equals(smsCode)){
				User user=new User();
				user.setUserName(userName);
				user.setPassword(Md5_1.GetMD5Code(password));
				user.setBorrowSta(false);
				user.setTime(new Date());
				user.setUserAuth(true);
				user.setUserSex(userSex);
				user.setNickName(nickName);
				dao.addUser(user);
				r.setStatus(1);
				r.setContent("注册成功");	
			}else{
				r.setStatus(0);
				r.setContent("验证码错误");	
			}
			
			
		}else{			
			r.setStatus(0);
			r.setContent("用户名重复，注册失败");
		}										
		return  r;
	}
	@RequestMapping(value = "/register-verifycode", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody NetResult registerVerfyCode(Locale locale, Model model,			
			@RequestParam(value="userName",required=true)  String userName
			) {
		NetResult r=new NetResult();
		UserDaoImpl dao=new UserDaoImpl();	   
		if(dao.findByName(userName)==null){
			UserVerifyCode userVerifyCode=new UserVerifyCode();
		
			String smsCode="";//生成4位验证码
			for (int i = 0; i < 4; i++) {
				smsCode += new Random().nextInt(10) + "";
			}
			userVerifyCode.setUserName(userName);
			userVerifyCode.setVerfyCode(smsCode);
			dao.addUserRegCode(userVerifyCode);
			SmsDemo.sendMessage(userName, smsCode);
			r.setStatus(1);
			r.setContent("下发验证码成功");
			
		}else{			
			r.setStatus(0);
			r.setContent("下发验证码失败，该手机号已注册，请登录");
		}										
		return  r;
	}
	
	@RequestMapping(value = "/login-mobile", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody UserLoginSta login(Locale locale, Model model,
			@RequestParam(value="userName",required=true)  String userName,		
			@RequestParam(value="password",required=true)  String password,			
			HttpSession session,
			HttpServletResponse response) {
			
		UserLoginSta userLoginSta=null;
		UserDao dao=new UserDaoImpl();	
		if(dao.findByName(userName)!=null){			
			if(dao.findByName(userName).getPassword().equals(Md5_1.GetMD5Code(password))){		
				userLoginSta=	dao.getUserLoginSta(userName);	
				userLoginSta.setStatus(1);
			}												
		}else{
			
			 userLoginSta=new UserLoginSta();
			 userLoginSta.setStatus(0);
		
		}		
	   return userLoginSta;	
	}	
	@RequestMapping(value = "/user-sta", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody UserLoginSta  getUserSta(Locale locale, Model model,
			@RequestParam(value="userName",required=true)  String userName,							
			HttpSession session,
			HttpServletResponse response) {
			UserDao userDao=new UserDaoImpl();
		    UserLoginSta userLoginSta=	userDao.getUserLoginSta(userName);						  
		    return userLoginSta ;
	}
	/**
	 * 网页端借还伞
	 */
	@RequestMapping(value = "/su", method = RequestMethod.GET)
	public String su(Locale locale, Model model) {
		return "su";
	}
	@RequestMapping(value = "/web-bar", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody NetResult barWeb(Locale locale, Model model,
			@RequestParam(value="userName",required=true)  String userName,		
			@RequestParam(value="umbCode",required=true)  String umbCode,
			@RequestParam(value="smsCode",required=true)   String smsCode,
			@RequestParam(value="umbId",required=true)   String umbId,
			@RequestParam(value="operate",required=true)   String operate,
			HttpSession session,
			HttpServletResponse response) {			
			NetResult r=new NetResult();		
			UserDao dao=new UserDaoImpl();
			String smsCodeLocal=dao.getUserRegCode(userName);
			if(smsCodeLocal.equals(smsCode)){
				if(dao.findByName(userName)==null){	//用户第一次使用共享雨伞		
					if(!operate.equals("borrow")){
						r.setStatus(0);
						r.setContent("第一次操作，只允许借伞，请重新填写验证码进行借伞操作");
						return r;
					}else{
						return borrowAndRebackUmb(userName, umbCode, umbId, operate, r);	
					}
									
				}else{//用户非首次使用共享雨伞
						if(operate.equals("borrow")){
							 User user=dao.findByName(userName);
							 if(user.isBorrowSta()){
								 r.setContent("您上次借伞尚未归还，请归还后再进行此操作");
								 r.setStatus(0);
								 return r;
							 }
							 System.out.println("this1");
					 return borrowAndRebackUmb(userName, umbCode, umbId, operate, r);
					
						}else{
							System.out.println("this2");
					 return borrowAndRebackUmb(userName, umbCode, umbId, operate, r);
					}
						
				}
				
			}
			r.setContent("验证码错误");
			r.setStatus(0);
			return r;						
	}
	private NetResult  borrowAndRebackUmb(String userName, String umbCode, String umbId, String operate, NetResult r) {
		byte[] uuidByte=TcpServerFoward.stringToByte(Md5_1.GetMD5Code(umbCode));
		byte[] umOperate=new byte[22];
		umOperate [0]=0x01;
		umOperate [1]=0x01;
		umOperate [2]=0x03;
		for(int i=0;i<uuidByte.length;i++){
			umOperate [i+3]=uuidByte[i];	
		}
		umOperate [19]=0x16;
		umOperate [20]=(byte) Integer.parseInt(umbId); 							
		if(operate.equals("borrow")){			
			umOperate [21]=0x00;			
		}else if(operate.equals("reback")){
			umOperate [21]=0x01;
		}							
		Map<String, Socket> sockets = SocketStart.getSocketClients();// 获取当前Socket列表
		Socket socket = sockets.get(Md5_1.GetMD5Code(umbCode));
		UmbrellaDao umbrellaDao = new UmbrellaDaoImpl();
		Umbrella umBefore = umbrellaDao.findDeviceByUuid(Md5_1.GetMD5Code(umbCode));
		System.out.println(Md5_1.GetMD5Code(umbCode));
		byte[] staBefore=umBefore.getUmbrellaSta();
		
		try{
			UmbrellaAjax.send(socket, umOperate);
		}catch(Exception e){
			r.setStatus(0);
			r.setContent("该伞架未在线");
		//	System.out.println("伞架未在线");
			return r;
		}				
		try {
			if(operate.equals("borrow")){	
				System.out.println("在等待");
				Thread.sleep(5000);		
			}else if(operate.equals("reback")){
				Thread.sleep(10000);
			}	
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//

		Umbrella umAfter = umbrellaDao.findDeviceByUuid(Md5_1.GetMD5Code(umbCode)); 
		byte[] staAfter=umAfter.getUmbrellaSta();
		int umIndex = 0;
		int umIdDeal=Integer.parseInt(umbId);
		if(Integer.parseInt(umbId) < 9){
			umIdDeal-=1;
			umIndex=0;
		}

		else if(8 < Integer.parseInt(umbId) && Integer.parseInt(umbId) < 12){
			umIdDeal-=9;
			umIndex=1;
		}			
		System.out.println("umIndex"+umIndex);	
		if (operate.equals("borrow")){
		if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
		
				UserBarHisDao ubhd =new UserBarHisDaoImpl();	//创建借伞历史信息方法			
				UserBarHistory us=	new UserBarHistory();   //创建历史信息对象
				us.setBorrowTime(new Date());               //添加借伞时间
				us.setUserName(userName);
				ubhd.addBarHis(us);						
				r.setStatus(1);
				r.setContent("借伞成功");	
				UserDao dao=new UserDaoImpl();
				if(dao.findByName(userName)==null){
					User user=new User();
					user.setUserName(userName);
					user.setBorrowSta(true);
					dao.addUser(user);
				}else{
					User user=dao.findByName(userName);
					user.setBorrowSta(true);
					dao.addUser(user);
				}
								
		}else{
			System.out.println("====================");
				r.setStatus(0);
				r.setContent("借伞失败");				
			}
		return r;
		}
		else if(operate.equals("reback")){
			if (BitUtils.getBitValue(staBefore[umIndex], umIdDeal) != BitUtils.getBitValue(staAfter[umIndex],umIdDeal)){
				UserBarHisDao ubhd =new UserBarHisDaoImpl();
				UserBarHistory us=	ubhd.findLatestHis(userName);
				us.setRebackTime(new Date());	
				ubhd.addBarHis(us);	
				r.setStatus(1);
				r.setContent("还伞成功");
				User user=new User();
				user.setBorrowSta(false);				
			}else{
				r.setStatus(0);
				r.setContent("还伞失败");	
			}
			
		}
		return r;
	}
}
