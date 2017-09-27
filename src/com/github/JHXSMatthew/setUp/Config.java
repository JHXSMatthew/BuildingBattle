package com.github.JHXSMatthew.setUp;

import org.bukkit.Location;

import com.github.JHXSMatthew.Tools.FileSystem;

public class Config {
	
	public Location lobby = null;
	public boolean setup = false;

	
	public void save(){
		FileSystem save = new FileSystem("config", "BuildingBattle");
	
		
		save.addLocation("qlobby", lobby);
		save.save();
	}
	
	public void load(){
		FileSystem save = new FileSystem("config", "BuildingBattle");
		setup = save.getBoolean("setUp");
		if(setup){
			lobby = save.getLocation("qlobby", true);
		}
		save.save();
	}
	
}
