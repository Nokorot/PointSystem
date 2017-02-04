package com.thecherno.raincloud.serialization;


public abstract class RCBase {
	
	protected String name;
	protected int size;
	
	public RCBase(int size) {
		this.size = size;
	}
	
	public void setName(String name) {
		assert (name.length() < Short.MAX_VALUE);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public int getBaseSize() {
		return size + name.length() + 2;
	}
	
	public abstract int getSize();
	
	
	public abstract Object getData();
	
	public abstract int getBytes(byte[] dest, int pointer);
	
	public String getStrukture(){
		return getStrukture(0);
	}
	
	public String getStrukture(int i){
		StringBuilder builder = new StringBuilder();
		getStrukture(builder, i);
		return builder.toString();
	}

	protected void getStrukture(StringBuilder builder, int i) {
		builder.append("\n");
		for (int j = 0; j < i; j++) 
			builder.append("   ");
		builder.append(name + ": ");
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [name=\"" + getName() + "\", size=" + getSize()
				+ "]" + getStrukture(1) + "\n";
	}
	
}
