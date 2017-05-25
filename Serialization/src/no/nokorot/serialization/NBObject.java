package no.nokorot.serialization;

import static com.thecherno.raincloud.serialization.SerializationUtils.readByte;
import static com.thecherno.raincloud.serialization.SerializationUtils.readInteger;
import static com.thecherno.raincloud.serialization.SerializationUtils.readShort;
import static com.thecherno.raincloud.serialization.SerializationUtils.readString;
import static com.thecherno.raincloud.serialization.SerializationUtils.writeBytes;

import java.awt.Dimension;
import java.util.Map;
import java.util.Vector;

import com.thecherno.raincloud.serialization.RCArray;
import com.thecherno.raincloud.serialization.RCObject;
import com.thecherno.raincloud.serialization.RCString;

public abstract class NBObject {

//	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
	
	Map<String, NBField> content;
	
//	private short fieldCount;
//	SortedMap<String, NBField> fields = new TreeMap<String, NBField>();
//	private short stringCount;
//	private SortedMap<String, RCString> strings = new TreeMap<String, RCString>();
//	private short arrayCount;
//	private SortedMap<String, NBArray> arrays = new TreeMap<String, RCArray>();

//	private int dataSize;
	
	public NBObject(String name){
		setName(name);
		
		
		
		int color;
		Vector pos;
		Dimension size;

		NBObject.define(Dimension.class, (Dimension d, NBObject o) -> {
			o.write("width", d.width);
			o.write("height", d.height);
		}, (Dimension d, NBObject o) -> {
			d.width = (int) o.read("width");
			d.height = (int) o.read("height");
		});
		
		write(NBObject o){
			o.write("color", color);
			o.write("pos", pos);
			o.write("pos", size);
		}
			
	}
	
	public void write(String key, Object object){
		
	}
	
	public Object read(String key){
		
	}

	public int getSize() {
		return getBaseSize() + dataSize;
	}

//	public int getDataSize() {
//		return dataSize;
//	}
//
//	public void addField(NBField field) {
//		fields.put(field.getName(), field);
//		dataSize += field.getSize();
//
//		fieldCount = (short) fields.size();
//	}
//
//	public NBField getField(String name) {
//		return fields.get(name);
//	}
//
//	public void addString(RCString string) {
//		strings.put(string.getName(), string);
//		dataSize += string.getSize();
//
//		stringCount = (short) strings.size();
//	}
//
//	public String getString(String name) {
//		if (!strings.containsKey(name))
//			return null;
//		return strings.get(name).getData();
//	}
//
//	public void addArray(RCArray array) {
//		arrays.put(array.getName(), array);
//		dataSize += array.getSize();
//
//		arrayCount = (short) arrays.size();
//	}
//
//	public RCArray getArray(String name) {
//		return arrays.get(name);
//	}
//
//	public void addSubObject(RCObject subObject) {
//		subObjects.put(subObject.getName(), subObject);
//		dataSize += subObject.getSize();
//
//		subObjectCount = (short) subObjects.size();
//	}
//
//	public RCObject getSubObject(String name) {
//		if (!subObjects.containsKey(name))
//			return new RCObject(name);
//		return subObjects.get(name);
//	}

	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);

		pointer = writeBytes(dest, pointer, fieldCount);
		for (NBField field : fields.values()) {
			pointer = field.getBytes(dest, pointer);
		}

		pointer = writeBytes(dest, pointer, stringCount);
		for (RCString string : strings.values()) {
			pointer = string.getBytes(dest, pointer);
		}

		pointer = writeBytes(dest, pointer, arrayCount);
		for (RCArray array : arrays.values()) {
			pointer = array.getBytes(dest, pointer);
		}

		pointer = writeBytes(dest, pointer, subObjectCount);
		for (RCObject subObject : subObjects.values())
			pointer = subObject.getBytes(dest, pointer);

		return pointer;
	}

	public static RCObject Deserialize(byte[] src, int pointer) {
		assert (readByte(src, pointer) == CONTAINER_TYPE);
		pointer++;

		RCObject result = new RCObject();

		result.name = readString(src, pointer);
		pointer += result.name.length() + 2;

		result.size = readInteger(src, pointer);
		pointer += 4;

		short fieldCount = readShort(src, pointer);
		pointer += 2;
		for (int i = 0; i < fieldCount; i++) {
			NBField field = NBField.Deserialize(src, pointer);
			pointer += field.getSize();
			result.addField(field);
		}

		short stringCount = readShort(src, pointer);
		pointer += 2;
		for (int i = 0; i < stringCount; i++) {
			RCString string = RCString.Deserialize(src, pointer);
			pointer += string.getSize();
			result.addString(string);
		}

		short arrayCount = readShort(src, pointer);
		pointer += 2;
		for (int i = 0; i < arrayCount; i++) {
			RCArray array = RCArray.Deserialize(src, pointer);
			pointer += array.getSize();
			result.addArray(array);
		}

		short subObjectCount = readShort(src, pointer);
		pointer += 2;
		for (int i = 0; i < subObjectCount; i++) {
			RCObject subObject = RCObject.Deserialize(src, pointer);
			pointer += subObject.getSize();
			result.addSubObject(subObject);
		}

		return result;
	}

//	public RCBase getData() {
//		return this;
//	}
//
//	public final Collection<NBField> getFields() {
//		return fields.values();
//	}
//
//	public final Collection<RCString> getStrings() {
//		return strings.values();
//	}
//
//	public final Collection<RCArray> getArrays() {
//		return arrays.values();
//	}
//
//	public final Collection<RCObject> getSubObjects() {
//		return subObjects.values();
//	}
//
//	protected void getStrukture(StringBuilder builder, int i) {
//		super.getStrukture(builder, i++);
//
//		for (NBField f : getFields())
//			f.getStrukture(builder, i);
//		for (RCString s : getStrings())
//			s.getStrukture(builder, i);
//		for (RCArray a : getArrays())
//			a.getStrukture(builder, i);
//		for (RCObject o : getSubObjects())
//			o.getStrukture(builder, i);
//	}
//
//	public void addString(String string, String data) {
//		addString(new RCString(string, data));
//	}
//
//	public void addInteger(String key, int value) {
//		addField(NBField.Integer(key, value));
//	}
//
//	public void addFloat(String key, float value) {
//		addField(NBField.Float(key, value));
//	}
//
//	public void addFloat(String key, float value) {
//		addField(NBField.Float(key, value));
//	}
//
//	public void addBoolean(String string, boolean value) {
//		addField(NBField.Booelan(string, value));
//	}
//
//	public void addObject(String name, RCObject save) {
//		save.setName(name);
//		addSubObject(save);
//	}
//
//	public int getInteger(String key) {
//		if (!fields.containsKey(key))
//			return -1;
//		return getField(key).intData();
//	}
//
//	public boolean getBoolean(String key) {
//		if (!fields.containsKey(key))
//			return false;
//		return getField(key).booleanData();
//	}

}
