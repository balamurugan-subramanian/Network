package com.ajiranet.connections.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Device {
	
	public Device(String name, String deviceType) {
		this.name = name;
		this.type = DeviceType.valueOf(deviceType);
		this.signalStrength=DeviceType.REPEATER.equals(this.type)?0:5;
	}

	private String name;
	private DeviceType type;
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
