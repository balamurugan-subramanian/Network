package com.ajiranet.connections.network.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.DeviceType;
import com.ajiranet.connections.model.Node;
import com.ajiranet.connections.model.Response;
import com.ajiranet.connections.model.RouteMap;
import com.ajiranet.connections.network.Network;
import com.ajiranet.connections.util.BreadthFirstSearchAlgorithm;

public class RouteInformation implements Network {
	
	private static Logger logger = LoggerFactory.getLogger(RouteInformation.class);

	@Override
	public Response process(String menuOption, Map<String, Node<Device>> devices) {
		logger.info("Entering process method");
		Response response = new Response(false, true, "failed", null);
		Matcher matcher = Constants.INFO_ROUTE_PATTERN.matcher(menuOption.toUpperCase());
		while (matcher.find()) {
			response.setPatternMatched(true);
			String sourceDeviceName = matcher.group(2).trim();
			String destinationDeviceName = matcher.group(3).trim();
			Node<Device> sourceDevice = devices.get(sourceDeviceName);
			Node<Device> destinationDevice = devices.get(destinationDeviceName);
			if (sourceDeviceName == destinationDeviceName) {
				response.setErrorMessage("Error: SAME DEVICE");
				break;
			}
			if (null == sourceDevice || null == destinationDevice) {
				response.setErrorMessage("Error: DEVICE NOT FOUND");
				break;
			}
			Set<Node<Device>> parentConnectedDevices = sourceDevice.getNeighbors();
			Set<Node<Device>> destinationConnectedDevices = destinationDevice.getNeighbors();
			if (sourceDevice.getValue().getSignalStrength() <= 0 && destinationDevice.getValue().getSignalStrength() <= 0) {
				response.setErrorMessage("Error: SIGNAL STRENGTH WEAK");
				break;
			}
			if (null == parentConnectedDevices || null == destinationConnectedDevices) {
				response.setErrorMessage("Error: CONNECTIONS DOES NOT EXIST");
				break;
			}
			 Optional<Node<Device>> opt = BreadthFirstSearchAlgorithm.search(destinationDevice.getValue(), sourceDevice);
			if (opt.isPresent()) {
				logger.info("Route map: " + sourceDeviceName + "(" + sourceDevice.getValue().getSignalStrength() + ") -> "
						+ destinationDeviceName);
				response.setError(false);
				response.setMessage("ROUTE INFO PATH SUCCESS");
				break;
			}

			//mapConnection(response, sourceDevice, destinationDeviceName, devices);
			//
			break;
		}
		response.setErrorMessage(
				!response.isPatternMatched() ? Constants.INVALID_COMMAND_SYNTAX_MSG + Constants.ROUTE_INFORMATION_OPTION
						: response.getErrorMessage());
		return response;
	}

//	public void mapConnection(Response response, Device sourceDevice, String destinationDeviceName,
//			Map<String, Device> devices) {
//		Map<String, Device> visitedDevice = new TreeMap<String, Device>();
//		visitedDevice.put(sourceDevice.getName(), sourceDevice);
//		List<Device> conDevice = new LinkedList<Device>();
//		List<Device> routeList = new LinkedList<Device>();
//		List<RouteMap> routeMap = new LinkedList<RouteMap>();
//		List<List<RouteMap>> routeMapList = new LinkedList<List<RouteMap>>();
//		// checkConnection(sourceDevice, devices, routeList,
//		// devices.get(destinationDeviceName), conDevice, visitedDevice);
//		checkConnection(devices,sourceDevice.getName(), destinationDeviceName );
//		List<Device> filteredConnection = conDevice.stream().distinct().collect(Collectors.toList());
//		if (!filteredConnection.isEmpty()) {
//			for (Device device : filteredConnection) {
//				System.out.println("conDevice route start " + device.getName());
//				getMap(sourceDevice.getName(), devices.get(destinationDeviceName), visitedDevice, routeMapList,
//						new LinkedList<RouteMap>(), device);
//				System.out.println("Route Map List inner");
//				routeMapList.forEach(System.out::println);
//				System.out.println("Route Map inner");
//				routeMap.forEach(System.out::println);
//				System.out.println("conDevice route end " + device.getName());
//			}
//			;
//			int messageStrength = sourceDevice.getSignalStrength();
//			StringBuilder builder = new StringBuilder(sourceDevice.getName() + "(" + messageStrength + ") -> ");
//			System.out.println("Route Map List");
//			routeMapList.forEach(System.out::println);
//			System.out.println("Route Map");
//			routeMap.forEach(System.out::println);
//
//			for (RouteMap router : routeMap) {
//
//				if (messageStrength == 0) {
//					break;
//				}
//				messageStrength--;
//				if (DeviceType.REPEATER.equals(router.getDeviceType())) {
//					messageStrength += messageStrength;
//				}
//				builder.append(router.getName() + "(" + messageStrength + ") -> ");
//			}
//			;
//			if (messageStrength >= 0) {
//				System.out.println("messageStrength " + messageStrength);
//				System.out.println("RoutePath " + builder.toString());
//				response.setError(false);
//				response.setMessage("ROUTE INFO PATH SUCCESS");
//
//			}
//
//		} else {
//			response.setErrorMessage("ROUTE INFO NOT FOUND");
//		}
//	}
//
//	private void getMap(String sourceName, Device destinationDevice, Map<String, Device> visitedDevice,
//			List<List<RouteMap>> routeMapList, List<RouteMap> routeMap, Device device) {
//		List<String> neighborList = device.getNeighbor();
//		System.out.println(device.getName() + " neighborList: " + neighborList);
//		for (String neighbour : neighborList) {
//			if (routeMap.isEmpty()) {
//				routeMap.add(new RouteMap(destinationDevice.getName(), destinationDevice.getSignalStrength(),
//						destinationDevice.getType()));
//				routeMap.add(new RouteMap(device.getName(), device.getSignalStrength(), device.getType()));
//			}
//			System.out.println("Device Name: " + device.getName() + "neighbourName: " + neighbour);
//			Device neighbourDevice = visitedDevice.get(neighbour);
//			routeMap.add(new RouteMap(neighbour, neighbourDevice.getSignalStrength(), neighbourDevice.getType()));
//			if (neighbour == sourceName) {
//				System.out.println("Neighbour Matched");
//				List<String> flattenedmap = routeMap.stream().map(x -> x.getName()).collect(Collectors.toList());
//				System.out.println("routeMap Matched: " + flattenedmap);
//				routeMap.add(new RouteMap(device.getName(), device.getSignalStrength(), device.getType()));
//				routeMap.add(new RouteMap(destinationDevice.getName(), destinationDevice.getSignalStrength(),
//						destinationDevice.getType()));
//				routeMapList.add(routeMap);
//				routeMap = new LinkedList<RouteMap>();
//				break;
//			}
//			System.out.println("Entering RouteMap");
//			getMap(sourceName, destinationDevice, visitedDevice, routeMapList, routeMap, neighbourDevice);
//
//		}
//		;
//	}
//
//	public void checkConnection(Device sourceDevice, Map<String, Device> devices, List<Device> routeList,
//			String destinationDeviceName, List<Device> conDeviceList, Map<String, Device> visitedDevice) {
//		visitedDevice.keySet().forEach(key -> System.out.println("visitedDeviceList: " + key));
//		Map<String, DeviceType> connectedDevices = sourceDevice.getConnectedDevices();
//		String sourceDeviceName = sourceDevice.getName();
//		if (!connectedDevices.isEmpty() && connectedDevices.containsKey(destinationDeviceName)) {
//			System.out.println("ConnectedDevice 1: " + sourceDeviceName);
//			if (!conDeviceList.contains(sourceDevice)) {
//				visitedDevice.put(sourceDeviceName, sourceDevice);
//				conDeviceList.add(sourceDevice);
//			}
//			System.out.println("+ + + + + + + + + + + ++ + + + + + + + + + + + + ++ + + + + + + + + + + + + + + ");
//		} else {
//			connectedDevices.keySet().forEach(name -> System.out
//					.println("connectedDevicesList for childDevice " + sourceDeviceName + ": " + name));
//			System.out.println("Devices Collected List: " + conDeviceList);
//			Set<String> filteredKeySet = connectedDevices.keySet().stream()
//					.filter(name -> !visitedDevice.containsKey(name)).collect(Collectors.toSet());
//			System.out.println("filteredKeySet" + filteredKeySet);
//			for (String name : filteredKeySet) {
//				Device childDevice = devices.get(name);
//				childDevice.getNeighbor().add(sourceDeviceName);
//				visitedDevice.put(name, childDevice);
//				Map<String, DeviceType> childconnectedDevices = childDevice.getConnectedDevices();
//				childconnectedDevices.keySet().forEach(
//						key -> System.out.println("childconnectedDevicesList for childDevice " + name + ": " + key));
//				if (!childconnectedDevices.isEmpty() && childconnectedDevices.containsKey(destinationDeviceName)) {
//					conDeviceList.add(childDevice);
//					System.out.println("ConnectedDevice 2: " + name);
//					System.out.println("- - - - - - - - - - - - - - - -- - - -- - - - - - -- - - -- -   ");
//					break;
//				}
//				System.out.println("* * * * * * * * * * * * * * * * * * * * * * * *  ");
//				checkConnection(childDevice, devices, routeList, destinationDeviceName, conDeviceList, visitedDevice);
//			}
//		}
//	}
//
//	public void checkConnection(Map<String, Device> devices, String sourceName, String destinationName) {
//
//		Device sourceDevice = devices.get(sourceName);
//		Device destinationDevice = devices.get(destinationName);
//
//		List<String> visitedDevice = new LinkedList<String>();
//
//		List<String> conDevice = new LinkedList<String>();
//
//		List<List<Device>> routeList = new LinkedList<List<Device>>();
//
//		Map<String, DeviceType> connectedDevices = sourceDevice.getConnectedDevices();
//		mapConnections(sourceDevice, visitedDevice, destinationName, devices, routeList);
//
//		/*
//		 * String sourceDeviceName = sourceDevice.getName(); if
//		 * (!connectedDevices.isEmpty() &&
//		 * connectedDevices.containsKey(destinationDevice)) {
//		 * System.out.println("ConnectedDevice 1: " + sourceDeviceName); if
//		 * (!conDeviceList.contains(sourceDevice)) { visitedDevice.put(sourceDeviceName,
//		 * sourceDevice); conDeviceList.add(sourceDevice); } System.out.
//		 * println("+ + + + + + + + + + + ++ + + + + + + + + + + + + ++ + + + + + + + + + + + + + + "
//		 * ); } else { connectedDevices.keySet().forEach(name -> System.out
//		 * .println("connectedDevicesList for childDevice " + sourceDeviceName + ": " +
//		 * name)); System.out.println("Devices Collected List: " + conDeviceList);
//		 * Set<String> filteredKeySet = connectedDevices.keySet().stream() .filter(name
//		 * -> !visitedDevice.containsKey(name)).collect(Collectors.toSet());
//		 * System.out.println("filteredKeySet" + filteredKeySet); for (String name :
//		 * filteredKeySet) { Device childDevice = devices.get(name);
//		 * childDevice.getNeighbor().add(sourceDeviceName); visitedDevice.put(name,
//		 * childDevice); Map<String, DeviceType> childconnectedDevices =
//		 * childDevice.getConnectedDevices(); childconnectedDevices.keySet().forEach(
//		 * key -> System.out.println("childconnectedDevicesList for childDevice " + name
//		 * + ": " + key)); if (!childconnectedDevices.isEmpty() &&
//		 * childconnectedDevices.containsKey(destinationDevice)) {
//		 * conDeviceList.add(childDevice); System.out.println("ConnectedDevice 2: " +
//		 * name); System.out.
//		 * println("- - - - - - - - - - - - - - - -- - - -- - - - - - -- - - -- -   ");
//		 * break; }
//		 * System.out.println("* * * * * * * * * * * * * * * * * * * * * * * *  ");
//		 * checkConnection(childDevice, devices, routeList, destinationDevice,
//		 * conDeviceList, visitedDevice); } }
//		 */
//	}
//
////	private void mapConnections(Device sourceDevice, String destinationName, Map<String, Device> devices,
////			List<List<Device>> routeList) {
////		map1(sourceDevice, new LinkedList<String>(), destinationName, devices, routeList);
////	
////
////	}
//	
//	private void mapConnections(Device sourceDevice, List<String> visitedDevice, String destinationName, Map<String, Device> devices, List<List<Device>> routeList) {
//		
//		visitedDevice.add(sourceDevice.getName());
//		Map<String, DeviceType> connectedDevices = sourceDevice.getConnectedDevices();
//		if (!connectedDevices.isEmpty()) {
//			if (connectedDevices.containsKey(destinationName)) {
//				visitedDevice.add(destinationName);
//			} else {
//				for (String deviceName : connectedDevices.keySet()) {
//					if (visitedDevice.contains(deviceName))
//						continue;
//					Device childDevice = devices.get(deviceName);
//					mapConnections(childDevice, visitedDevice, destinationName, devices, routeList);
//				}
//
//			}
//		}
//		List<Device> visitedDevices = visitedDevice.stream().map(device -> devices.get(device))
//				.collect(Collectors.toList());
//		routeList.add(visitedDevices);
//		visitedDevice = new LinkedList<String>();
//	}

}
