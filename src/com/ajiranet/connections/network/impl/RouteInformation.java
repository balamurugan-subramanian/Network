package com.ajiranet.connections.network.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.DeviceType;
import com.ajiranet.connections.model.Response;
import com.ajiranet.connections.model.RouteMap;
import com.ajiranet.connections.network.Network;

public class RouteInformation implements Network {

	@Override
	public Response process(String menuOption, Map<String, Device> devices) {
		Response response = new Response(false, true, "failed", null);
		Matcher matcher = Constants.INFO_ROUTE_PATTERN.matcher(menuOption.toUpperCase());
		while (matcher.find()) {
			response.setPatternMatched(true);
			String sourceDeviceName = matcher.group(2).trim();
			String destinationDeviceName = matcher.group(3).trim();
			Device sourceDevice = devices.get(sourceDeviceName);
			Device destinationDevice = devices.get(destinationDeviceName);
			if (null == sourceDevice || null == destinationDevice) {
				response.setErrorMessage("Error: DEVICE NOT FOUND");
				break;
			}
			Map<String, DeviceType> parentConnectedDevices = sourceDevice.getConnectedDevices();
			Map<String, DeviceType> destinationConnectedDevices = destinationDevice.getConnectedDevices();
			if (sourceDevice.getSignalStrength() <= 0 && destinationDevice.getSignalStrength() <= 0) {
				response.setErrorMessage("Error: SIGNAL STRENGTH WEAK");
				break;
			}
			if (null == parentConnectedDevices || null == destinationConnectedDevices) {
				response.setErrorMessage("Error: CONNECTIONS DOES NOT EXIST");
				break;
			}
			if (parentConnectedDevices.containsKey(destinationDeviceName)) {
				response.setError(false);
				response.setMessage("ROUTE INFO PATH SUCCESS");
				break;
			}
			Map<String, Device> visitedDevice = new TreeMap<String, Device>();
			visitedDevice.put(sourceDeviceName, sourceDevice);
			List<Device> conDevice = new LinkedList<Device>();
			List<RouteMap> routeMap = new LinkedList<RouteMap>();
			boolean visited = checkConnection(sourceDevice, devices, destinationDeviceName, conDevice, visitedDevice, 0,
					false);
			if (visited) {
				conDevice.stream().forEach( dev -> {
					routeMap.add(new RouteMap(dev.getName(), dev.getSignalStrength(), dev.getType()));
					getMap(visitedDevice, routeMap, dev);
					
				});
				int messageStrength = sourceDevice.getSignalStrength();
				
				for(RouteMap router: routeMap) {
					if(messageStrength==0) {
						break;
					}
					if(DeviceType.REPEATER.equals(router.getDeviceType())) {
						messageStrength+=messageStrength;
					}else {
						messageStrength+=router.getStrength();
					}
					messageStrength--;
				};
				if(messageStrength>=0) {
					System.out.println("messageStrength "+ messageStrength);
					response.setError(false);
					response.setMessage("ROUTE INFO PATH SUCCESS");
					break;
				}
				
			} else {
				response.setErrorMessage("ROUTE INFO NOT FOUND");
			}

			//
			break;
		}
		response.setErrorMessage(
				!response.isPatternMatched() ? Constants.INVALID_COMMAND_SYNTAX_MSG + Constants.ROUTE_INFORMATION_OPTION
						: response.getErrorMessage());
		return response;
	}

	private void getMap(Map<String, Device> visitedDevice, List<RouteMap> routeMap, Device device) {
		
		List<String> neighbor = device.getNeighbor();
		if(!neighbor.isEmpty()) {
			String neighbourName =  neighbor.get(0);
			System.out.println("neighbourName" + neighbourName);
			Device neighbourDevice = visitedDevice.get(neighbourName);
			System.out.println("routeMap" + routeMap.toString());
			routeMap.add(new RouteMap(neighbourName, neighbourDevice.getSignalStrength(), neighbourDevice.getType()));
			getMap(visitedDevice, routeMap, neighbourDevice);
		}
	}

	public boolean checkConnection(Device sourceDevice, Map<String, Device> devices, String destinationDeviceName,
			List<Device> conDevice, Map<String, Device> visitedDevice, int level, boolean visited) {
		if (visited)
			return visited;
		Map<String, DeviceType> connectedDevices = sourceDevice.getConnectedDevices();
		Set<String> connectedKeySet = connectedDevices.keySet();
		conDevice.stream().forEach(e -> System.out.println("Devices Collected List" + e.getName()));
		Set<String> filteredKeySet  = connectedKeySet.stream().filter(name -> !visitedDevice.containsKey(name)).collect(Collectors.toSet());
		System.out.println(connectedKeySet);
		for (String name : filteredKeySet) {
			if (visitedDevice.containsKey(name))
				continue;
			visitedDevice.put(name, devices.get(name));
			System.out.println("Visiting Device" + name);
			System.out.println("Visited Devices" + visitedDevice.keySet());

			if (name.equals(destinationDeviceName)) {
				conDevice.add(devices.get(name));
				System.out.println("ConnectedDevice " + name);
				visited = true;
				break;
			} else {
				level++;
				System.out.println("Level visited " + level + " name: " + name);
				Device device = devices.get(name);
				device.setLevel(level);
				String sourceDeviceName = sourceDevice.getName();
				device.getNeighbor().add(sourceDeviceName);
				if (device.getConnectedDevices().isEmpty())
					continue;
				if (device.getConnectedDevices().containsKey(destinationDeviceName)) {
					conDevice.add(device);
					System.out.println("ConnectedDevice " + name);
					visited = true;
				}
				visited = checkConnection(device, devices, destinationDeviceName, conDevice, visitedDevice, level,
						visited);
			}
		}

		return visited;

	}

}
