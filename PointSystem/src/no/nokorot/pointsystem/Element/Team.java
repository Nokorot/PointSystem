package no.nokorot.pointsystem.Element;

import no.nokorot.pointsystem.utils.FontObject;

import com.thecherno.raincloud.serialization.RCObject;

public class Team {

	public static final int MAX_TEAMS = 9;

	public static final Team[] TEAMS = new Team[MAX_TEAMS];
	
	static{
		for (int i = 0; i < TEAMS.length; i++)
			TEAMS[i] = new Team("Team " + (i + 1));
	}
	
	public NamedTeam label;
	public NamedTeamMenu menu;
	
	public String name;
	public int points;

	public FontObject fontName = new FontObject("Name");
	public FontObject fontPoints = new FontObject("Points");

	private Team(String name) {
		this.name = name;
	}

	public void set(String name, int points) {
		this.name = name;
		this.points = points;
	}

	public String stringedP() {
		return (points + "").length() > 1 ? points + "" : "0" + points;
	}

	public void setPoints(String text) {
		try {
			setPoints(Integer.parseInt(text));
		} catch (Exception e) {
			System.err.println("can't pass \"" + text + "\" too an Integer!!");
		}
	}

	public void setName(String name) {
		this.name = name;
		label.setName(name);
	}

	public void setPoints(int i){
		points = i;
		if (label != null)
			label.setPoints(stringedP());
	}
	
	public void addPoints(int i) {
		points += i;
		if (label != null)
			label.setPoints(stringedP());
	}

	public void load(RCObject parent, String key) {
		RCObject in = parent.getSubObject(key);
		name = in.getString("name");
		fontName.load(in, "nameFont");
		fontPoints.load(in, "pointsFont");
	}

	public void save(RCObject parent, String key) {
		RCObject out = new RCObject(key);
		out.addString("name", name);
		out.addSubObject(fontName.save("nameFont"));
		out.addSubObject(fontPoints.save("pointsFont"));
		parent.addSubObject(out);
	}
	
	public static void Load(RCObject parent, String key){
		RCObject in = parent.getSubObject(key);
		if(in == null)
			return;

//		fontName.load(in, "Names Font");
//		fontPoints.load(in, "Points Font");
		
		for (int i = 0; i < TEAMS.length; i++)
			TEAMS[i].load(in, "Team " + i);
		
	}
	
	public static void Save(RCObject parent, String key){
		RCObject out = new RCObject(key);

//		out.addSubObject(fontName.save("Names Font"));
//		out.addSubObject(fontPoints.save("Points Font"));
		
		for (int i = 0; i < TEAMS.length; i++)
			TEAMS[i].save(out, "Team " + i);
		parent.addSubObject(out);
	}

}
