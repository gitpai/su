package com.su.dao;

import java.net.Socket;
import java.util.List;
import java.util.Map;

import com.su.models.Umbrella;

public interface UmbrellaDao {
	public List<Umbrella> findAllDevice();//找出所有设备(已实现)
	public List<Umbrella> findAllAvlDevice();//找出所有可用设备
	public void deleteDevice(String uuid);//删除指定设备(已实现)
	public Umbrella findDeviceByUuid(String devUuid);//根据UUid查找指定设备(已实现)
	public void updateDeviceInfo(String deviceId,Umbrella umbrella);//更新伞架信息(暂时不采用这种方法更新)
	public void addDevice(Umbrella umbrella);        			//添加（Or更新）伞架设备(已实现)
	public boolean getDeviceStatus(String uuid);                //获取指定伞架的状态信息  在线或离线
	public Map<Integer,Boolean> getUmbrellaSta(String deviceId);//获取指定伞架各个伞的状态信息
	public void openUmbrellaById(Socket socket,byte [] operate);//借还指定伞(已实现)
	public List<Umbrella> findNearDevice(double lon,double lat);
	
}
