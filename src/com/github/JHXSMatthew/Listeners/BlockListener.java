package com.github.JHXSMatthew.Listeners;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Game.Game;
import com.github.JHXSMatthew.Game.GamePlayer;

public class BlockListener implements Listener {

	
	@EventHandler
	public void onBreak(BlockBreakEvent evt){
		if(evt.getPlayer().isOp() && BuildingBattle.get().editMode == 1){
			return;
		}
		
		GamePlayer p = BuildingBattle.getPc().getPlayer(evt.getPlayer());
		Block b = evt.getBlock();
		if(p == null){
			evt.getPlayer().kickPlayer("error, null player got");
		}
		if(!p.isBreakable(b)){
			evt.setCancelled(true);
			return ;
		}
		
		
		p.removeBlock(b);
		
	}
	
	@EventHandler
	public void onLavaWaterBlock(BlockFromToEvent evt){
		if(evt.getToBlock().getType().equals(Material.COBBLESTONE) || evt.getToBlock().getType().equals(Material.OBSIDIAN)){
			GamePlayer p  = BuildingBattle.getPc().getPlayerByLocation(evt.getBlock().getLocation());
			p.addBlock(evt.getBlock());
		}
	}
	
	
	@EventHandler
	public void onTreeGrow(StructureGrowEvent evt){
		Player p = evt.getPlayer();
		if(p == null){
			return;
		}
		GamePlayer gp = BuildingBattle.getPc().getPlayer(p);
		
		gp.addBlock(evt.getBlocks());
	}
	
	@EventHandler
	public void preventSandFall(BlockPhysicsEvent evt){
		if(evt.isCancelled()){
			return;
		}
		Material m = evt.getBlock().getType();
		if(m.equals(Material.SAND) ||
			m.equals(Material.GRAVEL)||
			m.equals(Material.ANVIL) ||
			m.equals(Material.ARMOR_STAND)){
			evt.setCancelled(true);
		}

	        
	    
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent evt){
		if(evt.getPlayer().isOp() && BuildingBattle.get().editMode == 1){
			return;
		}
		
		GamePlayer p = BuildingBattle.getPc().getPlayer(evt.getPlayer());
		Block b = evt.getBlock();
		if(p == null){
			evt.getPlayer().kickPlayer("error, null player got");
		}
		
	
		
		Game g = p.getGame();
		if(g == null){
			evt.setCancelled(true);
			return;
		}
		if(g.getState() != 2){
			evt.setCancelled(true);
			return;
		}
		
		if(p.getPlot().isOutOfBound(b.getLocation())){
			evt.setCancelled(true);
			return;
		}
		
		p.addBlock(b);
		if(b.getType().equals(Material.IRON_DOOR_BLOCK) ){
			Location up = b.getLocation().clone();
			up.setY(b.getY() + 1);
			Block upb =b.getWorld().getBlockAt(up); 
			p.addBlock(upb);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBurn(BlockBurnEvent evt) {
		evt.setCancelled(true);
	}
	@EventHandler
	public void onSpread(BlockSpreadEvent evt) {
		evt.setCancelled(true);
	}
	
	@EventHandler
	public void onFade(BlockFadeEvent evt) {
	   evt.setCancelled(true);
	}
	@EventHandler
	public void onIgnite(BlockIgniteEvent evt) {
		evt.setCancelled(true);
	}
}
