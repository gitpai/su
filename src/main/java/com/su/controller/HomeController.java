package com.su.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.dao.UmbrellaDao;
import com.su.dao.UserDao;
import com.su.dao.impl.UmbrellaDaoImpl;
import com.su.dao.impl.UserDaoImpl;
import com.su.models.Umbrella;
import com.su.models.User;
import com.su.util.BitUtils;
import com.su.util.Md5_1;
import com.su.util.NetResult;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method ={ RequestMethod.GET,RequestMethod.POST})
	public String homelogin(Locale locale, Model model) {
		logger.info("Welcome ,you need to sign", locale);		
		return "login";
	}
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home1(Locale locale, Model model) {
		return "home";
	}
	@RequestMapping(value = "/admin-list", method = RequestMethod.GET)
	
	public String adminList(Locale locale, Model model) {
		UserDao dao=new UserDaoImpl();
		List<User> users=dao.findAllUser();
		model.addAttribute("user", users);	
		return "admin-list";
	}
	@RequestMapping(value = "/device-list", method = RequestMethod.GET)
	
	public String deviceList(Locale locale, Model model) {
		
		UmbrellaDao dao=new  UmbrellaDaoImpl();	
        List<Umbrella> umbrellas=dao.findAllDevice();
        model.addAttribute("umbrellas", umbrellas);
      //  Umbrella um= umbrellas.get(0);
       
    /*    for (int i = 0; i < um.getUmbrellaSta().length; i++) {
        byte date1=	um.getUmbrellaSta()[i];
        String result ="";
        byte a = date1; 
        for (int j = 0; j < 8; j++)
        {
         byte c=a;
         a=(byte)(a>>1);
         a=(byte)(a<<1);
         if(a==c){
          result="0"+result;
         }else{
          result="1"+result;
         }
         a=(byte)(a>>1);
        }
        System.out.println(result);
		}*/
				
		return "device-list";
	}
	@RequestMapping(value = "/firstPage", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "firstPage";
	}
	@RequestMapping(value = "/getDeviceState", method ={ RequestMethod.GET,RequestMethod.POST})
	public void  getDeviceState(Locale locale, Model model) {
		
		System.out.println("接收到请求");
		
	
	}
	@RequestMapping(value = "/barUm", method ={ RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody NetResult  brrowUm(Locale locale, Model model,String devUuid,String umId,String operate ) {		
		NetResult r=new NetResult();
		System.out.println(devUuid);
		System.out.println(umId);
		System.out.println(operate);
		System.out.println("接收到请求");	
		UmbrellaDao dao=new	 UmbrellaDaoImpl();
	    Umbrella um=dao.findDeviceByUuid(devUuid);
	    byte[] umSta= um.getUmbrellaSta();
	    
		if(operate.equals("borrow")){ 
			if(Integer.parseInt(umId)<9){
				byte borro=0x00;
				umSta[0]=BitUtils.setBitValue(umSta[0],Integer.parseInt(umId)-1,borro);
			
			}else{
				byte borro=0x00;
				umSta[1]=BitUtils.setBitValue(umSta[0],Integer.parseInt(umId)-9,borro);						}	
				}
		if(operate.equals("reback")){ 
			System.out.println("raback");
			if(Integer.parseInt(umId)<9){
				byte borro=0x01;
				umSta[0]=BitUtils.setBitValue(umSta[0],Integer.parseInt(umId)-1,borro);
			
			}else{
				byte borro=0x01;
				umSta[1]=BitUtils.setBitValue(umSta[0],Integer.parseInt(umId)-9,borro);						}	
				}
		um.setUmbrellaSta(umSta);
		dao.addDevice(um);		
		return r;
		}
	@RequestMapping(value = "/reback", method ={ RequestMethod.GET,RequestMethod.POST})
	public void  rebackUm(Locale locale, Model model) {		
		System.out.println("接收到请求");			
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
	@RequestMapping(value = "/login", method = {RequestMethod.GET,RequestMethod.POST})
	public String login(Locale locale, Model model,
			@RequestParam(value="userName",required=true)  String userName,		
			@RequestParam(value="password",required=true)  String password) {
		
		System.out.println("进入了login");
		UserDao dao=new UserDaoImpl();
	
		if(dao.findByName(userName)!=null){			
			if(dao.findByName(userName).getPassword().equals(Md5_1.GetMD5Code(password))){		
				model.addAttribute("status", true);
				model.addAttribute("admin", userName);
			}		
										
		}else{
			model.addAttribute("status", false);
		}		
	   return "login";	
	}
	@RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.GET})
	public String register(Locale locale, Model model,			
			@RequestParam(value="userName",required=true)  String userName,		
			@RequestParam(value="password",required=true)  String password
			) {

		UserDaoImpl dao=new UserDaoImpl();
		User user=new User();
		user.setUserName(userName);
		user.setPassword(Md5_1.GetMD5Code(password));
		user.setBorrowSta(true);
	   
		if(dao.findByName(userName)==null){
			dao.addUser(user);
			model.addAttribute("status", true);
			model.addAttribute("reg", "注册成功");
			model.addAttribute("admin", userName);
			System.out.println(userName);
		}else{			
			model.addAttribute("status", false);
			model.addAttribute("reg", "注册失败");			
				
		}								
		
		
		return "login";
	}
	public static void main(String[] args) {
		UmbrellaDao dao=new  UmbrellaDaoImpl();
		dao.getUmbrellaSta("53013");
	/*	User user=new User();
		user.setUserName("兀玉洁");
		user.setPassword(Md5_1.GetMD5Code("yujie"));
		user.setBorrowSta(false);
		user.setTime(new Date());
		user.setType(1000);
		UserDao dao=new UserDaoImpl();
		dao.addUser(user);*/
		
		
	/*	UmbrellaDao dao=new  UmbrellaDaoImpl();
		int uuid=53013;
		for(int i=0;i<14;i++){
			Umbrella umbrella=new Umbrella();
			      
	        byte[] date=new byte[2];
	        date[0]=(byte) 0xff;
	        date[1]=0x03; 
	        umbrella.setName("伞架");
			umbrella.setDevice_uuid(""+uuid++);
			umbrella.setDevice_lon(121.405603);
			umbrella.setDevice_lat(31.322716);
			umbrella.setStatus(true);
			umbrella.setTime(new Date());
			umbrella.setUmbrellaSta(date);	
			dao.addDevice(umbrella);	
		}*/
		
   /*     List<Umbrella> umbrellas=dao.findAllDevice();
        Umbrella um= umbrellas.get(0);
        for (int i = 0; i < um.getUmbrellaSta().length; i++) {
        byte date1=	um.getUmbrellaSta()[i];
        String result ="";
        byte a = date1; 
        for (int j = 0; j < 8; j++)
        {
         byte c=a;
         a=(byte)(a>>1);
         a=(byte)(a<<1);
         if(a==c){
          result="0"+result;
         }else{
          result="1"+result;
         }
         a=(byte)(a>>1);
        }
        System.out.println(result);
		}*/
       // System.out.println("umbrellaSta:"+um.getUmbrellaSta());
	}
}
