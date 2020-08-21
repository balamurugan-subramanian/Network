package com.ajiranet.connections.model;

import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Device {
	
	private static final String REPEATER = "REPEATER";
	
	public Device(String name, String deviceType) {
		this.name = name;
		if(REPEATER.equals(deviceType)){
			this.type = DeviceType.REPEATER;
		}else {
			this.type = DeviceType.COMPUTER;
			this.signalStrength=5;
		}
		this.connectedDevices = new TreeMap<String, DeviceType>();
	}

	private String name;
	private DeviceType type;
	private Map<String, DeviceType> connectedDevices;
	private int signalStrength = 0;

	@Override
	public boolean equals(Object obj){
		if (obj instanceof Device) {
			Device device = (Device) obj;
			return (device.name.equals(this.name) );
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
	int hash = 3;
	hash = 7 * hash + this.name.hashCode();
	return hash;
	}
}
