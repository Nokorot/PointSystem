package com.thecherno.raincloud.serialization;

import static com.thecherno.raincloud.serialization.SerializationUtils.*;

public class RCField extends RCBase {

	public static final byte CONTAINER_TYPE = ContainerType.FIELD;
	private final byte type;
	private byte[] data;

	private RCField(String name, Object data, byte type) {
		super(1 + 1);
		this.type = type;
		setName(name);
		setData(data);
	}

	public int getSize() {
		assert (data.length == Type.getSize(type));
		return getBaseSize() + getDataSize();
	}
	
	public int getDataSize(){
		return data.length;
	}

	public void setData(Object data){
		this.data = new byte[Type.getSize(type)];
		if(data instanceof byte[]){
			writeBytes(this.data, 0, (byte[]) data);
			return;
		}
		
		try {
			switch (type) {
			case Type.BYTE:		writeBytes(this.data, 0, (byte) data);		break;
			case Type.SHORT:	writeBytes(this.data, 0, (short) data);		break;
			case Type.CHAR:		writeBytes(this.data, 0, (char) data);		break;
			case Type.INTEGER:	writeBytes(this.data, 0, (int) data);		break;
			case Type.LONG:		writeBytes(this.data, 0, (long) data);		break;
			case Type.FLOAT:	writeBytes(this.data, 0, (float) data);		break;
			case Type.DOUBLE:	writeBytes(this.data, 0, (double) data);	break;
			case Type.BOOLEAN:	writeBytes(this.data, 0, (boolean) data);	break;
			}
			
		}catch(ClassCastException e){
			e.printStackTrace();
		}
	}
	
	public void setData(byte[] data){
		assert(data.length == Type.getSize(type));
		this.data = new byte[Type.getSize(type)];
		writeBytes(this.data, 0, data);
	}
	
	public Object getData() {
		switch (type) {
		case Type.BYTE:
			return byteData();
		case Type.SHORT:
			return shortData();
		case Type.CHAR:
			return charData();
		case Type.INTEGER:
			return intData();
		case Type.LONG:
			return longData();
		case Type.FLOAT:
			return floatData();
		case Type.DOUBLE:
			return doubleData();
		case Type.BOOLEAN:
			return booleanData();
		}
		return null;
	}

	public byte byteData() {
		return readByte(data, 0);
	}

	public short shortData() {
		return readShort(data, 0);
	}

	public char charData() {
		return readChar(data, 0);
	}

	public int intData() {
		return readInteger(data, 0);
	}

	public long longData() {
		return readLong(data, 0);
	}

	public float floatData() {
		return readFloat(data, 0);
	}

	public double doubleData() {
		return readDouble(data, 0);
	}

	public boolean booleanData() {
		return readBoolean(data, 0);
	}

	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, data);
		return pointer;
	}

	public static RCField Deserialize(byte[] src, int pointer) {
		assert (readByte(src, pointer) == CONTAINER_TYPE);
		pointer += 1;

		String name = readString(src, pointer);
		pointer += name.length() + 2;

		byte type = readByte(src, pointer);
		pointer += 1;
		
		byte[] data = readBytes(src, pointer, Type.getSize(type));

		return new RCField(name, data, type);
	}

	@Override
	public String toString() {
		return "RCField [name=" + name.length() + "\"" + getName() + "\", type=" + type + ", data="
				+ getData() + "]";
	}
	
	public static RCField Byte(String name, byte value) {
		return new RCField(name, value, Type.BYTE);
	}

	public static RCField Short(String name, short value) {
		return new RCField(name, value, Type.SHORT);
	}

	public static RCField Char(String name, char value) {
		return new RCField(name, value, Type.CHAR);
	}

	public static RCField Integer(String name, int value) {
		return new RCField(name, value, Type.INTEGER);
	}

	public static RCField Long(String name, long value) {
		return new RCField(name, value, Type.LONG);
	}

	public static RCField Float(String name, float value) {
		return new RCField(name, value, Type.FLOAT);
	}

	public static RCField Double(String name, double value) {
		return new RCField(name, value, Type.DOUBLE);
	}

	public static RCField Booelan(String name, boolean value) {
		return new RCField(name, value, Type.BOOLEAN);
	}

	protected void getStrukture(StringBuilder builder, int i) {
		super.getStrukture(builder, i);
		builder.append(getData());
	}

}
