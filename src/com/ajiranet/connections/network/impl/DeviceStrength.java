package com.ajiranet.connections.network.impl;

import java.util.Map;
import java.util.regex.Matcher;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.DeviceType;
import com.ajiranet.connections.model.Response;
import com.ajiranet.connections.network.Network;

public class DeviceStrength implements Network{
	

		@Override
		public Response process(String menuOption, Map<String, Device> devices) {
			Response response = new Response(false, true, "failed", null);
			Matcher matcher = Constants.SIGNAL_STRENGTH_PATTERN.matcher(menuOption.toUpperCase());
			
			while (matcher.find()) {
				System.out.println("Pattern Matched");
				response.setPatternMatched(true);
				String parentDeviceName = matcher.group(2).trim();
				int signalStrength = Integer.parseInt(matcher.group(3));
				
				Device parentDevice = devices.get(parentDeviceName);
				
				
				if (null != parentDevice) {
					if(DeviceType.REPEATER.equals(parentDevice.getType())){
						response.setErrorMessage(String.format(Constants.SIGNAL_STRENGTH_DEVICE_UNSUPPORTED, parentDeviceName));
						break;
					}
					parentDevice.setSignalStrength(signalStrength);
					response.setError(false);
					response.setMessage(String.format(Constants.SIGNAL_STRENGTH_UPDATED, parentDeviceName));
				}else {
					response.setErrorMessage(String.format(Constants.SIGNAL_STRENGTH_DEVICE_NOT_FOUND, parentDeviceName));
				}
				break;
			}
			
			response.setErrorMessage(
					!response.isPatternMatched() ? Constants.INVALID_COMMAND_SYNTAX_MSG + Constants.SET_DEVICE_STRENGTH_OPTION
							: response.getErrorMessage());

			return response;
		}

}
