package no.nokorot.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Main {
	
	private int i = 0;
	public int j = 0;
	public int y = 0;

	public Main() {
		int i = 0;
		System.out.println(this.getClass().getFields().length);
		for (Method f : this.getClass().getMethods())
			System.out.println(f);
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}
