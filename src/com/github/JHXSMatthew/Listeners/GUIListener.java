package com.github.JHXSMatthew.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Game.GamePlayer;

public class GUIListener implements Listener{

	ItemStack timeItem;
	ItemStack floorItem;
	ItemStack WeatherItem;
	ItemStack SkullItem;
	
	ItemStack morning;
	ItemStack noon;
	ItemStack afternoon;
	ItemStack night;
	
	
	ItemStack clear;
	ItemStack rain;
	ItemStack snow;
	ItemStack light;
	
	
	public GUIListener(){
		timeItem = new ItemStack(Material.WATCH);
		floorItem = new ItemStack(Material.GRASS);
		WeatherItem = new ItemStack(Material.GLASS);
		SkullItem = new ItemStack(Material.SKULL_ITEM);
		
		
		List<String> lore = new ArrayList<String>();
		
		ItemMeta meta = timeItem.getItemMeta();
		meta = timeItem.getItemMeta();
		lore.add(ChatColor.GRAY + "点击调整时间");
		meta.setDisplayName(ChatColor.GREEN + "调整时间");
		meta.setLore(lore);
		timeItem.setItemMeta(meta);
		lore.clear();
		
		meta = floorItem.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "调整地板");
		lore.add(ChatColor.GRAY + "拖拽物品到这里");
		lore.add(ChatColor.GRAY+ "可以更改地板材质");
		meta.setLore(lore);
		floorItem.setItemMeta(meta);
		lore.clear();
		
