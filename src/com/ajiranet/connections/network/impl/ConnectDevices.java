package com.ajiranet.connections.network.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.DeviceType;
import com.ajiranet.connections.model.Response;
import com.ajiranet.connections.network.Network;
import com.ajiranet.connections.util.Utils;

public class ConnectDevices implements Network {

	@Override
	public Response process(String menuOption, Map<String, Device> devices) {
		Response response = new Response(false, true, "failed", null);
		Matcher matcher = Constants.CONNECT_DEVICE_PATTERN.matcher(menuOption.toUpperCase());

		while (matcher.find()) {
			response.setPatternMatched(true);

			String parentDeviceName = matcher.group(2).trim();

			String deviceList = matcher.group(3).trim();
			System.out.println(String.format("Parent device name: %S Device List %s", parentDeviceName, deviceList));
			String[] devicesArray = deviceList.split(Constants.WHITE_SPACE_STRING);

			Device parentDevice = devices.get(parentDeviceName);

			if (null != parentDevice && !deviceList.isEmpty()) {
				System.out.println("Parent Device retreived " + parentDevice);
				Map<String, DeviceType> connectedDeviceSet = parentDevice.getConnectedDevices();

				List<String> devicesList = Arrays.asList(devicesArray);

				String sameDeviceError = "";
				String deviceNotFoundError = "";
				String connectionExistsError = "";
				String connectedDevices = "";

				for (String deviceName : devicesList) {
					if (parentDeviceName != deviceName) {
						Device childDevice = devices.get(deviceName);
						if (childDevice == null) {
							deviceNotFoundError = Utils.concat(deviceNotFoundError, deviceName);
							continue;
						}
						Map<String, DeviceType> childConnectedDevices = childDevice.getConnectedDevices();
						if (connectedDeviceSet.containsKey(deviceName)) {
							connectionExistsError = Utils.concat(connectionExistsError, deviceName);
							System.out.println(String.format("Error: Connection already exists between %s and %s",
									parentDeviceName, deviceName));
							continue;
						}
							connectedDeviceSet.put(childDevice.getName(), childDevice.getType());
							childConnectedDevices.put(parentDeviceName, parentDevice.getType());
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
