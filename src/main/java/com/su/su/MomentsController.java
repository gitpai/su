package com.su.su;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.dao.MomentDao;
import com.su.dao.impl.MomentDaoImpl;
import com.su.models.Moments;
import com.su.models.NetResult;

@Controller
public class MomentsController {
	@RequestMapping(value = "/getLatestMoments", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody  List getLatestMoments(Locale locale, Model model) {
		MomentDao mDao=new MomentDaoImpl();
		 List<Moments> moments=	mDao.findLatestMoment();		 		
		return moments;
	}@RequestMapping(value = "/addMoment", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult  addMoment(Locale locale, Model model,String userName,String sex,String userLocal,String comment) {
		NetResult r=new NetResult();	
		try {
			Moments moment=new Moments();
			moment.setUserName(userName);
			if(sex.equals("man")){
				moment.setUserSex(true);	
			}else{
				moment.setUserSex(false);
			}
			moment.setMomentTime(new Date());
			moment.setUserLocal(userLocal);
			moment.setUserMoment(comment);
			MomentDao mDao=new MomentDaoImpl();
			mDao.addMoment(moment);
		} catch (Exception e) {
			r.setStatus(0);
			r.setContent("Ìí¼Óæ°É¡×´Ì¬Ê§°Ü");
			return  r;
		}
		r.setStatus(1);
		r.setContent("Ìí¼Óæ°É¡×´Ì¬³É¹¦");
		return  r;
		
	}

}
