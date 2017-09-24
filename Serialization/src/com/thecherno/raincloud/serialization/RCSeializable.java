package com.thecherno.raincloud.serialization;

public interface RCSeializable {

	public abstract RCObject serialize(String name);
	
	public abstract RCSeializable deserialize(RCObject object);
	
}
