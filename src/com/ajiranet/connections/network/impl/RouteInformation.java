package com.ajiranet.connections.network.impl;

import java.util.Map;
import java.util.regex.Matcher;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.DeviceType;
import com.ajiranet.connections.model.Response;
import com.ajiranet.connections.network.Network;

public class RouteInformation implements Network{
	
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
			if(null==sourceDevice|| null==destinationDevice) {
				response.setErrorMessage("Error: DEVICE NOT FOUND");
				break;
			}
			Map<String, DeviceType> parentConnectedDevices = sourceDevice.getConnectedDevices();
			Map<String, DeviceType> destinationConnectedDevices = destinationDevice.getConnectedDevices();
			///Map<String, DeviceType> combinedDevices = 
			if(sourceDevice.getSignalStrength()<=0 && destinationDevice.getSignalStrength()<=0) {
				response.setErrorMessage("Error: SIGNAL STRENGTH WEAK");
				break;
			}
			if(null==parentConnectedDevices|| null==destinationConnectedDevices) {
				response.setErrorMessage("Error: CONNECTIONS DOES NOT EXIST");
				break;
			}
			if(parentConnectedDevices.containsKey(destinationDeviceName)) {
				response.setError(false);
				response.setMessage("ROUTE INFO PATH SUCCESS");
				break;
			}else {
				response.setErrorMessage("ROUTE INFO NOT FOUND");
			}
			
			//
			break;
		}
		response.setErrorMessage(response.isPatternMatched()? Constants.INVALID_COMMAND_SYNTAX_MSG + Constants.ROUTE_INFORMATION_OPTION
				: response.getErrorMessage());
		return response;
	}

}
