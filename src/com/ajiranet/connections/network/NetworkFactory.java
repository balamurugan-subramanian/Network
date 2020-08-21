package com.ajiranet.connections.network;

import com.ajiranet.connections.network.impl.AddDevices;
import com.ajiranet.connections.network.impl.ConnectDevices;
import com.ajiranet.connections.network.impl.DeviceStrength;
import com.ajiranet.connections.network.impl.RouteInformation;

public class NetworkFactory {
	
	public Network addDevices;
	public Network connectDevices;
	public Network routeInformation;
	public Network deviceStrength;

	private Network getAddDevice() {
		if(addDevices==null) {
			addDevices =  new AddDevices();
		}
		return addDevices;
		
	}
	
	private Network getConnectDevice() {
		if(connectDevices==null) {
			connectDevices =  new ConnectDevices();
		}
		return connectDevices;
		
	}
	
	private Network getRouteInformation() {
		if(routeInformation==null) {
			routeInformation =  new RouteInformation();
		}
		return routeInformation;
		
	}
	
	private Network getSignalStrength() {
		if(deviceStrength==null) {
			deviceStrength =  new DeviceStrength();
		}
		return deviceStrength;	
	}
	
	public Network getNetwork(String optionType) {
		Network network;
		switch (optionType) {
		case "ADD":
			network = getAddDevice();
			break;
		case "CONNECT":
			network = getConnectDevice();
			break;
		case "INFO_ROUTE":
			network = getRouteInformation();
			break;
		case "SET_DEVICE_STRENGTH":
			network = getSignalStrength();
			break;
		default:
			network = getAddDevice();
		}
		return network;
	}
	
	
}