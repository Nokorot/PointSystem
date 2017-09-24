package com.thecherno.raincloud.serialization;

import static com.thecherno.raincloud.serialization.SerializationUtils.readByte;
import static com.thecherno.raincloud.serialization.SerializationUtils.readInteger;
import static com.thecherno.raincloud.serialization.SerializationUtils.readString;
import static com.thecherno.raincloud.serialization.SerializationUtils.writeBytes;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RCPointerTable {
	
	public static final byte[] FUTTER = "RCPTB".getBytes();
	
	public int pointer;

	public List<Pointer> pointers = new ArrayList<Pointer>();
	public Map<String, RCPointerTable> tabels = new HashMap<String, RCPointerTable>();

	public void put(String name, int pointer) {
		pointers.add(new Pointer(name, pointer));
	}
	
	public void put(String name, RCPointerTable tabel) {
		tabels.put(name, tabel);
	}
	 
	public int getByteSize() {
		int result = 0;
		for (Pointer p : pointers)
			result += 1 + 2 + p.key.length() + 4;
		for (String key : tabels.keySet())
			result += 1 + 2 + key.length() + 4 + tabels.get(key).getByteSize();
		return result;
	}	

	public int getBytes(byte[] dest, int pointer) {
		for (Pointer p : pointers) {
			pointer = writeBytes(dest, pointer, (byte) 0);
			pointer = writeBytes(dest, pointer, p.key);
			pointer = writeBytes(dest, pointer, p.value);
		}
		for (String key : tabels.keySet()) {
			pointer = writeBytes(dest, pointer, (byte) 1);
			pointer = writeBytes(dest, pointer, key);
			pointer = writeBytes(dest, pointer, tabels.get(key).pointer);
			pointer = tabels.get(key).getBytes(dest, pointer);
		}
		return pointer;
	}
	
	public static RCPointerTable Deserialize(byte[] data, int pointer) {
		RCPointerTable result = new RCPointerTable();
		byte type = readByte(data, pointer);
		pointer++;
		while (type == 0 || type == 1) {
			String key = readString(data, pointer);
			pointer += 2 + key.length();
			if (type == 0){
				result.put(key, readInteger(data, pointer));
				pointer += 4;
			} else {
				pointer += 4;
				RCPointerTable tabel = RCPointerTable.Deserialize(data, pointer);
				tabel.pointer = readInteger(data, pointer - 4);
				result.put(key, tabel);
				pointer += tabel.getByteSize();
			}
			type = readByte(data, pointer);
			pointer++;
		}
		return result;
	}
	
	public static RCPointerTable DeserializeFromFile(String path) {
		byte[] buffer = null;
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
			buffer = new byte[stream.available()];
			stream.read(buffer);
			stream.close();
			
//			PrintWriter writer = new PrintWriter(path);
//			for (int i = 0; i < data.length; i++){
//				writer.printf("%x\t", data[i]);
//				if((i + 1) % 10 == 0)
//					writer.println();
//			}
//			writer.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Deserialize(buffer, 0);
	}

	public void shift(int amount) {
		pointer += amount;
		for (Pointer p : pointers)
			p.shift(amount);
		for (String key : tabels.keySet())
			tabels.get(key).shift(amount);
	}
	private String toString(int tabs){
		StringBuilder builder = new StringBuilder();

		for (Pointer p : pointers) {
			for (int i = 0; i < tabs; i++) 
				builder.append('\t');
			builder.append(p.key + ": " + p.value);
			builder.append("\n");
		}
		
		for (String key : tabels.keySet()) {
			for (int i = 0; i < tabs; i++) 
				builder.append('\t');
			builder.append(key + ": " + tabels.get(key).pointer + "\n" + tabels.get(key).toString(tabs + 1));
			builder.append("\n");
		}

		return builder.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (Pointer p : pointers) {
			builder.append(p.key + ": " + p.value);
			builder.append("\n");
		}
		
		for (String key : tabels.keySet()) {
			builder.append(key + ": " + tabels.get(key).pointer + "\n" + tabels.get(key).toString(1));
			builder.append("\n");
		}

		return builder.toString();
	}
	
	public static class Pointer {
		String key;
		int value;
		
		public Pointer(String key, int value) {
			this.key = key;
			this.value = value;
		}
		
		void shift(int amount){
			value += amount;
		}
	}
	
}
