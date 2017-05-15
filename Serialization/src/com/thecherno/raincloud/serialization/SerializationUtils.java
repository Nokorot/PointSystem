package com.thecherno.raincloud.serialization;

import java.nio.ByteBuffer;

public class SerializationUtils {

	public static int writeBytes(byte[] dest, int pointer, byte[] src){
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++) 
			dest[pointer++] = src[i];
		return pointer;
	}

	public static int writeBytes(byte[] dest, int pointer, char[] src){
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++) 
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}

	public static int writeBytes(byte[] dest, int pointer, short[] src){
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++) 
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}

	public static int writeBytes(byte[] dest, int pointer, int[] src){
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++) 
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}

	public static int writeBytes(byte[] dest, int pointer, long[] src){
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++) 
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}

	public static int writeBytes(byte[] dest, int pointer, float[] src){
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++) 
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}

	public static int writeBytes(byte[] dest, int pointer, double[] src){
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++) 
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}

	public static int writeBytes(byte[] dest, int pointer, boolean[] src){
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++) 
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, byte value) {
		assert(dest.length > pointer + Type.getSize(Type.BYTE));
		dest[pointer++] = value;
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, short value) {
		assert(dest.length > pointer + Type.getSize(Type.SHORT));
		dest[pointer++] = (byte) ((value >> 8) & 0xff);
		dest[pointer++] = (byte) ((value >> 0) & 0xff);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, char value) {
		assert(dest.length > pointer + Type.getSize(Type.CHAR));
		dest[pointer++] = (byte) ((value >> 8) & 0xff);
		dest[pointer++] = (byte) ((value >> 0) & 0xff);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, int value) {
		assert(dest.length > pointer + Type.getSize(Type.INTEGER));
		dest[pointer++] = (byte) ((value >> 24) & 0xff);
		dest[pointer++] = (byte) ((value >> 16) & 0xff);
		dest[pointer++] = (byte) ((value >> 8) & 0xff);
		dest[pointer++] = (byte) ((value >> 0) & 0xff);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, long value) {
		dest[pointer++] = (byte) ((value >> 56) & 0xff);
		dest[pointer++] = (byte) ((value >> 48) & 0xff);
		dest[pointer++] = (byte) ((value >> 40) & 0xff);
		dest[pointer++] = (byte) ((value >> 32) & 0xff);
		dest[pointer++] = (byte) ((value >> 24) & 0xff);
		dest[pointer++] = (byte) ((value >> 16) & 0xff);
		dest[pointer++] = (byte) ((value >> 8) & 0xff);
		dest[pointer++] = (byte) ((value >> 0) & 0xff);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, float value){
		assert(dest.length > pointer + Type.getSize(Type.FLOAT));
		int data = Float.floatToIntBits(value);
		return writeBytes(dest, pointer, data);
	}
	
	public static int writeBytes(byte[] dest, int pointer, double value){
		assert(dest.length > pointer + Type.getSize(Type.DOUBLE));
		long data = Double.doubleToLongBits(value);
		return writeBytes(dest, pointer, data);
	}
	
	public static int writeBytes(byte[] dest, int pointer, boolean value){
		assert(dest.length > pointer + Type.getSize(Type.BOOLEAN));
		dest[pointer++] = (byte) (value ? 1 : 0);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, String string){
		pointer = writeBytes(dest, pointer, (short) string.length()) ;
		return writeBytes(dest, pointer, string.getBytes());
	}


	public static byte readByte(byte[] src, int pointer){
		return src[pointer];
	}

	public static byte[] readBytes(byte[] src, int pointer, int length){
		byte[] data = new byte[length];
		for (int i = 0; i < length; i++) 
			data[i] = src[pointer + i];
		return data;
	}
	
	public static short readShort(byte[] src, int pointer){
		if (src.length < pointer + 2) return -1;
		return ByteBuffer.wrap(src,pointer,2).getShort();
//		return (short) ((src[pointer] << 8) | (src[pointer + 1]));
	}
	
	public static short[] readShorts(byte[] src, int pointer, int count){
		short[] data = new short[count];
		for (int i = 0; i < count; i++) {
			data[i] = readShort(src, pointer);
			pointer += 2; 
		}
		return data;
	}
	
	public static char readChar(byte[] src, int pointer){
		if (src.length < pointer + 2) return ' ';
		return ByteBuffer.wrap(src,pointer,2).getChar();
//		return (char) ((src[pointer] << 8) | (src[pointer + 1]));
	}
	
	public static char[] readChars(byte[] src, int pointer, int count){
		char[] data = new char[count];
		for (int i = 0; i < count; i++) {
			data[i] = readChar(src, pointer);
			pointer += 2; 
		}
		return data;
	}

	public static int readInteger(byte[] src, int pointer){
		if (src.length < pointer + 4) return -1;
		return ByteBuffer.wrap(src, pointer, 4).getInt();
//		return (int) ((src[pointer] << 24) | (src[pointer + 1] << 16) | (src[pointer + 2] << 8) | (src[pointer + 3]));
	}
	
	public static int[] readIntegers(byte[] src, int pointer, int count){
		int[] data = new int[count];
		for (int i = 0; i < count; i++) {
			data[i] = readInteger(src, pointer);
			pointer += 4; 
		}
		return data;
	}

	public static long readLong(byte[] src, int pointer){
		if (src.length < pointer + 8) return -1;
		return ByteBuffer.wrap(src,pointer,8).getLong();
//		return (long) ((src[pointer + 0] << 56) | (src[pointer + 1] << 48) | (src[pointer + 2] << 40) | (src[pointer + 3] << 32) | //
//					   (src[pointer + 4] << 24) | (src[pointer + 5] << 16) | (src[pointer + 6] << 8)  | (src[pointer + 7]));
	}
	
	public static long[] readLongs(byte[] src, int pointer, int count){
		long[] data = new long[count];
		for (int i = 0; i < count; i++) {
			data[i] = readLong(src, pointer);
			pointer += 8; 
		}
		return data;
	}

	public static float readFloat(byte[] src, int pointer){
		if (src.length < pointer + 4) return -1;
		return ByteBuffer.wrap(src,pointer,4).getFloat();
//		return Float.intBitsToFloat(readInt(src, pointer));
	}
	
	public static float[] readFloats(byte[] src, int pointer, int count){
		float[] data = new float[count];
		for (int i = 0; i < count; i++) {
			data[i] = readFloat(src, pointer);
			pointer += 4; 
		}
		return data;
	}

	public static double readDouble(byte[] src, int pointer){
		if (src.length < pointer + 8) return -1;
		return ByteBuffer.wrap(src,pointer,8).getDouble();
//		return Double.longBitsToDouble(readLong(src, pointer));
	}
	
	public static double[] readDoubles(byte[] src, int pointer, int count){
		double[] data = new double[count];
		for (int i = 0; i < count; i++) {
			data[i] = readDouble(src, pointer);
			pointer += 2; 
		}
		return data;
	}
	
	public static boolean readBoolean(byte[] src, int pointer){
		if (src.length < pointer + 1) return false;
		assert(src[pointer] == 0 || src[pointer] == 1);
		return src[pointer] != 0;
	}
	
	public static boolean[] readBooleans(byte[] src, int pointer, int count){
		boolean[] data = new boolean[count];
		for (int i = 0; i < count; i++) {
			data[i] = readBoolean(src, pointer);
			pointer += 2; 
		}
		return data;
	}
	
	public static String readString(byte[] src, int pointer){
		short lenght = readShort(src, pointer);
		pointer += 2;
		return new String(src, pointer, lenght);
	}
	
	public static String readString(byte[] src, int pointer, int length){
		return new String(src, pointer, length);
	}
	
}
