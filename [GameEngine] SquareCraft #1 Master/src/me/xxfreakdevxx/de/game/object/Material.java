package me.xxfreakdevxx.de.game.object;

public enum Material {
	
	AIR(0, "air", "Luft", false, "", ""),
	STONE(1, "stone", "Stein", true, "/assets/textures/block/stone_2.png", ""),
	DIRT(2, "dirt", "Dreck", true, "/assets/textures/block/dirt_2.png", ""),
	GRASS(3, "grass", "Gras", true, "/assets/textures/block/grass_2.png", "");
	
	int id = 0;
	String name, displayname, filepath, audiofilepath;
	boolean isSolid;
	
	Material(int id, String name, String displayname, boolean isSolid, String filepath, String audiofilepath) {
		this.id=id;
		this.name=name;
		this.displayname=displayname;
		this.isSolid=isSolid;
		this.filepath=filepath;
		this.audiofilepath=audiofilepath;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDisplayname() {
		return displayname;
	}
	
	public boolean isSolid() {
		return isSolid;
	}
	
	public String getFilePath() {
		return filepath;
	}

	public String getSoundFilePath() {
		return audiofilepath;
	}
	
}