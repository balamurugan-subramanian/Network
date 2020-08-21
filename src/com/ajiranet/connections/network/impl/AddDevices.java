package com.ajiranet.connections.network.impl;

import java.util.Map;
import java.util.regex.Matcher;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.Response;
import com.ajiranet.connections.network.Network;
import com.ajiranet.connections.util.Utils;

public class AddDevices implements Network{
	
	@Override
	public Response process(String menuOption, Map<String, Device> devices) {
		Response response = new Response(false, true, "failed", null);

		Matcher matcher = Constants.ADD_DEVICE_PATTERN.matcher(menuOption);

		while (matcher.find()) {
			String deviceName = null, devicetype = null;
			devicetype = matcher.group(2).trim();
			deviceName = matcher.group(3).trim();
			System.out.println("Device Type:" + devicetype);
			if (deviceName.matches(Constants.ALPHABET_DETECTOR_PATTERN)) {
				response.setPatternMatched(true);
				try {
					if (null != devices.get(deviceName)) {
						response.setErrorMessage(String.format(Constants.DEVICE_NAME_ALREADY_EXISTS_MSG, deviceName));
					} else {
						devices.put(deviceName, new Device(deviceName, devicetype));
						response.setError(false);
						response.setMessage(String.format(Constants.SUCCESSFULLY_ADDED_MSG, deviceName));
					}
				} catch (Exception e) {
					response.setErrorMessage(
							String.format(Constants.ERROR_ADDING_DEVICE_MSG, deviceName, e.getMessage()));
				}

			}
			
			Utils.printConnections(devices);
			break;
		}
		response.setErrorMessage(
				!response.isPatternMatched() ? String.format("Error: Invalid command syntax %s", Constants.ADD_A_DEVICE_OPTION)
						: response.getErrorMessage());
		return response;

	}

}
