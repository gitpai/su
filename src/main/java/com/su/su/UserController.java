package com.su.su;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
public class UserController {
	@RequestMapping(value = "/admin-list", method = RequestMethod.GET)
	
	public String adminList(Locale locale, Model model) {
		UserDao dao=new UserDaoImpl();
		List<User> users=dao.findAllUser();
		model.addAttribute("user", users);	
		return "admin-list";
	}
	
	@RequestMapping(value = "/user-auth", method =  RequestMethod.GET )
	public String userAuth(Locale locale, Model model) {
		UserDao userDao=new UserDaoImpl();
		List<User>	userUnAuth=userDao.findUserUnAuth();
		System.out.println(userUnAuth.size());
		model.addAttribute("userUnAuth", userUnAuth);
		return "user-auth";
	
	}

	@RequestMapping(value = "/userAuthAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult adminBarOper(Locale locale, Model model, String userName, String operate,
			HttpSession session) {
			UserDao userDao = new UserDaoImpl();
			NetResult r = new NetResult();
			System.out.println( userName);
			System.out.println(operate);
				if (operate.equals("auth")) {
					User user = userDao.findByName(userName);
					user.setUserAuth(true);
					userDao.addUser(user);
					r.setStatus(1);
				}else
				r.setStatus(0);	
				
				return r;
	}
	@RequestMapping(value = "/login", method = {RequestMethod.GET,RequestMethod.POST})
	public String login(Locale locale, Model model,
			@RequestParam(value="userName",required=true)  String userName,		
			@RequestParam(value="password",required=true)  String password,
			HttpSession session,
			HttpServletResponse response) {
			Cookie c = new Cookie("JSESSIONID",session.getId());
			c.setMaxAge(60*60);
			response.addCookie(c);
		System.out.println("进入了login");
		UserDao dao=new UserDaoImpl();
	
		if(dao.findByName(userName)!=null){			
			if(dao.findByName(userName).getPassword().equals(Md5_1.GetMD5Code(password))){		
				model.addAttribute("status", true);
			//	model.addAttribute("admin", userName);
				session.setAttribute("admin", userName);
				session.setAttribute("status", true);
			}		
										
		}else{
			model.addAttribute("status", false);
		}		
	   return "login";	
	}
	@RequestMapping(value = "/login-out", method = {RequestMethod.GET,RequestMethod.POST})
	public String loginOut(Locale locale, Model model,			
			HttpSession session,
			HttpServletResponse response) {
			Cookie c = new Cookie("JSESSIONID",session.getId());
			c.setMaxAge(60*60);
			response.addCookie(c);		
			session.setAttribute("status", false);
				
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
	
	
	
	
}
