package util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("resource")
public class Log {

	private Map<String, String> lines = new HashMap<String, String>();

	private File path;

	public Log(String path) {
		this.path = new File(path);
	}
	
	public Log(File path) {
		this.path = path;
	}

	public void load() throws FileNotFoundException {
		read(path);
	}

	public void save() throws FileNotFoundException {
		Formatter x = new Formatter(path);
		for (String kay : lines.keySet()) {
			String object = lines.get(kay);
			x.format("%s: %s\n", kay, object);
		}
		x.close();
	}

	public void addLogable(String kay, Logable logable) {
		lines.put(kay, logable.toLogString());
	}

	public Logable getLogable(String kay, Logable output) {
		output.fromLogString(lines.get(kay));
		return output;
	}

	public void addString(String kay, String object) {
		lines.put(kay, object);
	}

	public String getString(String kay) {
		return lines.get(kay);
	}

	public void addInt(String kay, int i) {
		lines.put(kay, Integer.toString(i));
	}

	public int getInt(String kay) throws Exception {
		return Integer.parseInt(lines.get(kay));
	}

	public void addFloat(String kay, float f) {
		lines.put(kay, Float.toString(f));
	}

	public float getFlaot(String kay) {
		return Float.parseFloat(lines.get(kay));
	}

	public void addBoolean(String kay, boolean b) {
		lines.put(kay, Boolean.toString(b));
	}

	public boolean getBoolean(String kay) {
		return Boolean.parseBoolean(lines.get(kay));
	}

	public void addDouble(String kay, double d) {
		lines.put(kay, Double.toString(d));
	}

	public double getDouble(String kay) {
		return Double.parseDouble(lines.get(kay));
	}

	public void read(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		
		System.out.println(file.getPath());

		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split(": ");
			if (line.length < 2)
				lines.put(line[0], "");
			else
				lines.put(line[0], line[1]);
		}
	}
	
	public void read(String s) throws FileNotFoundException {
		Scanner scanner = new Scanner(s);
		
		
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split(": ");
			if (line.length < 2)
				lines.put(line[0], "");
			else
				lines.put(line[0], line[1]);
		}
	}

	public void clear() {
		lines.clear();
	}
}