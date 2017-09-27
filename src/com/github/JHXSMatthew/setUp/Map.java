package com.github.JHXSMatthew.setUp;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Tools.FileSystem;

public class Map {

	public String name;
	public Location lobby;
	public Location up;
	public Location down;
	public int Player;
	
//time is in minutes
	public int time;
	
	public Map(String arg){
		this.name = arg;
	}

	
	public void save(){
		if(up.getX() < down.getX() ){
			Double temp = up.getX();
			up.setX(down.getX());
			down.setX(temp);
		}
		if(up.getZ() < down.getZ() ){
			Double temp = up.getZ();
			up.setZ(down.getZ());
			down.setZ(temp);
		}
		FileSystem save = new FileSystem(name, "BuildingBattle", "arena");
		save.addLocation("lobby", lobby);
		save.addLocation("up", up);
		save.addLocation("down", down);
		save.addInt("player", this.Player);
		save.addInt("time", this.time);
		save.save();
		File source = new File(lobby.getWorld().getName());
		File target = new File(Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder() + "/" + "maps"+ "/" , name);
		BuildingBattle.getWc().copyWorld(source, target);
		
	}
	
	public void load(){
		FileSystem save = new FileSystem(name, "BuildingBattle", "arena");
		this.lobby = save.getLocation("lobby", true);
		this.up = save.getLocation("up", true);
		this.down = save.getLocation("down", true);
		this.Player = save.getInt("player");
		this.time = save.getInt("time");
		save.save();
	}
	
	public void setWorld(World world){
		lobby.setWorld(world);
		up.setWorld(world);
		down.setWorld(world);
	}
}

