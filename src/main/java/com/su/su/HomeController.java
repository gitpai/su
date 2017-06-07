package com.su.su;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
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
import com.su.models.NetResult;
import com.su.models.Umbrella;
import com.su.models.User;
import com.su.util.BitUtils;
import com.su.util.Md5_1;

/**
 * Handles requests for the application home page.
 */

/**
 * @author Yujie
 *
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

	@RequestMapping(value = "/device-list", method ={ RequestMethod.GET,RequestMethod.POST})
	
	public String deviceList(Locale locale, Model model) {
		
		
		UmbrellaDao dao=new  UmbrellaDaoImpl();	
        List<Umbrella> umbrellas=dao.findAllDevice();
        model.addAttribute("umbrellas", umbrellas);	
        model.addAttribute("device",umbrellas.size());
       
		return "device-list";
	}
	@RequestMapping(value = "/device-map", method = { RequestMethod.GET, RequestMethod.POST })
	public String umbrellaMap(Locale locale, Model model) {
		UmbrellaDao dao=new  UmbrellaDaoImpl();	
        List<Umbrella> umbrellas=dao.findAllDevice();
        model.addAttribute("umbrellas", umbrellas);	
        model.addAttribute("device",umbrellas.size());
		return "device-map";
	
	}
	@RequestMapping(value = "/firstPage", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "firstPage";
	}

	
	

	public static void main(String[] args) {
		/*UmbrellaDao dao=new  UmbrellaDaoImpl();
		dao.getUmbrellaSta("53013");*/
		User user=new User();
		user.setUserName("yujie");
		user.setPassword(Md5_1.GetMD5Code("wuyujie123"));
		user.setBorrowSta(false);
		user.setTime(new Date());
		
		UserDao dao1=new UserDaoImpl();
		dao1.addUser(user);
		
		
		
	/*	int uuid=53013;
		for(int i=0;i<14;i++){
			Umbrella umbrella=new Umbrella();
			      
	        byte[] date=new byte[2];
	        date[0]=(byte) 0xff;
	        date[1]=0x03; 
	        umbrella.setName("É¡¼Ü");
			umbrella.setDevice_uuid("81cee0dd9793330ac4cd1ddc133a0035");
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
