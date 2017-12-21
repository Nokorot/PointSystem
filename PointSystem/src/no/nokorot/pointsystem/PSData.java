package no.nokorot.pointsystem;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import no.nokorot.pointsystem.Element.Team;
import no.nokorot.pointsystem.Windows.FontEditor2;
import util.handelers.ImageHandeler;
import util.handelers.ImageHandeler.ScaleType;

import com.thecherno.raincloud.serialization.RCObject;

public class PSData {

	public static final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();;

	public static final int[] colors = { -14514142, -14336, -8355712, -16777216, -1, -4144960, -16776961,
			-12976070, -16711936, -256, -14754596, -65536 };

	public static enum Material {
		IMAGE, COLOR;
	}
	
	public static boolean SlideShowEnaled = false;
	public static BufferedImage slidePage;
	public static ScaleType slideScale = ScaleType.TILLPASS;
	public static int selectedPage = 0;
	

	public static BufferedImage backgroundImage = PointSystem.Logo, logo = PointSystem.Logo;
	public static ScaleType backgroundScaleType = ScaleType.TILLPASS;
	public static ScaleType logoScaleType = ScaleType.TILLPASS;

	public static String backgroundLocation, logoLocation;

	public static Rectangle custumScreenBounds = new Rectangle(800, 600);
	public static int screen = 1;

	public static void init() {
	}

	public static void load() {
		RCObject in = PointSystem.database.getObject("PSData");
		if (in == null)
			return;

		Team.Load(in, "Teams");

		screen = in.getInteger("screen");
		custumScreenBounds = loadRectangle(in, "custumScreenBounds");

		backgroundLocation = in.getString("backgroundImageLocation");
		backgroundScaleType = ScaleType.parse(in.getString("backgroundScaleType"));
		backgroundImage = ImageHandeler.loadGloab(backgroundLocation);
		if (backgroundImage == null)
			backgroundImage = PointSystem.Logo;

		logoLocation = in.getString("logoLocation");
		logoScaleType = ScaleType.parse(in.getString("logoScaleType"));
		logo = ImageHandeler.loadGloab(logoLocation);
		if (logo == null)
			logo = PointSystem.Logo;

	}

	public static void save() {
		RCObject out = new RCObject("PSData");
		Team.Save(out, "Teams");
		out.addString("backgroundImageLocation", backgroundLocation);
		out.addString("logoLocation", logoLocation);
		out.addString("backgroundScaleType", backgroundScaleType.name());
		out.addString("logoScaleType", logoScaleType.name());
		out.addInteger("screen", screen);
		saveRectangle(out, "custumScreenBounds", custumScreenBounds);
		PointSystem.database.addObject(out);

		FontEditor2.save();
	}

	private static Rectangle loadRectangle(RCObject parent, String key) {
		RCObject in = parent.getSubObject(key);
		return new Rectangle(in.getInteger("x"), in.getInteger("y"), in.getInteger("width"),
				in.getInteger("height"));
	}

	private static void saveRectangle(RCObject parent, String key, Rectangle object) {
		RCObject out = new RCObject(key);
		out.addInteger("x", object.x);
		out.addInteger("y", object.y);
		out.addInteger("width", object.width);
		out.addInteger("height", object.height);
		parent.addObject(key, out);
	}

	public static Rectangle getLiveWindowBounds() {
		GraphicsDevice[] gd = environment.getScreenDevices();
		if(screen >= 0 && screen < gd.length)
			return gd[screen].getDefaultConfiguration().getBounds();
		return custumScreenBounds;
	}

	public static void setCustumX(String text) {
		custumScreenBounds.x = Integer.parseInt(text);
	}

	public static void setCustumY(String text) {
		custumScreenBounds.y = Integer.parseInt(text);
	}

	public static void setCustumWidth(String text) {
		custumScreenBounds.width = Math.max(Integer.parseInt(text), 10);
	}

	public static void setCustumHeight(String text) {
		custumScreenBounds.height = Math.max(Integer.parseInt(text), 10);
	}

}
