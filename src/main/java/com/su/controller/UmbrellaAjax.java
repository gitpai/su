package com.su.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.dao.UmbrellaDao;
import com.su.dao.impl.UmbrellaDaoImpl;
import com.su.models.Umbrella;
import com.su.util.NetResult;

@Controller
public class UmbrellaAjax {
	@RequestMapping(value ="/getNearUmbrella", method ={ RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody List getNearUmbrella(Locale locale, Model model,double lon,double lat) {
		NetResult r=new NetResult();		
		UmbrellaDao dao=new  UmbrellaDaoImpl();	
		List<Umbrella> nearDevice =dao.findNearDevice(lon, lat);	 
		return nearDevice;			
	}
}
