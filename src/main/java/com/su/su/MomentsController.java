package com.su.su;

import java.util.ArrayList;
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
import com.su.models.Comment;
import com.su.models.Moments;
import com.su.models.NetResult;
import com.su.util.Md5_1;

@Controller
public class MomentsController {
	@RequestMapping(value = "/getLatestMoments", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody  List getLatestMoments(Locale locale, Model model) {
		MomentDao mDao=new MomentDaoImpl();
		
		List<Moments> moments=	mDao.findLatestMoment();
		
		List<Moments> momentWithComent=new ArrayList<Moments>();
		
		for(int i=0;i<moments.size();i++){		
		moments.get(i).setComment(mDao.findComments(moments.get(i).getUuid()));
		momentWithComent.add(moments.get(i));
		}	
		return momentWithComent;
	}
	@RequestMapping(value = "/getMomentsTest", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody  List getLatestMomentsTest(Locale locale, Model model) {
		MomentDao mDao=new MomentDaoImpl();
		List<Moments> moments=	mDao.findLatestMoment();
		List<Moments> momentWithComent=new ArrayList<Moments>();
		for(int i=0;i<moments.size();i++){
		moments.get(i).setComment(mDao.findComments(moments.get(i).getUuid()));
		momentWithComent.add(moments.get(i));
		}
	
		return momentWithComent;
	}
	@RequestMapping(value = "/addMoment", method = { RequestMethod.GET, RequestMethod.POST })
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
			moment.setUuid(Md5_1.GetMD5Code(userName+new Date()) );
			MomentDao mDao=new MomentDaoImpl();
			mDao.addMoment(moment);
		} catch (Exception e) {
			e.printStackTrace();
			r.setStatus(0);
			r.setContent("Ìí¼Óæ°É¡×´Ì¬Ê§°Ü");
			return  r;
		}
		r.setStatus(1);
		r.setContent("Ìí¼Óæ°É¡×´Ì¬³É¹¦");
		return  r;
		
	}
	@RequestMapping(value = "/addComment", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody NetResult  addComment(Locale locale, Model model,String userName,String uuid_moment,String commentContent) {
		NetResult r=new NetResult();	
		try {
			MomentDao mDao=new MomentDaoImpl();
			Comment comment=new Comment();
			comment.setUuid(uuid_moment);
			comment.setComment(commentContent);
			comment.setUserName(userName);
			comment.setCommentTime(new Date());
			mDao.addComment(comment);
		} catch (Exception e) {
			r.setStatus(0);
			r.setContent("ÆÀÂÛÊ§°Ü");
			return  r;
		}
		r.setStatus(1);
		r.setContent("ÆÀÂÛ³É¹¦");
		return  r;
		
	}

}
