package no.nokorot.pointsystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JFrame;

import no.nokorot.pointsystem.Windows.MainMenu;
import util.DefaultWindowsSetings;
import util.Platform;
import util.handelers.FileHandler;
import util.handelers.ImageHandeler;
import util.swing.ButtonSetings;
import util.swing.LabelSetings;
import util.swing.TextFieldSetings;

import com.thecherno.raincloud.serialization.RCDatabase;
import com.thecherno.raincloud.serialization.RCObject;

public class PointSystem extends Platform {

	public static String Version = "2.8.0";
	public static String filedLoc, srcLoc;

	public static RCDatabase database;
	private static String dataLoc;

	public static BufferedImage Logo, icons;

	public PointSystem() {
		if (OSName.toLowerCase().contains("win"))
			filedLoc = System.getProperty("user.home") + "/appdata/Roaming/nokorot/pointsystem";
		else
			filedLoc = System.getProperty("user.home") + "/.nokorot/pointsystem";
		dataLoc = filedLoc + "/data_" + Version;

		Logo = ImageHandeler.load("/logo.jpg");
		icons = ImageHandeler.load("/icons.png");

		PSData.init();
		Load();

		DefaultWindowsSetings.icon = Logo;
		DefaultWindowsSetings.defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE;
		DefaultWindowsSetings.background = Color.DARK_GRAY;

		ButtonSetings.s_border = null;
		ButtonSetings.s_bColor = Color.LIGHT_GRAY;
		ButtonSetings.s_font = new Font("Arial", Font.LAYOUT_RIGHT_TO_LEFT, 20);
		TextFieldSetings.s_border = null;
		TextFieldSetings.s_font = new Font("Arial", Font.LAYOUT_RIGHT_TO_LEFT, 20);
		LabelSetings.s_tColor = Color.WHITE;
		LabelSetings.s_font = LabelSetings.s_font.deriveFont(24f);

		MainMenu.Open();
	}

	public static void close() {
		Save();
		System.exit(0);
	}

	private void Load() {
		System.out.print("Deserializeing - ");

		database = RCDatabase.DeserializeFromFile(dataLoc);

		System.out.println("finised");

		if (database == null)
			loadDefault();

		RCObject info = database.getObject("PSInfo");
		if (info == null || !info.getString("Version").equals(PointSystem.Version))
			loadDefault();

		PSData.load();
	}

	private void loadDefault() {
		database = new RCDatabase("ps_data");
	}

	private static void Save() {
		PSData.save();

		FileHandler.createPath(Paths.get(filedLoc));

		RCObject info = new RCObject("PSInfo");
		info.addString("Version", PointSystem.Version);
		database.addObject(info);

		database.SerializeToFile(dataLoc);
	}

	public static void main(String[] args) {
		new PointSystem().start();
	}

	public static void restartApplication() {
		MainMenu.window = null;
		try {
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator
					+ "java";
			File currentJar;
			currentJar = new File(PointSystem.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI());

			/* is it a jar file? */
			if (!currentJar.getName().endsWith(".jar"))
				return;

			/* Build command: java -jar application.jar */
			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.exit(0);
	}
}
