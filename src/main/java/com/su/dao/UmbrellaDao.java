package com.su.dao;

import java.util.List;
import java.util.Map;

import com.su.models.Umbrella;

public interface UmbrellaDao {
	public List<Umbrella> findAllDevice();//找出所有设备
	public List<Umbrella> findAllAvlDevice();//找出所有可用设备
	public Umbrella findDeviceByUuid(String devUuid);
	public void updateDeviceInfo(String deviceId,Umbrella umbrella);//更新伞架信息
	public void addDevice(Umbrella umbrella);                    //添加伞架设备	
	public boolean getDeviceStatus(String uuid);                //获取指定伞架的状态信息  在线或离线
	
	public Map<Integer,Boolean> getUmbrellaSta(String deviceId);//获取指定伞架各个伞的状态信息
	
	public void openUmbrellaById(String deviceId,String umbrellaId);//打开指定伞架的指定伞
	
}
