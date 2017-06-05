package com.su.su;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.dao.UserDao;
import com.su.dao.impl.UserDaoImpl;
import com.su.models.NetResult;
import com.su.models.User;
import com.su.util.Md5_1;

/**
 * @author Yujie
 *
 */
@Controller
public class UserAjax {

	@RequestMapping(value = "/register-mobile", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody NetResult register(Locale locale, Model model,			
			@RequestParam(value="userName",required=true)  String userName,		
			@RequestParam(value="password",required=true)  String password
			) {
		NetResult r=new NetResult();
		UserDaoImpl dao=new UserDaoImpl();	   
		if(dao.findByName(userName)==null){
			User user=new User();
			user.setUserName(userName);
			user.setPassword(Md5_1.GetMD5Code(password));
			user.setBorrowSta(true);
			dao.addUser(user);
			r.setStatus(1);
			r.setContent("注册成功");
			
		}else{			
			r.setStatus(0);
			r.setContent("用户名重复，注册失败");
		}										
		return  r;
	}
	
	@RequestMapping(value = "/login-mobile", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody NetResult login(Locale locale, Model model,
			@RequestParam(value="userName",required=true)  String userName,		
			@RequestParam(value="password",required=true)  String password,
			HttpSession session,
			HttpServletResponse response) {
			
		NetResult r=new NetResult();
		UserDao dao=new UserDaoImpl();	
		if(dao.findByName(userName)!=null){			
			if(dao.findByName(userName).getPassword().equals(Md5_1.GetMD5Code(password))){		
				r.setStatus(1);
				r.setContent("登录成功");
			}		
										
		}else{
			r.setContent("账户或密码错误");
		}		
	   return r;	
	}
}