		meta = WeatherItem.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "调整天气");
		lore.add(ChatColor.GRAY + "点击调整天气");
		meta.setLore(lore);
		WeatherItem.setItemMeta(meta);
		lore.clear();
		
		meta = SkullItem.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "饰品头颅");
		SkullItem.setDurability((short)3);
		lore.add(ChatColor.GRAY + "点击获取饰品头颅");
		meta.setLore(lore);
		SkullItem.setItemMeta(meta);
		lore.clear();
		
		
		
		//time inv
		morning = new ItemStack(Material.FLOWER_POT_ITEM);
		noon = new ItemStack(Material.DOUBLE_PLANT);
	    afternoon = new ItemStack(Material.REDSTONE);
		night = new ItemStack(Material.COAL_BLOCK);
		
		meta = morning.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "早晨");
		lore.add(ChatColor.GRAY + "确认使用早晨时间");
		meta.setLore(lore);
		morning.setItemMeta(meta);
		lore.clear();
		
		meta = noon.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "正午");
		lore.add(ChatColor.GRAY + "确认使用正午时间");
		meta.setLore(lore);
		noon.setItemMeta(meta);
		lore.clear();
		
		meta = afternoon.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "傍晚");
		lore.add(ChatColor.GRAY + "确认使用傍晚时间");
		meta.setLore(lore);
		afternoon.setItemMeta(meta);
		lore.clear();
		
		meta = night.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "深夜");
		lore.add(ChatColor.GRAY + "确认使用深夜时间");
		meta.setLore(lore);
		night.setItemMeta(meta);
		lore.clear();
		
		//weather inv
		clear = new ItemStack(Material.FLOWER_POT_ITEM);
		rain = new ItemStack(Material.WATER_BUCKET);
		snow = new ItemStack(Material.SNOW);
		light = new ItemStack(Material.ANVIL);
		
		meta = clear.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "晴天");
		lore.add(ChatColor.GRAY + "确认改变晴天");
		meta.setLore(lore);
		clear.setItemMeta(meta);
		lore.clear();
		
		meta = rain.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "下雨");
		lore.add(ChatColor.GRAY + "确认改变下雨");
		meta.setLore(lore);
		rain.setItemMeta(meta);
		lore.clear();
		
		
		meta = snow.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "下雪");
		lore.add(ChatColor.GRAY + "确认改变下雪");
		meta.setLore(lore);
		snow.setItemMeta(meta);
		lore.clear();
		
		meta = light.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "电闪雷鸣");
		lore.add(ChatColor.GRAY + "确认改变电闪雷鸣");
		meta.setLore(lore);
		light.setItemMeta(meta);
		lore.clear();
		
		
	}
	
	
	@EventHandler
	public void onOpen(PlayerInteractEvent evt){
		if(evt.getAction()==Action.RIGHT_CLICK_AIR 
				|| evt.getAction() == Action.LEFT_CLICK_AIR 
				|| evt.getAction() == Action.LEFT_CLICK_BLOCK 
				|| evt.getAction() == Action.RIGHT_CLICK_BLOCK){
		
			ItemStack item = evt.getPlayer().getItemInHand();
			if(item != null && item.getType().equals(Material.WATCH) && item.hasItemMeta()){
				if(BuildingBattle.getIc().isGUIItem(item)){
					openOptionGui(evt.getPlayer(),0);
				}
			}
			
			
		}
		
	}
	

	
	@EventHandler
	public void onClick(InventoryClickEvent evt){
		if(evt.isCancelled()){
			return;
		}
		
		
		try
		{
		ItemStack item = evt.getCurrentItem();	
		
		if(item == null){
			return;
		}
		
		if(BuildingBattle.getIc().isVoteItem(item) != -1){
			evt.setCancelled(true);
			return;
		}
			
		String title =evt.getClickedInventory().getTitle();
			if(title == null 
					|| item == null
					|| item.getType().equals(Material.AIR)){
				return;
			}
			if(! item.hasItemMeta() || !item.getItemMeta().hasDisplayName()){
				return;
			}
			
			String clickedItem =item.getItemMeta().getDisplayName();
			

			
			
			//make sure cannot throw the stuff
			if(clickedItem.contains("选项菜单")){
				evt.setCancelled(true);
				evt.setCursor(new ItemStack (Material.AIR));
				return;
			}
			
			GamePlayer p = BuildingBattle.getPc().getPlayer((Player) evt.getWhoClicked());
			if(p == null && evt.getWhoClicked() instanceof Player && evt.getWhoClicked() != null){
				BuildingBattle.getPc().newPlayer((Player) evt.getWhoClicked());
			}
	
			
			
			evt.setCancelled(true);
			
			if(title.contains("选项菜单")){
				if(clickedItem.contains("时间")){
					p.get().closeInventory();
					openOptionGui(p.get(),1);
					
				}else if(clickedItem.contains("天气")){
					p.get().closeInventory();
					openOptionGui(p.get(),2);
				}else if(clickedItem.contains("地板")){
					ItemStack clicked = evt.getCursor();
					if(clicked != null){
						p.getPlot().setFloorBlock(clicked.getType(),(byte) clicked.getDurability());
						p.get().closeInventory();
					}
				}else if(clickedItem.contains("头颅")){
					p.get().closeInventory();
					openOptionGui(p.get(),3);
				}
				
			}else if(title.contains("时间")){
				if(clickedItem.contains("早晨")){
					p.getPlot().setTime(0);
				}else if(clickedItem.contains("正午")){
					p.getPlot().setTime(6000);
				}else if(clickedItem.contains("傍晚")){
					p.getPlot().setTime(12700);
				}else if(clickedItem.contains("深夜")){
					p.getPlot().setTime(15000);
				}
				p.get().closeInventory();
			}else if(title.contains("天气")){
				
				if(clickedItem.contains("晴天")){
					p.getPlot().setWeather(0);
				}else if(clickedItem.contains("下雨")){
					p.getPlot().setWeather(1);
				}else if(clickedItem.contains("下雪")){
					p.getPlot().setWeather(2);
				}else if(clickedItem.contains("电闪雷鸣")){
					p.getPlot().setWeather(3);
				}
				p.get().closeInventory();
			}else if(title.contains("饰品头颅")){
				if(clickedItem.contains("一页")){
					String s = item.getItemMeta().getLore().get(0);
					
					Inventory i = BuildingBattle.getSkuc().pickInv(Integer.parseInt(s.substring(2)));
					p.get().closeInventory();
					p.get().openInventory(i);
				}else{
					if(p.get().getInventory().firstEmpty() != -1){
						p.get().getInventory().addItem(item.clone());
						p.get().playSound(p.get().getLocation(), Sound.ITEM_PICKUP, 1, 1);
					}else{
						p.get().closeInventory();
						p.get().sendMessage(ChatColor.YELLOW + "YourCraft >> 您的背包已满.");
					}
				}
			}else{
				evt.setCancelled(false);
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	
	private void openOptionGui(Player p,int code){
		GamePlayer gp = BuildingBattle.getPc().getPlayer(p);
		if(p == null){
			System.out.print("PLAYER GUI ERROR, NULL Player");
			BuildingBattle.getPc().newPlayer(p);
			return;
		}
		switch(code){
		case 0 : 
			Inventory inv = Bukkit.createInventory(null, 36,"选项菜单");
			ItemStack timeItem = this.timeItem.clone();
			ItemStack floorItem = this.floorItem.clone();
			ItemStack WeatherItem = this.WeatherItem.clone();
			ItemStack SkullItem = this.SkullItem.clone();
			SkullMeta sm = (SkullMeta) SkullItem.getItemMeta();
			sm.setOwner(p.getName());
		
			SkullItem.setItemMeta(sm);
			
			if(gp.getPlot().getFloorBlock() != null){
				floorItem.setType((gp.getPlot().getFloorBlock()));
				floorItem.setDurability(gp.getPlot().getFloorBlockData());
			}
			inv.setItem(10, floorItem);
			inv.setItem(12, timeItem);
			inv.setItem(14, WeatherItem);
			inv.setItem(16, SkullItem);
			p.openInventory(inv);
			
			inv = null;
			break;
		case 1 :
			Inventory timeInv = Bukkit.createInventory(null, 36,"设置时间");
			ItemStack morning = this.morning.clone();
			ItemStack noon = this.noon.clone();
			ItemStack afternoon = this.afternoon.clone();
			ItemStack night = this.night.clone();
			
			switch((int)gp.getPlot().getTime()){
				case 12700 :
					afternoon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					break;
				case 15000 :
					night.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					break;
				case 6000:
					noon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					break;
				case 0:
					morning.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					break;
			}
			
			timeInv.setItem(10, morning);
			timeInv.setItem(12, noon);
			timeInv.setItem(14, afternoon);
			timeInv.setItem(16, night);
			p.openInventory(timeInv);
			
			timeInv = null;
			break;
		case 2:
			Inventory weatherInv = Bukkit.createInventory(null, 36,"设置天气");
			ItemStack clear = this.clear.clone();
			ItemStack rain = this.rain.clone();
			ItemStack snow = this.snow.clone();
			ItemStack light = this.light.clone();
			
			switch(gp.getPlot().getWeather()){
				case 0:
					clear.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					break;
				case 1:
					rain.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					break;
				case 2:
					snow.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					break;
				case 3:
					light.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
			}
			
			weatherInv.setItem(10, clear);
			weatherInv.setItem(12, rain);
			weatherInv.setItem(14, snow);
			weatherInv.setItem(16, light);
			
			
			p.openInventory(weatherInv);
			weatherInv = null;
			break;
		case 3:
			Inventory skullBase = BuildingBattle.getSkuc().pickInv(0);
			p.openInventory(skullBase);
			break;
		}
		
			
		
	}
	
}
