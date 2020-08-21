package com.ajiranet.connections.util;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;

public class Utils {
	
	
	public static String concat(String connectedDevices, String deviceName) {
		return String.format("%s %s", connectedDevices, deviceName);
	}

	public static void printConnections(Map<String, Device> devicesMap) {
		System.out.println("Active Connections");
		devicesMap.values().forEach(x -> {
			x.getConnectedDevices().keySet().stream().forEach(y -> {
				System.out.println(String.format(Constants.CONNECTED_WITH_MSG, x.getName(), y));
			});
		});
	}
	
	public static Map<String, Device> getMapFromSet(Set<Device> devices) {
		Map<String, Device> devicesMap = devices.stream()
				.collect(Collectors.toMap(Device::getName, Function.identity()));
		return devicesMap;
	}
	
	public static Set<Device> getSetFromMap(Map<String, Device> devicesMap){
		return devicesMap.values().stream().collect(Collectors.toSet());
	}

}
