package com.su.models;

import javax.persistence.Column;

/**
 * @author Yujie
 * */
public class UmbrellaNear {

	
	private String deviceId;
		
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	private Double device_lon;
	
	private Double device_lat;
	
}
