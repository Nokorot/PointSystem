package com.thecherno.raincloud.serialization;

import static com.thecherno.raincloud.serialization.SerializationUtils.readByte;
import static com.thecherno.raincloud.serialization.SerializationUtils.readInteger;
import static com.thecherno.raincloud.serialization.SerializationUtils.readShort;
import static com.thecherno.raincloud.serialization.SerializationUtils.readString;
import static com.thecherno.raincloud.serialization.SerializationUtils.writeBytes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RCDatabase extends RCBase {
	
	public static final byte[] HEADER = "RCDB".getBytes();
	public static final short VERSION = 0x0100;
	
	public static final byte CONTAINER_TYPE = ContainerType.DATABASE;
	
	private short objectCount;
	private Map<String, RCObject> objects = new HashMap<String, RCObject>();
	
	private int dataSize;

	private RCDatabase() {
		super(HEADER.length + 2 + 1 + 4 + 2);
	}

	public RCDatabase(String name){
		this();
		setName(name);
	}
	
	public int getSize(){
		return getBaseSize() + dataSize;
	}
	
	public int getDataSize() {
		return dataSize;
	}

	public void addObject(RCObject object) {
		objects.put(object.getName(), object);
		dataSize += object.getSize();
		
		objectCount = (short) objects.size();
	}
	
	public RCObject getObject(String name) {
//		if (!objects.containsKey(name))
//			addObject(new RCObject(name));
		return objects.get(name);
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, HEADER);
		pointer = writeBytes(dest, pointer, VERSION);
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		pointer = writeBytes(dest, pointer, objectCount);
		for (RCObject object : objects.values())
			pointer = object.getBytes(dest, pointer);
	
		return pointer;
	}

	public Collection<RCObject> getObjects() {
		return objects.values();
	}

	public Map<String, RCObject> getData() {
		return objects;
	}
	
	public byte[] Serialize() {
		byte[] data = new byte[getSize()];
		getBytes(data, 0);
		return data;
	}
	
	public void SerializeToFile(String path) {
		byte[] data = Serialize();
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path + ".rcd"));
			stream.write(data);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RCDatabase Deserialize(byte[] data) {
		int pointer = 0;
		assert(readString(data, pointer, HEADER.length).equals(HEADER));
		pointer += HEADER.length;
		
		if(readShort(data, pointer) != VERSION){
			System.err.println("RC Invalide RCBD Version.");
			return null;
		}
		pointer += 2;

		assert(readByte(data, pointer) == CONTAINER_TYPE);
		pointer++;
		
		RCDatabase result = new RCDatabase();
		
		result.name = readString(data, pointer);
		pointer += result.name.length() + 2;
		
		result.size = readInteger(data, pointer);
		pointer += 4;
		
		int objectCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < objectCount; i++){
			RCObject object = RCObject.Deserialize(data, pointer);
			pointer += object.getSize();
			result.addObject(object);
		}

		return result;
	}
	
	public static RCDatabase DeserializeFromFile(String path) {
		byte[] buffer = null;
		try {
			if(!path.endsWith(".rcd"))
				path += ".rcd";
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
			buffer = new byte[stream.available()];
			stream.read(buffer);
			stream.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return Deserialize(buffer);
	}

	protected void getStrukture(StringBuilder builder, int i) {
		super.getStrukture(builder, i++);
		
		for (RCObject o : getObjects()) 
			o.getStrukture(builder, i);
	}

}
