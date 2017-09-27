package com.github.JHXSMatthew.Controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullController {
	private List<Inventory> invList;
	
	public SkullController(){
		invList = new ArrayList<Inventory>();
		loadInv();
	}
	
	public void loadInv(){
		BufferedReader bf = null;
		try {
			 bf = new BufferedReader(new FileReader(Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder()  +"/"+ "skulls.yml"));
			
		} catch (FileNotFoundException e) {
			System.err.print("Error, IO exception Dest = "+ Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder()  + "/" + "skulls.yml");
			e.printStackTrace();
		}
		if(bf == null){
			return;
		}
		String data ;
		try {
			
			while((data = bf.readLine()) != null){
				
				if(invList.size() == 0){
					addInv();
				}
				Inventory temp = invList.get(invList.size() -1);
				if(addSkull(temp,data)){
					Bukkit.getLogger().info("New Skull Page Done.");
				}else{
					addInv();
					temp = invList.get(invList.size() -1);
					addSkull(temp,data);
				}
			}
		} catch (IOException e) {
			System.err.print("Error, IO exception Dest = "+ Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder() + "/"+ "skulls.yml");
			e.printStackTrace();
		}
		
		try {
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	private void addInv(){
		invList.add(Bukkit.createInventory(null, 54,"饰品头颅"));
		
		if(invList.size() != 1){	
			ItemStack next = new ItemStack(Material.SKULL_ITEM);
			next.setDurability((short) 3);
			List<String> lore = new ArrayList<String>();
			SkullMeta meta = (SkullMeta)next.getItemMeta();
			meta.setOwner("MHF_ArrowRight");
			meta.setDisplayName(ChatColor.YELLOW + "下一页");
			lore.add(ChatColor.GRAY + String.valueOf(invList.size() -1));
			meta.setLore(lore);
			next.setItemMeta(meta);
			
			
			ItemStack prev = new ItemStack(Material.SKULL_ITEM);
			prev.setDurability((short) 3);
			List<String> prevLore = new ArrayList<String>();
			SkullMeta prevMeta = (SkullMeta)prev.getItemMeta();
			prevMeta.setOwner("MHF_ArrowLeft");
			prevMeta.setDisplayName(ChatColor.YELLOW + "上一页");
			prevLore.add(ChatColor.GRAY + String.valueOf(invList.size() -2));
			prevMeta.setLore(prevLore);
			prev.setItemMeta(prevMeta);
			
			
			invList.get(invList.size() - 2).setItem(53, next);
			invList.get(invList.size() - 1).setItem(45, prev);
		}
	}
	
	private boolean addSkull(Inventory temp,String data){
		
		int position = - 1;
		for(int i = 0 ; i < temp.getSize() ; i ++){
			if(temp.getItem(i) == null || temp.getItem(i).getType().equals(Material.AIR) ){
				if(i % 9 ==0 || i % 9 == 8 || (i >= 45 && i <= 54)){
					continue;
				}else{
					position = i;
					break;
				}
			}
		}
		if(position == -1){
			return false;
		}else{
			StringTokenizer in = new StringTokenizer(data,"|");
			ItemStack item = new ItemStack(Material.SKULL_ITEM);
			item.setDurability((short) 3);
			SkullMeta meta = (SkullMeta)item.getItemMeta();
			meta.setOwner(in.nextToken());
			meta.setDisplayName(in.nextToken());
			item.setItemMeta(meta);
			temp.setItem(position, item);
			return true;
		}
	}
	
	public Inventory pickInv(int i){
		return invList.get(i);
	}
}
