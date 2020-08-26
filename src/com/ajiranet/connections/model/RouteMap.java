package com.ajiranet.connections.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @ToString
public class RouteMap {
	String name;
	int strength;
	DeviceType deviceType;
}
