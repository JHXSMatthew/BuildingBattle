package com.github.JHXSMatthew.Controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.setUp.Map;

public class MapController {

	ArrayList<Map> map;
	Random ran;

	
	public MapController(){
		map = new ArrayList<Map>();
		ran = new Random();
		
		File dir = new File(Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder() + "/" + "arena", "");
		String files[] = dir.list();
		for(String s : files){
			map.add(new Map(s.substring(0,s.lastIndexOf("."))));
			map.get(map.size() - 1).load();
			BuildingBattle.get().getLogger().info("[buildingBattle] Arena " + s.substring(0,s.lastIndexOf(".")) + " loaded!");
		}
	}
	
	
	
	public Map getranMap(int num){
		Map temp = map.get(ran.nextInt(map.size()));
		
		File source = new File(Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder() + "/" + "maps"+ "/" , temp.name);
		File target = new File(temp.name+ num);
		BuildingBattle.getWc().copyWorld(source,target);
		BuildingBattle.getWc().loadWorld(temp.name+ num);
		temp.setWorld(Bukkit.getWorld(temp.name+ num));
		
		return temp;
	}
	

}
