package com.su.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Yujie
 *
 */
@Entity
@Table(name="umbrella")
public class Umbrella {

	@Column(name="uuid",unique=true)
	private String device_uuid;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;
	@Column(name="device_name")
	private String name;
	
	@Column(name="device_id")
	private String deviceId;
	
	@Column(name="lon")
	private Double device_lon;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	@Column(name="lat")
	private Double device_lat;
	
	@Column(name="reg_time")
	private Date time; 
	
	@Column(name="device_sta")
	private boolean status;
	@Column(name="umbrella_sta")
    private byte[] umbrellaSta;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevice_uuid() {
		return device_uuid;
	}
	public void setDevice_uuid(String device_uuid) {
		this.device_uuid = device_uuid;
	}
	public Double getDevice_lon() {
		return device_lon;
	}
	public void setDevice_lon(Double device_lon) {
		this.device_lon = device_lon;
	}
	public Double getDevice_lat() {
		return device_lat;
	}
	public void setDevice_lat(Double device_lat) {
		this.device_lat = device_lat;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public byte[] getUmbrellaSta() {
		return umbrellaSta;
	}
	public void setUmbrellaSta(byte[] umbrellaSta) {
		this.umbrellaSta = umbrellaSta;
	}	
	
	
}
