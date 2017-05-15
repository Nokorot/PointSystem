package com.thecherno.raincloud.serialization;

import static com.thecherno.raincloud.serialization.SerializationUtils.readByte;
import static com.thecherno.raincloud.serialization.SerializationUtils.readChars;
import static com.thecherno.raincloud.serialization.SerializationUtils.readInteger;
import static com.thecherno.raincloud.serialization.SerializationUtils.readString;
import static com.thecherno.raincloud.serialization.SerializationUtils.writeBytes;

public class RCString extends RCBase {

		private static final byte CONTAINER_TYPE = ContainerType.STRING; 
	
		private char[] chars;
		
		private RCString(){
			super(1 + 4 + 4);
		}
		
		public RCString(String name, String data){
			this();
			setName(name);
			if(data == null)
				data = "";
			chars = data.toCharArray();
		}
		
		public int getSize(){
			return getBaseSize() + getDataSize();
		}
		
		public int getDataSize(){
			return chars.length * Type.getSize(Type.CHAR);
		}
		
		public int getCount(){
			return chars.length;
		}
		
		public String getData() {
			return new String(chars);
		}
		
		public int getBytes(byte[] dest, int pointer){
			pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
			pointer = writeBytes(dest, pointer, name);
			pointer = writeBytes(dest, pointer, getSize());
			pointer = writeBytes(dest, pointer, getCount());
			pointer = writeBytes(dest, pointer, chars);
			return pointer;
		}

		public static RCString Deserialize(byte[] src, int pointer) {
			assert (readByte(src, pointer) == CONTAINER_TYPE);
			pointer++;

			RCString result = new RCString();

			result.name = readString(src, pointer);
			pointer += result.name.length() + 2;

			int size = readInteger(src, pointer);
			pointer += 4;
			
			// TODO: skip
			
			int count = readInteger(src, pointer);
			pointer += 4;

			result.chars = readChars(src, pointer, count);

			return result;
		}

		protected void getStrukture(StringBuilder builder, int i) {
			super.getStrukture(builder, i);
			builder.append("\"" + getData() + "\"");
		}

//		@Override
//		public String toString() {
//			return "RCString [name=" + name.length() + "\"" + getName() + "\", size="
//					+ size + ", count=" + count + ", chars=" + getData() + "]";
//		}
		
}
