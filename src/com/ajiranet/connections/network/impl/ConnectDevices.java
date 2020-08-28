package com.ajiranet.connections.network.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.DeviceType;
import com.ajiranet.connections.model.Node;
import com.ajiranet.connections.model.Response;
import com.ajiranet.connections.network.Network;
import com.ajiranet.connections.util.Utils;

public class ConnectDevices implements Network {
	
	private static Logger logger = LoggerFactory.getLogger(ConnectDevices.class);

	@Override
	public Response process(String menuOption, Map<String, Node<Device>> devices) {
		Response response = new Response(false, true, "failed", null);
		Matcher matcher = Constants.CONNECT_DEVICE_PATTERN.matcher(menuOption.toUpperCase());

		while (matcher.find()) {
			response.setPatternMatched(true);

			String parentDeviceName = matcher.group(2).trim();

			String deviceList = matcher.group(3).trim();
			System.out.println(String.format("Parent device name: %S Device List %s", parentDeviceName, deviceList));
			String[] devicesArray = deviceList.split(Constants.WHITE_SPACE_STRING);

			Node<Device> parentDevice = devices.get(parentDeviceName);

			if (null != parentDevice && !deviceList.isEmpty()) {
				System.out.println("Parent Device retreived " + parentDevice);
				//Set<Node<Device>> connectedDeviceSet = parentDevice.getNeighbors();

				List<String> devicesList = Arrays.asList(devicesArray);

				String sameDeviceError = "";
				String deviceNotFoundError = "";
				String connectionExistsError = "";
				String connectedDevices = "";

				for (String deviceName : devicesList) {
					if (parentDeviceName != deviceName) {
						Node<Device> childDevice = devices.get(deviceName);
						if (childDevice == null) {
							deviceNotFoundError = Utils.concat(deviceNotFoundError, deviceName);
							continue;
						}
						try {
							parentDevice.connect(childDevice);
						}catch(IllegalArgumentException e) {
							logger.error(deviceName+ ": "+e.getMessage());
							continue;
						}
							connectedDevices = Utils.concat(connectedDevices, deviceName);

					} else {
						sameDeviceError = Utils.concat(sameDeviceError, deviceName);
					}
				}
				;

				response.setError(!sameDeviceError.isEmpty() || !deviceNotFoundError.isEmpty());

				response.setErrorMessage(String.format(Constants.CONNECT_ERROR_MSG, connectionExistsError,
						sameDeviceError, deviceNotFoundError));
				response.setMessage(String.format(Constants.CONNECTED_WITH_MSG, parentDeviceName, connectedDevices));

				// devices.put(parentDeviceName, parentDevice);

				Utils.printConnections(devices);

			} else {
				response.setErrorMessage(String.format(Constants.DEVICE_NOT_FOUND_MSG, parentDeviceName));
			}
			break;
		}
		response.setErrorMessage(
				!response.isPatternMatched() ? Constants.INVALID_COMMAND_SYNTAX_MSG + Constants.CONNECT_DEVICES_OPTION
						: response.getErrorMessage());

		return response;

	}

}
