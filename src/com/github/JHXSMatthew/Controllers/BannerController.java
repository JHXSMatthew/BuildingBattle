package com.github.JHXSMatthew.Controllers;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class BannerController {
	
	Inventory color = Bukkit.createInventory(null, 54,"选择一个颜色");
	Inventory partern = Bukkit.createInventory(null, 54,"选择一个图案");
	
	public BannerController(){
		
	}

}
