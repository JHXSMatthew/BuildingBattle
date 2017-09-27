package com.github.JHXSMatthew.Controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;

public class SubjectController {

	ArrayList<String> subjects = new ArrayList<String>();
	Random ran = new Random();
	
	public SubjectController(){
		BufferedReader bf = null;
		try {
			 bf = new BufferedReader(new FileReader(Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder()  +"/"+ "subjects.yml"));
			
		} catch (FileNotFoundException e) {
			System.err.print("Error, IO exception Dest = "+ Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder()  + "/" + "subjects.yml");
			e.printStackTrace();
		}
		if(bf == null){
			return;
		}
		String data ;
		try {
			while((data = bf.readLine()) != null){
				subjects.add(data);
				
			}
		} catch (IOException e) {
			System.err.print("Error, IO exception Dest = "+ Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder() + "/"+ "subjects.yml");
			e.printStackTrace();
		}
		
		try {
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String getRanSub(){
		

		return subjects.get(ran.nextInt(subjects.size()));
	}
	
}
