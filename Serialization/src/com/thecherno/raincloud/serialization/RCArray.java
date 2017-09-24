package com.thecherno.raincloud.serialization;

import static com.thecherno.raincloud.serialization.SerializationUtils.readBooleans;
import static com.thecherno.raincloud.serialization.SerializationUtils.readByte;
import static com.thecherno.raincloud.serialization.SerializationUtils.readBytes;
import static com.thecherno.raincloud.serialization.SerializationUtils.readChars;
import static com.thecherno.raincloud.serialization.SerializationUtils.readDoubles;
import static com.thecherno.raincloud.serialization.SerializationUtils.readFloats;
import static com.thecherno.raincloud.serialization.SerializationUtils.readInteger;
import static com.thecherno.raincloud.serialization.SerializationUtils.readIntegers;
import static com.thecherno.raincloud.serialization.SerializationUtils.readLongs;
import static com.thecherno.raincloud.serialization.SerializationUtils.readShorts;
import static com.thecherno.raincloud.serialization.SerializationUtils.readString;
import static com.thecherno.raincloud.serialization.SerializationUtils.writeBytes;

public class RCArray extends RCBase {

	public static final byte CONTAINER_TYPE = ContainerType.ARRAY;
	private final byte type;

	private byte[] byteData;
	private short[] shortData;
	private char[] charData;
	private int[] intData;
	private long[] longData;
	private float[] floatData;
	private double[] doubleData;
	private boolean[] booleanData;
	
	private RCArray(byte type) {
		super(1 + 1 + 4 + 4);
		this.type = type;
	}
	
	public int getSize(){
		return getBaseSize() + getDataSize();
	}
	
	public int getCount(){
		return getDataSize() / Type.getSize(type);
	}
	
	public int getDataSize(){
		switch (type) {
		case Type.BYTE:		return byteData.length 		* Type.getSize(Type.BYTE);
		case Type.SHORT:	return shortData.length 	* Type.getSize(Type.SHORT);
		case Type.CHAR:		return charData.length 		* Type.getSize(Type.CHAR);
		case Type.INTEGER:	return intData.length 		* Type.getSize(Type.INTEGER);
		case Type.LONG:		return longData.length 		* Type.getSize(Type.LONG);
		case Type.FLOAT:	return floatData.length 	* Type.getSize(Type.FLOAT);
		case Type.DOUBLE:	return doubleData.length 	* Type.getSize(Type.DOUBLE);
		case Type.BOOLEAN:	return booleanData.length 	* Type.getSize(Type.BOOLEAN);
		}
		return 0;
	}
	
	public Object getData() {
		switch (type) {
		case Type.BYTE:		return byteData;
		case Type.SHORT:	return shortData;
		case Type.CHAR:		return charData;
		case Type.INTEGER:	return intData;
		case Type.LONG:		return longData;
		case Type.FLOAT:	return floatData;
		case Type.DOUBLE:	return doubleData;
		case Type.BOOLEAN:	return booleanData;
		}
		return null;
	}
	
	public void setData(Object data){
		try {
			switch (type) {
			case Type.BYTE:		byteData	= (byte[]) data;	break;
			case Type.SHORT:	shortData	= (short[]) data;   break;
			case Type.CHAR:		charData    = (char[]) data;    break;
			case Type.INTEGER:	intData     = (int[]) data;     break;
			case Type.LONG:		longData    = (long[]) data;    break;
			case Type.FLOAT:	floatData   = (float[]) data;   break;
			case Type.DOUBLE:	doubleData  = (double[]) data;  break;
			case Type.BOOLEAN:	booleanData = (boolean[]) data; break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getBytes(byte[] dest, int pointer){
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, getSize());
		pointer = writeBytes(dest, pointer, getCount());
		
		switch (type) {
		case Type.BYTE:
			pointer = writeBytes(dest, pointer, byteData);
			break;
		case Type.SHORT:
			pointer = writeBytes(dest, pointer, shortData);
			break;
		case Type.CHAR:
			pointer = writeBytes(dest, pointer, charData);
			break;
		case Type.INTEGER:
			pointer = writeBytes(dest, pointer, intData);
			break;
		case Type.LONG:
			pointer = writeBytes(dest, pointer, longData);
			break;
		case Type.FLOAT:
			pointer = writeBytes(dest, pointer, floatData);
			break;
		case Type.DOUBLE:
			pointer = writeBytes(dest, pointer, doubleData);
			break;
		case Type.BOOLEAN:
			pointer = writeBytes(dest, pointer, booleanData);
			break;
		}
		
		return pointer;
	}

	public static RCArray Deserialize(byte[] src, int pointer) {
		assert (readByte(src, pointer) == CONTAINER_TYPE);
		pointer++;

		String name = readString(src, pointer);
		pointer += name.length() + 2;

		byte type = readByte(src, pointer++);

		int size = readInteger(src, pointer);
		pointer += 4;
		
		// TODO: skip
		
		int count = readInteger(src, pointer);
		pointer += 4;
		
		Object data = null;
		switch (type) {
		case Type.BYTE:
			data = readBytes(src, pointer, count);
			break;
		case Type.SHORT:
			data = readShorts(src, pointer, count);
			break;
		case Type.CHAR:
			data = readChars(src, pointer, count);
			break;
		case Type.INTEGER:
			data = readIntegers(src, pointer, count);
			break;
		case Type.LONG:
			data = readLongs(src, pointer, count);
			break;
		case Type.FLOAT:
			data = readFloats(src, pointer, count);
			break;
		case Type.DOUBLE:
			data = readDoubles(src, pointer, count);
			break;
		case Type.BOOLEAN:
			data = readBooleans(src, pointer, count);
			break;
		}
		
		return Create(name, data, type);
	}
	
	@Override
	public String toString() {
		return "RCArray [name=" + name.length() + "\"" + getName() + "\", size=" + getSize()
			+ getCount() + "-" + Type.getTypeName(type) + "S\n" + "]";
	}
	
	private static RCArray Create(String name, Object data, byte type){
		RCArray array = new RCArray(type);
		array.setName(name);
		array.setData(data);
		return array;
	}

	public static RCArray Byte(String name, byte[] data){
		return Create(name, data, Type.BYTE);
	}
	
	public static RCArray Short(String name, short[] data){
		return Create(name, data, Type.SHORT);
	}
	
	public static RCArray Char(String name, char[] data){
		return Create(name, data, Type.CHAR);
	}
	
	public static RCArray Integer(String name, int[] data){
		return Create(name, data, Type.INTEGER);
	}
	
	public static RCArray Long(String name, long[] data){
		return Create(name, data, Type.LONG);
	}
	
	public static RCArray Flaot(String name, float[] data){
		return Create(name, data, Type.FLOAT);
	}
	
	public static RCArray Double(String name, double[] data){
		return Create(name, data, Type.DOUBLE);
	}
	
	public static RCArray Boolean(String name, boolean[] data){
		return Create(name, data, Type.BOOLEAN);
	}

	protected void getStrukture(StringBuilder builder, int i) {
		super.getStrukture(builder, i);
		builder.append(getCount() + "-" + Type.getTypeName(type) + "S\n");
	}
	
}
