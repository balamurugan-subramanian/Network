package com.ajiranet.connections.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NodeOld<T> {

    private T value;
    private DeviceType type;
    private int signalStrength = 0;
    private Set<NodeOld<T>> neighbors;

    public NodeOld(T value, DeviceType type) {
        this.value = value;
        this.type = type;
		this.signalStrength=DeviceType.REPEATER.equals(type)?0:5;
        this.neighbors = new HashSet<>();
    }

    public T getValue() {
        return value;
    }
    
    public DeviceType getType() {
        return type;
    }
    
    public int getStrength() {
        return signalStrength;
    }
    
    public void setStrength(int strength){
    	if(DeviceType.REPEATER.equals(this.type)) {
    		throw new IllegalArgumentException("Can't set strength to Repeater");
    	}	
        this.signalStrength=strength;
    } 

    public Set<NodeOld<T>> getNeighbors() {
        return Collections.unmodifiableSet(neighbors);
    }

    public void connect(NodeOld<T> node) {
        if (this == node) throw new IllegalArgumentException("Can't connect node to itself");
        this.neighbors.add(node);
        node.neighbors.add(this);
    }

}
