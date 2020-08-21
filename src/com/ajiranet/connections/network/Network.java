package com.ajiranet.connections.network;

import java.util.Map;

import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.Response;

public interface Network {
	
	public Response process(String menuOption, Map<String, Device> devices);

}
