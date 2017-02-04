package com.thecherno.raincloud.serialization;

import static com.thecherno.raincloud.serialization.SerializationUtils.readByte;
import static com.thecherno.raincloud.serialization.SerializationUtils.readInteger;
import static com.thecherno.raincloud.serialization.SerializationUtils.readShort;
import static com.thecherno.raincloud.serialization.SerializationUtils.readString;
import static com.thecherno.raincloud.serialization.SerializationUtils.writeBytes;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class RCObject extends RCBase {
	
	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
	
	private short fieldCount;
	SortedMap<String, RCField> fields = new TreeMap<String, RCField>();
	private short stringCount;
	private SortedMap<String, RCString> strings 		= new TreeMap<String, RCString>();
	private short arrayCount;
	private SortedMap<String, RCArray> arrays 		= new TreeMap<String, RCArray>();
	private short subObjectCount;
	private SortedMap<String, RCObject> subObjects	= new TreeMap<String, RCObject>();

	private int dataSize;
	
	private RCObject(){
		super(1 + 4 + 2 + 2 + 2 + 2);
	}
	
	public RCObject(String name){
		this();
		setName(name);
	}
	
	public int getSize() {
		return getBaseSize() + dataSize;
	}
	
	public int getDataSize(){
		return dataSize;
	}
	
	public void addField(RCField field) {
		fields.put(field.getName(), field);
		dataSize += field.getSize();
		
		fieldCount = (short) fields.size();
	}

	public RCField getField(String name) {
		return fields.get(name);
	}

	public void addString(RCString string) {
		strings.put(string.getName(), string);
		dataSize += string.getSize();
		
		stringCount = (short) strings.size();
	}

	public String getString(String name) {
		if (!strings.containsKey(name))
			return null;
		return strings.get(name).getData();
	}

	public void addArray(RCArray array) {
		arrays.put(array.getName(), array);
		dataSize += array.getSize();

		arrayCount = (short) arrays.size();
	}

	public RCArray getArray(String name) {
		return arrays.get(name);
	}

	public void addSubObject(RCObject subObject) {
		subObjects.put(subObject.getName(), subObject);
		dataSize += subObject.getSize();

		subObjectCount = (short) subObjects.size();
	}

	public RCObject getSubObject(String name) {
		if(!subObjects.containsKey(name))
			return new RCObject(name);
		return subObjects.get(name);
	}

	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);

		pointer = writeBytes(dest, pointer, fieldCount);
		for (RCField field : fields.values()){
			pointer = field.getBytes(dest, pointer);
		}

		pointer = writeBytes(dest, pointer, stringCount);
		for (RCString string : strings.values()){
			pointer = string.getBytes(dest, pointer);
		}
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for (RCArray array : arrays.values()){
			pointer = array.getBytes(dest, pointer);
		}
		
		pointer = writeBytes(dest, pointer, subObjectCount);
		for (RCObject subObject : subObjects.values())
			pointer = subObject.getBytes(dest, pointer);
		
		return pointer;
	}

	public static RCObject Deserialize(byte[] src, int pointer) {
		assert(readByte(src, pointer) == CONTAINER_TYPE);
		pointer++;
		
		RCObject result = new RCObject();
		
		result.name = readString(src, pointer);
		pointer += result.name.length() + 2;
		
		result.size = readInteger(src, pointer);
		pointer += 4;
		
		short fieldCount = readShort(src, pointer);
		pointer += 2;
		for (int i = 0; i < fieldCount; i++){
			RCField field = RCField.Deserialize(src, pointer);
			pointer += field.getSize();
			result.addField(field);
		}
		
		short stringCount = readShort(src, pointer);
		pointer += 2;
		for (int i = 0; i < stringCount; i++){
			RCString string = RCString.Deserialize(src, pointer);
			pointer += string.getSize();
			result.addString(string);
		}
		
		short arrayCount = readShort(src, pointer);
		pointer += 2;
		for (int i = 0; i < arrayCount; i++){
			RCArray array = RCArray.Deserialize(src, pointer);
			pointer += array.getSize();
			result.addArray(array);
		}
		
		short subObjectCount = readShort(src, pointer);
		pointer += 2;
		for (int i = 0; i < subObjectCount; i++){
			RCObject subObject = RCObject.Deserialize(src, pointer);
			pointer += subObject.getSize();
			result.addSubObject(subObject);
		}
		
		return result;
	}
	
	public RCBase getData() {
		return this;
	}
	
	public final Collection<RCField> getFields() {
		return fields.values();
	}
	
	public final Collection<RCString> getStrings() {
		return strings.values();
	}
	
	public final Collection<RCArray> getArrays() {
		return arrays.values();
	}
	
	public final Collection<RCObject> getSubObjects() {
		return subObjects.values();
	}

	protected void getStrukture(StringBuilder builder, int i) {
		super.getStrukture(builder, i++);
		
		for (RCField f : getFields()) 
			f.getStrukture(builder, i);
		for (RCString s : getStrings()) 
			s.getStrukture(builder, i);
		for (RCArray a : getArrays()) 
			a.getStrukture(builder, i);
		for (RCObject o : getSubObjects()) 
			o.getStrukture(builder, i);
	}

	public void addString(String string, String data) {
		addString(new RCString(string, data));
	}

	public void addInteger(String key, int value) {
		addField(RCField.Integer(key, value));
	}

	public void addFloat(String key, float value) {
		addField(RCField.Float(key, value));
	}
	
	public void addBoolean(String string, boolean value) {
		addField(RCField.Booelan(string, value));
	}

	public void addObject(String name, RCObject save) {
		save.setName(name);
		addSubObject(save);
	}

	public int getInteger(String key) {
		if(!fields.containsKey(key)) return -1;
		return getField(key).intData();
	}
	
	public float getFloat(String key) {
		if(!fields.containsKey(key)) return -1;
		return getField(key).floatData();
	}

	public boolean getBoolean(String key) {
		if(!fields.containsKey(key)) return false;
		return getField(key).booleanData();
	}


	
}
