package util.handelers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

public class FilesHandeler {
	public static Scanner scan;
	private static List<Scanner> scans = new ArrayList<Scanner>();
	public static Formatter x;

	public FilesHandeler() {
	}

	public static Formatter createFile(File file) {
		try {
			return FilesHandeler.x = new Formatter(file);
		} catch (Exception e) {
			System.out.println("can't create the file");
		}
		return null;
	}

	public static void closeFile() {
		x.close();
	}

	public static int loadFile(File file) throws Exception {
		Scanner scan = new Scanner(file);
		scans.add(scan);

		return scans.size() - 1;
	}

	public static Scanner scan(int i) {
		return (Scanner) scans.get(i);
	}

	public static String readFile(String path) {
		if(path == null)
			return null;
		return readFile(new File(path));
	}
	
	@SuppressWarnings("resource")
	public static String readFile(File file) {
		if(!file.exists())
			return null;
		
		Scanner scan;
		try {
			scan = new Scanner(file);
		
			StringBuffer builder = new StringBuffer();
			while (scan.hasNextLine()) 
				builder.append(scan.nextLine() + "\n");
			return builder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}