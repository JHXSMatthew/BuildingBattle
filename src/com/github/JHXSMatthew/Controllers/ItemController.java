package com.github.JHXSMatthew.Controllers;

import java.util.ArrayList;





import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Game.GamePlayer;

public class ItemController implements Listener {
	
	private ArrayList<ItemStack> itemList;
	private ArrayList<Material> banList;
	private ItemStack optionItem;
	private ItemStack quitItem;

	public ItemController(){
		itemList = new ArrayList<ItemStack>();
		banList = new ArrayList<Material>();
		try{
			register();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	
	public ItemStack getMarkItem(short mark){
		return itemList.get(mark);
	}

	
	public void giveVoteItems(GamePlayer p){
		for(int i =0; i < itemList.size() ; i ++){
			p.get().getInventory().addItem(itemList.get(i));
		}
		
	}
	public int isVoteItem(ItemStack item){
		return itemList.indexOf(item);
	}
	
	public boolean isBan(Material m){
		return banList.contains(m);
	}
	public boolean isGUIItem(ItemStack item){
		return optionItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName());
	}
	public boolean isQuitItem(ItemStack item){
	//	System.out.print(quitItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()));
		return quitItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName());
	}
	
	public void giveLobbyItem(GamePlayer p){
		p.get().getInventory().setItem(8, quitItem);
	}
	
	public void giveGameBeginItem(GamePlayer p ){
		p.get().getInventory().setItem(8, optionItem);
	}
	
	private void register(){
		ItemStack item = new ItemStack(Material.STAINED_CLAY);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GRAY + BuildingBattle.getMsg().phaseVote(1));
		item.setItemMeta(meta);
		item.setDurability((short) 7);
		itemList.add(item.clone());
		
		
		meta.setDisplayName(ChatColor.WHITE + BuildingBattle.getMsg().phaseVote(2));
		item.setItemMeta(meta);
		item.setDurability((short) 8);
		itemList.add(item.clone());
		
		meta.setDisplayName(ChatColor.GREEN +  BuildingBattle.getMsg().phaseVote(3));
		item.setItemMeta(meta);
		item.setDurability((short) 5);
		itemList.add(item.clone());
		
		meta.setDisplayName(ChatColor.AQUA +  BuildingBattle.getMsg().phaseVote(4));
		item.setItemMeta(meta);
		item.setDurability((short) 2);
		itemList.add(item.clone());
		
		meta.setDisplayName(ChatColor.DARK_PURPLE +  BuildingBattle.getMsg().phaseVote(5));
		item.setItemMeta(meta);
		item.setDurability((short) 11);
		itemList.add(item.clone());
		
		meta.setDisplayName(ChatColor.GOLD +  BuildingBattle.getMsg().phaseVote(6));
		item.setItemMeta(meta);
		item.setDurability((short) 4);
		itemList.add(item);
		
		
		// ban item
		
		//banList.add(Material.REDSTONE);
		banList.add(Material.REDSTONE_TORCH_ON);
		banList.add(Material.REDSTONE_TORCH_OFF);
		banList.add(Material.REDSTONE_BLOCK);
		banList.add(Material.ENDER_PEARL);
		banList.add(Material.ENDER_PORTAL_FRAME);
		banList.add(Material.REDSTONE_COMPARATOR);
		banList.add(Material.REDSTONE_ORE);
		banList.add(Material.DIODE);
		banList.add(Material.MAP);
		//option item
		optionItem = new ItemStack(Material.WATCH);
		meta = optionItem.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "选项菜单");
		optionItem.setItemMeta(meta);
		
		//leave Item
		quitItem = new ItemStack(Material.BARRIER);
		meta = optionItem.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "离开");
		quitItem.setItemMeta(meta);
		
		
	}
	
	
	
	
	
}
